package geni.witherutils.base.common.entity.cursed.spider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import geni.witherutils.base.common.init.WUTEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

public class CursedSpider extends Spider {
	
	public CursedSpider(EntityType<? extends CursedSpider> type, Level worldIn)
	{
		super(type, worldIn);
	}

	public static AttributeSupplier.Builder registerAttributes()
	{
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 10)
				.add(Attributes.FOLLOW_RANGE, 35)
				.add(Attributes.MOVEMENT_SPEED, 0.3)
				.add(Attributes.ATTACK_DAMAGE, 3)
				.add(Attributes.ARMOR, 0);
	}

	@Override
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.SPIDER_AMBIENT;
	}
	@Override
	protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource)
	{
		return SoundEvents.SPIDER_HURT;
	}
	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.SPIDER_DEATH;
	}

	@Override
	public void tick()
	{
		super.tick();

		double pad = 0.2;
		
		AABB aabb = getBoundingBox();
		double x = aabb.minX + Math.random() * (aabb.maxX - aabb.minX + (pad * 2)) - pad;
		double y = aabb.minY + Math.random() * (aabb.maxY - aabb.minY + (pad * 2)) - pad;
		double z = aabb.minZ + Math.random() * (aabb.maxZ - aabb.minZ + (pad * 2)) - pad;
		getCommandSenderWorld().addParticle(ParticleTypes.MYCELIUM, x, y, z, 0, 0, 0);
	
		if(Math.random() < 0.1)
		{
			y = aabb.minY + 0.1;
			getCommandSenderWorld().addParticle(ParticleTypes.SOUL, x, y, z, 0, 0, 0);
		}
	}

	public static void init(RegisterSpawnPlacementsEvent event)
    {
		event.register(WUTEntities.CURSEDSPIDER.get(), Monster::checkMonsterSpawnRules);
    }

	@Override
	public void addAdditionalSaveData(@Nonnull CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
	}

	@Override
	public void readAdditionalSaveData(@Nonnull CompoundTag compound)
	{
		super.readAdditionalSaveData(compound);
	}
	
	/*
	 * 
	 * CREEPY
	 * 
	 */
	private static final ResourceLocation SPEED_MODIFIER_ATTACKING_RL = ResourceLocation.withDefaultNamespace("Attacking speed boost");
	private static final AttributeModifier SPEED_MODIFIER_ATTACKING = new AttributeModifier(SPEED_MODIFIER_ATTACKING_RL, (double)0.15F, AttributeModifier.Operation.ADD_VALUE);
	private static final EntityDataAccessor<Boolean> DATA_CREEPY = SynchedEntityData.defineId(CursedSpider.class, EntityDataSerializers.BOOLEAN);
	
	public boolean isCreepy()
	{
		return this.entityData.get(DATA_CREEPY);
	}
	
	@Override
	protected void defineSynchedData(Builder pBuilder)
	{
		super.defineSynchedData(pBuilder);
		pBuilder.define(DATA_CREEPY, false);
	}
	
	@Override
	public void setTarget(@Nullable LivingEntity target)
	{
		AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
		if(target == null)
		{
			this.entityData.set(DATA_CREEPY, false);
			attributeinstance.removeModifier(SPEED_MODIFIER_ATTACKING);		
		}
		else
		{
			this.entityData.set(DATA_CREEPY, true);
			if (!attributeinstance.hasModifier(SPEED_MODIFIER_ATTACKING_RL))
				attributeinstance.addTransientModifier(SPEED_MODIFIER_ATTACKING);
			super.setTarget(target);
		}
	}
}
