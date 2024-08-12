package geni.witherutils.base.common.item.shovel;

import geni.witherutils.base.common.base.WitherItem;
import geni.witherutils.base.common.io.item.MachineInstallable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class ShovelItem extends WitherItem {
	
	private final ShovelType shovelType;
	
	public ShovelItem(ShovelType shovelTypeIn, Properties pProperties)
	{
		super(pProperties.stacksTo(1));
		this.shovelType = shovelTypeIn;
	}
	
    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context)
    {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (level.getBlockEntity(pos) instanceof MachineInstallable equippable)
            return equippable.tryItemInstall(stack, context, 0);
        return super.onItemUseFirst(stack, context);
    }
	
    public ShovelType getShovelType()
    {
    	return shovelType;
    }
	
    public enum ShovelType
    {
        BASIC,
        ADVANCED,
        MASTER;
    }
}
