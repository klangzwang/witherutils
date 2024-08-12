package geni.witherutils.core.common.lib.entityfilter;

import org.jetbrains.annotations.UnknownNullability;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;

/**
 * Created by brandon3055 on 7/11/19.
 */
public class FilterHostile extends FilterBase {

    protected boolean whitelistHostile = true;

    public FilterHostile(EntityFilter filter) {
        super(filter);
    }

    public void setWhitelistHostile(boolean whitelistHostile) {
        boolean prev = this.whitelistHostile;
        this.whitelistHostile = whitelistHostile;
        getFilter().nodeModified(this);
        this.whitelistHostile = prev;
    }

    public boolean isWhitelistHostile() {
        return whitelistHostile;
    }

    @Override
    public boolean test(Entity entity) {
        if (entity instanceof Player) return false;
        boolean isHostile = entity instanceof Enemy;
        return isHostile == whitelistHostile;
    }

    @Override
    public FilterType getType() {
        return FilterType.HOSTILE;
    }

//    @Override
//    public CompoundTag serializeNBT() {
//        CompoundTag compound = super.serializeNBT();
//        compound.putBoolean("include", whitelistHostile);
//        return compound;
//    }
//
//    @Override
//    public void deserializeNBT(CompoundTag nbt) {
//        super.deserializeNBT(nbt);
//        whitelistHostile = nbt.getBoolean("include");
//    }
//
//    @Override
//    public void serializeMCD(MCDataOutput output) {
//        super.serializeMCD(output);
//        output.writeBoolean(whitelistHostile);
//    }
//
//    @Override
//    public void deSerializeMCD(MCDataInput input) {
//        super.deSerializeMCD(input);
//        whitelistHostile = input.readBoolean();
//    }

	@Override
	public @UnknownNullability CompoundTag serializeNBT(Provider provider) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deserializeNBT(Provider provider, CompoundTag nbt) {
		// TODO Auto-generated method stub
		
	}
}
