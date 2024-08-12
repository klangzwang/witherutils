//package geni.witherutils.base.common.init;
//
//import java.util.function.Function;
//
//import com.mojang.serialization.MapCodec;
//
//import geni.witherutils.api.WitherUtilsRegistry;
//import geni.witherutils.api.lib.Names;
//import geni.witherutils.base.common.block.deco.sliced.SlicedConcreteBlock;
//import geni.witherutils.core.common.registration.impl.AdapterDeferredRegister;
//import geni.witherutils.core.common.registration.impl.DeferredMapCodecHolder;
//import geni.witherutils.core.common.registration.impl.DeferredMapCodecRegister;
//import net.minecraft.core.Registry;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.resources.ResourceKey;
//import net.minecraft.world.level.block.Block;
//
//public class WUTMapCodecs {
//
//    private static <T> ResourceKey<Registry<MapCodec<? extends T>>> codecRegistryKey(@SuppressWarnings("unused") Class<T> compileTimeTypeValidator, String path)
//    {
//        return ResourceKey.createRegistryKey(WitherUtilsRegistry.loc(path));
//    }
//	
//	public static final ResourceKey<Registry<MapCodec<? extends Block>>> REGISTRY_NAME = codecRegistryKey(Block.class, "slurry_ingredient_type");
//	public static final DeferredMapCodecRegister<? extends Block> CODEC_TYPES = new DeferredMapCodecRegister<>(REGISTRY_NAME, WitherUtilsRegistry.MODID);
//	
//	public static final DeferredMapCodecHolder<SlicedConcreteBlock, SlicedConcreteBlock> COMPOUND = CODEC_TYPES.registerCodec("compound", () -> SlicedConcreteBlock.CODEC);
//}
