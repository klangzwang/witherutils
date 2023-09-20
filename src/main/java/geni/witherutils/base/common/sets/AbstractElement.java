package geni.witherutils.base.common.sets;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public abstract class AbstractElement {

	abstract void clientTick(Level level, BlockPos pos);
	abstract void serverTick(ServerLevel level, BlockPos pos);
}
