package geni.witherutils.base.common.block.fluid;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import geni.witherutils.base.common.init.WUTFluids;
import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class BlueLimboBlock extends LiquidBlock {

	private static final double SPEED = 0.035;
    private static final double SPEED_4 = SPEED * 4;

    public BlueLimboBlock(FlowingFluid supplier, Properties props)
    {
        super(supplier, props);
	}

    private int getRange()
    {
        return 5;
    }
    
	@Override
	public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos)
	{
		return WUTFluids.BLUELIMBO_FLUID_TYPE.get().getLightLevel();
	}
	
	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos)
	{
		return true;
	}
	
	@Override
	public boolean isSignalSource(BlockState state)
	{
		return true;
	}
	
	@Override
	public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction facing)
	{
		return state.getValue(LEVEL) * 2 + 1;
	}
	
	@Override
	public void animateTick(BlockState state, Level worldIn, BlockPos pos, RandomSource random)
	{
		BlockPos blockpos = pos.above();
		BlockState blockstate = worldIn.getBlockState(blockpos);

		if(!blockstate.isFaceSturdy(worldIn, blockpos, Direction.UP))
		{
			double d1 = (double) pos.getX() + random.nextDouble();
			double d2 = (double) pos.getY() + 0.1F + state.getFluidState().getHeight(worldIn, blockpos);
			double d3 = (double) pos.getZ() + random.nextDouble();

			worldIn.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d1, d2, d3, 0.0D, 0.02D, 0.0D);

			if(random.nextFloat() < 0.1F)
			{
				worldIn.addParticle(ParticleTypes.SOUL, d1, d2, d3, 0.0D, 0.1D, 0.0D);
				SoundUtil.playSoundDistrib(worldIn, blockpos, SoundEvents.SOUL_ESCAPE.value(), 0.8f, 1.0f, false, true);
			}
		}
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity)
	{
		if(!(entity instanceof LivingEntity))
			return;
		if(!entity.fireImmune())
		{
			entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 1);
			if(entity.getRemainingFireTicks() == 0)
			{
				entity.setRemainingFireTicks(28);
			}
			entity.hurt(level.damageSources().inFire(), 0.1F);
		}
		LivingEntity living = (LivingEntity) entity;
		if(living.isAlive())
		{
			living.addEffect(new MobEffectInstance(MobEffects.WITHER, 22, 5));
		}
		super.entityInside(state, level, pos, entity);
	}

	@Override
	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player)
	{
		if(!player.fireImmune())
		{
			player.hurt(level.damageSources().inFire(), 0.01F);
		}
		if(player.isAlive())
		{
			player.addEffect(new MobEffectInstance(MobEffects.WITHER, 22, 5));
		}
		return super.playerWillDestroy(level, pos, state, player);
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldstate, boolean moving)
	{
		super.onPlace(state, level, pos, oldstate, moving);
		level.scheduleTick(pos, this, 10);
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
	{
		super.tick(state, level, pos, random);
		
		if(level == null || level.isClientSide)
			return;
		
		level.scheduleTick(pos, this, 3);

        int range = getRange();
        AABB bounds = new AABB(pos.getX() - range, pos.getY() - range, pos.getZ() - range, pos.getX() + range, pos.getY() + range, pos.getZ() + range);

        List<Mob> toMove = level.getEntitiesOfClass(Mob.class, bounds);

        int livingsRemaining = 2;
        if(livingsRemaining <= 0)
        {
        	livingsRemaining = Integer.MAX_VALUE;
        }

        for(Mob living : toMove)
        {
        	living.goalSelector.removeAllGoals(p_351790_ -> true);
        	
        	MobEffectInstance effect = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 20);
        	living.addEffect(effect);
        	living.setSpeed(0);
        	living.setSprinting(false);

            double x = pos.getX() - living.getX();
            double y = pos.getY() - living.getY();
            double z = pos.getZ() - living.getZ();

            double distanceSq = x * x + y * y + z * z;

            if(distanceSq > 0.1F)
            {
                double adjustedSpeed = SPEED_4 / distanceSq;
                Vec3 mov = living.getDeltaMovement();
                double deltaX = mov.x + x * adjustedSpeed;
                double deltaZ = mov.z + z * adjustedSpeed;
                double deltaY;
                if(y > 0)
                {
                    deltaY = 0.12;
                }
                else
                {
                    deltaY = mov.y + y * SPEED;
                }
                living.setDeltaMovement(deltaX, deltaY, deltaZ);
            }
            if(livingsRemaining-- <= 0)
            {
                return;
            }
        }
	}

	@Override
	public void neighborChanged(@Nonnull BlockState state, @Nonnull Level worldIn, @Nonnull BlockPos pos, @Nonnull Block blockIn, @Nonnull BlockPos fromPos, boolean p_54714_)
	{
		checkForFire(worldIn, pos);
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos, p_54714_);
	}

    @SuppressWarnings("unused")
	protected void checkForFire(final Level worldIn, final BlockPos pos)
    {
    	RandomSource RANDOM = RandomSource.create();
    	Direction facing = Direction.Plane.HORIZONTAL.getRandomDirection(RANDOM);
		BlockState neighbor = worldIn.getBlockState(pos.relative(facing));

		if(neighbor.getBlock() instanceof FireBlock)
		{
			if(worldIn.random.nextFloat() < .5f)
			{
				List<BlockPos> explosions = new ArrayList<BlockPos>();
				explosions.add(pos);
				BlockPos up = pos.above();
				worldIn.explode(null, pos.getX() + .5d, pos.getY() + .5d, pos.getZ() + .5d, 2.0f, Level.ExplosionInteraction.NONE);
				float strength = .5f;
				for(BlockPos explosion : explosions)
				{
					worldIn.explode(null, pos.getX() + .5d, pos.getY() + .5d, pos.getZ() + .5d, 2.0f, Level.ExplosionInteraction.NONE);
					strength = Math.min(strength * 1.05f, 7f);
				}
				return;
			}
		}
    }
}
