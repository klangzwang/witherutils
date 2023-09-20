package geni.witherutils.base.common.item.soulorb;

import geni.witherutils.base.common.base.WitherItem;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.base.common.soul.SoulOrbManager;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SoulOrbItem extends WitherItem {
	
    public SoulOrbItem()
	{
        super(new Item.Properties());
	}
    
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		ItemStack itemstack = player.getItemInHand(hand);
		
		if(SoulOrbManager.getCurrentSouls() >= 200)
			return InteractionResultHolder.fail(itemstack);

		player.startUsingItem(hand);
		
		world.playSound((Player)null, player.getX(), player.getY(), player.getZ(), WUTSounds.WRINKLYDEATH.get(), SoundSource.NEUTRAL, 0.38F, 1.0F);
        return InteractionResultHolder.sidedSuccess(itemstack, world.isClientSide());
	}
}
