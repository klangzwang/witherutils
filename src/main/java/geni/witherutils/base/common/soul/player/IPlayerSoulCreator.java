package geni.witherutils.base.common.soul.player;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IPlayerSoulCreator
{
    PlayerSoul createSoul(final Player player, final ItemStack stack);
    
    default boolean shouldOverride(final PlayerSoul soul, final Player player, final ItemStack stack, final boolean isSelected)
    {
        return false;
    }
}
