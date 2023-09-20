package geni.witherutils.base.common.init;

import geni.witherutils.WitherUtils;
import geni.witherutils.core.common.particle.IntParticleType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class WUTParticles
{
	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, WitherUtils.MODID);
	
	public static final RegistryObject<IntParticleType> ENERGY = PARTICLE_TYPES.register("energy", () -> new IntParticleType(false));
	public static final RegistryObject<IntParticleType> ENERGY_CORE = PARTICLE_TYPES.register("energy_core", () -> new IntParticleType(false));
	
	public static final RegistryObject<IntParticleType> BLACKSMOKE = PARTICLE_TYPES.register("blacksmoke", () -> new IntParticleType(false));
	
	public static final RegistryObject<SimpleParticleType> PORTAL = PARTICLE_TYPES.register("portal", () -> new SimpleParticleType(true));
	public static final RegistryObject<SimpleParticleType> BUBBLE = PARTICLE_TYPES.register("bubble", () -> new SimpleParticleType(true));
	public static final RegistryObject<SimpleParticleType> SOULFLAKE = PARTICLE_TYPES.register("soulflake", () -> new SimpleParticleType(true));
	public static final RegistryObject<SimpleParticleType> EXPERIENCE = PARTICLE_TYPES.register("experience", () -> new SimpleParticleType(true));
	public static final RegistryObject<SimpleParticleType> LIQUIDSPRAY = PARTICLE_TYPES.register("liquidspray", () -> new SimpleParticleType(true));
	public static final RegistryObject<SimpleParticleType> FERTSPRAY = PARTICLE_TYPES.register("fertilizerspray", () -> new SimpleParticleType(true));
	public static final RegistryObject<SimpleParticleType> SOULORB = PARTICLE_TYPES.register("soulorb", () -> new SimpleParticleType(true));
	public static final RegistryObject<SimpleParticleType> WIND = PARTICLE_TYPES.register("wind", () -> new SimpleParticleType(true));
	public static final RegistryObject<SimpleParticleType> RISINGSOUL = PARTICLE_TYPES.register("risingsoul", () -> new SimpleParticleType(true));
	public static final RegistryObject<SimpleParticleType> SOULFRAGSOFT = PARTICLE_TYPES.register("soulfragsoft", () -> new SimpleParticleType(true));
	public static final RegistryObject<SimpleParticleType> SOULFRAGHARD = PARTICLE_TYPES.register("soulfraghard", () -> new SimpleParticleType(true));
}