package com.rc2s.client.controllers;

import com.rc2s.client.components.LedCube;
import com.rc2s.common.utils.EJB;
import com.rc2s.client.utils.Resources;
import com.rc2s.common.exceptions.EJBException;
import com.rc2s.common.vo.Cube;
import com.rc2s.ejb.cube.CubeFacadeRemote;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class CubicDetailsController implements Initializable
{
	private final CubeFacadeRemote cubeEJB = (CubeFacadeRemote)EJB.lookup("CubeEJB");
	
	private Cube cube;
	
	@FXML private HBox display;
	@FXML private Button backButton;
	
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
	public void initialize(URL url, ResourceBundle rb)
	{
		backButton.setOnAction((ActionEvent e) -> {
			Node root = ((Node)e.getSource()).getScene().getRoot().getChildrenUnmodifiable().get(0);
		
			if(root instanceof TabPane)
			{
				TabPane tabPane = (TabPane)root;
				
				FXMLLoader loader = Resources.loadFxml("CubicListView");				
				tabPane.getTabs().get(0).setContent(loader.getRoot());
			}
		});
	}
	
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
		
		try
		{
			boolean status = cubeEJB.getStatus(cube);
			statusLabel.setText(status ? "Online" : "Offline");
		}
		catch(EJBException e)
		{
			System.err.println(e.getMessage());
			statusLabel.setText("Offline");
		}
	}
}
