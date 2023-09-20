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
public class ModelSteelHelmet {

	public static LayerDefinition createBodyLayer()
	{
		return AbstractArmorModel.createLayer(128, 64, root -> {

			PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(64, 7).addBox(-3.0F, -9.5F, -3.0F, 6.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(7, 8).addBox(-3.5F, -7.6F, -5.0F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-4.1F, -8.5F, -4.3F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(5, 7).addBox(-4.0F, -6.0F, -4.5F, 8.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(64, 7).addBox(-4.5F, -6.5F, -3.9F, 9.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(2, 6).addBox(-2.0F, -10.0F, -3.0F, 4.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(67, 7).addBox(-2.0F, -9.0F, 3.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.ZERO);

			head.addOrReplaceChild("helmet00", CubeListBuilder.create()
				.texOffs(72, 7).addBox(-2.9907F, -2.6703F, -3.7709F, 5.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(7, 9).addBox(-1.9957F, -0.7693F, -4.0998F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.0043F, -5.8545F, -1.0825F, 0.2618F, 0.0F, 0.0F));

			head.addOrReplaceChild("helmet01", CubeListBuilder.create().texOffs(6, 8).addBox(-2.9707F, -0.8426F, -4.457F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0043F, -5.8545F, -1.0825F, -0.6109F, 0.0F, 0.0F));
			head.addOrReplaceChild("helmet02", CubeListBuilder.create().texOffs(56, 16).addBox(-2.9957F, -1.4129F, -4.9376F, 6.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0043F, -5.8545F, -1.0825F, 0.3491F, 0.0F, 0.0F));
			head.addOrReplaceChild("helmet03", CubeListBuilder.create().texOffs(1, 9).addBox(-3.6012F, 3.095F, -3.0061F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0043F, -5.8545F, -1.0825F, 0.0F, -0.4363F, 0.2618F));
			head.addOrReplaceChild("helmet04", CubeListBuilder.create().texOffs(4, 8).addBox(-2.4957F, -5.8594F, 0.3315F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0043F, -5.8545F, -1.0825F, -0.6981F, 0.0F, 0.0F));
			head.addOrReplaceChild("helmet05", CubeListBuilder.create().texOffs(6, 6).addBox(-1.9957F, -4.5674F, -1.9859F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0043F, -5.8545F, -1.0825F, 0.4363F, 0.0F, 0.0F));
			head.addOrReplaceChild("helmet06", CubeListBuilder.create().texOffs(80, 0).addBox(0.2537F, -4.995F, -2.4175F, 1.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0043F, -5.8545F, -1.0825F, 0.0F, 0.0F, 0.7854F));
			head.addOrReplaceChild("helmet07", CubeListBuilder.create().texOffs(80, 0).addBox(-1.2477F, -4.9889F, -2.4175F, 1.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0043F, -5.8545F, -1.0825F, 0.0F, 0.0F, -0.7854F));
			head.addOrReplaceChild("helmet08", CubeListBuilder.create().texOffs(72, 7).addBox(-3.4957F, -2.6493F, 4.23F, 7.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0043F, -5.8545F, -1.0825F, -0.0436F, 0.0F, 0.0F));
			head.addOrReplaceChild("helmet09", CubeListBuilder.create().texOffs(64, 11).addBox(-2.4957F, -0.5521F, 4.3157F, 5.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0043F, -5.8545F, -1.0825F, -0.3491F, 0.0F, 0.0F));
			head.addOrReplaceChild("helmet10", CubeListBuilder.create().texOffs(9, 10).addBox(-0.9957F, 3.8737F, -1.6323F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0043F, -5.8545F, -1.0825F, -0.5236F, 0.0F, 0.0F));
			head.addOrReplaceChild("helmet11", CubeListBuilder.create().texOffs(13, 9).addBox(0.6087F, 3.0972F, -3.0026F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0043F, -5.8545F, -1.0825F, 0.0F, 0.4363F, -0.2618F));
		});
	}

	public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
	}
}
