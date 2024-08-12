package geni.witherutils.base.common.block.creative;

import org.jetbrains.annotations.Nullable;

import geni.witherutils.api.io.energy.EnergyIOMode;
import geni.witherutils.base.common.base.WitherMachineBlockEntity;
import geni.witherutils.base.common.init.WUTBlockEntityTypes;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.base.common.io.energy.IWitherEnergyStorage;
import geni.witherutils.base.common.io.energy.WitherEnergyStorage;
import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class CreativeEnergyBlockEntity extends WitherMachineBlockEntity {

    private static final int BASE_FE_PRODUCTION = 4000;
    private final WitherEnergyStorage energy = new WitherEnergyStorage(EnergyIOMode.OUTPUT, () -> Integer.MAX_VALUE, () -> Integer.MAX_VALUE);
    
    private int rfPerTick;
    
    /*************************************************************/
    
    private boolean powered = false;
    private int maxProgress = 10;
    private int slideProgress;
    private int prevSlideProgress;
    
    /*************************************************************/
    
    public int timer;
    
    /*************************************************************/
    
	public CreativeEnergyBlockEntity(BlockPos pos, BlockState state)
	{
		super(WUTBlockEntityTypes.CREATIVE_GENERATOR.get(), pos, state);
	}
	
    @Override
    public boolean hasItemCapability()
    {
        return false;
    }

    @Override
    public boolean hasEnergyCapability()
    {
        return true;
    }
    
    @Override
    public boolean hasFluidCapability()
    {
        return true;
    }
    
    @Override
    public IWitherEnergyStorage getEnergyHandler(@Nullable Direction dir)
    {
        return dir == getCurrentFacing() ? null : energy;
    }
    
    @Override
    public void serverTick()
    {
        super.serverTick();

        if (level.getGameTime() % 5 == 0)
        {
            rfPerTick = BASE_FE_PRODUCTION;
        }
        if (energy.getEnergyStored() != Integer.MAX_VALUE)
        {
            energy.receiveEnergy(rfPerTick, false);
        }
        
//        System.out.println(tank.getFluidAmount());
//        System.out.println(energy.getEnergyStored());
    }

    @Override
    public void clientTick()
    {
    	super.clientTick();
    	
        prevSlideProgress = slideProgress;
        if(powered)
        {
            if(slideProgress < Math.max(0, maxProgress))
            {
                slideProgress++;
            }
        }
        else if(slideProgress > 0)
        {
            slideProgress--;
        }
        
        
    	if(powered)
    	{
        	if(this.timer < 200)
        		this.timer += 5;
        	else if(this.timer >= 200)
        	{
        		setPowered(false);
        		this.timer = 200;
        	}
    	}
    	else
    	{
        	if(this.timer > 0)
        		this.timer -= 5;
        	else if(this.timer <= 0)
        	{
        		this.timer = 0;
        	}
    	}
    }
    
    /*************************************************************/
    
    public float getSlideProgress(float partialTicks)
    {
        float partialSlideProgress = prevSlideProgress + (slideProgress - prevSlideProgress) * partialTicks;
        float normalProgress = partialSlideProgress / (float) maxProgress;
        return 1.0F * (1.0F - ((float) Math.sin(Math.toRadians(90.0 + 180.0 * normalProgress)) / 2.0F + 0.5F));
    }
    public void setPowered(boolean powered)
    {
        this.powered = powered;
    }
    public boolean isPowered()
    {
        return powered;
    }
    @Override
    public InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult)
    {
        boolean powered = isPowered();
        setPowered(pLevel, pPos, pState, !powered);
    	return super.useWithoutItem(pState, pLevel, pPos, pPlayer, pHitResult);
    }
    private void setPowered(Level world, BlockPos pos, BlockState state, boolean powered)
    {
        if(isPowered() == powered)
            return;

        setPowered(powered);
        sync();

        if(powered)
        {
        	SoundUtil.playSoundDistrib(world, pos, WUTSounds.WORMBIP.get(), 1.f, 1.2f);
        }
        else
        {
        	SoundUtil.playSoundDistrib(world, pos, WUTSounds.WORMBIP.get(), 1.f, 0.8f);
        }
    }
    
    /*************************************************************/
    
    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider)
    {
        super.saveAdditional(tag, provider);
        energy.serializeNBT(provider);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider)
    {
        super.loadAdditional(tag, provider);
        energy.deserializeNBT(provider, tag);
    }
}
