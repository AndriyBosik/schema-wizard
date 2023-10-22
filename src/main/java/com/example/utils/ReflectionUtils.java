package com.example.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

@SuppressWarnings("unchecked")
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

    public static <T> T invokeMethod(Method method, Object obj, Object... args) {
        try {
            return (T) method.invoke(obj, args);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public static Method setAccessible(Method method, boolean accessible) {
        method.setAccessible(accessible);
        return method;
    }
}
