package geni.witherutils.core.common.util;

import geni.witherutils.WitherUtils;
import geni.witherutils.api.soulbank.ISoulBankData;
import geni.witherutils.api.soulbank.SoulBankModifier;
import geni.witherutils.base.common.init.WUTCapabilities;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = WitherUtils.MODID)
public class SoulBankUtil {

    public static void getTooltip(ItemStack stack, List<Component> tooltipComponents)
    {
    }

    private static String getFlavor(int flavor)
    {
        return "description.witherutils.soulbank.flavor." + flavor;
    }

    private static MutableComponent getBaseText(float base)
    {
        MutableComponent t = Component.translatable("description.witherutils.soulbank.base." + (int) Math.ceil(base));
        t.withStyle(ChatFormatting.ITALIC);
        return t;
    }

    private static MutableComponent getTypeText(String type)
    {
        MutableComponent t = Component.translatable("description.witherutils.soulbank.type." + type);
        t.withStyle(ChatFormatting.ITALIC);
        return t;
    }

    private static MutableComponent getGradeText(float grade)
    {
        MutableComponent t = Component.translatable("description.witherutils.soulbank.grade." + (int) Math.ceil(grade));
        t.withStyle(ChatFormatting.ITALIC);
        return t;
    }

    public static Optional<ISoulBankData> getSoulBankData(ItemStack itemStack)
    {
        LazyOptional<ISoulBankData> soulBankDataCap = itemStack.getCapability(WUTCapabilities.SOULBANK);
        if (soulBankDataCap.isPresent())
            return Optional.of(soulBankDataCap.orElseThrow(NullPointerException::new));
        return Optional.empty();
    }

    public static boolean isSoulBank(ItemStack itemStack)
    {
        LazyOptional<ISoulBankData> soulBankDataCap = itemStack.getCapability(WUTCapabilities.SOULBANK);
        return soulBankDataCap.isPresent();
    }

    public static SoulBankModifier getRandomModifier(RandomSource randomSource)
    {
        return SoulBankModifier.values()[randomSource.nextInt(SoulBankModifier.values().length)];
    }
}
