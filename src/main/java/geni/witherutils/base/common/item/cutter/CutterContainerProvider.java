package geni.witherutils.base.common.item.cutter;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class CutterContainerProvider implements MenuProvider {

	@Override
	public Component getDisplayName()
	{
		return Component.translatable("cutter");
	}
	@Override
	public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player player)
	{
		return new CutterContainer(i, playerInventory, player);
	}
}
