package geni.witherutils.base.common.soul;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Nullable;

import geni.witherutils.base.common.soul.player.IPlayerSoulCreator;
import geni.witherutils.base.common.soul.player.PlayerSoul;
import geni.witherutils.core.common.network.CoreNetwork;
import geni.witherutils.core.common.network.PacketSoulInfo;
import geni.witherutils.core.common.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class ClientSoul // implements IHudHandler
{
    private static final HashSet<Item> powerItems;
    public static HashMap<IPlayerSoulCreator, PlayerSoul> powerClient;
    public static float powerCreated;
    public static float powerDrained;
    public static final SoulOrbManager.IPowerReport POWER_REPORT;
    public static BlockPos currentPosition;
    public static float currentPositionEnergy;
    public static float currentPositionEfficiency;

    public static void init()
    {
    }

    @SuppressWarnings("unlikely-arg-type")
	@Nullable
    public static <T extends Item> PlayerSoul getClient(final T item)
    {
        return ClientSoul.powerClient.get(item);
    }
    public static String powerStatusString()
    {
        return Component.literal("Grid Power:") + " " + ClientSoul.powerDrained + " / " + ClientSoul.powerCreated;
    }
    public static boolean isPowered()
    {
        return ClientSoul.powerDrained <= ClientSoul.powerCreated;
    }
    public static boolean hasNoPower()
    {
        return ClientSoul.powerDrained == 0.0f && ClientSoul.powerCreated == 0.0f;
    }

    @SuppressWarnings("resource")
	@SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void PlayerSoulManagerTick(final TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            return;
        }
        final Player player = Minecraft.getInstance().player;
        if (player == null)
        {
            return;
        }
        for (final ItemStack stack : PlayerUtil.invStacks(player))
        {
            if (!stack.isEmpty() && stack.getItem() instanceof IPlayerSoulCreator)
            {
                final IPlayerSoulCreator creator = (IPlayerSoulCreator)stack.getItem();
                PlayerSoul PlayerSoul = ClientSoul.powerClient.get(creator);
                if (PlayerSoul == null || creator.shouldOverride(PlayerSoul, player, stack, stack == player.inventory.getSelected()))
                {
                    PlayerSoul = creator.createSoul(player, stack);
                    ClientSoul.powerClient.put(creator, PlayerSoul);
                }
                else if (!PlayerSoul.shouldSustain(stack))
                {
                    continue;
                }
                PlayerSoul.cooldown = 2;
            }
        }
        final Iterator<Map.Entry<IPlayerSoulCreator, PlayerSoul>> iterator = ClientSoul.powerClient.entrySet().iterator();
        while (iterator.hasNext())
        {
            final Map.Entry<IPlayerSoulCreator, PlayerSoul> entry = iterator.next();
            final PlayerSoul PlayerSoul3;
            final PlayerSoul PlayerSoul2 = PlayerSoul3 = entry.getValue();
            --PlayerSoul3.cooldown;
            if (PlayerSoul2.cooldown < 0)
            {
                iterator.remove();
            }
            else
            {
                PlayerSoul2.tickClient();
            }
        }
    }
    
    @SuppressWarnings("resource")
	@SubscribeEvent
    public void clientTick(final TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END) {
            return;
        }
        final HitResult mop = Minecraft.getInstance().hitResult;
        if (mop == null || mop.getType() != HitResult.Type.BLOCK)
        {
            ClientSoul.currentPosition = null;
            ClientSoul.currentPositionEnergy = Float.NaN;
        }
        else
        {
            if (ClientSoul.currentPosition != null && !ClientSoul.currentPosition.equals(mop.getLocation()))
            {
                ClientSoul.currentPositionEnergy = Float.NaN;
            }
            ClientSoul.currentPosition = new BlockPos((int) mop.getLocation().x, (int) mop.getLocation().y, (int) mop.getLocation().z);
            final ClientLevel theWorld = Minecraft.getInstance().level;
            if (theWorld != null)
            {
                final BlockEntity tileEntity = theWorld.getBlockEntity(new BlockPos((int) mop.getLocation().x, (int) mop.getLocation().y, (int) mop.getLocation().z));
                if (tileEntity instanceof ISoul)
                {
                    CoreNetwork.sendToServer(new PacketSoulInfo(ClientSoul.currentPositionEnergy, ClientSoul.currentPosition));
                }
                else
                {
                    ClientSoul.currentPositionEnergy = Float.NaN;
                    ClientSoul.currentPositionEfficiency = 1.0f;
                    ClientSoul.currentPosition = null;
                }
            }
        }
    }
    
    @SuppressWarnings("resource")
	public void render(GuiGraphics gg, final float partialTicks)
    {
    	float resolutionSW = Minecraft.getInstance().getWindow().getGuiScaledWidth();
    	float resolutionSH = Minecraft.getInstance().getWindow().getGuiScaledHeight();
    	Font font = Minecraft.getInstance().font;
        boolean flag = ClientSoul.currentPosition != null;
        final AbstractClientPlayer thePlayer = Minecraft.getInstance().player;
        if (!flag && thePlayer != null && !thePlayer.getMainHandItem().isEmpty() && ClientSoul.powerItems.contains(thePlayer.getMainHandItem().getItem()))
        {
            flag = true;
        }
        if (!flag)
        {
            return;
        }
        int y = (int) (resolutionSH * 7 / 10);
        gg.drawCenteredString(font, powerStatusString(), (int) resolutionSW / 2, y, -1);
        if (!Float.isNaN(ClientSoul.currentPositionEnergy)) {
            y += font.lineHeight + 1;
            if (ClientSoul.currentPositionEnergy == 0.0f) {
                final String text = "No Power Used/Generated";
                gg.drawCenteredString(font, text, (int) resolutionSW / 2, y, -1);
            }
            else {
                String text;
                if (ClientSoul.currentPositionEnergy < 0.0f) {
                    text = "Power Generating:" + " " + -ClientSoul.currentPositionEnergy;
                }
                else {
                    text = "Power Drain:" + " " + ClientSoul.currentPositionEnergy;
                }
                gg.drawCenteredString(font, text, (int) resolutionSW / 2, y, -1);
                if (ClientSoul.currentPositionEfficiency != 1.0f) {
                    y += font.lineHeight + 1;
                    text = "Effective Rate:" + " " + Math.abs(-ClientSoul.currentPositionEnergy * ClientSoul.currentPositionEfficiency) + " (" + "1.0f - ClientSoul.currentPositionEfficiency" + " " + "Power Loss" + ")";
                    gg.drawCenteredString(font, text, (int) resolutionSW / 2, y, -1);
                }
            }
        }
    }
    
    static
    {
        powerItems = new HashSet<Item>();
        ClientSoul.powerClient = new HashMap<IPlayerSoulCreator, PlayerSoul>();
        POWER_REPORT = new SoulOrbManager.IPowerReport()
        {
            @Override
            public boolean isPowered()
            {
                return ClientSoul.isPowered();
            }
            @Override
            public float getPowerDrain()
            {
                return ClientSoul.powerDrained;
            }
            @Override
            public float getPowerCreated()
            {
                return ClientSoul.powerCreated;
            }
        };
        ClientSoul.currentPositionEfficiency = 1.0f;
//        for (final Entry entry : EntryHandler.entries)
//        {
//            if (entry instanceof EntryBlock && entry.enabled)
//            {
//                for (final Class clazz : (entry).teClazzes)
//                {
//                    if (ISoul.class.isAssignableFrom(clazz))
//                    {
//                        ClientSoul.powerItems.add((entry.value).itemBlock);
//                    }
//                }
//            }
//        }
//        final ClientSoul handler = new ClientSoul();
//        MinecraftForge.EVENT_BUS.register((Object)handler);
//        HUDHandler.register((IHudHandler)handler);
    }
}
