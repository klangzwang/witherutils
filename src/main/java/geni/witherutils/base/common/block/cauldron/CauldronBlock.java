package geni.witherutils.base.common.block.cauldron;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.init.WUTParticles;
import geni.witherutils.core.common.particle.IntParticleType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemHandlerHelper;

public class CauldronBlock extends WitherAbstractBlock implements EntityBlock {

	private static final VoxelShape INSIDE = box(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	protected static final VoxelShape SHAPE = Shapes.join(Shapes.block(), Shapes.or(box(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), box(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), INSIDE), BooleanOp.ONLY_FIRST);

	public CauldronBlock(BlockBehaviour.Properties props)
	{
		super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.valueOf(false)));
        this.setHasLiquid();
        this.setHasTooltip();
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
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult brtr)
    {
        BlockEntity te = world.getBlockEntity(pos);
        ItemStack heldStack = player.getItemInHand(hand);
        
        if (player.isShiftKeyDown() ||
        		te instanceof MenuProvider ||
        		!world.getBlockState(pos.above()).isAir())
            	return super.use(state, world, pos, player, hand, brtr);
    	
        if (te instanceof CauldronBlockEntity)
        {
        	CauldronBlockEntity cauldron = (CauldronBlockEntity) te;
        	cauldron.onBlockUse(state, player, hand, brtr);

        	if(!cauldron.getInventory().getStackInSlot(0).isEmpty())
			{
    			ItemStack outstack = cauldron.getInventory().extractItem(0, 1, false);
    			ItemHandlerHelper.giveItemToPlayer(player, outstack, player.getInventory().selected);
            	world.playSound(null, pos, SoundEvents.NETHERITE_BLOCK_PLACE, SoundSource.BLOCKS, 0.8f, 1.0f);
            	return InteractionResult.SUCCESS;
			}
			else
			{
				if(heldStack.isEmpty())
				{
		            world.playSound(null, pos, SoundEvents.MUD_BRICKS_PLACE, SoundSource.BLOCKS, 0.8f, 1.0f);
		            return InteractionResult.SUCCESS;
				}
				else
				{
					if(cauldron.getFluidTank().isEmpty())
					{
			            ItemStack stack = heldStack.split(1);
			            player.setItemInHand(hand, heldStack.isEmpty() ? ItemStack.EMPTY : heldStack);
			            ItemHandlerHelper.insertItem(cauldron.getInventory(), stack, false);
			            world.playSound(null, pos, SoundEvents.BONE_BLOCK_PLACE, SoundSource.BLOCKS, 0.8f, 1.0f);
			            return InteractionResult.SUCCESS;
					}
					else
					{
						return super.use(state, world, pos, player, hand, brtr);
					}
				}
			}
        }
		return InteractionResult.SUCCESS;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(final BlockState state, final Level level, final BlockPos pos, final RandomSource r) 
    {
    	if(state.getValue(LIT))
    	{
    		for(Direction facing : Direction.values())
    		{
    			Direction.Axis axis = facing.getAxis();
    			
    			double d3 = axis == Direction.Axis.X ? facing.getStepX() * 0.02D : -facing.getStepX() * 0.02D;
    			double d4 = axis == Direction.Axis.Z ? facing.getStepZ() * 0.02D : -facing.getStepZ() * 0.02D;
    			
    			for(int i = 0; i < 10; i++)
    			{
    				level.addParticle(ParticleTypes.SMOKE,
    						pos.getX() + 0.5D + (facing == Direction.EAST ? 0.7D : facing == Direction.WEST ? -0.7D : 0.0D),
    						pos.getY() + 0.4D,
    						pos.getZ() + 0.5D + (facing == Direction.SOUTH ? 0.7D : facing == Direction.NORTH ? -0.7D : 0.0D),
    						d3, -0.05D, d4);
    			}

    			level.addParticle(ParticleTypes.SOUL_FIRE_FLAME,
    					pos.getX() + r.nextDouble(),
    					pos.getY() + 0.8 + r.nextDouble(),
    					pos.getZ() + r.nextDouble(),
    					0.0D, 0.09D, 0.0D);
    			
                level.addAlwaysVisibleParticle(new IntParticleType.IntParticleData(WUTParticles.BLACKSMOKE.get(), 50, 50, 50, 50),
                		(double)pos.getX() + 0.5D + level.random.nextDouble() / 5.0D * (double)(level.random.nextBoolean() ? 1 : -1),
                		(double)pos.getY() + 1.8D,
                		(double)pos.getZ() + 0.5D + level.random.nextDouble() / 5.0D * (double)(level.random.nextBoolean() ? 1 : -1),
                		0.0D, 0.01D, 0.0D);
                
        		if(r.nextDouble() < 0.1D)
        		{
        			level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
        			level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.SOUL_ESCAPE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
        		}
    		}
    	}
    }
	
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context)
    {
    	return SHAPE;
    }
    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter world, BlockPos pos)
    {
    	return INSIDE;
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

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new CauldronBlockEntity(pos, state);
    }
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> pBlockEntityType)
    {
        return createTickerHelper(pBlockEntityType, WUTEntities.CAULDRON.get(), CauldronBlockEntity::tick);
    }
}
