package geni.witherutils.base.data.generator;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import geni.witherutils.api.data.WitherUtilsTags;
import geni.witherutils.api.lib.Names;
import geni.witherutils.base.common.init.WUTItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class WitherUtilsItemTags extends ItemTagsProvider {
	
    public WitherUtilsItemTags(DataGenerator generatorIn, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTagsProvider, ExistingFileHelper existingFileHelper)
    {
        super(generatorIn.getPackOutput(), lookupProvider, blockTagsProvider, Names.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider)
    {
//        copy(WitherUtilsTags.Blocks.SLABS, WitherUtilsTags.Items.SLABS);
//        copy(WitherUtilsTags.Blocks.STAIRS, WitherUtilsTags.Items.STAIRS);
//        copy(WitherUtilsTags.Blocks.WALLS, WitherUtilsTags.Items.WALLS);
//        copy(WitherUtilsTags.Blocks.DOORS, WitherUtilsTags.Items.DOORS);
//        copy(WitherUtilsTags.Blocks.FLUID_TANKS, WitherUtilsTags.Items.FLUID_TANKS);
//        copy(WitherUtilsTags.Blocks.CHESTS, WitherUtilsTags.Items.CHESTS);
//
//        appendToTag(ItemTags.SLABS, WitherUtilsTags.Items.SLABS);
//        appendToTag(ItemTags.STAIRS, WitherUtilsTags.Items.STAIRS);
//        appendToTag(ItemTags.WALLS, WitherUtilsTags.Items.WALLS);
//        appendToTag(ItemTags.DOORS, WitherUtilsTags.Items.DOORS);
//        appendToTag(Tags.Items.CHESTS, WitherUtilsTags.Items.CHESTS);

        addItemsToTag(WitherUtilsTags.Items.WRENCHES, WUTItems.WRENCH);
        addItemsToTag(WitherUtilsTags.Items.GEARS, WUTItems.IRON_GEAR);
    }

    @SafeVarargs
    private void addItemsToTag(TagKey<Item> tag, Supplier<? extends ItemLike>... items)
    {
        tag(tag).add(Arrays.stream(items).map(Supplier::get).map(ItemLike::asItem).toArray(Item[]::new));
    }

    @SuppressWarnings("unused")
	@SafeVarargs
    private void appendToTag(TagKey<Item> tag, TagKey<Item>... toAppend)
    {
        tag(tag).addTags(toAppend);
    }

    @Override
    public String getName()
    {
        return "WitherUtils Item Tags";
    }

}