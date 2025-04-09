package io.github.andriybosik.schemawizard.core.config.extension;

import io.github.andriybosik.schemawizard.core.metadata.DatabaseProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DisableFor {
    DatabaseProvider[] value();
}
