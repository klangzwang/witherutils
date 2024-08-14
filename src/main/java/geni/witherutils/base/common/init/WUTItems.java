package geni.witherutils.base.common.init;

import java.util.function.Supplier;
import java.util.stream.IntStream;

import geni.witherutils.api.lib.Names;
import geni.witherutils.api.upgrade.WUTUpgrade;
import geni.witherutils.base.common.base.WitherItem;
import geni.witherutils.base.common.item.bucket.FluidBucketItem;
import geni.witherutils.base.common.item.card.CardItem;
import geni.witherutils.base.common.item.cutter.CutterItem;
import geni.witherutils.base.common.item.hammer.HammerItem;
import geni.witherutils.base.common.item.material.MaterialItem;
import geni.witherutils.base.common.item.pickaxe.PickaxeHeadItem;
import geni.witherutils.base.common.item.remote.RemoteItem;
import geni.witherutils.base.common.item.scaper.ScaperItem;
import geni.witherutils.base.common.item.shovel.ShovelItem;
import geni.witherutils.base.common.item.shovel.ShovelItem.ShovelType;
import geni.witherutils.base.common.item.soulbank.SoulBankItem;
import geni.witherutils.base.common.item.soulorb.SoulOrbItem;
import geni.witherutils.base.common.item.upgrade.UpgradeItem;
import geni.witherutils.base.common.item.withersteel.shield.ShieldSteelItem;
import geni.witherutils.base.common.item.withersteel.shield.ShieldSteelItem.ShieldType;
import geni.witherutils.base.common.item.withersteel.sword.SwordSteelItem;
import geni.witherutils.base.common.item.withersteel.wand.WandSteelItem;
import geni.witherutils.base.common.item.worm.WormItem;
import geni.witherutils.base.common.item.wrench.WrenchItem;
import geni.witherutils.base.common.upgrade.BuiltinUpgrade;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class WUTItems
{
    public static final DeferredRegister.Items ITEM_TYPES = DeferredRegister.createItems(Names.MODID);

	/*
	 * 
	 * MINECRAFTITEM 
	 *
	 */
    public static final DeferredItem<Item> TABWU = register("tabwu");
    public static final DeferredItem<Item> FEATHER = register("feather");
    
	/*
	 * 
	 * WITHERUTILSITEM 
	 *
	 */
    public static final DeferredItem<Item> WORM = register("worm", WormItem::new);
    public static final DeferredItem<Item> HAMMER = register("hammer", HammerItem::new);
    public static final DeferredItem<Item> SOULORB = register("soulorb", SoulOrbItem::new);
    public static final DeferredItem<Item> WRENCH = register("wrench", WrenchItem::new);
    public static final DeferredItem<Item> CUTTER = register("cutter", CutterItem::new);
    public static final DeferredItem<Item> REMOTE = register("remote", RemoteItem::new);
    public static final DeferredItem<Item> SCAPER = register("scaper", ScaperItem::new);
    public static final DeferredItem<Item> CARD = register("card", CardItem::new);
    public static final DeferredItem<SwordSteelItem> SWORD = register("sword", SwordSteelItem::new);
    public static final DeferredItem<WandSteelItem> WAND = register("wand", WandSteelItem::new);

    /*
    *
    * PICKAXE
    *
    */
    public static final DeferredItem<Item> PICKAXEHEAD = register("pickaxe_head", PickaxeHeadItem::new);
    
    /*
    *
    * SHOVELS
    *
    */    
    public static final DeferredItem<Item> SHOVEL_BASIC = register("shovel_basic", () -> new ShovelItem(ShovelType.BASIC, new Item.Properties()));
    public static final DeferredItem<Item> SHOVEL_ADVANCED = register("shovel_advanced", () -> new ShovelItem(ShovelType.ADVANCED, new Item.Properties()));
    public static final DeferredItem<Item> SHOVEL_MASTER = register("shovel_master", () -> new ShovelItem(ShovelType.MASTER, new Item.Properties()));

    /*
    *
    * BUCKETS
    *
    */ 
    public static final DeferredItem<BucketItem> BLUELIMBO_BUCKET = registerBucket("bluelimbo_bucket", WUTFluids.BLUELIMBO);
    public static final DeferredItem<BucketItem> COLDSLUSH_BUCKET = registerBucket("coldslush_bucket", WUTFluids.COLDSLUSH);
    public static final DeferredItem<BucketItem> EXPERIENCE_BUCKET = registerBucket("experience_bucket", WUTFluids.EXPERIENCE);
    public static final DeferredItem<BucketItem> FERTILIZER_BUCKET = registerBucket("fertilizer_bucket", WUTFluids.FERTILIZER);
    public static final DeferredItem<BucketItem> PORTIUM_BUCKET = registerBucket("portium_bucket", WUTFluids.PORTIUM);
    public static final DeferredItem<BucketItem> REDRESIN_BUCKET = registerBucket("redresin_bucket", WUTFluids.REDRESIN);
    public static final DeferredItem<BucketItem> SOULFUL_BUCKET = registerBucket("soulful_bucket", WUTFluids.SOULFUL);
    public static final DeferredItem<BucketItem> WITHERWATER_BUCKET = registerBucket("witherwater_bucket", WUTFluids.WITHERWATER);

    /*
    *
    * UPGRADES
    *
    */ 
    public static final DeferredItem<Item> UPGRADEFEATHER = ITEM_TYPES.register("upgrade_feather", () -> new WitherItem(new Item.Properties()));
    public static final DeferredItem<Item> UPGRADEJUMP = ITEM_TYPES.register("upgrade_jump", () -> new WitherItem(new Item.Properties()));
    public static final DeferredItem<Item> UPGRADESPEED = ITEM_TYPES.register("upgrade_speed", () -> new WitherItem(new Item.Properties()));
    public static final DeferredItem<Item> UPGRADESQUID = ITEM_TYPES.register("upgrade_squid", () -> new WitherItem(new Item.Properties()));
    public static final DeferredItem<Item> UPGRADEVISION = ITEM_TYPES.register("upgrade_vision", () -> new WitherItem(new Item.Properties()));
    
    /*
     *
     * MATERIALS
     *
     */
    public static final DeferredItem<Item> WITHERSTEEL_INGOT = ITEM_TYPES.register("withersteel_ingot", () -> new WitherItem(new Item.Properties()));
    public static final DeferredItem<Item> WITHERSTEEL_NUGGET = ITEM_TYPES.register("withersteel_nugget", () -> new WitherItem(new Item.Properties()));
    public static final DeferredItem<Item> WITHERSTEEL_PLATE = ITEM_TYPES.register("withersteel_plate", () -> new WitherItem(new Item.Properties()));
    public static final DeferredItem<Item> WITHERSTEEL_GEAR = ITEM_TYPES.register("withersteel_gear", () -> new MaterialItem(new Item.Properties(), false, false, true, 90));
    public static final DeferredItem<Item> UPCASE = register("upcase", () -> new WitherItem(new Item.Properties()));
    public static final DeferredItem<Item> ADCASE = register("adcase", () -> new WitherItem(new Item.Properties()));
    public static final DeferredItem<Item> IRON_PLATE = register("iron_plate", () -> new WitherItem(new Item.Properties()));
    public static final DeferredItem<Item> IRON_GEAR = register("iron_gear", () -> new MaterialItem(new Item.Properties(), false, false, true, 45));
    public static final DeferredItem<Item> IRON_ROD = register("iron_rod", () -> new WitherItem(new Item.Properties()));
    public static final DeferredItem<Item> SOULISHED_INGOT = ITEM_TYPES.register("soulished_ingot", () -> new WitherItem(new Item.Properties()));
    public static final DeferredItem<Item> SOULISHED_NUGGET = ITEM_TYPES.register("soulished_nugget", () -> new WitherItem(new Item.Properties()));
    public static final DeferredItem<Item> ENDERPSHARD = ITEM_TYPES.register("enderpearl_shard", () -> new WitherItem(new Item.Properties()));
    public static final DeferredItem<Item> BLINK_PLATE = ITEM_TYPES.register("blink_plate", () -> new WitherItem(new Item.Properties()));
    public static final DeferredItem<Item> FAN = ITEM_TYPES.register("fan", () -> new MaterialItem(new Item.Properties(), false, false, true, -180));
    public static final DeferredItem<Item> SPIRAL = ITEM_TYPES.register("spiral", () -> new WitherItem(new Item.Properties()));
    public static final DeferredItem<Item> ANCHOR = ITEM_TYPES.register("anchor", () -> new WitherItem(new Item.Properties()));

    /*
     * 
     * SHIELD
     * 
     */
    public static final DeferredItem<Item> SHIELDBASIC = ITEM_TYPES.register("shield_basic", () -> new ShieldSteelItem(ShieldType.BASIC));
    public static final DeferredItem<Item> SHIELDADV = ITEM_TYPES.register("shield_adv", () -> new ShieldSteelItem(ShieldType.ADVANCED));
    public static final DeferredItem<Item> SHIELDROTTEN = ITEM_TYPES.register("shield_rotten", () -> new ShieldSteelItem(ShieldType.ROTTEN));
//    public static final DeferredItem<Item> SHIELDENERGY = ITEM_TYPES.register("shield_energy", () -> new ShieldPoweredItem());

    /*
     *
     * SOULBANK
     *
     */
    public static final DeferredItem<Item> SOULBANK_CASE = register("soulbank_case", () -> new WitherItem(new Item.Properties()));
    public static final DeferredItem<SoulBankItem> SOULBANK_BASIC = register("soulbank_basic", () -> new SoulBankItem(new Item.Properties()));
    public static final DeferredItem<SoulBankItem> SOULBANK_ADVANCED = register("soulbank_adv", () -> new SoulBankItem(new Item.Properties()));
    public static final DeferredItem<SoulBankItem> SOULBANK_ULTRA = register("soulbank_ultra", () -> new SoulBankItem(new Item.Properties()));
    
    /*
     * 
     * EGGS
     * 
     */
    public static final DeferredItem<Item> EGG_CURSEDZOMBIE = register("egg_cursedzombie", () -> new DeferredSpawnEggItem(WUTEntities.CURSEDZOMBIE, 0x3F8439, 0x9D612A, new Item.Properties()));
    public static final DeferredItem<Item> EGG_CURSEDCREEPER = register("egg_cursedcreeper", () -> new DeferredSpawnEggItem(WUTEntities.CURSEDCREEPER, 0x51857A, 0x7F6258, new Item.Properties()));
    public static final DeferredItem<Item> EGG_CURSEDSKELETON = register("egg_cursedskeleton", () -> new DeferredSpawnEggItem(WUTEntities.CURSEDSKELETON, 0x7CE2E5, 0x564645, new Item.Properties()));
    public static final DeferredItem<Item> EGG_CURSEDSPIDER = register("egg_cursedspider", () -> new DeferredSpawnEggItem(WUTEntities.CURSEDSPIDER, 0x9F8439, 0x364645, new Item.Properties()));
    public static final DeferredItem<Item> EGG_CURSEDDRYHEAD = register("egg_curseddryhead", () -> new DeferredSpawnEggItem(WUTEntities.CURSEDDRYHEAD, 0x121212, 0x464646, new Item.Properties()));
    
    /*
     * 
     * REGISTERING
     * 
     */
    public static Item.Properties defaultProps()
    {
        return new Item.Properties();
    }
    public static Item.Properties filledBucketProps()
    {
        return defaultProps().stacksTo(1).craftRemainder(Items.BUCKET);
    }
    private static DeferredItem<Item> register(final String name)
    {
        return register(name, () -> new Item(WUTItems.defaultProps()));
    }
    private static <T extends Item> DeferredItem<T> register(final String name, final Supplier<T> sup)
    {
        return ITEM_TYPES.register(name, sup);
    }
    private static DeferredItem<BucketItem> registerBucket(String name, Supplier<? extends Fluid> sup)
    {
        return register(name, () -> new FluidBucketItem(sup.get()));
    }

	/*
	 * 
	 * BLOCKITEM 
	 *
	 */
//    public static void registerBlockItems(IEventBus modEventBus)
//    {
//        modEventBus.addListener((RegisterEvent event) -> {
//            if (event.getRegistryKey() == Registries.ITEM)
//            {
//                for (var entry : BuiltInRegistries.BLOCK.entrySet())
//                {
//                    var id = entry.getKey();
//                    if (id.location().getNamespace().equals(Names.MODID))
//                    {
//                        var block = entry.getValue();
//                        var name = BuiltInRegistries.BLOCK.getKey(block);
//                        BlockItem blockItem;
//                        if (block instanceof IBlock iBlock)
//                        	blockItem = iBlock.getBlockItem(new Item.Properties());
//                        else
//                        	blockItem = new ItemBlock<>(block, new Item.Properties());
//                        Registry.register(BuiltInRegistries.ITEM, name, blockItem);
//                    }
//                }
//            }
//        });
//    }

	/*
	 * 
	 * UPGRADEITEM 
	 *
	 */
    static
    {
        for (BuiltinUpgrade bu : BuiltinUpgrade.values())
        {
            registerUpgrade(bu);
        }
    }
    
    private static void registerUpgrade(BuiltinUpgrade builtin)
    {
        WUTUpgrade pncUpgrade = builtin.registerUpgrade();
        IntStream.rangeClosed(1, builtin.getMaxTier()).forEach(tier -> {
            register(pncUpgrade.getItemRegistryName(tier).getPath(), () -> new UpgradeItem(pncUpgrade, tier, builtin.getRarity()));
        });
    }
}
