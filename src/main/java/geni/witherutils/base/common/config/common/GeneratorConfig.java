package geni.witherutils.base.common.config.common;

import geni.witherutils.base.common.config.common.generator.LavaGeneratorConfig;
import geni.witherutils.base.common.config.common.generator.WaterGeneratorConfig;
import geni.witherutils.base.common.config.common.generator.WindGeneratorConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class GeneratorConfig {
    
	public final LavaGeneratorConfig LAVAGENERATOR;
	public final WaterGeneratorConfig WATERGENERATOR;
	public final WindGeneratorConfig WINDGENERATOR;
	
    public GeneratorConfig(ModConfigSpec.Builder builder)
    {
    	LAVAGENERATOR = new LavaGeneratorConfig(builder);
	    WATERGENERATOR = new WaterGeneratorConfig(builder);
	    WINDGENERATOR = new WindGeneratorConfig(builder);
    }
}
