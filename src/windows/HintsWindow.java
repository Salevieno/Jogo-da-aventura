package windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;

import graphics.Align;
import graphics.Scale;
import graphics2.Draw;
import main.Game;
import main.GamePanel;
import main.Path;
import main.TextCategories;
import utilities.Util;


public class HintsWindow extends GameWindow
{
	private static final Point windowPos ;
	private static final Font font ;
	private static final Image image ;
	
	static
	{
		windowPos = Game.getScreen().pos(0.15, 0.4) ;
		font = new Font(Game.MainFontName, Font.BOLD, 12) ;
		image = Game.loadImage(Path.WINDOWS_IMG + "Hints.png") ;
	}
	
	public HintsWindow()
	{
		super("Dicas", windowPos, image, 0, 0, 0, 0) ;
	}
	
	public void navigate(String action)
	{
		stdNavigation(action);
	}
	
	public void display(Point mousePos)
	{
		String[] text = Game.allText.get(TextCategories.hints) ;
		Point textPos = new Point(windowPos.x + 15, windowPos.y + 10) ;
		Color textColor = Game.palette[0] ;
		double angle = Draw.stdAngle ;
		int sy = font.getSize() + 2 ;
		numberWindows = text.length - 6 ;
		
		GamePanel.DP.drawImage(image, windowPos, 0, Scale.unit, Align.topLeft, stdOpacity) ;
		
		GamePanel.DP.drawText(Util.translate(windowPos, size.width / 2, 20), Align.center, angle, text[0], font, textColor) ;
		GamePanel.DP.drawText(Util.translate(textPos, 10, size.height - 35), Align.topLeft, angle, text[1], font, textColor) ;
		GamePanel.DP.drawText(Util.translate(textPos, (int)(0.9 * size.width), size.height - 35), Align.topRight, angle, text[2], font, textColor) ;
		GamePanel.DP.drawText(Util.translate(textPos, size.width / 2, size.height - 40), Align.center, angle, text[3], font, textColor) ;
		Draw.fitText(Util.translate(textPos, 0, 30), sy, Align.topLeft, text[window + 4], font, 70, textColor) ;
		
		Draw.windowArrows(Util.translate(windowPos, 0, size.height + 10), size.width, window, numberWindows - 1, stdOpacity) ;
	}
}
