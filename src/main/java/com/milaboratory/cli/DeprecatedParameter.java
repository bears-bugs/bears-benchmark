package com.milaboratory.cli;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Mark some parameter as deprecated */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DeprecatedParameter {
    /** informative message */
    String value();

    /** first version with deprecation */
    String version() default "";
}
