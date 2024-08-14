package geni.witherutils.base.common.init;

import geni.witherutils.api.lib.Names;
import geni.witherutils.base.common.entity.bolt.CursedLightningBolt;
import geni.witherutils.base.common.entity.cursed.creeper.CursedCreeper;
import geni.witherutils.base.common.entity.cursed.dryhead.CursedDryHead;
import geni.witherutils.base.common.entity.cursed.skeleton.CursedSkeleton;
import geni.witherutils.base.common.entity.cursed.spider.CursedSpider;
import geni.witherutils.base.common.entity.cursed.zombie.CursedZombie;
import geni.witherutils.base.common.entity.naked.ChickenNaked;
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
    public static final Supplier<EntityType<CursedLightningBolt>> CURSEDBOLT = register("cursed_bolt", WUTEntities::cursedbolt);
    public static final Supplier<EntityType<CursedCreeper>> CURSEDCREEPER = register("cursedcreeper", WUTEntities::cursedcreeper);
    public static final Supplier<EntityType<CursedDryHead>> CURSEDDRYHEAD = register("curseddryhead", WUTEntities::curseddryhead);
    public static final Supplier<EntityType<CursedSkeleton>> CURSEDSKELETON = register("cursedskeleton", WUTEntities::cursedskeleton);
    public static final Supplier<EntityType<CursedSpider>> CURSEDSPIDER = register("cursedspider", WUTEntities::cursedspider);
    public static final Supplier<EntityType<CursedZombie>> CURSEDZOMBIE = register("cursedzombie", WUTEntities::cursedzombie);
    public static final Supplier<EntityType<ChickenNaked>> CHICKENNAKED = register("chickennaked", WUTEntities::chickennaked);
    public static final Supplier<EntityType<Portal>> PORTAL = register("portal", WUTEntities::portal);

    private static <E extends Entity> Supplier<EntityType<E>> register(final String name, final Supplier<EntityType.Builder<E>> sup)
    {
        return ENTITY_TYPES.register(name, () -> sup.get().build(name));
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
    private static EntityType.Builder<CursedCreeper> cursedcreeper()
    {
        return EntityType.Builder.of(CursedCreeper::new, MobCategory.MONSTER)
        		.sized(0.6F, 1.7F)
                .setTrackingRange(64)
                .setUpdateInterval(3)
                .fireImmune()
                .setShouldReceiveVelocityUpdates(true);
    }
    private static EntityType.Builder<CursedSkeleton> cursedskeleton()
    {
        return EntityType.Builder.of(CursedSkeleton::new, MobCategory.MONSTER)
        		.sized(0.6F, 1.95F)
                .setTrackingRange(64)
                .setUpdateInterval(3)
                .fireImmune()
                .setShouldReceiveVelocityUpdates(true);
    }
    private static EntityType.Builder<CursedSpider> cursedspider()
    {
        return EntityType.Builder.of(CursedSpider::new, MobCategory.MONSTER)
        		.sized(0.6F, 1.95F)
                .setTrackingRange(64)
                .setUpdateInterval(3)
                .fireImmune()
                .setShouldReceiveVelocityUpdates(true);
    }
    private static EntityType.Builder<CursedDryHead> curseddryhead()
    {
        return EntityType.Builder.of(CursedDryHead::new, MobCategory.MONSTER)
        		.sized(0.6F, 1.8F);
    }
    private static EntityType.Builder<ChickenNaked> chickennaked()
    {
        return EntityType.Builder.of(ChickenNaked::new, MobCategory.CREATURE)
        		.sized(0.4F, 0.7F)
                .setTrackingRange(80)
                .setUpdateInterval(1);
    }
    private static EntityType.Builder<Portal> portal()
    {
        return EntityType.Builder.<Portal>of(Portal::new, MobCategory.MISC)
        		.sized(2F, 3F)
                .setTrackingRange(5)
                .setUpdateInterval(20);
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
            event.put(WUTEntities.CURSEDCREEPER.get(), CursedZombie.createAttributes().build());
            event.put(WUTEntities.CURSEDDRYHEAD.get(), CursedZombie.createAttributes().build());
            event.put(WUTEntities.CURSEDSKELETON.get(), CursedZombie.createAttributes().build());
            event.put(WUTEntities.CURSEDSPIDER.get(), CursedZombie.createAttributes().build());
            event.put(WUTEntities.CURSEDZOMBIE.get(), CursedZombie.createAttributes().build());
            event.put(WUTEntities.CHICKENNAKED.get(), CursedZombie.createAttributes().build());
        }

        @SubscribeEvent
        public static void init(RegisterSpawnPlacementsEvent event)
        {
        }
    }
}
