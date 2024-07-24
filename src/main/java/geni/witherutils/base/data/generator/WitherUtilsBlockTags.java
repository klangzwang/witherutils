package geni.witherutils.base.data.generator;

import java.util.concurrent.CompletableFuture;

import geni.witherutils.api.lib.Names;
import geni.witherutils.base.common.block.LogicalBlocks;
import geni.witherutils.base.common.block.cutter.CutterBlock;
import geni.witherutils.base.common.init.WUTBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class WitherUtilsBlockTags extends BlockTagsProvider {

    public WitherUtilsBlockTags(PackOutput generator, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper helper)
    {
        super(generator, lookupProvider, Names.MODID, helper);
    }

    public static void setup()
    {
    }
    
	@Override
    protected void addTags(Provider p_256380_)
    {
    	for (CutterBlock cutterBlock : WUTBlocks.CUTTERBLOCKS)
        {
    		final String blockNameId = BuiltInRegistries.BLOCK.getKey(cutterBlock).getPath();
    		if(blockNameId.contains("dirt"))
    		{
                tag(BlockTags.MINEABLE_WITH_SHOVEL)
        				.add(cutterBlock)
        				;
    		}
    		else
    		{
                tag(BlockTags.MINEABLE_WITH_PICKAXE)
        				.add(cutterBlock)
        				;
    		}
        }
		
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
				.add(LogicalBlocks.ANVIL.get())
				.add(LogicalBlocks.CREATIVEENERGY.get())
        		.add(WUTBlocks.ANGEL.get())
        		.add(WUTBlocks.CASE.get())
        		.add(WUTBlocks.CTM_METAL_0.get())
                .add(WUTBlocks.DOOR_CASED.get())
                .add(WUTBlocks.DOOR_CREEP.get())
                .add(WUTBlocks.DOOR_LIRON.get())
                .add(WUTBlocks.DOOR_STEEL.get())
                .add(WUTBlocks.DOOR_STRIP.get())
        		;
    }
    
    @Override
    public String getName()
    {
        return "WitherUtils BlockTags";
    }
}