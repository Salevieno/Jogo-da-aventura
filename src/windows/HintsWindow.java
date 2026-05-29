package windows;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;

import graphics.Align;
import graphics.Scale;
import graphics2.Draw;
import main.Game;
import main.GamePanel;
import main.ImageLoader;
import main.Palette;
import main.Path;
import main.TextCategories;
import screen.Screen;
import utilities.Util;


public class HintsWindow extends GameWindow
{
	private static final Image IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "Hints.png") ;

	public HintsWindow()
	{
		super("Dicas", Screen.getMe().pos(0.15, 0.4), IMAGE, 0, 0, 0, 0) ;
	}
	
	public void navigate(String action)
	{
		stdNavigation(action);
	}
	
	public void display(Point mousePos)
	{
		String[] text = Game.getAllText().get(TextCategories.hints) ;
		Point textPos = new Point(topLeftPos.x + 15, topLeftPos.y + 10) ;
		Color textColor = Palette.colors[0] ;
		double angle = Draw.stdAngle ;
		int sy = SUBTITLE_FONT.getSize() + 2 ;
		numberWindows = text.length - 6 ;
		
		GamePanel.getDP().drawImage(image, topLeftPos, 0, Scale.unit, Align.topLeft, stdOpacity) ;
		
		GamePanel.getDP().drawText(Util.translate(topLeftPos, size.width / 2, 20), Align.center, angle, text[0], SUBTITLE_FONT, textColor) ;
		GamePanel.getDP().drawText(Util.translate(textPos, 10, size.height - 35), Align.topLeft, angle, text[1], SUBTITLE_FONT, textColor) ;
		GamePanel.getDP().drawText(Util.translate(textPos, (int)(0.9 * size.width), size.height - 35), Align.topRight, angle, text[2], SUBTITLE_FONT, textColor) ;
		GamePanel.getDP().drawText(Util.translate(textPos, size.width / 2, size.height - 40), Align.center, angle, text[3], SUBTITLE_FONT, textColor) ;
		Draw.fitText(Util.translate(textPos, 0, 30), sy, Align.topLeft, text[window + 4], SUBTITLE_FONT, 70, textColor) ;
		
		Draw.windowArrows(Util.translate(topLeftPos, 0, size.height + 10), size.width, window, numberWindows - 1, stdOpacity) ;
	}
}
