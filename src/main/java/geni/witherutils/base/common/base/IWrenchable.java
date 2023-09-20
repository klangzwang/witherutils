package geni.witherutils.base.common.base;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraftforge.common.extensions.IForgeBlockEntity;

public interface IWrenchable extends IForgeBlockEntity {
    
    InteractionResult onWrenched(UseOnContext context);
}
