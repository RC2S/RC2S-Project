package com.rc2s.client.controllers;

import com.rc2s.client.Main;
import com.rc2s.client.utils.Resources;
import com.rc2s.common.vo.Role;
import com.rc2s.common.vo.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class HomeController implements Initializable
{
    @FXML
    private TabPane tabPane;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        tabPane.getTabs().add(new Tab("Cubic List", Resources.loadFxml("CubicListView").getRoot()));
        tabPane.getTabs().add(new Tab("Music PlayList", Resources.loadFxml("MusicPlaylistView").getRoot()));
		
		if(isAdmin(Main.getAuthenticatedUser()))
		{
			tabPane.getTabs().add(new Tab("Access Management", Resources.loadFxml("AccessManagementView").getRoot()));
			tabPane.getTabs().add(new Tab("Plugins Management", Resources.loadFxml("PluginsManagementView").getRoot()));
		}
    }
	
	/**
	 * Returns true if the given User is mapped with the ADMIN role.
	 * @param user
	 * @return 
	 */
	private boolean isAdmin(User user)
	{
		for(Role r : user.getRoles())
		{
			if(r.getName().equalsIgnoreCase("admin"))
			{
				return true;
			}
		}
		
		return false;
	}
}
