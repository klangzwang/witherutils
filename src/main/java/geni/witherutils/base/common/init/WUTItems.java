package geni.witherutils.base.common.init;

import java.util.List;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.base.WitherArmorMaterial;
import geni.witherutils.base.common.base.WitherItem;
import geni.witherutils.base.common.item.card.CardItem;
import geni.witherutils.base.common.item.cutter.CutterItem;
import geni.witherutils.base.common.item.hammer.HammerItem;
import geni.witherutils.base.common.item.material.MaterialItem;
import geni.witherutils.base.common.item.remote.RemoteItem;
import geni.witherutils.base.common.item.soulbank.SoulBankItem;
import geni.witherutils.base.common.item.soulorb.SoulOrbItem;
import geni.witherutils.base.common.item.withersteel.armor.SteelArmorItem;
import geni.witherutils.base.common.item.withersteel.shield.ShieldPoweredItem;
import geni.witherutils.base.common.item.withersteel.shield.ShieldSteelItem;
import geni.witherutils.base.common.item.withersteel.shield.ShieldSteelItem.ShieldType;
import geni.witherutils.base.common.item.withersteel.sword.SwordSteelItem;
import geni.witherutils.base.common.item.withersteel.wand.WandSteelItem;
import geni.witherutils.base.common.item.worm.WormItem;
import geni.witherutils.base.common.item.wrench.WrenchItem;
import geni.witherutils.base.common.soulbank.DefaultSoulBankData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class WUTItems
{
    public static final DeferredRegister<Item> ITEM_TYPES = DeferredRegister.create(ForgeRegistries.ITEMS, WitherUtils.MODID);

    /*
     * 
     * TIER
     * 
     */
    public static final Tier DARK_STEEL_TIER = TierSortingRegistry.registerTier(
            new ForgeTier(3, 2000, 8.0F, 3, 25, WUTTags.Blocks.WITHER_STEEL_TIER, () -> Ingredient.of(WUTItems.WITHERSTEEL_INGOT.get())),
            WitherUtils.loc("wither_steel_tier"), List.of(Tiers.DIAMOND), List.of(Tiers.NETHERITE));
    
    /*
     * 
     * BASIC
     * 
     */
    public static final RegistryObject<Item> TABWU = ITEM_TYPES.register("tabwu", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HAMMER = ITEM_TYPES.register("hammer", () -> new HammerItem());
    public static final RegistryObject<Item> WRENCH = ITEM_TYPES.register("wrench", () -> new WrenchItem());
    public static final RegistryObject<Item> CUTTER = ITEM_TYPES.register("cutter", () -> new CutterItem());
    public static final RegistryObject<Item> SOULORB = ITEM_TYPES.register("soulorb", () -> new SoulOrbItem());
    public static final RegistryObject<Item> SWORD = ITEM_TYPES.register("sword", () -> new SwordSteelItem());
    public static final RegistryObject<Item> WAND = ITEM_TYPES.register("wand", () -> new WandSteelItem());
    public static final RegistryObject<Item> WORM = ITEM_TYPES.register("worm", () -> new WormItem());
    public static final RegistryObject<Item> CARD = ITEM_TYPES.register("card", () -> new CardItem());
    public static final RegistryObject<Item> UPCASE = ITEM_TYPES.register("upcase", () -> new WitherItem(new Item.Properties()));
    public static final RegistryObject<Item> REMOTE = ITEM_TYPES.register("remote", () -> new RemoteItem());
    public static final RegistryObject<Item> FEATHER = ITEM_TYPES.register("feather", () -> new WitherItem(new Item.Properties()));
    
    /*
     * 
     * BUCKETS
     * 
     */
    public static final RegistryObject<Item> FERTILIZER_BUCKET = ITEM_TYPES.register("fertilizer_bucket", () -> new BucketItem(() -> WUTFluids.FERTILIZER.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final RegistryObject<Item> EXPERIENCE_BUCKET = ITEM_TYPES.register("experience_bucket", () -> new BucketItem(() -> WUTFluids.EXPERIENCE.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    /*
     * 
     * ARMOR
     * 
     */
    public static WitherArmorMaterial STANDARDARMOR_MATERIAL = new WitherArmorMaterial("standardarmor_material", 35, new int[]{2, 6, 5, 2}, 15, SoundEvents.ARMOR_EQUIP_DIAMOND, 1.0F);
    public static final RegistryObject<Item> STEELARMOR_HELMET = ITEM_TYPES.register("steelarmor_helmet", () -> new SteelArmorItem(STANDARDARMOR_MATERIAL, ArmorItem.Type.HELMET, new Item.Properties().durability(385).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> STEELARMOR_CHEST = ITEM_TYPES.register("steelarmor_chest", () -> new SteelArmorItem(STANDARDARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(560).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> STEELARMOR_LEGGINGS = ITEM_TYPES.register("steelarmor_leggings", () -> new SteelArmorItem(STANDARDARMOR_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(525).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> STEELARMOR_BOOTS = ITEM_TYPES.register("steelarmor_boots", () -> new SteelArmorItem(STANDARDARMOR_MATERIAL, ArmorItem.Type.BOOTS, new Item.Properties().durability(455).rarity(Rarity.COMMON)));

    /*
     * 
     * SHIELD
     * 
     */
    public static final RegistryObject<Item> SHIELDBASIC = ITEM_TYPES.register("shield_basic", () -> new ShieldSteelItem(ShieldType.BASIC));
    public static final RegistryObject<Item> SHIELDADV = ITEM_TYPES.register("shield_adv", () -> new ShieldSteelItem(ShieldType.ADVANCED));
    public static final RegistryObject<Item> SHIELDROTTEN = ITEM_TYPES.register("shield_rotten", () -> new ShieldSteelItem(ShieldType.ROTTEN));
    public static final RegistryObject<Item> SHIELDENERGY = ITEM_TYPES.register("shield_energy", () -> new ShieldPoweredItem());
    
    /*
     * 
     * SOULBANK
     * 
     */
    public static final RegistryObject<Item> SOULBANK_CASE = ITEM_TYPES.register("soulbank_case", () -> new WitherItem(new Item.Properties()));
    public static final RegistryObject<SoulBankItem> SOULBANK_BASIC = ITEM_TYPES.register("soulbank_basic", () -> new SoulBankItem(DefaultSoulBankData.BASIC, new Item.Properties()));
    public static final RegistryObject<SoulBankItem> SOULBANK_ADVANCED = ITEM_TYPES.register("soulbank_adv", () -> new SoulBankItem(DefaultSoulBankData.ADVANCED, new Item.Properties()));
    public static final RegistryObject<SoulBankItem> SOULBANK_ULTRA = ITEM_TYPES.register("soulbank_ultra", () -> new SoulBankItem(DefaultSoulBankData.ULTRA, new Item.Properties()));
    
    /*
     * 
     * MATERIALS
     * 
     */
    public static final RegistryObject<Item> WITHERSTEEL_INGOT = ITEM_TYPES.register("withersteel_ingot", () -> new WitherItem(new Item.Properties()));
    public static final RegistryObject<Item> WITHERSTEEL_NUGGET = ITEM_TYPES.register("withersteel_nugget", () -> new WitherItem(new Item.Properties()));
    public static final RegistryObject<Item> WITHERSTEEL_PLATE = ITEM_TYPES.register("withersteel_plate", () -> new WitherItem(new Item.Properties()));
    public static final RegistryObject<Item> WITHERSTEEL_GEAR = ITEM_TYPES.register("withersteel_gear", () -> new MaterialItem(new Item.Properties(), false, false, true, 90));
    
    public static final RegistryObject<Item> IRON_PLATE = ITEM_TYPES.register("iron_plate", () -> new WitherItem(new Item.Properties()));
    public static final RegistryObject<Item> IRON_GEAR = ITEM_TYPES.register("iron_gear", () -> new MaterialItem(new Item.Properties(), false, false, true, 45));
    public static final RegistryObject<Item> IRON_ROD = ITEM_TYPES.register("iron_rod", () -> new WitherItem(new Item.Properties()));
    
    public static final RegistryObject<Item> SOULISHED_INGOT = ITEM_TYPES.register("soulished_ingot", () -> new WitherItem(new Item.Properties()));
    public static final RegistryObject<Item> SOULISHED_NUGGET = ITEM_TYPES.register("soulished_nugget", () -> new WitherItem(new Item.Properties()));
    
    public static final RegistryObject<Item> ENDERPSHARD = ITEM_TYPES.register("enderpearl_shard", () -> new WitherItem(new Item.Properties()));
    
    public static final RegistryObject<Item> BLINK_PLATE = ITEM_TYPES.register("blink_plate", () -> new WitherItem(new Item.Properties()));
    
    public static final RegistryObject<Item> SHOVEL = ITEM_TYPES.register("shovel", () -> new MaterialItem(new Item.Properties(), false, false, true, 45));
    public static final RegistryObject<Item> FAN = ITEM_TYPES.register("fan", () -> new MaterialItem(new Item.Properties(), false, false, true, -180));
    
    public static final RegistryObject<Item> SPIRAL = ITEM_TYPES.register("spiral", () -> new WitherItem(new Item.Properties()));
    public static final RegistryObject<Item> ANCHOR = ITEM_TYPES.register("anchor", () -> new WitherItem(new Item.Properties()));
    
    /*
     * 
     * EGGS
     * 
     */
    public static final RegistryObject<Item> EGG_CURSEDZOMBIE = ITEM_TYPES.register("egg_cursedzombie", () -> new ForgeSpawnEggItem(WUTEntities.CURSEDZOMBIE, 0x3F8439, 0x9D612A, new Item.Properties()));
    public static final RegistryObject<Item> EGG_CURSEDCREEPER = ITEM_TYPES.register("egg_cursedcreeper", () -> new ForgeSpawnEggItem(WUTEntities.CURSEDCREEPER, 0x51857A, 0x7F6258, new Item.Properties()));
    public static final RegistryObject<Item> EGG_CURSEDSKELETON = ITEM_TYPES.register("egg_cursedskeleton", () -> new ForgeSpawnEggItem(WUTEntities.CURSEDSKELETON, 0x7CE2E5, 0x564645, new Item.Properties()));
    public static final RegistryObject<Item> EGG_CURSEDSPIDER = ITEM_TYPES.register("egg_cursedspider", () -> new ForgeSpawnEggItem(WUTEntities.CURSEDSPIDER, 0x9F8439, 0x364645, new Item.Properties()));
}
