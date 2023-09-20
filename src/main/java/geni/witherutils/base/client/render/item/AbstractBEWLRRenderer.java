package geni.witherutils.base.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import geni.witherutils.core.common.util.McTimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import static net.minecraft.world.item.ItemDisplayContext.*;

import org.joml.Quaternionf;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractBEWLRRenderer extends BlockEntityWithoutLevelRenderer {

	public AbstractBEWLRRenderer(BlockEntityRenderDispatcher disp, EntityModelSet set)
	{
		super(disp, set);
	}

    @Override
    public void renderByItem(ItemStack pStack, ItemDisplayContext pTransformType, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pOverlay)
    {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        float partialTick = McTimerUtil.renderPartialTickTime;
        RandomSource rand = RandomSource.create();
        
        ResourceLocation itemRegName = ForgeRegistries.ITEMS.getKey(pStack.getItem());
        
        if (player != null)
        {
            render(pStack, pTransformType, partialTick, pPoseStack, pBuffer, mc, player.clientLevel, player, pPackedLight, pOverlay, rand);
            
            if(itemRegName.getPath().contains("shield"))
            {
            	handleShieldPerspective(player, pTransformType, pPoseStack);
            }
        }
    }
    
    public abstract void render(ItemStack pStack, ItemDisplayContext pTransformType, float partialTick, PoseStack matrix, MultiBufferSource buffer, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int overlayLight, RandomSource rand);
    
	public static void renderItemStack(BakedModel model, ItemStack stack, PoseStack matrix, Minecraft mc, int light, RenderType type)
    {
		matrix.pushPose();
		VertexConsumer consumer = mc.renderBuffers().bufferSource().getBuffer(type);
		mc.getItemRenderer().renderModelLists(model, stack, light, OverlayTexture.NO_OVERLAY, matrix, consumer);
		matrix.popPose();
    }
	
	/*
	 * 
	 * NONFACING
	 * 
	 */
	public static void renderSpecialModel(BakedModel model, ItemDisplayContext transformType, boolean leftHanded, PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int color, int lightTexture, int overlayTexture, RenderType renderType)
    {
        matrixStack.pushPose();
        net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(matrixStack, model, transformType, leftHanded);
        
        if(!model.isCustomRenderer())
        {
            VertexConsumer vertexBuilder = renderTypeBuffer.getBuffer(renderType);
            Minecraft.getInstance().getItemRenderer().renderModelLists(model, ItemStack.EMPTY, lightTexture, overlayTexture, matrixStack, vertexBuilder);
        }
        matrixStack.popPose();
    }
	
	public void handleShieldPerspective(LivingEntity holder, ItemDisplayContext cameraItemDisplayContext, PoseStack mat)
	{
		if(holder == null || !holder.isUsingItem())
			return;
		
		boolean leftHand = cameraItemDisplayContext == FIRST_PERSON_LEFT_HAND || cameraItemDisplayContext == THIRD_PERSON_LEFT_HAND;
		boolean rightHand = cameraItemDisplayContext == FIRST_PERSON_RIGHT_HAND || cameraItemDisplayContext == THIRD_PERSON_RIGHT_HAND;
		
		if(!leftHand && !rightHand)
			return;
		
		boolean leftIsMain = holder.getMainArm() == HumanoidArm.LEFT;
		InteractionHand inHand = (leftIsMain == leftHand) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
		
		if(holder.getUsedItemHand() != inHand)
			return;

		if(cameraItemDisplayContext == FIRST_PERSON_RIGHT_HAND)
		{
			mat.mulPose(new Quaternionf().rotateXYZ(-.15F, 0, 0));
			mat.translate(-.25, .5, -.4375);
		}
		else if(cameraItemDisplayContext == THIRD_PERSON_RIGHT_HAND)
		{
			mat.mulPose(new Quaternionf().rotateXYZ(0.52359F, 0, 0));
			mat.mulPose(new Quaternionf().rotateXYZ(0, 0.78539F, 0));
			mat.translate(.40625, -.125, -.125);
		}
		if(cameraItemDisplayContext == FIRST_PERSON_LEFT_HAND)
		{
			mat.mulPose(new Quaternionf().rotateXYZ(.15F, 0, 0));
			mat.translate(-.25, .375, .4375);
		}
		else if(cameraItemDisplayContext == THIRD_PERSON_LEFT_HAND)
		{
			mat.mulPose(new Quaternionf().rotateX(-0.52359F).rotateY(-0.78539F));
			mat.translate(-.1875, .3125, .4375);
		}
	}
}
