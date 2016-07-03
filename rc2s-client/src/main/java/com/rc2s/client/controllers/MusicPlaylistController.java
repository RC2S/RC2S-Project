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
import javafx.scene.input.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

public class MusicPlaylistController extends TabController implements Initializable
{
    private final TrackFacadeRemote trackEJB = (TrackFacadeRemote) EJB.lookup("TrackEJB");
    private final SynchronizationFacadeRemote syncEJB = (SynchronizationFacadeRemote) EJB.lookup("SynchronizationEJB");

	private MediaPlayer mediaPlayer;
	private boolean playing = false;
	private int currentTrack = -1;

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

		tracksTable.setRowFactory(table -> {
			TableRow<Track> row = new TableRow<>();

			row.setOnMouseClicked(e -> {
				if(!row.isEmpty() && e.getClickCount() == 2) {
					int index = tracksTable.getSelectionModel().getSelectedIndex();

					setTrack(index);
					play();
				}
			});

			return row;
		});
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
            syncBox.getItems().addAll(syncEJB.getByUser(Main.getAuthenticatedUser()));
        }
        catch(EJBException e)
        {
            Dialog.message("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

	@FXML
	private void onKeyPressedEvent(KeyEvent e)
	{
		if(e.getEventType() == KeyEvent.KEY_PRESSED)
		{
			if(e.getCode() == KeyCode.BACK_SPACE || e.getCode() == KeyCode.DELETE)
			{
				try
				{
					Track track = tracksTable.getSelectionModel().getSelectedItem();

					ButtonType answer = Dialog.confirm("Are you sure you want to remove this track from your playlist?");

					if(answer == ButtonType.OK)
					{
						trackEJB.delete(Main.getAuthenticatedUser(), track);
						updateTracks();
					}
				}
				catch(EJBException ex)
				{
					Dialog.message("Error", ex.getMessage(), Alert.AlertType.ERROR);
				}
			}

			e.consume();
		}
	}

	@FXML
	private void onDragOverEvent(DragEvent event)
	{
		Dragboard db = event.getDragboard();

		if(db.hasFiles())
			event.acceptTransferModes(TransferMode.COPY);
		else
			event.consume();
	}

	@FXML
	private void onDragDroppedEvent(DragEvent event)
	{
		Dragboard db = event.getDragboard();
		boolean success = false;

		if(db.hasFiles())
		{
			success = true;

			for(File file : db.getFiles())
			{
				try
				{
					Track track = uriToTrack(file.toURI());
					trackEJB.add(Main.getAuthenticatedUser(), track);
					updateTracks();
				}
				catch(EJBException | MediaException e)
				{
					Dialog.message("Error", e.getMessage(), Alert.AlertType.ERROR);
				}
			}
		}

		event.setDropCompleted(success);
		event.consume();
	}

	private Track uriToTrack(URI uri) throws MediaException
	{
		Media media = new Media(uri.toString()); // If the file is not a media file, this will raise a MediaException

		Track track = new Track();
		track.setPath(uri.toString());
		track.setOrder(getNextOrder());
		track.setUser(Main.getAuthenticatedUser());

		return track;
	}

	private short getNextOrder()
	{
		short next = 0;

		if(tracksTable.getItems() != null && tracksTable.getItems().size() != 0)
		{
			for (Track t : tracksTable.getItems())
			{
				if(t.getOrder() >= next)
					next = (short) (t.getOrder() + 1);
			}
		}

		return next;
	}

	@FXML
	private void onPreviousEvent(ActionEvent e)
	{
		if(currentTrack != -1 && currentTrack > 0)
		{
			setTrack(currentTrack - 1);
			play();
		}
	}

	@FXML
	private void onNextEvent(ActionEvent e)
	{
		if(currentTrack != -1 && currentTrack + 1 < tracksTable.getItems().size())
		{
			setTrack(currentTrack + 1);
			play();
		}
	}
    
    @FXML
    private void onPlayPauseEvent(ActionEvent e)
    {
		if(mediaPlayer == null)
		{
			if(tracksTable.getItems().size() > 0)
			{
				setTrack(0);
				play();
			}
			else
				Dialog.message("Music Playlist", "You have no music tracks in your playlist at the moment", Alert.AlertType.INFORMATION);
		}
		else if(playing)
			pause();
		else
			play();
    }

	private void setTrack(int trackIndex)
	{
		Track track = tracksTable.getItems().get(trackIndex);
		Media media = new Media(track.getPath());

		if(mediaPlayer != null)
		{
			mediaPlayer.stop();
			mediaPlayer.dispose();
		}
		mediaPlayer = new MediaPlayer(media);

		mediaPlayer.setOnEndOfMedia(() -> {
			try
			{
				if(tracksTable.getItems().get(trackIndex + 1) != null)
				{
					setTrack(trackIndex + 1);
					play();
				}
			}
			catch(ArrayIndexOutOfBoundsException e) { /* End of the playlist */ }
		});

		currentTrack = trackIndex;
	}

	private void pause()
	{
		mediaPlayer.pause();
		playing = false;
		playPauseButton.setText("Play");
	}

	private void play()
	{
		mediaPlayer.play();
		playing = true;
		playPauseButton.setText("||");
	}
}
