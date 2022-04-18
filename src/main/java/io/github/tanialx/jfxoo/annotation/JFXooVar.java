package io.github.tanialx.jfxoo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface JFXooVar {
    JFXooVarType type() default JFXooVarType._auto;
    String label() default "";
}
