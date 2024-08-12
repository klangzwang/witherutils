package geni.witherutils.core.common.registration.impl;

import org.jetbrains.annotations.NotNull;

import geni.witherutils.api.providers.IItemProvider;
import geni.witherutils.core.common.registration.WUTDeferredHolder;
import net.minecraft.Util;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class AdapterRegistryObject<ADAPTER extends Item> extends WUTDeferredHolder<Item, ADAPTER> implements IItemProvider {

    private final String translationKey;
    
	public AdapterRegistryObject(ResourceKey<Item> key)
	{
		super(key);
        translationKey = Util.makeDescriptionId("item", getId());
	}

    @NotNull
    @Override
    public ADAPTER asItem()
    {
        return get();
    }
    
    @Override
    public String getTranslationKey()
    {
        return translationKey;
    }
}