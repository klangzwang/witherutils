package geni.witherutils.base.common.init;

import java.util.function.Supplier;

import com.mojang.serialization.Codec;

import geni.witherutils.api.WitherUtilsRegistry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class WUTComponents {
	
    public static final DeferredRegister.DataComponents DATACOMP_TYPES = DeferredRegister.createDataComponents(WitherUtilsRegistry.MODID);

    public static Supplier<DataComponentType<Integer>> ENERGY = DATACOMP_TYPES.registerComponentType("energy",
            builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
}
