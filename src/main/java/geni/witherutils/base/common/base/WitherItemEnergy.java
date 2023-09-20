package geni.witherutils.base.common.base;

import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import geni.witherutils.core.common.util.NBTUtil;
import geni.witherutils.core.common.util.PlayerUtil;
import geni.witherutils.core.common.util.StackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.energy.IEnergyStorage;

public class WitherItemEnergy implements IEnergyStorage {

    public static final WitherItemEnergy EMPTY = WitherItemEnergy.create(0);
    public static final int MAX = Integer.MAX_VALUE;
    public static final int MIN = 0;

    private int capacity;
    private int stored;
    private int maxExtract;
    private int maxReceive;

    /**
     * 
     * Constructor
     * 
     */
    public WitherItemEnergy(WitherItemEnergy energy)
    {
        this(energy.capacity, energy.maxExtract, energy.maxReceive);
        setStored(energy.stored);
    }
    public WitherItemEnergy(int capacity, int maxExtract, int maxReceive)
    {
        this.capacity = capacity;
        this.maxExtract = maxExtract;
        this.maxReceive = maxReceive;
    }

    /**
     * 
     * Item/Provider
     * 
     */
    public static class Item extends WitherItemEnergy {

        private final ItemStack stack;

        public Item(ItemStack stack, Item energy)
        {
            super(energy);
            this.stack = stack;
        }

        public Item(ItemStack stack, int capacity, int maxExtract, int maxReceive)
        {
            super(capacity, maxExtract, maxReceive);
            this.stack = stack;
            read(StackUtil.getTagOrEmpty(stack).getCompound(NBTUtil.TAG_STORABLE_STACK), false, false);
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate)
        {
            int energy = super.receiveEnergy(maxReceive, simulate);
            if(!simulate)
            {
                write(this.stack.getOrCreateTagElement(NBTUtil.TAG_STORABLE_STACK), false, false);
            }
            return energy;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate)
        {
            int energy = super.extractEnergy(maxExtract, simulate);
            if(!simulate)
            {
                write(this.stack.getOrCreateTagElement(NBTUtil.TAG_STORABLE_STACK), false, false);
            }
            return energy;
        }

        public static class Provider implements ICapabilityProvider
        {
            private final ItemStack stack;
            private final int capacity;
            private final int maxExtract;
            private final int maxReceive;

            public Provider(ItemStack stack, int capacity, int maxExtract, int maxReceive)
            {
                this.stack = stack;
                this.capacity = capacity;
                this.maxExtract = maxExtract;
                this.maxReceive = maxReceive;
            }

            @Override
            public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side)
            {
                return cap == ForgeCapabilities.ENERGY ? LazyOptional.of(() -> {
                    return new Item(this.stack, this.capacity, this.maxExtract, this.maxReceive);
                }).cast() : LazyOptional.empty();
            }
        }
    }

    /**
     * 
     * Create
     * 
     */
    public static WitherItemEnergy create(int capacity)
    {
        return create(capacity, capacity, capacity);
    }
    public static WitherItemEnergy create(int capacity, int transfer)
    {
        return create(capacity, transfer, transfer);
    }
    public static WitherItemEnergy create(int capacity, int maxExtract, int maxReceive)
    {
        return new WitherItemEnergy(capacity, maxExtract, maxReceive);
    }

    /**
     * 
     * CHARGING
     * 
     */
    public int chargeInventory(Player player, Predicate<ItemStack> checker)
    {
        int l = 0;
        for(ItemStack stack1 : PlayerUtil.invStacks(player))
        {
            if (stack1.isEmpty() || !isPresent(stack1) || !checker.test(stack1)) continue;
            int amount = Math.min(getMaxExtract(), getEnergyStored());
            if (amount <= 0) break;
            int received = WitherItemEnergy.receive(stack1, amount, false);
            l += extractEnergy(received, false);
        }
        return l;
    }
    public int chargeSolarInventory(Player player, Predicate<ItemStack> checker)
    {
        int l = 0;
        for(ItemStack stack1 : PlayerUtil.invArmorStacks(player))
        {
            int amount = WitherItemEnergy.calculateSunEnergy(player.level(), player.blockPosition());
            if(amount <= 0) break;
            int received = WitherItemEnergy.receive(stack1, amount, false);
            l += receiveEnergy((int) received, false);
        }
        return l;
    }
    public int getMaxExtract()
    {
        return this.maxExtract;
    }

    /**
     * 
     * GETTER/SETTER
     * 
     */

    public int getStored()
    {
        return Math.min(this.stored, this.capacity);
    }
    public WitherItemEnergy setStored(int stored)
    {
        this.stored = Math.max(0, Math.min(this.capacity, stored));
        return this;
    }
    public int getCapacity()
    {
        return this.capacity;
    }
    public WitherItemEnergy setCapacity(int capacity)
    {
        this.capacity = Math.max(0, Math.min(MAX, capacity));
        if(this.stored > this.capacity)
        {
            this.stored = this.capacity;
        }
        return this;
    }
    public WitherItemEnergy setTransfer(int transfer)
    {
        this.maxReceive = transfer;
        this.maxExtract = transfer;
        return this;
    }
    public int getTransfer()
    {
        return Math.max(this.maxExtract, this.maxReceive);
    }

    /**
     * 
     * IEnergyStorage
     * 
     */
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate)
    {
        if(!canReceive())
            return 0;
        int received = Math.min(this.capacity - this.stored, Math.min(this.maxReceive, maxReceive));
        if(!simulate)
            produce(received);
        return safeInt(received);
    }
    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
        if(!canExtract())
            return 0;
        int extracted = Math.min(this.stored, Math.min(this.maxExtract, maxExtract));
        if(!simulate)
            consume(extracted);
        return safeInt(extracted);
    }
    @Override
    public int getEnergyStored()
    {
        return safeInt(this.stored);
    }
    @Override
    public int getMaxEnergyStored()
    {
        return safeInt(this.capacity);
    }
    @Override
    public boolean canExtract()
    {
        return this.maxExtract > 0 && !isEmpty();
    }
    @Override
    public boolean canReceive()
    {
        return this.maxReceive > 0 && !isFull();
    }
    public boolean isEmpty()
    {
        return this.stored <= 0;
    }
    public boolean isFull()
    {
        return this.stored > 0 && this.stored >= this.capacity;
    }

    /**
     * 
     * Produce/Consume
     * 
     */
    public int produce(int amount)
    {
        int min = Math.min(this.capacity - this.stored, Math.max(0, amount));
        this.stored += min;
        return min;
    }
    public int consume(int amount)
    {
        int min = Math.min(this.stored, Math.max(0, amount));
        this.stored -= min;
        return min;
    }
    public static int consume(ItemStack stack, int amount)
    {
    	int stored = get(stack).orElse(EMPTY).getEnergyStored();
        int min = Math.min(stored, Math.max(0, amount));
        stored -= min;
        return min;
    }

    /**
     * 
     * Extract/Receive
     * 
     */
    public static int extract(ItemStack stack, int energy, boolean simulate)
    {
        return get(stack).orElse(EMPTY).extractEnergy(safeInt(energy), simulate);
    }
    public static int receive(ItemStack stack, int energy, boolean simulate)
    {
        return get(stack).orElse(EMPTY).receiveEnergy(safeInt(energy), simulate);
    }

    /**
     * 
     * Present
     * 
     */
    public static void ifPresent(ItemStack stack, NonNullConsumer<? super IEnergyStorage> consumer)
    {
        get(stack).ifPresent(consumer);
    }
    public static boolean isPresent(@Nullable BlockEntity tile, @Nullable Direction direction)
    {
        return get(tile, direction).isPresent();
    }
    public static LazyOptional<IEnergyStorage> get(@Nullable BlockEntity tile, @Nullable Direction direction)
    {
        return tile == null ? LazyOptional.empty() : tile.getCapability(ForgeCapabilities.ENERGY, direction != null ? direction.getOpposite() : null);
    }
    public static boolean isPresent(ItemStack stack)
    {
        return get(stack).isPresent();
    }
    public static LazyOptional<IEnergyStorage> get(ItemStack stack)
    {
        return !stack.isEmpty() ? stack.getCapability(ForgeCapabilities.ENERGY, null) : LazyOptional.empty();
    }

    /**
     * 
     * Tag
     * 
     */
    public WitherItemEnergy read(CompoundTag nbt, boolean capacity, boolean transfer)
    {
        return read(nbt, "main_energy", capacity, transfer);
    }
    public WitherItemEnergy read(CompoundTag nbt, String key, boolean capacity, boolean transfer)
    {
        if(capacity)
        {
            this.capacity = nbt.getInt("energy_capacity_" + key);
        }
        this.stored = nbt.getInt("energy_stored_" + key);
        if(transfer)
        {
            this.maxExtract = nbt.getInt("max_extract_" + key);
            this.maxReceive = nbt.getInt("max_receive_" + key);
        }
        return this;
    }
    public CompoundTag write(boolean capacity, boolean transfer)
    {
        return write("main_energy", capacity, transfer);
    }
    public CompoundTag write(String key, boolean capacity, boolean transfer)
    {
        return write(new CompoundTag(), key, capacity, transfer);
    }
    public CompoundTag write(CompoundTag nbt, boolean capacity, boolean transfer)
    {
        return write(nbt, "main_energy", capacity, transfer);
    }
    public CompoundTag write(CompoundTag nbt, String key, boolean capacity, boolean transfer)
    {
        if(capacity)
        {
            nbt.putInt("energy_capacity_" + key, this.capacity);
        }
        nbt.putInt("energy_stored_" + key, this.stored);
        if(transfer)
        {
            nbt.putInt("max_extract_" + key, this.maxExtract);
            nbt.putInt("max_receive_" + key, this.maxReceive);
        }
        return nbt;
    }
    
    /**
     * 
     * SolarPowering
     * 
     */
    public static int calculateSunEnergy(Level level, BlockPos pos)
    {
    	int baseRatio = WitherItemEnergy.calculateLightRatio(level, pos);
        return calculateLocalLightRatio(level, pos, baseRatio);
    }
    static boolean isSolarPowered(@Nonnull Level level, BlockPos pos)
    {
    	return level.canSeeSky(pos.above());
    }
	static int calculateLocalLightRatio(@Nonnull Level level, BlockPos pos, int baseRatio)
	{
		int ratio = isSolarPowered(level, pos) ? baseRatio : 0;
		return ratio;
	}
	public static int calculateLightRatio(@Nonnull Level level, BlockPos pos)
	{
		int lightValue = level.getBrightness(LightLayer.SKY, pos) - level.getSkyDarken();
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
		lightValue = (int) Mth.clamp(lightValue, 0, 15);

		return lightValue / 4 * 2;
	}
	
    public static int safeInt(int value) {
        return value > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) value;
    }
    public static int safeInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }
}
