package liveBeings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.List;

import graphics.Align;
import graphics2.Draw;
import items.Item;
import main.Game;
import main.GamePanel;
import main.Path;
import screen.SideBar;
import utilities.Util;

import windows.BagWindow;

public abstract class HotKeysBar
{
	private static final Font font ;
	private static final Color textColor  ;
	private static final Image image ;
	private static final Point barPos ;
	
	static
	{
		font = new Font(Game.MainFontName, Font.BOLD, 12) ;
		textColor = Game.palette[0] ;
		image = Game.loadImage(Path.SIDEBAR_IMG + "HotBar.png") ;
		barPos = new Point(Game.getScreen().mapSize().width + 2, Game.getScreen().getSize().height - SideBar.sy) ;
	}
	
	public static Dimension size() { return Util.getSize(image) ;}
	public static Point topLeft() { return new Point(barPos.x, barPos.y - size().height) ;}

	public static int slotHovered(Point mousePos)
	{
		Dimension slotSize = Util.getSize(SideBar.slotImage) ;
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
		Dimension slotSize = Util.getSize(SideBar.slotImage) ;

		GamePanel.DP.drawImage(image, barPos, Align.bottomLeft) ;
		
		for (int i = 0 ; i <= Player.getHotKeys().length - 1 ; i += 1)
		{
			Point slotCenter = Util.translate(topLeft(), 13, 10 + 20 * i) ;
			Point keyTextPos = Util.translate(slotCenter, slotSize.width / 2 + 6, slotSize.height / 2) ;
			
			GamePanel.DP.drawImage(BagWindow.slotImage, slotCenter, Align.center) ;
			GamePanel.DP.drawText(keyTextPos, Align.bottomLeft, Draw.stdAngle, Player.getHotKeys()[i], font, textColor) ;
			
			if (hotItems.get(i) == null) { continue ;}

			GamePanel.DP.drawImage(hotItems.get(i).getImage(), slotCenter, Align.center) ;
			
			if (!Util.isInside(mousePos, Util.translate(slotCenter, -slotSize.width / 2, -slotSize.height / 2), slotSize)) { continue ;}
			
			Point textPos = Util.translate(slotCenter, - slotSize.width / 2 - 10, 0);
			GamePanel.DP.drawText(textPos, Align.centerRight, Draw.stdAngle, hotItems.get(i).getName(), font, textColor) ;
		}
	}
}