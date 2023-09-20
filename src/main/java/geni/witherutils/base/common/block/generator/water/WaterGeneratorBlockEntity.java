package geni.witherutils.base.common.block.generator.water;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import geni.witherutils.api.io.energy.EnergyIOMode;
import geni.witherutils.api.soulbank.FixedScalable;
import geni.witherutils.api.soulbank.QuadraticScalable;
import geni.witherutils.api.soulbank.SoulBankModifier;
import geni.witherutils.base.common.base.WitherMachineEnergyGenBlockEntity;
import geni.witherutils.base.common.config.common.generator.WaterGeneratorConfig;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.base.common.io.energy.WitherEnergyStorage;
import geni.witherutils.base.common.io.item.MachineInventoryLayout;
import geni.witherutils.base.common.io.item.SingleSlotAccess;
import geni.witherutils.core.common.sync.BooleanDataSlot;
import geni.witherutils.core.common.sync.FloatDataSlot;
import geni.witherutils.core.common.sync.IntegerDataSlot;
import geni.witherutils.core.common.sync.SyncMode;
import geni.witherutils.core.common.util.SoulBankUtil;
import geni.witherutils.core.common.util.SoundUtil;
import geni.witherutils.core.common.util.StackUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;

public class WaterGeneratorBlockEntity extends WitherMachineEnergyGenBlockEntity implements MenuProvider {

	public static final SingleSlotAccess INPUT = new SingleSlotAccess();
    public static final SingleSlotAccess OUTPUT = new SingleSlotAccess();
    
    public static final QuadraticScalable CAPACITY = new QuadraticScalable(SoulBankModifier.ENERGY_CAPACITY, () -> WaterGeneratorConfig.MAXENERGY.get());
    public static final QuadraticScalable TRANSFER = new QuadraticScalable(SoulBankModifier.ENERGY_TRANSFER, () -> WaterGeneratorConfig.SENDPERTICK.get());
    public static final FixedScalable USAGE = new FixedScalable(() -> 0f); // not in use

    private int generationRate;
    private double efficiencyRate = WaterGeneratorConfig.EFFECIENCYBASE.get();
    private float maxSpeed = 30F;
    private float acceleration = 0.25F;
    
    @OnlyIn(Dist.CLIENT)
    public boolean soulBankInstalled;
    
    @OnlyIn(Dist.CLIENT)
    public float prevFanRotation;
    @OnlyIn(Dist.CLIENT)
    public float fanRotation;
    @OnlyIn(Dist.CLIENT)
    private float currentSpeed;
    
    public WaterGeneratorBlockEntity(BlockPos worldPosition, BlockState blockState)
    {
        super(CAPACITY, TRANSFER, USAGE, WUTEntities.WATER_GENERATOR.get(), worldPosition, blockState);
        addDataSlot(new BooleanDataSlot(this::isSoulBankInstalled, p -> soulBankInstalled = p, SyncMode.WORLD));
        addDataSlot(new IntegerDataSlot(this::getGenerationRate, p -> generationRate = p, SyncMode.WORLD));
        addDataSlot(new FloatDataSlot(this::getEfficiencyRate, p -> efficiencyRate = p, SyncMode.GUI));
    }

    @Override
    protected WitherEnergyStorage createEnergyStorage(EnergyIOMode energyIOMode, Supplier<Integer> capacity, Supplier<Integer> transferRate, Supplier<Integer> usageRate)
    {
        return new WitherEnergyStorage(getIOConfig(), EnergyIOMode.Output, capacity, transferRate, usageRate)
        {
            @Override
            public LazyOptional<IEnergyStorage> getCapability(@Nullable Direction side)
            {
            	if(side != Direction.UP)
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

        if(!canAct())
            return;

        if(energyStorage.getEnergyStored() > 0)
        {
        	moveEnergy(Direction.UP, 1000);
        	if(!getInventory().getStackInSlot(1).isEmpty() && getInventory().getStackInSlot(2).isEmpty())
        		handleChargingItem(getInventory());
        }
	}

    @Override
    public void clientTick()
    {
    	super.clientTick();

        maxSpeed = 60 / 30 * this.generationRate;
        
        prevFanRotation = fanRotation;
        if(soulBankInstalled && this.generationRate > 0 && energyStorage.getEnergyStored() < WaterGeneratorConfig.MAXENERGY.get())
        {
            if(level.getGameTime() % (22 - this.generationRate) == 0)
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
                for(int i = 0; i < (int) this.generationRate / 4; i++)
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
        return new WaterGeneratorContainer(this, playerInventory, i);
    }
    
    @Override
    public boolean isGenerating()
    {
    	return getGenerationRate() > 0;
    }
    
    @Override
    public int getGenerationRate()
    {
        if(getInventory().getStackInSlot(getSoulBankSlot()).isEmpty())
        	return 0;

        int v = 0;
        int scaledBy = (int) SoulBankUtil.getSoulBankData(getInventory().getStackInSlot(getSoulBankSlot())).get().getBase();
        
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
                        v += (value + 1) / 3 * scaledBy;
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
}
