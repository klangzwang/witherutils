package geni.witherutils.base.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WitherBaseParticle extends TextureSheetParticle {

	private final SpriteSet sprites;

	protected WitherBaseParticle(final ClientLevel level, final double x, final double y, final double z, final float p_171908_, final float p_171909_, final float p_171910_, final double p_171911_, final double p_171912_, final double p_171913_, final float p_171914_, final SpriteSet p_171915_, final float p_171916_, final int p_171917_, final float p_171918_, final boolean p_171919_)
	{
		super(level, x, y, z, 0.0D, 0.0D, 0.0D);
		this.friction = 0.96F;
		this.gravity = p_171918_;
		this.speedUpWhenYMotionIsBlocked = true;
		this.sprites = p_171915_;
		this.xd *= (double) p_171908_;
		this.yd *= (double) p_171909_;
		this.zd *= (double) p_171910_;
		this.xd += p_171911_;
		this.yd += p_171912_;
		this.zd += p_171913_;
		float f = level.random.nextFloat() * p_171916_;
		this.rCol = f;
		this.gCol = f;
		this.bCol = f;
		this.quadSize *= 0.75F * p_171914_;
		this.lifetime = (int) ((double) p_171917_ / ((double) level.random.nextFloat() * 0.8D + 0.2D));
		this.lifetime = (int) ((float) this.lifetime * p_171914_);
		this.lifetime = Math.max(this.lifetime, 1);
		this.setSpriteFromAge(p_171915_);
		this.hasPhysics = p_171919_;
	}

	public ParticleRenderType getRenderType()
	{
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	public float getQuadSize(float p_105642_)
	{
		return this.quadSize * Mth.clamp(((float) this.age + p_105642_) / (float) this.lifetime * 32.0F, 0.0F, 1.0F);
	}

	public void tick()
	{
		super.tick();
		this.setSpriteFromAge(this.sprites);
	}
}