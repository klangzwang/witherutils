package geni.witherutils.base.common.config.common;

import net.neoforged.neoforge.common.ModConfigSpec;

public class BaseCommonConfig {

    public final ItemsConfig ITEMS;
    public final LootConfig LOOT;
    public final WitherSteelConfig WITHERSTEEL;
    public final GeneratorConfig GENERATOR;
    public final RestrictionConfig RESTRICTION;
    public final SaturationConfig SATURATION;
    public final BlocksConfig BLOCKS;
    public final SolarConfig SOLAR;
    
	public BaseCommonConfig(ModConfigSpec.Builder builder)
	{
        ITEMS = new ItemsConfig(builder);
        LOOT = new LootConfig(builder);
        WITHERSTEEL = new WitherSteelConfig(builder);
        GENERATOR = new GeneratorConfig(builder);
        RESTRICTION = new RestrictionConfig(builder);
        SATURATION = new SaturationConfig(builder);
        BLOCKS = new BlocksConfig(builder);
        SOLAR = new SolarConfig(builder);
	}
}
