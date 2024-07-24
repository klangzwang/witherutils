package geni.witherutils.base.common.config.client;

import net.neoforged.neoforge.common.ModConfigSpec;

public class SoundDebugConfig {

	public static ModConfigSpec.ConfigValue<Boolean> RENDER_SOUNDBOUNCES;
	public static ModConfigSpec.ConfigValue<Boolean> RENDER_OCCLUSION;
	
    public SoundDebugConfig(ModConfigSpec.Builder builder)
    {
    	builder.push("sound");

	    	builder.push("debug");
		    
				builder.push("bounces");
				RENDER_SOUNDBOUNCES = builder.comment("If enabled, the path of the sound will be rendered in game")
		        		.define("render_soundbounces", false);
		        builder.pop();

				builder.push("occlusion");
				RENDER_OCCLUSION = builder.comment("If enabled, occlusion will be visualized in game")
		        		.define("render_occlusion", false);
		        builder.pop();
		        
		    builder.pop();
		    
        builder.pop();
    }
}
