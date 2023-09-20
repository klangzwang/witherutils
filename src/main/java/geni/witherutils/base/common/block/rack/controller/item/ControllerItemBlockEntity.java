package geni.witherutils.base.common.block.rack.controller.item;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import geni.witherutils.base.common.base.WitherMachineBlockEntity;
import geni.witherutils.base.common.block.rack.casing.CaseBlock;
import geni.witherutils.base.common.block.rack.casing.CaseBlockEntity;
import geni.witherutils.base.common.config.common.BlocksConfig;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.core.common.sync.IntegerDataSlot;
import geni.witherutils.core.common.sync.ItemStackDataSlot;
import geni.witherutils.core.common.sync.SyncMode;
import geni.witherutils.core.common.util.FacingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class ControllerItemBlockEntity extends WitherMachineBlockEntity {

	static HashMap<UUID, Long> INTERACTION_LOGGER = new HashMap<>();
    
    private ItemStack blStack;
    private int itemsStored;
    private boolean hasItemNBT;

    private ItemControllerHandler itemHandler;
    private final LazyOptional<IItemHandler> lazyIStorage;
    
    public static final int ITEMCAPACITY = BlocksConfig.ITEMCONTROLLERCAPACITY.get();
    
	public ControllerItemBlockEntity(BlockPos pos, BlockState state)
	{
		super(WUTEntities.RACKITEM_CONTROLLER.get(), pos, state);
		addDataSlot(new ItemStackDataSlot(() -> getBlStack(), p -> blStack = p, SyncMode.WORLD));
		addDataSlot(new IntegerDataSlot(this::getItemsStored, p -> itemsStored = p, SyncMode.WORLD));

        this.blStack = ItemStack.EMPTY;
		this.itemsStored = 0;
        this.hasItemNBT = false;

        this.itemHandler = new ItemControllerHandler(ITEMCAPACITY);
        this.lazyIStorage = LazyOptional.of(() -> itemHandler);
	}
    
	@Override
	public void serverTick()
	{
		super.serverTick();
		
        if (!this.hasItemNBT && this.blStack.hasTag())
        {
            ItemStack stack = this.blStack.copy();
            stack.setTag(null);
            this.setBlStack(stack);
        }
    	itemHandler.canHandle = isFormed();
	}
	@Override
	public void clientTick()
	{
		super.clientTick();
		
		setLitProperty(itemsStored > 0);
	}
	
	public boolean isFormed()
	{
		for(BlockPos aroundPos : FacingUtil.AROUND_ALL)
		{
			if(aroundPos != worldPosition)
			{
				BlockEntity te = level.getBlockEntity(worldPosition.offset(aroundPos));
				if (te instanceof CaseBlockEntity)
				{
					CaseBlockEntity caseBe = (CaseBlockEntity) te; 
					return CaseBlock.isFormed(caseBe.getBlockState());
				}
			}
		}
		return false;
	}
    
    @Nonnull
    @Override
    public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side)
    {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
        {
            return lazyIStorage.cast();
        }
        return super.getCapability(cap, side);
    }
    
    public ItemStack getDisplayStack()
    {
        return blStack;
    }
    
    public ItemStack getBlStack()
    {
    	return blStack;
    }
    public void setBlStack(ItemStack blStack)
    {
        this.blStack = blStack;
        this.hasItemNBT = this.blStack.hasTag();
    }
	public int getItemsStored()
	{
		return itemsStored;
	}
    public void setItemsStored(int stored)
    {
        this.itemsStored = stored;
    }

	public LazyOptional<IItemHandler> getILazyStorage()
	{
		return lazyIStorage;
	}
	
    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);
        this.hasItemNBT = tag.getBoolean("hasitemnbt");
        this.itemsStored = tag.getInt("itemsstored");
        this.blStack = ItemStack.of(tag.getCompound("blstack"));
    }

    @Override
    public void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);
        tag.putBoolean("hasitemnbt", hasItemNBT);
        tag.putInt("itemsstored", itemsStored);
     	tag.put("blStack", this.blStack.serializeNBT());
    }
    
    public String getFormatedDisplayAmount()
    {
    	return itemsStored == 0 ? "0" : (itemsStored >= 64 ? getFormatedBigNumber(itemsStored / 64) + " x64" : "") + (itemsStored >= 64 && itemsStored % 64 != 0 ? " + " : "") + (itemsStored % 64 != 0 ? itemsStored % 64 : "");
    }
    
    private static DecimalFormat formatterWithUnits = new DecimalFormat("####0.#");

    public static String getFormatedBigNumber(int number)
    {
        if (number >= 1000000000) {
            float numb = number / 1000000000F;
            return formatterWithUnits.format(numb) + "B";
        } else if (number >= 1000000) {
            float numb = number / 1000000F;
            if (number > 100000000) numb = Math.round(numb);
            return formatterWithUnits.format(numb) + "M";
        } else if (number >= 1000) {
            float numb = number / 1000F;
            if (number > 100000) numb = Math.round(numb);
            return formatterWithUnits.format(numb) + "K";
        }
        return String.valueOf(number);
    }
    
    /*
     * 
     * ITEMHANDLER
     * 
     */
    private class ItemControllerHandler implements IItemHandler {

        private int amount;
        private boolean canHandle;
        
        public ItemControllerHandler(int amount)
        {
            this.amount = amount;
        }

        @Override
        public int getSlots()
        {
            return 1;
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot)
        {
            ItemStack copied = blStack.copy();
            copied.setCount(itemsStored);
            return copied;
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
        {
            if (isItemValid(slot, stack) && canHandle)
            {
                int inserted = Math.min(this.amount - itemsStored, stack.getCount());
                if (!simulate)
                {
                    setBlStack(stack);
                    setItemsStored(Math.min(itemsStored + inserted, amount));
                }
                if (inserted == stack.getCount()) return ItemStack.EMPTY;
                return ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - inserted);
            }
            return stack;
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate)
        {
    		if(!canHandle) return ItemStack.EMPTY;
            if (amount == 0) return ItemStack.EMPTY;
            if (blStack.isEmpty()) return ItemStack.EMPTY;
            if (itemsStored <= amount)
            {
                ItemStack out = blStack.copy();
                int newAmount = itemsStored;
                if (!simulate)
                {
                    setBlStack(ItemStack.EMPTY);
                    setItemsStored(0);
                }
                out.setCount(newAmount);
                return out;
            }
            else
            {
                if (!simulate)
                {
                    setItemsStored(itemsStored - amount);
                }
                return ItemHandlerHelper.copyStackWithSize(blStack, amount);
            }
        }

        @Override
        public int getSlotLimit(int slot)
        {
            return amount;
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack)
        {
            if (slot == 0)
            {
                ItemStack fl = blStack;
                return fl.isEmpty() || (ItemStack.isSameItemSameTags(fl, stack));
            }
            return false;
        }
    }
    
    /*
     * 
     * CLICK INSERT EXTRACT
     * 
     */
    public void onLeftClicked(Player player)
    {
        if (!level.isClientSide())
            ItemHandlerHelper.giveItemToPlayer(player, itemHandler.extractItem(0, player.isShiftKeyDown() ? 64 : 1, false));
    }
    public void onRightClicked(Player player, InteractionHand hand)
    {
        if (!level.isClientSide())
        {
            ItemStack stack = player.getItemInHand(hand);
            
            if (!stack.isEmpty() && itemHandler.isItemValid(0, stack))
            {
                player.setItemInHand(hand, itemHandler.insertItem(0, stack, false));
            }
            else if (System.currentTimeMillis() - INTERACTION_LOGGER.getOrDefault(player.getUUID(), System.currentTimeMillis()) < 300)
            {
                for (ItemStack itemStack : player.inventory.items)
                {
                    if (!itemStack.isEmpty() && itemHandler.insertItem(0, itemStack, true).isEmpty())
                    {
                    	itemHandler.insertItem(0, itemStack.copy(), false);
                        itemStack.setCount(0);
                    }
                }
            }
            INTERACTION_LOGGER.put(player.getUUID(), System.currentTimeMillis());
        }
    }
}
