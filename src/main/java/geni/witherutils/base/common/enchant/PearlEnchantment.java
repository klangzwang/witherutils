package geni.witherutils.base.common.enchant;

import geni.witherutils.api.enchant.IWitherEnchantable;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class PearlEnchantment extends EnchantmentWither  {

    public static final String ID = "pearl";
    
    public PearlEnchantment(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot... slots)
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
}
