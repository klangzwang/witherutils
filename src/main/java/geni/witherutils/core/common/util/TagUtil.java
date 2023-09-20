package geni.witherutils.core.common.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.tags.ITag;
import net.minecraftforge.registries.tags.ITagManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TagUtil {

    public static <T> boolean hasTag(IForgeRegistry<T> registry, T type, TagKey<T> tag)
    {
        return registry.tags().getTag(tag).contains(type);
    }

    public static ITagManager<Block> getAllBlockTags()
    {
        return ForgeRegistries.BLOCKS.tags();
    }

    public static ITagManager<Item> getAllItemTags()
    {
        return ForgeRegistries.ITEMS.tags();
    }

    public static ITagManager<Fluid> getAllFluidTags()
    {
        return ForgeRegistries.FLUIDS.tags();
    }

    @SuppressWarnings("unchecked")
    public static <T> Collection<T> getAllEntries(IForgeRegistry<T> registry, TagKey<T>... tags)
    {
        if (tags.length == 0)
            return Collections.emptyList();
        if (tags.length == 1)
            return registry.tags().getTag(tags[0]).stream().toList();
        List<T> list = new ArrayList<>();
        for (TagKey<T> tag : tags) {
            list.addAll(registry.tags().getTag(tag).stream().toList());
        }
        return list;
    }

    public static <T> Collection<T> getAllEntries(IForgeRegistry<T> registry, TagKey<T> tag)
    {
        return registry.tags().getTag(tag).stream().toList();
    }

    public static <T> TagKey<T> getOrCreateTag(IForgeRegistry<T> registry, ResourceLocation resourceLocation)
    {
        return registry.tags().createTagKey(resourceLocation);
    }

    public static TagKey<Item> getItemTag(ResourceLocation resourceLocation)
    {
        return ForgeRegistries.ITEMS.tags().stream().filter(items -> items.getKey().location().equals(resourceLocation)).map(ITag::getKey).findFirst().orElse(getOrCreateTag(ForgeRegistries.ITEMS, resourceLocation));
    }

    public static TagKey<Block> getBlockTag(ResourceLocation resourceLocation)
    {
        return ForgeRegistries.BLOCKS.tags().stream().filter(items -> items.getKey().location().equals(resourceLocation)).map(ITag::getKey).findFirst().orElse(getOrCreateTag(ForgeRegistries.BLOCKS, resourceLocation));
    }

    public static TagKey<EntityType<?>> getEntityTypeTag(ResourceLocation resourceLocation)
    {
        return ForgeRegistries.ENTITY_TYPES.tags().stream().filter(items -> items.getKey().location().equals(resourceLocation)).map(ITag::getKey).findFirst().orElse(getOrCreateTag(ForgeRegistries.ENTITY_TYPES, resourceLocation));
    }

    public static TagKey<Fluid> getFluidTag(ResourceLocation resourceLocation)
    {
        return ForgeRegistries.FLUIDS.tags().stream().filter(items -> items.getKey().location().equals(resourceLocation)).map(ITag::getKey).findFirst().orElse(getOrCreateTag(ForgeRegistries.FLUIDS, resourceLocation));
    }
}