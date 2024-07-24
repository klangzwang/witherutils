package geni.witherutils;

import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.CompletableFuture;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.api.lib.Names;
import geni.witherutils.base.client.ClientHudEvents;
import geni.witherutils.base.common.CommonEventHandler;
import geni.witherutils.base.common.WitherUtilsAPIHandler;
import geni.witherutils.base.common.block.LogicalBlockEntities;
import geni.witherutils.base.common.block.LogicalBlocks;
import geni.witherutils.base.common.block.LogicalConfig;
import geni.witherutils.base.common.block.anvil.AnvilRecipeProvider;
import geni.witherutils.base.common.config.BaseConfig;
import geni.witherutils.base.common.init.WUTAttachments;
import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTCapabilities;
import geni.witherutils.base.common.init.WUTComponents;
import geni.witherutils.base.common.init.WUTCreativeTab;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.base.common.init.WUTMenus;
import geni.witherutils.base.common.init.WUTParticles;
import geni.witherutils.base.common.init.WUTRecipes;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.base.common.item.cutter.CutterRecipeProvider;
import geni.witherutils.base.data.generator.WitherUtilsBlockModels;
import geni.witherutils.base.data.generator.WitherUtilsBlockStates;
import geni.witherutils.base.data.generator.WitherUtilsBlockTags;
import geni.witherutils.base.data.generator.WitherUtilsItemModels;
import geni.witherutils.base.data.generator.WitherUtilsItemTags;
import geni.witherutils.base.data.generator.WitherUtilsLanguages;
import geni.witherutils.base.data.generator.WitherUtilsLootTables;
import geni.witherutils.base.data.generator.recipes.WitherUtilsCraftingRecipes;
import geni.witherutils.base.data.generator.recipes.WitherUtilsMachineRecipes;
import geni.witherutils.core.common.fakeplayer.WUTFakePlayer;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

@Mod(Names.MODID)
public class WitherUtils {

    public WitherUtils(IEventBus modEventBus, ModContainer modContainer)
    {
        WitherUtilsRegistry.init(WitherUtilsAPIHandler.getInstance());
		
        try {
            Files.createDirectories(FMLPaths.CONFIGDIR.get().resolve(Names.MODID));
        } catch (IOException e) {
            e.printStackTrace();
        }

        modContainer.registerConfig(ModConfig.Type.COMMON, BaseConfig.COMMON_SPEC, "witherutils/base-common.toml");
    	modContainer.registerConfig(ModConfig.Type.COMMON, LogicalConfig.COMMON_SPEC, "witherutils/logical-common.toml");
        modContainer.registerConfig(ModConfig.Type.CLIENT, BaseConfig.CLIENT_SPEC, "witherutils/base-client.toml");
    	
        WUTCreativeTab.init(modEventBus);

        WUTSounds.SOUND_TYPES.register(modEventBus);
        WUTBlocks.BLOCK_TYPES.register(modEventBus);
        WUTItems.ITEM_TYPES.register(modEventBus);
        WUTEntities.ENTITY_TYPES.register(modEventBus);
        WUTParticles.PARTICLE_TYPES.register(modEventBus);
        WUTComponents.DATACOMP_TYPES.register(modEventBus);
        WUTAttachments.ATTACHMENT_TYPES.register(modEventBus);
        WUTRecipes.RECIPE_TYPES.register(modEventBus);
        WUTRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        WUTMenus.MENU_TYPES.register(modEventBus);
        
        LogicalBlocks.BLOCK_TYPES.register(modEventBus);
        LogicalBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);

        WUTItems.registerBlockItems(modEventBus);

        modEventBus.addListener(WUTCapabilities::register);
        modEventBus.addListener(EventPriority.LOWEST, this::onGatherData);
        
        WitherUtilsBlockTags.setup();
        
		if (FMLEnvironment.dist.isClient())
		{
	        modEventBus.register(new ClientHudEvents());
		}
        
        IEventBus forgeBus = NeoForge.EVENT_BUS;
        forgeBus.register(new CommonEventHandler());
        forgeBus.addListener(EventPriority.HIGHEST, this::blockJoin);
        forgeBus.addListener(this::worldUnload);
    }
	
    public void blockJoin(EntityJoinLevelEvent e)
    {
        if (e.getEntity() instanceof WUTFakePlayer)
        	e.setCanceled(true);
    }
    
    private void worldUnload(final LevelEvent.Unload event)
    {
        if (event.getLevel() instanceof ServerLevel)
            WUTFakePlayer.unload(event.getLevel());
    }
    
	public void onGatherData(GatherDataEvent event)
	{
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        WitherUtilsBlockTags blockTags = new WitherUtilsBlockTags(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTags);
        WitherUtilsItemTags itemTags = new WitherUtilsItemTags(generator.getPackOutput(), event.getLookupProvider(), blockTags.contentsGetter(), existingFileHelper);
        generator.addProvider(event.includeServer(), itemTags);
        WitherUtilsBlockStates stateProvider = new WitherUtilsBlockStates(packOutput, existingFileHelper);
        generator.addProvider(event.includeClient(), stateProvider);
        generator.addProvider(event.includeClient(), new WitherUtilsBlockModels(generator.getPackOutput(), stateProvider));
        generator.addProvider(event.includeServer(), new WitherUtilsCraftingRecipes(generator.getPackOutput(), lookupProvider));
        generator.addProvider(event.includeClient(), new WitherUtilsItemModels(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new WitherUtilsLanguages(packOutput, Names.MODID, "en_us"));
        generator.addProvider(event.includeServer(), new WitherUtilsLootTables(generator, lookupProvider));
        
        WitherUtilsMachineRecipes provider = new WitherUtilsMachineRecipes("witherutils");
        provider.addSubProvider(event.includeServer(), new AnvilRecipeProvider(packOutput, lookupProvider));
        provider.addSubProvider(event.includeServer(), new CutterRecipeProvider(packOutput, lookupProvider));
        
        generator.addProvider(true, provider);
    }
}
