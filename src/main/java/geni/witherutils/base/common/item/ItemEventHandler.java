package geni.witherutils.base.common.item;

import geni.witherutils.base.common.config.common.ItemsConfig;
import geni.witherutils.base.common.config.common.LootConfig;
import geni.witherutils.base.common.entity.soulorb.SoulOrb;
import geni.witherutils.base.common.init.WUTAttachments;
import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTCapabilities;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.base.common.item.hammer.HammerItem;
import geni.witherutils.base.common.item.soulorb.SoulOrbItem;
import geni.witherutils.base.common.item.withersteel.shield.ShieldSteelItem;
import geni.witherutils.core.common.helper.SoulsHelper;
import geni.witherutils.core.common.network.CoreNetwork;
import geni.witherutils.core.common.network.PacketSoulsSync;
import geni.witherutils.core.common.util.ConfigUtil;
import geni.witherutils.core.common.util.ItemStackUtil;
import geni.witherutils.core.common.util.SoundUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent.BlockToolModificationEvent;
import net.neoforged.neoforge.event.level.BlockEvent.BreakEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class ItemEventHandler {

	/*
	 * 
	 * DAMAGE
	 * 
	 */
	@SubscribeEvent
	public void onLivingAttackedShield(LivingShieldBlockEvent event)
	{
		if(event.getEntity() instanceof Player player)
		{
			ItemStack activeStack = player.getUseItem();
			if(!activeStack.isEmpty() && activeStack.getItem() instanceof ShieldSteelItem && event.getOriginalBlockedDamage() >= 3 && canBlockDamageSource(player, event.getDamageSource()))
			{
				ItemStackUtil.damageItem(activeStack);
			}
		}
	}
	public boolean canBlockDamageSource(LivingEntity entity, DamageSource damageSourceIn)
	{
		if(!damageSourceIn.is(DamageTypeTags.BYPASSES_ARMOR) && entity.isBlocking())
		{
			Vec3 vec3d = damageSourceIn.getSourcePosition();
			if(vec3d!=null)
			{
				Vec3 vec3d1 = entity.getViewVector(1.0F);
				Vec3 vec3d2 = vec3d.vectorTo(entity.position()).normalize();
				vec3d2 = new Vec3(vec3d2.x, 0.0D, vec3d2.z);
				return vec3d2.dot(vec3d1) < 0;
			}
		}
		return false;
	}
	
	/*
	 * 
	 * SPAWNING
	 * 
	 */
    public void spawnEffect(Level level, BlockPos pos, BlockState state)
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
                            0.15F);
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
                            0.15F);
                }
            }
        }
    }
    
    public void spawnItem(Level level, BlockPos pos, ItemStack stack, int amount)
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
        
    	if(!player.isCreative())
    	{
            if (!level.isClientSide() && level.getServer() != null)
            {
            	if(block instanceof DropExperienceBlock dropBlock)
            	{
                    if (player instanceof ServerPlayer)
                    {
            			if(player.getData(WUTAttachments.SOULS_CONTROL) > 0)
            			{
            				ResourceKey<LootTable> pLootTableKey = dropBlock.getLootTable();
            				ObjectArrayList<ItemStack> itemstackit = level.getServer().reloadableRegistries().getLootTable(pLootTableKey)
            						.getRandomItems(new LootParams.Builder((ServerLevel) level).create(LootContextParamSets.EMPTY));
            				
            				for (ItemStack itemstackiteratorWith : itemstackit)
            				{
            					spawnItem(level, pos, itemstackiteratorWith, itemstackiteratorWith.getCount());
            				}
                			
            				SoulsHelper.removeSoul();
                			SoundUtil.playSoundDistrib(level, pos, WUTSounds.PLACEBOOMONE.get(), 1, 1, false, true);
                			spawnEffect(level, pos, state);
            			}
                    }
            	}
            }
    	}
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

	@SubscribeEvent
	public void onLivingTickEvent(EntityTickEvent.Post event)
	{
		if (!(event.getEntity() instanceof ServerPlayer)) {
        }
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
            LivingEntity living = event.getEntity();
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
            onHammerSmashSlicer(level, pos, state);
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
    public void onHammerSmashSlicer(Level level, BlockPos pos, BlockState state)
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
    public void spawnCompletionItem(Level level, BlockPos pos, ItemStack stack, int amount)
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
