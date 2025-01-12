package com.yozisunji.palipalette;

import java.util.ArrayList;


public class PaliItemList {
	String FuncName;
	ArrayList<PaliItem> items;
	
	public PaliItemList()
	{
		items = new ArrayList<PaliItem>();
	}
	public PaliItemList(String f)
	{
		items = new ArrayList<PaliItem>();
		this.FuncName = f;
	}
	
	public void putItem(int func, int item, int itemType, int width, int height, int d)
	{
		items.add(item,new PaliItem(func, item, itemType, width, height,d,FuncName));
	}
	
	public void putItem(int func, int item, int itemType, int width, int height, int d, String f)
	{
		items.add(item, new PaliItem(func, item, itemType, width, height,d,f));
		items.get(item).setVisible(true);
	}
	
	public void removeItem(int index)
	{
		items.get(index).setVisible(false);
	}
	
	public void restoreItem(int index)
	{
		items.get(index).setVisible(true);
	}
	/*
	public PaliItem getItem(int func, int item)
	{
		
	}*/
}