package geni.witherutils.core.common.lib.entityfilter;

import org.jetbrains.annotations.UnknownNullability;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

/**
 * Created by brandon3055 on 7/11/19.
 */
public class FilterEntity extends FilterBase {

    protected boolean whitelistEntity = true;
    protected String entityName = "";

    public FilterEntity(EntityFilter filter) {
        super(filter);
    }

    public void setWhitelistEntity(boolean whitelistEntity) {
        boolean prev = this.whitelistEntity;
        this.whitelistEntity = whitelistEntity;
        getFilter().nodeModified(this);
        this.whitelistEntity = prev;
    }

    public void setEntityName(String entityName) {
//        String prev = this.entityName;//This needs to be set real time to avoid breaking the text field
        this.entityName = entityName;
        getFilter().nodeModified(this);
//        this.entityName = prev;
    }

    public boolean isWhitelistEntity() {
        return whitelistEntity;
    }

    public String getEntityName() {
        return entityName;
    }

    @Override
    public boolean test(Entity entity) {
        ResourceLocation res = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        if (entityName.isEmpty() || res == null) {
            return !whitelistEntity;
        }
        String name = entityName.contains(":") ? entityName : "minecraft:" + entityName;
        return res.toString().equals(name) == whitelistEntity;
    }

    @Override
    public FilterType getType() {
        return FilterType.ENTITY_TYPE;
    }

//    @Override
//    public CompoundTag serializeNBT() {
//        CompoundTag compound = super.serializeNBT();
//        compound.putBoolean("include", whitelistEntity);
//        compound.putString("name", entityName);
//        return compound;
//    }
//
//    @Override
//    public void deserializeNBT(CompoundTag nbt) {
//        super.deserializeNBT(nbt);
//        whitelistEntity = nbt.getBoolean("include");
//        entityName = nbt.getString("name");
//    }
//
//    @Override
//    public void serializeMCD(MCDataOutput output) {
//        super.serializeMCD(output);
//        output.writeBoolean(whitelistEntity);
//        output.writeString(entityName);
//    }
//
//    @Override
//    public void deSerializeMCD(MCDataInput input) {
//        super.deSerializeMCD(input);
//        whitelistEntity = input.readBoolean();
//        entityName = input.readString();
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