package geni.witherutils.base.common.config.common;

import net.neoforged.neoforge.common.ModConfigSpec;

public class BaseCommonConfig {

    public final ItemsConfig ITEMS;
    public final LootConfig LOOT;
    public final WitherSteelConfig WITHERSTEEL;
    
	public BaseCommonConfig(ModConfigSpec.Builder builder)
	{
        ITEMS = new ItemsConfig(builder);
        LOOT = new LootConfig(builder);
        WITHERSTEEL = new WitherSteelConfig(builder);
	}
}
