package geni.witherutils.base.common.init;

import java.util.function.Supplier;

import com.mojang.serialization.Codec;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.api.misc.RedstoneControl;
import geni.witherutils.base.common.upgrade.SavedUpgrades;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class WUTComponents {
	
    public static final DeferredRegister.DataComponents DATACOMP_TYPES = DeferredRegister.createDataComponents(WitherUtilsRegistry.MODID);

    public static Supplier<DataComponentType<Float>> PICKAXE = DATACOMP_TYPES.registerComponentType("pickaxe",
            builder -> builder.persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT));
    
    public static Supplier<DataComponentType<Integer>> LEVEL = DATACOMP_TYPES.registerComponentType("level",
            builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
    
    public static Supplier<DataComponentType<Integer>> ENERGY = DATACOMP_TYPES.registerComponentType("energy",
            builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
    
    public static Supplier<DataComponentType<SimpleFluidContent>> ITEM_FLUID_CONTENT = DATACOMP_TYPES.registerComponentType("item_fluid_content",
            builder -> builder.persistent(SimpleFluidContent.CODEC).networkSynchronized(SimpleFluidContent.STREAM_CODEC));

    public static Supplier<DataComponentType<ItemContainerContents>> ITEMS = register("items", ItemContainerContents.CODEC, ItemContainerContents.STREAM_CODEC);
    public static final Supplier<DataComponentType<SavedUpgrades>> ITEM_UPGRADES = register("upgrades", SavedUpgrades.CODEC, SavedUpgrades.STREAM_CODEC);
    
    public static Supplier<DataComponentType<RedstoneControl>> REDSTONE_CONTROL =
            savedAndSynced("redstone_control", RedstoneControl.CODEC, RedstoneControl.STREAM_CODEC);

    private static <T> Supplier<DataComponentType<T>> savedAndSynced(String name, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec)
    {
        return DATACOMP_TYPES.register(name, () -> DataComponentType.<T>builder().persistent(codec).networkSynchronized(streamCodec).build());
    }
    
    private static <T> Supplier<DataComponentType<T>> register(String name, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec)
    {
        return DATACOMP_TYPES.registerComponentType(name, builder -> builder
                .persistent(codec)
                .networkSynchronized(streamCodec)
        );
    }
}
