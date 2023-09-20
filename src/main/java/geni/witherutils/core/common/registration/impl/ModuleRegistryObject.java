package geni.witherutils.core.common.registration.impl;

import org.jetbrains.annotations.NotNull;

import geni.witherutils.api.providers.IItemProvider;
import geni.witherutils.core.common.registration.WrappedRegistryObject;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class ModuleRegistryObject<MODULE extends Item> extends WrappedRegistryObject<MODULE> implements IItemProvider {

    public ModuleRegistryObject(RegistryObject<MODULE> registryObject)
    {
        super(registryObject);
    }

    @NotNull
    @Override
    public MODULE asItem()
    {
        return get();
    }
}