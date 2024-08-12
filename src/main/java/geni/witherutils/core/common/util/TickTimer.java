package geni.witherutils.core.common.util;

import javax.annotation.Nonnull;

import geni.witherutils.api.WitherUtilsRegistry;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = WitherUtilsRegistry.MODID)
public final class TickTimer {

	private static long serverTickCount = 0;
	private static long clientTickCount = 0;
	private static long clientPausedTickCount = 0;

	@SubscribeEvent
	public static void onTick(@Nonnull ServerTickEvent.Post evt)
	{
		if (evt.hasTime())
		{
			++serverTickCount;
		}
	}
	@SubscribeEvent
	public static void onTick(@Nonnull ClientTickEvent.Post evt)
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