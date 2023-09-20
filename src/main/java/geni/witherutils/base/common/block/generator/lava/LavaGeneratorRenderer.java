package geni.witherutils.base.common.block.generator.lava;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import geni.witherutils.base.client.model.special.SpecialModels;
import geni.witherutils.base.common.base.AbstractBlockEntityRenderer;
import geni.witherutils.core.common.math.Vector3;
import geni.witherutils.core.common.util.FacingUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class LavaGeneratorRenderer extends AbstractBlockEntityRenderer<LavaGeneratorBlockEntity> {

    public LavaGeneratorRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public void render(LavaGeneratorBlockEntity te, float partialTick, PoseStack matrix, MultiBufferSource buffer, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int overlayLight)
    {
        if (te.getLevel() == null)
            return;

        matrix.pushPose();
        
        double x = Vector3.CENTER.x - 0.5F;
        double y = Vector3.CENTER.y - 0.5F;
        double z = Vector3.CENTER.z - 0.5F;

        matrix.translate(x, y, z);
        
        for(Direction facing : FacingUtil.FACES_AROUND_Y)
        {
        	BlockState state = te.getLevel().getBlockState(te.getBlockPos().relative(facing));
        	if(!state.isAir() && state.getBlock() == Blocks.LAVA)
        	{
                if(facing != null)
                {
                    matrix.translate(0.5, 0.5, 0.5);
                    if(facing == Direction.NORTH)
                    {
                    }
                    else if(facing == Direction.SOUTH)
                    {
                    	matrix.mulPose(Axis.YN.rotationDegrees(180));
                    }
                    else if(facing == Direction.EAST)
                    {
                    	matrix.mulPose(Axis.YN.rotationDegrees(90));
                    }
                    else if(facing == Direction.WEST)
                    {
                    	matrix.mulPose(Axis.YP.rotationDegrees(90));
                    }
                    matrix.translate(-0.5, -0.5, -0.5);
                    
                    VertexConsumer vertexBuilder = buffer.getBuffer(RenderType.solid());
                    Minecraft.getInstance().getItemRenderer().renderModelLists(SpecialModels.LAVA.getModel(), ItemStack.EMPTY, light, overlayLight, matrix, vertexBuilder);
                }
        	}
        }
        
        matrix.popPose();
    }
}
