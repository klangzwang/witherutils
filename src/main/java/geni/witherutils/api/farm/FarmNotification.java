package geni.witherutils.api.farm;

import javax.annotation.Nonnull;

import geni.witherutils.core.common.helper.NamedEnum;

public enum FarmNotification implements NamedEnum<FarmNotification> {

	OUTPUT_FULL("outputfull"),
	NO_SEEDS("noseeds", true),
	NO_AXE("noaxe", true),
	NO_HOE("nohoe", true),
	NO_TREETAP("notreetap", true),
	NO_POWER("nopower"),
	NO_SHEARS("noshears", true),
	NO_BANK("nosoulbank"),
	NO_WATER("nowater");

	private final @Nonnull String langStr;
	private final boolean autoCleanup;

	private FarmNotification(@Nonnull String langStr)
	{
		this(langStr, false);
	}

	private FarmNotification(@Nonnull String langStr, boolean autoCleanup)
	{
		this.langStr = "info.witherutils.farm." + langStr;
		this.autoCleanup = autoCleanup;
	}

	public boolean isAutoCleanup()
	{
		return autoCleanup;
	}

	@Override
	public @Nonnull String getName()
	{
		return langStr;
	}

	@Override
	public String[] getDescription()
	{
		return null;
	}
}
