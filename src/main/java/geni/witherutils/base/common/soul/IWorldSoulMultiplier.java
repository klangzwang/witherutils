package geni.witherutils.base.common.soul;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.world.level.Level;

public interface IWorldSoulMultiplier {
	
    public static final IWorldSoulMultiplier CONSTANT = new IWorldSoulMultiplier()
    {
        @Override
        public float multiplier(final Level world)
        {
            return 1.0f;
        }
        @Override
        public String toString()
        {
            return "CONSTANT";
        }
    };
    
    default float capPower(final float input, @Nullable final TreeMap<Float, Pair<Float, Float>> efficiencyLevels)
    {
        if (efficiencyLevels == null || efficiencyLevels.isEmpty() || input < efficiencyLevels.firstKey())
        {
            return input;
        }
        final Map.Entry<Float, Pair<Float, Float>> calc = efficiencyLevels.floorEntry(input);
        if (calc == null)
        {
            return input;
        }
        return (float)calc.getValue().getKey() + (input - calc.getKey()) * (float)calc.getValue().getValue();
    }
    
    default TreeMap<Float, Pair<Float, Float>> createCapsTree(final float[][] caps)
    {
        if (caps.length == 0)
        {
            return null;
        }
        final TreeMap<Float, Pair<Float, Float>> map = new TreeMap<Float, Pair<Float, Float>>();
        float curTotal = caps[0][0];
        if (curTotal != 0.0f)
        {
            map.put(0.0f, Pair.of(0.0f, 1.0f));
        }
        for (int i = 0; i < caps.length; ++i)
        {
            final float prevLower = caps[i][0];
            final float multiplier = caps[i][1];
            map.put(prevLower, Pair.of(curTotal, multiplier));
            if (i + 1 < caps.length)
            {
                final float nextLevel = caps[i + 1][0];
                curTotal += (nextLevel - prevLower) * multiplier;
            }
        }
        return map;
    }
    
    float multiplier(@Nullable final Level p0);
    
    default IWorldSoulMultiplier getStaticVariation()
    {
        return IWorldSoulMultiplier.CONSTANT;
    }
    default float alterTotal(final float value)
    {
        return value;
    }
    default boolean hasInefficiencies()
    {
        return false;
    }
}