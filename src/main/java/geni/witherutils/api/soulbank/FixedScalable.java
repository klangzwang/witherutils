package geni.witherutils.api.soulbank;

import net.minecraftforge.common.util.NonNullSupplier;

import java.util.function.Supplier;

public record FixedScalable(Supplier<Float> value) implements ISoulBankScalable {
    
    @Override
    public Supplier<Float> scaleF(NonNullSupplier<ISoulBankData> data) {
        return value;
    }

    @Override
    public Supplier<Integer> scaleI(NonNullSupplier<ISoulBankData> data) {
        return () -> Math.round(value.get());
    }
}
