package geni.witherutils.base.client.particle;

import geni.witherutils.core.common.math.Vec3D;
import geni.witherutils.core.common.particle.IntParticleType;
import geni.witherutils.core.common.util.Utils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;

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
            EnergyParticle particleEnergy = new EnergyParticle(world, x, y, z, new Vec3D(xSpeed, ySpeed, zSpeed), spriteSet);
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
