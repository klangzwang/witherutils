package geni.witherutils.base.common.data;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import geni.witherutils.WitherUtils;
import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.common.data.PlayerData.PlayerSave;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

public class WorldData extends SavedData {

    public static final String DATA_TAG = WitherUtilsRegistry.MODID + "data";
    public static final String SAVE_NAME = WitherUtilsRegistry.MODID + "_worldsave";
    
    private static WorldData data;
    
    public final ConcurrentHashMap<UUID, PlayerSave> playerSaveData = new ConcurrentHashMap<>();

    public WorldData()
    {
        super();
    }

    public static WorldData get(Level level)
    {
        return ((ServerLevel) level).getDataStorage().computeIfAbsent(new Factory<>(WorldData::new, WorldData::load), SAVE_NAME);
    }

    public static void clear()
    {
        if (data != null)
        {
            data = null;
        }
    }

    public static WorldData load(CompoundTag compound, HolderLookup.Provider provider)
    {
        WorldData worldData = new WorldData();
        worldData.playerSaveData.clear();
        ListTag playerList = compound.getList("PlayerData", 10);
        
        for (int i = 0; i < playerList.size(); i++)
        {
            CompoundTag player = playerList.getCompound(i);

            UUID id = player.getUUID("UUID");
            CompoundTag data = player.getCompound("Data");

            PlayerSave save = new PlayerSave(id);
            save.readFromNBT(data, true);
            worldData.playerSaveData.put(id, save);
            
            WitherUtils.LOGGER.info("WorldData ### Load: Data " + data + " for Player " + player);
        }
        return worldData;
    }

    @Override
    public CompoundTag save(CompoundTag compound, HolderLookup.Provider provider)
    {
        ListTag playerList = new ListTag();
        for (PlayerSave save : this.playerSaveData.values())
        {
            CompoundTag player = new CompoundTag();
            player.putUUID("UUID", save.id);

            CompoundTag data = new CompoundTag();
            save.writeToNBT(data, true);
            player.put("Data", data);

            playerList.add(player);
            
            WitherUtils.LOGGER.info("WorldData ### Save: Data " + data + " for Player " + player);
        }
        compound.put("PlayerData", playerList);
        
        return compound;
    }
}
