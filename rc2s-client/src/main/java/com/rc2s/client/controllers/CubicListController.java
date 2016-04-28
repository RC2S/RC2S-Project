
package com.rc2s.client.controllers;

import com.rc2s.client.utils.EJB;
import com.rc2s.client.utils.Resources;
import com.rc2s.common.vo.Cube;
import com.rc2s.ejb.cube.CubeFacadeRemote;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

public class CubicListController implements Initializable
{
	private final int MAX_COLUMNS = 3;
	
	private final CubeFacadeRemote cubeEJB = (CubeFacadeRemote)EJB.lookup("CubeEJB");
	
	@FXML private ScrollPane scroller;
	@FXML private GridPane grid;
	
	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		this.scroller.setFitToHeight(true);
		this.scroller.setFitToWidth(true);
		
		List<Cube> cubes = cubeEJB.getAllCubes();
		
		int i = 0, j = 0;
		for(Cube cube : cubes)
		{
			FXMLLoader loader = Resources.loadFxml("CubicItemView");
			CubicItemController controller = loader.getController();
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
}
