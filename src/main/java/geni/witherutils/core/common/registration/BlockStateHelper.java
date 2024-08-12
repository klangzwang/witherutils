package geni.witherutils.core.common.registration;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class BlockStateHelper {

    private BlockStateHelper() {
    }

    public static final BooleanProperty storageProperty = BooleanProperty.create("storage");
    public static final EnumProperty<FluidLogType> FLUID_LOGGED = EnumProperty.create("fluid_logged", FluidLogType.class);

    public static final BlockBehaviour.StatePredicate NEVER_PREDICATE = (state, world, pos) -> false;
    public static final BlockBehaviour.StatePredicate ALWAYS_PREDICATE = (state, world, pos) -> true;
}