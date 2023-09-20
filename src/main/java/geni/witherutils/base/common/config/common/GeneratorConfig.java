package geni.witherutils.base.common.config.common;

import geni.witherutils.base.common.config.common.generator.LavaGeneratorConfig;
import geni.witherutils.base.common.config.common.generator.WaterGeneratorConfig;
import geni.witherutils.base.common.config.common.generator.WindGeneratorConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class GeneratorConfig {
    
	public final WaterGeneratorConfig WATERGENERATOR;
	public final LavaGeneratorConfig LAVAGENERATOR;
	public final WindGeneratorConfig WINDGENERATOR;
	
    public GeneratorConfig(ForgeConfigSpec.Builder builder)
    {
	    WATERGENERATOR = new WaterGeneratorConfig(builder);
	    LAVAGENERATOR = new LavaGeneratorConfig(builder);
	    WINDGENERATOR = new WindGeneratorConfig(builder);
    }
}
