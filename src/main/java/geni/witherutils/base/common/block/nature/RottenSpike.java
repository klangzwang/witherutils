package geni.witherutils.base.common.block.nature;

import java.util.Locale;

import org.jetbrains.annotations.Nullable;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import geni.witherutils.base.common.init.WUTParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RottenSpike extends WitherAbstractBlock {

	public static final EnumProperty<SpikeSize> SIZE = EnumProperty.create("size", SpikeSize.class);

	public RottenSpike()
	{
	    super(BlockBehaviour.Properties.of().strength(0.9F).sound(SoundType.STONE).noOcclusion().randomTicks());
	    this.registerDefaultState(this.getStateDefinition().any().setValue(SIZE, SpikeSize.BIG));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		SpikeSize size = SpikeSize.values()[Math.max(0, getBearing(context.getLevel(), context.getClickedPos()) - 1)];
		return defaultBlockState().setValue(SIZE, size);
	}

	private int getBearing(LevelReader world, BlockPos pos)
	{
		return Math.max(getStrength(world, pos.below()), getStrength(world, pos.above()));
	}

	private int getStrength(LevelReader world, BlockPos pos)
	{
		BlockState state = world.getBlockState(pos);
		if(state.isSolidRender(world, pos))
			return 3;
		if(state.getValues().containsKey(SIZE))
			return state.getValue(SIZE).strength;
		return 0;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context)
	{
		return state.getValue(SIZE).shape;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(SIZE);
	}

	@Override
	public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
	{
		return true;
	}
	@Override
	public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face)
	{
		return 5;
	}
	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face)
	{
		return 5;
	}
	
	@Override
	public boolean isPathfindable(BlockState state, PathComputationType type)
	{
		return true;
	}

	@Override
	public @Nullable PathType getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob)
	{
		return PathType.BLOCKED;
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand)
	{
		if(rand.nextInt(50) == 0)
		{
	    	for(int i = 0; i < 40; i++)
	    	{
				double d1 = (double) pos.getX() + 0.25D + level.random.nextDouble() / 2;
				double d2 = (double) pos.getY() + level.random.nextDouble() * (double) 0.5F;
				double d3 = (double) pos.getZ() + 0.25D + level.random.nextDouble() / 2;
				level.addParticle(WUTParticles.RISINGSOUL.get(), d1, d2, d3, 0.0D, 0.1D, 0.0D);
				level.addParticle(ParticleTypes.SMOKE, d1, d2, d3, 0.0D, 0.05D, 0.0D);
	    	}			
		}
	}
	
	public enum SpikeSize implements StringRepresentable
	{
		SMALL(0, 2),
		MEDIUM(1, 4),
		BIG(2, 8);

		SpikeSize(int strength, int width)
		{
			this.strength = strength;
			
			int pad = (16 - width) / 2;
			shape = Block.box(pad, 0, pad, 16 - pad, 16, 16 - pad);
		}

		public final int strength;
		public final VoxelShape shape;

		@Override
		public String getSerializedName()
		{
			return name().toLowerCase(Locale.ROOT);
		}
	}
}
