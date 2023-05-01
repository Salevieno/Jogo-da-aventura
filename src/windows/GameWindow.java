package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import main.Game;

public abstract class GameWindow
{
	protected String name ;
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
	protected Dimension size ;
	
	protected final Font stdFont = new Font(Game.MainFontName, Font.BOLD, 10) ;
	protected final Font subTitleFont = new Font(Game.MainFontName, Font.BOLD, 11) ;
	protected final Font titleFont = new Font(Game.MainFontName, Font.BOLD, 13) ;
	protected final Color stdColor = Game.colorPalette[9] ;
	protected final Color selColor = Game.colorPalette[3] ;
	protected final int border = 6 ;
	protected final int padding = 4 ;
	
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

	public abstract void navigate(String action) ;
//	public abstract void display(Point MousePos, DrawingOnPanel DP) ;
}
