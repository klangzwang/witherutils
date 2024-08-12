package geni.witherutils.base.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class ArmorHelmetLayer extends RenderLayer {

	private final RenderLayerParent parent;

	public ArmorHelmetLayer(RenderLayerParent parent)
    {
        super(parent);
        this.parent = parent;
    }

    @Override
    public void render(PoseStack matrix, MultiBufferSource buffer, int packedLightIn, Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) 
    {
        PoseStack.Pose currentMatrix = matrix.last();
        float red = 255.0F;
        float green = 255.0F;
        float blue = 255.0F;
        VertexConsumer vertexBuffer = buffer.getBuffer(RenderType.translucent());

	    matrix.mulPose(Axis.YP.rotationDegrees(netHeadYaw));
	    matrix.mulPose(Axis.XP.rotationDegrees(headPitch));

	    matrix.pushPose();
	    matrix.mulPose(Axis.XP.rotationDegrees(90));
	    matrix.scale(0.4f, 1.0f, 0.4f);
	    matrix.translate(0, 0.877, 0.75);
        if(entity instanceof LivingEntity)
        {
            if(entity instanceof Player)
            {
//            	Player player = (Player) entity;
//            	if(player.getItemBySlot(EquipmentSlot.HEAD).getItem() == WUTItems.STEELARMOR_HELMET.get())
//            	{
//        			if(EnchantmentHelper.getEnchantments(player.getItemBySlot(EquipmentSlot.HEAD)).get(WUTEnchants.SOLAR_POWER.get()) != null)
//        				Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack(WUTBlocks.SOLARBASIC.get()), ItemDisplayContext.HEAD, packedLightIn, OverlayTexture.NO_OVERLAY, matrix, buffer, player.level(), 0);
//            	}
            }
        }
        matrix.popPose();
    }
}
