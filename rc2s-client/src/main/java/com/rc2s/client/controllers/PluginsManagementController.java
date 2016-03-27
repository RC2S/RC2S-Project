package com.rc2s.client.controllers;

import com.rc2s.client.Main;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PluginsManagementController implements Initializable
{
    private static final Logger log = LogManager.getLogger(PluginsManagementController.class);
    
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {}
    
    @FXML
    public void handleUploadFileButton(ActionEvent event)
    {
        log.info("test logger");
        File file = fileChooser.showOpenDialog(Main.getStage());
        pluginGridFileButton.setText(file.getName());
    }
}
