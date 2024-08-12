package geni.witherutils.base.common.block.nature;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RottenRoots extends WitherAbstractBlock {

	protected static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D);

    public RottenRoots()
    {
        super(BlockBehaviour.Properties.of().noOcclusion().noCollission().randomTicks().instabreak().sound(SoundType.GRASS).dynamicShape().offsetType(BlockBehaviour.OffsetType.XZ));
    }

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos)
	{
		if(!stateIn.canSurvive(worldIn, currentPos))
		{
			return Blocks.AIR.defaultBlockState();
		}
		return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
	{
		Vec3 vec3d = state.getOffset(worldIn, pos);
		return SHAPE.move(vec3d.x, vec3d.y, vec3d.z);
	}
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext selectionContext)
	{
		return Block.box(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
	}

	@Override
	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player)
	{
		super.playerWillDestroy(level, pos, state, player);

        if(player.getItemInHand(InteractionHand.MAIN_HAND) == null || !(player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ShearsItem))
        {
        	Inventory inventory = player.getInventory();
            if(inventory.getArmor(0) != ItemStack.EMPTY && inventory.getArmor(1) != ItemStack.EMPTY)
            	return state;
        	player.addEffect(new MobEffectInstance(MobEffects.POISON, 200));
        	level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_HURT_SWEET_BERRY_BUSH, player.getSoundSource(), 1.0F, 1.0F);
        }
    	return state;
	}
}
