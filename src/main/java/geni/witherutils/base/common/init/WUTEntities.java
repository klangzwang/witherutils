package geni.witherutils.base.common.init;

import geni.witherutils.api.lib.Names;
import geni.witherutils.base.common.entity.bolt.CursedLightningBolt;
import geni.witherutils.base.common.entity.cursed.zombie.CursedZombie;
import geni.witherutils.base.common.entity.portal.Portal;
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
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class WUTEntities {

	/*
     * 
     * ENTITY
     * 
     */
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, Names.MODID);

    public static final Supplier<EntityType<Worm>> WORM = register("worm", WUTEntities::worm);
    public static final Supplier<EntityType<SoulOrb>> SOULORB = register("soulorb", WUTEntities::soulorb);
    public static final Supplier<EntityType<SoulOrbProjectile>> SOULORBPRO = register("soulorb_projectile", WUTEntities::soulorbpro);
    public static final Supplier<EntityType<Portal>> PORTAL = register("portal", WUTEntities::portal);
    public static final Supplier<EntityType<CursedZombie>> CURSEDZOMBIE = register("cursed_zombie", WUTEntities::cursedzombie);
    public static final Supplier<EntityType<CursedLightningBolt>> CURSEDBOLT = register("cursed_bolt", WUTEntities::cursedbolt);

    private static <E extends Entity> Supplier<EntityType<E>> register(final String name, final Supplier<EntityType.Builder<E>> sup)
    {
        return ENTITY_TYPES.register(name, () -> sup.get().build(name));
    }
    
    private static EntityType.Builder<Worm> worm()
    {
        return EntityType.Builder.of(Worm::new, MobCategory.MISC)
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
    private static EntityType.Builder<Portal> portal()
    {
        return EntityType.Builder.<Portal>of(Portal::new, MobCategory.MISC)
        		.sized(2F, 3F)
                .setTrackingRange(5)
                .setUpdateInterval(20);
    }
    private static EntityType.Builder<CursedZombie> cursedzombie()
    {
        return EntityType.Builder.of(CursedZombie::new, MobCategory.MONSTER)
        		.sized(0.6F, 1.95F)
                .setTrackingRange(64)
                .setUpdateInterval(3)
                .fireImmune()
                .setShouldReceiveVelocityUpdates(true);
    }
    private static EntityType.Builder<CursedLightningBolt> cursedbolt()
    {
        return EntityType.Builder.of(CursedLightningBolt::new, MobCategory.MISC)
        		.noSave()
        		.sized(0.0F, 0.0F)
        		.clientTrackingRange(16)
        		.updateInterval(Integer.MAX_VALUE);
    }

    @EventBusSubscriber(modid = Names.MODID, bus = EventBusSubscriber.Bus.MOD)
    public static class Listener
    {
        @SubscribeEvent
        public static void registerGlobalAttributes(EntityAttributeCreationEvent event)
        {
            event.put(WUTEntities.SOULORB.get(), SoulOrb.createAttributes().build());
            event.put(WUTEntities.CURSEDZOMBIE.get(), CursedZombie.createAttributes().build());
        }

        @SubscribeEvent
        public static void init(RegisterSpawnPlacementsEvent event)
        {
        }
    }
}
