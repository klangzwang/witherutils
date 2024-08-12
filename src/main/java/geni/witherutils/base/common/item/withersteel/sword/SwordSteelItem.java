package geni.witherutils.base.common.item.withersteel.sword;

import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.api.soul.IPlayerSoul;
import geni.witherutils.base.client.ClientTooltipHandler;
import geni.witherutils.base.client.render.item.EnergyBarDecorator;
import geni.witherutils.base.common.base.CreativeTabVariants;
import geni.witherutils.base.common.base.IWitherPoweredItem;
import geni.witherutils.base.common.base.WitherItemEnergy;
import geni.witherutils.base.common.config.common.ItemsConfig;
import geni.witherutils.base.common.init.WUTComponents;
import geni.witherutils.base.common.init.WUTEffects;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.base.common.item.withersteel.IWitherSteelItem;
import geni.witherutils.base.common.item.withersteel.WitherSteelRecipeManager;
import geni.witherutils.core.common.helper.SoulsHelper;
import geni.witherutils.core.common.item.IRotatingItem;
import geni.witherutils.core.common.network.CoreNetwork;
import geni.witherutils.core.common.network.PacketDirectionDash;
import geni.witherutils.core.common.util.EnergyUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab.Output;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import net.neoforged.neoforge.energy.ComponentEnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class SwordSteelItem extends SwordItem implements IItemExtension, IWitherSteelItem, IWitherPoweredItem, IRotatingItem, IPlayerSoul, CreativeTabVariants {

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
		public TagKey<Block> getIncorrectBlocksForDrops()
		{
			return null;
		}
		@Override
		public int getEnchantmentValue()
		{
			return 20;
		}
		@Override
		public Ingredient getRepairIngredient()
		{
			return Ingredient.of(new ItemStack(Items.IRON_INGOT));
		}
	};
	
    static
    {
        NeoForge.EVENT_BUS.register(new WitherSteelRecipeManager());
    }
    
	public SwordSteelItem()
	{
		super(WITHERSTEEL, new Item.Properties().stacksTo(1).fireResistant());
        if(FMLEnvironment.dist.isClient())
        {
        	registerSwordProperty();
        }
    }

	@OnlyIn(Dist.CLIENT)
    public void registerSwordProperty()
    {
        ItemProperties.register(this, WitherUtilsRegistry.loc("powered"), (stack, world, entity, i) -> {
            return entity != null && EnergyUtil.getEnergyStored(stack) > 0 ? 1.0F : 0.0F;
        });
        ItemProperties.register(this, WitherUtilsRegistry.loc("swinging"), (stack, world, entity, i) -> {
            return entity != null && entity.getMainHandItem().getItem() instanceof SwordSteelItem && EnergyUtil.getEnergyStored(stack) > 0 && entity.swinging ? 1.0F : 0.0F;
        });
    }
	
	@Override
	public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack)
	{
		ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
    	
//		if(!hasCharge(stack))
//		{
//			builder.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, (float)-1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
//			return builder.build();
//		}
//		else
//		{
//			if (BaseConfig.COMMON.WITHERSTEEL.witherSteelSwordPowerUsePerHit.get() <= 0 || EnergyUtil.getEnergyStored(stack) >= BaseConfig.COMMON.WITHERSTEEL.witherSteelSwordPowerUsePerHit.get())
//			{
//				builder.add(Attributes.ATTACK_DAMAGE, WitherSteelAttributeModifiers.getAttackDamage(getPowerLevel(stack)), EquipmentSlotGroup.MAINHAND);
//				builder.add(Attributes.ATTACK_SPEED, WitherSteelAttributeModifiers.getAttackSpeed(getPowerLevel(stack)), EquipmentSlotGroup.MAINHAND);
//			}
//			builder.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, SoulsHelper.getSouls(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
//		}
		return builder.build();
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext pContext, List<Component> list, TooltipFlag pTooltipFlag)
	{
		super.appendHoverText(stack, pContext, list, pTooltipFlag);
		
        ClientTooltipHandler.Tooltip.addInformation(stack, pContext, list, pTooltipFlag, true);
        
    	list.add(Component.empty());
        list.add(Component.literal(ChatFormatting.GRAY + "When SoulPowered:"));
        list.add(Component.literal(ChatFormatting.DARK_GREEN + " " + SoulsHelper.getSouls() + " Soul Attack Damage"));
	}
    
	protected int getPowerPerDamagePoint(@Nonnull ItemStack stack)
	{
		return EnergyUtil.getMaxEnergyStored(stack) / WITHERSTEEL.getUses();
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
	    if (attacker instanceof Player player)
	    {
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
		boolean isCreative = player.isCreative();
		if (hasResources(stack) || isCreative)
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
		        	consumeResources(stack);
	    			player.addEffect(new MobEffectInstance(WUTEffects.BLIND, 20, 0, false, false));
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

	public static int getMaxEnergy()
    {
        return ItemsConfig.SWORDENERGY.get();
    }

    public boolean hasResources(ItemStack stack)
    {
        return WitherItemEnergy.hasEnergy(stack, ItemsConfig.SWORDENERGYUSE.get());
    }

    public void consumeResources(ItemStack stack)
    {
    	WitherItemEnergy.extractEnergy(stack, ItemsConfig.SWORDENERGYUSE.get(), false);
    }

	@Override
	public void addAllVariants(Output modifier)
	{
        modifier.accept(this);
        ItemStack is = new ItemStack(this);
        WitherItemEnergy.setFull(is);
        modifier.accept(is);
	}

    @Override
    public boolean isBarVisible(ItemStack stack)
    {
        return true; // getPowerLevel(stack) > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack)
    {
        var energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (energyStorage != null)
        {
            return Math.round(energyStorage.getEnergyStored() * 13.0F / energyStorage.getMaxEnergyStored());
        }
        return 0;
    }

    @Override
    public int getBarColor(ItemStack pStack)
    {
        return EnergyBarDecorator.BAR_COLOR;
    }

    /*
     * 
     * ITEM
     * 
     */
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
    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        if (slotChanged)
        {
            return super.shouldCauseReequipAnimation(oldStack, newStack, true);
        }
        return oldStack.getItem() != newStack.getItem();
    }
    
    /*
     * 
     * ROTATINGITEM
     * 
     */
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
	
    /*
     * 
     * ENERGY
     * 
     */
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
        int powerlevel = 0;
        if(stack.isEmpty() == false && stack.get(WUTComponents.LEVEL) != null)
        {
            int newlevel = stack.get(WUTComponents.LEVEL);
            if(newlevel > powerlevel)
            {
            	powerlevel = newlevel;
            }
        }
        return powerlevel;
    }
	@Override
	public ICapabilityProvider<ItemStack, Void, IEnergyStorage> initEnergyCap()
	{
		return (stack, v) -> new ComponentEnergyStorage(stack, WUTComponents.ENERGY.get(), SwordSteelItem.getMaxEnergy());
	}
}
