package geni.witherutils.base.data.generator;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import geni.witherutils.api.data.WitherUtilsTags;
import geni.witherutils.api.lib.Names;
import geni.witherutils.base.common.init.WUTFluids;

public class WitherUtilsFluidTags extends FluidTagsProvider {
	
    public WitherUtilsFluidTags(DataGenerator generatorIn, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper)
    {
        super(generatorIn.getPackOutput(), lookupProvider, Names.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider)
    {
        createTag(WitherUtilsTags.Fluids.BLUELIMBO, WUTFluids.BLUELIMBO);
        createTag(WitherUtilsTags.Fluids.COLDSLUSH, WUTFluids.COLDSLUSH);
        createTag(WitherUtilsTags.Fluids.EXPERIENCE, WUTFluids.EXPERIENCE);
        createTag(WitherUtilsTags.Fluids.FERTILIZER, WUTFluids.FERTILIZER);
        createTag(WitherUtilsTags.Fluids.PORTIUM, WUTFluids.PORTIUM);
        createTag(WitherUtilsTags.Fluids.REDRESIN, WUTFluids.REDRESIN);
        createTag(WitherUtilsTags.Fluids.SOULFUL, WUTFluids.SOULFUL);
        createTag(WitherUtilsTags.Fluids.WITHERWATER, WUTFluids.WITHERWATER);
    }

    @Override
    public String getName()
    {
        return "PneumaticCraft Fluid Tags";
    }

    @SafeVarargs
    private <T> T[] resolveAll(IntFunction<T[]> creator, Supplier<? extends T>... suppliers)
    {
        return Arrays.stream(suppliers).map(Supplier::get).toArray(creator);
    }

    @SafeVarargs
    private void createTag(TagKey<Fluid> tag, Supplier<? extends Fluid>... blocks)
    {
        tag(tag).add(resolveAll(Fluid[]::new, blocks));
    }

    @SafeVarargs
    private void appendToTag(TagKey<Fluid> tag, TagKey<Fluid>... toAppend)
    {
        tag(tag).addTags(toAppend);
    }

    @SuppressWarnings("unused")
	@SafeVarargs
    private void createAndAppend(TagKey<Fluid> tag, TagKey<Fluid> to, Supplier<? extends Fluid>... fluids)
    {
        createTag(tag, fluids);
        appendToTag(to, tag);
    }
}
