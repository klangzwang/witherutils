package geni.witherutils.base.common.block.creative;

import org.jetbrains.annotations.Nullable;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import geni.witherutils.core.common.block.WitherEntityBlock;
import geni.witherutils.core.common.blockentity.WitherBlockEntity;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CreativeEnergyBlock extends WitherAbstractBlock implements WitherEntityBlock {

	public CreativeEnergyBlock(BlockBehaviour.Properties props)
    {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.valueOf(false)));
		this.setHasScreen();
        this.setHasTooltip();
    }

	@Override
	public CreativeEnergyBlockItem getBlockItem(Item.Properties properties)
	{
		return new CreativeEnergyBlockItem(this, properties.stacksTo(1));
	}

    @Override
    protected boolean isWaterloggable()
    {
        return true;
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getClockWise());
    }
    
    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder)
    {
    	builder.add(FACING, LIT);
    }
    
	@Override
	public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos)
	{
        return 15;
	}
    
    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter worldIn, BlockPos pos)
    {
        return Block.box(0, 0, 0, 16, 16, 16);
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return Block.box(0, 0, 0, 16, 16, 16);
    }

    @Override
    public <T extends WitherBlockEntity> AbstractContainerMenu getContainer(int id, Inventory inventory, FriendlyByteBuf buffer, BlockPos pos)
    {
        return new CreativeEnergyContainer(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(pos));
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
    {
        return new CreativeEnergyBlockEntity(pPos, pState);
    }
}
