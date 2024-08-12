package geni.witherutils.base.common.init;

import geni.witherutils.api.lib.Names;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class WUTParticles
{
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, Names.MODID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ENERGY = PARTICLE_TYPES.register("energy", () -> new SimpleParticleType(true));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ENERGY_CORE = PARTICLE_TYPES.register("energy_core", () -> new SimpleParticleType(true));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLACKSMOKE = PARTICLE_TYPES.register("blacksmoke", () -> new SimpleParticleType(true));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PORTAL = PARTICLE_TYPES.register("portal", () -> new SimpleParticleType(true));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BUBBLE = PARTICLE_TYPES.register("bubble", () -> new SimpleParticleType(true));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SOULFLAKE = PARTICLE_TYPES.register("soulflake", () -> new SimpleParticleType(true));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> EXPERIENCE = PARTICLE_TYPES.register("experience", () -> new SimpleParticleType(true));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> LIQUIDSPRAY = PARTICLE_TYPES.register("liquidspray", () -> new SimpleParticleType(true));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FERTSPRAY = PARTICLE_TYPES.register("fertilizerspray", () -> new SimpleParticleType(true));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SOULORB = PARTICLE_TYPES.register("soulorb", () -> new SimpleParticleType(true));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> WIND = PARTICLE_TYPES.register("wind", () -> new SimpleParticleType(true));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RISINGSOUL = PARTICLE_TYPES.register("risingsoul", () -> new SimpleParticleType(true));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SOULFRAGSOFT = PARTICLE_TYPES.register("soulfragsoft", () -> new SimpleParticleType(true));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SOULFRAGHARD = PARTICLE_TYPES.register("soulfraghard", () -> new SimpleParticleType(true));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MONK_PROJECTILE = PARTICLE_TYPES.register("monk_projectile", () -> new SimpleParticleType(true));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MONK_CLOUD = PARTICLE_TYPES.register("monk_cloud", () -> new SimpleParticleType(true));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MONK_BEAM = PARTICLE_TYPES.register("monk_beam", () -> new SimpleParticleType(true));
}