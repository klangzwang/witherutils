package geni.witherutils.base.common.io.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

public interface MachineInstallable {

    InteractionResult tryItemInstall(ItemStack stack, UseOnContext context, int slot);
}

