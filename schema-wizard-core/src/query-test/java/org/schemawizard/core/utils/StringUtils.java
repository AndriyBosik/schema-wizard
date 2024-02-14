package org.schemawizard.core.utils;

public class StringUtils {
    private StringUtils() {}

    public static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
