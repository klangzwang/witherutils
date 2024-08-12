package geni.witherutils.base.common.block.generator.solar;

import java.util.List;

import geni.witherutils.base.client.ClientTooltipHandler;
import geni.witherutils.base.common.config.common.SolarConfig;
import geni.witherutils.core.common.item.ItemBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

public class SolarPanelBlockItem extends ItemBlock<Block> implements ISolarPowered {

	private final SolarType type;
	
	public SolarPanelBlockItem(Block block, Properties prop, SolarType type)
	{
		super(block, prop);
		this.type = type;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext pContext, List<Component> tooltip, TooltipFlag flag)
	{
		ClientTooltipHandler.Tooltip.addInformation(stack, pContext, tooltip, flag, true);
	}

	@Override
	public SolarType getType()
	{
		return type;
	}
	
	@SuppressWarnings("incomplete-switch")
	@Override
	public int getProduction(SolarType type)
	{
		switch(type)
		{
			case BASIC:
				return SolarConfig.SOLARBASICINPUTRF.get();
			case ADVANCED:
				return SolarConfig.SOLARADVINPUTRF.get();
			case ULTRA:
				return SolarConfig.SOLARULTRAINPUTRF.get();
		}
		return 0;
	}
}
