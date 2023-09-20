package geni.witherutils.base.common.soul;

import org.apache.commons.lang3.*;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.nbt.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import com.mojang.authlib.*;

import geni.witherutils.core.common.util.NBTUtil;

import java.util.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@SuppressWarnings("unused")
public class Freq
{
	private static final String WU_TAG = "WU";
    private static final String WU_FREQ_TAG = "Frequency";
    public static Freq INSTANCE;
    private static Random rand;
    
    @SuppressWarnings("deprecation")
	public static int getBasePlayerFreq(final ServerPlayer player)
    {
        final CompoundTag entityData = NBTUtil.getPersistentTag(player);
        final CompoundTag wu2Tag = NBTUtil.getOrInitTagCompound(entityData, "WU");
        
        int i = wu2Tag.getInt("Frequency");
        
        final GameProfile gameProfile = player.getGameProfile();
        
        if (i != 0)
        {
            SoulOrbManager.INSTANCE.frequncies.putIfAbsent(i, (GameProfile) gameProfile);
            return i;
        }
        
        synchronized (SoulOrbManager.MUTEX)
        {
            final UUID uuid = (player).getUUID();
            Freq.rand.setSeed(uuid.getLeastSignificantBits() ^ uuid.getMostSignificantBits() ^ ObjectUtils.hashCode(gameProfile.getName()));
            do
            {
                i = Freq.rand.nextInt();
            }
            while (i == 0 || SoulOrbManager.INSTANCE.frequncies.containsKey(i));
            
            wu2Tag.putInt("Frequency", i);
            
            SoulOrbManager.INSTANCE.frequncies.put(i, gameProfile);
            SoulOrbManager.INSTANCE.reassignValues();
        }
        return i;
    }
    
    @SubscribeEvent
    public void load(final PlayerEvent.LoadFromFile event)
    {
        getBasePlayerFreq((ServerPlayer)event.getEntity());
    }

    static
    {
        Freq.rand = new Random();
        Freq.INSTANCE = new Freq();
    }
}
