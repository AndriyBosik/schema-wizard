package org.schemawizard.core.analyzer.factory;

import org.reflections.Reflections;

public interface ReflectionsFactory {
    Reflections newInstance(String packageName);
}
