package geni.witherutils.base.common.block.angel;

import java.util.ArrayList;
import java.util.List;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import geni.witherutils.base.common.init.WUTBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
import net.minecraft.world.phys.BlockHitResult;

public class AngelBlock extends WitherAbstractBlock implements Fallable {

	public AngelBlock(BlockBehaviour.Properties props)
	{
		super(props);
		this.setHasTooltip();
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder)
	{
		List<ItemStack> dropsOriginal = new ArrayList<>();
		dropsOriginal.add(new ItemStack(WUTBlocks.CASE_BIG.get(), 1));
		dropsOriginal.add(new ItemStack(Items.GOLD_NUGGET, 4));
		dropsOriginal.add(new ItemStack(Items.FEATHER, 4));
		return dropsOriginal;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		world.scheduleTick(pos, this, 2);
		return super.use(state, world, pos, player, hand, hit);
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
	{
		if(level.getBlockState(pos.below()).isAir() && pos.getY() >= level.getMinBuildHeight())
		{
			FallingBlockEntity fallingblockentity = FallingBlockEntity.fall(level, pos, state);
			this.falling(fallingblockentity);
		}
	}

	protected void falling(FallingBlockEntity entity)
	{
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
	{
		if(random.nextInt(16) == 0)
		{
			BlockPos blockpos = pos.below();
			if(level.getBlockState(blockpos).isAir())
			{
				double d0 = (double) pos.getX() + random.nextDouble();
				double d1 = (double) pos.getY() - 0.05D;
				double d2 = (double) pos.getZ() + random.nextDouble();

				level.addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, state), d0, d1, d2, 0.0D, 0.0D, 0.0D);
			}
		}

	}
	public int getDustColor(BlockState state, BlockGetter level, BlockPos pos)
	{
		return -16777216;
	}
}
