package geni.witherutils.core.common.helper;

import net.minecraft.world.entity.player.Player;

public class ExperienceHelper {

	public static void addPlayerXP(Player player, int amount)
	{
		int experience = getPlayerXP(player) + amount;
		player.totalExperience = experience;
		player.experienceLevel = getLevelForExperience(experience);
		int expForLevel = getExperienceForLevel(player.experienceLevel);
		player.experienceProgress = (float)(experience - expForLevel) / (float)player.getXpNeededForNextLevel();
	}

	public static int getPlayerXP(Player player)
	{
		return (getExperienceForLevel(player.experienceLevel) + (int)(player.experienceProgress * player.getXpNeededForNextLevel()));
	}

	public static int getLevelForExperience(int experience)
	{
		int i = 0;
		while (getExperienceForLevel(i) <= experience)
		{
			i++;
		}
		return i - 1;
	}

	public static int getExperienceForLevel(int level)
	{
		if (level == 0)
			return 0;
		if (level > 0 && level < 17)
			return (int) (Math.pow(level, 2) + 6 * level);
		else if (level > 16 && level < 32)
			return (int) (2.5 * Math.pow(level, 2) - 40.5 * level + 360);
		else
			return (int) (4.5 * Math.pow(level, 2) - 162.5 * level + 2220);
	}
}
