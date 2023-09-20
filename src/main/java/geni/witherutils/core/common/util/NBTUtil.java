package geni.witherutils.core.common.util;

import java.util.Collection;
import java.util.UUID;
import java.util.regex.Pattern;

import geni.witherutils.WitherUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidStack;

public class NBTUtil {

	public static final String TAG_STORABLE_STACK = WitherUtils.MODID + "tile_stack_nbt";

	public static boolean hasTag(ItemStack stack)
	{
		return stack.hasTag();
	}

	public static boolean hasKey(ItemStack stack, String key)
	{
		return hasTag(stack)&&stack.getOrCreateTag().contains(key);
	}

	public static boolean hasKey(ItemStack stack, String key, int type)
	{
		return hasTag(stack)&&stack.getOrCreateTag().contains(key, type);
	}
	public static void putInt(ItemStack stack, String key, int val)
	{
		stack.getOrCreateTag().putInt(key, val);
	}

	public static void modifyInt(CompoundTag tagCompound, String key, int mod)
	{
		tagCompound.putInt(key, tagCompound.getInt(key)+mod);
	}

	public static int getInt(ItemStack stack, String key)
	{
		return hasTag(stack)?stack.getOrCreateTag().getInt(key): 0;
	}

	public static void putString(ItemStack stack, String key, String val)
	{
		stack.getOrCreateTag().putString(key, val);
	}

	public static String getString(ItemStack stack, String key)
	{
		return hasTag(stack)?stack.getOrCreateTag().getString(key): "";
	}

	public static void putLong(ItemStack stack, String key, long val)
	{
		stack.getOrCreateTag().putLong(key, val);
	}

	public static long getLong(ItemStack stack, String key)
	{
		return hasTag(stack)?stack.getOrCreateTag().getLong(key): 0;
	}

	public static void putIntArray(ItemStack stack, String key, int[] val)
	{
		stack.getOrCreateTag().putIntArray(key, val);
	}

	public static int[] getIntArray(ItemStack stack, String key)
	{
		return hasTag(stack)?stack.getOrCreateTag().getIntArray(key): new int[0];
	}

	public static void putFloat(ItemStack stack, String key, float val)
	{
		stack.getOrCreateTag().putFloat(key, val);
	}

	public static void modifyFloat(CompoundTag tagCompound, String key, float mod)
	{
		tagCompound.putFloat(key, tagCompound.getFloat(key)+mod);
	}

	public static float getFloat(ItemStack stack, String key)
	{
		return hasTag(stack)?stack.getOrCreateTag().getFloat(key): 0;
	}

	public static void putBoolean(ItemStack stack, String key, boolean val)
	{
		stack.getOrCreateTag().putBoolean(key, val);
	}

	public static boolean getBoolean(ItemStack stack, String key)
	{
		return hasTag(stack)&&stack.getOrCreateTag().getBoolean(key);
	}
	
	public static CompoundTag getOrCreateTag(ItemStack stack)
	{
		if (!hasTag(stack))
		{
			stack.getOrCreateTag();
		}
		return stack.getTag();
	}
	
	public static void setTagCompound(ItemStack stack, String key, CompoundTag val)
	{
		stack.getOrCreateTag().put(key, val);
	}

	public static CompoundTag getTagCompound(ItemStack stack, String key)
	{
		return hasTag(stack)?stack.getOrCreateTag().getCompound(key): new CompoundTag();
	}
	public static FluidStack getFluidStack(ItemStack stack, String key)
	{
		if(hasTag(stack))
			return FluidStack.loadFluidStackFromNBT(getTagCompound(stack, key));
		return null;
	}

	public static void setItemStack(ItemStack stack, String key, ItemStack val)
	{
		stack.getOrCreateTag().put(key, val.save(new CompoundTag()));
	}

	public static ItemStack getItemStack(ItemStack stack, String key)
	{
		if(hasTag(stack)&&stack.getOrCreateTag().contains(key))
			return ItemStack.of(getTagCompound(stack, key));
		return ItemStack.EMPTY;
	}

	public static void setLore(ItemStack stack, Component... lore)
	{
		CompoundTag displayTag = getTagCompound(stack, "display");
		ListTag list = new ListTag();
		for(Component s : lore)
			list.add(StringTag.valueOf(Component.Serializer.toJson(s)));
		displayTag.put("Lore", list);
		setTagCompound(stack, "display", displayTag);
	}

	public static CompoundTag combineTags(CompoundTag target, CompoundTag add, Pattern pattern)
	{
		if(target==null||target.isEmpty())
			return add.copy();
		for(String key : add.getAllKeys())
			if(pattern==null||pattern.matcher(key).matches())
				if(!target.contains(key))
					target.put(key, add.get(key));
				else
				{
					switch(add.getTagType(key))
					{
						case Tag.TAG_BYTE -> target.putByte(key, (byte)(target.getByte(key)+add.getByte(key)));
						case Tag.TAG_SHORT -> target.putShort(key, (short)(target.getShort(key)+add.getShort(key)));
						case Tag.TAG_INT -> target.putInt(key, (target.getInt(key)+add.getInt(key)));
						case Tag.TAG_LONG -> target.putLong(key, (target.getLong(key)+add.getLong(key)));
						case Tag.TAG_FLOAT -> target.putFloat(key, (target.getFloat(key)+add.getFloat(key)));
						case Tag.TAG_DOUBLE -> target.putDouble(key, (target.getDouble(key)+add.getDouble(key)));
						case Tag.TAG_BYTE_ARRAY -> {
							byte[] bytesTarget = target.getByteArray(key);
							byte[] bytesAdd = add.getByteArray(key);
							byte[] bytes = new byte[bytesTarget.length+bytesAdd.length];
							System.arraycopy(bytesTarget, 0, bytes, 0, bytesTarget.length);
							System.arraycopy(bytesAdd, 0, bytes, bytesTarget.length, bytesAdd.length);
							target.putByteArray(key, bytes);
						}
						case Tag.TAG_STRING -> target.putString(key, (target.getString(key)+add.getString(key)));
						case Tag.TAG_LIST -> {
							ListTag listTarget = (ListTag)target.get(key);
							ListTag listAdd = (ListTag)add.get(key);
							listTarget.addAll(listAdd);
							target.put(key, listTarget);
						}
						case Tag.TAG_COMPOUND -> combineTags(target.getCompound(key), add.getCompound(key), null);
						case Tag.TAG_INT_ARRAY -> {
							int[] intsTarget = target.getIntArray(key);
							int[] intsAdd = add.getIntArray(key);
							int[] ints = new int[intsTarget.length+intsAdd.length];
							System.arraycopy(intsTarget, 0, ints, 0, intsTarget.length);
							System.arraycopy(intsAdd, 0, ints, intsTarget.length, intsAdd.length);
							target.putIntArray(key, ints);
						}
					}
				}
		return target;
	}

	public static void setItemStackBlockPos(ItemStack item, BlockPos pos) {
		if (pos == null || item.isEmpty()) {
			return;
		}
		NBTUtil.setItemStackNBTVal(item, "xpos", pos.getX());
		NBTUtil.setItemStackNBTVal(item, "ypos", pos.getY());
		NBTUtil.setItemStackNBTVal(item, "zpos", pos.getZ());
	}

	public static void putBlockPos(CompoundTag tag, BlockPos pos) {
		tag.putInt("xpos", pos.getX());
		tag.putInt("ypos", pos.getY());
		tag.putInt("zpos", pos.getZ());
	}

	public static BlockPos getItemStackBlockPos(ItemStack item) {
		if (item.isEmpty() || item.getTag() == null || !item.getTag().contains("xpos")) {
			return null;
		}
		CompoundTag tag = item.getOrCreateTag();
		return getBlockPos(tag);
	}

	public static BlockPos getBlockPos(CompoundTag tag) {
		return new BlockPos(tag.getInt("xpos"), tag.getInt("ypos"), tag.getInt("zpos"));
	}

	public static void setItemStackNBTVal(ItemStack item, String prop, int value) {
		if (item.isEmpty()) {
			return;
		}
		item.getOrCreateTag().putInt(prop, value);
	}

	public static CompoundTag getItemStackNBT(ItemStack held) {
		return held.getOrCreateTag();
	}
	
    // SETTERS ///////////////////////////////////////////////////////////////////
    public static CompoundTag getCompound(ItemStack stack) {
        if (stack.getTag() == null) stack.setTag(new CompoundTag());
        return stack.getTag();
    }

    public static ItemStack setByte(ItemStack stack, String tag, byte b) {
        getCompound(stack).putByte(tag, b);
        return stack;
    }

    public static ItemStack setBoolean(ItemStack stack, String tag, boolean b) {
        getCompound(stack).putBoolean(tag, b);
        return stack;
    }

    public static ItemStack setShort(ItemStack stack, String tag, short s) {
        getCompound(stack).putShort(tag, s);
        return stack;
    }

    public static ItemStack setInteger(ItemStack stack, String tag, int i) {
        getCompound(stack).putInt(tag, i);
        return stack;
    }

    public static ItemStack setLong(ItemStack stack, String tag, long i) {
        getCompound(stack).putLong(tag, i);
        return stack;
    }

    public static ItemStack setFloat(ItemStack stack, String tag, float f) {
        getCompound(stack).putFloat(tag, f);
        return stack;
    }

    public static ItemStack setDouble(ItemStack stack, String tag, double d) {
        getCompound(stack).putDouble(tag, d);
        return stack;
    }

    public static ItemStack setString(ItemStack stack, String tag, String s) {
        getCompound(stack).putString(tag, s);
        return stack;
    }

    public static ItemStack setUUID(ItemStack stack, String tag, UUID uuid) {
        getCompound(stack).putUUID(tag, uuid);
        return stack;
    }

    // GETTERS ///////////////////////////////////////////////////////////////////

    public static boolean verifyExistance(ItemStack stack, String tag) {
        CompoundTag compound = stack.getTag();
        if (compound == null) return false;
        else return stack.getTag().contains(tag);
    }

    public static byte getByte(ItemStack stack, String tag, int defaultExpected) {
        return verifyExistance(stack, tag) ? stack.getTag().getByte(tag) : (byte) defaultExpected;
    }

    public static boolean getBoolean(ItemStack stack, String tag, boolean defaultExpected) {
        return verifyExistance(stack, tag) ? stack.getTag().getBoolean(tag) : defaultExpected;
    }

    public static short getShort(ItemStack stack, String tag, short defaultExpected) {
        return verifyExistance(stack, tag) ? stack.getTag().getShort(tag) : defaultExpected;
    }

    public static int getInteger(ItemStack stack, String tag, int defaultExpected) {
        return verifyExistance(stack, tag) ? stack.getTag().getInt(tag) : defaultExpected;
    }

    public static long getLong(ItemStack stack, String tag, long defaultExpected) {
        return verifyExistance(stack, tag) ? stack.getTag().getLong(tag) : defaultExpected;
    }

    public static float getFloat(ItemStack stack, String tag, float defaultExpected) {
        return verifyExistance(stack, tag) ? stack.getTag().getFloat(tag) : defaultExpected;
    }

    public static double getDouble(ItemStack stack, String tag, double defaultExpected) {
        return verifyExistance(stack, tag) ? stack.getTag().getDouble(tag) : defaultExpected;
    }

    public static String getString(ItemStack stack, String tag, String defaultExpected) {
        return verifyExistance(stack, tag) ? stack.getTag().getString(tag) : defaultExpected;
    }

    public static UUID getUUID(ItemStack stack, String tag, UUID defaultExpected) {
        return verifyExistance(stack, tag) ? stack.getTag().getUUID(tag) : defaultExpected;
    }
    
    public static <T extends Collection<BlockPos>> T readPosList(CompoundTag nbt, String key, T list) {
        ListTag listNBT = nbt.getList(key, Tag.TAG_COMPOUND);
        for (int i = 0; i < listNBT.size(); i++) {
            CompoundTag compound = listNBT.getCompound(i);
            list.add(readPos(compound, "Pos"));
        }
        return list;
    }

    public static void writePosList(CompoundTag nbt, Collection<BlockPos> list, String key) {
        ListTag listNBT = new ListTag();
        list.forEach(pos -> {
            CompoundTag compound = new CompoundTag();
            writePos(compound, pos, "Pos");
            listNBT.add(compound);
        });
        nbt.put(key, listNBT);
    }

    public static BlockPos readPos(CompoundTag nbt, String key) {
        return NbtUtils.readBlockPos(nbt.getCompound(key));
    }

    public static void writePos(CompoundTag nbt, BlockPos pos, String key) {
        nbt.put(key, NbtUtils.writeBlockPos(pos));
    }
    
    /*
     * 
     * AABB
     * 
     */
	public static void putAABB(CompoundTag tag, AABB aabb)
	{
		tag.putInt("minx", (int) aabb.minX);
		tag.putInt("miny", (int) aabb.minY);
		tag.putInt("minz", (int) aabb.minZ);
		tag.putInt("maxx", (int) aabb.maxX);
		tag.putInt("maxy", (int) aabb.maxY);
		tag.putInt("maxz", (int) aabb.maxZ);
	}
	public static AABB getAABB(CompoundTag tag)
	{
		AABB aabb = new AABB(0, 0, 0, 0, 0, 0);
		aabb.setMinX(tag.getInt("minx"));
		aabb.setMinY(tag.getInt("miny"));
		aabb.setMinZ(tag.getInt("minz"));
		aabb.setMaxX(tag.getInt("maxx"));
		aabb.setMaxY(tag.getInt("maxy"));
		aabb.setMaxZ(tag.getInt("maxz"));
		return aabb;
	}
	
    public static CompoundTag getPersistentTag(final Player player)
    {
        return getOrInitTagCompound(player.getPersistentData(), "PlayerPersisted", null);
    }
    public static CompoundTag getOrCreatePersistentTag(final Player player)
    {
        return getOrInitTagCompound(player.getPersistentData(), "PlayerPersisted", new CompoundTag());
    }
    
    public static CompoundTag getOrInitTagCompound(final CompoundTag parent, final String key)
    {
        return getOrInitTagCompound(parent, key, null);
    }
    public static CompoundTag getOrInitTagCompound(final CompoundTag parent, final String key, CompoundTag defaultTag)
    {
        if (parent.contains(key, 10))
        {
            return parent.getCompound(key);
        }
        if (defaultTag == null)
        {
            defaultTag = new CompoundTag();
        }
        else
        {
            defaultTag = defaultTag.copy();
        }
        parent.put(key, defaultTag);
        return defaultTag;
    }
    
    
    public static CompoundTag getOrInitTagCompound(final ItemStack stack)
    {
    	CompoundTag tags = stack.getOrCreateTag();
        if (tags != null)
        {
            return tags;
        }
        tags = new CompoundTag();
        stack.setTag(tags);
        return tags;
    }
}
