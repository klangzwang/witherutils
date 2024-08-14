package geni.witherutils.base.common.entity.cursed.dryhead;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.client.render.layer.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CursedDryHeadRenderer extends MobRenderer<CursedDryHead, CursedDryHeadModel<CursedDryHead>> {

	public CursedDryHeadRenderer(EntityRendererProvider.Context context)
	{
		super(context, new CursedDryHeadModel<>(context.bakeLayer(ModelLayers.CURSEDDRYHEAD)), 1.0f);
	}

    @Override
    public ResourceLocation getTextureLocation(CursedDryHead entity)
    {
    	return WitherUtilsRegistry.loc("textures/block/model/entity/dryhead_wings.png");
    }
    
    @Override
    public void render(CursedDryHead dryhead, float p_115456_, float p_115457_, PoseStack p_115458_, MultiBufferSource p_115459_, int p_115460_)
    {
    	CursedDryHeadModel<CursedDryHead> dryheadmodel = this.getModel();
    	dryheadmodel.isKamikaze = dryhead.getEntityData().get(CursedDryHead.DATA_KAMIKAZE);
        
    	super.render(dryhead, p_115456_, p_115457_, p_115458_, p_115459_, p_115460_);
    }
    
    @Override
    protected void scale(CursedDryHead p_115314_, PoseStack matrix, float p_115316_)
    {
    	matrix.scale(0.5F, 0.5F, 0.5F);
    }
    
    @Override
    protected void setupRotations(CursedDryHead pEntity, PoseStack pPoseStack, float pBob, float pYBodyRot, float pPartialTick, float pScale)
    {
    	super.setupRotations(pEntity, pPoseStack, pBob, pYBodyRot, pPartialTick, pScale);
    	pPoseStack.mulPose(Axis.XP.rotationDegrees(pEntity.getXRot()));
    }
}