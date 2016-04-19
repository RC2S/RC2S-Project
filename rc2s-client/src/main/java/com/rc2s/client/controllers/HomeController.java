/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rc2s.client.controllers;

import com.rc2s.annotations.Knowledge;
import com.rc2s.client.utils.Resources;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

@Knowledge(name="testknowledge")
public class HomeController implements Initializable
{
    @FXML
    private TabPane tabPane;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {      
        tabPane.getTabs().add(new Tab("Music PlayList", Resources.loadFxml("MusicPlaylistView").getRoot()));
        tabPane.getTabs().add(new Tab("Access Management", Resources.loadFxml("AccessManagementView").getRoot()));
        tabPane.getTabs().add(new Tab("Plugins Management", Resources.loadFxml("PluginsManagementView").getRoot()));
    }
}
