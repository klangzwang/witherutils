package geni.witherutils.core.common.registration.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import geni.witherutils.api.providers.IItemProvider;
import geni.witherutils.core.common.registration.WrappedDeferredRegister;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.ForgeRegistries;

public class ModuleDeferredRegister extends WrappedDeferredRegister<Item> {

    private final List<IItemProvider> allItems = new ArrayList<>();

    public ModuleDeferredRegister(String modid) {
        super(modid, ForgeRegistries.ITEMS);
    }

    public static Item.Properties getBaseProperties() {
        return new Item.Properties();
    }

    public ModuleRegistryObject<Item> register(String name) {
        return register(name, Item::new);
    }

    public ModuleRegistryObject<Item> registerUnburnable(String name) {
        return registerUnburnable(name, Item::new);
    }

    public ModuleRegistryObject<Item> register(String name, Rarity rarity) {
        return register(name, properties -> new Item(properties.rarity(rarity)));
    }

    public <MODULE extends Item> ModuleRegistryObject<MODULE> register(String name, Function<Item.Properties, MODULE> sup) {
        return register(name, () -> sup.apply(getBaseProperties()));
    }

    public <MODULE extends Item> ModuleRegistryObject<MODULE> registerUnburnable(String name, Function<Item.Properties, MODULE> sup) {
        return register(name, () -> sup.apply(getBaseProperties().fireResistant()));
    }

    public <MODULE extends Item> ModuleRegistryObject<MODULE> register(String name, Supplier<? extends MODULE> sup) {
    	ModuleRegistryObject<MODULE> registeredItem = register(name, sup, ModuleRegistryObject::new);
        allItems.add(registeredItem);
        return registeredItem;
    }

    public List<IItemProvider> getAllItems()
    {
        return Collections.unmodifiableList(allItems);
    }
}