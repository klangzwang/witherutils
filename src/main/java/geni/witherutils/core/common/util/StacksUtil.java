package geni.witherutils.core.common.util;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang3.Validate;

import com.google.common.collect.Lists;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public class StacksUtil extends NonNullList<ItemStack> {
    
    protected StacksUtil(List<ItemStack> delegateIn, @Nullable ItemStack listType) {
        super(delegateIn, listType);
    }

    protected StacksUtil() {
        this(Lists.newArrayList(), null);
    }

    @SuppressWarnings("unchecked")
	public static StacksUtil create()
    {
        return new StacksUtil();
    }

    @SuppressWarnings("deprecation")
	public static StacksUtil withSize(int size, ItemStack fill) {
        Validate.notNull(fill);
        ItemStack[] objects = new ItemStack[size];
        Arrays.fill(objects, fill);
        return new StacksUtil(Arrays.asList(objects), fill);
    }

    public static StacksUtil from(ItemStack... elements) {
        return new StacksUtil(Arrays.asList(elements), ItemStack.EMPTY);
    }

    public static StacksUtil from(NonNullList<ItemStack> stacks) {
        return new StacksUtil(stacks, ItemStack.EMPTY);
    }

    public StacksUtil copy() {
        StacksUtil stacks = StacksUtil.create();
        forEach(stack -> stacks.add(stack.copy()));
        return stacks;
    }

    public int sum() {
        return stream().filter(stack -> !stack.isEmpty())
                .mapToInt(ItemStack::getCount).sum();
    }
}
