package geni.witherutils.base.common.block.totem.handler;

import javax.annotation.Nonnull;

import geni.witherutils.base.common.block.totem.TotemBlockEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;

public class MobPullHandler implements IMobAttractionHandler {
	
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

	@SuppressWarnings("static-access")
	@Override
	public void startAttracting(TotemBlockEntity attractor, LivingEntity entity)
	{
		attractor.spawnerClients.add(entity);
		((Mob) entity).setTarget((LivingEntity) attractor.getFakePlayer());
	}

	@Override
	public void tick(TotemBlockEntity attractor, LivingEntity entity)
	{
		double x = (attractor.getBlockPos().getX() + 0.5D - entity.position().x);
		double y = (attractor.getBlockPos().getY() + 1D - entity.position().y);
		double z = (attractor.getBlockPos().getZ() + 0.5D - entity.position().z);
		
		double distance = Math.sqrt(x * x + y * y + z * z);
		
		if (distance > 2)
		{
			Mob mob = (Mob) entity;
			mob.lookAt(attractor.getFakePlayer(), 180, 0);
			mob.moveRelative(0, attractor.getBlockPos().getCenter());

			if (mob.position().y < attractor.getBlockPos().getY())
			{
				mob.setJumping(true);
			}
			else
			{
				mob.setJumping(false);
			}
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public void release(TotemBlockEntity attractor, LivingEntity entity)
	{
		attractor.spawnerClients.remove(entity);
		((Mob) entity).setTarget((LivingEntity) null);
	}

	@Override
	public String getHandlerName()
	{
		return "pull";
	}
}
