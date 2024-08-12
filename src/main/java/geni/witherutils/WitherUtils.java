package geni.witherutils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.authlib.GameProfile;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.api.lib.Names;
import geni.witherutils.base.client.ClientHudEvents;
import geni.witherutils.base.client.ClientSetup;
import geni.witherutils.base.client.render.layer.ModelLayers;
import geni.witherutils.base.common.CommonEventHandler;
import geni.witherutils.base.common.WitherUtilsAPIHandler;
import geni.witherutils.base.common.block.anvil.AnvilRecipeProvider;
import geni.witherutils.base.common.block.cauldron.CauldronRecipeProvider;
import geni.witherutils.base.common.config.BaseConfig;
import geni.witherutils.base.common.entity.EntityEventHandler;
import geni.witherutils.base.common.init.WUTAdapters;
import geni.witherutils.base.common.init.WUTAttachments;
import geni.witherutils.base.common.init.WUTBlockEntityTypes;
import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTCapabilities;
import geni.witherutils.base.common.init.WUTComponents;
import geni.witherutils.base.common.init.WUTCreativeTab;
import geni.witherutils.base.common.init.WUTCriterions;
import geni.witherutils.base.common.init.WUTEffects;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.init.WUTFluids;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.base.common.init.WUTMenus;
import geni.witherutils.base.common.init.WUTParticles;
import geni.witherutils.base.common.init.WUTRecipes;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.base.common.item.ItemEventHandler;
import geni.witherutils.base.common.item.cutter.CutterRecipeProvider;
import geni.witherutils.base.data.generator.WitherUtilsAdvancements;
import geni.witherutils.base.data.generator.WitherUtilsBlockModels;
import geni.witherutils.base.data.generator.WitherUtilsBlockStates;
import geni.witherutils.base.data.generator.WitherUtilsBlockTags;
import geni.witherutils.base.data.generator.WitherUtilsFluidTags;
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
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

@Mod(Names.MODID)
public class WitherUtils {

	public static final Logger LOGGER = LogManager.getLogger();
	
//	public static Regilite REGILITE = new Regilite(Names.MODID);
    public static final GameProfile gameProfile = new GameProfile(UUID.nameUUIDFromBytes("mekanism.common".getBytes(StandardCharsets.UTF_8)), "faker");
    
    public WitherUtils(IEventBus modEventBus, ModContainer modContainer)
    {
        WitherUtilsRegistry.init(WitherUtilsAPIHandler.getInstance());
		
        try {
            Files.createDirectories(FMLPaths.CONFIGDIR.get().resolve(Names.MODID));
        } catch (IOException e) {
            e.printStackTrace();
        }

        modContainer.registerConfig(ModConfig.Type.COMMON, BaseConfig.COMMON_SPEC, "witherutils/base-common.toml");
        modContainer.registerConfig(ModConfig.Type.CLIENT, BaseConfig.CLIENT_SPEC, "witherutils/base-client.toml");
    	
        WUTCreativeTab.init(modEventBus);

        WUTSounds.SOUND_TYPES.register(modEventBus);
        WUTBlocks.BLOCK_TYPES.register(modEventBus);
        WUTItems.ITEM_TYPES.register(modEventBus);
        WUTFluids.FLUIDS.register(modEventBus);
        WUTFluids.FLUID_TYPES.register(modEventBus);
        WUTEntities.ENTITY_TYPES.register(modEventBus);
        WUTParticles.PARTICLE_TYPES.register(modEventBus);
        WUTComponents.DATACOMP_TYPES.register(modEventBus);
        WUTAttachments.ATTACHMENT_TYPES.register(modEventBus);
        WUTRecipes.RECIPE_TYPES.register(modEventBus);
        WUTRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        WUTMenus.MENU_TYPES.register(modEventBus);
        WUTCriterions.TRIGGER_TYPES.register(modEventBus);
        WUTEffects.EFFECT_TYPES.register(modEventBus);
        WUTBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
        WUTAdapters.ADAPTER_TYPES.register(modEventBus);

//        REGILITE.register(modEventBus);
        
        modEventBus.addListener(WUTCapabilities::register);
        modEventBus.addListener(EventPriority.LOWEST, this::onGatherData);
        
		if (FMLEnvironment.dist.isClient())
		{
	        modEventBus.register(new ClientHudEvents());
			ModelLayers.init(modEventBus);
			ClientSetup.onTextureStitch(modEventBus, ClientSetup::onTextureStitch);
//			modEventBus.addListener(ModelLayers::onAddRenderLayers);
//			modEventBus.addListener(ModelLayers::onAddLayers);
		}
        
        IEventBus forgeBus = NeoForge.EVENT_BUS;
        forgeBus.register(new CommonEventHandler());
        forgeBus.register(new ItemEventHandler());
        forgeBus.register(new EntityEventHandler());
        forgeBus.addListener(EventPriority.HIGHEST, this::blockJoin);
        forgeBus.addListener(this::onWorldUnload);
        
        LOGGER.info("Fake player readout: UUID = {}, name = {}", gameProfile.getId(), gameProfile.getName());
    }

    public void blockJoin(EntityJoinLevelEvent e)
    {
        if (e.getEntity() instanceof WUTFakePlayer)
        	e.setCanceled(true);
    }
    
    private void onWorldUnload(LevelEvent.Unload event)
    {
        if (event.getLevel() instanceof ServerLevel level)
        {
            WUTFakePlayer.releaseInstance(level);
        }
    }

	public void onGatherData(GatherDataEvent event)
	{
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        BlockTagsProvider blockTagsProvider = new WitherUtilsBlockTags(generator, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTagsProvider);

        generator.addProvider(event.includeServer(), new WitherUtilsItemTags(generator, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper));
        generator.addProvider(event.includeServer(), new WitherUtilsFluidTags(generator, lookupProvider, existingFileHelper));
        
        WitherUtilsBlockStates stateProvider = new WitherUtilsBlockStates(packOutput, existingFileHelper);
        generator.addProvider(event.includeClient(), stateProvider);
        generator.addProvider(event.includeClient(), new WitherUtilsBlockModels(generator.getPackOutput(), stateProvider));
        generator.addProvider(event.includeServer(), new WitherUtilsCraftingRecipes(generator.getPackOutput(), lookupProvider));
        generator.addProvider(event.includeClient(), new WitherUtilsItemModels(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new WitherUtilsLanguages(packOutput, Names.MODID, "en_us"));
        generator.addProvider(event.includeServer(), new WitherUtilsLootTables(generator, lookupProvider));
        generator.addProvider(event.includeServer(), new WitherUtilsAdvancements(generator, lookupProvider, existingFileHelper));

        WitherUtilsMachineRecipes provider = new WitherUtilsMachineRecipes("witherutils");
        provider.addSubProvider(event.includeServer(), new AnvilRecipeProvider(packOutput, lookupProvider));
        provider.addSubProvider(event.includeServer(), new CutterRecipeProvider(packOutput, lookupProvider));
        provider.addSubProvider(event.includeServer(), new CauldronRecipeProvider(packOutput, lookupProvider));
        
        generator.addProvider(true, provider);
    }
}
