package geni.witherutils.base.common.config.common;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import net.neoforged.neoforge.common.ModConfigSpec;

public class LootConfig {

    public static ModConfigSpec.ConfigValue<Double> LILLYDROPCHANCE;
	public static ModConfigSpec.ConfigValue<Double> SOULORBDROPCHANCE;
	public static ModConfigSpec.ConfigValue<List<? extends String>> SOULORBDROPLIST;

	private static final Predicate<Object> STRING_PREDICATE = s -> s instanceof String;

    public LootConfig(ModConfigSpec.Builder builder)
    {
    	builder.push("loot");
    	
	    	builder.push("lilly");
	    	
				builder.push("enderlillychance");
				LILLYDROPCHANCE = builder
		                .comment("The Chance Modifier, EnderMan drops a EnderLilly")
		                .defineInRange("enderlillychance", 0.1, 0.01, 1.0);
		        builder.pop();
		        
		    builder.pop();

	    	builder.push("soulorbdimension");
	    	
				builder.push("dropatdimension");
				SOULORBDROPLIST = builder
						.comment("The Dimensions, the SoulOrb will drop from LivingEntitys when they killed by the Player"
								+ " Ex: [\"minecraft:overworld\", \"minecraft:the_nether\", \"minecraft:the_end\"]")
						.defineList("dropatdimension", Collections.singletonList("minecraft:nether"), STRING_PREDICATE);
		    	builder.pop();

	    	builder.push("soulorbchance");
	    	
				builder.push("dropwithchance");
				SOULORBDROPCHANCE = builder
		                .comment("The Chance Modifier, LivingEntity drops a SoulOrb.")
		                .defineInRange("dropwithchance", 0.1, 0.75, 1.0);
		        builder.pop();
		        
		    builder.pop();
	
        builder.pop();
    }
}
