package geni.witherutils.core.common.helper;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class LightHelper {

	public static float calculateLight(@Nonnull Level level, BlockPos pos)
	{
		if(!isSunAbove(level, pos))
			return 0F;
		return calculateLightRatio(level, pos);
	}
	private static boolean isSunAbove(@Nonnull Level world, BlockPos pos)
	{
		return world.canSeeSkyFromBelowWater(pos);
	}
	private static float calculateLightRatio(@Nonnull Level level, BlockPos pos)
	{
		int lightValue = level.getLightEngine().getLayerListener(LightLayer.SKY).getLightValue(pos);
		float sunAngle = level.getSunAngle(1.0F);
		if(sunAngle < (float) Math.PI)
		{
			sunAngle += (0.0F - sunAngle) * 0.2F;
		}
		else
		{
			sunAngle += (((float) Math.PI * 2F) - sunAngle) * 0.2F;
		}
		lightValue = Math.round(lightValue * Mth.cos(sunAngle));
		lightValue = Mth.clamp(lightValue, 0, 15);

		return lightValue / 15f;
	}
}
