package geni.witherutils.base.common.block.tank.drum;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.mojang.blaze3d.systems.RenderSystem;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.core.client.gui.screen.WUTScreen;
import geni.witherutils.core.client.gui.widgets.FluidStackWidget;
import geni.witherutils.core.common.math.Vector2i;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.fluids.FluidStack;

public class TankDrumScreen extends WUTScreen<TankDrumContainer> {
	
	float eftimer;
	
    public TankDrumScreen(TankDrumContainer pMenu, Inventory pPlayerInventory, Component pTitle)
    {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init()
    {
        super.init();
        setHotbarOffset(108);
        
		eftimer = 0.5f;
		addRenderableOnly(new FluidStackWidget(this, getMenu().getBlockEntity()::getFluidTank, 80 + leftPos, 28 + topPos, 16, 47));
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(gg);
        super.render(gg, mouseX, mouseY, partialTicks);
        this.renderTooltip(gg, mouseX, mouseY);

        NumberFormat fmt = NumberFormat.getInstance(Locale.ENGLISH);
        drawTextWithScale(gg, Component.translatable(ChatFormatting.WHITE + "TankCapacity: ")
        		.append(fmt.format(getMenu().getBlockEntity().getFluidTank().getFluidAmount())
        		+ "/" + fmt.format(getMenu().getBlockEntity().getFluidTank().getCapacity())),
        		leftPos + 48, topPos + 90, 0xFF9999FF, 0.545f, false);
        
		renderToolTip(gg, mouseY, mouseY);
		drawEffect(gg, partialTicks);
    }
    
	protected void drawEffect(GuiGraphics gg, float partialTicks)
	{
		if(eftimer >= 1.0f)
			eftimer = 1.0f;
		else
			eftimer += 0.025f;

		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		
		int relX = (this.width - this.imageWidth) / 2;
		int relY = (this.height - this.imageHeight) / 2;
		
        RenderSystem.setShaderColor(eftimer, eftimer, 1, eftimer);
		gg.blit(WitherUtils.loc("textures/gui/tank_effect.png"), relX, relY, 0, 0, this.imageWidth, this.imageHeight);
//        RenderSystem.setShaderColor(1, 1, 1, 1);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		if(isHovering(84, 74, 8, 8, mouseX, mouseY))
		{
			if(menu.getBlockEntity().getFluidTank().getFluid() == FluidStack.EMPTY)
				return false;
			else
				menu.getBlockEntity().setFluid(FluidStack.EMPTY);
			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else
		{
			return super.mouseClicked(mouseX, mouseY, mouseButton);
		}
		return true;
	}
	
    public void renderToolTip(GuiGraphics gg, int mouseX, int mouseY)
    {
        List<Component> list = new ArrayList<>();
        if(isHovering(84, 74, 8, 8, mouseX, mouseY))
        {
        	list.add(Component.translatable(ChatFormatting.RED + "Fluid Dump"));
        }
        gg.renderComponentTooltip(font, list, mouseX, mouseY); 
    }
    
    @Override
    protected String getBarName()
    {
        return "Tank Drum";
    }
    
    @Override
    public ResourceLocation getBackgroundImage()
    {
        return WitherUtils.loc("textures/gui/drum.png");
    }

    @Override
    protected Vector2i getBackgroundImageSize()
    {
        return new Vector2i(176, 136);
    }
    
    @Override
    public void onClose()
    {
    	super.onClose();
		Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(WUTSounds.BUCKET.get(), 0.6F, 1.0f));
    }
}
