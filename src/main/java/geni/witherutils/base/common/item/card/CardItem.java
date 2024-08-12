package geni.witherutils.base.common.item.card;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.common.base.IWitherInventoryItem;
import geni.witherutils.base.common.base.WitherItem;
import geni.witherutils.base.common.init.WUTComponents;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.core.common.util.ItemStackUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.items.ComponentItemHandler;
import net.neoforged.neoforge.items.IItemHandler;

public class CardItem extends WitherItem implements IWitherInventoryItem {

//	@SuppressWarnings("unused")
//	private static final String NBTFILTER = "itemfilter";

    private CompoundTag nbt = new CompoundTag();
	
	public CardItem()
	{
		super(new Properties().stacksTo(1).rarity(Rarity.RARE));
        if(FMLEnvironment.dist.isClient())
        {
            registerProperty();
        }
        
        if (!nbt.contains("bound_item"))
            nbt.putString("bound_item", "");
    }
	
	@Nullable
	@Override
	public ICapabilityProvider<ItemStack, Void, IItemHandler> initItemHandlerCap()
	{
		return (stack, v) -> new ComponentItemHandler(stack, WUTComponents.ITEMS.get(), 9);
	}

    @OnlyIn(Dist.CLIENT)
    public void registerProperty()
    {
        ItemProperties.register(this, WitherUtilsRegistry.loc("bound"), (stack, world, entity, i) -> {
        	float f = 0.0F;
        	if (nbt != null)
        	{
        		if (nbt.hasUUID("bound_playerid") || nbt.contains("bound_mobname") || nbt.contains("bound_item"))
        		{
        			f = 1.0F;
        		}
        	}
        	return f;
        });
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand handIn)
    {
        ItemStack stack = player.getItemInHand(handIn);
        if (player instanceof ServerPlayer sp)
        {
            openGui(sp, stack, handIn);
        }
        return InteractionResultHolder.success(stack);
    }
	
    @Override
    public InteractionResult onItemUseFirst(ItemStack remote, UseOnContext ctx)
    {
        Player player = ctx.getPlayer();
        if (player instanceof ServerPlayer sp)
            openGui(sp, remote, ctx.getHand());
        return InteractionResult.SUCCESS;
    }
	
    private void openGui(ServerPlayer player, ItemStack remote, InteractionHand hand)
    {
        if (player.isCrouching())
        	player.openMenu(new CardContainerProvider());
		else
			saveId(player, hand);
    }

	public boolean isFilterSet(ItemStack stack)
	{
   		IItemHandler cap = stack.getCapability(Capabilities.ItemHandler.ITEM);
		if(cap != null)
		{
			for(int i = 0; i < cap.getSlots(); i++)
			{
				if(!cap.getStackInSlot(i).isEmpty())
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean filterAllowsTransfer(ItemStack stack, ItemStack itemTarget)
	{
		boolean isEmpty = false;
		boolean isMatchingList = false;
   		IItemHandler myFilter = stack.getCapability(Capabilities.ItemHandler.ITEM);
		if(myFilter != null)
		{
			for(int i = 0; i < myFilter.getSlots(); i++)
			{
				ItemStack filterPtr = myFilter.getStackInSlot(i);
				if(!filterPtr.isEmpty())
				{
					isEmpty = false;
					if(ItemStackUtil.matches(itemTarget, filterPtr))
					{
						isMatchingList = true;
						break;
					}
				}
			}
		}
		return isEmpty || isMatchingList;
	}
	
	public static boolean filterAllowsExtract(ItemStack stack, ItemStack itemTarget)
	{
//		if (stack.getItem() instanceof CardItem == false)
//		{
//			return true;
//		}
//
//		boolean isEmpty = false;
//		boolean isMatchingList = false;
//		boolean isIgnoreList = getIsIgnoreList(stack);
//		
//   		IItemHandler myFilter = stack.getCapability(Capabilities.ItemHandler.ITEM);
//		if (myFilter != null)
//		{
//			for (int i = 0; i < myFilter.getSlots(); i++)
//			{
//				ItemStack filterPtr = myFilter.getStackInSlot(i);
//				if (!filterPtr.isEmpty())
//				{
//					isEmpty = false;
//					if (ItemStackUtil.matches(itemTarget, filterPtr))
//					{
//						isMatchingList = true;
//						break;
//					}
//				}
//			}
//		}
//		if (isIgnoreList)
//		{
//			return !isMatchingList;
//		}
//		else
//		{
//			return isEmpty || isMatchingList;
//		}
		return false;
	}
	
//	private static boolean getIsIgnoreList(ItemStack filterStack)
//	{
//		return filterStack.getOrCreateTag().getBoolean(NBTFILTER);
//	}

	/*
	 * 
	 * TOOLTIP
	 * 
	 */
    @Override
    public void appendHoverText(ItemStack stack, @Nullable TooltipContext pContext, List<Component> tooltip, TooltipFlag flagIn)
    {
    	super.appendHoverText(stack, pContext, tooltip, flagIn);

        if(nbt == null)
        {
        	tooltip.add(Component.empty());
        	tooltip.add(Component.translatable(ChatFormatting.GOLD + "PlayerId: "));
        	tooltip.add(Component.translatable(ChatFormatting.WHITE + "RightClick to bound Player Id."));
            tooltip.add(Component.translatable(ChatFormatting.GOLD + "MobId: "));
        	tooltip.add(Component.translatable(ChatFormatting.WHITE + "LeftClick Mobs to bound Mob Id."));
            tooltip.add(Component.translatable(ChatFormatting.GOLD + "Items: "));
        	tooltip.add(Component.translatable(ChatFormatting.WHITE + "Crouch RightClick and insert Blocks/Items."));
        	return;
        }
        
        Component appendPlayer = Component.literal("");
        Component appendMob = Component.literal("");
        
        if(nbt.hasUUID("bound_playerid"))
        	appendPlayer = Component.literal(ChatFormatting.WHITE + nbt.getString("bound_playername"));
        tooltip.add(Component.translatable(ChatFormatting.GOLD + "PlayerId: ").append(appendPlayer));
        
        if(nbt.contains("bound_mobname"))
        	appendMob = Component.literal(ChatFormatting.WHITE + nbt.getString("bound_mobname"));
        tooltip.add(Component.translatable(ChatFormatting.GOLD + "MobId: ").append(appendMob));

   		IItemHandler cap = stack.getCapability(Capabilities.ItemHandler.ITEM);
        if(cap != null)
        {
            tooltip.add(Component.translatable(ChatFormatting.GOLD + "Items: "));
            if (nbt.contains("bound_item"))
            {
    			for(int i = 0; i < cap.getSlots(); i++)
    			{
    				ItemStack filterPtr = cap.getStackInSlot(i);
    				if(!filterPtr.isEmpty() || BuiltInRegistries.ITEM.getKey(filterPtr.getItem()).getPath() != "air")
    					tooltip.add(Component.translatable(ChatFormatting.WHITE + BuiltInRegistries.ITEM.getKey(filterPtr.getItem()).getPath()));
    			}
            }
        }
    }
    
	/*
	 * 
	 * PLAYERID
	 * 
	 */
    public InteractionResultHolder<ItemStack> saveId(ServerPlayer playerIn, InteractionHand handIn)
	{
        ItemStack stack = playerIn.getItemInHand(handIn);
        nbt = new CompoundTag();
        
        if (!nbt.hasUUID("bound_playerid"))
        {
            nbt.putUUID("bound_playerid", playerIn.getUUID());
            nbt.putString("bound_playername", playerIn.getDisplayName().getString());
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
        }
        else if (!playerIn.getUUID().equals(nbt.getUUID("bound_playerid")))
        {
            playerIn.displayClientMessage(Component.translatable("info.witherutils.nobinding", nbt.getString("bound_playername")).withStyle(ChatFormatting.DARK_RED), true);
            return new InteractionResultHolder<>(InteractionResult.FAIL, stack);
        }
        return new InteractionResultHolder<>(InteractionResult.PASS, stack);
	}
//    public Optional<ServerPlayer> getPlayer(ServerLevel level, ItemStack stack)
//    {
//        return get(level, getTagOrEmpty(stack).getUUID("bound_playerid"));
//    }
//    public static Optional<ServerPlayer> get(ServerLevel level, UUID uuid)
//    {
//        return Optional.ofNullable(level.getServer().getPlayerList().getPlayer(uuid));
//    }
//    public static CompoundTag getTagOrEmpty(ItemStack stack)
//    {
//        CompoundTag nbt = stack.getTag();
//        return nbt != null ? nbt : new CompoundTag();
//    }
    
    /*
     * 
     * MOB
     * 
     */
    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity)
    {
        LivingEntity entityLiving = getEntityLivingFromClickedEntity(entity);
        
        IItemHandler cap = stack.getCapability(Capabilities.ItemHandler.ITEM);

        CompoundTag nbt = new CompoundTag();
        nbt.putString("bound_mobname", entityLiving.getDisplayName().getString());
        
        if(entityLiving != null && cap != null)
        {
        	ItemStack egg = setSpawnEgg(entity.getType(), stack);
        	if(cap.getStackInSlot(0).isEmpty())
        		cap.insertItem(0, egg, false);
        	player.level().playSound(player, player.blockPosition(), WUTSounds.FILTERMOB.get(), SoundSource.MASTER, 1.0F, 1.0F);
        }
        return super.onLeftClickEntity(stack, player, entity);
    }
    @Nullable
    public static LivingEntity getEntityLivingFromClickedEntity(Entity entity)
    {
        if(entity instanceof LivingEntity)
        {
            return (LivingEntity) entity;
        }
        return null;
    }
	@Nonnull
	public ItemStack setSpawnEgg(@Nonnull EntityType<?> entityType, ItemStack stack)
	{
		for(SpawnEggItem eggItem : SpawnEggItem.eggs())
		{
			if(eggItem.getType(stack).equals(entityType))
			{
				return new ItemStack(eggItem);
			}
		}
		return new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(entityType.toShortString() + "_spawn_egg")));
	}
}
