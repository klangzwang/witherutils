package geni.witherutils.base.common.soulbank;

import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.Tag;

import java.util.Map;

import geni.witherutils.WitherUtils;
import geni.witherutils.api.soulbank.ISoulBankData;
import geni.witherutils.api.soulbank.SoulBankModifier;

public enum DefaultSoulBankData implements ISoulBankData {

    NONE(0),
    BASIC(1.0f),
    ADVANCED(2.0f),
    ULTRA(3.0f);

    private final float base;

    DefaultSoulBankData(float base)
    {
        this.base = base;
    }

    @Override
    public float getBase()
    {
        return base;
    }

    @Override
    public float getModifier(SoulBankModifier modifier)
    {
        return getBase();
    }

    @Override
    public Map<SoulBankModifier, Float> getAllModifiers()
    {
        return Map.of();
    }

    @Override
    public Tag serializeNBT()
    {
        return FloatTag.valueOf(base);
    }

    @Override
    public void deserializeNBT(Tag nbt)
    {
        WitherUtils.LOGGER.warn("Tried to deserialize NBT for a default soulbank datum.");
    }
}
