package liveBeings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;

import graphics.Align;
import graphics2.Draw;
import items.Item;
import main.Game;
import screen.SideBar;
import utilities.Util;
import utilities.UtilS;
import windows.BagWindow;

public class HotKeysBar
{
	private static final Font font ;
	private static final Color textColor  ;
	private static final Image image ;
	private static final Point barPos ;
	
	static
	{
		font = new Font(Game.MainFontName, Font.BOLD, 12) ;
		textColor = Game.palette[0] ;
		image = UtilS.loadImage("\\SideBar\\" + "HotBar.png") ;
		barPos = new Point(Game.getScreen().getSize().width + 2, Game.getScreen().getSize().height - 5) ;
	}

	public static int slotHovered(Point mousePos)
	{
		Point barTopLeft = new Point(Game.getScreen().getSize().width + 1, Game.getScreen().getSize().height - 70) ;
		Dimension slotSize = Util.getSize(SideBar.slotImage) ;
		for (int i = 0 ; i <= Player.HotKeys.length - 1 ; i += 1)
		{
			Point slotCenter = Util.Translate(barTopLeft, 10, 10 + 20 * i) ;
			if (Util.isInside(mousePos, Util.Translate(slotCenter, -slotSize.width / 2, -slotSize.height / 2), slotSize))
			{
				return i ;
			}
		}
		
		return -1 ;
	}
	
	public static void display(Item[] hotItems, Point mousePos)
	{
		Point barTopLeft = new Point(Game.getScreen().getSize().width + 1, Game.getScreen().getSize().height - 70) ;
		Dimension slotSize = Util.getSize(SideBar.slotImage) ;

		Game.DP.drawImage(image, barPos, Align.bottomLeft) ;
		
		for (int i = 0 ; i <= Player.HotKeys.length - 1 ; i += 1)
		{
			Point slotCenter = Util.Translate(barTopLeft, 13, 10 + 20 * i) ;
			Point keyTextPos = Util.Translate(slotCenter, slotSize.width / 2 + 6, slotSize.height / 2) ;
			
			Game.DP.drawImage(BagWindow.slotImage, slotCenter, Align.center) ;
			Game.DP.drawText(keyTextPos, Align.bottomLeft, Draw.stdAngle, Player.HotKeys[i], font, textColor) ;
			
			if (hotItems[i] == null) { continue ;}

			Game.DP.drawImage(hotItems[i].getImage(), slotCenter, Align.center) ;
			
			if (!Util.isInside(mousePos, Util.Translate(slotCenter, -slotSize.width / 2, -slotSize.height / 2), slotSize)) { continue ;}
			
			Point textPos = Util.Translate(slotCenter, - slotSize.width / 2 - 10, 0);
			Game.DP.drawText(textPos, Align.centerRight, Draw.stdAngle, hotItems[i].getName(), font, textColor) ;
		}
	}
}