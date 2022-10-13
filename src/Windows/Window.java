package Windows;

import java.awt.Image;

public class Window
{
	protected Image image ;
	protected boolean isOpen ;
	protected int menu ;
	protected int numberMenus ;
	protected int tab ;
	protected int numberTabs ;
	protected int item ;
	protected int numberItems ;
	protected int window ;
	protected int numberWindows ;
	
	public Window(Image image, int numberMenus, int numberTabs, int numberItems, int numberWindows)
	{
		this.image = image ;
		this.numberMenus = numberMenus ;
		this.numberTabs = numberTabs ;
		this.numberItems = numberItems ;
		this.numberWindows = numberWindows ;
		isOpen = false ;
		menu = 0 ;
		tab = 0 ;
		item = 0 ;
		window = 0 ;
	}
	public boolean isOpen() {return isOpen ;}
	public int getMenu() {return menu ;}
	public int getTab() {return tab ;}
	public int getWindow() {return window ;}
	public int getItem() {return item ;}
	public void setItem(int newValue) {item = newValue ;}
	
	public void open()
	{
		isOpen = !isOpen ;	
	}
	
	public void menuUp()
	{
		if (menu < numberMenus - 1)
		{
			menu += 1 ;
		}
	}	
	public void menuDown()
	{
		if (0 < menu)
		{
			menu -= 1 ;
		}
	}
	
	public void tabUp()
	{
		if (tab < numberTabs - 1)
		{
			tab += 1 ;
		}
	}	
	public void tabDown()
	{
		if (0 < tab)
		{
			tab -= 1 ;
		}
	}
	
	public void windowUp()
	{
		if (window < numberWindows - 1)
		{
			window += 1 ;
		}
	}	
	public void windowDown()
	{
		if (0 < window)
		{
			window -= 1 ;
		}
	}
	
	public void itemUp()
	{
		if (item < numberItems - 1)
		{
			item += 1 ;
		}
	}	
	public void itemDown()
	{
		if (0 < item)
		{
			item -= 1 ;
		}
	}
}
