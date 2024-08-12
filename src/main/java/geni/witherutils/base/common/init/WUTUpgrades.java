package geni.witherutils.base.common.init;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

import geni.witherutils.api.upgrade.WUTUpgrade;
import geni.witherutils.base.common.upgrade.BuiltinUpgrade;

public class WUTUpgrades {
	
    private static final Map<BuiltinUpgrade, WUTUpgrade> BUILTIN_MAP = new EnumMap<>(BuiltinUpgrade.class);

    public static final Supplier<WUTUpgrade> VOLUME = () -> BUILTIN_MAP.get(BuiltinUpgrade.VOLUME);
    public static final Supplier<WUTUpgrade> DISPENSER = () -> BUILTIN_MAP.get(BuiltinUpgrade.DISPENSER);
    public static final Supplier<WUTUpgrade> ITEM_LIFE = () -> BUILTIN_MAP.get(BuiltinUpgrade.ITEM_LIFE);
    public static final Supplier<WUTUpgrade> ENTITY_TRACKER = () -> BUILTIN_MAP.get(BuiltinUpgrade.ENTITY_TRACKER);
    public static final Supplier<WUTUpgrade> BLOCK_TRACKER = () -> BUILTIN_MAP.get(BuiltinUpgrade.BLOCK_TRACKER);
    public static final Supplier<WUTUpgrade> SPEED = () -> BUILTIN_MAP.get(BuiltinUpgrade.SPEED);
    public static final Supplier<WUTUpgrade> SEARCH = () -> BUILTIN_MAP.get(BuiltinUpgrade.SEARCH);
    public static final Supplier<WUTUpgrade> COORDINATE_TRACKER = () -> BUILTIN_MAP.get(BuiltinUpgrade.COORDINATE_TRACKER);
    public static final Supplier<WUTUpgrade> RANGE = () -> BUILTIN_MAP.get(BuiltinUpgrade.RANGE);
    public static final Supplier<WUTUpgrade> SECURITY = () -> BUILTIN_MAP.get(BuiltinUpgrade.SECURITY);
    public static final Supplier<WUTUpgrade> MAGNET = () -> BUILTIN_MAP.get(BuiltinUpgrade.MAGNET);
    public static final Supplier<WUTUpgrade> CHARGING = () -> BUILTIN_MAP.get(BuiltinUpgrade.CHARGING);
    public static final Supplier<WUTUpgrade> ARMOR = () -> BUILTIN_MAP.get(BuiltinUpgrade.ARMOR);
    public static final Supplier<WUTUpgrade> JET_BOOTS = () -> BUILTIN_MAP.get(BuiltinUpgrade.JET_BOOTS);
    public static final Supplier<WUTUpgrade> NIGHT_VISION = () -> BUILTIN_MAP.get(BuiltinUpgrade.NIGHT_VISION);
    public static final Supplier<WUTUpgrade> SCUBA = () -> BUILTIN_MAP.get(BuiltinUpgrade.SCUBA);
    public static final Supplier<WUTUpgrade> CREATIVE = () -> BUILTIN_MAP.get(BuiltinUpgrade.CREATIVE);
    public static final Supplier<WUTUpgrade> INVENTORY = () -> BUILTIN_MAP.get(BuiltinUpgrade.INVENTORY);
    public static final Supplier<WUTUpgrade> JUMPING = () -> BUILTIN_MAP.get(BuiltinUpgrade.JUMPING);
    public static final Supplier<WUTUpgrade> FLIPPERS = () -> BUILTIN_MAP.get(BuiltinUpgrade.FLIPPERS);
    public static final Supplier<WUTUpgrade> STANDBY = () -> BUILTIN_MAP.get(BuiltinUpgrade.STANDBY);
    public static final Supplier<WUTUpgrade> MINIGUN = () -> BUILTIN_MAP.get(BuiltinUpgrade.MINIGUN);
    public static final Supplier<WUTUpgrade> GILDED = () -> BUILTIN_MAP.get(BuiltinUpgrade.GILDED);
    public static final Supplier<WUTUpgrade> ENDER_VISOR = () -> BUILTIN_MAP.get(BuiltinUpgrade.ENDER_VISOR);
    public static final Supplier<WUTUpgrade> STOMP = () -> BUILTIN_MAP.get(BuiltinUpgrade.STOMP);
    public static final Supplier<WUTUpgrade> ELYTRA = () -> BUILTIN_MAP.get(BuiltinUpgrade.ELYTRA);
    public static final Supplier<WUTUpgrade> CHUNKLOADER = () -> BUILTIN_MAP.get(BuiltinUpgrade.CHUNKLOADER);

    public static WUTUpgrade registerBuiltin(BuiltinUpgrade bu, WUTUpgrade pncUpgrade)
    {
        BUILTIN_MAP.put(bu, pncUpgrade);
        return pncUpgrade;
    }
}
