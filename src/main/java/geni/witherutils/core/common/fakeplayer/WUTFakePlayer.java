package geni.witherutils.core.common.fakeplayer;

import com.mojang.authlib.GameProfile;

import geni.witherutils.WitherUtils;

import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import javax.annotation.Nullable;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.common.UsernameCache;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import org.jetbrains.annotations.NotNull;

public class WUTFakePlayer extends FakePlayer {

    private static WeakReference<WUTFakePlayer> INSTANCE;

    private UUID emulatingUUID = null;

    private WUTFakePlayer(ServerLevel world) {
        super(world, new FakeGameProfile());
        ((FakeGameProfile) this.getGameProfile()).myFakePlayer = this;
    }

    @Override
    public boolean canBeAffected(@NotNull MobEffectInstance effect) {
        return false;
    }

    public void setEmulatingUUID(UUID uuid) {
        this.emulatingUUID = uuid;
    }

    @NotNull
    @Override
    public UUID getUUID() {
        return this.emulatingUUID == null ? super.getUUID() : this.emulatingUUID;
    }

    public void cleanupFakePlayer(ServerLevel world) {
        emulatingUUID = null;
        //don't keep reference to the World, note we set it to the overworld to avoid any potential null pointers
        setServerLevel(world.getServer().overworld());
    }

     public static WUTFakePlayer setupFakePlayer(ServerLevel world) {
        WUTFakePlayer actual = INSTANCE == null ? null : INSTANCE.get();
        if (actual == null) {
            actual = new WUTFakePlayer(world);
            INSTANCE = new WeakReference<>(actual);
        }
        WUTFakePlayer player = actual;
        player.setServerLevel(world);
        return player;
    }

    public static WUTFakePlayer setupFakePlayer(ServerLevel world, double x, double y, double z) {
        WUTFakePlayer player = setupFakePlayer(world);
        player.setPosRaw(x, y, z);
        return player;
    }

    public static <R> R withFakePlayer(ServerLevel world, Function<WUTFakePlayer, R> fakePlayerConsumer) {
        WUTFakePlayer player = setupFakePlayer(world);
        R result = fakePlayerConsumer.apply(player);
        player.cleanupFakePlayer(world);
        return result;
    }

    public static <R> R withFakePlayer(ServerLevel world, double x, double y, double z, Function<WUTFakePlayer, R> fakePlayerConsumer) {
        WUTFakePlayer player = setupFakePlayer(world);
        player.setPosRaw(x, y, z);
        R result = fakePlayerConsumer.apply(player);
        player.cleanupFakePlayer(world);
        return result;
    }

    public static void releaseInstance(ServerLevel world)
    {
        WUTFakePlayer actual = INSTANCE == null ? null : INSTANCE.get();
        if (actual != null && actual.serverLevel() == world) {
            actual.setServerLevel(world.getServer().overworld());
        }
    }

    private static class FakeGameProfile extends GameProfile {

        private WUTFakePlayer myFakePlayer = null;

        public FakeGameProfile() {
            super(WitherUtils.gameProfile.getId(), WitherUtils.gameProfile.getName());
        }

        private UUID getEmulatingUUID() {
            return myFakePlayer == null ? null : myFakePlayer.emulatingUUID;
        }

        @Override
        public UUID getId() {
            UUID emulatingUUID = getEmulatingUUID();
            return emulatingUUID == null ? super.getId() : emulatingUUID;
        }

        @Override
        public String getName() {
            UUID emulatingUUID = getEmulatingUUID();
            return emulatingUUID == null ? super.getName() : getLastKnownUsername(emulatingUUID);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof GameProfile that)) {
                return false;
            }
            return Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName());
        }

        @Override
        public int hashCode() {
            UUID id = getId();
            String name = getName();
            int result = id == null ? 0 : id.hashCode();
            result = 31 * result + (name == null ? 0 : name.hashCode());
            return result;
        }
    }
    
    @NotNull
    public static String getLastKnownUsername(@Nullable UUID uuid)
    {
        if (uuid == null)
        {
            return "<???>";
        }
        
        String ret = UsernameCache.getLastKnownUsername(uuid);
        if (ret == null && EffectiveSide.get().isServer())
        {
            Optional<GameProfile> gp = ServerLifecycleHooks.getCurrentServer().getProfileCache().get(uuid);
            if (gp.isPresent())
            {
                ret = gp.get().getName();
            }
        }
        
//        if (ret == null && !warnedFails.contains(uuid))
//        {
//            WitherUtils.LOGGER.warn("Failed to retrieve username for UUID {}, you might want to add it to the JSON cache", uuid);
//            warnedFails.add(uuid);
//        }
        return ret == null ? "<" + uuid + ">" : ret;
    }
    
    /*
     * 
     * OLD PLAYER
     * 
     */
//    public static WeakReference<WUTFakePlayer> get(ServerLevel world)
//    {
//        if (INSTANCE == null)
//        {
//            INSTANCE = new WUTFakePlayer(world, GAME_PROFILE);
//        }
//        INSTANCE.setLevel(world);
//        return new WeakReference<>(INSTANCE);
//    }
//
//    public static WeakReference<WUTFakePlayer> get(ServerLevel world, double x, double y, double z)
//    {
//        if (INSTANCE == null)
//        {
//            INSTANCE = new WUTFakePlayer(world, GAME_PROFILE);
//            INSTANCE.connection = new FakeNetHandler(world.getServer(), INSTANCE);
//        }
//        INSTANCE.setLevel(world);
//        INSTANCE.setPos(x,y,z);
//        INSTANCE.setPosition(x,y,z);
//        return new WeakReference<>(INSTANCE);
//    }
//
//    public void setPosition(double x, double y, double z)
//    {
//        if (pos.x != x || pos.y != y || pos.z != z)
//        {
//            pos = new Vec3(x, y, z);
//            blockPos.set(Math.floor(x), Math.floor(y), Math.floor(z));
//        }
//    }
//    @Override
//    public float getEyeHeight(Pose pose)
//    {
//        return 0;
//    }
//
//    @Override
//    public void initMenu(AbstractContainerMenu p_143400_) {}
//
//    @Override
//    public OptionalInt openMenu(MenuProvider p_9033_)
//    {
//        return OptionalInt.empty();
//    }
//
//    @Override
//    public float getAttackStrengthScale(float adjustTicks)
//    {
//        return 1;
//    }
//    
//    @Override
//    public boolean canBeAffected(MobEffectInstance potioneffectIn)
//    {
//        return false;
//    }
//    public static void unload(LevelAccessor world)
//    {
//        if (INSTANCE != null && INSTANCE.level() == world)
//            INSTANCE = null;
//    }
//    
//    @Override
//    public Vec3 position()
//    {
//        return this.pos;
//    }
//    @Override
//    public BlockPos blockPosition()
//    {
//        return this.blockPos;
//    }
//    
//	private @Nonnull UserIdent owner = UserIdent.NOBODY;
//	
//	public UUID getOwner()
//	{
//		return owner == UserIdent.NOBODY ? null : owner.getUUID();
//	}
//
//	public @Nonnull WUTFakePlayer setOwner(@Nullable UserIdent owner)
//	{
//		this.owner = owner == null ? UserIdent.NOBODY : owner;
//		return this;
//	}
//
//	public void clearOwner()
//	{
//		this.owner = UserIdent.NOBODY;
//	}
//
//	@Override
//	public ServerLevel serverLevel()
//	{
//		return origWorld;
//	}
//	
//	@Override
//	public void onItemPickup(@Nonnull ItemEntity entityIn)
//	{
//		if (level() instanceof ServerLevel)
//		{
//			super.onItemPickup(entityIn);
//		}
//	}
}