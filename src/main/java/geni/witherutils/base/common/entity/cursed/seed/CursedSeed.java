package geni.witherutils.base.common.entity.cursed.seed;

import javax.annotation.Nonnull;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkHooks;

public class CursedSeed extends PathfinderMob {

	public CursedSeed(EntityType<? extends CursedSeed> type, Level worldIn)
	{
		super(type, worldIn);
	}

    @Override
    protected void registerGoals()
    {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    	super.registerGoals();
    }
    
	public static AttributeSupplier.Builder createAttributes()
	{
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 50)
				.add(Attributes.FOLLOW_RANGE, 0)
				.add(Attributes.MOVEMENT_SPEED, 0.0)
				.add(Attributes.ATTACK_DAMAGE, 1)
				.add(Attributes.ARMOR, 0);
	}

	@Override
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.CHORUS_FLOWER_DEATH;
	}
	@Override
	protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource)
	{
		return SoundEvents.ZOGLIN_HURT;
	}
	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ZOGLIN_DEATH;
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
	
	@Override
	public float getVoicePitch()
	{
        return (float) (1.3f - this.position().y() / 10.0f);
	}
	@Override
	protected float getSoundVolume()
	{
        return (float) (this.position().y() / 8.0f);
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
}
