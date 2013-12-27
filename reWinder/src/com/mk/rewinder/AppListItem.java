package com.mk.rewinder;

public class AppListItem
{
	public boolean checkBox;
	public String appName;
	public String button;

	public AppListItem() {
		super();
	}

	public AppListItem(boolean checkBox, String appName, String button) {
		super();
		this.checkBox = checkBox;
		this.appName = appName;
		this.button = button;
	}
}
