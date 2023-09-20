package geni.witherutils.api.steelupable;

import geni.witherutils.api.nbt.INamedNBTSerializable;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

public interface ISteelUpable extends INamedNBTSerializable<Tag> {

    @Override
    default String getSerializedName()
    {
        return "SteelUpgradeData";
    }

    void addUpgrade(ItemStack upgrade);

    int getLevel();

    void setLevel(int level);

    int getPowerLevel(ItemStack stack);

    int getSolarLevel(ItemStack stack);

    boolean isChargeable(ItemStack stack);
}
