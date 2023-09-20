package geni.witherutils.base.common.soul.player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Nullable;

import com.mojang.datafixers.util.Pair;

import geni.witherutils.base.common.soul.SoulOrbManager;
import geni.witherutils.core.common.util.PlayerUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

public class PlayerSoulManager {

	private MinecraftServer server;
	static HashMap<Pair<Player, IPlayerSoulCreator>, PlayerSoul> powerServer;
	
    public static void clear()
    {
        PlayerSoulManager.powerServer.clear();
    }
    
    @Nullable
    public static <T extends Item> PlayerSoul get(final Player player, final T item)
    {
        return PlayerSoulManager.powerServer.get(Pair.of(player, item));
    }
    
	@SubscribeEvent
    public void tick(final TickEvent.ServerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
            return;
        
        final HashSet<Player> players = new HashSet<Player>(getServerPlayerList().getPlayers());
        final HashSet<PlayerSoul> loadedPowers = new HashSet<PlayerSoul>();
        final HashSet<PlayerSoul> addedPowers = new HashSet<PlayerSoul>();
        for (final Player player : players)
        {
            for (final ItemStack stack : PlayerUtil.invStacks(player))
            {
                if (!stack.isEmpty() && stack.getItem() instanceof IPlayerSoulCreator)
                {
                    final IPlayerSoulCreator creator = (IPlayerSoulCreator)stack.getItem();
                    final Pair<Player, IPlayerSoulCreator> key = Pair.of(player, creator);
                    PlayerSoul playerPower = PlayerSoulManager.powerServer.get(key);
                    if (playerPower == null || creator.shouldOverride(playerPower, player, stack, stack == player.inventory.getSelected()))
                    {
                        if (playerPower != null)
                        {
                            playerPower.onRemove();
                        }
                        playerPower = creator.createSoul(player, stack);
                        if (playerPower == null) {
                            continue;
                        }
                        PlayerSoulManager.powerServer.put(key, playerPower);
                        addedPowers.add(playerPower);
                    }
                    else if (!playerPower.shouldSustain(stack)) {
                        continue;
                    }
                    playerPower.update(player.inventory.getSelected() == stack, stack);
                    playerPower.cooldown = 20;
                    loadedPowers.add(playerPower);
                }
            }
        }
        final Iterator<Map.Entry<Pair<Player, IPlayerSoulCreator>, PlayerSoul>> iterator = PlayerSoulManager.powerServer.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<Pair<Player, IPlayerSoulCreator>, PlayerSoul> entry = iterator.next();
            final Player player2 = entry.getKey().getFirst();
            final PlayerSoul power = entry.getValue();
            final boolean playerIsLoaded = !players.contains(player2);
//            final boolean incorrectDimension = player2.level().dimension() != power.dimension;
            final boolean expired = !loadedPowers.contains(power);
            if (playerIsLoaded || expired)
            {
                this.removePlayer(power);
                iterator.remove();
            }
            else
            {
                if(addedPowers.contains(power))
                {
                    continue;
                }
                power.tick();
            }
        }
        for (final PlayerSoul playerPower2 : addedPowers) {
            playerPower2.onAdd();
            SoulOrbManager.INSTANCE.addSoulHandler(playerPower2);
        }
    }
    
	public MinecraftServer getServer()
	{
		MinecraftServer server = this.server;
		if(ServerLifecycleHooks.getCurrentServer() != null)
			return ServerLifecycleHooks.getCurrentServer();
		return server;
	}
	
	public PlayerList getServerPlayerList()
	{
		return getServer().getPlayerList();
	}
	
    public void removePlayer(final PlayerSoul power)
    {
        power.invalid = true;
        power.onRemove();
        SoulOrbManager.INSTANCE.removeSoulHandler(power);
    }
    
    static
    {
        PlayerSoulManager.powerServer = new HashMap<Pair<Player, IPlayerSoulCreator>, PlayerSoul>();
    }
}
