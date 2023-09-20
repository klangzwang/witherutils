package geni.witherutils.base.common.block.miner.basic;

import java.util.LinkedList;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import geni.witherutils.api.io.energy.EnergyIOMode;
import geni.witherutils.api.soulbank.QuadraticScalable;
import geni.witherutils.api.soulbank.SoulBankModifier;
import geni.witherutils.base.client.sound.MachineSoundController;
import geni.witherutils.base.common.base.WitherMachineEnergyFakeBlockEntity;
import geni.witherutils.base.common.config.common.BlocksConfig;
import geni.witherutils.base.common.config.common.FakePlayerConfig;
import geni.witherutils.base.common.event.WitherItemCaptureEvents;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.base.common.init.WUTParticles;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.base.common.init.WUTTags;
import geni.witherutils.base.common.io.energy.WitherEnergyStorage;
import geni.witherutils.base.common.io.item.MachineInventory;
import geni.witherutils.base.common.io.item.MachineInventoryLayout;
import geni.witherutils.base.common.io.item.SingleSlotAccess;
import geni.witherutils.core.common.network.CoreNetwork;
import geni.witherutils.core.common.network.PacketStartupShutdown;
import geni.witherutils.core.common.particle.IntParticleType;
import geni.witherutils.core.common.sync.BooleanDataSlot;
import geni.witherutils.core.common.sync.SyncMode;
import geni.witherutils.core.common.util.FakePlayerUtil;
import geni.witherutils.core.common.util.ItemStackUtil;
import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.registries.ForgeRegistries;

public class MinerBasicBlockEntity extends WitherMachineEnergyFakeBlockEntity implements MenuProvider {

    public static final SingleSlotAccess INPUTTOOL = new SingleSlotAccess();

    public static final QuadraticScalable CAPACITY = new QuadraticScalable(SoulBankModifier.ENERGY_CAPACITY, () -> BlocksConfig.MINERMAXENERGY.get());
    public static final QuadraticScalable TRANSFER = new QuadraticScalable(SoulBankModifier.ENERGY_TRANSFER, () -> BlocksConfig.MINERSENDPERTICK.get());
    public static final QuadraticScalable USAGE = new QuadraticScalable(SoulBankModifier.ENERGY_USE, () -> BlocksConfig.MINERUSEENERGY.get());
	
    private float maxSpeed = 30F;
    private float acceleration = 0.5F;
    
    @OnlyIn(Dist.CLIENT)
    public boolean soulBankInstalled;
    @OnlyIn(Dist.CLIENT)
    public float prevFanRotation;
    @OnlyIn(Dist.CLIENT)
    public float fanRotation;
    @OnlyIn(Dist.CLIENT)
    private float currentSpeed;
    
	private final static ItemStack genericDigger = new ItemStack(Items.DIAMOND_PICKAXE, 1);
	private ItemStack tool;
	
    public MinerBasicBlockEntity(BlockPos worldPosition, BlockState blockState)
    {
        super(EnergyIOMode.Input, CAPACITY, TRANSFER, USAGE, WUTEntities.MINERBASIC.get(), worldPosition, blockState);
        addDataSlot(new BooleanDataSlot(this::isSoulBankInstalled, p -> soulBankInstalled = p, SyncMode.WORLD));
    }
    
    @Override
    protected MachineInventory createMachineInventory(MachineInventoryLayout layout)
    {
        return new MachineInventory(getIOConfig(), layout)
        {
            @NotNull
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
            {
                if(slot == 0)
                {
                	if(!(stack.getItem() instanceof PickaxeItem))
                	{
                        return stack;
                	}
                	else
                	{
        	      		SoundUtil.playSlotSound(level, worldPosition, SoundEvents.ARMOR_EQUIP_IRON, 1.0f, 2.0f);
        	      		return super.insertItem(slot, stack, simulate);
                	}
                }
            	if(slot == getInventory().getLayout().getSoulBankSlot())
            	{
                	if(stack.getItem() != WUTItems.SOULBANK_BASIC.get())
                	{
                        return stack;
                	}
                	else
                	{
                		SoundUtil.playSlotSound(level, worldPosition, WUTSounds.REACT.get(), 0.4f, 1.0f);
        	      		return super.insertItem(slot, stack, simulate);
                	}
            	}
	      		return super.insertItem(slot, stack, simulate);
            }
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate)
            {
            	if(slot == getInventory().getLayout().getSoulBankSlot() || slot == 0)
            		SoundUtil.playSlotSound(level, worldPosition, SoundEvents.CHISELED_BOOKSHELF_PICKUP, 0.4f, 1.0f);
	      		return super.extractItem(slot, amount, simulate);
            }
            @Override
	      	protected void onContentsChanged(int slot)
	      	{
                onInventoryContentsChanged(slot);
                setChanged();
	      	}
        };
    }
    @Override
    protected WitherEnergyStorage createEnergyStorage(EnergyIOMode energyIOMode, Supplier<Integer> capacity, Supplier<Integer> transferRate, Supplier<Integer> usageRate)
    {
        return new WitherEnergyStorage(getIOConfig(), EnergyIOMode.Input, capacity, transferRate, usageRate)
        {
            @Override
            public LazyOptional<IEnergyStorage> getCapability(@Nullable Direction side)
            {
            	if(side == getCurrentFacing())
            		return LazyOptional.empty();
            	else
            		return super.getCapability(side);
            }
        };
    }
    
    @Override
    public void serverTick()
    {
        super.serverTick();

        if(fakePlayer == null)
        {
    		this.fakePlayer = initFakePlayer((ServerLevel) level, ForgeRegistries.BLOCKS.getKey(getBlockState().getBlock()).getPath(), this);
        	return;
        }

        if(!canAct() || energyStorage.getEnergyStored() < BlocksConfig.MINERUSEENERGY.get() || INPUTTOOL.getItemStack(inventory).isEmpty())
        {
        	setLitProperty(false);
        	resetFakeStamina();
	        return;
        }
    	
		if(tool == null)
			tool = genericDigger.copy();

		BlockPos target = worldPosition.relative(getCurrentFacing());
		setLitProperty(getCanInteract());
		
        if(getCanInteract())
        {
            if(isBreakable(target))
            {
        		if(fakeStamina < FakePlayerConfig.FAKEPLAYERSTAMINA.get())
        		{
        			if(level.random.nextFloat() < 0.25F)
        			{
        				level.levelEvent(2001, target, Block.getId(level.getBlockState(target).getBlock().defaultBlockState()));
        				level.destroyBlockProgress(0, target, Math.min(fakeStamina / 20, 10));
        			}
        			fakeStamina += getSoulBankData().getBase();
        			return;
        		}
        		level.destroyBlockProgress(0, target, 0);            		
        		resetFakeStamina();
            	
    			FakePlayerUtil.setupFakePlayerForUse(fakePlayer.get(), this.worldPosition, getCurrentFacing(), tool.copy(), false);

    			WitherItemCaptureEvents.startCapturing();
    			
    			fakePlayer.get().gameMode.destroyBlock(target);
    			ItemStackUtil.damageItem(INPUTTOOL.getItemStack(inventory));
    			energyStorage.takeEnergy(BlocksConfig.MINERUSEENERGY.get());
            	
    			LinkedList<ItemStack> stacks = WitherItemCaptureEvents.stopCapturing();
        		for(ItemStack stack : stacks)
        			ItemStackUtil.drop(this.level, this.worldPosition.above(), stack);
        		
    			getFakePlayer().setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            }
        }
        else
        	level.destroyBlockProgress(0, target, 0);
    }
    
    @Override
    public void clientTick()
    {
        super.clientTick();
        
        if(!soulBankInstalled)
	    	return;

   		BlockPos target = worldPosition.relative(getCurrentFacing());
		BlockState state = level.getBlockState(target);
        
        prevFanRotation = fanRotation;
        if(!state.isAir() && getRedstoneControl().isActive(level.hasNeighborSignal(worldPosition)))
        {
        	handleActivate();
            currentSpeed += acceleration + getSoulBankData().getBase() * 2;
            
            if(currentSpeed > maxSpeed)
            {
                currentSpeed = maxSpeed;
                setCanInteract(true);
            }

            level.addAlwaysVisibleParticle(new IntParticleType.IntParticleData(WUTParticles.BLACKSMOKE.get(), 50, 50, 50, 3*(int)currentSpeed),
            		(double)worldPosition.getX() + 0.5D + level.random.nextDouble() / 15.0D * (double)(level.random.nextBoolean() ? 1 : -1),
            		(double)worldPosition.getY() + 1.0D,
            		(double)worldPosition.getZ() + 0.5D + level.random.nextDouble() / 15.0D * (double)(level.random.nextBoolean() ? 1 : -1),
            		0.0D, 0.0D, 0.0D);
            
            level.addParticle(ParticleTypes.SMOKE,
            		(double)worldPosition.getX() + 0.5D + level.random.nextDouble() / 15.0D * (double)(level.random.nextBoolean() ? 1 : -1),
            		(double)worldPosition.getY() + 1.0D,
            		(double)worldPosition.getZ() + 0.5D + level.random.nextDouble() / 15.0D * (double)(level.random.nextBoolean() ? 1 : -1),
            		0.0D, 0.005D, 0.0D);
        }
        else
        {
        	handleDeactivate();
            setCanInteract(false);
            currentSpeed *= 0.95F;
        }
        fanRotation += currentSpeed;

        CoreNetwork.sendToServer(new PacketStartupShutdown(getBlockPos(), getCanInteract()));
        
        tickMachineSound();
    }
    
	private boolean isBreakable(BlockPos pos)
	{
		BlockState state = level.getBlockState(pos);

		if(state.is(WUTTags.Blocks.BREAKER_BLACKLIST))
			return false;
		if(state.isAir())
			return false;
		if(state.getMapColor(level, pos) == MapColor.WATER)
			return false;
		if(state.getBlock() instanceof LiquidBlock)
			return false;
		float bh = state.getDestroySpeed(level, pos);
		return !((bh < 0) || (bh > 55));
	}
	
	@Override
	public void load(CompoundTag tag)
	{
        startup = tag.getInt("startup");
        shutdown = tag.getInt("shutdown");
        active = tag.getBoolean("active");
        clientSound = PlayingSound.values()[tag.getInt("playingSound")];
		super.load(tag);
	}
	@Override
	public void saveAdditional(CompoundTag tag)
	{
        tag.putInt("startup", startup);
        tag.putInt("shutdown", shutdown);
        tag.putBoolean("active", active);
        tag.putInt("playingSound", clientSound.ordinal());
		super.saveAdditional(tag);
	}
	
	@Override
	public void resetFakeStamina()
	{
		fakeStamina = 0;
	}
	
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity)
    {
        return new MinerBasicContainer(this, playerInventory, i);
    }
    
    @Override
    public MachineInventoryLayout getInventoryLayout()
    {
        return MachineInventoryLayout.builder()
            .inputSlot().slotAccess(INPUTTOOL)
            .soulbank().build();
    }
    
    /*
     * 
     * MACHINESOUND
     * 
     */
    private int startup = 0;
    private int shutdown = 0;
    private boolean active = false;
    private int startupCounter;
    private int shutdownCounter;
    
    enum PlayingSound {
        NONE,
        STARTUP,
        ACTIVE,
        SHUTDOWN
    }
    private PlayingSound clientSound = PlayingSound.NONE;

    private void tickMachineSound()
    {
        if (BlocksConfig.MACHINESOUND_VOLUME.get() < 0.01f)
            return;
        
        PlayingSound newsound = getPlayingSound();
        if (newsound != clientSound)
        {
            clientSound = newsound;
        }
        
        switch (clientSound)
        {
            case NONE:
                stopSounds();
                break;
            case STARTUP:
                if (!MachineSoundController.isStartupPlaying(level, worldPosition))
                {
                	MachineSoundController.playStartup(level, worldPosition);
                }
                break;
            case ACTIVE:
                if (!MachineSoundController.isLoopPlaying(level, worldPosition))
                {
                	MachineSoundController.playLoop(level, worldPosition);
                }
                break;
            case SHUTDOWN:
                if (!MachineSoundController.isShutdownPlaying(level, worldPosition))
                {
                	MachineSoundController.playShutdown(level, worldPosition);
                }
                break;
        }
    }
    
    private PlayingSound getPlayingSound()
    {
        if (startup != 0) {
            return PlayingSound.STARTUP;
        } else if (shutdown != 0) {
            return PlayingSound.SHUTDOWN;
        } else if (active) {
            return PlayingSound.ACTIVE;
        } else {
            return PlayingSound.NONE;
        }
    }
    
    @Override
    public void setRemoved()
    {
    	super.setRemoved();
        if (level.isClientSide)
        {
            stopSounds();
        }
    }
    
    private void stopSounds()
    {
        MachineSoundController.stopSound(level, getBlockPos());
    }
    
    private boolean handleActivate()
    {
        if (isActive() && getShutdownCounter() == 0)
            return false;

        startup = getStartupCounter();
        
        if (startup == 0)
            startup = 70;
        startup--;
        
        if (startup <= 0)
        {
            startup = 0;
            MinerBasicBlockEntity generatorTileEntity = (MinerBasicBlockEntity) level.getBlockEntity(worldPosition);
            generatorTileEntity.activate(true);
        }
                
        active = isActive();
        shutdown = 0;
        setShutdownCounter(0);
        setStartupCounter(startup);
        setChanged();
        return true;
    }

    private boolean handleDeactivate()
    {
        if ((!isActive()) && getShutdownCounter() == 0 && getStartupCounter() == 0)
        {
            if (getShutdownCounter() != shutdown || getStartupCounter() != startup || (isActive() != active))
            {
                shutdown = getShutdownCounter();
                startup = getStartupCounter();
                active = isActive();
                setChanged();
            }
            return false;
        }

        shutdown = getShutdownCounter();
        if (isActive() || getStartupCounter() != 0)
        {
            shutdown = 70;
            MinerBasicBlockEntity generatorTileEntity = (MinerBasicBlockEntity) level.getBlockEntity(worldPosition);
            generatorTileEntity.activate(false);
        }
        shutdown--;
        if (shutdown <= 0)
        {
            shutdown = 0;
        }
        startup = 0;

        active = isActive();
        setStartupCounter(0);
        setShutdownCounter(shutdown);
        setChanged();

        return true;
    }
    
    public void activate(boolean active)
    {
        if(isActive() != active)
        {
        	this.active = active;
        }
    }
    public boolean isActive()
    {
        return active;
    }
    public void setActive(boolean active)
    {
        this.active = active;
    }
    public int getStartupCounter()
    {
        return startupCounter;
    }
    public void setStartupCounter(int startupCounter)
    {
        this.startupCounter = startupCounter;
    }
    public int getShutdownCounter()
    {
        return shutdownCounter;
    }
    public void setShutdownCounter(int shutdownCounter)
    {
        this.shutdownCounter = shutdownCounter;
    }
}
