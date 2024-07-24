package geni.witherutils.core.common.item;

import java.util.function.Consumer;
import java.util.function.Supplier;

import geni.witherutils.base.client.render.item.RotatingItemBEWLR;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public interface IRotatingItem {
	
    float getTicksPerRotation();
    boolean hasItemOnTop();
    boolean isRotating();
    
    default void setupBEWLR(Consumer<IClientItemExtensions> consumer)
    {
        consumer.accept(new IClientItemExtensions()
        {
            final Supplier<BlockEntityWithoutLevelRenderer> renderer = () -> RotatingItemBEWLR.INSTANCE;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer()
            {
                return renderer.get();
            }
        });
    }
}
