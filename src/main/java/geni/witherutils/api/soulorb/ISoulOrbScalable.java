package geni.witherutils.api.soulorb;

import net.minecraftforge.common.util.NonNullSupplier;

import java.util.function.Supplier;

public interface ISoulOrbScalable {
    
    Supplier<Integer> scaleI(NonNullSupplier<ISoulOrbData> data);
}
