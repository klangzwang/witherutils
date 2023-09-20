package geni.witherutils.core.common.helper;

import java.util.Locale;

public record ScreenPositionHelper(float percentageXX, float percentageXY, float pxX, float percentageYX, float percentageYY, float pxY) {
    
	public float x(float width, float height)
	{
		return width * this.percentageXX + height * this.percentageXY + this.pxX;
	}
	public float y(float width, float height)
	{
		return width * this.percentageYX + height * this.percentageYY + this.pxY;
	}
	public static ScreenPositionHelper parse(String x, String y)
	{
		float[] xParts = decompose(x, false);
		float[] yParts = decompose(y, true);
		return new ScreenPositionHelper(xParts[0], xParts[1], xParts[2], yParts[0], yParts[1], yParts[2]);
	}
	private static float[] decompose(String axisPosition, boolean isYAxis)
	{
		axisPosition = axisPosition.toLowerCase(Locale.ROOT) + "+";
		StringBuilder part = new StringBuilder();

		float resultWidthMultiplier = 0;
		float resultHeightMultiplier = 0;
		float resultPxOffset = 0;

		for (char c : axisPosition.toCharArray())
		{
			if (c == '+' || c == '-')
			{
				String component = part.toString();
				part = new StringBuilder().append(c);

				if (component.endsWith("%"))
				{
					component = component.substring(0, component.length() - 1);

					if (isYAxis)
						resultHeightMultiplier += Float.parseFloat(component);
					else
						resultWidthMultiplier += Float.parseFloat(component);
				}
				else if (component.endsWith("vw"))
				{
					component = component.substring(0, component.length() - 2);
					resultWidthMultiplier += Float.parseFloat(component);
				}
				else if (component.endsWith("vh"))
				{
					component = component.substring(0, component.length() - 2);
					resultHeightMultiplier += Float.parseFloat(component);
				}
				else
				{
					float additionMultiplier = 1.0f;
					if (component.endsWith("em"))
					{
						component = component.substring(0, component.length() - 2);
						additionMultiplier = 16.0f;
					}
					else if (component.endsWith("px"))
					{
						component = component.substring(0, component.length() - 2);
					}
					resultPxOffset += additionMultiplier * Float.parseFloat(component);
				}
			}
			else
			{
				part.append(c);
			}
		}

		return new float[]
		{
				resultWidthMultiplier / 100.0f,
				resultHeightMultiplier / 100.0f,
				resultPxOffset
		};
	}
}
