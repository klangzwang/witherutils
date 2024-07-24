package geni.witherutils.base.common.item.cutter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import geni.witherutils.base.common.base.WitherItem;
import geni.witherutils.core.common.util.ItemStackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

public class CutterItem extends WitherItem {

	public CutterItem()
	{
		super(new Item.Properties().stacksTo(1).durability(250));
        NeoForge.EVENT_BUS.register(new EventHandler());
	}
	
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand handIn)
    {
        ItemStack stack = player.getItemInHand(handIn);
        if (player instanceof ServerPlayer sp)
        {
            openGui(sp, stack, handIn);
        }
        return InteractionResultHolder.success(stack);
    }
	
    @Override
    public InteractionResult onItemUseFirst(ItemStack remote, UseOnContext ctx)
    {
        Player player = ctx.getPlayer();
        if (player instanceof ServerPlayer sp)
            openGui(sp, remote, ctx.getHand());
        return InteractionResult.SUCCESS;
    }
	
    private void openGui(ServerPlayer player, ItemStack remote, InteractionHand hand)
    {
        if (!player.isCrouching())
        	player.openMenu(new CutterContainerProvider());
    }

	@Override
	public int getEnchantmentValue(ItemStack stack)
	{
		return 0;
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack)
	{
		return false;
	}
	
	@Override
	public float getDestroySpeed(ItemStack par1ItemStack, BlockState par2Block)
	{
		return 1F;
	}
    
    @Override
    public int getMaxDamage(ItemStack stack)
    {
        return 0;
    }

    @Override
    public boolean isValidRepairItem(ItemStack damagedItem, ItemStack repairMaterial)
    {
        return repairMaterial == Items.IRON_INGOT.getDefaultInstance();
    }
    
    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return slotChanged || !ItemStack.isSameItem(oldStack, newStack);
    }
    
    /*
     * 
     * EVENTS
     * 
     */
    public static class EventHandler
    {
        @SubscribeEvent
        public void onPlayerInteract(PlayerInteractEvent.LeftClickBlock event)
        {
            Player player = event.getEntity();
            ItemStack held = event.getItemStack();
            
            if (held.getItem() instanceof CutterItem)
            {
            	ItemStackUtil.damageItem(held);
            	BlockState state = event.getLevel().getBlockState(event.getPos());
            	playSound(player, held, state.getBlock(), SoundEvents.STONE_HIT);
            	setNextVariation(event.getLevel(), player, event.getPos(), state);
            	event.setCanceled(true);
            }
        }
        
        private static void setNextVariation(Level level, Player player, BlockPos pos, BlockState origState)
        {
        	level.levelEvent(2001, pos, Block.getId(origState.getBlock().defaultBlockState()));
//        	RandomSource random = RandomSource.create();
//            CutterBlock cutterBlocks = WUTBlocks.CUTTERBLOCKS.get(random.nextInt(WUTBlocks.CUTTERBLOCKS.size()));
//        	level.setBlockAndUpdate(pos, cutterBlocks.defaultBlockState());
        }

//        @SubscribeEvent
//        public void onRightClickItem(PlayerInteractEvent.RightClickItem event)
//        {
//        	Level level = event.getLevel();
//            if (!level.isClientSide)
//            {
//                ItemStack stack = event.getItemStack();
//                if (stack.getItem() instanceof CutterItem)
//                {
//                }
//            }
//        }
        
//        @SubscribeEvent
//        public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
//        {
//            if (event.getHand() == InteractionHand.OFF_HAND)
//            {
//                ItemStack mainhandStack = event.getEntity().getMainHandItem();
//                if (mainhandStack.getItem() instanceof CutterItem)
//                {
//                    event.setCanceled(true);
//                }
//            }
//        }

        public void playSound(Player player, ItemStack chisel, @Nullable Block target, @Nonnull SoundEvent sound)
        {
            playSound(player, player.blockPosition(), sound);
        }
        public void playSound(Player player, BlockPos pos, SoundEvent sound)
        {
            playSound(player, pos, sound, SoundSource.BLOCKS);
        }
        public void playSound(Player player, BlockPos pos, SoundEvent sound, SoundSource category)
        {
            Level world = player.getCommandSenderWorld();
            world.playSound(player, pos, sound, category, 0.3f + 0.7f * world.random.nextFloat(), 0.6f + 0.4f * world.random.nextFloat());
        }

        @SubscribeEvent
        public void onBlockBreak(BlockEvent.BreakEvent event)
        {
            ItemStack stack = event.getPlayer().getMainHandItem();
            if (event.getPlayer().getAbilities().instabuild && !stack.isEmpty() && stack.getItem() instanceof CutterItem)
            {
                event.setCanceled(true);
            }
        }
    }
}
