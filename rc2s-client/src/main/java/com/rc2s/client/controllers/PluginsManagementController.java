package com.rc2s.client.controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
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
    private Stage stage;
    
    private final FileChooser fileChooser = new FileChooser();

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
        pluginGridFileButton.setText(file.getName());
    }

    @FXML
    void initialize(URL location, ResourceBundle resources) {}
	
    public void initView(Stage stage)
    {
        this.stage = stage;
    }
}
