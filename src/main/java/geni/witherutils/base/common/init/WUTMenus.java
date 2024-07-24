package geni.witherutils.base.common.init;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.common.block.creative.CreativeEnergyContainer;
import geni.witherutils.base.common.item.cutter.CutterContainer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class WUTMenus {
	
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, WitherUtilsRegistry.MODID);

	public static final DeferredHolder<MenuType<?>, MenuType<CreativeEnergyContainer>> CREATIVEGEN = MENU_TYPES.register("creativeenergy", () -> IMenuTypeExtension.create(CreativeEnergyContainer::new));
	
	public static final DeferredHolder<MenuType<?>, MenuType<CutterContainer>> CUTTER = MENU_TYPES.register("cutter", () -> IMenuTypeExtension.create(CutterContainer::new));
}
