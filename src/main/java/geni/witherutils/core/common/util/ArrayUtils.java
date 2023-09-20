package geni.witherutils.core.common.util;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;

public class ArrayUtils {

    public static String[] arrayToLowercase(String[] array) {
        String[] copy = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            copy[i] = array[i].toLowerCase();
        }
        return copy;
    }

    public static Map<String, String> convertKeyValueArrayToMap(String[] array) {
        HashMap<String, String> map = new HashMap<>();
        for (String entry : array) {
            String[] split = entry.split("=");
            map.put(split[0], split[1]);
        }
        return map;
    }

    public static List<String> prefixStringList(String prefix, List<String> list) {
        List<String> finalList = new ArrayList<>();
        for (String string : list) {
            finalList.add(prefix + string);
        }
        return finalList;
    }

    @SafeVarargs
    public static <T> boolean containsKeys(Map<T, ?> map, T... keys) {
        for (T object : keys) {
            if (!map.containsKey(object)) {
                return false;
            }
        }
        return true;
    }

    public static <T> T[] addToArrayFirstNull(T[] array, T value) {
        int nullIndex = -1;
        for (int i = 0; i < array.length; i++) {
            T v = array[i];
            if (v == null) {
                nullIndex = i;
                break;
            }
        }
        if (nullIndex == -1) {
            T[] copy = createNewArray(array, array.length + 1);
            System.arraycopy(array, 0, copy, 0, array.length);
            nullIndex = array.length;
            array = copy;
        }
        array[nullIndex] = value;
        return array;
    }

    public static <T> List<T> addAllNoNull(T[] array, List<T> list) {
        for (T value : array) {
            if (value != null) {
                list.add(value);
            }
        }
        return list;
    }

    public static <T> boolean isEmpty(T[] array) {
        for (T value : array) {
            if (value != null) {
                return false;
            }
        }
        return true;
    }

    public static <T> int countNoNull(T[] array) {
        return count(array, Objects::nonNull);
    }

    public static <T> int count(T[] array, Function<T, Boolean> check) {
        int counter = 0;
        for (T value : array) {
            if (check.apply(value)) {
                counter++;
            }
        }
        return counter;
    }

    @SuppressWarnings ("unchecked")
    public static <T> T[] fill(T[] array, T value) {
        for (int i = 0; i < array.length; i++) {
            T newValue = value;
            if (value instanceof Copyable) {
                newValue = ((Copyable<T>) value).copy();
            }
            array[i] = newValue;
        }
        return array;
    }

    @SuppressWarnings ("unchecked")
    public static <T> void fillArray(T[] array, T value, Function<T, Boolean> check) {
        for (int i = 0; i < array.length; i++) {
            if (check.apply(array[i])) {
                T newValue = value;
                if (value instanceof Copyable) {
                    newValue = ((Copyable<T>) value).copy();
                }
                array[i] = newValue;
            }
        }
    }

    @SuppressWarnings("unchecked")
	public static <I, O> List<O> applyArray(Function<I, O> function, I... array) {
        List<O> finalList = new ArrayList<>();
        for (I i : array) {
            finalList.add(function.apply(i));
        }

        return finalList;
    }

    @SuppressWarnings("unchecked")
	public static void arrayCopy(Object src, int srcPos, Object dst, int destPos, int length) {
        System.arraycopy(src, srcPos, dst, destPos, length);
        if (dst instanceof Copyable[]) {
            Object[] oa = (Object[]) dst;
            Copyable<Object>[] c = (Copyable[]) dst;
            for (int i = destPos; i < destPos + length; i++) {
                if (c[i] != null) {
                    oa[i] = c[i].copy();
                }
            }
        }
    }

    public static <T> int indexOf(T[] array, T object) {
        if (object == null) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < array.length; i++) {
                if (object.equals(array[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static <T> int indexOfRef(T[] array, T object) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == object) {
                return i;
            }
        }
        return -1;
    }

    public static <T> T[] createNewArray(T[] array) {
        return createNewArray(array, array.length);
    }

    @SuppressWarnings ("unchecked")
    public static <T> T[] createNewArray(T[] array, int length) {
        Class<? extends T[]> newType = (Class<? extends T[]>) array.getClass();
        T[] copy;
        if (newType.equals(Object[].class)) {
            copy = (T[]) new Object[length];
        } else {
            copy = (T[]) newArray(newType.getComponentType(), length);
        }
        return copy;
    }

    @SuppressWarnings ("unchecked")
    public static <T> T[] newArray(Class<T> arrayClass, int length) {
        return (T[]) Array.newInstance(arrayClass, length);
    }

    public static <T> T[] rollArray(T[] input, int shift) {
        T[] newArray = createNewArray(input);

        for (int i = 0; i < input.length; i++) {
            int newPos = (i + shift) % input.length;

            if (newPos < 0) {
                newPos += input.length;
            }
            newArray[newPos] = input[i];
        }
        return newArray;
    }

    public static <T> boolean contains(T[] input, T element) {
        for (T test : input) {
            if (Objects.equals(test, element)) {
                return true;
            }
        }
        return false;
    }

    public static <T> T[] inverse(T[] input, T[] allElements) {
        List<T> list = new LinkedList<>();
        for (T e : allElements) {
            if (!contains(input, e)) {
                list.add(e);
            }
        }

        return list.toArray(createNewArray(input, list.size()));
    }

    public static <T> boolean isNullOrContainsNull(T[] input) {
        if (input != null) {
            for (T t : input) {
                if (t == null) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public static List<Integer> toList(int[] arr) {
        List<Integer> list = new ArrayList<>();
        for (int i : arr) {
            list.add(i);
        }
        return list;
    }
}
