package geni.witherutils.base.common.base;

import java.util.function.Consumer;

import geni.witherutils.api.item.IRotatingItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class WitherBlockItem extends BlockItem implements IRotatingItem {
	
    private final boolean hasGlint;
    private final boolean hasItemOnTop;
    private final boolean isRotating;
    private final float tickRotate;
    
    public WitherBlockItem(Block blockIn, Properties builder, boolean hasGlint, boolean hasItemOnTop, boolean isRotating, float tickRotate)
    {
        super(blockIn, builder);
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