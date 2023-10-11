package com.example.utils;

import java.lang.reflect.Constructor;

public class ReflectionUtils {
    private ReflectionUtils() {
    }

    public static <T> T invokeConstructor(Constructor<T> constructor, Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
