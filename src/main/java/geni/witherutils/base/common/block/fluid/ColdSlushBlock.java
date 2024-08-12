package geni.witherutils.base.common.block.fluid;

import java.util.HashMap;

import geni.witherutils.base.common.init.WUTFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.phys.Vec3;

public class ColdSlushBlock extends LiquidBlock {

	private final HashMap<BlockState, BlockState> collisionMap = new HashMap<>();
	private final HashMap<Block, BlockState> anyState = new HashMap<>();

    public ColdSlushBlock(FlowingFluid supplier, Properties props)
    {
        super(supplier, props);
	}
    
	@Override
	public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos)
	{
		return WUTFluids.COLDSLUSH_FLUID_TYPE.get().getLightLevel();
	}
	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos)
	{
		return true;
	}

	@Override
	public void animateTick(BlockState state, Level worldIn, BlockPos pos, RandomSource random)
	{
		BlockPos blockpos = pos.above();
		BlockState blockstate = worldIn.getBlockState(blockpos);

		if(!blockstate.isFaceSturdy(worldIn, blockpos, Direction.UP))
		{
			double d1 = (double) pos.getX() + random.nextDouble();
			double d2 = (double) pos.getY() + 0.1F + state.getFluidState().getHeight(worldIn, blockpos);
			double d3 = (double) pos.getZ() + random.nextDouble();
			
			if(random.nextFloat() < 0.1F)
			{
				worldIn.addParticle(ParticleTypes.SNOWFLAKE, d1, d2, d3, 0.0D, 0.05D, 0.0D);
			}
		}
	}

	@Override
	public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entity)
	{
		if(entity instanceof ItemEntity || entity instanceof ExperienceOrb || entity instanceof Player)
			return;

		entity.makeStuckInBlock(state, new Vec3(0.2D, (double)0.61F, 0.25D));

		if(worldIn.isClientSide())
		{
			return;
		}
		if(worldIn.getGameTime() % 8 != 0) 
		{
			return;
		}
		if(entity.onGround())
		{
			entity.remove(RemovalReason.KILLED);
		}

		if(entity instanceof LivingEntity && !(entity instanceof Player) && entity.isAlive())
		{
			LivingEntity living = (LivingEntity) entity;
			if(living.isAlive() && !(living instanceof SnowGolem))
			{
				living.remove(RemovalReason.KILLED);
				SnowGolem snowman = EntityType.SNOW_GOLEM.create(worldIn);
				snowman.moveTo(living.getX(), living.getY(), living.getZ(), living.xRotO, living.yRotO);
				snowman.setHealth(1);
				worldIn.addFreshEntity((Entity) snowman);
			}
		}
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldstate, boolean moving)
	{
		super.onPlace(state, level, pos, oldstate, moving);
		addInteractions();
		level.scheduleTick(pos, this, 10);
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
	{
		super.tick(state, level, pos, random);
		if(level == null || level.isClientSide)
			return;
		checkForInteraction(level, pos);
		level.scheduleTick(pos, this, 10);
	}

	/**
	 * 
	 * INTERACTIONS
	 * 
	 */
	public boolean addInteraction(Block preBlock, Block postBlock)
	{
		if(preBlock == null || postBlock == null)
		{
			return false;
		}
		return addInteraction(preBlock.defaultBlockState(), postBlock.defaultBlockState(), true);
	}
	public boolean addInteraction(BlockState pre, BlockState post)
	{
		return addInteraction(pre, post, false);
	}
	public boolean addInteraction(BlockState pre, BlockState post, boolean anyState)
	{
		if(pre == null || post == null)
		{
			return false;
		}
		if(anyState)
		{
			this.anyState.put(pre.getBlock(), post);
		}
		else
		{
			collisionMap.put(pre, post);
		}
		return true;
	}
	public boolean addInteraction(BlockState pre, Block postBlock)
	{
		return addInteraction(pre, postBlock.defaultBlockState(), false);
	}
	public boolean hasInteraction(BlockState state)
	{
		return collisionMap.containsKey(state) || anyState.containsKey(state.getBlock());
	}
	protected void checkForInteraction(Level world, BlockPos pos)
	{
		if(world.getBlockState(pos).getBlock() != this)
		{
			return;
		}
		for(Direction face : Direction.values())
		{
			interactWithBlock(world, pos.relative(face));
		}
		interactWithBlock(world, pos.offset(-1, 0, -1));
		interactWithBlock(world, pos.offset(-1, 0, 1));
		interactWithBlock(world, pos.offset(1, 0, -1));
		interactWithBlock(world, pos.offset(1, 0, 1));
	}
	public BlockState getInteraction(BlockState state)
	{
		if(collisionMap.containsKey(state))
		{
			return collisionMap.get(state);
		}
		return anyState.get(state.getBlock());
	}
	protected void interactWithBlock(Level world, BlockPos pos)
	{
		BlockState state = world.getBlockState(pos);
		if(state.isAir() || state.getBlock() == this)
			return;

		if(hasInteraction(state))
		{
			world.setBlock(pos, getInteraction(state), 3);
		}
		else if(state.isSolidRender(world, pos) && world.isEmptyBlock(pos.relative(Direction.UP)))
		{
			world.setBlock(pos.relative(Direction.UP), Blocks.SNOW.defaultBlockState().setValue(SnowLayerBlock.LAYERS, world.random.nextInt(3) + 1), 3);
		}
	}
	public void addInteractions()
	{
		addInteraction(Blocks.GRASS_BLOCK, Blocks.DIRT);
		addInteraction(Blocks.WATER.defaultBlockState(), Blocks.ICE);
		addInteraction(Blocks.WATER, Blocks.SNOW);
		addInteraction(Blocks.WATER.defaultBlockState(), Blocks.ICE);
		addInteraction(Blocks.WATER.defaultBlockState(), Blocks.POWDER_SNOW);
		addInteraction(Blocks.LAVA.defaultBlockState(), Blocks.OBSIDIAN);
		addInteraction(Blocks.TALL_GRASS, Blocks.AIR);
		addInteraction(Blocks.LAVA, Blocks.STONE);
		addInteraction(Blocks.FIRE, Blocks.AIR);
	}
}