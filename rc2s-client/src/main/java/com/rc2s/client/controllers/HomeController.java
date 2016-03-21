package com.rc2s.client.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class HomeController implements Initializable
{
    @FXML
    private Label data;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {}
    
    public void initView(String data)
    {
        this.data.setText(data);
    }
}
