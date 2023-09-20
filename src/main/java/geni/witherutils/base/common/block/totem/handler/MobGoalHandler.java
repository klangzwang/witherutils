package geni.witherutils.base.common.block.totem.handler;

import javax.annotation.Nonnull;

import geni.witherutils.base.common.block.totem.TotemBlockEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;

//@EventBusSubscriber(modid = WitherUtils.MODID)
public class MobGoalHandler implements IMobAttractionHandler {
	
	@Override
	public @Nonnull State canAttract(TotemBlockEntity attractor, LivingEntity entity)
	{
		if (entity instanceof PathfinderMob)
		{
			if (((Mob) entity).getTarget() == attractor.getFakePlayer())
			{
				return State.ALREADY_ATTRACTING;
			}
			return State.CAN_ATTRACT;
		}
		return State.CANNOT_ATTRACT;
    }

	@Override
	public void startAttracting(TotemBlockEntity attractor, LivingEntity entity)
	{
		((Mob) entity).setTarget(attractor.getFakePlayer());
	}

	@Override
	public void tick(TotemBlockEntity attractor, LivingEntity entity)
	{
	}

	@Override
	public void release(TotemBlockEntity attractor, LivingEntity entity)
	{
	}
	
//	@SubscribeEvent
//	public void onMonsterAppear(EntityJoinLevelEvent event) {
//		Entity e = event.getEntity();
//		if(e instanceof Monster monster && !(e instanceof PatrollingMonster) && e.canChangeDimensions() && e.isAlive()) {
//			boolean alreadySetUp = monster.goalSelector.getAvailableGoals().stream().anyMatch((goal) -> goal.getGoal() instanceof RunAwayFromPikesGoal);
//
//			if (!alreadySetUp)
//				monster.goalSelector.addGoal(3, new RunAwayFromPikesGoal(monster, (float) pikeRange, 1.0D, 1.2D));
//		}
//	}
	
	@Override
	public String getHandlerName()
	{
		return "goal";
	}
}
