package com.rc2s.client.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class MusicPlaylistController implements Initializable
{
    @FXML
    private AnchorPane musicAnchorPane;

    @FXML
    private GridPane anchorGridPaneMain;
/*
    @FXML
    private TableView<?> mainGridTableView;

    @FXML
    private TableColumn<?, ?> musicTableCol;

    @FXML
    private TableColumn<?, ?> timeTableCol;

    @FXML
    private TableColumn<?, ?> authorTableCol;
*/
    @FXML
    private GridPane mainGridGridPane;
/*
    @FXML
    private ComboBox<?> midGridComboBox;
*/
    @FXML
    private GridPane midGridButtonGrid;

    @FXML
    private Button buttonGridBackButton;

    @FXML
    private Button buttonGridPlayButton;

    @FXML
    private Button buttonGridNextButton;

    @FXML
    private GridPane midGridSoundGrid;

    @FXML
    private Slider soundGridSlider;

    @FXML
    private ImageView soundGridImageView;

    @FXML
    private BorderPane MainGridBorderPane;

    @FXML
    private Label borderPaneLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {}
    
    @FXML
    public void handlePlayPauseButton(ActionEvent event)
    {
        System.out.println("ClickOnPlayPause o/");
    }
    
    @FXML
    void anchor1TableViewSort(ActionEvent event)
    {
    
    }
}
