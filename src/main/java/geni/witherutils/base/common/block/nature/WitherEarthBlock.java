package geni.witherutils.base.common.block.nature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
import net.minecraft.world.ticks.TickPriority;

public class WitherEarthBlock extends WitherAbstractBlock {

	public static final int MAX_DECAY = 8;
	public static final IntegerProperty DECAY = IntegerProperty.create("decay", 0, MAX_DECAY);

	public WitherEarthBlock(BlockBehaviour.Properties props)
	{
		super(props);
		this.registerDefaultState(this.getStateDefinition().any().setValue(DECAY, Integer.valueOf(0)));
	}

	public static void startFastSpread(Level worldIn, BlockPos pos)
	{
		Block earth = WUTBlocks.WITHEREARTH.get().defaultBlockState().getBlock();
		if(earth instanceof WitherEarthBlock)
		{
			BlockState cursedEarthState = earth.defaultBlockState();
			worldIn.setBlock(pos, cursedEarthState, 2);
			worldIn.scheduleTick(pos, earth, 3, TickPriority.HIGH);
//			worldIn.levelEvent(1027, pos, 0);
		}
	}
	
	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moving)
	{
		super.onPlace(state, world, pos, oldState, moving);
		world.scheduleTick(pos, this, 3);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
	{
		super.tick(state, level, pos, random);
		
		level.scheduleTick(pos, this, 3);
		
		if(level == null || level.isClientSide)
			return;
		
		List<BlockPos> list = BlockPos.betweenClosedStream(pos.offset(-1, 0, -1), pos.offset(1, 0, 1)).map(BlockPos::immutable).collect(Collectors.toList());
		Collections.shuffle(list);
		
		for(BlockPos listPos : list)
		{
			if(listPos.getY() > level.getMinBuildHeight() && listPos.getY() < level.getMaxBuildHeight() && level.isLoaded(listPos))
			{
				BlockState stateAround = level.getBlockState(listPos);
				BlockState stateAbove = level.getBlockState(listPos.above());
				if(stateAround.getBlock() == Blocks.GRASS_BLOCK || stateAround.getBlock() == Blocks.DIRT)
				{
					if(!stateAbove.isAir())
					{
						if(stateAbove.getBlock() instanceof BushBlock)
							level.destroyBlock(listPos.above(1), false);
					}
					
					int decay = MAX_DECAY + 1;
					
					for(BlockPos offsetPos : BlockPos.MutableBlockPos.betweenClosed(listPos.offset(-1, -1, -1), listPos.offset(1, 1, 1)))
					{
						BlockState blockState = level.getBlockState(offsetPos);
						if(blockState.getBlock() == this)
							decay = Math.min(decay, blockState.getValue(DECAY) + 1);
					}

					if(random.nextBoolean())
						decay++;
					
					if(decay > MAX_DECAY)
						return;

					level.destroyBlock(listPos, false);
					level.setBlockAndUpdate(listPos, this.defaultBlockState().setValue(DECAY, decay));
//					level.levelEvent(2001, pos, 0);
					level.getBlockTicks().hasScheduledTick(listPos, this);
				}
			}
		}
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder)
	{
		List<ItemStack> dropsOriginal = new ArrayList<>();
		dropsOriginal.add(new ItemStack(Blocks.DIRT, 1));
		return dropsOriginal;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(DECAY);
	}
	
	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
	{
		if(random.nextInt(24) == 0)
		{
			level.playLocalSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D,
					SoundEvents.SOUL_ESCAPE, SoundSource.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
		}
		if(level.getBlockState(pos.above()).isAir())
		{
			double d1 = (double) pos.getX() + random.nextDouble();
			double d2 = (double) (pos.getY() + 1) - random.nextDouble() * (double) 0.1F;
			double d3 = (double) pos.getZ() + random.nextDouble();
			level.addParticle(WUTParticles.RISINGSOUL.get(), d1, d2, d3, 0.0D, 0.0D, 0.0D);
			level.addParticle(ParticleTypes.SMOKE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
		}
	}
	
	/*
	 * 
	 * SPAWNMOB
	 * 
	 */
	@SuppressWarnings("deprecation")
	public void spawnMob(ServerLevel level, BlockPos pos)
	{
		Holder<Biome> biomeHolder = level.getBiome(pos);
		Biome biome = level.registryAccess().registry(Registries.BIOME).flatMap(reg -> reg.getOptional(Biomes.PLAINS)).orElseGet(biomeHolder::value);
		List<SpawnerData> spawns = biome.getMobSettings().getMobs(MobCategory.MONSTER).unwrap();
		
		if (!spawns.isEmpty())
		{
			int indexSize = spawns.size();
			EntityType<?> type = spawns.get(level.getRandom().nextInt(indexSize)).type;

			if (type == null || !NaturalSpawner.isSpawnPositionOk(SpawnPlacements.getPlacementType(type), level, pos.above(), type))
				return;
			
			Mob entity = (Mob) type.create(level);

			if (entity != null)
			{
				entity.setPos(pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 0.5D);
				
				if(level.getEntities(entity.getType(), entity.getBoundingBox(), EntitySelector.ENTITY_STILL_ALIVE).isEmpty() && level.noCollision(entity))
				{
					entity.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.NATURAL, null, null);
					level.addFreshEntity(entity);
				}
			}
		}
	}
}