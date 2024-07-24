package geni.witherutils.base.data.generator;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.api.lib.Names;
import geni.witherutils.base.common.block.LogicalBlocks;
import geni.witherutils.base.common.block.cutter.CutterBlock;
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

	@Override
    protected void registerModels()
    {
        // CutterBlock Item
    	for (CutterBlock cutterBlock : WUTBlocks.CUTTERBLOCKS)
        {
    		final String cutterBlockName = BuiltInRegistries.BLOCK.getKey(cutterBlock).getPath();
    		withExistingParent(cutterBlockName, modLoc("block/ctm/" + cutterBlockName));
        }
		
        // Block Item
        withExistingParent(LogicalBlocks.ANVIL.getId().getPath(), modLoc("block/anvil"));
        withExistingParent(LogicalBlocks.CREATIVEENERGY.getId().getPath(), modLoc("block/creativeenergy"));
        
        withExistingParent(WUTBlocks.ANGEL.getId().getPath(), modLoc("block/angel"));
        withExistingParent(WUTBlocks.CASE.getId().getPath(), modLoc("block/case"));

        // Block Items Material
        withExistingParent(WUTBlocks.CTM_METAL_0.getId().getPath(), modLoc("block/metal0"));
        
        // Standard Item
        singleTexture(WUTItems.TABWU.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/tabwu"));
        singleTexture(WUTItems.WORM.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/worm"));
        singleTexture(WUTItems.SOULORB.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/soulorb"));
        singleTexture(WUTItems.CUTTER.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/cutter"));
        
        // Wrench Item
        ItemModelBuilder modelNormal = withExistingParent(WUTItems.WRENCH.getId().getPath(), mcLoc("item/generated"))
        .texture("layer0", "witherutils:item/wrench");
        ItemModelBuilder modelUsing = withExistingParent(WUTItems.WRENCH.getId().getPath() + "_using", mcLoc("item/generated"))
        .texture("layer0", "witherutils:item/wrench_using");
        
        modelNormal
            .override()
            .predicate(WitherUtilsRegistry.loc("using"), 0.0f)
            .model(modelNormal)
            .end()
            .override()
            .predicate(WitherUtilsRegistry.loc("using"), 1.0f)
            .model(modelUsing)
            .end();
    }
}
