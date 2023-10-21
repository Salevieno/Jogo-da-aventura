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
	
	public void displayTypingField(Point pos, DrawingOnPanel DP)
	{
//		DP.DrawImage(Player.CoinIcon, UtilG.Translate(pos, 10, 10), Align.centerLeft) ;
//		DP.DrawText(UtilG.Translate(pos, 15, 10), Align.centerLeft, DrawingOnPanel.stdAngle, text, DrawingOnPanel.stdFont, Game.colorPalette[21]) ;		
	
//		Point pos = UtilG.Translate(windowPos, 50, size.height / 3) ;
//		DP.DrawText(UtilG.Translate(pos, 0, -30), Align.centerLeft, DrawingOnPanel.stdAngle, text, stdFont, Game.colorPalette[21]) ;
		DP.DrawRoundRect(pos, Align.centerLeft, new Dimension(150, 20), 1, Game.colorPalette[3], Game.colorPalette[3], true) ;
		DP.DrawLine(UtilG.Translate(pos, 20, 5), UtilG.Translate(pos, 20, -5), 2, Game.colorPalette[0]) ;
		DP.DrawImage(Player.CoinIcon, UtilG.Translate(pos, 5, 0), Align.centerLeft) ;
		DP.DrawText(UtilG.Translate(pos, 20, 0), Align.centerLeft, DrawingOnPanel.stdAngle, text, font, Game.colorPalette[0]) ;
	
	}
}
