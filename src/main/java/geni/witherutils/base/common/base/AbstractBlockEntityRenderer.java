package geni.witherutils.base.common.base;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.math.Axis;

import geni.witherutils.WitherUtils;
import geni.witherutils.api.io.IOMode;
import geni.witherutils.base.client.model.special.SpecialModels;
import geni.witherutils.base.client.render.type.WUTRenderType;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.base.common.io.fluid.MachineFluidHandler;
import geni.witherutils.core.client.gui.widgets.io.IOModeMap;
import geni.witherutils.core.common.blockentity.WitherBlockEntity;
import geni.witherutils.core.common.math.Vector3;
import geni.witherutils.core.common.util.FacingUtil;
import geni.witherutils.core.common.util.ModelRenderUtil;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WitherUtils.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public abstract class AbstractBlockEntityRenderer<T extends WitherBlockEntity> implements BlockEntityRenderer<T> {
	
	protected ItemRenderer itemRenderer;
	protected BlockRenderDispatcher blockRenderer;
	
	private static final Map<Direction, Quaternionf> ROTATE_FOR_FACING = Util.make(
			new EnumMap<>(Direction.class), m -> {
				for(Direction facing : FacingUtil.BY_HORIZONTAL_INDEX)
					m.put(facing, new Quaternionf().rotateY(Mth.DEG_TO_RAD*(180-facing.toYRot())));
			}
	);

	protected static void rotateForFacingNoCentering(PoseStack stack, Direction facing)
	{
		stack.mulPose(ROTATE_FOR_FACING.get(facing));
	}

	protected static void rotateForFacing(PoseStack stack, Direction facing)
	{
		stack.translate(0.5, 0.5, 0.5);
		rotateForFacingNoCentering(stack, facing);
		stack.translate(-0.5, -0.5, -0.5);
	}
	
	public static final Map<IOMode, TextureAtlasSprite> overlays = new EnumMap<>(IOMode.class);
	
    public static final ResourceLocation EMISSIVE = new ResourceLocation("witherutils:textures/block/emissive/blue.png");
	
	private boolean hasStatusLight = false;
	private boolean hasGlowEffect = false;
	
    protected static RenderType ATYPE = createRenderType();
	
    public static RenderType createRenderType()
    {
        RenderType.CompositeState state = RenderType.CompositeState.builder()
                .setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionColorShader))
                .setTransparencyState(new RenderStateShard.TransparencyStateShard("translucent_transparency", () -> {
                    RenderSystem.enableBlend();
                    RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                }, () -> {
                    RenderSystem.disableBlend();
                    RenderSystem.defaultBlendFunc();
                })).createCompositeState(true);
        return RenderType.create("working_area_render", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, true, state);
    }
    
    protected final BlockEntityRendererProvider.Context context;
    
	protected AbstractBlockEntityRenderer(BlockEntityRendererProvider.Context context)
	{
        this.context = context;

        itemRenderer = Minecraft.getInstance().getItemRenderer();
        blockRenderer = Minecraft.getInstance().getBlockRenderer();
	}

    protected AbstractBlockEntityRenderer<T> setHasStatusLight()
    {
        hasStatusLight = true;
        return this;
    }
    protected AbstractBlockEntityRenderer<T> setHasGlowEffect()
    {
        hasGlowEffect = true;
        return this;
    }
    
    @Override
    public final void render(T te, float partialTick, PoseStack matrix, MultiBufferSource buffer, int light, int overlayLight)
    {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        
        if (player != null)
        {
            render(te, partialTick, matrix, buffer, mc, player.clientLevel, player, light, overlayLight);
        }
        
        if(te instanceof WitherMachineBlockEntity)
        {
        	WitherMachineBlockEntity machine = (WitherMachineBlockEntity) te;
        	
        	checkPlugs(machine, partialTick, matrix, buffer, light, overlayLight);
        	
        	if(machine.getIOConfig() != null)
            	renderSelection(machine, matrix, mc);
        }
        
//        if(te instanceof WitherMachineFakeBlockEntity || te instanceof WitherMachineEnergyFakeBlockEntity)
//        {
//            WUTFakePlayer.renderPlacementEmblem(partialTick, matrix, buffer, mc, player.clientLevel, light, overlayLight);
//        }

        if(this.hasStatusLight)
        {
            renderSpecialFacingModel(SpecialModels.STATUS_LIGHT.getModel(), ItemDisplayContext.NONE, false, matrix, buffer, -1, light, OverlayTexture.NO_OVERLAY, WUTRenderType.eyes(new ResourceLocation("witherutils:textures/block/emissive/" + getStatusTexture((WitherMachineEnergyBlockEntity) te) + ".png")), te.getCurrentFacing());
        }
        if(this.hasGlowEffect)
        {
//            renderGlowBoxModel(SpecialModels.GLOW.getModel(), ItemDisplayContext.NONE, false, matrix, buffer, -1, light, OverlayTexture.NO_OVERLAY, RenderType.entityTranslucentEmissive(new ResourceLocation("witherutils:textures/block/emissive/blue.png")));
//            renderGlowBoxModel(SpecialModels.GLOW.getModel(), ItemDisplayContext.NONE, false, matrix, buffer, -1, light, OverlayTexture.NO_OVERLAY, RenderTypes.getSpiritTranslucent(new ResourceLocation("witherutils:textures/block/emissive/blue.png")));
        }
    }

    public abstract void render(T te, float partialTick, PoseStack matrix, MultiBufferSource buffer, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int overlayLight);

	public static void renderBlock(BlockEntity be, PoseStack matrix, Block block)
    {
		matrix.pushPose();
		BlockState state = block.defaultBlockState();
		for (RenderType layer : Minecraft.getInstance().getBlockRenderer().getBlockModel(state).getRenderTypes(state, RandomSource.create(state.getSeed(be.getBlockPos())), ModelData.EMPTY))
		{
			Minecraft.getInstance().getBlockRenderer().getModelRenderer().tesselateBlock(
					be.getLevel(),
					Minecraft.getInstance().getBlockRenderer().getBlockModel(state),
					state,
					be.getBlockPos(),
					matrix,
					Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.solid()),
					true,
					RandomSource.create(),
					state.getSeed(be.getBlockPos()),
					OverlayTexture.NO_OVERLAY,
					ModelData.EMPTY,
					layer);
		}
		matrix.popPose();
    }

	/*
	 * 
	 * NONFACING
	 * 
	 */
	public static void renderSpecialModel(BakedModel model, ItemDisplayContext transformType, boolean leftHanded, PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int color, int lightTexture, int overlayTexture, RenderType renderType)
    {
        matrixStack.pushPose();
        net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(matrixStack, model, transformType, leftHanded);
        
        if(!model.isCustomRenderer())
        {
            VertexConsumer vertexBuilder = renderTypeBuffer.getBuffer(renderType);
            Minecraft.getInstance().getItemRenderer().renderModelLists(model, ItemStack.EMPTY, lightTexture, overlayTexture, matrixStack, vertexBuilder);
        }
        matrixStack.popPose();
    }
	
	/*
	 * 
	 * FACING
	 * 
	 */
    public static void renderSpecialFacingModel(BakedModel model, ItemDisplayContext transformType, boolean leftHanded, PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int color, int lightTexture, int overlayTexture, RenderType renderType, @Nullable Direction facing)
    {
        net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(matrixStack, model, transformType, leftHanded);
        if(!model.isCustomRenderer())
        {
            matrixStack.pushPose();
            
            double x = Vector3.CENTER.x - 0.5F;
            double y = Vector3.CENTER.y - 0.5F;
            double z = Vector3.CENTER.z - 0.5F;

            matrixStack.translate(x, y, z);
            
            if(facing != null)
            {
                matrixStack.translate(0.5, 0.5, 0.5);
                if(facing == Direction.NORTH)
                {
                }
                else if(facing == Direction.SOUTH)
                {
                    matrixStack.mulPose(Axis.YN.rotationDegrees(180));
                }
                else if(facing == Direction.EAST)
                {
                    matrixStack.mulPose(Axis.YN.rotationDegrees(90));
                }
                else if(facing == Direction.WEST)
                {
                    matrixStack.mulPose(Axis.YP.rotationDegrees(90));
                }
                else if(facing == Direction.UP)
                {
                    matrixStack.mulPose(Axis.XP.rotationDegrees(90));
                }
                else if(facing == Direction.DOWN)
                {
                    matrixStack.mulPose(Axis.XN.rotationDegrees(90));
                }
                matrixStack.translate(-0.5, -0.5, -0.5);
            }
            VertexConsumer vertexBuilder = renderTypeBuffer.getBuffer(renderType);
            Minecraft.getInstance().getItemRenderer().renderModelLists(model, ItemStack.EMPTY, lightTexture, overlayTexture, matrixStack, vertexBuilder);
            
            matrixStack.popPose();
        }
    }
    
	/*
	 * 
	 * FACING ROTATION OFFSET
	 * 
	 */
    public static void renderSpecialFacingModel(BakedModel model, ItemDisplayContext transformType, boolean leftHanded, PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int color, int lightTexture, int overlayTexture, RenderType renderType, @Nullable Direction facing, float rotation, float offset)
    {
        net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(matrixStack, model, transformType, leftHanded);
        if(!model.isCustomRenderer())
        {
            matrixStack.pushPose();
            
            double x = Vector3.CENTER.x - 0.5F;
            double y = Vector3.CENTER.y - 0.5F;
            double z = Vector3.CENTER.z - 0.5F;

            matrixStack.translate(x, y, z);
            
            if(facing != null)
            {
                matrixStack.translate(0.5, 0.5, 0.5);
                if(facing == Direction.NORTH)
                {
                	matrixStack.translate(0.0, 0.0, -offset);
                }
                else if(facing == Direction.SOUTH)
                {
                	matrixStack.translate(0.0, 0.0, offset);
                    matrixStack.mulPose(Axis.YN.rotationDegrees(180));
                }
                else if(facing == Direction.EAST)
                {
                	matrixStack.translate(offset, 0.0, 0.0);
                    matrixStack.mulPose(Axis.YN.rotationDegrees(90));
                }
                else if(facing == Direction.WEST)
                {
                	matrixStack.translate(-offset, 0.0, 0.0);
                    matrixStack.mulPose(Axis.YP.rotationDegrees(90));
                }
                else if(facing == Direction.UP)
                {
                	matrixStack.translate(0.0, offset, 0.0);
                    matrixStack.mulPose(Axis.XP.rotationDegrees(90));
                }
                else if(facing == Direction.DOWN)
                {
                	matrixStack.translate(0.0, -offset, 0.0);
                    matrixStack.mulPose(Axis.XN.rotationDegrees(90));
                }
                matrixStack.mulPose(Axis.XN.rotationDegrees(rotation));
                matrixStack.translate(-0.5, -0.5, -0.5);
            }
            VertexConsumer vertexBuilder = renderTypeBuffer.getBuffer(renderType);
            Minecraft.getInstance().getItemRenderer().renderModelLists(model, ItemStack.EMPTY, lightTexture, overlayTexture, matrixStack, vertexBuilder);
            
            matrixStack.popPose();
        }
    }
    
    @Override
    public boolean shouldRenderOffScreen(T te)
    {
        return true;
    }
    @Override
    public int getViewDistance()
    {
        return 128;
    }
    
    /*
     * 
     * STATUSLIGHT
     * 
     */
    public String getStatusTexture(WitherMachineEnergyBlockEntity te)
    {
        String colorString;
        if(te.getSoulBankItem() != null)
        {
            if(te.getEnergyStorage().getEnergyStored() > 0)
                colorString = "green";
            else
                colorString = "yellow";
        }
        else
            colorString = "red";

        if(te.getInventory().getStackInSlot(1).getCount() >= 64 && te.getLevel().getGameTime() % 10 == 0)
        {
            if(colorString == "yellow")
                colorString = "red";
            else
                colorString = "yellow";
        }
        
        return colorString;
    }
    
    /*
     * 
     * STATUSTEXT
     * 
     */
    @SuppressWarnings({ "resource", "incomplete-switch" })
    public void renderTextHorizontalFacing(PoseStack matrix, MultiBufferSource renderer, int overlayLight, Component text, Direction side, float maxScale)
    {
        matrix.pushPose();
        matrix.translate(0, -0.3725, 0);
        
        switch (side)
        {
            case SOUTH:
                matrix.translate(0, 1, 0.0001);
                matrix.mulPose(Axis.XP.rotationDegrees(90));
                break;
            case NORTH:
                matrix.translate(1, 1, 0.9999);
                matrix.mulPose(Axis.YP.rotationDegrees(180));
                matrix.mulPose(Axis.XP.rotationDegrees(90));
                break;
            case EAST:
                matrix.translate(0.0001, 1, 1);
                matrix.mulPose(Axis.YP.rotationDegrees(90));
                matrix.mulPose(Axis.XP.rotationDegrees(90));
                break;
            case WEST:
                matrix.translate(0.9999, 1, 0);
                matrix.mulPose(Axis.YP.rotationDegrees(-90));
                matrix.mulPose(Axis.XP.rotationDegrees(90));
                break;
        }

        float displayWidth = 1;
        float displayHeight = 1;
        matrix.translate(displayWidth / 2, 1, displayHeight / 2);
        matrix.mulPose(Axis.XP.rotationDegrees(-90));

        Font font = Minecraft.getInstance().font;

        int requiredWidth = Math.max(font.width(text), 1);
        int requiredHeight = font.lineHeight + 2;
        float scaler = 0.4F;
        float scaleX = displayWidth / requiredWidth;
        float scale = scaleX * scaler;
        
        if (maxScale > 0)
        {
            scale = Math.min(scale, maxScale);
        }

        matrix.scale(scale, -scale, scale);
        int realHeight = (int) Math.floor(displayHeight / scale);
        int realWidth = (int) Math.floor(displayWidth / scale);
        int offsetX = (realWidth - requiredWidth) / 2;
        int offsetY = (realHeight - requiredHeight) / 2;
        font.drawInBatch(text, (float) offsetX - realWidth / 2, (float) 3 + offsetY - realHeight / 2, overlayLight, false, matrix.last().pose(), renderer, DisplayMode.NORMAL, 0, 0xF000F0);
        
        matrix.popPose();
    }
    
	public void renderTextAboveCamera(PoseStack matrix, MultiBufferSource renderer, int overlayLight, Component text, float maxScale, Minecraft mc)
    {    
		float opacity = mc.options.getBackgroundOpacity(0.25F);
		int j = (int) (opacity * 255.0F) << 24;
		float halfWidth = -mc.font.width(text) / 2;
		Matrix4f positionMatrix;
    	matrix.pushPose();
    	matrix.translate(0.5D, 1.25D, 0.5D);
    	matrix.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
    	matrix.scale(-0.010F, -0.010F, 0.010F);
		positionMatrix = matrix.last().pose();
		mc.font.drawInBatch(text, halfWidth, 0, 553648127, false, positionMatrix, renderer, DisplayMode.SEE_THROUGH, j, overlayLight);
		mc.font.drawInBatch(text, halfWidth, 0, -1, false, positionMatrix, renderer, DisplayMode.NORMAL, 0, overlayLight);
    	matrix.popPose();
    }
	
	public void renderText(PoseStack matrix, MultiBufferSource renderer, int overlayLight, Component text, float maxScale, Minecraft mc)
    {
        matrix.pushPose();
        matrix.translate(0, -0.3725, 0);
        matrix.translate(0.5D, 1.5D, 0.5D);
        matrix.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());

        float displayWidth = 1;
        float displayHeight = 1;
        matrix.translate(displayWidth / 2, 1, displayHeight / 2);
        matrix.mulPose(Axis.XP.rotationDegrees(-90));

        Font font = mc.font;

        int requiredWidth = Math.max(font.width(text), 1);
        int requiredHeight = font.lineHeight + 2;
        float scaler = 0.4F;
        float scaleX = displayWidth / requiredWidth;
        float scale = scaleX * scaler;
        
        if (maxScale > 0)
        {
            scale = Math.min(scale, maxScale);
        }

        matrix.scale(scale, -scale, scale);
        int realHeight = (int) Math.floor(displayHeight / scale);
        int realWidth = (int) Math.floor(displayWidth / scale);
        int offsetX = (realWidth - requiredWidth) / 2;
        int offsetY = (realHeight - requiredHeight) / 2;
        font.drawInBatch(text, (float) offsetX - realWidth / 2, (float) 3 + offsetY - realHeight / 2, overlayLight, false, matrix.last().pose(), renderer, DisplayMode.NORMAL, 0, 0xF000F0);
        
        matrix.popPose();
    }
    
    /*
     * 
     * GLOWBOX
     * 
     */
    public static void renderGlowBoxModel(BakedModel model, ItemDisplayContext transformType, boolean leftHanded, PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int color, int lightTexture, int overlayTexture, RenderType renderType)
    {
        matrixStack.pushPose();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        VertexConsumer vertexBuilder = renderTypeBuffer.getBuffer(renderType);
        Minecraft.getInstance().getItemRenderer().renderModelLists(model, ItemStack.EMPTY, lightTexture, overlayTexture, matrixStack, vertexBuilder);
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        matrixStack.popPose();
    }
    
    @SuppressWarnings("unused")
    private void renderCuboid(VertexConsumer buffer, PoseStack matrixStack, float xMax, float xMin, float yMin, float height, float zMin, float zMax, TextureAtlasSprite textureAtlasSprite, float red, float green, float blue, float alpha, int combinedLight)
    {
        float uMin = textureAtlasSprite.getU0();
        float uMax = textureAtlasSprite.getU1();
        float vMin = textureAtlasSprite.getV0();
        float vMax = textureAtlasSprite.getV1();

        float vHeight = vMax - vMin;

        // top
        addVertexWithUV(buffer, matrixStack, xMax, height, zMax, uMax, vMin, red, green, blue, alpha, combinedLight);
        addVertexWithUV(buffer, matrixStack, xMax, height, zMin, uMin, vMin, red, green, blue, alpha, combinedLight);
        addVertexWithUV(buffer, matrixStack, xMin, height, zMin, uMin, vMax, red, green, blue, alpha, combinedLight);
        addVertexWithUV(buffer, matrixStack, xMin, height, zMax, uMax, vMax, red, green, blue, alpha, combinedLight);

        // north
        addVertexWithUV(buffer, matrixStack, xMax, yMin, zMin, uMax, vMin, red, green, blue, alpha, combinedLight);
        addVertexWithUV(buffer, matrixStack, xMin, yMin, zMin, uMin, vMin, red, green, blue, alpha, combinedLight);
        addVertexWithUV(buffer, matrixStack, xMin, height, zMin, uMin, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
        addVertexWithUV(buffer, matrixStack, xMax, height, zMin, uMax, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);

        // south
        addVertexWithUV(buffer, matrixStack, xMax, yMin, zMax, uMin, vMin, red, green, blue, alpha, combinedLight);
        addVertexWithUV(buffer, matrixStack, xMax, height, zMax, uMin, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
        addVertexWithUV(buffer, matrixStack, xMin, height, zMax, uMax, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
        addVertexWithUV(buffer, matrixStack, xMin, yMin, zMax, uMax, vMin, red, green, blue, alpha, combinedLight);

        // east
        addVertexWithUV(buffer, matrixStack, xMax, yMin, zMin, uMin, vMin, red, green, blue, alpha, combinedLight);
        addVertexWithUV(buffer, matrixStack, xMax, height, zMin, uMin, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
        addVertexWithUV(buffer, matrixStack, xMax, height, zMax, uMax, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
        addVertexWithUV(buffer, matrixStack, xMax, yMin, zMax, uMax, vMin, red, green, blue, alpha, combinedLight);

        // west
        addVertexWithUV(buffer, matrixStack, xMin, yMin, zMax, uMin, vMin, red, green, blue, alpha, combinedLight);
        addVertexWithUV(buffer, matrixStack, xMin, height, zMax, uMin, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
        addVertexWithUV(buffer, matrixStack, xMin, height, zMin, uMax, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
        addVertexWithUV(buffer, matrixStack, xMin, yMin, zMin, uMax, vMin, red, green, blue, alpha, combinedLight);

        // down
        addVertexWithUV(buffer, matrixStack, xMax, yMin, zMin, uMax, vMin, red, green, blue, alpha, combinedLight);
        addVertexWithUV(buffer, matrixStack, xMax, yMin, zMax, uMin, vMin, red, green, blue, alpha, combinedLight);
        addVertexWithUV(buffer, matrixStack, xMin, yMin, zMax, uMin, vMax, red, green, blue, alpha, combinedLight);
        addVertexWithUV(buffer, matrixStack, xMin, yMin, zMin, uMax, vMax, red, green, blue, alpha, combinedLight);
    }

    /*
     * 
     * SIDECONFIG
     * 
     */
    private void renderSelection(WitherMachineBlockEntity tile, PoseStack poseStack, Minecraft mc)
    {
    	if(tile.getIOConfig() == null)
    		return;
    	
    	boolean isHoldingWrench = mc.player.getMainHandItem().getItem() == WUTItems.WRENCH.get();
    	
    	if (isHoldingWrench && tile != null && tile.getBlockPos() != null && mc.hitResult instanceof BlockHitResult hitResult && tile.getBlockPos().equals(hitResult.getBlockPos()))
    	{
        	for(Direction side : Direction.values())
        	{
            	poseStack.pushPose();
            	
                double x = Vector3.CENTER.x - 0.5F;
                double y = Vector3.CENTER.y - 0.5F;
                double z = Vector3.CENTER.z - 0.5F;

                poseStack.translate(x, y, z);

                var ioMode = tile.getIOConfig().getMode(side);
                IOModeMap map = IOModeMap.getMapFromMode(ioMode);

            	BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
            	RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
            	TextureAtlasSprite tex = mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(WitherUtils.loc("block/overlay/" + map.name().toLowerCase()));
            	RenderSystem.setShaderTexture(0, tex.atlasLocation());
            	RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            	bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);

            	Vector3f[] vec = Arrays.stream(ModelRenderUtil.createQuadVerts(side, 0, 1, 1)).map(Vec3::toVector3f).toArray(Vector3f[]::new);

            	Matrix4f matrix4f = poseStack.last().pose();

            	bufferbuilder.vertex(matrix4f, vec[0].x(), vec[0].y(), vec[0].z()).color(1F, 1F, 1F, 1F).uv(tex.getU0(), tex.getV0()).endVertex();
            	bufferbuilder.vertex(matrix4f, vec[1].x(), vec[1].y(), vec[1].z()).color(1F, 1F, 1F, 1F).uv(tex.getU0(), tex.getV1()).endVertex();
            	bufferbuilder.vertex(matrix4f, vec[2].x(), vec[2].y(), vec[2].z()).color(1F, 1F, 1F, 1F).uv(tex.getU1(), tex.getV1()).endVertex();
            	bufferbuilder.vertex(matrix4f, vec[3].x(), vec[3].y(), vec[3].z()).color(1F, 1F, 1F, 1F).uv(tex.getU1(), tex.getV0()).endVertex();
          	  
            	BufferUploader.drawWithShader(bufferbuilder.end());
            	
            	poseStack.popPose();
        	}
    	}
    }
    
    /*
     * 
     * PLUGS
     * 
     */
	public void checkPlugs(WitherMachineBlockEntity te, float v, PoseStack matrix, MultiBufferSource renderer, int light, int overlayLight)
    {
        for(Direction side : Direction.values())
        {
            BlockPos pos = te.getBlockPos().relative(side);
            if(!te.getLevel().getBlockState(pos).isAir() && !(te.getLevel().getBlockState(pos).getBlock() instanceof LiquidBlock))
            {
                BlockEntity be = te.getLevel().getBlockEntity(pos);
                if(be != null && !(be instanceof WitherMachineBlockEntity))
                {
                    if(be.getCapability(ForgeCapabilities.ENERGY, side) != null
                            || be.getCapability(ForgeCapabilities.FLUID_HANDLER, side) != null
                            || be.getCapability(ForgeCapabilities.ITEM_HANDLER, side) != null)
                    {
                        matrix.pushPose();
                        renderSpecialFacingModel(SpecialModels.PLUG.getModel(), ItemDisplayContext.NONE, false, matrix, renderer, -1, light, OverlayTexture.NO_OVERLAY, WUTRenderType.cutout(), side);
                        renderSpecialFacingModel(SpecialModels.PLUG_LIGHT.getModel(), ItemDisplayContext.NONE, false, matrix, renderer, -1, light, OverlayTexture.NO_OVERLAY, WUTRenderType.eyes(EMISSIVE), side);
                        matrix.popPose();
                    }
                }
                else
                {
                	if(!(be instanceof WitherMachineBlockEntity))
					{
            			for(Property<?> prop : te.getLevel().getBlockState(pos).getBlock().defaultBlockState().getProperties())
            			{
            				if(prop instanceof BooleanProperty bool)
            				{
            					if(bool.getName() == "has_data")
            					{
                                    matrix.pushPose();
                                    renderSpecialFacingModel(SpecialModels.PLUG.getModel(), ItemDisplayContext.NONE, false, matrix, renderer, -1, light, OverlayTexture.NO_OVERLAY, WUTRenderType.cutout(), side);
                                    renderSpecialFacingModel(SpecialModels.PLUG_LIGHT.getModel(), ItemDisplayContext.NONE, false, matrix, renderer, -1, light, OverlayTexture.NO_OVERLAY, WUTRenderType.eyes(EMISSIVE), side);
                                    matrix.popPose();
            					}
            				}
            			}
					}
                }
            }
        }
    }
    
    /*
     * 
     * NEW
     * 
     */
    public static void renderColorOverlay(PoseStack matrix, int x, int y, int width, int height, int color)
    {
        int r = FastColor.ARGB32.red(color);
        int g = FastColor.ARGB32.green(color);
        int b = FastColor.ARGB32.blue(color);
        int a = FastColor.ARGB32.alpha(color);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        Matrix4f matrix4f = matrix.last().pose();
        bufferbuilder.vertex(matrix4f, width, y, 0).color(r, g, b, a).endVertex();
        bufferbuilder.vertex(matrix4f, x, y, 0).color(r, g, b, a).endVertex();
        bufferbuilder.vertex(matrix4f, x, height, 0).color(r, g, b, a).endVertex();
        bufferbuilder.vertex(matrix4f, width, height, 0).color(r, g, b, a).endVertex();
        tesselator.end();
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
    }
    
    public static float getPartialTick()
    {
        return Minecraft.getInstance().getFrameTime();
    }

    @SuppressWarnings("incomplete-switch")
	public static void rotate(PoseStack matrix, Direction facing, float north, float south, float west, float east)
    {
        switch (facing)
        {
            case NORTH -> matrix.mulPose(Axis.YP.rotationDegrees(north));
            case SOUTH -> matrix.mulPose(Axis.YP.rotationDegrees(south));
            case WEST -> matrix.mulPose(Axis.YP.rotationDegrees(west));
            case EAST -> matrix.mulPose(Axis.YP.rotationDegrees(east));
        }
    }

    @SuppressWarnings("deprecation")
	@SubscribeEvent
    public static void onStitch(TextureStitchEvent.Post event)
    {
        TextureAtlas map = event.getAtlas();
        if (!map.location().equals(TextureAtlas.LOCATION_BLOCKS))
        {
            return;
        }
        for (IOMode mode : IOMode.values())
        {
            overlays.put(mode, map.getSprite(WitherUtils.loc("block/overlay/" + mode.name().toLowerCase())));
        }
    }
    
    @SuppressWarnings("deprecation")
	public static TextureAtlasSprite getSprite(ResourceLocation spriteLocation)
    {
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(spriteLocation);
    }
    
    public static TextureAtlasSprite getFluidTexture(@NotNull FluidStack fluidStack, @NotNull FluidTextureType type)
    {
        IClientFluidTypeExtensions properties = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        ResourceLocation spriteLocation;
        if (type == FluidTextureType.STILL)
        {
            spriteLocation = properties.getStillTexture(fluidStack);
        }
        else
        {
            spriteLocation = properties.getFlowingTexture(fluidStack);
        }
        return getSprite(spriteLocation);
    }
    
    public enum FluidTextureType
    {
        STILL,
        FLOWING
    }

    public static class Model3D {

        public float minX, minY, minZ;
        public float maxX, maxY, maxZ;

        private final SpriteInfo[] textures = new SpriteInfo[6];
        private final boolean[] renderSides = {true, true, true, true, true, true};

        public Model3D setSideRender(Predicate<Direction> shouldRender)
        {
            for (Direction direction : Direction.values())
            {
                setSideRender(direction, shouldRender.test(direction));
            }
            return this;
        }

        public Model3D setSideRender(Direction side, boolean value)
        {
            renderSides[side.ordinal()] = value;
            return this;
        }

        public Model3D copy()
        {
            Model3D copy = new Model3D();
            System.arraycopy(textures, 0, copy.textures, 0, textures.length);
            System.arraycopy(renderSides, 0, copy.renderSides, 0, renderSides.length);
            return copy.bounds(minX, minY, minZ, maxX, maxY, maxZ);
        }

        @Nullable
        public SpriteInfo getSpriteToRender(Direction side)
        {
            int ordinal = side.ordinal();
            return renderSides[ordinal] ? textures[ordinal] : null;
        }

        public Model3D shrink(float amount)
        {
            return grow(-amount);
        }

        public Model3D grow(float amount)
        {
            return bounds(minX - amount, minY - amount, minZ - amount, maxX + amount, maxY + amount, maxZ + amount);
        }

        public Model3D xBounds(float min, float max)
        {
            this.minX = min;
            this.maxX = max;
            return this;
        }

        public Model3D yBounds(float min, float max)
        {
            this.minY = min;
            this.maxY = max;
            return this;
        }

        public Model3D zBounds(float min, float max)
        {
            this.minZ = min;
            this.maxZ = max;
            return this;
        }

        public Model3D bounds(float min, float max)
        {
            return bounds(min, min, min, max, max, max);
        }

        public Model3D bounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ)
        {
            return xBounds(minX, maxX)
                  .yBounds(minY, maxY)
                  .zBounds(minZ, maxZ);
        }

        public Model3D prepSingleFaceModelSize(Direction face)
        {
            bounds(0, 1);
            return switch (face) {
                case DOWN -> yBounds(-0.01F, -0.001F);
                case UP -> yBounds(1.001F, 1.01F);
                case NORTH -> zBounds(-0.01F, -0.001F);
                case SOUTH -> zBounds(1.001F, 1.01F);
                case WEST -> xBounds(-0.01F, -0.001F);
                case EAST -> xBounds(1.001F, 1.01F);
            };
        }

        public Model3D prepFlowing(@NotNull FluidStack fluid)
        {
            SpriteInfo still = new SpriteInfo(getFluidTexture(fluid, FluidTextureType.STILL), 16);
            SpriteInfo flowing = new SpriteInfo(getFluidTexture(fluid, FluidTextureType.FLOWING), 8);
            return setTextures(still, still, flowing, flowing, flowing, flowing);
        }

        public Model3D setTexture(Direction side, @Nullable SpriteInfo spriteInfo)
        {
            textures[side.ordinal()] = spriteInfo;
            return this;
        }

        public Model3D setTexture(TextureAtlasSprite tex)
        {
            return setTexture(tex, 16);
        }

        public Model3D setTexture(TextureAtlasSprite tex, int size)
        {
            Arrays.fill(textures, new SpriteInfo(tex, size));
            return this;
        }

        public Model3D setTextures(SpriteInfo down, SpriteInfo up, SpriteInfo north, SpriteInfo south, SpriteInfo west, SpriteInfo east)
        {
            textures[0] = down;
            textures[1] = up;
            textures[2] = north;
            textures[3] = south;
            textures[4] = west;
            textures[5] = east;
            return this;
        }

        public record SpriteInfo(TextureAtlasSprite sprite, int size)
        {
            public float getU(float u)
            {
                return sprite.getU(u * size);
            }
            public float getV(float v)
            {
                return sprite.getV(v * size);
            }
        }

        public interface ModelBoundsSetter
        {

            Model3D set(float min, float max);
        }
    }

    public static class LazyModel implements Supplier<Model3D> {

        private final Supplier<Model3D> supplier;
        @Nullable
        private Model3D model;

        public LazyModel(Supplier<Model3D> supplier)
        {
            this.supplier = supplier;
        }

        public void reset()
        {
            model = null;
        }

        @Override
        public Model3D get()
        {
            if (model == null)
            {
                model = supplier.get();
            }
            return model;
        }
    }
    
    public static float rotateMatrixForDirection(PoseStack matrixStack, Direction facing)
    {
        float yRotation;
        switch (facing)
        {
            case UP -> {
                yRotation = 0;
                matrixStack.mulPose(Axis.XP.rotationDegrees(90f));
                matrixStack.translate(0, -1, -1);
            }
            case DOWN -> {
                yRotation = 0;
                matrixStack.mulPose(Axis.XP.rotationDegrees(-90f));
                matrixStack.translate(0, -1, 1);
            }
            case NORTH -> yRotation = 0;
            case EAST -> yRotation = 90;
            case SOUTH -> yRotation = 180;
            default -> yRotation = 270;
        }
        matrixStack.mulPose(Axis.YP.rotationDegrees(yRotation));
        return yRotation;
    }
    
    /*
     * 
     * FLUIDTANK
     * 
     */
	public static void renderFluidTank(BlockEntity te, PoseStack matrixStack, MultiBufferSource renderer, int combinedLight, float alpha, float minX, float minY, float maxX, float minZ, float maxZ, float maxY)
	{
		IFluidHandler fluidHandler = te.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().get();
		if(fluidHandler instanceof MachineFluidHandler)
		{
			MachineFluidHandler machineFlHandler = (MachineFluidHandler) fluidHandler;
			
			if(machineFlHandler.getTank(0).getFluid().isEmpty())
				return;
			float fluidLevel = machineFlHandler.getTank(0).getFluidAmount();
			if(fluidLevel < 1)
				return;
			
			FluidStack fluidStack = new FluidStack(machineFlHandler.getTank(0).getFluid(), 100);
			float height = (0.96875F / machineFlHandler.getTank(0).getCapacity()) * machineFlHandler.getTank(0).getFluidAmount();

			render(te, matrixStack, renderer, combinedLight, fluidStack, alpha, minX, minY, minZ, maxX, maxY, maxZ, height);
		}
	}
	public static void render(BlockEntity te, PoseStack matrixStack, MultiBufferSource renderer, int combinedLight, FluidStack fluidStack, float alpha, float minX, float minY, float minZ, float maxX, float maxHeight, float maxZ, float height)
	{
		var fluidExtensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());
		int fluidColor = fluidExtensions.getTintColor();
		
		TextureAtlasSprite fluidStillSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidExtensions.getStillTexture());

		float red = (fluidColor >> 16 & 0xFF) / 255.0F;
		float green = (fluidColor >> 8 & 0xFF) / 255.0F;
		float blue = (fluidColor & 0xFF) / 255.0F;
		
		if(height >= maxHeight)
		{
			height = maxHeight;
		}

		float uMin = fluidStillSprite.getU0();
		float uMax = fluidStillSprite.getU1();
		float vMin = fluidStillSprite.getV0();
		float vMax = fluidStillSprite.getV1();

		float vHeight = vMax - vMin;
		
		VertexConsumer buffer = renderer.getBuffer(RenderType.translucent());
		
		addVertexWithUV(buffer, matrixStack, maxX, height, maxZ, uMax, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, maxX, height, minZ, uMin, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, minX, height, minZ, uMin, vMax, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, minX, height, maxZ, uMax, vMax, red, green, blue, alpha, combinedLight);

		addVertexWithUV(buffer, matrixStack, maxX, minY, minZ, uMax, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, minX, minY, minZ, uMin, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, minX, height, minZ, uMin, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, maxX, height, minZ, uMax, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);

		addVertexWithUV(buffer, matrixStack, maxX, minY, maxZ, uMin, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, maxX, height, maxZ, uMin, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, minX, height, maxZ, uMax, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, minX, minY, maxZ, uMax, vMin, red, green, blue, alpha, combinedLight);

		addVertexWithUV(buffer, matrixStack, maxX, minY, minZ, uMin, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, maxX, height, minZ, uMin, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, maxX, height, maxZ, uMax, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, maxX, minY, maxZ, uMax, vMin, red, green, blue, alpha, combinedLight);

		addVertexWithUV(buffer, matrixStack, minX, minY, maxZ, uMin, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, minX, height, maxZ, uMin, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, minX, height, minZ, uMax, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, minX, minY, minZ, uMax, vMin, red, green, blue, alpha, combinedLight);

		addVertexWithUV(buffer, matrixStack, maxX, minY, minZ, uMax, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, maxX, minY, maxZ, uMin, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, minX, minY, maxZ, uMin, vMax, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, minX, minY, minZ, uMax, vMax, red, green, blue, alpha, combinedLight);
	}
	
	/*
	 * 
	 * SOUL FIRE
	 * 
	 */
    @SuppressWarnings("unused")
	public void renderSoulFire(float partialTicks, PoseStack matrix, MultiBufferSource buffer, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int overlay, float modulation, float scaleX, float scaleY, float scaleZ, float x, float y, float z)
    {
        matrix.pushPose();

		float time = level.getLevelData().getGameTime() + partialTicks;
		
		float translateFire = (float) Math.sin(time * modulation / 8.0F) / 10.0F;
		float scaleFire = (float) Math.sin(time * modulation / 8.0F) / 10.0F;
		
        matrix.translate(0.5, 0.5, 0.5);
        matrix.scale(scaleX, scaleY + scaleFire, scaleZ);
        matrix.translate(x, y, z);
        matrix.translate(-0.5, -0.5, -0.5);

        VertexConsumer vertexBuilder = buffer.getBuffer(RenderType.cutout());
        Minecraft.getInstance().getItemRenderer().renderModelLists(SpecialModels.SOUL_FIRE.getModel(), ItemStack.EMPTY, light, OverlayTexture.NO_OVERLAY, matrix, vertexBuilder);
        matrix.popPose();
	}

	/*
	 * 
	 * ADDVERTEX
	 * 
	 */
	private static void addVertexWithUV(VertexConsumer buffer, PoseStack matrixStack, float x, float y, float z, float u, float v, float red, float green, float blue, float alpha, int combinedLight)
	{
		buffer.vertex(matrixStack.last().pose(), x / 2f, y, z / 2f).color(red, green, blue, alpha).uv(u, v).uv2(combinedLight, 240).normal(1, 0, 0).endVertex();
	}
}
