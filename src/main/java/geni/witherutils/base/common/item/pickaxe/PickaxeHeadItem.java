package geni.witherutils.base.common.item.pickaxe;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.common.base.WitherItem;
import geni.witherutils.base.common.init.WUTComponents;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;

public class PickaxeHeadItem extends WitherItem {

    public static final ICapabilityProvider<ItemStack, Void, Float> STORED_FLOAT_PROVIDER
    					= (stack, ctx) -> stack.get(WUTComponents.PICKAXE);
    
	public PickaxeHeadItem()
	{
		super(new Properties().stacksTo(1).rarity(Rarity.RARE));
        if(FMLEnvironment.dist.isClient())
        {
        	registerPickaxeProperty();
        }
	}
	
	@OnlyIn(Dist.CLIENT)
    public void registerPickaxeProperty()
    {
        ItemProperties.register(this, WitherUtilsRegistry.loc("chance"), (stack, world, entity, i) -> {
            float f = 0.0F;
            float chance = stack.getOrDefault(WUTComponents.PICKAXE, 0.5f);
            if(chance < 0.5f)
            {
            	f = 0.5f;
            }
            if(chance > 0.5f && chance < 0.8f)
            {
            	f = 0.8f;
            }
            if(chance > 0.8f)
            {
            	f = 1.0f;
            }
            return f;
        });
    }
	
	public void setPickaxeFloat(float random)
	{
		ItemStack pickstack = this.getDefaultInstance();
		pickstack.set(WUTComponents.PICKAXE, random);
	}
	
//	@Override
//	public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected)
//	{
//		super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
//		
//		if(pStack.get(WUTComponents.PICKAXE) != null)
//		{
//			float chance = pStack.get(WUTComponents.PICKAXE);
//			System.out.println(chance);
//		}
//	}
}
