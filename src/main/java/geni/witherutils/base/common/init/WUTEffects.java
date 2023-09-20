package geni.witherutils.base.common.init;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.effect.BlindEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class WUTEffects {

	public static final DeferredRegister<MobEffect> EFFECT_TYPES = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, WitherUtils.MODID);

	public static final RegistryObject<BlindEffect> BLIND = EFFECT_TYPES.register("blinded", () -> new BlindEffect(MobEffectCategory.HARMFUL, 0));
}
