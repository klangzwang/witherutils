package geni.witherutils.api.soulbank;

import net.minecraft.nbt.Tag;

import java.util.Map;

import geni.witherutils.api.nbt.INamedNBTSerializable;

public interface ISoulBankData extends INamedNBTSerializable<Tag> {

    float getBase();
    float getModifier(SoulBankModifier modifier);

    Map<SoulBankModifier, Float> getAllModifiers();

    @Override
    default String getSerializedName()
    {
        return "SoulBankData";
    }
}
