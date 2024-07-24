package geni.witherutils.base.common.init;

import geni.witherutils.api.lib.Names;
import geni.witherutils.base.common.base.CreativeTabVariants;
import geni.witherutils.base.common.block.LogicalBlocks;
import geni.witherutils.base.common.block.cutter.CutterBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
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
                    output.accept(new ItemStack(WUTItems.WORM.get()));
                    output.accept(new ItemStack(WUTItems.SOULORB.get()));
                    output.accept(new ItemStack(WUTItems.WAND.get()));
                    output.accept(new ItemStack(WUTItems.WRENCH.get()));
                    output.accept(new ItemStack(WUTItems.SHIELDBASIC.get()));
                    output.accept(new ItemStack(WUTItems.CUTTER.get()));
                    
                    WUTItems.ITEM_TYPES.getEntries().forEach(allitems -> {
                    	if(allitems.get() instanceof CreativeTabVariants variant)
                    	{
                    		variant.addAllVariants(output);
                    	}
                    });
                    
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
                	output.accept(new ItemStack(LogicalBlocks.ANVIL.get()));
                	output.accept(new ItemStack(LogicalBlocks.CREATIVEENERGY.get()));
                	output.accept(new ItemStack(WUTBlocks.ANGEL.get()));
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
                	output.accept(new ItemStack(WUTBlocks.CASE.get()));
                    output.accept(new ItemStack(WUTBlocks.DOOR_CASED.get()));
                    output.accept(new ItemStack(WUTBlocks.DOOR_CREEP.get()));
                    output.accept(new ItemStack(WUTBlocks.DOOR_LIRON.get()));
                    output.accept(new ItemStack(WUTBlocks.DOOR_STEEL.get()));
                    output.accept(new ItemStack(WUTBlocks.DOOR_STRIP.get()));
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
}
