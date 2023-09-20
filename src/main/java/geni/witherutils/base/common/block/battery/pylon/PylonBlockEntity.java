package geni.witherutils.base.common.block.battery.pylon;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import geni.witherutils.base.common.base.IInteractBlockEntity;
import geni.witherutils.base.common.base.WitherMachineBlockEntity;
import geni.witherutils.base.common.block.battery.core.CoreBlockEntity;
import geni.witherutils.base.common.block.battery.pylon.PylonBlock.Mode;
import geni.witherutils.base.common.config.common.BatteryConfig;
import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.init.WUTParticles;
import geni.witherutils.core.common.math.Vec3D;
import geni.witherutils.core.common.particle.IntParticleType;
import geni.witherutils.core.common.sync.EnumDataSlot;
import geni.witherutils.core.common.sync.IntegerDataSlot;
import geni.witherutils.core.common.sync.SyncMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

@SuppressWarnings("deprecation")
public class PylonBlockEntity extends WitherMachineBlockEntity implements IInteractBlockEntity {

	private Mode ioMode = Mode.OUTPUT;
    public CoreBlockEntity core = null;
    public BlockPos coreOffset = (BlockPos) null;
    private int particleRate;
    public boolean active;

    public static final int MAX = BatteryConfig.PYLONCACHEDENERGY.get();
    
	EnergyStorage energy = new EnergyStorage(MAX)
	{
		@Override
		public boolean canExtract()
		{
			return ioMode.canExtract();
		}
		@Override
		public boolean canReceive()
		{
			return ioMode.canReceive();
		}
		
		@Override
		public int receiveEnergy(int maxReceive, boolean simulate)
		{
            if (coreOffset == BlockPos.ZERO || !canReceive() || getCore() == null || !getCore().active)
            {
                return 0;
            }
            int received = getCore().getEnergyStorage().receiveEnergy(maxReceive, simulate);
            if (!simulate && received > 0)
            {
                particleRate = Math.min(20, received < 500 ? 1 : received / 500);
            }
            return received;
		}
		@Override
		public int extractEnergy(int maxExtract, boolean simulate)
		{
            if (coreOffset == BlockPos.ZERO || !canExtract() || getCore() == null || !getCore().active)
            {
                return 0;
            }
            int extracted = getCore().getEnergyStorage().extractEnergy(maxExtract, simulate);
            if (!simulate && extracted > 0)
            {
                particleRate = Math.min(20, extracted < 500 ? 1 : extracted / 500);
            }
            return extracted;
		}
	};
	
    private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> this.energy);
    
    public PylonBlockEntity(BlockPos pos, BlockState state)
    {
		super(WUTEntities.PYLON.get(), pos, state);
		addDataSlot(new IntegerDataSlot(this::getParticleRate, p -> particleRate = p, SyncMode.WORLD));
		addDataSlot(new EnumDataSlot<>(this::getiOMode, p -> ioMode = p, SyncMode.WORLD));
    }

    @Override
    public void serverTick()
    {
        super.serverTick();

    	if(findCore() == null)
    		return;
    	
        if(core == null)
        	core = findCore();
        
        if (coreOffset == null || getCore() == null || !getCore().active)
            return;

        if (!level.isClientSide && getCore().active)
        {
            int extracted = core.getEnergyStorage().extractEnergy(sendEnergyToAll(core.getEnergyStorage().getEnergyStored(), core.getEnergyStorage().getEnergyStored()), false);
            if (extracted > 0)
            {
                particleRate = Math.min(20, extracted < 500 ? 1 : extracted / 500);
            }
        }

        if (!level.isClientSide && (particleRate > 1 || (particleRate > 0 && level.random.nextInt(2) == 0)))
        {
            particleRate = particleRate -2;
        }
    }
    
    @Override
    public void clientTick()
    {
        super.clientTick();
        
    	if(findCore() == null)
    		return;
    	
        if(core == null)
        	core = findCore();
        
        spawnParticles();
    }
    
    public int getParticleRate()
    {
    	return particleRate;
    }
    public Mode getiOMode()
    {
    	return ioMode;
    }

    @Override
    public InteractionResult onBlockUse(BlockState state, Player player, InteractionHand hand, BlockHitResult hit)
    {
    	if(!level.isClientSide)
    	{
            ioMode = ioMode.reverse();
            level.setBlockAndUpdate(worldPosition, level.getBlockState(worldPosition).setValue(PylonBlock.MODE, ioMode));
    	}
        return InteractionResult.SUCCESS;
    }
    
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction direction)
    {
		if(direction == getCurrentFacing().getOpposite() && cap == ForgeCapabilities.ENERGY)
			return energyCap.cast();
        return super.getCapability(cap, direction);
    }

    public CoreBlockEntity findCore()
    {
        if (getCore() != null)
            return getCore();
        
        int range = 9;
        Direction dir = this.getCurrentFacing();
        BlockPos offset = new BlockPos(dir.getStepX() * range, dir.getStepY() * range, dir.getStepZ() * range);
        BlockPos min = worldPosition.offset(-9, -9, -9).offset(offset);
        BlockPos max = worldPosition.offset(9, 9, 9).offset(offset);
        
        for (BlockPos blockPos : BlockPos.betweenClosed(min, max))
        {
            if (level.getBlockState(blockPos).getBlock() == WUTBlocks.CORE.get())
            {
                if (level.getBlockEntity(blockPos) instanceof CoreBlockEntity tile && tile.active)
                {
                	CoreBlockEntity core = (CoreBlockEntity) tile;
                	setCore(core);
                    return core;
                }
            }
        }
        return null;
    }
    
    public CoreBlockEntity getCore()
    {
        BlockPos pos = coreOffset;
        if (pos != null)
        {
            BlockPos corePos = worldPosition.subtract(pos);
            LevelChunk coreChunk = level.getChunkAt(corePos);

            if (!level.isAreaLoaded(corePos, 16))
            {
                core = null;
                return null;
            }

            BlockEntity tileAtPos = coreChunk.getBlockEntity(corePos, LevelChunk.EntityCreationType.CHECK);
            if (tileAtPos == null || core == null || tileAtPos != core)
            {
                BlockEntity tile = level.getBlockEntity(corePos);

                if (tile instanceof CoreBlockEntity)
                {
                    core = (CoreBlockEntity) tile;
                }
                else
                {
                    core = null;
                    coreOffset = null;

                }
            }
        }
        return core;
    }
    
    public void setCore(@Nullable CoreBlockEntity core)
    {
        if (core == null)
        {
            coreOffset = null;
            return;
        }
        BlockPos offset = worldPosition.subtract(core.getBlockPos());
        coreOffset = offset;
        setChanged();
    }
   
    @Nullable
    private BlockPos getCorePos()
    {
        return coreOffset == null ? null : worldPosition.subtract(Objects.requireNonNull(coreOffset));
    }

    @OnlyIn(Dist.CLIENT)
    private void spawnParticles()
    {
        RandomSource rand = level.random;

        if (getCore() == null || particleRate <= 0)
        	return;
        
        if (particleRate > 20)
        	particleRate = 20;
        
        Vec3D spawn;
        Vec3D dest;

        int r = 120;
        int g = 200;
        int b = 255;
        
        if (particleRate > 10)
        {
            for (int i = 0; i <= particleRate; i++)
            {
                spawn = getParticleSpawn(rand);
                dest = getParticleDest(rand);
               	level.addParticle(new IntParticleType.IntParticleData(WUTParticles.ENERGY.get(), r, g, b, 200), spawn.x, spawn.y, spawn.z, dest.x, dest.y, dest.z);
            }
        }
        else if (rand.nextInt(Math.max(1, 10 - particleRate)) == 0)
        {
        	spawn = getParticleSpawn(rand);
        	dest = getParticleDest(rand);
        	level.addParticle(new IntParticleType.IntParticleData(WUTParticles.ENERGY.get(), r, g, b, 200), spawn.x, spawn.y, spawn.z, dest.x, dest.y, dest.z);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private Vec3D getParticleSpawn(RandomSource random)
    {
        if (ioMode.canExtract())
        {
            double range = 5;
            return new Vec3D(getCore().getBlockPos()).add(
            				(random.nextFloat() - 0.5F) * range,
            				(random.nextFloat() - 0.25F) * range,
            				(random.nextFloat() - 0.5F) * range);
        }
        else
        {
            return Vec3D.getCenter(getBlockPos()).add(
		    				(-0.0F),
		    				(-0.5F + random.nextFloat()),
		    				(-0.0F));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private Vec3D getParticleDest(RandomSource random)
    {
        if (ioMode.canExtract())
        {
            return Vec3D.getCenter(getBlockPos()).add(
		    				(-0.0F),
		    				(-0.0F),
		    				(-0.0F));
        }
        else
        {
            double range = 10 / 2D;
            return new Vec3D(getCore().getBlockPos()).add(
    				(random.nextFloat() - 0.5F) * range,
    				(random.nextFloat() - 0.5F) * range,
    				(random.nextFloat() - 0.5F) * range);
        }
    }
    
    public int sendEnergyToAll(int maxPerTarget, int maxAvailable)
    {
        int i = 0;
        for (Direction direction : Direction.values())
        {
            i += sendEnergyTo(Math.min(maxPerTarget, maxAvailable - i), direction);
        }
        return i;
    }
    public int sendEnergyTo(int maxSend, Direction side)
    {
        if (maxSend == 0)
        {
            return 0;
        }
        BlockEntity tile = level.getBlockEntity(worldPosition.relative(side));
        if (tile != null)
        {
            return insertEnergy(tile, maxSend, side.getOpposite(), false);
        }
        return 0;
    }
    public static int sendEnergyTo(LevelReader world, BlockPos pos, int maxSend, Direction side)
    {
        if (maxSend == 0)
        {
            return 0;
        }
        BlockEntity tile = world.getBlockEntity(pos.relative(side));
        if (tile != null)
        {
            return insertEnergy(tile, maxSend, side.getOpposite(), false);
        }
        return 0;
    }
    public static int sendEnergyToAll(LevelReader world, BlockPos pos, int maxPerTarget, int maxAvailable)
    {
        int i = 0;
        for (Direction direction : Direction.values())
        {
            i += sendEnergyTo(world, pos, Math.min(maxPerTarget, maxAvailable - i), direction);
        }
        return i;
    }
    public static int insertEnergy(BlockEntity tile, int energy, Direction side, boolean simulate)
    {
        IEnergyStorage storage = getStorage(tile, side);
        if (storage != null && storage.canReceive())
        {
            return storage.receiveEnergy(energy, simulate);
        }
        return 0;
    }
    public static IEnergyStorage getStorage(BlockEntity tile, Direction side)
    {
        return getStorageFromProvider(tile, side);
    }
    public static IEnergyStorage getStorage(ItemStack stack)
    {
        if (!stack.isEmpty())
        {
            return getStorageFromProvider(stack, null);
        }
        return null;
    }
    public static IEnergyStorage getStorageFromProvider(ICapabilityProvider provider, Direction side)
    {
        LazyOptional<IEnergyStorage> op = provider.getCapability(ForgeCapabilities.ENERGY, side);
        if (op.isPresent())
        {
            return op.orElse(null);
        }
        LazyOptional<IEnergyStorage> fe = provider.getCapability(ForgeCapabilities.ENERGY, side);
        if (fe.isPresent())
        {
            return fe.orElse(null);
        }
        return null;
    }
	
    @Override
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox()
    {
        return new AABB(worldPosition.offset(-1, -1, -1), worldPosition.offset(2, 2, 2));
    }
    
    @Override
    public void saveAdditional(CompoundTag pTag)
    {
        pTag.putInt("iomode", this.ioMode.ordinal());
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag)
    {
    	ioMode = Mode.values()[pTag.getInt("iomode")];
        super.load(pTag);
    }
}
