package windows;

import java.awt.Dimension;
import java.awt.Image;

public class GameWindow
{
	protected String name ;
	protected Image image ;
	protected boolean isOpen ;
	protected int menu ;
	protected int numberMenus ;
	protected int tab ;
	protected int numberTabs ;
	protected int item ;
	public int numberItems ;
	protected int window ;
	protected int numberWindows ;
	protected Dimension size ;
	
	public GameWindow(String name, Image image, int numberMenus, int numberTabs, int numberItems, int numberWindows)
	{
		this.name = name ;
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
		size = image != null ? new Dimension(image.getWidth(null), image.getHeight(null)) : new Dimension(0, 0) ;
	}
	public boolean isOpen() {return isOpen ;}
	public int getMenu() {return menu ;}
	public int getTab() {return tab ;}
	public int getWindow() {return window ;}
	public int getItem() {return item ;}
	public void setItem(int newValue) {item = newValue ;}
	
	public void open() { isOpen = !isOpen ;}
	
	public void close() { isOpen = false ;}
	
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
