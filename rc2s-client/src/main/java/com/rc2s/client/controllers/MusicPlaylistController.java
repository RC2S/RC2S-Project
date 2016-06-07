package com.rc2s.client.controllers;

import com.rc2s.client.Main;
import com.rc2s.client.utils.Dialog;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.utils.EJB;
import com.rc2s.common.vo.Track;
import com.rc2s.ejb.synchronization.SynchronizationFacadeRemote;
import com.rc2s.ejb.track.TrackFacadeRemote;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class MusicPlaylistController extends TabController implements Initializable
{
    private final TrackFacadeRemote trackEJB = (TrackFacadeRemote) EJB.lookup("TrackEJB");
    private final SynchronizationFacadeRemote syncEJB = (SynchronizationFacadeRemote) EJB.lookup("SynchronizationEJB");

    @FXML private TableView<Track> tracksTable;
    @FXML private TableColumn<Track, String> musicColumn;
    @FXML private TableColumn<Track, String> timeColumn;
    @FXML private TableColumn<Track, String> authorColumn;

    @FXML private ComboBox syncBox;
    @FXML private Button previousButton;
    @FXML private Button playPauseButton;
    @FXML private Button nextButton;
    @FXML private Slider soundSlider;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        musicColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPath()));
    }
	
	@Override
	public void updateContent()
	{
        updateTracks();
		updateSync();
	}

    private void updateTracks()
    {
        try
        {
            tracksTable.getItems().clear();
            tracksTable.getItems().addAll(trackEJB.getTracksByUser(Main.getAuthenticatedUser()));
        }
        catch(EJBException e)
        {
            Dialog.message("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void updateSync()
    {
        try
        {
            syncBox.getItems().clear();
            syncBox.getItems().addAll(syncEJB.getAll());
        }
        catch(EJBException e)
        {
            Dialog.message("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    public void onPlayPauseEvent(ActionEvent e)
    {
        System.out.println("onPlayPauseEvent");
    }
}
