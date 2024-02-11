package windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;

import graphics.Draw;
import graphics.DrawPrimitives;
import main.Game;
import main.TextCategories;
import utilities.Align;
import utilities.UtilG;
import utilities.UtilS;

public class HintsWindow extends GameWindow
{
	private static final Point windowPos ;
	private static final Font font ;
	private static final Image image ;
	
	static
	{
		windowPos = Game.getScreen().pos(0.15, 0.4) ;
		font = new Font(Game.MainFontName, Font.BOLD, 12) ;
		image = UtilS.loadImage("\\Windows\\" + "Hints.png") ;
	}
	
	public HintsWindow()
	{
		super("Dicas", windowPos, image, 0, 0, 0, 0) ;
	}
	
	public void navigate(String action)
	{
		stdNavigation(action);
	}
	
	public void display(Point mousePos, DrawPrimitives DP)
	{
		String[] text = Game.allText.get(TextCategories.hints) ;
		Point textPos = new Point(windowPos.x + 15, windowPos.y + 10) ;
		Color textColor = Game.colorPalette[0] ;
		double angle = Draw.stdAngle ;
		int sy = font.getSize() + 2 ;
		numberWindows = text.length - 6 ;
		
		DP.drawImage(image, windowPos, Align.topLeft) ;
		
		DP.drawText(UtilG.Translate(windowPos, size.width / 2, 20), Align.center, angle, text[0], font, textColor) ;
		DP.drawText(UtilG.Translate(textPos, 10, size.height - 35), Align.topLeft, angle, text[1], font, textColor) ;
		DP.drawText(UtilG.Translate(textPos, (int)(0.9 * size.width), size.height - 35), Align.topRight, angle, text[2], font, textColor) ;
		DP.drawText(UtilG.Translate(textPos, size.width / 2, size.height - 40), Align.center, angle, text[3], font, textColor) ;
		Draw.fitText(UtilG.Translate(textPos, 0, 30), sy, Align.topLeft, text[window + 4], font, 70, textColor) ;
		
		Draw.windowArrows(UtilG.Translate(windowPos, 0, size.height + 10), size.width, window, numberWindows - 1) ;
	}
}
