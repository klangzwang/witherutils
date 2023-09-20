package geni.witherutils.base.common.world.levelgen;

import java.util.OptionalInt;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.init.WUTBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.ThreeLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.DarkOakFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.PineFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.ForkingTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

public class WUTTreeFeatures {

	public static final ResourceKey<ConfiguredFeature<?, ?>> ROTTENVARONE = FeatureUtils.createKey("rotten_var_one");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ROTTENVARTWO = FeatureUtils.createKey("rotten_var_two");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ROTTENVARTHR = FeatureUtils.createKey("rotten_var_thr");
	
    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context)
    {
    	register(context, WUTTreeFeatures.ROTTENVARONE, Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(WUTBlocks.ROTTEN_LOG.get()), new DarkOakTrunkPlacer(6, 2, 1), BlockStateProvider.simple(WUTBlocks.ROTTEN_LEAVES.get()), new DarkOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)), new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty()))).ignoreVines().build());
    	register(context, WUTTreeFeatures.ROTTENVARTWO, Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(WUTBlocks.ROTTEN_LOG.get()), new ForkingTrunkPlacer(5, 2, 2), BlockStateProvider.simple(WUTBlocks.ROTTEN_LEAVES.get()), new AcaciaFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)), new TwoLayersFeatureSize(1, 0, 2))).ignoreVines().build());
    	register(context, WUTTreeFeatures.ROTTENVARTHR, Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(WUTBlocks.ROTTEN_LOG.get()), new StraightTrunkPlacer(6, 4, 0), BlockStateProvider.simple(WUTBlocks.ROTTEN_LEAVES.get()), new PineFoliagePlacer(ConstantInt.of(1), ConstantInt.of(1), UniformInt.of(3, 4)), new TwoLayersFeatureSize(2, 0, 2))).ignoreVines().build());
    }
	
    public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name)
    {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(WitherUtils.MODID, name));
    }
    
    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> configuredFeatureKey, F feature, FC configuration)
    {
        context.register(configuredFeatureKey, new ConfiguredFeature<>(feature, configuration));
    }
}
