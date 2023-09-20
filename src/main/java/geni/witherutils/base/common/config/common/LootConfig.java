package geni.witherutils.base.common.config.common;

import net.minecraftforge.common.ForgeConfigSpec;

public class LootConfig {

    public static ForgeConfigSpec.ConfigValue<Double> LILLYDROPCHANCE;
	
    public LootConfig(ForgeConfigSpec.Builder builder)
    {
    	builder.push("loot");
    	
	    	builder.push("lilly");
	    	
				builder.push("enderlillychance");
				LILLYDROPCHANCE = builder
		                .comment("The Chance Modifier, EnderMan drops a EnderLilly")
		                .defineInRange("enderlillychance", 0.1, 0.01, 1.0);
		        builder.pop();
		        
		    builder.pop();
	
        builder.pop();
    }
}
