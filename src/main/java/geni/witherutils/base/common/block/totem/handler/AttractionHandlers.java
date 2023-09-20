package geni.witherutils.base.common.block.totem.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class AttractionHandlers {

	public static final AttractionHandlers instance = new AttractionHandlers();

	private final Map<IMobAttractionHandler, Item> registry = new HashMap<>();

	private AttractionHandlers()
	{
		registry.put(new MobPullHandler(), Items.CHAIN);
		registry.put(new MobSwitchHandler(), Items.HOPPER);
		registry.put(new MobFireHandler(), Items.TORCH);
	}

	public static void register(IMobAttractionHandler handler, Item item)
	{
		instance.registry.put(handler, item);
	}

	public Map<IMobAttractionHandler, Item> getRegistry()
	{
		return registry;
	}
	
	public List<Item> getItemRegistry()
	{
		List<Item> itemList = new ArrayList<>(); 
		for(Item item : getRegistry().values())
		{
			itemList.add(item);
		}
		return itemList;
	}
	
	public List<IMobAttractionHandler> getHandlerRegistry()
	{
		List<IMobAttractionHandler> handlerList = new ArrayList<>(); 
		for(IMobAttractionHandler handler : registry.keySet())
		{
			handlerList.add(handler);
		}
		return handlerList;
	}
}
