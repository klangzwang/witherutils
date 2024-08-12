package geni.witherutils.base.common.entity.naked;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ChickenNakedModel<T extends Entity> extends AgeableListModel<T> {
	
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart rightLeg;
	private final ModelPart leftLeg;
	private final ModelPart rightWing;
	private final ModelPart leftWing;
	private final ModelPart beak;
	private final ModelPart redThing;
	private final ModelPart[] tentacles = new ModelPart[5];
	
	public ChickenNakedModel(ModelPart root)
	{
		this.head = root.getChild("head");
		this.beak = root.getChild("beak");
		this.redThing = root.getChild("red_thing");
		this.body = root.getChild("body");
		this.rightLeg = root.getChild("right_leg");
		this.leftLeg = root.getChild("left_leg");
		this.rightWing = root.getChild("right_wing");
		this.leftWing = root.getChild("left_wing");
		for(int i = 0; i < this.tentacles.length; ++i)
		{
			this.tentacles[i] = root.getChild(createTentacleName(i));
		}
	}
	private static String createTentacleName(int p_170573_)
	{
		return "tentacle" + p_170573_;
	}
	
	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 6.0F, 3.0F), PartPose.offset(0.0F, 15.0F, -4.0F));
		partdefinition.addOrReplaceChild("beak", CubeListBuilder.create().texOffs(14, 0).addBox(-2.0F, -4.0F, -4.0F, 4.0F, 2.0F, 2.0F), PartPose.offset(0.0F, 15.0F, -4.0F));
		partdefinition.addOrReplaceChild("red_thing", CubeListBuilder.create().texOffs(14, 4).addBox(-1.0F, -2.0F, -3.0F, 2.0F, 2.0F, 2.0F), PartPose.offset(0.0F, 15.0F, -4.0F));
		partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 9).addBox(-2.0F, -4.0F, -3.0F, 4.0F, 8.0F, 6.0F), PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, ((float)Math.PI / 2F), 0.0F, 0.0F));

		CubeListBuilder cubelistbuilder = CubeListBuilder.create().texOffs(26, 0).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F);
		
		partdefinition.addOrReplaceChild("right_leg", cubelistbuilder, PartPose.offset(-2.0F, 19.0F, 1.0F));
		partdefinition.addOrReplaceChild("left_leg", cubelistbuilder, PartPose.offset(1.0F, 19.0F, 1.0F));
		partdefinition.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(24, 13).addBox(0.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F), PartPose.offset(-4.0F, 13.0F, 0.0F));
		partdefinition.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(24, 13).addBox(-1.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F), PartPose.offset(4.0F, 13.0F, 0.0F));
		
		partdefinition.addOrReplaceChild("tentacle0", CubeListBuilder.create().texOffs(20, 13).addBox(0.0F, 0.0F, 0.0F, 1.0F, 4.0F, 1.0F), PartPose.offset(-2.7F, 14.0F, -2.0F));
		partdefinition.addOrReplaceChild("tentacle1", CubeListBuilder.create().texOffs(20, 13).addBox(3.0F, 0.0F, 4.0F, 1.0F, 3.0F, 1.0F), PartPose.offset(-3.7F, 14.0F, -6.0F));
		partdefinition.addOrReplaceChild("tentacle2", CubeListBuilder.create().texOffs(20, 13).addBox(-6.0F, 2.0F, 6.0F, 1.0F, 4.0F, 1.0F), PartPose.offset(4.3F, 11.0F, -6.0F));
		partdefinition.addOrReplaceChild("tentacle3", CubeListBuilder.create().texOffs(20, 13).addBox(7.0F, 2.0F, -1.0F, 1.0F, 4.0F, 1.0F), PartPose.offset(-6.3F, 11.0F, 0.0F));
		partdefinition.addOrReplaceChild("tentacle4", CubeListBuilder.create().texOffs(20, 13).addBox(3.0F, 3.0F, -1.0F, 1.0F, 4.0F, 1.0F), PartPose.offset(-1.3F, 11.0F, 0.0F));
		partdefinition.addOrReplaceChild("tentacle5", CubeListBuilder.create().texOffs(20, 13).addBox(-4.0F, 2.0F, 0.0F, 1.0F, 4.0F, 1.0F), PartPose.offset(3.7F, 11.0F, 0.0F));
		
		return LayerDefinition.create(meshdefinition, 64, 32);
	}
	
	@Override
	protected Iterable<ModelPart> headParts()
	{
		return ImmutableList.of(this.head, this.beak, this.redThing);
	}
	@Override
	protected Iterable<ModelPart> bodyParts()
	{
		return ImmutableList.of(this.body, this.rightLeg, this.leftLeg, this.rightWing, this.leftWing);
	}
		   
	@Override
	public void setupAnim(T p_102392_, float p_102393_, float p_102394_, float p_102395_, float p_102396_, float p_102397_)
	{
		this.head.xRot = p_102397_ * ((float)Math.PI / 180F);
		this.head.yRot = p_102396_ * ((float)Math.PI / 180F);
		this.beak.xRot = this.head.xRot;
		this.beak.yRot = this.head.yRot;
		this.redThing.xRot = this.head.xRot;
		this.redThing.yRot = this.head.yRot;
		this.rightLeg.xRot = Mth.cos(p_102393_ * 0.6662F) * 1.4F * p_102394_;
		this.leftLeg.xRot = Mth.cos(p_102393_ * 0.6662F + (float)Math.PI) * 1.4F * p_102394_;
		for(int i = 0; i < this.tentacles.length; ++i)
			this.tentacles[i].xRot = 0.2F * Mth.sin(p_102395_ * 0.3F + (float)i) + 0.4F;
	}

	@Override
	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, int pColor)
	{
		head.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, pColor);
		body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, pColor);
		rightLeg.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, pColor);
		leftLeg.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, pColor);
		beak.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, pColor);
		redThing.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, pColor);
		for(int i = 0; i < this.tentacles.length; ++i)
			tentacles[i].render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, pColor);
	}
}
