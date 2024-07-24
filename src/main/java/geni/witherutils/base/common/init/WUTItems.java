package geni.witherutils.base.common.init;

import geni.witherutils.api.lib.Names;
import geni.witherutils.base.common.item.cutter.CutterItem;
import geni.witherutils.base.common.item.hammer.HammerItem;
import geni.witherutils.base.common.item.soulorb.SoulOrbItem;
import geni.witherutils.base.common.item.withersteel.shield.ShieldSteelItem;
import geni.witherutils.base.common.item.withersteel.shield.ShieldSteelItem.ShieldType;
import geni.witherutils.base.common.item.withersteel.wand.WandSteelItem;
import geni.witherutils.base.common.item.worm.WormItem;
import geni.witherutils.base.common.item.wrench.WrenchItem;
import geni.witherutils.core.common.item.IBlock;
import geni.witherutils.core.common.item.ItemBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;

public class WUTItems
{
    public static final DeferredRegister.Items ITEM_TYPES = DeferredRegister.createItems(Names.MODID);
	
	/*
	 * 
	 * MINECRAFTITEM 
	 *
	 */
    public static final DeferredItem<Item> TABWU = ITEM_TYPES.register("tabwu", () -> new Item(new Item.Properties()));
    
	/*
	 * 
	 * WITHERUTILSITEM 
	 *
	 */
    public static final DeferredItem<Item> WORM = ITEM_TYPES.register("worm", () -> new WormItem());
    public static final DeferredItem<Item> HAMMER = ITEM_TYPES.register("hammer", () -> new HammerItem());
    public static final DeferredItem<Item> SOULORB = ITEM_TYPES.register("soulorb", () -> new SoulOrbItem());
    public static final DeferredItem<WandSteelItem> WAND = ITEM_TYPES.register("wand", () -> new WandSteelItem());
    public static final DeferredItem<Item> WRENCH = ITEM_TYPES.register("wrench", () -> new WrenchItem());
    public static final DeferredItem<Item> CUTTER = ITEM_TYPES.register("cutter", () -> new CutterItem());
    
    /*
     * 
     * SHIELD
     * 
     */
    public static final DeferredItem<Item> SHIELDBASIC = ITEM_TYPES.register("shield_basic", () -> new ShieldSteelItem(ShieldType.BASIC));
    
	/*
	 * 
	 * BLOCKITEM 
	 *
	 */
    public static void registerBlockItems(IEventBus modEventBus)
    {
        modEventBus.addListener((RegisterEvent event) -> {
            if (event.getRegistryKey() == Registries.ITEM)
            {
                for (var entry : BuiltInRegistries.BLOCK.entrySet())
                {
                    var id = entry.getKey();
                    if (id.location().getNamespace().equals(Names.MODID))
                    {
                        var block = entry.getValue();
                        var name = BuiltInRegistries.BLOCK.getKey(block);
                        BlockItem blockItem;
                        if (block instanceof IBlock iBlock)
                        	blockItem = iBlock.getBlockItem(new Item.Properties());
                        else
                        	blockItem = new ItemBlock<>(block, new Item.Properties());
                        Registry.register(BuiltInRegistries.ITEM, name, blockItem);
                    }
                }
            }
        });
    }
}
