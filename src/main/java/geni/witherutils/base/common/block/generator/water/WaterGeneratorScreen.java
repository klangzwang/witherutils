package geni.witherutils.base.common.block.generator.water;

import java.text.NumberFormat;
import java.util.Locale;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.core.client.gui.screen.WUTScreen;
import geni.witherutils.core.client.gui.widgets.EnergyWidget;
import geni.witherutils.core.common.math.Vector2i;
import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

public class WaterGeneratorScreen extends WUTScreen<WaterGeneratorContainer> {

    private int timer;
    
    public WaterGeneratorScreen(WaterGeneratorContainer pMenu, Inventory pPlayerInventory, Component pTitle)
    {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init()
    {
        super.init();
        addRenderableOnly(new EnergyWidget(this, this.getMenu(), this.font, () -> getMenu().getBlockEntity().getEnergyHandler(null), leftPos + 8, topPos + 23, 16, 40));
    }
    
    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(gg, mouseX, mouseY, partialTicks);
        super.render(gg, mouseX, mouseY, partialTicks);
        this.renderTooltip(gg, mouseX, mouseY);

        renderEffectChannel(gg, mouseX, mouseY, partialTicks, 1 + (int)menu.getBlockEntity().getGenerationRate());
        
        drawTextWithScale(gg, Component.literal("Generator Info"), leftPos + 45, topPos + 30, 0xFFFFFF00, 0.845f, false);
        drawTextWithScale(gg, Component.literal("Energy for Blockheads"), leftPos + 47, topPos + 36, 0xFFCCCCAA, 0.5f, false);
        
        drawTextWithScale(gg, Component.literal("Generates: ").append(Component.literal(ChatFormatting.WHITE + "       RF/FE")), leftPos + 47, topPos + 46, 0xFF00FF00, 0.5f, false);
        drawTextWithScale(gg, Component.literal(ChatFormatting.WHITE + "" + menu.getBlockEntity().getGenerationRate()), leftPos + 78, topPos + 46, 0xFF00FF00, 0.75f, false);

        if(menu.getBlockEntity().hasEfficiencyRate())
        {
            NumberFormat fmt = NumberFormat.getInstance(Locale.ENGLISH);
            drawTextWithScale(gg, Component.literal("Efficiency: "), leftPos + 47, topPos + 56, 0xFF00FF00, 0.5f, false);
            drawTextWithScale(gg, Component.literal(ChatFormatting.WHITE + "" + fmt.format(menu.getBlockEntity().getEfficiencyRate())), leftPos + 78, topPos + 56, 0xFF00FF00, 0.75f, false);
        }
        
        renderWaterChannel(gg, mouseY, mouseY, partialTicks);
        renderAnimated(gg, mouseY, mouseY, partialTicks, this.timer);

        drawSlotNormal(gg, 150, 26, true, false, !menu.getSlot(0).hasItem());
        drawSlotNormal(gg, 150, 58, false, true, !menu.getSlot(2).hasItem());
    }

    public void renderEffectChannel(GuiGraphics gg, int mouseX, int mouseY, float partialTicks, int currentSpeed)
    {
        Minecraft mc = Minecraft.getInstance();
        gg.pose().pushPose();
        gg.pose().translate(leftPos + 108, topPos + 35, 0);
        gg.pose().mulPose(Axis.ZP.rotationDegrees((-360.0F / 180) * (mc.player.clientLevel.getGameTime() % 180)));
        gg.blit(WitherUtilsRegistry.loc("textures/item/withersteel_gear.png"), -10, -10, 0, 0, 20, 20, 20, 20);
        gg.pose().popPose();
    }
    
    public void renderWaterChannel(GuiGraphics gg, int mouseX, int mouseY, float partialTicks)
    {   
        float capacity = 64.0f;
        float time = menu.getBlockEntity().getLevel().getGameTime() + partialTicks;
        double offset = Math.sin(time * 4.0D / 14.0D) / 10.0D;
        
        float amount = menu.getBlockEntity().getGenerationRate() + (float) offset * menu.getBlockEntity().getGenerationRate();
        
        float scale = amount / capacity;
        int fluidAmount = (int) (scale * 32);
        
        FluidStack fluidStack = new FluidStack(Fluids.WATER, 1000);
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(IClientFluidTypeExtensions.of(fluidStack.getFluid()).getFlowingTexture(fluidStack));
        
        RenderSystem.setShaderColor(0.0f, 0.65f, 1, 1);

        int xPosition = leftPos + 114;
        int yPosition = topPos + 56;
        
        int maximum = 20;
        int desiredWidth = 18;
        int desiredHeight = fluidAmount;
        
        gg.blit(xPosition, yPosition + (maximum - desiredHeight), 0, desiredWidth, desiredHeight, sprite);

        RenderSystem.setShaderColor(1, 1, 1, 1);
    }
    
    public void renderAnimated(GuiGraphics gg, int mouseX, int mouseY, float partialTicks, int timer)
    {
        if(timer == 0)
            return;
        gg.pose().pushPose();
        gg.blit(WitherUtilsRegistry.loc("textures/block/blitz.png"), leftPos + 140, topPos + 44, 0, timer * 64, 32, 32, 32, 480);
        gg.pose().popPose();
    }
    
    @Override
    protected void containerTick()
    {
        super.containerTick();
        if(menu.getSlot(1).hasItem())
        {
            if(this.timer < 14)
                timer++;
            else
                timer = 0;
        }
        else
        {
            timer = 0;
        }
    }
    
    @Override
    protected String getBarName()
    {
        return "Water Generator";
    }
    
    @Override
    public ResourceLocation getBackgroundImage()
    {
        return WitherUtilsRegistry.loc("textures/gui/generator.png");
    }

    @Override
    protected Vector2i getBackgroundImageSize()
    {
        return new Vector2i(176, 131);
    }
    
    @Override
    public void onClose()
    {
        SoundUtil.playSound(menu.getBlockEntity().getLevel(), menu.getBlockEntity().getBlockPos(), WUTSounds.BUCKET.get(), 0.4f);
        super.onClose();
    }
}
