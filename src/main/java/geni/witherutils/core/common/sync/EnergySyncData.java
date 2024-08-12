package geni.witherutils.core.common.sync;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import geni.witherutils.base.common.io.energy.IWitherEnergyStorage;
import geni.witherutils.base.common.io.energy.ImmutableMachineEnergyStorage;
import geni.witherutils.core.common.network.NetworkDataSlot;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record EnergySyncData(int energyStored, int maxEnergyStored, int maxEnergyUse) {
	
    public static Codec<EnergySyncData> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.INT.fieldOf("EnergyStored").forGetter(EnergySyncData::energyStored),
            Codec.INT.fieldOf("MaxEnergyStored").forGetter(EnergySyncData::maxEnergyStored),
            Codec.INT.fieldOf("MaxEnergyStored").forGetter(EnergySyncData::maxEnergyStored)
        ).apply(instance, EnergySyncData::new)
    );

    public static StreamCodec<ByteBuf, EnergySyncData> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT,
        EnergySyncData::energyStored,
        ByteBufCodecs.INT,
        EnergySyncData::maxEnergyStored,
        ByteBufCodecs.INT,
        EnergySyncData::maxEnergyStored,
        EnergySyncData::new
    );

    public static NetworkDataSlot.CodecType<EnergySyncData> DATA_SLOT_TYPE =
        new NetworkDataSlot.CodecType<>(CODEC, STREAM_CODEC.cast());

    public static EnergySyncData from(IWitherEnergyStorage storage)
    {
        return new EnergySyncData(
            storage.getEnergyStored(),
            storage.getMaxEnergyStored(),
            storage.getMaxEnergyUse()
        );
    }

    public ImmutableMachineEnergyStorage toImmutableStorage()
    {
        return new ImmutableMachineEnergyStorage(energyStored, maxEnergyStored, maxEnergyUse);
    }
}
