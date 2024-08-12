package geni.witherutils.base.common.base;

import javax.annotation.Nonnull;

import geni.witherutils.api.upgrade.WUTUpgrade;
import geni.witherutils.base.common.init.WUTComponents;
import geni.witherutils.base.common.io.item.UpgradeInventory;
import geni.witherutils.base.common.upgrade.ApplicableUpgradesDB;
import geni.witherutils.base.common.upgrade.IUpgradeHolder;
import geni.witherutils.base.common.upgrade.SavedUpgrades;
import geni.witherutils.base.common.upgrade.UpgradeCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class WitherMachineUpgradeBlockEntity extends WitherMachineBlockEntity implements IUpgradeHolder {

	private final UpgradeCache upgradeCache = new UpgradeCache(this);
    private final UpgradeHandler upgradeHandler;
    
    public WitherMachineUpgradeBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState, int upgradeSize)
    {
		super(type, worldPosition, blockState);
		
	    this.upgradeHandler = new UpgradeHandler(upgradeSize);
	}

    public class UpgradeHandler extends UpgradeInventory {
    	
        UpgradeHandler(int upgradeSize)
        {
            super(WitherMachineUpgradeBlockEntity.this, upgradeSize);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack itemStack)
        {
            return itemStack.isEmpty() || isApplicable(itemStack) && isUnique(slot, itemStack);
        }

        @Override
        protected int getStackLimit(int slot, @Nonnull ItemStack stack)
        {
            WUTUpgrade upgrade = WUTUpgrade.from(stack);
            if (upgrade == null) return 0;
            return ApplicableUpgradesDB.getInstance().getMaxUpgrades(te, upgrade);
        }

        private boolean isUnique(int slot, ItemStack stack)
        {
            for (int i = 0; i < getSlots(); i++) {
                if (i != slot && WUTUpgrade.from(stack) == WUTUpgrade.from(getStackInSlot(i))) return false;
            }
            return true;
        }

        private boolean isApplicable(ItemStack stack)
        {
            WUTUpgrade upgrade = WUTUpgrade.from(stack);
            return ApplicableUpgradesDB.getInstance().getMaxUpgrades(WitherMachineUpgradeBlockEntity.this, upgrade) > 0;
        }

        @Override
        protected void onContentsChanged(int slot)
        {
            super.onContentsChanged(slot);

            upgradeCache.invalidateCache();
        }
    }
    
    public void getContentsToDrop(NonNullList<ItemStack> drops)
    {
        UpgradeHandler uh = getUpgradeHandler();
        for (int i = 0; i < uh.getSlots(); i++)
        {
            if (!uh.getStackInSlot(i).isEmpty())
            {
                drops.add(uh.getStackInSlot(i));
            }
        }
    }
    
    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider)
    {
        super.saveAdditional(tag, provider);
        if (getUpgradeHandler().getSlots() > 0)
        {
            tag.put("UpgradeInventory", getUpgradeHandler().serializeNBT(provider));
        }
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider)
    {
        super.loadAdditional(tag, provider);
        if (tag.contains("UpgradeInventory") && getUpgradeHandler() != null)
        {
            getUpgradeHandler().deserializeNBT(provider, tag.getCompound("UpgradeInventory"));
        }
    }
    
    @Override
    protected void applyImplicitComponents(DataComponentInput componentInput)
    {
        super.applyImplicitComponents(componentInput);
        componentInput.getOrDefault(WUTComponents.ITEM_UPGRADES, SavedUpgrades.EMPTY).fillItemHandler(getUpgradeHandler());
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder)
    {
        super.collectImplicitComponents(builder);
        builder.set(WUTComponents.ITEM_UPGRADES, SavedUpgrades.fromItemHandler(getUpgradeHandler()));
    }

    public int getUpgrades(WUTUpgrade upgrade)
    {
        return upgradeCache.getUpgrades(upgrade);
    }
    
	@Override
	public UpgradeHandler getUpgradeHandler()
	{
        return upgradeHandler;
	}
	
    public UpgradeCache getUpgradeCache()
    {
        return upgradeCache;
    }
}
