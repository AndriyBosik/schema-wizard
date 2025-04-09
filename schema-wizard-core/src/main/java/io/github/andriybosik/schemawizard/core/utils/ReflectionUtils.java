package io.github.andriybosik.schemawizard.core.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
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
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T invokeConstructor(Constructor<T> constructor, Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static <T> T invokeMethod(Method method, Object obj, Object... args) {
        try {
            return (T) method.invoke(obj, args);
        } catch (IllegalAccessException | InvocationTargetException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static <T> T getValue(Field field, Object obj) {
        try {
            return (T) field.get(obj);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static Method setAccessible(Method method, boolean accessible) {
        method.setAccessible(accessible);
        return method;
    }

    public static Field setAccessible(Field field, boolean accessible) {
        field.setAccessible(accessible);
        return field;
    }
}
