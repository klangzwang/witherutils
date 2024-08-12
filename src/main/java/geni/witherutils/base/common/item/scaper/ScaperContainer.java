package geni.witherutils.base.common.item.scaper;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import geni.witherutils.api.soul.PlayerSoul;
import geni.witherutils.base.common.init.WUTCapabilities;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.base.common.init.WUTMenus;
import geni.witherutils.base.common.item.cutter.CutterRecipe;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

@SuppressWarnings("unused")
public class ScaperContainer extends AbstractContainerMenu {

	public ItemStack stack;
	public int slot;
	public int slotcount;
	protected Player player;
	protected Inventory playerInventory;

    public ScaperContainer(int pContainerId, Inventory playerInventory, FriendlyByteBuf extraData)
    {
        super(WUTMenus.SCAPER.get(), pContainerId);
		this.player = playerInventory.player;
		this.playerInventory = playerInventory;

		if(player.getMainHandItem().getItem() instanceof ScaperItem)
		{
			this.stack = player.getMainHandItem();
			this.slot = player.getInventory().selected;
		}
		else if(player.getOffhandItem().getItem() instanceof ScaperItem)
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
						if(stack.getItem() == WUTItems.SCAPER.get())
						{
							return false;
						}
						return super.mayPlace(stack);
					}
				});
			}
		}
		layoutPlayerInventorySlots(8, 155);
	}
    
	protected void layoutPlayerInventorySlots(int leftCol, int topRow)
	{
		topRow += 0;
		addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
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

	@Override
	public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player)
	{
		if(!(slotId < 0 || slotId >= this.slots.size()))
		{
			ItemStack myBag = this.slots.get(slotId).getItem();
			if(myBag.getItem() instanceof ScaperItem)
			{
				return;
			}
		}
		super.clicked(slotId, dragType, clickTypeIn, player);
	}

	@Override
	public ItemStack quickMoveStack(Player pPlayer, int pIndex)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(Player pPlayer)
	{
		return true;
	}
}
