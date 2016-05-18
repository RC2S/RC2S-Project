package com.rc2s.client.controllers;

import com.rc2s.client.components.LedCube;
import com.rc2s.client.utils.EJB;
import com.rc2s.client.utils.Resources;
import com.rc2s.common.vo.Cube;
import com.rc2s.ejb.cube.CubeFacadeRemote;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class CubicItemController implements Initializable
{
	private final CubeFacadeRemote cubeEJB = (CubeFacadeRemote)EJB.lookup("CubeEJB");
	
	private Cube cube;
	
	@FXML private AnchorPane root;
	@FXML private HBox display;
	@FXML private Label name;
	@FXML private Label ip;
	@FXML private Label status;
	
	@Override
    public void initialize(URL url, ResourceBundle rb)
	{
		root.setOnMouseClicked((MouseEvent e) -> {
			Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
			
			if(cube != null)
			{
				FXMLLoader loader = Resources.loadFxml("CubicDetailsView");
				Scene scene = new Scene((Parent)loader.getRoot());
				CubicDetailsController controller = loader.getController();
				controller.update(cube);
				
				stage.setScene(scene);
			}
		});
	}
	
	public void update(Cube cube)
	{
		this.cube = cube;
		
		LedCube ledCube = new LedCube(this.display, cube.getSize().getX(), cube.getSize().getY(), cube.getSize().getZ(), 5., Color.web(cube.getColor()), false);
		this.display.getChildren().add(ledCube);
		
		this.name.setText(cube.getName());
		this.ip.setText(cube.getIp());
		this.status.setText(cubeEJB.getStatus(cube) ? "Online" : "Offline");
	}
}
