package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;

import components.GameButton;
import components.IconFunction;
import graphics.DrawPrimitives;
import libUtil.Align;
import libUtil.Util;
import liveBeings.PlayerActions;
import main.Game;
import utilities.UtilS;

public abstract class GameWindow
{
	protected String name ;
	protected Point topLeftPos ;
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
	
	protected String stdMenuUp = PlayerActions.moveUp.getKey() ;
	protected String stdMenuDown = PlayerActions.moveDown.getKey() ;
	protected String stdWindowUp = PlayerActions.moveRight.getKey() ;
	protected String stdWindowDown = PlayerActions.moveLeft.getKey() ;
	protected String stdEnter = KeyEvent.getKeyText(KeyEvent.VK_ENTER) ;
	protected String stdReturn = "MouseRightClick" ;
	protected String stdExit = KeyEvent.getKeyText(KeyEvent.VK_ESCAPE) ;	
	protected double stdOpacity = 0.85 ;

	protected static final Image buttonWindowUpImage = UtilS.loadImage("\\Windows\\" + "moveUp.png") ;
	protected static final Image selectedButtonWindowUpImage = UtilS.loadImage("\\Windows\\" + "SelectedMoveUp.gif") ;
	protected static final Image buttonWindowDownImage = UtilS.loadImage("\\Windows\\" + "moveDown.png") ;
	protected static final Image selectedButtonWindowDownImage = UtilS.loadImage("\\Windows\\" + "selectedMoveDown.gif") ;
	protected static final Font stdFont = new Font(Game.MainFontName, Font.BOLD, 10) ;
	protected static final Font subTitleFont = new Font(Game.MainFontName, Font.BOLD, 11) ;
	protected static final Font titleFont = new Font(Game.MainFontName, Font.BOLD, 13) ;
	protected static final Color stdColor = Game.colorPalette[0] ;
	protected static final Color selColor = Game.colorPalette[18] ;
	protected static final int border = 6 ;
	protected static final int padding = 4 ;
	
	public GameWindow(String name, Point topLeftPos, Image image, int numberMenus, int numberTabs, int numberItems, int numberWindows)
	{
		this.name = name ;
		this.topLeftPos = topLeftPos ;
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
	protected int getMenu() {return menu ;}
	protected int getTab() {return tab ;}
	protected int getWindow() {return window ;}
	protected int getItem() {return item ;}
	protected void setItem(int newValue) {item = newValue ;}
	
	public static boolean actionIsForward(String action) { return action == null ? false : action.equals("Enter") | action.equals("LeftClick") ;}
	protected GameButton windowUpButton(Point pos, Align align)
	{
		IconFunction action = () -> { windowUp() ;} ;
		return new GameButton(pos, align, buttonWindowUpImage, selectedButtonWindowUpImage, action) ;
	}
	protected GameButton windowDownButton(Point pos, Align align)
	{
		IconFunction action = () -> { windowDown() ;} ;
		return new GameButton(pos, align, buttonWindowDownImage, selectedButtonWindowDownImage, action) ;
	}
	
	protected boolean mouseIsOver(Point mousePos) { return Util.isInside(mousePos, topLeftPos, size) ;}
	
	public void open() { isOpen = true ;}
	public void close() { isOpen = false ;}
	
	protected void menuUp()
	{
		if (menu < numberMenus - 1)
		{
			menu += 1 ;
		}
	}	
	protected void menuDown()
	{
		if (0 < menu)
		{
			menu -= 1 ;
		}
	}
	
	protected void tabUp()
	{
		if (tab < numberTabs - 1)
		{
			tab += 1 ;
		}
	}	
	protected void tabDown()
	{
		if (0 < tab)
		{
			tab -= 1 ;
		}
	}
	
	protected void windowUp()
	{
		if (window < numberWindows - 1)
		{
			window += 1 ;
		}
	}	
	protected void windowDown()
	{
		if (0 < window)
		{
			window -= 1 ;
		}
	}
	
	protected void itemUp()
	{
		if (item < numberItems - 1)
		{
			item += 1 ;
		}
	}	
	protected void itemDown()
	{
		if (0 < item)
		{
			item -= 1 ;
		}
	}

	public void reset()
	{
		menu = 0 ;
		tab = 0 ;
		window = 0 ;
		item = 0 ;
	}
	
	protected Color getTextColor(boolean isSelected) { return isSelected ? selColor : stdColor ;}
	
	protected void checkMouseSelection(Point mousePos, Point itemPos, Align align, Dimension itemSize, int itemID)
	{
		Point textTopLeft = Util.getTopLeft(itemPos, align, itemSize) ;
		if (!Util.isInside(mousePos, textTopLeft, itemSize)) { return ;}
		
		item = itemID ;
	}
	
	protected void stdNavigation(String action)
	{
		if (action.equals(stdWindowUp)) { windowUp() ;}
		if (action.equals(stdWindowDown)) { windowDown() ;}
		if (action.equals(stdMenuUp)) { menuUp() ;}
		if (action.equals(stdMenuDown)) { menuDown() ;}
		if (action.equals(stdEnter)) { tabUp() ;}
		if (action.equals(stdReturn)) { tabDown() ;}
	}
	
	public abstract void navigate(String action) ;
	public abstract void display(Point mousePos, DrawPrimitives DP) ;
}
