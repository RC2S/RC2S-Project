package com.rc2s.client.controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PluginsManagementController
{
	private Desktop desktop = Desktop.getDesktop();
	private Stage stage = null;
	private final FileChooser fileChooser = new FileChooser();
			
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane pluginsAnchorPane;

    @FXML
    private GridPane anchorGridPane;

    @FXML
    private TableView<?> gridTableView;

    @FXML
    private BorderPane gridBorderPane;

    @FXML
    private Label addPluginPaneLabel;

    @FXML
    private GridPane addPluginPaneGridPane;

    @FXML
    private Button pluginGridFileButton;

    @FXML
    void handleUploadFileButton(ActionEvent event)
	{
		File file = fileChooser.showOpenDialog(this.stage);
		
		if (file != null)
		{
			openFile(file);
		}
    }

    @FXML
    void initialize() {
        assert pluginsAnchorPane != null : "fx:id=\"pluginsAnchorPane\" was not injected: check your FXML file 'PluginsManagementView.fxml'.";
        assert anchorGridPane != null : "fx:id=\"anchorGridPane\" was not injected: check your FXML file 'PluginsManagementView.fxml'.";
        assert gridTableView != null : "fx:id=\"gridTableView\" was not injected: check your FXML file 'PluginsManagementView.fxml'.";
        assert gridBorderPane != null : "fx:id=\"gridBorderPane\" was not injected: check your FXML file 'PluginsManagementView.fxml'.";
        assert addPluginPaneLabel != null : "fx:id=\"addPluginPaneLabel\" was not injected: check your FXML file 'PluginsManagementView.fxml'.";
        assert addPluginPaneGridPane != null : "fx:id=\"addPluginPaneGridPane\" was not injected: check your FXML file 'PluginsManagementView.fxml'.";
        assert pluginGridFileButton != null : "fx:id=\"pluginGridFileButton\" was not injected: check your FXML file 'PluginsManagementView.fxml'.";
    }
	
	public void setStage(Stage stage)
	{
		this.stage = stage;
	}
	
	private void openFile(File file)
	{
        try
		{
            desktop.open(file);
        }
		catch (IOException ex)
		{
            ex.printStackTrace();
        }
    }
}
