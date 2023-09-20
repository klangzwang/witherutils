package geni.witherutils.base.common.config.common;

import net.minecraftforge.common.ForgeConfigSpec;

public class BaseCommonConfig {
	
    public final ItemsConfig ITEMS;
    public final ToolsConfig TOOLS;
    public final WitherSteelConfig WITHERSTEEL;
    public final GeneratorConfig GENERATOR;
    public final BatteryConfig BATTERY;
    public final BlocksConfig BLOCKS;
    public final SolarConfig SOLAR;
    public final LootConfig LOOT;
    public final FakePlayerConfig FAKE;
    public final IntegrationConfig INTEGRATION;
    
    public BaseCommonConfig(ForgeConfigSpec.Builder builder)
    {
        ITEMS = new ItemsConfig(builder);
        TOOLS = new ToolsConfig(builder);
        WITHERSTEEL = new WitherSteelConfig(builder);
        GENERATOR = new GeneratorConfig(builder);
        BATTERY = new BatteryConfig(builder);
        BLOCKS = new BlocksConfig(builder);
        SOLAR = new SolarConfig(builder);
        LOOT = new LootConfig(builder);
        FAKE = new FakePlayerConfig(builder);
        INTEGRATION = new IntegrationConfig(builder);
    }
}
