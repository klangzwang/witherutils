package geni.witherutils.core.common.lib.entityfilter;

import org.jetbrains.annotations.UnknownNullability;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class FilterAdults extends FilterBase {

    protected boolean whitelistAdults = true;
    protected boolean includeNonAgeable = true;

    public FilterAdults(EntityFilter filter) {
        super(filter);
    }

    public void setWhitelistAdults(boolean whitelistAdults) {
        boolean prev = this.whitelistAdults;
        this.whitelistAdults = whitelistAdults;
        getFilter().nodeModified(this);
        this.whitelistAdults = prev;
    }

    public void setIncludeNonAgeable(boolean includeNonAgeable) {
        boolean prev = this.includeNonAgeable;
        this.includeNonAgeable = includeNonAgeable;
        getFilter().nodeModified(this);
        this.includeNonAgeable = prev;
    }

    public boolean isWhitelistAdults() {
        return whitelistAdults;
    }

    public boolean isIncludeNonAgeable() {
        return includeNonAgeable;
    }

    @Override
    public boolean test(Entity entity) {
        if (entity instanceof Player) return false;
        boolean isAgeable = entity instanceof AgeableMob;
        if (isAgeable) {
            AgeableMob ageable = (AgeableMob) entity;
            return whitelistAdults == !ageable.isBaby();
        }
        return includeNonAgeable;
    }

    @Override
    public FilterType getType() {
        return FilterType.ADULTS;
    }

	@Override
	public @UnknownNullability CompoundTag serializeNBT(Provider provider)
	{
        CompoundTag compound = new CompoundTag();
        compound.putBoolean("include", whitelistAdults);
        compound.putBoolean("tamable", includeNonAgeable);
        return compound;
	}

	@Override
	public void deserializeNBT(Provider provider, CompoundTag nbt)
	{
        whitelistAdults = nbt.getBoolean("include");
        includeNonAgeable = nbt.getBoolean("tamable");
	}
}
