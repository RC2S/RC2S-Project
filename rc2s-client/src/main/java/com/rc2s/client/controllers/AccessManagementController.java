package com.rc2s.client.controllers;

import com.rc2s.client.utils.Dialog;
import com.rc2s.client.utils.Tools;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.utils.EJB;
import com.rc2s.common.vo.Plugin;
import com.rc2s.common.vo.Role;
import com.rc2s.common.vo.Synchronization;
import com.rc2s.common.vo.User;
import com.rc2s.ejb.role.RoleFacadeRemote;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javax.validation.ConstraintViolation;
import org.apache.log4j.Logger;

public class AccessManagementController extends TabController implements Initializable
{
	private final Logger logger = Logger.getLogger(this.getClass());
	private final UserFacadeRemote userEJB = (UserFacadeRemote)EJB.lookup("UserEJB");
	private final RoleFacadeRemote roleEJB = (RoleFacadeRemote)EJB.lookup("RoleEJB");
	private final SynchronizationFacadeRemote syncEJB = (SynchronizationFacadeRemote)EJB.lookup("SynchronizationEJB");
	
	private List<User> users;
	private User element;
	
	@FXML private TableView<User> usersTable;
	
	@FXML private TableColumn<User, String> usernameColumn;
	@FXML private TableColumn<User, String> activatedColumn;
	@FXML private TableColumn<User, String> lockedColumn;
	@FXML private TableColumn<User, String> lastLoginColumn;
	@FXML private TableColumn<User, String> createdColumn;
	@FXML private TableColumn<User, String> updatedColumn;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPassField;

    @FXML private ComboBox rolesBox;
    @FXML private ComboBox cubicAccessBox;

    @FXML private Button addButton;
	@FXML private Label errorLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources)
	{
		usernameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()));
		activatedColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isActivated() ? "Yes" : "No"));
		lockedColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isLocked() ? "Yes" : "No"));
		
		lastLoginColumn.setCellValueFactory(data -> new SimpleStringProperty(formatDate(data.getValue().getLastLogin())));
		createdColumn.setCellValueFactory(data -> new SimpleStringProperty(formatDate(data.getValue().getCreated())));
		updatedColumn.setCellValueFactory(data -> new SimpleStringProperty(formatDate(data.getValue().getUpdated())));
	}
	
	@Override
	public void updateContent()
	{
		updateUsers();
		clearElement();
		updateRoles();
		updateSync();
	}
	
	private void error(String err)
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
			
			usernameField.clear();
			passwordField.clear();
			confirmPassField.clear();
			rolesBox.getSelectionModel().select(null);
			cubicAccessBox.getSelectionModel().select(null);
		}
		catch(EJBException e)
		{
			error(e.getMessage());
		}
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
	
	private void updateElement()
	{
		Role role = (Role)rolesBox.getSelectionModel().getSelectedItem();
		Synchronization synchronization = (Synchronization)cubicAccessBox.getSelectionModel().getSelectedItem();
		
		element.setUsername(usernameField.getText());
		element.setPassword(passwordField.getText());
		element.setActivated(false);
		element.setLocked(false);
		
		element.setRoles(Arrays.asList(new Role[] {role}));
		element.setSynchronizations(Arrays.asList(new Synchronization[] {synchronization}));
	}
	
	@FXML
	private void onAddEvent(ActionEvent e)
	{
		if(passwordField.getText().equals(confirmPassField.getText()))
		{
			updateElement();
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
		{
			error("Passwords don't match");
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
					User user = usersTable.getSelectionModel().getSelectedItem();
					ButtonType answer = Dialog.confirm("Are you sure you want to definitely remove this user account?");
		
					if(answer == ButtonType.OK)
					{
						userEJB.delete(user);
						updateUsers();
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
	
	private String formatDate(Date date)
	{
		if(date == null)
			return "";
		
		return new SimpleDateFormat("MM-dd-YYYY hh:mm").format(date);
	}
}
