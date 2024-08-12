package geni.witherutils.core.common.util;

import java.util.LinkedList;

import geni.witherutils.core.common.math.Vector3;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;

public class TeleportUtil {

	public static boolean teleportEntityTo(Entity entity, BlockPos pos)
	{
		return teleportEntityTo(entity, pos.getX(), pos.getY(), pos.getZ());
	}

	public static boolean teleportEntityTo(Entity entity, double x, double y, double z)
	{

		if (entity instanceof LivingEntity)
		{
			return teleportEntityTo((LivingEntity) entity, x, y, z);
		}
		else
		{
			entity.moveTo(x, y, z, entity.xRotO, entity.yRotO);
			entity.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
		}
		return true;
	}

	public static boolean teleportEntityTo(LivingEntity entity, double x, double y, double z)
	{
		EntityTeleportEvent event = new EntityTeleportEvent(entity, x, y, z);
		if (NeoForge.EVENT_BUS.post(event) == null)
		{
			return false;
		}

		entity.moveTo(event.getTargetX(), event.getTargetY(), event.getTargetZ());
		entity.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);

		return true;
	}
	
    public static Entity teleportEntity(Entity entity, Entity destination)
    {
        return teleportEntity(entity, destination.level().dimension(), destination.getX(), destination.getY(), destination.zOld, destination.getYRot(), destination.getXRot());
    }

    @SuppressWarnings("resource")
	public static Entity teleportEntity(Entity entity, ResourceKey<Level> dimension, double xCoord, double yCoord, double zCoord, float yaw, float pitch)
    {
        if (entity == null || entity.level().isClientSide)
        {
            return entity;
        }

        MinecraftServer server = entity.getServer();
        ResourceKey<Level> sourceDim = entity.level().dimension();

        if (!entity.isVehicle() && !entity.isPassenger()) {
            return handleEntityTeleport(entity, server, sourceDim, dimension, xCoord, yCoord, zCoord, yaw, pitch);
        }

        Entity rootEntity = entity.getRootVehicle();
        PassengerHelper passengerHelper = new PassengerHelper(rootEntity);
        PassengerHelper rider = passengerHelper.getPassenger(entity);
        if (rider == null)
        {
            return entity;
        }
        passengerHelper.teleport(server, sourceDim, dimension, xCoord, yCoord, zCoord, yaw, pitch);
        passengerHelper.updateClients();

        return rider.entity;
    }

    public static Entity teleportEntity(Entity entity, ResourceKey<Level> dimension, Vector3 pos, float yaw, float pitch)
    {
        return teleportEntity(entity, dimension, pos.x, pos.y, pos.z, yaw, pitch);
    }

    public static Entity teleportEntity(Entity entity, ResourceKey<Level> dimension, double xCoord, double yCoord, double zCoord)
    {
        return teleportEntity(entity, dimension, xCoord, yCoord, zCoord, entity.getYRot(), entity.getXRot());
    }

    public static Entity teleportEntity(Entity entity, ResourceKey<Level> dimension, Vector3 pos)
    {
        return teleportEntity(entity, dimension, pos.x, pos.y, pos.z, entity.getYRot(), entity.getXRot());
    }

    @SuppressWarnings("resource")
	private static Entity handleEntityTeleport(Entity entity, MinecraftServer server, ResourceKey<Level> sourceDim, ResourceKey<Level> targetDim, double xCoord, double yCoord, double zCoord, float yaw, float pitch)
    {
        if (entity == null || entity.level().isClientSide || targetDim == null) {
            return entity;
        }

        boolean interDimensional = sourceDim != targetDim;

        if (interDimensional && !CommonHooks.onTravelToDimension(entity, sourceDim)) {
            return entity;
        }

        if (interDimensional) {
            if (entity instanceof ServerPlayer) {
                return teleportPlayerInterdimentional((ServerPlayer) entity, server, targetDim, xCoord, yCoord, zCoord, yaw, pitch);
            } else {
                return teleportEntityInterdimentional(entity, server, targetDim, xCoord, yCoord, zCoord, yaw, pitch);
            }
        } else {
            if (entity instanceof ServerPlayer player) {
                player.connection.teleport(xCoord, yCoord, zCoord, yaw, pitch);
                player.setYHeadRot(yaw);
            } else {
                entity.moveTo(xCoord, yCoord, zCoord, yaw, pitch);
                entity.setYHeadRot(yaw);
            }
        }

        return entity;
    }

    private static Entity teleportEntityInterdimentional(Entity entity, MinecraftServer server, ResourceKey<Level> targetDim, double xCoord, double yCoord, double zCoord, float yaw, float pitch) {
        ServerLevel targetWorld = server.getLevel(targetDim);
        if (!entity.isAlive() || targetWorld == null) {
            return null;
        }

//        Entity movedEntity = entity.refreshDimensions();
//        if (movedEntity != null) {
//            movedEntity.moveTo(xCoord, yCoord, zCoord, yaw, pitch);
//            return movedEntity;
//        }

        entity.unRide();
        Entity movedEntity = entity.getType().create(targetWorld);
        if (movedEntity != null) {
            movedEntity.restoreFrom(entity);
            movedEntity.moveTo(xCoord, yCoord, zCoord, yaw, pitch);
            targetWorld.addDuringTeleport(movedEntity);
            entity.remove(Entity.RemovalReason.CHANGED_DIMENSION);
            ((ServerLevel) entity.level()).resetEmptyTime();
            targetWorld.resetEmptyTime();
            return movedEntity;
        }


        return entity;
    }

    private static Player teleportPlayerInterdimentional(ServerPlayer player, MinecraftServer server, ResourceKey<Level> targetDim, double xCoord, double yCoord, double zCoord, float yaw, float pitch)
    {
//        ServerLevel originWorld = player.serverLevel();
        ServerLevel targetWorld = server.getLevel(targetDim);
        if (!player.isAlive() || targetWorld == null) {
            return player;
        }
        player.isChangingDimension();
        player.teleportTo(targetWorld, xCoord, yCoord, zCoord, yaw, pitch);

//        player.lastSentExp = -1;
//        player.lastSentHealth = -1.0F;
//        player.lastSentFood = -1;

        player.onUpdateAbilities();
        return player;
    }

    public static Entity getHighestRidingEntity(Entity mount) {
        Entity entity;

        for (entity = mount; entity.getPassengers().size() > 0; entity = entity.getPassengers().get(0)) ;

        return entity;
    }

    private static class PassengerHelper {
        public Entity entity;
        public LinkedList<PassengerHelper> passengers = new LinkedList<>();
        @SuppressWarnings("unused")
		public double offsetX, offsetY, offsetZ;

        /**
         * Creates a new passenger helper for the given entity and recursively adds all of the entities passengers.
         *
         * @param entity The root entity. If you have multiple stacked entities this would be the one at the bottom of the stack.
         */
        public PassengerHelper(Entity entity) {
            this.entity = entity;
            if (entity.isPassenger()) {
                offsetX = entity.getX() - entity.getVehicle().getX();
                offsetY = entity.getY() - entity.getVehicle().getY();
                offsetZ = entity.getZ() - entity.getVehicle().getZ();
            }
            for (Entity passenger : entity.getPassengers()) {
                passengers.add(new PassengerHelper(passenger));
            }
        }

        /**
         * Recursively teleports the entity and all of its passengers after dismounting them.
         *
         * @param server    The minecraft server.
         * @param sourceDim The source dimension.
         * @param targetDim The target dimension.
         * @param xCoord    The target x position.
         * @param yCoord    The target y position.
         * @param zCoord    The target z position.
         * @param yaw       The target yaw.
         * @param pitch     The target pitch.
         */
        public void teleport(MinecraftServer server, ResourceKey<Level> sourceDim, ResourceKey<Level> targetDim, double xCoord, double yCoord, double zCoord, float yaw, float pitch) {
            entity.ejectPassengers();
            entity = handleEntityTeleport(entity, server, sourceDim, targetDim, xCoord, yCoord, zCoord, yaw, pitch);
            for (PassengerHelper passenger : passengers) {
                passenger.teleport(server, sourceDim, targetDim, xCoord, yCoord, zCoord, yaw, pitch);
            }
        }

        /**
         * Recursively remounts all of this entities riders and offsets their position relative to their position before teleporting.
         */


        /**
         * This method sends update packets to any players that were teleported with the entity stack.
         */
        public void updateClients() {
            if (entity instanceof ServerPlayer) {
                updateClient((ServerPlayer) entity);
            }
            for (PassengerHelper passenger : passengers) {
                passenger.updateClients();
            }
        }

        /**
         * This is the method that is responsible for actually sending the update to each client.
         *
         * @param playerMP The Player.
         */
        private void updateClient(ServerPlayer playerMP) {
            if (entity.isVehicle()) {
                playerMP.connection.send(new ClientboundSetPassengersPacket(entity));
            }
            for (PassengerHelper passenger : passengers) {
                passenger.updateClients();
            }
        }

        /**
         * This method returns the helper for a specific entity in the stack.
         *
         * @param passenger The passenger you are looking for.
         * @return The passenger helper for the specified passenger.
         */
        public PassengerHelper getPassenger(Entity passenger) {
            if (this.entity == passenger) {
                return this;
            }

            for (PassengerHelper rider : passengers) {
                PassengerHelper re = rider.getPassenger(passenger);
                if (re != null) {
                    return re;
                }
            }

            return null;
        }
    }
}
