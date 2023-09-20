package geni.witherutils.base.common.item.fertilizer;

import geni.witherutils.api.farm.IFertilizer;
import geni.witherutils.api.farm.IFertilizerResult;
import geni.witherutils.base.common.base.WitherItem;
import geni.witherutils.core.common.fertilizer.FertilizerResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BambooSaplingBlock;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.BigDripleafBlock;
import net.minecraft.world.level.block.BigDripleafStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.FungusBlock;
import net.minecraft.world.level.block.GlowLichenBlock;
import net.minecraft.world.level.block.HangingRootsBlock;
import net.minecraft.world.level.block.MangroveRootsBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.NetherrackBlock;
import net.minecraft.world.level.block.NyliumBlock;
import net.minecraft.world.level.block.RootsBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.WitherRoseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;

public class MealItem extends WitherItem implements IFertilizer {
	
    private static final RandomSource random = RandomSource.create();
    
	public MealItem(Properties properties)
	{
		super(properties);
	}

    @Override
	public InteractionResult useOn(UseOnContext context)
    {
        final ItemStack stack = context.getItemInHand();
        final Level world = context.getLevel();
        final BlockPos pos = context.getClickedPos();
        final BlockState state = world.getBlockState(pos);

        if (useOnGround(stack, world, pos, state)) return InteractionResult.sidedSuccess(world.isClientSide());
        return InteractionResult.PASS;
    }

    public static boolean useOnGround(ItemStack stack, Level world, BlockPos pos, BlockState state)
    {
        final Block block = state.getBlock();

        if (block instanceof WitherRoseBlock)                return false;
        if (block instanceof RootsBlock)                     return false;
        if (block instanceof NetherrackBlock netherrack)     return handleVanillaBonemeal(world, stack, pos, netherrack, state, 0.5);
        if (block instanceof NyliumBlock nylium)             return handleVanillaBonemeal(world, stack, pos, nylium, state, 0.5);
        if (block instanceof FungusBlock fungus)             return handleVanillaBonemeal(world, stack, pos, fungus, state);
        if (block instanceof TallFlowerBlock)                return replaceTallBlockWith(world, stack, pos, state, Blocks.WITHER_ROSE);
        if (block instanceof FlowerBlock)                    return replaceWith(world, stack, pos, Blocks.WITHER_ROSE);
        if (block instanceof NetherWartBlock)                return handleNetherWart(world, stack, pos, state);
        if (block instanceof StemBlock)                      return handleAgedBlock(world, stack, pos, state, BlockStateProperties.AGE_7);
        if (block instanceof TallFlowerBlock)                return replaceTallBlockWith(world, stack, pos, state, Blocks.AIR);
        if (block instanceof MangroveRootsBlock)             return replaceWith(world, stack, pos, Blocks.AIR);
        if (block instanceof HangingRootsBlock)              return replaceWith(world, stack, pos, Blocks.AIR);
        if (block instanceof SugarCaneBlock)                 return replaceWith(world, stack, pos, Blocks.AIR);
        if (block instanceof CactusBlock)                    return replaceWith(world, stack, pos, Blocks.AIR);
        if (block instanceof BigDripleafBlock)               return replaceWith(world, stack, pos, Blocks.AIR);
        if (block instanceof BigDripleafStemBlock)           return replaceWith(world, stack, pos, Blocks.AIR);
        if (block instanceof BambooStalkBlock)               return replaceWith(world, stack, pos, Blocks.AIR);
        if (block instanceof BambooSaplingBlock)             return replaceWith(world, stack, pos, Blocks.AIR);
        if (block instanceof GlowLichenBlock)                return replaceWith(world, stack, pos, Blocks.AIR);
        if (block instanceof VineBlock)                      return replaceWith(world, stack, pos, Blocks.AIR);

        return false;
    }

    private static boolean handleVanillaBonemeal(Level world, ItemStack stack, BlockPos pos, BonemealableBlock block, BlockState state)
    {
        return handleVanillaBonemeal(world, stack, pos, block, state, 0);
    }
    private static boolean handleVanillaBonemeal(Level world, ItemStack stack, BlockPos pos, BonemealableBlock block, BlockState state,  double particleYOffset)
    {
        if (!block.isValidBonemealTarget(world, pos, state, world.isClientSide)) return false;
        if (world instanceof ServerLevel serverLevel) block.isBonemealSuccess(serverLevel, random, pos, state);
        onSuccess(world, stack, pos, particleYOffset);
        return true;
    }
    private static boolean handleNetherWart(Level world, ItemStack stack, BlockPos pos, BlockState state)
    {
        int newAge = state.getValue(BlockStateProperties.AGE_3) + 1;
        if (newAge > BlockStateProperties.MAX_AGE_3) return false;
        return replaceWith(world, stack, pos, state.setValue(BlockStateProperties.AGE_3, newAge));
    }
    private static boolean handleAgedBlock(Level world, ItemStack stack, BlockPos pos, BlockState state, IntegerProperty ageProperty)
    {
        int newAge = state.getValue(ageProperty) - 1;
        if (newAge < 0) return replaceWith(world, stack, pos, Blocks.AIR);
        return replaceWith(world, stack, pos, state.setValue(ageProperty, newAge));
    }
    private static boolean replaceTallBlockWith(Level world, ItemStack stack, BlockPos pos, BlockState state, Block replaceWith)
    {
        if (state.getValue(TallFlowerBlock.HALF).equals(DoubleBlockHalf.UPPER)) pos = pos.below();
        return replaceWith(world, stack, pos, replaceWith);
    }
    private static boolean replaceWith(Level world, ItemStack stack, BlockPos pos, Block replaceWith)
    {
        return replaceWith(world, stack, pos, replaceWith, 0);
    }
    private static boolean replaceWith(Level world, ItemStack stack, BlockPos pos, Block replaceWith, double particleYOffset) {
        return replaceWith(world, stack, pos, replaceWith.defaultBlockState(), particleYOffset);
    }
    private static boolean replaceWith(Level world, ItemStack stack, BlockPos pos, BlockState replaceWith) {
        return replaceWith(world, stack, pos, replaceWith, 0);
    }
    private static boolean replaceWith(Level world, ItemStack stack, BlockPos pos, BlockState replaceWith, double particleYOffset)
    {
        onSuccess(world, stack, pos, particleYOffset);
        setBlock(world, pos, replaceWith);
        return true;
    }
    private static void setBlock(Level world, BlockPos pos, BlockState newState)
    {
        if (world.isClientSide()) return;
        world.setBlockAndUpdate(pos, newState);
    }
    private static void onSuccess(Level world, ItemStack stack, BlockPos pos, double particleYOffset)
    {
        addSmokeParticles(world, Vec3.atCenterOf(pos).add(0, particleYOffset, 0));
        playUseSound(world, pos);
        stack.shrink(1);
    }
    private static void playUseSound(Level world, BlockPos pos)
    {
        if (world.isClientSide()) return;
        world.playSound(null, pos, SoundEvents.WITHER_SHOOT, SoundSource.BLOCKS, 0.1f, 1.25f);
    }

    private static void addSmokeParticles(Level world, Vec3 pos)
    {
        if (!world.isClientSide()) return;
        for (int i = 0; i < 64; i++)
        {
            double velX = random.nextGaussian() * 0.02;
            double velY = random.nextGaussian() * 0.02;
            double velZ = random.nextGaussian() * 0.02;
            double x = pos.x() + random.nextDouble();
            double y = pos.y() + random.nextDouble();
            double z = pos.z() + random.nextDouble();
            world.addParticle(ParticleTypes.SMOKE, x, y, z, velX, velY, velZ);
        }
    }

	@Override
	public IFertilizerResult apply(ItemStack stack, Player player, Level level, BlockPos pos)
	{
		ItemStack before = player.getItemInHand(InteractionHand.MAIN_HAND);
		player.setItemInHand(InteractionHand.MAIN_HAND, stack);
		InteractionResult res = stack.getItem().useOn(new UseOnContext(level, player, InteractionHand.MAIN_HAND, before, null));
		ItemStack after = player.getItemInHand(InteractionHand.MAIN_HAND);
		player.setItemInHand(InteractionHand.MAIN_HAND, before);
		return new FertilizerResult(after, res != InteractionResult.PASS);
	}
	@Override
	public boolean applyOnAir()
	{
		return false;
	}
	@Override
	public boolean applyOnPlant()
	{
		return true;
	}
	@Override
	public boolean matches(ItemStack stack)
	{
		return ItemStack.matches(this.getDefaultInstance(), stack);
	}
}
