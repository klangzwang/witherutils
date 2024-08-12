package geni.witherutils.base.common.block.fluid;

import geni.witherutils.base.common.init.WUTFluids;
import geni.witherutils.core.common.util.SoundUtil;
import geni.witherutils.core.common.util.TeleportUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

public class PortiumBlock extends LiquidBlock {

    public PortiumBlock(FlowingFluid supplier, Properties props)
    {
        super(supplier, props);
	}
    
	@Override
	public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos)
	{
		return WUTFluids.PORTIUM_FLUID_TYPE.get().getLightLevel();
	}
	
	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos)
	{
		return true;
	}

	@Override
	public void animateTick(BlockState state, Level worldIn, BlockPos pos, RandomSource random)
	{
		RandomSource RANDOM = RandomSource.create();
        if (RANDOM.nextInt(100) == 0)
        {
        	for(int i = 0; i < 20; i++)
        	{
				worldIn.addParticle(ParticleTypes.PORTAL,
						pos.getX() + random.nextDouble(),
						pos.getY() + random.nextDouble(),
						pos.getZ() + random.nextDouble(),
						0.0D, 0.01D, 0.0D);
        	}
        }
        if (RANDOM.nextInt(100) == 0)
        {
        	for(int i = 0; i < 20; i++)
        	{
				worldIn.addParticle(ParticleTypes.REVERSE_PORTAL,
						pos.getX() + random.nextDouble(),
						pos.getY() + random.nextDouble(),
						pos.getZ() + random.nextDouble(),
						0.0D, 0.01D, 0.0D);
        	}
        }
	}

	@SuppressWarnings("deprecation")
	@Override
	public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entity)
	{
		if(entity instanceof ItemEntity || entity instanceof ExperienceOrb)
			return;

		if(worldIn.isClientSide())
		{
			return;
		}
		if(worldIn.getGameTime() % 8 == 0)
		{
			BlockPos randPos = pos.offset(-8 + worldIn.random.nextInt(17), worldIn.random.nextInt(8), -8 + worldIn.random.nextInt(17));

			if(!worldIn.getBlockState(randPos).isSolid())
			{
				if(entity instanceof LivingEntity)
				{
					TeleportUtil.teleportEntityTo(entity, randPos);
				}
				else
				{
					entity.setPos(pos.getX(), pos.getY(), pos.getZ());
					SoundUtil.playSoundDistrib(worldIn, pos, SoundEvents.ENDERMAN_TELEPORT, 1.f, 1.f, false, true);
				}
			}
		}
	}
}