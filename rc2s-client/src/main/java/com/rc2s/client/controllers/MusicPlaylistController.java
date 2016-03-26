package com.rc2s.client.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class MusicPlaylistController
{
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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

    @FXML
    void anchor1TableViewSort(ActionEvent event)
	{

    }

    @FXML
    void handlePlayPauseButton(ActionEvent event)
	{
		System.out.println("ClickOnPlayPause o/");
    }

    @FXML
    void initialize(URL url, ResourceBundle rb)
	{
        assert musicAnchorPane != null : "fx:id=\"musicAnchorPane\" was not injected: check your FXML file 'musicplaylistview.fxml'.";
        assert anchorGridPaneMain != null : "fx:id=\"anchorGridPaneMain\" was not injected: check your FXML file 'musicplaylistview.fxml'.";
        //assert mainGridTableView != null : "fx:id=\"mainGridTableView\" was not injected: check your FXML file 'musicplaylistview.fxml'.";
        //assert musicTableCol != null : "fx:id=\"musicTableCol\" was not injected: check your FXML file 'musicplaylistview.fxml'.";
        //assert timeTableCol != null : "fx:id=\"timeTableCol\" was not injected: check your FXML file 'musicplaylistview.fxml'.";
        //assert authorTableCol != null : "fx:id=\"authorTableCol\" was not injected: check your FXML file 'musicplaylistview.fxml'.";
        assert mainGridGridPane != null : "fx:id=\"mainGridGridPane\" was not injected: check your FXML file 'musicplaylistview.fxml'.";
        //assert midGridComboBox != null : "fx:id=\"midGridComboBox\" was not injected: check your FXML file 'musicplaylistview.fxml'.";
        assert midGridButtonGrid != null : "fx:id=\"midGridButtonGrid\" was not injected: check your FXML file 'musicplaylistview.fxml'.";
        assert buttonGridBackButton != null : "fx:id=\"buttonGridBackButton\" was not injected: check your FXML file 'musicplaylistview.fxml'.";
        assert buttonGridPlayButton != null : "fx:id=\"buttonGridPlayButton\" was not injected: check your FXML file 'musicplaylistview.fxml'.";
        assert buttonGridNextButton != null : "fx:id=\"buttonGridNextButton\" was not injected: check your FXML file 'musicplaylistview.fxml'.";
        assert midGridSoundGrid != null : "fx:id=\"midGridSoundGrid\" was not injected: check your FXML file 'musicplaylistview.fxml'.";
        assert soundGridSlider != null : "fx:id=\"soundGridSlider\" was not injected: check your FXML file 'musicplaylistview.fxml'.";
        assert soundGridImageView != null : "fx:id=\"soundGridImageView\" was not injected: check your FXML file 'musicplaylistview.fxml'.";
        assert MainGridBorderPane != null : "fx:id=\"MainGridBorderPane\" was not injected: check your FXML file 'musicplaylistview.fxml'.";
        assert borderPaneLabel != null : "fx:id=\"borderPaneLabel\" was not injected: check your FXML file 'musicplaylistview.fxml'.";

    }
}
