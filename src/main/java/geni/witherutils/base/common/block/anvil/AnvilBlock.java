package geni.witherutils.base.common.block.anvil;

import org.jetbrains.annotations.Nullable;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.base.common.item.hammer.HammerItem;
import geni.witherutils.core.common.block.WitherEntityBlock;
import geni.witherutils.core.common.util.ParticleUtil;
import geni.witherutils.core.common.util.ParticleUtil.EParticlePosition;
import geni.witherutils.core.common.util.ParticleUtil.ERandomChance;
import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AnvilBlock extends WitherAbstractBlock implements WitherEntityBlock {

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
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context)
	{
		Direction direction = state.getValue(FACING);
		return direction.getAxis() == Direction.Axis.X ? X_AXIS_AABB : Z_AXIS_AABB;
	}
    
    @Override
    public ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult)
    {
        BlockEntity te = pLevel.getBlockEntity(pPos);

        if (pPlayer.isShiftKeyDown() || te instanceof MenuProvider || !pLevel.getBlockState(pPos.above()).isAir())
        	return super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
        else if (te instanceof AnvilBlockEntity anvil)
        {
            if (!pLevel.isClientSide)
            {
                if (anvil.getInventory().getStackInSlot(0).isEmpty())
                {
                	if (pPlayer.getItemInHand(pHand).getItem() != WUTItems.HAMMER.get())
                	{
                   		ParticleUtil.playParticleDistrib(pLevel, pPlayer, pPos, ParticleTypes.SMOKE, EParticlePosition.BLOCKRANDOM, ERandomChance.RARELY, 5, -1);
                   		SoundUtil.playSoundDistrib(pLevel, pPos, SoundEvents.PLAYER_ATTACK_STRONG, 0.75f, 1.0F, false, true);

                        ItemStack excess = anvil.getInventory().insertItem(0, pPlayer.getItemInHand(pHand), false);
                        if (!pPlayer.isCreative())
                        {
                        	pPlayer.setItemInHand(pHand, excess);
                        }
                	}
                }
                else
                {
            		if(pPlayer.getCooldowns().isOnCooldown(pPlayer.getItemInHand(pHand).getItem()))
            			return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
                	
                	if (pPlayer.getItemInHand(pHand).getItem() instanceof HammerItem ||
                		pPlayer.getItemInHand(pHand).getItem() == WUTItems.HAMMER.get())
                	{
                   		ParticleUtil.playParticleStarEffect(pLevel, pPlayer, ParticleTypes.SMOKE, 0.5D, EParticlePosition.HITRESULT);
                   		ParticleUtil.playParticleDistrib(pLevel, pPlayer, pPos, ParticleTypes.LAVA, EParticlePosition.BLOCKRANDOM, ERandomChance.RARELY, 1, 1503);
                		SoundUtil.playSoundDistrib(pLevel, pPos, WUTSounds.HAMMERHIT.get(), 0.75f, 0.5F, false, true);

                		anvil.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
                	}
                	else
                	{
                        ItemStack stack = anvil.getInventory().extractItem(0, 64, false);
                        ItemEntity entityItem = new ItemEntity(pLevel, pPos.getX() + 0.5, pPos.getY() + 0.6, pPos.getZ() + 0.5, stack);
                        entityItem.setDeltaMovement(0, 0, 0);
                        entityItem.setPickUpDelay(20);
                        pLevel.addFreshEntity(entityItem);
                	}
                }
            }
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
    {
        return new AnvilBlockEntity(pPos, pState);
    }
}
