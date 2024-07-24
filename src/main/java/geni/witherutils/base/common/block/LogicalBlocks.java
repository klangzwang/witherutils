package geni.witherutils.base.common.block;

import geni.witherutils.api.lib.Names;
import geni.witherutils.base.common.base.WitherSoundType;
import geni.witherutils.base.common.block.anvil.AnvilBlock;
import geni.witherutils.base.common.block.creative.CreativeEnergyBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class LogicalBlocks {
	
	public static final DeferredRegister.Blocks BLOCK_TYPES = DeferredRegister.createBlocks(Names.MODID);
	
    /*
     * 
     * PROPERTIES
     * 
     */
    public static Block.Properties machineProps =
    		BlockBehaviour.Properties
            .of()
            .mapColor(MapColor.METAL)
            .strength(2.5F, 8.F)
            .sound(WitherSoundType.SOULMETAL)
            .noOcclusion();
	
	/*
	 * 
	 * WITHERUTILSBLOCK 
	 *
	 */
	public static final DeferredBlock<Block> ANVIL = BLOCK_TYPES.register("anvil", () -> new AnvilBlock(machineProps));
	public static final DeferredBlock<Block> CREATIVEENERGY = BLOCK_TYPES.register("creativeenergy", () -> new CreativeEnergyBlock(machineProps));
}
