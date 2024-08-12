package geni.witherutils.core.common.fakeplayer;

import javax.annotation.Nonnull;

import geni.witherutils.core.common.helper.NamedEnum;

public enum FakePlayerNotification implements NamedEnum<FakePlayerNotification> {

	NOT_READY("not_ready"),
	READY("ready");

	private final @Nonnull String langStr;
	private final boolean autoCleanup;

	private FakePlayerNotification(@Nonnull String langStr)
	{
		this(langStr, false);
	}

	private FakePlayerNotification(@Nonnull String langStr, boolean autoCleanup)
	{
		this.langStr = "info.witherutils.fakeplayer." + langStr;
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
