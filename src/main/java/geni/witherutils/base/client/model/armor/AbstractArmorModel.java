package geni.witherutils.base.client.model.armor;

import java.util.function.Consumer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AbstractArmorModel<T extends LivingEntity> extends HumanoidModel<T> {

	private final ModelPart spikes;
	private final ModelPart vision;
	private final ModelPart lshoulder;
	private final ModelPart rshoulder;
	private final ModelPart wings;
	private final ModelPart rightWing;
	private final ModelPart leftWing;
	private final ModelPart rightWingTip;
	private final ModelPart leftWingTip;
	protected final EquipmentSlot slot;

	public AbstractArmorModel(ModelPart part, EquipmentSlot slot)
	{
		super(part);
		this.slot = slot;
		this.spikes = this.head.getChild("spikes");
		this.vision = this.head.getChild("vision");
		this.lshoulder = this.leftArm.getChild("left_Shoulder");
		this.rshoulder = this.rightArm.getChild("right_Shoulder");
		this.wings = this.body.getChild("wings");
		this.rightWing = this.wings.getChild("right_wing");
		this.rightWingTip = this.rightWing.getChild("right_wing_tip");
		this.leftWing = this.wings.getChild("left_wing");
		this.leftWingTip = this.leftWing.getChild("left_wing_tip");
	}

	@SuppressWarnings("unused")
	public static LayerDefinition createLayer(int textureWidth, int textureHeight, Consumer<PartDefinition> rootConsumer)
	{
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
		root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
		root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
		root.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.ZERO);
		root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);
		root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.ZERO);
		root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.ZERO);

		PartDefinition spikes = root.getChild("head").addOrReplaceChild("spikes", CubeListBuilder.create(), PartPose.ZERO);
		spikes.addOrReplaceChild("spike01", CubeListBuilder.create().texOffs(22, 12).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0095F, -8.5679F, 6.3355F, 0.0F, -1.1345F, -1.5708F));
		spikes.addOrReplaceChild("spike02", CubeListBuilder.create().texOffs(22, 12).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0095F, -7.7166F, 4.7464F, 0.0F, -1.1345F, -1.5708F));
		spikes.addOrReplaceChild("spike03", CubeListBuilder.create().texOffs(22, 12).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0095F, -11.2966F, 4.8922F, 0.0F, -0.6109F, -1.5708F));
		spikes.addOrReplaceChild("spike04", CubeListBuilder.create().texOffs(22, 12).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0095F, -9.5764F, 3.6877F, 0.0F, -0.6109F, -1.5708F));
		spikes.addOrReplaceChild("spike05", CubeListBuilder.create().texOffs(22, 12).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0095F, -13.0285F, 1.9315F, 0.0F, -0.1745F, -1.5708F));
		spikes.addOrReplaceChild("spike06", CubeListBuilder.create().texOffs(22, 12).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0095F, -10.8619F, 1.5495F, 0.0F, -0.1745F, -1.5708F));
		spikes.addOrReplaceChild("spike07", CubeListBuilder.create().texOffs(22, 12).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0095F, -13.1404F, -1.7347F, 0.0F, 0.2618F, -1.5708F));
		spikes.addOrReplaceChild("spike08", CubeListBuilder.create().texOffs(22, 12).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0095F, -11.0153F, -1.1653F, 0.0F, 0.2618F, -1.5708F));
		spikes.addOrReplaceChild("spike09", CubeListBuilder.create().texOffs(22, 12).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0095F, -11.5017F, -5.0803F, 0.0F, 0.7418F, -1.5708F));
		spikes.addOrReplaceChild("spike10", CubeListBuilder.create().texOffs(22, 12).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0095F, -9.6585F, -3.3913F, 0.0F, 0.7418F, -1.5708F));

		PartDefinition vision = root.getChild("head").addOrReplaceChild("vision", CubeListBuilder.create(), PartPose.ZERO);
		vision.addOrReplaceChild("vision01", CubeListBuilder.create().texOffs(59, 46).mirror().addBox(-2.0F, -2.0F, -1.5F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, -7.0F, 1.0F, -0.8727F, -0.4363F, 0.0F));
		vision.addOrReplaceChild("vision02", CubeListBuilder.create().texOffs(61, 40).mirror().addBox(-1.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-5.7369F, -8.1662F, -0.4164F, -0.9599F, -1.0472F, 0.7854F));
		vision.addOrReplaceChild("vision03", CubeListBuilder.create().texOffs(81, 43).addBox(-1.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.8501F, -9.3148F, -2.2639F, 1.0472F, -0.8727F, 2.0071F));
		vision.addOrReplaceChild("vision04", CubeListBuilder.create().texOffs(59, 46).addBox(-2.0F, -2.0F, -1.5F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -7.0F, 1.0F, -0.8727F, 0.4363F, 0.0F));
		vision.addOrReplaceChild("vision05", CubeListBuilder.create().texOffs(61, 40).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.7369F, -8.1662F, -0.4164F, -0.9599F, 1.0472F, -0.7854F));
		vision.addOrReplaceChild("vision06", CubeListBuilder.create().texOffs(81, 43).addBox(-1.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.8501F, -9.3148F, -2.2639F, 1.0472F, 0.8727F, -2.0071F));

		PartDefinition lshoulder = root.getChild("left_arm").addOrReplaceChild("left_Shoulder", CubeListBuilder.create()
    		.texOffs(76, 34).addBox(1.8F, 0.5F, -3.0F, 3.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
    		.texOffs(76, 53).addBox(2.8F, 5.5F, -2.5F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
    		.texOffs(76, 45).addBox(1.8F, 5.5F, -3.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
    		.texOffs(56, 35).addBox(-0.5F, -0.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)),
    		PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

		PartDefinition rshoulder = root.getChild("right_arm").addOrReplaceChild("right_Shoulder", CubeListBuilder.create()
    		.texOffs(76, 34).addBox(-4.7F, 0.5F, -3.0F, 3.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
    		.texOffs(76, 53).addBox(-3.7F, 5.5F, -2.5F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
    		.texOffs(76, 45).addBox(-2.7F, 5.5F, -3.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
    		.texOffs(56, 35).mirror().addBox(-4.5F, -0.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false),
    		PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

		PartDefinition wings = root.getChild("body").addOrReplaceChild("wings", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition leftwing = wings.addOrReplaceChild("left_wing", CubeListBuilder.create()
			.texOffs(37, 0).addBox(-5.0F, -9.0F, 0.0F, 5.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)),
			PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition rightwing = wings.addOrReplaceChild("right_wing", CubeListBuilder.create()
			.texOffs(37, 0).mirror().addBox(0.0F, -9.0F, 0.0F, 5.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false),
			PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

		leftwing.addOrReplaceChild("left_wing_tip", CubeListBuilder.create()
			.texOffs(50, 0).addBox(-6.0F, -10.0F, 0.0F, 6.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)),
			PartPose.offset(-5.0F, 0.0F, 0.0F));
		rightwing.addOrReplaceChild("right_wing_tip", CubeListBuilder.create()
			.texOffs(50, 0).mirror().addBox(0.0F, -10.0F, 0.0F, 6.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false),
			PartPose.offset(5.0F, 0.0F, 0.0F));

		rootConsumer.accept(root);

		return LayerDefinition.create(mesh, textureWidth, textureHeight);
	}

    public AbstractArmorModel<T> withAnimations(LivingEntity entity)
    {
    	float partialTick = Minecraft.getInstance().getFps();
        setupAnim(entity, 0, 0, entity.tickCount + partialTick, 0, 0);
        setupWings(entity, 0, 0, entity.tickCount + partialTick, 0, 0);
        return this;
    }

	@SuppressWarnings("unchecked")
	@Override
	public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		if (!(entity instanceof ArmorStand entityIn))
		{
			super.setupAnim((T) entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			return;
		}

		this.head.xRot = ((float) Math.PI / 180F) * entityIn.getHeadPose().getX();
		this.head.yRot = ((float) Math.PI / 180F) * entityIn.getHeadPose().getY();
		this.head.zRot = ((float) Math.PI / 180F) * entityIn.getHeadPose().getZ();
		this.head.setPos(0.0F, 1.0F, 0.0F);
		this.body.xRot = ((float) Math.PI / 180F) * entityIn.getBodyPose().getX();
		this.body.yRot = ((float) Math.PI / 180F) * entityIn.getBodyPose().getY();
		this.body.zRot = ((float) Math.PI / 180F) * entityIn.getBodyPose().getZ();
		this.leftArm.xRot = ((float) Math.PI / 180F) * entityIn.getLeftArmPose().getX();
		this.leftArm.yRot = ((float) Math.PI / 180F) * entityIn.getLeftArmPose().getY();
		this.leftArm.zRot = ((float) Math.PI / 180F) * entityIn.getLeftArmPose().getZ();
		this.rightArm.xRot = ((float) Math.PI / 180F) * entityIn.getRightArmPose().getX();
		this.rightArm.yRot = ((float) Math.PI / 180F) * entityIn.getRightArmPose().getY();
		this.rightArm.zRot = ((float) Math.PI / 180F) * entityIn.getRightArmPose().getZ();
		this.leftLeg.xRot = ((float) Math.PI / 180F) * entityIn.getLeftLegPose().getX();
		this.leftLeg.yRot = ((float) Math.PI / 180F) * entityIn.getLeftLegPose().getY();
		this.leftLeg.zRot = ((float) Math.PI / 180F) * entityIn.getLeftLegPose().getZ();
		this.leftLeg.setPos(1.9F, 11.0F, 0.0F);
		this.rightLeg.xRot = ((float) Math.PI / 180F) * entityIn.getRightLegPose().getX();
		this.rightLeg.yRot = ((float) Math.PI / 180F) * entityIn.getRightLegPose().getY();
		this.rightLeg.zRot = ((float) Math.PI / 180F) * entityIn.getRightLegPose().getZ();
		this.rightLeg.setPos(-1.9F, 11.0F, 0.0F);
		this.hat.copyFrom(this.head);
	}
	
	@Override
	public void renderToBuffer(PoseStack mStack, VertexConsumer buffer, int light, int overlay, int pColor)
	{
		super.renderToBuffer(mStack, buffer, light, overlay, pColor);
		setPartVisibility(slot);
	}

	private void setPartVisibility(EquipmentSlot slot)
	{
//		final Minecraft mc = Minecraft.getInstance();
//		Player player = mc.player;

		setAllVisible(false);
		switch (slot) {
			case HEAD -> {
				head.visible = true;
				hat.visible = true;
				spikes.visible = false;
				vision.visible = false;
			}
			case CHEST -> {
				body.visible = true;
				rightArm.visible = true;
				leftArm.visible = true;
				lshoulder.visible = false;
				rshoulder.visible = false;
				leftWing.visible = true;
				rightWing.visible = true;
			}
			case LEGS -> {
				body.visible = true;
				rightLeg.visible = true;
				leftLeg.visible = true;
				leftWing.visible = false;
				rightWing.visible = false;
			}
			case FEET -> {
				rightLeg.visible = true;
				leftLeg.visible = true;
			}
			default -> {}
		}
	}
	
	/**
	 * 
	 * WINGS
	 * 
	 */
	public void setupWings(LivingEntity player, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		float rotationTime = player.tickCount % 40 + ageInTicks;

		this.wings.yRot = 3.15f;
		this.wings.setPos(0.0F, 8.0F, 5.0F);
		this.rightWing.setPos(3.0F, 0.0F, 0.0F);
		this.leftWing.setPos(-3.0F, 0.0F, 0.0F);

		if(player.onGround())
		{
			this.rightWing.xRot = -0.1F;
			this.rightWing.yRot = 0.3491F * 1.5F + 0.6491F / 2 * (float) Math.sin(Math.PI * rotationTime / 20);
			this.rightWing.zRot = -0.4F;

			this.leftWing.xRot = this.rightWing.xRot;
			this.leftWing.yRot = -this.rightWing.yRot;
			this.leftWing.zRot = -this.rightWing.zRot;
			
			this.rightWingTip.xRot = 0.0F;
			this.rightWingTip.yRot = 0.0F;
			this.rightWingTip.zRot = 0.0F;

			this.leftWingTip.xRot = -this.rightWingTip.xRot;
			this.leftWingTip.yRot = -this.rightWingTip.yRot;
			this.leftWingTip.zRot = -this.rightWingTip.zRot;
		}
		else
		{
			if(player.fallDistance > 2.2F)
			{
				this.rightWing.yRot = 0.3491F * 1.5F + 1.2491F / 2 * (float) Math.sin(Math.PI * rotationTime / 2);
				this.leftWing.yRot = -this.rightWing.yRot;
			}
		}
	}
}