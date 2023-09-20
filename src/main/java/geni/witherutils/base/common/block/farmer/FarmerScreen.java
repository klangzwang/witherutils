package geni.witherutils.base.common.block.farmer;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.base.MachineSlotStack;
import geni.witherutils.base.common.base.RedstoneControl;
import geni.witherutils.core.client.gui.screen.WUTScreen;
import geni.witherutils.core.client.gui.widgets.EnergyWidget;
import geni.witherutils.core.client.gui.widgets.FluidStackWidget;
import geni.witherutils.core.client.gui.widgets.SmallToggleButton;
import geni.witherutils.core.common.math.Vector2i;
import geni.witherutils.core.common.util.ColorUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class FarmerScreen extends WUTScreen<FarmerContainer> {
	
    public FarmerScreen(FarmerContainer pMenu, Inventory pPlayerInventory, Component pTitle)
    {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init()
    {
        super.init();
        setHotbarOffset(87);
        addRenderableOnly(new EnergyWidget(this, this.getMenu(), this.font, getMenu().getBlockEntity()::getEnergyStorage, leftPos + 8, topPos + 23, 16, 40));
		addRenderableOnly(new FluidStackWidget(this, getMenu().getBlockEntity()::getFluidTank, leftPos + 154, topPos + 23, 16, 47));
		addRenderableWidget(new SmallToggleButton<>(this, this.font, () -> menu.getBlockEntity().getShowFarmingPos(), mode -> menu.getBlockEntity().setShowFarmingPos(mode), "showpos", leftPos + 140, topPos + 37, 12, "FarmingPosition", (press) -> {}));
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(gg);
        super.render(gg, mouseX, mouseY, partialTicks);
        this.renderTooltip(gg, mouseX, mouseY);

        gg.blit(WitherUtils.loc("textures/gui/baroverlay.png"), this.leftPos + 154, this.topPos + 23, 0, 0, 16, 47, 16, 47);
    	gg.blit(getRedstoneTexture(), this.leftPos + 140, this.topPos + 23, 0, 0, 12, 12, 12, 12);
    	
    	if(menu.getBlockEntity().getLockedSW() == false)
    		gg.blit(WitherUtils.loc("textures/gui/unlocked.png"), this.leftPos + 32, this.topPos + 50, 0, 0, 10, 10, 10, 10);
    	if(menu.getBlockEntity().getLockedSE() == false)
    		gg.blit(WitherUtils.loc("textures/gui/unlocked.png"), this.leftPos + 80, this.topPos + 50, 0, 0, 10, 10, 10, 10);
    	if(menu.getBlockEntity().getLockedNW() == false)
    		gg.blit(WitherUtils.loc("textures/gui/unlocked.png"), this.leftPos + 32, this.topPos + 68, 0, 0, 10, 10, 10, 10);
    	if(menu.getBlockEntity().getLockedNE() == false)
    		gg.blit(WitherUtils.loc("textures/gui/unlocked.png"), this.leftPos + 80, this.topPos + 68, 0, 0, 10, 10, 10, 10);
    	
//        RenderSystem.disableDepthTest();
//        RenderSystem.enableBlend();
//    	for(Slot slot : menu.slots)
//    	{
//    		if(slot instanceof MachineSlotStack)
//    		{
//    			MachineSlotStack mslot = (MachineSlotStack) slot;
//    			drawGhostSlot(gg, mslot);
//    		}
//    	}
//        RenderSystem.disableBlend();
//        RenderSystem.enableDepthTest();
    	
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        if(!menu.getSlot(3).hasItem())
        	gg.drawString(this.font, "SW", menu.getSlot(3).x + leftPos + 2, menu.getSlot(3).y + topPos + 4, ColorUtil.getARGB(1f, 1f, 0.35f, 1f), true);
        if(!menu.getSlot(4).hasItem())
        	gg.drawString(this.font, "SE", menu.getSlot(4).x + leftPos + 2, menu.getSlot(4).y + topPos + 4, ColorUtil.getARGB(1f, 1f, 0.35f, 1f), true);
        if(!menu.getSlot(5).hasItem())
        	gg.drawString(this.font, "NW", menu.getSlot(5).x + leftPos + 2, menu.getSlot(5).y + topPos + 4, ColorUtil.getARGB(1f, 1f, 0.35f, 1f), true);
        if(!menu.getSlot(6).hasItem())
        	gg.drawString(this.font, "NE", menu.getSlot(6).x + leftPos + 2, menu.getSlot(6).y + topPos + 4, ColorUtil.getARGB(1f, 1f, 0.35f, 1f), true);
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
        
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
    protected String getBarName()
    {
        return "Farmer";
    }
    
    @Override
    public ResourceLocation getBackgroundImage()
    {
        return WitherUtils.loc("textures/gui/farmer.png");
    }

    @Override
    protected Vector2i getBackgroundImageSize()
    {
        return new Vector2i(176, 131);
    }
    
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		if(isHovering(158, 69, 8, 8, mouseX, mouseY))
		{
			if(menu.getBlockEntity().getFluidTank().getFluid() == FluidStack.EMPTY)
				return false;
			else
				menu.getBlockEntity().setFluid(FluidStack.EMPTY);
			
			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else if(isHovering(140, 23, 12, 12, mouseX, mouseY))
		{
			if(menu.getBlockEntity().getRedstoneControl() == RedstoneControl.ALWAYS_ACTIVE)
				menu.getBlockEntity().setRedstoneControl(RedstoneControl.ACTIVE_WITH_SIGNAL);
			else
				menu.getBlockEntity().setRedstoneControl(RedstoneControl.ALWAYS_ACTIVE);

			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else if(isHovering(32, 50, 10, 10, mouseX, mouseY))
		{
			if(menu.getBlockEntity().getLockedSW() == false)
				menu.getBlockEntity().setLockedSW(true);
			else
				menu.getBlockEntity().setLockedSW(false);

			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else if(isHovering(80, 50, 10, 10, mouseX, mouseY))
		{
			if(menu.getBlockEntity().getLockedSE())
				menu.getBlockEntity().setLockedSE(false);
			else
				menu.getBlockEntity().setLockedSE(true);

			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else if(isHovering(32, 68, 10, 10, mouseX, mouseY))
		{
			if(menu.getBlockEntity().getLockedNW())
				menu.getBlockEntity().setLockedNW(false);
			else
				menu.getBlockEntity().setLockedNW(true);

			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else if(isHovering(80, 68, 10, 10, mouseX, mouseY))
		{
			if(menu.getBlockEntity().getLockedNE())
				menu.getBlockEntity().setLockedNE(false);
			else
				menu.getBlockEntity().setLockedNE(true);

			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else
		{
			return super.mouseClicked(mouseX, mouseY, mouseButton);
		}
		return true;
	}
	
    public void renderSlotButtonToolTips(GuiGraphics gg, int mouseX, int mouseY)
    {
        List<Component> list = new ArrayList<>();

        if(isHovering(158, 69, 8, 8, mouseX, mouseY))
        {
        	list.add(Component.translatable(ChatFormatting.RED + "Fluid Dump"));
        }
        else if(isHovering(140, 23, 12, 12, mouseX, mouseY))
        {
            Component appendRedSt = Component.literal("");
            
            if(menu.getBlockEntity().getRedstoneControl() == RedstoneControl.ALWAYS_ACTIVE)
            	appendRedSt = Component.literal(ChatFormatting.GREEN + "Always");
            else if(menu.getBlockEntity().getRedstoneControl() == RedstoneControl.ACTIVE_WITH_SIGNAL)
            	appendRedSt = Component.literal(ChatFormatting.RED + "withSignal");

            list.add(Component.translatable(ChatFormatting.GRAY + "Redstone Signal: ").append(appendRedSt));
        }
        else if(isHovering(menu.getSlot(1).x, menu.getSlot(1).y, 16, 16, mouseX, mouseY))
        {
            if(!menu.getSlot(1).hasItem())
            	list.add(Component.translatable(ChatFormatting.GRAY + "Hoe: ").append(Component.translatable(ChatFormatting.RED + "not found")));
        }
        else if(isHovering(menu.getSlot(2).x, menu.getSlot(2).y, 16, 16, mouseX, mouseY))
        {
            if(!menu.getSlot(2).hasItem())
            	list.add(Component.translatable(ChatFormatting.GRAY + "Fertilizer: ").append(Component.translatable(ChatFormatting.RED + "not found")));
        }
        else if(isHovering(32, 50, 10, 10, mouseX, mouseY))
        {
            Component appendSlot = Component.literal("");
            
			if(menu.getBlockEntity().getLockedSW() == false)
				appendSlot = Component.literal(ChatFormatting.GREEN + "Unlocked");
			else
				appendSlot = Component.literal(ChatFormatting.RED + "Locked");

            list.add(Component.translatable(ChatFormatting.GRAY + "Slot: ").append(appendSlot));
        }
        else if(isHovering(80, 50, 10, 10, mouseX, mouseY))
        {
            Component appendSlot = Component.literal("");
            
			if(menu.getBlockEntity().getLockedSE() == false)
				appendSlot = Component.literal(ChatFormatting.GREEN + "Unlocked");
			else
				appendSlot = Component.literal(ChatFormatting.RED + "Locked");

            list.add(Component.translatable(ChatFormatting.GRAY + "Slot: ").append(appendSlot));
        }
        else if(isHovering(32, 68, 10, 10, mouseX, mouseY))
        {
            Component appendSlot = Component.literal("");
            
			if(menu.getBlockEntity().getLockedNW() == false)
				appendSlot = Component.literal(ChatFormatting.GREEN + "Unlocked");
			else
				appendSlot = Component.literal(ChatFormatting.RED + "Locked");

            list.add(Component.translatable(ChatFormatting.GRAY + "Slot: ").append(appendSlot));
        }
        else if(isHovering(80, 68, 10, 10, mouseX, mouseY))
        {
            Component appendSlot = Component.literal("");
            
			if(menu.getBlockEntity().getLockedNE() == false)
				appendSlot = Component.literal(ChatFormatting.GREEN + "Unlocked");
			else
				appendSlot = Component.literal(ChatFormatting.RED + "Locked");

            list.add(Component.translatable(ChatFormatting.GRAY + "Slot: ").append(appendSlot));
        }
        gg.renderComponentTooltip(font, list, mouseX, mouseY); 
    }
    
    public void drawGhostSlot(GuiGraphics gg, MachineSlotStack slot)
    {
		ItemStack stack = slot.getStack();
		if (stack != null)
		{
//	    	RenderSystem.enableDepthTest();

			if (slot.shouldGrayOut())
				drawGhostSlotGrayout(gg, slot);
			gg.renderFakeItem(stack, slot.x + leftPos, slot.y + topPos);
		}
    }
    
    public void drawGhostSlotGrayout(GuiGraphics gg, MachineSlotStack slot)
    {
    	gg.blit(WitherUtils.loc("textures/gui/slot/background.png"), slot.x + leftPos, slot.y + topPos, 0, 0, 16, 16, 16, 16);
    	
//    	RenderSystem.enableBlend();
//    	RenderSystem.disableDepthTest();
//    	RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

//    	Vector2i pos = new Vector2i(slot.x + leftPos, slot.y + topPos);
//    	Vector2i pos2 = pos.add(new Vector2i(16, 16));

//    	RenderSystem.clearColor(1.0F, 1.0F, 1.0F, slot.getGrayOutLevel());

//    	gg.blit(WitherUtils.loc("textures/gui/slot/background.png"), slot.x + leftPos, slot.y + topPos, 0, 0, 16, 16, 16, 16);
//    	gg.fill(RenderType.guiOverlay(), pos.x(), pos.y(), pos2.x(), pos2.y(), 0xFF000000);
//    	RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0f);

//    	RenderSystem.enableDepthTest();
//    	RenderSystem.disableBlend();
    }
}
