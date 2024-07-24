package geni.witherutils.base.common.item.cutter;

import java.awt.Color;
import java.util.List;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.core.client.gui.screen.WUTScreen;
import geni.witherutils.core.common.math.Vector2i;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeHolder;

public class CutterScreen extends WUTScreen<CutterContainer> {

	private float scrollOffs;
	private boolean scrolling;
	private int startIndex;
	private boolean displayRecipes;

	public CutterScreen(CutterContainer screenContainer, Inventory inv, Component titleIn)
	{
		super(screenContainer, inv, titleIn);
		screenContainer.registerUpdateListener(this::containerChanged);
		this.titleLabelY--;
	}
	
    @Override
    protected void init()
    {
        super.init();
        setHotbarOffset(140);
    }
    
	@Override
	public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks)
	{
        this.renderBackground(gg, mouseX, mouseY, partialTicks);
        super.render(gg, mouseX, mouseY, partialTicks);
        this.renderTooltip(gg, mouseX, mouseY);
		
        drawSlotNormal(gg, 16, 112, true, false, false);
        drawSlotNormal(gg, 142, 112, false, true, !menu.getSlot(1).hasItem());
        
		int x = this.leftPos + 52;
		int y = this.topPos + 14;

		if(menu.inputSlot.hasItem())
		{
			menu.findMatchingCutterRecipe(menu.container);
			this.renderRecipes(gg, x, y, this.menu.getRecipes().size());
		}

		int startX = (this.width - this.imageWidth) / 2;
		int startY = (this.height - this.imageHeight) / 2;

		gg.drawCenteredString(this.font, "Input", startX + 25, startY + 95, Color.WHITE.getRGB());
		gg.drawCenteredString(this.font, "Output", startX + 150, startY + 95, Color.WHITE.getRGB());

		MutableComponent text2 = Component.translatable("")
		.withStyle(ChatFormatting.RESET)
		.withStyle(ChatFormatting.WHITE)
		.append(Component.translatable("||||")
		.withStyle(ChatFormatting.RESET)
		.withStyle(ChatFormatting.GOLD)
		.append(Component.translatable(" CARVING ")
		.withStyle(ChatFormatting.RESET)
		.withStyle(ChatFormatting.WHITE)
		.append(Component.translatable("||||")
		.withStyle(ChatFormatting.RESET)
		.withStyle(ChatFormatting.GOLD))));

		gg.drawString(this.font, text2, this.leftPos + 56, this.topPos + 112, 9999999);		
		if(!this.menu.hasInputItem())
		{
			gg.drawCenteredString(this.font, "Please insert", startX + 87, startY + 121, Color.WHITE.getRGB());
			return;
		}
		if(this.menu.hasInputItem())
		{
			Item blockname = this.menu.inputSlot.getItem().getItem();
			gg.drawCenteredString(this.font, "" + blockname, startX + 87, startY + 121, Color.ORANGE.getRGB());
		}
	}

	private void renderRecipes(GuiGraphics pGuiGraphics, int pX, int pY, int pStartIndex)
	{
        List<RecipeHolder<CutterRecipe>> list = this.menu.getRecipes();

        for (int i = this.startIndex; i < pStartIndex && i < this.menu.getNumRecipes(); i++)
        {
            int j = i - this.startIndex;
            int k = pX + j % 4 * 16;
            int l = j / 4;
            int i1 = pY + l * 18 + 2;
            pGuiGraphics.renderItem(list.get(i).value().getResultItem(this.minecraft.level.registryAccess()), k, i1);
        }
	}

	@SuppressWarnings("unused")
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		this.scrolling = false;
		if(this.displayRecipes)
		{
			int i = this.leftPos + 15;
			int j = this.topPos + 30;
			int k = this.startIndex + 18;

			for(int l = this.startIndex; l < k; l++)
			{
				int i1 = l - this.startIndex;
				double d0 = mouseX - (double) (i + i1 % 8 * 18);
				double d1 = mouseY - (double) (j + i1 / 8 * 18);

				if(d0 >= 0.0D && d1 >= 0.0D && d0 < 16.0D && d1 < 18.0D && this.menu.clickMenuButton(this.minecraft.player, l))
				{
					Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
					this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, l);
					return true;
				}
			}

			i = this.leftPos + 119;
			j = this.topPos + 9;

			if(mouseX >= (double) i && mouseX < (double) (i + 12) && mouseY >= (double) j && mouseY < (double) (j + 54))
			{
				this.scrolling = true;
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean mouseDragged(double p_99322_, double p_99323_, int p_99324_, double p_99325_, double p_99326_)
	{
		if(this.scrolling && this.isScrollBarActive())
		{
			int i = this.topPos + 14;
			int j = i + 54;
			this.scrollOffs = ((float) p_99323_ - (float) i - 7.5F) / ((float) (j - i) - 15.0F);
			this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
			this.startIndex = (int) ((double) (this.scrollOffs * (float) this.getOffscreenRows()) + 0.5D) * 4;
			return true;
		}
		else
		{
			return super.mouseDragged(p_99322_, p_99323_, p_99324_, p_99325_, p_99326_);
		}
	}
	
    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pScrollX, double pScrollY)
    {
        if (this.isScrollBarActive())
        {
            int i = this.getOffscreenRows();
            float f = (float)pScrollY / (float)i;
            this.scrollOffs = Mth.clamp(this.scrollOffs - f, 0.0F, 1.0F);
            this.startIndex = (int)((double)(this.scrollOffs * (float)i) + 0.5) * 4;
        }
        return true;
    }
    
    private boolean isScrollBarActive()
    {
        return this.displayRecipes && this.menu.getNumRecipes() > 12;
    }
	
    protected int getOffscreenRows()
    {
        return (this.menu.getNumRecipes() + 4 - 1) / 4 - 3;
    }
    
    private void containerChanged()
    {
        this.displayRecipes = this.menu.hasInputItem();
        if (!this.displayRecipes)
        {
            this.scrollOffs = 0.0F;
            this.startIndex = 0;
        }
    }

    @Override
    protected String getBarName()
    {
        return "Cutter";
    }
    
    @Override
    public ResourceLocation getBackgroundImage()
    {
        return WitherUtilsRegistry.loc("textures/gui/cutter.png");
    }

    @Override
    protected Vector2i getBackgroundImageSize()
    {
        return new Vector2i(176, 142);
    }
}
