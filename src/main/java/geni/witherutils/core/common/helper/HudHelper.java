package geni.witherutils.core.common.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

public final class HudHelper {

	public static HudPos getHudPos()
	{
		var window = Minecraft.getInstance().getWindow();
		return new HudPos(10, window.getGuiScaledHeight() / 2, 0);
	}

	public static int getEnergyBarScaled(ItemStack stack)
	{
		IEnergyStorage cap = stack.getCapability(Capabilities.EnergyStorage.ITEM);
    	int i = cap.getEnergyStored();
    	int j = cap.getMaxEnergyStored();
		return (int) (j != 0 && i != 0 ? (long) i * 156 / j : 0);
	}

	public static String getFuelString(ItemStack stack)
	{
		IEnergyStorage cap = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        int number = cap.getEnergyStored();
		if(number >= 1000000000)
		{
			return number / 1000000000 + "\u00A77" + "G FE";
		}
		else if (number >= 1000000)
		{
			return number / 1000000 + "\u00A77" + "M FE";
		}
		else if (number >= 1000)
		{
			return number / 1000 + "\u00A77" + "k FE";
		}
		else
		{
			return number + "\u00A77" + " FE";
		}
	}

	public static String getStatusString(boolean on)
	{
		return on ? "\u00A7a" + "ON" : "\u00A7c" + "OFF";
	}

	public static class HudPos
	{
		public int x;
		public int y;
		public int side;

		public HudPos(int x, int y, int side)
		{
			this.x = x;
			this.y = y;
			this.side = side;
		}
	}
}
