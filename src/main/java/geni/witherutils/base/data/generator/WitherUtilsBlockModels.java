package geni.witherutils.base.data.generator;

import com.google.gson.JsonObject;
import earth.terrarium.athena.impl.client.DefaultModels;
import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.common.block.deco.cutter.CutterBlock;
import geni.witherutils.base.common.block.deco.cutter.CutterBlock.CutterBlockType;
import geni.witherutils.base.common.init.WUTBlocks;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WitherUtilsBlockModels implements DataProvider {

    private final PackOutput output;
    private final WitherUtilsBlockStates files;
    
    public WitherUtilsBlockModels(PackOutput output, WitherUtilsBlockStates files)
    {
        this.output = output;
        this.files = files;
    }

	@Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput arg)
    {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (CutterBlock cutterBlock : WUTBlocks.CUTTERBLOCKS)
        {
            futures.add(CompletableFuture.runAsync(() -> {
            	
                final String blockNameId = BuiltInRegistries.BLOCK.getKey(cutterBlock).getPath();
                final ResourceLocation blockLoc = WitherUtilsRegistry.loc("block/ctm/" + blockNameId);
                final ResourceLocation location = WitherUtilsRegistry.loc("blockstates/" + blockNameId + ".json");
                Path path = this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(location.getNamespace()).resolve(location.getPath());
                
                try
                {
                    Block block = BuiltInRegistries.BLOCK.get(WitherUtilsRegistry.loc(blockNameId));
                    var json = files.getVariantBuilder(block).toJson();
                    var textures = GsonHelper.getAsJsonObject(files.models().generatedModels.get(blockLoc).toJson(), "textures");
                    var ctmTexture = getCommonTexture(textures);

                    JsonObject ctmTextures = new JsonObject();
                    
                    if(cutterBlock.getType() == CutterBlockType.CONNECTED)
                    {
                        ctmTextures.addProperty("particle", ctmTexture);
                        
                        ctmTextures.addProperty("center", "witherutils:block/ctm/" + blockNameId + "/" + "center");
                        ctmTextures.addProperty("empty", "witherutils:block/ctm/" + blockNameId + "/" + "empty");
                        ctmTextures.addProperty("horizontal", "witherutils:block/ctm/" + blockNameId + "/" + "horizontal");
                        ctmTextures.addProperty("vertical", "witherutils:block/ctm/" + blockNameId + "/" + "vertical");
                        ctmTextures.addProperty("particle", "witherutils:block/ctm/" + blockNameId + "/" + "particle");
                        json.add("ctm_textures", ctmTextures);
                        json.addProperty(ResourceLocation.fromNamespaceAndPath(DefaultModels.MODID, "loader").toString(), ResourceLocation.fromNamespaceAndPath(DefaultModels.MODID, "ctm").toString());
                    }
                    else
                        ctmTextures.addProperty(blockNameId, ctmTexture);

                    DataProvider.saveStable(arg, json, path).join();
                }
                catch (Exception e)
                {
                    throw e;
                }
            }, Util.backgroundExecutor()));
        }
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    private static String getCommonTexture(JsonObject object)
    {
        if (object.size() == 1)
        {
            return object.entrySet().iterator().next().getValue().getAsString();
        }
        else if (object.has("all"))
        {
            return GsonHelper.getAsString(object, "all");
        }
        return GsonHelper.getAsString(object, "particle");
    }

    @Override
    public @NotNull String getName()
    {
        return "WitherUtils Cutter Blockstates";
    }
}
