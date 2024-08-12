package geni.witherutils.base.common;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.api.upgrade.IUpgradeRegistry;
import geni.witherutils.base.common.upgrade.ApplicableUpgradesDB;

public enum WitherUtilsAPIHandler implements WitherUtilsRegistry.IWitherUtilsInterface {
	
    INSTANCE;

    public static WitherUtilsAPIHandler getInstance()
	{
        return INSTANCE;
    }
    
    @Override
    public IUpgradeRegistry getUpgradeRegistry()
    {
        return ApplicableUpgradesDB.getInstance();
    }
}
