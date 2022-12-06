package Windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

import javax.swing.ImageIcon;

import Graphics.DrawFunctions;
import Graphics.DrawPrimitives;
import LiveBeings.Player;
import Main.Game;
import Utilities.Size;
import Utilities.UtilG;

public class HintsWindow extends Window
{
	public HintsWindow()
	{
		super(new ImageIcon(Game.ImagesPath + "Hints.png").getImage(), 0, 0, 0, 0) ;
	}
	
	public void navigate(String action)
	{
		if (action.equals(Player.ActionKeys[3]))
		{
			windowUp() ;
		}
		if (action.equals(Player.ActionKeys[1]))
		{
			windowDown() ;
		}
	}
	
	public void display(Player player, DrawFunctions DF)
	{
		DrawPrimitives DP = DF.getDrawPrimitives() ;
		String[] text = player.allText.get("* Menu de dicas *") ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 12) ;
		Point windowPos = new Point((int) (0.1 * Game.getScreen().getSize().x),
				(int) (0.4 * Game.getScreen().getSize().y)) ;
		Point textPos = new Point(windowPos.x + 15, windowPos.y + 10) ;
		Color textColor = Game.ColorPalette[5] ;
		double angle = DrawPrimitives.OverallAngle ;
		int sy = font.getSize() + 2 ;
		numberWindows = text.length - 6 ;
		
		DP.DrawImage(image, windowPos, "TopLeft") ;
		
		DP.DrawText(UtilG.Translate(windowPos, size.x / 2, 20), "Center", angle, text[1], font, textColor) ;
		DP.DrawText(UtilG.Translate(textPos, 10, size.y - 35), "TopLeft", angle, text[2], font, textColor) ;
		DP.DrawText(UtilG.Translate(textPos, (int)(0.9 * size.x), size.y - 35), "TopRight", angle, text[3], font, textColor) ;
		DP.DrawText(UtilG.Translate(textPos, size.x / 2, size.y - 40), "Center", angle, text[4], font, textColor) ;
		DP.DrawFitText(UtilG.Translate(textPos, 0, 30), sy, "TopLeft", text[window + 5], font, 70, textColor) ;
		
		DF.DrawWindowArrows(UtilG.Translate(windowPos, size.x / 2, size.y - 23), (int) (0.92 * size.x), window, numberWindows - 1) ;
	}
}
