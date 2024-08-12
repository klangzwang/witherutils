package geni.witherutils.base.common.item.scaper;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.common.base.IWitherInventoryItem;
import geni.witherutils.base.common.base.WitherItem;
import geni.witherutils.base.common.init.WUTComponents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;
import net.neoforged.neoforge.items.ComponentItemHandler;
import net.neoforged.neoforge.items.IItemHandler;

public class ScaperItem extends WitherItem implements IWitherInventoryItem {
	
	private Shape shapeMode = Shape.BOXED;
//	private static final int COOLDOWN = 28;
	public static IntValue RADIUS;
	
	public ScaperItem()
	{
		super(new Item.Properties().stacksTo(1).durability(250));
        if(FMLEnvironment.dist.isClient())
            ItemProperties.register(this, WitherUtilsRegistry.loc("canplace"), ScaperItem::registerScaperProperty);
	}

	@Override
	public ICapabilityProvider<ItemStack, Void, IItemHandler> initItemHandlerCap()
	{
		return (stack, v) -> new ComponentItemHandler(stack, WUTComponents.ITEMS.get(), 9);
	}
	
    @OnlyIn(Dist.CLIENT)
    static float registerScaperProperty(ItemStack stack, ClientLevel level, LivingEntity livingEntity, int seed)
    {
        float f = 0.0F;
        IItemHandler inv = stack.getCapability(Capabilities.ItemHandler.ITEM);
        if(inv != null)
        {
            int stackcount = inv.getStackInSlot(0).getCount();
            if(stackcount > 32 && stackcount < 64)
            	f = 0.5F;
            else if(stackcount == 64)
                f = 1.0F;
        }
        return f;
    }
    
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity pEntity, int pSlotId, boolean pIsSelected)
    {
    	if(pEntity instanceof Player player)
    	{
            BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
            if(result != null && result.getType() == HitResult.Type.BLOCK)
            {
    			BlockPos pos = result.getBlockPos();
    			System.out.println("TEST: " + pos);
            }
    	}
    	super.inventoryTick(stack, level, pEntity, pSlotId, pIsSelected);
    }
    
	public enum Shape {

		BOXED,
		CIRCLE,
		SPHERE;

		public static Shape getNextType(Shape btype)
		{
			int type = btype.ordinal();
			
			type++;
			if (type > SPHERE.ordinal())
			{
				type = BOXED.ordinal();
			}
			return Shape.values()[type];
		}

		public static String getSerializedName()
		{
			return Shape.values().toString();
		}
	}
	
    public Shape getShapeMode()
    {
    	return shapeMode;
    }
    
    public void setShapeMode(Shape mode)
    {
    	this.shapeMode = mode;
    }
    
    @Override
    public InteractionResult useOn(UseOnContext context)
    {
		Player player = context.getPlayer();
//		Level level = context.getLevel();
		
//        BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
//        if(result != null && result.getType() == HitResult.Type.BLOCK)
//        {
//			BlockPos pos = result.getBlockPos();
//			System.out.println("TEST: " + pos);
//			
////			buildShapeFromCenter(context.getLevel(), player, pos);
//        }
		player.swing(context.getHand());
		return super.useOn(context);
    }

//	private boolean buildShapeFromCenter(Level world, Player player, BlockPos posCenter)
//	{
//		ItemStack stack = player.getItemInHand(player.getUsedItemHand());
//
//		int count = 0;
//		for (BlockPos pos : ScaperShapes.sphere(posCenter, 4))
//		{
//			if (world.getBlockState(pos).isEmpty())
//			{
//		        IItemHandler inv = stack.getCapability(WUTCapabilities.SCAPERITEMHANDLER);
//		        if(inv != null)
//		        {
//					world.setBlockAndUpdate(pos, Blocks.SAND.defaultBlockState());
//					count++;
//					
//        			for (int j = 0; j < inv.getSlots(); j++)
//        			{
//        				inv.extractItem(j, 1, false);
//        			}
//		        }
//			}
//		}
//		
//		boolean success = count > 0;
//		if (success)
//		{
//			player.getCooldowns().addCooldown(stack.getItem(), COOLDOWN);
//		}
//		return success;
//	}

//    @Override
//    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand)
//    {
//        ItemStack itemstack = pPlayer.getItemInHand(pHand);
//        BlockHitResult blockhitresult = getPlayerPOVHitResult(pLevel, pPlayer, ClipContext.Fluid.NONE);
//        if (blockhitresult.getType() == HitResult.Type.BLOCK)
//        {
//        	if (pPlayer instanceof ServerPlayer sp)
//        		openGui(sp, itemstack, pHand);
//            return InteractionResultHolder.pass(itemstack);
//        }
//        else
//        {
//            pPlayer.startUsingItem(pHand);
//            if (pLevel instanceof ServerLevel level)
//            {
//        		int x = (int) (pPlayer.getX() + 3 * pPlayer.getLookAngle().x);
//        		int y = (int) (1.5 + pPlayer.getY() + 3 * pPlayer.getLookAngle().y);
//        		int z = (int) (pPlayer.getZ() + 3 * pPlayer.getLookAngle().z);
//
//        		BlockPos pos = new BlockPos(x, y, z);
//        		
//                if (pos != null)
//                {
////                    if (pPlayer instanceof ServerPlayer serverplayer)
////                    {
////                        CriteriaTriggers.USED_ENDER_EYE.trigger(serverplayer, blockpos);
////                    }
//
//                    IItemHandler inventory = itemstack.getCapability(Capabilities.ItemHandler.ITEM);
//                    if(inventory != null)
//                    {
//            			for (int j = 0; j < inventory.getSlots(); j++)
//            			{
//            				inventory.extractItem(j, 64, false);
//            			}
//                    }
//                    
//            		if(level.getBlockState(pos).isAir() || !level.getFluidState(pos).isEmpty())
//            		{
//            			for(BlockPos evPos : sphere(pos, 4))
//            				level.setBlock(evPos, Blocks.SAND.defaultBlockState(), 2);
//            		}
//
//        	        SoundUtil.playSoundDistrib(level, pos, SoundEvents.BONE_BLOCK_PLACE, 1.0f, 1.0f, false, true);
//        	        
//                    pPlayer.awardStat(Stats.ITEM_USED.get(this));
//                    pPlayer.swing(pHand, true);
//                    
//                    return InteractionResultHolder.success(itemstack);
//                }
//            }
//            return InteractionResultHolder.consume(itemstack);
//        }
//    }
    
	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand)
	{
		ItemStack itemstack = pPlayer.getItemInHand(pHand);
		BlockHitResult blockhitresult = getPlayerPOVHitResult(pLevel, pPlayer, ClipContext.Fluid.NONE);
		if (blockhitresult.getType() == HitResult.Type.BLOCK)
		{
			if (pPlayer instanceof ServerPlayer sp)
				openGui(sp, itemstack, pHand);
			return InteractionResultHolder.pass(itemstack);
		}
		return InteractionResultHolder.consume(itemstack);
	}
    
    private void openGui(ServerPlayer player, ItemStack remote, InteractionHand hand)
    {
        if (!player.isCrouching())
        	player.openMenu(new ScaperContainerProvider());
		else
		{
//			Shape.getNextType(shapeMode.CIRCLE);
			player.displayClientMessage(Component.literal(shapeMode.name()), true);
		}
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
}
