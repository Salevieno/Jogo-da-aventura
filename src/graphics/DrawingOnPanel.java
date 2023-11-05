package graphics ;

import java.awt.AlphaComposite ;
import java.awt.BasicStroke ;
import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Font ;
import java.awt.GradientPaint ;
import java.awt.Graphics2D ;
import java.awt.Image ;
import java.awt.Point;
import java.awt.geom.AffineTransform ;
import java.util.Arrays;
import java.util.List;

import components.GameButton;
import items.Item;
import liveBeings.Pet;
import liveBeings.Player;
import main.AtkResults;
import main.Game;
import main.TextCategories;
import maps.Continents;
import maps.FieldMap;
import maps.GameMap;
import screen.Sky;
import utilities.Align;
import utilities.AtkEffects;
import utilities.Scale;
import utilities.TimeCounter;
import utilities.UtilG;
import utilities.UtilS;
import windows.PlayerAttributesWindow;

public class DrawingOnPanel 
{
	private Graphics2D graph2D ;
	
	private static Color[] colorPalette = Game.colorPalette;
	private Dimension screenSize = Game.getScreen().getSize() ;
	private static Image menuWindow ;
	private static Image ArrowIconImage ;
	
	public static double stdAngle ;
	public static final int stdStroke ;
	public static final Font stdFont ;
	public static final Image[] ElementImages ;
	
	static
	{
		stdAngle = 0 ;
		stdStroke = 1;
		stdFont = new Font(Game.MainFontName, Font.BOLD, 13) ;
		menuWindow = UtilS.loadImage("MenuWindow.png") ;
		ArrowIconImage = UtilS.loadImage("\\Windows\\" + "ArrowIcon.png") ;
		ElementImages = new Image[] {
				UtilS.loadImage("\\Elements\\" + "ElementNeutral.png"),
				UtilS.loadImage("\\Elements\\" + "ElementWater.png"),
				UtilS.loadImage("\\Elements\\" + "ElementFire.png"),
				UtilS.loadImage("\\Elements\\" + "ElementPlant.png"),
				UtilS.loadImage("\\Elements\\" + "ElementEarth.png"),
				UtilS.loadImage("\\Elements\\" + "ElementAir.png"),
				UtilS.loadImage("\\Elements\\" + "ElementThunder.png"),
				UtilS.loadImage("\\Elements\\" + "ElementLight.png"),
				UtilS.loadImage("\\Elements\\" + "ElementDark.png"),
				UtilS.loadImage("\\Elements\\" + "ElementSnow.png")
				} ;
	}
	
	public void setGraphics(Graphics2D graph2D)
	{
		this.graph2D = graph2D ;
	}

	// primitive methods
	public void DrawImage(Image image, Point pos, Align align)
	{
		DrawImage(image, pos, 0, Scale.unit, false, false, align, 1) ;
	}
	public void DrawImage(Image image, Point pos, Scale scale, Align align)
	{
		DrawImage(image, pos, 0, scale, false, false, align, 1) ;
	}
	public void DrawImage(Image image, Point pos, double angle, Scale scale, Align align)
	{
		DrawImage(image, pos, angle, scale, false, false, align, 1) ;
	}
	public void DrawImage(Image image, Point pos, double angle, Scale scale, boolean flipH, boolean flipV, Align align, double alpha)
	{       
		if (image == null) { System.out.println("Tentando desenhar imagem nula na pos " + pos) ; return ; }
		
		Dimension size = new Dimension((int)(scale.x * image.getWidth(null)), (int)(scale.y * image.getHeight(null))) ;
		size = new Dimension ((!flipH ? 1 : -1) * size.width, (!flipV ? 1 : -1) * size.height) ;
		Point offset = UtilG.offsetForAlignment(align, size) ;
		AffineTransform backup = graph2D.getTransform() ;
		graph2D.transform(AffineTransform.getRotateInstance(-angle * Math.PI / 180, pos.x, pos.y)) ;
		graph2D.setComposite(AlphaComposite.SrcOver.derive((float) alpha)) ;
		
		graph2D.drawImage(image, pos.x + offset.x, pos.y + offset.y, size.width, size.height, null) ;
		
		graph2D.setComposite(AlphaComposite.SrcOver.derive((float) 1.0)) ;
        graph2D.setTransform(backup) ;
	}
	public void DrawGif(Image gif, Point pos, Align align)
	{
		Dimension size = new Dimension(gif.getWidth(null), gif.getHeight(null)) ;
		Point offset = UtilG.offsetForAlignment(align, size) ;
		graph2D.drawImage(gif, pos.x + offset.x, pos.y + offset.y, null) ;
	}
	public void DrawText(Point pos, Align align, double angle, String text, Font font, Color color)
	{
		// by default starts at the left bottom
		Dimension size = new Dimension(UtilG.TextL(text, font, graph2D), UtilG.TextH(font.getSize())) ;
		Point offset = UtilG.offsetForAlignment(align, size) ;
		AffineTransform backup = graph2D.getTransform() ;		
		
		graph2D.transform(AffineTransform.getRotateInstance(-angle * Math.PI / 180, pos.x, pos.y)) ;

		graph2D.setColor(color) ;
		graph2D.setFont(font) ;
		graph2D.drawString(text, pos.x + offset.x, pos.y + offset.y + size.height) ;
        
		graph2D.setTransform(backup) ;
	}
	public void DrawFitText(Point pos, int sy, Align align, String text, Font font, int maxLength, Color color)
	{
		List<String> FitText = UtilG.FitText(text, maxLength) ;
		for (int i = 0 ; i <= FitText.size() - 1 ; i += 1)
		{
			DrawText(new Point(pos.x, pos.y + i*sy), align, stdAngle, FitText.get(i), font, color) ;						
		}
	}
	public void DrawTextUntil(Point pos, Align align, double angle, String text, Font font, Color color, int maxLength, Point mousePos)
	{
		Point offset = UtilG.offsetForAlignment(align, new Dimension(maxLength, UtilG.TextH(font.getSize()))) ;
		int minlength = 3 ;	// 3 is the length of "..."
		String shortText = text ;
		maxLength = Math.max(maxLength, minlength) ;
		if (maxLength < text.length())
		{
			char[] chararray = new char[maxLength - minlength] ;
			text.getChars(0, maxLength - 4, chararray, 0) ;
			shortText = String.valueOf(chararray) ;
		}
		
		Point topLeftPos = UtilG.Translate(pos, offset.x, offset.y) ;
		Dimension textSize = new Dimension(UtilG.TextL(shortText, font, graph2D), UtilG.TextH(font.getSize())) ;
		if (text.length() <= maxLength | UtilG.isInside(mousePos, topLeftPos, textSize))
		{
			DrawText(pos, align, stdAngle, text, font, color) ;
		}
		else
		{
			DrawText(pos, align, stdAngle, shortText + "...", font, color) ;
		}
	}
	public void DrawLine(Point p1, Point p2, int stroke, Color color)
	{
		graph2D.setColor(color) ;
		graph2D.setStroke(new BasicStroke(stroke)) ;
		
		graph2D.drawLine(p1.x, p1.y, p2.x, p2.y) ;
		
		graph2D.setStroke(new BasicStroke(stdStroke)) ;
        //Ut.CheckIfPosIsOutsideScreen(new int[] {x[0], y[0]}, new int[] {ScreenL + 55, ScreenH + 19}, "A line is being drawn outside window") ;
		//Ut.CheckIfPosIsOutsideScreen(new int[] {x[1], y[1]}, new int[] {ScreenL + 55, ScreenH + 19}, "A line is being drawn outside window") ;
	}
	public void DrawRect(Point pos, Align align, Dimension size, int stroke, Color color, Color contourColor)
	{
		// Rectangle by default starts at the left top
		Point offset = UtilG.offsetForAlignment(align, size) ;
		int[] Corner = new int[] {pos.x + offset.x, pos.y + offset.y} ;
		graph2D.setStroke(new BasicStroke(stroke)) ;
		if (color != null)
		{
			graph2D.setColor(color) ;
			graph2D.fillRect(Corner[0], Corner[1], size.width, size.height) ;
		}
		if (contourColor != null)
		{
			//int[] xPoints = new int[] {Corner[0], Corner[0] + size.width, Corner[0] + size.width, Corner[0], Corner[0]} ;
			//int[] yPoints = new int[] {Corner[1], Corner[1], Corner[1] + size.height, Corner[1] + size.height, Corner[1]} ;
			graph2D.setColor(contourColor) ;
			graph2D.drawRect(Corner[0], Corner[1], size.width, size.height) ;
			//G.drawPolyline(xPoints, yPoints, xPoints.length) ;
		}
		graph2D.setStroke(new BasicStroke(stdStroke)) ;
	}
	public void DrawRoundRect(Point pos, Align align, Dimension size, int stroke, Color topColor, Color botColor, boolean contour)
	{
		// Round rectangle by default starts at the left top
		int ArcWidth = 10, ArcHeight = 10 ;
		Point offset = UtilG.offsetForAlignment(align, size) ;
		int[] Corner = new int[] {pos.x + offset.x, pos.y + offset.y} ;
		graph2D.setStroke(new BasicStroke(stroke)) ;
		if (topColor != null & botColor != null)
		{
		    GradientPaint gradient = new GradientPaint(Corner[0], Corner[1], topColor, Corner[0], Corner[1] + size.height, botColor) ;
		    graph2D.setPaint(gradient) ;
			graph2D.fillRoundRect(Corner[0], Corner[1], size.width, size.height, ArcWidth, ArcHeight) ;
		}
		if (contour)
		{
			graph2D.setColor(Color.black) ;
			graph2D.drawRoundRect(Corner[0], Corner[1], size.width, size.height, ArcWidth, ArcHeight) ;
		}
		graph2D.setStroke(new BasicStroke(stdStroke)) ;
	}
	public void DrawArc(Point center, int diameter, int stroke, int startAngle, int endAngle, Color color, Color contourColor)
	{
		graph2D.setColor(color) ;
		graph2D.setStroke(new BasicStroke(stroke)) ;
		if (color != null)
		{
			graph2D.fillArc(center.x - diameter/2, center.y - diameter/2, diameter, diameter, startAngle, endAngle) ;
		}
		if (contourColor != null)
		{
			graph2D.setColor(contourColor) ;
			graph2D.drawArc(center.x - diameter/2, center.y - diameter/2, diameter, diameter, startAngle, endAngle) ;
		}
		graph2D.setStroke(new BasicStroke(stdStroke)) ;
	}
	public void DrawCircle(Point center, int diameter, int stroke, Color color, Color contourColor)
	{
		graph2D.setColor(color) ;
		graph2D.setStroke(new BasicStroke(stroke)) ;
		if (color != null)
		{
			graph2D.fillOval(center.x - diameter/2, center.y - diameter/2, diameter, diameter) ;
		}
		if (contourColor != null)
		{
			graph2D.setColor(contourColor) ;
			graph2D.drawOval(center.x - diameter/2, center.y - diameter/2, diameter, diameter) ;
		}
		graph2D.setStroke(new BasicStroke(stdStroke)) ;
	}
	public void DrawPolygon(int[] x, int[] y, int stroke, Color color)
	{
		graph2D.setColor(color) ;
		graph2D.setStroke(new BasicStroke(stroke)) ;
		graph2D.fillPolygon(x, y, x.length) ;
		graph2D.setStroke(new BasicStroke(stdStroke)) ;
	}
	public void DrawPolyLine(int[] x, int[] y, int stroke, Color color)
	{
		graph2D.setColor(color) ;
		graph2D.setStroke(new BasicStroke(stroke)) ;
		graph2D.drawPolyline(x, y, x.length) ;
		graph2D.setStroke(new BasicStroke(stdStroke)) ;
	}
	
	
	// composed methods
	public void DrawSpeech(Point pos, String text, Font font, Image speechBubble, Color color)
	{
		// obs: text must end with . , ? or ! for this function to work
		int bubbleL = speechBubble.getWidth(null), bubbleH = speechBubble.getHeight(null) ;
		boolean flipH = 0.7 * screenSize.width < pos.x ? true : false ;
		Color textColor = color != null ? color : colorPalette[21] ;
		
		if (pos.x <= 0.3 * screenSize.width)
		{
			pos = UtilG.Translate(pos, 50, 0) ;
		}

		DrawImage(speechBubble, pos, stdAngle, Scale.unit, flipH, false, Align.bottomCenter, 1) ;
		
		Point textPos = UtilG.Translate(pos, 12 - bubbleL / 2, 5 - bubbleH) ;
		int maxTextL = 35 ;
		int sy = font.getSize() + 1 ;
		DrawFitText(textPos, sy, Align.topLeft, text, font, maxTextL, textColor) ;
	}
	
	public void DrawWindowArrows(Point pos, int width, int selectedWindow, int numberWindows)
	{
		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		if (0 < selectedWindow)
		{
			Point leftArrowPos = UtilG.Translate(pos, (int)(0.25 * width), 0) ;
			Point textPos = UtilG.Translate(leftArrowPos, 20, 0) ;
			DrawImage(ArrowIconImage, leftArrowPos, stdAngle, new Scale(-1, -1), Align.center) ;
			DrawText(textPos, Align.topRight, stdAngle, Player.ActionKeys[1], font, colorPalette[0]) ;			
		}
		if (selectedWindow < numberWindows - 1)
		{
			Point rightArrowPos = UtilG.Translate(pos, (int)(0.75 * width), 0) ;
			Point textPos = UtilG.Translate(rightArrowPos, -20, 0) ;
			DrawImage(ArrowIconImage, rightArrowPos, stdAngle, new Scale(1, -1), Align.center) ;
			DrawText(textPos, Align.topRight, stdAngle, Player.ActionKeys[3], font, colorPalette[0]) ;		
		}
	}
	
	public void DrawLoadingText(Image LoadingGif, Point Pos) { DrawGif(LoadingGif, Pos, Align.center) ;}
	
	public void DrawLoadingGameScreen(Player player, Pet pet, GameButton[] icons, int SlotID, int NumberOfUsedSlots, Image GoldCoinImage)
	{
		Point[] WindowPos = new Point[] {Game.getScreen().pos(0.15, 0.2), Game.getScreen().pos(0.65, 0.2), Game.getScreen().pos(0.5, 0.2)} ;
		Font font = new Font("SansSerif", Font.BOLD, 28) ;
		
		DrawText(Game.getScreen().pos(0.5, 0.05), Align.center, stdAngle, "Slot " + (SlotID + 1), font, colorPalette[5]) ;
		//player.DrawAttWindow(MainWinDim, WindowPos[0], null, AllText, AllTextCat, 0, GoldCoinImage, icons, DP) ;
		((PlayerAttributesWindow) player.getAttWindow()).display(new Point(0, 0), this) ;
		if (0 < pet.getLife().getCurrentValue())
		{
 			//pet.getAttWindow().display(pet, allText, null, null, NumberOfUsedSlots, null, null, null, null);
		}
 		if (ArrowIconImage == null) { return ;}
		
 		DrawWindowArrows(WindowPos[2], (int)(0.5*screenSize.width), SlotID, NumberOfUsedSlots - 1) ;
	}
	
	public void DrawEmptyLoadingSlot(int SlotID, int NumSlots)
	{
		Point[] WindowPos = new Point[] {new Point((int)(0.35*screenSize.width), (int)(0.2*screenSize.height)),
				new Point((int)(0.65*screenSize.width), (int)(0.2*screenSize.height)),
				new Point((int)(0.5*screenSize.width), (int)(0.2*screenSize.height))} ;
		DrawRoundRect(WindowPos[0], Align.topLeft, new Dimension(screenSize.width / 3, screenSize.height / 2), 2, Color.white, Color.lightGray, true) ;
		DrawText(new Point(WindowPos[0].x + screenSize.width / 6, WindowPos[0].y + screenSize.height / 4), Align.center, stdAngle, "Slot " + String.valueOf(SlotID + 1) + " is empty", new Font("SansSerif", Font.BOLD, 20), colorPalette[5]) ;
		DrawWindowArrows(new Point(WindowPos[0].x, WindowPos[0].y + screenSize.height / 2), screenSize.width / 3, SlotID, NumSlots) ;
	}

	public void DrawGrid(int[] spacing)
	{
		for (int i = 0 ; i <= screenSize.width/spacing[0] - 1 ; ++i)
		{
			int LineThickness = 1 ;
			Color color = colorPalette[21] ;
			if (i % 10 == 0)
			{
				LineThickness = 2 ;
			}
			if (i % 20 == 0)
			{
				LineThickness = 2 ;
				color = colorPalette[5] ;
			}
			DrawLine(new Point(i*spacing[0], 0), new Point(i*spacing[0], screenSize.height), LineThickness, color) ;
			for (int j = 0 ; j <= screenSize.height/spacing[1] - 1 ; ++j)
			{
				LineThickness = 1 ;
				color = colorPalette[21] ;
				if (j % 10 == 0)
				{
					LineThickness = 2 ;
				}
				if (j % 20 == 0)
				{
					LineThickness = 2 ;
					color = colorPalette[5] ;
				}
				DrawLine(new Point(0, j*spacing[1]), new Point(screenSize.width, j*spacing[1]), LineThickness, color) ;
			}							
		}
	}
	
	public void DrawTime(Sky sky)
	{
		Font font = new Font(Game.MainFontName, Font.BOLD, 14) ;
		float time = (float)(Sky.dayTime.getCounter()) / Game.DayDuration ;
		String message = (int)(24*time) + ":" + (int)(24*60*time % 60) ;
		DrawText(Game.getScreen().pos(0, 0.99), Align.bottomLeft, stdAngle, message, font, colorPalette[20]) ;
	}
	
	public void DrawFullMap(Point playerPos, GameMap map, Sky sky)
	{
		if ( !map.getContinent().equals(Continents.cave) ) { sky.display(this) ; sky.updateIsDay() ;}
		
		map.display(this) ;
		map.displayElements(playerPos, this) ;
		map.displayBuildings(playerPos, Arrays.asList(Game.getMaps()).indexOf(map), this) ;
		map.displayNPCs(this) ;
		map.displayGroundTypes(this) ;
		
		if (map instanceof FieldMap)
		{
			FieldMap fm = (FieldMap) map ;
			fm.displayCollectibles(this) ;
		}
		map.displayTudoEstaBem(this);
		DrawTime(sky) ;
	}

	public void DrawDamageAnimation(Point initialPos, AtkResults atkResults, TimeCounter counter, int style, Color color)
	{
		AtkEffects effect = atkResults.getEffect() ;
		
		if (effect == AtkEffects.none) { return ;}

		String message = null ;
		String damage = String.valueOf(UtilG.Round(atkResults.getDamage(), 1)) ;
		switch (effect)
		{
			case miss: message = "Miss" ; break ;
			case hit: message = damage ; break ;
			case crit: message = damage + "!" ; break ;
			case block: message = "Block" ;	break ;
			default: break ;
		}

		double rate = Math.pow(counter.rate(), 0.6) ;
//		Point[] movement = new Point[] {
//				new Point(0, (int) (-20 * rate)),
//				new Point((int) (Math.pow(8 * rate, 2)), (int) (-20 * rate)),
//				new Point((int) (Math.pow(rate, 2)), (int) (-20 * rate))
//		};
		
		Point move = new Point() ;
		switch (style)
		{
			case 1: move = new Point((int) (Math.pow(8 * rate, 2)), (int) (-20 * rate)) ; break ;
			case 2: move = new Point((int) (Math.pow(rate, 2)), (int) (-20 * rate)) ; break ;
			default: move = new Point(0, (int) (-20 * rate)) ; break ;
		}
				
//		Point move = switch(effect)
//				{
//		case 1 -> new Point((int) (Math.pow(8 * rate, 2)), (int) (-20 * rate)) ;
//		case 2 -> new Point((int) (Math.pow(rate, 2)), (int) (-20 * rate)) ;
//		default -> new Point(0, (int) (-20 * rate)) ;
//				}

		Font font = new Font(Game.MainFontName, Font.BOLD, 14) ;
		Point currentPos = UtilG.Translate(initialPos, move.x, move.y) ;
		DrawText(currentPos, Align.center, stdAngle, message, font, color) ;
		
	}
	
	public void DrawSkillNameAnimation(Point Pos, String SkillName, Color color)
	{
		Font font = new Font("SansSerif", Font.BOLD, 18) ;
		DrawText(Pos, Align.center, stdAngle, SkillName, font, color) ;
	}
	
	public void CollectingAnimation(Point Pos, int counter, int delay, int MessageTime, int CollectibleType, String Message)
	{
		/*int TextCat = AllTextCat[9] ;
		Font font = new Font("SansSerif", Font.BOLD, 20) ;
		int DotsDelay = 10 ;
		int L = 200 ;
		String[] CollectibleName = new String[] {AllText[TextCat][2], AllText[TextCat][3], AllText[TextCat][4], AllText[TextCat][5]} ;
		Color[] CollectibleColor = new Color[] {MapsTypeColor[12], MapsTypeColor[13], MapsTypeColor[14], MapsTypeColor[15]} ;
		if (counter <= delay - MessageTime)
		{			
			if (counter % (3*DotsDelay) < DotsDelay)
			{
				DrawText(new Point(Pos.x + (int)(0.05*screenSize.width) - L/2, Pos.y - (int)(0.05*screenSize.height)), alignPoints.bottomLeft, OverallAngle, AllText[TextCat][1] + " " + CollectibleName[CollectibleType] + ".", font, CollectibleColor[CollectibleType]) ;
			} else if (counter % (3*DotsDelay) < 2*DotsDelay)
			{
				DrawText(new Point(Pos.x + (int)(0.05*screenSize.width) - L/2, Pos.y - (int)(0.05*screenSize.height)), alignPoints.bottomLeft, OverallAngle, AllText[TextCat][1] + " " + CollectibleName[CollectibleType] + "..", font, CollectibleColor[CollectibleType]) ;
			} else
			{
				DrawText(new Point(Pos.x + (int)(0.05*screenSize.width) - L/2, Pos.y - (int)(0.05*screenSize.height)), alignPoints.bottomLeft, OverallAngle, AllText[TextCat][1] + " " + CollectibleName[CollectibleType] + "...", font, CollectibleColor[CollectibleType]) ;				
			}	
			DrawTimeBar(Pos, counter, delay, L, new int[] {0, 0}, "Right", "Horizontal", CollectibleColor[CollectibleType]) ;
		}
		else
		{
			DrawText(new Point(Pos.x + (int)(0.05*screenSize.width) - L/2, Pos.y - (int)(0.05*screenSize.height)), alignPoints.bottomLeft, OverallAngle, Message, font, CollectibleColor[CollectibleType]) ;			
		}
		*/
	}
	
	public void TentAnimation(Point Pos, int counter, int delay, Image TentImage)
	{
		DrawImage(TentImage, Pos, Align.center) ;
	}

	public void winAnimation(TimeCounter counter, Item[] items)
	{
		Point pos = Game.getScreen().pos(0.45, 0.2) ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		Color titleColor = Game.colorPalette[5] ;
		Color itemNamesColor = Game.colorPalette[6] ;

		DrawImage(menuWindow, pos, Scale.unit, Align.topLeft) ;
		Point textPos = UtilG.Translate(pos, 5, font.getSize() + 5) ;
		DrawText(textPos, Align.bottomLeft, stdAngle, "Você obteve!", font, titleColor) ;
		
		if ( counter.rate() <= 0.3 ) { return ;}
		
		for (int i = 0 ; i <= items.length - 1 ; i += 1)
		{
			if ( 0.3 + 0.5 * i / items.length <= counter.rate() )
			{
				Point newTextPos = UtilG.Translate(textPos, 0, (i + 1) * (font.getSize() + 2)) ;
				DrawText(newTextPos, Align.bottomLeft, stdAngle, items[i].getName(), font, itemNamesColor) ;
			}
		}
	}

	public void gainGoldAnimation(TimeCounter counter, int goldObtained)
	{
		
		Point pos = Game.getScreen().pos(0.45, 0.1) ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		Color textColor = Game.colorPalette[14] ;

		Point textPos = UtilG.Translate(pos, 5, 0) ;
		DrawText(textPos, Align.centerLeft, stdAngle, "+", font, textColor) ;
		
		Point coinPos = UtilG.Translate(pos, 15, 0) ;
		DrawImage(Player.CoinIcon, coinPos, Scale.unit, Align.centerLeft) ;
		
		Point amountPos = UtilG.Translate(pos, 35, 0) ;
		DrawText(amountPos, Align.centerLeft, stdAngle, String.valueOf(goldObtained), font, textColor) ;
		
	}

	public void notEnoughGold(TimeCounter counter)
	{

		Point pos = Game.getScreen().pos(0.45, 0.2) ;
//		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
//		Color titleColor = Game.colorPalette[3] ;

//		DrawImage(menuWindow, pos, Scale.unit, Align.topLeft) ;
		
//		Point textPos = UtilG.Translate(pos, 5, 0) ;
//		DrawText(textPos, Align.centerLeft, stdAngle, "Você não tem ouro suficiente!", font, titleColor) ;
		Game.getAnimations().get(12).start(200, new Object[] {pos, "Você não tem ouro suficiente!", Game.colorPalette[0]}) ;
		
	}

	public void quickTextAnimation(Point pos, TimeCounter counter, String text, Color color)
	{

		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		Dimension size = new Dimension(UtilG.TextL(text, font, graph2D) + 10, 20) ;
		Color bgColor = Game.colorPalette[3] ;
		
		DrawRoundRect(pos, Align.topLeft, size, 2, bgColor, bgColor, true) ;
		DrawText(UtilG.Translate(pos, 5, 5), Align.topLeft, stdAngle, text, font, color) ;
		
	}
	
	public void levelUpAnimation(TimeCounter counter, double[] AttributeIncrease, int playerLevel, Color textColor)
	{

		Point pos = Game.getScreen().pos(0.45, 0.2) ;
		Scale scale = Scale.unit ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		String[] attText = Game.allText.get(TextCategories.attributes) ;
		
		DrawImage(menuWindow, pos, scale, Align.topLeft) ;
		Point textPos = UtilG.Translate(pos, 5, font.getSize() + 5) ;
		DrawText(textPos, Align.bottomLeft, stdAngle, attText[0] + " " + playerLevel + "!", font, textColor) ;
		
		if ( counter.rate() <= 0.3 ) { return ;}
		
//		String[] attNames = new String[] {"Vida", "MP", "Ataque f�sico", "Ataque mágico", "Defesa física", "Defesa mágica", "Destreza", "Agilidade"} ;
		String[] attNames = Arrays.copyOfRange(attText, 1, 9) ;
		for (int i = 0 ; i <= AttributeIncrease.length - 1 ; i += 1)
		{
			if ( 0.3 + 0.5 * i / (AttributeIncrease.length - 1) <= counter.rate() )
			{
				Point newTextPos = UtilG.Translate(textPos, 0, (i + 1) * (font.getSize() + 2)) ;
				DrawText(newTextPos, Align.bottomLeft, stdAngle, attNames[i] + " + " + AttributeIncrease[i], font, textColor) ;
			}
		}
		
	}
	
	public void SailingAnimation(Image playerImage, Image sailorImage, Image boatImage, TimeCounter counter, String destination)
	{
//		int Step = player.getStep()/2 ;
//		Dimension sailorSize = new Dimension(sailorImage.getWidth(null), sailorImage.getHeight(null)) ;
		if (destination.equals("Island"))
		{
			
		}
		int step = 1 ;
		Point startPos = new Point(step, (int)(0.5*screenSize.height)) ;	
		Point currentPos = UtilG.Translate(startPos, (int) (step * counter.rate()), 0) ;

		DrawImage(boatImage, currentPos, Align.center) ;
		DrawImage(sailorImage, currentPos, Align.center) ;
		DrawImage(playerImage, currentPos, Align.center) ;
		
		if (Game.getScreen().posIsInMap(currentPos)) { return ;}
		
		
//		else
//		{
//			player.setPos(startPos) ;
//			player.setMap(maps[player.getMap().getid() + 1]) ;
//			if (player.getMap().getid() == 65)
//			{
//				player.setPos(new Point(20, 500)) ;
//				player.setMap(maps[40]) ;
//			}
//		}
//		else if (Destination.equals("Forest"))
//		{
//			Point InitialPos = new Point(screenSize.width - Step, (int)(0.5*screenSize.height)) ;
//			Point Pos = new Point((InitialPos.x - Step*(counter % (screenSize.width/Step - 1))) % screenSize.width, InitialPos.y) ;
//			if (0 < Pos.x - Step)
//			{
//				DrawImage(BoatImage, Pos, OverallAngle, new float[] {(float)1, (float)1}, new boolean[] {false, false}, alignPoints.bottomLeft, 1) ;
//				DrawImage(npc, new Point(Pos.x + NPCLength, (int) (Pos.y - 0.5*NPCHeight)), OverallAngle, new float[] {(float)0.5, (float)0.5}, new boolean[] {false, false}, alignPoints.center, 1) ;
//				player.setPos(new Point(Pos.x - Step, Pos.y)) ;
//			}
//			else
//			{
//				player.setPos(InitialPos) ;
//				player.setMap(maps[player.getMap().getid() - 1]) ;
//				if (player.getMap().getid() == 60)
//				{
//					player.setPos(new Point(640, 500)) ;
//					player.setMap(maps[13]) ;
//					//if (MusicIsOn)
//					//{
//					//	UtilGeral.SwitchMusic(Music[11], Music[MusicInMap[player.getMap()]]) ;
//					//}
//				}
//			}
//		}
	}
	
	public void FishingAnimation(Point playerPos, Image FishingGif, String WaterPos)
	{
		Point Pos = new Point(playerPos.x, playerPos.y) ;
		int offset = 23 ;
		if (WaterPos.equals("Touching Up"))
		{
			Pos = new Point(playerPos.x, playerPos.y - offset) ;
		}
		else if (WaterPos.equals("Touching Down"))
		{
			Pos = new Point(playerPos.x, playerPos.y + offset) ;
		}
		else if (WaterPos.equals("Touching Right"))
		{
			Pos = new Point(playerPos.x + offset, playerPos.y) ;
		}
		else if (WaterPos.equals("Touching Left"))
		{
			Pos = new Point(playerPos.x - offset, playerPos.y) ;
		}
		UtilG.PlayGif(Pos, FishingGif, this) ;
	}
	
	public void PterodactileAnimation(TimeCounter counter, Image pterodactile, Image speakingBubble, String[] message)
	{
		Font font = new Font(Game.MainFontName, Font.BOLD, 15) ;
		int screenWidth = screenSize.width ;
		int screenHeight = screenSize.height ;
		Point currentPos = new Point(screenWidth + pterodactile.getWidth(null)/2, (int)(0.25*screenSize.height)) ;
		Point messagePos = currentPos ;
		int imageWidth = pterodactile.getWidth(null) ;
		
		if (counter.rate() < 0.25)
		{
			currentPos.x += -4 * imageWidth * counter.rate() ;
			currentPos.y += screenHeight * counter.rate() ;
		}
		else if (counter.rate() < 0.5)
		{
			currentPos.x += -0.5*(screenWidth + imageWidth) ;
			currentPos.y += 0.25*screenHeight ;
			DrawSpeech(messagePos, message[1], font, speakingBubble, Game.colorPalette[19]) ;
		}
		else if (counter.rate() < 0.75)
		{
			currentPos.x += -0.5*(screenWidth + imageWidth) ;
			currentPos.y += -screenHeight * (counter.rate() - 0.5) ;
			DrawSpeech(messagePos, message[2], font, speakingBubble, Game.colorPalette[19]) ;
		}
		else
		{
			currentPos.x += -2 * (screenWidth + imageWidth) * (counter.rate() - 0.75 + 0.25) ;
			currentPos.y += screenHeight * (counter.rate() - 0.75 + 0.25) ;
		}
		DrawImage(pterodactile, currentPos, Align.center) ;
		
	}
	
	
}
