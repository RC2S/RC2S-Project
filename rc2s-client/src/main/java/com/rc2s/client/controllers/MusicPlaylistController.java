package com.rc2s.client.controllers;

import com.rc2s.client.Main;
import com.rc2s.client.test.Streaming;
import com.rc2s.client.test.StreamingHandler;
import com.rc2s.client.utils.Dialog;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.utils.EJB;
import com.rc2s.common.vo.Synchronization;
import com.rc2s.common.vo.Track;
import com.rc2s.ejb.streaming.StreamingFacadeRemote;
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
import javafx.stage.Window;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class MusicPlaylistController extends TabController implements Initializable
{
    private final TrackFacadeRemote trackEJB = (TrackFacadeRemote) EJB.lookup("TrackEJB");
    private final SynchronizationFacadeRemote syncEJB = (SynchronizationFacadeRemote) EJB.lookup("SynchronizationEJB");
	private final StreamingFacadeRemote streamingEJB = (StreamingFacadeRemote) EJB.lookup("StreamingEJB");

	private final String META_TITLE = "title";
	private final String META_ARTIST = "xmpDM:artist";
	private final String META_DURATION = "xmpDM:duration";
	private final String META_GENRE = "xmpDM:genre";

	private MediaPlayer mediaPlayer;
	private StreamingHandler streamingHandler;
	private boolean playing = false;
	private int currentTrack = -1;

	private Map<Track, Metadata> tracksMetadata;

    @FXML private TableView<Track> tracksTable;
    @FXML private TableColumn<Track, String> musicColumn;
	@FXML private TableColumn<Track, String> authorColumn;
	@FXML private TableColumn<Track, String> timeColumn;
	@FXML private TableColumn<Track, String> genreColumn;

    @FXML private ComboBox<Synchronization> syncBox;
    @FXML private Button previousButton;
    @FXML private Button playPauseButton;
    @FXML private Button nextButton;
    @FXML private Slider soundSlider;

    @Override
    public void initialize(final URL url, final ResourceBundle rb)
    {
		if(System.getProperty("os.name").toLowerCase().contains("windows"))
			System.setProperty("jna.library.path", "C:\\Program Files\\VideoLAN\\VLC");

		tracksMetadata = new HashMap<>();

		musicColumn.setCellValueFactory(data -> getValueFromMetadata(data.getValue(), META_TITLE));
		authorColumn.setCellValueFactory(data -> getValueFromMetadata(data.getValue(), META_ARTIST));
		timeColumn.setCellValueFactory(data -> getValueFromMetadata(data.getValue(), META_DURATION));
		genreColumn.setCellValueFactory(data -> getValueFromMetadata(data.getValue(), META_GENRE));

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

	private SimpleStringProperty getValueFromMetadata(Track track, String metadataName)
	{
		Metadata metadata = tracksMetadata.get(track);

		if(metadata != null)
		{
			String value = metadata.get(metadataName);

			if(metadataName.equalsIgnoreCase(META_DURATION))
			{
				double duration = Double.parseDouble(value) / 1000.;
				int minutes = (int)(duration / 60.);
				int seconds = (int)(duration - (60. * minutes));

				value = new StringBuilder().append(minutes).append(":").append(seconds).toString();
			}

			return new SimpleStringProperty(value);
		}

		return new SimpleStringProperty("No such metadata: " + metadataName);
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
			List<Track> tracks = trackEJB.getTracksByUser(Main.getAuthenticatedUser());

			// Clear metadata HashMap
			tracksMetadata.clear();

			// Gather metadata
			for(Track track : tracks)
			{
				try
				{
					File file = new File(URLDecoder.decode(track.getPath(), "UTF-8").replace("file:/", ""));

					Parser parser = new AutoDetectParser();
					BodyContentHandler handler = new BodyContentHandler();
					Metadata metadata = new Metadata();
					FileInputStream is = new FileInputStream(file);
					ParseContext context = new ParseContext();

					parser.parse(is, handler, metadata, context);
					tracksMetadata.put(track, metadata);
				}
				catch (IOException | TikaException | SAXException e) {}
			}

			// Update table content
			tracksTable.getItems().clear();
			tracksTable.getItems().addAll(tracks);
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

			syncBox.getSelectionModel().selectFirst();
        }
        catch(EJBException e)
        {
            Dialog.message("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

	@FXML
	private void onSyncSelected(final ActionEvent e)
	{
		Synchronization sync = syncBox.getSelectionModel().getSelectedItem();

		if(sync == null)
		{
			playPauseButton.setDisable(true);
			nextButton.setDisable(true);
			previousButton.setDisable(true);
		}
		else if(playPauseButton.isDisabled())
		{
			playPauseButton.setDisable(false);
			nextButton.setDisable(false);
			previousButton.setDisable(false);
		}

		streamingEJB.setSynchronization(sync);
	}

	@FXML
	private void onKeyPressedEvent(final KeyEvent e)
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
						trackEJB.delete(track);
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
	private void onDragOverEvent(final DragEvent event)
	{
		Dragboard db = event.getDragboard();

		if(db.hasFiles())
			event.acceptTransferModes(TransferMode.COPY);
		else
			event.consume();
	}

	@FXML
	private void onDragDroppedEvent(final DragEvent event)
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
					trackEJB.add(track);
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

	private Track uriToTrack(final URI uri) throws MediaException
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
	private void onPreviousEvent(final ActionEvent e)
	{
		if(currentTrack != -1 && currentTrack > 0)
		{
			setTrack(currentTrack - 1);
			play();
		}
	}

	@FXML
	private void onNextEvent(final ActionEvent e)
	{
		if(currentTrack != -1 && currentTrack + 1 < tracksTable.getItems().size())
		{
			setTrack(currentTrack + 1);
			play();
		}
	}
    
    @FXML
    private void onPlayPauseEvent(final ActionEvent e)
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

	private synchronized void setTrack(final int trackIndex)
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
			catch(IndexOutOfBoundsException e)
			{
				synchronized (streamingHandler) {
					// End of the playlist
					mediaPlayer.stop();
					mediaPlayer.dispose();
					mediaPlayer = null;

					streamingHandler.stopStreaming();
					streamingHandler = null;
				}
			}
		});

		// Create/Update the streaming object
		try
		{
			// If it exists, we notify it to stop
			if (streamingHandler != null) {
				synchronized (streamingHandler) {
					streamingHandler.stopStreaming();
				}
			}

			streamingHandler = new StreamingHandler(
				streamingEJB,
				Main.getAuthenticatedUser().getUsername(),
				URLDecoder.decode(track.getPath(), "UTF-8").replace("file:/", "")
			);
			streamingHandler.setDaemon(true);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		currentTrack = trackIndex;
	}

	private void pause()
	{
		mediaPlayer.pause();

		if (streamingHandler != null && streamingHandler.isPlaying()) {
			synchronized (streamingHandler) {
				streamingHandler.pauseStreaming();
			}
		}

		playing = false;
		playPauseButton.setText("Play");
	}

	private void play()
	{
		mediaPlayer.play();

		if (streamingHandler != null) {
			synchronized (streamingHandler) {
				if (streamingHandler.getStreamingState() == Streaming.StreamingState.INIT)
					streamingHandler.start();
				else if (!streamingHandler.isPlaying()) {
					streamingHandler.resumeStreaming();
				}
			}
		}

		playing = true;
		playPauseButton.setText("||");
	}
}
