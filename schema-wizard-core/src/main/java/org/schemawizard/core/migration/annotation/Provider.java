package org.schemawizard.core.migration.annotation;

import org.schemawizard.core.metadata.DatabaseProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Provider {
    DatabaseProvider value();
}
