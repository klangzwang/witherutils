package geni.witherutils.base.common.block.cutter;

import java.util.List;

import javax.annotation.Nullable;

import geni.witherutils.base.client.ClientTooltipHandler;
import geni.witherutils.base.common.base.WitherSimpleBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CutterBlock extends WitherSimpleBlock {

	public CutterBlock(BlockBehaviour.Properties props)
	{
		super(props);
	}

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag flag)
    {
        ClientTooltipHandler.Tooltip.addInformation(stack, world, tooltip, flag, true);
    	tooltip.add(Component.translatable("Most Cutter Block needs Athena Mod"));
    }
}
