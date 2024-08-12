package geni.witherutils.base.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SoulFragParticle extends TextureSheetParticle {

	SoulFragParticle(ClientLevel p_107074_, double p_107075_, double p_107076_, double p_107077_)
	{
		super(p_107074_, p_107075_, p_107076_, p_107077_, 0.0D, 0.0D, 0.0D);
		this.gravity = 0.75F;
		this.friction = 0.999F;
		this.xd *= 0.8F;
		this.yd *= 0.8F;
		this.zd *= 0.8F;
		this.yd = this.random.nextFloat() * 0.4F + 0.05F;
		this.quadSize *= this.random.nextFloat() * 0.5F + 0.2F;
		this.lifetime = (int) (16.0D / (Math.random() * 0.8D + 0.2D));
	}

	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}
	@SuppressWarnings("unused")
	public int getLightColor(float p_107086_) {
		int i = super.getLightColor(p_107086_);
		int j = 240;
		int k = i >> 16 & 255;
		return 240 | k << 16;
	}

	public float getQuadSize(float p_107089_) {
		float f = ((float) this.age + p_107089_) / (float) this.lifetime;
		return this.quadSize * (1.0F - f * f);
	}

	public void tick() {
		super.tick();
		if (!this.removed) {
			float f = (float) this.age / (float) this.lifetime;
			if (this.random.nextFloat() > f)
			{
//				if(this.gravity < 0.5f)
//					this.level.addParticle(ParticleTypes.ASH, this.x, this.y, this.z, this.xd, this.yd, this.zd);
//				else
//					this.level.addParticle(ParticleTypes.SOUL, this.x, this.y, this.z, this.xd, this.yd, this.zd);
			}
		}

	}

	@OnlyIn(Dist.CLIENT)
	public static class SoftProvider implements ParticleProvider<SimpleParticleType>
	{
		private final SpriteSet sprite;

		public SoftProvider(SpriteSet p_107092_) {
			this.sprite = p_107092_;
		}

		public Particle createParticle(SimpleParticleType p_107103_, ClientLevel p_107104_, double p_107105_,
				double p_107106_, double p_107107_, double p_107108_, double p_107109_, double p_107110_) {
			SoulFragParticle soulfragparticle = new SoulFragParticle(p_107104_, p_107105_, p_107106_, p_107107_);
			soulfragparticle.gravity = 2.2f;
			soulfragparticle.friction = 0.888F;
			soulfragparticle.lifetime = (int) (16.0D / (Math.random() * 0.8D + 0.2D));
			soulfragparticle.pickSprite(this.sprite);
			return soulfragparticle;
		}
	}
	@OnlyIn(Dist.CLIENT)
	public static class HardProvider implements ParticleProvider<SimpleParticleType>
	{
		private final SpriteSet sprite;

		public HardProvider(SpriteSet p_107092_) {
			this.sprite = p_107092_;
		}

		public Particle createParticle(SimpleParticleType p_107103_, ClientLevel p_107104_, double p_107105_,
				double p_107106_, double p_107107_, double p_107108_, double p_107109_, double p_107110_) {
			SoulFragParticle soulfragparticle = new SoulFragParticle(p_107104_, p_107105_, p_107106_, p_107107_);
			soulfragparticle.gravity = 0.9f;
			soulfragparticle.friction = 0.999F;
			soulfragparticle.lifetime = (int) (32.0D / (Math.random() * 0.8D + 1.2D));
			soulfragparticle.pickSprite(this.sprite);
			return soulfragparticle;
		}
	}
}