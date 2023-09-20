package geni.witherutils.api.teleport;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class TeleportEntityEvent extends EntityEvent {

	private @Nonnull BlockPos targetPos;
	private int dimension;

	public TeleportEntityEvent(@Nonnull Entity entity, @Nonnull BlockPos pos)
	{
		super(entity);
		this.targetPos = pos;
		this.setDimension(dimension);
	}

	public @Nonnull BlockPos getTarget()
	{
		return targetPos;
	}
	public void setTargetPos(@Nonnull BlockPos target)
	{
		this.targetPos = target;
	}
	public int getDimension()
	{
		return dimension;
	}
	public void setDimension(int dimension)
	{
		this.dimension = dimension;
	}
}
