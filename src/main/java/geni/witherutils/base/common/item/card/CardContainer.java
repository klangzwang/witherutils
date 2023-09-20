package geni.witherutils.base.common.item.card;

import javax.annotation.Nonnull;

import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.base.common.init.WUTMenus;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class CardContainer extends AbstractContainerMenu {

	public ItemStack stack;
	public int slot;
	public int slotcount;
	public CompoundTag nbt;
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

		this.nbt = stack.getOrCreateTag();

		stack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(h -> {
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
		});
		layoutPlayerInventorySlots(8, 87);
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
	
	private int addSlotRange(Inventory handler, int index, int x, int y, int amount, int dx)
	{
		for (int i = 0; i < amount; i++)
		{
			addSlot(new Slot(handler, index, x, y));
			x += dx;
			index++;
		}
		return index;
	}
	
	protected void layoutPlayerInventorySlots(int leftCol, int topRow)
	{
		topRow += 0;
		addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
	}
	
	@Override
	public ItemStack quickMoveStack(Player p_38941_, int p_38942_)
	{
		return ItemStack.EMPTY;
	}
}
