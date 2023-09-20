package geni.witherutils.core.common.fertilizer;

import javax.annotation.Nonnull;

import geni.witherutils.api.farm.IFertilizer;
import geni.witherutils.core.common.util.NNList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class Fertilizer {

	private static IForgeRegistry<IFertilizer> REGISTRY = null;

//	@SubscribeEvent(priority = EventPriority.NORMAL)
//	public static void registerRegistry(@Nonnull NewRegistryEvent event)
//	{
//		event.create(new RegistryBuilder<IFertilizer>().setName(new ResourceLocation(WitherUtils.MODID, "fertilizer")).setIDRange(0, Integer.MAX_VALUE - 1));
//	}
//
//	@SubscribeEvent(priority = EventPriority.HIGH)
//	public static void registerFertilizer(@Nonnull RegisterEvent event)
//	{
//		event.getForgeRegistry().register("", new Bonemeal(new ItemStack(Items.BONE_MEAL, 1)));
//	}
//
//	public static @Nonnull IFertilizer getInstance(@Nonnull ItemStack stack)
//	{
//		for (IFertilizer fertilizer : REGISTRY.getValues())
//		{
//			if (fertilizer.matches(stack))
//			{
//				return fertilizer;
//			}
//		}
//		return NoFertilizer.getNone();
//	}

	public static boolean isFertilizer(@Nonnull ItemStack stack)
	{
		final ResourceLocation name = new ResourceLocation("industrialforegoing", "fertilizer");
		if(ForgeRegistries.ITEMS.containsKey(name))
		{
			return true;
		}
		
		for (Item item : ForgeRegistries.ITEMS.getValues())
		{
			if(item == stack.getItem())
			{
				if(stack.getItem() instanceof IFertilizer fertilizer)
				{
					return true;
				}
				else if(stack.getItem() instanceof BoneMealItem)
				{
					return true;
				}
			}
		}
		return false;
	}

	public static @Nonnull NNList<ItemStack> getStacks()
	{
		NNList<ItemStack> result = new NNList<>();
		for (IFertilizer fertilizer : REGISTRY.getValues())
		{
			result.addAll(fertilizer.getGuiItem());
		}
		return result;
	}
}
