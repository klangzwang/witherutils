package geni.witherutils.base.common.config.common.generator;

import net.minecraftforge.common.ForgeConfigSpec;

public class LavaGeneratorConfig {
    
    public static ForgeConfigSpec.ConfigValue<Boolean> HASEFFECIENCY;
    public static ForgeConfigSpec.ConfigValue<Double> EFFECIENCYBASE;
    public static ForgeConfigSpec.ConfigValue<Integer> MAXENERGY;
    public static ForgeConfigSpec.ConfigValue<Integer> SENDPERTICK;
    public static ForgeConfigSpec.ConfigValue<Integer> CHARGEITEMPERTICK;
    public static ForgeConfigSpec.ConfigValue<Integer> DISSAPEARTIME;
    
    public LavaGeneratorConfig(ForgeConfigSpec.Builder builder)
    {
    	builder.push("lavagenerator");
    	
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
        
		builder.push("dissapearLavaTick");
        DISSAPEARTIME = builder
                .comment("How much ticks before Lava Blocks can dissapear.")
                .defineInRange("dissapearLavaTick", 1000, 0, Integer.MAX_VALUE);
        builder.pop();
        
        builder.pop();
    }
}
