package geni.witherutils.api.farm;

import geni.witherutils.core.common.helper.NamedEnum;

public enum FarmingAction implements NamedEnum<FarmingAction> {
	
	IDLE("Idle", 0, ""),
    TILLING("Tilling", 25, ""),
    MOISTURE("Moisture", 40, ""),
    PLANTING("Planting", 15, ""),
    FERTILIZE("Fertilize", 0, ""),
    HARVEST("Harvest", 10, "");

    private final String name;
    private final String[] description;
    private final int timerDelay;

    FarmingAction(String name, int timerDelay, String... description)
    {
        this.name = name;
        this.timerDelay = timerDelay;
        this.description = description;
    }
	public FarmingAction getMode()
	{
		return this;
	}
    @Override
    public String getName()
    {
        return name;
    }
    @Override
    public String[] getDescription()
    {
        return description;
    }
    public int getTimerDelay(FarmingAction mode)
    {
        return mode.timerDelay;
    }
	public FarmingAction getNextMode(FarmingAction mode)
	{
		int m = mode.ordinal();
		m++;
		if (m > FarmingAction.HARVEST.ordinal())
		{
			m = FarmingAction.TILLING.ordinal();
		}
		return FarmingAction.values()[m];
	}
}
