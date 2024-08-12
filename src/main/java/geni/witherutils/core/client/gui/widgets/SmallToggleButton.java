package geni.witherutils.core.client.gui.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.mojang.blaze3d.systems.RenderSystem;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.core.client.gui.screen.WUTScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;

public class SmallToggleButton<U extends WUTScreen<?>, T extends AbstractWidget> extends AbstractWidget {
	
    @SuppressWarnings("unused")
	private final U addedOn;
    private final Supplier<Boolean> getter;
    private final Consumer<Boolean> setter;
    private final Font font;
    private final String res;
    private final String tooltip;
    private int leftPos;
    private int topPos;
    private int size;
    
    public SmallToggleButton(U addedOn, Font font, Supplier<Boolean> getter, Consumer<Boolean> setter, String res, int leftPos, int topPos, int size, String tooltip, Button.OnPress press)
    {
        super(leftPos, topPos, size, size, Component.empty());
        this.addedOn = addedOn;
        this.getter = getter;
        this.font = font;
        this.setter = setter;
        this.res = res;
        this.tooltip = tooltip;
        this.leftPos = leftPos;
        this.topPos = topPos;
        this.size = size;
    }

	@Override
    public void onClick(double pMouseX, double pMouseY)
    {
    }
    
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
    	if(isHovered())
		{
        	if(getter.get() == false)
        		setter.accept(true);
        	else
    			setter.accept(false);
			
			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else
		{
			return super.mouseClicked(mouseX, mouseY, mouseButton);
		}
		return true;
	}
	
    @Override
    public void renderWidget(GuiGraphics gg, int pMouseX, int pMouseY, float pPartialTick)
    {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

    	if(getter.get() == false)
    		gg.blit(WitherUtilsRegistry.loc("textures/gui/" + res + "_off.png"), leftPos, topPos, 0, 0, size, size, size, size);
    	else
    		gg.blit(WitherUtilsRegistry.loc("textures/gui/" + res + "_on.png"), leftPos, topPos, 0, 0, size, size, size, size);

        if(isHovered) renderToolTip(gg, pMouseX, pMouseY);
    }
    
    public void renderToolTip(GuiGraphics gg, int mouseX, int mouseY)
    {
        List<Component> list = new ArrayList<>();
    	if(isHovered())
        {
            Component appendTooltip = Component.literal("");
            
			if(getter.get() == false)
				appendTooltip = Component.literal(ChatFormatting.RED + "Disabled");
			else
				appendTooltip = Component.literal(ChatFormatting.GREEN + "Enable");

            list.add(Component.translatable(ChatFormatting.GRAY + tooltip + ": ").append(appendTooltip));
        }
        gg.renderComponentTooltip(this.font, list, mouseX, mouseY); 
    }

    @Override
	public void updateWidgetNarration(NarrationElementOutput narration) {}
}
