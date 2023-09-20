package geni.witherutils.core.common.util;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@SuppressWarnings("unused")
public class McTimerUtil {

	public static int clientTimer;
	public static int frameTimer;
	public static float renderTimer;
	public static float renderPartialTickTime;
	public static int serverTimer;
	public static float deltaTime = 1;
	private static long lastServerTickTime = -1;

	private long start;

	public McTimerUtil()
	{
		start();
	}

	public McTimerUtil(long start)
	{
		setStart(start);
	}

	public void start()
	{
		setStart(System.currentTimeMillis());
	}

	public void setStart(long start)
	{
		if (start < 0)
			setRelativeStart(start);
		else
			this.start = start;
	}

	public void setRelativeStart(long start)
	{
		setStart(System.currentTimeMillis() + start);
	}

	public long getStart()
	{
		return start;
	}

	public long elapsedTime()
	{
		return System.currentTimeMillis() - start;
	}

	public long elapsedTick()
	{
		return timeToTick(System.currentTimeMillis() - start);
	}

	public static long timeToTick(long time)
	{
		return time * 20 / 1000;
	}

	public static long tickToTime(long ticks)
	{
		return ticks * 1000 / 20;
	}

	@Override
	public String toString()
	{
		int elapsed = (int) (elapsedTime() / 1000);
		int minutes = (elapsed % 3600) / 60;
		int seconds = elapsed % 60;

		return String.format("%02d:%02d ago", minutes, seconds);
	}

	static
	{
		MinecraftForge.EVENT_BUS.register(new McTimerUtil());
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void serverTick(TickEvent.ServerTickEvent event)
	{
		if(event.phase != TickEvent.Phase.START)
		{
			return;
		}
		serverTimer++;
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void clientTick(TickEvent.ClientTickEvent event)
	{
		if(event.phase == TickEvent.Phase.START)
			clientTimer++;
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void renderTick(TickEvent.RenderTickEvent event)
	{
		renderPartialTickTime = event.renderTickTime;
		renderTimer = clientTimer + renderPartialTickTime;
		if(event.phase == TickEvent.Phase.START)
		{
			frameTimer++;
		}
	}
	
    /**
     * Takes in the amount of ticks, and converts it into a time notation. 40 ticks will become "2s", while 2400 will result in "2m".
     *
     * @param ticks number of ticks
     * @param fraction When true, 30 ticks will show as '1.5s' instead of '1s'.
     * @return a formatted time
     */
    public static String convertTicksToMinutesAndSeconds(long ticks, boolean fraction)
    {
        String part = String.valueOf(ticks % 20 * 5);
        if (part.length() < 2) part = "0" + part;
        ticks /= 20;
        if (ticks < 60)
        {
            return ticks + (fraction ? "." + part : "") + "s";
        }
        else
        {
            return ticks / 60 + "m " + ticks % 60 + "s";
        }
    }
}
