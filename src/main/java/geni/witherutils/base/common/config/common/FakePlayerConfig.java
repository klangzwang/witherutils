package geni.witherutils.base.common.config.common;

import net.minecraftforge.common.ForgeConfigSpec;

public class FakePlayerConfig {

    public static ForgeConfigSpec.ConfigValue<Integer> FAKEPLAYERSTAMINA;
    
    public FakePlayerConfig(ForgeConfigSpec.Builder builder)
    {
    	builder.push("fakeplayer");
    	
	    	builder.push("basics");
	    	
				builder.push("stamina");
				FAKEPLAYERSTAMINA = builder
		                .comment("The Countdown in Ticks before the FakePlayer can act again.")
		                .define("stamina", 120);
		        builder.pop();
        
		    builder.pop();

        builder.pop();
    }
}
