package geni.witherutils.base.client.particle;

import geni.witherutils.core.common.math.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;

public class ParticlePortal extends TextureSheetParticle {

    public Vector3 target;
    public Vector3 start;
    public float baseScale;

    @SuppressWarnings("resource")
	public ParticlePortal(ClientLevel worldIn, Vector3 pos, Vector3 target)
    {
        super(worldIn, pos.x, pos.y, pos.z);
        this.start = pos;
        this.target = target;
        float speed = 0.12F + (random.nextFloat() * 0.2F);
        this.xd = (target.x - start.x) * speed;
        this.yd = (target.y - start.y) * speed;
        this.zd = (target.z - start.z) * speed;
        this.lifetime = 120;
        this.rCol = this.gCol = this.bCol = 1.0f;
        float baseSize = 0.05F + ((float) Math.sqrt(Minecraft.getInstance().player.distanceToSqr(pos.x, pos.y, pos.z))) * 0.007F;
        this.baseScale = (baseSize + (random.nextFloat() * (baseSize * 2F))) * 0.1F;
        this.quadSize = 0;
        this.hasPhysics = false;
    }

    public static Vector3 interpolateVec3(Vector3 from, Vector3 to, double position) {
        return new Vector3(from.x + (to.x - from.x) * position,
                from.y + (to.y - from.y) * position,
                from.z + (to.z - from.z) * position);
    }

    public static double distanceSq(Vector3 vec1, Vector3 vec2) {
        double dx = vec1.x - vec2.x;
        double dy = vec1.y - vec2.y;
        double dz = vec1.z - vec2.z;
        return dx * dx + dy * dy + dz * dz;
    }

    public static double distance(Vector3 vec1, Vector3 vec2) {
        return Math.sqrt(distanceSq(vec1, vec2));
    }
    
    @Override
    public ParticleRenderType getRenderType()
    {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        double distToTarget = distance(new Vector3(x, y, z), target);
        if (age >= lifetime || distToTarget < 0.15) {
            remove();
            return;
        }

        double startDist = distance(start, target);
        quadSize = ((float) (distToTarget / startDist)) * baseScale;
        age++;
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.move(this.xd, this.yd, this.zd);
    }

    @Override
    protected int getLightColor(float p_189214_1_) {
        return 0xF000F0;
    }
}
