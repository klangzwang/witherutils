package geni.witherutils.base.common.block.totem.handler;

import javax.annotation.Nonnull;

import geni.witherutils.base.common.block.totem.TotemBlockEntity;
import geni.witherutils.base.common.entity.cursed.creeper.CursedCreeper;
import geni.witherutils.base.common.entity.cursed.skeleton.CursedSkeleton;
import geni.witherutils.base.common.entity.cursed.spider.CursedSpider;
import geni.witherutils.base.common.entity.cursed.zombie.CursedZombie;
import geni.witherutils.base.common.init.WUTEntities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class MobSwitchHandler implements IMobAttractionHandler {
	
	@Override
	public @Nonnull State canAttract(TotemBlockEntity attractor, LivingEntity entity)
	{
		if(entity instanceof CursedSkeleton ||
		   entity instanceof CursedCreeper ||
		   entity instanceof CursedZombie ||
		   entity instanceof CursedSpider)
		{
			return State.CANNOT_ATTRACT;
		}
		else
		{
			if (entity instanceof PathfinderMob)
			{
				if (((Mob) entity).getTarget() == attractor.getFakePlayer())
				{
					return State.ALREADY_ATTRACTING;
				}
				return State.CAN_ATTRACT;
			}
		}
		return State.CANNOT_ATTRACT;
    }

	@Override
	public void startAttracting(TotemBlockEntity attractor, LivingEntity entity)
	{
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
		else
		{
			entity.level().levelEvent(1027, entity.blockPosition(), 0);
			
			if (entity.isAlive() && entity instanceof Skeleton)
			{
				Skeleton skeleton = (Skeleton) entity;
				CursedSkeleton cursedSkeleton = new CursedSkeleton(WUTEntities.CURSEDSKELETON.get(), entity.level());
				spawnSpecific(entity.level(), cursedSkeleton, skeleton, entity);
			}
			else if (entity.isAlive() && entity instanceof Zombie)
			{
				Zombie zombie = (Zombie) entity;
				CursedZombie cursedZombie = new CursedZombie(WUTEntities.CURSEDZOMBIE.get(), entity.level());
				spawnSpecific(entity.level(), cursedZombie, zombie, entity);
			}
			else if (entity.isAlive() && entity instanceof Creeper)
			{
				Creeper creeper = (Creeper) entity;
				CursedCreeper cursedCreeper = new CursedCreeper(WUTEntities.CURSEDCREEPER.get(), entity.level());
				spawnSpecific(entity.level(), cursedCreeper, creeper, entity);
			}
			else if (entity.isAlive() && entity instanceof Spider)
			{
				Spider spider = (Spider) entity;
				CursedSpider cursedSpider = new CursedSpider(WUTEntities.CURSEDSPIDER.get(), entity.level());
				spawnSpecific(entity.level(), cursedSpider, spider, entity);
			}
		}
	}

	public void spawnSpecific(Level level, LivingEntity cursedEntity, LivingEntity oldEntity, LivingEntity entity)
	{
		oldEntity = (LivingEntity) entity;
		oldEntity.blockPosition();
		oldEntity.remove(RemovalReason.KILLED);

		cursedEntity.absMoveTo(oldEntity.position().x, oldEntity.position().y, oldEntity.position().z, oldEntity.getYRot(), oldEntity.getXRot());
		cursedEntity.moveTo(oldEntity.getX(), oldEntity.getY(), oldEntity.getZ(), oldEntity.xRotO, oldEntity.yRotO);
		cursedEntity.setHealth(oldEntity.getHealth());
		
		level.addFreshEntity((Entity) cursedEntity);
	}
	
	@Override
	public void release(TotemBlockEntity attractor, LivingEntity entity)
	{
	}
	
	@Override
	public String getHandlerName()
	{
		return "switch";
	}
}
