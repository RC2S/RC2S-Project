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
import com.rc2s.client.models.Login;

public class LoginController implements Initializable
{
    @FXML private TextField         username;
    @FXML private PasswordField     password;
    @FXML private Label             errorLabel;
    
    private Login                   login;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    @FXML
    private void handleSendButton(ActionEvent event)
    {
        login = new Login();
        login.setUsername(username.getText());
        login.setPassword(password.getText());
        
        if(login.getUsername().equals("mathieu") && login.getPassword().equals("azeaze"))
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
}
