package geni.witherutils.base.common.item.withersteel;

import geni.witherutils.base.common.init.WUTComponents;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.base.common.item.soulbank.SoulBankItem;
import geni.witherutils.base.common.item.withersteel.sword.SwordSteelItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AnvilUpdateEvent;

public class WitherSteelRecipeManager {

	public static WitherSteelRecipeManager instance = new WitherSteelRecipeManager();

	public WitherSteelRecipeManager()
	{
    }

	/*
	 * 
	 * Energy Enchantment
	 * 
	 */
	@SubscribeEvent
	public void handleEnergyUpgrade(AnvilUpdateEvent event)
	{
		ItemStack leftItem = event.getLeft();
		ItemStack rightItem = event.getRight();
		
		/*
		 * 
		 * SWORDITEM
		 * 
		 */
		if(leftItem.getItem() instanceof SwordSteelItem && rightItem.getItem() instanceof SoulBankItem)
		{
			SwordSteelItem sword = (SwordSteelItem) leftItem.getItem();
			ItemStack stack = leftItem.copy();
			
			int swordLevel = sword.getPowerLevel(stack);
			
			if(swordLevel == 0 && rightItem.getItem() == WUTItems.SOULBANK_BASIC.get())
			{
				stack.set(WUTComponents.LEVEL, 1);
				event.setCost(3);
				event.setOutput(stack);
				event.setMaterialCost(1);
			}
			else if(swordLevel == 1 && rightItem.getItem() == WUTItems.SOULBANK_ADVANCED.get())
			{
				stack.set(WUTComponents.LEVEL, 2);
				event.setCost(6);
				event.setOutput(stack);
				event.setMaterialCost(1);
			}
			else if(swordLevel == 2 && rightItem.getItem() == WUTItems.SOULBANK_ULTRA.get())
			{
				stack.set(WUTComponents.LEVEL, 3);
				event.setCost(12);
				event.setOutput(stack);
				event.setMaterialCost(1);
			}
//			if(!stack.isEmpty())
//			{
//				stack.setHoverName(Component.literal(ChatFormatting.YELLOW + stack.getHoverName().getString() + " §r§f" + "■-§9Powered§r§f-■"));
//			}
		}
	}
}
