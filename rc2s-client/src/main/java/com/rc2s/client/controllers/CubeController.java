package com.rc2s.client.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class CubeController implements Initializable
{
    @FXML private Label label;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        label.setText("Coucou test");
    }
}
