package geni.witherutils.base.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class ArmorEyesLayer extends RenderLayer {

	private static final RenderType WITHERMONK_EYES = RenderType.eyes(ResourceLocation.withDefaultNamespace("witherutils:textures/model/armor/withersteel_armor_eyes.png"));
	
	private RenderLayerParent parent;

	public ArmorEyesLayer(RenderLayerParent parent)
    {
        super(parent);
        this.parent = parent;
    }

    @Override
    public void render(PoseStack matrix, MultiBufferSource buffer, int packedLightIn, Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) 
    {
//		VertexConsumer vertexconsumer = buffer.getBuffer(this.renderType());
//		this.getParentModel().renderToBuffer(matrix, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	}
    
	public RenderType renderType()
	{
		return WITHERMONK_EYES;
	}
}