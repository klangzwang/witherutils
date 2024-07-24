package geni.witherutils.core.common.util;

import geni.witherutils.base.common.config.client.EffectsConfig;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ParticleUtil {
	
    public enum ERandomChance
    {
        ALWAYS,
        OFTEN,
        FIFTY,
        RARELY;
    }
	
    public enum EParticlePosition
    {
        BLOCKSTATIC,
        BLOCKRANDOM,
        HITRESULT;
    }
	
	@SuppressWarnings("static-access")
	public static DoubleList getLocation(Level pLevel, Player pPlayer, EParticlePosition position)
	{
		DoubleList xyz = new DoubleArrayList();
		
        Vec3 vec3 = pPlayer.getEyePosition();
        Vec3 vec31 = vec3.add(pPlayer.calculateViewVector(pPlayer.getXRot(), pPlayer.getYRot()).scale(pPlayer.blockInteractionRange()));
        BlockHitResult blockhitresult = pLevel.clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, pPlayer));

        BlockState blockstate = pLevel.getBlockState(blockhitresult.getBlockPos());
        VoxelShape shape = blockstate.getShape(pLevel, blockhitresult.getBlockPos());

        switch(position)
        {
			case BLOCKSTATIC:
		        xyz.add(0, (double)blockhitresult.getBlockPos().getX() + 0.5D);
		        xyz.add(1, (double)blockhitresult.getBlockPos().getY() + shape.max(blockhitresult.getDirection().getAxis().Y) + 0.1D);
		        xyz.add(2, (double)blockhitresult.getBlockPos().getZ() + 0.5D);
				break;
			case BLOCKRANDOM:
		        xyz.add(0, (double)blockhitresult.getBlockPos().getX() + pLevel.random.nextDouble());
		        xyz.add(1, (double)blockhitresult.getBlockPos().getY() + shape.max(blockhitresult.getDirection().getAxis().Y) + 0.1D);
		        xyz.add(2, (double)blockhitresult.getBlockPos().getZ() + pLevel.random.nextDouble());
				break;
			case HITRESULT:
		        xyz.add(0, (double)blockhitresult.getLocation().x());
		        xyz.add(1, (double)blockhitresult.getLocation().y());
		        xyz.add(2, (double)blockhitresult.getLocation().z());
				break;
        }
		return xyz;
	}
	
	public static float getChance(Level level, ERandomChance chance)
	{
		float dynamic = 0.1f + level.random.nextInt(3);
        switch(chance)
        {
			case ALWAYS:
				return 1.0f;
			case OFTEN:
				return 0.6f + (dynamic / 10);
			case FIFTY:
				return 0.4f + (dynamic / 10);
			case RARELY:
				return 0.1f + (dynamic / 10);
			default:
				return 0.f;
        }
	}
	
	public static void playParticleDistrib(LevelAccessor levelacc, Player pPlayer, BlockPos pPos, ParticleOptions particleIn, EParticlePosition position, ERandomChance chance, int amount, int levelevent)
    {
		if (!EffectsConfig.CAN_PARTICLES.get())
			return;
		
		if (levelacc instanceof Level level)
		{
			double x = getLocation(level, pPlayer, position).getDouble(0);
			double y = getLocation(level, pPlayer, position).getDouble(1);
			double z = getLocation(level, pPlayer, position).getDouble(2);
			
        	for(int i = 0; i < amount; i++)
        	{
        		if(level.random.nextFloat() < getChance(level, chance))
        		{
        	    	if(!level.isClientSide)
        	    	{
        	    		if(level instanceof ServerLevel serverLevel)
        	    			serverLevel.sendParticles(particleIn, x, y, z, 0, 0, 0, 0.01D + i, 0);
        	    	}
        	    	else
        	    	{
        	    		level.addParticle(particleIn, x, y, z, 0, 0.01D, 0);
        	    	}

        	    	if(levelevent != -1)
        	    		level.levelEvent(levelevent, pPos, 0);
        		}
        	}
		}
    }
	
	/*
	 * 
	 * PARTICLE Templates 
	 *
	 */
	public static void playParticleStarEffect(LevelAccessor levelacc, Player pPlayer, ParticleOptions particleIn, double dirMultiplier, EParticlePosition position)
    {
		if (!EffectsConfig.CAN_PARTICLES.get())
			return;
		
		if(dirMultiplier == 0)
			dirMultiplier = 0.05D;

		if (levelacc instanceof Level pLevel)
		{
			double x = getLocation(pLevel, pPlayer, position).getDouble(0);
			double y = getLocation(pLevel, pPlayer, position).getDouble(1);
			double z = getLocation(pLevel, pPlayer, position).getDouble(2);
			
	    	if(!pLevel.isClientSide)
	    	{
	    		if(pLevel instanceof ServerLevel level)
	    		{
	    			level.sendParticles(particleIn, x, y, z, 0, dirMultiplier, -dirMultiplier, 0.0D, 0.2D);
	    			level.sendParticles(particleIn, x, y, z, 0, dirMultiplier, -dirMultiplier, dirMultiplier, 0.2D);
	    			level.sendParticles(particleIn, x, y, z, 0, dirMultiplier, -dirMultiplier, -dirMultiplier, 0.2D);
	    			level.sendParticles(particleIn, x, y, z, 0, -dirMultiplier, -dirMultiplier, 0.0D, 0.2D);
	    			level.sendParticles(particleIn, x, y, z, 0, -dirMultiplier, -dirMultiplier, -dirMultiplier, 0.2D);
	    			level.sendParticles(particleIn, x, y, z, 0, -dirMultiplier, -dirMultiplier, dirMultiplier, 0.2D);
	    			level.sendParticles(particleIn, x, y, z, 0, 0.0D, -dirMultiplier, dirMultiplier, 0.2D);
	    			level.sendParticles(particleIn, x, y, z, 0, 0.0D, -dirMultiplier, -dirMultiplier, 0.2D);
	    		}
	    	}
	    	else
	    	{
    			pLevel.addParticle(particleIn, x, y, z, dirMultiplier, -dirMultiplier, 0.0D);
    			pLevel.addParticle(particleIn, x, y, z, dirMultiplier, -dirMultiplier, dirMultiplier);
    			pLevel.addParticle(particleIn, x, y, z, dirMultiplier, -dirMultiplier, -dirMultiplier);
    			pLevel.addParticle(particleIn, x, y, z, -dirMultiplier, -dirMultiplier, 0.0D);
    			pLevel.addParticle(particleIn, x, y, z, -dirMultiplier, -dirMultiplier, -dirMultiplier);
    			pLevel.addParticle(particleIn, x, y, z, -dirMultiplier, -dirMultiplier, dirMultiplier);
    			pLevel.addParticle(particleIn, x, y, z, 0.0D, -dirMultiplier, dirMultiplier);
    			pLevel.addParticle(particleIn, x, y, z, 0.0D, -dirMultiplier, -dirMultiplier);
	    	}
		}
    }
}
