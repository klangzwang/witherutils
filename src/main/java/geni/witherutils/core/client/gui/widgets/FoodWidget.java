package geni.witherutils.core.client.gui.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.base.WitherMachineMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("unused")
public class FoodWidget extends WUTWidget {

	private final WitherMachineMenu<?> menu;
	private Font font;
    private int x;
    private int y;
    private int width;
    private int height;
    
    public FoodWidget(Font font, WitherMachineMenu<?> menu, int x, int y, int width, int height)
    {
		super(x, y, width, height);
		this.font = font;
		this.menu = menu;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
	}
    
    @Override
    public void renderWidget(GuiGraphics gg, int mouseX, int mouseY, float partialTicks)
    {
    	int food = menu.getBlockEntity().getInventory().getStackInSlot(0).getCount();
        if (food <= 0)
        {
            gg.pose().pushPose();
            gg.blit(WitherUtils.loc("textures/gui/energy_bar_locked.png"), x, y, 0, 0, 16, 40, 16, 40);
            gg.pose().popPose();
            renderToolTip(gg, mouseX, mouseY);
            return;
        }

        float filledVolume = food / (float) 64;
        int renderableHeight = (int)(filledVolume * height);
        
        gg.blit(WitherUtils.loc("textures/gui/food.png"), x, y, 16, 0, width, height, 32, height);
        float pct = Math.min(filledVolume, 1.0F);
        gg.blit(WitherUtils.loc("textures/gui/food.png"), x, y, 0, 0, width, height - (int) (height * pct), 32, height);

        renderToolTip(gg, mouseX, mouseY);
    }
    
	@SuppressWarnings("deprecation")
	public int getNutrition()
	{
		ItemStack stack = menu.getBlockEntity().getInventory().getStackInSlot(0);
		return menu.getSlot(0).hasItem() ? stack.getItem().getFoodProperties().getNutrition() : 0;
	}
	@SuppressWarnings("deprecation")
	public float getSaturation()
	{
		ItemStack stack = menu.getBlockEntity().getInventory().getStackInSlot(0);
		return menu.getSlot(0).hasItem() ? stack.getItem().getFoodProperties().getSaturationModifier() : 0.0f;
	}
    
    public void renderToolTip(GuiGraphics gg, int mouseX, int mouseY)
    {
    	List<Component> list = new ArrayList<>();
		if(isHovered(mouseX, mouseY))
		{
			String nutri = "" + getNutrition();
			String sat = "" + getSaturation();
			list.add(Component.translatable(new String("Nutrition: ")).withStyle(ChatFormatting.AQUA).append(Component.translatable(nutri).withStyle(ChatFormatting.WHITE)));
			list.add(Component.translatable(new String("Saturation: ")).withStyle(ChatFormatting.AQUA).append(Component.translatable(sat).withStyle(ChatFormatting.WHITE)));
		}
		gg.renderComponentTooltip(font, list, mouseX, mouseY); 
    }
	
	@Override
	protected void updateWidgetNarration(NarrationElementOutput p_259858_)
	{
	}
}
