package geni.witherutils.core.common.registration.impl;

import java.util.function.Function;
import java.util.function.Supplier;

import geni.witherutils.core.common.registration.WUTDeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class AdapterDeferredRegister extends WUTDeferredRegister<Item> {

    public AdapterDeferredRegister(String modid)
    {
        super(Registries.ITEM, modid, AdapterRegistryObject::new);
    }

    public AdapterRegistryObject<Item> register(String name)
    {
        return registerItem(name, Item::new);
    }

    public AdapterRegistryObject<Item> registerUnburnable(String name)
    {
        return registerUnburnable(name, Item::new);
    }

    public AdapterRegistryObject<Item> register(String name, Rarity rarity)
    {
        return registerItem(name, properties -> new Item(properties.rarity(rarity)));
    }

    public <ITEM extends Item> AdapterRegistryObject<ITEM> registerItem(String name, Function<Item.Properties, ITEM> sup)
    {
        return register(name, () -> sup.apply(new Item.Properties()));
    }

    public <ITEM extends Item> AdapterRegistryObject<ITEM> registerUnburnable(String name, Function<Item.Properties, ITEM> sup)
    {
        return register(name, () -> sup.apply(new Item.Properties().fireResistant()));
    }
    @Override
    @SuppressWarnings("unchecked")
    public <ITEM extends Item> AdapterRegistryObject<ITEM> register(String name, Supplier<? extends ITEM> sup)
    {
        return (AdapterRegistryObject<ITEM>) super.register(name, sup);
    }
}