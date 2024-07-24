//package geni.witherutils.base.common.init;
//
//import java.util.HashMap;
//import java.util.UUID;
//import java.util.function.Function;
//import java.util.function.Supplier;
//
//import geni.witherutils.api.WitherUtilsRegistry;
//import net.minecraft.core.registries.Registries;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.ai.attributes.Attribute;
//import net.minecraft.world.entity.ai.attributes.RangedAttribute;
//import net.neoforged.bus.api.SubscribeEvent;
//import net.neoforged.fml.common.EventBusSubscriber;
//import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
//import net.neoforged.neoforge.registries.DeferredHolder;
//import net.neoforged.neoforge.registries.DeferredRegister;
//
//@EventBusSubscriber(modid = WitherUtilsRegistry.MODID, bus = EventBusSubscriber.Bus.MOD)
//public class WUTAttributes {
//
//    public static final HashMap<Supplier<Attribute>, UUID> UUIDS = new HashMap<>();
//    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, WitherUtilsRegistry.MODID);
//
//    public static final DeferredHolder<Attribute, Attribute> MAX_SOULS = registerAttribute("souls.max_souls", (id) -> new RangedAttribute(id, 20.0D, 0.0D, 1024.0D).setSyncable(true), "1ce4960d-c50e-44bf-ad23-7bcd77f4c1dc");
//    public static final DeferredHolder<Attribute, Attribute> SOULS_LOOSE = registerAttribute("souls.souls_loose", (id) -> new RangedAttribute(id, 1.0D, 0.0D, 1024.0D).setSyncable(true), "d74ded8f-c5b6-4222-80e2-dbea7ccf8d02");
//
//    public static DeferredHolder<Attribute, Attribute> registerAttribute(String name, Function<String, Attribute> attribute, String uuid)
//    {
//        return registerAttribute(name, attribute, UUID.fromString(uuid));
//    }
//
//    public static DeferredHolder<Attribute, Attribute> registerAttribute(String name, Function<String, Attribute> attribute, UUID uuid)
//    {
//    	DeferredHolder<Attribute, Attribute> registryObject = ATTRIBUTES.register(name, () -> attribute.apply(name));
//        UUIDS.put(registryObject, uuid);
//        return registryObject;
//    }
//
//    @SubscribeEvent
//    public static void modifyEntityAttributes(EntityAttributeModificationEvent event)
//    {
//        for (EntityType<? extends LivingEntity> entity : event.getTypes())
//        {
//        	if (entity == EntityType.PLAYER)
//        	{
//        		event.add(entity, MAX_SOULS);
//        		event.add(entity, SOULS_LOOSE);
//        	}
//        }
//    }
//}
