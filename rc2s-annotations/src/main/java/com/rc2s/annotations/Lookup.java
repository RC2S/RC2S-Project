package com.rc2s.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface Lookup
{
    String remoteEJB();
    String hostIP() default "127.0.0.1";
    String hostPort() default "3700";
    String login() default "";
    String credentials() default "";
}
