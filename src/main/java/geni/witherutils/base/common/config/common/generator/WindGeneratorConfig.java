package geni.witherutils.base.common.config.common.generator;

import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.common.ForgeConfigSpec;

public class WindGeneratorConfig {
    
    public static ForgeConfigSpec.ConfigValue<Boolean> HASEFFECIENCY;
    public static ForgeConfigSpec.ConfigValue<Double> EFFECIENCYBASE;
    public static ForgeConfigSpec.ConfigValue<Integer> MAXENERGY;
    public static ForgeConfigSpec.ConfigValue<Integer> SENDPERTICK;
    public static ForgeConfigSpec.ConfigValue<Integer> CHARGEITEMPERTICK;
    public static ForgeConfigSpec.ConfigValue<Integer> GENERATIONMIN;
    public static ForgeConfigSpec.ConfigValue<Integer> GENERATIONMAX;
    public static ForgeConfigSpec.ConfigValue<Integer> GENERATIONMINY;
    public static ForgeConfigSpec.ConfigValue<Integer> GENERATIONMAXY;
    
    public WindGeneratorConfig(ForgeConfigSpec.Builder builder)
    {
    	builder.push("windgenerator");
    	
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
        
		builder.push("windGenerationMin");
        GENERATIONMIN = builder
                .comment("Minimum base generation value of the Wind Generator.")
                .define("windGenerationMin", 10);
        builder.pop();
        
		builder.push("windGenerationMax");
        GENERATIONMAX = builder
                .comment("Maximum base generation value of the Wind Generator.")
                .define("windGenerationMax", 480);    
        builder.pop();
        
		builder.push("minY");
        GENERATIONMINY = builder
                .comment("The minimum Y value that affects the Wind Generators Power generation.")
                .defineInRange("minY", 24, DimensionType.MIN_Y, DimensionType.MAX_Y - 1);
        builder.pop();
        
		builder.push("maxY");
        GENERATIONMAXY = builder
                .comment("The maximum Y value that affects the Wind Generators Power generation.")
                .define("maxY", DimensionType.MAX_Y, value -> value instanceof Integer && (Integer) value > GENERATIONMINY.get());
        builder.pop();
        
        builder.pop();
    }
}
