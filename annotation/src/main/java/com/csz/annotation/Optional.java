package com.csz.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface Optional {
    String stringValue() default "";
    byte byteValue() default 0;
    char charValue() default 0;
    short shortValue() default 0;
    int intValue() default 0;
    long longValue() default 0L;
    float floatValue() default 0f;
    double doubleValue() default 0d;
    boolean booleanValue() default false;
}
