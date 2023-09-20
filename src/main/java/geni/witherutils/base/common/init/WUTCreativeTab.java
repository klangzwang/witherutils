package geni.witherutils.base.common.init;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.client.render.item.BetaCheckItemDecorator;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class WUTCreativeTab {

    private static final DeferredRegister<CreativeModeTab> TAB_TYPES = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, WitherUtils.MODID);
    
    /*
     * 
     * BETACHECK
     * 
     */
    public static void initItems(RegisterItemDecorationsEvent event)
    {
        event.register(WUTItems.HAMMER.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.CUTTER.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.STEELARMOR_HELMET.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.STEELARMOR_CHEST.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.STEELARMOR_LEGGINGS.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.STEELARMOR_BOOTS.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.UPCASE.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTUpgrades.UPGRADEFEATHER.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTUpgrades.UPGRADEJUMP.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTUpgrades.UPGRADESPEED.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTUpgrades.UPGRADESQUID.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTUpgrades.UPGRADEVISION.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.EGG_CURSEDCREEPER.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.EGG_CURSEDSKELETON.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.EGG_CURSEDSPIDER.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.EGG_CURSEDZOMBIE.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.EXPERIENCE_BUCKET.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.FERTILIZER_BUCKET.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.SOULBANK_CASE.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.SOULBANK_BASIC.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.SOULBANK_ADVANCED.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.SOULBANK_ULTRA.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.SHIELDBASIC.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.SHIELDADV.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.SHIELDROTTEN.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.SHIELDENERGY.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.SOULORB.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.WORM.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.CARD.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.REMOTE.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTBlocks.LILLY.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.ENDERPSHARD.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTBlocks.WITHERSTEEL_BLOCK.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.WITHERSTEEL_INGOT.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.WITHERSTEEL_NUGGET.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.WITHERSTEEL_PLATE.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.WITHERSTEEL_GEAR.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTBlocks.SOULISHED_BLOCK.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.SOULISHED_INGOT.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.SOULISHED_NUGGET.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.IRON_PLATE.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.IRON_GEAR.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.IRON_ROD.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTBlocks.CASE_BIG.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTBlocks.CASE_SMALL.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.SHOVEL.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.FAN.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.BLINK_PLATE.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.SPIRAL.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTItems.ANCHOR.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTBlocks.ROTTEN_SAPLING.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTBlocks.ROTTEN_LOG.get(), BetaCheckItemDecorator.INSTANCE);
        event.register(WUTBlocks.ROTTEN_LEAVES.get(), BetaCheckItemDecorator.INSTANCE);
    }
    public static void initBlocks(RegisterItemDecorationsEvent event)
    {
    	event.register(WUTBlocks.ANVIL.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.CAULDRON.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.ANGEL.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.SOLARCASE.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.SOLARBASIC.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.SOLARADV.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.SOLARULTRA.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.GREENHOUSE.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.RESERVOIR.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.TANKDRUM.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.SMARTTV.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.FLOORSENSOR.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.WALLSENSOR.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.FARMER.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.ELECTRO_FURNACE.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.ALLOY_FURNACE.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.FISHER.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.FLOODGATE.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.LAVA_GENERATOR.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.WIND_GENERATOR.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.WATER_GENERATOR.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.COLLECTOR.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.SPAWNER.get(), BetaCheckItemDecorator.INSTANCE);
    }
    public static void initDecos(RegisterItemDecorationsEvent event)
    {
    	event.register(WUTBlocks.SLICEDCONCRETEBLACK.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.SLICEDCONCRETEGRAY.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.SLICEDCONCRETEWHITE.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.STEELPOLEHEAD.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.STEELPOLE.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.STEELRAILING.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.CASED_DOOR.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.CREEP_DOOR.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.LIRON_DOOR.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.STEEL_DOOR.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.STRIPED_DOOR.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.METALDOOR.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.CTM_CONCRETE_A.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.CTM_CONCRETE_B.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.CTM_CONCRETE_C.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.CTM_METAL_A.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.CTM_STONE_A.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.CTM_GLASS_A.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.CTM_GLASS_B.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.CTM_GLASS_C.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.BRICKSDARK.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.BRICKSLAVA.get(), BetaCheckItemDecorator.INSTANCE);
    	event.register(WUTBlocks.CATWALK.get(), BetaCheckItemDecorator.INSTANCE);
    }
    
    /*
     * 
     * TABS
     * 
     */
    public static final RegistryObject<CreativeModeTab> TABWU_ITEMS = TAB_TYPES.register("tabwu_items", () -> {
        return CreativeModeTab.builder()
                .backgroundSuffix("wither_gray.png")
                .icon(() -> new ItemStack(WUTItems.TABWU.get()))
                .displayItems((features, output) -> {
                    output.accept(new ItemStack(WUTItems.HAMMER.get()));
                    output.accept(new ItemStack(WUTItems.WRENCH.get()));
                    output.accept(new ItemStack(WUTItems.CUTTER.get()));
                    output.accept(new ItemStack(WUTItems.REMOTE.get()));
                    output.accept(new ItemStack(WUTItems.SWORD.get()));
                    output.accept(new ItemStack(WUTItems.WAND.get()));
                    output.accept(new ItemStack(WUTItems.IRON_ROD.get()));
                    
                    output.accept(new ItemStack(WUTItems.CARD.get()));
                    
                    output.accept(new ItemStack(WUTItems.SHIELDBASIC.get()));
                    output.accept(new ItemStack(WUTItems.SHIELDADV.get()));
                    output.accept(new ItemStack(WUTItems.SHIELDROTTEN.get()));
                    output.accept(new ItemStack(WUTItems.SHIELDENERGY.get()));
                    
                    output.accept(new ItemStack(WUTItems.WITHERSTEEL_INGOT.get()));
                    output.accept(new ItemStack(WUTItems.SOULISHED_INGOT.get()));
                    output.accept(new ItemStack(WUTItems.WITHERSTEEL_NUGGET.get()));
                    output.accept(new ItemStack(WUTItems.SOULISHED_NUGGET.get()));
                    
                    output.accept(new ItemStack(WUTItems.SOULORB.get()));
                    
                    output.accept(new ItemStack(WUTItems.WITHERSTEEL_GEAR.get()));
                    output.accept(new ItemStack(WUTItems.IRON_GEAR.get()));
   
                    output.accept(new ItemStack(WUTItems.FAN.get()));
                    output.accept(new ItemStack(WUTItems.SHOVEL.get()));

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
                    output.accept(new ItemStack(WUTBlocks.WITHEREARTH.get()));
                    output.accept(new ItemStack(WUTBlocks.SOULISHED_BLOCK.get()));
                    output.accept(new ItemStack(WUTBlocks.CASE_BIG.get()));
                    output.accept(new ItemStack(WUTBlocks.CASE_SMALL.get()));
                    
                    output.accept(new ItemStack(WUTItems.STEELARMOR_HELMET.get()));
                    output.accept(new ItemStack(WUTItems.STEELARMOR_CHEST.get()));
                    output.accept(new ItemStack(WUTItems.STEELARMOR_LEGGINGS.get()));
                    output.accept(new ItemStack(WUTItems.STEELARMOR_BOOTS.get()));

                    output.accept(new ItemStack(WUTItems.UPCASE.get()));
                    output.accept(new ItemStack(WUTUpgrades.UPGRADEFEATHER.get()));
                    output.accept(new ItemStack(WUTUpgrades.UPGRADEJUMP.get()));
                    output.accept(new ItemStack(WUTUpgrades.UPGRADESPEED.get()));
                    output.accept(new ItemStack(WUTUpgrades.UPGRADESQUID.get()));
                    output.accept(new ItemStack(WUTUpgrades.UPGRADEVISION.get()));

                    output.accept(new ItemStack(WUTItems.EGG_CURSEDCREEPER.get()));
                    output.accept(new ItemStack(WUTItems.EGG_CURSEDSKELETON.get()));
                    output.accept(new ItemStack(WUTItems.EGG_CURSEDSPIDER.get()));
                    output.accept(new ItemStack(WUTItems.EGG_CURSEDZOMBIE.get()));
                    
                    output.accept(new ItemStack(WUTItems.WORM.get()));
                    output.accept(new ItemStack(WUTBlocks.ROTTEN_SAPLING.get()));
                    output.accept(new ItemStack(WUTBlocks.LILLY.get()));
                    output.accept(new ItemStack(WUTItems.ENDERPSHARD.get()));
                    
                    output.accept(new ItemStack(WUTItems.EXPERIENCE_BUCKET.get()));
                    output.accept(new ItemStack(WUTItems.FERTILIZER_BUCKET.get()));
                })
                .title(Component.translatable("itemGroup.witherutils.items"))
                .build();
    });
    
    public static final RegistryObject<CreativeModeTab> TABWU_BLOCKS = TAB_TYPES.register("tabwu_blocks", () -> {
        return CreativeModeTab.builder()
                .backgroundSuffix("wither_gray.png")
                .icon(() -> new ItemStack(WUTItems.TABWU.get()))
                .displayItems((features, output) -> {
                	output.accept(new ItemStack(WUTBlocks.ANVIL.get()));
                	output.accept(new ItemStack(WUTBlocks.CAULDRON.get()));
                    output.accept(new ItemStack(WUTBlocks.ANGEL.get()));
                    
                    output.accept(new ItemStack(WUTBlocks.SOLARCASE.get()));
                    output.accept(new ItemStack(WUTBlocks.SOLARBASIC.get()));
                    output.accept(new ItemStack(WUTBlocks.SOLARADV.get()));
                    output.accept(new ItemStack(WUTBlocks.SOLARULTRA.get()));
                    
                    output.accept(new ItemStack(WUTBlocks.GREENHOUSE.get()));
                    output.accept(new ItemStack(WUTBlocks.RESERVOIR.get()));
                    output.accept(new ItemStack(WUTBlocks.TANKDRUM.get()));
 
                    output.accept(new ItemStack(WUTBlocks.SMARTTV.get()));
                    output.accept(new ItemStack(WUTBlocks.FLOORSENSOR.get()));
                    output.accept(new ItemStack(WUTBlocks.WALLSENSOR.get()));
                    output.accept(new ItemStack(WUTBlocks.COLLECTOR.get()));
                    output.accept(new ItemStack(WUTBlocks.FARMER.get()));
                    output.accept(new ItemStack(WUTBlocks.SPAWNER.get()));
                    output.accept(new ItemStack(WUTBlocks.TOTEM.get()));
                    output.accept(new ItemStack(WUTBlocks.ACTIVATOR.get()));
                    output.accept(new ItemStack(WUTBlocks.CLICKER.get()));
                    output.accept(new ItemStack(WUTBlocks.PLACER.get()));
                    output.accept(new ItemStack(WUTBlocks.SCANNER.get()));
                    
                    output.accept(new ItemStack(WUTBlocks.ELECTRO_FURNACE.get()));
                    output.accept(new ItemStack(WUTBlocks.ALLOY_FURNACE.get()));
                    output.accept(new ItemStack(WUTBlocks.MINERBASIC.get()));
                    output.accept(new ItemStack(WUTBlocks.MINERADV.get()));
                    
                    output.accept(new ItemStack(WUTBlocks.LAVA_GENERATOR.get()));
                    output.accept(new ItemStack(WUTBlocks.WIND_GENERATOR.get()));
                    output.accept(new ItemStack(WUTBlocks.WATER_GENERATOR.get()));

                    output.accept(new ItemStack(WUTBlocks.FISHER.get()));
                    output.accept(new ItemStack(WUTBlocks.FLOODGATE.get()));
                    
                    output.accept(new ItemStack(WUTBlocks.RACK_CASE.get()));
                    output.accept(new ItemStack(WUTBlocks.RACK_TERMINAL.get()));
                    output.accept(new ItemStack(WUTBlocks.RACKITEM_CONTROLLER.get()));
                    output.accept(new ItemStack(WUTBlocks.RACKFLUID_CONTROLLER.get()));
                    
//                  output.accept(new ItemStack(WUTBlocks.XPWIRELESS.get()));
//                  output.accept(new ItemStack(WUTBlocks.XPPLATE.get()));

                    output.accept(new ItemStack(WUTBlocks.CORE.get()));
                    output.accept(new ItemStack(WUTBlocks.PYLON.get()));
                    output.accept(new ItemStack(WUTBlocks.STAB.get()));

                    output.accept(new ItemStack(WUTBlocks.CREATIVE_GENERATOR.get()));
                    output.accept(new ItemStack(WUTBlocks.CREATIVE_TRASH.get()));
                })
                .title(Component.translatable("itemGroup.witherutils.blocks"))
                .build();
    });
    
    public static final RegistryObject<CreativeModeTab> TABWU_DECOS = TAB_TYPES.register("tabwu_decos", () -> {
        return CreativeModeTab.builder()
                .backgroundSuffix("wither_gray.png")
                .icon(() -> new ItemStack(WUTItems.TABWU.get()))
                .displayItems((features, output) -> {
                	output.accept(new ItemStack(WUTBlocks.SLICEDCONCRETEBLACK.get()));
                	output.accept(new ItemStack(WUTBlocks.SLICEDCONCRETEGRAY.get()));
                	output.accept(new ItemStack(WUTBlocks.SLICEDCONCRETEWHITE.get()));
                    output.accept(new ItemStack(WUTBlocks.STEELPOLEHEAD.get()));
                    output.accept(new ItemStack(WUTBlocks.STEELPOLE.get()));
                    output.accept(new ItemStack(WUTBlocks.STEELRAILING.get()));
                    output.accept(new ItemStack(WUTBlocks.CASED_DOOR.get()));
                    output.accept(new ItemStack(WUTBlocks.CREEP_DOOR.get()));
                    output.accept(new ItemStack(WUTBlocks.LIRON_DOOR.get()));
                    output.accept(new ItemStack(WUTBlocks.STEEL_DOOR.get()));
                    output.accept(new ItemStack(WUTBlocks.STRIPED_DOOR.get()));
                    output.accept(new ItemStack(WUTBlocks.METALDOOR.get()));
                    output.accept(new ItemStack(WUTBlocks.CTM_CONCRETE_A.get()));
                    output.accept(new ItemStack(WUTBlocks.CTM_CONCRETE_B.get()));
                    output.accept(new ItemStack(WUTBlocks.CTM_CONCRETE_C.get()));
                    output.accept(new ItemStack(WUTBlocks.CTM_METAL_A.get()));
                    output.accept(new ItemStack(WUTBlocks.CTM_STONE_A.get()));
                    output.accept(new ItemStack(WUTBlocks.CTM_GLASS_A.get()));
                    output.accept(new ItemStack(WUTBlocks.CTM_GLASS_B.get()));
                    output.accept(new ItemStack(WUTBlocks.CTM_GLASS_C.get()));
                    output.accept(new ItemStack(WUTBlocks.BRICKSDARK.get()));
                    output.accept(new ItemStack(WUTBlocks.BRICKSLAVA.get()));
                	output.accept(new ItemStack(WUTBlocks.CATWALK.get()));
                })
                .title(Component.translatable("itemGroup.witherutils.decos"))
                .build();
    });

    public static void init()
    {
        TAB_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
