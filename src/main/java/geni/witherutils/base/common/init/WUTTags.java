package geni.witherutils.base.common.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class WUTTags {

    public static class Items
    {
    	public static final TagKey<Item> WRENCHES = ItemTags.create(ResourceLocation.withDefaultNamespace("forge:" + "wrenches"));
        public static final TagKey<Item> WRENCH = ItemTags.create(ResourceLocation.withDefaultNamespace("forge:" + "tools/wrench"));
        public static final TagKey<Item> RODS = ItemTags.create(ResourceLocation.withDefaultNamespace("forge:" + "rods"));
        public static final TagKey<Item> GEARS = ItemTags.create(ResourceLocation.withDefaultNamespace("forge:" + "gears"));
        public static final TagKey<Item> INGOTS = ItemTags.create(ResourceLocation.withDefaultNamespace("forge:" + "ingots"));
        public static final TagKey<Item> NUGGETS = ItemTags.create(ResourceLocation.withDefaultNamespace("forge:" + "nuggets"));
        public static final TagKey<Item> PLATES = ItemTags.create(ResourceLocation.withDefaultNamespace("forge:" + "plates"));
        public static final TagKey<Item> FISHING_RODS = ItemTags.create(ResourceLocation.withDefaultNamespace("forge:" + "fishing_rods"));
//    	public static final TagKey<Item> COLLECTOR_BLACKLIST = TagKey.create(BuiltInRegistries.ITEM.key(), ResourceLocation.withDefaultNamespace("witherutils:" + "blacklists/collector"));
    }

    public static class Blocks
    {
//    	public static final TagKey<Block> WITHER_STEEL_TIER = BlockTags.create(ResourceLocation.withDefaultNamespace("needs_wither_steel"));
//    	public static final TagKey<Block> BREAKER_BLACKLIST = TagUtil.getBlockTag(ResourceLocation.withDefaultNamespace("witherutils:" + "blacklists/breaker"));
    }

    public static class Fluids
    {
//    	public static final TagKey<Fluid> EXPERIENCE = FluidTags.create(ResourceLocation.withDefaultNamespace("forge:" + "fluids/experience"));
//    	public static final TagKey<Fluid> XPJUICE = FluidTags.create(ResourceLocation.withDefaultNamespace("forge:" + "fluids/experience"));
    }
}
