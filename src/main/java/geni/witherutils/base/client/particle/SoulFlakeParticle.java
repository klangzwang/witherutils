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
public class SoulFlakeParticle extends TextureSheetParticle {
	
	private final double xStart;
	private final double yStart;
	private final double zStart;

	protected SoulFlakeParticle(ClientLevel level, double x, double y, double z, double xs, double xy, double xz)
	{
		super(level, x, y, z);
		this.xd = xs;
		this.yd = xy;
		this.zd = xz;
		this.x = x;
		this.y = y;
		this.z = z;
		this.xStart = this.x;
		this.yStart = this.y;
		this.zStart = this.z;
		this.quadSize = 0.1F * (this.random.nextFloat() * 0.2F + 0.75F);
		float f = this.random.nextFloat() * 0.6F + 0.4F;
		this.rCol = f * 0.9F;
		this.gCol = f * 0.0F;
		this.bCol = f * 0.9F;
		this.lifetime = (int) (Math.random() * 10.0D) + 40;
	}

	@Override
	public ParticleRenderType getRenderType()
	{
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}
	
	@Override
	public void move(double p_107560_, double p_107561_, double p_107562_)
	{
		this.setBoundingBox(this.getBoundingBox().move(p_107560_, p_107561_, p_107562_));
		this.setLocationFromBoundingbox();
	}
	
	@Override
	public float getQuadSize(float p_107567_)
	{
		float f = ((float) this.age + p_107567_) / (float) this.lifetime;
		f = 1.0F - f;
		f *= f;
		f = 1.0F - f;
		return this.quadSize * f;
	}
	
	@Override
	public int getLightColor(float p_107564_)
	{
		int i = super.getLightColor(p_107564_);
		float f = (float) this.age / (float) this.lifetime;
		f *= f;
		f *= f;
		int j = i & 255;
		int k = i >> 16 & 255;
		k += (int) (f * 15.0F * 16.0F);
		if (k > 240) {
			k = 240;
		}

		return j | k << 16;
	}
	
	@Override
	public void tick()
	{
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.age++ >= this.lifetime)
		{
			this.remove();
		}
		else
		{
			float f = (float) this.age / (float) this.lifetime;
			float f1 = -f + f * f * 2.0F;
			float f2 = 1.0F - f1;
			this.x = this.xStart + this.xd * (double) f2;
			this.y = this.yStart + this.yd * (double) f2 + (double) (1.0F - f);
			this.z = this.zStart + this.zd * (double) f2;
			this.setPos(this.x, this.y, this.z);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType>
	{
		private final SpriteSet sprite;

		public Provider(SpriteSet sprite)
		{
			this.sprite = sprite;
		}

		public Particle createParticle(SimpleParticleType p_107581_, ClientLevel p_107582_, double p_107583_, double p_107584_, double p_107585_, double p_107586_, double p_107587_, double p_107588_)
		{
			SoulFlakeParticle portalparticle = new SoulFlakeParticle(p_107582_, p_107583_, p_107584_, p_107585_, p_107586_, p_107587_, p_107588_);
			portalparticle.pickSprite(this.sprite);
			return portalparticle;
		}
	}
}
