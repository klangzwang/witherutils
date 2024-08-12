package geni.witherutils.base.common.block.fluid;

import java.util.ArrayList;
import java.util.List;

import geni.witherutils.base.common.init.WUTFluids;
import geni.witherutils.core.common.util.FacingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

public class SoulfulBlock extends LiquidBlock {

	private boolean hasFoundIron;
	
    public SoulfulBlock(FlowingFluid supplier, Properties props)
    {
        super(supplier, props);
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
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity)
	{
		hasFoundIron = false;
		if(entity instanceof ExperienceOrb)
			return;
		else
		{
			if(entity instanceof ItemEntity itement)
			{
				if(itement.getItem().getItem() == Items.IRON_INGOT)
				{
					hasFoundIron = true;
					return;
				}
			}
		}
		super.entityInside(state, level, pos, entity);
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
		
		
		List<BlockPos> coalPos = new ArrayList<>();
		for(var faces : FacingUtil.FACES_AROUND_Y)
		{
			BlockPos fromPosAround = pos.relative(faces);
			if(level.getBlockState(fromPosAround).getBlock() == Blocks.COAL_BLOCK)
			{
				coalPos.add(fromPosAround);
			}
		}
		
		if(coalPos.size() == 4 && hasFoundIron)
		{
			System.out.println("READY");
		}
	}
}
