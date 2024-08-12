package geni.witherutils.core.common.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public class Sissy {

	public static int tickClient;
	public static int tickServer;
	
	public static int tickToZero;
	
	public static int tickPingPong;
	
	static
	{
		NeoForge.EVENT_BUS.addListener(Sissy::tickClient);
		NeoForge.EVENT_BUS.addListener(Sissy::tickServer);
	}

	public static void tickClient(PlayerTickEvent.Post event)
	{
		tickClient++;
	}
	public static void tickServer(ServerTickEvent.Pre event)
	{
		tickServer++;
		
		if(tickToZero > 0)
			tickToZero--;
	}
	public static void tickToZero(int fromHere)
	{
		tickToZero = fromHere;
	}

	/*
	 * 
	 * PRINTLINE TICK DELAY
	 * 
	 */
	public static void printl(int delay)
	{
		if(afterDelayOf(delay))
			System.out.println("...");
	}
	public static void printl(int delay, boolean x)
	{
		if(afterDelayOf(delay))
			System.out.println("|boolean| " + " Info:" + x);
	}
	public static void printl(int delay, char x)
	{
		if(x != 0)
			if(afterDelayOf(delay))
				System.out.println("|char| " + x + " Info:" + x);
	}
	public static void printl(int delay, int x)
	{
		if(afterDelayOf(delay))
			System.out.println("|int| " + " Info:" + x);
	}
	public static void printl(int delay, long x)
	{
		if(afterDelayOf(delay))
			System.out.println("|long| " + " Info:" + x);
	}
	public static void printl(int delay, float x)
	{
		if(afterDelayOf(delay))
			System.out.println("|float| " + " Info:" + x);
	}
	public static void printl(int delay, double x)
	{
		if(afterDelayOf(delay))
			System.out.println("|double| " + " Info:" + x);
	}
	public static void printl(int delay, char[] x)
	{
		if(x.length > 0)
			for(int i = 0; i < x.length; i++)
			{
				if(afterDelayOf(delay))
					System.out.println("|char| " + x.toString() +" Info:" + x[i]);
			}
	}
	public static void printl(int delay, String x)
	{
		if(x != null)
			if(afterDelayOf(delay))
				System.out.println("|String| " + x + " Info:" + x);
	}
	public static void printl(int delay, Object x)
	{
		if(x != null)
			if(afterDelayOf(delay))
				System.out.println("|Object| " + x + " Info:" + x);
	}
	
	public static Minecraft getInstance()
	{
		return Minecraft.getInstance();
	}
	
	@SuppressWarnings("resource")
	public static Level level()
	{
		return getInstance().level;
	}
	
	public static long getGameTime()
	{
		return level() != null ? level().getGameTime() : null;
	}
	
	public static boolean afterDelayOf(int delay)
	{
		return level() != null && getGameTime() % delay == 0;
	}
}
