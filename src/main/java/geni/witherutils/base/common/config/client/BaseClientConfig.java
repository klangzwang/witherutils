package geni.witherutils.base.common.config.client;

import net.neoforged.neoforge.common.ModConfigSpec;

public class BaseClientConfig {
    
    public final SoundDebugConfig SOUNDDEBUG;
	public final EffectsConfig EFFECTS;
	
    public BaseClientConfig(ModConfigSpec.Builder builder)
    {
    	SOUNDDEBUG = new SoundDebugConfig(builder);
    	EFFECTS = new EffectsConfig(builder);
    }
}
