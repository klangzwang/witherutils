package geni.witherutils.base.common.block.rack.controller.fluid;

import java.util.ArrayList;
import java.util.List;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import geni.witherutils.base.common.base.WitherFluidHandlerCapability;
import geni.witherutils.base.common.block.rack.IBlockMulti;
import geni.witherutils.base.common.block.tank.drum.TankDrumBlockEntity;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.core.common.util.ItemStackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class ControllerFluidBlock extends WitherAbstractBlock implements IBlockMulti, EntityBlock {
	
    public ControllerFluidBlock(BlockBehaviour.Properties props)
	{
		super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.valueOf(false)));
        this.setHasTooltip();
    }
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context)
	{
		return Shapes.or(
				Block.box(2, 2, 2, 14, 14, 14),
				Block.box(5, 5, 0, 11, 11, 2),
				Block.box(0, 5, 5, 2, 11, 11),
				Block.box(5, 14, 5, 11, 16, 11),
				Block.box(5, 5, 14, 11, 11, 16),
				Block.box(5, 0, 5, 11, 2, 11),
				Block.box(14, 5, 5, 16, 11, 11)
				);
	}
	
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, LIT);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder)
    {
    	return new ArrayList<>();
    }
    
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
	{
		super.setPlacedBy(world, pos, state, placer, stack);
		try
		{
			IFluidHandlerItem storage = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse(null);
			BlockEntity container = world.getBlockEntity(pos);
			if (storage != null && container != null)
			{
				IFluidHandler storageTile = container.getCapability(ForgeCapabilities.FLUID_HANDLER, null).orElse(null);
				if (storageTile != null)
				{
					storageTile.fill(storage.getFluidInTank(0), FluidAction.EXECUTE);
				}
			}
		}
		catch (Exception e)
		{
		}
		world.setBlockAndUpdate(pos, state);
	}

	@Override
	public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, BlockEntity ent, ItemStack stackTool)
	{
		super.playerDestroy(world, player, pos, state, ent, stackTool);
		ItemStack tankStack = new ItemStack(this);
		if(ent != null)
		{
			IFluidHandler fluidInStack = tankStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse(null);
			if (fluidInStack != null && ent instanceof TankDrumBlockEntity)
			{
				ControllerFluidBlockEntity ttank = (ControllerFluidBlockEntity) ent;
				FluidStack fs = ttank.getFluidTank().getFluid();
				((WitherFluidHandlerCapability) fluidInStack).setFluid(fs);
			}
		}
        ItemStackUtil.dropItemStackMotionless(world, pos, tankStack);
	}
    
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new ControllerFluidBlockEntity(pos, state);
	}
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type)
	{
		return createTickerHelper(type, WUTEntities.RACKFLUID_CONTROLLER.get(), ControllerFluidBlockEntity::tick);
	}
}
