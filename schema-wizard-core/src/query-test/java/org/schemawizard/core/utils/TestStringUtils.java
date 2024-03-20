package org.schemawizard.core.utils;

public class TestStringUtils {
    private TestStringUtils() {}

    public static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
