package com.rc2s.client;

import com.rc2s.annotations.Knowledge;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.rc2s.client.utils.Resources;
import javafx.scene.Parent;

@Knowledge
public class Main extends Application
{
    private static Stage stage;
    
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
        
        this.stage = stage;
        this.stage.setTitle(Config.APP_NAME);
        
        loader = Resources.loadFxml("LoginView");
        scene = new Scene((Parent)loader.getRoot());
        
        this.stage.setScene(scene);
        this.stage.show();
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
}
