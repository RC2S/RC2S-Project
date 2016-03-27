package com.rc2s.client;

import com.rc2s.client.controllers.PluginsManagementController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.rc2s.client.utils.Resources;
import javafx.scene.Parent;

public class Main extends Application
{
    public static Stage stage;
    
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
        
        loader = Resources.loadFxml("PluginsManagementView");
        scene = new Scene((Parent)loader.getRoot());
        
	/*StackPane layout = new StackPane();
        layout.setPrefSize(1280, 720);
        scene = new Scene(layout);
        
        LedCube cube = new LedCube(scene.getRoot(), 4, 4, 4, 15., Color.RED);
        //cube.draw(scene);
        ((Pane)scene.getRoot()).getChildren().add(cube);*/

        this.stage.setScene(scene);
        Resources.loadCss(scene, "rc2s-client");
        Resources.loadCss(scene, "musicPlaylist-style");
        Resources.loadCss(scene, "managementAccess-style");
        Resources.loadCss(scene, "pluginsManagement-style");
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
