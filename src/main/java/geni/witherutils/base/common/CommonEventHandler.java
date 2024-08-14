package geni.witherutils.base.common;

import java.util.List;

import com.mojang.brigadier.CommandDispatcher;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.block.anvil.AnvilBlockEntity;
import geni.witherutils.base.common.block.deco.fire.SoulFireBlock;
import geni.witherutils.base.common.block.nature.RottenEarth;
import geni.witherutils.base.common.block.nature.RottenSapling;
import geni.witherutils.base.common.config.common.LootConfig;
import geni.witherutils.base.common.data.PlayerData;
import geni.witherutils.base.common.entity.bolt.CursedLightningBolt;
import geni.witherutils.base.common.entity.cursed.creeper.CursedCreeper;
import geni.witherutils.base.common.entity.cursed.dryhead.CursedDryHead;
import geni.witherutils.base.common.entity.cursed.skeleton.CursedSkeleton;
import geni.witherutils.base.common.entity.cursed.spider.CursedSpider;
import geni.witherutils.base.common.entity.cursed.zombie.CursedZombie;
import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTCommands;
import geni.witherutils.base.common.init.WUTEffects;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.base.common.init.WUTParticles;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.core.common.network.CoreNetwork;
import geni.witherutils.core.common.network.PacketServerToClient;
import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoulSandBlock;
import net.minecraft.world.level.block.WitherSkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent.PositionCheck;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent.PositionCheck.Result;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent.SpawnPlacementCheck;
import net.neoforged.neoforge.event.entity.player.BonemealEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickBlock;

public class CommonEventHandler {

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
		
		if(level.dimension() == Level.OVERWORLD)
		{
			if(state.getBlock() instanceof SoulSandBlock && level.getBlockState(pos.above()).getBlock() instanceof WitherSkullBlock)
			{
	        	for(int i = 0; i < 40; i++)
	        	{
	    			double d1 = (double) pos.getX() + level.random.nextDouble();
	    			double d2 = (double) pos.getY() + level.random.nextDouble() * (double) 0.5F;
	    			double d3 = (double) pos.getZ() + level.random.nextDouble();
	    			level.addParticle(WUTParticles.RISINGSOUL.get(), d1, d2, d3, 0.0D, 0.0D, 0.0D);
	    			level.addParticle(ParticleTypes.SMOKE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
	        	}
	        	RottenEarth.startFastSpread(level, pos);
			}
			if(state.getBlock() instanceof RottenSapling)
			{
	        	for(int i = 0; i < 40; i++)
	        	{
	    			double d1 = (double) pos.getX() + level.random.nextDouble();
	    			double d2 = (double) pos.getY() + level.random.nextDouble() * (double) 0.5F;
	    			double d3 = (double) pos.getZ() + level.random.nextDouble();
	    			level.addParticle(WUTParticles.RISINGSOUL.get(), d1, d2, d3, 0.0D, 0.0D, 0.0D);
	    			level.addParticle(ParticleTypes.SMOKE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
	        	}
			}
		}
	}

	/*
	 * 
	 * WITHERSTEEL
	 * 
	 */
	@SubscribeEvent
    public static void onSteelPickup(ItemEntityPickupEvent event)
    {
		Player player = event.getPlayer();
		final boolean iswitherblock = event.getItemEntity().getItem().getItem() == WUTBlocks.WITHERSTEEL_BLOCK.get().asItem();
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
//	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onSpawnForCursed(MobSpawnEvent.PositionCheck event)
	{
		if (event.getSpawnType() == MobSpawnType.SPAWNER)
			return;
		
		LivingEntity entity = event.getEntity();
		Result result = event.getResult();
		LevelAccessor level = event.getLevel();
		
		if(entity instanceof Mob mob && result != Result.FAIL && entity.getY() < 60 && level.getRandom().nextDouble() < 0.05D)
		{
			if(result == Result.SUCCEED || (mob.checkSpawnRules(level, event.getSpawnType()) && mob.checkSpawnObstruction(level)))
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
				else if(entity.getType() == EntityType.WITHER_SKELETON);
					onFinalizeSpawnForCursed(event, entity, level, new CursedDryHead(WUTEntities.CURSEDDRYHEAD.get(), entity.level()));
			}
		}
	}
	public static void onFinalizeSpawnForCursed(MobSpawnEvent.PositionCheck event, LivingEntity entity, LevelAccessor level, Mob cursed)
	{
		Vec3 epos = entity.position();
		cursed.absMoveTo(epos.x, epos.y, epos.z, entity.getYRot(), entity.getXRot());
	}
	
    /*
	 * 
	 * ENDERLILLY
	 * 
	 */
    @SubscribeEvent
    public void handleEnderLillyDropEvent(LivingDeathEvent event)
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
     * ANVILCAULDRON
     * 
     */
    @SubscribeEvent
    public void curseTheAnvilCauldron(RightClickBlock event)
    {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        
        InteractionHand hand = event.getHand();
        
        if (player != null && hand != null)
        {
        	ItemStack heldStack = player.getItemInHand(hand);
            if (!heldStack.isEmpty() && heldStack.getItem() == WUTItems.HAMMER.get())
            {
        		if(level.getBlockState(pos).getBlock() == Blocks.ANVIL ||
	    		level.getBlockState(pos).getBlock() == Blocks.CHIPPED_ANVIL ||
	    		level.getBlockState(pos).getBlock() == Blocks.DAMAGED_ANVIL ||
	    		level.getBlockState(pos).getBlock() == Blocks.CAULDRON)
	    		{
        	    	player.swing(hand);
        	        event.setCanceled(true);
        	        
	                calcTransform(level, player, pos, level.getBlockState(pos).getBlock());
	    		}
            }
        }
    }
    
    public void calcTransform(Level level, Player player, BlockPos pos, Block block)
    {
		if(level instanceof ServerLevel serverLevel)
		{
        	SoundUtil.playSoundDistrib(level, pos, SoundEvents.TRIDENT_THUNDER.value(), 0.75f, 1.0F, false, true);
        	SoundUtil.playSoundDistrib(level, pos, WUTSounds.HAMMERHIT.get(), 0.75f, 1.0F, false, false);
			
            if (player instanceof ServerPlayer serverPlayer)
            {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, serverPlayer.getItemInHand(serverPlayer.getUsedItemHand()));
                
            	doLightningStrike(serverLevel, serverPlayer, pos);
            	
            	MobEffectInstance effect = new MobEffectInstance(WUTEffects.BLIND, 25, 50, false, false);
            	serverPlayer.addEffect(effect);
            }
            
			doTransform(serverLevel, pos, block);
		}
		else
		{
            Vec3 vec3 = player.getLookAngle().normalize();
            float i = 6 / 20F;
            i = (i * i + i * 2) / 3;
            i *= 4;
            if (i > 6)
            {
                i = 6;
            }
            i *= 1;
            player.push(vec3.x * -i, vec3.y * -i / 2 + 0.25f, vec3.z * -i);
		}
    }
    
    public void doLightningStrike(Level level, Player player, BlockPos pos)
    {
        CursedLightningBolt lightningbolt = WUTEntities.CURSEDBOLT.get().create(level);
        lightningbolt.moveTo(Vec3.atBottomCenterOf(pos.above()));
        level.addFreshEntity(lightningbolt);
        
        if (level instanceof ServerLevel serverLevel)
        {
			serverLevel.sendParticles(ParticleTypes.SMOKE,
					pos.above().getX() + 0.5D,
					pos.above().getY() + 0.0D,
					pos.above().getZ() + 0.5D,
					200, 0, 0, 0, 0.025D);

            List<Entity> list1 = serverLevel.getEntities(player,
            		new AABB(player.getX() - 3.0, player.getY() - 3.0, player.getZ() - 3.0,
            				 player.getX() + 3.0, player.getY() + 6.0 + 3.0, player.getZ() + 3.0),
            				 Entity::isAlive);
            
            for (Entity entity : list1)
            {
                if (!net.neoforged.neoforge.event.EventHooks.onEntityStruckByLightning(entity, lightningbolt))
                entity.thunderHit(serverLevel, lightningbolt);
            }
        }

        for(int i = 0; i < 20; ++i)
        {
            BlockPos blockpos = pos.offset(-2 + level.random.nextInt(4), level.random.nextInt(3) - 1, -2 + level.random.nextInt(4));
            BlockState blockstate = SoulFireBlock.getState(level, blockpos);
            if(level.getBlockState(blockpos).isAir() && blockstate.canSurvive(level, blockpos))
            {
                level.setBlockAndUpdate(blockpos, blockstate);
            }
        }
    }

    public void doTransform(ServerLevel serverLevel, BlockPos pos, Block block)
    {
    	if(block == Blocks.CAULDRON)
    		serverLevel.setBlockAndUpdate(pos, WUTBlocks.CAULDRON.get().defaultBlockState());
    	else
    	{
    		serverLevel.setBlockAndUpdate(pos, WUTBlocks.ANVIL.get().defaultBlockState());
        	BlockEntity be = serverLevel.getBlockEntity(pos);
        	if(be instanceof AnvilBlockEntity avBe)
        	{
                avBe.setHotCounter(1.0f);
        	}
    	}
    }
    
    /*
     * 
     * CONSOLECOMMANDS
     * 
     */
    @SubscribeEvent
    public void serverLoad(RegisterCommandsEvent event)
    {
        WUTCommands.register((CommandDispatcher<CommandSourceStack>)event.getDispatcher());
    }
	
    /*
     * 
     * PACKETHANDLERHELPER
     * 
     */
    @SuppressWarnings("resource")
	@SubscribeEvent
    public void onLogInEvent(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (!event.getEntity().level().isClientSide && event.getEntity() instanceof ServerPlayer player)
        {
	        syncPlayerData(player, true);
            WitherUtils.LOGGER.info("Sending Player Data to player " + player.getName() + " with UUID " + player.getUUID() + ".");
        }
    }
    
    public void syncPlayerData(Player player, boolean log)
    {
        CompoundTag compound = new CompoundTag();
        CompoundTag data = new CompoundTag();
        PlayerData.getDataFromPlayer(player).writeToNBT(data, false);
        compound.put("Data", data);
        if (player instanceof ServerPlayer)
        {
            ((ServerPlayer) player).connection.send(new PacketServerToClient(compound, CoreNetwork.SYNC_PLAYER_DATA));
        }
    }
}
