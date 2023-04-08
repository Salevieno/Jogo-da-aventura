package liveBeings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;

import graphics.DrawingOnPanel;
import items.Item;
import main.Game;
import utilities.Align;
import utilities.UtilG;
import windows.BagWindow;

public class HotKeysBar
{
	
	public static final Image slotImage = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "BagSlot.png") ;

	public static void display(Item[] hotItems, Point mousePos, DrawingOnPanel DP)
	{
		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		Color textColor = Game.ColorPalette[19] ;
		Point barTopLeft = new Point(Game.getScreen().getSize().width + 1, Game.getScreen().getSize().height - 70) ;
		
		DP.DrawRoundRect(barTopLeft, Align.topLeft, new Dimension(36, 60), 1, Game.ColorPalette[7], Game.ColorPalette[19], true) ;
		
		for (int i = 0 ; i <= Player.HotKeys.length - 1 ; i += 1)
		{
			Point slotCenter = new Point(Game.getScreen().getSize().width + 10, Game.getScreen().getSize().height - 60 + 20 * i) ;
			Dimension slotSize = new Dimension(slotImage.getWidth(null), slotImage.getHeight(null)) ;
			Point keyTextPos = new Point(slotCenter.x + slotSize.width / 2 + 5, slotCenter.y + slotSize.height / 2) ;
			
			DP.DrawImage(BagWindow.SlotImage, slotCenter, Align.center) ;
			DP.DrawText(keyTextPos, Align.bottomLeft, DrawingOnPanel.stdAngle, Player.HotKeys[i], font, textColor) ;
			
			if (hotItems[i] == null) { continue ;}

			DP.DrawImage(hotItems[i].getImage(), slotCenter, Align.center) ;
			
			if (!UtilG.isInside(mousePos, UtilG.Translate(slotCenter, -slotSize.width / 2, -slotSize.height / 2), slotSize)) { continue ;}
			
			DP.DrawText(UtilG.Translate(slotCenter, - slotSize.width / 2 - 10, 0), Align.centerRight, DrawingOnPanel.stdAngle, hotItems[i].getName(), font, textColor) ;
		}
	}
}