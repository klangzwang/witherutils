package geni.witherutils.base.common.block.generator.solar;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import geni.witherutils.api.io.energy.EnergyIOMode;
import geni.witherutils.base.common.base.WitherMachineBlockEntity;
import geni.witherutils.base.common.config.common.SolarConfig;
import geni.witherutils.base.common.init.WUTBlockEntityTypes;
import geni.witherutils.base.common.io.energy.IWitherEnergyStorage;
import geni.witherutils.base.common.io.energy.WitherEnergyStorage;
import geni.witherutils.core.common.util.BlockEntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
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
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class SolarPanelBlockEntity extends WitherMachineBlockEntity {

    public static class Basic extends SolarPanelBlockEntity
    {
        public Basic(BlockPos pos, BlockState state)
        {
            super(WUTBlockEntityTypes.SOLARBASIC.get(), pos, state);
        }
    }
    public static class Advanced extends SolarPanelBlockEntity
    {
        public Advanced(BlockPos pos, BlockState state)
        {
            super(WUTBlockEntityTypes.SOLARADV.get(), pos, state);
        }
    }
    public static class Ultra extends SolarPanelBlockEntity
    {
        public Ultra(BlockPos pos, BlockState state)
        {
            super(WUTBlockEntityTypes.SOLARULTRA.get(), pos, state);
        }
    }
    
    private final WitherEnergyStorage energy = new WitherEnergyStorage(EnergyIOMode.OUTPUT, () -> 1, () -> 2000)
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
    };
    
    public ISolarPanelNetwork network = NoSolarPanelNetwork.INSTANCE;
	private int count = 1;
	
	public SolarPanelBlockEntity(BlockEntityType<?> pType, BlockPos worldPosition, BlockState blockState)
	{
		super(pType, worldPosition, blockState);
	}
	
    @Override
    public IWitherEnergyStorage getEnergyHandler(@Nullable Direction dir)
    {
        return dir != Direction.DOWN ? null : energy;
    }
    
    @Override
    public boolean hasEnergyCapability()
    {
        return true;
    }
    
    @Override
    public void serverTick()
    {
        super.serverTick();
        
	    if(!network.isValid())
	    {
	        SolarPanelNetwork.build(this);
	    }

	    IEnergyStorage receptor = getNeighbouringCapability(Capabilities.EnergyStorage.BLOCK, Direction.DOWN);
	    if(receptor != null && receptor.receiveEnergy(1, true) > 0)
	    {
	    	int canTransmit = network.getEnergyAvailableThisTick();
	    	if(canTransmit > 0)
	    	{
	    		network.extractEnergy(receptor.receiveEnergy(canTransmit, false));
	    	}
	    }
	}
    
	public boolean isGenerating()
	{
		return calculateLightRatio(level) > 0;
	}

	public int getGenerationRate()
	{
		return getEnergyPerTick();
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
    public void loadAdditional(CompoundTag pTag, Provider lookupProvider)
    {
    	super.loadAdditional(pTag, lookupProvider);
        this.readData(pTag);
    }
    @Override
    public void saveAdditional(CompoundTag pTag, Provider lookupProvider)
    {
    	super.saveAdditional(pTag, lookupProvider);
        this.writeData(pTag);
    }
    @Override
    public CompoundTag getUpdateTag(Provider registries)
    {
        return this.saveWithFullMetadata(registries);
    }
    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, Provider lookupProvider)
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
