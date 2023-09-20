package geni.witherutils.api.fisher;

import javax.annotation.Nonnull;

import geni.witherutils.core.common.helper.NamedEnum;

public enum FisherNotification implements NamedEnum<FisherNotification> {

	MORE_WATER("More Water"),
	OUTPUT_FULL("Output Full"),
	NO_ROD("No Rod"),
	NO_BREAD("No Bread"),
	FISHING("Fishing");

	private final @Nonnull String langStr;

	private FisherNotification(@Nonnull String langStr)
	{
		this.langStr = langStr;
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
