package geni.witherutils.base.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import geni.witherutils.api.item.IRotatingItem;
import geni.witherutils.base.client.model.special.SpecialModels;
import geni.witherutils.base.client.render.EffectLib;
import geni.witherutils.base.common.config.common.ItemsConfig;
import geni.witherutils.base.common.item.withersteel.sword.SwordSteelItem;
import geni.witherutils.base.common.item.withersteel.wand.WandSteelItem;
import geni.witherutils.core.common.math.Vector3;
import geni.witherutils.core.common.util.EnergyUtil;
import geni.witherutils.core.common.util.McTimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class RotatingItemBEWLR extends AbstractBEWLRRenderer {

    public static final RotatingItemBEWLR INSTANCE = new RotatingItemBEWLR(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());

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
            ResourceLocation itemRegName = ForgeRegistries.ITEMS.getKey(pStack.getItem());
            BakedModel model = mc.getModelManager().getModel(new ResourceLocation(itemRegName.getNamespace(), "item/" + itemRegName.getPath() + "_helper"));
            
            renderWand(model, itemRegName, rotatingItem, partialTick, mc, player, pStack, matrix, light, rand);
            renderSword(model, itemRegName, rotatingItem, partialTick, mc, player, pStack, matrix, light, rand);
            rotatingGear(model, itemRegName, rotatingItem, mc, pStack, matrix, light);
            renderWaterGeneratorAndShovel(model, itemRegName, rotatingItem, mc, pStack, matrix, light);
            renderWindGeneratorAndFan(model, itemRegName, rotatingItem, mc, pStack, matrix, light);
        }
    }
    
    public void renderWand(BakedModel model, ResourceLocation itemRegName, IRotatingItem rotatingItem, float partialTick, Minecraft mc, LocalPlayer player, ItemStack pStack, PoseStack matrix, int light, RandomSource rand)
    {
        if(itemRegName.getPath().contains("wand"))
        {
        	if(pStack.getItem() instanceof WandSteelItem)
        	{
        		mc.getItemRenderer().renderModelLists(model, pStack, light, OverlayTexture.NO_OVERLAY, matrix, mc.renderBuffers().bufferSource().getBuffer(RenderType.cutout()));
        		
        		WandSteelItem wand = (WandSteelItem) pStack.getItem();
        		if(wand.getPowerLevel(pStack) > 0 && EnergyUtil.getEnergyStored(pStack) > ItemsConfig.WANDENERGYUSE.get())
        		{
        			renderWandLightning(model, itemRegName, rotatingItem, partialTick, mc, player, pStack, matrix, light, rand);
        			
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
    }
    
    public void renderSword(BakedModel model, ResourceLocation itemRegName, IRotatingItem rotatingItem, float partialTick, Minecraft mc, LocalPlayer player, ItemStack pStack, PoseStack matrix, int light, RandomSource rand)
    {
        if(itemRegName.getPath().contains("sword"))
        {
        	if(pStack.getItem() instanceof SwordSteelItem)
        	{
        		mc.getItemRenderer().renderModelLists(model, pStack, light, OverlayTexture.NO_OVERLAY, matrix, mc.renderBuffers().bufferSource().getBuffer(RenderType.cutout()));
    			renderWandLightning(model, itemRegName, rotatingItem, partialTick, mc, player, pStack, matrix, light, rand);
    			mc.getItemRenderer().renderModelLists(SpecialModels.SWORDBASE.getModel(), pStack, light, OverlayTexture.NO_OVERLAY, matrix, mc.renderBuffers().bufferSource().getBuffer(RenderType.translucent()));
    			mc.getItemRenderer().renderModelLists(SpecialModels.SWORDENERGY.getModel(), pStack, light, OverlayTexture.NO_OVERLAY, matrix, mc.renderBuffers().bufferSource().getBuffer(RenderType.translucent()));
    			mc.getItemRenderer().renderModelLists(SpecialModels.SWORDHANDLE.getModel(), pStack, light, OverlayTexture.NO_OVERLAY, matrix, mc.renderBuffers().bufferSource().getBuffer(RenderType.translucent()));
    			mc.getItemRenderer().renderModelLists(SpecialModels.SWORDPEARL.getModel(), pStack, light, OverlayTexture.NO_OVERLAY, matrix, mc.renderBuffers().bufferSource().getBuffer(RenderType.translucent()));
        	}
        }
    }
    
    public void renderWandLightning(BakedModel model, ResourceLocation itemRegName, IRotatingItem rotatingItem, float partialTick, Minecraft mc, LocalPlayer player, ItemStack pStack, PoseStack matrix, int light, RandomSource rand)
    {
    	matrix.pushPose();
    	
        if(McTimerUtil.clientTimer % 5 == 0)
        {
            int pos = rand.nextInt(4);
            for(int i = 0; i < 4; i++)
            {
                if (i != pos) continue;
                float loopOffset = ((i / 4F) * ((float) Math.PI * 2F)) + (McTimerUtil.clientTimer / 100F);
                float rota = ((7 / 64F) * (float) Math.PI * 2F) + (McTimerUtil.clientTimer / 10F) + loopOffset;
                double x = Mth.sin(rota) * 2;
                double z = Mth.cos(rota) * 2;
                double y = Mth.cos(rota + loopOffset) * 1;
                
                matrix.translate(0.75, 0.75, 0.5);
                matrix.scale(0.25f, 0.25f, 0.25f);
                matrix.mulPose(Axis.YN.rotationDegrees(90));
                matrix.mulPose(Axis.XN.rotationDegrees(45));
                EffectLib.renderLightningP2PRotate(matrix, mc.renderBuffers().bufferSource(), new Vector3(x, y, z), Vector3.ZERO, 12, (McTimerUtil.clientTimer), 0.06F, 0.04F, false, 0, 0x6300BD);
            }
        }
        
        matrix.popPose();
    }

    public void rotatingGear(BakedModel model, ResourceLocation itemRegName, IRotatingItem rotatingItem, Minecraft mc, ItemStack pStack, PoseStack pPoseStack, int pPackedLight)
    {
        if(!itemRegName.getPath().contains("gear"))
            return;
        
        pPoseStack.pushPose();
        if (rotatingItem.getTicksPerRotation() != 0)
        {
            pPoseStack.scale(0.8f, 0.8f, 0.8f);
            pPoseStack.translate(0.5, 0.5, 0.5);
            pPoseStack.mulPose(Axis.ZP.rotationDegrees((360.0F / rotatingItem.getTicksPerRotation()) * (mc.player.clientLevel.getGameTime() % rotatingItem.getTicksPerRotation())));
            pPoseStack.translate(-0.5, -0.5, -0.5);
        }
        mc.getItemRenderer().renderModelLists(model, pStack, pPackedLight, OverlayTexture.NO_OVERLAY, pPoseStack, mc.renderBuffers().bufferSource().getBuffer(RenderType.cutout()));
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
        mc.getItemRenderer().renderModelLists(model, pStack, pPackedLight, OverlayTexture.NO_OVERLAY, pPoseStack, mc.renderBuffers().bufferSource().getBuffer(RenderType.cutout()));
        pPoseStack.popPose();
    }
    
    public void renderWaterGeneratorAndShovel(BakedModel model, ResourceLocation itemRegName, IRotatingItem rotatingItem, Minecraft mc, ItemStack pStack, PoseStack pPoseStack, int pPackedLight)
    {
    	double x = Vector3.CENTER.x - 0.5F;
    	double y = Vector3.CENTER.y - 0.5F;
    	double z = Vector3.CENTER.z - 0.5F;

        if(itemRegName.getPath().contains("shovel"))
        {
            pPoseStack.pushPose();
            if (rotatingItem.getTicksPerRotation() != 0)
            {
                pPoseStack.scale(1.0f, 1.0f, 1.0f);
                pPoseStack.translate(0.5, 0.5, 0.5);
                pPoseStack.mulPose(Axis.XP.rotationDegrees(30));
                pPoseStack.mulPose(Axis.YP.rotationDegrees(225));
                pPoseStack.mulPose(Axis.YP.rotationDegrees((360.0F / rotatingItem.getTicksPerRotation() * 3) * (mc.player.clientLevel.getGameTime() % rotatingItem.getTicksPerRotation())));
                pPoseStack.translate(-0.5, -0.5, -0.5);
            }
            mc.getItemRenderer().renderModelLists(model, pStack, pPackedLight, OverlayTexture.NO_OVERLAY, pPoseStack, mc.renderBuffers().bufferSource().getBuffer(RenderType.cutout()));
            pPoseStack.popPose();
        }
        if(itemRegName.getPath().contains("water_generator"))
        {
            pPoseStack.pushPose();
            if (rotatingItem.getTicksPerRotation() != 0)
            {
                pPoseStack.translate(0.5, 0.5, 0.5);
            	pPoseStack.scale(0.65f, 0.65f, 0.65f);
                pPoseStack.mulPose(Axis.XP.rotationDegrees(30));
                pPoseStack.mulPose(Axis.YP.rotationDegrees(225));
                pPoseStack.translate(-0.5, -0.5, -0.5);
            }
            mc.getItemRenderer().renderModelLists(model, pStack, pPackedLight, OverlayTexture.NO_OVERLAY, pPoseStack, mc.renderBuffers().bufferSource().getBuffer(RenderType.cutout()));
            
            pPoseStack.translate(x, y, z);
            
            pPoseStack.translate(0.5, 0.5, 0.5);
            pPoseStack.mulPose(Axis.YP.rotationDegrees((360.0F / rotatingItem.getTicksPerRotation() * 3) * (mc.player.clientLevel.getGameTime() % rotatingItem.getTicksPerRotation())));
        	pPoseStack.translate(-0.5, -0.5, -0.5);

            VertexConsumer vertexBuilder = mc.renderBuffers().bufferSource().getBuffer(RenderType.solid());
            Minecraft.getInstance().getItemRenderer().renderModelLists(SpecialModels.SHOVEL.getModel(), ItemStack.EMPTY, pPackedLight, OverlayTexture.NO_OVERLAY, pPoseStack, vertexBuilder);

            pPoseStack.popPose();
        }
    }
    
    public void renderWindGeneratorAndFan(BakedModel model, ResourceLocation itemRegName, IRotatingItem rotatingItem, Minecraft mc, ItemStack pStack, PoseStack pPoseStack, int pPackedLight)
    {
    	double x = Vector3.CENTER.x - 0.5F;
    	double y = Vector3.CENTER.y - 0.5F;
    	double z = Vector3.CENTER.z - 0.5F;
    	
        if(itemRegName.getPath().contains("fan"))
        {
            pPoseStack.pushPose();
            if (rotatingItem.getTicksPerRotation() != 0)
            {
                pPoseStack.scale(1.0f, 1.0f, 1.0f);
                pPoseStack.translate(0.5, 0.5, 0.5);
                pPoseStack.mulPose(Axis.ZP.rotationDegrees((360.0F / rotatingItem.getTicksPerRotation() * 3) * (mc.player.clientLevel.getGameTime() % rotatingItem.getTicksPerRotation())));
                pPoseStack.translate(-0.5, -0.5, -0.5);
            }
            mc.getItemRenderer().renderModelLists(model, pStack, pPackedLight, OverlayTexture.NO_OVERLAY, pPoseStack, mc.renderBuffers().bufferSource().getBuffer(RenderType.cutout()));
            pPoseStack.popPose();
        }
        if(itemRegName.getPath().contains("wind_generator"))
        {
            pPoseStack.pushPose();
            if (rotatingItem.getTicksPerRotation() != 0)
            {
                pPoseStack.translate(0.5, 0.5, 0.5);
            	pPoseStack.scale(0.7f, 0.7f, 0.7f);
                pPoseStack.mulPose(Axis.XP.rotationDegrees(20));
                pPoseStack.mulPose(Axis.YP.rotationDegrees(210));
                pPoseStack.translate(-0.5, -0.5, -0.5);
            }
            mc.getItemRenderer().renderModelLists(model, pStack, pPackedLight, OverlayTexture.NO_OVERLAY, pPoseStack, mc.renderBuffers().bufferSource().getBuffer(RenderType.cutout()));
            
            pPoseStack.translate(x, y, z);
            
            pPoseStack.translate(0.5, 0.5, 0.5);
            pPoseStack.mulPose(Axis.ZP.rotationDegrees((360.0F / rotatingItem.getTicksPerRotation() * 3) * (mc.player.clientLevel.getGameTime() % rotatingItem.getTicksPerRotation())));
        	pPoseStack.translate(-0.5, -0.5, -0.5);

            VertexConsumer vertexBuilder = mc.renderBuffers().bufferSource().getBuffer(RenderType.cutout());
            Minecraft.getInstance().getItemRenderer().renderModelLists(SpecialModels.FAN.getModel(), ItemStack.EMPTY, pPackedLight, OverlayTexture.NO_OVERLAY, pPoseStack, vertexBuilder);

            pPoseStack.popPose();
        }
    }
}
