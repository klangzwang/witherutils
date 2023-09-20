package geni.witherutils.base.common.entity.cursed.spider;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CursedSpiderModel<T extends CursedSpider> extends HierarchicalModel<T> {

	public boolean creepy;
	
	private final ModelPart head;
	private final ModelPart neck;
	private final ModelPart body;
	private final ModelPart jaw;
	private final ModelPart rightHindLeg;
	private final ModelPart leftHindLeg;
	private final ModelPart rightMiddleHindLeg;
	private final ModelPart leftMiddleHindLeg;
	private final ModelPart rightMiddleFrontLeg;
	private final ModelPart leftMiddleFrontLeg;
	private final ModelPart rightFrontLeg;
	private final ModelPart leftFrontLeg;
	
	public CursedSpiderModel(ModelPart root)
	{
		super(RenderType::entityTranslucent);
		
		this.head = root.getChild("head");
		this.neck = root.getChild("neck");
		this.body = root.getChild("body");
		this.jaw = root.getChild("jaw");
		this.rightHindLeg = root.getChild("right_hind_leg");
		this.leftHindLeg = root.getChild("left_hind_leg");
		this.rightMiddleHindLeg = root.getChild("right_middle_hind_leg");
		this.leftMiddleHindLeg = root.getChild("left_middle_hind_leg");
		this.rightMiddleFrontLeg = root.getChild("right_middle_front_leg");
		this.leftMiddleFrontLeg = root.getChild("left_middle_front_leg");
		this.rightFrontLeg = root.getChild("right_front_leg");
		this.leftFrontLeg = root.getChild("left_front_leg");
	}
	
	@SuppressWarnings("unused")
	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
	    		  	.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
					.texOffs(64, 7).addBox(-3.0F, -9.5F, -3.0F, 6.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
					.texOffs(7, 8).addBox(-3.5F, -7.6F, -5.0F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(0, 0).addBox(-4.1F, -8.5F, -4.3F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
					.texOffs(5, 7).addBox(-4.0F, -6.0F, -4.5F, 8.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
					.texOffs(64, 7).addBox(-4.5F, -6.5F, -3.9F, 9.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
					.texOffs(2, 6).addBox(-2.0F, -10.0F, -3.0F, 4.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
					.texOffs(67, 7).addBox(-2.0F, -9.0F, 3.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, -5.0F));

		PartDefinition neck = partdefinition.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 33).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, -2.0F));
		
		PartDefinition jaw = partdefinition.addOrReplaceChild("jaw", CubeListBuilder.create(), PartPose.offset(0.0F, 14.0F, -3.0F));
		jaw.addOrReplaceChild("jaw1", CubeListBuilder.create().texOffs(56, 16).addBox(-2.9957F, -1.4129F, -4.6376F, 6.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0043F, -3.8545F, -3.0825F, 0.3491F, 0.0F, 0.0F));
		
		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 15.0F, 9.0F));
		body.addOrReplaceChild("body1", CubeListBuilder.create().texOffs(84, 44).addBox(-5.0F, -13.0F, 3.0F, 10.0F, 8.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 9.0F, -9.0F, 0.3491F, 0.0F, 0.0F));

			head.addOrReplaceChild("rhorn_r1", CubeListBuilder.create().texOffs(59, 46).mirror().addBox(-2.0F, -2.0F, -1.5F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, -7.0F, 1.0F, -0.8727F, -0.4363F, 0.0F));
			head.addOrReplaceChild("rhorn_r2", CubeListBuilder.create().texOffs(61, 40).mirror().addBox(-1.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-5.7369F, -8.1662F, -0.4164F, -0.9599F, -1.0472F, 0.7854F));
			head.addOrReplaceChild("rhorn_r3", CubeListBuilder.create().texOffs(81, 43).addBox(-1.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.8501F, -9.3148F, -2.2639F, 1.0472F, -0.8727F, 2.0071F));
			head.addOrReplaceChild("lhorn_r1", CubeListBuilder.create().texOffs(59, 46).addBox(-2.0F, -2.0F, -1.5F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -7.0F, 1.0F, -0.8727F, 0.4363F, 0.0F));
			head.addOrReplaceChild("lhorn_r2", CubeListBuilder.create().texOffs(61, 40).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.7369F, -8.1662F, -0.4164F, -0.9599F, 1.0472F, -0.7854F));
			head.addOrReplaceChild("lhorn_r3", CubeListBuilder.create().texOffs(81, 43).addBox(-1.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.8501F, -9.3148F, -2.2639F, 1.0472F, 0.8727F, -2.0071F));
			head.addOrReplaceChild("head32_r1", CubeListBuilder.create().texOffs(22, 12).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0095F, -8.5679F, 6.3355F, 0.0F, -1.1345F, -1.5708F));
			head.addOrReplaceChild("head31_r1", CubeListBuilder.create().texOffs(22, 12).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0095F, -7.7166F, 4.7464F, 0.0F, -1.1345F, -1.5708F));
			head.addOrReplaceChild("head30_r1", CubeListBuilder.create().texOffs(22, 12).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0095F, -11.2966F, 4.8922F, 0.0F, -0.6109F, -1.5708F));
			head.addOrReplaceChild("head29_r1", CubeListBuilder.create().texOffs(22, 12).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0095F, -9.5764F, 3.6877F, 0.0F, -0.6109F, -1.5708F));
			head.addOrReplaceChild("head28_r1", CubeListBuilder.create().texOffs(22, 12).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0095F, -13.0285F, 1.9315F, 0.0F, -0.1745F, -1.5708F));
			head.addOrReplaceChild("head27_r1", CubeListBuilder.create().texOffs(22, 12).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0095F, -10.8619F, 1.5495F, 0.0F, -0.1745F, -1.5708F));
			head.addOrReplaceChild("head26_r1", CubeListBuilder.create().texOffs(22, 12).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0095F, -13.1404F, -1.7347F, 0.0F, 0.2618F, -1.5708F));
			head.addOrReplaceChild("head25_r1", CubeListBuilder.create().texOffs(22, 12).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0095F, -11.0153F, -1.1653F, 0.0F, 0.2618F, -1.5708F));
			head.addOrReplaceChild("head24_r1", CubeListBuilder.create().texOffs(22, 12).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0095F, -11.5017F, -5.0803F, 0.0F, 0.7418F, -1.5708F));
			head.addOrReplaceChild("head23_r1", CubeListBuilder.create().texOffs(22, 12).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0095F, -9.6585F, -3.3913F, 0.0F, 0.7418F, -1.5708F));
			head.addOrReplaceChild("head20_r1", CubeListBuilder.create().texOffs(6, 8).addBox(-2.9707F, -0.8426F, -4.257F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0043F, -5.8545F, -1.0825F, -0.6109F, 0.0F, 0.0F));
			head.addOrReplaceChild("head19_r1", CubeListBuilder.create().texOffs(72, 7).addBox(-2.9907F, -2.6703F, -3.7709F, 5.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(7, 9).addBox(-1.9957F, -0.7693F, -4.0998F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0043F, -5.8545F, -1.0825F, 0.2618F, 0.0F, 0.0F));
			head.addOrReplaceChild("head17_r1", CubeListBuilder.create().texOffs(4, 8).addBox(-2.4957F, -5.8594F, 0.3315F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0043F, -5.8545F, -1.0825F, -0.6981F, 0.0F, 0.0F));
			head.addOrReplaceChild("head16_r1", CubeListBuilder.create().texOffs(6, 6).addBox(-1.9957F, -4.5674F, -1.9859F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0043F, -5.8545F, -1.0825F, 0.4363F, 0.0F, 0.0F));
			head.addOrReplaceChild("head14_r1", CubeListBuilder.create().texOffs(80, 0).addBox(0.2537F, -4.995F, -2.4175F, 1.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0043F, -5.8545F, -1.0825F, 0.0F, 0.0F, 0.7854F));
			head.addOrReplaceChild("head13_r1", CubeListBuilder.create().texOffs(80, 0).addBox(-1.2477F, -4.9889F, -2.4175F, 1.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0043F, -5.8545F, -1.0825F, 0.0F, 0.0F, -0.7854F));
			head.addOrReplaceChild("head12_r1", CubeListBuilder.create().texOffs(72, 7).addBox(-3.4957F, -2.6493F, 4.23F, 7.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0043F, -5.8545F, -1.0825F, -0.0436F, 0.0F, 0.0F));
			head.addOrReplaceChild("head11_r1", CubeListBuilder.create().texOffs(64, 11).addBox(-2.4957F, -0.5521F, 4.3157F, 5.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0043F, -5.8545F, -1.0825F, -0.3491F, 0.0F, 0.0F));
			head.addOrReplaceChild("head09_r1", CubeListBuilder.create().texOffs(9, 10).addBox(-0.9957F, 3.8737F, -1.6323F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0043F, -5.8545F, -1.0825F, -0.5236F, 0.0F, 0.0F));
			head.addOrReplaceChild("head07_r1", CubeListBuilder.create().texOffs(13, 9).addBox(0.6087F, 3.0972F, -3.0026F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0043F, -5.8545F, -1.0825F, 0.0F, 0.4363F, -0.2618F));
			head.addOrReplaceChild("head06_r1", CubeListBuilder.create().texOffs(1, 9).addBox(-3.6012F, 3.095F, -3.0061F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0043F, -5.8545F, -1.0825F, 0.0F, -0.4363F, 0.2618F));
			head.addOrReplaceChild("head07_r2", CubeListBuilder.create().texOffs(41, 8).addBox(-3.0F, -5.1F, 0.0F, 6.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.3F, -6.3F, -0.3491F, 0.0F, 0.0F));
			head.addOrReplaceChild("head07_r3", CubeListBuilder.create().texOffs(39, 10).mirror().addBox(0.0F, -4.0F, 0.0F, 5.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -2.0F, -5.0F, -0.1309F, 0.1309F, -1.5708F));
			head.addOrReplaceChild("head06_r2", CubeListBuilder.create().texOffs(39, 10).addBox(-5.0F, -4.0F, 0.0F, 5.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, -5.0F, -0.1309F, -0.1309F, 1.5708F));

	      PartDefinition rightHindLeg = partdefinition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(13, 21).addBox(-15.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 15.0F, 4.0F));
	      PartDefinition leftHindLeg = partdefinition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(13, 21).addBox(-1.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 15.0F, 4.0F));
	      PartDefinition rightMiddleHindLeg = partdefinition.addOrReplaceChild("right_middle_hind_leg", CubeListBuilder.create().texOffs(13, 21).addBox(-15.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 15.0F, 1.0F));
	      PartDefinition leftMiddleHindLeg = partdefinition.addOrReplaceChild("left_middle_hind_leg", CubeListBuilder.create().texOffs(13, 21).addBox(-1.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 15.0F, 1.0F));
	      PartDefinition rightMiddleFrontLeg = partdefinition.addOrReplaceChild("right_middle_front_leg", CubeListBuilder.create().texOffs(13, 21).addBox(-15.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 15.0F, -2.0F));
	      PartDefinition leftMiddleFrontLeg = partdefinition.addOrReplaceChild("left_middle_front_leg", CubeListBuilder.create().texOffs(13, 21).addBox(-1.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 15.0F, -2.0F));
	      PartDefinition rightFrontLeg = partdefinition.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(13, 21).addBox(-15.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 15.0F, -5.0F));
	      PartDefinition leftFrontLeg = partdefinition.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(13, 21).addBox(-1.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 15.0F, -5.0F));

	      return LayerDefinition.create(meshdefinition, 128, 64);
	}
	
	@Override
	public ModelPart root()
	{
		return head;
	}
	
	@SuppressWarnings("unused")
	@Override
	public void setupAnim(T p_103866_, float p_103867_, float p_103868_, float p_103869_, float p_103870_, float p_103871_)
	{
		this.jaw.xRot = 0.2F * Mth.sin(p_103869_ * 0.3F) + 0.4F;
		
	      this.head.yRot = p_103870_ * ((float)Math.PI / 180F);
	      this.head.xRot = p_103871_ * ((float)Math.PI / 180F);
	      float f = ((float)Math.PI / 4F);
	      this.rightHindLeg.zRot = (-(float)Math.PI / 4F);
	      this.leftHindLeg.zRot = ((float)Math.PI / 4F);
	      this.rightMiddleHindLeg.zRot = -0.58119464F;
	      this.leftMiddleHindLeg.zRot = 0.58119464F;
	      this.rightMiddleFrontLeg.zRot = -0.58119464F;
	      this.leftMiddleFrontLeg.zRot = 0.58119464F;
	      this.rightFrontLeg.zRot = (-(float)Math.PI / 4F);
	      this.leftFrontLeg.zRot = ((float)Math.PI / 4F);
	      float f1 = -0.0F;
	      float f2 = ((float)Math.PI / 8F);
	      this.rightHindLeg.yRot = ((float)Math.PI / 4F);
	      this.leftHindLeg.yRot = (-(float)Math.PI / 4F);
	      this.rightMiddleHindLeg.yRot = ((float)Math.PI / 8F);
	      this.leftMiddleHindLeg.yRot = (-(float)Math.PI / 8F);
	      this.rightMiddleFrontLeg.yRot = (-(float)Math.PI / 8F);
	      this.leftMiddleFrontLeg.yRot = ((float)Math.PI / 8F);
	      this.rightFrontLeg.yRot = (-(float)Math.PI / 4F);
	      this.leftFrontLeg.yRot = ((float)Math.PI / 4F);
	      float f3 = -(Mth.cos(p_103867_ * 0.6662F * 2.0F + 0.0F) * 0.4F) * p_103868_;
	      float f4 = -(Mth.cos(p_103867_ * 0.6662F * 2.0F + (float)Math.PI) * 0.4F) * p_103868_;
	      float f5 = -(Mth.cos(p_103867_ * 0.6662F * 2.0F + ((float)Math.PI / 2F)) * 0.4F) * p_103868_;
	      float f6 = -(Mth.cos(p_103867_ * 0.6662F * 2.0F + ((float)Math.PI * 1.5F)) * 0.4F) * p_103868_;
	      float f7 = Math.abs(Mth.sin(p_103867_ * 0.6662F + 0.0F) * 0.4F) * p_103868_;
	      float f8 = Math.abs(Mth.sin(p_103867_ * 0.6662F + (float)Math.PI) * 0.4F) * p_103868_;
	      float f9 = Math.abs(Mth.sin(p_103867_ * 0.6662F + ((float)Math.PI / 2F)) * 0.4F) * p_103868_;
	      float f10 = Math.abs(Mth.sin(p_103867_ * 0.6662F + ((float)Math.PI * 1.5F)) * 0.4F) * p_103868_;
	      this.rightHindLeg.yRot += f3;
	      this.leftHindLeg.yRot += -f3;
	      this.rightMiddleHindLeg.yRot += f4;
	      this.leftMiddleHindLeg.yRot += -f4;
	      this.rightMiddleFrontLeg.yRot += f5;
	      this.leftMiddleFrontLeg.yRot += -f5;
	      this.rightFrontLeg.yRot += f6;
	      this.leftFrontLeg.yRot += -f6;
	      this.rightHindLeg.zRot += f7;
	      this.leftHindLeg.zRot += -f7;
	      this.rightMiddleHindLeg.zRot += f8;
	      this.leftMiddleHindLeg.zRot += -f8;
	      this.rightMiddleFrontLeg.zRot += f9;
	      this.leftMiddleFrontLeg.zRot += -f9;
	      this.rightFrontLeg.zRot += f10;
	      this.leftFrontLeg.zRot += -f10;
	      
	      if (this.creepy)
	      {
	    	  float time = p_103866_.level().getLevelData().getGameTime() + p_103869_;
	    	  double offset = Math.sin(time * 1 / 8.0D) / 10.0D;

	    	  this.head.xScale = 1.0f + (float) (offset);
	    	  this.head.yScale = 1.0f + (float) (offset);
	    	  this.head.zScale = 1.0f + (float) (offset);
	      }
	}
	
	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		neck.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		jaw.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		rightHindLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leftHindLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		rightFrontLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leftFrontLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		rightMiddleHindLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leftMiddleHindLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		rightMiddleFrontLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leftMiddleFrontLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}