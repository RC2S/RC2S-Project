package com.rc2s.client.controllers;

import com.rc2s.client.Main;
import com.rc2s.common.utils.EJB;
import com.rc2s.client.utils.Resources;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.vo.Cube;
import com.rc2s.ejb.cube.CubeFacadeRemote;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

public class CubicListController extends TabController implements Initializable
{
	private final int MAX_COLUMNS = 3;
	
	private final CubeFacadeRemote cubeEJB = (CubeFacadeRemote)EJB.lookup("CubeEJB");
	
	@FXML private ScrollPane scroller;
	@FXML private GridPane grid;
	@FXML private Button addButton;
	
	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		this.scroller.setFitToHeight(true);
		this.scroller.setFitToWidth(true);
	}
	
	@Override
	public void updateContent()
	{
		try
		{
			List<Cube> cubes = cubeEJB.getCubes(Main.getAuthenticatedUser());

			int i = 0, j = 0;
			for(Cube cube : cubes)
			{
				FXMLLoader loader = Resources.loadFxml("CubicItemView");
				CubicItemController controller = loader.getController();
				controller.setTab(getTab());
				controller.update(cube);

				this.grid.add(loader.getRoot(), i, j);

				if(++i == MAX_COLUMNS)
				{
					i = 0;
					grid.addRow(1);
					j++;
				}
			}
		}
		catch(EJBException e)
		{
			System.err.println(e.getMessage());
		}
	}
	
	@FXML
	private void showAddView(ActionEvent e)
	{
		FXMLLoader loader = Resources.loadFxml("CubicDetailsView");
		CubicDetailsController controller = loader.getController();
		controller.setTab(getTab());

		controller.initEmpty();
		getTab().setContent(loader.getRoot());
	}
}
