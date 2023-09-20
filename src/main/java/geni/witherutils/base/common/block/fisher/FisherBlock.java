package geni.witherutils.base.common.block.fisher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import geni.witherutils.base.common.init.WUTEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

public class FisherBlock extends WitherAbstractBlock implements EntityBlock {
	
	@Nonnull
	public static final EnumProperty<FisherBlockType> BLOCK_TYPE = EnumProperty.<FisherBlockType> create("blocktype", FisherBlockType.class);
	
	public FisherBlock(BlockBehaviour.Properties props)
	{
		super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(BLOCK_TYPE, FisherBlockType.SINGLE));
        this.setHasTooltip();
	}
	
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		FisherBlockType type = world.getBlockState(pos).getValue(BLOCK_TYPE);
		BlockPos masterPos = type.getLocationOfMaster(pos);
		if(masterPos != null)
		{
			if(!world.isClientSide)
			{
				FisherBlockEntity tp = (FisherBlockEntity) world.getBlockEntity(masterPos);
				if(tp != null)
				{
					NetworkHooks.openScreen((ServerPlayer) player, (MenuProvider) tp.getMasterBe(), tp.getMasterBe().getBlockPos());
				}
			}
		}
		return InteractionResult.SUCCESS;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context)
	{
		VoxelShape shape = Shapes.box(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
	    if (state.getBlock() == this)
	    {
	    	FisherBlockType bt = state.getValue(BLOCK_TYPE);
	    	if (bt != FisherBlockType.MASTER)
	    		shape = Shapes.box(0.0, 0.0, 0.0, 1.0, 0.125, 1.0);
	    }
	    return shape;
	}
	
	public @Nonnull BlockState getStateFromType(int types)
	{
		return defaultBlockState().setValue(BLOCK_TYPE, FisherBlockType.getType(types));
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(BLOCK_TYPE);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx)
	{
		Level worldIn = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();

	    BlockPos swCorner = findSouthWestCorner(worldIn, pos);
	    BlockPos masterPos = getMasterPosForNewMB(worldIn, swCorner, pos);

	    if (masterPos == null)
	    {
			return super.getStateForPlacement(ctx);
	    }

	    FisherBlockType myType = null;
	    for (FisherBlockType bt : FisherBlockType.values())
	    {
	    	BlockPos test = bt.getLocationOfMaster(pos);
	    	if (test != null && test.equals(masterPos))
	    	{
	    		myType = bt;
	    	}
	    }
	    if (myType == null)
	    {
			return super.getStateForPlacement(ctx);
	    }
	    if (!worldIn.isClientSide)
	    {
	    	updateMultiBlock(worldIn, masterPos, pos, true);
	    }
		return getStateFromType(myType.ordinal());
	}

	private void updateMultiBlock(Level world, BlockPos masterPos, BlockPos ignorePos, boolean form)
	{
	    for (FisherBlockType type : FisherBlockType.values())
	    {
	    	if (type != FisherBlockType.SINGLE)
	    	{
	    		Vec3i offset = type.getOffsetFromMaster();
	    		BlockPos targetPos = new BlockPos(masterPos.getX() + offset.getX(), masterPos.getY() + offset.getY(), masterPos.getZ() + offset.getZ());

	    		if (!targetPos.equals(ignorePos))
	    		{
	    			FisherBlockType setToType = FisherBlockType.SINGLE;
	    			if (form)
	    			{
	    				setToType = type;
	    			}
	    			world.setBlock(targetPos, defaultBlockState().setValue(BLOCK_TYPE, setToType), 3);
	    		}
	    	}
	    }
	}

	private BlockPos getMasterPosForNewMB(@Nonnull Level worldIn, @Nonnull BlockPos swCorner, @Nonnull BlockPos ignorePos)
	{
		BlockPos testPos = swCorner;
	    for (int i = 0; i < 3; i++)
	    {
	    	testPos = swCorner.relative(Direction.NORTH, i);
	    	for (int j = 0; j < 3; j++)
	    	{
	    		if (!testPos.equals(ignorePos) && !isSingle(worldIn, testPos))
	    		{
	    			return null;
	    		}
	    		testPos = testPos.east();
	    	}
	    }
	    return swCorner.relative(Direction.NORTH).relative(Direction.EAST);
	}

	private @Nonnull BlockPos findSouthWestCorner(@Nonnull Level worldIn, @Nonnull BlockPos pos)
	{
	    BlockPos res = pos;
	    int i = 0;
	    while (isSingle(worldIn, res.south()) && i < 2)
	    {
	    	res = res.south();
	    	i++;
	    }
	    i = 0;
	    while (isSingle(worldIn, res.west()) && i < 2)
	    {
	    	res = res.west();
	    	i++;
	    }
	    return res;
	}

	private boolean isSingle(@Nonnull Level worldIn, @Nonnull BlockPos pos)
	{
	    BlockState bs = worldIn.getBlockState(pos);
	    if (bs.getBlock() == this)
	    {
	    	return bs.getValue(BLOCK_TYPE) == FisherBlockType.SINGLE;
	    }
	    return false;
	}

	@Override
	public void destroy(@Nonnull LevelAccessor worldIn, @Nonnull BlockPos pos, @Nonnull BlockState state)
	{
		if (state.getBlock() == this)
		{
			FisherBlockType type = state.getValue(BLOCK_TYPE);
			if (type != FisherBlockType.SINGLE)
			{
				BlockPos masterPos = type.getLocationOfMaster(pos);
				updateMultiBlock((Level) worldIn, masterPos, pos, false);
			}
		}
	}

	@Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
		return new FisherBlockEntity(pos, state);
    }
	@Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> pBlockEntityType)
    {
		return createTickerHelper(pBlockEntityType, WUTEntities.FISHER.get(), FisherBlockEntity::tick);
    }
}
