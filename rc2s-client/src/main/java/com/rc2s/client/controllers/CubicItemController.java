package com.rc2s.client.controllers;

import com.rc2s.client.components.LedCube;
import com.rc2s.common.vo.Cube;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class CubicItemController implements Initializable
{
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
			if(cube != null)
			{
				System.out.println(cube.getName() + "!");
			}
		});
	}
	
	public void update(Cube cube)
	{
		this.cube = cube;
		
		LedCube ledCube = new LedCube(this.display, cube.getSize().getX(), cube.getSize().getY(), cube.getSize().getZ(), 5., Color.web(cube.getColor()));
		this.display.getChildren().add(ledCube);
		
		this.name.setText(cube.getName());
		this.ip.setText(cube.getIp());
		this.status.setText(cube.getCreated().toString());
	}
	
	
}
