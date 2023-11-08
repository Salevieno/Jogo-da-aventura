package windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

import graphics.DrawingOnPanel;
import liveBeings.Player;
import main.Game;
import main.TextCategories;
import utilities.Align;
import utilities.UtilG;
import utilities.UtilS;

public class HintsWindow extends GameWindow
{
	public HintsWindow()
	{
		super("Dicas", UtilS.loadImage("\\Windows\\" + "Hints.png"), 0, 0, 0, 0) ;
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
	
	public void display(Point mousePos, DrawingOnPanel DP)
	{
		String[] text = Game.allText.get(TextCategories.hints) ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 12) ;
		Point windowPos = Game.getScreen().pos(0.1, 0.4) ;
		Point textPos = new Point(windowPos.x + 15, windowPos.y + 10) ;
		Color textColor = Game.colorPalette[5] ;
		double angle = DrawingOnPanel.stdAngle ;
		int sy = font.getSize() + 2 ;
		numberWindows = text.length - 6 ;
		
		DP.DrawImage(image, windowPos, Align.topLeft) ;
		
		DP.DrawText(UtilG.Translate(windowPos, size.width / 2, 20), Align.center, angle, text[0], font, textColor) ;
		DP.DrawText(UtilG.Translate(textPos, 10, size.height - 35), Align.topLeft, angle, text[1], font, textColor) ;
		DP.DrawText(UtilG.Translate(textPos, (int)(0.9 * size.width), size.height - 35), Align.topRight, angle, text[2], font, textColor) ;
		DP.DrawText(UtilG.Translate(textPos, size.width / 2, size.height - 40), Align.center, angle, text[3], font, textColor) ;
		DP.DrawFitText(UtilG.Translate(textPos, 0, 30), sy, Align.topLeft, text[window + 4], font, 70, textColor) ;
		
		DP.DrawWindowArrows(UtilG.Translate(windowPos, 0, size.height + 10), size.width, window, numberWindows - 1) ;
	}
}
