package geni.witherutils.base.common.item.withersteel.armor;

import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import geni.witherutils.api.enchant.IWitherEnchantable;
import geni.witherutils.api.steelupable.ISteelUpable;
import geni.witherutils.base.client.render.item.ItemBarRenderer;
import geni.witherutils.base.client.render.layer.ModelHandler;
import geni.witherutils.base.common.base.IWitherPoweredItem;
import geni.witherutils.base.common.base.WitherArmorMaterial;
import geni.witherutils.base.common.base.WitherItemEnergy;
import geni.witherutils.base.common.init.WUTEnchants;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.base.common.item.withersteel.IWitherSteelItem;
import geni.witherutils.base.common.item.withersteel.WitherSteelRecipeManager;
import geni.witherutils.base.common.item.withersteel.armor.handler.ArmorController;
import geni.witherutils.core.common.util.EnergyUtil;
import geni.witherutils.core.common.util.PlayerUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.extensions.IForgeItem;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public class SteelArmorItem extends ArmorItem implements IWitherSteelItem, ISteelUpable, IForgeItem, IWitherEnchantable, IWitherPoweredItem {

	public static final int BasePower = 100000; 
	public static final int[] CAPACITY = new int[] { BasePower, BasePower, BasePower * 2, BasePower * 2 };
	public static final int[] RF_PER_DAMAGE_POINT = new int[] { BasePower, BasePower, BasePower * 2, BasePower * 2 };
	public static final String[] NAMES = new String[] { "helmet", "chestplate", "leggings", "boots" };

	static
	{
		MinecraftForge.EVENT_BUS.register(new WitherSteelRecipeManager());
		MinecraftForge.EVENT_BUS.register(new ArmorController());
	}
	
	@SuppressWarnings("rawtypes")
	@OnlyIn(Dist.CLIENT)
	private HumanoidModel model;
    private int level;
    
    public SteelArmorItem(WitherArmorMaterial material, Type type, Item.Properties props)
    {
        super(material, type, props);
    }

    @Override
    public boolean isRepairable(ItemStack stack)
    {
    	return false;
    }
    
    @Override
    public boolean isItemForRepair(ItemStack stack)
    {
    	return stack.getItem() == WUTItems.WITHERSTEEL_INGOT.get();
    }
    
    @Override
    public int getIngotsRequiredForFullRepair()
    {
        switch (type)
        {
            case HELMET:
              return 5;
            case CHESTPLATE:
              return 8;
            case LEGGINGS:
              return 7;
            case BOOTS:
            default:
              return 4;
        }
    }

	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
	    tooltip.add(Component.translatable(""));
	    tooltip.add(Component.translatable("Vanilla Anvil Upgradables:").withStyle(ChatFormatting.BLUE, ChatFormatting.UNDERLINE));
	    tooltip.add(Component.translatable("Energy Upgrade").withStyle(ChatFormatting.GRAY));
	    tooltip.add(Component.translatable("SoulBank Item needed").withStyle(ChatFormatting.DARK_GRAY));

    	switch(type)
    	{
			case BOOTS -> {
				tooltip.add(Component.translatable(""));
				tooltip.add(Component.translatable("Double Jump Upgrade").withStyle(ChatFormatting.GRAY));
			}
			case CHESTPLATE -> {
				tooltip.add(Component.translatable(""));
				tooltip.add(Component.translatable("Feather Fall Upgrade").withStyle(ChatFormatting.GRAY));
			}
			case HELMET -> {
				tooltip.add(Component.translatable(""));
				tooltip.add(Component.translatable("Solar Charging Upgrade").withStyle(ChatFormatting.GRAY));
				tooltip.add(Component.translatable("SolarPanel Item needed").withStyle(ChatFormatting.DARK_GRAY));
				tooltip.add(Component.translatable("Night Vision Upgrade").withStyle(ChatFormatting.GRAY));
			}
			case LEGGINGS -> {
				tooltip.add(Component.translatable(""));
				tooltip.add(Component.translatable("Sprinting Upgrade").withStyle(ChatFormatting.GRAY));
			}
			default -> {}
    	}

    	tooltip.add(Component.translatable(""));
	}
	
    /*
     * 
     * BAR
     * 
     */
    @Override
    public boolean isBarVisible(ItemStack pStack)
    {
        return pStack.isDamaged() || isChargeable(pStack);
    }
	@Override
	public int getBarWidth(ItemStack stack)
	{
		LazyOptional<IEnergyStorage> cap = stack.getCapability(ForgeCapabilities.ENERGY);
        if(!cap.isPresent())
        {
        	return super.getBarWidth(stack);
        }
        return cap.map(e -> Math.min(13 * e.getEnergyStored() / e.getMaxEnergyStored(), 13)).orElse(super.getBarWidth(stack));
	}
    @Override
    public int getBarColor(ItemStack pStack)
    {
    	if(isChargeable(pStack))
    		return ItemBarRenderer.ENERGY_BAR_RGB;
    	else
    		return super.getBarColor(pStack);
    }

    protected void setFullCharge(ItemStack pStack)
    {
    	EnergyUtil.setFull(pStack);
    }

    /**
     * 
     * ARMORMODEL
     * 
     */
	@Override
	@OnlyIn(Dist.CLIENT)
	public void initializeClient(Consumer<IClientItemExtensions> consumer)
	{
		consumer.accept(new IClientItemExtensions()
		{
			@Override
			public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot,  HumanoidModel<?> _default)
			{
				switch(armorSlot)
				{
					case HEAD -> { return ModelHandler.armorModel(ModelHandler.steelarmor_head, armorSlot, entityLiving); }
					case CHEST -> { return ModelHandler.armorModel(ModelHandler.steelarmor_body, armorSlot, entityLiving); }
					case LEGS -> { return ModelHandler.armorModel(ModelHandler.steelarmor_legs, armorSlot, entityLiving); }
					case FEET -> { return ModelHandler.armorModel(ModelHandler.steelarmor_boots, armorSlot, entityLiving); }
					default -> {}
				}
				return model;
			}
		});	
	}
    @Nullable
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String stringtype)
    {
    	return "witherutils:textures/model/armor/withersteel_armor.png";
    }

    /**
     * 
     * ARMORITEM
     * 
     */
    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        if(slotChanged)
        {
            return super.shouldCauseReequipAnimation(oldStack, newStack, true);
        }
        return oldStack.getItem() != newStack.getItem();
    }
    @Override
    public boolean isFoil(ItemStack pStack)
    {
        return false;
    }
    @Override
    public int getMaxStackSize(ItemStack stack)
    {
    	return 1;
    }
    @Override
    public boolean isEnchantable(ItemStack stack)
    {
    	return false;
    }
    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book)
    {
    	return false;
    }
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair)
    {
        return repair.getItem() == Items.IRON_INGOT;
    }

	/**
	 * 
	 * TICK
	 * 
	 */
    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex)
    {
    	super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
    	
		if(level.isClientSide)
			return;
		
    	if(!isChargeable(stack))
    		return;
		if(!player.getItemBySlot(EquipmentSlot.HEAD).isEmpty() && getSolarLevel(player.getItemBySlot(EquipmentSlot.HEAD)) > 0)
			doSolarCharge(player, getSolarLevel(player.getItemBySlot(EquipmentSlot.HEAD)));
    }
    
    /**
     * 
     * ENERGY
     * 
     */
    public boolean hasCharge(ItemStack stack)
    {
        return EnergyUtil.extractEnergy(stack, getEnergyUse(), true) > 0;
    }
    public boolean hasChargeMoreThen(ItemStack stack, int amount)
    {
        return EnergyUtil.extractEnergy(stack, getEnergyUse(), true) > amount;
    }
    public void doSolarCharge(Player player, int solarlevel)
    {
        for(ItemStack armorstack : PlayerUtil.invArmorStacks(player))
        {
            long amount = EnergyUtil.calculateSunEnergy(player.level(), player.blockPosition());
            if(amount <= 0)
            	break;
    		if(EnergyUtil.getEnergyStored(armorstack) < EnergyUtil.getMaxEnergyStored(armorstack))
    			EnergyUtil.receiveEnergy(armorstack, (int)amount * solarlevel / 2 * 1, false);
        }
    }
	@Override
	public int getSolarLevel(ItemStack stack)
	{
		int enchantlevel = 0;
		if(stack.isEmpty() == false && EnchantmentHelper.getEnchantments(stack) != null && EnchantmentHelper.getEnchantments(stack).containsKey(WUTEnchants.SOLAR_POWER.get()))
		{
			int newlevel = EnchantmentHelper.getEnchantments(stack).get(WUTEnchants.SOLAR_POWER.get());
			if(newlevel > enchantlevel)
			{
				enchantlevel = newlevel;
			}
		}
		return enchantlevel;
	}
	@Override
	public int getPowerLevel(ItemStack stack)
	{
		int enchantlevel = 0;
		if(stack.isEmpty() == false && EnchantmentHelper.getEnchantments(stack) != null && EnchantmentHelper.getEnchantments(stack).containsKey(WUTEnchants.ENERGY.get()))
		{
			int newlevel = EnchantmentHelper.getEnchantments(stack).get(WUTEnchants.ENERGY.get());
			if(newlevel > enchantlevel)
			{
				enchantlevel = newlevel;
			}
		}
		setLevel(enchantlevel);
		return enchantlevel;
	}
	@SuppressWarnings("deprecation")
	@Override
	public boolean isChargeable(ItemStack stack)
	{
		int level = EnchantmentHelper.getItemEnchantmentLevel(WUTEnchants.ENERGY.get(), stack);
		if(level > 0)
		{
			return true;
		}
		return false;
	}
    private int getEnergyUse()
    {
		return level * 330;
	}
    private int getMaxEnergy()
    {
		return level * 100000;
    }

    /**
     * 
     * DAMAGE / ENERGY
     * 
     */
    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int damage, T player, Consumer<T> onBroken)
    {
    	if(hasCharge(stack))
    	{
			int energyToUse = Math.round((float)damage * 10000);
			int currentEnergy = EnergyUtil.getEnergyStored(stack);
			int energyRemoved = Math.min(energyToUse, currentEnergy);
			damage = damage - level;
			EnergyUtil.extractEnergy(stack, energyRemoved, false);
			return damage;
    	}
   		return damage;
    }

    /**
     * 
     * CAPABILITY
     * 
     */
    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt)
    {
    	getPowerLevel(stack);
		if(level > 0)
	    	return new WitherItemEnergy.Item.Provider(stack, getMaxEnergy(), getEnergyUse(), getEnergyUse());
		else
			return super.initCapabilities(stack, nbt);
    }

	@Override
	public Tag serializeNBT()
	{
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("level", this.level);
        return nbt;
	}
	@Override
	public void deserializeNBT(Tag nbt)
	{
        if(nbt instanceof CompoundTag tag)
        {
            this.level = tag.getInt("level");
        }
	}
	@Override
	public int getLevel()
	{
		return level;
	}
	@Override
	public void setLevel(int level)
	{
		this.level = level;
	}
	@Override
	public void addUpgrade(ItemStack upgrade)
	{
	}

	@Override
	public boolean consumeByActive()
	{
		return false;
	}

	@Override
	public int getEnergyUse(ItemStack stack)
	{
		return 0;
	}

	@Override
	public int getMaxEnergy(ItemStack stack)
	{
		return 0;
	}

	@Override
	public int getMaxTransfer(ItemStack stack)
	{
		return 0;
	}

	@Override
	public void consumeCharge(ItemStack stack)
	{
		EnergyUtil.extractEnergy(stack, 2, false);
	}
}
