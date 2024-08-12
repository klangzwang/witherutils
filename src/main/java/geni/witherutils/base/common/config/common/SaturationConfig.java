package geni.witherutils.base.common.config.common;

import net.neoforged.neoforge.common.ModConfigSpec;

public class SaturationConfig {

    public static ModConfigSpec.ConfigValue<Double> ANVIL_SATURATION_COST;
    
    public SaturationConfig(ModConfigSpec.Builder builder)
    {
    	builder.push("saturation");
    	
	    	builder.push("anvil");

				builder.push("anvilsaturation");
				ANVIL_SATURATION_COST = builder
		                .comment("The Saturation Points, the Anvil Action cost, when you craft something with the Hammer.")
		                .define("anvilsaturation", 3.0d);
		        builder.pop();
		        
		    builder.pop();
	
        builder.pop();
    }
}
