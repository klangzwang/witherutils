package geni.witherutils.base.common.item.withersteel.attributes;

import javax.annotation.Nonnull;

import geni.witherutils.base.common.config.BaseConfig;
import geni.witherutils.core.common.util.NNList;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;

public class WitherSteelAttributeModifiers {

    private static final @Nonnull String UU_ATTACK = "WitherUtils Empowered Attack Bonus";
    private static final @Nonnull String UU_SPEED = "WitherUtils Speed Bonus";
    private static final @Nonnull String UU_TOUGHNESS = "WitherUtils Toughness Bonus";
    private static final @Nonnull String UU_RESISTANCE = "WitherUtils Resistance Bonus";
    
    private static final @Nonnull NNList<AttributeModifier> ATTACK_DAMAGE = new NNList<>(
            new WitherSteelAttributeModifier(UU_ATTACK, BaseConfig.COMMON.WITHERSTEEL.witherSteelSwordDamageBonusEmpowered0.get(), Operation.ADDITION),
            new WitherSteelAttributeModifier(UU_ATTACK, BaseConfig.COMMON.WITHERSTEEL.witherSteelSwordDamageBonusEmpowered1.get(), Operation.ADDITION),
            new WitherSteelAttributeModifier(UU_ATTACK, BaseConfig.COMMON.WITHERSTEEL.witherSteelSwordDamageBonusEmpowered2.get(), Operation.ADDITION),
            new WitherSteelAttributeModifier(UU_ATTACK, BaseConfig.COMMON.WITHERSTEEL.witherSteelSwordDamageBonusEmpowered3.get(), Operation.ADDITION));
    
    private static final @Nonnull NNList<AttributeModifier> ATTACK_SPEED = new NNList<>(
            new WitherSteelAttributeModifier(UU_ATTACK, BaseConfig.COMMON.WITHERSTEEL.witherSteelSwordSpeedBonusEmpowered0.get(), Operation.ADDITION),
            new WitherSteelAttributeModifier(UU_ATTACK, BaseConfig.COMMON.WITHERSTEEL.witherSteelSwordSpeedBonusEmpowered1.get(), Operation.ADDITION),
            new WitherSteelAttributeModifier(UU_ATTACK, BaseConfig.COMMON.WITHERSTEEL.witherSteelSwordSpeedBonusEmpowered2.get(), Operation.ADDITION),
            new WitherSteelAttributeModifier(UU_ATTACK, BaseConfig.COMMON.WITHERSTEEL.witherSteelSwordSpeedBonusEmpowered3.get(), Operation.ADDITION));
    
    private static final @Nonnull NNList<AttributeModifier> ARMOR_TOUGHNESS = new NNList<>(
            new WitherSteelAttributeModifier(UU_TOUGHNESS, BaseConfig.COMMON.WITHERSTEEL.witherSteelArmorToughnessBonusEmpowered0.get(), Operation.ADDITION),
            new WitherSteelAttributeModifier(UU_TOUGHNESS, BaseConfig.COMMON.WITHERSTEEL.witherSteelArmorToughnessBonusEmpowered1.get(), Operation.ADDITION),
            new WitherSteelAttributeModifier(UU_TOUGHNESS, BaseConfig.COMMON.WITHERSTEEL.witherSteelArmorToughnessBonusEmpowered2.get(), Operation.ADDITION),
            new WitherSteelAttributeModifier(UU_TOUGHNESS, BaseConfig.COMMON.WITHERSTEEL.witherSteelArmorToughnessBonusEmpowered3.get(), Operation.ADDITION));

    private static final @Nonnull NNList<AttributeModifier> RESISTANCE = new NNList<>(
            new WitherSteelAttributeModifier(UU_RESISTANCE, BaseConfig.COMMON.WITHERSTEEL.witherSteelKnockbackBonusEmpowered0.get(), Operation.ADDITION),
            new WitherSteelAttributeModifier(UU_RESISTANCE, BaseConfig.COMMON.WITHERSTEEL.witherSteelKnockbackBonusEmpowered1.get(), Operation.ADDITION),
            new WitherSteelAttributeModifier(UU_RESISTANCE, BaseConfig.COMMON.WITHERSTEEL.witherSteelKnockbackBonusEmpowered2.get(), Operation.ADDITION),
            new WitherSteelAttributeModifier(UU_RESISTANCE, BaseConfig.COMMON.WITHERSTEEL.witherSteelKnockbackBonusEmpowered3.get(), Operation.ADDITION));
    
    public static @Nonnull AttributeModifier getAttackDamage(int level)
    {
        return ATTACK_DAMAGE.get(level);
    }
    public static @Nonnull AttributeModifier getAttackSpeed(int level)
    {
        return ATTACK_SPEED.get(level);
    }
    public static @Nonnull AttributeModifier getArmorToughness(int level)
    {
        return ARMOR_TOUGHNESS.get(level);
    }
    public static @Nonnull AttributeModifier getKnockbackResistance(int level)
    {
        return RESISTANCE.get(level);
    }
}
