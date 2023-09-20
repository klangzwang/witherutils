package geni.witherutils.base.common.soul;

import java.lang.ref.ReferenceQueue;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.WeakHashMap;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.mojang.authlib.GameProfile;

import geni.witherutils.base.common.item.soulorb.SoulOrbItem;
import geni.witherutils.base.common.soul.player.PlayerSoulManager;
import geni.witherutils.core.common.helper.DescribeHelper;
import geni.witherutils.core.common.network.CoreNetwork;
import geni.witherutils.core.common.network.PacketSyncServerSouls;
import geni.witherutils.core.common.util.McTimerUtil;
import io.netty.util.collection.IntObjectHashMap;
import it.unimi.dsi.fastutil.ints.Int2BooleanFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunctions;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntObjectImmutablePair;
import it.unimi.dsi.fastutil.ints.IntObjectMutablePair;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@SuppressWarnings("unused")
public class SoulOrbManager {

	public static final SoulOrbManager INSTANCE;
    public static final Object MUTEX;
	public static int souls;
	
    public final IntOpenHashSet lockedFrequencies;
    public final WeakHashMap<ServerPlayer, Freq> assignedValuesPlayer;
	public final IntObjectHashMap<GameProfile> frequncies;
    public final ReferenceQueue<Object> weakSoulsToRemove;
	public final IntObjectHashMap<Freq> frequencyHolders;
	
    private final LinkedList<ISoul> soulsToAdd;
    private final LinkedList<ISoul> soulsToRemove;
    
    public WeakHashMap<ISoul, Freq> assignedValues;
    
	public static RandomSource random = RandomSource.create();
	
    private final Int2IntOpenHashMap links;
    
    public boolean dirty;
	private boolean playersDirty;
    int p;
    
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SoulOrbManager()
	{
		this.lockedFrequencies = new IntOpenHashSet();
		this.assignedValuesPlayer = new WeakHashMap<ServerPlayer, Freq>();
		this.frequncies = new IntObjectHashMap();
		this.weakSoulsToRemove = new ReferenceQueue<Object>();
		this.frequencyHolders = new IntObjectHashMap();
		
        this.soulsToAdd = new LinkedList<ISoul>();
        this.soulsToRemove = new LinkedList<ISoul>();
        
        this.links = new Int2IntOpenHashMap();
        
        this.dirty = true;
        this.playersDirty = true;
        int p = 0;
	}
	
    @SubscribeEvent
    public void tick(final TickEvent.PlayerTickEvent event)
    {
    	onPlayerTick(event.player);
    }

	public static void onPlayerTick(Player player)
	{
		for(InteractionHand hands : InteractionHand.values())
		{
			ItemStack heldStack = player.getItemInHand(hands);
			if(heldStack.getItem() instanceof SoulOrbItem)
			{
				if(player.isUsingItem())
				{
					if(souls < 2000)
					{
						souls += 200;
					}
					else
						return;
					
			        if(!heldStack.isEmpty())
			        	heldStack.shrink(1);
				}
			}
			if(souls < 0)
				souls = 0;
			fallDownSouls(player);
		}
	}

	public static void fallDownSouls(Player player)
	{
		if(player.level().getGameTime() % 2 == 0)
			souls -= 1;
		
		if(souls > 0)
			return;
		else
		{
			souls = 0;
		}
	}

	public static float calcDynamicAmount(Player player)
	{
		float time = player.level().getLevelData().getGameTime() + McTimerUtil.renderPartialTickTime;
		double offset = 1.0D + Math.sin(time * 1 / 3.0D) / 6.0D;
		double multiplier = 1.0D + offset;
		
		return (float) multiplier;
	}

    public static int getCurrentSouls()
    {
    	return souls / 10;
    }

    public void addSoulHandler(final ISoul needer)
    {
        synchronized (SoulOrbManager.MUTEX)
        {
            this.soulsToAdd.add(needer);
        }
    }
    public void removeSoulHandler(final ISoul needer)
    {
        synchronized (SoulOrbManager.MUTEX)
        {
            this.soulsToRemove.add(needer);
        }
    }
    
    public void reassignValues()
    {
        this.dirty = true;
        this.playersDirty = true;
    }
    
    public void getDebug(final List<String> info)
    {
    	Int2ObjectFunction<Object> procedure;
//        final Int2ObjectMap<Object> procedure = (Int2ObjectMap<Object>)((a, b) -> {
//            info.add(a + "=" + b.toString());
//            return true;
//        });
        DescribeHelper.addDescription(info, "Frequencies");
//        this.frequncies.forEachEntry((TIntObjectProcedure)procedure);
        DescribeHelper.addDescription(info, "Alliances");
//        this.alliances.forEachEntry((TIntObjectProcedure)procedure);
        DescribeHelper.addDescription(info, "AssignedValues");
//        DescribeHelper.addDescription(info, this.assignedValues);
        DescribeHelper.addDescription(info, "Frequencie Holders");
//        this.frequencyHolders.forEach(new Int2ObjectFunction<Freq> ->
//        {
//            public boolean execute(final int a, final Freq b)
//            {
//                DescribeHelper.addDescription(info, "Init");
//                this.describe(a, b);
//                DescribeHelper.addDescription(info, "QRefresh");
////                b.quickRefresh();
//                this.describe(a, b);
//                DescribeHelper.addDescription(info, "FRefresh");
////                b.refresh();
//                this.describe(a, b);
//                return true;
//            }
//            
//            public void describe(final int a, final Freq b)
//            {
//                DescribeHelper.addDescription(info, ("Freq = " + a), 2);
//                DescribeHelper.addDescription(info, "Power", 2);
////                DescribeHelper.addDescription(info, (b.powerDrained + " " + b.powerCreated), 3);
//                DescribeHelper.addDescription(info, "Players", 2);
////                DescribeHelper.addDescription(info, b.players, 3);
//                DescribeHelper.addDescription(info, "Creators", 2);
////                DescribeHelper.addDescription(info, b.worldPowerCreators, 3);
//                DescribeHelper.addDescription(info, "Drainers", 2);
////                DescribeHelper.addDescription(info, b.worldPowerDrainers, 3);
//                DescribeHelper.addDescription(info, "IPowers", 2);
////                DescribeHelper.addDescription(info, b.powerHandlers, 3);
//            }
//        });
    }
    public static void init()
    {
        MinecraftForge.EVENT_BUS.register(SoulOrbManager.INSTANCE);
        MinecraftForge.EVENT_BUS.register(new PlayerSoulManager());
    }
    
    public static float getCurrentPower(final ISoul power)
    {
        final float v = power.getPower();
        if (v == 0.0f)
        {
            return 0.0f;
        }
        final IWorldSoulMultiplier multiplier = power.getMultiplier();
        return v * multiplier.multiplier(power.world());
    }
    
    public static boolean areFreqOnSameGrid(final int freq, final int frequency)
    {
        return freq == frequency || SoulOrbManager.INSTANCE.getSoulFreq(freq) == SoulOrbManager.INSTANCE.getSoulFreq(frequency);
    }
    
    public static boolean canUse(final Player player, final ISoul power)
    {
        if (!(player instanceof ServerPlayer))
        {
            return false;
        }
        if (!SoulOrbManager.INSTANCE.lockedFrequencies.contains(power.frequency()))
        {
            return true;
        }
        final int basePlayerFreq = Freq.getBasePlayerFreq((ServerPlayer) player);
        return basePlayerFreq != 0 && areFreqOnSameGrid(power.frequency(), basePlayerFreq);
    }
    
    public void clear()
    {
        synchronized (SoulOrbManager.MUTEX)
        {
            this.frequncies.clear();
            this.p = 0;
            while (this.weakSoulsToRemove.poll() != null) {}
            this.links.clear();
            this.frequencyHolders.clear();
            this.soulsToAdd.clear();
            this.soulsToRemove.clear();
            this.assignedValuesPlayer.clear();
            this.playersDirty = true;
            this.assignedValues.clear();
        }
    }
    
    public boolean isPowered(final ServerPlayer player)
    {
        Freq powerFreq = this.assignedValuesPlayer.get(player);
        if (powerFreq == null)
        {
//            if (!PlayerHelper.isPlayerReal(player))
//            {
//                return false;
//            }
            powerFreq = this.getSoulFreq(Freq.getBasePlayerFreq(player));
        }
        return powerFreq != null && powerFreq.isPowered();
    }
    
    @Nullable
    public Freq getSoulFreqRaw(final int frequency)
    {
        final int i = this.links.containsKey(frequency) ? this.links.get(frequency) : frequency;
        return this.frequencyHolders.get(i);
    }
    
    public Freq getSoulFreq(final int frequency)
    {
        final int i = this.links.containsKey(frequency) ? this.links.get(frequency) : frequency;
        Freq powerFreq = this.frequencyHolders.get(i);
        if (powerFreq == null)
        {
            synchronized (SoulOrbManager.MUTEX)
            {
                powerFreq = new Freq(i);
                powerFreq.dirty = true;
                this.frequencyHolders.put(i, powerFreq);
            }
        }
        return powerFreq;
    }
    
    public void markDirty(final ISoul power)
    {
        Freq powerFreq = this.assignedValues.get(power);
        if (powerFreq == null)
        {
            powerFreq = this.getSoulFreq(power.frequency());
        }
        powerFreq.dirty = true;
    }
    
    @SubscribeEvent
    public void tick(final TickEvent.ServerTickEvent event)
    {
        ++this.p;
        if (this.p > 600)
        {
            this.p = 0;
        }
    }
    
	static
	{
		INSTANCE = new SoulOrbManager();
        MUTEX = new Object();
	}
	
    public static class Freq implements IPowerReport
    {
        final int frequency;
        public LinkedList<ISoul> powerHandlers;
        float powerDrained;
        float powerCreated;
        IntOpenHashSet playerFreqs;
        LinkedList<ServerPlayer> players;
        int refresh_delta;
        public boolean dirty;
        private WeakHashMap<Level, Object2FloatOpenHashMap<IWorldSoulMultiplier>> worldPowerCreators;
        private WeakHashMap<Level, Object2FloatOpenHashMap<IWorldSoulMultiplier>> worldPowerDrainers;
        private HashMap<ResourceLocation, Collection<ISoulSubType>> subTypes;
        
        public Freq(final int frequency)
        {
            this.powerHandlers = new LinkedList<ISoul>();
            this.playerFreqs = new IntOpenHashSet();
            this.players = new LinkedList<ServerPlayer>();
            this.refresh_delta = RandomSource.create().nextInt(600);
            this.dirty = true;
            this.worldPowerCreators = new WeakHashMap<Level, Object2FloatOpenHashMap<IWorldSoulMultiplier>>();
            this.worldPowerDrainers = new WeakHashMap<Level, Object2FloatOpenHashMap<IWorldSoulMultiplier>>();
            this.frequency = frequency;
        }

        public static int getBasePlayerFreq(ServerPlayer player)
        {
			return 0;
		}

		public void sendNetworkUpdates()
        {
            for (final ServerPlayer player : this.players)
            {
                CoreNetwork.sendToPlayer(player, new PacketSyncServerSouls(this.powerCreated, this.powerDrained));
            }
        }
        
        public void changeStatus(final boolean powered)
        {
            for (final ISoul powerNeeder : this.powerHandlers)
            {
                powerNeeder.powerChanged(powered);
            }
        }
        @Override
        public boolean isPowered()
        {
            return this.powerDrained <= this.powerCreated;
        }
        @Override
        public float getPowerDrain()
        {
            return this.powerDrained;
        }
        @Override
        public float getPowerCreated()
        {
            return this.powerCreated;
        }
    }

    public interface IPowerReport
    {
        boolean isPowered();

        float getPowerDrain();
        float getPowerCreated();
    }
}
