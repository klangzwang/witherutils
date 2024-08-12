package geni.witherutils.core.common.menu;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import geni.witherutils.base.common.menu.ArmorSlot;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class WitherMenu<T extends BlockEntity> extends AbstractContainerMenu {
	
    @Nullable
    private final T blockEntity;
    private final Inventory playerInventory;
    private final List<Slot> playerInventorySlots = new ArrayList<>();
    private boolean playerInvVisible = true;

    protected static final int PLAYER_INVENTORY_SIZE = 36;
    private static final EquipmentSlot[] EQUIPMENT_SLOTS = new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };

    protected WitherMenu(@Nullable MenuType<?> menuType, int containerId, Inventory playerInventory, @Nullable T blockEntity)
    {
        super(menuType, containerId);
        this.playerInventory = playerInventory;
        this.blockEntity = blockEntity;
    }

    protected Inventory getPlayerInventory()
    {
        return playerInventory;
    }

    protected void addPlayerInventorySlots(int x, int y)
    {
        addPlayerMainInventorySlots(x, y);
        addPlayerHotbarSlots(x, y + 58);
    }

    protected void addPlayerMainInventorySlots(int xStart, int yStart)
    {
        for (int y = 0; y < 3; y++)
        {
            for (int x = 0; x < 9; x++)
            {
                addSlot(new Slot(getPlayerInventory(), x + y * 9 + 9, xStart + x * 18, yStart + y * 18));
            }
        }
    }

    protected void addPlayerHotbarSlots(int x, int y)
    {
        for (int i = 0; i < 9; i++)
        {
            addSlot(new Slot(getPlayerInventory(), i, x + i * 18, y));
        }
    }

    protected void addArmorSlots(int x, int y)
    {
        for (int i = 0; i < EQUIPMENT_SLOTS.length; i++)
        {
            addSlot(new ArmorSlot(getPlayerInventory(), 36 + (3 - i), x, y + i * 18, EQUIPMENT_SLOTS[i]));
        }
    }

    @Nullable
    public T getBlockEntity()
    {
        return blockEntity;
    }
    
    public void addInventorySlots(int xPos, int yPos, boolean onlyHotbar)
    {
        // Hotbar
        for (int x = 0; x < 9; x++)
        {
            Slot ref = new Slot(playerInventory, x, xPos + x * 18, yPos + (onlyHotbar ? 0 : 58));
            playerInventorySlots.add(ref);
            this.addSlot(ref);
        }

        if(onlyHotbar)
            return;
        
        // Inventory
        for (int y = 0; y < 3; y++)
        {
            for (int x = 0; x < 9; x++)
            {
                Slot ref = new Slot(playerInventory, x + y * 9 + 9, xPos + x * 18, yPos + y * 18);
                playerInventorySlots.add(ref);
                this.addSlot(ref);
            }
        }
    }
    public boolean getPlayerInvVisible()
    {
        return playerInvVisible;
    }
    public boolean setPlayerInvVisible(boolean visible)
    {
        if (playerInvVisible != visible)
        {
            playerInvVisible = visible;
            int offset = playerInvVisible ? 1000 : -1000;
            for (int i = 0; i < 9; i++)
            {
                playerInventorySlots.get(i).y += offset;
            }
        }
        return visible;
    }
}
