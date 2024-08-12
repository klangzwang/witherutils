package geni.witherutils.base.client.particle;

import geni.witherutils.core.common.math.Vec3D;
import geni.witherutils.core.common.util.Utils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class EnergyParticle extends TextureSheetParticle {

    public Vec3D targetPos;
    private final SpriteSet spriteSet;

    public EnergyParticle(ClientLevel world, double xPos, double yPos, double zPos, Vec3D targetPos, SpriteSet spriteSet)
    {
        super(world, xPos, yPos, zPos);
        this.targetPos = targetPos;
        this.spriteSet = spriteSet;
        setSprite(spriteSet.get(world.random));
        hasPhysics = false;
    }
    
    @Override
    public ParticleRenderType getRenderType()
    {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
    
    @Override
    public void tick()
    {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        
        setSprite(spriteSet.get(level.random));

        Vec3D dir = Vec3D.getDirectionVec(new Vec3D(x, y, z), targetPos);
        
        double speed = 0.5D;
        
        xd = dir.x * speed;
        yd = dir.y * speed;
        zd = dir.z * speed;
        
        this.move(this.xd, this.yd, this.zd);

        if (age++ > lifetime || Utils.getDistance(x, y, z, targetPos.x, targetPos.y, targetPos.z) < 0.5)
        {
            remove();
        }
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
			EnergyParticle particleEnergy = new EnergyParticle(level, x, y, z, new Vec3D(xSpeed, ySpeed, zSpeed), spriteSet);
			particleEnergy.pickSprite(this.spriteSet);
			return particleEnergy;
		}
	}
}
