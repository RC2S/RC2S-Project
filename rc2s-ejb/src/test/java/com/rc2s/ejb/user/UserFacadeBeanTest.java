package com.rc2s.ejb.user;

import com.rc2s.common.vo.User;
import java.util.ArrayList;
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
        final Properties p = new Properties();  
        p.setProperty("java.naming.factory.initial", "org.apache.openejb.client.LocalInitialContextFactory");  
        userBean = ((UserFacadeRemote) new InitialContext(p).lookup("java:app/main/UserFacadeBean"));
    }
    
    @After
    public void tearDown() {}

    @Test
    public void testGetAllUsers()
    {
        /*final ArrayList<User> result = userBean.getAllUsers();  
        assertNotNull(result);  
        assertEquals(3, result.size());*/
    }
}
