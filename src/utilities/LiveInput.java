package utilities;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;

import graphics.DrawingOnPanel;
import liveBeings.Player;
import main.Game;

public class LiveInput
{
	private String text = "" ;
	private Font font = new Font(Game.MainFontName, Font.BOLD, 10) ;
	
	public String getText() { return text ;}
	
	public void addChar(char newChar) { text += newChar ;}
	
	public void removeChar(char newChar) { text = text.substring(0, text.length() - 1) ;}
	
	public void clearText() { text = "" ;}
	
	public void receiveInput(String action)
	{
		if (action.equals("Enter")) { return ;}

		Character newChar = action.toCharArray()[0] ;
		if (action.equals("Backspace"))
		{
			if (!text.isEmpty())
			{
				removeChar(newChar) ;
			}
			return ;
		}
		
		addChar(newChar) ;
	}
	
	public void displayTypingField(Point pos, boolean showBackground, DrawingOnPanel DP)
	{

		if (showBackground)
		{
			DP.DrawRoundRect(pos, Align.centerLeft, new Dimension(150, 20), 1, Game.colorPalette[3], Game.colorPalette[3], true) ;
		}
		int offsetX = (int) (7.3 * text.length()) ;
		DP.DrawLine(UtilG.Translate(pos, 20 + offsetX, 5), UtilG.Translate(pos, 20 + offsetX, -5), 2, Game.colorPalette[0]) ;
		DP.DrawText(UtilG.Translate(pos, 20, 0), Align.centerLeft, DrawingOnPanel.stdAngle, text, font, Game.colorPalette[0]) ;
	
	}
}
