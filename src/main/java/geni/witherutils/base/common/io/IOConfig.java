package geni.witherutils.base.common.io;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

import geni.witherutils.api.io.IIOConfig;
import geni.witherutils.api.io.IOMode;
import geni.witherutils.api.io.ISideConfig;
import geni.witherutils.base.common.init.WUTCapabilities;

import java.util.EnumMap;
import java.util.Map;

public class IOConfig implements IIOConfig {

    private final EnumMap<Direction, IOMode> config = new EnumMap<>(Direction.class);
    private final EnumMap<Direction, LazyOptional<SideAccess>> sideAccessCache = new EnumMap<>(Direction.class);

    public IOConfig()
    {
        for (Direction value : Direction.values())
        {
            config.put(value, IOMode.NONE);
        }
    }

    @Override
    public IOMode getMode(Direction side)
    {
        return config.get(translateSide(side));
    }

    @Override
    public void setMode(Direction side, IOMode mode)
    {
        Direction relSide = translateSide(side);
        IOMode oldMode = config.get(relSide);
        config.put(relSide, mode);
        onChanged(side, oldMode, mode);
    }

    @Override
    public boolean supportsMode(Direction side, IOMode state)
    {
        return true;
    }

    @Override
    public boolean renderOverlay()
    {
        return true;
    }

    private Direction translateSide(Direction side)
    {
        Direction south = getBlockFacing();
        return switch (side) {
        case NORTH -> south.getOpposite();
        case SOUTH -> south;
        case WEST -> south.getCounterClockWise();
        case EAST -> south.getClockWise();
        default -> side;
        };
    }

    @Override
    public Capability<ISideConfig> getCapabilityType()
    {
        return WUTCapabilities.SIDECONFIG;
    }

    public LazyOptional<ISideConfig> getCapability(@Nullable Direction side)
    {
        if (side == null)
            return LazyOptional.empty();
        return sideAccessCache.computeIfAbsent(side, dir -> LazyOptional.of(() -> new SideAccess(this, dir))).cast();
    }

    @Override
    public void invalidateSide(@Nullable Direction side)
    {
        if (side == null)
            return;

        if (sideAccessCache.containsKey(side))
        {
            sideAccessCache.get(side).invalidate();
            sideAccessCache.remove(side);
        }
    }

    public void invalidateCaps()
    {
        for (LazyOptional<SideAccess> access : sideAccessCache.values())
        {
            access.invalidate();
        }
    }

    protected void onChanged(Direction side, IOMode oldMode, IOMode newMode) {}

    protected Direction getBlockFacing()
    {
        return Direction.SOUTH;
    }

    @Override
    public CompoundTag serializeNBT()
    {
        CompoundTag nbt = new CompoundTag();
        ListTag listNbt = new ListTag();
        for (Map.Entry<Direction, IOMode> entry : config.entrySet())
        {
            CompoundTag entryNbt = new CompoundTag();
            entryNbt.putInt("direction", entry.getKey().ordinal());
            entryNbt.putInt("state", entry.getValue().ordinal());
            listNbt.add(entryNbt);
        }
        nbt.put("data", listNbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        ListTag listNbt = nbt.getList("data", Tag.TAG_COMPOUND);
        for (Tag tag : listNbt)
        {
            CompoundTag entryNbt = (CompoundTag) tag;
            config.put(Direction.values()[entryNbt.getInt("direction")], IOMode.values()[entryNbt.getInt("state")]);
        }
    }

    private record SideAccess(IOConfig config, Direction side) implements ISideConfig
    {
        @Override
        public IOMode getMode()
        {
            return config.getMode(side);
        }

        @Override
        public void setMode(IOMode mode)
        {
            config.setMode(side, mode);
        }

        @Override
        public void cycleMode()
        {
            config.cycleMode(side);
        }
    }
}
