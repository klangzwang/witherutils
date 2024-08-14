package geni.witherutils.base.common.block.nature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.mojang.blaze3d.systems.RenderSystem;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.common.base.WitherAbstractBlock;
import geni.witherutils.base.common.block.nature.RottenSpike.SpikeSize;
import geni.witherutils.base.common.entity.portal.Portal;
import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTParticles;
import geni.witherutils.base.common.init.WUTTags;
import geni.witherutils.core.common.helper.LightHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.ticks.TickPriority;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

public class RottenEarth extends WitherAbstractBlock {

	public static final int MAX_DECAY = 8;
	public static final IntegerProperty DECAY = IntegerProperty.create("decay", 0, MAX_DECAY);
	public static final LinkedList<Entity> spawnerClients = new LinkedList<>();
	
	public RottenEarth()
	{
		super(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT));
		this.registerDefaultState(this.getStateDefinition().any().setValue(DECAY, Integer.valueOf(0)));
        NeoForge.EVENT_BUS.register(new EventHandler());
	}

	public static void startFastSpread(Level worldIn, BlockPos pos)
	{
		Block earth = WUTBlocks.ROTTEN_EARTH.get().defaultBlockState().getBlock();
		if(earth instanceof RottenEarth)
		{
			BlockState cursedEarthState = earth.defaultBlockState();
			worldIn.setBlock(pos, cursedEarthState, 2);
			worldIn.scheduleTick(pos, earth, 10, TickPriority.HIGH);
			worldIn.levelEvent(1027, pos, 0);
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
					SoundEvents.SOUL_ESCAPE.value(), SoundSource.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
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
     * GENERATE
     * 
     */
	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moving)
	{
		super.onPlace(state, world, pos, oldState, moving);
		world.scheduleTick(pos, this, 3);
	}
	
	@Override
	public void tick(BlockState bstate, ServerLevel level, BlockPos pos, RandomSource random)
	{
		super.tick(bstate, level, pos, random);
		if(level == null || level.isClientSide)
			return;
		performTick(level, pos, random);
		level.scheduleTick(pos, this, 3);
	}
	
	public void performTick(Level levelIn, BlockPos pos, RandomSource rand)
    {
		ServerLevel level = (ServerLevel) levelIn;
		float light = LightHelper.calculateLight(level, pos.above());

		if(light >= 0.45F)
		{
			if(rand.nextInt(5) == 0)
			{
				for(int i = 1; i < 3; i++)
				{
					if(!level.getBlockState(pos.above(i)).isAir())
					{
						level.setBlock(pos.above(i), Blocks.FIRE.defaultBlockState(), 11);
					}
					else
					{
						level.setBlock(pos.above(), Blocks.FIRE.defaultBlockState(), 11);
						level.setBlockAndUpdate(pos, Blocks.MUD.defaultBlockState());
					}
				}
			}
		}
		else
			spread(level, pos, rand);
    }

	public void spread(ServerLevel level, BlockPos pos, RandomSource random)
	{
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
					
					level.scheduleTick(pos, this, decay > MAX_DECAY ? 30 : 3);
					
					for(BlockPos offsetPos : BlockPos.MutableBlockPos.betweenClosed(listPos.offset(-1, -1, -1), listPos.offset(1, 1, 1)))
					{
						BlockState blockState = level.getBlockState(offsetPos);
						if(blockState.getBlock() == this)
							decay = Math.min(decay, blockState.getValue(DECAY) + 1);
					}

					if(random.nextBoolean())
						decay++;
					
					if(decay > MAX_DECAY)
					{
						if(level.getDifficulty() != Difficulty.PEACEFUL)
						{
							AABB areaToCheck = new AABB(pos).inflate(7, 2, 7);
							int entityCount = level.getEntitiesOfClass(Mob.class, areaToCheck, entity -> entity != null && entity instanceof Enemy).size();
							
							if (entityCount < 4)
							{
								spawnMob(level, pos);
							}
						}
						return;
					}

					level.destroyBlock(listPos, false);
					level.setBlockAndUpdate(listPos, this.defaultBlockState().setValue(DECAY, decay));

					if(level.random.nextFloat() < 0.1F)
					{
						BlockPos spikepos = new BlockPos(pos.getX(), pos.getY()+1, pos.getZ());
						tryPlaceSpikes(level, spikepos, random);
					}
					if(level.random.nextFloat() < 0.075F && level.getBlockState(pos.above()).isAir())
					{
						BlockState roots = WUTBlocks.ROTTEN_ROOTS.get().defaultBlockState();
						level.setBlock(pos.above(), roots, 3);
					}
					if(level.random.nextFloat() < 0.075F && level.getBlockState(pos.above()).isAir())
					{
						BlockState rootspot = WUTBlocks.ROTTEN_ROOTS_POT.get().defaultBlockState();
						level.setBlock(pos.above(), rootspot, 3);
					}
					if(level.random.nextFloat() < 0.025F && level.getBlockState(pos.above()).isAir() && level.canSeeSky(pos.above()))
					{
						BlockState sapling = WUTBlocks.ROTTEN_SAPLING.get().defaultBlockState().setValue(RottenSapling.STAGE, 1);
						level.setBlock(pos.above(), sapling, 3);
					}

					level.levelEvent(2001, pos, 0);
					level.getBlockTicks().hasScheduledTick(listPos, this);
				}
			}
		}
	}
    
	public void tryPlaceSpikes(Level level, BlockPos pos, RandomSource random)
	{
		int size = random.nextInt(3) == 0 ? 2 : 3;
		for(int i = 0; i < size; i++)
		{
			if(!level.isEmptyBlock(pos.above(i)))
				return;
			SpikeSize sizeType = SpikeSize.values()[size - i - 1];
			BlockState targetBlock = WUTBlocks.ROTTEN_SPIKE.get().defaultBlockState().setValue(RottenSpike.SIZE, sizeType);
			level.setBlock(pos.above(i), targetBlock, 3);
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
		Biome biome = !biomeHolder.is(WUTTags.Biomes.HOSTILE_OVERRIDE) ? biomeHolder.value() : level.registryAccess().registry(Registries.BIOME)
				.flatMap(reg -> reg.getOptional(Biomes.PLAINS))
				.orElseGet(biomeHolder::value);
		
		List<SpawnerData> spawns = biome.getMobSettings().getMobs(MobCategory.MONSTER).unwrap();

		if (!spawns.isEmpty())
		{
			int indexSize = spawns.size();
			EntityType<?> type = spawns.get(level.getRandom().nextInt(indexSize)).type;
			
			Mob entity = (Mob) type.create(level);
			
			if (type.is(WUTTags.Entities.NO_DIRT_SPAWN))
				return;
//			if (type == null || !NaturalSpawner.isValidPositionForMob(level, entity, 10))
//				return;

			if (entity != null)
			{
				entity.setPos(pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 0.5D);
				entity.getPersistentData().putInt("PublicEnemy", 60);
				
				if(level.getEntities(entity.getType(), entity.getBoundingBox(), EntitySelector.ENTITY_STILL_ALIVE).isEmpty() && level.noCollision(entity))
				{
					entity.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.NATURAL, null);
					
					if(entity instanceof LivingEntity)
					{
						LivingEntity living = (LivingEntity) entity;
						applyAttribute(living, Attributes.ATTACK_DAMAGE, new AttributeModifier(WitherUtilsRegistry.loc("witherearth"), 1.5, AttributeModifier.Operation.ADD_VALUE));
						applyAttribute(living, Attributes.MOVEMENT_SPEED, new AttributeModifier(WitherUtilsRegistry.loc("witherearth"), 0.2, AttributeModifier.Operation.ADD_VALUE));
			
						AttributeInstance attributeInstanceByName = living.getAttributes().getInstance(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
						if(attributeInstanceByName != null)
						{
							attributeInstanceByName.setBaseValue(0);
						}
					}

					level.addFreshEntity(entity);
					entity.playAmbientSound();
					
					Portal portal = new Portal(entity.level());
					if (portal != null)
					{
						portal.setPos(entity.position().x + 0.5D, entity.position().y + 0D, entity.position().z + 0.5D);
						entity.level().addFreshEntity(portal);
					}
				}
			}
		}
	}
	
	private static void applyAttribute(LivingEntity mob, Holder<Attribute> attackDamage, AttributeModifier modifier)
	{
		AttributeInstance instance = mob.getAttribute(attackDamage);
		if(instance != null)
		{
			instance.addPermanentModifier(modifier);
		}
	}

	public static class EventHandler
	{
		@SubscribeEvent
		public void spawnInWorld(final EntityJoinLevelEvent event)
		{
			final Entity entity = event.getEntity();
			if (entity instanceof LivingEntity)
			{
				final CompoundTag nbt = entity.getPersistentData();
				if (nbt.contains("PublicEnemy", 3))
				{
					spawnerClients.add(event.getEntity());

					final int cursedEarth = nbt.getInt("PublicEnemy");
					if (cursedEarth <= 0)
					{
						entity.kill();
						event.setCanceled(true);
					}
					else
					{
						Mob mob = (Mob) entity;
						mob.goalSelector.addGoal(0, new AICursed(mob, cursedEarth));
					}
				}
			}
		}

		@SuppressWarnings("rawtypes")
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void render(final RenderLivingEvent.Pre event)
		{
			if(event.getRenderer() != null)
			{
				if (spawnerClients.contains(event.getEntity()))
				{
					System.out.println(event.getEntity());
//					final float v = 0.3f;
//					RenderSystem.setShaderColor(v, v, v, 1.0f);
				}
			}
		}

		@SuppressWarnings("rawtypes")
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void render(final RenderLivingEvent.Post event)
		{
			if(event.getRenderer() != null)
			{
				if (spawnerClients.contains(event.getEntity()))
				{
					System.out.println(event.getEntity());
//					RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
				}
			}
		}
	}

	@SubscribeEvent
	public void spawnInWorld(final EntityJoinLevelEvent event)
	{
		final Entity entity = event.getEntity();
		if (entity instanceof LivingEntity)
		{
			final CompoundTag nbt = entity.getPersistentData();
			if (nbt.contains("PublicEnemy", 3))
			{
				final int cursedEarth = nbt.getInt("PublicEnemy");
				if (cursedEarth <= 0)
				{
					entity.isRemoved();
					event.setCanceled(true);
				}
				else
				{
					final LivingEntity living = (LivingEntity) entity;
					if (entity instanceof Mob mob)
						mob.goalSelector.addGoal(0, (Goal) new AICursed(living, cursedEarth));
				}
			}
		}
	}

	public static class AICursed extends Goal
	{
		final LivingEntity living;
		int cursedEarth;

		public AICursed(final LivingEntity living, final int cursedEarth)
		{
			this.living = living;
			this.cursedEarth = cursedEarth;
		}

		@Override
		public boolean canUse()
		{
			return true;
		}

		@Override
		public boolean canContinueToUse()
		{
			return true;
		}

		public void updateTask()
		{
			if (this.living.level().getGameTime() % 20L != 0L) {
				return;
			}
			if (this.cursedEarth < 0) {
				return;
			}
			if (this.cursedEarth == 0) {
				this.living.kill();
			} else {
				--this.cursedEarth;
				this.living.getPersistentData().putInt("PublicEnemy", this.cursedEarth);
			}
		}
	}
}
