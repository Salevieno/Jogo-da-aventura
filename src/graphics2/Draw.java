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

import UI.GameButton;
import components.Hitbox;
import graphics.Align;
import graphics.DrawPrimitives;
import graphics.Scale;
import graphics.UtilAlignment;
import liveBeings.Pet;
import liveBeings.Player;
import liveBeings.PlayerActions;
import main.Game;
import main.GamePanel;
import main.ImageLoader;
import main.Palette;
import main.Path;
import maps.FieldMap;
import maps.GameMap;
import utilities.Util;
import windows.PlayerAttributesWindow;

public abstract class Draw 
{

	private static final Dimension screenSize ;
	private static final Image ArrowIconImage ;
	private static final Image KeyboardButtonImage ;
	private static final List<Image> textSelectionImages ;
	
	private static final Font stdFont = DrawPrimitives.stdFont ;
	private static final Font smallFont = new Font(DrawPrimitives.stdFont.getName(), Font.BOLD, 11) ;
	private static final Font loadingGameScreenFont = new Font("SansSerif", Font.BOLD, 28) ;

	public static double stdAngle ;
	
	static
	{
		screenSize = Game.getScreen().getSize() ;
		stdAngle = DrawPrimitives.stdAngle;
		ArrowIconImage = ImageLoader.loadImage(Path.WINDOWS_IMG + "ArrowIcon.png") ;
		KeyboardButtonImage = ImageLoader.loadImage(Path.UI_IMG + "KeyboardButton.png") ;
		textSelectionImages = Arrays.asList(ImageLoader.loadImage(Path.UI_IMG + "TextSelectionTopLeft.png"),
										ImageLoader.loadImage(Path.UI_IMG + "TextSelectionTopRight.png"),
										ImageLoader.loadImage(Path.UI_IMG + "TextSelectionBottomRight.png"),
										ImageLoader.loadImage(Path.UI_IMG + "TextSelectionBottomLeft.png"))
									.stream()
									.filter(Objects::nonNull)
									.collect(Collectors.toList()) ;
	}

	public static void bufferedText(Point pos, Align align, double angle, String text, Font font, Color color, Color outlineColor, int outlineWidth)
	{
		GamePanel.DP.drawBufferedText(pos, align, angle, text, font, color, outlineColor, outlineWidth) ;
	}
	public static void bufferedText(Point pos, Align align, double angle, String text, Font font, Color color)
	{
		bufferedText(pos, align, angle, text, font, color, Palette.colors[3], 1) ;
	}
	public static void bufferedText(Point pos, Align align, String text, Font font, Color color)
	{
		bufferedText(pos, align, Draw.stdAngle, text, font, color) ;
	}
	public static void bufferedText(Point pos, Align align, String text, Color color)
	{
		bufferedText(pos, align, text, stdFont, color) ;
	}
	public static void textSelection(Point center, Dimension size)
	{
		GamePanel.DP.drawImage(textSelectionImages.get(0), Util.translate(center, -size.width / 2, -size.height / 2), Align.bottomRight) ;
		GamePanel.DP.drawImage(textSelectionImages.get(1), Util.translate(center, size.width / 2, -size.height / 2), Align.bottomLeft) ;
		GamePanel.DP.drawImage(textSelectionImages.get(2), Util.translate(center, size.width / 2, size.height / 2), Align.topLeft) ;
		GamePanel.DP.drawImage(textSelectionImages.get(3), Util.translate(center, -size.width / 2, size.height / 2), Align.topRight) ;
	}
	
	
	public static void gif(Image gif, Point pos, Align align)
	{
		
		Dimension size = new Dimension(gif.getWidth(null), gif.getHeight(null)) ;
		Point offset = UtilAlignment.offsetForAlignment(align, size) ;
		GamePanel.DP.drawImage(gif, Util.translate(pos, offset.x, offset.y), align);
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
			if (maxLength <= GamePanel.DP.textLength(newLineText, font))
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
			GamePanel.DP.drawText(new Point(pos.x, pos.y + i*sy), align, stdAngle, lines.get(i), font, color) ;						
		}
	}
	
	public static void textUntil(Point pos, Align align, double angle, String text, Font font, Color color, int maxLength, Point mousePos)
	{
		Point offset = UtilAlignment.offsetForAlignment(align, new Dimension(maxLength, GamePanel.DP.textHeight(font))) ;
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
		Dimension size = new Dimension(GamePanel.DP.textLength(shortText, font), GamePanel.DP.textHeight(font)) ;
		String textDrawn =  text.length() <= maxLength | Util.isInside(mousePos, topLeftPos, size) ? text : shortText + "..." ;
		GamePanel.DP.drawText(pos, align, stdAngle, textDrawn, font, color) ;
	}
	
	public static void speech(Point pos, String text, Font font, Image speechBubble, Color color)
	{
		// obs: text must end with . , ? or ! for this function to work
		int bubbleL = speechBubble.getWidth(null) ;
		int bubbleH = speechBubble.getHeight(null) ;
		boolean flipH = Game.getScreen().mapSize().width / 2 <= pos.x ;
		Color textColor = color != null ? color : Palette.colors[21] ;
		
		GamePanel.DP.drawImage(speechBubble, pos, DrawPrimitives.stdAngle, Scale.unit, flipH, false, Align.bottomCenter, 1) ;
		
		Point textOffset = new Point(6, 5) ;
		Point textPos = Util.translate(pos, textOffset.x - bubbleL / 2, textOffset.y - bubbleH) ;
		int maxTextL = 297 ;
		int sy = font.getSize() + 1 ;
		fitText(textPos, sy, Align.topLeft, text, font, maxTextL, textColor) ;
	}

	public static void keyboardButton(Point pos, String key)
	{
		keyboardButton(pos, key, Palette.colors[0]) ;
	}
	
	public static void keyboardButton(Point pos, String key, Color color)
	{
		GamePanel.DP.drawImage(Draw.KeyboardButtonImage, pos, Draw.stdAngle, Scale.unit, Align.center) ;
		GamePanel.DP.drawText(pos, Align.center, Draw.stdAngle, key, Draw.stdFont, color) ;	
	}
	
	public static void windowArrows(Point pos, int width, int selectedWindow, int numberWindows, double opacity)
	{
		if (0 < selectedWindow)
		{
			Point leftArrowPos = Util.translate(pos, 25, 0) ;
			Point textPos = Util.translate(leftArrowPos, 18, 0) ;
			GamePanel.DP.drawImage(ArrowIconImage, leftArrowPos, stdAngle, new Scale(-1, -1), Align.center, opacity) ;
			Draw.keyboardButton(textPos, PlayerActions.moveLeft.getKey()) ;			
		}
		if (selectedWindow < numberWindows - 1)
		{
			Point rightArrowPos = Util.translate(pos, width - 25, 0) ;
			Point textPos = Util.translate(rightArrowPos, -18, 0) ;
			GamePanel.DP.drawImage(ArrowIconImage, rightArrowPos, stdAngle, new Scale(1, -1), Align.center, opacity) ;
			Draw.keyboardButton(textPos, PlayerActions.moveRight.getKey()) ;	
		}
	}
	
	public static void loadingText(Image LoadingGif, Point Pos) { gif(LoadingGif, Pos, Align.center) ;}
	
	public static void loadingGameScreen(Player player, Pet pet, GameButton[] icons, int SlotID, int NumberOfUsedSlots, Image GoldCoinImage)
	{
		Point[] WindowPos = new Point[] {Game.getScreen().pos(0.15, 0.2), Game.getScreen().pos(0.65, 0.2), Game.getScreen().pos(0.5, 0.2)} ;
		
		GamePanel.DP.drawText(Game.getScreen().pos(0.5, 0.05), Align.center, stdAngle, "Slot " + (SlotID + 1), loadingGameScreenFont, Palette.colors[5]) ;
		((PlayerAttributesWindow) player.getAttWindow()).display(new Point(0, 0)) ;
		if (0 < pet.getLife().getCurrentValue())
		{
 			//pet.getAttWindow().display(pet, allText, null, null, NumberOfUsedSlots, null, null, null, null);
		}
 		if (ArrowIconImage == null) { return ;}
		
 		windowArrows(WindowPos[2], (int)(0.5*screenSize.width), SlotID, NumberOfUsedSlots - 1, 1.0) ;
	}

	public static void gameGrid(int[] spacing)
	{
		for (int i = 0 ; i <= screenSize.width/spacing[0] - 1 ; ++i)
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
			GamePanel.DP.drawLine(new Point(i*spacing[0], 0), new Point(i*spacing[0], screenSize.height), LineThickness, color) ;
			for (int j = 0 ; j <= screenSize.height/spacing[1] - 1 ; ++j)
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
				GamePanel.DP.drawLine(new Point(0, j*spacing[1]), new Point(screenSize.width, j*spacing[1]), LineThickness, color) ;
			}							
		}
	}
	
	public static void menu(Point pos, Align align, Dimension size)
	{
		GamePanel.DP.drawRoundRect(pos, align, size, 1, Palette.colors[3], Palette.colors[0], true);
	}
	
	public static void time()
	{
		float time = (float) Game.dayTimeRate() ;
		String message = (int) (24 * time) + ":" + (int) (24 * 60 * time % 60) ;
		GamePanel.DP.drawText(Game.getScreen().pos(0, 0.99), Align.bottomLeft, stdAngle, message, stdFont, Palette.colors[20]) ;
	}
	
	public static void mapElements(Hitbox playerHitbox, Point playerPos, GameMap map)
	{
		map.displayNPCs(playerHitbox) ;
		
		if (map instanceof FieldMap)
		{
			FieldMap fm = (FieldMap) map ;
			fm.displayCollectibles() ;
		}
		map.displayTudoEstaBem();
		time() ;
	}

	public static void keyboardKey(Point pos, String key, Font font, Color color)
	{
		GamePanel.DP.drawRoundRect(pos, Align.center, new Dimension(12, 12), 1, Palette.colors[3], Palette.colors[0], true, 2, 2) ;
		GamePanel.DP.drawText(pos, Align.center, stdAngle, key, font, color) ;
	}
	public static void keyboardKey(Point pos, String key, Color color)
	{
		keyboardKey(pos, key, smallFont, color) ;
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
			GamePanel.DP.drawRoundRect(bottomLeftBar, align, new Dimension(barWidth, barHeight), 1, color, Palette.colors[3], true) ;
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
		
		GamePanel.DP.drawRoundRect(pos, align, new Dimension(width, height), borderThickness, backgroundColor, borderColor, true, 25, 25) ;
		
		Point centerPos = UtilAlignment.getPosAt(pos, align, Align.center, new Dimension(width, height)) ;
		Point circleCenter = Util.translate(centerPos, isOn ? width / 4 : -width / 4, 0) ;
		GamePanel.DP.drawCircle(circleCenter, height / 2 - borderThickness, borderColor) ;
	}
	
	public static void settingSwitch(Point pos, Align align, boolean isOn)
	{
		settingSwitch(pos, align, isOn, Palette.colors[3]) ;
	}

	public static void filter(Color color)
	{
		for (int i = 0 ; i <= Game.getScreen().getSize().height - 1 ; i += 2)
		{
			GamePanel.DP.drawLine(new Point(0, i), new Point(Game.getScreen().getSize().width, i), color) ;
		}
	}
}