//package geni.witherutils.base.common.block.farmer;
//
//import javax.annotation.Nullable;
//
//import geni.witherutils.base.common.base.WitherAbstractBlock;
//import geni.witherutils.base.common.init.WUTEntities;
//import geni.witherutils.core.common.block.WitherEntityBlock;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.context.BlockPlaceContext;
//import net.minecraft.world.level.BlockGetter;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.EntityBlock;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.entity.BlockEntityTicker;
//import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraft.world.level.block.state.BlockBehaviour;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.block.state.StateDefinition;
//
//public class FarmerBlock extends WitherAbstractBlock implements WitherEntityBlock {
//
//	public FarmerBlock(BlockBehaviour.Properties props)
//	{
//		super(props);
//        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.valueOf(false)));
//	}
//	
//	@Override
//	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
//	{
//		if(placer != null)
//			world.setBlock(pos, state.setValue(FACING, getFacingFromEntity(pos, placer)), 2);
//        BlockEntity te = world.getBlockEntity(pos);
//        if (!world.isClientSide && te instanceof FarmerBlockEntity && placer instanceof Player)
//        {
//            ((FarmerBlockEntity) te).setPlayer((Player) placer);
//        }
//	}
//	
//    @Override
//    public BlockState getStateForPlacement(BlockPlaceContext context)
//    {
//        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
//    }
//    
//    @Override
//    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
//    {
//        builder.add(FACING, LIT);
//    }
//    
//    @Override
//    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos)
//    {
//        Block block = state.getBlock();
//        if(block != this)
//        {
//            return block.getLightEmission(state, level, pos);
//        }
//        return state.getValue(LIT) ? 15 : 0;
//    }
//
//	@Nullable
//	@Override
//	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
//	{
//		return new FarmerBlockEntity(pos, state);
//	}
//}
