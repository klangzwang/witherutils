package geni.witherutils.core.common.registration.impl;

import org.jetbrains.annotations.NotNull;

import geni.witherutils.api.providers.IItemProvider;
import geni.witherutils.core.common.registration.WrappedRegistryObject;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class FertilizerRegistryObject<FERTILIZER extends Item> extends WrappedRegistryObject<FERTILIZER> implements IItemProvider {

    public FertilizerRegistryObject(RegistryObject<FERTILIZER> registryObject)
    {
        super(registryObject);
    }

    @NotNull
    @Override
    public FERTILIZER asItem()
    {
        return get();
    }
}