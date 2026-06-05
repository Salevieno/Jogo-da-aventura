package graphics2 ;

import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Font ;
import java.awt.Image ;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import graphics.Align;
import graphics.DrawPrimitives;
import graphics.Scale;
import graphics.UtilAlignment;
import main.GamePanel;
import main.ImageLoader;
import main.Palette;
import main.Path;
import screen.Screen;
import utilities.Util;

public abstract class Draw 
{
	private static final Image KEYBOARD_BTN_IMAGE = ImageLoader.loadImage(Path.UI_IMG + "KeyboardButton.png") ;

	public static void bufferedText(Point pos, Align align, double angle, String text, Font font, Color color, Color outlineColor, int outlineWidth)
	{
		GamePanel.getDP().drawBufferedText(pos, align, angle, text, font, color, outlineColor, outlineWidth) ;
	}
	public static void bufferedText(Point pos, Align align, double angle, String text, Font font, Color color)
	{
		bufferedText(pos, align, angle, text, font, color, Palette.colors[3], 1) ;
	}
	public static void bufferedText(Point pos, Align align, String text, Font font, Color color)
	{
		bufferedText(pos, align, text, font, color) ;
	}

	private static List<String> textFitted(String text, Font font, int maxLength)
	{
		List<String> words = Arrays.asList(text.split(" ")) ;
		List<String> lines = new ArrayList<>() ;
		lines.add("") ;
		for (String word : words)
		{
			String lastLine = lines.get(lines.size() - 1) ;
			String newLineText = lastLine + word + " " ;
			if (maxLength <= GamePanel.calcTextSize(newLineText, font).width)
			{
				lines.add("") ;
				lines.set(lines.size() - 1, word + " ") ;
				continue ;
			}
			lines.set(lines.size() - 1, newLineText) ;
		}
		return lines ;
	}

	public static void fitText(Point pos, int sy, Align align, String text, Font font, int maxLength, Color color)
	{
		List<String> lines = textFitted(text, font, maxLength) ;
		for (int i = 0 ; i <= lines.size() - 1 ; i += 1)
		{
			GamePanel.getDP().drawText(new Point(pos.x, pos.y + i*sy), align, DrawPrimitives.stdAngle, lines.get(i), font, color) ;						
		}
	}
	
	public static void textUntil(Point pos, Align align, double angle, String text, Font font, Color color, int maxLength, Point mousePos)
	{
		Point offset = UtilAlignment.offsetForAlignment(align, new Dimension(maxLength, GamePanel.calcTextSize(text, font).height)) ;
		int minlength = 3 ;	// 3 is the length of "..."
		String shortText = text ;
		maxLength = Math.max(maxLength, minlength) ;
		if (maxLength < text.length())
		{
			char[] chararray = new char[maxLength - minlength] ;
			text.getChars(0, maxLength - 4, chararray, 0) ;
			shortText = String.valueOf(chararray) ;
		}
		
		Point topLeftPos = Util.translate(pos, offset.x, offset.y) ;
		Dimension size = new Dimension(GamePanel.calcTextSize(shortText, font).width, GamePanel.calcTextSize(text, font).height) ;
		String textDrawn =  text.length() <= maxLength | Util.isInside(mousePos, topLeftPos, size) ? text : shortText + "..." ;
		GamePanel.getDP().drawText(pos, align, DrawPrimitives.stdAngle, textDrawn, font, color) ;
	}
	
	public static void textUntil(Point pos, Align align, String text, Font font, Color color, int maxLength, Point mousePos)
	{
		textUntil(pos, align, DrawPrimitives.stdAngle, text, font, color, maxLength, mousePos) ;
	}
	
	public static void keyboardButton(Point pos, String key, Font font)
	{
		keyboardButton(pos, key, font, Palette.colors[0]) ;
	}
	
	public static void keyboardButton(Point pos, String key, Font font, Color color)
	{
		GamePanel.getDP().drawImage(Draw.KEYBOARD_BTN_IMAGE, pos, Scale.unit, Align.center) ;
		GamePanel.getDP().drawText(pos, Align.center, key, font, color) ;	
	}
	
	public static void gameGrid(int[] spacing)
	{
		for (int i = 0 ; i <= Screen.getMe().getSize().width/spacing[0] - 1 ; ++i)
		{
			int LineThickness = 1 ;
			Color color = Palette.colors[21] ;
			if (i % 10 == 0)
			{
				LineThickness = 2 ;
			}
			if (i % 20 == 0)
			{
				LineThickness = 2 ;
				color = Palette.colors[5] ;
			}
			GamePanel.getDP().drawLine(new Point(i*spacing[0], 0), new Point(i*spacing[0], Screen.getMe().getSize().height), LineThickness, color) ;
			for (int j = 0 ; j <= Screen.getMe().getSize().height/spacing[1] - 1 ; ++j)
			{
				LineThickness = 1 ;
				color = Palette.colors[21] ;
				if (j % 10 == 0)
				{
					LineThickness = 2 ;
				}
				if (j % 20 == 0)
				{
					LineThickness = 2 ;
					color = Palette.colors[5] ;
				}
				GamePanel.getDP().drawLine(new Point(0, j*spacing[1]), new Point(Screen.getMe().getSize().width, j*spacing[1]), LineThickness, color) ;
			}							
		}
	}
	
	public static void keyboardKey(Point pos, String key, Font font, Color color)
	{
		GamePanel.getDP().drawRoundRect(pos, Align.center, new Dimension(12, 12), 1, Palette.colors[3], Palette.colors[0], true, 2, 2) ;
		GamePanel.getDP().drawText(pos, Align.center, DrawPrimitives.stdAngle, key, font, color) ;
	}

	public static void settingBars(Point pos, Align align, int height, int qtdFilledBars, int totalBars, boolean increasingHeights, Color fillColor)
	{
		int barWidth = 10 ;
		int barSpacing = 5 ;
		int maxBarHeight = height ;
		Point bottomLeftBar = new Point(pos) ;
		
		for (int i = 0 ; i <= totalBars - 1 ; i += 1)
		{
			int barHeight = increasingHeights ? (int) ((i + 1) * maxBarHeight / totalBars) : maxBarHeight ;
			Color color = i <= qtdFilledBars ? fillColor : null ;
			GamePanel.getDP().drawRoundRect(bottomLeftBar, align, new Dimension(barWidth, barHeight), 1, color, Palette.colors[3], true) ;
			bottomLeftBar.x += barWidth + barSpacing ;
		}
	}

	public static void settingBars(Point pos, Align align, int height, int qtdFilledBars, int totalBars)
	{
		settingBars(pos, align, height, qtdFilledBars, totalBars, false, Palette.colors[18]) ;
	}	

	public static void settingSwitch(Point pos, Align align, boolean isOn, Color fillColor)
	{
		int width = 60 ;
		int height = 30 ;
		int borderThickness = 1 ;
		Color borderColor = Palette.colors[0] ;
		Color backgroundColor = isOn ? fillColor : null ;
		
		GamePanel.getDP().drawRoundRect(pos, align, new Dimension(width, height), borderThickness, backgroundColor, borderColor, true, 25, 25) ;
		
		Point centerPos = UtilAlignment.getPosAt(pos, align, Align.center, new Dimension(width, height)) ;
		Point circleCenter = Util.translate(centerPos, isOn ? width / 4 : -width / 4, 0) ;
		GamePanel.getDP().drawCircle(circleCenter, height / 2 - borderThickness, borderColor) ;
	}
	
	public static void settingSwitch(Point pos, Align align, boolean isOn)
	{
		settingSwitch(pos, align, isOn, Palette.colors[3]) ;
	}

	public static void filter(Color color)
	{
		for (int i = 0 ; i <= Screen.getMe().getSize().height - 1 ; i += 2)
		{
			GamePanel.getDP().drawLine(new Point(0, i), new Point(Screen.getMe().getSize().width, i), color) ;
		}
	}
}