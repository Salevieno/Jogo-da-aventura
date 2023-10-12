package utilities;

import java.awt.Point;

import graphics.DrawingOnPanel;
import liveBeings.Player;
import main.Game;

public abstract class Typing
{
	private static int TypingCounter = 0 ;
	private static String[] TypingText = new String[1] ;
	private static String TypedText = "" ;
	
	public static String[] RemoveString(String[] input)
	{
		String[] result = new String[input.length - 1] ;
		for (int i = 0 ; i <= input.length - 2 ; ++i)
		{
			result[i] = input[i] ;
		}
		return result ;
	}
	
	public static String[] AddString(String[] input, String NewMember)
	{
		String[] result = new String[input.length + 1] ;
		for (int i = 0 ; i <= input.length - 1 ; ++i)
		{
			result[i] = input[i] ;
		}
		result[input.length] = NewMember ;
		return result ;
	}
	
	public static String LiveTyping(Point Pos, double angle, String Input)
	{		
		Character input = Input.toCharArray()[0] ;
		if (Character.isLetterOrDigit(input))
		{
			TypingText = AddString(TypingText, Input) ;	
			++TypingCounter ;
			if (2 <= TypingCounter)
			{
				TypedText += TypingText[TypingCounter] ;	
			}
		}
		else if (Input.equals("Backspace") & 0 < TypedText.length())
		{			
			--TypingCounter ;
			TypingText = RemoveString(TypingText) ;	
			TypedText = TypedText.substring(0, TypedText.length() - 1) ;
		}
		else if (Input.equals("Enter"))
		{
			TypedText = "" ;
		}
		return TypedText ;
	}
	
	public static void displayTypingField(Point pos, DrawingOnPanel DP)
	{
		DP.DrawImage(Player.CoinIcon, UtilG.Translate(pos, 10, 10), Align.centerLeft) ;
		DP.DrawText(UtilG.Translate(pos, 15, 10), Align.centerLeft, DrawingOnPanel.stdAngle, TypedText, DrawingOnPanel.stdFont, Game.colorPalette[9]) ;
		
	}
}
