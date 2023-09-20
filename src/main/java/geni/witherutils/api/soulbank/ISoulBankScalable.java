package geni.witherutils.api.soulbank;

import net.minecraftforge.common.util.NonNullSupplier;

import java.util.function.Supplier;

public interface ISoulBankScalable {
    
    Supplier<Float> scaleF(NonNullSupplier<ISoulBankData> data);
    Supplier<Integer> scaleI(NonNullSupplier<ISoulBankData> data);
}
