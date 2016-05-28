package com.rc2s.testpluginname.client.controllers;

import com.rc2s.annotations.SourceControl;
import java.net.URL;
import java.util.ResourceBundle;

@SourceControl
public class MainController
{
	public int add(int a, int b)
	{
		return a + b;
	}
	
	public long suppr(long a, long b)
	{
		return a - b;
	}
	
    public void initialize(URL location, ResourceBundle resources) {}
}
