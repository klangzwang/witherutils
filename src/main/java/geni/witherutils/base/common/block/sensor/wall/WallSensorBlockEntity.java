package geni.witherutils.base.common.block.sensor.wall;

import geni.witherutils.base.common.base.WitherMachineBlockEntity;
import geni.witherutils.base.common.init.WUTBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class WallSensorBlockEntity extends WitherMachineBlockEntity {

	public WallSensorBlockEntity(BlockPos pos, BlockState state)
	{
		super(WUTBlockEntityTypes.WALLSENSOR.get(), pos, state);
	}
}
