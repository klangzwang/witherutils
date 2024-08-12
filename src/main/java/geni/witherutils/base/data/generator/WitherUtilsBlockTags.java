package geni.witherutils.base.data.generator;

import java.util.concurrent.CompletableFuture;

import geni.witherutils.api.lib.Names;
import geni.witherutils.base.common.block.deco.cutter.CutterBlock;
import geni.witherutils.base.common.init.WUTBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class WitherUtilsBlockTags extends BlockTagsProvider {

    public WitherUtilsBlockTags(DataGenerator generator, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper)
    {
        super(generator.getPackOutput(), lookupProvider, Names.MODID, existingFileHelper);
    }
    
	@Override
    protected void addTags(Provider p_256380_)
    {
        WUTBlocks.BLOCK_TYPES.getEntries().forEach(ro -> {
            Block block = ro.get();
            if (!(block instanceof LiquidBlock) && !(block instanceof AirBlock) && !(block instanceof CutterBlock))
            {
                tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block);
                tag(BlockTags.NEEDS_IRON_TOOL).add(block);
            }
        });

    	for (CutterBlock cutterBlock : WUTBlocks.CUTTERBLOCKS)
        {
    		final String blockNameId = BuiltInRegistries.BLOCK.getKey(cutterBlock).getPath();
    		if(blockNameId.contains("dirt"))
    		{
                tag(BlockTags.MINEABLE_WITH_SHOVEL).add(cutterBlock);
                tag(BlockTags.NEEDS_IRON_TOOL).add(cutterBlock);
    		}
    		else
    		{
                tag(BlockTags.MINEABLE_WITH_PICKAXE).add(cutterBlock);
                tag(BlockTags.NEEDS_IRON_TOOL).add(cutterBlock);
    		}
        }
    }
    
    @Override
    public String getName()
    {
        return "WitherUtils BlockTags";
    }
}