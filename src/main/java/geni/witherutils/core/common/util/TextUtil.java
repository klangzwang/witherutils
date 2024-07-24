package geni.witherutils.core.common.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

public class TextUtil {

    private TextUtil() {
    }

    public static MutableComponent color(MutableComponent component, int color) {
        return component.setStyle(component.getStyle()
              .withColor(TextColor.fromRgb(color)));
    }

    @SuppressWarnings("removal")
	public static MutableComponent build(Object... components) {

        MutableComponent result = null;
        Style cachedStyle = Style.EMPTY;
        for (Object component : components) {
            if (component == null) {
                continue;
            }
            MutableComponent current = null;
            if (component instanceof Component c) {
                current = c.copy();
            } else if (component instanceof ChatFormatting formatting) {
                cachedStyle = cachedStyle.applyFormat(formatting);
            } else if (component instanceof ClickEvent event) {
                cachedStyle = cachedStyle.withClickEvent(event);
            } else if (component instanceof HoverEvent event) {
                cachedStyle = cachedStyle.withHoverEvent(event);
            } else if (component instanceof Block block) {
                current = translate(block.getDescriptionId());
            } else if (component instanceof Item item) {
                current = translate(item.getDescriptionId());
            } else if (component instanceof ItemStack stack) {
                current = stack.getHoverName().copy();
            } else if (component instanceof FluidStack stack) {
                current = stack.getDisplayName().copy();
            } else if (component instanceof Fluid fluid) {
                current = translate(fluid.getFluidType().getDescriptionId());
            } else {
                current = getString(component.toString());
            }
            if (current == null) {
                continue;
            }
            if (!cachedStyle.isEmpty()) {
                current.setStyle(cachedStyle);
                cachedStyle = Style.EMPTY;
            }
            if (result == null) {
                result = current;
            } else {
                result.append(current);
            }
        }
        return result;
    }
    public static MutableComponent getString(String component) {
        return Component.literal(cleanString(component));
    }
    private static String cleanString(String component) {
        return component.replace("\u00A0", " ")
              .replace("\u202f", " ");
    }
    public static MutableComponent translate(String key, Object... args) {
        return Component.translatable(key, args);
    }
    @SuppressWarnings("removal")
	public static MutableComponent smartTranslate(String key, Object... components) {
        if (components.length == 0) {
            return translate(key);
        }
        List<Object> args = new ArrayList<>();
        Style cachedStyle = Style.EMPTY;
        for (Object component : components) {
            if (component == null) {
                args.add(null);
                cachedStyle = Style.EMPTY;
                continue;
            }
            MutableComponent current = null;
            if (component instanceof Block block) {
                current = translate(block.getDescriptionId());
            } else if (component instanceof Item item) {
                current = translate(item.getDescriptionId());
            } else if (component instanceof ItemStack stack) {
                current = stack.getHoverName().copy();
            } else if (component instanceof FluidStack stack) {
                current = stack.getDisplayName().copy();
            } else if (component instanceof Fluid fluid) {
                current = translate(fluid.getFluidType().getDescriptionId());
            } else if (component instanceof ChatFormatting formatting && !hasStyleType(cachedStyle, formatting)) {
                cachedStyle = cachedStyle.applyFormat(formatting);
                continue;
            } else if (component instanceof ClickEvent event && cachedStyle.getClickEvent() == null) {
                cachedStyle = cachedStyle.withClickEvent(event);
                continue;
            } else if (component instanceof HoverEvent event && cachedStyle.getHoverEvent() == null) {
                cachedStyle = cachedStyle.withHoverEvent(event);
                continue;
            } else if (!cachedStyle.isEmpty()) {
                if (component instanceof Component) {
                    current = ((Component) component).copy();
                } else {
                    current = getString(component.toString());
                }
            } else if (component instanceof String) {
                component = cleanString((String) component);
            }
            if (!cachedStyle.isEmpty()) {

                if (current == null) {
                    args.add(component);
                } else {

                    args.add(current.setStyle(cachedStyle));
                }
                cachedStyle = Style.EMPTY;
            } else if (current == null) {

                args.add(component);
            } else {

                args.add(current);
            }
        }
        if (!cachedStyle.isEmpty())
        {
        }
        return translate(key, args.toArray());
    }

    private static boolean hasStyleType(Style current, ChatFormatting formatting) {
        return switch (formatting) {
            case OBFUSCATED -> current.isObfuscated();
            case BOLD -> current.isBold();
            case STRIKETHROUGH -> current.isStrikethrough();
            case UNDERLINE -> current.isUnderlined();
            case ITALIC -> current.isItalic();
            case RESET -> current.isEmpty();
            default -> current.getColor() != null;
        };
    }
}