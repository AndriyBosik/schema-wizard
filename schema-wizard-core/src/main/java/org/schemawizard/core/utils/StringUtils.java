package org.schemawizard.core.utils;

public class StringUtils {
    private StringUtils() {
    }

    public static boolean isBlank(String value) {
        return value == null || value.strip().isBlank();
    }

    public static String strip(String value) {
        return value == null ? null : value.strip();
    }
}
