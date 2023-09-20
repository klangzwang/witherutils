package geni.witherutils.base.client.particle;

import java.awt.Color;

import geni.witherutils.core.common.math.Vec3D;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class WindParticle extends TextureSheetParticle {

    public Vec3D targetPos;
    private final SpriteSet sprites;

    public WindParticle(ClientLevel world, double xPos, double yPos, double zPos, Vec3D targetPos, int count, int color, float scale, SpriteSet sprite)
    {
        super(world, xPos, yPos, zPos, 0.0D, 0.0D, 0.0D);
    	this.sprites = sprite;
        this.friction = 0.96F;
        xd = 0.0D;
        yd = 0.0D;
        zd = 0.0D;

        Color c = new Color(color);
        float mr = c.getRed() / 255.0F * 0.2F;
        float mg = c.getGreen() / 255.0F * 0.2F;
        float mb = c.getBlue() / 255.0F * 0.2F;
        rCol = (c.getRed() / 255.0F - mr + random.nextFloat() * mr);
        gCol = (c.getGreen() / 255.0F - mg + random.nextFloat() * mg);
        bCol = (c.getBlue() / 255.0F - mb + random.nextFloat() * mb);

        int i = (int)(8.0D / (Math.random() * 0.8D + 0.3D));
        this.lifetime = (int)Math.max((float)i * 2.5F, 1.0F);
        xd = 0.0D;
        yd = 0.0D;
        zd = 0.0D;
        quadSize = ((Mth.sin(count / 2.0F) * 0.1F + 1.0F) * scale);
        
        this.targetPos = targetPos;
        float f1 = 1.0F - (float)(Math.random() * (double)0.3F);
        this.rCol = f1;
        this.gCol = f1;
        this.bCol = f1;
        this.setSpriteFromAge(sprites);
        hasPhysics = false;
    }
    
    @Override
    public float getQuadSize(float p_107504_)
    {
        return this.quadSize * Mth.clamp(((float)this.age + p_107504_) / (float)this.lifetime * 32.0F, 0.0F, 1.0F);
    }
    
	@Override
    public void tick()
	{
        xo = x;
        yo = y;
        zo = z;
        
        Vec3D dir = Vec3D.getDirectionVec(new Vec3D(x, y, z), targetPos);
        double speed = 0.2D;
        xd = dir.x * speed;
        yd = dir.y * speed;
        zd = dir.z * speed;
        this.move(this.xd, this.yd, this.zd);
        yd *= 0.9800000190734863D;
        
        if (this.age++ >= this.lifetime)
            this.remove();
    }
    
    @Override
    public ParticleRenderType getRenderType()
    {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
    
	@Override
	public int getLightColor(float partialTicks)
	{
		return 15728880;
	}
	
    public static class Provider implements ParticleProvider<SimpleParticleType>
    {
		SpriteSet sprites;
        public Provider(SpriteSet sprite)
        {
        	this.sprites = sprite;
		}
		@Override
		public Particle createParticle(SimpleParticleType typeIn, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
		{
			WindParticle particle = new WindParticle(world, x + world.random.nextDouble() - 0.5D * 0.05D, y + 0.125D, z + world.random.nextDouble() - 0.5D * 0.05D, new Vec3D(xSpeed, ySpeed, zSpeed), 20, 16776960, 0.125F, sprites);
			particle.pickSprite(sprites);
			return particle;
		}
    }
}