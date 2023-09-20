package geni.witherutils.base.common.integration;

import geni.witherutils.WitherUtils;

public class ModIntegration {
	
    public static final ModIntegration INSTANCE = new ModIntegration();
    private static final String BASE = "geni.witherutils.base.common.integration.";
    private static final ClassLoader LOADER = WitherUtils.class.getClassLoader();

    public void init()
    {
        this.loadAPIs();
        this.sendComms();
    }

    private void loadAPIs() {
    }

    @SuppressWarnings("unused")
	private void loadAPI(String name) {
        try {
            LOADER.loadClass(BASE + name).getMethod("init", new Class[0]).invoke(null, new Object[0]);
        }
        catch (Throwable throwable) {
        }
    }

    private void sendComms()
    {
//        if (ModList.get().isLoaded("industrialforegoing"))
//        {
//            this.sendIsolatedComms("industrialforegoing.IndustrialForegoing_Active");
//        }
//        if (ModList.get().isLoaded("theoneprobe"))
//        {
//            this.sendIsolatedComms("theoneprobe.TheOneProbe_Active");
//        }
    }

    @SuppressWarnings("unused")
	private void sendIsolatedComms(String name) {
        try {
            LOADER.loadClass(BASE + name).getMethod("sendComms", new Class[0]).invoke(null, new Object[0]);
        }
        catch (Throwable throwable) {
        }
    }
}
