package io.github.andriybosik.schemawizard.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PostResetDb {
    String[] clean() default {};

    String[] drop() default {};

    String[] execute() default {};
}
