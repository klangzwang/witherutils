package geni.witherutils.core.client.gui;

import geni.witherutils.api.UseOnly;
import geni.witherutils.core.common.math.Vector2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.LogicalSide;

public interface IIcon {
    
    Vector2i DEFAULT_TEXTURE_SIZE = new Vector2i(256, 256);

    @UseOnly(LogicalSide.CLIENT)
    ResourceLocation getTextureLocation();

    @UseOnly(LogicalSide.CLIENT)
    Vector2i getIconSize();

    @UseOnly(LogicalSide.CLIENT)
    default Vector2i getRenderSize()
    {
        return getIconSize();
    }

    @UseOnly(LogicalSide.CLIENT)
    Vector2i getTexturePosition();

    @UseOnly(LogicalSide.CLIENT)
    default Component getTooltip()
    {
        return Component.empty();
    }

    @UseOnly(LogicalSide.CLIENT)
    default Vector2i getTextureSize()
    {
        return DEFAULT_TEXTURE_SIZE;
    }

    @UseOnly(LogicalSide.CLIENT)
    default boolean shouldRender()
    {
        return true;
    }
}
