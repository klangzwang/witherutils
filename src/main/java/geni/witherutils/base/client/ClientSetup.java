package geni.witherutils.base.client;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.client.particle.BubbleParticle;
import geni.witherutils.base.client.particle.LiquidSprayParticle;
import geni.witherutils.base.client.particle.RisingSoulParticle;
import geni.witherutils.base.client.particle.SoulFlakeParticle;
import geni.witherutils.base.client.particle.SoulFragParticle;
import geni.witherutils.base.client.particle.SoulOrbParticle;
import geni.witherutils.base.client.particle.WindParticle;
import geni.witherutils.base.client.particle.XpOrbParticle;
import geni.witherutils.base.common.block.LogicalBlockEntities;
import geni.witherutils.base.common.block.LogicalBlocks;
import geni.witherutils.base.common.block.anvil.AnvilRenderer;
import geni.witherutils.base.common.block.creative.CreativeEnergyRenderer;
import geni.witherutils.base.common.block.creative.CreativeEnergyScreen;
import geni.witherutils.base.common.block.cutter.CutterBlock;
import geni.witherutils.base.common.entity.soulorb.SoulOrbProjectileRenderer;
import geni.witherutils.base.common.entity.soulorb.SoulOrbRenderer;
import geni.witherutils.base.common.entity.worm.WormRenderer;
import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.init.WUTMenus;
import geni.witherutils.base.common.init.WUTParticles;
import geni.witherutils.base.common.item.cutter.CutterScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SuppressWarnings("deprecation")
	@SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event)
	{
        ItemBlockRenderTypes.setRenderLayer(LogicalBlocks.ANVIL.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(LogicalBlocks.CREATIVEENERGY.get(), RenderType.cutout());
    	ItemBlockRenderTypes.setRenderLayer(WUTBlocks.ANGEL.get(), RenderType.cutout());
    	
    	ItemBlockRenderTypes.setRenderLayer(WUTBlocks.CTM_METAL_A.get(), RenderType.cutout());
    	ItemBlockRenderTypes.setRenderLayer(WUTBlocks.CTM_METAL_K.get(), RenderType.cutout());
    	ItemBlockRenderTypes.setRenderLayer(WUTBlocks.CTM_METAL_L.get(), RenderType.cutout());
    	ItemBlockRenderTypes.setRenderLayer(WUTBlocks.CTM_METAL_M.get(), RenderType.cutout());
    	
    	for (CutterBlock cutterBlock : WUTBlocks.CUTTERBLOCKS)
        {
    		if(cutterBlock.isGlass())
    			ItemBlockRenderTypes.setRenderLayer(cutterBlock, RenderType.translucent());
        }
    }
    
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event)
	{
        event.register(WUTMenus.CREATIVEGEN.get(), CreativeEnergyScreen::new);
        event.register(WUTMenus.CUTTER.get(), CutterScreen::new);
    }
    
    @SubscribeEvent
    public static void additionalModels(ModelEvent.RegisterAdditional event)
	{
        event.register(ModelResourceLocation.standalone(WitherUtilsRegistry.loc("item/wand_helper")));
    }

    @SubscribeEvent
    public static void itemDecorators(RegisterItemDecorationsEvent event)
	{
    }

    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event)
	{
    }

    @SubscribeEvent
    public static void bakingCompleted(ModelEvent.BakingCompleted event)
	{
    }

    @SuppressWarnings({ "resource", "deprecation" })
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event)
    {
		Minecraft.getInstance().particleEngine.register(WUTParticles.SOULFLAKE.get(), sprite -> new SoulFlakeParticle.Provider(sprite));
		Minecraft.getInstance().particleEngine.register(WUTParticles.EXPERIENCE.get(), sprite -> new XpOrbParticle.Factory(sprite));
		
//	    Minecraft.getInstance().particleEngine.register(WUTParticles.ENERGY.get(), EnergyParticle.Factory::new);
//	    Minecraft.getInstance().particleEngine.register(WUTParticles.ENERGY_CORE.get(), EnergyCoreParticle.Factory::new);
//	    Minecraft.getInstance().particleEngine.register(WUTParticles.BLACKSMOKE.get(), BlackSmokeParticle.Factory::new);
	    
	    Minecraft.getInstance().particleEngine.register(WUTParticles.BUBBLE.get(), BubbleParticle.Provider::new);
	    Minecraft.getInstance().particleEngine.register(WUTParticles.LIQUIDSPRAY.get(), LiquidSprayParticle.WaterSplashProvider::new);
	    Minecraft.getInstance().particleEngine.register(WUTParticles.FERTSPRAY.get(), LiquidSprayParticle.FertilizerSplashProvider::new);
	    Minecraft.getInstance().particleEngine.register(WUTParticles.SOULORB.get(), SoulOrbParticle.SmallNodeProvider::new);
	    Minecraft.getInstance().particleEngine.register(WUTParticles.WIND.get(), WindParticle.Provider::new);
	    Minecraft.getInstance().particleEngine.register(WUTParticles.RISINGSOUL.get(), RisingSoulParticle.Provider::new);
	    Minecraft.getInstance().particleEngine.register(WUTParticles.SOULFRAGSOFT.get(), SoulFragParticle.SoftProvider::new);
	    Minecraft.getInstance().particleEngine.register(WUTParticles.SOULFRAGHARD.get(), SoulFragParticle.HardProvider::new);
    }

    @SubscribeEvent
    public static void modelInit(ModelEvent.RegisterGeometryLoaders event)
	{
    }
	
	@OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
	    event.registerBlockEntityRenderer(LogicalBlockEntities.ANVIL.get(), AnvilRenderer::new);
	    event.registerBlockEntityRenderer(LogicalBlockEntities.CREATIVEENERGY.get(), CreativeEnergyRenderer::new);
		event.registerEntityRenderer(WUTEntities.WORM.get(), WormRenderer::new);
		event.registerEntityRenderer(WUTEntities.SOULORB.get(), SoulOrbRenderer::new);
		event.registerEntityRenderer(WUTEntities.SOULORBPRO.get(), SoulOrbProjectileRenderer::new);
    }
	
    @SubscribeEvent
    public static void registerModelLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
	{
    }
    
    public static final IClientBlockExtensions PARTICLE_HANDLER = new IClientBlockExtensions()
    {
        @Override
        public boolean addDestroyEffects(BlockState state, Level Level, BlockPos pos, ParticleEngine manager)
        {
            state.getShape(Level, pos).forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
                double xDif = Math.min(1, maxX - minX);
                double yDif = Math.min(1, maxY - minY);
                double zDif = Math.min(1, maxZ - minZ);
                int xCount = Mth.ceil(xDif / 0.25);
                int yCount = Mth.ceil(yDif / 0.25);
                int zCount = Mth.ceil(zDif / 0.25);
                if (xCount > 0 && yCount > 0 && zCount > 0) {
                    for (int x = 0; x < xCount; x++) {
                        for (int y = 0; y < yCount; y++) {
                            for (int z = 0; z < zCount; z++) {
                                double d4 = (x + 0.5) / xCount;
                                double d5 = (y + 0.5) / yCount;
                                double d6 = (z + 0.5) / zCount;
                                double d7 = d4 * xDif + minX;
                                double d8 = d5 * yDif + minY;
                                double d9 = d6 * zDif + minZ;
                                manager.add(new TerrainParticle((ClientLevel) Level, pos.getX() + d7, pos.getY() + d8,
                                        pos.getZ() + d9, d4 - 0.5, d5 - 0.5, d6 - 0.5, state).updateSprite(state, pos));
                            }
                        }
                    }
                }
            });
            return true;
        }
    };
}
