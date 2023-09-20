package geni.witherutils;

import java.io.IOException;
import java.nio.file.Files;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import geni.witherutils.base.client.ClientHudEvents;
import geni.witherutils.base.client.ClientRegistry;
import geni.witherutils.base.client.render.layer.ModelLayers;
import geni.witherutils.base.common.CommonRegistry;
import geni.witherutils.base.common.config.BaseConfig;
import geni.witherutils.base.common.event.WitherKeyMappingHandler;
import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTCreativeTab;
import geni.witherutils.base.common.init.WUTEffects;
import geni.witherutils.base.common.init.WUTEnchants;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.init.WUTFerts;
import geni.witherutils.base.common.init.WUTFluids;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.base.common.init.WUTMenus;
import geni.witherutils.base.common.init.WUTParticles;
import geni.witherutils.base.common.init.WUTRecipes;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.base.common.init.WUTUpgrades;
import geni.witherutils.base.common.integration.ModIntegration;
import geni.witherutils.base.common.item.withersteel.armor.upgrades.squidring.SquidRingHudEvents;
import geni.witherutils.base.common.soul.ClientSoul;
import geni.witherutils.base.common.soul.SoulOrbHudEvent;
import geni.witherutils.base.common.soul.SoulOrbManager;
import geni.witherutils.base.data.generator.WitherUtilsBlockStates;
import geni.witherutils.base.data.generator.WitherUtilsBlockTags;
import geni.witherutils.base.data.generator.WitherUtilsItemModels;
import geni.witherutils.base.data.generator.WitherUtilsItemTags;
import geni.witherutils.base.data.generator.WitherUtilsLanguages;
import geni.witherutils.base.data.generator.WitherUtilsLootTables;
import geni.witherutils.base.data.generator.recipe.WitherAlloyRecipeProvider;
import geni.witherutils.base.data.generator.recipe.WitherAnvilRecipeProvider;
import geni.witherutils.base.data.generator.recipe.WitherCauldronRecipeProvider;
import geni.witherutils.base.data.generator.recipe.WitherCutterRecipeProvider;
import geni.witherutils.base.data.generator.recipe.WitherUtilsCraftingRecipes;
import geni.witherutils.base.data.generator.recipe.WitherUtilsMachineRecipes;
import geni.witherutils.core.common.fakeplayer.WUTFakePlayer;
import geni.witherutils.core.common.network.CoreNetwork;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(WitherUtils.MODID)
public class WitherUtils {

	public static final String MODID = "witherutils";

    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static ResourceLocation loc(String path) { return new ResourceLocation(MODID, path); }

    public WitherUtils()
	{
        FMLJavaModLoadingContext.get().getModEventBus().addListener(CommonRegistry::onCommonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientRegistry::setupClient);
        DistExecutor.safeRunForDist(() -> ClientRegistry::new, () -> CommonRegistry::new);
        
        try
        {
            Files.createDirectories(FMLPaths.CONFIGDIR.get().resolve(MODID));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(this::init);
        
        var ctx = ModLoadingContext.get();
        ctx.registerConfig(ModConfig.Type.COMMON, BaseConfig.COMMON_SPEC, "witherutils/base-common.toml");
        ctx.registerConfig(ModConfig.Type.CLIENT, BaseConfig.CLIENT_SPEC, "witherutils/base-client.toml");
        
        CoreNetwork.networkInit();

        WUTCreativeTab.init();
        
        WUTBlocks.BLOCK_TYPES.register(bus);
        WUTBlocks.BLOCKITEM_TYPES.register(bus);
        WUTItems.ITEM_TYPES.register(bus);
        WUTUpgrades.UPGRADES.register(bus);
        WUTEntities.TILE_TYPES.register(bus);
        WUTEntities.ENTITY_TYPES.register(bus);
        WUTMenus.CONTAINERS.register(bus);
        WUTParticles.PARTICLE_TYPES.register(bus);
        WUTEffects.EFFECT_TYPES.register(bus);
        WUTSounds.SOUND_TYPES.register(bus);
        WUTRecipes.RECIPE_TYPES.register(bus);
        WUTRecipes.RECIPE_SERIALIZERS.register(bus);
        WUTEnchants.ENCHANT_TYPES.register(bus);
        WUTFluids.FLUID_TYPES.register(bus);
        WUTFluids.FLUIDS.register(bus);
        WUTFerts.FERTS.register(bus);
        
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGHEST, this::blockJoin);
        
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            bus.register(new ClientHudEvents());
			bus.register(new SquidRingHudEvents());
			bus.register(new SoulOrbHudEvent());
			
			SoulOrbManager.init();
			ClientSoul.init();
			
            bus.addListener(WitherKeyMappingHandler::onRegisterKeyMappings);
            
			ClientRegistry.onTextureStitch(bus, ClientRegistry::onTextureStitch);
			
			ModelLayers.init(bus);
        });
        
        bus.addListener(this::onGatherData);
        bus.addListener(this::doClientStuff);
        
        WitherUtilsBlockTags.setup();
    }
    
    private void onGatherData(GatherDataEvent event)
    {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        
        WitherUtilsBlockTags blockTags = new WitherUtilsBlockTags(generator.getPackOutput(), event.getLookupProvider(), existingFileHelper);
        generator.addProvider(event.includeServer(), new WitherUtilsCraftingRecipes(generator.getPackOutput()));
        generator.addProvider(event.includeServer(), blockTags);
        WitherUtilsItemTags itemTags = new WitherUtilsItemTags(generator.getPackOutput(), event.getLookupProvider(), blockTags.contentsGetter(), existingFileHelper);
        generator.addProvider(event.includeServer(), itemTags);
        generator.addProvider(event.includeClient(), new WitherUtilsBlockStates(generator.getPackOutput(), existingFileHelper));
//        generator.addProvider(event.includeClient(), new WitherUtilsBlockModels(generator.getPackOutput(), existingFileHelper));
        generator.addProvider(event.includeClient(), new WitherUtilsItemModels(generator.getPackOutput(), existingFileHelper));
        generator.addProvider(event.includeClient(), new WitherUtilsLanguages(generator.getPackOutput(), "en_us"));
        generator.addProvider(event.includeClient(), new WitherUtilsLootTables(generator.getPackOutput()));
//        generator.addProvider(event.includeClient(), new WitherUtilsGenerator(generator.getPackOutput(), existingFileHelper));

        WitherUtilsMachineRecipes provider = new WitherUtilsMachineRecipes("witherutils");
        
        provider.addSubProvider(event.includeServer(), new WitherCutterRecipeProvider(generator.getPackOutput()));
        provider.addSubProvider(event.includeServer(), new WitherAlloyRecipeProvider(generator.getPackOutput()));
        provider.addSubProvider(event.includeServer(), new WitherAnvilRecipeProvider(generator.getPackOutput()));
        provider.addSubProvider(event.includeServer(), new WitherCauldronRecipeProvider(generator.getPackOutput()));
        
        generator.addProvider(true, provider);
    }
    
    private void doClientStuff(final FMLClientSetupEvent event)
    {
		MinecraftForge.EVENT_BUS.register(new WitherKeyMappingHandler());
        MinecraftForge.EVENT_BUS.addListener(this::worldUnload);
    }
    private void init(InterModEnqueueEvent event)
    {
        ModIntegration.INSTANCE.init();
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
}
