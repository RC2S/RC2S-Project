package com.rc2s.client.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.rc2s.client.Main;
import com.rc2s.client.utils.EJB;
import com.rc2s.client.utils.Resources;
import com.rc2s.client.utils.Tools;
import com.rc2s.common.vo.User;
import com.rc2s.ejb.authentication.AuthenticationFacadeRemote;
import javafx.event.Event;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LoginController implements Initializable
{    
    private final AuthenticationFacadeRemote authenticationEJB = (AuthenticationFacadeRemote)EJB.lookup("AuthenticationEJB");
    
    @FXML private TextField ipaddr;
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private Label errorLabel;
    
    private User user;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    @FXML
    private void handleReturnPressed(KeyEvent event)
    {
        if(event.getEventType() == KeyEvent.KEY_PRESSED
        && event.getCode() == KeyCode.ENTER)
            connect(event);
    }
	
    @FXML
    private void handleConnectButton(ActionEvent event)
    {
        connect(event);
    }
	
    @FXML
    private boolean validateIpAddress(KeyEvent event)
    {
        if(!Tools.matchIP(ipaddr.getText()))
        {
            errorLabel.setText("Invalid IP address");
            return false;
        }

        errorLabel.setText("");
        return true;
    }
	
    private void connect(Event event)
    {
        user = new User();
        user.setUsername(username.getText());
        user.setPassword(password.getText());

        if(validateIpAddress(null))
        {
            authenticationEJB.login(user);

            // TODO : remplacer par un appel à l'EJB d'authentification
            if(user.getUsername().equals("mathieu") && user.getPassword().equals("azeaze"))
            {
                Scene       scene;
                FXMLLoader  loader;

                loader  = Resources.loadFxml("HomeView");
                scene   = new Scene((Parent) loader.getRoot());
                
                Main.getStage().setScene(scene);
                Main.getStage().setMinWidth(scene.getWidth());
                Main.getStage().setMinHeight(scene.getHeight());
                Main.getStage().show();
            }
            else
                errorLabel.setText("Authentication failed");
        }
        else
            errorLabel.setText("Invalid IP address");
    }
}
