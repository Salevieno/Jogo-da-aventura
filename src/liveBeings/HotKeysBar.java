package liveBeings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;

import graphics.Draw;
import graphics.DrawPrimitives;
import items.Item;
import main.Game;
import utilities.Align;
import utilities.UtilG;
import utilities.UtilS;
import windows.BagWindow;

public class HotKeysBar
{
	private static final Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
	private static final Color textColor = Game.colorPalette[19] ;
	
	public static final Image slotImage = UtilS.loadImage("\\SideBar\\" + "Slot.png") ;

	public static void display(Item[] hotItems, Point mousePos, DrawPrimitives DP)
	{
		Point barTopLeft = new Point(Game.getScreen().getSize().width + 1, Game.getScreen().getSize().height - 70) ;
		Dimension slotSize = new Dimension(slotImage.getWidth(null), slotImage.getHeight(null)) ;
		
//		DP.drawGradRoundRect(barTopLeft, Align.topLeft, new Dimension(36, 60), 1, Game.colorPalette[3], Game.colorPalette[3], true) ;
		
		for (int i = 0 ; i <= Player.HotKeys.length - 1 ; i += 1)
		{
			Point slotCenter = UtilG.Translate(barTopLeft, 10, 10 + 20 * i) ;
			Point keyTextPos = UtilG.Translate(slotCenter, slotSize.width / 2 + 5, slotSize.height / 2) ;
			
			DP.drawImage(BagWindow.SlotImage, slotCenter, Align.center) ;
			DP.drawText(keyTextPos, Align.bottomLeft, Draw.stdAngle, Player.HotKeys[i], font, textColor) ;
			
			if (hotItems[i] == null) { continue ;}

			DP.drawImage(hotItems[i].getImage(), slotCenter, Align.center) ;
			
			if (!UtilG.isInside(mousePos, UtilG.Translate(slotCenter, -slotSize.width / 2, -slotSize.height / 2), slotSize)) { continue ;}
			
			Point textPos = UtilG.Translate(slotCenter, - slotSize.width / 2 - 10, 0);
			DP.drawText(textPos, Align.centerRight, Draw.stdAngle, hotItems[i].getName(), font, textColor) ;
		}
	}
}