package geni.witherutils.base.common.block.tank.drum;

import java.util.List;

import geni.witherutils.base.common.base.WitherFluidHandlerCapability;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TankDrumBlockItem extends BlockItem {

	public TankDrumBlockItem(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
	}

	@Override
	public boolean isBarVisible(ItemStack stack)
	{
		FluidStack fstack = copyFluidFromStack(stack);
		return fstack != null && fstack.getAmount() > 0;
	}

	@Override
	public int getBarWidth(ItemStack stack)
	{
		try
		{
			FluidStack fstack = copyFluidFromStack(stack);
			float current = fstack.getAmount();
			float max = TankDrumBlockEntity.CAPACITY;
			return Math.round(13.0F * current / max);
		}
		catch (Throwable e)
		{
		}
		return 1;
	}

	public static FluidStack copyFluidFromStack(ItemStack stack)
	{
		if (stack.getTag() != null)
		{
			WitherFluidHandlerCapability handler = new WitherFluidHandlerCapability(stack, TankDrumBlockEntity.CAPACITY);
			return handler.getFluid();
		}
		return null;
	}

	@Override
	public int getBarColor(ItemStack stack)
	{
		return 0xADD8E6;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
	{
		IFluidHandler storage = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse(null);
		if (storage != null)
		{
			FluidStack fs = storage.getFluidInTank(0);
			if (fs != null && !fs.isEmpty())
			{
				MutableComponent t = Component.translatable(fs.getDisplayName().getString() + " " + fs.getAmount() + "/" + storage.getTankCapacity(0));
				t.withStyle(ChatFormatting.GRAY);
				tooltip.add(t);
				return;
			}
		}
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, net.minecraft.nbt.CompoundTag nbt)
	{
		return new WitherFluidHandlerCapability(stack, TankDrumBlockEntity.CAPACITY);
	}
}
