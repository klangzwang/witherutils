package geni.witherutils.base.common.item.withersteel.armor.upgrades.squidring;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import geni.witherutils.base.common.init.WUTEnchants;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.base.common.item.withersteel.armor.SteelArmorItem;
import geni.witherutils.core.common.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SquidRingHudEvents {

    @SubscribeEvent
    public void onRegisterGuiOverlays(RegisterGuiOverlaysEvent event)
    {
        event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "chickenring_hud", RINGOVERLAY);
    }
    private static boolean isVisible(Minecraft mc)
    {
        return !(mc.screen instanceof ChatScreen)
                && !mc.options.hideGui
                && !mc.options.renderDebug;
    }
	
	private static final IGuiOverlay RINGOVERLAY = (gui, gg, partialTick, width, height) -> {
		
		var mc = Minecraft.getInstance();
		if (mc.player != null && isVisible(mc))
		{
			for (ItemStack armorstack : PlayerUtil.invArmorStacks(mc.player))
			{
				if (armorstack.getItem() instanceof SteelArmorItem steelArmor && armorstack.getEnchantmentLevel(WUTEnchants.SQUID_JUMP.get()) > 0)
				{
					gg.pose().pushPose();

					if (armorstack.is(WUTItems.STEELARMOR_BOOTS.get()) && steelArmor.isChargeable(armorstack))
					{
						RenderSystem.setShader(GameRenderer::getPositionTexShader);
						RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

						GlStateManager._disableBlend();
						final float charge = Mth.clamp(1.0f - SquidRingUpgrade.tickFlight / (float) getMaxFlightTime(), 0.0f, 1.0f);
						final int x = width / 2 - 91;
						final int filled = (int) (charge * 183.0f);
						final int top = height - 42 + 3 - 18;

						gg.blit(new ResourceLocation("textures/gui/icons.png"), x, top, 0, 84, 182, 5, 256, 256);

						if (filled > 0)
						{
							gg.blit(new ResourceLocation("textures/gui/icons.png"), x, top, 0, 89, filled, 5, 256, 256);
						}
						GlStateManager._enableBlend();

						gg.pose().popPose();
					}
				}
			}
		}
	};
	
	public static int getMaxFlightTime()
	{
		return 200;
	}
}
