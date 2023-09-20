package geni.witherutils.base.common.block.sensor.floor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import geni.witherutils.base.common.base.AbstractBlockEntityRenderer;
import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.minecraftforge.client.model.data.ModelData;

public class FloorSensorRenderer extends AbstractBlockEntityRenderer<FloorSensorBlockEntity> {

    public FloorSensorRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public void render(FloorSensorBlockEntity te, float partialTick, PoseStack matrix, MultiBufferSource buffer, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int overlayLight)
    {
        if (te.getLevel() == null)
            return;
        
		matrix.pushPose();
        renderStateCover(te, partialTick, matrix, buffer, mc, light, overlayLight);
        matrix.popPose();
    }
    
	public static void renderStateCover(FloorSensorBlockEntity tile, float v, PoseStack matrix, MultiBufferSource brenderer, Minecraft mc, int light, int overlayLight)
    {
    	boolean isWrenchInHand = mc.player.getMainHandItem().getItem() == WUTItems.WRENCH.get();
    	
    	if (tile.cover != null)
    	{
    		matrix.pushPose();

    		BlockRenderDispatcher renderer = Minecraft.getInstance().getBlockRenderer();
    		BakedModel model = renderer.getBlockModel(tile.cover);
    		
    		for (RenderType layer : model.getRenderTypes(tile.cover, RandomSource.create(tile.cover.getSeed(tile.getBlockPos())), ModelData.EMPTY))
    		{
        		VertexConsumer layerfullornot = brenderer.getBuffer(layer);
        		
        		if (isWrenchInHand)
        		{
        			model = renderer.getBlockModel(WUTBlocks.LINES.get().defaultBlockState());
        			layerfullornot = brenderer.getBuffer(RenderType.cutout());
        		}
    			renderer.getModelRenderer().tesselateBlock(tile.getLevel(), model, tile.cover, tile.getBlockPos(), matrix, layerfullornot, true, RandomSource.create(), tile.cover.getSeed(tile.getBlockPos()), overlayLight, ModelData.EMPTY, layer);
    		}
    		
    		matrix.popPose();
    	}
    }
}
