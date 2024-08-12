package geni.witherutils.base.common.config.common;

import net.neoforged.neoforge.common.ModConfigSpec;

public class RestrictionConfig {

    public static ModConfigSpec.ConfigValue<Boolean> FAKEPLAYERALLOWED;
    public static ModConfigSpec.ConfigValue<Integer> MAXBLOCKSALLOWED;

    public RestrictionConfig(ModConfigSpec.Builder builder)
    {
    	builder.push("restriction");
    	
	    	builder.push("fakeplayers");
	    	
				builder.push("fakeplayersallowed");
				FAKEPLAYERALLOWED = builder
		                .comment("Are Blocks with FakePlayers allowed?")
		                .define("fakeplayersallowed", true);
		        builder.pop();
		        
		    builder.pop();

	    	builder.push("maxblocks");
	    	
				builder.push("maxblocksallowed");
				MAXBLOCKSALLOWED = builder
						.comment("How many Blocks with Fake Players are allowed. Prevents too many etc.")
						.defineInRange("maxblocksallowed", 18, 0, 99);
		    	builder.pop();
		        
		    builder.pop();
	
        builder.pop();
    }
}
