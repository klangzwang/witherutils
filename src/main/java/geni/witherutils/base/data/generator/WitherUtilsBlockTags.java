package geni.witherutils.base.data.generator;

import java.util.concurrent.CompletableFuture;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class WitherUtilsBlockTags extends BlockTagsProvider {

	public static final TagKey<Fluid> EXPERIENCE = FluidTags.create(new ResourceLocation("forge:experience"));
	
    public WitherUtilsBlockTags(PackOutput generator, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper helper)
    {
        super(generator, lookupProvider, WitherUtils.MODID, helper);
    }

    public static void setup()
    {
    }
    
    @Override
    protected void addTags(Provider p_256380_)
    {
        tag(BlockTags.SAPLINGS)
				.add(WUTBlocks.ROTTEN_SAPLING.get())
				;
    	
        tag(BlockTags.LEAVES)
				.add(WUTBlocks.ROTTEN_LEAVES.get())
				;
        
        tag(BlockTags.MINEABLE_WITH_AXE)
        		.add(WUTBlocks.ROTTEN_LOG.get())
        		;
    	
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(WUTBlocks.ALLOY_FURNACE.get())
                .add(WUTBlocks.ELECTRO_FURNACE.get())
                .add(WUTBlocks.WITHERSTEEL_BLOCK.get())
                .add(WUTBlocks.SOULISHED_BLOCK.get())
                .add(WUTBlocks.ANVIL.get())
                .add(WUTBlocks.LAVA_GENERATOR.get())
                .add(WUTBlocks.WIND_GENERATOR.get())
                .add(WUTBlocks.WATER_GENERATOR.get())
                .add(WUTBlocks.CREATIVE_GENERATOR.get())
                .add(WUTBlocks.CREATIVE_TRASH.get())
                .add(WUTBlocks.CASE_BIG.get())
                .add(WUTBlocks.CASE_SMALL.get())
                .add(WUTBlocks.CORE.get())
                .add(WUTBlocks.PYLON.get())
                .add(WUTBlocks.STAB.get())
        		.add(WUTBlocks.ANGEL.get())
        		.add(WUTBlocks.RESERVOIR.get())
        		.add(WUTBlocks.TANKDRUM.get())
        		.add(WUTBlocks.SMARTTV.get())
        		.add(WUTBlocks.MINERBASIC.get())
        		.add(WUTBlocks.FLOORSENSOR.get())
        		.add(WUTBlocks.WALLSENSOR.get())
        		.add(WUTBlocks.MINERADV.get())
//        		.add(WUTBlocks.XPWIRELESS.get())
//        		.add(WUTBlocks.XPPLATE.get())
        		.add(WUTBlocks.BRICKSDARK.get())
        		.add(WUTBlocks.BRICKSLAVA.get())
        		.add(WUTBlocks.WITHEREARTH.get())
        		.add(WUTBlocks.TOTEM.get())
        		.add(WUTBlocks.FLOODGATE.get())
        		
        		.add(WUTBlocks.GREENHOUSE.get())
        		.add(WUTBlocks.STEELPOLE.get())
        		.add(WUTBlocks.STEELPOLEHEAD.get())
        		.add(WUTBlocks.SLICEDCONCRETEBLACK.get())
        		.add(WUTBlocks.SLICEDCONCRETEGRAY.get())
        		.add(WUTBlocks.SLICEDCONCRETEWHITE.get())
        		.add(WUTBlocks.STEELRAILING.get())
        		.add(WUTBlocks.COLLECTOR.get())
        		.add(WUTBlocks.CATWALK.get())
        		.add(WUTBlocks.SPAWNER.get())
        		.add(WUTBlocks.FARMER.get())
        		.add(WUTBlocks.FISHER.get())
        		.add(WUTBlocks.CAULDRON.get())
                
                .add(WUTBlocks.RACK_TERMINAL.get())
                .add(WUTBlocks.RACK_CASE.get())
                .add(WUTBlocks.RACKITEM_CONTROLLER.get())
                .add(WUTBlocks.RACKFLUID_CONTROLLER.get())
                
                .add(WUTBlocks.ACTIVATOR.get())
                .add(WUTBlocks.CLICKER.get())
                .add(WUTBlocks.PLACER.get())
                .add(WUTBlocks.SCANNER.get())
                
                .add(WUTBlocks.CASED_DOOR.get())
                .add(WUTBlocks.CREEP_DOOR.get())
                .add(WUTBlocks.LIRON_DOOR.get())
                .add(WUTBlocks.STEEL_DOOR.get())
                .add(WUTBlocks.STRIPED_DOOR.get())
                .add(WUTBlocks.METALDOOR.get())

        		.add(WUTBlocks.CTM_CONCRETE_A.get())
        		.add(WUTBlocks.CTM_CONCRETE_B.get())
        		.add(WUTBlocks.CTM_CONCRETE_C.get())
        		.add(WUTBlocks.CTM_METAL_A.get())
        		.add(WUTBlocks.CTM_STONE_A.get())
        		.add(WUTBlocks.CTM_GLASS_A.get())
        		.add(WUTBlocks.CTM_GLASS_B.get())
        		.add(WUTBlocks.CTM_GLASS_C.get())
        		;
        
        tag(WUTTags.Blocks.BREAKER_BLACKLIST)
				.add(Blocks.AIR)
				.add(Blocks.BEDROCK)
				.add(Blocks.FIRE)
				.add(Blocks.END_PORTAL)
				.add(Blocks.END_GATEWAY)
				.add(Blocks.END_PORTAL_FRAME)
				.add(Blocks.NETHER_PORTAL)
				.add(Blocks.BARRIER);
    }

    @Override
    public String getName()
    {
        return "WitherUtils BlockTags";
    }
}