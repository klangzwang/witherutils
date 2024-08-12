package geni.witherutils.base.common.init;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.common.item.fakejob.FakeJobActivatingItem;
import geni.witherutils.base.common.item.fakejob.FakeJobClickingItem;
import geni.witherutils.base.common.item.fakejob.FakeJobPlacingItem;
import geni.witherutils.base.common.item.fakejob.FakeJobMiningItem;
import geni.witherutils.base.common.item.fakejob.FakeJobScanningItem;
import geni.witherutils.core.common.registration.impl.AdapterDeferredRegister;
import geni.witherutils.core.common.registration.impl.AdapterRegistryObject;
import net.minecraft.world.item.Item;

public class WUTAdapters {

	public static final AdapterDeferredRegister ADAPTER_TYPES = new AdapterDeferredRegister(WitherUtilsRegistry.MODID);

    /*
    *
    * FAKEJOBS
    *
    */
    public static final AdapterRegistryObject<Item> FAKEJOB_ACTIVATING = ADAPTER_TYPES.register("fakejob_activating", () -> new FakeJobActivatingItem());
    public static final AdapterRegistryObject<Item> FAKEJOB_CLICKING = ADAPTER_TYPES.register("fakejob_clicking", () -> new FakeJobClickingItem());
    public static final AdapterRegistryObject<Item> FAKEJOB_MINING = ADAPTER_TYPES.register("fakejob_mining", () -> new FakeJobMiningItem());
    public static final AdapterRegistryObject<Item> FAKEJOB_PLACING = ADAPTER_TYPES.register("fakejob_placing", () -> new FakeJobPlacingItem());
    public static final AdapterRegistryObject<Item> FAKEJOB_SCANNING = ADAPTER_TYPES.register("fakejob_scanning", () -> new FakeJobScanningItem());
}
