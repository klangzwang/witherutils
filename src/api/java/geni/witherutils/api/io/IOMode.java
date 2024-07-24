package geni.witherutils.api.io;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.function.IntFunction;

public enum IOMode implements StringRepresentable {

    NONE(0, "none", true, true, false, true),
    PUSH(1, "push", false, true, true, true),
    PULL(2, "pull", true, false, true, true),
    BOTH(3, "both", true, true, true, true),
    DISABLED(4, "disable", false, false, false, false);

    public static final Codec<IOMode> CODEC = StringRepresentable.fromEnum(IOMode::values);
    public static final IntFunction<IOMode> BY_ID = ByIdMap.continuous(key -> key.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, IOMode> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, v -> v.id);

    private final int id;
    private final String name;
    private final boolean input;
    private final boolean output;
    private final boolean force;
    private final boolean canConnect;

    IOMode(int id, String name, boolean input, boolean output, boolean force, boolean canConnect) {
        this.id = id;
        this.name = name;
        this.input = input;
        this.output = output;
        this.force = force;
        this.canConnect = canConnect;
    }

    public boolean canInput()
    {
        return input;
    }

    public boolean canOutput()
    {
        return output;
    }

    public boolean canConnect()
    {
        return canConnect;
    }

    public boolean canPush()
    {
        return canOutput() && canForce();
    }

    public boolean canPull()
    {
        return canInput() && canForce();
    }

    public boolean canForce()
    {
        return force;
    }

    @Override
    public String getSerializedName()
    {
        return name;
    }
}
