package geni.witherutils.api.data;

import geni.witherutils.api.lib.Names;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class WitherUtilsTags {
	
    public static class Blocks extends WitherUtilsTags
    {
        public static final TagKey<Block> SLABS = modTag("slabs");
        public static final TagKey<Block> STAIRS = modTag("stairs");
        public static final TagKey<Block> DOORS = modTag("doors");
        public static final TagKey<Block> WALLS = modTag("walls");
        public static final TagKey<Block> FLUID_TANKS = modTag("fluid_tanks");
        public static final TagKey<Block> CHESTS = modTag("chests");
        public static final TagKey<Block> CUTTER = modTag("cutter");
        
        static TagKey<Block> tag(String modid, String name)
        {
            return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(modid, name));
        }

        static TagKey<Block> modTag(String name)
        {
            return tag(Names.MODID, name);
        }

        static TagKey<Block> neoforgeTag(String name)
        {
            return tag("neoforge", name);
        }

        static TagKey<Block> commonTag(String name)
        {
            return tag("c", name);
        }
    }

    public static class Items extends WitherUtilsTags
    {
        public static final TagKey<Item> SLABS = modTag("slabs");
        public static final TagKey<Item> STAIRS = modTag("stairs");
        public static final TagKey<Item> DOORS = modTag("doors");
        public static final TagKey<Item> WALLS = modTag("walls");
        public static final TagKey<Item> FLUID_TANKS = modTag("fluid_tanks");
        public static final TagKey<Item> CHESTS = modTag("chests");

        public static final TagKey<Item> GEARS = commonTag("gears");
        public static final TagKey<Item> WRENCHES = commonTag("tools/wrench");

        static TagKey<Item> tag(String modid, String name)
        {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(modid, name));
        }

        static TagKey<Item> modTag(String name)
        {
            return tag(Names.MODID, name);
        }

        static TagKey<Item> neoforgeTag(String name)
        {
            return tag("neoforge", name);
        }

        static TagKey<Item> commonTag(String name)
        {
            return tag("c", name);
        }
    }

    public static class Fluids extends WitherUtilsTags
    {
        public static final TagKey<Fluid> BLUELIMBO = commonTag("bluelimbo");
        public static final TagKey<Fluid> COLDSLUSH = commonTag("coldslush");
        public static final TagKey<Fluid> EXPERIENCE = commonTag("experience");
        public static final TagKey<Fluid> FERTILIZER = commonTag("fertilizer");
        public static final TagKey<Fluid> PORTIUM = commonTag("portium");
        public static final TagKey<Fluid> REDRESIN = commonTag("redresin");
        public static final TagKey<Fluid> SOULFUL = commonTag("soulful");
        public static final TagKey<Fluid> WITHERWATER = commonTag("witherwater");

        static TagKey<Fluid> tag(String modid, String name)
        {
            return TagKey.create(Registries.FLUID, ResourceLocation.fromNamespaceAndPath(modid, name));
        }

        static TagKey<Fluid> modTag(String name)
        {
            return tag(Names.MODID, name);
        }

        public static TagKey<Fluid> commonTag(String name)
        {
            return tag("c", name);
        }
    }
}