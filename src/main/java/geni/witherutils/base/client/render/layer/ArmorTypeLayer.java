package geni.witherutils.base.client.render.layer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Quaternionf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import geni.witherutils.base.client.model.special.SpecialModels;
import geni.witherutils.base.client.render.type.WUTRenderType;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.core.common.math.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
//@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
public class ArmorTypeLayer extends RenderLayer {

	private final RenderLayerParent parent;
	
	public ArmorTypeLayer(RenderLayerParent parent)
    {
        super(parent);
        this.parent = parent;
    }

    @Override
    public void render(PoseStack matrix, MultiBufferSource buffer, int light, Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) 
    {
        if(entity instanceof Player player)
        {

            HumanoidModel<Player> model = (HumanoidModel<Player>) this.getParentModel();
    		VertexConsumer vertex = buffer.getBuffer(WUTRenderType.solid());

            matrix.scale(0.0625F, 0.0625F, 0.0625F);
            matrix.mulPose(Axis.XP.rotationDegrees(180));
            
//            renderHead(player, SpecialModels.ARMOR_HELMET00.getModel(), matrix, buffer, vertex, light, model, netHeadYaw, headPitch);
//            renderChest(player, SpecialModels.ARMOR_CHEST00.getModel(), matrix, buffer, vertex, light, model);
//            
//    		matrix.pushPose();
//            matrix.translate(-5, -2, 0);
//            renderLeftArm(player, SpecialModels.ARMOR_ARM00.getModel(), matrix, buffer, vertex, light, model);
//    		matrix.popPose();
//    		
//    		matrix.pushPose();
//            matrix.translate(5, -2, 0);
//            renderRightArm(player, SpecialModels.ARMOR_ARM01.getModel(), matrix, buffer, vertex, light, model);
//    		matrix.popPose();
        }
    }
    
    public void renderHead(Player player, BakedModel bmodel, PoseStack matrix, MultiBufferSource buffer, VertexConsumer vertex, int light, HumanoidModel<Player> model, float netHeadYaw, float headPitch)
    {
		matrix.pushPose();
		
		if (player.isCrouching())
		{
	        matrix.translate(0, -4.2F, 0);
		}

	    matrix.mulPose(Axis.YN.rotationDegrees(netHeadYaw));
	    matrix.mulPose(Axis.XP.rotationDegrees(headPitch));
	    
	    vertex = buffer.getBuffer(WUTRenderType.eyes(ResourceLocation.withDefaultNamespace("witherutils:textures/block/emissive/blue.png")));
	    renderBakedModelLists(bmodel, matrix, vertex, light);
		
        matrix.popPose();
    }
    
    public void renderChest(Player player, BakedModel bmodel, PoseStack matrix, MultiBufferSource buffer, VertexConsumer vertex, int light, HumanoidModel<Player> model)
    {
		matrix.pushPose();

		double xT = model.body.x;
		double yT = model.body.y;
		double zT = model.body.z;
        matrix.translate(-xT, -yT, -zT);

		float xR = model.body.xRot;
		float yR = model.body.yRot;
		float zR = model.body.zRot;
        matrix.mulPose(new Quaternionf().rotateXYZ(xR, -yR, -zR));

	    renderBakedModelLists(bmodel, matrix, vertex, light);
		
        matrix.popPose();
    }
    
    public void renderLeftArm(Player player, BakedModel bmodel, PoseStack matrix, MultiBufferSource buffer, VertexConsumer vertex, int light, HumanoidModel<Player> model)
    {
		matrix.pushPose();

		double xT = model.leftArm.x;
		double yT = model.leftArm.y;
		double zT = model.leftArm.z;
        matrix.translate(xT, !player.isCrouching() ? yT : yT - 5.8, zT);

		float xR = model.leftArm.xRot;
		float yR = model.leftArm.yRot;
		float zR = model.leftArm.zRot;
        matrix.mulPose(new Quaternionf().rotateXYZ(xR, -yR, -zR));
        
	    renderBakedModelLists(bmodel, matrix, vertex, light);
		
        matrix.popPose();
    }
    public void renderRightArm(Player player, BakedModel bmodel, PoseStack matrix, MultiBufferSource buffer, VertexConsumer vertex, int light, HumanoidModel<Player> model)
    {
		matrix.pushPose();

		double xT = model.rightArm.x;
		double yT = model.rightArm.y;
		double zT = model.rightArm.z;
        matrix.translate(xT, !player.isCrouching() ? yT : yT - 5.8, zT);

		float xR = model.rightArm.xRot;
		float yR = model.rightArm.yRot;
		float zR = model.rightArm.zRot;
        matrix.mulPose(new Quaternionf().rotateXYZ(xR, -yR, -zR));

	    renderBakedModelLists(bmodel, matrix, vertex, light);
		
        matrix.popPose();
    }
    
    public void renderLeftLeg(Player player, BakedModel bmodel, PoseStack matrix, MultiBufferSource buffer, VertexConsumer vertex, int light, HumanoidModel<Player> model, float limbSwing, float limbSwingAmount)
    {
		matrix.pushPose();

//    	double x = model.leftLeg.x;
//    	double y = model.leftLeg.y;
//    	double z = model.leftLeg.z;
//    	
////        matrix.translate(x, y, z);
//    	
//		if (player.isCrouching())
//		{
//	        matrix.mulPose(Axis.ZP.rotationDegrees(model.leftLeg.z = 4.0F));
//	        matrix.translate(0, model.leftLeg.y - 12.2F, 0);
//		}
//
//        matrix.mulPose(new Quaternionf().rotateXYZ(model.leftLeg.xRot, model.leftLeg.yRot, model.leftLeg.zRot));
        
		double xT = model.leftLeg.x;
		double yT = model.leftLeg.y;
		double zT = model.leftLeg.z;
        matrix.translate(-xT, -yT, -zT);

		float xR = model.leftLeg.xRot;
		float yR = model.leftLeg.yRot;
		float zR = model.leftLeg.zRot;
        matrix.mulPose(new Quaternionf().rotateXYZ(-xR, -yR, -zR));
	    
	    renderBakedModelLists(bmodel, matrix, vertex, light);
		
        matrix.popPose();
    }
    public void renderRightLeg(Player player, BakedModel bmodel, PoseStack matrix, MultiBufferSource buffer, VertexConsumer vertex, int light, HumanoidModel<Player> model, float limbSwing, float limbSwingAmount)
    {
		matrix.pushPose();

//    	double x = model.rightLeg.x;
//    	double y = model.rightLeg.y;
//    	double z = model.rightLeg.z;
//
////        matrix.translate(x, y, z);
//    	
//		if (player.isCrouching())
//		{
//	        matrix.mulPose(Axis.ZP.rotationDegrees(model.rightLeg.z = 4.0F));
//	        matrix.translate(0, model.rightLeg.y - 12.2F, 0);
//		}
//
//        matrix.mulPose(new Quaternionf().rotateXYZ(model.rightLeg.xRot, model.rightLeg.yRot, model.rightLeg.zRot));
        
		double xT = model.rightLeg.x;
		double yT = model.rightLeg.y;
		double zT = model.rightLeg.z;
        matrix.translate(-xT, -yT, -zT);

		float xR = model.rightLeg.xRot;
		float yR = model.rightLeg.yRot;
		float zR = model.rightLeg.zRot;
        matrix.mulPose(new Quaternionf().rotateXYZ(-xR, -yR, -zR));
		
	    renderBakedModelLists(bmodel, matrix, vertex, light);
		
        matrix.popPose();
    }
    
    public void renderBakedModelLists(BakedModel model, PoseStack matrix, VertexConsumer vertex, int light)
    {
		Minecraft.getInstance().getItemRenderer().renderModelLists(model, ItemStack.EMPTY, light, OverlayTexture.NO_OVERLAY, matrix, vertex);
    }
    
//	@SubscribeEvent
//	@OnlyIn(Dist.CLIENT)
//	public static void onArmotTypeRender(RenderPlayerEvent event)
//	{
//		Player player = event.getEntity();
//		PlayerModel<AbstractClientPlayer> playerModel = event.getRenderer().getModel();
//
//		for(InteractionHand hands : InteractionHand.values())
//		{
//			if(player.getItemInHand(hands).getItem() == WUTItems.STEELARMOR_HELMET.get())
//			{
//				playerModel.head.visible = false;
//				playerModel.hat.visible = false;
//			}
//			if(player.getItemInHand(hands).getItem() == WUTItems.STEELARMOR_CHEST.get())
//			{
//				playerModel.jacket.visible = false;
//				playerModel.leftSleeve.visible = false;
//				playerModel.rightSleeve.visible = false;
//				playerModel.body.visible = false;
//				playerModel.leftArm.visible = false;
//				playerModel.rightArm.visible = false;
//			}
//			if(player.getItemInHand(hands).getItem() == WUTItems.STEELARMOR_LEGGINGS.get())
//			{
//				playerModel.leftPants.visible = false;
//				playerModel.rightPants.visible = false;
//				playerModel.leftLeg.visible = false;
//				playerModel.rightLeg.visible = false;
//			}
//		}
//	}
}
