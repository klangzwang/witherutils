package geni.witherutils.core.common.recipes;

import com.mojang.datafixers.util.Either;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public record OutputStack(Either<ItemStack, FluidStack> stack) {

    public static final OutputStack EMPTY = OutputStack.of(ItemStack.EMPTY);

    public static OutputStack of(ItemStack itemStack)
    {
        return new OutputStack(Either.left(itemStack));
    }
    public static OutputStack of(FluidStack fluidStack)
    {
        return new OutputStack(Either.right(fluidStack));
    }
    public ItemStack getItem()
    {
        return stack.left().orElse(ItemStack.EMPTY);
    }
    public FluidStack getFluid() 
    {
        return stack.right().orElse(FluidStack.EMPTY);
    }
    public boolean isItem()
    {
        return stack.left().isPresent();
    }
    public boolean isFluid()
    {
        return stack.right().isPresent();
    }
    public boolean isEmpty()
    {
        if (isItem())
            return stack.left().get().isEmpty();
        if (isFluid())
            return stack.right().get().isEmpty();
        return true;
    }
    public CompoundTag serializeNBT()
    {
        CompoundTag tag = new CompoundTag();
        if (isItem())
        {
            tag.put("item", stack.left().get().serializeNBT());
        }
        else if (isFluid())
        {
            tag.put("fluid", stack.right().get().writeToNBT(new CompoundTag()));
        }
        return tag;
    }
    public static OutputStack fromNBT(CompoundTag tag)
    {
        if (tag.contains("item"))
        {
            return OutputStack.of(ItemStack.of(tag.getCompound("item")));
        }
        else if (tag.contains("fluid"))
        {
            return OutputStack.fromNBT(tag.getCompound("fluid"));
        }
        return OutputStack.EMPTY;
    }
}
