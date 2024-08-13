package geni.witherutils.base.data.generator;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.api.lib.Names;
import geni.witherutils.api.upgrade.IUpgradeItem;
import geni.witherutils.base.common.block.deco.cutter.CutterBlock;
import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class WitherUtilsItemModels extends ItemModelProvider {
    
    public WitherUtilsItemModels(PackOutput generator, ExistingFileHelper existingFileHelper)
    {
        super(generator, Names.MODID, existingFileHelper);
    }

	@SuppressWarnings("deprecation")
	@Override
    protected void registerModels()
    {
		for(var entry : BuiltInRegistries.ITEM.entrySet())
		{
			var item = entry.getValue();
			if(item instanceof IUpgradeItem)
			{
		        singleTexture(item.builtInRegistryHolder().getRegisteredName(), mcLoc("item/generated"), "layer0", modLoc("item/iron_plate"));
			}
		}
        
        // CutterBlock Item
        for (CutterBlock cutterBlock : WUTBlocks.CUTTERBLOCKS)
        {
    		final String cutterBlockName = BuiltInRegistries.BLOCK.getKey(cutterBlock).getPath();
    		withExistingParent(cutterBlockName, modLoc("block/ctm/" + cutterBlockName));
        }
		
    	// Bucket Items
    	singleTexture("bluelimbo_bucket", mcLoc("item/generated"), "layer0", modLoc("item/fluid/bluelimbo_bucket"));
    	singleTexture("coldslush_bucket", mcLoc("item/generated"), "layer0", modLoc("item/fluid/coldslush_bucket"));
    	singleTexture("experience_bucket", mcLoc("item/generated"), "layer0", modLoc("item/fluid/experience_bucket"));
    	singleTexture("fertilizer_bucket", mcLoc("item/generated"), "layer0", modLoc("item/fluid/fertilizer_bucket"));
    	singleTexture("portium_bucket", mcLoc("item/generated"), "layer0", modLoc("item/fluid/portium_bucket"));
    	singleTexture("redresin_bucket", mcLoc("item/generated"), "layer0", modLoc("item/fluid/redresin_bucket"));
    	singleTexture("soulful_bucket", mcLoc("item/generated"), "layer0", modLoc("item/fluid/soulful_bucket"));
    	singleTexture("witherwater_bucket", mcLoc("item/generated"), "layer0", modLoc("item/fluid/witherwater_bucket"));
    	
        // Block Items
        withExistingParent(WUTBlocks.ANVIL.getId().getPath(), modLoc("block/anvil/anvil"));
        withExistingParent(WUTBlocks.LAVA_GENERATOR.getId().getPath(), modLoc("block/generator/lava/lava_generator_on"));
        withExistingParent(WUTBlocks.WIND_GENERATOR.getId().getPath(), modLoc("block/generator/wind/wind_generator_on"));
        withExistingParent(WUTBlocks.WATER_GENERATOR.getId().getPath(), modLoc("block/generator/water/water_generator"));
        withExistingParent(WUTBlocks.CREATIVE_GENERATOR.getId().getPath(), modLoc("block/generator/creative/creative_generator"));
//        withExistingParent(WUTBlocks.CREATIVE_TRASH.getId().getPath(), modLoc("block/creative_trash"));
//        withExistingParent(WUTBlocks.CREATIVE_EXPLOSION.getId().getPath(), modLoc("block/creative_trash"));
//        withExistingParent(WUTBlocks.CORE.getId().getPath(), modLoc("block/core"));
//        withExistingParent(WUTBlocks.PYLON.getId().getPath(), modLoc("block/pylon"));
//        withExistingParent(WUTBlocks.STAB.getId().getPath(), modLoc("block/stab_on"));
        withExistingParent(WUTBlocks.ANGEL.getId().getPath(), modLoc("block/angel"));
        withExistingParent(WUTBlocks.SMARTTV.getId().getPath(), modLoc("block/smarttv/smarttv_unmounted"));
        withExistingParent(WUTBlocks.FLOORSENSOR.getId().getPath(), modLoc("block/sensor/floor/floorsensor"));
        withExistingParent(WUTBlocks.WALLSENSOR.getId().getPath(), modLoc("block/sensor/wall/wallsensor"));
        withExistingParent(WUTBlocks.LINES.getId().getPath(), modLoc("block/lines"));
        withExistingParent(WUTBlocks.XPDRAIN.getId().getPath(), modLoc("block/xpdrain/xpdrain_on"));
        withExistingParent(WUTBlocks.BRICKSDARK.getId().getPath(), modLoc("block/bricks_dark"));
        withExistingParent(WUTBlocks.BRICKSLAVA.getId().getPath(), modLoc("block/bricks_lava_on"));
//        withExistingParent(WUTBlocks.SPAWNER.getId().getPath(), modLoc("block/spawner/spawner"));
//        withExistingParent(WUTBlocks.FARMER.getId().getPath(), modLoc("block/farmer/farmer"));
//        withExistingParent(WUTBlocks.FISHER.getId().getPath(), modLoc("block/fisher/fisher_master"));
        withExistingParent(WUTBlocks.CAULDRON.getId().getPath(), modLoc("block/cauldron/cauldron_on"));

        withExistingParent(WUTBlocks.TOTEM.getId().getPath(), modLoc("block/totem/totem"));
//        withExistingParent(WUTBlocks.FLOODGATE.getId().getPath(), modLoc("block/floodgate"));
//        withExistingParent(WUTBlocks.RACK_CASE.getId().getPath(), modLoc("block/rack/casing/case_none"));
//        withExistingParent(WUTBlocks.RACK_TERMINAL.getId().getPath(), modLoc("block/rack/terminal/terminal"));
//        withExistingParent(WUTBlocks.RACKITEM_CONTROLLER.getId().getPath(), modLoc("block/rack/controller/controller_on"));
//        withExistingParent(WUTBlocks.RACKFLUID_CONTROLLER.getId().getPath(), modLoc("block/rack/controller/controller_on"));
        
        withExistingParent(WUTBlocks.FAKE_DRIVER.getId().getPath(), modLoc("block/fakedriver/fake_driver"));
        
        withExistingParent(WUTBlocks.ROTTEN_LOG.getId().getPath(), modLoc("block/rotten/rotten_log"));
        withExistingParent(WUTBlocks.ROTTEN_LEAVES.getId().getPath(), modLoc("block/rotten/rotten_leaves"));
        withExistingParent(WUTBlocks.ROTTEN_SAPLING.getId().getPath(), modLoc("block/rotten/rotten_sapling"));
        withExistingParent(WUTBlocks.ROTTEN_EARTH.getId().getPath(), modLoc("block/rotten/rotten_earth"));
        withExistingParent(WUTBlocks.ROTTEN_ROOTS.getId().getPath(), modLoc("block/rotten/rotten_roots"));
        withExistingParent(WUTBlocks.ROTTEN_ROOTS_POT.getId().getPath(), modLoc("block/rotten/rotten_roots_pot"));
        
        withExistingParent(WUTItems.EGG_CURSEDZOMBIE.getId().getPath(), mcLoc("item/template_spawn_egg"));
//        withExistingParent(WUTItems.EGG_CURSEDCREEPER.getId().getPath(), mcLoc("item/template_spawn_egg"));
//        withExistingParent(WUTItems.EGG_CURSEDSKELETON.getId().getPath(), mcLoc("item/template_spawn_egg"));
//        withExistingParent(WUTItems.EGG_CURSEDSPIDER.getId().getPath(), mcLoc("item/template_spawn_egg"));
//        withExistingParent(WUTItems.EGG_CURSEDDRYHEAD.getId().getPath(), mcLoc("item/template_spawn_egg"));
        
        withExistingParent(WUTBlocks.FAN0.getId().getPath(), modLoc("block/fan/fan0"));
        withExistingParent(WUTBlocks.FAN1.getId().getPath(), modLoc("block/fan/fan1"));
        withExistingParent(WUTBlocks.FAN2.getId().getPath(), modLoc("block/fan/fan2"));
        withExistingParent(WUTBlocks.FAN3.getId().getPath(), modLoc("block/fan/fan3"));
        
        // Block Items Material
        withExistingParent(WUTBlocks.WITHERSTEEL_BLOCK.getId().getPath(), modLoc("block/withersteel_block"));
        withExistingParent(WUTBlocks.CTM_METAL_0.getId().getPath(), modLoc("block/metal0"));
        withExistingParent(WUTBlocks.SOULISHED_BLOCK.getId().getPath(), modLoc("block/soulished_block"));
        withExistingParent(WUTBlocks.CASE_BIG.getId().getPath(), modLoc("block/case/case_big"));
        withExistingParent(WUTBlocks.CASE_SMALL.getId().getPath(), modLoc("block/case/case_small"));
        
        singleTexture(WUTBlocks.LIGHT.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/light"));
        
        singleTexture(WUTItems.IRON_ROD.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/iron_rod"));
        singleTexture(WUTItems.IRON_PLATE.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/iron_plate"));
        singleTexture(WUTItems.IRON_GEAR.getId().getPath() + "_helper", mcLoc("item/generated"), "layer0", modLoc("item/iron_gear"));
            withExistingParent(WUTItems.IRON_GEAR.getId().getPath(), modLoc("item/witherentity"));

        singleTexture(WUTItems.ADCASE.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("block/processor/adapter_base"));
        singleTexture("fakejob_activating", mcLoc("item/generated"), "layer0", modLoc("block/processor/adapter_base"));
        singleTexture("fakejob_clicking", mcLoc("item/generated"), "layer0", modLoc("block/processor/adapter_base"));
        singleTexture("fakejob_mining", mcLoc("item/generated"), "layer0", modLoc("block/processor/adapter_base"));
        singleTexture("fakejob_placing", mcLoc("item/generated"), "layer0", modLoc("block/processor/adapter_base"));
        singleTexture("fakejob_scanning", mcLoc("item/generated"), "layer0", modLoc("block/processor/adapter_base"));
        
        singleTexture(WUTItems.SPIRAL.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/spiral"));

//        singleTexture(WUTItems.STEELARMOR_BOOTS.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/armor/steelarmor_boots"));
//        singleTexture(WUTItems.STEELARMOR_CHEST.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/armor/steelarmor_chest"));
//        singleTexture(WUTItems.STEELARMOR_HELMET.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/armor/steelarmor_helmet"));
//        singleTexture(WUTItems.STEELARMOR_LEGGINGS.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/armor/steelarmor_leggings"));

        singleTexture(WUTItems.UPCASE.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/upgrade/upgrade_case"));
        singleTexture("upgrade_feather", mcLoc("item/generated"), "layer0", modLoc("item/upgrade/upgrade_feather"));
        singleTexture("upgrade_jump", mcLoc("item/generated"), "layer0", modLoc("item/upgrade/upgrade_jump"));
        singleTexture("upgrade_speed", mcLoc("item/generated"), "layer0", modLoc("item/upgrade/upgrade_speed"));
        singleTexture("upgrade_squid", mcLoc("item/generated"), "layer0", modLoc("item/upgrade/upgrade_squid"));
        singleTexture(WUTItems.FEATHER.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/blackfeather"));

        singleTexture("upgrade_vision", mcLoc("item/generated"), "layer0", modLoc("item/upgrade/upgrade_vision"));
        
        singleTexture(WUTItems.WITHERSTEEL_INGOT.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/withersteel_ingot"));
        singleTexture(WUTItems.WITHERSTEEL_NUGGET.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/withersteel_nugget"));
        singleTexture(WUTItems.WITHERSTEEL_PLATE.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/withersteel_plate"));
        singleTexture(WUTItems.WITHERSTEEL_GEAR.getId().getPath() + "_helper", mcLoc("item/generated"), "layer0", modLoc("item/withersteel_gear"));
            withExistingParent(WUTItems.WITHERSTEEL_GEAR.getId().getPath(), modLoc("item/witherentity"));

        singleTexture(WUTItems.SOULISHED_INGOT.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/soulished_ingot"));
        singleTexture(WUTItems.SOULISHED_NUGGET.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/soulished_nugget"));
        
        singleTexture(WUTItems.BLINK_PLATE.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/blink_plate"));
        singleTexture(WUTItems.ANCHOR.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/anchor"));
        
        // Item items
        singleTexture(WUTItems.TABWU.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/tabwu"));
        singleTexture(WUTItems.SOULORB.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/soulorb"));
        singleTexture(WUTItems.WORM.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/worm"));
        singleTexture(WUTItems.CUTTER.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/cutter"));
        singleTexture(WUTBlocks.LILLY.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/lilly"));
        singleTexture(WUTItems.ENDERPSHARD.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/enderpearl_shard"));

        // Wrench Item
        ItemModelBuilder wrenchNormal = withExistingParent(WUTItems.WRENCH.getId().getPath(), mcLoc("item/generated"))
        .texture("layer0", "witherutils:item/wrench");
        ItemModelBuilder wrenchUsing = withExistingParent(WUTItems.WRENCH.getId().getPath() + "_using", mcLoc("item/generated"))
        .texture("layer0", "witherutils:item/wrench_using");
        
        wrenchNormal
            .override()
            .predicate(WitherUtilsRegistry.loc("using"), 0.0f)
            .model(wrenchNormal)
            .end()
            .override()
            .predicate(WitherUtilsRegistry.loc("using"), 1.0f)
            .model(wrenchUsing)
            .end();

        // Scaper Item
        ItemModelBuilder scaperNormal = withExistingParent(WUTItems.SCAPER.getId().getPath(), mcLoc("item/generated"))
        .texture("layer0", "witherutils:item/scaper");
        ItemModelBuilder scaperHalf = withExistingParent(WUTItems.SCAPER.getId().getPath() + "_half", mcLoc("item/generated"))
        .texture("layer0", "witherutils:item/scaper_half");
        ItemModelBuilder scaperFull = withExistingParent(WUTItems.SCAPER.getId().getPath() + "_full", mcLoc("item/generated"))
        .texture("layer0", "witherutils:item/scaper_full");
        
        scaperNormal
            .override()
            .predicate(WitherUtilsRegistry.loc("canplace"), 0.0f)
            .model(scaperNormal)
            .end()
            .override()
            .predicate(WitherUtilsRegistry.loc("canplace"), 0.5f)
            .model(scaperHalf)
            .end()
            .override()
            .predicate(WitherUtilsRegistry.loc("canplace"), 1.0f)
            .model(scaperFull)
            .end();

        // Item SoulBank
        singleTexture(WUTItems.SOULBANK_CASE.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/soulbank/soulbank"));
        withExistingParent(WUTItems.SOULBANK_BASIC.getId().getPath(), mcLoc("item/generated"))
                .texture("layer0", "witherutils:item/soulbank/soulbank")
                .texture("layer1", "witherutils:item/soulbank/soulbank_basic");
        withExistingParent(WUTItems.SOULBANK_ADVANCED.getId().getPath(), mcLoc("item/generated"))
                .texture("layer0", "witherutils:item/soulbank/soulbank")
                .texture("layer1", "witherutils:item/soulbank/soulbank_adv");
        withExistingParent(WUTItems.SOULBANK_ULTRA.getId().getPath(), mcLoc("item/generated"))
                .texture("layer0", "witherutils:item/soulbank/soulbank")
                .texture("layer1", "witherutils:item/soulbank/soulbank_ultra");

        singleTexture(WUTItems.IRON_PLATE.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/iron_plate"));
        singleTexture(WUTItems.IRON_GEAR.getId().getPath() + "_helper", mcLoc("item/generated"), "layer0", modLoc("item/iron_gear"));
            withExistingParent(WUTItems.IRON_GEAR.getId().getPath(), modLoc("item/witherentity"));

        singleTexture(WUTItems.FEATHER.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/blackfeather"));
        
        withExistingParent(WUTBlocks.SOULFIRE.getId().getPath(), modLoc("item/empty"));
        
        // Card Item
        ItemModelBuilder card = withExistingParent(WUTItems.CARD.getId().getPath(), mcLoc("item/generated"))
        .texture("layer0", "witherutils:item/card_unbound");
        ItemModelBuilder cardBound = withExistingParent(WUTItems.CARD.getId().getPath() + "_bound", mcLoc("item/generated"))
        .texture("layer0", "witherutils:item/card_bound");
        card
	        .override()
	        .predicate(WitherUtilsRegistry.loc("bound"), 1.0f)
	        .model(cardBound)
	        .end();
        
        // Sword Item
        ItemModelBuilder swordModel = withExistingParent(WUTItems.SWORD.getId().getPath(), modLoc("item/withertools"))
        .texture("layer0", "witherutils:item/sword");
        ItemModelBuilder swordPoweredModel = withExistingParent(WUTItems.SWORD.getId().getPath() + "_powered", modLoc("item/withertools"))
        .texture("layer0", "witherutils:item/sword_powered");
        ItemModelBuilder swordSwingingModel = withExistingParent(WUTItems.SWORD.getId().getPath() + "_swinging", modLoc("item/withertools"))
        .texture("layer0", "witherutils:item/sword_swinging");

        swordModel
            .override()
            .predicate(WitherUtilsRegistry.loc("powered"), 0.0f)
            .predicate(WitherUtilsRegistry.loc("swinging"), 0.0f)
            .model(swordModel)
            .end()
            .override()
            .predicate(WitherUtilsRegistry.loc("powered"), 0.0f)
            .predicate(WitherUtilsRegistry.loc("swinging"), 1.0f)
            .model(swordModel)
            .end()
            .override()
            .predicate(WitherUtilsRegistry.loc("powered"), 1.0f)
            .predicate(WitherUtilsRegistry.loc("swinging"), 0.0f)
            .model(swordPoweredModel)
            .end()
            .override()
            .predicate(WitherUtilsRegistry.loc("powered"), 1.0f)
            .predicate(WitherUtilsRegistry.loc("swinging"), 1.0f)
            .model(swordSwingingModel)
            .end();
        
        // Pickaxe Item
        ItemModelBuilder pickaxeModel = withExistingParent(WUTItems.PICKAXEHEAD.getId().getPath(), mcLoc("item/generated"))
        .texture("layer0", "witherutils:item/pickaxe_head");
        ItemModelBuilder pickaxeAdvModel = withExistingParent(WUTItems.PICKAXEHEAD.getId().getPath() + "_advanced", mcLoc("item/generated"))
        .texture("layer0", "witherutils:item/pickaxe_head_advanced");
        ItemModelBuilder pickaxeUltraModel = withExistingParent(WUTItems.PICKAXEHEAD.getId().getPath() + "_ultra", mcLoc("item/generated"))
        .texture("layer0", "witherutils:item/pickaxe_head_ultra");

        pickaxeModel
            .override()
            .predicate(WitherUtilsRegistry.loc("chance"), 0.5f)
            .model(pickaxeModel)
            .end()
            .override()
            .predicate(WitherUtilsRegistry.loc("chance"), 0.8f)
            .model(pickaxeAdvModel)
            .end()
            .override()
            .predicate(WitherUtilsRegistry.loc("chance"), 1.0f)
            .model(pickaxeUltraModel)
            .end();
    }
}
