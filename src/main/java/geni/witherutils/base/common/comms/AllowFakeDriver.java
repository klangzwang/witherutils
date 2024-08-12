package geni.witherutils.base.common.comms;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.commands.CommandSourceStack;

public class AllowFakeDriver {
	
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
//        dispatcher.register(Commands.literal("addmahou")
//		.then(Commands.argument("player", EntityArgument.player())
//		.then(Commands.argument("mana", IntegerArgumentType.integer(-1000000000, 1000000000))
//		.then((Commands.argument("overDrainHurts", FloatArgumentType.floatArg(0.0f, 1.0E9f))
//		.then(Commands.argument("damagetype", IntegerArgumentType.integer((int)0, (int)2)).executes(ctx ->
//		AddMana.run((CommandContext<CommandSourceStack>)ctx, IntegerArgumentType.getInteger(ctx, "mana"),
//		FloatArgumentType.getFloat(ctx, "overDrainHurts"), IntegerArgumentType.getInteger(ctx, "damagetype")))))
//		.requires(cs -> cs.hasPermission(2))))));
    }
}
