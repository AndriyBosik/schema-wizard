package org.schemawizard.core.utils;

public class StringUtils {
    private StringUtils() {
    }

    public static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public static boolean isNotBlank(String value) {
        return !isBlank(value);
    }

    public static String strip(String value) {
        return value == null ? null : value.strip();
    }
}
