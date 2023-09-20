package geni.witherutils.core.common.helper;

import java.util.Objects;

@SuppressWarnings("rawtypes")
public interface NamedEnum<T extends NamedEnum> {

    String getName();
    String[] getDescription();

    static <T extends NamedEnum<T>> T getEnumByName(String name, T[] values) {
        for (T value : values) {
            if (Objects.equals(name, value.getName())) {
                return value;
            }
        }
        return null;
    }
}