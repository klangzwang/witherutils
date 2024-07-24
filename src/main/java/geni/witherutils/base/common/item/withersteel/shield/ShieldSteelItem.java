package geni.witherutils.base.common.item.withersteel.shield;

import javax.annotation.Nonnull;

import geni.witherutils.base.common.base.WitherItem;
import geni.witherutils.base.common.init.WUTSounds;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class ShieldSteelItem extends WitherItem {
	
	private final Enum<ShieldType> type;
	
	public ShieldSteelItem(Enum<ShieldType> type)
	{
		super(new Item.Properties().stacksTo(1).fireResistant().durability(1024));
		this.type = type;
        if(FMLEnvironment.dist.isClient())
        {
            registerShieldProperty();
        }
	}
	
    @OnlyIn(Dist.CLIENT)
    public void registerShieldProperty()
    {
    	ItemProperties.register(this, ResourceLocation.withDefaultNamespace("blocking"), (stack, world, entity, i) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
    }
    
    @Override
    public int getUseDuration(ItemStack pStack, LivingEntity pEntity)
    {
		return 72000;
    }

    @Override
    public boolean isBarVisible(ItemStack pStack)
    {
        return true;
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
    
//	@Override
//    @Nonnull
//    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack stack)
//    {
//        Multimap<Attribute, AttributeModifier> multimap = Multimaps.newSetMultimap(new HashMap<>(), HashSet::new);
//        if (equipmentSlot == EquipmentSlot.MAINHAND || equipmentSlot == EquipmentSlot.OFFHAND)
//        {
//        	if(type == ShieldType.BASIC)
//        	{
//            	multimap.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier("Armor Toughness", 2.0D, Operation.ADDITION));
//            	multimap.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier("Knockback Resistance", 0.2D, Operation.ADDITION));
//        	}
//        	if(type == ShieldType.ADVANCED)
//        	{
//            	multimap.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier("Armor Toughness", 6.0D, Operation.ADDITION));
//            	multimap.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier("Knockback Resistance", 0.4D, Operation.ADDITION));
//        	}
//        }
//        return multimap;
//    }
    
	@Override
	public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity)
	{
		if(player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ShieldSteelItem)
		{
			player.level().playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.METAL_BREAK, SoundSource.NEUTRAL, 0.4F, 0.4F / (player.level().getRandom().nextFloat() * 0.4F + 0.8F));
			player.level().playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ANVIL_LAND, SoundSource.NEUTRAL, 0.1F, 0.3F / (player.level().getRandom().nextFloat() * 0.4F + 0.4F));
		}
		return super.onLeftClickEntity(stack, player, entity);
	}
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		ItemStack itemstack = player.getItemInHand(hand);
		player.startUsingItem(hand);
		world.playSound((Player)null, player.getX(), player.getY(), player.getZ(), WUTSounds.PLACEBOOMTWO.get(), SoundSource.NEUTRAL, 0.75F, 0.5F / (world.getRandom().nextFloat() * 0.4F + 0.8F));

//        if (!world.isClientSide)
//        {
//        	BoomerangEntity entity = new BoomerangEntity(world, player);
//            entity.setItem(itemstack);
//            entity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
//            world.addFreshEntity(entity);
//        }

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
	}
	
	@Override
	public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility)
	{
		return ItemAbility.getActions().contains(ItemAbility.get("shield_block"));
	}
	
	@Nonnull
	@Override
	public UseAnim getUseAnimation(ItemStack stack)
	{
		return UseAnim.BLOCK;
	}

    public Enum<ShieldType> getType()
    {
		return type;
	}

	public enum ShieldType implements StringRepresentable
    {
    	BASIC(0, "shieldbasic"),
    	ADVANCED(1, "shieldadvanced"),
    	ROTTEN(2, "shieldrotten");

    	private int type;
    	private final String name;
    	public static final ShieldType[] BY_ID = values();

    	ShieldType(int type, String name)
        {
        	this.name = name;
        	this.type = type;
        }

        public ShieldType getType()
        {
            return BY_ID[this.type];
        }
    	@Override
    	public String getSerializedName()
    	{
    		return this.name;
    	}
    	public boolean connectTo(@Nonnull ShieldType other)
    	{
    		return this == other;
    	}
    	public static @Nonnull ShieldType getTheType()
    	{
    		return values()[values().length];
    	}
    	public static @Nonnull ShieldType getType(int type)
    	{
    		return (values()[type >= 0 && type < values().length ? type : 0]);
    	}
    	public static @Nonnull ShieldType getType(@Nonnull ItemStack stack)
    	{
    		return getType(stack.getDamageValue());
    	}
    	public static int getValue(@Nonnull ShieldType value)
    	{
    		return value.ordinal();
    	}
    	public static Enum<ShieldType> getValue(@Nonnull Enum<ShieldType> value)
    	{
    		return value;
    	}
    	public static int getValueFromType(@Nonnull ShieldType solarType)
    	{
    		return solarType.ordinal();
    	}
    }
	
	public void hitShield(ItemStack stack, Player player, DamageSource source, float amount, LivingDamageEvent event)
	{
	}
	
//	@Override
//	public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int chargeTimer)
//	{
//		int charge = this.getUseDuration(stack, entity) - chargeTimer;
//		float percentageCharged = BowItem.getPowerForTime(charge);
//		if (percentageCharged < 0.1)
//		{
//			return;
//		}
//
//		if (entity instanceof Player == false)
//		{
//			return;
//		}
//		
//		Player player = (Player) entity;
//		BoomerangEntity e = new BoomerangEntity(world, player);
//		
//		shootMe(world, player, e, 0, percentageCharged * 1.5F);
//		ItemStackUtil.damageItem(player, stack);
//		player.setItemInHand(player.getUsedItemHand(), ItemStack.EMPTY);
//		e.setBoomerangThrown(stack.copy());
//		e.setOwner(player);
//	}
}
