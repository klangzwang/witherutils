package geni.witherutils.base.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LiquidSprayParticle extends TextureSheetParticle
{
	@SuppressWarnings("unused")
	private final SpriteSet sprite;
	
	protected LiquidSprayParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet)
	{
        super(world, x, y, z);
		this.sprite = spriteSet;
		this.setSize((float) 0.1, (float) 0.1);
		this.quadSize *= (float) Math.max(0.9f, 0.4f + (this.random.nextFloat() - 0.5f));
		this.lifetime = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
		this.gravity = (float) 0.09F;
		this.hasPhysics = false;
		this.xd = vx * (double)0.3F;
		this.yd = vy * Math.random() * (double)0.2F + (double)0.1F;
		this.zd = vz * (double)0.3F;
		this.setAlpha(100);
	}

	@SuppressWarnings("unused")
	@Override
	public int getLightColor(float value)
	{
		int i = super.getLightColor(value);
		int j = 240;
		int k = i >> 16 & 255;
		return 240 | k << 16;
	}
	@Override
	public ParticleRenderType getRenderType()
	{
		if (((Math.random() > 0) && (Math.random() < 0.9)))
		{
			return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
		}
		return ParticleRenderType.PARTICLE_SHEET_LIT;
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
		else
		{
			this.yd -= (double)this.gravity;
			this.move(this.xd, this.yd, this.zd);
			this.xd *= (double)0.98F;
			this.yd *= (double)0.98F;
			this.zd *= (double)0.98F;
			
			if (this.onGround)
			{
				if (Math.random() < 0.5D)
				{
					this.remove();
				}
				this.xd *= (double)0.7F;
				this.zd *= (double)0.7F;
			}

			BlockPos blockpos = new BlockPos((int) this.x, (int) this.y, (int) this.z);
			
			double d0 = Math.max(this.level.getBlockState(blockpos).getCollisionShape(this.level, blockpos).max(Direction.Axis.Y, this.x - (double)blockpos.getX(), this.z - (double)blockpos.getZ()), (double)this.level.getFluidState(blockpos).getHeight(this.level, blockpos));
			
			if (d0 > 0.0D && this.y < (double)blockpos.getY() + d0)
			{
				this.remove();
			}
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	public static class WaterSplashProvider implements ParticleProvider<SimpleParticleType>
	{
		private final SpriteSet spriteSet;
		public WaterSplashProvider(SpriteSet spriteSet)
		{
			this.spriteSet = spriteSet;
		}
		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double vx, double vy, double vz)
		{
			LiquidSprayParticle wrinklyparticle = new LiquidSprayParticle(level, x, y, z, vx, vy, vz, spriteSet);
			wrinklyparticle.pickSprite(this.spriteSet);
			return wrinklyparticle;
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	public static class FertilizerSplashProvider implements ParticleProvider<SimpleParticleType>
	{
		private final SpriteSet spriteSet;
		public FertilizerSplashProvider(SpriteSet spriteSet)
		{
			this.spriteSet = spriteSet;
		}
		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double vx, double vy, double vz)
		{
			LiquidSprayParticle wrinklyparticle = new LiquidSprayParticle(level, x, y, z, vx, vy, vz, spriteSet);
			wrinklyparticle.pickSprite(this.spriteSet);
			return wrinklyparticle;
		}
	}
}