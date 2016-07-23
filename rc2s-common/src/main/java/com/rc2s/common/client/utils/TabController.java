package com.rc2s.common.client.utils;

import javafx.scene.control.Tab;

public abstract class TabController
{
	private Tab tab;
	
	public abstract void updateContent();

	public Tab getTab()
	{
		return tab;
	}

	public void setTab(final Tab tab)
	{
		this.tab = tab;
	}
}
