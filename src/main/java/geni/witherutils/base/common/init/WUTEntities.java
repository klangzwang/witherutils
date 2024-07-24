package geni.witherutils.base.common.init;

import java.util.function.Supplier;

import geni.witherutils.api.lib.Names;
import geni.witherutils.base.common.entity.soulorb.SoulOrb;
import geni.witherutils.base.common.entity.soulorb.SoulOrbProjectile;
import geni.witherutils.base.common.entity.worm.Worm;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

public class WUTEntities {

	/*
     * 
     * LIVING ENTITY
     * 
     */
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, Names.MODID);

    public static final Supplier<EntityType<Worm>> WORM = register("worm", WUTEntities::worm);
    public static final Supplier<EntityType<SoulOrb>> SOULORB = register("soulorb", WUTEntities::soulorb);
    public static final Supplier<EntityType<SoulOrbProjectile>> SOULORBPRO = register("soulorb_projectile", WUTEntities::soulorbpro);
    
    private static <E extends Entity> Supplier<EntityType<E>> register(final String name, final Supplier<EntityType.Builder<E>> sup)
    {
        return ENTITY_TYPES.register(name, () -> sup.get().build(name));
    }
    
    private static EntityType.Builder<Worm> worm()
    {
        return EntityType.Builder.<Worm>of(Worm::new, MobCategory.MISC)
                .sized(0.6f, 1.8f)
                .setTrackingRange(64)
                .setUpdateInterval(1)
                .setShouldReceiveVelocityUpdates(true);
    }
    private static EntityType.Builder<SoulOrb> soulorb()
    {
        return EntityType.Builder.<SoulOrb>of(SoulOrb::new, MobCategory.MISC)
                .sized(0.6F, 1.8F)
                .setTrackingRange(10)
                .fireImmune();
    }
    private static EntityType.Builder<SoulOrbProjectile> soulorbpro()
    {
        return EntityType.Builder.<SoulOrbProjectile>of(SoulOrbProjectile::new, MobCategory.MISC)
                .sized(.6F, .6F)
                .setTrackingRange(128)
                .setUpdateInterval(1)
                .setShouldReceiveVelocityUpdates(true);
    }
    
    @EventBusSubscriber(modid = Names.MODID, bus = EventBusSubscriber.Bus.MOD)
    public static class Listener
    {
        @SubscribeEvent
        public static void registerGlobalAttributes(EntityAttributeCreationEvent event)
        {
            event.put(WUTEntities.SOULORB.get(), SoulOrb.createAttributes().build());
        }
        
//        @SubscribeEvent
//        public static void init(RegisterSpawnPlacementsEvent event)
//        {
//        	SoulOrb.init(event);
//        }
    }
}
