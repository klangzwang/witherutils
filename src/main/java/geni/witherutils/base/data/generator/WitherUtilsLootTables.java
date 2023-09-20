package geni.witherutils.base.data.generator;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.init.WUTBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.registries.ForgeRegistries;

public class WitherUtilsLootTables extends LootTableProvider
{
    public WitherUtilsLootTables(PackOutput output)
    {
        super(output, Set.of(), List.of(new SubProviderEntry(BlockProvider::new, LootContextParamSets.BLOCK)));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext context) {}

    private static class BlockProvider extends BlockLootSubProvider
    {
        protected BlockProvider()
        {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        public void generate()
        {
            this.dropSelf(WUTBlocks.ALLOY_FURNACE.get());
            this.dropSelf(WUTBlocks.ANGEL.get());
            this.dropSelf(WUTBlocks.ANVIL.get());
            this.dropSelf(WUTBlocks.CASE_BIG.get());
            this.dropSelf(WUTBlocks.CASE_SMALL.get());
            this.dropSelf(WUTBlocks.CORE.get());
            this.dropSelf(WUTBlocks.CREATIVE_GENERATOR.get());
            this.dropSelf(WUTBlocks.CREATIVE_TRASH.get());
            this.dropSelf(WUTBlocks.ELECTRO_FURNACE.get());
            this.dropSelf(WUTBlocks.LAVA_GENERATOR.get());
            this.dropSelf(WUTBlocks.PYLON.get());
            this.dropSelf(WUTBlocks.RESERVOIR.get());
            this.dropSelf(WUTBlocks.TANKDRUM.get());
            this.dropSelf(WUTBlocks.SOLARADV.get());
            this.dropSelf(WUTBlocks.SOLARBASIC.get());
            this.dropSelf(WUTBlocks.SOLARCASE.get());
            this.dropSelf(WUTBlocks.SOLARULTRA.get());
            this.dropSelf(WUTBlocks.SOULISHED_BLOCK.get());
            this.dropSelf(WUTBlocks.STAB.get());
            this.dropSelf(WUTBlocks.WATER_GENERATOR.get());
            this.dropSelf(WUTBlocks.WIND_GENERATOR.get());
            this.dropSelf(WUTBlocks.WITHERSTEEL_BLOCK.get());
            this.dropSelf(WUTBlocks.SMARTTV.get());
            this.dropSelf(WUTBlocks.MINERBASIC.get());
            this.dropSelf(WUTBlocks.MINERADV.get());
            this.dropSelf(WUTBlocks.LILLY.get());
            this.dropSelf(WUTBlocks.FLOORSENSOR.get());
            this.dropSelf(WUTBlocks.WALLSENSOR.get());
            this.dropSelf(WUTBlocks.LINES.get());
//            this.dropSelf(WUTBlocks.XPWIRELESS.get());
//            this.dropSelf(WUTBlocks.XPPLATE.get());
            this.dropSelf(WUTBlocks.BRICKSDARK.get());
            this.dropSelf(WUTBlocks.BRICKSLAVA.get());
            this.dropSelf(WUTBlocks.WITHEREARTH.get());
            this.dropSelf(WUTBlocks.TOTEM.get());
            this.dropSelf(WUTBlocks.FLOODGATE.get());
            this.dropSelf(WUTBlocks.RACK_CASE.get());
            this.dropSelf(WUTBlocks.RACK_TERMINAL.get());
            this.dropSelf(WUTBlocks.RACKITEM_CONTROLLER.get());
            this.dropSelf(WUTBlocks.RACKFLUID_CONTROLLER.get());
            
            this.dropSelf(WUTBlocks.ROTTEN_LOG.get());
            this.dropSelf(WUTBlocks.ROTTEN_LEAVES.get());
            this.dropSelf(WUTBlocks.ROTTEN_SAPLING.get());
            
            this.dropSelf(WUTBlocks.GREENHOUSE.get());
            this.dropSelf(WUTBlocks.STEELPOLE.get());
            this.dropSelf(WUTBlocks.STEELPOLEHEAD.get());
            this.dropSelf(WUTBlocks.SLICEDCONCRETEBLACK.get());
            this.dropSelf(WUTBlocks.SLICEDCONCRETEGRAY.get());
            this.dropSelf(WUTBlocks.SLICEDCONCRETEWHITE.get());
            this.dropSelf(WUTBlocks.STEELRAILING.get());
            this.dropSelf(WUTBlocks.COLLECTOR.get());
            this.dropSelf(WUTBlocks.CATWALK.get());
            this.dropSelf(WUTBlocks.SPAWNER.get());
            this.dropSelf(WUTBlocks.FARMER.get());
            this.dropSelf(WUTBlocks.FISHER.get());
            this.dropSelf(WUTBlocks.CAULDRON.get());
            
            this.dropSelf(WUTBlocks.ACTIVATOR.get());
            this.dropSelf(WUTBlocks.CLICKER.get());
            this.dropSelf(WUTBlocks.PLACER.get());
            this.dropSelf(WUTBlocks.SCANNER.get());
            
            this.dropSelf(WUTBlocks.CASED_DOOR.get());
            this.dropSelf(WUTBlocks.CREEP_DOOR.get());
            this.dropSelf(WUTBlocks.LIRON_DOOR.get());
            this.dropSelf(WUTBlocks.STEEL_DOOR.get());
            this.dropSelf(WUTBlocks.STRIPED_DOOR.get());
            this.dropSelf(WUTBlocks.METALDOOR.get());
            
            this.dropSelf(WUTBlocks.CTM_CONCRETE_A.get());
            this.dropSelf(WUTBlocks.CTM_CONCRETE_B.get());
            this.dropSelf(WUTBlocks.CTM_CONCRETE_C.get());
            this.dropSelf(WUTBlocks.CTM_METAL_A.get());
            this.dropSelf(WUTBlocks.CTM_STONE_A.get());
            this.dropSelf(WUTBlocks.CTM_GLASS_A.get());
            this.dropSelf(WUTBlocks.CTM_GLASS_B.get());
            this.dropSelf(WUTBlocks.CTM_GLASS_C.get());
            
            this.dropSelf(WUTBlocks.FERTILIZER_MOLTEN.get());
            this.dropSelf(WUTBlocks.EXPERIENCE_MOLTEN.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks()
        {
            return ForgeRegistries.BLOCKS.getValues().stream().filter(block -> WitherUtils.MODID.equals(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getNamespace())).collect(Collectors.toSet());
        }
    }
}
