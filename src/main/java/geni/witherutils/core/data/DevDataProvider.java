package geni.witherutils.core.data;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import com.google.gson.JsonObject;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.client.ClientTooltipHandler;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("unused")
public abstract class DevDataProvider implements DataProvider {
	
    private final Map<String, String> built = new TreeMap<>();
    
    private final PackOutput output;
    private final String modid;
    private final ExistingFileHelper existingFileHelper;

    public DevDataProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper)
    {
        this.output = output;
        this.modid = modid;
        this.existingFileHelper = existingFileHelper;
    }
    
    protected abstract void build();
    
    @Override
    public CompletableFuture<?> run(CachedOutput cache)
    {
    	build();

        if (!built.isEmpty())
            return save(cache, this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(this.modid).resolve("blocks").resolve("output.json"));

        return CompletableFuture.allOf();
    }
    
    private CompletableFuture<?> save(CachedOutput cache, Path target)
    {
        JsonObject json = new JsonObject();
        this.built.forEach(json::addProperty);
        return DataProvider.saveStable(cache, json, target);
    }

    public void add(String id, Block key)
    {
    	String br = " <br> ";
    	String blockquote = " > ";
    	
    	String urlpngname = "DYbJY1j.png";
    	String imageurl = "https://i.imgur.com/" + urlpngname;
    	String blockimage = "<img src='" + imageurl + "'/>";
    	String blockdesc = "dfsdffksdlfmsdkfmsdkfmslkdfmsdlkfmsdklfmsdlkfmsdkl";
    	
    	String blockname = key.getDescriptionId();
    	
    	built.put(id, blockname + br + blockquote + blockdesc + br + blockimage);
    }
    public void add(Supplier<? extends Block> key)
    {
    	MutableComponent name = Component.translatable("block.witherutils.alloy_furnace");
    	MutableComponent desc = Component.translatable(key.get().getDescriptionId() + ".desc");
    	
    	System.out.println(name);
    	System.out.println(desc);
    	
    	System.out.println(ForgeRegistries.BLOCKS.getDelegate(key.get()).toString());
    	System.out.println(ForgeRegistries.BLOCKS.getKey(key.get()).toString());
    	System.out.println(ForgeRegistries.BLOCKS.getResourceKey(key.get()).toString());
    	System.out.println(ForgeRegistries.BLOCKS.getKey(key.get()).getPath());
    	
    	System.out.println(ClientTooltipHandler.localizable_block_key(key.get().getDescriptionId()));
    	System.out.println(ClientTooltipHandler.localizable(key.get().getDescriptionId()));
    	
        add(ForgeRegistries.BLOCKS.getKey(key.get()).getPath(), ClientTooltipHandler.localizable_block_key(key.get().getDescriptionId()).getString());
    }
    
    public void add(String blockname, String blockdesc)
    {
    	String br = " <br> ";
    	String blockquote = " > ";
    	
    	String urlpngname = "DYbJY1j.png";
    	String imageurl = "https://i.imgur.com/" + urlpngname;
    	String blockimage = "<img src='" + imageurl + "'/>";
    	
//    	blockdesc = "dfsdffksdlfmsdkfmsdkfmslkdfmsdlkfmsdklfmsdlkfmsdkl";
    	
    	built.put("", blockname + br + blockquote + blockdesc + br + blockimage);
    }
    
    @Override
    public String getName()
    {
        return "WitherUtilsDevProvider: ";
    }
}
