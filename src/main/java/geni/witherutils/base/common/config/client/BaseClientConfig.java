package geni.witherutils.base.common.config.client;

import net.neoforged.neoforge.common.ModConfigSpec;

public class BaseClientConfig {
    
	public final EffectsConfig EFFECTS;
	
    public BaseClientConfig(ModConfigSpec.Builder builder)
    {
    	EFFECTS = new EffectsConfig(builder);
    }
}
