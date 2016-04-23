package com.rc2s.annotations.extensions;

import com.rc2s.annotations.Lookup;
import java.util.Properties;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class LookupLoaderExtension implements Extension
{
    public <T> void initializeLookupLoading(final @Observes ProcessInjectionTarget<T> process)
    { 
        AnnotatedType<T> at = process.getAnnotatedType();
        
        if(!at.isAnnotationPresent(Lookup.class))
            return;
        
        Lookup lookupAnnotation = at.getAnnotation(Lookup.class);
        
        //at.getFields()
	}
    
    
    private <T> T getInitialContextLookup(Lookup annotation) throws NamingException
    {
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
        props.put(Context.URL_PKG_PREFIXES, "com.sun.enterprise.naming");
        props.put(Context.STATE_FACTORIES, "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
        //props.put(Context.PROVIDER_URL, annotation.hostIP() + ":" + annotation.hostPort());
        props.put("org.omg.CORBA.ORBInitialHost", annotation.hostIP());
        props.put("org.omg.CORBA.ORBInitialPort", annotation.hostPort());

        InitialContext ctx = new InitialContext(props);
        
        System.out.println("Client Context" + ctx);
        
        //UserFacadeRemote userEJB = (UserFacadeRemote) ctx.lookup(annotation.value());
        
        /*Reflections reflections = new Reflections("com.rc2s.ejb");
        for(Class<?> type : reflections.getSubTypesOf(Object.class))
            System.out.println(type.toString());
        
        List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
            .setScanners(new SubTypesScanner(false), new ResourcesScanner())
        .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
        .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("org.your.package"))));
        */
        return null;
    }
}
