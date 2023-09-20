package geni.witherutils.api.thermal;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public class Thermal implements IThermal {
	
    private ThermalLevels thermal = ThermalLevels.TEMPERATE;
    private float temperature = 0F;

    public Thermal() {}
    
    @Override
    public void tick(Level level, BlockPos pos)
    {
        if (level instanceof ServerLevel)
        {
            temperature = ((ServerLevel) level).getBiome(pos).value().getHeightAdjustedTemperature(pos);
            thermal = ThermalLevels.from(temperature);
        }
    }

    @Override
    public ThermalLevels getThermalLevel()
    {
        return this.thermal;
    }
    @Override
    public float getTemperature()
    {
        return this.temperature;
    }
    @Override
    public CompoundTag serializeNBT()
    {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("thermal", (float)thermal.ordinal());
        tag.putFloat("temperature", getTemperature());
        return tag;
    }
    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        thermal = ThermalLevels.values()[(int)nbt.getFloat("thermal")];
        temperature = nbt.getFloat("temperature");
    }
}
