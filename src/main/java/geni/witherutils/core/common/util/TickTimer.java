package geni.witherutils.core.common.util;

import javax.annotation.Nonnull;

import geni.witherutils.WitherUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = WitherUtils.MODID)
public final class TickTimer {

	private static long serverTickCount = 0;
	private static long clientTickCount = 0;
	private static long clientPausedTickCount = 0;

	@SubscribeEvent
	public static void onTick(@Nonnull ServerTickEvent evt)
	{
		if (evt.phase == Phase.END)
		{
			++serverTickCount;
		}
	}
	@SubscribeEvent
	public static void onTick(@Nonnull ClientTickEvent evt)
	{
		if (evt.phase == Phase.END)
		{
			if (Minecraft.getInstance().isPaused())
			{
				++clientPausedTickCount;
			}
			else
			{
				++clientTickCount;
			}
		}
	}
	public static long getServerTickCount()
	{
		return serverTickCount;
	}
	public static long getClientTickCount()
	{
		return clientTickCount;
	}
	public static long getClientPausedTickCount()
	{
		return clientPausedTickCount;
	}
}