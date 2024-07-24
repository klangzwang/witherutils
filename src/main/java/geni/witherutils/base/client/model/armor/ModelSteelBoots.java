package geni.witherutils.base.client.model.armor;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelSteelBoots {

	public static LayerDefinition createBodyLayer()
	{
		return AbstractArmorModel.createLayer(128, 64, root -> {

			root.addOrReplaceChild("left_leg", CubeListBuilder.create()
				.texOffs(0, 42).addBox(-1.9F, 6.0F, -2.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 41).addBox(1.1F, 6.5F, 1.1F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 41).addBox(1.1F, 6.5F, -2.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 42).addBox(-1.9F, 6.5F, 0.1F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 43).addBox(-1.0F, 8.0F, -2.3F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 42).addBox(-1.8F, 10.0F, -2.5F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 42).addBox(-1.8F, 10.0F, -0.5F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 42).addBox(-1.8F, 11.1F, -0.5F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 41).addBox(-1.8F, 11.1F, -3.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offset(2.0F, 12.0F, 0.0F));

			root.addOrReplaceChild("right_leg", CubeListBuilder.create()
				.texOffs(0, 41).addBox(-2.2F, 11.1F, -3.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 42).addBox(-2.2F, 11.1F, -0.5F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 42).addBox(-2.2F, 10.0F, -0.5F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 42).addBox(-2.2F, 10.0F, -2.5F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 41).addBox(-2.1F, 6.5F, -2.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 41).addBox(-2.1F, 6.5F, 1.1F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 42).addBox(-2.1F, 6.5F, 0.1F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 42).addBox(-2.1F, 6.0F, -2.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 43).addBox(-1.0F, 8.0F, -2.3F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-2.0F, 12.0F, 0.0F));
		});
	}

    public void setupAnim(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}
}
