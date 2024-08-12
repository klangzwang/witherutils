package geni.witherutils.base.common.data;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import geni.witherutils.WitherUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public final class PlayerData {
	
    private static PlayerSave clientData = null;
    
    public static PlayerSave getDataFromPlayer(Player player)
    {
        if (!(player.getCommandSenderWorld() instanceof ServerLevel))
        {
            if (clientData == null)
            {
                clientData = new PlayerSave(player.getUUID());
            }
            return clientData;
        }
        
        WorldData worldData = WorldData.get(player.getCommandSenderWorld());
        ConcurrentHashMap<UUID, PlayerSave> data = worldData.playerSaveData;
        UUID id = player.getUUID();

        if (data.containsKey(id))
        {
            PlayerSave save = data.get(id);
            if (save != null && save.id != null && save.id.equals(id))
            {
                return save;
            }
        }

        PlayerSave save = new PlayerSave(id);
        data.put(id, save);
        worldData.setDirty();
        
        WitherUtils.LOGGER.info("PlayerData ### PlayerSave: NewData " + worldData + " for Player " + player);
        
        return save;
    }

    public static class PlayerSave
    {
        public UUID id;
        public int NumberOfPlaced;
        
        public PlayerSave(UUID id)
        {
            this.id = id;
        }

        public void readFromNBT(CompoundTag compound, boolean savingToFile)
        {
            this.NumberOfPlaced = compound.getInt("BlockPlaced");
        }

        public void writeToNBT(CompoundTag compound, boolean savingToFile)
        {
            compound.putInt("BlockPlaced", this.NumberOfPlaced);
        }
        
        public int getNumberOfPlaced()
        {
        	return NumberOfPlaced;
        }
        
        public void setNumberOfPlaced(boolean removeOne)
        {
        	NumberOfPlaced = removeOne ? (NumberOfPlaced - 1) : (NumberOfPlaced + 1) ;
        }
    }
}
