package geni.witherutils.base.common.config.client;

import net.neoforged.neoforge.common.ModConfigSpec;

public class EffectsConfig {

	public static ModConfigSpec.ConfigValue<Boolean> CAN_PARTICLES;
	public static ModConfigSpec.ConfigValue<Boolean> CAN_SOUNDS;
	
    public EffectsConfig(ModConfigSpec.Builder builder)
    {
    	builder.push("effects");

	    	builder.push("particlessounds");
		    
				builder.push("particles");
				CAN_PARTICLES = builder.comment("If enabled, the particles will be rendered in game")
		        		.define("can_particles", true);
		        builder.pop();

				builder.push("sounds");
				CAN_SOUNDS = builder.comment("If enabled, sounds can be heared in game")
		        		.define("can_sounds", true);
		        builder.pop();
		        
		    builder.pop();
		    
        builder.pop();
    }
}
