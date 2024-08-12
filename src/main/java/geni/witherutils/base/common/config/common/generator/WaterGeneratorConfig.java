package geni.witherutils.base.common.config.common.generator;

import net.neoforged.neoforge.common.ModConfigSpec;

public class WaterGeneratorConfig {
    
    public static ModConfigSpec.ConfigValue<Boolean> HASEFFECIENCY;
    public static ModConfigSpec.ConfigValue<Double> EFFECIENCYBASE;
    public static ModConfigSpec.ConfigValue<Integer> MAXENERGY;
    public static ModConfigSpec.ConfigValue<Integer> SENDPERTICK;
    public static ModConfigSpec.ConfigValue<Integer> CHARGEITEMPERTICK;
    
    public WaterGeneratorConfig(ModConfigSpec.Builder builder)
    {
    	builder.push("watergenerator");
    	
    	builder.push("generatorEffeciencyRate");
        HASEFFECIENCY = builder
                .comment("Generates RF by EffeciencyRate")
                .define("generatorEffeciencyRate", true);
        builder.pop();
        
        builder.push("generatorEffeciencyBase");
        EFFECIENCYBASE = builder
                .comment("Lowest of EffeciencyRate")
                .defineInRange("generatorEffeciencyBase", 0.5, 0.1, 100.0);
        builder.pop();
        
        builder.push("generatorMaxRF");
        MAXENERGY = builder
                .comment("Maximum RF storage that the generator can hold")
                .define("generatorMaxRF", 100000);
        builder.pop();
        
        builder.push("generatorRFPerTick");
        SENDPERTICK = builder
                .comment("RF per tick that the generator can send")
                .define("generatorRFPerTick", 2000);
        builder.pop();
        
        builder.push("generatorChargePerTick");
        CHARGEITEMPERTICK = builder
                .comment("RF per tick that the generator can charge items with")
                .defineInRange("generatorChargePerTick", 1000, 0, Integer.MAX_VALUE);
        builder.pop();
        
        builder.pop();
    }
}
