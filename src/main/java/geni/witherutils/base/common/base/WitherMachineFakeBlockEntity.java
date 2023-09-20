package geni.witherutils.base.common.base;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.authlib.GameProfile;

import geni.witherutils.base.common.io.item.MultiSlotAccess;
import geni.witherutils.core.common.fakeplayer.WUTFakePlayer;
import geni.witherutils.core.common.sync.IntegerDataSlot;
import geni.witherutils.core.common.sync.SyncMode;
import geni.witherutils.core.common.util.ItemStackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;

@SuppressWarnings("unused")
public abstract class WitherMachineFakeBlockEntity extends WitherMachineBlockEntity {
    
	private static final UUID ID = UUID.randomUUID();
	protected GameProfile gameProfile;
	protected WeakReference<WUTFakePlayer> fakePlayer;
	protected boolean unrotatedYaw = false;
	
	protected int fakeStamina = 0;
	
	private final static ItemStack genericDigger = new ItemStack(Items.DIAMOND_PICKAXE, 1);
	private final static ItemStack genericRod = new ItemStack(Items.FISHING_ROD, 1);

    public WitherMachineFakeBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState)
    {
        super(type, worldPosition, blockState);
        addDataSlot(new IntegerDataSlot(this::getFakeStamina, p -> fakeStamina = p, SyncMode.WORLD));
    }
    
    protected WitherMachineFakeBlockEntity setUnrotatedYaw(boolean unRot)
	{
    	unrotatedYaw = unRot;
		return this;
	}
    
    @Nullable
	public FoodData getFoodData()
	{
		if(fakePlayer == null)
			return null;
		return fakePlayer.get().getFoodData();
	}
	
	public WeakReference<WUTFakePlayer> initFakePlayer(ServerLevel ws, String blockName, BlockEntity machineBe)
	{
		final String name = "fp." + blockName;
		final GameProfile profile = new GameProfile(ID, name);
		WeakReference<WUTFakePlayer> fakePlayer = new WeakReference<>(WUTFakePlayer.get(ws).get());
		if (fakePlayer == null || fakePlayer.get() == null)
		{
			fakePlayer = null;
			gameProfile = null;
			return null;
		}
		fakePlayer.get().setOnGround(true);
		fakePlayer.get().connection = new ServerGamePacketListenerImpl(ws.getServer(), new Connection(PacketFlow.SERVERBOUND), fakePlayer.get())
		{
			@Override
			public void send(Packet<?> packetIn) {}
		};
		fakePlayer.get().setSilent(false);
		gameProfile = profile;
		
		fakePlayer.get().setPosition(machineBe.getBlockPos().getX(), machineBe.getBlockPos().getY(), machineBe.getBlockPos().getZ());

		if(!unrotatedYaw)
			fakePlayer.get().setYRot(getYawFromFacing(getCurrentFacing()));
		
		return fakePlayer;
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
	
    public void setPlayer(Player player)
    {
        this.gameProfile = player.getGameProfile();
        this.setChanged();
    }
    
	public void tryEquipItem(ItemStack item, InteractionHand hand)
	{
		fakePlayer.get().setItemInHand(hand, item);
	}
	
	public static InteractionResult playerAttackBreakBlock(WeakReference<WUTFakePlayer> fakePlayer, Level world, BlockPos targetPos, InteractionHand hand, Direction facing) throws Exception
	{
		if (fakePlayer == null)
			return InteractionResult.FAIL;
		try
		{
			fakePlayer.get().gameMode.handleBlockBreakAction(targetPos, ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, facing, world.getMaxBuildHeight(), 0);
			return InteractionResult.SUCCESS;
		}
		catch (Exception e)
		{
			return InteractionResult.FAIL;
		}
	}
	
    public boolean placeBlock(Level world, BlockPos pos, ItemStack stack)
    {
    	fakePlayer.get().setItemInHand(InteractionHand.MAIN_HAND, stack);
        return ForgeHooks.onPlaceItemIntoWorld(new UseOnContext(fakePlayer.get(), InteractionHand.MAIN_HAND, new BlockHitResult(new Vec3(0, 0, 0), Direction.DOWN, pos, false))) == InteractionResult.SUCCESS;
    }
    
    public static InteractionResult interactUseOnBlock(WeakReference<WUTFakePlayer> fakePlayer, Level world, BlockPos targetPos, InteractionHand hand, Direction facing) throws Exception
    {
		if (fakePlayer == null)
			return InteractionResult.FAIL;
		Direction placementOn = (facing == null) ? fakePlayer.get().getMotionDirection() : facing;
		BlockHitResult blockraytraceresult = new BlockHitResult(fakePlayer.get().getLookAngle(), placementOn, targetPos, true);
		ItemStack itemInHand = fakePlayer.get().getItemInHand(hand);
		InteractionResult result = fakePlayer.get().gameMode.useItemOn(fakePlayer.get(), world, itemInHand, hand, blockraytraceresult);
		return result;
	}

	public void fakePlayerDumpInventory(WeakReference<WUTFakePlayer> fakePlayer, MultiSlotAccess outputs, boolean onGround)
	{
		ArrayList<ItemStack> toDrop = new ArrayList<ItemStack>();
		
		for (int i = outputs.get(0).getIndex(); i < fakePlayer.get().getInventory().items.size(); i++)
		{
			ItemStack fpItem = fakePlayer.get().getInventory().items.get(i);
			
			if (fpItem.isEmpty())
			{
				continue;
			}
			
			if (fpItem == fakePlayer.get().getMainHandItem())
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
				fakePlayer.get().getInventory().items.set(i, fpItem);
			}
			
		}
		if (onGround)
		{
			ItemStackUtil.drop(this.level, this.worldPosition.above(), toDrop);
		}
	}
	  
    @Override
    public void serverTick()
    {
    	if(fakePlayer != null)
            this.fakePlayer.get().getCooldowns().tick();
    	
        super.serverTick();
    }
    
    public abstract void resetFakeStamina();
    
	public int getFakeStamina()
	{
		return fakeStamina;
	}
	
	public void attackTargetEntityWithCurrentItem(@Nonnull Entity targetEntity)
	{
		fakePlayer.get().setOnGround(true);
		faceEntity(targetEntity);
	}
	public void faceEntity(Entity entityIn)
	{
		double d0 = entityIn.getX() - fakePlayer.get().position().x;
		double d2 = entityIn.getZ() - fakePlayer.get().position().z;
		double d1;

		if (entityIn instanceof LivingEntity)
		{
			LivingEntity entitylivingbase = (LivingEntity) entityIn;
			d1 = entitylivingbase.position().y + entitylivingbase.getEyeHeight() - (fakePlayer.get().position().y + fakePlayer.get().getEyeHeight());
		}
		else
			d1 = (entityIn.getBoundingBox().minY + entityIn.getBoundingBox().maxY) / 2.0D - (fakePlayer.get().position().y + fakePlayer.get().getEyeHeight());

		double d3 = Math.sqrt(d0 * d0 + d2 * d2);
		fakePlayer.get().xRotO = Mth.wrapDegrees((float) (-(Math.atan2(d1, d3) * (180D / Math.PI))));
		fakePlayer.get().yRotO = Mth.wrapDegrees((float) (Math.atan2(d2, d0) * (180D / Math.PI)) - 90.0F);
	}
}