package geni.witherutils.base.common.block.totem;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import geni.witherutils.core.common.block.WitherEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TotemBlock extends WitherAbstractBlock implements WitherEntityBlock {

	private static final VoxelShape SHAPE1 = Block.box(4.0D, 3.0D, 4.0D, 12.0D, 5.85D, 12.0D);
	private static final VoxelShape SHAPE2 = Block.box(3.5D, 1.0D, 3.5D, 12.5D, 3.0D, 12.5D);
	private static final VoxelShape SHAPE3 = Block.box(2.5D, 0.0D, 2.5D, 13.5D, 1.0D, 13.5D);
	private static final VoxelShape SHAPE_AABB = Shapes.or(SHAPE1, SHAPE2, SHAPE3);	
	
	public TotemBlock(BlockBehaviour.Properties props)
	{
		super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.valueOf(false)));
        this.setHasTooltip();
	}

	@Override
	public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_)
	{
		return SHAPE_AABB;
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
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos)
    {
        Block block = state.getBlock();
        if(block != this)
        {
            return block.getLightEmission(state, level, pos);
        }
        return state.getValue(LIT) ? 15 : 0;
    }

//    @Override
//    public InteractionResult use(BlockState state, Level level, BlockPos worldPosition, Player player, InteractionHand hand, BlockHitResult hit)
//    {
//		ItemStack heldStack = player.getItemInHand(hand);
//		
//		BlockEntity tileEntity = level.getBlockEntity(worldPosition);
//		if(heldStack.isEmpty() && player.isCrouching())
//		{
//			if(!level.isClientSide)
//			{
//				if(tileEntity instanceof MenuProvider)
//				{
//					NetworkHooks.openScreen((ServerPlayer) player, (MenuProvider) tileEntity, tileEntity.getBlockPos());
//				}
//				else
//				{
//					throw new IllegalStateException("Our named container provider is missing!");
//				}
//			}
//			return InteractionResult.SUCCESS;
//		}
//		
//    	level.addParticle(ParticleTypes.SMOKE, worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5D, 0.1D, -0.05D, 0.1D);
//    	level.addParticle(ParticleTypes.SMOKE, worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5D, -0.1D, -0.05D, -0.1D);
//    	level.addParticle(ParticleTypes.SMOKE, worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5D, 0.1D, -0.05D, -0.1D);
//    	level.addParticle(ParticleTypes.SMOKE, worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5D, -0.1D, -0.05D, 0.1D);
//    	level.playLocalSound(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.PLAYERS, 0.75f, 1.0f, false);
//		
//		if(tileEntity instanceof TotemBlockEntity)
//		{
//			TotemBlockEntity totem = (TotemBlockEntity) tileEntity;
//
//			if(!totem.getInventory().getStackInSlot(0).isEmpty())
//			{
//	        	ItemStack stack = totem.getInventory().extractItem(0, 64, false);
//	            ItemEntity entityItem = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1.2, worldPosition.getZ() + 0.5, stack);
//	            entityItem.setPickUpDelay(20);
//	            level.addFreshEntity(entityItem);
//			}
//			if(totem.getInventory().getStackInSlot(0).isEmpty())
//			{
//				List<Item> item = AttractionHandlers.instance.getItemRegistry();
//				for (int i = 0; i < item.size(); i++)
//				{
//					if(heldStack.getItem() == item.get(i))
//					{
//						ItemStack excess = totem.getInventory().insertItem(0, player.getItemInHand(hand), false).split(1);
//						player.setItemInHand(hand, excess);
//					}
//				}
//			}
//		}
//        return InteractionResult.SUCCESS;
//    }
    
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new TotemBlockEntity(pos, state);
    }
}
