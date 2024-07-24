package geni.witherutils.base.common.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import geni.witherutils.api.lib.Names;
import geni.witherutils.base.common.block.anvil.AnvilBlockEntity;
import geni.witherutils.base.common.block.creative.CreativeEnergyBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class LogicalBlockEntities {
	
	/*
	 * 
	 * BLOCK ENTITY
	 * 
	 */
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Names.MODID);
    
    public static final Supplier<BlockEntityType<AnvilBlockEntity>> ANVIL = register("anvil", AnvilBlockEntity::new, LogicalBlocks.ANVIL);
    public static final Supplier<BlockEntityType<CreativeEnergyBlockEntity>> CREATIVEENERGY = register("creativeenergy", CreativeEnergyBlockEntity::new, LogicalBlocks.CREATIVEENERGY);
    
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
