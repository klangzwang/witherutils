package geni.witherutils.core.common.util;

import net.minecraft.client.Minecraft;

public class McTimerUtil {

	public static float getFps(Minecraft mc)
	{
		return mc.getFps();
	}
	
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
