package geni.witherutils.base.common.init;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.base.WitherItem;
import geni.witherutils.core.common.registration.impl.ModuleDeferredRegister;
import geni.witherutils.core.common.registration.impl.ModuleRegistryObject;
import net.minecraft.world.item.Item;

public class WUTModules {

	public static final ModuleDeferredRegister MODULES = new ModuleDeferredRegister(WitherUtils.MODID);
	
	public static final ModuleRegistryObject<Item> MEAL = MODULES.register("detector", WitherItem::new);
}
