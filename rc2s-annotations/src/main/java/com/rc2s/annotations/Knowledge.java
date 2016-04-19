package com.rc2s.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface Knowledge
{
    String name() default "coucouknow";
    String description() default "njkjhk";
    String[] parameters() default {"test", "ted2"};
}
