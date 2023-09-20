package geni.witherutils.api.item;

import java.util.function.Consumer;

import geni.witherutils.base.client.render.item.RotatingItemBEWLR;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.util.NonNullLazy;

public interface IRotatingItem {
	
    float getTicksPerRotation();
    boolean hasItemOnTop();
    boolean isRotating();
    
    default void setupBEWLR(Consumer<IClientItemExtensions> consumer)
    {
        consumer.accept(new IClientItemExtensions()
        {
            final NonNullLazy<BlockEntityWithoutLevelRenderer> renderer = NonNullLazy.of(() -> RotatingItemBEWLR.INSTANCE);

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer()
            {
                return renderer.get();
            }
        });
    }
}
