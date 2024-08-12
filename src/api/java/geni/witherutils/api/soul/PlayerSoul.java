package geni.witherutils.api.soul;

public class PlayerSoul {

	private int souls = 0;
	private final int maxSouls = 20;

	public void addSouls(int souls)
	{
		this.souls = Math.min(this.souls + souls, maxSouls);
	}
	
	public int getSouls()
	{
		return souls;
	}
	
	public void setSouls(int souls)
	{
		this.souls = souls;
	}
}
