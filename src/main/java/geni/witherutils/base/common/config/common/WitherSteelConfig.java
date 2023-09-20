package geni.witherutils.base.common.config.common;

import net.minecraftforge.common.ForgeConfigSpec;

public class WitherSteelConfig {
    
    public final ForgeConfigSpec.ConfigValue<Integer> witherSteelSwordPowerUsePerHit;
    public final ForgeConfigSpec.ConfigValue<Double> witherSteelSwordDamageBonusEmpowered0;
    public final ForgeConfigSpec.ConfigValue<Double> witherSteelSwordDamageBonusEmpowered1;
    public final ForgeConfigSpec.ConfigValue<Double> witherSteelSwordDamageBonusEmpowered2;
    public final ForgeConfigSpec.ConfigValue<Double> witherSteelSwordDamageBonusEmpowered3;
    public final ForgeConfigSpec.ConfigValue<Double> witherSteelSwordSpeedBonusEmpowered0;
    public final ForgeConfigSpec.ConfigValue<Double> witherSteelSwordSpeedBonusEmpowered1;
    public final ForgeConfigSpec.ConfigValue<Double> witherSteelSwordSpeedBonusEmpowered2;
    public final ForgeConfigSpec.ConfigValue<Double> witherSteelSwordSpeedBonusEmpowered3;
    public final ForgeConfigSpec.ConfigValue<Double> witherSteelArmorToughnessBonusEmpowered0;
    public final ForgeConfigSpec.ConfigValue<Double> witherSteelArmorToughnessBonusEmpowered1;
    public final ForgeConfigSpec.ConfigValue<Double> witherSteelArmorToughnessBonusEmpowered2;
    public final ForgeConfigSpec.ConfigValue<Double> witherSteelArmorToughnessBonusEmpowered3;
    public final ForgeConfigSpec.ConfigValue<Double> witherSteelKnockbackBonusEmpowered0;
    public final ForgeConfigSpec.ConfigValue<Double> witherSteelKnockbackBonusEmpowered1;
    public final ForgeConfigSpec.ConfigValue<Double> witherSteelKnockbackBonusEmpowered2;
    public final ForgeConfigSpec.ConfigValue<Double> witherSteelKnockbackBonusEmpowered3;
    
    public WitherSteelConfig(ForgeConfigSpec.Builder builder)
    {
    	builder.push("withersteel");
    	
        builder.push("powerUsePerHit");
        witherSteelSwordPowerUsePerHit = builder.comment("The amount of energy used per hit.").defineInRange("powerUsePerHit", 750, 1, 99999999);
        builder.pop();

        builder.push("damageBonus");
        
	        builder.push("damageBonusEmpowered0");
	        witherSteelSwordDamageBonusEmpowered0 = builder.comment("The extra damage dealt when the sword has no SoulBank Power.").defineInRange("damageBonusEmpowered0", 0.0, 0, 32);
	        builder.pop();
	        
	        builder.push("damageBonusEmpowered1");
	        witherSteelSwordDamageBonusEmpowered1 = builder.comment("The extra damage dealt when the sword has SoulBank Power I and has energy.").defineInRange("damageBonusEmpowered1", 3.0, 0, 32);
	        builder.pop();
	        
	        builder.push("damageBonusEmpowered2");
	        witherSteelSwordDamageBonusEmpowered2 = builder.comment("The extra damage dealt when the sword has SoulBank Power II and has energy.").defineInRange("damageBonusEmpowered2", 6.0, 0, 32);
	        builder.pop();
	        
	        builder.push("damageBonusEmpowered3");
	        witherSteelSwordDamageBonusEmpowered3 = builder.comment("The extra damage dealt when the sword has SoulBank Power III and has energy.").defineInRange("damageBonusEmpowered3", 9.0, 0, 32);
	        builder.pop();

	    builder.pop();
	        
	    builder.push("speedBonus");
	        
	        builder.push("speedBonusEmpowered0");
	        witherSteelSwordSpeedBonusEmpowered0 = builder.comment("The increase in attack speed when the sword has no SoulBank Power.").defineInRange("speedBonusEmpowered0", 0.0, 0, 2);
	        builder.pop(); 
	        
	        builder.push("speedBonusEmpowered1");
	        witherSteelSwordSpeedBonusEmpowered1 = builder.comment("The increase in attack speed when the sword has SoulBank Power I and has energy.").defineInRange("speedBonusEmpowered1", 0.5, 0, 2);
	        builder.pop();
	        
	        builder.push("speedBonusEmpowered2");
	        witherSteelSwordSpeedBonusEmpowered2 = builder.comment("The increase in attack speed when the sword has SoulBank Power II and has energy.").defineInRange("speedBonusEmpowered2", 0.6, 0, 2);
	        builder.pop();
	        
	        builder.push("speedBonusEmpowered3");
	        witherSteelSwordSpeedBonusEmpowered3 = builder.comment("The increase in attack speed when the sword has SoulBank Power III and has energy.").defineInRange("speedBonusEmpowered3", 0.7, 0, 2);
	        builder.pop();
	        
	    builder.pop();
	        
	    builder.push("armorToughnessBonus");
	    
	        builder.push("armorToughnessBonusEmpowered0");
	        witherSteelArmorToughnessBonusEmpowered0 = builder.comment("The increase in armor toughness. No SoulBank Power.").defineInRange("armorToughnessBonusEmpowered0", 0.0D, 0.0D, 20.0D);
	        builder.pop();
	        
	        builder.push("armorToughnessBonusEmpowered1");
	        witherSteelArmorToughnessBonusEmpowered1 = builder.comment("The increase in armor toughness when SoulBank Power I and has energy.").defineInRange("armorToughnessBonusEmpowered1", 5.0D, 0.0D, 20.0D);
	        builder.pop();
	        
	        builder.push("armorToughnessBonusEmpowered2");
	        witherSteelArmorToughnessBonusEmpowered2 = builder.comment("The increase in armor toughness when SoulBank Power II and has energy.").defineInRange("armorToughnessBonusEmpowered2", 10.0D, 0.0D, 20.0D);
	        builder.pop();
	        
	        builder.push("armorToughnessBonusEmpowered3");
	        witherSteelArmorToughnessBonusEmpowered3 = builder.comment("The increase in armor toughness when SoulBank Power III and has energy.").defineInRange("armorToughnessBonusEmpowered3", 15.0D, 0.0D, 20.0D);
	        builder.pop();
	        
	    builder.pop();

	    builder.push("knockbackBonus");
	        
	        builder.push("knockbackBonusEmpowered0");
	        witherSteelKnockbackBonusEmpowered0 = builder.comment("The increase in Knockback Resistance. No SoulBank Power.").defineInRange("knockbackBonusEmpowered0", 0.0D, 0.0D, 1.0D);
	        builder.pop();
	        
	        builder.push("knockbackBonusEmpowered1");
	        witherSteelKnockbackBonusEmpowered1 = builder.comment("The increase in Knockback Resistance when SoulBank Power I and has energy.").defineInRange("knockbackBonusEmpowered1", 0.2D, 0.0D, 1.0D);
	        builder.pop();
	        
	        builder.push("knockbackBonusEmpowered2");
	        witherSteelKnockbackBonusEmpowered2 = builder.comment("The increase in Knockback Resistance when SoulBank Power II and has energy.").defineInRange("knockbackBonusEmpowered2", 0.5D, 0.0D, 1.0D);
	        builder.pop();
	        
	        builder.push("knockbackBonusEmpowered3");
	        witherSteelKnockbackBonusEmpowered3 = builder.comment("The increase in Knockback Resistance when SoulBank Power III and has energy.").defineInRange("knockbackBonusEmpowered3", 0.8D, 0.0D, 1.0D);
	        builder.pop();
	        
        builder.pop();    
        
        builder.pop();
    }
}
