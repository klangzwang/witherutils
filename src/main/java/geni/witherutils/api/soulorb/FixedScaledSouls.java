package geni.witherutils.api.soulorb;

import net.minecraftforge.common.util.NonNullSupplier;

import java.util.function.Supplier;

public record FixedScaledSouls(Supplier<Integer> value) implements ISoulOrbScalable {
    
    @Override
    public Supplier<Integer> scaleI(NonNullSupplier<ISoulOrbData> data)
    {
        return () -> Math.round(value.get());
    }
}
