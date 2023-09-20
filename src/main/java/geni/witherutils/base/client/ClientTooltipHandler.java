package geni.witherutils.base.client;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.base.IWitherPoweredItem;
import geni.witherutils.base.common.block.generator.solar.ISolarPowered;
import geni.witherutils.base.common.block.generator.solar.SolarPanelBlockItem;
import geni.witherutils.base.common.block.generator.solar.SolarType;
import geni.witherutils.base.common.block.totem.handler.AttractionHandlers;
import geni.witherutils.base.common.config.common.SolarConfig;
import geni.witherutils.core.common.util.EnergyUtil;
import geni.witherutils.core.common.util.SoulBankUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientTooltipHandler {

    ClientTooltipHandler() {}
    
    @SubscribeEvent
    public static void addAdvancedTooltips(ItemTooltipEvent evt)
    {
        ItemStack stack = evt.getItemStack();
        addSoulBankTooltips(stack, evt.getToolTip());
        addTotemTooltips(stack, evt.getToolTip());
        addEnergyPowerTooltips(stack, evt.getToolTip());
        addEnergyCreateTooltips(stack, evt.getToolTip());
        addDurabilityTooltips(stack, evt.getToolTip());
    }

    private static void addSoulBankTooltips(ItemStack itemStack, List<Component> components)
    {
        SoulBankUtil.getSoulBankData(itemStack).ifPresent(data -> {
            components.add(Component.translatable(ChatFormatting.GREEN + "BankType: ").append(data.toString()));
        });
    }
    private static void addTotemTooltips(ItemStack stack, List<Component> components)
    {
		List<Item> item = AttractionHandlers.instance.getItemRegistry();
		for (int i = 0; i < item.size(); i++)
		{
			if(stack.getItem() == item.get(i))
				components.add(Component.translatable(ChatFormatting.GRAY + "- TotemBlock Item"));
			if(stack.getItem() == Items.CHAIN)
				components.add(Component.translatable(ChatFormatting.GRAY + "Pull Mobs together."));
			else if(stack.getItem() == Items.HOPPER)
				components.add(Component.translatable(ChatFormatting.GRAY + "Switch to Cursed Monster."));
			else if(stack.getItem() == Items.TORCH)
				components.add(Component.translatable(ChatFormatting.GRAY + "Burns the Monster."));
		}
    }
    private static void addEnergyPowerTooltips(ItemStack stack, List<Component> components)
    {
    	if(EnergyUtil.hasEnergyHandler(stack))
    	{
        	IEnergyStorage storage = stack.getCapability(ForgeCapabilities.ENERGY, null).orElse(null);
            if (storage != null)
            {
            	if(stack.getItem() instanceof IWitherPoweredItem iEnergyItem)
            	{
            		if(iEnergyItem.getPowerLevel(stack) == 0)
            		{
                    	components.add(Component.literal(" "));
            			components.add(Component.translatable("tooltip.witherutils.powerstatus").withStyle(ChatFormatting.GRAY).append(Component.translatable(": ")
            					.append(Component.translatable("tooltip.witherutils.isnotpowered").withStyle(ChatFormatting.DARK_RED))));
            		}
            		else
            		{
                    	components.add(Component.literal(" "));
            			components.add(Component.translatable("tooltip.witherutils.powerstatus").withStyle(ChatFormatting.GRAY).append(Component.translatable(": ")
            					.append(Component.translatable("tooltip.witherutils.ispowered").withStyle(ChatFormatting.GREEN))));
            			
                        String energy = String.format("%,d", EnergyUtil.getEnergyStored(stack)) + "/" +  String.format("%,d", EnergyUtil.getMaxEnergyStored(stack));
                        components.add(ClientTooltipHandler.styledWithArgs("tooltip.witherutils.energy_amount", energy));
            		}
            	}
            }
    	}
    }
    @SuppressWarnings("incomplete-switch")
	private static void addEnergyCreateTooltips(ItemStack stack, List<Component> components)
    {
    	if(stack.getItem() instanceof ISolarPowered iSolarItem)
    	{
        	if(iSolarItem instanceof SolarPanelBlockItem)
        	{
        		SolarPanelBlockItem solarBItem = (SolarPanelBlockItem) iSolarItem;
        		SolarType type = solarBItem.getType();
        		
        		int prod = 0;
        		switch(type)
        		{
					case BASIC:
						prod = SolarConfig.SOLARBASICINPUTRF.get();
						break;
					case ADVANCED:
						prod = SolarConfig.SOLARADVINPUTRF.get();
						break;
					case ULTRA:
						prod = SolarConfig.SOLARULTRAINPUTRF.get();
						break;
        		}

            	components.add(Component.literal(""));
                components.add(Component.literal(ChatFormatting.GRAY + "Can produce " + ChatFormatting.WHITE + "" + prod + ChatFormatting.GRAY + " RF/t from" + ChatFormatting.YELLOW + " Sunlight"));
        	}
    	}
    }
    private static void addDurabilityTooltips(ItemStack stack, List<Component> components)
    {
        int durability = stack.getMaxDamage();

        if(durability != 0)
        {
        	components.add(Component.literal(" "));
        	components.add(Component.translatable("tooltip.witherutils.durability").withStyle(ChatFormatting.GRAY)
                    .append(Component.translatable(": ")
                    .append((stack.getMaxDamage() - stack.getDamageValue()) + "/" + stack.getMaxDamage()).withStyle(ChatFormatting.DARK_GRAY)));
        }
    }

    private final static Minecraft mc = Minecraft.getInstance();

    @OnlyIn(Dist.CLIENT)
    public static boolean isShiftDown()
    {
        return (InputConstants.isKeyDown(mc.getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT) || InputConstants.isKeyDown(mc.getWindow().getWindow(), GLFW.GLFW_KEY_RIGHT_SHIFT));
    }
    @OnlyIn(Dist.CLIENT)
    public static boolean isCtrlDown()
    {
        return (InputConstants.isKeyDown(mc.getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_CONTROL) || InputConstants.isKeyDown(mc.getWindow().getWindow(), GLFW.GLFW_KEY_RIGHT_CONTROL));
    }
    @OnlyIn(Dist.CLIENT)
    public static boolean hasTranslation(String key)
    {
        return net.minecraft.client.resources.language.I18n.exists(key);
    }

    public static final class Tooltip
    {
        @OnlyIn(Dist.CLIENT)
        public static boolean extendedTipCondition()
        {
            return isShiftDown();
        }
        @OnlyIn(Dist.CLIENT)
        public static boolean addInformation(ItemStack stack, @Nullable String advancedTooltipTranslationKey, @Nullable String helpTranslationKey, List<Component> tooltip, TooltipFlag flag, boolean addAdvancedTooltipHints)
        {
            final boolean tip_available = (advancedTooltipTranslationKey != null) && ClientTooltipHandler.hasTranslation(helpTranslationKey + ".desc");

            if(!tip_available)
                return false;

            String tip_text = "";
            String mcid_text = "";
            
            if (extendedTipCondition())
            {
                if (tip_available)
                    tip_text = localize(advancedTooltipTranslationKey + ".desc");
                mcid_text = "witherutils:" + stack.getItem().toString();
            }
            else if (addAdvancedTooltipHints)
            {
                if (tip_available)
                    tip_text += localize("tooltip." + WitherUtils.MODID + ".hint.extended");
            }
            
            if (tip_text.isEmpty())
                return false;
            
            String[] tip_list = tip_text.split("\\r?\\n");
            for (String tip : tip_list)
            {
                tooltip.add(Component.literal(tip.replaceAll("\\s+$", "").replaceAll("^\\s+", "")).withStyle(ChatFormatting.GRAY));
            }
            
            if (mcid_text.isEmpty())
                return false;
            
            tooltip.add(Component.literal(""));
            tooltip.add(Component.literal("ID: ").withStyle(ChatFormatting.BLUE).append(Component.literal(mcid_text).withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.ITALIC)));

            return true;
        }

        @OnlyIn(Dist.CLIENT)
        public static boolean addInformation(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag flag, boolean addAdvancedTooltipHints)
        {
            return addInformation(stack, stack.getDescriptionId(), stack.getDescriptionId(), tooltip, flag, addAdvancedTooltipHints);
        }

        @OnlyIn(Dist.CLIENT)
        public static boolean addInformation(String translation_key, List<Component> tooltip)
        {
            if (!ClientTooltipHandler.hasTranslation(translation_key))
                return false;
            
            tooltip.add(Component.literal(localize(translation_key).replaceAll("\\s+$", "").replaceAll("^\\s+", "")).withStyle(ChatFormatting.GRAY));
            return true;
        }
    }

    public static void playerChatMessage(final Player player, final String message)
    {
        String s = message.trim();
        if (!s.isEmpty())
            player.sendSystemMessage(Component.translatable(s));
    }

    public static @Nullable Component unserializeTextComponent(String serialized) {
        return Component.Serializer.fromJson(serialized);
    }

    public static String serializeTextComponent(Component tc) {
        return (tc == null) ? ("") : (Component.Serializer.toJson(tc));
    }

    public static Component localizable(String modtrkey, Object... args) {
        return Component.translatable((modtrkey.startsWith("block.") || (modtrkey.startsWith("item."))) ? (modtrkey)
                : (WitherUtils.MODID + "." + modtrkey), args);
    }

    public static Component localizable(String modtrkey) {
        return localizable(modtrkey, new Object[] {});
    }

    public static Component localizable_block_key(String blocksubkey) {
        return Component.translatable("block." + WitherUtils.MODID + "." + blocksubkey);
    }

    @OnlyIn(Dist.CLIENT)
    public static String localize(String translationKey, Object... args) {
        Component tr = Component.translatable(translationKey, args);
//      tr.contains(Formatting.RESET);
        final String ft = tr.getString();
        if (ft.contains("${")) {
            Pattern pt = Pattern.compile("\\$\\{([^}]+)\\}");
            Matcher mt = pt.matcher(ft);
            StringBuffer sb = new StringBuffer();
            while (mt.find()) {
                String m = mt.group(1);
                if (m.contains("?")) {
                    String[] kv = m.split("\\?", 2);
                    String key = kv[0].trim();
                    boolean not = key.startsWith("!");
                    if (not)
                        key = key.replaceFirst("!", "");
                    m = kv[1].trim();
                }
                mt.appendReplacement(sb, Matcher.quoteReplacement((Component.translatable(m)).getString().trim()));
            }
            mt.appendTail(sb);
            return sb.toString();
        } else {
            return ft;
        }
    }

    public static boolean anyMatch(int[] arr, int value)
    {
        return Arrays.stream(arr).anyMatch(i -> i == value);
    }
    public static boolean anyMatch(long[] arr, long value)
    {
        return Arrays.stream(arr).anyMatch(i -> i == value);
    }
    public static int safeInt(long value)
    {
        return value > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) value;
    }
    public static int safeInt(String s)
    {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }
    public static long safeLong(String s)
    {
        try {
            return Long.parseLong(s);
        } catch (Exception e) {
            return 0L;
        }
    }

    static final NavigableMap<Long, String> SUFFIXES = new TreeMap<>();

    static {
        SUFFIXES.put(1_000L, "k");
        SUFFIXES.put(1_000_000L, "M");
        SUFFIXES.put(1_000_000_000L, "B");
        SUFFIXES.put(1_000_000_000_000L, "T");
        SUFFIXES.put(1_000_000_000_000_000L, "P");
        SUFFIXES.put(1_000_000_000_000_000_000L, "E");
    }

    public static String numFormat(long value) {
        if (value == Long.MIN_VALUE)
            return numFormat(Long.MIN_VALUE + 1);
        if (value < 0)
            return "-" + numFormat(-value);
        if (value < 1000)
            return Long.toString(value);

        Map.Entry<Long, String> e = SUFFIXES.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10);
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    public static String addCommas(long value) {
        return NumberFormat.getInstance().format(value);
    }
    public static Component style(MutableComponent component)
    {
        return component.withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY);
    }
    public static MutableComponent withArgs(MutableComponent component, Object... args)
    {
        if (component.getContents() instanceof TranslatableContents translatableContents)
        {
            return Component.translatable(translatableContents.getKey(), args);
        }
        return component;
    }
    public static Component styledWithArgs(MutableComponent component, Object... args)
    {
        return style(withArgs(component, args));
    }
    public static Component styledWithArgs(ResourceLocation key, Object... args)
    {
        return style(Component.translatable(key.toLanguageKey(), args));
    }
    public static Component styledWithArgs(String key, Object... args)
    {
        return style(Component.translatable(key, args));
    }
}
