package geni.witherutils.core.common.util;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class BlockUtil {
	
    public static List<BlockPos> getBlockPosInAABB(AABB axisAlignedBB) {
        List<BlockPos> blocks = new ArrayList<>();
        for (double y = axisAlignedBB.minY; y < axisAlignedBB.maxY; ++y) {
            for (double x = axisAlignedBB.minX; x < axisAlignedBB.maxX; ++x) {
                for (double z = axisAlignedBB.minZ; z < axisAlignedBB.maxZ; ++z) {
                    blocks.add(new BlockPos((int) x, (int) y, (int) z));
                }
            }
        }
        return blocks;
    }
    
	public static BlockPos getLastAirBlockBelow(Level world, BlockPos pos) {
		return getLastAirBlock(world, pos, Direction.DOWN);
	}

	public static BlockPos getLastAirBlock(Level world, BlockPos pos, Direction direction) {
		int increment;
		if (direction == Direction.DOWN) {
			increment = -1;
		} else {
			increment = 1;
		}
		BlockPos posCurrent;
		BlockPos posPrevious = pos;
		for (int y = pos.getY(); y < 256 && y > 0; y += increment) {
			posCurrent = new BlockPos(pos.getX(), y, pos.getZ());
			if (!world.isEmptyBlock(posCurrent)) {
				return posPrevious;
			}
			posPrevious = posCurrent;
		}
		return pos;
	}

	public static BlockPos[] getAdjacentBlocks(BlockPos blockPos)
	{
		return new BlockPos[]
		{
				blockPos.relative(Direction.NORTH), 
				blockPos.relative(Direction.SOUTH),
				blockPos.relative(Direction.EAST), 
				blockPos.relative(Direction.WEST),
				blockPos.relative(Direction.UP)
		};
	}

	public static float getRotationFromDirection(Direction direction)
	{
		switch (direction) {
			case NORTH:
				return 180F;
			case SOUTH:
				return 0F;
			case WEST:
				return 90F;
			case EAST:
				return -90F;
			case DOWN:
				return -90f;
			case UP:
				return 90f;
			default:
				return 0f;
		}
	}

	public static ItemEntity dropItemStackInWorld(Level worldObj, Vec3i pos, @Nonnull ItemStack stack)
	{
		return dropItemStackInWorld(worldObj, pos.getX(), pos.getY(), pos.getZ(), stack);
	}

	public static ItemEntity dropItemStackInWorld(Level worldObj, double x, double y, double z, @Nonnull ItemStack stack)
	{
		float f = 0.7F;
		float d0 = worldObj.random.nextFloat() * f + (1.0F - f) * 0.5F;
		float d1 = worldObj.random.nextFloat() * f + (1.0F - f) * 0.5F;
		float d2 = worldObj.random.nextFloat() * f + (1.0F - f) * 0.5F;
		ItemEntity entityitem = new ItemEntity(worldObj, x + d0, y + d1, z + d2, stack);
		entityitem.setDefaultPickUpDelay();
		if (stack.hasTag()) {
			entityitem.getItem().setTag(stack.getTag().copy());
		}
		worldObj.addFreshEntity(entityitem);
		return entityitem;
	}

	public static BlockEntity getTileInDirection(BlockEntity tile, Direction direction) {
		final BlockPos offset = tile.getBlockPos().relative(direction);
		return tile.getLevel().getBlockEntity(offset);
	}

	public static BlockEntity getTileInDirection(Level world, BlockPos coord, Direction direction) {
		return world.getBlockEntity(coord.relative(direction));
	}

	public static BlockEntity getTileInDirectionSafe(Level world, BlockPos coord, Direction direction) {
		BlockPos n = coord.relative(direction);
		return world.isLoaded(n)? world.getBlockEntity(n) : null;
	}

	public static AABB expandAround(BlockPos pos, int x, int y, int z) {
		return new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + x, pos.getY() + y, pos.getZ() + z);
	}

	public static AABB expandAround(BlockPos pos, double x, double y, double z) {
		return new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + x, pos.getY() + y, pos.getZ() + z);
	}

	public static AABB singleBlock(BlockPos pos) {
		return new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
	}

	public static void playSoundAtPos(Level world, BlockPos pos, SoundEvent sound, SoundSource category, float volume, float pitch) {
		world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, sound, category, volume, pitch);
	}
	
	public static AABB aabbOffset(BlockPos pos, AABB aabb)
	{
		return aabb.expandTowards(pos.getX(), pos.getY(), pos.getZ());
	}

	public static AABB aabbOffset(BlockPos pos, double x1, double y1, double z1, double x2, double y2, double z2)
	{
		return new AABB(pos.getX() + x1, pos.getY() + y1, pos.getZ() + z1, pos.getX() + x2, pos.getY() + y2, pos.getZ() + z2);
	}

	public static AABB getPixeledAABB(double x0, double y0, double z0, double x1, double y1, double z1)
	{
		return new AABB(x0 / 16.0, y0 / 16.0, z0 / 16.0, x1 / 16.0, y1 / 16.0, z1 / 16.0);
	}
	
	/*
	 * 
	 * FLUID
	 * 
	 */
    public static boolean isFullFluidBlock(Level world, BlockPos pos)
    {
        return isFullFluidBlock(world.getBlockState(pos), world, pos);
    }

    public static boolean isFullFluidBlock(BlockState state, Level world, BlockPos pos)
    {
        Block block = state.getBlock();
        if (block instanceof IFluidBlock)
        {
            FluidStack fluid = ((IFluidBlock) block).drain(world, pos, FluidAction.EXECUTE);
            return fluid == null || fluid.getAmount() > 0;
        }
        else if (block instanceof LiquidBlock)
        {
            int level = state.getValue(LiquidBlock.LEVEL);
            return level == 0;
        }
        return false;
    }

    public static Fluid getFluid(Level world, BlockPos pos)
    {
        FluidStack fluid = drainBlock(world, pos, FluidAction.EXECUTE);
        return fluid != null ? fluid.getFluid() : null;
    }

    public static Fluid getFluidWithFlowing(Level world, BlockPos pos)
    {
        BlockState fluidState = world.getBlockState(pos);
        Fluid fluid = fluidState.getFluidState().getType();
        
        if (fluid == Fluids.FLOWING_WATER)
        {
            return Fluids.WATER;
        }
        if (fluid == Fluids.FLOWING_LAVA)
        {
            return Fluids.LAVA;
        }
        return getFluid(fluid);
    }

    public static Fluid getFluid(Fluid fluid)
    {
        if (fluid instanceof IFluidBlock)
        {
            return getFluid(((IFluidBlock) fluid).getFluid());
        }
        return Fluids.EMPTY;
    }
    public static Fluid getFluid(Block block)
    {
        if (block instanceof IFluidBlock fluid)
        {
            return getFluid(fluid.getFluid());
        }
        return Fluids.EMPTY;
    }
    
    @SuppressWarnings("deprecation")
	public static Fluid getFluidWithoutFlowing(BlockState state)
    {
        Block block = state.getBlock();
        if (block instanceof IFluidBlock)
        {
            if (((IFluidBlock) block).getFluid().isSource(block.getFluidState(state)))
            {
                return getFluid(block);
            }
        }
        if (block instanceof LiquidBlock)
        {
            if (state.getValue(LiquidBlock.LEVEL) != 0)
            {
                return null;
            }
            if (block == Blocks.WATER || getFluid(block) == Fluids.FLOWING_WATER)
            {
                return Fluids.WATER;
            }
            if (block == Blocks.LAVA || getFluid(block) == Fluids.FLOWING_LAVA)
            {
                return Fluids.LAVA;
            }
            return Fluids.EMPTY;
        }
        return null;
    }

    public static Fluid getFluidWithFlowing(Block block)
    {
        Fluid fluid = null;
        if (block == Blocks.LAVA || getFluid(block) == Fluids.FLOWING_LAVA)
        {
            fluid = Fluids.LAVA;
        }
        else if (block == Blocks.WATER || getFluid(block) == Fluids.FLOWING_WATER)
        {
            fluid = Fluids.WATER;
        }
        else if (block instanceof IFluidBlock)
        {
            fluid = ((IFluidBlock) block).getFluid();
        }
        return fluid;
    }

    public static FluidStack drainBlock(Level world, BlockPos pos, FluidAction doDrain)
    {
    	IFluidHandler fluidHandler = FluidUtil.getFluidHandler(world, pos, null).orElse(null);
    	
    	if(fluidHandler == null)
    		return FluidStack.EMPTY;

    	if(fluidHandler.drain(FluidType.BUCKET_VOLUME, doDrain) != null)
    	{
    		FluidStack fstack = fluidHandler.drain(FluidType.BUCKET_VOLUME, doDrain);
    		return fstack;	
    	}
    	else
    	{
    		return FluidStack.EMPTY;
    	}
    }
}
