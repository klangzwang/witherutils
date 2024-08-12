package geni.witherutils.core.common.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;

import geni.witherutils.base.common.config.common.LootConfig;
import net.minecraft.Util;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@SuppressWarnings("deprecation")
public class ConfigUtil {
	
	public static boolean dimensionKeyIslisted(ResourceLocation dimensionKey)
	{
		final List<? extends String> dimensionlist = LootConfig.SOULORBDROPLIST.get();
		for (String key : dimensionlist)
		{
			if (dimensionKey.toString().matches(convertToRegex(key)))
			{
				return true;
			}
		}
		return false;
	}

	private static String convertToRegex(String glob) {
		String regex = "^";
		for (char i = 0; i < glob.length(); i++) {
			char c = glob.charAt(i);
			if (c == '*') {
				regex += ".*";
			} else if (c == '?') {
				regex += ".";
			} else if (c == '.') {
				regex += "\\.";
			} else {
				regex += c;
			}
		}
		regex += "$";
		return regex;
	}
	
	public static List<ResourceLocation> getDimensionKeys(ServerLevel serverLevel)
	{
		final List<ResourceLocation> dimensions = new ArrayList<ResourceLocation>();
		for (ServerLevel level : serverLevel.getServer().getAllLevels())
		{
			dimensions.add(level.dimension().location());
		}
		return dimensions;
	}

	@OnlyIn(Dist.CLIENT)
	public static String getDimensionStringsForDisplay()
	{
		final List<? extends String> dimensionlist = LootConfig.SOULORBDROPLIST.get();
		for (String key : dimensionlist)
		{
			return key;
		}
		return "";
	}

	@OnlyIn(Dist.CLIENT)
	private static String getDimensionName(ResourceLocation dimensionKey)
	{
		String name = I18n.get(Util.makeDescriptionId("dimension", dimensionKey));
		if (name.equals(Util.makeDescriptionId("dimension", dimensionKey))) {
			name = dimensionKey.toString();
			if (name.contains(":"))
			{
				name = name.substring(name.indexOf(":") + 1);
			}
			name = WordUtils.capitalize(name.replace('_', ' '));
		}
		return name;
	}
	
	public static String dimensionToString(Level world)
	{
		return world.dimension().location().toString();
	}

	public static ResourceKey<Level> stringToDimension(String key)
	{
		return ResourceKey.create(Registries.DIMENSION, ResourceLocation.tryParse(key));
	}
}
