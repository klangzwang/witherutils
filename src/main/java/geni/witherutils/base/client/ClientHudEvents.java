package geni.witherutils.base.client;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.init.WUTEffects;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.base.common.item.withersteel.shield.ShieldPoweredItem;
import geni.witherutils.base.common.item.withersteel.sword.SwordSteelItem;
import geni.witherutils.base.common.item.withersteel.wand.WandSteelItem;
import geni.witherutils.core.common.helper.HudHelper;
import geni.witherutils.core.common.helper.ScreenPositionHelper;
import geni.witherutils.core.common.util.EnergyUtil;
import geni.witherutils.core.common.util.McTimerUtil;
import geni.witherutils.core.common.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class ClientHudEvents {
	
    private static final ResourceLocation ICONS = new ResourceLocation("witherutils", "textures/gui/icons.png");
    private static final ResourceLocation HUD_TEXTURE = new ResourceLocation(WitherUtils.MODID, "textures/gui/hud.png");
    
    @SubscribeEvent
    public void onRegisterGuiOverlays(RegisterGuiOverlaysEvent event)
    {
        event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "wuhud", HUDOVERLAY);
    }
    
	private static final IGuiOverlay HUDOVERLAY = (gui, guiGraphics, partialTick, width, height) -> {
		
		renderBlindHud(guiGraphics);
		renderSwordHud(guiGraphics);
		renderWandHud(guiGraphics);
		renderShieldHud(guiGraphics);
	};

    private static boolean isVisible(Minecraft mc)
    {
        return !(mc.screen instanceof ChatScreen)
                && !mc.options.hideGui
                && !mc.options.renderDebug;
    }
    
    private static void renderBlindHud(GuiGraphics guiGraphics)
    {
		var mc = Minecraft.getInstance();
		if(mc.player != null && isVisible(mc))
	    {
			var pos = HudHelper.getHudPos();
  			if(pos != null)
  			{
  		        Player player = mc.player;
  		        MobEffectInstance effect = player.getEffect(WUTEffects.BLIND.get());
  		        if (effect != null)
  		        {
  		            float percent = Math.min((effect.getDuration() / (float) 40), 1);
  		            Window window = Minecraft.getInstance().getWindow();
  		            guiGraphics.fill(RenderType.guiOverlay(), 0, 0, window.getScreenWidth(), window.getScreenHeight(), ((int) (percent * 255 + 0.5) << 24) | 16777215);
  		        }
  			}
	    }
    }
    
    private static void renderSwordHud(GuiGraphics guiGraphics)
    {
		var mc = Minecraft.getInstance();
		if(mc.player != null && isVisible(mc))
	    {
			var pos = HudHelper.getHudPos();
  			if(pos != null)
  			{
  		        Player player = mc.player;
  		        ItemStack heldStack = PlayerUtil.getPlayerItemIfHeld(player);

  				guiGraphics.pose().pushPose();

  	        	int i2 = HudHelper.getEnergyBarScaled(heldStack);
  	        	int xPosHead = (int) (pos.x / 0.33) - 18 + 40;
  	        	int yPosHead = (int) (pos.y / 0.33) - 78 - 70;
  	        	
  		        if (heldStack.getItem() instanceof SwordSteelItem sword)
  		        {
  		            float progress = Mth.clamp(player.getCooldowns().getCooldownPercent(WUTItems.SWORD.get(), 0.0f), 0, 1);
  		            if (progress < 1)
  		            {
  		                int blitHeight = (int) (progress * 32);
  		                guiGraphics.pose().pushPose();
  		                RenderSystem.disableBlend();
  		                AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(ICONS);
  		                RenderSystem.setShaderTexture(0, texture.getId());
  		                Window window = Minecraft.getInstance().getWindow();
  		                ScreenPositionHelper screenPosition = new ScreenPositionHelper(0, 0, 8, 0, 1, -32);
  		                int x = (int) screenPosition.x(window.getGuiScaledWidth(), window.getGuiScaledHeight());
  		                int y = (int) screenPosition.y(window.getGuiScaledWidth(), window.getGuiScaledHeight());
  		                guiGraphics.blit(ICONS, x - 10, y / 2 + 16, 0, 0, 32, 32);
  		                guiGraphics.blit(ICONS, x - 10, y / 2 + 24 + 32 - blitHeight - 8, 0, 32 + (32 - blitHeight), 32, blitHeight);
  		                guiGraphics.pose().popPose();
  		            }
  					if(sword.getPowerLevel(heldStack) > 0)
  					{
  						guiGraphics.pose().pushPose();
  						guiGraphics.pose().scale(0.33F, 0.33F, 1.0F);
  						guiGraphics.blit(HUD_TEXTURE, xPosHead, yPosHead + 192, 0, 0, 156, 28, 256, 256);
  						guiGraphics.blit(HUD_TEXTURE, xPosHead +2, yPosHead + 192, 156 - i2, 28, i2, 28, 256, 256);
  						guiGraphics.pose().popPose();

  	              		if(EnergyUtil.getEnergyStored(heldStack) > 0)
  	              		{
  	  						guiGraphics.pose().pushPose();
  	  						
  	  		                Window window = Minecraft.getInstance().getWindow();
  	  		                ScreenPositionHelper screenPosition = new ScreenPositionHelper(0, 0, 8, 0, 1, -32);
  	  		                int x = (int) screenPosition.x(window.getGuiScaledWidth(), window.getGuiScaledHeight());
  	  		                int y = (int) screenPosition.y(window.getGuiScaledWidth(), window.getGuiScaledHeight());
  	  						
  	  		  				RenderSystem.setShader(GameRenderer::getPositionTexShader);

  	  	              		if(player.swinging)
  	  	  	              		guiGraphics.blit(WitherUtils.loc("textures/item/sword_swinging.png"), x - 5, y / 2 + 16, 0, 0, 32, 32, 32, 32);
  	  	              		else
  	  	              			guiGraphics.blit(WitherUtils.loc("textures/item/sword_powered.png"), x - 5, y / 2 + 16, 0, 0, 32, 32, 32, 390);
  	  	              		
  	  	              		guiGraphics.pose().popPose();
  	              		}

  	                  	if(EnergyUtil.getEnergyStored(heldStack) <= 0)
  	                  	{
  	                  		guiGraphics.pose().pushPose();
  	            			float time = mc.level.getGameTime() + McTimerUtil.renderPartialTickTime;
  	            			guiGraphics.pose().translate(0.0D + Math.sin(time * 1.0D / 2.0D) / 12.0D * 12, 0.0D, 0.0D);
  	            			guiGraphics.drawString(mc.font, HudHelper.getFuelString(heldStack), pos.x + 12, pos.y + 14, 16324998);
  	                      	guiGraphics.pose().popPose();
  	                  	}
  	                  	else
  	                  	{
  	                  		if(player.swinging)
  	                  		{
  	                			float time = mc.level.getGameTime() + McTimerUtil.renderPartialTickTime;
  	                			guiGraphics.pose().translate(0.0D + Math.sin(time * 4.0D) / 12.0D * 12, 0.0D, 0.0D);
  	                			guiGraphics.drawString(mc.font, HudHelper.getFuelString(heldStack), pos.x + 12, pos.y + 14, 16324998);
  	                  		}
  	                  		else
  	                  			guiGraphics.drawString(mc.font, HudHelper.getFuelString(heldStack), pos.x + 12, pos.y + 14, 11113998);
  	                  	}
  					}
  		        }
  		        guiGraphics.pose().popPose();
  			}
	    }
    }
    
    private static void renderWandHud(GuiGraphics guiGraphics)
    {
		var mc = Minecraft.getInstance();
		if(mc.player != null && isVisible(mc))
	    {
			var pos = HudHelper.getHudPos();
  			if(pos != null)
  			{
  		        Player player = mc.player;
  		        ItemStack heldStack = PlayerUtil.getPlayerItemIfHeld(player);

  				guiGraphics.pose().pushPose();

  	        	int i2 = HudHelper.getEnergyBarScaled(heldStack);
  	        	int xPosHead = (int) (pos.x / 0.33) - 18 + 40;
  	        	int yPosHead = (int) (pos.y / 0.33) - 78 - 70;
  	        	
  		        if (heldStack.getItem() instanceof WandSteelItem wand)
  		        {
  		            float progress = Mth.clamp(player.getCooldowns().getCooldownPercent(WUTItems.WAND.get(), 0.0f), 0, 1);
  		            if (progress < 1)
  		            {
  		                int blitHeight = (int) (progress * 32);
  		                guiGraphics.pose().pushPose();
  		                RenderSystem.disableBlend();
  		                AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(ICONS);
  		                RenderSystem.setShaderTexture(0, texture.getId());
  		                Window window = Minecraft.getInstance().getWindow();
  		                ScreenPositionHelper screenPosition = new ScreenPositionHelper(0, 0, 8, 0, 1, -32);
  		                int x = (int) screenPosition.x(window.getGuiScaledWidth(), window.getGuiScaledHeight());
  		                int y = (int) screenPosition.y(window.getGuiScaledWidth(), window.getGuiScaledHeight());
  		                guiGraphics.blit(ICONS, x - 10, y / 2 + 16, 0, 0, 32, 32);
  		                guiGraphics.blit(ICONS, x - 10, y / 2 + 24 + 32 - blitHeight - 8, 0, 32 + (32 - blitHeight), 32, blitHeight);
  		                guiGraphics.pose().popPose();
  		            }
  					if(wand.getPowerLevel(heldStack) > 0)
  					{
  						guiGraphics.pose().pushPose();
  						guiGraphics.pose().scale(0.33F, 0.33F, 1.0F);
  						guiGraphics.blit(HUD_TEXTURE, xPosHead, yPosHead + 192, 0, 0, 156, 28, 256, 256);
  						guiGraphics.blit(HUD_TEXTURE, xPosHead +2, yPosHead + 192, 156 - i2, 28, i2, 28, 256, 256);
  						guiGraphics.pose().popPose();

  	              		if(EnergyUtil.getEnergyStored(heldStack) > 0)
  	              		{
  	  						guiGraphics.pose().pushPose();
  	  						
  	  		                Window window = Minecraft.getInstance().getWindow();
  	  		                ScreenPositionHelper screenPosition = new ScreenPositionHelper(0, 0, 8, 0, 1, -32);
  	  		                int x = (int) screenPosition.x(window.getGuiScaledWidth(), window.getGuiScaledHeight());
  	  		                int y = (int) screenPosition.y(window.getGuiScaledWidth(), window.getGuiScaledHeight());
  	  						
  	  		  				RenderSystem.setShader(GameRenderer::getPositionTexShader);

  	  	              		if(player.swinging)
  	  	  	              		guiGraphics.blit(WitherUtils.loc("textures/item/wand_swinging.png"), x - 5, y / 2 + 16, 0, 0, 32, 32, 32, 32);
  	  	              		else
  	  	              			guiGraphics.blit(WitherUtils.loc("textures/item/wand_powered.png"), x - 5, y / 2 + 16, 0, 0, 32, 32, 32, 390);
  	  	              		
  	  	              		guiGraphics.pose().popPose();
  	              		}

  	                  	if(EnergyUtil.getEnergyStored(heldStack) <= 0)
  	                  	{
  	                  		guiGraphics.pose().pushPose();
  	            			float time = mc.level.getGameTime() + McTimerUtil.renderPartialTickTime;
  	            			guiGraphics.pose().translate(0.0D + Math.sin(time * 1.0D / 2.0D) / 12.0D * 12, 0.0D, 0.0D);
  	            			guiGraphics.drawString(mc.font, HudHelper.getFuelString(heldStack), pos.x + 12, pos.y + 14, 16324998);
  	                      	guiGraphics.pose().popPose();
  	                  	}
  	                  	else
  	                  	{
  	                  		if(player.swinging)
  	                  		{
  	                			float time = mc.level.getGameTime() + McTimerUtil.renderPartialTickTime;
  	                			guiGraphics.pose().translate(0.0D + Math.sin(time * 4.0D) / 12.0D * 12, 0.0D, 0.0D);
  	                			guiGraphics.drawString(mc.font, HudHelper.getFuelString(heldStack), pos.x + 12, pos.y + 14, 16324998);
  	                  		}
  	                  		else
  	                  			guiGraphics.drawString(mc.font, HudHelper.getFuelString(heldStack), pos.x + 12, pos.y + 14, 11113998);
  	                  	}
  					}
  		        }
  		        guiGraphics.pose().popPose();
  			}
	    }
    }
    
    private static void renderShieldHud(GuiGraphics guiGraphics)
    {
		var mc = Minecraft.getInstance();
		if(mc.player != null && isVisible(mc))
	    {
			var pos = HudHelper.getHudPos();
  			if(pos != null)
  			{
  		        Player player = mc.player;
  				ItemStack heldStack = PlayerUtil.getPlayerItemIfHeld(player);

  				guiGraphics.pose().pushPose();

  	        	int i2 = HudHelper.getEnergyBarScaled(heldStack);
  	        	int xPosHead = (int) (pos.x / 0.33) - 18 + 40;
  	        	int yPosHead = (int) (pos.y / 0.33) - 78 - 70;
  	        	
  		        if (heldStack.getItem() instanceof ShieldPoweredItem shield)
  		        {
  		            float progress = Mth.clamp(player.getCooldowns().getCooldownPercent(WUTItems.SHIELDENERGY.get(), 0.0f), 0, 1);
  		            if (progress < 1)
  		            {
  		                int blitHeight = (int) (progress * 32);
  		                guiGraphics.pose().pushPose();
  		                RenderSystem.disableBlend();
  		                AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(ICONS);
  		                RenderSystem.setShaderTexture(0, texture.getId());
  		                Window window = Minecraft.getInstance().getWindow();
  		                ScreenPositionHelper screenPosition = new ScreenPositionHelper(0, 0, 8, 0, 1, -32);
  		                int x = (int) screenPosition.x(window.getGuiScaledWidth(), window.getGuiScaledHeight());
  		                int y = (int) screenPosition.y(window.getGuiScaledWidth(), window.getGuiScaledHeight());
  		                guiGraphics.blit(ICONS, x - 10, y / 2 + 16, 0, 0, 32, 32);
  		                guiGraphics.blit(ICONS, x - 10, y / 2 + 24 + 32 - blitHeight - 8, 0, 32 + (32 - blitHeight), 32, blitHeight);
  		                guiGraphics.pose().popPose();
  		            }
  					if(shield.getPowerLevel(heldStack) > 0)
  					{
  						guiGraphics.pose().pushPose();
  						guiGraphics.pose().scale(0.33F, 0.33F, 1.0F);
  						guiGraphics.blit(HUD_TEXTURE, xPosHead, yPosHead + 192, 0, 0, 156, 28, 256, 256);
  						guiGraphics.blit(HUD_TEXTURE, xPosHead +2, yPosHead + 192, 156 - i2, 28, i2, 28, 256, 256);
  						guiGraphics.pose().popPose();

  	              		if(EnergyUtil.getEnergyStored(heldStack) > 0)
  	              		{
  	  						guiGraphics.pose().pushPose();
  	  						
  	  		                Window window = Minecraft.getInstance().getWindow();
  	  		                ScreenPositionHelper screenPosition = new ScreenPositionHelper(0, 0, 8, 0, 1, -32);
  	  		                int x = (int) screenPosition.x(window.getGuiScaledWidth(), window.getGuiScaledHeight());
  	  		                int y = (int) screenPosition.y(window.getGuiScaledWidth(), window.getGuiScaledHeight());
  	  						
  	  		  				RenderSystem.setShader(GameRenderer::getPositionTexShader);

  	  		  				guiGraphics.blit(WitherUtils.loc("textures/item/shield/energy.png"), x, y / 2 + 32, 0, 0, 22, 22, 22, 22);
  	  	              		
  	  	              		guiGraphics.pose().popPose();
  	              		}

  	                  	if(EnergyUtil.getEnergyStored(heldStack) <= 0)
  	                  	{
  	                  		guiGraphics.pose().pushPose();
  	            			float time = mc.level.getGameTime() + McTimerUtil.renderPartialTickTime;
  	            			guiGraphics.pose().translate(0.0D + Math.sin(time * 1.0D / 2.0D) / 12.0D * 12, 0.0D, 0.0D);
  	            			guiGraphics.drawString(mc.font, HudHelper.getFuelString(heldStack), pos.x + 12, pos.y + 14, 16324998);
  	                      	guiGraphics.pose().popPose();
  	                  	}
  	                  	else
  	                  	{
  	                  		if(player.isUsingItem())
  	                  		{
  	                			float time = mc.level.getGameTime() + McTimerUtil.renderPartialTickTime;
  	                			guiGraphics.pose().translate(0.0D + Math.sin(time * 4.0D) / 12.0D * 12, 0.0D, 0.0D);
  	                			guiGraphics.drawString(mc.font, HudHelper.getFuelString(heldStack), pos.x + 12, pos.y + 14, 16324998);
  	                  		}
  	                  		else
  	                  			guiGraphics.drawString(mc.font, HudHelper.getFuelString(heldStack), pos.x + 12, pos.y + 14, 11113998);
  	                  	}
  					}
  		        }
  		        guiGraphics.pose().popPose();
  			}
	    }
    }
}
