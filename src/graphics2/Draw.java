package graphics2 ;

import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Font ;
import java.awt.Image ;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import components.Hitbox;
import graphics.Align;
import graphics.DrawPrimitives;
import graphics.Scale;
import graphics.UtilAlignment;
import liveBeings.PlayerActions;
import main.Game;
import main.GamePanel;
import main.ImageLoader;
import main.Palette;
import main.Path;
import maps.FieldMap;
import maps.GameMap;
import screen.Screen;
import utilities.Util;

public abstract class Draw 
{
	private static final Image ARROW_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "ArrowIcon.png") ;
	private static final Image KEYBOARD_BTN_IMAGE = ImageLoader.loadImage(Path.UI_IMG + "KeyboardButton.png") ;
	private static final List<Image> TEXT_SELECTION_IMAGES ;
	public static double stdAngle = DrawPrimitives.stdAngle;
	
	static
	{
		TEXT_SELECTION_IMAGES = Arrays.asList(ImageLoader.loadImage(Path.UI_IMG + "TextSelectionTopLeft.png"),
										ImageLoader.loadImage(Path.UI_IMG + "TextSelectionTopRight.png"),
										ImageLoader.loadImage(Path.UI_IMG + "TextSelectionBottomRight.png"),
										ImageLoader.loadImage(Path.UI_IMG + "TextSelectionBottomLeft.png"))
									.stream()
									.filter(Objects::nonNull)
									.collect(Collectors.toList()) ;
	}

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
		bufferedText(pos, align, Draw.stdAngle, text, font, color) ;
	}

	public static void textSelection(Point center, Dimension size)
	{
		GamePanel.getDP().drawImage(TEXT_SELECTION_IMAGES.get(0), Util.translate(center, -size.width / 2, -size.height / 2), Align.bottomRight) ;
		GamePanel.getDP().drawImage(TEXT_SELECTION_IMAGES.get(1), Util.translate(center, size.width / 2, -size.height / 2), Align.bottomLeft) ;
		GamePanel.getDP().drawImage(TEXT_SELECTION_IMAGES.get(2), Util.translate(center, size.width / 2, size.height / 2), Align.topLeft) ;
		GamePanel.getDP().drawImage(TEXT_SELECTION_IMAGES.get(3), Util.translate(center, -size.width / 2, size.height / 2), Align.topRight) ;
	}
	
	
	public static void gif(Image gif, Point pos, Align align)
	{
		
		Dimension size = new Dimension(gif.getWidth(null), gif.getHeight(null)) ;
		Point offset = UtilAlignment.offsetForAlignment(align, size) ;
		GamePanel.getDP().drawImage(gif, Util.translate(pos, offset.x, offset.y), align);
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
			GamePanel.getDP().drawText(new Point(pos.x, pos.y + i*sy), align, stdAngle, lines.get(i), font, color) ;						
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
		GamePanel.getDP().drawText(pos, align, stdAngle, textDrawn, font, color) ;
	}
	
	public static void speech(Point pos, String text, Font font, Image speechBubbleImage, Color color)
	{
		// obs: text must end with . , ? or ! for this function to work
		int bubbleL = speechBubbleImage.getWidth(null) ;
		int bubbleH = speechBubbleImage.getHeight(null) ;
		boolean flipH = Screen.getMe().mapSize().width / 2 <= pos.x ;
		Color textColor = color != null ? color : Palette.colors[21] ;
		
		GamePanel.getDP().drawImage(speechBubbleImage, pos, DrawPrimitives.stdAngle, Scale.unit, flipH, false, Align.bottomCenter, 1) ;
		
		Point padding = new Point(6, 5) ;
		Point textPos = Util.translate(pos, padding.x - bubbleL / 2, padding.y - bubbleH) ;
		int maxTextL = speechBubbleImage.getWidth(null) - padding.x ;
		int sy = font.getSize() + 1 ;
		fitText(textPos, sy, Align.topLeft, text, font, maxTextL, textColor) ;
	}

	public static void keyboardButton(Point pos, String key, Font font)
	{
		keyboardButton(pos, key, font, Palette.colors[0]) ;
	}
	
	public static void keyboardButton(Point pos, String key, Font font, Color color)
	{
		GamePanel.getDP().drawImage(Draw.KEYBOARD_BTN_IMAGE, pos, Draw.stdAngle, Scale.unit, Align.center) ;
		GamePanel.getDP().drawText(pos, Align.center, Draw.stdAngle, key, font, color) ;	
	}
	
	public static void windowArrows(Point pos, int width, Font font, int selectedWindow, int numberWindows, double opacity)
	{
		if (0 < selectedWindow)
		{
			Point leftArrowPos = Util.translate(pos, 25, 0) ;
			Point textPos = Util.translate(leftArrowPos, 18, 0) ;
			GamePanel.getDP().drawImage(ARROW_ICON, leftArrowPos, stdAngle, new Scale(-1, -1), Align.center, opacity) ;
			Draw.keyboardButton(textPos, PlayerActions.moveLeft.getKey(), font) ;			
		}
		if (selectedWindow < numberWindows - 1)
		{
			Point rightArrowPos = Util.translate(pos, width - 25, 0) ;
			Point textPos = Util.translate(rightArrowPos, -18, 0) ;
			GamePanel.getDP().drawImage(ARROW_ICON, rightArrowPos, stdAngle, new Scale(1, -1), Align.center, opacity) ;
			Draw.keyboardButton(textPos, PlayerActions.moveRight.getKey(), font) ;	
		}
	}
	
	public static void loadingText(Image LoadingGif, Point Pos) { gif(LoadingGif, Pos, Align.center) ;}


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
	
	public static void menu(Point pos, Align align, Dimension size)
	{
		GamePanel.getDP().drawRoundRect(pos, align, size, 1, Palette.colors[3], Palette.colors[0], true);
	}
	
	public static void time(Font font)
	{
		float time = (float) Game.dayTimeRate() ;
		String message = (int) (24 * time) + ":" + (int) (24 * 60 * time % 60) ;
		GamePanel.getDP().drawText(Screen.getMe().pos(0, 0.99), Align.bottomLeft, stdAngle, message, font, Palette.colors[20]) ;
	}
	
	public static void mapElements(Hitbox playerHitbox, Point playerPos, GameMap map, Font font)
	{
		map.displayNPCs(playerHitbox) ;
		
		if (map instanceof FieldMap)
		{
			FieldMap fm = (FieldMap) map ;
			fm.displayCollectibles(playerHitbox) ;
		}
		map.displayTudoEstaBem();
		time(font) ;
	}

	public static void keyboardKey(Point pos, String key, Font font, Color color)
	{
		GamePanel.getDP().drawRoundRect(pos, Align.center, new Dimension(12, 12), 1, Palette.colors[3], Palette.colors[0], true, 2, 2) ;
		GamePanel.getDP().drawText(pos, Align.center, stdAngle, key, font, color) ;
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