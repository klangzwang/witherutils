package geni.witherutils.base.data.generator;

import java.util.concurrent.CompletableFuture;

import geni.witherutils.WitherUtils;
import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.base.common.init.WUTTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

@SuppressWarnings("unused")
public class WitherUtilsItemTags extends ItemTagsProvider {

    public static final TagKey<Item> WRENCHES = forgeTag("wrenches");
    public static final TagKey<Item> WRENCH = forgeTag("tools/wrench");
    public static final TagKey<Item> RODS = forgeTag("rods");
    public static final TagKey<Item> GEARS = forgeTag("gears");
    public static final TagKey<Item> INGOTS = forgeTag("ingots");
    public static final TagKey<Item> NUGGETS = forgeTag("nuggets");
    public static final TagKey<Item> PLATES = forgeTag("plates");
    public static final TagKey<Item> FISHING_RODS = forgeTag("fishing_rods");
    
    private static TagKey<Item> witherUtilsTag(String name)
    {
        return ItemTags.create(WitherUtilsRegistry.loc(name));
    }
    private static TagKey<Item> minecraftTag(String name)
    {
        return ItemTags.create(WitherUtilsRegistry.loc(name));
    }
    private static TagKey<Item> forgeTag(String name)
    {
        return ItemTags.create(WitherUtilsRegistry.loc(name));
    }

    public WitherUtilsItemTags(PackOutput generator, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, ExistingFileHelper helper)
    {
        super(generator, lookupProvider, blockTags, WitherUtilsRegistry.MODID, helper);
    }
    
    @Override
    protected void addTags(Provider provider)
    {
        addForgeTags(provider);
    }
    
    protected void addWitherUtilsTags(Provider provider)
    {
    }
    protected void addMinecraftTags(Provider provider)
    {
    }
    protected void addForgeTags(Provider provider)
    {
        tag(WRENCHES)
                .add(WUTItems.WRENCH.get());
        tag(WRENCH)
                .add(WUTItems.WRENCH.get());
//        tag(RODS)
//                .add(WUTItems.IRON_ROD.get());
//        tag(GEARS)
//                .add(WUTItems.IRON_GEAR.get())
//                .add(WUTItems.WITHERSTEEL_GEAR.get());
//        tag(INGOTS)
//                .add(WUTItems.WITHERSTEEL_INGOT.get())
//                .add(WUTItems.SOULISHED_INGOT.get());
//        tag(NUGGETS)
//                .add(WUTItems.WITHERSTEEL_NUGGET.get())
//                .add(WUTItems.SOULISHED_NUGGET.get());
//        tag(PLATES)
//                .add(WUTItems.IRON_PLATE.get())
//                .add(WUTItems.WITHERSTEEL_PLATE.get());
        tag(FISHING_RODS)
		        .add(Items.FISHING_ROD);
        
//        tag(WUTTags.Items.COLLECTOR_BLACKLIST)
//				.add(Items.WITHER_SPAWN_EGG);
    }

    @Override
    public String getName()
    {
        return "WitherUtils ItemTags";
    }
}
