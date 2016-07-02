package com.rc2s.application.services.authentication;

import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@Interceptor
public class SecurityInterceptor
{   
    @AroundInvoke
    public Object checkAuthentication(InvocationContext ctx) throws Exception
    {       
        System.out.println("Only In interceptor");
        
        for(String key : ctx.getContextData().keySet()) {
            System.out.println("CtxData " + ctx.getContextData().get(key));
        }
        
        try {
            InitialContext ic = new InitialContext();
            SessionContext sessionContext = (SessionContext) ic.lookup("java:comp/EJBContext");
            
            for(String key : sessionContext.getContextData().keySet()) {
                System.out.println("SessionData " + sessionContext.getContextData().get(key));
            }
        } catch (NamingException ex) {
            throw new IllegalStateException(ex);
        }
        
        System.out.println("Only In interceptor");
        return ctx.proceed();
    }
}
