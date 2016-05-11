package com.rc2s.client.controllers;

import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.utils.EJB;
import com.rc2s.common.vo.User;
import com.rc2s.ejb.user.UserFacadeRemote;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.apache.log4j.Logger;

public class AccessManagementController implements Initializable
{
	private final Logger logger = Logger.getLogger(this.getClass());
	private final UserFacadeRemote userEJB = (UserFacadeRemote)EJB.lookup("UserEJB");
	
	private List<User> users;
	private User element;
	
	@FXML private TableView gridTableView;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPassField;

    @FXML private ComboBox rolsBox;
    @FXML private ComboBox cubicAccessBox;

    @FXML private Button addButton;

    @Override
    public void initialize(URL location, ResourceBundle resources)
	{
		try
		{
			users = userEJB.getAll();
		}
		catch(EJBException e)
		{
			logger.error(e.getMessage());
		}
	}
	
	@FXML
	private void onAddEvent(ActionEvent e)
	{
		logger.info("ADD");
	}
}
