package geni.witherutils.base.client.render.type;

import java.util.OptionalDouble;
import java.util.function.Function;

import org.lwjgl.opengl.GL14;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;

import geni.witherutils.WitherUtils;
import net.minecraft.Util;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public class WUTRenderType extends RenderType {

	private static final boolean SORT = false;
	private static final boolean CRUMBLING = false;
	private static final int BUFFERSIZE = 256;

	/*
	 * 
	 * TESTING
	 * 
	 */
    private static final Function<ResourceLocation, RenderType> TRANSLUCENT_NO_CULL = Util.memoize((rl) -> {
        RenderType.CompositeState state = RenderType.CompositeState.builder()
                .setShaderState(RenderStateShard.RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(rl, false, false))
                .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                .setCullState(NO_CULL)
                .setLightmapState(LIGHTMAP)
                .setOverlayState(OVERLAY)
                .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                .createCompositeState(true);
        return create("armor_translucent_no_cull", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS,
                256, true, false,
                state);
    });
    public static RenderType getTranslucentNoCull(ResourceLocation rl) {
        return TRANSLUCENT_NO_CULL.apply(rl);
    }
	////////
    
    
    protected static final RenderStateShard.TransparencyStateShard WORM_TRANSPARANCY = new RenderStateShard.TransparencyStateShard("translucent_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });
    public static final RenderStateShard.ShaderStateShard RENDERTYPE_COLLECTOR_PORTAL_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::getRendertypeEndPortalShader);

	private WUTRenderType(String nameIn, VertexFormat formatIn, VertexFormat.Mode drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn)
	{
		super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
	}

    private static final TransparencyStateShard GHOST_TRANSPARENCY = new TransparencyStateShard("ghost_transparency",
            () -> {
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(GlStateManager.SourceFactor.CONSTANT_ALPHA, GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);
//                long i = Util.getMillis() * 4L;
//                float f1 = (float)(i % 30000L) / 30000.0F;
                GL14.glBlendColor(1.0F, 1.0F, 1.0F, 0.35F);
            },
            () -> {
                GL14.glBlendColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.disableBlend();
                RenderSystem.defaultBlendFunc();
            });

    protected static final RenderStateShard.TransparencyStateShard BLENDED = new RenderStateShard.TransparencyStateShard("blended", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });
    public static RenderType getTextBlended(ResourceLocation locationIn) {
        CompositeState state = CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(locationIn, false, false))
                .setShaderState(RenderStateShard.RENDERTYPE_TEXT_SHADER)
                .setTransparencyState(BLENDED)
                .setLightmapState(NO_LIGHTMAP).createCompositeState(false);
        return create("text_blended", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, true, state);
    }
    
    public static final RenderType GHOST = create("witherutils:ghost", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 2097152, true, false, RenderType.CompositeState.builder()
                .setLightmapState(LIGHTMAP)
                .setShaderState(RENDERTYPE_SOLID_SHADER)
                .setTextureState(BLOCK_SHEET)
                .setTransparencyState(GHOST_TRANSPARENCY)
                .setWriteMaskState(COLOR_WRITE)
                .createCompositeState(false)
    );
	public static final RenderType LASER = create(WitherUtils.MODID + ":laserlaser", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, BUFFERSIZE, CRUMBLING, SORT, RenderType.CompositeState.builder()
			.setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
			.setLayeringState(VIEW_OFFSET_Z_LAYERING)
			.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
			.setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
			.setCullState(NO_CULL)
			.setLightmapState(NO_LIGHTMAP)
			.setWriteMaskState(COLOR_WRITE)
			.createCompositeState(false));

	public static final RenderType FLUIDTANKRESIZABLE = create(WitherUtils.MODID + ":resizable_cuboid", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, true, false, RenderType.CompositeState.builder()
			.setShaderState(RENDERTYPE_CUTOUT_SHADER)
			.setTextureState(new RenderStateShard.TextureStateShard(InventoryMenu.BLOCK_ATLAS, false, false))
			.setCullState(CULL)
			.setLightmapState(LIGHTMAP)
			.setWriteMaskState(COLOR_WRITE)
			.setLightmapState(LIGHTMAP)
			.createCompositeState(true));

	public static final RenderType ITEMSTACKRESIZABLE = create(WitherUtils.MODID + ":resizable_cuboid_item", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, true, false, RenderType.CompositeState.builder()
			.setShaderState(RENDERTYPE_SOLID_SHADER)
			.setTextureState(new RenderStateShard.TextureStateShard(InventoryMenu.BLOCK_ATLAS, false, false))
			.setCullState(NO_CULL)
			.setLightmapState(LIGHTMAP)
			.setWriteMaskState(COLOR_WRITE)
			.setLightmapState(LIGHTMAP)
			.createCompositeState(true));

	public static final RenderType ITEMSTACKRESIZABLE_SOLID = create(WitherUtils.MODID + ":resizable_cuboid_solid", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, true, false, RenderType.CompositeState.builder()
			.setLightmapState(LIGHTMAP)
			.setShaderState(RENDERTYPE_SOLID_SHADER)
			.setTextureState(BLOCK_SHEET_MIPPED)
			.createCompositeState(true));

//	public static final RenderType COLLECTOR_PORTAL = create("collector_portal", DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
//			.setShaderState(RENDERTYPE_COLLECTOR_PORTAL_SHADER)
//			.setTextureState(RenderStateShard.MultiTextureStateShard.builder()
//					.add(CollectorRenderer.SKY_LOCATION, false, false)
//					.add(CollectorRenderer.PORTAL_LOCATION, false, false).build())
//			.createCompositeState(false));

    public static RenderType standard(ResourceLocation resourceLocation) {
        return STANDARD.apply(resourceLocation);
    }

    private static final Function<ResourceLocation, RenderType> STANDARD = Util.memoize(resourceLocation -> {
        RenderType.CompositeState state = RenderType.CompositeState.builder()
              .setShaderState(RenderStateShard.RENDERTYPE_ENTITY_SOLID_SHADER)
              .setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false))
              .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
              .createCompositeState(true);
        return create("wu_standard", DefaultVertexFormat.NEW_ENTITY, Mode.QUADS, 256, true, false, state);
    });

    /*
     * 
     * EMISSIVE
     * 
     */
    public static RenderType eyes(ResourceLocation p_110489_)
    {
        RenderStateShard.TextureStateShard renderstateshard$texturestateshard = new RenderStateShard.TextureStateShard(p_110489_, false, false);
        
        return create("eyes", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder()
        		.setShaderState(RENDERTYPE_EYES_SHADER)
        		.setTextureState(renderstateshard$texturestateshard)
        		.setTransparencyState(ADDITIVE_TRANSPARENCY)
        		.setWriteMaskState(COLOR_WRITE)
        		.createCompositeState(false));
    }
    public static RenderType getEyesFlickering(ResourceLocation p_228652_0_, float lightLevel)
    {
        RenderStateShard.TextureStateShard lvt_1_1_ = new RenderStateShard.TextureStateShard(p_228652_0_, false, false);
        
        return create("eyes_flickering", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder()
        		.setTextureState(lvt_1_1_)
        		.setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_CULL_SHADER)
        		.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
        		.setCullState(NO_CULL)
        		.setLightmapState(LIGHTMAP)
        		.setOverlayState(OVERLAY)
        		.createCompositeState(false));
    }

//    @SuppressWarnings("unused")
//	public static RenderType getEyesAlphaEnabled(Level level, float partialTicks, ResourceLocation locationIn)
//    {
//        RenderStateShard.TransparencyStateShard CORE_TRANSPARENCY = new TransparencyStateShard("core_transparency",
//                () -> {
//                    RenderSystem.enableBlend();
//                    RenderSystem.blendFunc(GlStateManager.SourceFactor.CONSTANT_ALPHA, GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);
//            		float time = level.getLevelData().getGameTime() + partialTicks;
//            		double offset = Math.sin(time * 4.2 / 16.0D) / 20.0D;
//            		float rotation = (UtilMcTimer.renderTimer + partialTicks) / 2F;
//                    GL14.glBlendColor(1.0F, 1.0F, 1.0F, 0.6f + (float) offset * 4);
//                },
//                () -> {
//                    GL14.glBlendColor(1.0F, 1.0F, 1.0F, 1.0F);
//                    RenderSystem.disableBlend();
//                    RenderSystem.defaultBlendFunc();
//                });
//        
//        return create("eyes", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder()
//        		.setShaderState(RENDERTYPE_COLLECTOR_PORTAL_SHADER)
//    			.setTextureState(RenderStateShard.MultiTextureStateShard.builder()
//    					.add(locationIn, false, false)
//    					.add(new ResourceLocation("textures/environment/end_sky.png"), false, false)
//    					.add(new ResourceLocation("textures/entity/end_portal.png"), false, false)
//    					.build())
//        		.setTransparencyState(CORE_TRANSPARENCY)
//				.setWriteMaskState(COLOR_WRITE)
//        		.createCompositeState(false));
//    }

    public static RenderType getMungusBeam(ResourceLocation guardianBeamTexture)
    {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_EYES_SHADER).setTextureState(new RenderStateShard.TextureStateShard(guardianBeamTexture, false, false)).setTransparencyState(WORM_TRANSPARANCY).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(true);
        return create("mungus", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, rendertype$compositestate);
    }
    //////
    
//    public static RenderType collectorPortal()
//    {
//        return COLLECTOR_PORTAL;
//    }
    
	public static final RenderType SOLID_COLOUR = create(WitherUtils.MODID + ":solidcolour",
			DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, BUFFERSIZE, CRUMBLING, SORT,
			RenderType.CompositeState.builder()
					.setShaderState(RenderStateShard.ShaderStateShard.RENDERTYPE_LINES_SHADER)
					.setLayeringState(VIEW_OFFSET_Z_LAYERING).setOutputState(ITEM_ENTITY_TARGET)
					.setTransparencyState(ADDITIVE_TRANSPARENCY).setTextureState(NO_TEXTURE)
					.setDepthTestState(NO_DEPTH_TEST).setCullState(CULL).setLightmapState(NO_LIGHTMAP)
					.setWriteMaskState(COLOR_DEPTH_WRITE).createCompositeState(false));
	
	  public static final RenderType TOMB_LINES = create(WitherUtils.MODID + ":tomb_lines",
		      DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.LINES, BUFFERSIZE, CRUMBLING, SORT,
		      RenderType.CompositeState.builder()
		          .setShaderState(RENDERTYPE_LINES_SHADER)
		          .setLineState(new LineStateShard(OptionalDouble.of(2.5D)))
		          .setLayeringState(VIEW_OFFSET_Z_LAYERING)
		          .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
		          .setOutputState(ITEM_ENTITY_TARGET)
		          .setWriteMaskState(COLOR_DEPTH_WRITE)
		          .setCullState(NO_CULL)
		          .setDepthTestState(NO_DEPTH_TEST)
		          .createCompositeState(false));

	  public static final RenderType RESIZABLE = create(WitherUtils.MODID + ":resizable_cuboid", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, true, false,
		      RenderType.CompositeState.builder()
		          .setShaderState(RENDERTYPE_CUTOUT_SHADER)
		          .setTextureState(new RenderStateShard.TextureStateShard(InventoryMenu.BLOCK_ATLAS, false, false))
		          .setCullState(CULL)
		          .setLightmapState(LIGHTMAP)
		          .setWriteMaskState(COLOR_WRITE)
		          .setLightmapState(LIGHTMAP)
		          .createCompositeState(true));

	protected static final RenderStateShard.TransparencyStateShard BLENDED_NO_DEPT = new RenderStateShard.TransparencyStateShard("blended_no_dept", () -> {
		RenderSystem.enableBlend();
		RenderSystem.depthMask(false);
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
	}, () -> {
		RenderSystem.disableBlend();
		RenderSystem.depthMask(true);
		RenderSystem.defaultBlendFunc();
	});
	    
	public static RenderType entityBlendedNoDept(ResourceLocation location)
	{
		return makeBlendNoDept(location, true);
	}

	public static RenderType makeBlendNoDept(ResourceLocation location, boolean b)
	{
        CompositeState state = CompositeState.builder().setTextureState(new TextureStateShard(location, false, false))
                .setTransparencyState(BLENDED_NO_DEPT)
                .setShaderState(RenderStateShard.POSITION_COLOR_TEX_SHADER)
                .setCullState(NO_CULL)
                .setLightmapState(NO_LIGHTMAP).createCompositeState(true);
        return create("blend_bo_dept", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, true, true, state);
	}
}