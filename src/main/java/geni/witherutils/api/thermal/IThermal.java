package geni.witherutils.api.thermal;

import javax.annotation.Nonnull;

import geni.witherutils.base.common.init.WUTCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public interface IThermal extends INBTSerializable<CompoundTag> {
	
    void tick(Level level, BlockPos pos);
    ThermalLevels getThermalLevel();
    float getTemperature();

    public static class Provider implements ICapabilitySerializable<CompoundTag>
    {
        private IThermal thermal;

        public Provider(IThermal thermal)
        {
            this.thermal = thermal;
        }
        public Provider()
        {
            this.thermal = new Thermal();
        }

        @SuppressWarnings("unchecked")
		@Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side)
        {
            return cap == WUTCapabilities.THERMAL ? (LazyOptional<T>) LazyOptional.of(() -> (T) thermal) : LazyOptional.empty();
        }
        @Override
        public CompoundTag serializeNBT()
        {
            return thermal.serializeNBT();
        }
        @Override
        public void deserializeNBT(CompoundTag nbt)
        {
            thermal.deserializeNBT(nbt);
        }
    }
}
