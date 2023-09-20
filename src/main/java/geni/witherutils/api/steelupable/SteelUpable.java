package geni.witherutils.api.steelupable;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

public class SteelUpable implements ISteelUpable {

	@SuppressWarnings("unused")
	private final Map<String, ISteelUpable> upgrades = new HashMap<>();
    private int level = 0;
    
    public SteelUpable()
    {
    }
    public SteelUpable(int level)
    {
        this.level = level;
    }

    @Override
    public CompoundTag serializeNBT()
    {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("level", this.level);
        return nbt;
    }
    @Override
    public void deserializeNBT(Tag nbt)
    {
        if(nbt instanceof CompoundTag tag)
        {
            this.level = tag.getInt("level");
        }
    }
	@Override
	public int getLevel()
	{
		return level;
	}
	@Override
	public void setLevel(int level)
	{
		this.level = level;
	}
	@Override
	public void addUpgrade(ItemStack upgrade)
	{
	}
	@Override
	public int getPowerLevel(ItemStack stack)
	{
		return level;
	}
	@Override
	public int getSolarLevel(ItemStack stack)
	{
		return 0;
	}
	@Override
	public boolean isChargeable(ItemStack stack)
	{
		return false;
	}
}
