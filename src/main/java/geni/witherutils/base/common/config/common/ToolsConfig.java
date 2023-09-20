package geni.witherutils.base.common.config.common;

import net.minecraftforge.common.ForgeConfigSpec;

public class ToolsConfig {
    
    public final ForgeConfigSpec.ConfigValue<Boolean> SOULIE_INK_ENABLED;

    public ToolsConfig(ForgeConfigSpec.Builder builder)
    {
    	builder.push("tools");
    	
	        builder.push("soulieink");
	        SOULIE_INK_ENABLED = builder.comment("Toggle the Soulie Font On/Off.").define("soulieink", true);
	        builder.pop();
        
        builder.pop();
    }
}
