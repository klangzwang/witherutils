package geni.witherutils.base.common.block.generator.solar;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import geni.witherutils.api.io.energy.EnergyIOMode;
import geni.witherutils.api.soulbank.FixedScalable;
import geni.witherutils.base.common.base.WitherMachineEnergyGenBlockEntity;
import geni.witherutils.base.common.config.common.SolarConfig;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.io.energy.WitherEnergyStorage;
import geni.witherutils.core.common.sync.IntegerDataSlot;
import geni.witherutils.core.common.sync.SyncMode;
import geni.witherutils.core.common.util.BlockEntityUtil;
import geni.witherutils.core.common.util.CompatibilityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public class SolarPanelBlockEntity extends WitherMachineEnergyGenBlockEntity {

    public static class Basic extends SolarPanelBlockEntity
    {
        public Basic(BlockPos pos, BlockState state)
        {
            super(WUTEntities.SOLARBASIC.get(), pos, state);
        }
    }
    public static class Advanced extends SolarPanelBlockEntity
    {
        public Advanced(BlockPos pos, BlockState state)
        {
            super(WUTEntities.SOLARADV.get(), pos, state);
        }
    }
    public static class Ultra extends SolarPanelBlockEntity
    {
        public Ultra(BlockPos pos, BlockState state)
        {
            super(WUTEntities.SOLARULTRA.get(), pos, state);
        }
    }
    
    public ISolarPanelNetwork network = NoSolarPanelNetwork.INSTANCE;
    private int generationRate;
	@SuppressWarnings("unused")
	private int idleCounter = 0;
	private int count = 1;
	
    public SolarPanelBlockEntity(BlockEntityType<?> pType, BlockPos worldPosition, BlockState blockState)
    {
        super(new FixedScalable(() -> 0f), new FixedScalable(() -> 0f), new FixedScalable(() -> 0f), pType, worldPosition, blockState);
        addDataSlot(new IntegerDataSlot(this::getGenerationRate, p -> generationRate = p, SyncMode.WORLD));
    }

    @Override
    protected WitherEnergyStorage createEnergyStorage(EnergyIOMode energyIOMode, Supplier<Integer> capacity, Supplier<Integer> transferRate, Supplier<Integer> usageRate)
    {
        return new WitherEnergyStorage(getIOConfig(), EnergyIOMode.Output, () -> 1, () -> 2000, () -> 0)
        {
        	@Override
        	public int getEnergyStored()
        	{
        		return network.getEnergyAvailablePerTick();
        	}
        	@Override
        	public int getMaxEnergyStored()
        	{
        		return network.getEnergyMaxPerTick();
        	}

            @Override
            public LazyOptional<IEnergyStorage> getCapability(@Nullable Direction side)
            {
            	if(side != Direction.DOWN)
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
        
	    if(!network.isValid())
	    {
	        SolarPanelNetwork.build(this);
	    }

	    IEnergyStorage receptor = CompatibilityUtil.getEnergyHandler(level.getBlockEntity(getBlockPos().relative(Direction.DOWN)), Direction.UP);
	    if(receptor != null && receptor.receiveEnergy(1, true) > 0)
	    {
	    	int canTransmit = network.getEnergyAvailableThisTick();
	    	if(canTransmit > 0)
	    	{
	    		network.extractEnergy(receptor.receiveEnergy(canTransmit, false));
	    	}
	    	else
	    	{
	    		idleCounter = level.random.nextInt(32);
	    	}
	    }
	    else
	    {
	    	idleCounter = level.random.nextInt(256);
	    }
	}
    
	@Override
	public boolean isGenerating()
	{
		return generationRate > 0;
	}

	@Override
	public int getGenerationRate()
	{
		return getEnergyPerTick();
	}

	@Override
	public boolean hasEfficiencyRate()
	{
		return false;
	}
	@Override
	public float getEfficiencyRate()
	{
        return 0;
	}
	
	/*
	 * 
	 * NETWORK
	 * 
	 */
	public void setNetwork(ISolarPanelNetwork network)
	{
		this.network = network;
	}
	
	int getEnergyPerTick()
	{
		return getEnergyPerTick(level, worldPosition);
	}
	
	public static int getEnergyPerTick(@Nonnull Level world, @Nonnull BlockPos pos)
	{
		final BlockState state = world.getBlockState(pos);
		if(state.getBlock() instanceof SolarPanelBlock solar)
		{
			if(solar.getType() == SolarType.ADVANCED)
				return SolarConfig.SOLARADVINPUTRF.get();
			else if(solar.getType() == SolarType.ULTRA)
				return SolarConfig.SOLARULTRAINPUTRF.get();
			return SolarConfig.SOLARBASICINPUTRF.get();
		}
		else
		{
			return -1;
		}
	}
	
	public boolean isSolarPowered(@Nonnull Level world, BlockPos pos)
	{
		return world.canSeeSkyFromBelowWater(pos);
	}
	
	public float calculateLocalLightRatio(@Nonnull Level level, BlockPos pos, float baseRatio)
	{
		float ratio = isSolarPowered(level, pos) ? baseRatio : 0;
		return ratio;
	}
	
	public float calculateLightRatio(@Nonnull Level level)
	{
		int lightValue = level.getLightEngine().getLayerListener(LightLayer.SKY).getLightValue(getBlockPos());
		float sunAngle = level.getSunAngle(1.0F);
		if(sunAngle < (float) Math.PI)
		{
			sunAngle += (0.0F - sunAngle) * 0.2F;
		}
		else
		{
			sunAngle += (((float) Math.PI * 2F) - sunAngle) * 0.2F;
		}
		lightValue = Math.round(lightValue * Mth.cos(sunAngle));
		lightValue = Mth.clamp(lightValue, 0, 15);

		return lightValue / 15f;
	}

	/**
	 * 
	 * GET/SET COUNT
	 * 
	 */
    @Override
    public void onLoad()
    {
        BlockEntityUtil.sendUpdatePacket(this);
    }
    public void setCount(int count)
    {
        this.count = count;
        BlockEntityUtil.sendUpdatePacket(this);
    }
    public int getCount()
    {
        return this.count;
    }
    public void updateCount()
    {
        Set<SolarPanelBlockEntity> panels = new HashSet<>();
        this.isSolarPanel(panels, this.worldPosition);
        panels.forEach(panel -> panel.setCount(panels.size()));
    }
    private void isSolarPanel(Set<SolarPanelBlockEntity> panels, BlockPos pos)
    {
        if(this.level == null)
            return;

        BlockEntity tileEntity = this.level.getBlockEntity(pos);
        if(tileEntity instanceof SolarPanelBlockEntity)
        {
            if(panels.contains(tileEntity))
                return;

            panels.add((SolarPanelBlockEntity) tileEntity);
            this.isSolarPanel(panels, pos.relative(Direction.NORTH));
            this.isSolarPanel(panels, pos.relative(Direction.EAST));
            this.isSolarPanel(panels, pos.relative(Direction.SOUTH));
            this.isSolarPanel(panels, pos.relative(Direction.WEST));
        }
    }
    
    /**
     * 
     * TAG
     * 
     */
    @Override
    public void load(CompoundTag compound)
    {
        super.load(compound);
        this.readData(compound);
    }
    @Override
	public void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);
        this.writeData(tag);
    }
    @Override
    public CompoundTag getUpdateTag()
    {
        return this.saveWithFullMetadata();
    }
    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
    {
        CompoundTag compound = pkt.getTag();
        this.readData(compound);
    }
    private void readData(CompoundTag compound)
    {
        if(compound.contains("Count", Tag.TAG_INT))
        {
            this.count = compound.getInt("Count");
        }
    }
    private CompoundTag writeData(CompoundTag compound)
    {
        compound.putInt("Count", this.count);
        return compound;
    }
    @Nonnull
    @Override
    public ModelData getModelData()
    {
        return super.getModelData();
    }
}
