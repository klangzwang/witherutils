package geni.witherutils.base.common.init;

import java.util.function.Supplier;

import geni.witherutils.api.lib.Names;
import geni.witherutils.base.common.adv.StandardTrigger;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

public class WUTCriterions {
	
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGER_TYPES = DeferredRegister.create(Registries.TRIGGER_TYPE, Names.MODID);

    public static final Supplier<StandardTrigger> ROOT = register("root");
    public static final Supplier<StandardTrigger> ANVIL = register("anvil");

    private static Supplier<StandardTrigger> register(String name)
    {
        return TRIGGER_TYPES.register(name, () -> new StandardTrigger(name));
    }
}