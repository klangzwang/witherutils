package geni.witherutils.base.common.entity.cursed.dryhead;

import java.util.Collection;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("resource")
public class CursedDryHead extends FlyingMob implements Enemy {

	private static final ResourceLocation SPEED_MODIFIER_ATTACKING_RL = ResourceLocation.withDefaultNamespace("Attacking speed boost");
	private static final AttributeModifier SPEED_MODIFIER_ATTACKING = new AttributeModifier(SPEED_MODIFIER_ATTACKING_RL, (double)0.15F, AttributeModifier.Operation.ADD_VALUE);

	public static final float FLAP_DEGREES_PER_TICK = 74.48451F;
	public static final int TICKS_PER_FLAP = Mth.ceil(2.4166098F);

	public static final EntityDataAccessor<Boolean> DATA_KAMIKAZE = SynchedEntityData.defineId(CursedDryHead.class, EntityDataSerializers.BOOLEAN);
	   
	private BlockPos lightBlockPos = null;
	
	Vec3 moveTargetPoint = Vec3.ZERO;
	BlockPos anchorPoint = BlockPos.ZERO;
	CursedDryHead.AttackPhase attackPhase = CursedDryHead.AttackPhase.CIRCLE;

	public CursedDryHead(EntityType<? extends CursedDryHead> dryhead, Level level)
	{
		super(dryhead, level);
		this.xpReward = 5;
		this.moveControl = new CursedDryHead.CursedDryHeadMoveControl(this);
	    this.lookControl = new CursedDryHead.CursedDryHeadLookControl(this);
	}
	
	static enum AttackPhase
	{
		CIRCLE,
		SWOOP;
	}
	
    @Override
    protected BodyRotationControl createBodyControl()
    {
    	return new CursedDryHead.CursedDryHeadBodyRotationControl(this);
	}
	   
    @Override
    protected void registerGoals()
    {
    	this.goalSelector.addGoal(3, new CursedDryHead.CircleAroundAnchorGoal());
        this.targetSelector.addGoal(1, new CursedDryHead.FindPlayerTargetGoal());
    }
    
	public static AttributeSupplier.Builder createAttributes()
	{
        return Monster.createMonsterAttributes()
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.5F)
                .add(Attributes.FLYING_SPEED, 0.5F)
                .add(Attributes.FOLLOW_RANGE, 48.0D);
	}

	/*
	 * 
	 * TICK ETC.
	 * 
	 */
	@Override
	public void tick()
	{
		super.tick();

		if (this.level().isClientSide)
		{
			float f = Mth.cos((float) (this.getUniqueFlapTickOffset() + this.tickCount) * 7.448451F * ((float) Math.PI / 180F) + (float) Math.PI);
			float f1 = Mth.cos((float) (this.getUniqueFlapTickOffset() + this.tickCount + 1) * 7.448451F * ((float) Math.PI / 180F) + (float) Math.PI);
			
			if (f > 0.0F && f1 <= 0.0F)
			{

			}
			
			this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.PHANTOM_FLAP, this.getSoundSource(), 0.95F + this.random.nextFloat() * 0.05F, 4.0F, false);
			
			float f2 = Mth.cos(this.getYRot() * ((float) Math.PI / 180F)) * (1.3F + 0.21F);
			float f3 = Mth.sin(this.getYRot() * ((float) Math.PI / 180F)) * (1.3F + 0.21F);
			float f4 = (0.3F + f * 0.45F) * ((float) 0.2F + 1.0F);
			this.level().addParticle(ParticleTypes.MYCELIUM, this.getX() + (double) f2, this.getY() + (double) f4, this.getZ() + (double) f3, 0.0D, 0.0D, 0.0D);
			this.level().addParticle(ParticleTypes.MYCELIUM, this.getX() - (double) f2, this.getY() + (double) f4, this.getZ() - (double) f3, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	protected void customServerAiStep()
	{
		super.customServerAiStep();
	}
	
	@Override
	public void aiStep()
	{
		super.aiStep();
		final var isInsideWaterBlock = level().isWaterAt(blockPosition());
		spawnLightSource(this, isInsideWaterBlock);
	}
	
	@Override
	public void setTarget(LivingEntity player)
	{
		AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
		if (player == null)
		{
			this.entityData.set(DATA_KAMIKAZE, false);
			attributeinstance.removeModifier(SPEED_MODIFIER_ATTACKING);
		}
		else
		{
			this.entityData.set(DATA_KAMIKAZE, true);
			if (!attributeinstance.hasModifier(SPEED_MODIFIER_ATTACKING_RL))
			{
				attributeinstance.addTransientModifier(SPEED_MODIFIER_ATTACKING);
			}
		}
		super.setTarget(player);
	}
	
	/*
	 * 
	 * KAMIKAZE
	 * 
	 */
	@Override
	protected void defineSynchedData(Builder pBuilder)
	{
		super.defineSynchedData(pBuilder);
		pBuilder.define(DATA_KAMIKAZE, false);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag tag)
	{
		super.readAdditionalSaveData(tag);
		if (tag.contains("AX"))
		{
			this.anchorPoint = new BlockPos(tag.getInt("AX"), tag.getInt("AY"), tag.getInt("AZ"));
		}
		this.entityData.set(DATA_KAMIKAZE, tag.getBoolean("iskamikaze"));
	}
	@Override
	public void addAdditionalSaveData(CompoundTag tag)
	{
		super.addAdditionalSaveData(tag);
		tag.putInt("AX", this.anchorPoint.getX());
		tag.putInt("AY", this.anchorPoint.getY());
		tag.putInt("AZ", this.anchorPoint.getZ());
		if (this.entityData.get(DATA_KAMIKAZE))
		{
			tag.putBoolean("iskamikaze", true);
		}
	}

	public boolean isKamikaze()
	{
		return this.entityData.get(DATA_KAMIKAZE);
	}
	public void setKamikaze(boolean kamikaze)
	{
		this.entityData.set(DATA_KAMIKAZE, kamikaze);
	}
	
	/*
	 * 
	 * EXPLOSION
	 * 
	 */
	@Override
	public void playerTouch(Player player)
	{
		if(!level().isClientSide() && player.distanceToSqr(this) < 3.2F)
		{
//			dryKamikaze();
		}
	}
	
	public void dryKamikaze()
	{
		if (!this.level().isClientSide)
		{
			this.dead = true;
			this.level().explode(this, this.getX(), this.getY(), this.getZ(), (float) 6, Level.ExplosionInteraction.MOB);
			this.discard();
			this.spawnLingeringCloud();
		}
	}
	
	private void spawnLingeringCloud()
	{
		Collection<MobEffectInstance> collection = this.getActiveEffects();
		if (!collection.isEmpty())
		{
			AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
			areaeffectcloud.setRadius(2.5F);
			areaeffectcloud.setRadiusOnUse(-0.5F);
			areaeffectcloud.setWaitTime(10);
			areaeffectcloud.setDuration(areaeffectcloud.getDuration() / 2);
			areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float)areaeffectcloud.getDuration());

			for(MobEffectInstance mobeffectinstance : collection)
			{
				areaeffectcloud.addEffect(new MobEffectInstance(mobeffectinstance));
			}

			this.level().addFreshEntity(areaeffectcloud);
		}
	}
	
	@Override
	protected void tickDeath()
	{
		++deathTime;
		if (deathTime == 5)
		{
			remove(RemovalReason.KILLED);
			if (!level().isClientSide)
				explode();
		}
	}

	protected void explode()
	{
		level().explode(this, this.getX(), this.getY(0.0625D), this.getZ(), 1.0F, Level.ExplosionInteraction.NONE);
	}
	
	/*
	 * 
	 * ENTITY
	 * 
	 */
	@Override
	public boolean canAttackType(EntityType<?> p_33111_)
	{
		return true;
	}
	   
	@Override
	protected boolean shouldDespawnInPeaceful()
	{
		return false;
	}

	protected boolean shouldBurnInDay()
	{
		return false;
	}

	@Override
	public int getMaxSpawnClusterSize()
	{
		return 7;
	}

//	@Override
//	public boolean causeFallDamage(float p_147105_, float p_147106_, DamageSource p_147107_)
//	{
//		return false;
//	}

	@Override
	public boolean isFlapping()
	{
		return this.tickCount % TICKS_PER_FLAP == 0;
	}
	
//	@Override
//	public double getMeleeAttackRangeSqr(LivingEntity livingEntity)
//	{
//		return this.getBbWidth() * 1.5f * (this.getBbWidth() * 1.5f + livingEntity.getBbWidth());
//	}

//	@Override
//	public boolean isWithinMeleeAttackRange(LivingEntity livingEntity)
//	{
//		var distance = this.distanceToSqr(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
//		return distance <= this.getMeleeAttackRangeSqr(livingEntity);
//	}

//	@Override
//	protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos)
//	{
//	}

	public int getUniqueFlapTickOffset()
	{
		return this.getId() * 3;
	}
	
	/*
	 * 
	 * SPAWN N INIT
	 * 
	 */
	@SuppressWarnings("deprecation")
	public static void init()
	{
//		SpawnPlacements.register(WUTEntities.CURSEDDRYHEAD.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CursedDryHead::checkDryHeadSpawnRules);
	}
	
	public static boolean checkDryHeadSpawnRules(EntityType<CursedDryHead> cursedzombie, LevelAccessor level, MobSpawnType type, BlockPos pos, RandomSource random)
	{
		return CursedDryHead.checkMobSpawnRules(cursedzombie, level, type, pos, random);
	}
	
	/*
	 * 
	 * SPAWNLIGHT
	 * 
	 */
	public void spawnLightSource(Entity entity, boolean isInWaterBlock)
	{
	}
	
	private boolean checkDistance(BlockPos blockPosA, BlockPos blockPosB, int distance)
	{
		return Math.abs(blockPosA.getX() - blockPosB.getX()) <= distance && Math.abs(blockPosA.getY() - blockPosB.getY()) <= distance && Math.abs(blockPosA.getZ() - blockPosB.getZ()) <= distance;
	}

	private BlockPos findFreeSpace(Level world, BlockPos blockPos, int maxDistance)
	{
		if (blockPos == null)
			return null;

		var offsets = new int[maxDistance * 2 + 1];
		offsets[0] = 0;
		for (var i = 2; i <= maxDistance * 2; i += 2)
		{
			offsets[i - 1] = i / 2;
			offsets[i] = -i / 2;
		}
		for (final var x : offsets)
			for (final var y : offsets)
				for (final var z : offsets)
				{
					final var offsetPos = blockPos.offset(x, y, z);
					final var state = world.getBlockState(offsetPos);
					if (state.isAir())
						return offsetPos;
				}
		return null;
	}
	
	/*
	 * 
	 * LOOK CONTROL
	 * 
	 */
	class CursedDryHeadLookControl extends LookControl
	{
		public CursedDryHeadLookControl(Mob p_33235_)
		{
			super(p_33235_);
		}
		public void tick()
		{
		}
	}
	
	/*
	 * 
	 * ROTATION CONTROL
	 * 
	 */
	class CursedDryHeadBodyRotationControl extends BodyRotationControl
	{
		public CursedDryHeadBodyRotationControl(Mob p_33216_)
		{
			super(p_33216_);
		}

		public void clientTick()
		{
			CursedDryHead.this.yHeadRot = CursedDryHead.this.yBodyRot;
			CursedDryHead.this.yBodyRot = CursedDryHead.this.getYRot();
		}
	}

	/*
	 * 
	 * MOVE CONTROL
	 * 
	 */
	class CursedDryHeadMoveControl extends MoveControl
	{
		private float speed = 0.1F;

		public CursedDryHeadMoveControl(Mob p_33241_)
		{
			super(p_33241_);
		}

		public void tick()
		{
			if (CursedDryHead.this.horizontalCollision)
			{
				CursedDryHead.this.setYRot(CursedDryHead.this.getYRot() + 180.0F);
				this.speed = 0.1F;
			}

			double d0 = CursedDryHead.this.moveTargetPoint.x - CursedDryHead.this.getX();
			double d1 = CursedDryHead.this.moveTargetPoint.y - CursedDryHead.this.getY();
			double d2 = CursedDryHead.this.moveTargetPoint.z - CursedDryHead.this.getZ();
			
			double d3 = Math.sqrt(d0 * d0 + d2 * d2);
			
			if (Math.abs(d3) > (double) 1.0E-5F)
			{
				double d4 = 1.0D - Math.abs(d1 * (double) 0.7F) / d3;
				d0 *= d4;
				d2 *= d4;
				d3 = Math.sqrt(d0 * d0 + d2 * d2);
				double d5 = Math.sqrt(d0 * d0 + d2 * d2 + d1 * d1);
				float f = CursedDryHead.this.getYRot();
				float f1 = (float) Mth.atan2(d2, d0);
				float f2 = Mth.wrapDegrees(CursedDryHead.this.getYRot() + 90.0F);
				float f3 = Mth.wrapDegrees(f1 * (180F / (float) Math.PI));
				CursedDryHead.this.setYRot(Mth.approachDegrees(f2, f3, 4.0F) - 90.0F);
				CursedDryHead.this.yBodyRot = CursedDryHead.this.getYRot();
				if (Mth.degreesDifferenceAbs(f, CursedDryHead.this.getYRot()) < 3.0F) {
					this.speed = Mth.approach(this.speed, 1.8F, 0.005F * (1.8F / this.speed));
				} else {
					this.speed = Mth.approach(this.speed, 0.2F, 0.025F);
				}

				float f4 = (float) (-(Mth.atan2(-d1, d3) * (double) (180F / (float) Math.PI)));
				CursedDryHead.this.setXRot(f4);
				float f5 = CursedDryHead.this.getYRot() + 90.0F;
				double d6 = (double) (this.speed * Mth.cos(f5 * ((float) Math.PI / 180F))) * Math.abs(d0 / d5);
				double d7 = (double) (this.speed * Mth.sin(f5 * ((float) Math.PI / 180F))) * Math.abs(d2 / d5);
				double d8 = (double) (this.speed * Mth.sin(f4 * ((float) Math.PI / 180F))) * Math.abs(d1 / d5);
				Vec3 vec3 = CursedDryHead.this.getDeltaMovement();
				CursedDryHead.this.setDeltaMovement(vec3.add((new Vec3(d6, d8, d7)).subtract(vec3).scale(0.2D)));
			}
		}
	}

	/*
	 * 
	 * GOALS
	 * 
	 */
	class FindPlayerTargetGoal extends Goal
	{
		private final TargetingConditions attackTargeting = TargetingConditions.forCombat().range(64.0D);
		private int nextScanTick = reducedTickDelay(20);

		public boolean canUse()
		{
			if (this.nextScanTick > 0)
			{
				--this.nextScanTick;
				return false;
			}
			else
			{
				this.nextScanTick = reducedTickDelay(60);
				List<Player> list = CursedDryHead.this.level().getNearbyPlayers(this.attackTargeting, CursedDryHead.this, CursedDryHead.this.getBoundingBox().inflate(6.0D, 6.0D, 6.0D));
				if (!list.isEmpty())
				{
					list.sort(Comparator.<Entity, Double>comparing(Entity::getY).reversed());

					for (Player player : list)
					{
						if (CursedDryHead.this.canAttack(player, TargetingConditions.DEFAULT))
						{
							CursedDryHead.this.setTarget(player);
							return true;
						}
					}
				}
				return false;
			}
		}

		public boolean canContinueToUse()
		{
			LivingEntity livingentity = CursedDryHead.this.getTarget();
			return livingentity != null ? CursedDryHead.this.canAttack(livingentity, TargetingConditions.DEFAULT) : false;
		}
	}
	
	abstract class CursedDryHeadMoveTargetGoal extends Goal
	{
		public CursedDryHeadMoveTargetGoal()
		{
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		protected boolean touchingTarget()
		{
			return CursedDryHead.this.moveTargetPoint.distanceToSqr(
					CursedDryHead.this.getX(),
					CursedDryHead.this.getY(),
					CursedDryHead.this.getZ()
					) < 4.0D;
		}
	}
	
	class CircleAroundAnchorGoal extends CursedDryHead.CursedDryHeadMoveTargetGoal
	{
		private float angle;
		private float distance;
		private float height;
		private float clockwise;

		public boolean canUse() {
			return CursedDryHead.this.getTarget() == null || CursedDryHead.this.attackPhase == CursedDryHead.AttackPhase.CIRCLE;
		}

		public void start() {
			this.distance = 5.0F + CursedDryHead.this.random.nextFloat() * 10.0F;
			this.height = -4.0F + CursedDryHead.this.random.nextFloat() * 9.0F;
			this.clockwise = CursedDryHead.this.random.nextBoolean() ? 1.0F : -1.0F;
			this.selectNext();
		}

		public void tick() {
			if (CursedDryHead.this.random.nextInt(this.adjustedTickDelay(350)) == 0) {
				this.height = -4.0F + CursedDryHead.this.random.nextFloat() * 9.0F;
			}

			if (CursedDryHead.this.random.nextInt(this.adjustedTickDelay(250)) == 0) {
				++this.distance;
				if (this.distance > 15.0F) {
					this.distance = 5.0F;
					this.clockwise = -this.clockwise;
				}
			}

			if (CursedDryHead.this.random.nextInt(this.adjustedTickDelay(450)) == 0) {
				this.angle = CursedDryHead.this.random.nextFloat() * 2.0F * (float) Math.PI;
				this.selectNext();
			}

			if (this.touchingTarget()) {
				this.selectNext();
			}

			if (CursedDryHead.this.moveTargetPoint.y < CursedDryHead.this.getY()
					&& !CursedDryHead.this.level().isEmptyBlock(CursedDryHead.this.blockPosition().below(1))) {
				this.height = Math.max(1.0F, this.height);
				this.selectNext();
			}

			if (CursedDryHead.this.moveTargetPoint.y > CursedDryHead.this.getY()
					&& !CursedDryHead.this.level().isEmptyBlock(CursedDryHead.this.blockPosition().above(1))) {
				this.height = Math.min(-1.0F, this.height);
				this.selectNext();
			}

		}

		private void selectNext() {
			if (BlockPos.ZERO.equals(CursedDryHead.this.anchorPoint)) {
				CursedDryHead.this.anchorPoint = CursedDryHead.this.blockPosition();
			}

			this.angle += this.clockwise * 15.0F * ((float) Math.PI / 180F);
			CursedDryHead.this.moveTargetPoint = Vec3.atLowerCornerOf(CursedDryHead.this.anchorPoint).add(
					(double) (this.distance * Mth.cos(this.angle)), (double) (-4.0F + this.height),
					(double) (this.distance * Mth.sin(this.angle)));
		}
	}
}
