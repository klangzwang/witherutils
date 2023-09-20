package geni.witherutils.base.common.block.miner.basic;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.base.RedstoneControl;
import geni.witherutils.core.client.gui.screen.WUTScreen;
import geni.witherutils.core.client.gui.widgets.EnergyWidget;
import geni.witherutils.core.common.math.Vector2i;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MinerBasicScreen extends WUTScreen<MinerBasicContainer> {

    public MinerBasicScreen(MinerBasicContainer pMenu, Inventory pPlayerInventory, Component pTitle)
    {
        super(pMenu, pPlayerInventory, pTitle);
    }

	@Override
    protected void init()
    {
        super.init();
        addRenderableOnly(new EnergyWidget(this, this.getMenu(), this.font, getMenu().getBlockEntity()::getEnergyStorage, leftPos + 8, topPos + 23, 16, 40));
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(gg);
        super.render(gg, mouseX, mouseY, partialTicks);
        this.renderTooltip(gg, mouseX, mouseY);
        
        RenderSystem.setShaderColor(1, 1, 1, 0.6f);
        drawTextWithScale(gg, Component.translatable("no Internal Slots"), leftPos + 115, topPos + 40, 0xFF666666, 0.5f, true);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        
        RenderSystem.setShaderColor(1, 1, 1, 0.6f);
        drawTextWithScale(gg, Component.translatable("only Advanced"), leftPos + 118, topPos + 46, 0xFF666666, 0.5f, true);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        
    	gg.blit(getRedstoneTexture(), this.leftPos + 75, this.topPos + 48, 0, 0, 12, 12, 12, 12);
    	
    	renderSlotButtonToolTips(gg, mouseX, mouseY);
    }
    
	public ResourceLocation getRedstoneTexture()
	{
		if(menu.getBlockEntity().getRedstoneControl() == RedstoneControl.ALWAYS_ACTIVE)
			return WitherUtils.loc("textures/gui/redstone_active.png");
		else
			return WitherUtils.loc("textures/gui/redstone_signal.png");
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		if(isHovering(75, 48, 12, 12, mouseX, mouseY))
		{
			if(menu.getBlockEntity().getRedstoneControl() == RedstoneControl.ALWAYS_ACTIVE)
				menu.getBlockEntity().setRedstoneControl(RedstoneControl.ACTIVE_WITH_SIGNAL);
			else
				menu.getBlockEntity().setRedstoneControl(RedstoneControl.ALWAYS_ACTIVE);

			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else
		{
			return super.mouseClicked(mouseX, mouseY, mouseButton);
		}
		return true;
	}
	
    @Override
    protected String getBarName()
    {
        return "Miner (Basic)";
    }
    
    @Override
    public ResourceLocation getBackgroundImage()
    {
        return WitherUtils.loc("textures/gui/miner_basic.png");
    }

    @Override
    protected Vector2i getBackgroundImageSize()
    {
        return new Vector2i(176, 131);
    }
    
    public void renderSlotButtonToolTips(GuiGraphics gg, int mouseX, int mouseY)
    {
        List<Component> list = new ArrayList<>();
        
        if(isHovering(75, 48, 12, 12, mouseX, mouseY))
        {
            Component appendRedSt = Component.literal("");
            
            if(menu.getBlockEntity().getRedstoneControl() == RedstoneControl.ALWAYS_ACTIVE)
            	appendRedSt = Component.literal(ChatFormatting.GREEN + "Always");
            else if(menu.getBlockEntity().getRedstoneControl() == RedstoneControl.ACTIVE_WITH_SIGNAL)
            	appendRedSt = Component.literal(ChatFormatting.RED + "withSignal");

            list.add(Component.translatable(ChatFormatting.GRAY + "Redstone Signal: ").append(appendRedSt));
        }
        else if(isHovering(44, 30, 16, 16, mouseX, mouseY))
        {
           	list.add(Component.translatable(ChatFormatting.GRAY + "Enchantment: ").append(Component.translatable(ChatFormatting.RED + "not supported")));
        }
        else if(isHovering(76, 30, 16, 16, mouseX, mouseY))
        {
            if(!menu.getSlot(1).hasItem())
            {
            	list.add(Component.translatable(ChatFormatting.GRAY + "Pickaxe: ").append(Component.translatable(ChatFormatting.RED + "not found")));
            }
        }
        
        gg.renderComponentTooltip(font, list, mouseX, mouseY); 
    }
}
