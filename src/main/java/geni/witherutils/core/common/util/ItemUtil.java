package geni.witherutils.core.common.util;

import java.util.Random;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemUtil {

    private static final Random rand = new Random();

    /**
     * Tests if two {@link ItemStack}s are completely equal, forgoing stack size.
     * This means that for this method to return true, Item type, damage value, and
     * NBT data of both ItemStacks must be identical.
     * 
     * @param s1 The first ItemStack to compare.
     * @param s2 The second ItemStack to compare.
     * @author powercrystals
     */
    public static boolean stacksEqual(ItemStack s1, ItemStack s2) {
        if (s1 == null && s2 == null)
            return true;
        if (s1 == null || s2 == null)
            return false;
        if (!s1.equals(s2))
            return false;
        if (s1.getTag() == null && s2.getTag() == null)
            return true;
        if (s1.getTag() == null || s2.getTag() == null)
            return false;
        return s1.getTag().equals(s2.getTag());
    }

    /**
     * Spawns an ItemStack into the world with motion that simulates a normal block
     * drop.
     * 
     * @param world The world object.
     * @param item  The ItemStack to spawn.
     * @param x     X coordinate of the block in which to spawn the entity.
     * @param y     Y coordinate of the block in which to spawn the entity.
     * @param z     Z coordinate of the block in which to spawn the entity.
     */
    public static void spawnItemInWorldWithRandomMotion(Level world, ItemStack item, int x, int y, int z) {
        if (item != null) {
            spawnItemInWorldWithRandomMotion(new ItemEntity(world, x + 0.5, y + 0.5, z + 0.5, item));
        }
    }

    /**
     * Spawns an EntityItem into the world with motion that simulates a normal block
     * drop.
     * 
     * @param entity The entity to spawn.
     */
    public static void spawnItemInWorldWithRandomMotion(ItemEntity entity) {
        entity.setPickUpDelay(10);

        float f = (rand.nextFloat() * 0.1f) - 0.05f;
        float f1 = (rand.nextFloat() * 0.1f) - 0.05f;
        float f2 = (rand.nextFloat() * 0.1f) - 0.05f;

        entity.xo += f;
        entity.yo += f1;
        entity.zo += f2;

        entity.level().addFreshEntity(entity);
    }

    @SuppressWarnings("unused")
    private final static int min(int i1, int i2, int i3) {
        return i1 < i2 ? (i1 < i3 ? i1 : i3) : (i2 < i3 ? i2 : i3);
    }

    public static boolean areStackMergable(ItemStack s1, ItemStack s2) {
        if (s1 == null || s2 == null || !s1.isStackable() || !s2.isStackable()) {
            return false;
        }
        if (!s1.equals(s2)) {
            return false;
        }
        return ItemStack.isSameItem(s1, s2);
    }

    /**
     * Checks if items, damage and NBT are equal.
     * 
     * @param s1
     * @param s2
     * @return True if the two stacks are equal, false otherwise.
     */
    public static boolean areStacksEqual(ItemStack s1, ItemStack s2) {
        if (s1 == null || s2 == null) {
            return false;
        }
        if (!s1.equals(s2)) {
            return false;
        }
        return ItemStack.isSameItem(s1, s2);
    }

    private interface ISlotIterator {
        int nextSlot();

        boolean hasNext();
    }

    @SuppressWarnings("unused")
    private final static class invSlotter implements ISlotIterator {
        private static final invSlotter me = new invSlotter();
        private int end;
        private int current;

        public final static invSlotter getInstance(int start, int end) {
            me.end = end;
            me.current = start;
            return me;
        }

        @Override
        public final int nextSlot() {
            return current++;
        }

        @Override
        public final boolean hasNext() {
            return current < end;
        }
    }

    @SuppressWarnings("unused")
    private final static class sidedSlotter implements ISlotIterator {
        private static final sidedSlotter me = new sidedSlotter();
        private int[] slots;
        private int current;

        public final static sidedSlotter getInstance(int[] slots) {
            me.slots = slots;
            me.current = 0;
            return me;
        }

        @Override
        public final int nextSlot() {
            return slots[current++];
        }

        @Override
        public final boolean hasNext() {
            return slots != null && current < slots.length;
        }
    }
    
    public static boolean isStackFull(ItemStack contents)
    {
        if (contents == null)
        {
        	return false;
        }
        return contents.getMaxStackSize() >= contents.getMaxStackSize();
    }
}
