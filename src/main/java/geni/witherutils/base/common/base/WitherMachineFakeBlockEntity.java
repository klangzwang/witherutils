package geni.witherutils.base.common.base;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.authlib.GameProfile;

import geni.witherutils.base.common.io.item.MultiSlotAccess;
import geni.witherutils.core.common.fakeplayer.FakePlayerNotification;
import geni.witherutils.core.common.fakeplayer.WUTFakePlayer;
import geni.witherutils.core.common.helper.MathHelper;
import geni.witherutils.core.common.math.Vector3;
import geni.witherutils.core.common.util.ItemStackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;

public abstract class WitherMachineFakeBlockEntity extends WitherMachineBlockEntity {

	private static final UUID ID = UUID.randomUUID();
	protected WUTFakePlayer fakePlayer;
	protected GameProfile gameProfile;
	
	protected boolean unrotatedYaw = false;
	
//	private final static ItemStack genericDigger = new ItemStack(Items.DIAMOND_PICKAXE, 1);
//	private final static ItemStack genericRod = new ItemStack(Items.FISHING_ROD, 1);
	
    public boolean fakePlayerReady;
    private boolean canInteract;
    
    private boolean fakePlayerVisible = true;
	
    public int interactTimer;
    public int interactTimerMax = -1;
    
    /*
     * 
     * RENDER
     * 
     */
    public float lookYaw = 0;
    public float lookPitch = (float) Math.PI / 2;
    public float lastLookYaw = 0;
    public float lastLookPitch = 0;
    
    private final @Nonnull Set<FakePlayerNotification> notification = EnumSet.noneOf(FakePlayerNotification.class);
    
	public WitherMachineFakeBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState)
	{
		super(type, worldPosition, blockState);
	}

	public WUTFakePlayer initFakePlayer(ServerLevel ws, String blockName, @Nullable BlockEntity machineBe)
	{
		final String name = "fp." + blockName;
		final GameProfile profile = new GameProfile(ID, name);
		
		fakePlayer = WUTFakePlayer.setupFakePlayer(ws);
		fakePlayer.setEmulatingUUID(getOwnerUUID());

		if (fakePlayer == null)
		{
			fakePlayer = null;
			gameProfile = null;
			return null;
		}
		
		fakePlayer.setOnGround(true);
		fakePlayer.setSilent(false);
		gameProfile = profile;
		fakePlayer.gameMode.setLevel((ServerLevel) level);
		
		if(machineBe != null)
			fakePlayer.setPos(machineBe.getBlockPos().getX(), machineBe.getBlockPos().getY(), machineBe.getBlockPos().getZ());

		if(!unrotatedYaw)
			fakePlayer.setYRot(getYawFromFacing(getCurrentFacing()));

		return fakePlayer;
	}
    
	public void disableFakePlayer()
	{
		if(fakePlayer == null)
			return;
		
		gameProfile = null;
		interactTimer = -1;
		setInteractTimerMax(interactTimerMax);
		fakePlayerReady = false;
		fakePlayer.setItemInHand(fakePlayer.getUsedItemHand(), ItemStack.EMPTY);
		fakePlayer = null;
	}
	
	/*
	 * 
	 * TICK / REACTTIMER
	 * 
	 */
    @Override
    public void serverTick()
    {
        super.serverTick();

    	if(fakePlayer != null && this.interactTimerMax > 0)
    	{
            this.fakePlayer.getCooldowns().tick();

            if(this.interactTimer-- >= 0)
            {
            	this.canInteract = false;
            	return;
            }
            else
            {
           		this.canInteract = true;
            	this.interactTimer = this.interactTimerMax;
            }
    	}
    }
	
    @Override
    public void clientTick()
    {
    	super.clientTick();
    	
    	if(fakePlayerVisible)
    	{
            updateAnimation();
            
    		if (!fakePlayerReady)
    		{
    			setSingleNotification(FakePlayerNotification.NOT_READY);
    		}
    		else
    		{
    			removeNotification(FakePlayerNotification.NOT_READY);

    			setSingleNotification(FakePlayerNotification.READY);
    		}
    	}
    }
    
	public int getInteractTimer()
	{
		return this.interactTimer;
	}
	public int getInteractTimerMax()
	{
		return this.interactTimerMax;
	}
	public void setInteractTimerMax(int newInteractTimeMax)
	{
		this.interactTimerMax = newInteractTimeMax;
		this.interactTimer = this.interactTimerMax;
	}
	public boolean canInteract()
	{
		return canInteract;
	}
	
	public boolean isFakePlayerVisible()
	{
		return fakePlayerVisible;
		
	}
    public void setFakePlayerVisible(boolean visible)
    {
        this.fakePlayerVisible = visible;
    }

	public float getYawFromFacing(Direction currentFacing)
	{
		switch (currentFacing)
		{
			case DOWN:
			case UP:
			case SOUTH:
			default:
				return 0;
			case EAST:
				return 270F;
			case NORTH:
				return 180F;
			case WEST:
				return 90F;
		}
	}
    
	public WUTFakePlayer getFakePlayer()
	{
		return fakePlayer;
		
	}
    public void setPlayer(Player player)
    {
        this.gameProfile = player.getGameProfile();
        this.setChanged();
    }
    
	public void tryEquipItem(ItemStack item, InteractionHand hand)
	{
		fakePlayer.setItemInHand(hand, item);
	}
	
	public static InteractionResult playerAttackBreakBlock(WUTFakePlayer fakePlayer, Level world, BlockPos targetPos, InteractionHand hand, Direction facing) throws Exception
	{
		if (fakePlayer == null)
			return InteractionResult.FAIL;
		try
		{
//			fakePlayer.gameMode.handleBlockBreakAction(targetPos, ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, facing, world.getMaxBuildHeight(), 0);
			return InteractionResult.SUCCESS;
		}
		catch (Exception e)
		{
			return InteractionResult.FAIL;
		}
	}

    public static InteractionResult interactUseOnBlock(WUTFakePlayer fakePlayer, Level world, BlockPos targetPos, InteractionHand hand, Direction facing) throws Exception
    {
		if (fakePlayer == null)
			return InteractionResult.FAIL;
		Direction placementOn = (facing == null) ? fakePlayer.getMotionDirection() : facing;
		BlockHitResult blockraytraceresult = new BlockHitResult(fakePlayer.getLookAngle(), placementOn, targetPos, true);
		ItemStack itemInHand = fakePlayer.getItemInHand(hand);
		InteractionResult result = fakePlayer.gameMode.useItemOn(fakePlayer, world, itemInHand, hand, blockraytraceresult);
		return result;
	}

	public void fakePlayerDumpInventory(WUTFakePlayer fakePlayer, MultiSlotAccess outputs, boolean onGround)
	{
		ArrayList<ItemStack> toDrop = new ArrayList<ItemStack>();
		
		for (int i = outputs.get(0).getIndex(); i < fakePlayer.getInventory().items.size(); i++)
		{
			ItemStack fpItem = fakePlayer.getInventory().items.get(i);
			
			if (fpItem.isEmpty())
			{
				continue;
			}
			
			if (fpItem == fakePlayer.getMainHandItem())
			{
				continue;
			}
			
			for (int j = 0; j < outputs.getAccesses().size(); j++)
			{
				fpItem = outputs.get(j).insertItem(getInventory(), fpItem, false);
			}
			
			if (onGround)
			{
				toDrop.add(fpItem);
			}
			
			else
			{
				fakePlayer.getInventory().items.set(i, fpItem);
			}
			
		}
		if (onGround)
		{
			ItemStackUtil.drop(this.level, this.worldPosition.above(), toDrop);
		}
	}

	public void attackTargetEntityWithCurrentItem(@Nonnull Entity targetEntity)
	{
		fakePlayer.setOnGround(true);
		faceEntity(targetEntity);
	}
	public void faceEntity(Entity entityIn)
	{
		double d0 = entityIn.getX() - fakePlayer.position().x;
		double d2 = entityIn.getZ() - fakePlayer.position().z;
		double d1;

		if (entityIn instanceof LivingEntity)
		{
			LivingEntity entitylivingbase = (LivingEntity) entityIn;
			d1 = entitylivingbase.position().y + entitylivingbase.getEyeHeight() - (fakePlayer.position().y + fakePlayer.getEyeHeight());
		}
		else
			d1 = (entityIn.getBoundingBox().minY + entityIn.getBoundingBox().maxY) / 2.0D - (fakePlayer.position().y + fakePlayer.getEyeHeight());

		double d3 = Math.sqrt(d0 * d0 + d2 * d2);
		fakePlayer.xRotO = Mth.wrapDegrees((float) (-(Math.atan2(d1, d3) * (180D / Math.PI))));
		fakePlayer.yRotO = Mth.wrapDegrees((float) (Math.atan2(d2, d0) * (180D / Math.PI)) - 90.0F);
	}
	
    protected boolean isFakePlayerReady()
    {
        return fakePlayerReady;
    }

    public void setFakePlayerReady(boolean ready)
    {
    	fakePlayerReady = ready;
    }

    public void copyFromFakePlayer()
    {
        if (!fakePlayerReady)
        	return;

        Inventory fakeInv = getFakePlayer().getInventory();
        for (int slot = 1; slot < fakeInv.getContainerSize(); slot++)
        {
            ItemStack stack = fakeInv.getItem(slot);
            if (!stack.isEmpty())
            {
//                Vec3 v = getFakePlayer().position();
//              PneumaticCraftUtils.dropItemOnGround(stack, level, v.x(), v.y(), v.z());
                fakeInv.setItem(slot, ItemStack.EMPTY);
            }
        }
    }
    
    public void withFakePlayer(ServerLevel level, double x, double y, double z, BlockPos hitPos, BlockState hitState, Direction hitSide)
    {
        WUTFakePlayer faker = WUTFakePlayer.setupFakePlayer(level, x, y, z);
        faker.setEmulatingUUID(getOwnerUUID());
        BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(level, hitPos, hitState, faker);
        if (!NeoForge.EVENT_BUS.post(event).isCanceled())
        {
            if (hitState.getBlock() instanceof TntBlock && hitState.isFlammable(level, hitPos, hitSide))
            {
                hitState.onCaughtFire(level, hitPos, hitSide, null);
                level.removeBlock(hitPos, false);
            }
            else
            {
//                handleBreakBlock(hitState, level, hitPos, faker, ItemAtomicDisassembler.fullyChargedStack());
            }
        }
        faker.cleanupFakePlayer(level);
    }
    
//    private boolean canMine(BlockState state, BlockPos pos)
//    {
//    	WUTFakePlayer faker = WUTFakePlayer.setupFakePlayer((ServerLevel) level, this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ());
//    	faker.setEmulatingUUID(getOwnerUUID());//pretend to be the owner
//        boolean canMine = !NeoForge.EVENT_BUS.post(new BlockEvent.BreakEvent(level, pos, state, faker)).isCanceled();
//        faker.cleanupFakePlayer((ServerLevel) level);
//        return canMine;
//    }

//    private BlockState getStateForPlacement(ItemStack stack, BlockPos pos)
//    {
//    	WUTFakePlayer dummy = WUTFakePlayer.setupFakePlayer((ServerLevel) level, this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ());
//        dummy.setEmulatingUUID(getOwnerUUID());//pretend to be the owner
//        BlockState result = StackUtils.getStateForPlacement(stack, pos, dummy);
//        dummy.cleanupFakePlayer((ServerLevel) level);
//        return result;
//    }
    
//    private List<ItemStack> getDrops(ServerLevel level, BlockState state, BlockPos pos)
//    {
//        if (state.isAir())
//        {
//            return Collections.emptyList();
//        }
//
//        WUTFakePlayer dummy = WUTFakePlayer.setupFakePlayer(level, this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ());
//        dummy.setEmulatingUUID(getOwnerUUID());
//        List<ItemStack> drops = WorldUtils.getDrops(state, level, pos, WorldUtils.getTileEntity(level, pos), dummy, stack);
//        dummy.cleanupFakePlayer(level);
//        return drops;
//    }
    
    @SuppressWarnings("static-access")
	public UUID getOwnerUUID()
    {
    	return this.ID;
    }
    
    /*
     * 
     * RENDER 
     * 
     */
    @OnlyIn(Dist.CLIENT)
    private void updateAnimation()
    {
        List<Entity> entities = level.getEntitiesOfClass(Entity.class, new AABB(worldPosition.offset(1, 1, 1)).inflate(10, 10, 10));
        Entity closest = null;
        double closestDist = -1;

        Vec3 posVec = new Vec3(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ());
        for (Entity entity : entities)
        {
            if (closest == null)
            {
                closest = entity;
                closestDist = entity.distanceToSqr(posVec);
            }
            else if (entity.distanceToSqr(posVec) < closestDist)
            {
                closest = entity;
                closestDist = entity.distanceToSqr(posVec);
            }
        }

        lastLookYaw = lookYaw;
        lastLookPitch = lookPitch;

        if (closest != null)
        {
            Vector3 closePos = new Vector3(closest.getEyePosition());
            Vector3 relative = closePos.copy().subtract(Vector3.fromBlockPosCenter(getBlockPos()));
            double dist = closePos.distance(Vector3.fromBlockPosCenter(getBlockPos()));
            float targetYaw = (float) (Mth.atan2(relative.x, relative.z) * MathHelper.todeg) + 180;
            float deviation = targetYaw - lookYaw;

            if (deviation < -180)
            {
                lookYaw -= 360;
                lastLookYaw -= 360;
            }
            else if (deviation > 180)
            {
                lookYaw += 360;
                lastLookYaw += 360;
            }
            lookYaw += (targetYaw - lookYaw) * 0.2;
            float pitchAngle = (float) (Mth.atan2(relative.y, dist) * MathHelper.todeg);
            lookPitch += (pitchAngle - lookPitch) * 0.2;
        }
        else
        {
            lookYaw += 1.15;
            if (lookYaw >= 360)
            {
                lookYaw -= 360;
                lastLookYaw -= 360;
            }
            if (lookPitch % 360 > 0)
            {
                lookPitch -= 1.15;
            }
            else if (lookPitch % 360 < 0)
            {
                lookPitch += 1.15;
            }
        }
    }
    
	/*
	 * 
	 * NOTIFICATION
	 * 
	 */
	public @Nonnull Set<FakePlayerNotification> getNotification()
	{
	    return notification;
	};

	public void setSingleNotification(@Nonnull FakePlayerNotification note)
	{
		setNotification(note);
		for (Iterator<FakePlayerNotification> itr = notification.iterator(); itr.hasNext();)
		{
			if (itr.next() != note)
			{
				itr.remove();
			}
		}
	}
	public void setNotification(@Nonnull FakePlayerNotification note)
	{
		if (!notification.contains(note))
		{
			notification.add(note);
		}
	}
	
	public void removeNotification(FakePlayerNotification note)
	{
		getNotification().remove(note);
	}

	public void clearNotification(boolean all)
	{
		if (hasNotification())
		{
			if (all)
			{
				getNotification().clear();
			}
			else
			{
				for (Iterator<FakePlayerNotification> itr = notification.iterator(); itr.hasNext();)
				{
					if (itr.next().isAutoCleanup())
					{
						itr.remove();
					}
				}
			}
		}
	}

	public boolean hasNotification()
	{
		return !getNotification().isEmpty();
	}
}
