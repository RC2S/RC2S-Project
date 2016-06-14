package com.rc2s.application.services.authentication;

import java.security.Principal;
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
        for(String key : ctx.getContextData().keySet()) {
            System.out.println("CtxData " + ctx.getContextData().get(key));
        }
        
        try {
            InitialContext ic = new InitialContext();
            SessionContext sessionContext = (SessionContext) ic.lookup("java:comp/EJBContext");
            
            for(String key : sessionContext.getContextData().keySet()) {
                System.out.println("SessionData " + sessionContext.getContextData().get(key));
            }

            //System.out.println("look up injected sctx: " + sessionContext);

            if(sessionContext != null) {
                Principal currentUser = sessionContext.getCallerPrincipal();
                System.out.println("SessionInitialContext In Interceptor " + (currentUser != null ? currentUser.toString() : "Null"));
            }
        } catch (NamingException ex) {
            throw new IllegalStateException(ex);
        }
        
        System.out.println("Only In interceptor");
        
        return ctx.proceed();
    }
}
