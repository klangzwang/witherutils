package geni.witherutils.base.common.item.withersteel.armor.upgrades.minespeed;

import geni.witherutils.base.common.item.withersteel.armor.upgrades.SteelUpgradeItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MineSpeedUpgrade extends SteelUpgradeItem {

    @SuppressWarnings("resource")
	@SubscribeEvent
    public void armorMineSpeed(PlayerEvent.BreakSpeed event)
    {
        if(event.getEntity() == null)
            return;

        if(event.getEntity() instanceof Player)
        {
        	Player player = (Player) event.getEntity();
            ItemStack pickaxeStack = player.getItemInHand(InteractionHand.MAIN_HAND);
            if(pickaxeStack.getItem() instanceof PickaxeItem)
            {
                if(event.getEntity().level().isClientSide)
                {
                    if(pickaxeStack != null)
                    	pickaxeStack.getOrCreateTag().putInt("MiningSpeed", 1);
                }
                if(pickaxeStack == null || !pickaxeStack.hasTag())
                    return;

                CompoundTag tags = pickaxeStack.getTag().getCompound("MiningSpeed");

                float mineSpeed = tags.getInt("MiningSpeed");
                float modifier = 1f + mineSpeed / 1000f;
                float base = mineSpeed / 250f;
                event.setNewSpeed((event.getOriginalSpeed() + base) * modifier);
            }
        }
    }
}
