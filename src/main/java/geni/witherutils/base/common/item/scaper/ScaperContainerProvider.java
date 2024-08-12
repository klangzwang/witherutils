package geni.witherutils.base.common.item.scaper;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class ScaperContainerProvider implements MenuProvider {

	@Override
	public Component getDisplayName()
	{
		return Component.translatable("scaper");
	}
	@Override
	public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player player)
	{
		return new ScaperContainer(i, playerInventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(player.blockPosition()));
	}
}
