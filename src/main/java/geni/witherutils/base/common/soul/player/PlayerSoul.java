package geni.witherutils.base.common.soul.player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import geni.witherutils.base.common.soul.Freq;
import geni.witherutils.base.common.soul.ISoul;
import geni.witherutils.base.common.soul.IWorldSoulMultiplier;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.server.ServerLifecycleHooks;

public abstract class PlayerSoul implements ISoul, IWorldSoulMultiplier {
	
	private MinecraftServer server;
	
    public final int freq;
    
    @Nonnull
    private final Player player;
    public int cooldown;
    public boolean invalid;
    
    public PlayerSoul(@Nonnull final Player player)
    {
        this.cooldown = 20;
        this.player = player;
        
        if (player instanceof ServerPlayer)
        {
            this.freq = Freq.getBasePlayerFreq((ServerPlayer) player);
        }
        else
        {
            this.freq = 0;
        }
    }
    
    public boolean isLoaded()
    {
        return true;
    }
    
    public IWorldSoulMultiplier getMultiplier()
    {
        return (IWorldSoulMultiplier) this;
    }
    
    public int frequency()
    {
        return this.freq;
    }
    
    public Level world()
    {
        return null;
    }
    
    @SuppressWarnings("unlikely-arg-type")
	public float multiplier(final Level world)
    {
        final AbstractClientPlayer playerMP = this.getPlayerMP();
        if (this.invalid || !getServerPlayerList().getPlayers().contains(playerMP))
            return 0.0f;
        return this.power((Player) playerMP);
    }
    
    @Nonnull
    public Player getPlayer()
    {
        return this.player;
    }
    
    @Nonnull
    public AbstractClientPlayer getPlayerMP()
    {
        return (AbstractClientPlayer) this.player;
    }
    
    public abstract float power(final Player p0);
    
    public final float getPower()
    {
        return 1.0f;
    }
    
    public void onAdd() {
    }
    
    public void onRemove() {
    }
    
    public void update(final boolean selected, final ItemStack params) {}
    
    public boolean shouldOveride(final Player player, final PlayerSoul other)
    {
        return false;
    }
    public void tick()
    {
    }
    public void onAddClient()
    {
    }
    public void onRemoveClient()
    {
    }
    public void tickClient()
    {
    }
    @Nullable
    public BlockPos getLocation()
    {
        return null;
    }
    public boolean shouldSustain(final ItemStack stack)
    {
        return true;
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
}
