package geni.witherutils.base.client.model.armor;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelSteelChest {

	@SuppressWarnings("unused")
	public static LayerDefinition createBodyLayer()
	{
		return AbstractArmorModel.createLayer(128, 64, root -> {

			PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
					.texOffs(44, 19).addBox(-1.0F, 0.0F, 2.0F, 2.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(12, 9).addBox(-4.0F, 10.0F, 2.0F, 8.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(12, 9).addBox(-4.0F, 10.0F, -3.0F, 8.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(22, 23).addBox(1.0F, 1.0F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(22, 23).addBox(-2.0F, 1.0F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(22, 21).addBox(-1.0F, 2.0F, -3.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(48, 24).addBox(-1.0F, 0.5F, 3.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(0, 8).addBox(-3.0F, 5.0F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(0, 8).addBox(2.0F, 5.0F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(3, 8).addBox(1.0F, 6.0F, -3.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(3, 8).addBox(-3.0F, 6.0F, -3.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(6, 4).addBox(4.0F, 10.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
					.texOffs(6, 4).addBox(-5.0F, 10.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)),
					PartPose.offset(0.0F, 0.0F, 0.0F));
			body.addOrReplaceChild("body_r1", CubeListBuilder.create()
					.texOffs(7, 14).addBox(0.0F, -2.5F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(7, 14).addBox(0.0F, -4.5F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(7, 14).addBox(0.0F, -6.5F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
					PartPose.offsetAndRotation(-4.0F, 6.5F, 2.5F, 0.0F, 0.1309F, 0.0F));
			body.addOrReplaceChild("body_r2", CubeListBuilder.create()
					.texOffs(2, 14).addBox(-3.0F, -2.5F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(2, 14).addBox(-3.0F, -4.5F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(2, 14).addBox(-3.0F, -6.5F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
					PartPose.offsetAndRotation(4.0F, 6.5F, 2.5F, 0.0F, -0.1309F, 0.0F));
			body.addOrReplaceChild("body_r3", CubeListBuilder.create().texOffs(14, 6).addBox(-0.9371F, -0.8767F, -2.4059F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.6157F, 5.6851F, -0.4709F, 0.2618F, -0.8727F, 0.0698F));
			body.addOrReplaceChild("body_r4", CubeListBuilder.create().texOffs(14, 6).addBox(-0.5F, -2.5F, -2.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.5F, 6.5F, 2.0F, 0.2618F, 0.2443F, 0.0698F));
			body.addOrReplaceChild("body_r5", CubeListBuilder.create().texOffs(14, 6).addBox(-0.0629F, -0.8767F, -2.4059F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.6157F, 5.6851F, -0.4709F, 0.2618F, 0.8727F, -0.0698F));
			body.addOrReplaceChild("body_r6", CubeListBuilder.create().texOffs(14, 6).addBox(-0.5F, -2.5F, -2.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, 6.5F, 2.0F, 0.2618F, -0.2443F, -0.0698F));
			body.addOrReplaceChild("body_r7", CubeListBuilder.create().texOffs(14, 6).addBox(-0.9371F, -0.8767F, -2.4059F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.6157F, 3.6851F, -0.4709F, 0.2618F, -0.6981F, 0.0698F));
			body.addOrReplaceChild("body_r8", CubeListBuilder.create().texOffs(14, 6).addBox(-0.5F, -2.5F, -2.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.5F, 4.5F, 2.0F, 0.2618F, 0.2443F, 0.0698F));
			body.addOrReplaceChild("body_r9", CubeListBuilder.create().texOffs(14, 6).addBox(-0.0629F, -0.8767F, -2.4059F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.6157F, 3.6851F, -0.4709F, 0.2618F, 0.6981F, -0.0698F));
			body.addOrReplaceChild("body_r10", CubeListBuilder.create().texOffs(14, 6).addBox(-0.5F, -2.5F, -2.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, 4.5F, 2.0F, 0.2618F, -0.2443F, -0.0698F));
			body.addOrReplaceChild("body_r11", CubeListBuilder.create().texOffs(14, 6).addBox(-0.0629F, -0.8767F, -2.4059F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.6157F, 1.6851F, -0.4709F, 0.2618F, 0.3491F, -0.0698F));
			body.addOrReplaceChild("body_r12", CubeListBuilder.create().texOffs(14, 6).addBox(-0.9371F, -0.8767F, -2.4059F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.6157F, 1.6851F, -0.4709F, 0.2618F, -0.3491F, 0.0698F));
			body.addOrReplaceChild("body_r13", CubeListBuilder.create().texOffs(14, 6).addBox(-0.5F, -2.5F, -2.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, 2.5F, 2.0F, 0.2618F, -0.2443F, -0.0698F));
			body.addOrReplaceChild("body_r14", CubeListBuilder.create().texOffs(14, 6).addBox(-0.5F, -2.5F, -2.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.5F, 2.5F, 2.0F, 0.2618F, 0.2443F, 0.0698F));
			body.addOrReplaceChild("body_r15", CubeListBuilder.create().texOffs(47, 24).addBox(-1.5F, -0.4F, -0.9F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 3.5F, 0.5236F, 0.0F, 0.0F));
			body.addOrReplaceChild("body_r16", CubeListBuilder.create().texOffs(14, 13).addBox(2.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, -3.0F, 0.5236F, -0.1745F, 0.0F));
			body.addOrReplaceChild("body_r17", CubeListBuilder.create().texOffs(14, 13).addBox(-4.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, -3.0F, 0.5236F, 0.1745F, 0.0F));

			PartDefinition rightArm = root.addOrReplaceChild("right_arm", CubeListBuilder.create()
				.texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 39).addBox(-3.3F, -2.1F, -1.8F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 41).addBox(-1.8F, -2.5F, -1.7F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 40).addBox(-3.3F, -2.1F, -2.3F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 40).addBox(-3.3F, 4.4F, -2.3F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 40).addBox(-3.3F, 4.4F, -1.8F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 40).addBox(-3.3F, 7.9F, -2.3F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 40).addBox(-3.3F, 7.9F, -1.8F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 40).addBox(-2.7F, 7.9F, -2.3F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 40).addBox(-2.7F, 7.9F, -1.8F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-5.0F, 2.0F, 0.0F));

			PartDefinition leftArm = root.addOrReplaceChild("left_arm", CubeListBuilder.create()
				.texOffs(40, 16).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 41).addBox(-1.2F, -2.5F, -1.7F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 40).addBox(-0.7F, -2.1F, -2.3F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 40).addBox(-0.7F, 4.4F, -2.3F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 40).addBox(-0.7F, 4.4F, -1.8F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 39).addBox(-0.7F, -2.1F, -1.8F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 40).addBox(-0.7F, 7.9F, -1.8F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 40).addBox(-0.7F, 7.9F, -2.3F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 40).addBox(-1.3F, 7.9F, -1.8F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 40).addBox(-1.3F, 7.9F, -2.3F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offset(5.0F, 2.0F, 0.0F));
		});
	}

    public void setupAnim(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}
}
