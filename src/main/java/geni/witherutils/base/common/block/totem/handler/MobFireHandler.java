package geni.witherutils.base.common.block.totem.handler;

import javax.annotation.Nonnull;

import geni.witherutils.base.common.block.totem.TotemBlockEntity;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;

public class MobFireHandler implements IMobAttractionHandler {
	
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
		RandomSource random = RandomSource.create();
		Mob mob = (Mob) entity;
		if(!mob.fireImmune() && !mob.isOnFire())
		{
			mob.setRemainingFireTicks(random.nextInt(5));
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
		return "fire";
	}
}
