package io.github.andriybosik.schemawizard.core.analyzer.factory.impl;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import io.github.andriybosik.schemawizard.core.analyzer.factory.ReflectionsFactory;

public class ReflectionsFactoryImpl implements ReflectionsFactory {
    private final ClassLoader classLoader;

    public ReflectionsFactoryImpl(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public Reflections newInstance(String packageName) {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder()
                .addClassLoaders(classLoader)
                .addUrls(ClasspathHelper.forPackage(packageName, classLoader))
                .filterInputsBy(input -> input.startsWith(packageName.replace('.', '/')) && input.endsWith(".class"))
                .addScanners(Scanners.SubTypes, Scanners.Resources);
        return new Reflections(configurationBuilder);
    }
}
