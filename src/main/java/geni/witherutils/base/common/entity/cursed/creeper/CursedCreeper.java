package geni.witherutils.base.common.entity.cursed.creeper;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import geni.witherutils.base.common.init.WUTEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkHooks;

public class CursedCreeper extends Creeper {

	public CursedCreeper(EntityType<? extends CursedCreeper> type, Level worldIn)
	{
		super(type, worldIn);
	}

    @SuppressWarnings("deprecation")
	public static void init()
    {
        SpawnPlacements.register(WUTEntities.CURSEDCREEPER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
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
		return null;
	}
	@Override
	protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource)
	{
		return SoundEvents.CREEPER_HURT;
	}
	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.CREEPER_DEATH;
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

	@SuppressWarnings("deprecation")
	@Override
	public SpawnGroupData finalizeSpawn(@Nonnull ServerLevelAccessor worldIn, @Nonnull DifficultyInstance difficultyIn, @Nonnull MobSpawnType reason, SpawnGroupData spawnDataIn, CompoundTag dataTag)
	{
		return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
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

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	/*
	 * 
	 * CREEPY
	 * 
	 */
	private static final UUID SPEED_MODIFIER_ATTACKING_UUID = UUID.fromString("076E0DFB-83AE-4623-9511-833310E291A0");
	private static final AttributeModifier SPEED_MODIFIER_ATTACKING = new AttributeModifier(SPEED_MODIFIER_ATTACKING_UUID, "Attacking speed boost", (double)0.15F, AttributeModifier.Operation.ADDITION);
	private static final EntityDataAccessor<Boolean> DATA_CREEPY = SynchedEntityData.defineId(CursedCreeper.class, EntityDataSerializers.BOOLEAN);
	
	public boolean isCreepy()
	{
		return this.entityData.get(DATA_CREEPY);
	}
	
	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(DATA_CREEPY, false);
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
			if (!attributeinstance.hasModifier(SPEED_MODIFIER_ATTACKING))
				attributeinstance.addTransientModifier(SPEED_MODIFIER_ATTACKING);
			super.setTarget(target);
		}
	}
}
