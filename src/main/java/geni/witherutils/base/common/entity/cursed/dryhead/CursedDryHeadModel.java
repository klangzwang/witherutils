package geni.witherutils.base.common.entity.cursed.dryhead;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.client.model.special.SpecialModels;
import geni.witherutils.base.client.render.type.WUTRenderType;
import geni.witherutils.core.common.math.Vector3;
import geni.witherutils.core.common.util.McTimerUtil;
import geni.witherutils.core.common.util.TickTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public class CursedDryHeadModel<T extends CursedDryHead> extends HierarchicalModel<T> {

	public boolean isKamikaze;
	
	private final ModelPart head;

	private final ModelPart lWing1;
	private final ModelPart lWing2;
	private final ModelPart rWing1;
	private final ModelPart rWing2;
	
	public CursedDryHeadModel(ModelPart root)
	{
		this.head = root.getChild("head");

		this.lWing1 = this.head.getChild("lWing1");
		this.lWing2 = this.lWing1.getChild("lWing2");
		this.rWing1 = this.head.getChild("rWing1");
		this.rWing2 = this.rWing1.getChild("rWing2");
	}

	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		
		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), PartPose.ZERO);
		
		PartDefinition lWing1 = head.addOrReplaceChild("lWing1", CubeListBuilder.create().texOffs(1, 14).mirror().addBox(0.0F, -7.5F, 0.0F, 9.0F, 14.0F, 1.0F).mirror(false), PartPose.offsetAndRotation(8.0F, 19.0F, 0.0F, 0.2182F, 0.2182F, 0.0F));
		lWing1.addOrReplaceChild("lWing2", CubeListBuilder.create().texOffs(1, 1).mirror().addBox(0.0F, -7.5F, 0.0F, 9.0F, 11.0F, 1.0F).mirror(false), PartPose.offsetAndRotation(9.0F, 0.0F, 0.0F, 0.0F, 0.2182F, 0.0F));
		PartDefinition rWing1 = head.addOrReplaceChild("rWing1", CubeListBuilder.create().texOffs(1, 14).addBox(-9.0F, -7.5F, 0.0F, 9.0F, 14.0F, 1.0F), PartPose.offsetAndRotation(-8.0F, 19.0F, 0.0F, 0.2182F, -0.2182F, 0.0F));
		rWing1.addOrReplaceChild("rWing2", CubeListBuilder.create().texOffs(1, 1).addBox(-9.0F, -7.5F, 0.0F, 9.0F, 11.0F, 1.0F), PartPose.offsetAndRotation(-9.0F, 0.0F, 0.0F, 0.0F, -0.2182F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}
	
	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		float f = ((float)entity.getUniqueFlapTickOffset() + ageInTicks) * 110.448451F * ((float)Math.PI / 180F);
		this.lWing1.yRot = Mth.cos(f) * 32.0F * ((float)Math.PI / 180F);
		this.lWing2.yRot = Mth.cos(f) * 32.0F * ((float)Math.PI / 180F);
		this.rWing1.yRot = -this.lWing1.yRot;
		this.rWing2.yRot = -this.lWing2.yRot;
	}

	@Override
	public void renderToBuffer(@Nonnull PoseStack matrix, @Nonnull VertexConsumer vertexBuilder, int light, int overlayLight, int color)
	{
        matrix.pushPose();

        head.render(matrix, vertexBuilder, light, overlayLight, color);

        lWing1.y = -8.0F;
        rWing1.y = -8.0F;
        lWing2.y = 0.0F;
        rWing2.y = 0.0F;
        
        MultiBufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
		VertexConsumer vertex = buffer.getBuffer(WUTRenderType.cutout());
		matrix.scale(2, 2, 2);
		renderHead(matrix, buffer, 255, head.xRot, head.yRot, vertex);
		
        matrix.popPose();
	}

    @SuppressWarnings("resource")
	public void renderHead(PoseStack matrix, MultiBufferSource buffer, int light, float netHeadYaw, float headPitch, VertexConsumer vertex)
    {
		matrix.pushPose();

        double x = Vector3.CENTER.x - 1.0F;
        double y = Vector3.CENTER.y - 1.0F;
        double z = Vector3.CENTER.z - 1.0F;

        matrix.translate(x, y - 0.5, z);
		
		matrix.mulPose(Axis.XP.rotationDegrees(headPitch *  ((float)Math.PI / 180F)));
		matrix.mulPose(Axis.YP.rotationDegrees(netHeadYaw * ((float)Math.PI / 180F)));
        
        matrix.translate(0.5, 0.5, 0.5);
        matrix.mulPose(Axis.XP.rotationDegrees(180));
    	matrix.mulPose(Axis.YP.rotationDegrees(180));
        matrix.translate(-0.5, -0.5, -0.5);

		float time = Minecraft.getInstance().level.getLevelData().getGameTime() + Minecraft.getInstance().getFps();
		double offset = Math.sin(time * 0.1D) / 30.0D;
		
    	matrix.translate(0, offset, 0);
    	
//        matrix.translate(0.5, 3.0F + Mth.sin(McTimerUtil.renderPartialTickTime * 180 ) * 2, 0.5);
        
	    renderBakedModelLists(SpecialModels.DRYHEAD_HEAD.getModel(), matrix, vertex, light);
	    renderBakedModelLists(SpecialModels.DRYHEAD_HAIR.getModel(), matrix, vertex, light);

	    vertex = buffer.getBuffer(WUTRenderType.eyes(WitherUtilsRegistry.loc("textures/block/emissive/blue.png")));
	    renderBakedModelLists(SpecialModels.DRYHEAD_EYES.getModel(), matrix, vertex, light);

//		System.out.println("MODEL: " + this.isKamikaze);
	    
	    if(this.isKamikaze)
	    {
		    matrix.translate(0, offset - 0.440, -0.195);
		    
	        matrix.translate(0.5, 0.5, 0.5);
	        offset = -10 + Math.sin(time * 0.1D) / 30.0D * 275;
	        System.out.println(offset);
	        matrix.mulPose(Axis.XP.rotationDegrees(-20f + (float)offset));
	        matrix.translate(-0.5, -0.5, -0.5);
	    }
	    else
	    {
	        matrix.translate(0, -0.45, -0.2);
	    }
	    vertex = buffer.getBuffer(WUTRenderType.cutout());
	    renderBakedModelLists(SpecialModels.DRYHEAD_JAW.getModel(), matrix, vertex, light);

		matrix.popPose();
    }

    public void renderBakedModelLists(BakedModel model, PoseStack matrix, VertexConsumer vertex, int light)
    {
		Minecraft.getInstance().getItemRenderer().renderModelLists(model, ItemStack.EMPTY, light, OverlayTexture.NO_OVERLAY, matrix, vertex);
    }

	@Override
	public ModelPart root()
	{
		return head;
	}
}