package geni.witherutils.base.common;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CommonRegistry {

	public static void onCommonSetup(final FMLCommonSetupEvent event)
	{
	}
	
    public Player getClientPlayer()
    {
        return null;
    }
}
