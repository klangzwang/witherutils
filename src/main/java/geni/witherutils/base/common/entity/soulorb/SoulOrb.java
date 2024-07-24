package geni.witherutils.base.common.entity.soulorb;

import java.util.EnumSet;

import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.base.common.init.WUTParticles;
import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

public class SoulOrb extends FlyingMob {

	private int age;
	public int value;
	
	public SoulOrb(Level level, double x, double y, double z, int value)
	{
		this(WUTEntities.SOULORB.get(), level);
		this.setPos(x, y, z);
		this.value = value;
		this.moveControl = new SoulOrb.SoulOrbMoveControl(this);
	}

	public SoulOrb(EntityType<SoulOrb> entity, Level level)
	{
		super(entity, level);
	}
	
	@Override
	public boolean isPushable()
	{
		return false;
	}

	public static AttributeSupplier.Builder createAttributes()
	{
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder
				.add(Attributes.FLYING_SPEED, 0.0F)
				.add(Attributes.MOVEMENT_SPEED, 0.0F)
				.add(Attributes.MAX_HEALTH, 100.0F)
				.add(Attributes.FOLLOW_RANGE, 100.0F);
		return builder;
	}

	@Override
	protected void registerGoals()
	{
		this.goalSelector.addGoal(5, new SoulOrb.RandomFloatAroundGoal(this));
	}

	public static void init(RegisterSpawnPlacementsEvent event)
    {
		event.register(WUTEntities.SOULORB.get(), SoulOrb::checkSoulOrbSpawnRules);
    }
	
    public static boolean checkSoulOrbSpawnRules(EntityType<SoulOrb> soulorb, LevelAccessor level, MobSpawnType type, BlockPos pos, RandomSource random)
    {
        return SoulOrb.checkMobSpawnRules(soulorb, level, type, pos, random);
    }
	
	public static void award(ServerLevel level, Vec3 vec3, int value)
	{
		while(value > 0)
		{
			value -= 1;
			level.addFreshEntity(new SoulOrb(level, vec3.x(), vec3.y(), vec3.z(), 1));
		}
	}
	
	public static void fxthis(ServerLevel level, BlockPos pos)
	{
		SoundUtil.playSoundDistrib(level, pos, SoundEvents.SOUL_ESCAPE.value(), 1, 1, false, true);
	}
	
	@Override
	protected Entity.MovementEmission getMovementEmission()
	{
		return Entity.MovementEmission.NONE;
	}
	
    @Override
    public boolean hurt(DamageSource source, float amount)
    {
        return false;
    }
    @Override
    public boolean canBeAffected(MobEffectInstance effect)
    {
        return false;
    }
    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source)
    {
        return false;
    }
    @Override
    public boolean isInvulnerableTo(DamageSource source)
    {
        return true;
    }
    
	@Override
	public void tick()
	{
		super.tick();

		this.level().addParticle(WUTParticles.SOULORB.get(),
				this.getX(),
				this.getY(),
				this.getZ(),
				0.0D, 0.0D, 0.0D);
		
		double d8 = (double) this.getX() - this.random.nextDouble() + 0.5D;
		double d9 = (double) this.getZ() - this.random.nextDouble() + 0.5D;
		
		for(int i = 0; i < 2; i++)
		{
			level().addParticle(WUTParticles.SOULFLAKE.get(),
					d8, this.getY() - 0.7D, d9,
					0, -0.1D, 0);
		}

		++this.age;
		if(this.age >= 400)
		{
			ItemEntity entityitem = new ItemEntity(level(), position().x, position().y, position().z, new ItemStack(WUTItems.SOULORB.get()));
            entityitem.setNoPickUpDelay();
            level().addFreshEntity(entityitem);
			this.discard();
		}
	}

    @Override
	public void playerTouch(Player player)
	{
//		if(!level().isClientSide() && player.distanceToSqr(this) < 3.2F)
//		{
//			ItemEntity entityitem = new ItemEntity(level(), player.getX(), player.getY(), player.getZ(), new ItemStack(WUTItems.SOULORB.get()));
//            entityitem.setNoPickUpDelay();
//            level().addFreshEntity(entityitem);
//            this.discard();
//		}
	}

	public int getIcon()
	{
		if (this.value >= 2477) {
			return 10;
		} else if (this.value >= 1237) {
			return 9;
		} else if (this.value >= 617) {
			return 8;
		} else if (this.value >= 307) {
			return 7;
		} else if (this.value >= 149) {
			return 6;
		} else if (this.value >= 73) {
			return 5;
		} else if (this.value >= 37) {
			return 4;
		} else if (this.value >= 17) {
			return 3;
		} else if (this.value >= 7) {
			return 2;
		} else {
			return this.value >= 3 ? 1 : 0;
		}
	}
	
	@Override
	public boolean isAttackable()
	{
		return false;
	}
	
	public int getValue()
	{
		return value;
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag tag)
	{
		tag.putShort("Age", (short)this.age);
		tag.putShort("Value", (short)this.value);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag tag)
	{
	      this.age = tag.getShort("Age");
	      this.value = tag.getShort("Value");
	}
	
	static class SoulOrbMoveControl extends MoveControl
	{
		private final SoulOrb soulorb;
		private int floatDuration;

		public SoulOrbMoveControl(SoulOrb soulorb)
		{
			super(soulorb);
			this.soulorb = soulorb;
		}

		public void tick()
		{
			if (this.operation == MoveControl.Operation.MOVE_TO)
			{
				if (this.floatDuration-- <= 0)
				{
					this.floatDuration += this.soulorb.getRandom().nextInt(30) + 2;
					Vec3 vec3 = new Vec3(this.wantedX - this.soulorb.getX(), this.wantedY - this.soulorb.getY(), this.wantedZ - this.soulorb.getZ());
					double d0 = vec3.length();
					vec3 = vec3.normalize();
					if (this.canReach(vec3, Mth.ceil(d0)))
					{
						this.soulorb.setDeltaMovement(this.soulorb.getDeltaMovement().add(vec3.scale(0.05D)));
					}
					else
					{
						this.operation = MoveControl.Operation.WAIT;
					}
				}
			}
		}
		private boolean canReach(Vec3 p_32771_, int p_32772_)
		{
			AABB aabb = this.soulorb.getBoundingBox();
			for (int i = 1; i < p_32772_; ++i)
			{
				aabb = aabb.move(p_32771_);
				if (!this.soulorb.level().noCollision(this.soulorb, aabb))
				{
					return false;
				}
			}
			return true;
		}
	}

	static class RandomFloatAroundGoal extends Goal
	{
		private final SoulOrb soulorb;

		public RandomFloatAroundGoal(SoulOrb soulorb)
		{
			this.soulorb = soulorb;
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		public boolean canUse()
		{
			MoveControl movecontrol = this.soulorb.getMoveControl();
			if (!movecontrol.hasWanted())
			{
				return true;
			}
			else
			{
				double d0 = movecontrol.getWantedX() - this.soulorb.getX();
				double d1 = movecontrol.getWantedY() - this.soulorb.getY();
				double d2 = movecontrol.getWantedZ() - this.soulorb.getZ();
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;
				return d3 < 1.0D || d3 > 3600.0D;
			}
		}

		public boolean canContinueToUse()
		{
			return false;
		}

		public void start()
		{
			RandomSource randomsource = this.soulorb.getRandom();
			double d0 = this.soulorb.getX() + (double) ((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
			double d1 = this.soulorb.getY() + (double) ((randomsource.nextFloat() * 2.0F - 1.0F) * 2.0F);
			double d2 = this.soulorb.getZ() + (double) ((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
			this.soulorb.getMoveControl().setWantedPosition(d0, d1, d2, 0);
		}
	}
}
