package geni.witherutils.base.common.event;

import java.util.LinkedList;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WitherItemCaptureEvents {

	static WitherItemCaptureEvents INSTANCE = new WitherItemCaptureEvents();
	static ThreadLocal<LinkedList<ItemStack>> capturing = new ThreadLocal<>();

	static {
		MinecraftForge.EVENT_BUS.register(INSTANCE);
	}

	public static void startCapturing()
	{
		if(capturing.get() != null)
		{
			throw new IllegalStateException();
		}
		capturing.set(new LinkedList<>());
	}

	public static LinkedList<ItemStack> stopCapturing()
	{
		LinkedList<ItemStack> list = capturing.get();
		if(list == null)
			throw new IllegalStateException();
		capturing.set(null);
		return list;
	}

	@SubscribeEvent
	public void onItemJoin(EntityJoinLevelEvent event)
	{
		LinkedList<ItemStack> list = capturing.get();
		if(list == null) return;

		Entity entity = event.getEntity();
		if(entity instanceof ItemEntity)
		{
			ItemStack stack = ((ItemEntity) entity).getItem();
			list.add(stack);
			event.setCanceled(true);
		}
	}
}
