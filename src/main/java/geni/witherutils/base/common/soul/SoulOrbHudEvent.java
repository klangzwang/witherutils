package geni.witherutils.base.common.soul;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.item.soulorb.SoulOrbItem;
import geni.witherutils.core.common.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SoulOrbHudEvent {

    @SubscribeEvent
    public void onRegisterGuiOverlays(RegisterGuiOverlaysEvent event)
    {
        event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "soulorb_hud", SOULORBOVERLAY);
    }
    
    private static boolean isVisible(Minecraft mc)
    {
        return !(mc.screen instanceof ChatScreen)
                && !mc.options.hideGui
                && !mc.options.renderDebug;
    }
	
	private static final IGuiOverlay SOULORBOVERLAY = (gui, gg, partialTick, width, height) -> {
		
		var mc = Minecraft.getInstance();
		if (mc.player != null && isVisible(mc))
		{
			gg.pose().pushPose();
			
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, SoulOrbManager.souls / 100.0F);
			
			if(PlayerUtil.getPlayerItemIfHeld(mc.player).getItem() instanceof SoulOrbItem || SoulOrbManager.souls > 0)
			{
				final float charge = Mth.clamp(0.0f + SoulOrbManager.souls / (float) 2000, 0.0f, 1.0f);
				final int x = width / 2 - 91;
				final int filled = (int) (charge * 183.0f);
				final int top = height - height + 48 - 18;

				gg.blit(new ResourceLocation("textures/gui/icons.png"), x, top, 0, 84, 182, 5, 256, 256);

				if (filled > 0)
				{
					gg.blit(new ResourceLocation("textures/gui/icons.png"), x, top, 0, 89, filled, 5, 256, 256);
				}
				
				gg.blit(WitherUtils.loc("textures/item/soulorb.png"), x - 20, top - 6, 0, 0, 24, 16, 22, 100);
				gg.drawString(mc.font, Component.literal("Souls: " + SoulOrbManager.souls), x, top + 8, 0xFF228888, false);

				renderClientInfos(mc, mc.player, gui, gg, partialTick, width, height, x, top + 16);
			}

			RenderSystem.disableBlend();
			RenderSystem.defaultBlendFunc();
			
			gg.pose().popPose();
		}
	};

	public static int getMaxSoulOrbs()
	{
		return 200;
	}
	
	/*
	 * 
	 * SOULORBMANAGEMENT
	 * 
	 */
	public static void renderClientInfos(Minecraft mc, Player player, ForgeGui gui, GuiGraphics gg, float partialTick, int w, int h, int x, int y)
	{
		SoulOrbManager manager = SoulOrbManager.INSTANCE;
		if(manager == null)
			return;
		gg.drawString(mc.font, Component.literal("SoulAmount: " + SoulOrbManager.getCurrentSouls()), x, y + 16, 0xFF228888, false);
		gg.drawString(mc.font, Component.literal("Multiplier: " + "..."), x, y + 24, 0xFF2288FF, false);
		gg.drawString(mc.font, Component.literal("Strength: " + "n/a"), x, y + 32, 0xFF2288FF, false);
	}
}