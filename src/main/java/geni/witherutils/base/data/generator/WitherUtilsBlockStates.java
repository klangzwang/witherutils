package geni.witherutils.base.data.generator;

import java.util.Map;
import java.util.function.Function;

import geni.witherutils.api.lib.Names;
import geni.witherutils.base.common.block.LogicalBlocks;
import geni.witherutils.base.common.block.cutter.CutterBlock;
import geni.witherutils.base.common.block.cutter.CutterBlock.CutterBlockType;
import geni.witherutils.base.common.init.WUTBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.IGeneratedBlockState;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class WitherUtilsBlockStates extends BlockStateProvider {

    @SuppressWarnings("unused")
	private final PackOutput output;
    
    public WitherUtilsBlockStates(PackOutput output, ExistingFileHelper fileHelper)
    {
        super(output, Names.MODID, fileHelper);
        this.output = output;
    }

    @Override
    protected void registerStatesAndModels()
    {
    	registerBlock(WUTBlocks.CTM_METAL_0.get());
    	
        simpleBlock(WUTBlocks.ANGEL.get(), existingBlock(WUTBlocks.ANGEL));
        simpleBlock(WUTBlocks.CASE.get(), existingBlock(WUTBlocks.CASE));

        registerAnvil();
        registerCreativeEnergy();
        registerCutter();
    }
    
    private void registerBlock(Block block)
    {
		String name = name(block);
        simpleBlock(block, models().getBuilder("block/" + name)
        		.parent(models().getExistingFile(mcLoc("block/cube_all")))
        		.texture("all", modLoc("block/" + name)));
    }

    private void registerCutter()
    {
    	for (CutterBlock cutterBlock : WUTBlocks.CUTTERBLOCKS)
        {
    		String name = name(cutterBlock);
            simpleBlock(cutterBlock, models().getBuilder("block/ctm/" + name)
            		.parent(models().getExistingFile(mcLoc("block/cube_all")))
            		.texture("all", modLoc("block/ctm/" + name +
            		(cutterBlock.getType() == CutterBlockType.CONNECTED ? "/particle" : "/" + name))));
        }
    }
    
    private void registerAnvil()
    {
        getVariantBuilder(LogicalBlocks.ANVIL.get()).forAllStates(state -> {
            
            Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            boolean lit = state.getValue(BlockStateProperties.LIT);
            
            return ConfiguredModel.builder()
                    .modelFile(lit ? existingBlock(LogicalBlocks.ANVIL, "on") : existingBlock(LogicalBlocks.ANVIL))
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                    .build();
        });
    }
    private void registerCreativeEnergy()
    {
        getVariantBuilder(LogicalBlocks.CREATIVEENERGY.get()).forAllStates(state -> {
            
            Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            boolean lit = state.getValue(BlockStateProperties.LIT);
            
            return ConfiguredModel.builder()
                    .modelFile(lit ? existingBlock(LogicalBlocks.CREATIVEENERGY, "on") : existingBlock(LogicalBlocks.CREATIVEENERGY))
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                    .build();
        });
    }

    /*
     * 
     * SORTOF
     * 
     */
    private ResourceLocation key(Block block)
    {
        return BuiltInRegistries.BLOCK.getKey(block);
    }
    
    @SuppressWarnings("unused")
	private ResourceLocation key(DeferredBlock<Block> block)
    {
        return BuiltInRegistries.BLOCK.getKey(block.get());
    }

    @SuppressWarnings("unused")
	private BlockModelBuilder block(DeferredBlock<Block> block) { return block(block, ""); }

    private BlockModelBuilder block(DeferredBlock<Block> block, String suffix)
    {
        String name = block.getId().getPath();
        String path = "block/" + name;
        if (!suffix.isBlank())
        {
            path += "_" + suffix;
        }
        return models().getBuilder(path);
    }
    
    private ModelFile existingBlock(DeferredBlock<Block> block) { return existingBlock(block, ""); }

    private ModelFile existingBlock(DeferredBlock<Block> block, String suffix)
    {
        ResourceLocation name = block.getId();
        String path = "block/" + name.getPath();
        if (!suffix.isBlank())
        {
            path += "_" + suffix;
        }
        return models().getExistingFile(ResourceLocation.fromNamespaceAndPath(name.getNamespace(), path));
    }
    
    @SuppressWarnings("unused")
	private ModelFile existingBlockSubDirectory(DeferredBlock<Block> block, String suffix)
    {
        ResourceLocation name = block.getId();
        String path = "block/" + name.getPath() + "/" + name.getPath();
        if (!suffix.isBlank())
        {
            path += "_" + suffix;
        }
        return models().getExistingFile(ResourceLocation.fromNamespaceAndPath(name.getNamespace(), path));
    }
    
    @SuppressWarnings("unused")
	private ItemModelBuilder simpleBlockWithItem(DeferredBlock<Block> block, ModelFile model, String itemRenderType)
    {
        return simpleBlockWithItem(block, model).renderType(itemRenderType);
    }

    private ItemModelBuilder simpleBlockWithItem(DeferredBlock<Block> block, ModelFile model)
    {
        simpleBlock(block.get(), model);
        return simpleBlockItem(block, model);
    }

    @SuppressWarnings("unused")
	private ItemModelBuilder simpleBlockItem(DeferredBlock<Block> block, ModelFile model, String renderType)
    {
        return simpleBlockItem(block, model).renderType(renderType);
    }

    private ItemModelBuilder simpleBlockItem(DeferredBlock<Block> block, ModelFile model)
    {
        return itemModels().getBuilder(block.getId().getPath()).parent(model);
    }

    @SuppressWarnings("unused")
	private ItemModelBuilder simpleItem(DeferredBlock<Block> block, String renderType)
    {
        return simpleItem(block.getId().getPath(), renderType);
    }

    @SuppressWarnings("unused")
	private ItemModelBuilder simpleItem(DeferredBlock<Block> block, String texture, String renderType)
    {
        return simpleItem(block.getId().getPath(), texture, renderType);
    }

    private ItemModelBuilder simpleItem(String name, String renderType)
    {
        return simpleItem(name, "item/" + name, renderType);
    }

    private ItemModelBuilder simpleItem(String name, String texture, String renderType)
    {
        return itemModels().singleTexture(name, mcLoc("item/generated"), "layer0", modLoc(texture)).renderType(renderType);
    }

    @SuppressWarnings("unused")
	private void dummyBlock(Block block)
    {
        ModelFile model = models()
                .withExistingParent("dummy", "block")
                .texture("particle", "minecraft:block/glass");
        simpleBlock(block, model);
    }

    public void directionalFromNorth(Block block, ModelFile model)
    {
        directionalFromNorth(block, model, 180);
    }

    public void directionalFromNorth(Block block, ModelFile model, int angleOffset)
    {
        directionalFromNorth(block, $ -> model, angleOffset);
    }

    public void directionalFromNorth(Block block, Function<BlockState, ModelFile> modelFunc)
    {
        directionalFromNorth(block, modelFunc, 180);
    }

    public void directionalFromNorth(Block block, Function<BlockState, ModelFile> modelFunc, int angleOffset)
    {
        getVariantBuilder(block)
                .forAllStates(state -> {
                    Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                    return ConfiguredModel.builder()
                            .modelFile(modelFunc.apply(state))
//                            .rotationX(dir == DOWN ? 90 : dir == UP ? -90 : 0)
                            .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + angleOffset) % 360)
                            .build();
                });
    }

    private String name(Block block)
    {
        return key(block).getPath();
    }
    
    public ResourceLocation customTexture(Block block, String folder)
    {
        ResourceLocation name = key(block);
        return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + folder + "/" + name.getPath());
    }
    
    public Map<Block, IGeneratedBlockState> getGeneratedBlockStates()
    {
        return registeredBlocks;
    }
    
    @Override
    public String getName()
    {
        return "WitherUtils Blockstates";
    }
}