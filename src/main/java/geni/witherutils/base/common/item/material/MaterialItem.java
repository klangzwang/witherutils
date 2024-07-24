package geni.witherutils.base.common.item.material;

import java.util.function.Consumer;

import geni.witherutils.base.common.base.WitherItem;
import geni.witherutils.core.common.item.IRotatingItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class MaterialItem extends WitherItem implements IRotatingItem {

    private final boolean hasGlint;
    private final boolean hasItemOnTop;
    private final boolean isRotating;
    private final float tickRotate;
    
    public MaterialItem(Properties props, boolean hasGlint, boolean hasItemOnTop, boolean isRotating, float tickRotate)
    {
        super(props);
        this.hasGlint = hasGlint;
        this.hasItemOnTop = hasItemOnTop;
        this.isRotating = isRotating;
        this.tickRotate = tickRotate;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer)
    {
    	setupBEWLR(consumer);
    }
    @Override
    public float getTicksPerRotation()
    {
        return tickRotate;
    }
    @Override
    public boolean hasItemOnTop()
    {
        return hasItemOnTop;
    }
    @Override
    public boolean isRotating()
    {
        return isRotating;
    }
    @Override
    public boolean isFoil(ItemStack itemStack)
    {
        return hasGlint;
    }
}
