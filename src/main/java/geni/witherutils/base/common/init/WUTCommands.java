package geni.witherutils.base.common.init;

import com.mojang.brigadier.CommandDispatcher;

import geni.witherutils.base.common.comms.AllowFakeDriver;
import net.minecraft.commands.CommandSourceStack;

public class WUTCommands {
	
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
    	AllowFakeDriver.register(dispatcher);
    }
}
