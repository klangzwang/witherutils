package geni.witherutils.core.common.helper;

import geni.witherutils.api.soul.PlayerSoul;
import geni.witherutils.base.common.init.WUTAttachments;
import geni.witherutils.base.common.init.WUTCapabilities;
import geni.witherutils.core.common.network.CoreNetwork;
import geni.witherutils.core.common.network.PacketSoulsSync;
import net.minecraft.server.level.ServerPlayer;

public class SoulsHelper {

	public static ServerPlayer player;
	
	public static void addSoul()
	{
		int attachedSouls = player.getData(WUTAttachments.SOULS_CONTROL);
		PlayerSoul soulHandler = WUTCapabilities.getPlayerSoulHandler(player).orElse(null);
		soulHandler.addSouls(attachedSouls + 1);
		player.setData(WUTAttachments.SOULS_CONTROL, soulHandler.getSouls());
		CoreNetwork.sendToPlayer(new PacketSoulsSync(soulHandler.getSouls()), player);
	}
	
	public static void removeSoul()
	{
		int attachedSouls = player.getData(WUTAttachments.SOULS_CONTROL);
		if(attachedSouls > 0)
		{
			PlayerSoul soulHandler = WUTCapabilities.getPlayerSoulHandler(player).orElse(null);
			soulHandler.addSouls(attachedSouls - 1);
			player.setData(WUTAttachments.SOULS_CONTROL, soulHandler.getSouls());
			CoreNetwork.sendToPlayer(new PacketSoulsSync(soulHandler.getSouls()), player);
		}
	}
	
	public static int getSouls()
	{
		return WUTCapabilities.getPlayerSoulHandler(player).orElse(null).getSouls();
	}
	
	public static void setSouls(int souls)
	{
		PlayerSoul soulHandler = WUTCapabilities.getPlayerSoulHandler(player).orElse(null);
		soulHandler.setSouls(souls);
		CoreNetwork.sendToPlayer(new PacketSoulsSync(soulHandler.getSouls()), player);
		
//		WUTCapabilities.getPlayerSoulHandler(player).ifPresent(f -> {
//			f.setSouls(souls);
//			CoreNetwork.sendToPlayer(new PacketSoulsSync(f.getSouls()), player);
//		});
	}
}
