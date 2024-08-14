package geni.witherutils.base.common.entity.cursed.skeleton;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CursedSkeletonModel<T extends Mob & RangedAttackMob> extends HumanoidModel<T> {

	public boolean creepy;
	
	public final ModelPart head;
	public final ModelPart hat;
	public final ModelPart body;
	public final ModelPart rightArm;
	public final ModelPart leftArm;
	public final ModelPart rightLeg;
	public final ModelPart leftLeg;
	
	public CursedSkeletonModel(ModelPart part)
	{
		super(part);
		this.head = part.getChild("head");
		this.hat = part.getChild("hat");
		this.body = part.getChild("body");
		this.rightArm = part.getChild("right_arm");
		this.leftArm = part.getChild("left_arm");
		this.rightLeg = part.getChild("right_leg");
		this.leftLeg = part.getChild("left_leg");
	}

	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, new CubeDeformation(0.0F).extend(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
		partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(-5.0F, 2.0F, 0.0F));
		partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(5.0F, 2.0F, 0.0F));
		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(-2.0F, 12.0F, 0.0F));
		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(2.0F, 12.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		ItemStack itemstack = entity.getMainHandItem();
		if (entity.isAggressive() && (itemstack.isEmpty() || !itemstack.is(Items.BOW)))
		{
			float f = Mth.sin(this.attackTime * (float)Math.PI);
			float f1 = Mth.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * (float)Math.PI);
			this.rightArm.zRot = 0.0F;
			this.leftArm.zRot = 0.0F;
			this.rightArm.yRot = -(0.1F - f * 0.6F);
			this.leftArm.yRot = 0.1F - f * 0.6F;
			this.rightArm.xRot = (-(float)Math.PI / 2F);
			this.leftArm.xRot = (-(float)Math.PI / 2F);
			this.rightArm.xRot -= f * 1.2F - f1 * 0.4F;
			this.leftArm.xRot -= f * 1.2F - f1 * 0.4F;
			AnimationUtils.bobArms(this.rightArm, this.leftArm, ageInTicks);
		}
		
		if (this.creepy)
		{
	        float time = entity.level().getLevelData().getGameTime() + ageInTicks;
			double offset = Math.sin(time * 1 / 8.0D) / 10.0D;

			this.head.xScale = 1.0f + (float) (offset);
			this.head.yScale = 1.0f + (float) (offset);
			this.head.zScale = 1.0f + (float) (offset);
		}
	}
	
	@Override
	public void prepareMobModel(T p_103793_, float p_103794_, float p_103795_, float p_103796_)
	{
		this.rightArmPose = HumanoidModel.ArmPose.EMPTY;
		this.leftArmPose = HumanoidModel.ArmPose.EMPTY;
		ItemStack itemstack = p_103793_.getItemInHand(InteractionHand.MAIN_HAND);
		if (itemstack.is(Items.BOW) && p_103793_.isAggressive())
		{
			if (p_103793_.getMainArm() == HumanoidArm.RIGHT)
			{
				this.rightArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
			}
			else
			{
				this.leftArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
			}
		}
		super.prepareMobModel(p_103793_, p_103794_, p_103795_, p_103796_);
	}

	@Override
	public void translateToHand(HumanoidArm p_103778_, PoseStack p_103779_)
	{
		float f = p_103778_ == HumanoidArm.RIGHT ? 1.0F : -1.0F;
		ModelPart modelpart = this.getArm(p_103778_);
		modelpart.x += f;
		modelpart.translateAndRotate(p_103779_);
		modelpart.x -= f;
	}
}
