package geni.witherutils.base.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SoulOrbParticle extends TextureSheetParticle
{
    SoulOrbParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz)
    {
        super(world, x, y, z);
        this.lifetime = 1;
        this.quadSize = 1.0F;
        this.hasPhysics = false;
    }

    @SuppressWarnings("unused")
    private boolean isFarAwayFromCamera()
    {
    	Minecraft minecraft = Minecraft.getInstance();
    	return minecraft.gameRenderer.getMainCamera().getPosition().distanceToSqr(this.x, this.y, this.z) >= 128.0D;
    }
    @Override
    public ParticleRenderType getRenderType()
    {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
	@Override
	public int getLightColor(float value)
	{
		return 255;
	}

	@OnlyIn(Dist.CLIENT)
	public static class FlareProvider implements ParticleProvider<SimpleParticleType>
	{
		private final SpriteSet spriteSet;
		public FlareProvider(SpriteSet spriteSet)
		{
			this.spriteSet = spriteSet;
		}
		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double vx, double vy, double vz)
		{
			SoulOrbParticle wrinklyparticle = new SoulOrbParticle(level, x, y, z, vx, vy, vz);
			wrinklyparticle.pickSprite(this.spriteSet);
			wrinklyparticle.setSize(1.75F, 1.75F);
			wrinklyparticle.setAlpha(0.75F);
			return wrinklyparticle;
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class NodeProvider implements ParticleProvider<SimpleParticleType>
	{
		private final SpriteSet spriteSet;
		public NodeProvider(SpriteSet spriteSet)
		{
			this.spriteSet = spriteSet;
		}
		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double vx, double vy, double vz)
		{
		    SoulOrbParticle wrinklyparticle = new SoulOrbParticle(level, x, y, z, vx, vy, vz);
			wrinklyparticle.pickSprite(this.spriteSet);
			wrinklyparticle.setAlpha(1.0F);
	        if(!wrinklyparticle.removed && wrinklyparticle.random.nextFloat() < 0.1F)
	        {
	        	wrinklyparticle.level.addParticle(ParticleTypes.SOUL_FIRE_FLAME,
	        			wrinklyparticle.x, wrinklyparticle.y, wrinklyparticle.z,
	        			wrinklyparticle.xd, wrinklyparticle.yd, wrinklyparticle.zd);
	        }
			return wrinklyparticle;
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class SmallNodeProvider implements ParticleProvider<SimpleParticleType>
	{
		private final SpriteSet spriteSet;
		public SmallNodeProvider(SpriteSet spriteSet)
		{
			this.spriteSet = spriteSet;
		}
		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double vx, double vy, double vz)
		{
		    SoulOrbParticle wrinklyparticle = new SoulOrbParticle(level, x, y, z, vx, vy, vz);
			wrinklyparticle.pickSprite(this.spriteSet);
			wrinklyparticle.setAlpha(1.0F);
			wrinklyparticle.quadSize = 0.55F;
			return wrinklyparticle;
		}
	}
}
