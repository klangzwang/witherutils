package geni.witherutils.core.client.gui.widgets;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

@SuppressWarnings("unused")
public class FluidWidget {

    private int x;
    private int y;
    private final Supplier<FluidTank> fluidSupplier;
    private int width = 16;
    private int height = 40;
    public int guiLeft;
    public int guiTop;
    public boolean visible = true;
    private final Font font;

    public FluidWidget(Font font, Supplier<FluidTank> fluidSupplier, int x, int y)
    {
        this.fluidSupplier = fluidSupplier;
        this.font = font;
        this.x = x;
        this.y = y;
    }
    
    public boolean isMouseOver(int mouseX, int mouseY)
    {
        return guiLeft + x < mouseX && mouseX < guiLeft + x + width && guiTop + y < mouseY && mouseY < guiTop + y + getHeight();
    }

    public void draw(GuiGraphics gg, Supplier<FluidTank> fluidSupplier)
    {
        FluidStack fluid = fluidSupplier.get().getFluid();
        if (fluid.getAmount() <= 0 || !visible)
            return;
        
        float capacity = this.fluidSupplier.get().getCapacity();
        float amount = fluid.getAmount();
        float scale = amount / capacity;
        int fluidAmount = (int) (scale * height);
        
        var fluidExtensions = IClientFluidTypeExtensions.of(fluid.getFluid());
        TextureAtlasSprite fluidStillSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidExtensions.getStillTexture());

        int xPosition = guiLeft + x + 1;
        int yPosition = guiTop + y + 1;
        int maximum = height - 2;
        int desiredWidth = width - 2;
        int desiredHeight = fluidAmount - 2;
        
        gg.blit(xPosition, yPosition + (maximum - desiredHeight), 0, desiredWidth, desiredHeight, fluidStillSprite);
    }

    public int getHeight()
    {
        return height;
    }
    public void setHeight(int height)
    {
        this.height = height;
    }
}
