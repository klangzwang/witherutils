package geni.witherutils.base.common.block.totem.handler;

import javax.annotation.Nonnull;

import geni.witherutils.base.common.block.totem.TotemBlockEntity;
import geni.witherutils.core.common.helper.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

public class MobPathHandler implements IMobAttractionHandler {

	@Override
	public @Nonnull State canAttract(TotemBlockEntity attractor, LivingEntity entity)
	{
		if (entity instanceof Skeleton)
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
		tick(attractor, entity);
	}

	@Override
	public void tick(TotemBlockEntity attractor, LivingEntity entity)
	{
		Skeleton skel = (Skeleton) entity;
		Path pathentity = getPathEntityToEntity(entity, attractor.getFakePlayer(), 8 * 2);
		skel.getNavigation().moveTo(pathentity, skel.getSpeed());
	}

	@Override
	public void release(TotemBlockEntity attractor, LivingEntity entity)
	{
	}

	@SuppressWarnings("unused")
	public Path getPathEntityToEntity(Entity entity, Entity targetEntity, float range)
	{
		int targX = MathHelper.floor(targetEntity.position().x);
		int targY = MathHelper.floor(targetEntity.position().y + 1.0D);
		int targZ = MathHelper.floor(targetEntity.position().z);

		PathFinder pf = new PathFinder(new WalkNodeEvaluator(), 1);
		return null; //pf.findPath(null, (Mob) entity, new BlockPos(targX, targY, targZ).mutable(), range);
	}
	
	@Override
	public String getHandlerName()
	{
		return "path";
	}
}
