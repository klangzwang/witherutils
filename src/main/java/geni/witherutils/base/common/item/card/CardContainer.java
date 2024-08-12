package geni.witherutils.base.common.item.card;

import javax.annotation.Nonnull;

import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.base.common.init.WUTMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class CardContainer extends AbstractContainerMenu {

	public ItemStack stack;
	public int slot;
	public int slotcount;
	protected Player playerEntity;
	protected Inventory playerInventory;
	
	public CardContainer(int id, Inventory playerInventory, Player player)
	{
		super(WUTMenus.BLOCKCARD.get(), id);
		this.playerEntity = player;
		this.playerInventory = playerInventory;

		if(player.getMainHandItem().getItem() instanceof CardItem)
		{
			this.stack = player.getMainHandItem();
			this.slot = player.getInventory().selected;
		}
		else if(player.getOffhandItem().getItem() instanceof CardItem)
		{
			this.stack = player.getOffhandItem();
			this.slot = 40;
		}

		IItemHandler h = stack.getCapability(Capabilities.ItemHandler.ITEM);
		if(h != null)
		{
			this.slotcount = h.getSlots();
			for (int j = 0; j < h.getSlots(); j++)
			{
				int row = j / 9;
				int col = j % 9;
				int xPos = 8 + col * 18;
				int yPos = 47 + row * 18;
				this.addSlot(new SlotItemHandler(h, j, xPos, yPos)
				{
					@Override
					public boolean mayPlace(@Nonnull ItemStack stack)
					{
						if(stack.getItem() == WUTItems.CARD.get())
						{
							return false;
						}
						return super.mayPlace(stack);
					}
				});
			}
		}
		
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; k++)
        {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
	}

	@Override
	public boolean stillValid(Player playerIn)
	{
		return true;
	}

	@Override
	public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player)
	{
		if(!(slotId < 0 || slotId >= this.slots.size()))
		{
			ItemStack myBag = this.slots.get(slotId).getItem();
			if(myBag.getItem() instanceof CardItem)
			{
				return;
			}
		}
		super.clicked(slotId, dragType, clickTypeIn, player);
	}
	
	@Override
	public ItemStack quickMoveStack(Player p_38941_, int p_38942_)
	{
		return ItemStack.EMPTY;
	}
}
