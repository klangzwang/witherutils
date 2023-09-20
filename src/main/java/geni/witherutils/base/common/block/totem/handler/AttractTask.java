package geni.witherutils.base.common.block.totem.handler;

import javax.annotation.Nonnull;

import geni.witherutils.base.common.block.totem.TotemBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.FakePlayer;

class AttractTask extends Goal {

	protected final @Nonnull Mob mob;
	protected final @Nonnull BlockPos coord;
	protected final @Nonnull FakePlayer target;
	private int updatesSincePathing;

	AttractTask(@Nonnull Mob mob, @Nonnull FakePlayer target, @Nonnull BlockPos coord)
	{
		this.mob = mob;
		this.coord = coord;
		this.target = target;
	}

	@Override
	public boolean canUse()
	{
		return canContinueToUse();
	}

	@Override
	public boolean canContinueToUse()
	{
		boolean res = false;
		BlockEntity te = mob.level().getBlockEntity(coord);
		if (te instanceof TotemBlockEntity)
		{
			TotemBlockEntity attractor = (TotemBlockEntity) te;
			res = attractor.canAct() && attractor.canAttract(mob);
		}
		return res;
	}
	
	@Override
	public void tick()
	{
		if (--updatesSincePathing <= 0)
		{
			doUpdateTask();
			updatesSincePathing = 20;
		}
	}
	
	@Override
	public boolean isInterruptable()
	{
		return true;
	}
	
	@Override
	public void stop()
	{
		super.stop();
	}
	@Override
	public void start()
	{
		super.start();
		updatesSincePathing = 0;
	}

	protected void doUpdateTask()
	{
		int speed = 1;

		boolean res = mob.getNavigation().moveTo(target, speed);
		if (!res)
		{
			for (Direction dir : Direction.values())
			{
				if (!res) {
					res = mob.getNavigation().moveTo(target.position().x + dir.getStepX(), target.position().y + dir.getStepY(), target.position().z + dir.getStepZ(), speed);
				}
			}
		}
	}

}