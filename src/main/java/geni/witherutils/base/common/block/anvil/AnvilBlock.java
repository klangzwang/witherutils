package geni.witherutils.base.common.block.anvil;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import geni.witherutils.base.common.base.WitherMachineBlockEntity;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.base.common.item.hammer.HammerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
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
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AnvilBlock extends WitherAbstractBlock implements EntityBlock {

	private static final VoxelShape BASE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D);
	private static final VoxelShape X_LEG1 = Block.box(3.0D, 4.0D, 4.0D, 13.0D, 5.0D, 12.0D);
	private static final VoxelShape X_LEG2 = Block.box(4.0D, 5.0D, 6.0D, 12.0D, 10.0D, 10.0D);
	private static final VoxelShape X_TOP = Block.box(0.0D, 10.0D, 3.0D, 16.0D, 16.0D, 13.0D);
	private static final VoxelShape Z_LEG1 = Block.box(4.0D, 4.0D, 3.0D, 12.0D, 5.0D, 13.0D);
	private static final VoxelShape Z_LEG2 = Block.box(6.0D, 5.0D, 4.0D, 10.0D, 10.0D, 12.0D);
	private static final VoxelShape Z_TOP = Block.box(3.0D, 10.0D, 0.0D, 13.0D, 16.0D, 16.0D);
	private static final VoxelShape X_AXIS_AABB = Shapes.or(BASE, X_LEG1, X_LEG2, X_TOP);
	private static final VoxelShape Z_AXIS_AABB = Shapes.or(BASE, Z_LEG1, Z_LEG2, Z_TOP);

    public AnvilBlock(BlockBehaviour.Properties props)
    {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.valueOf(false)));
        this.setHasTooltip();
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
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context)
	{
		Direction direction = state.getValue(FACING);
		return direction.getAxis() == Direction.Axis.X ? X_AXIS_AABB : Z_AXIS_AABB;
	}
	
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult brtr)
    {
        BlockEntity te = world.getBlockEntity(pos);
        ItemStack heldStack = player.getItemInHand(hand);
        
        if (player.isShiftKeyDown() ||
        		te instanceof MenuProvider ||
        		!world.getBlockState(pos.above()).isAir())
            	return super.use(state, world, pos, player, hand, brtr);

        if (te instanceof AnvilBlockEntity)
        {
        	AnvilBlockEntity anvil = (AnvilBlockEntity) te;
        	if (heldStack.getItem() instanceof HammerItem || heldStack.getItem() == WUTItems.HAMMER.get())
			{
        		  world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5D, pos.getY() + 1.1, pos.getZ() + 0.5D, 0.1D, -0.05D, 0.1D);
              	  world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5D, pos.getY() + 1.1, pos.getZ() + 0.5D, -0.1D, -0.05D, -0.1D);
              	  world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5D, pos.getY() + 1.1, pos.getZ() + 0.5D, 0.1D, -0.05D, -0.1D);
              	  world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5D, pos.getY() + 1.1, pos.getZ() + 0.5D, -0.1D, -0.05D, 0.1D);
              	  world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), WUTSounds.HAMMERHIT.get(), SoundSource.PLAYERS, 0.75f, 0.5F * ((world.random.nextFloat() - world.random.nextFloat()) * 0.7F + 1.8F), false);
                  if(world.random.nextFloat() < 0.4f)
                	  world.addParticle(ParticleTypes.LAVA, pos.getX() + world.random.nextDouble(), pos.getY() + 1.0, pos.getZ() + world.random.nextDouble(), 0.0D, 0.01D, 0.0D);
                  
	              return super.use(state, world, pos, player, hand, brtr);
			}
        	else
        	{
	              if (anvil.getInventory().getStackInSlot(0).isEmpty())
	              {
	              	world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5D, pos.getY() + 1.1, pos.getZ() + 0.5D, 0.1D, -0.05D, 0.1D);
	              	world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5D, pos.getY() + 1.1, pos.getZ() + 0.5D, -0.1D, -0.05D, -0.1D);
	              	world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5D, pos.getY() + 1.1, pos.getZ() + 0.5D, 0.1D, -0.05D, -0.1D);
	              	world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5D, pos.getY() + 1.1, pos.getZ() + 0.5D, -0.1D, -0.05D, 0.1D);
	              	world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.PLAYERS, 0.75f, 1.0f, false);
	              	
	                  ItemStack excess = anvil.getInventory().insertItem(0, player.getItemInHand(hand), false);
	                  if (!player.isCreative()) player.setItemInHand(hand, excess);
	              }
	              else
	              {
	
	              	ItemStack stack = anvil.getInventory().extractItem(0, 64, false);
	                  ItemEntity entityItem = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, stack);
	                  entityItem.setPickUpDelay(20);
	                  world.addFreshEntity(entityItem);
	              }
	              
	              return super.use(state, world, pos, player, hand, brtr);
        	}
        }
        return super.use(state, world, pos, player, hand, brtr);
    }
	
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new AnvilBlockEntity(pos, state);
    }
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> pBlockEntityType)
    {
        return createTickerHelper(pBlockEntityType, WUTEntities.ANVIL.get(), WitherMachineBlockEntity::tick);
    }
}
