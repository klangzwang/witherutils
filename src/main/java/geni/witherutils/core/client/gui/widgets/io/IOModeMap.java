package geni.witherutils.core.client.gui.widgets.io;

import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.Comparator;

import geni.witherutils.api.io.IOMode;

public enum IOModeMap {

    NONE(IOMode.NONE, Component.translatable("NONE"), new Rect2i(0, 0, 0, 0)),
    PUSH(IOMode.PUSH, Component.translatable("PUSH"), new Rect2i(16, 16, 16, 8)),
    PULL(IOMode.PULL, Component.translatable("PULL"), new Rect2i(0, 16, 16, 8)),
    BOTH(IOMode.BOTH, Component.translatable("BOTH"), new Rect2i(0, 16, 32, 8)),
    DISABLED(IOMode.DISABLED, Component.translatable("DISABLED"), new Rect2i(32, 16, 16, 16));

    private static final IOModeMap[] BY_MODE = Arrays.stream(values()).sorted(Comparator.comparingInt(m -> m.mode.ordinal())).toArray(IOModeMap[]::new);

    public static IOModeMap getMapFromMode(IOMode mode)
    {
        return BY_MODE[mode.ordinal()];
    }

    private final IOMode mode;
    private final Component component;
    private final Rect2i rect;

    IOModeMap(IOMode mode, Component name, Rect2i rect)
    {
        this.mode = mode;
        this.component = name;
        this.rect = rect;
    }

    public IOMode getMode()
    {
        return mode;
    }
    public Component getComponent()
    {
        return component;
    }
    public Rect2i getRect()
    {
        return rect;
    }
}
