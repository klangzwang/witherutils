package geni.witherutils.base.client.render.item;

import java.awt.Color;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import geni.witherutils.base.client.model.special.SpecialModels;
import geni.witherutils.base.client.render.EffectLib;
import geni.witherutils.base.common.config.common.ItemsConfig;
import geni.witherutils.base.common.item.withersteel.wand.WandSteelItem;
import geni.witherutils.core.common.item.IRotatingItem;
import geni.witherutils.core.common.math.Vector3;
import geni.witherutils.core.common.util.EnergyUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RotatingItemBEWLR extends AbstractBEWLRRenderer {

    public static final RotatingItemBEWLR INSTANCE = new RotatingItemBEWLR(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());

//    public static final List<ResourceLocation> SWORD_PARTS = IntStream.range(0, 3).mapToObj((name) -> {
//        return WitherUtilsRegistry.loc("wither/sword/sword_" + name);
//     }).collect(Collectors.toList());

    public RotatingItemBEWLR(BlockEntityRenderDispatcher pBlockEntityRenderDispatcher, EntityModelSet pEntityModelSet)
    {
        super(pBlockEntityRenderDispatcher, pEntityModelSet);
    }

    @Override
    public void render(ItemStack pStack, ItemDisplayContext pTransformType, float partialTick, PoseStack matrix, MultiBufferSource buffer, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int overlayLight, RandomSource rand)
    {
        if(mc == null)
            return;

        if (pStack.getItem() instanceof IRotatingItem rotatingItem)
        {
            ResourceLocation itemRegName = BuiltInRegistries.ITEM.getKey(pStack.getItem());
            if(itemRegName.getPath().contains("wand"))
                renderWand(partialTick, mc, player, pStack, matrix, light, rand);
            if(itemRegName.getPath().contains("iron_gear"))
            	rotatingGear(rotatingItem, mc, pStack, matrix, light);
        }
    }
    
    public void renderWand(float partialTick, Minecraft mc, LocalPlayer player, ItemStack pStack, PoseStack matrix, int light, RandomSource rand)
    {
    	if(pStack.getItem() instanceof WandSteelItem wand)
    	{
    		mc.getItemRenderer().renderModelLists(SpecialModels.WANDHELPER.getModel(), pStack, light, OverlayTexture.NO_OVERLAY, matrix, mc.renderBuffers().bufferSource().getBuffer(RenderType.cutout()));

            if(wand.getPowerLevel(pStack) > 0 && EnergyUtil.getEnergyStored(pStack) > ItemsConfig.WANDENERGYUSE.get())
    		{
    			renderWandLightning(partialTick, mc, matrix, rand);
    			
        		if(player.swinging)
        		{
        			mc.getItemRenderer().renderModelLists(SpecialModels.WANDSWING.getModel(), pStack, light, OverlayTexture.NO_OVERLAY, matrix, mc.renderBuffers().bufferSource().getBuffer(RenderType.translucent()));
        		}
        		else
        		{
        	    	matrix.pushPose();
        			float time = mc.player.clientLevel.getGameTime() + partialTick;
        			double offset = Math.sin(time * 0.4D / 8.0D) / 30.0D;
        	    	matrix.translate(0.5 + offset, 0.5 + offset, 0.51);
        	    	matrix.translate(-0.5, -0.5, -0.5);
        	        mc.getItemRenderer().renderModelLists(SpecialModels.WANDPOWER.getModel(), pStack, light, OverlayTexture.NO_OVERLAY, matrix, mc.renderBuffers().bufferSource().getBuffer(RenderType.translucent()));
        	        matrix.popPose();
        		}
    		}
    	}
    }
    
    public void renderWandLightning(float partialTick, Minecraft mc, PoseStack matrix, RandomSource rand)
    {
    	matrix.pushPose();
    	
        if(partialTick % 5 == 0)
        {
            int pos = rand.nextInt(4);
            for(int i = 0; i < 4; i++)
            {
                if (i != pos) continue;
                float loopOffset = ((i / 4F) * ((float) Math.PI * 2F)) + (partialTick / 100F);
                float rota = ((7 / 64F) * (float) Math.PI * 2F) + (partialTick / 10F) + loopOffset;
                double x = Mth.sin(rota) * 2;
                double z = Mth.cos(rota) * 2;
                double y = Mth.cos(rota + loopOffset) * 1;
                
                matrix.translate(0.75, 0.75, 0.5);
                matrix.scale(0.25f, 0.25f, 0.25f);
                matrix.mulPose(Axis.YN.rotationDegrees(90));
                matrix.mulPose(Axis.XN.rotationDegrees(45));
                long longPartialTick = (long) partialTick;
                EffectLib.renderLightningP2PRotate(matrix, mc.renderBuffers().bufferSource(), new Vector3(x, y, z), Vector3.ZERO, 12, longPartialTick, 0.06F, 0.04F, false, 0.f, 0x6300BD);
            }
        }
        
        matrix.popPose();
    }
    
    public void rotatingGear(IRotatingItem rotatingItem, Minecraft mc, ItemStack pStack, PoseStack pPoseStack, int pPackedLight)
    {
        pPoseStack.pushPose();
        if (rotatingItem.getTicksPerRotation() != 0)
        {
            pPoseStack.scale(0.8f, 0.8f, 0.8f);
            pPoseStack.translate(0.5, 0.5, 0.5);
            pPoseStack.mulPose(Axis.ZP.rotationDegrees((360.0F / rotatingItem.getTicksPerRotation()) * (mc.player.clientLevel.getGameTime() % rotatingItem.getTicksPerRotation())));
            pPoseStack.translate(-0.5, -0.5, -0.5);
        }
        mc.getItemRenderer().renderModelLists(SpecialModels.IRONGEAR.getModel(), pStack, pPackedLight, OverlayTexture.NO_OVERLAY, pPoseStack, mc.renderBuffers().bufferSource().getBuffer(RenderType.cutout()));
        pPoseStack.popPose();
        
        pPoseStack.pushPose();
        if (rotatingItem.getTicksPerRotation() != 0)
        {
            pPoseStack.scale(0.9f, 0.9f, 0.9f);
            pPoseStack.translate(0.15f, 0.1f, 0.0f);
            pPoseStack.translate(0.5, 0.5, 0.5);
            pPoseStack.mulPose(Axis.ZP.rotationDegrees((-360.0F / rotatingItem.getTicksPerRotation()) * (mc.player.clientLevel.getGameTime() % rotatingItem.getTicksPerRotation())));
            pPoseStack.translate(-0.5, -0.5, -0.5);
        }
        mc.getItemRenderer().renderModelLists(SpecialModels.IRONGEAR.getModel(), pStack, pPackedLight, OverlayTexture.NO_OVERLAY, pPoseStack, mc.renderBuffers().bufferSource().getBuffer(RenderType.cutout()));
        pPoseStack.popPose();
    }

    public Color getColor(ResourceLocation rl)
    {
        if(rl.getPath().contains("adapter_colorchange"))
    		return Color.PINK;
        else if(rl.getPath().contains("adapter_damage"))
    		return Color.RED;
        else if(rl.getPath().contains("adapter_explosion"))
    		return Color.YELLOW;
        else if(rl.getPath().contains("adatper_quarry"))
    		return Color.MAGENTA;
		return Color.WHITE;
    }
}
