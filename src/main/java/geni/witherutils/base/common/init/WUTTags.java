package geni.witherutils.base.common.init;

import geni.witherutils.core.common.util.TagUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

public class WUTTags {

    public static class Items
    {
        public static final TagKey<Item> WRENCHES = TagUtil.getItemTag(new ResourceLocation("forge:" + "wrenches"));
        public static final TagKey<Item> WRENCH = TagUtil.getItemTag(new ResourceLocation("forge:" + "tools/wrench"));
        public static final TagKey<Item> RODS = TagUtil.getItemTag(new ResourceLocation("forge:" + "rods"));
        public static final TagKey<Item> GEARS = TagUtil.getItemTag(new ResourceLocation("forge:" + "gears"));
        public static final TagKey<Item> INGOTS = TagUtil.getItemTag(new ResourceLocation("forge:" + "ingots"));
        public static final TagKey<Item> NUGGETS = TagUtil.getItemTag(new ResourceLocation("forge:" + "nuggets"));
        public static final TagKey<Item> PLATES = TagUtil.getItemTag(new ResourceLocation("forge:" + "plates"));
        public static final TagKey<Item> FISHING_RODS = TagUtil.getItemTag(new ResourceLocation("forge:" + "fishing_rods"));
    	public static final TagKey<Item> COLLECTOR_BLACKLIST = TagUtil.getItemTag(new ResourceLocation("witherutils:" + "blacklists/collector"));
    }

    public static class Blocks
    {
    	public static final TagKey<Block> WITHER_STEEL_TIER = BlockTags.create(new ResourceLocation("needs_wither_steel"));
    	public static final TagKey<Block> BREAKER_BLACKLIST = TagUtil.getBlockTag(new ResourceLocation("witherutils:" + "blacklists/breaker"));
    }

    public static class EntityTypes
    {
    }

    public static class Fluids
    {
    	public static final TagKey<Fluid> EXPERIENCE = TagKey.create(ForgeRegistries.FLUIDS.getRegistryKey(), new ResourceLocation("forge", "experience"));
    }
}
