package geni.witherutils.base.common.block.greenhouse;

import javax.annotation.Nonnull;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import geni.witherutils.base.common.config.common.BlocksConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;

public class GreenhouseBlock extends WitherAbstractBlock {

    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty CORNER_NORTH_WEST = BooleanProperty.create("corner_north_west");
    public static final BooleanProperty CORNER_NORTH_EAST = BooleanProperty.create("corner_north_east");
    public static final BooleanProperty CORNER_SOUTH_EAST = BooleanProperty.create("corner_south_east");
    public static final BooleanProperty CORNER_SOUTH_WEST = BooleanProperty.create("corner_south_west");
    public static final BooleanProperty CORNER_ROD = BooleanProperty.create("corner_rod");

    public GreenhouseBlock(BlockBehaviour.Properties props)
	{
		super(props);
		this.registerDefaultState(this.getStateDefinition().any().setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false).setValue(CORNER_NORTH_WEST, false).setValue(CORNER_NORTH_EAST, false).setValue(CORNER_SOUTH_EAST, false).setValue(CORNER_SOUTH_WEST, false).setValue(CORNER_ROD, false));
    }

    private VoxelShape getShape(BlockState state)
    {
        return Block.box(0, 0, 0, 16, 2, 16);
    }
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext selectionContext)
    {
    	return Block.box(0, 0, 0, 16, 2, 16);
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter worldIn, Entity entityIn)
    {
    }

    @Override
    public boolean addLandingEffects(BlockState state1, ServerLevel level, BlockPos pos, BlockState state2, LivingEntity entity, int numberOfParticles)
    {
        return true;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor level, BlockPos pos, BlockPos newPos)
    {
        return this.getPanelState(state, level, pos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.getPanelState(this.defaultBlockState(), context.getLevel(), context.getClickedPos());
    }
    private BlockState getPanelState(BlockState state, LevelAccessor level, BlockPos pos)
    {
        boolean north = level.getBlockState(pos.north()).getBlock() == this;
        boolean east = level.getBlockState(pos.east()).getBlock() == this;
        boolean south = level.getBlockState(pos.south()).getBlock() == this;
        boolean west = level.getBlockState(pos.west()).getBlock() == this;
        boolean cornerNorthWest = north && west && level.getBlockState(pos.north().west()).getBlock() != this;
        boolean cornerNorthEast = north && east && level.getBlockState(pos.north().east()).getBlock() != this;
        boolean cornerSouthEast = south && east && level.getBlockState(pos.south().east()).getBlock() != this;
        boolean cornerSouthWest = south && west && level.getBlockState(pos.south().west()).getBlock() != this;
        boolean cornerRod = level.getBlockState(pos.below()).getBlock() instanceof CropBlock;

        return state.setValue(NORTH, north).setValue(EAST, east).setValue(SOUTH, south).setValue(WEST, west).setValue(CORNER_NORTH_WEST, cornerNorthWest).setValue(CORNER_NORTH_EAST, cornerNorthEast).setValue(CORNER_SOUTH_EAST, cornerSouthEast).setValue(CORNER_SOUTH_WEST, cornerSouthWest).setValue(CORNER_ROD, cornerRod);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if(!state.is(newState.getBlock()))
        {
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(NORTH);
        builder.add(EAST);
        builder.add(SOUTH);
        builder.add(WEST);
        builder.add(CORNER_NORTH_WEST);
        builder.add(CORNER_NORTH_EAST);
        builder.add(CORNER_SOUTH_EAST);
        builder.add(CORNER_SOUTH_WEST);
        builder.add(CORNER_ROD);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context)
    {
        return this.getShape(state);
    }
    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter reader, BlockPos pos)
    {
        return this.getShape(state);
    }

	@Override
	public boolean isRandomlyTicking(BlockState state)
	{
		return true;
	}
	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
	{
		if(level.isClientSide)
			return;
		performTick(level, pos, random, state);
	}
	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moving)
	{
		super.onPlace(state, world, pos, oldState, moving);
		world.scheduleTick(pos, this, 3);
	}
	@SuppressWarnings("deprecation")
	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
	{
		super.tick(state, level, pos, random);
		if(level == null || level.isClientSide)
			return;
		performTick(level, pos, random, state);
		level.scheduleTick(pos, this, 3);
	}

    @SuppressWarnings("deprecation")
    public void performTick(ServerLevel worldIn, BlockPos pos, RandomSource random, BlockState state)
    {
        if(!worldIn.isAreaLoaded(pos, 1))
            return;
    	
    	if(calculateLightRatio(worldIn, pos) > 0)
    	{
            if(worldIn.canSeeSkyFromBelowWater(pos))
            {
                if(worldIn.getBlockState(pos.below(1)).getBlock() instanceof IPlantable)
                {
                	BlockPos croppos = pos.below();
                    BlockState cropstate = worldIn.getBlockState(croppos);
                    Block cropBlock = cropstate.getBlock();
                    Block farmBlock = worldIn.getBlockState(croppos.relative(Direction.DOWN)).getBlock();
                    
                	int moisture = worldIn.getBlockState(croppos.relative(Direction.DOWN)).getValue(FarmBlock.MOISTURE);

                    if(cropstate != null && farmBlock instanceof FarmBlock farmland)
                    {
                    	if(BlocksConfig.GREENHOUSEMOISTURE.get())
                    	{
                    		if(moisture == 0)
                    			return;
                    	}
                        if(cropBlock instanceof CropBlock crop)
                        {
                            final int currentAge = crop.getAge(cropstate);
                            final int maxAge = crop.getMaxAge();

                            if(currentAge == maxAge)
                            {
                                return;
                            }
                            else
                            {
                                float f = getGrowthChance(cropstate.getBlock(), worldIn, pos.below(1));
                                if(net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos.below(1), cropstate, random.nextInt((int) (2.0F + BlocksConfig.GREENHOUSECHANCE.get() / f) + 1) == 0))
                                {
                                    crop.growCrops(worldIn, pos.below(1), cropstate);
                                    worldIn.levelEvent(1505, pos.below(1), 0);
                                    net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos.below(1), cropstate);
                                }
                            }
                        }
                    }
                }
            }
    	}
    }
    
	private float calculateLightRatio(@Nonnull Level level, BlockPos pos)
	{
		int lightValue = level.getLightEngine().getLayerListener(LightLayer.SKY).getLightValue(pos);
		float sunAngle = level.getSunAngle(1.0F);
		if(sunAngle < (float) Math.PI)
		{
			sunAngle += (0.0F - sunAngle) * 0.2F;
		}
		else
		{
			sunAngle += (((float) Math.PI * 2F) - sunAngle) * 0.2F;
		}
		lightValue = Math.round(lightValue * Mth.cos(sunAngle));
		lightValue = Mth.clamp(lightValue, 0, 15);

		return lightValue / 15f;
	}
	
    protected static float getGrowthChance(Block blockIn, LevelReader worldIn, BlockPos pos)
    {
        float f = 1.0F;
        BlockPos blockpos = pos.below();

        for(int i = -1; i <= 1; ++i)
        {
            for(int j = -1; j <= 1; ++j)
            {
                float f1 = 0.0F;
                BlockState blockstate = worldIn.getBlockState(blockpos.offset(i, 0, j));
                if(blockstate.canSustainPlant(worldIn, blockpos.offset(i, 0, j), Direction.UP, (net.minecraftforge.common.IPlantable) blockIn))
                {
                    f1 = 1.0F;
                    if(blockstate.isFertile(worldIn, pos.offset(i, 0, j)))
                    {
                        f1 = 3.0F;
                    }
                }
                if(i != 0 || j != 0)
                {
                    f1 /= 4.0F;
                }
                f += f1;
            }
        }

        BlockPos blockpos1 = pos.north();
        BlockPos blockpos2 = pos.south();
        BlockPos blockpos3 = pos.west();
        BlockPos blockpos4 = pos.east();

        boolean flag = blockIn == worldIn.getBlockState(blockpos3).getBlock() || blockIn == worldIn.getBlockState(blockpos4).getBlock();
        boolean flag1 = blockIn == worldIn.getBlockState(blockpos1).getBlock() || blockIn == worldIn.getBlockState(blockpos2).getBlock();
        
        if(flag && flag1)
        {
            f /= 2.0F;
        }
        else
        {
            boolean flag2 =
                       blockIn == worldIn.getBlockState(blockpos3.north()).getBlock()
                    || blockIn == worldIn.getBlockState(blockpos4.north()).getBlock()
                    || blockIn == worldIn.getBlockState(blockpos4.south()).getBlock()
                    || blockIn == worldIn.getBlockState(blockpos3.south()).getBlock();
            if(flag2)
            {
                f /= 2.0F;
            }
        }
        return f;
    }
}
