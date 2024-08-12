package geni.witherutils.core.client.gui.widgets;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

import geni.witherutils.WitherUtils;
import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.common.base.WitherMachineEnergyBlockEntity;
import geni.witherutils.base.common.io.energy.IWitherEnergyStorage;
import geni.witherutils.base.common.menu.WitherMachineMenu;
import geni.witherutils.core.client.gui.screen.WUTScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings({ "rawtypes", "unused" })
public class ProgressWidget extends WUTWidget {

    private WUTScreen parent;
    private WitherMachineMenu menu;
    private final Font font;
    private final Supplier<Integer> timerSupplr;
    private final Supplier<Integer> burnMaxSupplr;
    private int leftPos;
    private int topPos;
    private int x;
    private int y;
    private int width;
    private int height;

    public ProgressWidget(WUTScreen parent, WitherMachineMenu menu, Font font, Supplier<Integer> timerSupplr, Supplier<Integer> burnMaxSupplr, int leftPos, int topPos, int x, int y, int width, int height)
    {
        super(x, y, width, height);
        this.parent = parent;
        this.menu = menu;
        this.font = font;
        this.timerSupplr = timerSupplr;
        this.burnMaxSupplr = burnMaxSupplr;
        this.leftPos = leftPos;
        this.topPos = topPos;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void renderWidget(GuiGraphics gg, int mouseX, int mouseY, float partialTicks)
    {
        float filledVolume = timerSupplr.get() / (float) burnMaxSupplr.get();
        int renderableHeight = (int)(filledVolume * height);

        gg.blit(WitherUtilsRegistry.loc("textures/gui/progress_bar.png"), x, y, 6, 0, width, height, 12, height);
        float pct = Math.min(filledVolume, 1.0F);
        gg.blit(WitherUtilsRegistry.loc("textures/gui/progress_bar.png"), x, y, 0, 0, width, height - (int) (height * pct), 12, height);

        renderToolTip(gg, mouseX, mouseY);
    }
    
    public void renderToolTip(GuiGraphics gg, int mouseX, int mouseY)
    {
        if(isHovered(mouseX, mouseY))
        {
            List<Component> list = new ArrayList<>();
            NumberFormat fmt = NumberFormat.getInstance(Locale.ENGLISH);
            int seconds = 0;
            if(timerSupplr.get() > 0)
            {
                seconds = Math.toIntExact(burnMaxSupplr.get().longValue() - timerSupplr.get())  / 20;
            }
            list.add(Component.translatable(ChatFormatting.WHITE + "BurnTimer: ").append(ChatFormatting.DARK_GRAY + fmt.format(seconds)).append(ChatFormatting.AQUA + " Seconds"));
            gg.renderComponentTooltip(font, list, mouseX, mouseY); 
        }
    }

	@Override
	protected void updateWidgetNarration(NarrationElementOutput p_259858_) {
		// TODO Auto-generated method stub
		
	}
}
