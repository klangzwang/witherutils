package geni.witherutils.base.common.init;

import geni.witherutils.api.WitherUtilsRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.Fluid;

public class WUTTags {

    public static class Blocks
    {
    }
    public static class Items
    {
        public static final TagKey<Item> WRENCHES = ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "wrenches"));
        public static final TagKey<Item> WRENCH = ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "tools/wrench"));
        public static final TagKey<Item> RODS = ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "rods"));
        public static final TagKey<Item> GEARS = ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "gears"));
        public static final TagKey<Item> INGOTS = ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "ingots"));
        public static final TagKey<Item> NUGGETS = ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "nuggets"));
        public static final TagKey<Item> PLATES = ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "plates"));
        public static final TagKey<Item> FISHING_RODS = ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "fishing_rods"));
    	public static final TagKey<Item> COLLECTOR_BLACKLIST = ItemTags.create(ResourceLocation.fromNamespaceAndPath("witherutils", "blacklists/collector"));
    }
    public static class Fluids
    {
    	public static final TagKey<Fluid> EXPERIENCE = FluidTags.create(ResourceLocation.fromNamespaceAndPath("forge", "fluids/experience"));
    	public static final TagKey<Fluid> XPJUICE = FluidTags.create(ResourceLocation.fromNamespaceAndPath("forge", "fluids/experience"));
    }
    public static class Entities
    {
    	public static final TagKey<EntityType<?>> NO_SPAWN = TagKey.create(Registries.ENTITY_TYPE, WitherUtilsRegistry.loc("no_spawn"));
        public static final TagKey<EntityType<?>> NO_DIRT_SPAWN = TagKey.create(Registries.ENTITY_TYPE, WitherUtilsRegistry.loc("no_dirt_spawn"));
    }
    public static class Biomes
    {
        public static final TagKey<Biome> PASSIVE_OVERRIDE = TagKey.create(Registries.BIOME, WitherUtilsRegistry.loc("passive_override"));
        public static final TagKey<Biome> HOSTILE_OVERRIDE = TagKey.create(Registries.BIOME, WitherUtilsRegistry.loc("hostile_override"));
    }
}
