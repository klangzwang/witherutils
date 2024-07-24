package geni.witherutils.base.common;

import geni.witherutils.base.common.block.LogicalBlocks;
import geni.witherutils.base.common.block.anvil.AnvilBlockEntity;
import geni.witherutils.base.common.config.common.ItemsConfig;
import geni.witherutils.base.common.config.common.LootConfig;
import geni.witherutils.base.common.entity.soulorb.SoulOrb;
import geni.witherutils.base.common.init.WUTAttachments;
import geni.witherutils.base.common.init.WUTCapabilities;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.base.common.item.hammer.HammerItem;
import geni.witherutils.base.common.item.soulorb.SoulOrbItem;
import geni.witherutils.core.common.helper.SoulsHelper;
import geni.witherutils.core.common.network.CoreNetwork;
import geni.witherutils.core.common.network.PacketSoulsSync;
import geni.witherutils.core.common.util.ConfigUtil;
import geni.witherutils.core.common.util.ItemStackUtil;
import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.neoforged.neoforge.event.level.BlockEvent.BlockToolModificationEvent;
import net.neoforged.neoforge.event.level.BlockEvent.BreakEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class CommonEventHandler {

	/*
	 * 
	 * SPAWNING
	 * 
	 */
    public static void spawnEffect(Level level, BlockPos pos, BlockState state)
    {    
        for(int i = 0; i < 1; i++)
        {
            level.levelEvent(2001, pos, Block.getId(state.getBlock().defaultBlockState()));
            if(level instanceof ServerLevel serverlevel)
            {
//    			BlockPos endPos = pos.offset(-2 + level.random.nextInt(4), level.random.nextInt(3) - 1, -2 + level.random.nextInt(4));
                for(int j = 0; j < 20; ++j)
                {
                	serverlevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Block.byItem(state.getBlock().asItem()))),
                        (double)pos.getX() + 0.5D,
                        (double)pos.getY() + 0.7D,
                        (double)pos.getZ() + 0.5D, 3,
                        ((double)level.random.nextFloat() - 0.5D) * 0.08D,
                        ((double)level.random.nextFloat() - 0.5D) * 0.08D,
                        ((double)level.random.nextFloat() - 0.5D) * 0.08D,
                        (double)0.15F);
                }
                for(int j = 0; j < 20; ++j)
                {
                	serverlevel.sendParticles(ParticleTypes.EXPLOSION,
                        (double)pos.getX() + 0.5D,
                        (double)pos.getY() + 0.7D,
                        (double)pos.getZ() + 0.5D, 3,
                        ((double)level.random.nextFloat() - 0.5D) * 0.08D,
                        ((double)level.random.nextFloat() - 0.5D) * 0.08D,
                        ((double)level.random.nextFloat() - 0.5D) * 0.08D,
                        (double)0.15F);
                }
            }
        }
    }
    
    public static void spawnItem(Level level, BlockPos pos, ItemStack stack, int amount)
    {
        ItemEntity drop = new ItemEntity(level, 0, 0, 0, new ItemStack(stack.getItem(), amount));
        double variance = 0.05F * 4;
        drop.setPos(pos.getX(), pos.getY() + 1, pos.getZ());
        drop.setDeltaMovement(Mth.nextDouble(level.random, -variance, variance), 2 / 20F, Mth.nextDouble(level.random, -variance, variance));
        level.addFreshEntity(drop);
    }
	
	/*
	 * 
	 * SOULS
	 * 
	 */
	@SubscribeEvent
	public void onBreakWithSouls(BreakEvent event)
	{	
        Player player = event.getPlayer();
        Level level = player.level();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
		Block block = state.getBlock();
        
        if (!level.isClientSide() && level.getServer() != null)
        {
        	if(block instanceof DropExperienceBlock)
        	{
                if (player instanceof ServerPlayer)
                {
                	if(!player.isCreative())
                	{
            			if(player.getData(WUTAttachments.SOULS_CONTROL) > 0)
            				SoulsHelper.removeSoul();
            			
            			spawnEffect(level, pos, state);
            			SoundUtil.playSoundDistrib(level, pos, WUTSounds.PLACEBOOMONE.get(), 1, 1, false, true);
                	}
                }
        	}
        }

//		if (!level.isClientSide() && level.getServer() != null)
//		{
//			if(block instanceof DropExperienceBlock dropBlock)
//			{
//				ResourceKey<LootTable> pLootTableKey = dropBlock.getLootTable();
//				ObjectArrayList<ItemStack> itemstackit = level.getServer().reloadableRegistries().getLootTable(pLootTableKey).getRandomItems(new LootParams.Builder((ServerLevel) level).create(LootContextParamSets.EMPTY));
//				for (ItemStack itemstackiteratorWith : itemstackit)
//				{
//					System.out.println(itemstackiteratorWith.getCount() * 2);
//				}
//			}
//		}
	}

	@SubscribeEvent
	public void onPlayerPickiPick(PlayerInteractEvent.RightClickItem event)
	{	
        if (event.getItemStack().getItem() instanceof SoulOrbItem
                && event.getEntity() instanceof ServerPlayer player)
        {
        	if(!player.isCreative())
        	{
    			if(player.getCapability(WUTCapabilities.PLAYERSOUL) != null)
    			{
    				if(player.isCrouching())
    				{
        				SoulsHelper.removeSoul();
    					if(player.getData(WUTAttachments.SOULS_CONTROL) == 0)
    					{
    						SoundUtil.playSoundDistrib(player.level(), player.blockPosition(), WUTSounds.BUZZ.get(), 0.6f, 1, false, true);
    						SoundUtil.playSoundDistrib(player.level(), player.blockPosition(), WUTSounds.PICKPLICK.get(), 0.6f, 1, false, true);
    						SoundUtil.playSoundDistrib(player.level(), player.blockPosition(), WUTSounds.MINIONCAST.get(), 0.2f, 1, false, true);
    					}
    				}
    				else
    				{
    					if(player.getData(WUTAttachments.SOULS_CONTROL) < 1)
    					{
    						SoundUtil.playSoundDistrib(player.level(), player.blockPosition(), WUTSounds.BUZZ.get(), 0.6f, 1, false, true);
    						SoundUtil.playSoundDistrib(player.level(), player.blockPosition(), WUTSounds.PICKPLICK.get(), 0.6f, 1, false, true);
    						SoundUtil.playSoundDistrib(player.level(), player.blockPosition(), WUTSounds.MINIONCAST.get(), 0.2f, 1, false, true);
    					}
        				SoulsHelper.addSoul();
        				ItemStackUtil.shrink(player, event.getItemStack());
    				}
    			}
        	}
        }
	}
	
	/* DONE */
	@SubscribeEvent
	public void onPlayerJoinWorld(EntityJoinLevelEvent event)
	{
		Level level = event.getLevel();
		if(!level.isClientSide && (event.getEntity() instanceof ServerPlayer player))
		{
			SoulsHelper.player = player;
			SoulsHelper.setSouls(player.getData(WUTAttachments.SOULS_CONTROL));
			WUTCapabilities.getPlayerSoulHandler(player).ifPresent(f -> {
				f.setSouls(player.getData(WUTAttachments.SOULS_CONTROL));
				CoreNetwork.sendToPlayer(new PacketSoulsSync(f.getSouls()), player);
			});
		}
	}
	
//	@SubscribeEvent
//	public void onPlayerLeaveWorld(EntityLeaveLevelEvent event)
//	{
//		Level level = event.getLevel();
//		if(!level.isClientSide && (event.getEntity() instanceof ServerPlayer))
//		{
//			CompoundTag compoundtag = new CompoundTag();
//			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
//			System.out.println(compoundtag.getAsString());
//			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
//		}
//	}

//	@SubscribeEvent
//	public void onPlayerCloned(PlayerEvent.Clone event)
//	{
//		Player player = event.getOriginal();
//		if(player instanceof ServerPlayer serverPlayer)
//		{
//			if (!event.isWasDeath())
//			{
//				WUTCapabilities.getPlayerSoulHandler(serverPlayer).ifPresent(oldStore -> {
//					WUTCapabilities.getPlayerSoulHandler(serverPlayer).ifPresent(newStore -> {
//						newStore.copyFrom(oldStore);
//					});
//				});
//			}
//		}
//	}
	
//    @SubscribeEvent
//    public void handleSoulOrbPickupEvent(ItemEntityPickupEvent.Post event)
//    {   
//        if(event.getItemEntity().getName().contains(Component.translatable("SoulOrb")))
//        {
//            event.getItemEntity().level().playSound((Player) null, event.getItemEntity().blockPosition(), WUTSounds.PICKSHEE.get(), SoundSource.BLOCKS, 0.3f, 0.5f);
//        }
//    }
	
//	@SubscribeEvent
//	public void onLivingAttackedSouls(LivingDamageEvent.Post event)
//	{
//		if(event.getEntity() instanceof ServerPlayer player)
//		{
//			WUTCapabilities.getPlayerSoulHandler(player).ifPresent(f -> {
//				
//				if(SoulsHelper.getSouls() > 0 && event.getNewDamage() >= 1.1f)
//				{
//					if(player instanceof ServerPlayer)
//					{
//						ServerPlayer serverPlayer = (ServerPlayer) player;
//
//						if(!player.isCreative())
//						{
//							if(SoulsHelper.getSouls() > 0)
//							{
//								SoulsHelper.addCooldown(serverPlayer, SoulsHelper.getCooldown(serverPlayer) + (int)event.getNewDamage() * 20);
//							}
//							if(SoulsHelper.getCooldown(serverPlayer) >= 40)
//							{
//				    			SoulsHelper.addSouls(serverPlayer, -(int)event.getNewDamage() / 2);
//				    			player.level().playSound((Player)null, player.getX(), player.getY(), player.getZ(), WUTSounds.WRINKLYSPAWN.get(), SoundSource.NEUTRAL, 0.38F, 1.0F);
//							}							
//						}
//					}
//				}
//			});
//		}
//	}

	@SubscribeEvent
	public void onLivingTickEvent(EntityTickEvent.Post event)
	{
		if (!(event.getEntity() instanceof ServerPlayer))
			return;
	}
	
    @SubscribeEvent
    public void handleSoulOrbDropEvent(LivingDeathEvent event)
    {
        Level level = event.getEntity().level();
        Vec3 pos = new Vec3(event.getEntity().getX(), event.getEntity().getEyeY(), event.getEntity().getZ());

    	if(level.random.nextFloat() >= LootConfig.SOULORBDROPCHANCE.get())
    		return;

        if(LootConfig.SOULORBDROPLIST.get().size() == 0)
        {
        	if(level.dimension() != Level.NETHER)
        		return;
        }
        else
        {
    		if(!ConfigUtil.dimensionKeyIslisted(level.dimension().location()))
    			return;
        }

        if(event.getEntity() instanceof LivingEntity)
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
	 * HAMMER
	 * 
	 */
    @SubscribeEvent
    public void onHammerSmash(BreakEvent event)
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
//            onHammerSmashSlicer(level, pos, state);
        }
    }
    public void onHammerSmashUncraft(HammerItem hammer, Level level, BlockPos pos, BlockState state, Player player)
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
	
    /*
     * 
     * ANVIL
     * 
     */
    @SubscribeEvent
    public void curseTheAnvil(RightClickBlock event)
    {
        Level level = (Level) event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        ItemStack heldStack = player.getItemInHand(event.getHand());

        if (!level.isClientSide && player != null)
        {
            if (!heldStack.isEmpty() && heldStack.getItem() == Items.ENDER_PEARL)
            {
            	BlockEntity anvilBe = level.getBlockEntity(pos);
                if(level.getBlockState(pos).getBlock() == Blocks.ANVIL)
                {
                    level.levelEvent(1027, pos, 0);
                    datLightComes(level, pos);
                    if(level.setBlockAndUpdate(pos, LogicalBlocks.ANVIL.get().defaultBlockState()))
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
    
    public void datLightComes(Level level, BlockPos pos)
    {
        LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(level);
        lightningbolt.moveTo(Vec3.atBottomCenterOf(pos.above()));
        level.addFreshEntity(lightningbolt);
   		SoundUtil.playSoundDistrib(level, pos, SoundEvents.TRIDENT_THUNDER.value(), 0.75f, 1.0F, false, true);
   		
   		level.playLocalSound(pos, SoundEvents.TRIDENT_THUNDER.value(), SoundSource.WEATHER, 5.0F, 1.0F, false);
   		
        spawnFire(20, level, lightningbolt);
    }
    
    public void spawnFire(int amount, Level level, Entity entity)
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
    public void onHoe(BlockToolModificationEvent event)
	{
		boolean canSpawnWorms = ItemsConfig.SPAWNWITHERWORMS.get();
		if(!canSpawnWorms)
			event.setCanceled(true);
		
		Level level = (Level) event.getLevel();
		
		ItemStack heldStack = event.getHeldItemStack();
		ItemAbility action = event.getItemAbility();

		if(heldStack.getItem() instanceof HoeItem && action == ItemAbility.get("till"))
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
