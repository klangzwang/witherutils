package geni.witherutils.base.common.init;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import geni.witherutils.api.lib.Names;
import geni.witherutils.base.common.base.CreativeTabStackProvider;
import geni.witherutils.base.common.base.CreativeTabVariants;
import geni.witherutils.base.common.block.deco.cutter.CutterBlock;
import geni.witherutils.base.common.item.bucket.FluidBucketItem;
import geni.witherutils.base.common.item.upgrade.UpgradeItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class WUTCreativeTab {

	public static final DeferredRegister<CreativeModeTab> TAB_TYPES = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Names.MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TABWU_ITEMS = TAB_TYPES.register("tabwu_items", () -> {
		CreativeModeTab tab = CreativeModeTab.builder()
    			.backgroundTexture(CreativeModeTab.createTextureLocation("wither_gray"))
    			.icon(() -> new ItemStack(WUTItems.TABWU.get()))
    			.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
    			.title(Component.translatable("itemGroup.witherutils.items"))
    			.displayItems((features, output) -> {

                    output.accept(new ItemStack(WUTItems.HAMMER.get()));
                    output.accept(new ItemStack(WUTItems.WRENCH.get()));
                    output.accept(new ItemStack(WUTItems.CUTTER.get()));
                    output.accept(new ItemStack(WUTItems.REMOTE.get()));
                    output.accept(new ItemStack(WUTItems.SWORD.get()));
                    output.accept(new ItemStack(WUTItems.WAND.get()));
                    output.accept(new ItemStack(WUTItems.IRON_ROD.get()));
                    
                    output.accept(new ItemStack(WUTItems.CARD.get()));
                    
                    output.accept(new ItemStack(WUTItems.PICKAXEHEAD.get()));
                    
                    output.accept(new ItemStack(WUTItems.ADCASE.get()));
                    output.accept(new ItemStack(WUTAdapters.FAKEJOB_ACTIVATING.get()));
                    output.accept(new ItemStack(WUTAdapters.FAKEJOB_CLICKING.get()));
                    output.accept(new ItemStack(WUTAdapters.FAKEJOB_MINING.get()));
                    output.accept(new ItemStack(WUTAdapters.FAKEJOB_PLACING.get()));
                    output.accept(new ItemStack(WUTAdapters.FAKEJOB_SCANNING.get()));
                    
                    output.accept(new ItemStack(WUTItems.SHIELDBASIC.get()));
                    output.accept(new ItemStack(WUTItems.SHIELDADV.get()));
                    output.accept(new ItemStack(WUTItems.SHIELDROTTEN.get()));
//                    output.accept(new ItemStack(WUTItems.SHIELDENERGY.get()));
                    
                    output.accept(new ItemStack(WUTItems.WITHERSTEEL_INGOT.get()));
                    output.accept(new ItemStack(WUTItems.SOULISHED_INGOT.get()));
                    output.accept(new ItemStack(WUTItems.WITHERSTEEL_NUGGET.get()));
                    output.accept(new ItemStack(WUTItems.SOULISHED_NUGGET.get()));
                    
                    output.accept(new ItemStack(WUTItems.SOULORB.get()));
                    
                    output.accept(new ItemStack(WUTItems.WITHERSTEEL_GEAR.get()));
                    output.accept(new ItemStack(WUTItems.IRON_GEAR.get()));
   
                    output.accept(new ItemStack(WUTItems.FAN.get()));

					output.accept(new ItemStack(WUTItems.SHOVEL_BASIC.get()));
					output.accept(new ItemStack(WUTItems.SHOVEL_ADVANCED.get()));
					output.accept(new ItemStack(WUTItems.SHOVEL_MASTER.get()));
                    
                    output.accept(new ItemStack(WUTItems.SPIRAL.get()));
                    output.accept(new ItemStack(WUTItems.ANCHOR.get()));
                    output.accept(new ItemStack(WUTItems.BLINK_PLATE.get()));
                    output.accept(new ItemStack(WUTItems.IRON_PLATE.get()));
                    output.accept(new ItemStack(WUTItems.WITHERSTEEL_PLATE.get()));
                    
                    output.accept(new ItemStack(WUTItems.SOULBANK_CASE.get()));
                    output.accept(new ItemStack(WUTItems.SOULBANK_BASIC.get()));
                    output.accept(new ItemStack(WUTItems.SOULBANK_ADVANCED.get()));
                    output.accept(new ItemStack(WUTItems.SOULBANK_ULTRA.get()));
                    
                    output.accept(new ItemStack(WUTBlocks.WITHERSTEEL_BLOCK.get()));
                    output.accept(new ItemStack(WUTBlocks.SOULISHED_BLOCK.get()));
                    output.accept(new ItemStack(WUTBlocks.ROTTEN_LOG.get()));
                    output.accept(new ItemStack(WUTBlocks.ROTTEN_LEAVES.get()));
                    output.accept(new ItemStack(WUTBlocks.ROTTEN_EARTH.get()));
                    output.accept(new ItemStack(WUTBlocks.ROTTEN_ROOTS.get()));
                    output.accept(new ItemStack(WUTBlocks.ROTTEN_ROOTS_POT.get()));
                    output.accept(new ItemStack(WUTBlocks.ROTTEN_SPIKE.get()));
                    output.accept(new ItemStack(WUTBlocks.SOULISHED_BLOCK.get()));
                    output.accept(new ItemStack(WUTBlocks.CASE_BIG.get()));
                    output.accept(new ItemStack(WUTBlocks.CASE_SMALL.get()));
                    
//                    output.accept(new ItemStack(WUTItems.STEELARMOR_HELMET.get()));
//                    output.accept(new ItemStack(WUTItems.STEELARMOR_CHEST.get()));
//                    output.accept(new ItemStack(WUTItems.STEELARMOR_LEGGINGS.get()));
//                    output.accept(new ItemStack(WUTItems.STEELARMOR_BOOTS.get()));

                    output.accept(new ItemStack(WUTItems.UPCASE.get()));
                    output.accept(new ItemStack(WUTItems.UPGRADEFEATHER.get()));
                    output.accept(new ItemStack(WUTItems.UPGRADEJUMP.get()));
                    output.accept(new ItemStack(WUTItems.UPGRADESPEED.get()));
                    output.accept(new ItemStack(WUTItems.UPGRADESQUID.get()));
                    output.accept(new ItemStack(WUTItems.UPGRADEVISION.get()));

                    output.accept(new ItemStack(WUTItems.EGG_CURSEDZOMBIE.get()));
//                    output.accept(new ItemStack(WUTItems.EGG_CURSEDCREEPER.get()));
//                    output.accept(new ItemStack(WUTItems.EGG_CURSEDSKELETON.get()));
//                    output.accept(new ItemStack(WUTItems.EGG_CURSEDSPIDER.get()));
//                    output.accept(new ItemStack(WUTItems.EGG_CURSEDDRYHEAD.get()));

                    output.accept(new ItemStack(WUTItems.WORM.get()));
                    output.accept(new ItemStack(WUTBlocks.ROTTEN_SAPLING.get()));
                    output.accept(new ItemStack(WUTBlocks.LILLY.get()));
                    output.accept(new ItemStack(WUTItems.ENDERPSHARD.get()));
                    
                    output.accept(new ItemStack(WUTItems.BLUELIMBO_BUCKET.get()));
                    output.accept(new ItemStack(WUTItems.COLDSLUSH_BUCKET.get()));
                    output.accept(new ItemStack(WUTItems.EXPERIENCE_BUCKET.get()));
                    output.accept(new ItemStack(WUTItems.FERTILIZER_BUCKET.get()));
                    output.accept(new ItemStack(WUTItems.PORTIUM_BUCKET.get()));
                    output.accept(new ItemStack(WUTItems.REDRESIN_BUCKET.get()));
                    output.accept(new ItemStack(WUTItems.SOULFUL_BUCKET.get()));
                    output.accept(new ItemStack(WUTItems.WITHERWATER_BUCKET.get()));
                    
                    /*
                     * 
                     * CREATIVETABVARIANTS
                     * 
                     */
                    WUTItems.ITEM_TYPES.getEntries().forEach(allitems -> {
                    	if(allitems.get() instanceof CreativeTabVariants variant)
                    	{
                    		variant.addAllVariants(output);
                    	}
                    });

                    /*
                     * 
                     * CREATIVETABSTACKS
                     * 
                     */
                    List<ItemStack> items = WUTItems.ITEM_TYPES.getEntries().stream()
                            .flatMap(ro -> stacksForItem(ro.get()))
                            .sorted(new ItemSorter())
                            .collect(Collectors.toCollection(ArrayList::new));
                    output.acceptAll(items);
                    
    			})
    			.build();
		return tab;
	});
    	
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TABWU_BLOCKS = TAB_TYPES.register("tabwu_blocks", () -> {
		CreativeModeTab tab = CreativeModeTab.builder()
				.backgroundTexture(CreativeModeTab.createTextureLocation("wither_gray"))
    			.icon(() -> new ItemStack(WUTItems.TABWU.get()))
    			.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
    			.title(Component.translatable("itemGroup.witherutils.blocks"))
    			.displayItems((features, output) -> {
    				
                	output.accept(new ItemStack(WUTBlocks.ANVIL.get()));
                	output.accept(new ItemStack(WUTBlocks.CAULDRON.get()));
                    output.accept(new ItemStack(WUTBlocks.ANGEL.get()));

                    output.accept(new ItemStack(WUTBlocks.GREENHOUSE.get()));
//                    output.accept(new ItemStack(WUTBlocks.RESERVOIR.get()));
//                    output.accept(new ItemStack(WUTBlocks.TANKDRUM.get()));
 
                    output.accept(new ItemStack(WUTBlocks.SMARTTV.get()));
                    output.accept(new ItemStack(WUTBlocks.FLOORSENSOR.get()));
                    output.accept(new ItemStack(WUTBlocks.WALLSENSOR.get()));
                    output.accept(new ItemStack(WUTBlocks.COLLECTOR.get()));
//                    output.accept(new ItemStack(WUTBlocks.FARMER.get()));
                    output.accept(new ItemStack(WUTBlocks.SPAWNER.get()));
                    output.accept(new ItemStack(WUTBlocks.FISHER.get()));
                    output.accept(new ItemStack(WUTBlocks.TOTEM.get()));

                    output.accept(new ItemStack(WUTBlocks.SOLARCASE.get()));
                    output.accept(new ItemStack(WUTBlocks.SOLARBASIC.get()));
                    output.accept(new ItemStack(WUTBlocks.SOLARADV.get()));
                    output.accept(new ItemStack(WUTBlocks.SOLARULTRA.get()));
                    output.accept(new ItemStack(WUTBlocks.LAVA_GENERATOR.get()));
                    output.accept(new ItemStack(WUTBlocks.WIND_GENERATOR.get()));
                    output.accept(new ItemStack(WUTBlocks.WATER_GENERATOR.get()));


//                    output.accept(new ItemStack(WUTBlocks.FLOODGATE.get()));
                    
//                    output.accept(new ItemStack(WUTBlocks.RACK_CASE.get()));
//                    output.accept(new ItemStack(WUTBlocks.RACK_TERMINAL.get()));
//                    output.accept(new ItemStack(WUTBlocks.RACKITEM_CONTROLLER.get()));
//                    output.accept(new ItemStack(WUTBlocks.RACKFLUID_CONTROLLER.get()));
                    
                    output.accept(new ItemStack(WUTBlocks.XPDRAIN.get()));
                    
//                    output.accept(new ItemStack(WUTBlocks.CORE.get()));
//                    output.accept(new ItemStack(WUTBlocks.PYLON.get()));
//                    output.accept(new ItemStack(WUTBlocks.STAB.get()));

                    output.accept(new ItemStack(WUTBlocks.CREATIVE_GENERATOR.get()));
//                    output.accept(new ItemStack(WUTBlocks.CREATIVE_TRASH.get()));
                    
                	output.accept(new ItemStack(WUTBlocks.FAKE_DRIVER.get()));
    			})
    			.build();
		return tab;
	});
    
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TABWU_DECOS = TAB_TYPES.register("tabwu_decos", () -> {
		CreativeModeTab tab = CreativeModeTab.builder()
				.backgroundTexture(CreativeModeTab.createTextureLocation("wither_gray"))
    			.icon(() -> new ItemStack(WUTItems.TABWU.get()))
    			.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
    			.title(Component.translatable("itemGroup.witherutils.decos"))
    			.displayItems((features, output) -> {
    				
                	output.accept(new ItemStack(WUTBlocks.SLICEDCONCRETEBLACK.get()));
                	output.accept(new ItemStack(WUTBlocks.SLICEDCONCRETEGRAY.get()));
                	output.accept(new ItemStack(WUTBlocks.SLICEDCONCRETEWHITE.get()));
                    output.accept(new ItemStack(WUTBlocks.STEELPOLEHEAD.get()));
                    output.accept(new ItemStack(WUTBlocks.STEELPOLE.get()));
                    output.accept(new ItemStack(WUTBlocks.STEELRAILING.get()));
                    output.accept(new ItemStack(WUTBlocks.DOOR_CASED.get()));
                    output.accept(new ItemStack(WUTBlocks.DOOR_CREEP.get()));
                    output.accept(new ItemStack(WUTBlocks.DOOR_LIRON.get()));
                    output.accept(new ItemStack(WUTBlocks.DOOR_STEEL.get()));
                    output.accept(new ItemStack(WUTBlocks.DOOR_STRIP.get()));
                    output.accept(new ItemStack(WUTBlocks.METALDOOR.get()));
                    output.accept(new ItemStack(WUTBlocks.BRICKSDARK.get()));
                    output.accept(new ItemStack(WUTBlocks.BRICKSLAVA.get()));
                	output.accept(new ItemStack(WUTBlocks.CATWALK.get()));
                	output.accept(new ItemStack(WUTBlocks.LIGHT.get()));
                	
                	output.accept(new ItemStack(WUTBlocks.FAN0.get()));
                	output.accept(new ItemStack(WUTBlocks.FAN1.get()));
                	output.accept(new ItemStack(WUTBlocks.FAN2.get()));
                	output.accept(new ItemStack(WUTBlocks.FAN3.get()));

                	output.accept(new ItemStack(WUTBlocks.CTM_METAL_0.get()));
                	for (CutterBlock cutterBlock : WUTBlocks.CUTTERBLOCKS)
                    {
                    	output.accept(new ItemStack(cutterBlock));
                    }
    			})
    			.build();
		return tab;
	});
    
    public static void init(IEventBus bus)
    {
        TAB_TYPES.register(bus);
    }

    private static Stream<ItemStack> stacksForItem(Item item)
    {
		if(item instanceof CreativeTabStackProvider tabStack)
		{
			CreativeTabStackProvider provider = tabStack;
			return provider.getStacksForItem();
		}
		return null;
    }

    private static class ItemSorter implements Comparator<ItemStack> {
    	
        @Override
        public int compare(ItemStack s1, ItemStack s2)
        {
            for (Class<?> cls : List.of(BlockItem.class, UpgradeItem.class, FluidBucketItem.class))
            {
                if (cls.isAssignableFrom(s1.getItem().getClass()) && !cls.isAssignableFrom(s2.getItem().getClass()))
                {
                    return -1;
                }
                else if (cls.isAssignableFrom(s2.getItem().getClass()) && !cls.isAssignableFrom(s1.getItem().getClass()))
                {
                    return 1;
                }
            }
            return s1.getDisplayName().getString().compareTo(s2.getDisplayName().getString());
        }
    }
}
