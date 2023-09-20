package geni.witherutils.base.common;

import geni.witherutils.base.common.block.anvil.AnvilBlockEntity;
import geni.witherutils.base.common.block.nature.WitherEarthBlock;
import geni.witherutils.base.common.block.nature.rotten.RottenSaplingBlock;
import geni.witherutils.base.common.block.tank.drum.TankDrumBlock;
import geni.witherutils.base.common.block.tank.drum.TankDrumBlockEntity;
import geni.witherutils.base.common.config.common.ItemsConfig;
import geni.witherutils.base.common.config.common.LootConfig;
import geni.witherutils.base.common.entity.cursed.creeper.CursedCreeper;
import geni.witherutils.base.common.entity.cursed.skeleton.CursedSkeleton;
import geni.witherutils.base.common.entity.cursed.spider.CursedSpider;
import geni.witherutils.base.common.entity.cursed.zombie.CursedZombie;
import geni.witherutils.base.common.entity.soulorb.SoulOrb;
import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.init.WUTFluids;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.base.common.init.WUTParticles;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.base.common.item.hammer.HammerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent.FinalizeSpawn;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.ItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.level.BlockEvent.BlockToolModificationEvent;
import net.minecraftforge.event.level.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEventHandler {

	/*
	 * 
	 * EXPERIENCE
	 * 
	 */
	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void clickBottle(RightClickBlock event)
	{
		if (!event.getLevel().isClientSide && event.getEntity() != null)
		{
			Player player = event.getEntity();
			ItemStack handItem = player.getItemInHand(event.getHand());
			if (!handItem.isEmpty())
			{
				if (handItem.getItem() == Items.GLASS_BOTTLE) {
					if (event.getLevel().getBlockState(event.getPos()).getBlock() instanceof TankDrumBlock) {
						TankDrumBlockEntity tileentity = (TankDrumBlockEntity) event.getLevel().getBlockEntity(event.getPos());
						if (tileentity != null) {
							if (tileentity.getFluidTank().getFluid() != null && tileentity.getFluidTank().getFluid().containsFluid(new FluidStack(WUTFluids.EXPERIENCE.get(), 220)))
							{
								if (tileentity.getFluidTank().getFluidAmount() >= 200)
								{
									tileentity.getFluidTank().drain(new FluidStack(tileentity.getFluidTank().getFluid(), 200), FluidAction.EXECUTE);
									event.getLevel().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
									turnBottleIntoItem(handItem, player, new ItemStack(Items.EXPERIENCE_BOTTLE));
									handItem.shrink(1);
									event.getLevel().sendBlockUpdated(tileentity.getBlockPos(), event.getLevel().getBlockState(tileentity.getBlockPos()), event.getLevel().getBlockState(tileentity.getBlockPos()), 3);
								}
							}
						}
					}
				}
			}
		}
	}

	protected static ItemStack turnBottleIntoItem(ItemStack stackIn, Player player, ItemStack stack)
	{
		if (stackIn.getCount() <= 0)
			return stack;
		else
		{
			if(!player.getInventory().add(stack))
				player.drop(stack, false);
			return stackIn;
		}
	}
	
	/*
	 * 
	 * ROTTEN
	 * 
	 */
	@SubscribeEvent
	public static void onUseBonemeal(BonemealEvent event)
	{
		Level level = event.getLevel();
		BlockPos pos = event.getPos();
		BlockState state = level.getBlockState(pos);
		BlockState lowerstate = level.getBlockState(pos.below());

		final boolean isrottensap = state.getBlock() instanceof RottenSaplingBlock;
		if(isrottensap && level.dimension() == Level.OVERWORLD)
		{
			if(lowerstate.getBlock() instanceof GrassBlock || lowerstate.getBlock() == Blocks.DIRT)
			{
				level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
	        	for(int i = 0; i < 40; i++)
	        	{
	    			double d1 = (double) pos.getX() + level.random.nextDouble();
	    			double d2 = (double) (pos.getY() + 1) - level.random.nextDouble() * (double) 0.1F;
	    			double d3 = (double) pos.getZ() + level.random.nextDouble();
	    			level.addParticle(WUTParticles.RISINGSOUL.get(), d1, d2, d3, 0.0D, 0.0D, 0.0D);
	    			level.addParticle(ParticleTypes.SMOKE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
	        	}
				WitherEarthBlock.startFastSpread(level, pos.below());
			}
		}
	}

	/*
	 * 
	 * WITHERSTEEL
	 * 
	 */
	@SubscribeEvent
    public static void onSteelPickup(ItemPickupEvent event)
    {
		Player player = event.getEntity();
		final boolean iswitherblock = event.getStack().getItem() == WUTBlocks.WITHERSTEEL_BLOCK.get().asItem();
		if(iswitherblock && player.isAlive())
		{
			player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 148, 3, false, false, false));
		}
    }
	
    /*
     * 
     * CURSED SPAWN
     * 
     */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onSpawnForCursed(MobSpawnEvent.FinalizeSpawn event)
	{
		if (event.getSpawnType() == MobSpawnType.SPAWNER)
			return;
		
		LivingEntity entity = event.getEntity();
		Result result = event.getResult();
		LevelAccessor level = event.getLevel();
		
		if(entity instanceof ZombifiedPiglin)
		{
			onFinalizeSpawnForPlague(entity, (Level) level);
		}
		
		if(entity instanceof Mob mob && result != Result.DENY && entity.getY() < 60 && level.getRandom().nextDouble() < 0.05D)
		{
			if(result == Result.ALLOW || (mob.checkSpawnRules(level, event.getSpawnType()) && mob.checkSpawnObstruction(level)))
			{
				if(entity.getType() == EntityType.CREEPER)
					onFinalizeSpawnForCursed(event, entity, level, new CursedCreeper(WUTEntities.CURSEDCREEPER.get(), entity.level()));
				else if(entity.getType() == EntityType.SKELETON)
				{
					CursedSkeleton cursed = new CursedSkeleton(WUTEntities.CURSEDSKELETON.get(), entity.level());
					cursed.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BOW));
					onFinalizeSpawnForCursed(event, entity, level, cursed);
				}
				else if(entity.getType() == EntityType.SPIDER)
				{
					CursedSpider cursed = new CursedSpider(WUTEntities.CURSEDSPIDER.get(), entity.level());
					onFinalizeSpawnForCursed(event, entity, level, cursed);
				}
				else if(entity.getType() == EntityType.ZOMBIE)
					onFinalizeSpawnForCursed(event, entity, level, new CursedZombie(WUTEntities.CURSEDZOMBIE.get(), entity.level()));
			}
		}
	}
	public static void onFinalizeSpawnForCursed(MobSpawnEvent.FinalizeSpawn event, LivingEntity entity, LevelAccessor level, Mob cursed)
	{
		Vec3 epos = entity.position();
		
		cursed.absMoveTo(epos.x, epos.y, epos.z, entity.getYRot(), entity.getXRot());

		MobSpawnEvent.FinalizeSpawn newEvent = new FinalizeSpawn(cursed, (ServerLevelAccessor) level, event.getX(), event.getY(), event.getZ(),
				event.getDifficulty(), event.getSpawnType(), event.getSpawnData(), event.getSpawnTag(), event.getSpawner());
		
		MinecraftForge.EVENT_BUS.post(newEvent);
		
		if(newEvent.getResult() != Result.DENY)
		{
			level.addFreshEntity(cursed);
			event.setResult(Result.DENY);
		}
	}

	/*
	 * 
	 * NETHERPLAGUE
	 * 
	 */
	public static void onFinalizeSpawnForPlague(LivingEntity entity, Level level)
	{
//		BlockState state = level.getBlockState(entity.blockPosition().below());
//		if(state.getBlock() == Blocks.NETHERRACK)
//		{
//			PlagueBlock.startFastSpread(level, entity.blockPosition().below());
//		}
	}
	
	
	/*
	 * 
	 * HAMMER
	 * 
	 */
    @SubscribeEvent
    public static void onHammerSmash(BreakEvent event)
    {
        Player player = event.getPlayer();
        Level level = player.level();
        BlockPos pos = event.getPos();
        ItemStack heldStack = player.getItemInHand(InteractionHand.MAIN_HAND);
        BlockState state = level.getBlockState(pos);

        final boolean isHoldingHammer = heldStack.getItem() == WUTItems.HAMMER.get();
        if (isHoldingHammer)
        {
            if (level.isClientSide || level.getServer() == null || player.isCreative())
                return;
            HammerItem hammer = (HammerItem) heldStack.getItem();

            onHammerSmashUncraft(hammer, level, pos, state, player);
            onHammerSmashSlicer(level, pos, state);
        }
    }
    public static void onHammerSmashUncraft(HammerItem hammer, Level level, BlockPos pos, BlockState state, Player player)
    {
        Recipe<?> match = hammer.findMatchingRecipe(level, new ItemStack(state.getBlock()));
        if (match != null && !player.getCooldowns().isOnCooldown(hammer))
        {
            if (hammer.uncraftRecipe(level, pos, match))
            {
                hammer.hammerSmash(level, player, pos, match);
            }
        }
    }
    public static void onHammerSmashSlicer(Level level, BlockPos pos, BlockState state)
    {
		spawnEffect(level, pos, state);
    	
    	for(int j = 0; j < 8; j++)
    	{
        	if(state.getBlock() == Blocks.BLACK_CONCRETE)
        		spawnCompletionItem(level, pos, new ItemStack(WUTBlocks.SLICEDCONCRETEBLACK.get()), 1);
        	else if(state.getBlock() == Blocks.GRAY_CONCRETE)
        		spawnCompletionItem(level, pos, new ItemStack(WUTBlocks.SLICEDCONCRETEGRAY.get()), 1);
        	else if(state.getBlock() == Blocks.WHITE_CONCRETE)
        		spawnCompletionItem(level, pos, new ItemStack(WUTBlocks.SLICEDCONCRETEWHITE.get()), 1);
    	}
    }
    public static void spawnEffect(Level level, BlockPos pos, BlockState state)
    {    
        for(int i = 0; i < 1; i++)
        {
            level.levelEvent(2001, pos, Block.getId(state.getBlock().defaultBlockState()));
            if(level instanceof ServerLevel serverlevel)
            {
    			BlockPos endPos = pos.offset(-2 + level.random.nextInt(4), level.random.nextInt(3) - 1, -2 + level.random.nextInt(4));
                for(int j = 0; j < 20; ++j)
                {
                	serverlevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Block.byItem(state.getBlock().asItem()))),
                        (double)endPos.getX() + 0.5D,
                        (double)endPos.getY() + 0.7D,
                        (double)endPos.getZ() + 0.5D, 3,
                        ((double)level.random.nextFloat() - 0.5D) * 0.08D,
                        ((double)level.random.nextFloat() - 0.5D) * 0.08D,
                        ((double)level.random.nextFloat() - 0.5D) * 0.08D,
                        (double)0.15F);
                }
            }
            
            for(int j = 0; j < 20; ++j)
            {
            	level.addParticle(ParticleTypes.EXPLOSION,
                    (double)pos.getX() + 0.5D,
                    (double)pos.getY() + 0.7D,
                    (double)pos.getZ() + 0.5D,
            		0, 0, 0);
            }
        }
    }
    public static void spawnItem(Level level, BlockPos pos, ItemStack stack, int amount)
    {
        ItemEntity i = new ItemEntity(level, 0, 0, 0, stack);
        i.setPos(pos.getX() + Mth.nextDouble(level.random, -0.5, 0.5), pos.getY() + 1.5, pos.getZ() + Mth.nextDouble(level.random, -0.5, 0.5));
        i.setDeltaMovement(Mth.nextDouble(level.random, -0.15, 0.15), 0.4, Mth.nextDouble(level.random, -0.15, 0.15));
        level.addFreshEntity(i);
    }
    public static void spawnCompletionItem(Level level, BlockPos pos, ItemStack stack, int amount)
    {
        ItemEntity i = new ItemEntity(level, 0, 0, 0, stack);
        double variance = 0.05F * 4;
        i.setPos(pos.getX(), pos.getY() + 1, pos.getZ());
        i.setDeltaMovement(Mth.nextDouble(level.random, -variance, variance), 2 / 20F, Mth.nextDouble(level.random, -variance, variance));
        i.setUnlimitedLifetime();
        level.addFreshEntity(i);
    }

    /*
	 * 
	 * ENDERLILLY
	 * 
	 */
    @SubscribeEvent
    public static void handleEnderLillyDropEvent(LivingDeathEvent event)
    {
        Level level = event.getEntity().level();
        Vec3 pos = new Vec3(event.getEntity().getX(), event.getEntity().getEyeY(), event.getEntity().getZ());
        ItemStack lilly = new ItemStack(WUTBlocks.LILLY.get());
        
        if(event.getEntity() instanceof EnderMan && level.random.nextFloat() <= LootConfig.LILLYDROPCHANCE.get())
        {
            LivingEntity living = (LivingEntity) event.getEntity();
            if(level instanceof ServerLevel)
            {
                if(living != null)
                {
        			double d0 = level.random.nextFloat() * 0.5F + 0.25D;
        			double d1 = level.random.nextFloat() * 0.5F + 0.25D;
        			double d2 = level.random.nextFloat() * 0.5F + 0.25D;
        			ItemEntity entityitem = new ItemEntity(level, pos.x() + d0, pos.y() + d1, pos.z() + d2, lilly);
        			entityitem.setDefaultPickUpDelay();
        			level.addFreshEntity(entityitem);
                }
            }
        }
    }
    
    /*
     * 
     * SOULORB
     * 
     */
    @SubscribeEvent
    public static void handleSoulOrbPickupEvent(EntityItemPickupEvent event)
    {   
        if(event.getItem().getName().contains(Component.translatable("SoulOrb")))
        {
//            event.getEntity().addEffect(new MobEffectInstance(WUTEffects.BLIND.get(), 20, 5, false, false));
//            event.getEntity().addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 30, 5, false, false));
            event.getEntity().level().playSound((Player) null, event.getEntity().blockPosition(), WUTSounds.PICKSHEE.get(), SoundSource.BLOCKS, 0.3f, 0.5f);
        }
    }

    @SubscribeEvent
    public static void handleSoulOrbDropEvent(LivingDeathEvent event)
    {
        Level level = event.getEntity().level();
        Vec3 pos = new Vec3(event.getEntity().getX(), event.getEntity().getEyeY(), event.getEntity().getZ());

        final boolean atNether = level.dimension() == Level.NETHER;
        if(atNether && event.getEntity() instanceof LivingEntity)
        {
            LivingEntity living = (LivingEntity) event.getEntity();
            if(level instanceof ServerLevel)
            {
                if(living != null)
                {
                    SoulOrb.award((ServerLevel) level, pos, 1);
                    SoulOrb.fxthis((ServerLevel) level, event.getEntity().blockPosition());
                }
            }
        }
    }
    
    /*
     * 
     * CAULDRON
     * 
     */
    @SubscribeEvent
	public static void curseTheCauldron(RightClickBlock event)
	{
        Level level = (Level) event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        ItemStack heldStack = player.getItemInHand(event.getHand());

        if (!level.isClientSide && player != null)
        {
            if (!heldStack.isEmpty() && heldStack.getItem() == WUTItems.SOULORB.get())
            {
            	Block block = level.getBlockState(pos).getBlock();
                if(block == Blocks.CAULDRON)
                {
                    level.levelEvent(1027, pos, 0);
                    datLightComes(level, pos);
                    level.setBlockAndUpdate(pos, WUTBlocks.CAULDRON.get().defaultBlockState());
                }
            }
        }
	}
    
    /*
     * 
     * ANVIL
     * 
     */
    @SubscribeEvent
    public static void curseTheAnvil(RightClickBlock event)
    {
        Level level = (Level) event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        ItemStack heldStack = player.getItemInHand(event.getHand());
        
        if (!level.isClientSide && player != null)
        {
            if (!heldStack.isEmpty() && heldStack.getItem() == WUTItems.SOULORB.get())
            {
            	BlockEntity anvilBe = level.getBlockEntity(pos);
                if(level.getBlockState(pos).getBlock() == Blocks.ANVIL)
                {
                    level.levelEvent(1027, pos, 0);
                    datLightComes(level, pos);
                    if(level.setBlockAndUpdate(pos, WUTBlocks.ANVIL.get().defaultBlockState()))
                    {
                    	if(anvilBe instanceof AnvilBlockEntity)
                    	{
                    		AnvilBlockEntity avBe = (AnvilBlockEntity) anvilBe;
                    		avBe.setHotCounter(1.0f);
                    	}
                    }
                }
            }
        }
    }
    
    public static void datLightComes(Level level, BlockPos pos)
    {
        LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(level);
        lightningbolt.moveTo(Vec3.atBottomCenterOf(pos.above()));
        level.addFreshEntity(lightningbolt);
        level.playSound((Player)null, pos, SoundEvents.TRIDENT_THUNDER, SoundSource.WEATHER, 5.0F, 1.0F);
        spawnFire(20, level, lightningbolt);
    }
    
    public static void spawnFire(int amount, Level level, Entity entity)
    {
        if(!level.isClientSide && level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK))
        {
            BlockPos blockpos = entity.blockPosition();
            BlockState blockstate = BaseFireBlock.getState(level, blockpos);

            if(level.getBlockState(blockpos).isAir() && blockstate.canSurvive(level, blockpos))
            {
                level.setBlockAndUpdate(blockpos, blockstate);
            }
            for(int i = 0; i < amount; ++i)
            {
                BlockPos blockpos1 = blockpos.offset(-2 + level.random.nextInt(4), level.random.nextInt(3) - 1, -2 + level.random.nextInt(4));
                blockstate = BaseFireBlock.getState(level, blockpos1);

                if(level.getBlockState(blockpos1).isAir() && blockstate.canSurvive(level, blockpos1))
                {
                    level.setBlockAndUpdate(blockpos1, blockstate);
                }
            }
        }
    }
    
    /*
     * 
     * WORM
     * 
     */
	@SubscribeEvent
    public static void onHoe(BlockToolModificationEvent event)
	{
		boolean canSpawnWorms = ItemsConfig.SPAWNWITHERWORMS.get();
		if(!canSpawnWorms)
			event.setCanceled(true);
		
		Level level = (Level) event.getLevel();
		
		ItemStack heldStack = event.getHeldItemStack();
		ToolAction action = event.getToolAction();

		if(heldStack.getItem() instanceof HoeItem && action == ToolActions.HOE_TILL)
		{
			if (event.getResult() != Result.DENY)
			{
	            if (!level.isClientSide)
	            {
	            	BlockPos pos = event.getPos();
	            	BlockState state = level.getBlockState(pos.above());
	            	if(state.isAir())
	            	{
	            		BlockState st = level.getBlockState(pos);
	            		
	                    if (level.random.nextFloat() >= 0.95F)
	                    {
		                    if (st.getBlock() instanceof GrassBlock || st.getBlock() == Blocks.DIRT)
		                    {
		                        ItemStack stack = new ItemStack(WUTItems.WORM.get());
		                        ItemEntity item = new ItemEntity((Level) event.getLevel(), pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, stack);
		                        level.addFreshEntity(item);
		                    }
	                    }
	            	}
	            }
			}
		}
	}
}
