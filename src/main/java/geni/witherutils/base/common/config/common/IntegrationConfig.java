package geni.witherutils.base.common.config.common;

import net.minecraftforge.common.ForgeConfigSpec;

public class IntegrationConfig {

	public static ForgeConfigSpec.ConfigValue<Boolean> ENABLEINDUSTRIALFOREGOING;
	
    public IntegrationConfig(ForgeConfigSpec.Builder builder)
    {
    	builder.push("modintegration");
    	
	    	builder.push("industrialforegoing");
	    	
				builder.push("fertilizerIF");
				ENABLEINDUSTRIALFOREGOING = builder.comment("If Industrial Foregoing is loaded, the Fertilizer can used in Farmer Block.")
		        		.define("fertilizerIF", true);
		        builder.pop();
		        
		    builder.pop();

	    builder.pop();
    }
}
