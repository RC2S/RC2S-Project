package com.rc2s.client.controllers;

import com.rc2s.client.Main;
import com.rc2s.client.utils.Dialog;
import com.rc2s.client.utils.Tools;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.utils.EJB;
import com.rc2s.common.utils.Hash;
import com.rc2s.common.vo.Group;
import com.rc2s.common.vo.Synchronization;
import com.rc2s.common.vo.User;
import com.rc2s.ejb.group.GroupFacadeRemote;
import com.rc2s.ejb.synchronization.SynchronizationFacadeRemote;
import com.rc2s.ejb.user.UserFacadeRemote;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javax.validation.ConstraintViolation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AccessManagementController extends TabController implements Initializable
{
	private final Logger logger = LogManager.getLogger(this.getClass());
    
	private final UserFacadeRemote userEJB = (UserFacadeRemote) EJB.lookup("UserEJB");
	private final GroupFacadeRemote groupEJB = (GroupFacadeRemote) EJB.lookup("GroupEJB");
	private final SynchronizationFacadeRemote syncEJB = (SynchronizationFacadeRemote) EJB.lookup("SynchronizationEJB");
	
	private List<User> users;
	private User element;
	
	@FXML private TableView<User> usersTable;
	
	@FXML private TableColumn<User, String> usernameColumn;
	@FXML private TableColumn<User, String> activatedColumn;
	@FXML private TableColumn<User, String> lockedColumn;
	@FXML private TableColumn<User, String> groupColumn;
	@FXML private TableColumn<User, String> lastLoginColumn;
	@FXML private TableColumn<User, String> createdColumn;
	@FXML private TableColumn<User, String> updatedColumn;

	@FXML private Label boxLabel;
	@FXML private Button cancelEditButton;
	
	@FXML private CheckBox activatedCheckbox;
	@FXML private CheckBox lockedCheckbox;
	
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPassField;

    @FXML private ComboBox groupsBox;
    @FXML private ComboBox cubicAccessBox;

    @FXML private Button addButton;
	@FXML private Label errorLabel;

    @Override
    public void initialize(final URL location, final ResourceBundle resources)
	{
		usernameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()));
		activatedColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isActivated() ? "Yes" : "No"));
		lockedColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isLocked() ? "Yes" : "No"));
		groupColumn.setCellValueFactory(data -> new SimpleStringProperty((data.getValue().getGroups() != null) ? data.getValue().getGroups().get(0).getName() : "None"));
		lastLoginColumn.setCellValueFactory(data -> new SimpleStringProperty(formatDate(data.getValue().getLastLogin())));
		createdColumn.setCellValueFactory(data -> new SimpleStringProperty(formatDate(data.getValue().getCreated())));
		updatedColumn.setCellValueFactory(data -> new SimpleStringProperty(formatDate(data.getValue().getUpdated())));
		
		usersTable.setRowFactory(table -> {
			TableRow<User> row = new TableRow<>();
			
			row.setOnMouseClicked(e -> {
				if(!row.isEmpty() && e.getClickCount() == 2)
				{
					element = row.getItem();
					onEditStarts(element);
				}
			});
			
			return row;
		});
	}
	
	@Override
	public void updateContent()
	{
		updateUsers();
		clearElement();
		updateGroups();
		updateSync();
	}
	
	private void error(final String err)
	{
		errorLabel.setText(err);
		
		if(!err.isEmpty())
			logger.error(err);
	}
	
	private void updateUsers()
	{
		try
		{
			users = userEJB.getAll();
			usersTable.getItems().clear();
			usersTable.getItems().addAll(users);
			
			emptyForm();
		}
		catch(EJBException e)
		{
			error(e.getMessage());
		}
	}
	
	private void updateGroups()
	{
		try
		{
			groupsBox.getItems().clear();
			groupsBox.getItems().addAll(groupEJB.getAll());
		}
		catch(EJBException e)
		{
			error(e.getMessage());
		}
	}
	
	private void updateSync()
	{
		try
		{
			cubicAccessBox.getItems().clear();
			cubicAccessBox.getItems().addAll(syncEJB.getAll());
		}
		catch(EJBException e)
		{
			error(e.getMessage());
		}
	}
	
	private void clearElement()
	{
		element = new User();
	}
	
	private void updateElement(final boolean isNew)
	{
		Group group = (Group) groupsBox.getSelectionModel().getSelectedItem();
		Synchronization synchronization = (Synchronization)cubicAccessBox.getSelectionModel().getSelectedItem();
		
		if(isNew)
			element.setUsername(usernameField.getText());
		
		if(isNew || (!isNew && !passwordField.getText().isEmpty()))
			element.setPassword(Hash.sha1(passwordField.getText()));
        
		if(!isNew)
		{
			element.setActivated(activatedCheckbox.isSelected());
			element.setLocked(lockedCheckbox.isSelected());
		}
		
		if(group != null)
			element.setGroups(Arrays.asList(new Group[] {group}));
		if(synchronization != null)
			element.setSynchronizations(Arrays.asList(new Synchronization[] {synchronization}));
	}
	
	@FXML
	private void onAddEvent(final ActionEvent e)
	{
		if(passwordField.getText().equals(confirmPassField.getText()))
		{
			Group group = (Group) groupsBox.getSelectionModel().getSelectedItem();
			
			if(group != null)
			{
				updateElement(true);
				Set<ConstraintViolation<User>> violations = Tools.validate(element);

				if(violations.isEmpty())
				{
					try
					{
						userEJB.add(element);

						clearElement();
						updateUsers();
						error("");
					}
					catch(EJBException ex)
					{
						error(ex.getMessage());
					}
				}
				else
				{
					for(ConstraintViolation<User> v : violations)
					{
						error(v.getRootBeanClass().getSimpleName() + "." + v.getPropertyPath() + " " + v.getMessage());
						break;
					}
				}
			}
			else
				error("Please select a group for this user");
		}
		else
			error("Passwords don't match");
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
					User user = usersTable.getSelectionModel().getSelectedItem();
					
					if(!Main.getAuthenticatedUser().equals(user))
					{
						ButtonType answer = Dialog.confirm("Are you sure you want to definitely remove this user account?");

						if(answer == ButtonType.OK)
						{
							userEJB.delete(user);
							updateUsers();
						}
					}
					else
						Dialog.message("Error", "You cannot delete your own user account!", Alert.AlertType.ERROR);
					
				}
				catch(EJBException ex)
				{
					error(ex.getMessage());
				}
			}
			
			e.consume();
		}
	}
	
	private void onEditStarts(final User user)
	{
		error("");
		
		boxLabel.setText("Edit User");
		cancelEditButton.setVisible(true);
		addButton.setText("Edit!");
		addButton.setOnAction(event -> onEditSubmit(event));
		
		activatedCheckbox.setVisible(true);
		activatedCheckbox.setSelected(user.isActivated());
		lockedCheckbox.setVisible(true);
		lockedCheckbox.setSelected(user.isLocked());
		
		usernameField.setText(user.getUsername());
		usernameField.setDisable(true);
		
		groupsBox.getSelectionModel().select(user.getGroups().get(0));
	}
	
	@FXML
	private void onEditCanceled(final ActionEvent e)
	{
		error("");
		
		boxLabel.setText("Add User");
		cancelEditButton.setVisible(false);
		addButton.setText("Add!");
		addButton.setOnAction(event -> onAddEvent(event));
		activatedCheckbox.setVisible(false);
		lockedCheckbox.setVisible(false);
		usernameField.setDisable(false);
		
		clearElement();
		emptyForm();
	}
	
	@FXML
	private void onEditSubmit(final ActionEvent e)
	{
		boolean passwordUpdated = !passwordField.getText().isEmpty();
		
		if(!passwordUpdated || (passwordUpdated && passwordField.getText().equals(confirmPassField.getText())))
		{
			updateElement(false);
			Set<ConstraintViolation<User>> violations = Tools.validate(element);

			if(violations.isEmpty())
			{
				try
				{
					userEJB.update(element, passwordUpdated);

					clearElement();
					updateUsers();
					onEditCanceled(null);
				}
				catch(EJBException ex)
				{
					error(ex.getMessage());
				}
			}
			else
			{
				for(ConstraintViolation<User> v : violations)
				{
					error(v.getRootBeanClass().getSimpleName() + "." + v.getPropertyPath() + " " + v.getMessage());
					break;
				}
			}
		}
		else
		{
			error("Passwords don't match");
		}
	}
	
	private void emptyForm()
	{
		usernameField.clear();
		passwordField.clear();
		confirmPassField.clear();
		groupsBox.getSelectionModel().select(null);
		cubicAccessBox.getSelectionModel().select(null);
		activatedCheckbox.setSelected(false);
		lockedCheckbox.setSelected(false);
	}
	
	private String formatDate(Date date)
	{
		if(date == null)
			return "";
		
		return new SimpleDateFormat("MM-dd-YYYY hh:mm").format(date);
	}
}
