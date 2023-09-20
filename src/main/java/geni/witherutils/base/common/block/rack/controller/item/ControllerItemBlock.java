package geni.witherutils.base.common.block.rack.controller.item;

import java.util.ArrayList;
import java.util.List;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import geni.witherutils.base.common.block.rack.IBlockMulti;
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
import net.minecraftforge.items.IItemHandler;

public class ControllerItemBlock extends WitherAbstractBlock implements IBlockMulti, EntityBlock {
	
    public ControllerItemBlock(BlockBehaviour.Properties props)
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
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack)
    {
    	try
    	{
    		IItemHandler stackHandler = stack.getCapability(ForgeCapabilities.ITEM_HANDLER, null).orElse(null);
			BlockEntity blockEntity = level.getBlockEntity(pos);
			
			if (stackHandler != null && blockEntity != null)
			{
				IItemHandler controllerHandler = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).orElse(null);
				if (controllerHandler != null)
				{
				}
			}
		}
		catch (Exception e)
		{
			
		}
		level.setBlockAndUpdate(pos, state);
    }

    @Override
    public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, BlockEntity ent, ItemStack stackTool)
    {
        super.playerDestroy(world, player, pos, state, ent, stackTool);

        ItemStack ctrlStack = new ItemStack(this);
        
        if (ent != null)
        {
        	IItemHandler handler = ctrlStack.getCapability(ForgeCapabilities.ITEM_HANDLER, null).orElse(null);
        	if (handler != null && ent instanceof ControllerItemBlockEntity)
        	{
        		ControllerItemBlockEntity controller = (ControllerItemBlockEntity) ent;
        		
            	ItemStack is = controller.getBlStack();
            	((ControllerItemBlockItemHandlerCapability) handler).setStack(is);
            	int stored = controller.getItemsStored();
            	((ControllerItemBlockItemHandlerCapability) handler).setStored(stored);
        	}
        }
        ItemStackUtil.dropItemStackMotionless(world, pos, ctrlStack);
    }
    
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new ControllerItemBlockEntity(pos, state);
	}
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type)
	{
		return createTickerHelper(type, WUTEntities.RACKITEM_CONTROLLER.get(), ControllerItemBlockEntity::tick);
	}
}
