package geni.witherutils.base.common.block.generator.wind;

import geni.witherutils.api.soulbank.FixedScalable;
import geni.witherutils.api.soulbank.QuadraticScalable;
import geni.witherutils.api.soulbank.SoulBankModifier;
import geni.witherutils.base.common.base.WitherMachineEnergyGenBlockEntity;
import geni.witherutils.base.common.config.common.generator.WindGeneratorConfig;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.init.WUTParticles;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.base.common.io.item.MachineInventoryLayout;
import geni.witherutils.base.common.io.item.SingleSlotAccess;
import geni.witherutils.core.common.math.Vec3D;
import geni.witherutils.core.common.sync.BooleanDataSlot;
import geni.witherutils.core.common.sync.FloatDataSlot;
import geni.witherutils.core.common.sync.IntegerDataSlot;
import geni.witherutils.core.common.sync.SyncMode;
import geni.witherutils.core.common.util.SoulBankUtil;
import geni.witherutils.core.common.util.SoundUtil;
import geni.witherutils.core.common.util.StackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;

public class WindGeneratorBlockEntity extends WitherMachineEnergyGenBlockEntity implements MenuProvider {

	public static final SingleSlotAccess INPUT = new SingleSlotAccess();
    public static final SingleSlotAccess OUTPUT = new SingleSlotAccess();
    
    public static final QuadraticScalable CAPACITY = new QuadraticScalable(SoulBankModifier.ENERGY_CAPACITY, () -> WindGeneratorConfig.MAXENERGY.get());
    public static final QuadraticScalable TRANSFER = new QuadraticScalable(SoulBankModifier.ENERGY_TRANSFER, () -> WindGeneratorConfig.SENDPERTICK.get());
    public static final FixedScalable USAGE = new FixedScalable(() -> 0f); // not in use

    private int generationRate;
    private double efficiencyRate = WindGeneratorConfig.EFFECIENCYBASE.get();
    
    private float currentMultiplier = 0;

    @OnlyIn(Dist.CLIENT)
    public boolean soulBankInstalled;

    private float maxSpeed = 30F;
    private float acceleration = 0.25F;
    @OnlyIn(Dist.CLIENT)
    public float prevFanRotation;
    @OnlyIn(Dist.CLIENT)
    public float fanRotation;
    @OnlyIn(Dist.CLIENT)
    private float currentSpeed;

    public WindGeneratorBlockEntity(BlockPos worldPosition, BlockState blockState)
    {
        super(CAPACITY, TRANSFER, USAGE, WUTEntities.WIND_GENERATOR.get(), worldPosition, blockState);
        addDataSlot(new BooleanDataSlot(this::isSoulBankInstalled, p -> soulBankInstalled = p, SyncMode.WORLD));
        addDataSlot(new IntegerDataSlot(this::getGenerationRate, p -> generationRate = p, SyncMode.WORLD));
        addDataSlot(new FloatDataSlot(this::getEfficiencyRate, p -> efficiencyRate = p, SyncMode.GUI));
        addDataSlot(new FloatDataSlot(this::getCurrentMultiplier, p -> currentMultiplier = p, SyncMode.GUI));
    }

    @Override
    public void serverTick()
    {
        super.serverTick();

        if(canAct())
        {
            if(energyStorage.getEnergyStored() > 0)
            {
            	moveEnergy(Direction.UP, 1000);
            	if(!getInventory().getStackInSlot(1).isEmpty() && getInventory().getStackInSlot(2).isEmpty())
            		handleChargingItem(getInventory());
            }
            
            if(freeAirPlacement())
            {
                int scaledBy = (int) SoulBankUtil.getSoulBankData(getInventory().getStackInSlot(getSoulBankSlot())).get().getBase();
                
                if(level.getGameTime() % 20 == 0)
                	currentMultiplier = getMultiplier() * scaledBy;
            }
        }
        else
        	currentMultiplier = 0;
	}
    
    public boolean freeAirPlacement()
    {
    	BlockState frontState = level.getBlockState(worldPosition.relative(getCurrentFacing()));
    	BlockState backState = level.getBlockState(worldPosition.relative(getCurrentFacing().getOpposite()));
   		return frontState.isAir() && backState.isAir();
    }
    
    @Override
    public void clientTick()
    {
    	super.clientTick();

    	maxSpeed = generationRate * 2;
        prevFanRotation = fanRotation;
        if(freeAirPlacement() && soulBankInstalled && this.generationRate > 0 && energyStorage.getEnergyStored() < WindGeneratorConfig.MAXENERGY.get())
        {
            currentSpeed += acceleration;
            if(currentSpeed > maxSpeed)
            {
                currentSpeed = maxSpeed;
            }
            spawnParticles();
        }
        else
        {
            currentSpeed *= 0.95F;
        }
        fanRotation += currentSpeed;
    }
    
    private void handleChargingItem(IItemHandler handler)
    {
    	IEnergyStorage storage = this.getInventory().getStackInSlot(1).getCapability(ForgeCapabilities.ENERGY, null).resolve().get();
    	int received = storage.receiveEnergy(getGenerationRate(), false);
    	energyStorage.extractEnergy(received, false);

    	if(storage.getEnergyStored() == storage.getMaxEnergyStored())
    	{
    		this.getInventory().setStackInSlot(2, this.getInventory().getStackInSlot(1).copy());
    		this.getInventory().setStackInSlot(1, StackUtil.shrink(this.getInventory().getStackInSlot(1), 1));
    	}
    }

    public static long receiveEnergy(ItemStack stack, long maxReceive)
    {
    	return stack.getCapability(ForgeCapabilities.ENERGY).map(handler ->
    	handler.receiveEnergy(unsignedClampToInt(maxReceive), false)).orElse(0);
    }

    public static int unsignedClampToInt(long l)
    {
        return l > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)l;
    }

    @Override
    public MachineInventoryLayout getInventoryLayout()
    {
        return MachineInventoryLayout.builder()
                .soulbank()
                .setStackLimit(1)
                .inputSlot((slot, stack) -> stack.getCapability(ForgeCapabilities.ENERGY).isPresent()
                		&& stack.getCapability(ForgeCapabilities.ENERGY).resolve().get().getEnergyStored() <
                		stack.getCapability(ForgeCapabilities.ENERGY).resolve().get().getMaxEnergyStored()).slotAccess(INPUT)
                .setStackLimit(1)
                .outputSlot().slotAccess(OUTPUT)
                .build();
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
                SoundUtil.playSoundFromServer((ServerPlayer) player, WUTSounds.BUCKET.get(), 0.4f, 1.0f);
            }
            else
            {
                throw new IllegalStateException("Our named container provider is missing!");
            }
        }
        return InteractionResult.SUCCESS;
    }
    
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity)
    {
        return new WindGeneratorContainer(this, playerInventory, i);
    }
    
    public float getCurrentMultiplier()
    {
        return currentMultiplier;
    }
    
    private float getMultiplier()
    {
        if (level != null)
        {
            if (level.getFluidState(getBlockPos().above()).isEmpty() && level.canSeeSky(getBlockPos().above()))
            {
                int minY = Math.max(WindGeneratorConfig.GENERATIONMINY.get(), level.getMinBuildHeight());
                int maxY = Math.min(WindGeneratorConfig.GENERATIONMAXY.get(), level.dimensionType().logicalHeight());
                float clampedY = Math.min(maxY, Math.max(minY, getBlockPos().above().getY()));
                float minG = WindGeneratorConfig.GENERATIONMIN.get();
                float maxG = WindGeneratorConfig.GENERATIONMAX.get();
                float slope = (maxG - minG) / (maxY - minY);
                float toGen = minG += (slope * (clampedY - minY));
                return slope * (clampedY - minY) / toGen;
            }
        }
        return 0.0f;
    }
    
    @Override
    public boolean isGenerating()
    {
    	return canAct() && freeAirPlacement();
    }
    
    @Override
    public int getGenerationRate()
    {
   		return (int) (WindGeneratorConfig.GENERATIONMIN.get() * getCurrentMultiplier());
    }

	@Override
	public boolean hasEfficiencyRate()
	{
		return WindGeneratorConfig.HASEFFECIENCY.get();
	}

	@Override
	public float getEfficiencyRate()
	{
		if (level == null || getInventory().getStackInSlot(getSoulBankSlot()).isEmpty() || getGenerationRate() == 0)
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

    @OnlyIn(Dist.CLIENT)
    private void spawnParticles()
    {
        RandomSource rand = level.random;
        
        if (fanRotation <= 0)
        	return;

        Vec3D spawn;
        Vec3D dest;

        for(int k = 0; k < 2; k++)
        {
            if (fanRotation > 10)
            {
                for (int i = 0; i < 2; i++)
                {
                    spawn = this.getParticleSpawn(rand);
                    dest = this.getParticleDest(rand);
                   	level.addParticle(WUTParticles.WIND.get(), spawn.x, spawn.y, spawn.z, dest.x, dest.y, dest.z);
                }
            }
            else if (rand.nextInt(Math.max(1, 10 - (int) fanRotation)) == 0)
            {
                spawn = this.getParticleSpawn(rand);
                dest = this.getParticleDest(rand);
                level.addParticle(WUTParticles.WIND.get(), spawn.x, spawn.y, spawn.z, dest.x, dest.y, dest.z);
            }
        }
    }
	
    @OnlyIn(Dist.CLIENT)
    private Vec3D getParticleSpawn(RandomSource random)
    {
        double range = 15 / 6D;
    	
        if (this.getCurrentFacing().getAxis() == Direction.Axis.Z)
        {
            return new Vec3D(getBlockPos().relative(this.getCurrentFacing())).add(
    				(random.nextFloat() - 0.5F) * range,
    				(random.nextFloat() - 0.5F) * range,
    				random.nextFloat() + 3.0F);
        }
        else if (this.getCurrentFacing().getAxis() == Direction.Axis.Y)
        {
            return new Vec3D(getBlockPos().relative(this.getCurrentFacing())).add(
    				(random.nextFloat() - 0.5F) * range,
    				(random.nextFloat() - 0.5F) * range,
    				(random.nextFloat() - 0.5F) * range);
        }
        else
        {
            return new Vec3D(getBlockPos().relative(this.getCurrentFacing())).add(
    				(random.nextFloat() - 0.5F) * range,
    				(random.nextFloat() - 0.5F) * range,
    				(random.nextFloat() - 0.5F) * range);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private Vec3D getParticleDest(RandomSource random)
    {
        return Vec3D.getCenter(getBlockPos()).add(0.0F, 0.0F, 0.0F);
    }
}
