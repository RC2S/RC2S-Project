package com.rc2s.client.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.rc2s.client.Config;
import com.rc2s.client.utils.Dialog;
import com.rc2s.client.utils.Resources;
import com.rc2s.common.vo.User;
import java.util.regex.Pattern;
import javafx.event.Event;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LoginController implements Initializable
{
    private static final Pattern IP_PATTERN = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    
    @FXML private TextField	ipaddr;
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
        if(!IP_PATTERN.matcher(ipaddr.getText()).matches())
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
            // TODO : remplacer par un appel à l'EJB d'authentification
            if(user.getUsername().equals("mathieu") && user.getPassword().equals("azeaze"))
            {
                Stage       stage;
                Scene       scene;
                FXMLLoader  loader;

                if(Dialog.confirm("test", "1234") == ButtonType.OK)
                {
                    // A simplifier ! Cache la première window
                    ((Node)event.getSource()).getScene().getWindow().hide();

                    loader  = Resources.loadFxml("HomeView");
                    scene   = new Scene((Parent) loader.getRoot());
                    HomeController controller = loader.getController();
                    controller.initView("Passing Data Test");
                    // Ou directement loader.<LoginController>getController().initView("Passing Data Test");

                    stage   = new Stage();
                    stage.setScene(scene);
                    stage.setTitle(Config.APP_NAME);
                    stage.show();
                }
            }
            else
                errorLabel.setText("Authentication failed");
        }
        else
            errorLabel.setText("Invalid IP address");
    }
}
