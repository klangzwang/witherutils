package geni.witherutils.core.common.util;

import java.text.NumberFormat;
import java.util.Locale;

public class StringUtil {

    private StringUtil() {
    }

    public static String niceFormat(final double v)
    {
        String format;
        if (v == (int)v) {
            format = String.format(Locale.ENGLISH, "%d", (int)v);
        }
        else {
            format = String.format(Locale.ENGLISH, "%.2f", v);
        }
        return format;
    }
    public static String erasePrefix(final String string, final String prefix)
    {
        if (string.startsWith(prefix))
        {
            return string.substring(prefix.length());
        }
        return string;
    }
    public static String formatPercent(final double v)
    {
        return NumberFormat.getPercentInstance(Locale.UK).format(v);
    }
    public static String format(final int number)
    {
        return NumberFormat.getInstance(Locale.UK).format(number);
    }
    public static String format(final double number)
    {
        return NumberFormat.getInstance(Locale.UK).format(number);
    }
    public static String format(final float number)
    {
        return NumberFormat.getInstance(Locale.UK).format(number);
    }
}