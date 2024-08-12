package geni.witherutils.base.common.init;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import geni.witherutils.api.lib.Names;
import geni.witherutils.base.common.block.anvil.AnvilBlockEntity;
import geni.witherutils.base.common.block.cauldron.CauldronBlockEntity;
import geni.witherutils.base.common.block.collector.CollectorBlockEntity;
import geni.witherutils.base.common.block.creative.CreativeEnergyBlockEntity;
import geni.witherutils.base.common.block.deco.door.metal.MetalDoorBlockEntity;
import geni.witherutils.base.common.block.fakedriver.FakeDriverBlockEntity;
import geni.witherutils.base.common.block.generator.lava.LavaGeneratorBlockEntity;
import geni.witherutils.base.common.block.generator.solar.SolarPanelBlockEntity;
import geni.witherutils.base.common.block.generator.water.WaterGeneratorBlockEntity;
import geni.witherutils.base.common.block.generator.wind.WindGeneratorBlockEntity;
import geni.witherutils.base.common.block.smarttv.SmartTVBlockEntity;
import geni.witherutils.base.common.block.totem.TotemBlockEntity;
import geni.witherutils.base.common.block.xpdrain.XpDrainBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class WUTBlockEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Names.MODID);
    
    public static final Supplier<BlockEntityType<AnvilBlockEntity>> ANVIL = register("anvil", AnvilBlockEntity::new, WUTBlocks.ANVIL);
    public static final Supplier<BlockEntityType<FakeDriverBlockEntity>> FAKE_DRIVER = register("fake_driver", FakeDriverBlockEntity::new, WUTBlocks.FAKE_DRIVER);
    public static final Supplier<BlockEntityType<CreativeEnergyBlockEntity>> CREATIVE_GENERATOR = register("creative_generator", CreativeEnergyBlockEntity::new, WUTBlocks.CREATIVE_GENERATOR);
    public static final Supplier<BlockEntityType<CauldronBlockEntity>> CAULDRON = register("cauldron", CauldronBlockEntity::new, WUTBlocks.CAULDRON);
    public static final Supplier<BlockEntityType<CollectorBlockEntity>> COLLECTOR = register("collector", CollectorBlockEntity::new, WUTBlocks.COLLECTOR);
    public static final Supplier<BlockEntityType<MetalDoorBlockEntity>> METALDOOR = register("metaldoor", MetalDoorBlockEntity::new, WUTBlocks.METALDOOR);
    public static final Supplier<BlockEntityType<TotemBlockEntity>> TOTEM = register("totem", TotemBlockEntity::new, WUTBlocks.TOTEM);
	public static final Supplier<BlockEntityType<XpDrainBlockEntity>> XPDRAIN = register("xpdrain", XpDrainBlockEntity::new, WUTBlocks.XPDRAIN);
    
	public static final Supplier<BlockEntityType<SmartTVBlockEntity>> SMARTTV = register("smarttv", SmartTVBlockEntity::new, WUTBlocks.SMARTTV);
	
    public static final Supplier<BlockEntityType<LavaGeneratorBlockEntity>> LAVA_GENERATOR = register("lava_generator", LavaGeneratorBlockEntity::new, WUTBlocks.LAVA_GENERATOR);
    public static final Supplier<BlockEntityType<WindGeneratorBlockEntity>> WIND_GENERATOR = register("wind_generator", WindGeneratorBlockEntity::new, WUTBlocks.WIND_GENERATOR);
    public static final Supplier<BlockEntityType<WaterGeneratorBlockEntity>> WATER_GENERATOR = register("water_generator", WaterGeneratorBlockEntity::new, WUTBlocks.WATER_GENERATOR);
	public static final Supplier<BlockEntityType<SolarPanelBlockEntity.Basic>> SOLARBASIC = register("solarbasic", SolarPanelBlockEntity.Basic::new, WUTBlocks.SOLARBASIC);
	public static final Supplier<BlockEntityType<SolarPanelBlockEntity.Advanced>> SOLARADV = register("solaradv", SolarPanelBlockEntity.Advanced::new, WUTBlocks.SOLARADV);
	public static final Supplier<BlockEntityType<SolarPanelBlockEntity.Ultra>> SOLARULTRA = register("solarultra", SolarPanelBlockEntity.Ultra::new, WUTBlocks.SOLARULTRA);

    @SafeVarargs
    public static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supplier, Supplier<? extends Block>... blocks)
    {
        return BLOCK_ENTITY_TYPES.register(name, () -> new BlockEntityType<>(supplier, Arrays.stream(blocks).map(Supplier::get).collect(Collectors.toSet()), null));
    }

    private static final List<BlockEntity> DUMMY_BE_LIST = new ArrayList<>();
    public static Stream<BlockEntity> streamBlockEntities()
    {
        if (DUMMY_BE_LIST.isEmpty())
        {
            DUMMY_BE_LIST.addAll(BLOCK_ENTITY_TYPES.getEntries().stream()
                    .flatMap(holder -> holder.get().getValidBlocks().stream()
                            .findFirst()
                            .stream()
                            .map(b -> holder.get().create(BlockPos.ZERO, b.defaultBlockState())))
                    .toList()
            );
        }
        return DUMMY_BE_LIST.stream();
    }
}
