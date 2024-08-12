package geni.witherutils.core.common.util;

import java.util.Map;
import java.util.UUID;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.UsernameCache;

public class PlayerUtil {

	public static UUID getPlayerUUID(String username)
	{
		for (Map.Entry<UUID, String> entry : UsernameCache.getMap().entrySet())
		{
			if (entry.getValue().equalsIgnoreCase(username))
			{
				return entry.getKey();
			}
		}
		return null;
	}
	public static UUID getPlayerUIDUnstable(String possibleUUID)
	{
		if (possibleUUID == null || possibleUUID.isEmpty())
			return null;
		UUID uuid = null;
		try
		{
			uuid = UUID.fromString(possibleUUID);
		}
		catch (IllegalArgumentException e)
		{
			uuid = getPlayerUUID(possibleUUID);
		}
		return uuid;
	}
	public static void setPlayerReach(Player player, int currentReach)
	{
//		player.getAttribute(NeoForgeMod.NAMETAG_DISTANCE.getKey()).setBaseValue(currentReach);
	}
	public static double getExpTotal(Player player)
	{
		int level = player.experienceLevel;
		double totalExp = getXpForLevel(level);
		double progress = Math.round(player.getXpNeededForNextLevel() * player.experienceProgress);
		totalExp += (int) progress;
		return totalExp;
	}
	public static int getXpForLevel(int level)
	{
		int totalExp = 0;
		if (level <= 15)
		{
			totalExp = level * level + 6 * level;
		}
		else if (level <= 30)
		{
			totalExp = (int) (2.5 * level * level - 40.5 * level + 360);
		}
		else
		{
			totalExp = (int) (4.5 * level * level - 162.5 * level + 2220);
		}
		return totalExp;
	}
	public static ItemStack getPlayerItemIfHeld(Player player)
	{
		ItemStack wand = player.getMainHandItem();
		if(wand.isEmpty())
		{
			wand = player.getOffhandItem();
		}
		return wand;
	}
	public static int getFirstSlotWithBlock(Player player, BlockState targetState)
	{
		int ret = -1;
		ItemStack stack;
		for(int i = 0; i < player.getInventory().getContainerSize(); i++)
		{
			stack = player.getInventory().getItem(i);
			if(!stack.isEmpty() && stack.getItem() != null && Block.byItem(stack.getItem()) == targetState.getBlock())
			{
				return i;
			}
		}
		return ret;
	}
	public static BlockState getBlockstateFromSlot(Player player, int slot)
	{
		ItemStack stack = player.getInventory().getItem(slot);
		if(!stack.isEmpty() && stack.getItem() != null && Block.byItem(stack.getItem()) != null)
		{
			Block b = Block.byItem(stack.getItem());
			return b.defaultBlockState();
		}
		return null;
	}
	public static void decrStackSize(Player player, int slot)
	{
		if(!player.isCreative() && slot >= 0)
		{
			player.getInventory().removeItem(slot, 1);
		}
	}
	public static Item getItemArmorSlot(Player player, EquipmentSlot slot)
	{
		ItemStack inslot = player.getInventory().armor.get(slot.getIndex());
		Item item = (inslot.isEmpty()) ? null : inslot.getItem();
		return item;
	}
	public static boolean canBlockDamageSource(LivingEntity entity, DamageSource damageSourceIn)
	{
		if(damageSourceIn.isDirect() && entity.isBlocking())
		{
			Vec3 vec3d = damageSourceIn.getSourcePosition();
			if(vec3d!=null)
			{
				Vec3 vec3d1 = entity.getViewVector(1.0F);
				Vec3 vec3d2 = vec3d.vectorTo(entity.position()).normalize();
				vec3d2 = new Vec3(vec3d2.x, 0.0D, vec3d2.z);
				return vec3d2.dot(vec3d1) < 0;
			}
		}
		return false;
	}
    public static StacksUtil invStacks(Player player)
    {
        StacksUtil stacks = StacksUtil.create();
        Inventory inventory = player.getInventory();
        for(int i = 0; i < inventory.items.size(); i++)
        {
        	stacks.add(inventory.items.get(i));
        }
        return stacks;
    }
    public static StacksUtil invArmorStacks(Player player)
    {
        StacksUtil stacks = StacksUtil.create();
        Inventory inventory = player.getInventory();
        for(int i = 0; i < inventory.armor.size(); i++)
        {
        	stacks.add(inventory.armor.get(i));
        }
        return stacks;
    }
    public static boolean isPlayerCrouching(Entity entity)
    {
        return entity instanceof Player && entity.isCrouching();
    }
}
