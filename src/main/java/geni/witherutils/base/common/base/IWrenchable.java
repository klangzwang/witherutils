package geni.witherutils.base.common.base;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;

public interface IWrenchable {
    
    InteractionResult onWrenched(UseOnContext context);
}
