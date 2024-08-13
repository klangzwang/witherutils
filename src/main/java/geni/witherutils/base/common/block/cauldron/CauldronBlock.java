package geni.witherutils.base.common.block.cauldron;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import geni.witherutils.base.common.init.WUTParticles;
import geni.witherutils.core.common.block.WitherEntityBlock;
import geni.witherutils.core.common.util.ParticleUtil;
import geni.witherutils.core.common.util.ParticleUtil.EParticlePosition;
import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class CauldronBlock extends WitherAbstractBlock implements WitherEntityBlock {

	private static final VoxelShape INSIDE = box(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	protected static final VoxelShape SHAPE = Shapes.join(Shapes.block(), Shapes.or(box(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), box(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), INSIDE), BooleanOp.ONLY_FIRST);

	public CauldronBlock(BlockBehaviour.Properties props)
	{
		super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.valueOf(false)));
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
    public ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult)
    {
        BlockEntity te = pLevel.getBlockEntity(pPos);

        if (pPlayer.isShiftKeyDown() || !pLevel.getBlockState(pPos.above()).isAir())
        	return super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
        else if (te instanceof CauldronBlockEntity cauldron)
        {
            if (!pLevel.isClientSide)
            {
            	if(pPlayer.getItemInHand(pHand).getItem() instanceof BucketItem)
            		return cauldron.onBlockEntityUsed(pState, pLevel, pPos, pPlayer, pHand, pHitResult);

        		if(pPlayer.getItemInHand(pHand).isEmpty())
        			SoundUtil.playSoundDistrib(pLevel, pPos, SoundEvents.PLAYER_ATTACK_STRONG, 0.75f, 1.0F, false, true);
        		else
        		{
            		if (!cauldron.getInventory().getStackInSlot(0).isEmpty())
            		{
                        ItemStack stack = cauldron.getInventory().extractItem(0, 64, false);
                        ItemEntity entityItem = new ItemEntity(pLevel, pPos.getX() + 0.5, pPos.getY() + 0.6, pPos.getZ() + 0.5, stack);
                        entityItem.setDeltaMovement(
                        		-0.5 + pLevel.random.nextFloat(),
                        		0.25,
                        		-0.5 + pLevel.random.nextFloat());
                        entityItem.setPickUpDelay(20);
                        pLevel.addFreshEntity(entityItem);
                        SoundUtil.playSoundDistrib(pLevel, pPos, SoundEvents.NETHERITE_BLOCK_PLACE, 0.75f, 1.0F, false, true);
            		}
            		else
            		{
            			if(cauldron.getFluidHandler(null).getFluidInTank(0).isEmpty())
            			{
                            ItemStack excess = cauldron.getInventory().insertItem(0, pPlayer.getItemInHand(pHand), false);
                            SoundUtil.playSoundDistrib(pLevel, pPos, SoundEvents.BONE_BLOCK_PLACE, 0.75f, 1.0F, false, true);
                            if (!pPlayer.isCreative())
                            {
                            	pPlayer.setItemInHand(pHand, excess);
                            }
            			}
            		}
        		}

        		ParticleUtil.playParticleStarEffect(pLevel, pPlayer, ParticleTypes.SMOKE, 0.5D, EParticlePosition.HITRESULT);
                return ItemInteractionResult.SUCCESS;
            }
        }
        return super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
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
    			
    			level.addParticle(WUTParticles.BLACKSMOKE.get(),
                		(double)pos.getX() + 0.5D + level.random.nextDouble() / 5.0D * (double)(level.random.nextBoolean() ? 1 : -1),
                		(double)pos.getY() + 1.8D,
                		(double)pos.getZ() + 0.5D + level.random.nextDouble() / 5.0D * (double)(level.random.nextBoolean() ? 1 : -1),
    					0.0D, 0.01D, 0.0D);
                
        		if(r.nextDouble() < 0.1D)
        		{
        			level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
        			level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.SOUL_ESCAPE.value(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
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
}
