package com.rc2s.client;

import com.rc2s.annotations.Knowledge;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.rc2s.client.utils.Resources;
import com.rc2s.common.vo.User;
import javafx.scene.Parent;

@Knowledge
public class Main extends Application
{
    private static Stage stage;
	private static User user;
    
    public static void main(String[] args)
    {
        Resources.setViewsPackage(Config.VIEWS_PACKAGE);
        Resources.setCssPackage(Config.CSS_PACKAGE);
        Resources.setResourcesPackage(Config.RESOURCES_PACKAGE);
        
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception
    {
        Scene scene;
        FXMLLoader loader;
        
        Main.stage = stage;
        Main.stage.setTitle(Config.APP_NAME);
        
        loader = Resources.loadFxml("LoginView");
        scene = new Scene((Parent)loader.getRoot());
        
        Main.stage.setScene(scene);
        Main.stage.show();
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
	
	public static void setAuthenticatedUser(User user)
	{
		Main.user = user;
	}
	
	public static User getAUthenticatedUser()
	{
		return Main.user;
	}
}
