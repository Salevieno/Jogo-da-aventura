package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import UI.ButtonFunction;
import UI.GameButton;
import UI.GameIconButton;
import graphics.Align;
import graphics.UtilAlignment;
import liveBeings.PlayerActions;
import main.Game;
import main.ImageLoader;
import main.Palette;
import main.Path;
import utilities.Util;


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
	protected String stdMenuUp ;
	protected String stdMenuDown ;
	protected String stdWindowUp ;
	protected String stdWindowDown ;
	protected String stdEnter ;
	protected String stdReturn ;
	protected String stdExit ;	
	protected double stdOpacity ;
	protected List<GameButton> buttons ;	

	protected static final int BORDER = 6 ;
	protected static final int PADDING = 4 ;
	protected static final Color STD_COLOR = Palette.colors[0] ;
	protected static final Color SELECTED_COLOR = Palette.colors[18];
	protected static final Font STD_FONT = new Font(Game.getMainFontName(), Font.BOLD, 10) ;
	protected static final Font TITLE_FONT = new Font(Game.getMainFontName(), Font.BOLD, 13) ;
	protected static final Font SUBTITLE_FONT = new Font(Game.getMainFontName(), Font.BOLD, 12) ;
	protected static final Image BTN_WINDOW_UP_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "moveUp.png") ;
	protected static final Image SELECTED_BTN_WINDOW_UP_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "selectedMoveUpSprite.png") ;
	protected static final Image BTN_WINDOW_DOWN_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "moveDown.png") ;
	protected static final Image SLOT_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "BagSlot.png") ;
	protected static final Image SELECTED_SLOT_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "BagSelectedSlot.png") ;
	protected static final Image SELECTED_BTN_WINDOW_DOWN_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "selectedMoveDownSprite.png") ;
	
	public GameWindow(String name, Point topLeftPos, Image image, int numberMenus, int numberTabs, int numberItems, int numberWindows)
	{
		this.name = name ;
		this.topLeftPos = topLeftPos ;
		this.image = image ;
		this.numberMenus = numberMenus ;
		this.numberTabs = numberTabs ;
		this.numberItems = numberItems ;
		this.numberWindows = numberWindows ;
		this.buttons = new ArrayList<>() ;
		this.isOpen = false ;
		this.menu = 0 ;
		this.tab = 0 ;
		this.item = 0 ;
		this.window = 0 ;
		this.size = image != null ? new Dimension(image.getWidth(null), image.getHeight(null)) : new Dimension(0, 0) ;
		this.stdMenuUp = PlayerActions.moveUp.getKey() ;
		this.stdMenuDown = PlayerActions.moveDown.getKey() ;
		this.stdWindowUp = PlayerActions.moveRight.getKey() ;
		this.stdWindowDown = PlayerActions.moveLeft.getKey() ;
		this.stdEnter = KeyEvent.getKeyText(KeyEvent.VK_ENTER) ;
		this.stdReturn = "MouseRightClick" ;
		this.stdExit = KeyEvent.getKeyText(KeyEvent.VK_ESCAPE) ;	
		this.stdOpacity = 0.9 ;
	}
	public boolean isOpen() {return isOpen ;}
	protected void addButton(GameButton button) { buttons.add(button) ;}
	public String getName() { return name ;}
	protected int getMenu() {return menu ;}
	protected int getTab() {return tab ;}
	protected int getWindow() {return window ;}
	protected int getItem() {return item ;}
	protected void setItem(int newValue) {item = newValue ;}

	public static Image getSlotImage() { return SLOT_IMAGE ;}
	
	public static boolean actionIsForward(String action) { return action == null ? false : action.equals("Enter") | action.equals("LeftClick") ;}
	protected GameButton windowUpButton(Point pos, Align align)
	{
		ButtonFunction action = () -> { windowUp() ;} ;
		return new GameIconButton(pos, align, BTN_WINDOW_UP_IMAGE, SELECTED_BTN_WINDOW_UP_IMAGE, action) ;
	}
	protected GameButton windowDownButton(Point pos, Align align)
	{
		ButtonFunction action = () -> { windowDown() ;} ;
		return new GameIconButton(pos, align, BTN_WINDOW_DOWN_IMAGE, SELECTED_BTN_WINDOW_DOWN_IMAGE, action) ;
	}
	
	protected boolean mouseIsOver(Point mousePos) { return Util.isInside(mousePos, topLeftPos, size) ;}
	
	public void open() { isOpen = true ; activateButtons() ;}
	public void close() { isOpen = false ; deactivateButtons() ;}
	public void switchOpenClose() { isOpen = !isOpen ;}

	private void activateButtons() { buttons.forEach(GameButton::activate) ;}
	private void deactivateButtons() { buttons.forEach(GameButton::deactivate) ;}

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
	
	protected Color getTextColor(boolean isSelected) { return isSelected ? SELECTED_COLOR : STD_COLOR ;}
	
	protected void checkMouseSelection(Point mousePos, Point itemPos, Align align, Dimension itemSize, int itemID)
	{
		Point textTopLeft = UtilAlignment.getTopLeft(itemPos, align, itemSize) ;
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
	public abstract void display(Point mousePos) ;
}
