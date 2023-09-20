package geni.witherutils.core.client.gui.widgets.io;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.base.WitherMachineEnergyBlockEntity;
import geni.witherutils.core.client.gui.screen.WUTScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;

public class IOConfigWidget<U extends WUTScreen<?>> extends AbstractWidget {

    private final U addedOn;
    private final BlockEntity blockEntity;
    
    public IOConfigWidget(U addedOn, int x, int y, int width, int height, Font font, BlockEntity blockEntity)
    {
        super(x, y, width, height, Component.empty());
        this.addedOn = addedOn;
        this.blockEntity = blockEntity;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton)
    {
        if (this.active && this.visible)
        {
//        	if(isHovered())
//        	{
//            	if(blockEntity instanceof WitherMachineBlockEntity)
//            	{
//            		WitherMachineBlockEntity machine = (WitherMachineBlockEntity) blockEntity;
//                    LazyOptional<ISideConfig> optSideConfig = machine.getCapability(WUTCapabilities.SIDECONFIG, Direction.UP);
//                    
//                    if (optSideConfig.isPresent())
//                    {
//                        optSideConfig.ifPresent(ISideConfig::cycleMode);
//                    }
//                    machine.getIOConfig().cycleMode(Direction.UP);
//                    
//            		System.out.println(machine.getIOConfig().getMode(Direction.UP));
//            	}
//                this.playDownSound(Minecraft.getInstance().getSoundManager());        		
//        	}
            if (pButton == 1)
            {
                return true;
            }
        }
        return false;
    }
    
    public void cycleEnergyUp()
    {
    	if(blockEntity instanceof WitherMachineEnergyBlockEntity)
    	{
    		WitherMachineEnergyBlockEntity machine = (WitherMachineEnergyBlockEntity) blockEntity;
    		machine.getIOConfig().cycleMode(Direction.UP);
    	}
        this.playDownSound(Minecraft.getInstance().getSoundManager());
    }
    
    @Override
    protected void renderWidget(GuiGraphics gg, int mouseX, int mouseY, float partialTick)
    {
        if (visible)
        {
            if (isMouseOver(mouseX, mouseY))
                addedOn.setFocused(this);
            gg.enableScissor(addedOn.getGuiLeft(), addedOn.getGuiTop() + 88, getX() + 179, getY() + 88);
            gg.blit(WitherUtils.loc("textures/gui/io_overlay.png"), addedOn.getGuiLeft(), addedOn.getGuiTop() + 88, 0, 0, 179, 49, 179, 49);
            gg.disableScissor();
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {}
}
