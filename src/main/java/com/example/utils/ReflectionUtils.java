package com.example.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

@SuppressWarnings("unchecked")
public class ReflectionUtils {
    private ReflectionUtils() {
    }

    public static <T> T invokeConstructor(Class<T> type, Object... args) {
        var parameterTypes = Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);
        try {
            return type.getConstructor(parameterTypes).newInstance(args);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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
