package liveBeings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;

import graphics.DrawingOnPanel;
import main.Game;
import utilities.Align;
import utilities.UtilG;

public class HotKeysBar
{	
	public static void display(Point mousePos, DrawingOnPanel DP)
	{
		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		Color textColor = Game.ColorPalette[19] ;
		DP.DrawRoundRect(new Point(Game.getScreen().getSize().width + 1, Game.getScreen().getSize().height - 70), Align.topLeft, new Dimension(36, 60), 1, Game.ColorPalette[7], Game.ColorPalette[19], true) ;
		for (int i = 0 ; i <= Player.HotKeys.length - 1 ; i += 1)
		{
			Point slotCenter = new Point(Game.getScreen().getSize().width + 10, Game.getScreen().getSize().height - 60 + 20 * i) ;
			Dimension slotSize = new Dimension(Game.slotImage.getWidth(null), Game.slotImage.getHeight(null)) ;
			DP.DrawImage(Game.slotImage, slotCenter, Align.center) ;
			DP.DrawText(new Point(slotCenter.x + slotSize.width / 2 + 5, slotCenter.y + slotSize.height / 2), Align.bottomLeft, DrawingOnPanel.stdAngle, Player.HotKeys[i], font, textColor) ;
			if (Player.HotKeys[i] != null)
			{
				DP.DrawImage(Game.slotImage, slotCenter, Align.center) ;
				if (UtilG.isInside(mousePos, slotCenter, slotSize))
				{
					DP.DrawText(new Point(slotCenter.x - slotSize.width - 10, slotCenter.y), Align.centerRight, DrawingOnPanel.stdAngle, Player.HotKeys[i], font, textColor) ;
				}
			}
		}
	}
}