package geni.witherutils.base.client.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import geni.witherutils.core.common.math.Vec3D;
import geni.witherutils.core.common.util.McTimerUtil;
import geni.witherutils.core.common.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class EnergyCoreParticle extends TextureSheetParticle {

    public static final ParticleRenderType PARTICLE_NO_DEPTH_NO_LIGHT = new ParticleRenderType()
    {
		@SuppressWarnings("deprecation")
		@Override
		public BufferBuilder begin(Tesselator pTesselator, TextureManager pTextureManager)
		{
			pTesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
            RenderSystem.depthMask(false);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			return null;
		}
        public String toString()
        {
            return "PARTICLE_NO_DEPTH_NO_LIGHT";
        }

    };

    public Vec3D targetPos;
    public boolean toCore = false;
    public int startRotation = 0;
    private final Direction.Axis direction;
    public boolean isLargeStabilizer = false;
    private final SpriteSet spriteSet;

    public EnergyCoreParticle(ClientLevel world, double xPos, double yPos, double zPos, Vec3D targetPos, SpriteSet spriteSet)
    {
        super(world, xPos, yPos, zPos);
        this.targetPos = targetPos;
        this.spriteSet = spriteSet;
        setSprite(spriteSet.get(world.random));
        hasPhysics = false;
        Vec3D dir = Vec3D.getDirectionVec(new Vec3D(xPos, yPos, zPos), targetPos);
        this.direction = Direction.getNearest((float) dir.x, (float) dir.y, (float) dir.z).getAxis();
        lifetime = 40;
    }

    @Override
    public ParticleRenderType getRenderType()
    {
        return PARTICLE_NO_DEPTH_NO_LIGHT;
    }

    @Override
    protected int getLightColor(float p_107249_)
    {
        return 255;
    }

    @Override
    public void tick()
    {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        Vec3D tPos = this.targetPos.copy();
        setSprite(spriteSet.get(level.random));

        if (toCore) {
            double rotation = McTimerUtil.getFps(Minecraft.getInstance());
            double offsetX = Math.sin((rotation / 180D * Math.PI) + (startRotation / 100D));
            double offsetY = Math.cos((rotation / 180D * Math.PI) + (startRotation / 100D));

            double d = isLargeStabilizer ? 1.8 : 0.2;
            if (direction == Direction.Axis.Z) {
                tPos.add(offsetX * d, offsetY * d, 0);
            }
            else if (direction == Direction.Axis.Y) {
                tPos.add(offsetX * d, 0, offsetY * d);
            }
            else if (direction == Direction.Axis.X) {
                tPos.add(0, offsetY * d, offsetX * d);
            }
        }

        Vec3D dir = Vec3D.getDirectionVec(new Vec3D(x, y, z), tPos);
        double speed = (toCore ? 0.5D : 0.25D);
        xd = dir.x * speed;
        yd = dir.y * speed;
        zd = dir.z * speed;
        move(xd, yd, zd);

        if (age++ > lifetime || Utils.getDistanceSq(x, y, z, tPos.x, tPos.y, tPos.z) < 0.01) {
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
			EnergyCoreParticle particleEnergy = new EnergyCoreParticle(level, x, y, z, new Vec3D(xSpeed, ySpeed, zSpeed), spriteSet);
			particleEnergy.pickSprite(this.spriteSet);
			return particleEnergy;
		}
	}
}
