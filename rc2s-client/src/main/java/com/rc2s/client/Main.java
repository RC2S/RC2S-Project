package com.rc2s.client;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.rc2s.client.utils.Resources;

public class Main extends Application
{
    private Stage       stage;
    
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
        Parent      root;
        Scene       scene;
        FXMLLoader  loader;
        
        this.stage  = stage;
        
        loader      = Resources.loadFxml("LoginView");
        scene       = new Scene((Parent) loader.getRoot());

        this.stage.setScene(scene);
        this.stage.setTitle(Config.APP_NAME);
        Resources.loadCss(scene, "test");
        
        this.stage.show();
    }

    @Override
    public void stop() throws Exception
    {
        super.stop();
    }
    
    public Stage getStage()
    {
        return stage;
    }
}
