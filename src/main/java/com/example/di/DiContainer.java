package com.example.di;

import com.example.exception.DependencyRegisteredException;
import com.example.utils.ReflectionUtils;
import com.example.di.annotation.Primary;
import com.example.di.annotation.Qualifier;
import com.example.exception.CircularDependencyException;
import com.example.exception.ConflictDependencyException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class DiContainer {
    private final Map<Class<?>, Map<String, Class<?>>> DI = new HashMap<>();
    private final Map<Class<?>, Map<String, Object>> CACHE = new HashMap<>();

    public <T, R extends T> void register(Class<T> baseType, Class<R> instanceType) {
        DI.putIfAbsent(baseType, new HashMap<>());
        String name = getInstanceTypeName(instanceType);
        if (DI.get(baseType).get(name) != null) {
            throw new DependencyRegisteredException(name);
        }
        DI.get(baseType).put(name, instanceType);
    }

    public <T> T resolve(Class<T> baseType) {
        return resolve(baseType, null);
    }

    public <T> T resolve(Class<T> baseType, String name) {
        return resolve(baseType, name, new Stack<>()).getValue();
    }

    private <T> Map.Entry<String, T> resolve(Class<T> baseType, String name, Stack<Class<?>> dependencyStack) {
        T obj = (T) CACHE.getOrDefault(baseType, new HashMap<>()).get(name);
        if (obj != null) {
            return new AbstractMap.SimpleEntry<>(name, obj);
        }
        Class<? extends T> instanceType = getInstanceType(baseType, name);
        checkDependencyStack(instanceType, dependencyStack);

        dependencyStack.push(instanceType);
        T instance = instantiateDependency(instanceType, dependencyStack);
        dependencyStack.pop();

        AbstractMap.SimpleEntry<String, T> entry = new AbstractMap.SimpleEntry<>(
                name != null ? name : getInstanceTypeName(instanceType),
                instance);
        CACHE.putIfAbsent(baseType, new HashMap<>());
        CACHE.get(baseType).put(entry.getKey(), entry.getValue());
        return entry;
    }

    private <T> T instantiateDependency(Class<T> instanceType, Stack<Class<?>> dependencyStack) {
        Constructor<?> primaryConstructor = getPrimaryConstructor(instanceType);
        Object[] constructorParameters = Arrays.stream(primaryConstructor.getParameters())
                .map(parameter -> resolveConstructorParameter(parameter, dependencyStack))
                .toArray(Object[]::new);
        return (T) ReflectionUtils.invokeConstructor(primaryConstructor, constructorParameters);
    }

    private Object resolveConstructorParameter(Parameter parameter, Stack<Class<?>> dependencyStack) {
        if (parameter.getType().isAssignableFrom(List.class)) {
            Class<?> listBaseType = (Class<?>) ((ParameterizedType) parameter.getParameterizedType()).getActualTypeArguments()[0];
            return DI.get(listBaseType).keySet().stream()
                    .map(aClass -> resolve(listBaseType, aClass, dependencyStack))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
        } else if (parameter.getType().isAssignableFrom(Map.class)) {
            Class<?> mapBaseType = (Class<?>) ((ParameterizedType) parameter.getParameterizedType()).getActualTypeArguments()[1];
            return DI.get(mapBaseType).keySet().stream()
                    .map(aClass -> resolve(mapBaseType, aClass, dependencyStack))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        String name = Optional.ofNullable(parameter.getAnnotation(Qualifier.class))
                .map(Qualifier::value)
                .orElse(null);
        return resolve(parameter.getType(), name, dependencyStack).getValue();
    }

    private String getInstanceTypeName(Class<?> instanceType) {
        Qualifier annotation = instanceType.getAnnotation(Qualifier.class);
        if (annotation != null) {
            return annotation.value();
        }
        return instanceType.getName();
    }

    private <T, R extends T> Class<R> getInstanceType(Class<T> baseType, String name) {
        Map<String, Class<?>> baseDiMap = DI.getOrDefault(baseType, new HashMap<>());
        if (name == null && baseDiMap.size() != 1) {
            throw new ConflictDependencyException(
                    String.format("Unable to instantiate dependee. Container doesn't contain %s class metadata or contains more than one records", baseType));
        }
        if (name != null) {
            Class<?> instanceType = baseDiMap.get(name);
            if (instanceType == null) {
                throw new ConflictDependencyException(
                        String.format("Unable to instantiate dependee. Container has no information about dependency of %s class and with %s name", baseType, name));
            }
            return (Class<R>) instanceType;
        }
        return (Class<R>) baseDiMap.values().stream().findFirst().orElse(null);
    }

    private Constructor<?> getPrimaryConstructor(Class<?> instanceType) {
        Constructor<?>[] constructors = instanceType.getConstructors();
        if (constructors.length == 1) {
            return constructors[0];
        }
        List<Constructor<?>> primaryAnnotatedConstructors = Arrays.stream(constructors)
                .filter(constructor -> constructor.getAnnotation(Primary.class) != null)
                .collect(Collectors.toList());
        if (primaryAnnotatedConstructors.size() != 1) {
            throw new ConflictDependencyException(
                    String.format(
                            "Unable to instantiate dependee of class %s. No unique primary constructor was found. Consider using %s annotation only once!",
                            instanceType,
                            Primary.class));
        }
        return primaryAnnotatedConstructors.get(0);
    }

    private void checkDependencyStack(Class<?> instanceType, Stack<Class<?>> dependencyStack) {
        int existingIndex = dependencyStack.indexOf(instanceType);
        if (existingIndex >= 0) {
            throw CircularDependencyException.fromDependencyStack(dependencyStack, existingIndex);
        }
    }
}
