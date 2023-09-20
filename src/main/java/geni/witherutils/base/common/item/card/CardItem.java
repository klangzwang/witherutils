package geni.witherutils.base.common.item.card;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.base.WitherItem;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.core.common.util.ItemStackUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

public class CardItem extends WitherItem {

//	private boolean locked = false;
	private static final String NBTFILTER = "itemfilter";

	public CardItem()
	{
		super(new Properties().stacksTo(1).rarity(Rarity.create("NONEVIL", ChatFormatting.GRAY)));
        if(FMLEnvironment.dist.isClient())
        {
        	registerCardProperty();
        }
    }
    
	@OnlyIn(Dist.CLIENT)
    public void registerCardProperty()
    {
        ItemProperties.register(this, WitherUtils.loc("bound"), (stack, world, entity, i) -> {
            float f = 0.0F;
            CompoundTag nbt = stack.getTag();
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
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn)
	{
		if(!worldIn.isClientSide)
		{
			if(playerIn.isCrouching())
				openItemScreen((ServerPlayer) playerIn, handIn);
			else
				saveId((ServerPlayer) playerIn, handIn);
		}
		return super.use(worldIn, playerIn, handIn);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt)
	{
		return new CardCapabilityProvider();
	}

	public boolean isFilterSet(ItemStack stack)
	{
		IItemHandler cap = stack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
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
	
	public static boolean filterAllowsTransfer(ItemStack filterStack, ItemStack itemTarget)
	{
		boolean isEmpty = false;
		boolean isMatchingList = false;
		IItemHandler myFilter = filterStack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
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
	
	public static boolean filterAllowsExtract(ItemStack filterStack, ItemStack itemTarget)
	{
		if (filterStack.getItem() instanceof CardItem == false)
		{
			return true;
		}

		boolean isEmpty = false;
		boolean isMatchingList = false;
		boolean isIgnoreList = getIsIgnoreList(filterStack);
		
		IItemHandler myFilter = filterStack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);

		if (myFilter != null)
		{
			for (int i = 0; i < myFilter.getSlots(); i++)
			{
				ItemStack filterPtr = myFilter.getStackInSlot(i);
				if (!filterPtr.isEmpty())
				{
					isEmpty = false;
					if (ItemStackUtil.matches(itemTarget, filterPtr))
					{
						isMatchingList = true;
						break;
					}
				}
			}
		}
		if (isIgnoreList)
		{
			return !isMatchingList;
		}
		else
		{
			return isEmpty || isMatchingList;
		}
	}
	
	private static boolean getIsIgnoreList(ItemStack filterStack)
	{
		return filterStack.getOrCreateTag().getBoolean(NBTFILTER);
	}
	
	/*
	 * 
	 * TOOLTIP
	 * 
	 */
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
    	super.appendHoverText(stack, worldIn, tooltip, flagIn);
    	
   		IItemHandler cap = stack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
        CompoundTag nbt = stack.getTag();

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
        
        if(cap == null)
        	return;
        
        tooltip.add(Component.translatable(ChatFormatting.GOLD + "Items: "));
        if (nbt.contains("bound_item"))
        {
			for(int i = 0; i < cap.getSlots(); i++)
			{
				ItemStack filterPtr = cap.getStackInSlot(i);
				if(!filterPtr.isEmpty() || ForgeRegistries.ITEMS.getKey(filterPtr.getItem()).getPath() != "air")
					tooltip.add(Component.translatable(ChatFormatting.WHITE + ForgeRegistries.ITEMS.getKey(filterPtr.getItem()).getPath()));
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
        CompoundTag nbt = stack.getOrCreateTag();
        
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
    public Optional<ServerPlayer> getPlayer(ServerLevel level, ItemStack stack)
    {
        return get(level, getTagOrEmpty(stack).getUUID("bound_playerid"));
    }
    public static Optional<ServerPlayer> get(ServerLevel level, UUID uuid)
    {
        return Optional.ofNullable(level.getServer().getPlayerList().getPlayer(uuid));
    }
    public static CompoundTag getTagOrEmpty(ItemStack stack)
    {
        CompoundTag nbt = stack.getTag();
        return nbt != null ? nbt : new CompoundTag();
    }
    
    /*
     * 
     * MOB
     * 
     */
    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity)
    {
        LivingEntity entityLiving = getEntityLivingFromClickedEntity(entity);
        IItemHandler cap = stack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);

        CompoundTag nbt = stack.getOrCreateTag();
        nbt.putString("bound_mobname", entityLiving.getDisplayName().getString());
        
        if(entityLiving != null && cap != null)
        {
        	ItemStack egg = setSpawnEgg(entity.getType());
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
	public ItemStack setSpawnEgg(@Nonnull EntityType<?> entityType)
	{
		for(SpawnEggItem eggItem : SpawnEggItem.eggs())
		{
			if(eggItem.getType(null).equals(entityType))
			{
				return new ItemStack(eggItem);
			}
		}
		return new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(entityType.toShortString() + "_spawn_egg")));
	}
	
	/*
	 * 
	 * ITEM
	 * 
	 */
	public InteractionResultHolder<ItemStack> openItemScreen(ServerPlayer playerIn, InteractionHand handIn)
	{
		ItemStack stack = playerIn.getItemInHand(handIn);
        CompoundTag nbt = stack.getOrCreateTag();

		NetworkHooks.openScreen(playerIn, new CardContainerProvider(), playerIn.blockPosition());
		
        if (!nbt.contains("bound_item"))
            nbt.putString("bound_item", "");
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}
}
