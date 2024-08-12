package geni.witherutils.core.client.gui.widgets;

import java.util.function.Consumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class GuiSliderInteger extends AbstractSliderButton {

    private final Consumer<Integer> setter;
    
	public static final int ARROW_LEFT = 263;
	public static final int ARROW_RIGHT = 262;
	static final int ESC = 256;
	private final double min;
	private final double max;

	public GuiSliderInteger(int x, int y, int width, int height, int min, int max, Consumer<Integer> setter)
	{
		super(x, y, width, height, Component.empty(), 0);
		this.setter = setter;
		this.min = min;
		this.max = max;
	}

	@Override
	public boolean mouseScrolled(double pMouseX, double pMouseY, double pScrollX, double pScrollY)
	{
		if (pScrollY != 0)
		{
			setSliderValueActual(this.getSliderValueActual() + (int) pScrollY);
			this.updateMessage();
			return true;
		}
		return super.mouseScrolled(pMouseX, pMouseY, pScrollX, pScrollY);
	}
	
	@SuppressWarnings("resource")
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers)
	{
		if (keyCode == ESC)
		{
			LocalPlayer pl = Minecraft.getInstance().player;
			if (pl != null)
			{
				pl.closeContainer();
			}
			return true;
		}
		if (keyCode == ARROW_LEFT || keyCode == ARROW_RIGHT)
		{
			int delta = (keyCode == ARROW_LEFT) ? -1 : 1;
			if (Screen.hasShiftDown())
			{
				delta = delta * 5;
			}
			else if (Screen.hasAltDown())
			{
				delta = delta * 10;
			}
			setSliderValueActual(this.getSliderValueActual() + delta);
			this.updateMessage();
			return true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	protected void updateMessage()
	{
		int val = getSliderValueActual();
		this.setMessage(Component.translatable("" + val));
	}

	@Override
	protected void applyValue()
	{
		int val = getSliderValueActual();
		setter.accept(val);
	}

	@Override
	protected void onDrag(double mouseX, double mouseY, double dragX, double dragY)
	{
		super.onDrag(mouseX, mouseY, dragX, dragY);
		applyValue();
		updateMessage();
	}

	public void setSliderValueActual(int val)
	{
		this.value = val / max;
		this.updateMessage();
		this.applyValue();
	}

	public int getSliderValueActual()
	{
		return Mth.floor(Mth.clampedLerp(min, max, this.value));
	}
}
