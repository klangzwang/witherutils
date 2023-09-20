package geni.witherutils.base.client.particle;

import java.awt.Color;

import javax.annotation.Nonnull;

import geni.witherutils.core.common.math.Vec3D;
import geni.witherutils.core.common.util.Utils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class XpOrbParticle extends TextureSheetParticle {

    private final SpriteSet sprites;
    public Vec3D targetPos;
    
    public XpOrbParticle(ClientLevel world, double x, double y, double z, double tx, double ty, double tz, int count, int color, float scale, Vec3D targetPos, SpriteSet sprite)
    {
    	super(world, x, y, z, 0.0D, 0.0D, 0.0D);
    	
        this.targetPos = targetPos;
        
        this.sprites = sprite;
        setSprite(sprites.get(world.random));
    	
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

        gravity = 0.01F;
        lifetime = (int) (4.0D / (Math.random() * 0.8D + 0.2D));
        xd = 0.0D;
        yd = 0.0D;
        zd = 0.0D;
        quadSize = ((Mth.sin(count / 2.0F) * 0.5F + 1.5F) * scale);
    }

    @Override
    public void tick()
    {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        
        setSprite(sprites.get(level.random));

        Vec3D dir = Vec3D.getDirectionVec(new Vec3D(x, y, z), targetPos);
        
        double speed = 1.2D;
        
        xd = dir.x;
        yd = dir.y * speed;
        zd = dir.z * speed;
        
        this.move(this.xd, this.yd, this.zd);

        if (age++ > lifetime || Utils.getDistance(x, y, z, targetPos.x, targetPos.y, targetPos.z) < 0.5)
            remove();
    }

    @Nonnull
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
    
    public static class Factory implements ParticleProvider<SimpleParticleType>
    {
        SpriteSet sprites;

        public Factory(SpriteSet sprite)
        {
        	this.sprites = sprite;
		}

        @Override
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
        	XpOrbParticle particle = new XpOrbParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, 20, 16776960, 0.125F, new Vec3D(xSpeed, ySpeed, zSpeed), sprites);
            particle.pickSprite(sprites);
            return particle;
        }
    }
}