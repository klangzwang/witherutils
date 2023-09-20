package geni.witherutils.base.common.init;

import geni.witherutils.WitherUtils;
import geni.witherutils.core.common.registration.impl.FertilizerDeferredRegister;
import geni.witherutils.core.common.registration.impl.FertilizerRegistryObject;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;

public class WUTFerts {

	public static final FertilizerDeferredRegister FERTS = new FertilizerDeferredRegister(WitherUtils.MODID);
	
	public static final FertilizerRegistryObject<Item> MEAL = FERTS.register("meal", BoneMealItem::new);
}
