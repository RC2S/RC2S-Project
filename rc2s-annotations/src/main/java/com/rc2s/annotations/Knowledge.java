package com.rc2s.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Knowledge interface
 * 
 * @author RC2S
 */
@Documented
@Target({
    ElementType.METHOD,
    ElementType.TYPE,
    ElementType.FIELD,
    ElementType.CONSTRUCTOR
})
@Retention(RetentionPolicy.SOURCE)
public @interface Knowledge
{
    String description() default "";
    String[] parametersDescription() default {""};
}
