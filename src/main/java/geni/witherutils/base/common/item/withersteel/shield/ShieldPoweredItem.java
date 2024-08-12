//package geni.witherutils.base.common.item.withersteel.shield;
//
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//
//import javax.annotation.Nonnull;
//
//import com.google.common.collect.Multimap;
//import com.google.common.collect.Multimaps;
//
//import geni.witherutils.api.enchant.IWitherEnchantable;
//import geni.witherutils.base.client.ClientTooltipHandler;
//import geni.witherutils.base.common.base.IWitherPoweredItem;
//import geni.witherutils.base.common.base.WitherItemEnergy;
//import geni.witherutils.base.common.config.common.ItemsConfig;
//import geni.witherutils.base.common.init.WUTEnchants;
//import geni.witherutils.base.common.init.WUTItems;
//import geni.witherutils.base.common.init.WUTSounds;
//import geni.witherutils.base.common.item.withersteel.IWitherSteelItem;
//import geni.witherutils.base.common.item.withersteel.WitherSteelRecipeManager;
//import geni.witherutils.base.common.item.withersteel.attributes.WitherSteelAttributeModifiers;
//import geni.witherutils.core.common.util.EnergyUtil;
//import net.minecraft.client.renderer.item.ItemProperties;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.sounds.SoundEvents;
//import net.minecraft.sounds.SoundSource;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.InteractionResult;
//import net.minecraft.world.InteractionResultHolder;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.EquipmentSlot;
//import net.minecraft.world.entity.ai.attributes.Attribute;
//import net.minecraft.world.entity.ai.attributes.AttributeModifier;
//import net.minecraft.world.entity.ai.attributes.Attributes;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.ShieldItem;
//import net.minecraft.world.item.TooltipFlag;
//import net.minecraft.world.item.UseAnim;
//import net.minecraft.world.item.enchantment.EnchantmentHelper;
//import net.minecraft.world.level.Level;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.common.MinecraftForge;
//import net.minecraftforge.common.ToolAction;
//import net.minecraftforge.common.ToolActions;
//import net.minecraftforge.common.capabilities.ForgeCapabilities;
//import net.minecraftforge.common.capabilities.ICapabilityProvider;
//import net.minecraftforge.fml.loading.FMLEnvironment;
//
//public class ShieldPoweredItem extends ShieldItem implements IWitherSteelItem, IWitherEnchantable, IWitherPoweredItem {
//
//    static
//    {
//        MinecraftForge.EVENT_BUS.register(new WitherSteelRecipeManager());
//    }
//    
//	public ShieldPoweredItem()
//	{
//		super(new Item.Properties().stacksTo(1).fireResistant().defaultDurability(1024));
//        if(FMLEnvironment.dist.isClient())
//        {
//            registerShieldProperty();
//        }
//	}
//
//    @OnlyIn(Dist.CLIENT)
//    public void registerShieldProperty()
//    {
//    	ItemProperties.register(this, new ResourceLocation("blocking"), (stack, world, entity, i) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
//    	ItemProperties.register(this, new ResourceLocation("powered"), (stack, world, entity, i) -> EnergyUtil.hasEnergyHandler(stack) ? 1.0f : 0.0f);
//    }
//
//    @Override
//    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag)
//    {
//        super.appendHoverText(stack, world, list, flag);
//        ClientTooltipHandler.Tooltip.addInformation(stack, world, list, flag, true);
//    }
//    
//	@Override
//	public void setDamage(@Nonnull ItemStack stack, int damageNew)
//	{
//		int damage = damageNew - getDamage(stack);
//
//		if (damage > 0 && getPowerLevel(stack) > 0 && EnergyUtil.getEnergyStored(stack) > 0)
//		{
//			EnergyUtil.extractEnergy(stack, damage * getPowerPerDamagePoint(stack), false);
//		}
//		else
//		{
//			super.setDamage(stack, damageNew);
//		}
//	}
//
//	@SuppressWarnings("deprecation")
//	protected int getPowerPerDamagePoint(@Nonnull ItemStack stack)
//	{
//		if (getPowerLevel(stack) > 0)
//		{
//			return EnergyUtil.getMaxEnergyStored(stack) / this.getMaxDamage();
//		}
//		else
//		{
//			return 1;
//		}
//	}
//
//    @Override
//    public boolean isFoil(ItemStack pStack)
//    {
//        return false;
//    }
//    @Override
//    public int getMaxStackSize(ItemStack stack)
//    {
//        return 1;
//    }
//    @Override
//    public boolean isEnchantable(ItemStack stack)
//    {
//        return false;
//    }
//    @Override
//    public boolean isBookEnchantable(ItemStack stack, ItemStack book)
//    {
//        return false;
//    }
//    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair)
//    {
//        return repair.getItem() == WUTItems.WITHERSTEEL_INGOT.get();
//    }
//	@Override
//	public boolean canPerformAction(ItemStack stack, ToolAction toolAction)
//	{
//		return ToolActions.DEFAULT_SHIELD_ACTIONS.contains(toolAction);
//	}
//	@Nonnull
//	@Override
//	public UseAnim getUseAnimation(ItemStack stack)
//	{
//		return UseAnim.BLOCK;
//	}
//	@Override
//    @Nonnull
//    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack stack)
//    {
//        Multimap<Attribute, AttributeModifier> multimap = Multimaps.newSetMultimap(new HashMap<>(), HashSet::new);
//        if (equipmentSlot == EquipmentSlot.MAINHAND || equipmentSlot == EquipmentSlot.OFFHAND)
//        {
//        	multimap.put(Attributes.ARMOR_TOUGHNESS, WitherSteelAttributeModifiers.getArmorToughness(getPowerLevel(stack)));
//        	multimap.put(Attributes.KNOCKBACK_RESISTANCE, WitherSteelAttributeModifiers.getKnockbackResistance(getPowerLevel(stack)));
//        }
//        return multimap;
//    }
//    
//	@Override
//	public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity)
//	{
//		if(player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ShieldSteelItem)
//		{
//			player.level().playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.METAL_BREAK, SoundSource.NEUTRAL, 0.4F, 0.4F / (player.level().getRandom().nextFloat() * 0.4F + 0.8F));
//			player.level().playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ANVIL_LAND, SoundSource.NEUTRAL, 0.1F, 0.3F / (player.level().getRandom().nextFloat() * 0.4F + 0.4F));
//		}
//		return super.onLeftClickEntity(stack, player, entity);
//	}
//	
//	@Override
//	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
//	{
//		ItemStack itemstack = player.getItemInHand(hand);
//		
//		if(getPowerLevel(itemstack) == 0 || EnergyUtil.getEnergyStored(itemstack) == 0)
//			return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
//		
//		player.startUsingItem(hand);
//		world.playSound((Player)null, player.getX(), player.getY(), player.getZ(), WUTSounds.PLACEBOOMTWO.get(), SoundSource.NEUTRAL, 0.75F, 0.5F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
//		
//		return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
//	}
//	
//	@Override
//	public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected)
//	{
//		if (pEntity instanceof Player player)
//		{
//			if (EnergyUtil.hasEnergyHandler(pStack) && pIsSelected)
//			{
//				if (consumeByActive() && player.isUsingItem())
//					consumeCharge(pStack);
//			}
//		}
//	}
//
//	@Override
//	public int getEnergyUse(ItemStack stack)
//	{
//		return ItemsConfig.SHIELDENERGYUSE.get();
//	}
//
//	@Override
//	public int getMaxEnergy(ItemStack stack)
//	{
//		return getPowerLevel(stack) * ItemsConfig.SHIELDENERGY.get();
//	}
//	
//	@Override
//	public int getMaxTransfer(ItemStack stack)
//	{
//		return getMaxEnergy(stack) / 4;
//	}
//	
//	@Override
//	public int getUseDuration(ItemStack stack)
//	{
//		return 72000;
//	}
//
//	@Override
//	public boolean consumeByActive()
//	{
//		return true;
//	}
//
//	@Override
//	public boolean hasCharge(ItemStack stack)
//	{
//		return EnergyUtil.extractEnergy(stack, getEnergyUse(stack), true) > 0;
//	}
//
//	@Override
//	public void consumeCharge(ItemStack stack)
//	{
//		EnergyUtil.extractEnergy(stack, getEnergyUse(stack), false);
//	}
//
//	@Override
//    public int getPowerLevel(ItemStack stack)
//    {
//        int enchantlevel = 0;
//        if(stack.isEmpty() == false && EnchantmentHelper.getEnchantments(stack) != null && EnchantmentHelper.getEnchantments(stack).containsKey(WUTEnchants.ENERGY.get()))
//        {
//            int newlevel = EnchantmentHelper.getEnchantments(stack).get(WUTEnchants.ENERGY.get());
//            if(newlevel > enchantlevel)
//            {
//                enchantlevel = newlevel;
//            }
//        }
//        return enchantlevel;
//    }
//	
//	@Override
//	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt)
//	{
//		return new WitherItemEnergy.Item.Provider(stack, getMaxEnergy(stack), getEnergyUse(stack), getEnergyUse(stack));
//	}
//	
//    @Override
//    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
//    {
//        if (slotChanged)
//        {
//            return super.shouldCauseReequipAnimation(oldStack, newStack, true);
//        }
//        return oldStack.getItem() != newStack.getItem();
//    }
//    
//    @Override
//    public boolean isBarVisible(ItemStack pStack)
//    {
//        return getPowerLevel(pStack) > 0;
//    }
//    @Override
//    public int getBarWidth(ItemStack pStack)
//    {
//        return pStack
//            .getCapability(ForgeCapabilities.ENERGY)
//            .map(energyStorage -> Math.round(energyStorage.getEnergyStored() * 13.0F / energyStorage.getMaxEnergyStored()))
//            .orElse(0);
//    }
//    @Override
//    public int getBarColor(ItemStack pStack)
//    {
//        return 0x0000fff6;
//    }
//}
