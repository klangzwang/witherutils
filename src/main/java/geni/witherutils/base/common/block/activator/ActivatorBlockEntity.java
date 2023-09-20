package geni.witherutils.base.common.block.activator;

import geni.witherutils.base.common.base.WitherMachineFakeBlockEntity;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.core.common.sync.IntegerDataSlot;
import geni.witherutils.core.common.sync.SyncMode;
import geni.witherutils.core.common.util.McTimerUtil;
import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

public class ActivatorBlockEntity extends WitherMachineFakeBlockEntity implements MenuProvider {

	int speed = 0;
	int power = 0;

	public ActivatorBlockEntity(BlockPos pos, BlockState state)
	{
		super(WUTEntities.ACTIVATOR.get(), pos, state);
		add2WayDataSlot(new IntegerDataSlot(this::getPower, this::setPower, SyncMode.GUI));
		add2WayDataSlot(new IntegerDataSlot(this::getSpeed, this::setSpeed, SyncMode.GUI));
	}
	
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if(!world.isClientSide)
        {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if(tileEntity instanceof MenuProvider)
            {
                NetworkHooks.openScreen((ServerPlayer) player, (MenuProvider) tileEntity, tileEntity.getBlockPos());
                SoundUtil.playSoundFromServer((ServerPlayer) player, WUTSounds.BUCKET.get(), 0.05f, 1.0f);
            }
            else
            {
                throw new IllegalStateException("Our named container provider is missing!");
            }
        }
        return InteractionResult.SUCCESS;
    }
    
	@Override
	public void serverTick()
	{
		super.serverTick();

    	if(fakePlayer == null)
    		this.fakePlayer = initFakePlayer((ServerLevel) level,
    				ForgeRegistries.BLOCKS.getKey(getBlockState().getBlock()).getPath(), this);
		
        Direction facing = level.getBlockState(this.worldPosition).getValue(BlockStateProperties.FACING);
		BlockPos targetpos = worldPosition.relative(facing.getOpposite());
		BlockState state = level.getBlockState(targetpos);
		
		float time = level.getLevelData().getGameTime() + McTimerUtil.renderPartialTickTime;
		double react = Math.sin(time * speed / 60.0D) / 10.0D;
		
	    if(state.isAir())
	    {
	    	setLitProperty(false);
	        return;
	    }
	    if((float) react < -0.09 || (float) react > 0.09)
	    	setLitProperty(true);
	    else
	    	setLitProperty(false);
	}

	@Override
	public void clientTick()
	{
		super.clientTick();
	}
	
	@Override
	public void load(CompoundTag tag)
	{
		speed = tag.getInt("speed");
		power = tag.getInt("power");
		super.load(tag);
	}
	@Override
	public void saveAdditional(CompoundTag tag)
	{
		tag.putInt("speed", speed);
		tag.putInt("power", power);
		super.saveAdditional(tag);
	}

	public int getSpeed()
	{
		return speed;
	}
	public void setSpeed(int speed)
	{
		this.speed = speed;
	}
	public int getPower()
	{
		return power;
	}
	public void setPower(int power)
	{
		this.power = power;
	}

    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity)
    {
        return new ActivatorContainer(this, playerInventory, i);
    }
    
	@Override
	public void resetFakeStamina()
	{
	}
}
