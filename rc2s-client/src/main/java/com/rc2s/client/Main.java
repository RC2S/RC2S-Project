package com.rc2s.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.rc2s.client.utils.Resources;
import com.rc2s.common.utils.EJB;
import com.rc2s.common.vo.User;
import com.sun.enterprise.security.ee.auth.login.ProgrammaticLogin;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class Main extends Application
{
    private static Stage stage;
    
	private static User user;
    
    private static ProgrammaticLogin pm;
    private static ArrayList<Thread> threadPool;
    
    private static final Logger log = LogManager.getLogger(Main.class);
    
    public static void main(String[] args)
    {
        log.info("Starting RC2S Client");
        
        System.setProperty("java.security.auth.login.config", Main.class.getResource("/jaas.config").toString());
        
        Main.pm = new ProgrammaticLogin();
        
        Resources.setViewsPackage(Config.VIEWS_PACKAGE);
        Resources.setCssPackage(Config.CSS_PACKAGE);
        Resources.setResourcesPackage(Config.RESOURCES_PACKAGE);
        
        launch(args);
    }
    
    @Override
    public void start(final Stage stage) throws Exception
    {
        Scene scene;
        FXMLLoader loader;

		Main.threadPool = new ArrayList<>();
        
        Main.stage = stage;
        Main.stage.setTitle(Config.APP_NAME);
		Main.stage.getIcons().add(new Image(Resources.loadResource("views/images/rc2s_icon.png")));
        
        loader = Resources.loadFxml("LoginView");
        scene = new Scene((Parent)loader.getRoot());
        
        Main.stage.setScene(scene);
        Main.stage.show();
        
        Main.stage.setOnCloseRequest((event) -> {
            log.info("Logout and close lookup context");
            
			// First interrupt all the children threads
			interruptChildrenProcesses();

			// Then logout and close the EJB context
            if(Main.user != null && Main.pm != null) 
                Main.pm.logout();

            EJB.closeContext();
            
            log.info("Closing RC2S Client");
        });
    }

    @Override
    public void stop() throws Exception
    {
        super.stop();
    }
    
    public static Stage getStage()
    {
        return stage;
    }
	
	public static void setAuthenticatedUser(final User user)
	{
		Main.user = user;
	}
	
	public static User getAuthenticatedUser()
	{
		return Main.user;
	}
    
    public static ProgrammaticLogin getProgrammaticLogin()
    {
        return Main.pm;
    }

	private void interruptChildrenProcesses()
	{
		for (Thread t : threadPool)
		{
			try
			{
				t.interrupt();
				t.join();
			}
			catch (InterruptedException e) {}
		}
	}

	public static void registerThread(Thread t)
	{
		Main.threadPool.add(t);
	}

	public static void releaseThread(Thread t)
	{
		Main.threadPool.remove(t);
	}
}
