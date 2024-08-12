package geni.witherutils.base.client;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.common.init.WUTAttachments;
import geni.witherutils.base.common.init.WUTEffects;
import geni.witherutils.core.common.helper.HudHelper;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

public final class ClientHudEvents {
	
    private static final ResourceLocation SOULS_EMPTY_SPRITE = WitherUtilsRegistry.loc("textures/hud/souls_empty.png");
    private static final ResourceLocation SOULS_HALF_SPRITE = WitherUtilsRegistry.loc("textures/hud/souls_half.png");
    private static final ResourceLocation SOULS_FULL_SPRITE = WitherUtilsRegistry.loc("textures/hud/souls_full.png");
    
	public static int k = 0;
	static float alphagui = 1.0f;
	static int prevSouls;
	
    @SubscribeEvent
    public void onRegisterGuiOverlays(RegisterGuiLayersEvent event)
    {
        event.registerAbove(VanillaGuiLayers.ARMOR_LEVEL, WitherUtilsRegistry.loc("soulshud"), SOULOVERLAY);
    }
	
	private static final LayeredDraw.Layer SOULOVERLAY = (GuiGraphics pGuiGraphics, DeltaTracker pDeltaTracker) -> {
		renderBlindHud(pGuiGraphics, pDeltaTracker);
		renderSoulsHud(pGuiGraphics, pDeltaTracker);
	};
	
    private static boolean isVisible(Minecraft mc)
    {
        return !(mc.screen instanceof ChatScreen) && !mc.options.hideGui;
    }

    private static void renderBlindHud(GuiGraphics guiGraphics, DeltaTracker pDeltaTracker)
    {
		var mc = Minecraft.getInstance();
		if(mc.player != null && isVisible(mc))
	    {
			var pos = HudHelper.getHudPos();
  			if(pos != null)
  			{
  		        Player player = mc.player;
  		        MobEffectInstance effect = player.getEffect(WUTEffects.BLIND);
  		        if (effect != null)
  		        {
  		            float percent = Math.min((effect.getDuration() / (float) 40), 1);
  		            Window window = Minecraft.getInstance().getWindow();
  		            guiGraphics.fill(RenderType.guiOverlay(), 0, 0, window.getScreenWidth(), window.getScreenHeight(), ((int) (percent * 255 + 0.5) << 24) | 16777215);
  		        }
  			}
	    }
    }
    
	private static void renderSoulsHud(GuiGraphics guiGraphics, DeltaTracker pDeltaTracker)
    {
		var mc = Minecraft.getInstance();
		if(mc.player != null && isVisible(mc))
	    {
			var pos = HudHelper.getHudPos();
			var level = mc.player.level();
			
  			if(pos != null && level != null)
  			{
  		        Player player = mc.player;
  		        if (mc.gameMode.canHurtPlayer())
  		        {
	        		int pHeartRows = 1;
	        		int pHeight = 0;
	        		int pX = guiGraphics.guiWidth() / 2 - 91;
	        		int pY = guiGraphics.guiHeight() - mc.gui.leftHeight + 10;

  		        	int i = player.getData(WUTAttachments.SOULS_CONTROL);
  		        	if(i > 0)
  		        	{
  		        		alphagui = alphagui >= 1.0f ? 1.0f : alphagui + 1.0f / 40;
  		        	}
  		        	else
  		        	{
  		        		alphagui = alphagui <= 0.025 ? 0 : alphagui - 1.0f / 40;
  		        	}
	                
	        		RenderSystem.disableDepthTest();
	        		RenderSystem.depthMask(false);
	        		RenderSystem.enableBlend();
	        		guiGraphics.setColor(1.0F, 1.0F, 1.0F, alphagui);

	        		if(alphagui > 0.025f)
	        		{
		        		int j = pY - (pHeartRows - 1) * pHeight - 10;
		        		
//		                if (mc.gui.getGuiTicks() % (player.getData(WUTAttachments.SOULS_CONTROL) * 3 + 1) == 0)
//		                {
//		                    k = j + (level.random.nextInt(3) - 1);
//		                }
		        		
			        	for (int p = 0; p < 10; p++)
	  		            {
		        			int l = pX + p * 8;
		        			if (p * 2 + 1 < i)
		        			{
		        				guiGraphics.blit(SOULS_FULL_SPRITE, l, j, 0, 0, 9, 9, 9, 9);
		        			}
		        			if (p * 2 + 1 == i)
		        			{
		        				guiGraphics.blit(SOULS_HALF_SPRITE, l, j, 0, 0, 9, 9, 9, 9);
		        			}
		        			if (p * 2 + 1 > i)
		        			{
		  		            	guiGraphics.blit(SOULS_EMPTY_SPRITE, l, j, 0, 0, 9, 9, 9, 9);
		        			}
	  		            }
	        		}

  		        	RenderSystem.disableBlend();
  		        	RenderSystem.depthMask(true);
  		        	RenderSystem.enableDepthTest();
  		        	guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
  		        }
  			}
	    }
    }
}
