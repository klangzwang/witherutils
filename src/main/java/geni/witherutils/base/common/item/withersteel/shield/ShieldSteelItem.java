package geni.witherutils.base.common.item.withersteel.shield;

import java.util.HashMap;
import java.util.HashSet;

import javax.annotation.Nonnull;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import geni.witherutils.base.common.base.WitherItem;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.base.common.item.withersteel.IWitherSteelItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.extensions.IForgeItem;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class ShieldSteelItem extends WitherItem implements IWitherSteelItem, IForgeItem {
	
	private final Enum<ShieldType> type;
	
	public ShieldSteelItem(Enum<ShieldType> type)
	{
		super(new Item.Properties().stacksTo(1).fireResistant().defaultDurability(1024));
		this.type = type;
        if(FMLEnvironment.dist.isClient())
        {
            registerShieldProperty();
        }
	}
	
    @OnlyIn(Dist.CLIENT)
    public void registerShieldProperty()
    {
    	ItemProperties.register(this, new ResourceLocation("blocking"), (stack, world, entity, i) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
    }
    
	@Override
	public int getUseDuration(ItemStack stack)
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
        return repair.getItem() == WUTItems.WITHERSTEEL_INGOT.get();
    }

	@Override
    @Nonnull
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack stack)
    {
        Multimap<Attribute, AttributeModifier> multimap = Multimaps.newSetMultimap(new HashMap<>(), HashSet::new);
        if (equipmentSlot == EquipmentSlot.MAINHAND || equipmentSlot == EquipmentSlot.OFFHAND)
        {
        	if(type == ShieldType.BASIC)
        	{
            	multimap.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier("Armor Toughness", 2.0D, Operation.ADDITION));
            	multimap.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier("Knockback Resistance", 0.2D, Operation.ADDITION));
        	}
        	if(type == ShieldType.ADVANCED)
        	{
            	multimap.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier("Armor Toughness", 6.0D, Operation.ADDITION));
            	multimap.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier("Knockback Resistance", 0.4D, Operation.ADDITION));
        	}
        }
        return multimap;
    }
    
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
		return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
	}
	@Override
	public boolean canPerformAction(ItemStack stack, ToolAction toolAction)
	{
		return ToolActions.DEFAULT_SHIELD_ACTIONS.contains(toolAction);
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
}
