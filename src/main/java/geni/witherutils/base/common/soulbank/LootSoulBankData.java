package geni.witherutils.base.common.soulbank;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.HashMap;
import java.util.Map;

import geni.witherutils.WitherUtils;
import geni.witherutils.api.soulbank.ISoulBankData;
import geni.witherutils.api.soulbank.SoulBankModifier;

public final class LootSoulBankData implements ISoulBankData {
    
    private float base;

    private final Map<SoulBankModifier, Float> modifiers;

    public LootSoulBankData()
    {
        this.base = 1.0f;
        this.modifiers = new HashMap<>();
    }

    public LootSoulBankData(float base, Map<SoulBankModifier, Float> specializations)
    {
        this.base = base;
        this.modifiers = specializations;
    }

    @Override
    public float getBase()
    {
        return base;
    }

    public void setBase(float base)
    {
        this.base = base;
    }

    @Override
    public float getModifier(SoulBankModifier modifier)
    {
        return modifiers.getOrDefault(modifier, getBase());
    }

    @Override
    public Map<SoulBankModifier, Float> getAllModifiers()
    {
        return modifiers;
    }

    public void addSpecialization(SoulBankModifier modifier, float level)
    {
        this.modifiers.put(modifier, level);
    }

    public void addNewModifier(SoulBankModifier modifier, float level)
    {
        this.modifiers.clear();
        addSpecialization(modifier, level);
    }

    public void addAllModifiers(Map<SoulBankModifier, Float> specializations)
    {
        for (Map.Entry<SoulBankModifier, Float> entry : specializations.entrySet())
        {
            addSpecialization(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Tag serializeNBT()
    {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("base", base);
        ListTag list = new ListTag();
        modifiers.forEach((s, l) -> {
            CompoundTag entry = new CompoundTag();
            entry.putInt("modifier", s.ordinal());
            entry.putFloat("level", l);
            list.add(entry);
        });
        tag.put("modifiers", list);
        return tag;
    }

    @Override
    public void deserializeNBT(Tag nbt)
    {
        if (nbt instanceof CompoundTag tag)
        {
            this.modifiers.clear();
            this.base = tag.getFloat("base");
            ListTag list = tag.getList("modifiers", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++)
            {
                CompoundTag listElement = list.getCompound(i);
                
                try
                {
                    addSpecialization(SoulBankModifier.values()[listElement.getInt("modifier")], listElement.getFloat("level"));
                }
                catch (IndexOutOfBoundsException ex)
                {
                    WitherUtils.LOGGER.error("Invalid soulbank modifier in loot soulbank NBT. Ignoring.");
                }
            }
        }
    }
}
