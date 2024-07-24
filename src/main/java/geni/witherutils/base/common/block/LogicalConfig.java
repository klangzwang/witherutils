package geni.witherutils.base.common.block;

import org.apache.commons.lang3.tuple.Pair;

import net.neoforged.neoforge.common.ModConfigSpec;

public class LogicalConfig {
	
    public static final LogicalConfig COMMON;
    public static final ModConfigSpec COMMON_SPEC;

    static
    {
        Pair<LogicalConfig, ModConfigSpec> commonSpecPair = new ModConfigSpec.Builder().configure(LogicalConfig::new);
        COMMON = commonSpecPair.getLeft();
        COMMON_SPEC = commonSpecPair.getRight();
    }

    public static ModConfigSpec.ConfigValue<Double> ANVIL_SATURATION_COST;

    public LogicalConfig(ModConfigSpec.Builder builder)
    {
        builder.push("anvil");
        	ANVIL_SATURATION_COST = builder.comment("The Saturation Points, the Anvil Action cost, when you craft something with the Hammer.").define("saturationCostFactor", 3.0d);
        builder.pop();
    }
}
