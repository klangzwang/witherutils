package geni.witherutils.base.common.block.miner.advanced;

import java.util.ArrayList;
import java.util.List;

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

public class MinerAdvancedScreen extends WUTScreen<MinerAdvancedContainer> {

    public MinerAdvancedScreen(MinerAdvancedContainer pMenu, Inventory pPlayerInventory, Component pTitle)
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

    	gg.blit(getRedstoneTexture(), this.leftPos + 43, this.topPos + 48, 0, 0, 18, 18, 18, 18);
    	gg.blit(getRenderTexture(), this.leftPos + 75, this.topPos + 63, 0, 0, 18, 18, 18, 18);
    	gg.blit(WitherUtils.loc("textures/gui/range.png"), this.leftPos + 75, this.topPos + 48, 0, 0, 18, 18, 18, 18);
    	
    	String RANGE = String.valueOf(menu.getBlockEntity().getRange());
    	drawTextWithScale(gg, Component.literal(RANGE), leftPos + 68, topPos + 53, 0xFFAAAAAA, 0.8f, false);
    	
    	renderSlotButtonToolTips(gg, mouseX, mouseY);
    }
    
	public ResourceLocation getRedstoneTexture()
	{
		if(menu.getBlockEntity().getRedstoneControl() == RedstoneControl.ALWAYS_ACTIVE)
			return WitherUtils.loc("textures/gui/redstone_active.png");
		else
			return WitherUtils.loc("textures/gui/redstone_signal.png");
	}
	public ResourceLocation getRenderTexture()
	{
		if(menu.getBlockEntity().getRender())
			return WitherUtils.loc("textures/gui/render_on.png");
		else
			return WitherUtils.loc("textures/gui/render_off.png");
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		if(isHovering(44, 49, 16, 16, mouseX, mouseY))
		{
			if(menu.getBlockEntity().getRedstoneControl() == RedstoneControl.ALWAYS_ACTIVE)
				menu.getBlockEntity().setRedstoneControl(RedstoneControl.ACTIVE_WITH_SIGNAL);
			else
				menu.getBlockEntity().setRedstoneControl(RedstoneControl.ALWAYS_ACTIVE);

			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else if(isHovering(76, 68, 16, 10, mouseX, mouseY))
		{
			if(menu.getBlockEntity().getRender() == false)
				menu.getBlockEntity().setRender(true);
			else
				menu.getBlockEntity().setRender(false);

			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else if(isHovering(76, 50, 7, 14, mouseX, mouseY))
		{
			int f = menu.getBlockEntity().getRange();
	        if(menu.getBlockEntity().getRange() > 1)
	        	menu.getBlockEntity().setRange(f -1);
			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else if(isHovering(85, 50, 7, 14, mouseX, mouseY))
		{
			int f = menu.getBlockEntity().getRange();
	        if(menu.getBlockEntity().getRange() < 6)
	        	menu.getBlockEntity().setRange(f +1);
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
        return "Miner (Advanced)";
    }
    
    @Override
    public ResourceLocation getBackgroundImage()
    {
        return WitherUtils.loc("textures/gui/miner_adv.png");
    }

    @Override
    protected Vector2i getBackgroundImageSize()
    {
        return new Vector2i(176, 131);
    }
    
    public void renderSlotButtonToolTips(GuiGraphics gg, int mouseX, int mouseY)
    {
        List<Component> list = new ArrayList<>();
    	
        if(isHovering(44, 49, 16, 16, mouseX, mouseY))
        {
            Component appendRedSt = Component.literal("");
            
            if(menu.getBlockEntity().getRedstoneControl() == RedstoneControl.ALWAYS_ACTIVE)
            	appendRedSt = Component.literal(ChatFormatting.GREEN + "Always");
            else if(menu.getBlockEntity().getRedstoneControl() == RedstoneControl.ACTIVE_WITH_SIGNAL)
            	appendRedSt = Component.literal(ChatFormatting.RED + "withSignal");

            list.add(Component.translatable(ChatFormatting.GRAY + "Redstone Signal: ").append(appendRedSt));
        }
        else if(isHovering(76, 30, 16, 16, mouseX, mouseY))
        {
            if(!menu.getSlot(1).hasItem())
            {
            	list.add(Component.translatable(ChatFormatting.GRAY + "Pickaxe: ").append(Component.translatable(ChatFormatting.RED + "not found")));
            }
        }
        else if(isHovering(76, 68, 16, 10, mouseX, mouseY))
        {
            list.add(Component.translatable(ChatFormatting.GRAY + "Preview"));
        }
        else if(isHovering(76, 50, 7, 14, mouseX, mouseY))
        {
            list.add(Component.translatable(ChatFormatting.GRAY + "Lower Range"));
        }
        else if(isHovering(85, 50, 7, 14, mouseX, mouseY))
        {
            list.add(Component.translatable(ChatFormatting.GRAY + "Higher Range"));
        }
        gg.renderComponentTooltip(font, list, mouseX, mouseY); 
    }
}
