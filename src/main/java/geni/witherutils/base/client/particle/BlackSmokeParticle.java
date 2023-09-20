package geni.witherutils.base.client.particle;

import geni.witherutils.core.common.particle.IntParticleType;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;

public class BlackSmokeParticle extends TextureSheetParticle {

    @SuppressWarnings("unused")
	private final SpriteSet spriteSet;
    
    public BlackSmokeParticle(ClientLevel world, double xPos, double yPos, double zPos, double xMotion, double yMotion, double zMotion, SpriteSet spriteSet, int progress)
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
           this.xd += (double)(this.random.nextFloat() / 5000.0F * (float)(this.random.nextBoolean() ? 1 : -1));
           this.zd += (double)(this.random.nextFloat() / 5000.0F * (float)(this.random.nextBoolean() ? 1 : -1));
           this.yd -= (double)this.gravity;
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
	
    public static class Factory implements ParticleProvider<IntParticleType.IntParticleData>
    {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet)
        {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(IntParticleType.IntParticleData data, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            BlackSmokeParticle particleEnergy = new BlackSmokeParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet, 0);
            if (data.get().length >= 3)
            {
                particleEnergy.setColor(data.get()[0] / 255F, data.get()[1] / 255F, data.get()[2] / 255F);
            }
            if (data.get().length >= 4)
            {
                particleEnergy.scale(data.get()[3] / 100F);
            }
            return particleEnergy;
        }
    }
}
