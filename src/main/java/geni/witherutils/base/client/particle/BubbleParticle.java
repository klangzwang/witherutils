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

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public class BubbleParticle extends TextureSheetParticle {

	private final double xStart;
	private final double yStart;
	private final double zStart;

	BubbleParticle(ClientLevel p_107551_, double p_107552_, double p_107553_, double p_107554_, double p_107555_, double p_107556_, double p_107557_)
	{
		super(p_107551_, p_107552_, p_107553_, p_107554_);
		this.xd = p_107555_;
		this.yd = p_107556_;
		this.zd = p_107557_;
		this.x = p_107552_;
		this.y = p_107553_;
		this.z = p_107554_;
		this.xStart = this.x;
		this.yStart = this.y;
		this.zStart = this.z;
		this.quadSize = 0.8F * (this.random.nextFloat() * 0.2F + 0.5F);
		float f = this.random.nextFloat() * 0.6F + 0.4F;
		this.rCol = 0.184F;
		this.gCol = 0.941F;
		this.bCol = 0.0F;
		this.lifetime = (int)(Math.random() * 10.0D) + 40;
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

		if(this.age++ >= this.lifetime)
		{
			this.remove();
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType>
	{
		private final SpriteSet sprite;

		public Provider(SpriteSet p_107092_)
		{
			this.sprite = p_107092_;
		}

		public Particle createParticle(SimpleParticleType p_107103_, ClientLevel p_107582_, double p_107583_, double p_107584_, double p_107585_, double p_107586_, double p_107587_, double p_107588_)
		{
			BubbleParticle lavaparticle = new BubbleParticle(p_107582_, p_107583_, p_107584_, p_107585_, p_107586_, p_107587_, p_107588_);
			lavaparticle.pickSprite(this.sprite);
			return lavaparticle;
		}
	}
}
