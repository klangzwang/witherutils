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
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
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
	public AngelBlockItem getBlockItem(Item.Properties properties)
	{
		return new AngelBlockItem(this, properties.stacksTo(12));
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder)
	{
		List<ItemStack> dropsOriginal = new ArrayList<>();
		
//		Collection<RecipeHolder<?>> list = builder.getLevel().getServer().getRecipeManager().getRecipes();
//		for(RecipeHolder<?> recipe : list)
//		{
//			if(recipe.value().getType() == RecipeType.CRAFTING)
//			{
//				if(new ItemStack(state.getBlock()).getItem() == recipe.value().getResultItem(builder.getLevel().registryAccess()).getItem() &&
//						ItemStack.matches(new ItemStack(state.getBlock()), recipe.value().getResultItem(builder.getLevel().registryAccess())))
//				{
//					System.out.println("");
//					dropsOriginal = recipe.value().getIngredients().stream().flatMap(ingredient -> Arrays.stream(ingredient.getItems()).filter(stack ->
//					!stack.hasCraftingRemainingItem()).findAny().map(Stream::of).orElseGet(Stream::empty)).collect(Collectors.toList());
//				}
//			}
//		}

		dropsOriginal.add(new ItemStack(WUTBlocks.CASE.get(), 1));
		dropsOriginal.add(new ItemStack(Items.GOLD_NUGGET, 4));
		dropsOriginal.add(new ItemStack(Items.FEATHER, 4));
		return dropsOriginal;
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit)
	{
		world.scheduleTick(pos, this, 2);
		return super.useWithoutItem(state, world, pos, player, hit);
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
