package utilities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

import graphics.DrawingOnPanel;

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
	
	public static String LiveTyping(Point Pos, double angle, String Input, Font font, Color color, DrawingOnPanel DP)
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
//		if (TypedText != null)
//		{
//			DP.DrawText(Pos, Align.bottomLeft, angle, TypedText.toString(), font, color) ;									
//		}	
		return TypedText ;
	}
	
}
