package geni.witherutils.base.data.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import geni.witherutils.base.common.init.WUTBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class WitherUtilsLootTables extends LootTableProvider {

    public WitherUtilsLootTables(DataGenerator dataGeneratorIn, CompletableFuture<HolderLookup.Provider> lookupProvider)
    {
        super(dataGeneratorIn.getPackOutput(), Set.of(), List.of(new LootTableProvider.SubProviderEntry(BlockLootTableWUT::new, LootContextParamSets.BLOCK)), lookupProvider);
    }

    @Override
    protected void validate(WritableRegistry<LootTable> writableregistry, ValidationContext validationcontext, ProblemReporter.Collector problemreporter$collector)
    {
    }
    
    private static class BlockLootTableWUT extends BlockLootSubProvider
    {
        public BlockLootTableWUT(HolderLookup.Provider provider)
        {
            super(Set.of(), FeatureFlags.DEFAULT_FLAGS, provider);
        }

        @Override
        protected void generate()
        {
            for (var holder: WUTBlocks.BLOCK_TYPES.getEntries())
            {
                Block b = holder.get();
                if (b.asItem() != Items.AIR)
                {
                    dropSelf(b);
                }
            }
        }

        @Override
        protected Iterable<Block> getKnownBlocks()
        {
            List<Block> l = new ArrayList<>();
            for (var holder: WUTBlocks.BLOCK_TYPES.getEntries())
            {
                if (BuiltInRegistries.ITEM.containsKey(holder.getId()))
                {
                    l.add(holder.get());
                }
            }
            return l;
        }
    }
}
