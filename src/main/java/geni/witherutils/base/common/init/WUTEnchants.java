package geni.witherutils.base.common.init;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.enchant.EnergyEnchantment;
import geni.witherutils.base.common.enchant.FeatherFallEnchantment;
import geni.witherutils.base.common.enchant.JumpingEnchantment;
import geni.witherutils.base.common.enchant.NightVisionEnchantment;
import geni.witherutils.base.common.enchant.PearlEnchantment;
import geni.witherutils.base.common.enchant.SolarPowerEnchantment;
import geni.witherutils.base.common.enchant.SprintingEnchantment;
import geni.witherutils.base.common.enchant.SquidJumpEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class WUTEnchants {

	public static final DeferredRegister<Enchantment> ENCHANT_TYPES = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, WitherUtils.MODID);

    @SuppressWarnings("unused")
    private static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };
	public static final RegistryObject<EnergyEnchantment> ENERGY = ENCHANT_TYPES.register(EnergyEnchantment.ID, () -> new EnergyEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));
	public static final RegistryObject<PearlEnchantment> PEARL = ENCHANT_TYPES.register(PearlEnchantment.ID, () -> new PearlEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));
	
	public static final RegistryObject<FeatherFallEnchantment> FEATHER_FALL = ENCHANT_TYPES.register(FeatherFallEnchantment.ID, () -> new FeatherFallEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR_CHEST, EquipmentSlot.CHEST));
	public static final RegistryObject<SquidJumpEnchantment> SQUID_JUMP = ENCHANT_TYPES.register(SquidJumpEnchantment.ID, () -> new SquidJumpEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.LEGS));
	public static final RegistryObject<SolarPowerEnchantment> SOLAR_POWER = ENCHANT_TYPES.register(SolarPowerEnchantment.ID, () -> new SolarPowerEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR_HEAD, EquipmentSlot.HEAD));
	public static final RegistryObject<NightVisionEnchantment> NIGHT_VISION = ENCHANT_TYPES.register(NightVisionEnchantment.ID, () -> new NightVisionEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR_HEAD, EquipmentSlot.HEAD));
	public static final RegistryObject<SprintingEnchantment> SPRINTING = ENCHANT_TYPES.register(SprintingEnchantment.ID, () -> new SprintingEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR_FEET, EquipmentSlot.FEET));
	public static final RegistryObject<JumpingEnchantment> JUMPING = ENCHANT_TYPES.register(JumpingEnchantment.ID, () -> new JumpingEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.LEGS));
}
