package geni.witherutils.base.common.block.generator.water;

import java.util.function.Supplier;

import geni.witherutils.api.io.energy.EnergyIOMode;
import geni.witherutils.base.common.base.WitherMachineEnergyGenBlockEntity;
import geni.witherutils.base.common.config.common.generator.WaterGeneratorConfig;
import geni.witherutils.base.common.init.WUTBlockEntityTypes;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.base.common.io.energy.WitherEnergyStorage;
import geni.witherutils.base.common.io.item.MachineInventoryLayout;
import geni.witherutils.base.common.io.item.SingleSlotAccess;
import geni.witherutils.base.common.item.shovel.ShovelItem;
import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class WaterGeneratorBlockEntity extends WitherMachineEnergyGenBlockEntity {

	public static final SingleSlotAccess INPUT = new SingleSlotAccess();
    public static final SingleSlotAccess OUTPUT = new SingleSlotAccess();
    
    public static final Supplier<Integer> CAPACITY = () -> WaterGeneratorConfig.MAXENERGY.get();
    public static final Supplier<Integer> USAGE = () -> WaterGeneratorConfig.SENDPERTICK.get();
    
	private int generationRate;
    private double efficiencyRate = WaterGeneratorConfig.EFFECIENCYBASE.get();
    
	private Shovel shovelModul = Shovel.NONE;
	@SuppressWarnings("unused")
	private int particleRate;
	
	private WaterGeneratorData generatorData = new WaterGeneratorData();

    private float maxSpeed = 30F;
    private float acceleration = 0.25F;
    
    @OnlyIn(Dist.CLIENT)
    public float prevFanRotation;
    @OnlyIn(Dist.CLIENT)
    public float fanRotation;
    @OnlyIn(Dist.CLIENT)
    private float currentSpeed;
    
    public WaterGeneratorBlockEntity(BlockPos worldPosition, BlockState blockState)
    {
		super(EnergyIOMode.OUTPUT, CAPACITY, USAGE, WUTBlockEntityTypes.WATER_GENERATOR.get(), worldPosition, blockState);
	}
    
    @Override
    protected WitherEnergyStorage createEnergyStorage(EnergyIOMode energyIOMode, Supplier<Integer> capacity, Supplier<Integer> usageRate)
    {
        return new WitherEnergyStorage(energyIOMode, capacity, usageRate)
        {
        	@Override
        	public int addEnergy(int energy, boolean simulate)
        	{
        		return shovelModul.canProduce() ? energy : 0;
        	}
        	@Override
        	public boolean canExtract()
        	{
        		return shovelModul.canExtract();
        	}
    		@Override
    		public int receiveEnergy(int maxReceive, boolean simulate)
    		{
                int received = getEnergyHandler(null).receiveEnergy(maxReceive, simulate);
                if (!simulate && received > 0)
                {
                    particleRate = Math.min(20, received < 500 ? 1 : received / 500);
                }
                return received;
    		}
    		@Override
    		public int extractEnergy(int maxExtract, boolean simulate)
    		{
                int extracted = getEnergyHandler(null).extractEnergy(maxExtract, simulate);
                if (!simulate && extracted > 0)
                {
                    particleRate = Math.min(20, extracted < 500 ? 1 : extracted / 500);
                }
                return extracted;
    		}
            @Override
            protected void onContentsChanged()
            {
                setChanged();
            }
        };
    }
    
    @Override
    public MachineInventoryLayout getInventoryLayout()
    {
        return MachineInventoryLayout.builder().setStackLimit(1).inputSlot((slot, stack) -> stack.getItem() instanceof ShovelItem).slotAccess(INPUT).build();
    }
    
    public Shovel getShovelModul()
    {
    	return shovelModul;
    }
    public void setShovelModul(Shovel module)
    {
    	this.shovelModul = module;
    }
    
    @Override
    public void serverTick()
    {
        super.serverTick();

        generatorData.tick(this);
        
    	float efficiency = generatorData.outputPower;
        
    	if(getInventory().getStackInSlot(0).isEmpty())
    		return;
    	
        if (isGenerating() || efficiency != 0)
        {
        	getEnergyHandler(null).addEnergy(getGenerationRate());
        }
    	moveEnergy(Direction.UP, 1000);
	}

    @Override
    public void clientTick()
    {
    	super.clientTick();

//    	System.out.println("SERVER: EFFECIENCY" + getInventory().getStackInSlot(0));
    	
    	if(getInventory().getStackInSlot(0).isEmpty())
    		return;
    	
    	switch(shovelModul)
    	{
			case ADVANCED:
				setShovelModul(Shovel.ADVANCED);
				break;
			case BASIC:
				setShovelModul(Shovel.BASIC);
				break;
			case MASTER:
				setShovelModul(Shovel.MASTER);
				break;
			case NONE:
				break;
    	}

        maxSpeed = 60 / 30 * this.getGenerationRate();
        
        prevFanRotation = fanRotation;
        if(this.getGenerationRate() > 0 && getEnergyHandler(null).getEnergyStored() < getEnergyHandler(null).getMaxEnergyStored())
        {
            if(level.getGameTime() % (9 - this.getGenerationRate()) == 0)
            	SoundUtil.playDistanceSound(Minecraft.getInstance(), level, worldPosition, WUTSounds.SWOOSH.get(), 30);

            currentSpeed += acceleration;
            if(currentSpeed > maxSpeed)
            {
                currentSpeed = maxSpeed;
            }

            spawnParticles(level, worldPosition);
        }
        else
        {
            currentSpeed *= 0.95F;
        }
        fanRotation += currentSpeed;
    }
    
    @SuppressWarnings("deprecation")
    private void spawnParticles(Level level, BlockPos pos)
    {
    	BlockState state = level.getBlockState(pos.above());
        if(state.isAir() || state.is(BlockTags.FIRE) || !state.isSolid() || state.liquid() || state.canBeReplaced())
        {
            float particleX = (float) pos.getX() + 0.5f;
            float particleY = (float) pos.getY() + 1.25f;
            float particleZ = (float) pos.getZ() + 0.5f;
            if(level.random.nextFloat() < 0.3F)
            {
                for(int i = 0; i < (int) this.getGenerationRate() / 4; i++)
                {
                    level.addParticle(ParticleTypes.SPLASH, (double) particleX + Math.random() - 0.5f, (double) particleY + Math.random() - 0.5f, (double) particleZ + Math.random() - 0.5f, 0, 0, 0);
                }
            }
            else if(level.random.nextFloat() > 0.97F)
            {
            	level.addParticle(ParticleTypes.BUBBLE_POP, (double) particleX + Math.random() - 0.5f, (double) particleY + Math.random() - 0.5f, (double) particleZ + Math.random() - 0.5f, 0.0D, 0.0D, 0.0D);
            	level.addParticle(ParticleTypes.SPLASH, (double) particleX + Math.random() - 0.5f, (double) particleY + Math.random() - 0.5f, (double) particleZ + Math.random() - 0.5f, 0, 7, 0);
            }
        }
    }

    public static int unsignedClampToInt(long l)
    {
        return l > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)l;
    }
    
    @Override
    public boolean isGenerating()
    {
    	return getGenerationRate() > 0 && shovelModul.canProduce();
    }
    
    @Override
    public int getGenerationRate()
    {
        int v = 0;
        for (final Direction enumFacing : Direction.values())
        {
            final BlockPos offset = worldPosition.relative(enumFacing);
            final BlockState state = level.getBlockState(offset);
            final BlockState stateAbove = level.getBlockState(offset.above());
            if (state.getBlock() == Blocks.WATER || state.getFluidState().getType() == Fluids.FLOWING_WATER)
            {
                if (stateAbove.getBlock() != Blocks.WATER || stateAbove.getFluidState().getType() != Fluids.FLOWING_WATER)
                {
                    final int value = 8 - (int) state.getValue(LiquidBlock.LEVEL);
                    if (value < 8)
                    {
                        v += (value + 1) / 7;
                    }
                }
            }
        }
        generationRate = v;
        
        return generationRate;
    }

	@Override
	public boolean hasEfficiencyRate()
	{
		return WaterGeneratorConfig.HASEFFECIENCY.get();
	}

	@Override
	public float getEfficiencyRate()
	{
		if (level == null || getGenerationRate() == 0)
			return 0.0f;

		final long t = level.getGameTime();
		final float v = ((int)t & 0xFF) / 256.0f;
		long k = t >> 8;
        k += 3 * 31L;
        final long a = k * k * 42317861L + k * 11L;
        final long b = a + (2L * k + 1L) * 42317861L + 11L;
        final float ai = (int)(a & 0xFFL) / 256.0f;
        final float bi = (int)(b & 0xFFL) / 256.0f;
        final float v2 = ai + (bi - ai) * v;

        return (float) efficiencyRate + v2 * 2.0f;
	}

    @Override
    public void saveAdditional(CompoundTag pTag, Provider lookupProvider)
    {
        pTag.putInt("shovelmodule", this.shovelModul.ordinal());
        super.saveAdditional(pTag, lookupProvider);
    }

    @Override
    public void loadAdditional(CompoundTag pTag, Provider lookupProvider)
    {
    	shovelModul = Shovel.values()[pTag.getInt("shovelmodule")];
        super.loadAdditional(pTag, lookupProvider);
    }
	
    public enum Shovel
    {
        NONE,
        BASIC,
        ADVANCED,
        MASTER;

        public boolean hasNoShovel()
        {
            return this == NONE;
        }
        public float getShovelSpeed()
        {
        	return this == BASIC ? 1.0f :
        		   this == ADVANCED ? 2.0f :
        		   this == MASTER ? 3.0f :
        			   				0.0f;
        }
        public boolean canExtract()
        {
            return this != NONE;
        }
        public boolean canProduce()
        {
            return this != NONE;
        }
    }
}
