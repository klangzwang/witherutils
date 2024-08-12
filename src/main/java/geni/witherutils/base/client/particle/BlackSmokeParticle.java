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

public class BlackSmokeParticle extends TextureSheetParticle {

    @SuppressWarnings("unused")
	private final SpriteSet spriteSet;
    
    public BlackSmokeParticle(ClientLevel world, double xPos, double yPos, double zPos, double xMotion, double yMotion, double zMotion, SpriteSet spriteSet)
    {
        super(world, xPos, yPos, zPos);
        
        this.scale(3.0F);
        this.setSize(0.25F, 0.25F);
        
        this.lifetime = this.random.nextInt(100);
        
        this.spriteSet = spriteSet;
        setSprite(spriteSet.get(world.random));
        
//        this.gravity = 3.0E-6F;
        this.xd = xMotion;
        this.yd = yMotion + 0.0125;
        this.zd = zMotion;
    }

    @Override
    public void tick()
    {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ < this.lifetime && !(this.alpha <= 0.0F))
        {
           this.xd += this.random.nextFloat() / 5000.0F * (float)(this.random.nextBoolean() ? 1 : -1);
           this.zd += this.random.nextFloat() / 5000.0F * (float)(this.random.nextBoolean() ? 1 : -1);
           this.yd -= this.gravity;
           this.move(this.xd, this.yd, this.zd);
           if (this.age >= this.lifetime - 60 && this.alpha > 0.01F)
           {
              this.alpha -= 0.020F;
           }

        }
        else
        {
           this.remove();
        }
    }
    
	@Override
	public ParticleRenderType getRenderType()
	{
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	@OnlyIn(Dist.CLIENT)
	public static class Factory implements ParticleProvider<SimpleParticleType>
	{
		private final SpriteSet spriteSet;
		public Factory(SpriteSet spriteSet)
		{
			this.spriteSet = spriteSet;
		}
		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
		{
			BlackSmokeParticle particleEnergy = new BlackSmokeParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
			particleEnergy.pickSprite(this.spriteSet);
			return particleEnergy;
		}
	}
}
