package geni.witherutils.core.client.gui.widgets;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

import geni.witherutils.WitherUtils;
import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.common.base.WitherMachineEnergyBlockEntity;
import geni.witherutils.base.common.io.energy.IWitherEnergyStorage;
import geni.witherutils.base.common.menu.WitherMachineMenu;
import geni.witherutils.core.client.gui.screen.WUTScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings({ "rawtypes", "unused" })
public class EnergyWidget extends WUTWidget {

    private WUTScreen parent;
    private WitherMachineMenu menu;
    private final Font font;
    private final Supplier<IWitherEnergyStorage> storageSupplier;
    private int leftPos;
    private int topPos;
    private int x;
    private int y;
    private int width;
    private int height;

    public EnergyWidget(WUTScreen parent, WitherMachineMenu menu, Font font, Supplier<IWitherEnergyStorage> storageSupplier, int x, int y, int width, int height)
    {
        super(x, y, width, height);
        this.parent = parent;
        this.menu = menu;
        this.font = font;
        this.storageSupplier = storageSupplier;
        this.leftPos = parent.getGuiLeft();
        this.topPos = parent.getGuiTop();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void renderWidget(GuiGraphics gg, int mouseX, int mouseY, float partialTicks)
    {
        IWitherEnergyStorage storage = storageSupplier.get();
        if (storage.getMaxEnergyStored() <= 0)
        {
            renderToolTip(gg, mouseX, mouseY);
            renderBank(gg, mouseX, mouseY, partialTicks);
            return;
        }

        float filledVolume = storage.getEnergyStored() / (float) storage.getMaxEnergyStored();
        int renderableHeight = (int)(filledVolume * height);
        
        gg.blit(WitherUtilsRegistry.loc("textures/gui/energy_bar.png"), x, y, 16, 0, width, height, 32, height);
        float pct = Math.min(filledVolume, 1.0F);
        gg.blit(WitherUtilsRegistry.loc("textures/gui/energy_bar.png"), x, y, 0, 0, width, height - (int) (height * pct), 32, height);

        renderToolTip(gg, mouseX, mouseY);
    }
    
    public void renderBank(GuiGraphics gg, int mouseX, int mouseY, float partialTicks)
    {
        gg.pose().pushPose();
        gg.blit(WitherUtilsRegistry.loc("textures/gui/energy_bar_locked.png"), x, y, 0, 0, 16, 40, 16, 40);
        gg.pose().popPose();
        
        gg.pose().pushPose();
        float time = menu.getBlockEntity().getLevel().getGameTime() + partialTicks;
        double offset = Math.sin(time * 4.0D / 12.0D) / 10.0D;
        
        gg.pose().translate(0.0D, 0.0D + offset * 45, 0.0D);
        gg.blit(WitherUtilsRegistry.loc("textures/item/soulbank/soulbank.png"), x, y + 36, 0, 0, 16, 16, 16, 16);
        gg.pose().popPose();
    }
    
    public void renderToolTip(GuiGraphics gg, int mouseX, int mouseY)
    {
        if(isHovered(mouseX, mouseY))
        {
            List<Component> list = new ArrayList<>();
            if(menu.getSlot(0).hasItem())
            {
                IWitherEnergyStorage energyStorage = storageSupplier.get();
                ItemStack stack = menu.getSlot(0).getItem();
                NumberFormat fmt = NumberFormat.getInstance(Locale.ENGLISH);
//                list.add(Component.translatable(ChatFormatting.GREEN + "BankType: ").append("" + SoulBankUtil.getSoulBankData(stack).get()));
                list.add(Component.translatable(ChatFormatting.AQUA + "Stored Energy: ").append(fmt.format(energyStorage.getEnergyStored()) + "/" + fmt.format(energyStorage.getMaxEnergyStored())).append(Component.translatable(ChatFormatting.AQUA + " FE")));
                list.add(Component.translatable(ChatFormatting.DARK_GRAY + "Capacity: ").append(fmt.format(energyStorage.getMaxEnergyStored())).append(Component.translatable(ChatFormatting.AQUA + " FE")));
                list.add(Component.translatable(ChatFormatting.DARK_GRAY + "Transfer: ").append(fmt.format(energyStorage.getMaxEnergyUse())).append(Component.translatable(ChatFormatting.AQUA + " FE/t")));
                list.add(Component.translatable(ChatFormatting.DARK_GRAY + "MaxCost: ").append(fmt.format(energyStorage.getMaxEnergyUse())).append(Component.translatable(ChatFormatting.AQUA + " FE")));
            }
            else
            {
                list.add(Component.translatable(ChatFormatting.AQUA + "BankSlot Info"));
                list.add(Component.translatable(ChatFormatting.WHITE + "SoulBank not found"));
            }
            gg.renderComponentTooltip(font, list, mouseX, mouseY); 
        }
    }

	@Override
	protected void updateWidgetNarration(NarrationElementOutput p_259858_)
	{
	}
}
