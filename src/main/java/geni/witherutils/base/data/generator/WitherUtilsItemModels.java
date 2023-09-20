package geni.witherutils.base.data.generator;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class WitherUtilsItemModels extends ItemModelProvider {
    
    public WitherUtilsItemModels(PackOutput generator, ExistingFileHelper existingFileHelper)
    {
        super(generator, WitherUtils.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels()
    {
        // Block Items
        withExistingParent(WUTBlocks.ALLOY_FURNACE_BI.getId().getPath(), modLoc("block/alloy_furnace"));
        withExistingParent(WUTBlocks.ELECTRO_FURNACE_BI.getId().getPath(), modLoc("block/electro_furnace"));
        withExistingParent(WUTBlocks.ANVIL.getId().getPath(), modLoc("block/anvil"));
        withExistingParent(WUTBlocks.LAVA_GENERATOR.getId().getPath(), modLoc("block/lava_generator_on"));
        withExistingParent(WUTBlocks.CREATIVE_GENERATOR.getId().getPath(), modLoc("block/creative_generator"));
        withExistingParent(WUTBlocks.CREATIVE_TRASH.getId().getPath(), modLoc("block/creative_trash"));
        withExistingParent(WUTBlocks.CORE.getId().getPath(), modLoc("block/core"));
        withExistingParent(WUTBlocks.PYLON.getId().getPath(), modLoc("block/pylon"));
        withExistingParent(WUTBlocks.STAB.getId().getPath(), modLoc("block/stab_on"));
        withExistingParent(WUTBlocks.ANGEL.getId().getPath(), modLoc("block/angel"));
        withExistingParent(WUTBlocks.SMARTTV.getId().getPath(), modLoc("block/smarttv/smarttv_unmounted"));
        withExistingParent(WUTBlocks.MINERBASIC.getId().getPath(), modLoc("block/miner_basic"));
        withExistingParent(WUTBlocks.MINERADV.getId().getPath(), modLoc("block/miner_adv"));
        withExistingParent(WUTBlocks.FLOORSENSOR.getId().getPath(), modLoc("block/sensor/floor/floorsensor"));
        withExistingParent(WUTBlocks.WALLSENSOR.getId().getPath(), modLoc("block/sensor/wall/wallsensor"));
        withExistingParent(WUTBlocks.LINES.getId().getPath(), modLoc("block/lines"));
//        withExistingParent(WUTBlocks.XPWIRELESS.getId().getPath(), modLoc("block/xpwireless"));
//        withExistingParent(WUTBlocks.XPPLATE.getId().getPath(), modLoc("block/xpplate"));
        withExistingParent(WUTBlocks.BRICKSDARK.getId().getPath(), modLoc("block/bricks_dark"));
        withExistingParent(WUTBlocks.BRICKSLAVA.getId().getPath(), modLoc("block/bricks_lava_on"));
        withExistingParent(WUTBlocks.ACTIVATOR.getId().getPath(), modLoc("block/activator/activator_on"));
        withExistingParent(WUTBlocks.CLICKER.getId().getPath(), modLoc("block/clicker/clicker_on"));
        withExistingParent(WUTBlocks.PLACER.getId().getPath(), modLoc("block/placer/placer_on"));
        withExistingParent(WUTBlocks.SCANNER.getId().getPath(), modLoc("block/scanner/scanner_on"));
        withExistingParent(WUTBlocks.SPAWNER.getId().getPath(), modLoc("block/spawner/spawner"));
        withExistingParent(WUTBlocks.FARMER.getId().getPath(), modLoc("block/farmer/farmer"));
        withExistingParent(WUTBlocks.FISHER.getId().getPath(), modLoc("block/fisher/fisher_master"));
        withExistingParent(WUTBlocks.CAULDRON.getId().getPath(), modLoc("block/cauldron/cauldron_on"));
        withExistingParent(WUTBlocks.WITHEREARTH.getId().getPath(), modLoc("block/witherearth"));
        withExistingParent(WUTBlocks.TOTEM.getId().getPath(), modLoc("block/totem"));
        withExistingParent(WUTBlocks.FLOODGATE.getId().getPath(), modLoc("block/floodgate"));
        withExistingParent(WUTBlocks.RACK_CASE.getId().getPath(), modLoc("block/rack/casing/case_none"));
        withExistingParent(WUTBlocks.RACK_TERMINAL.getId().getPath(), modLoc("block/rack/terminal/terminal"));
        withExistingParent(WUTBlocks.RACKITEM_CONTROLLER.getId().getPath(), modLoc("block/rack/controller/controller_on"));
        withExistingParent(WUTBlocks.RACKFLUID_CONTROLLER.getId().getPath(), modLoc("block/rack/controller/controller_on"));

        withExistingParent(WUTBlocks.ROTTEN_LOG.getId().getPath(), modLoc("block/rotten/rotten_log"));
        withExistingParent(WUTBlocks.ROTTEN_LEAVES.getId().getPath(), modLoc("block/rotten/rotten_leaves"));
        withExistingParent(WUTBlocks.ROTTEN_SAPLING.getId().getPath(), modLoc("block/rotten/rotten_sapling"));
        
        withExistingParent(WUTItems.EGG_CURSEDCREEPER.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(WUTItems.EGG_CURSEDSKELETON.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(WUTItems.EGG_CURSEDSPIDER.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(WUTItems.EGG_CURSEDZOMBIE.getId().getPath(), mcLoc("item/template_spawn_egg"));
        
        // CTM
        withExistingParent(WUTBlocks.CTM_CONCRETE_A.getId().getPath(), modLoc("block/ctm/concrete_a"));
        withExistingParent(WUTBlocks.CTM_CONCRETE_B.getId().getPath(), modLoc("block/ctm/concrete_b"));
        withExistingParent(WUTBlocks.CTM_CONCRETE_C.getId().getPath(), modLoc("block/ctm/concrete_c"));
        withExistingParent(WUTBlocks.CTM_METAL_A.getId().getPath(), modLoc("block/ctm/metal_a"));
        withExistingParent(WUTBlocks.CTM_STONE_A.getId().getPath(), modLoc("block/ctm/stone_a"));
        withExistingParent(WUTBlocks.CTM_GLASS_A.getId().getPath(), modLoc("block/ctm/glass_a"));
        withExistingParent(WUTBlocks.CTM_GLASS_B.getId().getPath(), modLoc("block/ctm/glass_b"));
        withExistingParent(WUTBlocks.CTM_GLASS_C.getId().getPath(), modLoc("block/ctm/glass_c"));
        
        // Block Items Material
        withExistingParent(WUTBlocks.WITHERSTEEL_BLOCK_BI.getId().getPath(), modLoc("block/withersteel_block"));
        withExistingParent(WUTBlocks.SOULISHED_BLOCK_BI.getId().getPath(), modLoc("block/soulished_block"));
        withExistingParent(WUTBlocks.CASE_BIG.getId().getPath(), modLoc("block/case_big"));
        withExistingParent(WUTBlocks.CASE_SMALL.getId().getPath(), modLoc("block/case_small"));
        
        singleTexture(WUTItems.IRON_ROD.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/iron_rod"));
        singleTexture(WUTItems.IRON_PLATE.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/iron_plate"));
        singleTexture(WUTItems.IRON_GEAR.getId().getPath() + "_helper", mcLoc("item/generated"), "layer0", modLoc("item/iron_gear"));
            withExistingParent(WUTItems.IRON_GEAR.getId().getPath(), modLoc("item/witherentity"));

        singleTexture(WUTItems.SPIRAL.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/spiral"));
        
        singleTexture(WUTItems.STEELARMOR_BOOTS.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/armor/steelarmor_boots"));
        singleTexture(WUTItems.STEELARMOR_CHEST.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/armor/steelarmor_chest"));
        singleTexture(WUTItems.STEELARMOR_HELMET.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/armor/steelarmor_helmet"));
        singleTexture(WUTItems.STEELARMOR_LEGGINGS.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/armor/steelarmor_leggings"));

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
        
        // CardPlayerId
        ItemModelBuilder card = withExistingParent(WUTItems.CARD.getId().getPath(), mcLoc("item/generated"))
        .texture("layer0", "witherutils:item/card_unbound");
        ModelFile cardBound = withExistingParent(WUTItems.CARD.getId().getPath() + "_bound", mcLoc("item/generated"))
        .texture("layer0", "witherutils:item/card_bound");
        card
	        .override()
	        .predicate(WitherUtils.loc("bound"), 1.0f)
	        .model(cardBound)
	        .end();

        // Wrench Item
        ItemModelBuilder modelNormal = withExistingParent(WUTItems.WRENCH.getId().getPath(), mcLoc("item/generated"))
        .texture("layer0", "witherutils:item/wrench");
        ModelFile modelUsing = withExistingParent(WUTItems.WRENCH.getId().getPath() + "_using", mcLoc("item/generated"))
        .texture("layer0", "witherutils:item/wrench_using");
        modelNormal
            .override()
            .predicate(WitherUtils.loc("using"), 0.0f)
            .model(modelNormal)
            .end()
            .override()
            .predicate(WitherUtils.loc("using"), 1.0f)
            .model(modelUsing)
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
    }
}
