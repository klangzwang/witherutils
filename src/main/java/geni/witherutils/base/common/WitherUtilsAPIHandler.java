package geni.witherutils.base.common;

import geni.witherutils.api.WitherUtilsRegistry;

public enum WitherUtilsAPIHandler implements WitherUtilsRegistry.IWitherUtilsInterface {
	
    INSTANCE;

    public static WitherUtilsAPIHandler getInstance()
	{
        return INSTANCE;
    }
}
