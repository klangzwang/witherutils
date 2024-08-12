package geni.witherutils.api;

import geni.witherutils.api.upgrade.IUpgradeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModLoadingContext;

public final class WitherUtilsRegistry {
	
    public static final String MODID = "witherutils";

    private static IWitherUtilsInterface instance;

    public static IWitherUtilsInterface getInstance() {
        return instance;
    }

    public static void init(IWitherUtilsInterface inter)
	{
        if (instance == null && ModLoadingContext.get().getActiveContainer().getModId().equals(MODID))
            instance = inter;
        else throw new IllegalStateException("Only witherutils is allowed to call this method!");
    }

    public static ResourceLocation loc(String path)
	{
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public interface IWitherUtilsInterface
	{
        IUpgradeRegistry getUpgradeRegistry();
    }
}
