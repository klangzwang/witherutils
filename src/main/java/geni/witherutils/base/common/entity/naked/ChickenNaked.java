package geni.witherutils.base.common.entity.naked;

import java.util.UUID;

import javax.annotation.Nullable;

import geni.witherutils.base.common.init.WUTParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.EatBlockGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public class ChickenNaked extends Chicken {

	@Nullable
	private UUID persistentAngerTarget;
	   
	public ChickenNaked(EntityType<? extends Chicken> p_28236_, Level p_28237_)
	{
		super(p_28236_, p_28237_);
		this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
	}
	
	public static AttributeSupplier.Builder createAttributes()
	{
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 12.0D).add(Attributes.MOVEMENT_SPEED, 0.35D);
	}

	@Override
	protected void registerGoals()
	{
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new FollowParentGoal(this, 1.1D));
		this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(5, new EatBlockGoal(this));
	}
	
	@SuppressWarnings("resource")
	@Override
	public void baseTick()
	{
		super.baseTick();
		
		if(level().random.nextDouble() < 0.1D)
		{
	        double d0 = (double)this.getX() - 0.0D;
	        double d1 = (double)this.getY() + 0.5D;
	        double d2 = (double)this.getZ() - 0.0D;
	        double d6 = this.level().random.nextDouble() * 6.0D / 16.0D;
	        this.level().addParticle(WUTParticles.RISINGSOUL.get(),
	        		d0, d1 + d6, d2,
	        		0.0D, 0.0D, 0.0D);
	        this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME,
	        		d0, d1 + d6, d2,
	        		0.0D, 0.0D, 0.0D);
		}

	}
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		return null;
	}
	@Override
	protected SoundEvent getHurtSound(DamageSource p_28262_)
	{
		return null;
	}
	@Override
	protected SoundEvent getDeathSound()
	{
		return null;
	}
	@Override
	protected void playStepSound(BlockPos p_28254_, BlockState p_28255_)
	{
		this.playSound(SoundEvents.CHICKEN_STEP, 0.15F, 0.85F);
	}
	@Override
	public boolean isFood(ItemStack p_28271_)
	{
		return false;
	}
	@Override
	public boolean removeWhenFarAway(double p_28266_)
	{
		return true;
	}
}
