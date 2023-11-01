package com.example.utils;

public class StringUtils {
    private StringUtils() {
    }

    public static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public static String strip(String value) {
        return value == null ? null : value.strip();
    }
}
