package com.rc2s.ejb.user;

import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserFacadeBeanTest
{
    private UserFacadeRemote userBean;
    
    public UserFacadeBeanTest() {}
    
    @BeforeClass
    public static void setUpClass() {}
    
    @AfterClass
    public static void tearDownClass() {}
    
    @Before
    public void setUp() throws NamingException
    {
        /*final Properties p = new Properties();  
        p.setProperty("java.naming.factory.initial", "org.apache.openejb.client.LocalInitialContextFactory");  
        p.setProperty("openejb.jndiname.format", "{ejbName}/{interfaceType.annotationName}");  
        p.setProperty("openejb.deployments.classpath.include", "com.rc2s.ejb.*");  
        userBean = ((UserFacadeRemote) new InitialContext(p).lookup("UserFacadeBean/Local"));*/
    }
    
    @After
    public void tearDown() {}

    @Test
    public void testGetAllUsers()
    {
        /*final String result = userBean.getAllUsers();  
        assertNotNull(result);  
        assertEquals("coucou", result); */
    }
}
