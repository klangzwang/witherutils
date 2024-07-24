package geni.witherutils.base.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.common.NeoForge;

public class ItemRenderOverrideHandler {
	
    public static void init()
    {
        NeoForge.EVENT_BUS.addListener(ItemRenderOverrideHandler::renderHandEvent);
    }

    private static void renderHandEvent(RenderHandEvent event)
    {
        Minecraft mc = Minecraft.getInstance();
        AbstractClientPlayer player = mc.player;
        
        if (player == null)
        	return;
        
//        ItemStack stack = event.getItemStack();
//        if (event.getHand() == InteractionHand.MAIN_HAND && stack.getItem() instanceof WandSteelItem)
//        {
//            modularItemRenderOverride(stack, event);
//            
//            if (event.isCanceled())
//            	return;
//
//            HumanoidArm handside = mc.player.getMainArm();
//
//            boolean rightHand = handside == HumanoidArm.RIGHT;
//            float swingProgress = event.getSwingProgress();
//            float equippedProgress = 0;
//            PoseStack mStack = event.getPoseStack();
//            mStack.pushPose();
//
//            float f5 = -0.3F * Mth.sin(Mth.sqrt(swingProgress) * (float) Math.PI);
//            float f6 = 0.05F * Mth.sin(Mth.sqrt(swingProgress) * ((float) Math.PI * 2F));
//            float f10 = -0.3F * Mth.sin(swingProgress * (float) Math.PI);
//
//            int l = rightHand ? 1 : -1;
//            mStack.translate((float) l * f5, f6, f10);
//
//            event.setCanceled(true);
//            applyItemArmTransform(mStack, handside, equippedProgress);
//
//            mc.gameRenderer.itemInHandRenderer.renderItem(mc.player, stack, rightHand ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, !rightHand, mStack, event.getMultiBufferSource(), event.getPackedLight());
//            mStack.popPose();
//        }
    }

    private static void applyItemArmTransform(PoseStack poseStack, HumanoidArm arm, float equippedProg)
    {
        int i = arm == HumanoidArm.RIGHT ? 1 : -1;
        poseStack.translate((float) i * 0.56F, -0.52F + equippedProg * -0.6F, -0.72F);
    }

    @SuppressWarnings("unused")
	private static void modularItemRenderOverride(ItemStack stack, RenderHandEvent event)
    {
        Minecraft mc = Minecraft.getInstance();
        AbstractClientPlayer player = mc.player;
        
        if (player == null || player.isScoping() || !player.isUsingItem() || player.getUseItemRemainingTicks() <= 0 || player.getUsedItemHand() != event.getHand())
        	return;

        event.setCanceled(true);

        ItemInHandRenderer renderer = mc.gameRenderer.itemInHandRenderer;
        if (event.getHand() == InteractionHand.MAIN_HAND)
        {
            renderArmWithItem(event, renderer, mc.player, InteractionHand.MAIN_HAND, event.getItemStack(), event.getEquipProgress(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
        }
        else
        {
            renderArmWithItem(event, renderer, mc.player, InteractionHand.OFF_HAND, event.getItemStack(), event.getEquipProgress(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
        }
    }

    private static void renderArmWithItem(RenderHandEvent event, ItemInHandRenderer renderer, AbstractClientPlayer clientPlayer, InteractionHand hand, ItemStack stack, float handHeight, PoseStack poseStack, MultiBufferSource getter, int packedLight)
    {
        boolean renderingMainHand = hand == InteractionHand.MAIN_HAND;
        HumanoidArm renderingArm = renderingMainHand ? clientPlayer.getMainArm() : clientPlayer.getMainArm().getOpposite();
        boolean rightHanded = renderingArm == HumanoidArm.RIGHT;

        poseStack.pushPose();
        applyItemArmTransform(poseStack, renderingArm, handHeight);
        renderer.renderItem(clientPlayer, stack, rightHanded ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, !rightHanded, poseStack, getter, packedLight);
        poseStack.popPose();
    }
}
