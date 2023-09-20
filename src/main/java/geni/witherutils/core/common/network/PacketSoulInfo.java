package geni.witherutils.core.common.network;

import java.util.Optional;

import geni.witherutils.base.common.soul.Freq;
import geni.witherutils.base.common.soul.ISoul;
import geni.witherutils.base.common.soul.SoulOrbManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;

public class PacketSoulInfo implements Packet
{
    BlockPos pos;
    float energy;
    static Player player;
    
    public PacketSoulInfo(float energy, BlockPos pos)
    {
        this.energy = -1.0f;
        this.energy = energy;
        this.pos = pos;
    }

    @SuppressWarnings("static-access")
	public PacketSoulInfo(FriendlyByteBuf buf, Player player)
    {
        this.player = player;
        this.pos = buf.readBlockPos();
        this.energy = buf.readFloat();
    }
    
    @Override
    public boolean isValid(Context context)
    {
        return context.getDirection() == NetworkDirection.PLAY_TO_SERVER;
    }

    protected void write(FriendlyByteBuf buf)
    {
    	buf.writeBlockPos(this.pos);
    	buf.writeFloat(this.energy);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle(NetworkEvent.Context context)
    {
        if (!(PacketSoulInfo.player instanceof ServerPlayer))
        {
            return;
        }
        final BlockEntity tile = PacketSoulInfo.player.level().getBlockEntity(PacketSoulInfo.this.pos);
        if (!(tile instanceof ISoul))
        {
            return;
        }
        
        final int freq = Freq.getBasePlayerFreq((ServerPlayer)PacketSoulInfo.player);
        final ISoul power = (ISoul)tile;
        final int frequency = power.frequency();
        
        float v;

        if (SoulOrbManager.areFreqOnSameGrid(freq, frequency))
        {
            v = SoulOrbManager.getCurrentPower(power);
        }
        else
        {
            v = Float.NaN;
        }
        if (v == PacketSoulInfo.this.energy)
        {
            return;
        }
        CoreNetwork.sendToPlayer((ServerPlayer) PacketSoulInfo.player, new PacketSoulInfo(v, PacketSoulInfo.this.pos));
    }

    public static class Handler extends PacketHandler<PacketSoulInfo>
    {
        @Override
        public PacketSoulInfo fromNetwork(FriendlyByteBuf buf)
        {
            return new PacketSoulInfo(buf, player);
        }
        @Override
        public Optional<NetworkDirection> getDirection()
        {
            return Optional.of(NetworkDirection.PLAY_TO_CLIENT);
        }
        @Override
        public void toNetwork(PacketSoulInfo packet, FriendlyByteBuf buf)
        {
            packet.write(buf);
        }
    }
}
