package geni.witherutils.core.common.registration;

import java.util.function.Supplier;
import net.minecraftforge.registries.RegistryObject;

public class WrappedRegistryObject<T> implements Supplier<T>, INamedEntry {

    protected RegistryObject<T> registryObject;

    protected WrappedRegistryObject(RegistryObject<T> registryObject)
    {
        this.registryObject = registryObject;
    }

    @Override
    public T get()
    {
        return registryObject.get();
    }

    @Override
    public String getInternalRegistryName()
    {
        return registryObject.getId().getPath();
    }
}