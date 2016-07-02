package com.rc2s.application.services.authentication;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
public class SecurityInterceptor
{   
    @AroundInvoke
    public Object checkAuthentication(InvocationContext ctx) throws Exception
    {       
        System.out.println("Only In interceptor");
        
        return ctx.proceed();
    }
}
