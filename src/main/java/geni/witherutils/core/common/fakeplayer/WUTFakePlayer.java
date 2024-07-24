package geni.witherutils.core.common.fakeplayer;

import java.lang.ref.WeakReference;
import java.util.OptionalInt;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.authlib.GameProfile;

import geni.witherutils.core.common.util.FakeNetHandler;
import geni.witherutils.core.common.util.UserIdent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;

public class WUTFakePlayer extends FakePlayer {

	public static final GameProfile GAME_PROFILE = new GameProfile(UUID.nameUUIDFromBytes("fakeplayer.wither".getBytes()), Component.translatable("fakeplayer.wither").getString());

	private final @Nonnull ServerLevel origWorld;
    private static WUTFakePlayer INSTANCE;
    protected Vec3 pos = new Vec3(0, 0, 0);
    protected BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos(0, 0, 0);
    
	public WUTFakePlayer(ServerLevel level, GameProfile profile)
	{
		super(level, profile);
	    origWorld = super.serverLevel();
	}
	
    public static WeakReference<WUTFakePlayer> get(ServerLevel world)
    {
        if (INSTANCE == null)
        {
            INSTANCE = new WUTFakePlayer(world, GAME_PROFILE);
        }
        INSTANCE.setLevel(world);
        return new WeakReference<>(INSTANCE);
    }

    public static WeakReference<WUTFakePlayer> get(ServerLevel world, double x, double y, double z)
    {
        if (INSTANCE == null)
        {
            INSTANCE = new WUTFakePlayer(world, GAME_PROFILE);
            INSTANCE.connection = new FakeNetHandler(world.getServer(), null, INSTANCE, null);
        }
        INSTANCE.setLevel(world);
        INSTANCE.setPos(x,y,z);
        INSTANCE.setPosition(x,y,z);
        return new WeakReference<>(INSTANCE);
    }

    public void setPosition(double x, double y, double z)
    {
        if (pos.x != x || pos.y != y || pos.z != z)
        {
            pos = new Vec3(x, y, z);
            blockPos.set(Math.floor(x), Math.floor(y), Math.floor(z));
        }
    }

    @Override
    public OptionalInt openMenu(MenuProvider p_9033_)
    {
        return OptionalInt.empty();
    }

    @Override
    public float getAttackStrengthScale(float adjustTicks)
    {
        return 1;
    }
    
    @Override
    public boolean canBeAffected(MobEffectInstance potioneffectIn)
    {
        return false;
    }
    public static void unload(LevelAccessor world)
    {
        if (INSTANCE != null && INSTANCE.level() == world)
            INSTANCE = null;
    }
    
    @Override
    public Vec3 position()
    {
        return this.pos;
    }
    @Override
    public BlockPos blockPosition()
    {
        return this.blockPos;
    }
    
	private @Nonnull UserIdent owner = UserIdent.NOBODY;
	
	public UUID getOwner()
	{
		return owner == UserIdent.NOBODY ? null : owner.getUUID();
	}

	public @Nonnull WUTFakePlayer setOwner(@Nullable UserIdent owner)
	{
		this.owner = owner == null ? UserIdent.NOBODY : owner;
		return this;
	}

	public void clearOwner()
	{
		this.owner = UserIdent.NOBODY;
	}

	@Override
	public ServerLevel serverLevel()
	{
		return origWorld;
	}
	
	@Override
	public void onItemPickup(@Nonnull ItemEntity entityIn)
	{
		if (level() instanceof ServerLevel)
		{
			super.onItemPickup(entityIn);
		}
	}
	
//    public static void renderPlacementEmblem(float partialTicks, PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, ClientLevel level, int light, int overlayLight)
//    {
//    	if(emblemTimer-- > 0)
//    	{
//    		poseStack.pushPose();
//    		
//    		float time = level.getLevelData().getGameTime() + partialTicks;
//    		double offset = Math.sin(time * 1 / 8.0D) / 10.0D;
//    		BakedModel model = mc.getItemRenderer().getModel(new ItemStack(WUTItems.TABWU.get()), level, null, 0);
//
//    		poseStack.translate(0.5D, 1.25D + offset, 0.5D);
//    		poseStack.mulPose(Axis.YP.rotationDegrees(time * 4.0F * 2.0f));
//    		mc.getItemRenderer().render(new ItemStack(WUTItems.TABWU.get()), ItemDisplayContext.GROUND, false, poseStack, buffer, light, overlayLight, model);
//    		
//    		poseStack.popPose();
//    	}
//    }
}
