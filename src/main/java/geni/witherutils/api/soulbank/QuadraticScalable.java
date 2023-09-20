package geni.witherutils.api.soulbank;

import net.minecraftforge.common.util.NonNullSupplier;

import java.util.function.Supplier;

public record QuadraticScalable(SoulBankModifier modifier, Supplier<Integer> base) implements ISoulBankScalable {

    @Override
    public Supplier<Float> scaleF(NonNullSupplier<ISoulBankData> data)
    {
        return () -> scale(base.get(), data.get().getModifier(modifier));
    }
    @Override
    public Supplier<Integer> scaleI(NonNullSupplier<ISoulBankData> data)
    {
        return () -> Math.round(scale(base.get(), data.get().getModifier(modifier)));
    }
    private static float scale(float base, float level)
    {
        return base * level * level;
    }
}
