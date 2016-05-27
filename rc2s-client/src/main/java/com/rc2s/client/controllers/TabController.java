package com.rc2s.client.controllers;

import javafx.scene.control.Tab;

public abstract class TabController
{
	private Tab tab;
	
	public abstract void updateContent();

	public Tab getTab()
	{
		return tab;
	}

	public void setTab(Tab tab)
	{
		this.tab = tab;
	}
}
