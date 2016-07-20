package com.rc2s.client.controllers;

import com.rc2s.client.Main;
import com.rc2s.client.utils.Dialog;
import com.rc2s.client.utils.Tools;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.utils.EJB;
import com.rc2s.common.vo.Plugin;
import com.rc2s.common.vo.Group;
import com.rc2s.ejb.plugin.PluginFacadeRemote;
import com.rc2s.ejb.plugin.loader.PluginLoaderFacadeRemote;
import com.rc2s.ejb.group.GroupFacadeRemote;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PluginsManagementController extends TabController implements Initializable
{
    private static final Logger log = LogManager.getLogger(PluginsManagementController.class);
    
	private static final String SERVER_PROTOCOL = "http";
    
	private static final String SERVER_PORT = "8080";
    
	private static final String SERVER_JNLP = "/rc2s-jnlp/rc2s-client.jnlp";
	
	private final GroupFacadeRemote groupEJB = (GroupFacadeRemote) EJB.lookup("GroupEJB");
    
	private final PluginFacadeRemote pluginEJB = (PluginFacadeRemote) EJB.lookup("PluginEJB");
    
	private final PluginLoaderFacadeRemote pluginLoaderEJB = (PluginLoaderFacadeRemote) EJB.lookup("PluginLoaderEJB");
	
    private final FileChooser fileChooser = new FileChooser();
    
	private File pluginFile;
	
    @FXML private TableView<Plugin> pluginsTable;
	
	@FXML private TableColumn<Plugin, String> nameColumn;
	@FXML private TableColumn<Plugin, String> versionColumn;
	@FXML private TableColumn<Plugin, String> authorColumn;
	@FXML private TableColumn<Plugin, String> activatedColumn;
	@FXML private TableColumn<Plugin, String> accessColumn;
	@FXML private TableColumn<Plugin, String> createdColumn;
	@FXML private TableColumn<Plugin, String> updatedColumn;
	
	@FXML private Button explorerButton;
	@FXML private ComboBox groupsBox;
	@FXML private Button addButton;
	@FXML private Label statusLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources)
	{
		nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
		versionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVersion()));
		authorColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAuthor()));
		activatedColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isActivated() ? "Yes" : "No"));
		accessColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAccess().toUpperCase()));
		createdColumn.setCellValueFactory(data -> new SimpleStringProperty(Tools.formatDate(data.getValue().getCreated())));
		updatedColumn.setCellValueFactory(data -> new SimpleStringProperty(Tools.formatDate(data.getValue().getUpdated())));
	}
	
	@Override
	public void updateContent()
	{
		pluginFile = null;
		updateGroups();
		updatePlugins();
	}
	
	private void updateGroups()
	{
		try
		{
			groupsBox.getItems().clear();
			groupsBox.getItems().addAll(groupEJB.getAll());
		}
		catch (EJBException e)
		{
			error(e.getMessage());
		}
	}
	
	private void updatePlugins()
	{
		try
		{
			pluginsTable.getItems().clear();
			pluginsTable.getItems().addAll(pluginEJB.getAll());
		}
		catch (EJBException e)
		{
			error(e.getMessage());
		}
	}
    
    @FXML
    private void handleUploadFileButton(final ActionEvent event)
    {
        pluginFile = fileChooser.showOpenDialog(Main.getStage());
		
		if (pluginFile != null)
			explorerButton.setText(pluginFile.getName());
	}
	
	@FXML
	private void onAddEvent(final ActionEvent e)
	{
		error("");
        
		if (pluginFile != null)
		{
			Group group = (Group) groupsBox.getSelectionModel().getSelectedItem();
			
			if(group != null)
			{
				try
				{
					pluginLoaderEJB.uploadPlugin("Test Plugin", group, Files.readAllBytes(pluginFile.toPath()));
					updatePlugins();
					ButtonType updateJnlp = Dialog.confirm("Upload success!", "Your plugin has been successfully uploaded to the server! Do you wish to restart your client now?");
						
					if (updateJnlp == ButtonType.OK)
					{
						String url = SERVER_PROTOCOL + "://" + EJB.getServerAddress() + ":" + SERVER_PORT + SERVER_JNLP;
						String jwsCmd = "javaws " + url;
                        
						log.info("JavaWS cmd: " + jwsCmd);
                        
						Runtime.getRuntime().exec(jwsCmd);
						Platform.exit();
					}
				}
				catch (IOException | EJBException ex)
				{
					Dialog.message("Error", ex.getMessage(), Alert.AlertType.ERROR);
					logger.error(ex);
					ex.printStackTrace();
				}
			}
			else
				error("Please select an access role for your plugin");
		}
		else
			error("Please pick a file in order to upload it to the server");
	}
	
	@FXML
	private void onKeyPressedEvent(final KeyEvent e)
	{
		if (e.getEventType() == KeyEvent.KEY_PRESSED)
		{
			if (e.getCode() == KeyCode.BACK_SPACE || e.getCode() == KeyCode.DELETE)
			{
				try
				{
					Plugin plugin = pluginsTable.getSelectionModel().getSelectedItem();
					ButtonType answer = Dialog.confirm("Are you sure you want to definitely remove this plugin from the RC2S Server?");
		
					if (answer == ButtonType.OK)
					{
						pluginLoaderEJB.deletePlugin(plugin);
						hidePluginTab(plugin.getName());
						updatePlugins();
					}
					
				}
				catch (EJBException ex)
				{
					error(ex.getMessage());
				}
			}
			
			e.consume();
		}
	}
	
	private void hidePluginTab(final String pluginName)
	{
		TabPane tabPane = getTab().getTabPane();
		
		for (Tab tab : tabPane.getTabs())
		{
			if (tab.getText().equals(pluginName))
			{
				tabPane.getTabs().remove(tab);
				break;
			}
		}
	}

	private void error(final String err)
	{
		statusLabel.setText(err);
		
		if(!err.isEmpty())
			log.error(err);
	}
}
