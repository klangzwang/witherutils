package geni.witherutils.base.common.enchant;

import java.util.Set;

import geni.witherutils.api.enchant.IWitherEnchantable;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class NightVisionEnchantment extends EnchantmentWither
{
    public static final String ID = "night_vision";
	
    public NightVisionEnchantment(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot... slots)
    {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }

    @Override
    public int getMaxLevel()
    {
        return 1;
    }
    
    @Override
    public boolean canEnchant(ItemStack stack)
    {
    	return stack.getItem() instanceof IWitherEnchantable;
    }
    
    @Override
    public boolean allowedInCreativeTab(Item book, Set<EnchantmentCategory> allowedCategories)
    {
    	return true;
    }
}
