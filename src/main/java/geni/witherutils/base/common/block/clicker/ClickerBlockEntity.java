package geni.witherutils.base.common.block.clicker;

import java.util.List;
import java.util.function.Consumer;

import geni.witherutils.base.common.base.WitherMachineFakeBlockEntity;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.base.common.io.item.MachineInventory;
import geni.witherutils.base.common.io.item.MachineInventoryLayout;
import geni.witherutils.base.common.io.item.SingleSlotAccess;
import geni.witherutils.core.common.sync.BooleanDataSlot;
import geni.witherutils.core.common.sync.IntegerDataSlot;
import geni.witherutils.core.common.sync.SyncMode;
import geni.witherutils.core.common.util.FakePlayerUtil;
import geni.witherutils.core.common.util.ItemStackUtil;
import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

public class ClickerBlockEntity extends WitherMachineFakeBlockEntity implements Consumer<ItemStack>, MenuProvider {

	public static final SingleSlotAccess INPUT = new SingleSlotAccess();
	
	int speed = 0;
	int power = 0;
	int timer = 0;
	
	boolean rightClick = false;
	boolean sneak = false;
	
	private ItemStack tool = new ItemStack(Items.STICK);
	
	public ClickerBlockEntity(BlockPos pos, BlockState state)
	{
		super(WUTEntities.CLICKER.get(), pos, state);
		addDataSlot(new IntegerDataSlot(this::getTimer, p -> timer = p, SyncMode.GUI));
		add2WayDataSlot(new IntegerDataSlot(this::getPower, this::setPower, SyncMode.GUI));
		add2WayDataSlot(new IntegerDataSlot(this::getSpeed, this::setSpeed, SyncMode.GUI));
		add2WayDataSlot(new BooleanDataSlot(this::getRightClick, this::setRightClick, SyncMode.GUI));
		add2WayDataSlot(new BooleanDataSlot(this::getSneak, this::setSneak, SyncMode.GUI));
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
    protected MachineInventory createMachineInventory(MachineInventoryLayout layout)
    {
        return new MachineInventory(getIOConfig(), layout)
        {
        	@Override
        	protected void onContentsChanged(int slot)
        	{
            	if(slot == 0)
	      		{
	      			tool = null;
		      		ClickerBlockEntity.this.setChanged();
        			level.playLocalSound(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(),
        					WUTSounds.PICKSHEE.get(), SoundSource.BLOCKS, 0.5f, 1.0f, false);
	      		}
                onInventoryContentsChanged(slot);
        		setChanged();
        	}
//        	@Override
//        	public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
//        	{
//    			if(stack.getItem() instanceof BlockItem)
//    				return stack;
//   				return super.insertItem(slot, stack, simulate);
//        	}
        };
    }
    
	@Override
	public void serverTick()
	{
		super.serverTick();
		
    	if(fakePlayer == null)
    		this.fakePlayer = initFakePlayer((ServerLevel) level,
    				ForgeRegistries.BLOCKS.getKey(getBlockState().getBlock()).getPath(), this);
		
		Direction facing = level.getBlockState(this.worldPosition).getValue(BlockStateProperties.FACING);
		BlockPos target = worldPosition.relative(getCurrentFacing().getOpposite());
		
		AABB AABB = new AABB(target);
		List<LivingEntity> living = this.level.getEntitiesOfClass(LivingEntity.class, AABB);
		
		if(!rightClick)
		{
		    if(living.size() == 0)
		    {
		    	setLitProperty(false);
		    	timer = 100;
		        return;
		    }
		}

	    if(speed <= 0 && power > 0)
	    {
	    	timer = 0;
	    	setLitProperty(true);
	    	return;
	    }
	    
		timer -= speed;
		
		if(timer > 0)
		{
			setLitProperty(false);
			return;
		}
		else
		{
			setLitProperty(true);
			timer = 100;
		}

		if(fakePlayer.get() != null)
		{
			tool = getInventory().getStackInSlot(0).copy();

			try
			{
				var cooldowns = fakePlayer.get().getCooldowns();
				tryEquipItem(tool, InteractionHand.MAIN_HAND);
				var item = fakePlayer.get().getItemInHand(InteractionHand.MAIN_HAND).getItem();
				if (cooldowns.isOnCooldown(item))
					cooldowns.removeCooldown(item);
			}
			catch (Exception e) {}

			if(rightClick)
			{
				try
				{
					ItemStack result = INPUT.getItemStack(inventory);
					if(result.isEmpty())
						return;

					if(result.getItem() instanceof BlockItem)
					{
						BlockEntity be = level.getBlockEntity(target);
						if(be != null)
						{
							IItemHandler itemHandler =  be.getCapability(ForgeCapabilities.ITEM_HANDLER, getCurrentFacing()).resolve().get();
							if(itemHandler != null)
							{
								ItemStack stack = inventory.extractItem(0, 1, false).split(1);
								ItemHandlerHelper.insertItem(itemHandler, stack, false);
							}
						}
					}
					else if(result.getItem() instanceof Item)
					{
						FakePlayerUtil.setupFakePlayerForUse(fakePlayer.get(), this.worldPosition, getCurrentFacing().getOpposite(), tool.copy(), sneak);
						result = FakePlayerUtil.rightClickInDirection(fakePlayer.get(), this.level, this.worldPosition, getCurrentFacing().getOpposite(), level.getBlockState(this.worldPosition));
					}
					
					this.extinguishFires(target, facing);
					
					this.level.getEntities(fakePlayer.get(), AABB, EntitySelector.NO_SPECTATORS).forEach((entityFound) -> {

				        InteractionResult res = fakePlayer.get().interactOn(entityFound, InteractionHand.MAIN_HAND);
				        if (res.consumesAction())
				        {
//				        	WitherUtils.LOGGER.info(worldPosition + "| entity consume result detected " + res);
				        }
					});

                    FakePlayerUtil.cleanupFakePlayerFromUse(fakePlayer.get(), result, INPUT.getItemStack(inventory), this);
                    this.setChanged();
				}
				catch(Exception e)
				{
				}
			}
			else
			{
				try
				{
					FakePlayerUtil.setupFakePlayerForUse(this.fakePlayer.get(), this.worldPosition, getCurrentFacing(), tool.copy(), sneak);
					ItemStack result = INPUT.getItemStack(inventory);
					result = FakePlayerUtil.leftClickInDirection(this.fakePlayer.get(), this.level, this.worldPosition, getCurrentFacing(), level.getBlockState(this.worldPosition));
					
					this.level.getEntities(fakePlayer.get(), AABB, EntitySelector.NO_SPECTATORS).forEach((entityFound) -> {
						
//						WitherUtils.LOGGER.info(worldPosition + "| ??   " + fakePlayer.get().getMainHandItem());
//						WitherUtils.LOGGER.info(worldPosition + "| interactEntities ATTACK  " + entityFound);
						
						fakePlayer.get().attack(entityFound);
					});
					
					ItemStackUtil.damageItem(getInventory().getStackInSlot(0));
					
                    FakePlayerUtil.cleanupFakePlayerFromUse(fakePlayer.get(), result, INPUT.getItemStack(inventory), this);
                    this.setChanged();
				}
				catch(Exception e)
				{
				}
			}
			
			fakePlayer.get().setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
		}
	}
	
	public InteractionResult interactAtBlock(Block block, InteractionHand hand)
	{
		return InteractionResult.PASS;
	}
	
	@Override
	public void clientTick()
	{
		super.clientTick();
	}
	
	@Override
	public void load(CompoundTag tag)
	{
		power = tag.getInt("power");
		speed = tag.getInt("speed");
		sneak = tag.getBoolean("sneak");
		rightClick = tag.getBoolean("rightClick");
		super.load(tag);
	}
	@Override
	public void saveAdditional(CompoundTag tag)
	{
		tag.putInt("power", power);
		tag.putInt("speed", speed);
		tag.putBoolean("sneak", sneak);
		tag.putBoolean("rightClick", rightClick);
		super.saveAdditional(tag);
	}

	public int getTimer()
	{
		return timer;
	}
	public int getPower()
	{
		return power;
	}
	public void setPower(int power)
	{
		this.power = power;
        this.setChanged();
	}
	public boolean getRightClick()
	{
		return rightClick;
	}
	public void setRightClick(boolean rightClick)
	{
		this.rightClick = rightClick;
        this.setChanged();
	}
	public boolean getSneak()
	{
		return sneak;
	}
	public void setSneak(boolean sneak)
	{
		this.sneak = sneak;
        this.setChanged();
	}
	public int getSpeed()
	{
		return speed;
	}
	public void setSpeed(int speed)
	{
		this.speed = speed;
        this.setChanged();
	}
	public boolean isCrouching()
	{
		return sneak;
	}
	public void setCrouching(boolean sneak)
	{
		this.sneak = sneak;
		setChanged();
	}
	public boolean isRightClicking()
	{
		return rightClick;
	}
	public void setRightClicking(boolean rightClick)
	{
		this.rightClick = rightClick;
		setChanged();
	}

	private void extinguishFires(BlockPos pos, Direction facing)
	{
		BlockState blockstate = this.level.getBlockState(pos);
		if (blockstate.is(BlockTags.FIRE))
		{
			this.level.removeBlock(pos, false);
		}
		else if(CampfireBlock.isLitCampfire(blockstate))
		{
			CampfireBlock.isLitCampfire(blockstate);
			this.level.setBlock(pos, blockstate.setValue(CampfireBlock.LIT, Boolean.valueOf(false)), 3);
		}
	}

    @Override
    public MachineInventoryLayout getInventoryLayout()
    {
        return MachineInventoryLayout
            .builder()
            .inputSlot().slotAccess(INPUT)
            .build();
    }
    
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity)
    {
        return new ClickerContainer(this, playerInventory, i);
    }
    
	@Override
	public void accept(ItemStack t)
	{
		INPUT.getItemStack(inventory);
	}
    
	@Override
	public void resetFakeStamina()
	{
	}
}
