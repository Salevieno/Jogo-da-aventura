package sidebar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.List;

import graphics.Align;
import items.Item;
import liveBeings.Player;
import main.Game;
import main.GamePanel;
import main.ImageLoader;
import main.Palette;
import main.Path;
import screen.Screen;
import utilities.Util;
import windows.GameWindow;

public abstract class HotKeysBar
{
	private static final Font FONT = new Font(Game.getMainFontName(), Font.BOLD, 14) ;
	private static final Color TEXT_COLOR = Palette.colors[0] ;
	private static final Image IMAGE = ImageLoader.loadImage(Path.SIDEBAR_IMG + "HotBar.png") ;
	private static final Point BAR_POS = new Point(Screen.getMe().mapSize().width + 2, Screen.getMe().getSize().height - SideBar.SY) ;

	public static Dimension size() { return Util.getSize(IMAGE) ;}
	public static Point topLeft() { return new Point(BAR_POS.x, BAR_POS.y - size().height) ;}

	public static int slotHovered(Point mousePos)
	{
		Dimension slotSize = Util.getSize(SideBar.SLOT_IMAGE) ;
		for (int i = 0 ; i <= Player.getHotKeys().length - 1 ; i += 1)
		{
			Point slotCenter = Util.translate(topLeft(), 10, 10 + 20 * i) ;
			if (Util.isInside(mousePos, Util.translate(slotCenter, -slotSize.width / 2, -slotSize.height / 2), slotSize))
			{
				return i ;
			}
		}
		
		return -1 ;
	}
	
	public static void display(List<Item> hotItems, Point mousePos)
	{
		Dimension slotSize = Util.getSize(SideBar.SLOT_IMAGE) ;

		GamePanel.getDP().drawImage(IMAGE, BAR_POS, Align.bottomLeft) ;
		
		for (int i = 0 ; i <= Player.getHotKeys().length - 1 ; i += 1)
		{
			Point slotCenter = Util.translate(topLeft(), 13, 16 + 24 * i) ;
			Point keyTextPos = Util.translate(slotCenter, slotSize.width / 2 + 6, slotSize.height / 2) ;
			
			GamePanel.getDP().drawImage(GameWindow.getSlotImage(), slotCenter, Align.center) ;
			GamePanel.getDP().drawText(keyTextPos, Align.bottomLeft, Player.getHotKeys()[i], FONT, TEXT_COLOR) ;
			
			if (hotItems.get(i) == null) { continue ;}

			GamePanel.getDP().drawImage(hotItems.get(i).getImage(), slotCenter, Align.center) ;
			
			if (!Util.isInside(mousePos, Util.translate(slotCenter, -slotSize.width / 2, -slotSize.height / 2), slotSize)) { continue ;}
			
			Point textPos = Util.translate(slotCenter, - slotSize.width / 2 - 10, 0);
			GamePanel.getDP().drawText(textPos, Align.centerRight, hotItems.get(i).getName(), FONT, TEXT_COLOR) ;
		}
	}
}