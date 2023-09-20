package geni.witherutils.core.client.gui.widgets;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidStackWidget extends WUTWidget {

    private final Screen displayOn;
    private final Supplier<FluidTank> tankFluid;

    public FluidStackWidget(Screen displayOn, Supplier<FluidTank> tankFluid, int pX, int pY, int pWidth, int pHeight)
    {
        super(pX, pY, pWidth, pHeight);
        this.displayOn = displayOn;
        this.tankFluid = tankFluid;
    }

    @SuppressWarnings("deprecation")
	@Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        FluidTank fluidTank = tankFluid.get();
        if (!fluidTank.isEmpty())
        {
            FluidStack fluidStack = fluidTank.getFluid();
            IClientFluidTypeExtensions props = IClientFluidTypeExtensions.of(fluidStack.getFluid());
            ResourceLocation still = props.getStillTexture(fluidStack);
            if (still != null)
            {
                AbstractTexture texture = minecraft.getTextureManager().getTexture(TextureAtlas.LOCATION_BLOCKS);
                if (texture instanceof TextureAtlas atlas)
                {
                    TextureAtlasSprite sprite = atlas.getSprite(still);

                    int color = props.getTintColor();
                    RenderSystem.setShaderColor(
                        FastColor.ARGB32.red(color) / 255.0F,
                        FastColor.ARGB32.green(color) / 255.0F,
                        FastColor.ARGB32.blue(color) / 255.0F,
                        FastColor.ARGB32.alpha(color) / 255.0F);
                    RenderSystem.enableBlend();

                    int stored = fluidTank.getFluidAmount();
                    float capacity = fluidTank.getCapacity();
                    float filledVolume = stored / capacity;
                    int renderableHeight = (int)(filledVolume * height);

                    int atlasWidth = (int)(sprite.contents().width() / (sprite.getU1() - sprite.getU0()));
                    int atlasHeight = (int)(sprite.contents().height() / (sprite.getV1() - sprite.getV0()));

                    guiGraphics.pose().pushPose();
                    guiGraphics.pose().translate(0, height-16, 0);
                    for (int i = 0; i < Math.ceil(renderableHeight / 16f); i++)
                    {
                        int drawingHeight = Math.min(16, renderableHeight - 16*i);
                        int notDrawingHeight = 16 - drawingHeight;
                        guiGraphics.blit(TextureAtlas.LOCATION_BLOCKS, x, y + notDrawingHeight, 0, sprite.getU0()*atlasWidth, sprite.getV0()*atlasHeight + notDrawingHeight, width, drawingHeight, atlasWidth, atlasHeight);
                        guiGraphics.pose().translate(0,-16, 0);
                    }
                    RenderSystem.setShaderColor(1, 1, 1, 1);
                    guiGraphics.pose().popPose();
                }
            }
        }
        renderToolTip(guiGraphics, mouseX, mouseY);
        RenderSystem.disableDepthTest();
    }

    @SuppressWarnings("resource")
	public void renderToolTip(GuiGraphics guiGraphics, int mouseX, int mouseY)
    {
        if (isHovered(mouseX, mouseY))
        {
            List<Component> list = new ArrayList<>();
            Fluid fluid = tankFluid.get().getFluid().getFluid();
            NumberFormat fmt = NumberFormat.getInstance(Locale.ENGLISH);
            list.add(Component.translatable(ChatFormatting.AQUA + "Fluid Type: ").append(ForgeRegistries.FLUIDS.getKey(fluid).getPath()));
            list.add(Component.translatable(ChatFormatting.DARK_GRAY + "Stored: ").append(fmt.format(tankFluid.get().getFluidAmount()) + "/" + fmt.format(tankFluid.get().getCapacity())).append(Component.translatable(ChatFormatting.AQUA + " mB")));
            list.add(Component.translatable(ChatFormatting.DARK_GRAY + "Capacity: ").append(fmt.format(tankFluid.get().getCapacity())).append(Component.translatable(ChatFormatting.AQUA + " mB")));
            guiGraphics.renderComponentTooltip(displayOn.getMinecraft().font, list, mouseX, mouseY);
        }
    }

	@Override
	protected void updateWidgetNarration(NarrationElementOutput p_259858_)
	{
	}
}
