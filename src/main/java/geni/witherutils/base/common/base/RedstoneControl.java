package geni.witherutils.base.common.base;

import java.util.function.UnaryOperator;

import geni.witherutils.WitherUtils;
import geni.witherutils.core.client.gui.IIcon;
import geni.witherutils.core.common.math.Vector2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public enum RedstoneControl implements IIcon {
    
    ALWAYS_ACTIVE(bool -> true, Component.translatable("gui.witherutils.redstone_always_active")),
    ACTIVE_WITH_SIGNAL(bool -> bool, Component.translatable("gui.witherutils.redstone_active_with_signal")),
    ACTIVE_WITHOUT_SIGNAL(bool -> !bool, Component.translatable("gui.witherutils.redstone_active_without_signal")),
    NEVER_ACTIVE(bool -> false, Component.translatable("gui.witherutils.redstone_never_active"));
    
    private static final ResourceLocation TEXTURE = WitherUtils.loc("textures/gui/redstone_control.png");
    private static final Vector2i SIZE = new Vector2i(12, 12);

    private final UnaryOperator<Boolean> isActive;

    private final Vector2i pos;
    private final Component tooltip;

    RedstoneControl(UnaryOperator<Boolean> isActive, Component tooltip)
    {
        this.isActive = isActive;
        pos = new Vector2i(12*ordinal(), 0);
        this.tooltip = tooltip;
    }
    public boolean isActive(boolean hasRedstone)
    {
        return isActive.apply(hasRedstone);
    }
    @Override
    public ResourceLocation getTextureLocation()
    {
        return TEXTURE;
    }
    @Override
    public Vector2i getIconSize()
    {
        return SIZE;
    }
    @Override
    public Vector2i getTexturePosition()
    {
        return pos;
    }
    @Override
    public Component getTooltip()
    {
        return tooltip;
    }
}
