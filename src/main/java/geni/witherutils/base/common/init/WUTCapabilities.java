package geni.witherutils.base.common.init;

import java.util.Map;

import javax.annotation.Nullable;

import geni.witherutils.WitherUtils;
import geni.witherutils.api.font.ISoulieTextProvider;
import geni.witherutils.api.font.SoulieInkProvider;
import geni.witherutils.api.io.ISideConfig;
import geni.witherutils.api.soulbank.ISoulBankData;
import geni.witherutils.api.soulorb.ISoulOrbData;
import geni.witherutils.api.steelupable.ISteelUpable;
import geni.witherutils.api.thermal.IThermal;
import geni.witherutils.core.common.helper.SoulieInkHelper;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WitherUtils.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WUTCapabilities {
    
    private static final Map<Class<?>, Capability<?>> TOKENS = new Object2ObjectOpenHashMap<>();
    public static final Capability<ISoulBankData> SOULBANK = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<ISoulieTextProvider> SOULIETEXT = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<ISideConfig> SIDECONFIG = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<IThermal> THERMAL = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<ISteelUpable> STEELUPABLE = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<ISoulOrbData> SOULORB = CapabilityManager.get(new CapabilityToken<>() {});
    
    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event)
    {
        event.register(ISoulBankData.class);
        event.register(ISoulieTextProvider.class);
        event.register(ISideConfig.class);
        event.register(IThermal.class);
        event.register(ISteelUpable.class);
        event.register(ISoulOrbData.class);
        
        TOKENS.put(ISoulBankData.class, SOULBANK);
        TOKENS.put(ISoulieTextProvider.class, SOULIETEXT);
        TOKENS.put(ISideConfig.class, SIDECONFIG);
        TOKENS.put(IThermal.class, THERMAL);
        TOKENS.put(ISteelUpable.class, STEELUPABLE);
        TOKENS.put(ISoulOrbData.class, SOULORB);
    }
    
    public static void attachBlockEntityCapabilities(AttachCapabilitiesEvent<BlockEntity> event)
    {
        if (SoulieInkHelper.isEnabled() && (event.getObject() instanceof SignBlockEntity ||  event.getObject() instanceof HangingSignBlockEntity))
        {
            event.addCapability(WitherUtils.loc("soulie_ink"), new SoulieInkProvider());
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <T> Capability<T> getToken(Class<T> capClass)
    {
       return (Capability<T>) TOKENS.get(capClass);
    }
    @org.jetbrains.annotations.Nullable
    public static <T> T get(ICapabilityProvider provider, Capability<T> cap)
    {
        return provider.getCapability(cap).orElse(null);
    }
    @org.jetbrains.annotations.Nullable
    public static <T> T get(ICapabilityProvider provider, Capability<T> cap, Direction dir)
    {
        return provider.getCapability(cap, dir).orElse(null);
    }
}
