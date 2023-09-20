package geni.witherutils.base.client;

import geni.witherutils.WitherUtils;
import geni.witherutils.core.common.menu.SyncedMenu;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WitherUtils.MODID, value = Dist.CLIENT)
public class ClientEventHandler {
    
    @SuppressWarnings("resource")
    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent e)
    {
        if (e.phase == TickEvent.Phase.END && Minecraft.getInstance().player != null)
        {
            if (Minecraft.getInstance().player.containerMenu instanceof SyncedMenu<?> syncedMenu)
            {
                syncedMenu.clientTick();
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public static void renderGameOverlayEvent(RenderGuiOverlayEvent.Pre event)
    {
        if (!event.getOverlay().id().equals(VanillaGuiOverlay.TITLE_TEXT.id()))
            return;
        ClientOverlayRenderer.renderHUD(event.getGuiGraphics(), event.getPartialTick());
    }
}
