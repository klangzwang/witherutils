package geni.witherutils.base.data.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.api.lib.Names;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class WitherUtilsLootTables extends LootTableProvider
{
    public WitherUtilsLootTables(DataGenerator dataGeneratorIn, CompletableFuture<HolderLookup.Provider> lookupProvider)
    {
        super(dataGeneratorIn.getPackOutput(), Set.of(), List.of(new LootTableProvider.SubProviderEntry(BlockProvider::new, LootContextParamSets.BLOCK)), lookupProvider);
    }

    @Override
    protected void validate(WritableRegistry<LootTable> writableregistry, ValidationContext validationcontext, ProblemReporter.Collector problemreporter$collector)
    {
    }

    @SuppressWarnings("unused")
	private static ResourceKey<LootTable> lootResourceKey(String id)
    {
        return ResourceKey.create(Registries.LOOT_TABLE, WitherUtilsRegistry.loc(id));
    }

    private static class BlockProvider extends BlockLootSubProvider
    {
        public BlockProvider(HolderLookup.Provider provider)
        {
            super(Set.of(), FeatureFlags.DEFAULT_FLAGS, provider);
        }

        @Override
        protected void generate()
        {
            for (var entry : BuiltInRegistries.BLOCK.entrySet())
            {
                var id = entry.getKey();
                if (id.location().getNamespace().equals(Names.MODID))
                {
                    var block = entry.getValue();
                    dropSelf(block);
                }
            }
        }

        @Override
        protected Iterable<Block> getKnownBlocks()
        {
            List<Block> l = new ArrayList<>();
            for (var entry : BuiltInRegistries.BLOCK.entrySet())
            {
                var id = entry.getKey();
                if (id.location().getNamespace().equals(Names.MODID))
                {
                    var block = entry.getValue();
                    l.add(block);
                }
            }
            return l;
        }
    }
}
