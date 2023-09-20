package geni.witherutils.base.common.base;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public class WitherArmorMaterial implements ArmorMaterial {

    protected static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
    private String name;
    private int durability;
    private int[] damageReduction;
    private int encantability;
    private SoundEvent sound;
    private float toughness;
    private Ingredient ingredient = null;
    public float knockbackResistance = 0.0F;

    public WitherArmorMaterial(String name, int durability, int[] damageReduction, int encantability, SoundEvent sound, float toughness)
    {
        this.name = name;
        this.durability = durability;
        this.damageReduction = damageReduction;
        this.encantability = encantability;
        this.sound = sound;
        this.toughness = toughness;
        this.knockbackResistance = 0;
    }
    public WitherArmorMaterial(String name, int durability, int[] damageReduction, int encantability, SoundEvent sound, float toughness, float knockbackResist)
    {
        this.name = name;
        this.durability = durability;
        this.damageReduction = damageReduction;
        this.encantability = encantability;
        this.sound = sound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResist;
    }
    @Override
    public int getEnchantmentValue()
    {
        return this.encantability;
    }
    @Override
    public SoundEvent getEquipSound()
    {
        return this.sound;
    }
    @Override
    public Ingredient getRepairIngredient()
    {
        return this.ingredient == null ? Ingredient.EMPTY : this.ingredient;
    }
    public void setRepairMaterial(Ingredient ingredient)
    {
        this.ingredient = ingredient;
    }
    @Override
    public String getName()
    {
        return name;
    }
    @Override
    public float getToughness()
    {
        return toughness;
    }
    @Override
    public float getKnockbackResistance()
    {
        return knockbackResistance;
    }
    @Override
    public int getDurabilityForType(Type slotIn)
    {
        return MAX_DAMAGE_ARRAY[slotIn.ordinal()] * this.durability;
    }
    @Override
    public int getDefenseForType(Type slotIn)
    {
        return this.damageReduction[slotIn.ordinal()];
    }
}
