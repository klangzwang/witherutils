package geni.witherutils.base.common.item.worm;

import java.util.List;

import geni.witherutils.base.common.base.WitherItem;
import geni.witherutils.base.common.entity.worm.Worm;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class WormItem extends WitherItem {

	public WormItem()
	{
		super(new Item.Properties());
	}

	@Override
	public InteractionResult useOn(UseOnContext context)
	{
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getPlayer().getItemInHand(context.getHand());
        BlockState state = context.getLevel().getBlockState(pos);
        Level level = context.getLevel();

        if(Worm.canWormify(level, pos, state))
        {
            List<Worm> worms = level.getEntitiesOfClass(Worm.class, new AABB(pos.getX() - 1, pos.getY(), pos.getZ() - 1, pos.getX() + 2, pos.getY() + 1, pos.getZ() + 2));
            if(worms.isEmpty())
            {
                if(!level.isClientSide)
                {
                	Worm worm = new Worm(WUTEntities.WORM.get(), level);
                    worm.moveTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                    worm.setCustomName(stack.getDisplayName());
                    
                    level.addFreshEntity(worm);
                    
                    if(!context.getPlayer().isCreative())
                    {
                        stack.shrink(1);
                    }
                }
                
                SoundUtil.playSoundDistrib(level, pos, WUTSounds.WORMBIP.get(), 2.0f, 1.0f, false, true);
                return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(context);
	}
}
