package geni.witherutils.base.common.base;

import geni.witherutils.base.common.io.item.MachineInventory;
import geni.witherutils.base.common.io.item.SingleSlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class MachineSlotStack extends SlotItemHandler {
	
	public final ItemStack stack;
	public boolean grayOut = false;
	public float grayOutLevel = 0.5f;
	  
    public MachineSlotStack(MachineInventory itemHandler, int index, int xPosition, int yPosition, ItemStack stack)
    {
        super(itemHandler, index, xPosition, yPosition);
        this.stack = stack;
    }

    public MachineSlotStack(MachineInventory itemHandler, SingleSlotAccess access, int xPosition, int yPosition, ItemStack stack)
    {
        super(itemHandler, access.getIndex(), xPosition, yPosition);
        this.stack = stack;
    }
    
    @Override
    public MachineInventory getItemHandler()
    {
        return (MachineInventory) super.getItemHandler();
    }

    @Override
    public boolean mayPlace(ItemStack stack)
    {
        return getItemHandler().getLayout().guiCanInsert(this.getSlotIndex()) && super.mayPlace(stack);
    }

    @Override
    public boolean mayPickup(Player playerIn)
    {
        return getItemHandler().getLayout().guiCanExtract(this.getSlotIndex()) && super.mayPickup(playerIn);
    }
    
    public ItemStack getStack()
    {
		return stack;
	}
    
	public MachineSlotStack setIsGrayOut()
	{
		this.grayOut = true;
		return this;
	}

	public boolean shouldGrayOut()
	{
		return grayOut;
	}

	public float getGrayOutLevel()
	{
		return grayOutLevel;
	}
}
