package com.rc2s.client.controllers;

import com.rc2s.client.Main;
import com.rc2s.client.utils.Dialog;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.utils.EJB;
import com.rc2s.common.vo.Plugin;
import com.rc2s.common.vo.Role;
import com.rc2s.ejb.plugin.PluginFacadeRemote;
import com.rc2s.ejb.plugin.loader.PluginLoaderFacadeRemote;
import com.rc2s.ejb.role.RoleFacadeRemote;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private static final Logger logger = LogManager.getLogger(PluginsManagementController.class);
	
	private final RoleFacadeRemote roleEJB = (RoleFacadeRemote)EJB.lookup("RoleEJB");
	private final PluginFacadeRemote pluginEJB = (PluginFacadeRemote)EJB.lookup("PluginEJB");
	private final PluginLoaderFacadeRemote pluginLoaderEJB = (PluginLoaderFacadeRemote)EJB.lookup("PluginLoaderEJB");
	
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
	@FXML private ComboBox rolesBox;
	@FXML private Button addButton;
	@FXML private Label statusLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources)
	{
		nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
		versionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVersion()));
		authorColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAuthor()));
		activatedColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isActivated() ? "Yes" : "No"));
		accessColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAccess()));
		createdColumn.setCellValueFactory(data -> new SimpleStringProperty(formatDate(data.getValue().getCreated())));
		updatedColumn.setCellValueFactory(data -> new SimpleStringProperty(formatDate(data.getValue().getUpdated())));
	}
	
	@Override
	public void updateContent()
	{
		pluginFile = null;
		updateRoles();
		updatePlugins();
	}
	
	private void updateRoles()
	{
		try
		{
			rolesBox.getItems().clear();
			rolesBox.getItems().addAll(roleEJB.getAll());
		}
		catch(EJBException e)
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
		catch(EJBException e)
		{
			error(e.getMessage());
		}
	}
    
    @FXML
    private void handleUploadFileButton(ActionEvent event)
    {
        pluginFile = fileChooser.showOpenDialog(Main.getStage());
		
		if(pluginFile != null)
			explorerButton.setText(pluginFile.getName());
	}
	
	@FXML
	private void onAddEvent(ActionEvent e)
	{
		error("");
		if(pluginFile != null)
		{
			Role role = (Role)rolesBox.getSelectionModel().getSelectedItem();
			
			if(role != null)
			{
				try
				{
					pluginLoaderEJB.uploadPlugin("Test Plugin", role, Files.readAllBytes(pluginFile.toPath()));
					ButtonType confirm = Dialog.confirm("Upload success!", "Your plugin has been successfully uploaded to the server! Would you wish to restart your client now?");
						
					if(confirm == ButtonType.OK)
						Platform.exit();
				}
				catch(EJBException | IOException ex)
				{
					error(ex.getMessage());
				}
			}
			else
				error("Please select an access role for your plugin");
		}
		else
			error("Please pick a file in order to upload it to the server");
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
					Plugin plugin = pluginsTable.getSelectionModel().getSelectedItem();
					ButtonType answer = Dialog.confirm("Are you sure you want to definitely remove this plugin from the RC2S Server?");
		
					if(answer == ButtonType.OK)
					{
						pluginLoaderEJB.deletePlugin(plugin);
						hidePluginTab(plugin.getName());
						updatePlugins();
					}
					
				}
				catch(EJBException ex)
				{
					error(ex.getMessage());
				}
			}
			
			e.consume();
		}
	}
	
	private void hidePluginTab(String pluginName)
	{
		TabPane tabPane = getTab().getTabPane();
		
		for(Tab tab : tabPane.getTabs())
		{
			if(tab.getText().equals(pluginName))
			{
				tabPane.getTabs().remove(tab);
				break;
			}
		}
	}
	
	private String formatDate(Date date)
	{
		if(date == null)
			return "";
		
		return new SimpleDateFormat("MM-dd-YYYY hh:mm").format(date);
	}
	
	private void error(String err)
	{
		statusLabel.setText(err);
		
		if(!err.isEmpty())
			logger.error(err);
	}
}