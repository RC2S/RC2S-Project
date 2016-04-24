package com.rc2s.client.controllers;

import com.rc2s.client.components.LedCube;
import com.rc2s.client.utils.EJB;
import com.rc2s.common.vo.Cube;
import com.rc2s.ejb.cube.CubeFacadeRemote;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class CubicDetailsController implements Initializable
{
	private final CubeFacadeRemote cubeEJB = (CubeFacadeRemote)EJB.lookup("CubeEJB");
	
	private Cube cube;
	
	@FXML private HBox display;
	
	// Status
	@FXML private Label nameLabel;
	@FXML private TextField nameField;
	
	@FXML private Label ipLabel;
	@FXML private TextField ipField;
	
	@FXML private Label colorLabel;
	@FXML private ComboBox colorBox;
	
	@FXML private Label sizeLabel;
	@FXML private ComboBox sizeBox;
	
	@FXML private Label statusLabel;
	
	@FXML ToggleButton editButton;
	
	// Synchronization
	@FXML private ComboBox cubesBox;
	@FXML private Button addCubeButton;
	
	@FXML private ListView synchronizedList;
	@FXML private TextField synchronizedField;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {}
	
	public void update(Cube cube)
	{
		this.cube = cube;
		
		LedCube ledCube = new LedCube(this.display, cube.getSize().getX(), cube.getSize().getY(), cube.getSize().getZ(), 10., Color.web(cube.getColor()), false);
		display.getChildren().add(ledCube);
		
		nameLabel.setText(cube.getName());
		nameField.setText(cube.getName());
		
		ipLabel.setText(cube.getIp());
		ipField.setText(cube.getIp());
		
		colorLabel.setText(cube.getColor());
		
		sizeLabel.setText(cube.getSize().getName());
		
		statusLabel.setText(cubeEJB.getStatus(cube) ? "Online" : "Offline");
	}
}
