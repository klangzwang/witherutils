package geni.witherutils.core.common.util;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class CompatibilityUtil {

	@Nullable
	public static IFluidHandler getFluidHandler(BlockEntity te, Direction side)
	{
		if (te == null) return null;
		return te.getCapability(ForgeCapabilities.FLUID_HANDLER, side).orElse(null);
	}
	public static IFluidHandler getTank(LevelReader world, BlockPos pos, Direction side)
	{
		BlockEntity te = world.getBlockEntity(pos);
		if (te == null) return null;
		return te.getCapability(ForgeCapabilities.FLUID_HANDLER).orElse(null);
	}
	public static IFluidHandler getBucketHandler(ItemStack bucket)
	{
		if(bucket == null) return null;
		return bucket.getCapability(ForgeCapabilities.FLUID_HANDLER).orElse(null);
	}
	public static IFluidHandler getBucketHandlerSlot(ItemStackHandler handler, int slot)
	{
		if(handler.getStackInSlot(slot).getItem() instanceof BucketItem)
		{
			return handler.getStackInSlot(slot).getCapability(ForgeCapabilities.FLUID_HANDLER).orElse(null);			
		}
		return null;
	}

	@Nullable
	public static IItemHandler getItemHandler(BlockEntity te, Direction side)
	{
		if (te == null) return null;
		return te.getCapability(ForgeCapabilities.ITEM_HANDLER, side).orElse(null);
	}
	public static IItemHandler getItems(LevelReader world, BlockPos pos, Direction side)
	{
		BlockEntity te = world.getBlockEntity(pos);
		if (te == null) return null;
		return te.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
	}
	
	@Nullable
	public static IItemHandler getPlayerItemHandler(Player player)
	{
		if (player == null) return null;
		return player.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
	}
	
	@Nullable
	public static IEnergyStorage getEnergyHandler(BlockEntity te, Direction side)
	{
		if (te == null) return null;
		return te.getCapability(ForgeCapabilities.ENERGY, side).orElse(null);
	}
	public static IEnergyStorage getEnergy(Level world, BlockPos pos, Direction side)
	{
		BlockEntity te = world.getBlockEntity(pos);
		if (te == null) return null;
		return te.getCapability(ForgeCapabilities.ENERGY).orElse(null);
	}
	
	public static boolean isFluidHandler(BlockEntity te, Direction side)
	{
		return te != null && (te.getCapability(ForgeCapabilities.FLUID_HANDLER, side).isPresent());
	}
	
	public static boolean isItemHandler(BlockEntity te, Direction side)
	{
		return te != null && (te.getCapability(ForgeCapabilities.ITEM_HANDLER, side).isPresent());
	}
	
	public static boolean isEnergyHandler(BlockEntity te, Direction side)
	{
		return te != null && (te.getCapability(ForgeCapabilities.ENERGY, side).isPresent());
	}

	public static boolean isBucketFluidHandler(ItemStack bucket)
	{
		return bucket != null && (bucket.getCapability(ForgeCapabilities.FLUID_HANDLER).isPresent());
	}
}