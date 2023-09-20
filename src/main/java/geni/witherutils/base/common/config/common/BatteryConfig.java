package geni.witherutils.base.common.config.common;

import net.minecraftforge.common.ForgeConfigSpec;

public class BatteryConfig {

	public static ForgeConfigSpec.ConfigValue<Integer> PYLONCACHEDENERGY;
	public static ForgeConfigSpec.ConfigValue<Long> CORECAPACITY;
	
    public BatteryConfig(ForgeConfigSpec.Builder builder)
    {
    	builder.push("battery");
    	
	    	builder.push("core");
	    	
				builder.push("coreCapacity");
				CORECAPACITY = builder.comment("The Energy Amount the Core can hold.")
		        		.defineInRange("coreCapacity", 1000000000L, 0L, 2000000000L);
		        builder.pop();
	    	
		    builder.pop();
	
	    	builder.push("pylon");
	    	
				builder.push("pylonCapacity");
				PYLONCACHEDENERGY = builder.comment("The Energy Amount the Pylon can hold.")
		        		.defineInRange("pylonCapacity", 1, 0, Integer.MAX_VALUE);
		        builder.pop();
	
		    builder.pop();
		        
	    	builder.push("stab");
		    builder.pop();
		    
        builder.pop();
    }
}
