package geni.witherutils.base.common.item.withersteel.sword;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import geni.witherutils.api.enchant.IWitherEnchantable;
import geni.witherutils.api.item.IRotatingItem;
import geni.witherutils.base.client.ClientTooltipHandler;
import geni.witherutils.base.common.base.IWitherPoweredItem;
import geni.witherutils.base.common.base.WitherItemEnergy;
import geni.witherutils.base.common.config.BaseConfig;
import geni.witherutils.base.common.config.common.ItemsConfig;
import geni.witherutils.base.common.init.WUTEffects;
import geni.witherutils.base.common.init.WUTEnchants;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.base.common.item.withersteel.IWitherSteelItem;
import geni.witherutils.base.common.item.withersteel.WitherSteelRecipeManager;
import geni.witherutils.base.common.item.withersteel.attributes.WitherSteelAttributeModifiers;
import geni.witherutils.base.common.soul.SoulOrbManager;
import geni.witherutils.core.common.network.CoreNetwork;
import geni.witherutils.core.common.network.PacketDirectionDash;
import geni.witherutils.core.common.util.EnergyUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class SwordSteelItem extends SwordItem implements IWitherSteelItem, IWitherEnchantable, IWitherPoweredItem, IRotatingItem {

	static final Tier WITHERSTEEL = new Tier()
	{
		@Override
		public int getUses()
		{
			return 2031;
		}
		@Override
		public float getSpeed()
		{
			return 6.0f;
		}
		@Override
		public float getAttackDamageBonus()
		{
			return 4.0f;
		}
		@Override
		public int getLevel()
		{
			return 3;
		}
		@Override
		public int getEnchantmentValue()
		{
			return 20;
		}
		@Override
		public Ingredient getRepairIngredient()
		{
			return Ingredient.of(new ItemStack(WUTItems.SOULISHED_INGOT.get()));
		}
	};
	
    static
    {
        MinecraftForge.EVENT_BUS.register(new WitherSteelRecipeManager());
    }
    
	public SwordSteelItem()
	{
		super(WITHERSTEEL, 3, -2.4F, new Item.Properties().stacksTo(1).fireResistant());
    }
    
    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag)
    {
        super.appendHoverText(stack, world, list, flag);
        ClientTooltipHandler.Tooltip.addInformation(stack, world, list, flag, true);
    }
    
	protected int getPowerPerDamagePoint(@Nonnull ItemStack stack)
	{
		return EnergyUtil.getMaxEnergyStored(stack) / WITHERSTEEL.getUses();
	}

    @Override
    @Nonnull
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack stack)
    {
        Multimap<Attribute, AttributeModifier> multimap = Multimaps.newSetMultimap(new HashMap<>(), HashSet::new);
        if (equipmentSlot == EquipmentSlot.MAINHAND)
        {
        	multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", -2 + SoulOrbManager.getCurrentSouls(), Operation.ADDITION));
        	
            if (BaseConfig.COMMON.WITHERSTEEL.witherSteelSwordPowerUsePerHit.get() <= 0 || EnergyUtil.getEnergyStored(stack) >= BaseConfig.COMMON.WITHERSTEEL.witherSteelSwordPowerUsePerHit.get())
            {
                multimap.put(Attributes.ATTACK_DAMAGE, WitherSteelAttributeModifiers.getAttackDamage(getPowerLevel(stack)));
                multimap.put(Attributes.ATTACK_SPEED, WitherSteelAttributeModifiers.getAttackSpeed(getPowerLevel(stack)));
            }
        }
        return multimap;
    }
    
	@SuppressWarnings("resource")
	@Override
	public boolean hurtEnemy(@Nonnull ItemStack stack, @Nonnull LivingEntity target, @Nonnull LivingEntity attacker)
	{
        if (target == null)
        {
            return false;
        }
        if (attacker.level().isClientSide)
        {
            return false;
        }
	    if (attacker instanceof Player)
	    {
	        Player player = (Player) attacker;
	        if (EnergyUtil.getEnergyStored(stack) > 0)
	            EnergyUtil.extractEnergy(player.getMainHandItem(), getPowerPerDamagePoint(stack), false);
	        else
	        	super.hurtEnemy(stack, target, attacker);
	    }
	    return true;
	}
	
	@SuppressWarnings("resource")
    @Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
        if (EnergyUtil.getEnergyStored(stack) > 0)
        {
    		final boolean forward = Minecraft.getInstance().options.keyUp.isDown();
    		final boolean backward = Minecraft.getInstance().options.keyDown.isDown();
    		final boolean left = Minecraft.getInstance().options.keyLeft.isDown();
    		final boolean right = Minecraft.getInstance().options.keyRight.isDown();

			if(forward || backward || left || right)
			{
	    		if (level.isClientSide)
	    		{
	    			if(forward)
	    				sendDash(0);
	    			else if(backward)
	    				sendDash(1);
	    			else if(left)
	    				sendDash(2);
	    			else if(right)
	    				sendDash(3);
	    			
			        player.playNotifySound(WUTSounds.SWOOSH.get(), SoundSource.PLAYERS, 0.5F, 1F);
			        player.playNotifySound(WUTSounds.FEAR.get(), SoundSource.PLAYERS, 0.5F, 2F);
	    		}
	    		else
	    		{
		        	consumeCharge(stack);
					
	    			player.addEffect(new MobEffectInstance(WUTEffects.BLIND.get(), 20, 0, false, false));
	        		player.getCooldowns().addCooldown(this, 20);
	        	    player.awardStat(Stats.ITEM_USED.get(this));
	    		}
			}
        }
       	return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}
	
	private static void sendDash(int direction)
	{
		CoreNetwork.sendToServer(new PacketDirectionDash(0L, (byte) direction));
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
        return repair.getItem() == WUTItems.SOULISHED_INGOT.get();
    }

	@Override
	public boolean consumeByActive()
	{
		return false;
	}

	@Override
	public int getEnergyUse(ItemStack stack)
	{
		return ItemsConfig.SWORDENERGYUSE.get();
	}

	@Override
	public int getMaxEnergy(ItemStack stack)
	{
		return getPowerLevel(stack) * ItemsConfig.SWORDENERGY.get();
	}

	@Override
	public int getMaxTransfer(ItemStack stack)
	{
		return getMaxEnergy(stack) / 4;
	}
	
	@Override
	public boolean hasCharge(ItemStack stack)
	{
		return EnergyUtil.extractEnergy(stack, getEnergyUse(stack), true) > 0;
	}

	@Override
	public void consumeCharge(ItemStack stack)
	{
		EnergyUtil.extractEnergy(stack, getEnergyUse(stack), false);
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
        return enchantlevel;
    }
    
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt)
	{
		return new WitherItemEnergy.Item.Provider(stack, getMaxEnergy(stack), getEnergyUse(stack), getEnergyUse(stack));
	}
	
    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        if (slotChanged)
        {
            return super.shouldCauseReequipAnimation(oldStack, newStack, true);
        }
        return oldStack.getItem() != newStack.getItem();
    }
    
    @Override
    public boolean isBarVisible(ItemStack pStack)
    {
        return getPowerLevel(pStack) > 0;
    }
    @Override
    public int getBarWidth(ItemStack pStack)
    {
        return pStack
            .getCapability(ForgeCapabilities.ENERGY)
            .map(energyStorage -> Math.round(energyStorage.getEnergyStored() * 13.0F / energyStorage.getMaxEnergyStored()))
            .orElse(0);
    }
    @Override
    public int getBarColor(ItemStack pStack)
    {
        return 0x0000fff6;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer)
    {
        setupBEWLR(consumer);
    }
    
    @Override
	public float getTicksPerRotation()
	{
		return 45;
	}
	@Override
	public boolean hasItemOnTop()
	{
		return false;
	}
	@Override
	public boolean isRotating()
	{
		return false;
	}
}
