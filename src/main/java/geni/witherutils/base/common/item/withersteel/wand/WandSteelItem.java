package geni.witherutils.base.common.item.withersteel.wand;

import java.util.List;
import java.util.function.Consumer;

import geni.witherutils.api.soul.IPlayerSoul;
import geni.witherutils.base.client.render.item.EnergyBarDecorator;
import geni.witherutils.base.common.base.CreativeTabVariants;
import geni.witherutils.base.common.base.IWitherPoweredItem;
import geni.witherutils.base.common.base.WitherItem;
import geni.witherutils.base.common.base.WitherItemEnergy;
import geni.witherutils.base.common.config.common.ItemsConfig;
import geni.witherutils.base.common.init.WUTComponents;
import geni.witherutils.base.common.item.withersteel.IWitherSteelItem;
import geni.witherutils.base.common.item.withersteel.WitherSteelRecipeManager;
import geni.witherutils.core.common.helper.SoulsHelper;
import geni.witherutils.core.common.item.IRotatingItem;
import geni.witherutils.core.common.util.EnergyUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab.Output;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.energy.ComponentEnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class WandSteelItem extends WitherItem implements IWitherSteelItem, IWitherPoweredItem, IRotatingItem, IPlayerSoul, CreativeTabVariants {

    static
    {
        NeoForge.EVENT_BUS.register(new WitherSteelRecipeManager());
    }
	
	public WandSteelItem()
	{
		super(new Item.Properties().stacksTo(1).fireResistant());
    }
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext pContext, List<Component> list, TooltipFlag flag)
	{
		super.appendHoverText(stack, pContext, list, flag);

        list.add(Component.empty());
        list.add(Component.literal(ChatFormatting.GRAY + "When SoulPowered:"));
        list.add(Component.literal(ChatFormatting.DARK_GREEN + " " + SoulsHelper.getSouls() / 2 + " Soul Attack Damage"));
	}
	
//	@Override
//	public void inventoryTick(ItemStack stack, Level level, Entity entity, int value, boolean isSelected)
//	{
//		if(entity instanceof Player player)
//		{
//			if(!isSelected)
//				return;
//	    	if(EnergyUtil.getEnergyStored(stack) < ItemsConfig.WANDENERGYUSE.get())
//	    		return;
//			if(player.getCooldowns().isOnCooldown(stack.getItem()))
//				return;
//			if(!player.isCrouching())
//				return;
//			
//			if(player.getMainHandItem() == stack)
//				player.swing(InteractionHand.MAIN_HAND);
//			else
//				player.swing(InteractionHand.OFF_HAND);
//			
//			if(!level.isClientSide)
//			{
//				float f = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
//				
//				if(player instanceof ServerPlayer)
//				{
//					ServerPlayer serverPlayer = (ServerPlayer) player;
//	    			f = f + SoulsHelper.getSouls(serverPlayer) / 2;
//				}
//				float f3 = 1.0F; // * f;
//				
//				Vec3 look = player.getLookAngle();
//				
//				List<LivingEntity> targets = getTargetsInCone(player.getCommandSenderWorld(), player.position().subtract(look), player.getLookAngle().scale(9), 1.57079f, .5f);
//				for(LivingEntity t : targets)
//				{
//					if (t != player && t != entity && !player.isAlliedTo(t) && (!(t instanceof ArmorStand) || !((ArmorStand)t).isMarker()))
//					{
//	                    t.knockback((double) 0.4F, (double) Mth.sin(player.getYRot() * ((float) Math.PI / 180F)), (double)(-Mth.cos(player.getYRot() * ((float) Math.PI / 180F))));
//	                    t.hurt(level.damageSources().playerAttack(player), f3);
//					}
//				}
//				
//				player.getCooldowns().addCooldown(this, ItemsConfig.WANDPORTDELAY.get() * 2);
//				player.addEffect(new MobEffectInstance(WUTEffects.BLIND.get(), 5, 0, false, false));
//				EnergyUtil.extractEnergy(player.getMainHandItem(), getEnergyUse(stack), false);
//
//            	for(int i = 0; i < 3; i++)
//            	{
//	            	SoulOrbProjectile orb = new SoulOrbProjectile(level, player);
//	            	orb.shootFromRotation(player, player.getXRot(), player.getYRot() - 20 + (20 * i), 0.0F, 4.0F, 1.0F);
//	            	orb.handleEntityEvent((byte)3);
//	            	level.addFreshEntity(orb);
//            	}
//			}
//			else
//			{
//	            player.playNotifySound(WUTSounds.SWOOSH.get(), SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
//	            player.playNotifySound(WUTSounds.FEAR.get(), SoundSource.NEUTRAL, 0.5F, 2F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
//			}
//		}
//	}
	
	public static List<LivingEntity> getTargetsInCone(Level world, Vec3 start, Vec3 dir, float spreadAngle, float truncationLength)
	{
		double length = dir.length();
		Vec3 dirNorm = dir.normalize();
		double radius = Math.tan(spreadAngle/2)*length;

		Vec3 endLow = start.add(dir).subtract(radius, radius, radius);
		Vec3 endHigh = start.add(dir).add(radius, radius, radius);

		AABB box = new AABB(minInArray(start.x, endLow.x, endHigh.x), minInArray(start.y, endLow.y, endHigh.y), minInArray(start.z, endLow.z, endHigh.z),
				maxInArray(start.x, endLow.x, endHigh.x), maxInArray(start.y, endLow.y, endHigh.y), maxInArray(start.z, endLow.z, endHigh.z));

		List<LivingEntity> list = world.getEntitiesOfClass(LivingEntity.class, box);
		list.removeIf(e -> !isPointInCone(dirNorm, radius, length, truncationLength, e.position().subtract(start)));
		return list;
	}
	
	public static boolean isPointInCone(Vec3 normDirection, double radius, double length, float truncationLength, Vec3 relativePoint)
	{
		double projectedDist = relativePoint.dot(normDirection);
		if(projectedDist < truncationLength||projectedDist > length)
			return false;
		double radiusAtDist = projectedDist/length*radius;
		Vec3 orthVec = relativePoint.subtract(normDirection.scale(projectedDist));
		return orthVec.lengthSqr() < (radiusAtDist*radiusAtDist);
	}
	public static double minInArray(double... f)
	{
		if(f.length < 1)
			return 0;
		double min = f[0];
		for(int i = 1; i < f.length; i++)
			min = Math.min(min, f[i]);
		return min;
	}
	public static double maxInArray(double... f)
	{
		if(f.length < 1)
			return 0;
		double max = f[0];
		for(int i = 1; i < f.length; i++)
			max = Math.max(max, f[i]);
		return max;
	}

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {
        ItemStack stack = player.getItemInHand(usedHand);
        if (tryPerformAction(level, player, stack))
        {
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        if (context.getPlayer() != null && tryPerformAction(context.getLevel(), context.getPlayer(), context.getItemInHand()))
        {
            return InteractionResult.sidedSuccess(context.getLevel().isClientSide());
        }
        return super.useOn(context);
    }

    private boolean tryPerformAction(Level level, Player player, ItemStack stack)
    {
        boolean isCreative = player.isCreative();
        if (hasResources(stack) || isCreative)
        {
        	player.getCooldowns().addCooldown(this, ItemsConfig.WANDPORTDELAY.get());
            if (!level.isClientSide() && !isCreative)
            {
                consumeResources(stack);
            }
            return true;
        }
        return false;
    }

    public static int getMaxEnergy()
    {
        return ItemsConfig.WANDENERGY.get();
    }

    public boolean hasResources(ItemStack stack)
    {
        return WitherItemEnergy.hasEnergy(stack, ItemsConfig.WANDENERGYUSE.get());
    }

    public void consumeResources(ItemStack stack)
    {
    	WitherItemEnergy.extractEnergy(stack, ItemsConfig.WANDENERGYUSE.get(), false);
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
		return ItemsConfig.WANDENERGYUSE.get();
	}
	@Override
	public int getMaxEnergy(ItemStack stack)
	{
		return getPowerLevel(stack) * ItemsConfig.WANDENERGY.get();
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
        int enchantlevel = 1;
//        if(stack.isEmpty() == false && EnchantmentHelper.getEnchantments(stack) != null && EnchantmentHelper.getEnchantments(stack).containsKey(WUTEnchants.ENERGY.get()))
//        {
//            int newlevel = EnchantmentHelper.getEnchantments(stack).get(WUTEnchants.ENERGY.get());
//            if(newlevel > enchantlevel)
//            {
//                enchantlevel = newlevel;
//            }
//        }
        return enchantlevel;
    }

	@Override
	public ICapabilityProvider<ItemStack, Void, IEnergyStorage> initEnergyCap()
	{
		return (stack, v) -> new ComponentEnergyStorage(stack, WUTComponents.ENERGY.get(), WandSteelItem.getMaxEnergy());
	}
}
