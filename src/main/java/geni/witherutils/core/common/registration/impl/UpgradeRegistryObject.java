package geni.witherutils.core.common.registration.impl;

import org.jetbrains.annotations.NotNull;

import geni.witherutils.api.providers.IItemProvider;
import geni.witherutils.core.common.registration.WrappedRegistryObject;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class UpgradeRegistryObject<UPGRADE extends Item> extends WrappedRegistryObject<UPGRADE> implements IItemProvider {

    public UpgradeRegistryObject(RegistryObject<UPGRADE> registryObject)
    {
        super(registryObject);
    }

    @NotNull
    @Override
    public UPGRADE asItem()
    {
        return get();
    }
}