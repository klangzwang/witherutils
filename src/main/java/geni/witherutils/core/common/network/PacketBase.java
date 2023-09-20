package geni.witherutils.core.common.network;

import java.util.function.Supplier;

import net.minecraftforge.network.NetworkEvent.Context;

public class PacketBase {

	public void done(Supplier<Context> ctx)
	{
		ctx.get().setPacketHandled(true);
	}
}
