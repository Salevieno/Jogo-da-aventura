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
import utilities.AttackEffects;
import utilities.Scale;
import utilities.TimeCounter;
import utilities.UtilG;

import windows.PlayerAttributesWindow;

public class DrawingOnPanel 
{
	public static double stdAngle = 0 ;
	public static int stdStroke = 1;
	private Graphics2D G ;
	
	private static Color[] colorPalette = Game.colorPalette;
	private Dimension screenSize = Game.getScreen().getSize() ;
	private static Image menuWindow = UtilG.loadImage(Game.ImagesPath + "MenuWindow.png") ;
	private static Image ArrowIconImage = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "ArrowIcon.png") ;
	public static Image[] ElementImages = new Image[] {
			UtilG.loadImage(Game.ImagesPath + "\\Elements\\" + "ElementNeutral.png"),
			UtilG.loadImage(Game.ImagesPath + "\\Elements\\" + "ElementWater.png"),
			UtilG.loadImage(Game.ImagesPath + "\\Elements\\" + "ElementFire.png"),
			UtilG.loadImage(Game.ImagesPath + "\\Elements\\" + "ElementPlant.png"),
			UtilG.loadImage(Game.ImagesPath + "\\Elements\\" + "ElementEarth.png"),
			UtilG.loadImage(Game.ImagesPath + "\\Elements\\" + "ElementAir.png"),
			UtilG.loadImage(Game.ImagesPath + "\\Elements\\" + "ElementThunder.png"),
			UtilG.loadImage(Game.ImagesPath + "\\Elements\\" + "ElementLight.png"),
			UtilG.loadImage(Game.ImagesPath + "\\Elements\\" + "ElementDark.png"),
			UtilG.loadImage(Game.ImagesPath + "\\Elements\\" + "ElementSnow.png")
			} ;
	
	
	public void setGraphics(Graphics2D G)
	{
		this.G = G ;
	}

	// primitive methods
	public void DrawImage(Image image, Point pos, Align align)
	{
		DrawImage(image, pos, 0, new Scale(1, 1), false, false, align, 1) ;
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
		if (image == null) { System.out.println("Tentando desenhar imagem nula") ; return ; }
		
		Dimension size = new Dimension((int)(scale.x * image.getWidth(null)), (int)(scale.y * image.getHeight(null))) ;
		size = new Dimension ((!flipH ? 1 : -1) * size.width, (!flipV ? 1 : -1) * size.height) ;
		Point offset = UtilG.OffsetFromPos(align, size) ;
		AffineTransform backup = G.getTransform() ;
		G.transform(AffineTransform.getRotateInstance(-angle * Math.PI / 180, pos.x, pos.y)) ;
		G.setComposite(AlphaComposite.SrcOver.derive((float) alpha)) ;
		
		G.drawImage(image, pos.x + offset.x, pos.y + offset.y, size.width, size.height, null) ;
		
		G.setComposite(AlphaComposite.SrcOver.derive((float) 1.0)) ;
        G.setTransform(backup) ;
	}
	public void DrawGif(Image gif, Point pos, Align align)
	{
		Dimension size = new Dimension(gif.getWidth(null), gif.getHeight(null)) ;
		Point offset = UtilG.OffsetFromPos(align, size) ;
		G.drawImage(gif, pos.x + offset.x, pos.y + offset.y, null) ;
	}
	public void DrawText(Point pos, Align align, double angle, String text, Font font, Color color)
	{
		// by default starts at the left bottom
		Dimension size = new Dimension(UtilG.TextL(text, font, G), UtilG.TextH(font.getSize())) ;
		Point offset = UtilG.OffsetFromPos(align, size) ;
		AffineTransform backup = G.getTransform() ;		
		
		G.transform(AffineTransform.getRotateInstance(-angle * Math.PI / 180, pos.x, pos.y)) ;

		G.setColor(color) ;
		G.setFont(font) ;
		G.drawString(text, pos.x + offset.x, pos.y + offset.y + size.height) ;
        
		G.setTransform(backup) ;
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
		Point offset = UtilG.OffsetFromPos(align, new Dimension(maxLength, UtilG.TextH(font.getSize()))) ;
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
		Dimension textSize = new Dimension(UtilG.TextL(shortText, font, G), UtilG.TextH(font.getSize())) ;
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
		G.setColor(color) ;
		G.setStroke(new BasicStroke(stroke)) ;
		
		G.drawLine(p1.x, p1.y, p2.x, p2.y) ;
		
		G.setStroke(new BasicStroke(stdStroke)) ;
        //Ut.CheckIfPosIsOutsideScreen(new int[] {x[0], y[0]}, new int[] {ScreenL + 55, ScreenH + 19}, "A line is being drawn outside window") ;
		//Ut.CheckIfPosIsOutsideScreen(new int[] {x[1], y[1]}, new int[] {ScreenL + 55, ScreenH + 19}, "A line is being drawn outside window") ;
	}
	public void DrawRect(Point pos, Align align, Dimension size, int stroke, Color color, Color contourColor)
	{
		// Rectangle by default starts at the left top
		Point offset = UtilG.OffsetFromPos(align, size) ;
		int[] Corner = new int[] {pos.x + offset.x, pos.y + offset.y} ;
		G.setStroke(new BasicStroke(stroke)) ;
		if (color != null)
		{
			G.setColor(color) ;
			G.fillRect(Corner[0], Corner[1], size.width, size.height) ;
		}
		if (contourColor != null)
		{
			//int[] xPoints = new int[] {Corner[0], Corner[0] + size.width, Corner[0] + size.width, Corner[0], Corner[0]} ;
			//int[] yPoints = new int[] {Corner[1], Corner[1], Corner[1] + size.height, Corner[1] + size.height, Corner[1]} ;
			G.setColor(contourColor) ;
			G.drawRect(Corner[0], Corner[1], size.width, size.height) ;
			//G.drawPolyline(xPoints, yPoints, xPoints.length) ;
		}
		G.setStroke(new BasicStroke(stdStroke)) ;
	}
	public void DrawRoundRect(Point pos, Align align, Dimension size, int stroke, Color topColor, Color botColor, boolean contour)
	{
		// Round rectangle by default starts at the left top
		int ArcWidth = 10, ArcHeight = 10 ;
		Point offset = UtilG.OffsetFromPos(align, size) ;
		int[] Corner = new int[] {pos.x + offset.x, pos.y + offset.y} ;
		G.setStroke(new BasicStroke(stroke)) ;
		if (topColor != null & botColor != null)
		{
		    GradientPaint gradient = new GradientPaint(Corner[0], Corner[1], topColor, Corner[0], Corner[1] + size.height, botColor) ;
		    G.setPaint(gradient) ;
			G.fillRoundRect(Corner[0], Corner[1], size.width, size.height, ArcWidth, ArcHeight) ;
		}
		if (contour)
		{
			G.setColor(Color.black) ;
			G.drawRoundRect(Corner[0], Corner[1], size.width, size.height, ArcWidth, ArcHeight) ;
		}
		G.setStroke(new BasicStroke(stdStroke)) ;
	}
	public void DrawArc(Point center, int diameter, int stroke, int startAngle, int endAngle, Color color, Color contourColor)
	{
		G.setColor(color) ;
		G.setStroke(new BasicStroke(stroke)) ;
		if (color != null)
		{
			G.fillArc(center.x - diameter/2, center.y - diameter/2, diameter, diameter, startAngle, endAngle) ;
		}
		if (contourColor != null)
		{
			G.setColor(contourColor) ;
			G.drawArc(center.x - diameter/2, center.y - diameter/2, diameter, diameter, startAngle, endAngle) ;
		}
		G.setStroke(new BasicStroke(stdStroke)) ;
	}
	public void DrawCircle(Point center, int diameter, int stroke, Color color, Color contourColor)
	{
		G.setColor(color) ;
		G.setStroke(new BasicStroke(stroke)) ;
		if (color != null)
		{
			G.fillOval(center.x - diameter/2, center.y - diameter/2, diameter, diameter) ;
		}
		if (contourColor != null)
		{
			G.setColor(contourColor) ;
			G.drawOval(center.x - diameter/2, center.y - diameter/2, diameter, diameter) ;
		}
		G.setStroke(new BasicStroke(stdStroke)) ;
	}
	public void DrawPolygon(int[] x, int[] y, int stroke, Color color)
	{
		G.setColor(color) ;
		G.setStroke(new BasicStroke(stroke)) ;
		G.fillPolygon(x, y, x.length) ;
		G.setStroke(new BasicStroke(stdStroke)) ;
	}
	public void DrawPolyLine(int[] x, int[] y, int stroke, Color color)
	{
		G.setColor(color) ;
		G.setStroke(new BasicStroke(stroke)) ;
		G.drawPolyline(x, y, x.length) ;
		G.setStroke(new BasicStroke(stdStroke)) ;
	}
	
	
	// composed methods
	public void DrawSpeech(Point pos, String text, Font font, Image speechBubble, Color color)
	{
		// obs: text must end with . , ? or ! for this function to work
		int bubbleL = speechBubble.getWidth(null), bubbleH = speechBubble.getHeight(null) ;
		boolean flipH = 0.7 * screenSize.width < pos.x ? true : false ;
		Color textColor = color != null ? color : colorPalette[0] ;
		
		if (0.7 * screenSize.width < pos.x)
		{
			pos = UtilG.Translate(pos, 50, 0) ;
		}

		DrawImage(speechBubble, pos, stdAngle, new Scale(1, 1), flipH, false, Align.bottomCenter, 1) ;
		
		Point textPos = UtilG.Translate(pos, 12 - bubbleL / 2, 5 - bubbleH) ;
		int maxTextL = 35 ;
		int sy = font.getSize() + 1 ;
		DrawFitText(textPos, sy, Align.topLeft, text, font, maxTextL, textColor) ;
	}
	
	public void DrawWindowArrows(Point pos, int width, int selectedWindow, int numberWindows)
	{
		Font font = new Font(Game.MainFontName, Font.BOLD, 11) ;
		if (0 < selectedWindow)
		{
			Point LeftArrowPos = new Point(pos.x + (int)(0.25 * width), pos.y) ;
			DrawImage(ArrowIconImage, LeftArrowPos, stdAngle, new Scale(-1, -1), Align.center) ;
			DrawText(LeftArrowPos, Align.topRight, stdAngle, Player.ActionKeys[1], font, colorPalette[5]) ;			
		}
		if (selectedWindow < numberWindows - 1)
		{
			Point RightArrowPos = new Point(pos.x + (int)(0.75 * width), pos.y) ;
			DrawImage(ArrowIconImage, RightArrowPos, stdAngle, new Scale(1, -1), Align.center) ;
			DrawText(RightArrowPos, Align.topRight, stdAngle, Player.ActionKeys[3], font, colorPalette[5]) ;		
		}
	}
	
	public void DrawLoadingText(Image LoadingGif, Point Pos) { DrawGif(LoadingGif, Pos, Align.center) ;}
	
	public void DrawLoadingGameScreen(Player player, Pet pet, GameButton[] icons, int SlotID, int NumberOfUsedSlots, Image GoldCoinImage)
	{
		Point[] WindowPos = new Point[] {new Point((int)(0.15*screenSize.width), (int)(0.2*screenSize.height)),
				new Point((int)(0.65*screenSize.width), (int)(0.2*screenSize.height)),
				new Point((int)(0.5*screenSize.width), (int)(0.2*screenSize.height))} ;
		Font font = new Font("SansSerif", Font.BOLD, 28) ;
		
		DrawText(new Point((int)(0.5*screenSize.width), (int)(0.05*screenSize.height)), Align.center, stdAngle, "Slot " + (SlotID + 1), font, colorPalette[5]) ;
		//player.DrawAttWindow(MainWinDim, WindowPos[0], null, AllText, AllTextCat, 0, GoldCoinImage, icons, DP) ;
		((PlayerAttributesWindow) player.getAttWindow()).display(player, player.getEquips(), player.getEquippedArrow(), new Point(0, 0), this) ;
		if (0 < pet.getLife().getCurrentValue())
		{
 			//pet.getAttWindow().display(pet, allText, null, null, NumberOfUsedSlots, null, null, null, null);
		}
 		if (ArrowIconImage != null)
 		{
 			DrawWindowArrows(WindowPos[2], (int)(0.5*screenSize.width), SlotID, NumberOfUsedSlots - 1) ;
 		}
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
			Color color = colorPalette[9] ;
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
				color = colorPalette[9] ;
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
		float time = (float)(sky.dayTime.getCounter()) / Game.DayDuration ;
		DrawText(new Point(0, (int) (0.99*screenSize.height)), Align.bottomLeft, stdAngle, (int)(24*time) + ":" + (int)(24*60*time % 60), font, colorPalette[5]) ;
	}
	
	public void DrawFullMap(Point playerPos, GameMap map, Sky sky)
	{
		if ( !map.getContinent().equals(Continents.cave) ) { sky.display(this) ;}
		
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
		AttackEffects effect = atkResults.getEffect() ;
		
		if (effect == AttackEffects.none) { return ;}

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

		Font font = new Font(Game.MainFontName, Font.BOLD, 16) ;
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
		Point pos = Game.getScreen().getPoint(0.45, 0.2) ;
		Scale scale = new Scale(1, 1) ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		Color titleColor = Game.colorPalette[5] ;
		Color itemNamesColor = Game.colorPalette[6] ;

		DrawImage(menuWindow, pos, scale, Align.topLeft) ;
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
		Point pos = Game.getScreen().getPoint(0.45, 0.6) ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		Color titleColor = Game.colorPalette[3] ;

		Point textPos = UtilG.Translate(pos, 5, 0) ;
		DrawText(textPos, Align.centerLeft, stdAngle, "+", font, titleColor) ;
		
		Point coinPos = UtilG.Translate(pos, 15, 0) ;
		DrawImage(Player.CoinIcon, coinPos, new Scale(1, 1), Align.centerLeft) ;
		
		Point amountPos = UtilG.Translate(pos, 35, 0) ;
		DrawText(amountPos, Align.centerLeft, stdAngle, String.valueOf(goldObtained), font, titleColor) ;
		
	}
	
	public void levelUpAnimation(TimeCounter counter, double[] AttributeIncrease, int playerLevel, Color textColor)
	{

		Point pos = Game.getScreen().getPoint(0.45, 0.2) ;
		Scale scale = new Scale(1, 1) ;
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
	
	

//	public void DrawFabBook(Image BookImage, Item[] items, int SelectedPage, int[][] Ingredients, int[][] Products, Point MousePos)
//	{
//		Point Pos = new Point((int)(0.5*screenSize.width), (int)(0.5*screenSize.height)) ;
//		Font Titlefont = new Font("SansSerif", Font.BOLD, 16) ;
//		Font font = new Font("SansSerif", Font.BOLD, 14) ;
//		int L = BookImage.getWidth(null), H = BookImage.getHeight(null) ;
//		int sy = H/15 ;
//		int IngredientsCont = 0, ProductsCont = 0 ;
//		int MaxTextL = 10 ;
//		DrawImage(BookImage, Pos, stdAngle, new Scale(1, 1), Align.center) ;
//		DrawText(new Point(Pos.x - 3*L/8, Pos.y - H/5 - sy/4), Align.bottomLeft, stdAngle, "Ingredients:", Titlefont, colorPalette[5]) ;
//		DrawText(new Point(Pos.x + 3*L/8, Pos.y - H/5 - sy/4), Align.topRight, stdAngle, "Products:", Titlefont, colorPalette[5]) ;		
//		for (int j = 0 ; j <= Ingredients[SelectedPage].length - 1 ; ++j)
//		{
//			if (-1 < Ingredients[SelectedPage][j])
//			{
//				/*if (Utg.MouseIsInside(MousePos, new int[] {Pos.x - 3*L/8, Pos.y - H/5 + IngredientsCont*sy + sy/2}, MaxTextL*font.getSize()/2, Utg.TextH(font.getSize())))
//				{
//					DrawText(new Point(Pos.x - 3*L/8, Pos.y - H/5 + IngredientsCont*sy + sy/2}, alignPoints.bottomLeft, OverallAngle, items[Ingredients[SelectedPage][j]].getName(), font, ColorPalette[5]) ;
//				}
//				else
//				{*/
////					DrawTextUntil(new Point(Pos.x - 3*L/8, Pos.y - H/5 + IngredientsCont*sy + sy/2), Align.bottomLeft, stdAngle, items[Ingredients[SelectedPage][j]].getName(), font, colorPalette[5], MaxTextL, MousePos) ;
//				//}
//				IngredientsCont += 1 ;
//			}
//		}
//		for (int j = 0 ; j <= Products[SelectedPage].length - 1 ; ++j)
//		{
//			if (-1 < Products[SelectedPage][j])
//			{
//				/*if (Utg.MouseIsInside(MousePos, new int[] {Pos.x + 3*L/8 - MaxTextL*font.getSize()/2, Pos.y - H/5 + ProductsCont*sy + sy/2}, MaxTextL*font.getSize()/2, Utg.TextH(font.getSize())))
//				{
//					DrawText(new Point(Pos.x + 3*L/8, Pos.y - H/5 + ProductsCont*sy + sy/2}, alignPoints.topRight, OverallAngle, items[Products[SelectedPage][j]].getName(), font, ColorPalette[5]) ;
//				}
//				else
//				{*/
////					DrawTextUntil(new Point(Pos.x + 3*L/8, Pos.y - H/5 + ProductsCont*sy + sy/2), Align.topRight, stdAngle, items[Products[SelectedPage][j]].getName(), font, colorPalette[5], MaxTextL, MousePos) ;
//				//}
//				ProductsCont += 1 ;
//			}
//		}
//		DrawWindowArrows(new Point(Pos.x, Pos.y + 15*H/32), L, SelectedPage, Ingredients.length - 1) ;
//	}
	
	

//	public void ChestRewardsAnimation(Items[] items, int counter, int duration, int[] ItemRewards, int[] GoldRewards, Color TextColor, Image CoinIcon)
//	{
		/*Font font = new Font("SansSerif", Font.BOLD, 20) ;
		int TextCat = AllTextCat[29] ;
		Point Pos = new Point((int)(0.3*screenSize.width), (int)(0.8*screenSize.height)) ;
		Size size = new Size((int)(0.5*screenSize.width), (int)(0.6*screenSize.height)) ;
		int Sy = size.y / 12 ;
		Point TextPos = new Point(Pos.x + (int)(0.05 * size.x), Pos.y - (int)(0.95 * size.y)) ;
		DrawMenuWindow(Pos, size, null, 0, ColorPalette[18], ColorPalette[7]) ;
		DrawText(TextPos, alignPoints.bottomLeft, OverallAngle, AllText[TextCat][1], font, TextColor) ;
		if (counter < duration/3)
		{
			DrawText(new Point(TextPos.x, TextPos.y + Sy), alignPoints.bottomLeft, OverallAngle, AllText[TextCat][2], font, TextColor) ;			
		} else if (counter < duration)
		{
			DrawText(new Point(TextPos.x, TextPos.y + Sy), alignPoints.bottomLeft, OverallAngle, AllText[TextCat][2], font, TextColor) ;			
			for (int i = 0 ; i <= ItemRewards.length - 1 ; i += 1)
			{
				if (duration/3 + 2*duration/30*i < counter % duration)
				{
					DrawText(new Point(TextPos.x, TextPos.y + (i + 2)*Sy), alignPoints.bottomLeft, OverallAngle, items[ItemRewards[i]].getName(), font, TextColor) ;															
				}
			}
			for (int i = 0 ; i <= GoldRewards.length - 1 ; i += 1)
			{
				if (duration/3 + 2*duration/30*i < counter % duration)
				{
					DrawText(new Point(TextPos.x, TextPos.y + (i + 2 + ItemRewards.length)*Sy), alignPoints.bottomLeft, OverallAngle, String.valueOf(GoldRewards[i]), font, ColorPalette[18]) ;															
					DrawImage(CoinIcon, new Point((int) (TextPos.x + 1.05*UtilG.TextL(String.valueOf(GoldRewards[i]), font, G)), TextPos.y + (i + 2 + ItemRewards.length)*Sy + UtilG.TextH(font.getSize())/2), OverallAngle, new float[] {1, 1}, new boolean[] {false, false}, alignPoints.bottomLeft, 1) ;
				}
			}
		}
		*/
//	}
	
	
	
	
	
	
	
//	public void PetLevelUpAnimation(Pet pet, int counter, int duration, double[] AttributeIncrease)
//	{
//		Font font = new Font("SansSerif", Font.BOLD, 20) ;
//		int AttCat = AllTextCat[6], WinCat = AllTextCat[10] ;
//		Point Pos = new Point((int)(0.25*screenSize.width), (int)(0.8*screenSize.height)) ;
//		Size size = new Size((int)(0.5*screenSize.width), (int)(0.6*screenSize.height)) ;
//		int Sy = size.y / 10 ;
//		DrawMenuWindow(Pos, size, null, 0, ColorPalette[pet.getJob()], ColorPalette[7]) ;
//		DrawText(new Point(Pos.x + (int)(0.05 * size.x), Pos.y - (int)(0.95 * size.y)), alignPoints.bottomLeft, OverallAngle, AllText[WinCat][1], font, pet.getColor()) ;
//		if (counter % duration < duration/3)
//		{
//			DrawText(new Point(Pos.x + (int)(0.05 * size.x), Pos.y - (int)(0.95 * size.y) + Sy), alignPoints.bottomLeft, OverallAngle, AllText[AttCat][1] + " " + pet.getLevel(), font, pet.getColor()) ;		
//		} else if (counter % duration < 2*duration/3)
//		{
//			DrawText(new Point(Pos.x + (int)(0.05 * size.x), Pos.y - (int)(0.95 * size.y) + Sy), alignPoints.bottomLeft, OverallAngle, AllText[AttCat][1] + " " + pet.getLevel(), font, pet.getColor()) ;					
//			for (int i = 0 ; i <= AttributeIncrease.length - 1 ; ++i)
//			{
//				if (duration/3 + duration/15*i < counter % duration)
//				{
//					DrawText(new Point(Pos.x + (int)(0.05 * size.x), Pos.y - (int)(0.95 * size.y) + (i + 2)*Sy), alignPoints.bottomLeft, OverallAngle, AllText[AttCat][i + 2] + " + " + AttributeIncrease[i], font, pet.getColor()) ;								
//				}
//			}
//		}
//		
//	}
//	public void DrawMenuWindow(Point Pos, Scale scale, String Title, int type, Color color1, Color color2)
//	{
//		DrawImage(menuWindow, Pos, scale, Align.topLeft) ;
		/*if (type == 0)
		{
			DrawRoundRect(Pos, alignPoints.topLeft, size, 3, color1, color2, true) ;
			if (Title != null)
			{
				Font font = new Font("SansSerif", Font.BOLD, size.x * size.y / 3500) ;
				Point WindowPos = new Point((int) (Pos.x + 0.5 * size.x), (int) (Pos.y - size.y - 0.5*3*UtilG.TextH(font.getSize()))) ;
				Color TextColor = ColorPalette[9] ;
				DrawRoundRect(WindowPos, alignPoints.center, new Size((int)(0.6 * size.x), (int)(3*UtilG.TextH(font.getSize()))), 3, color1, color2, true) ;
				DrawText(WindowPos, alignPoints.center, OverallAngle, Title, font, TextColor) ;
			}
		}
		if (type == 1)
		{
		}*/
//	}
//	public void DrawTimeBar(Point pos, int counter, int delay, int size2, Point offset, String relPos, String dir, Color color)
//	{
//		Dimension size = new Dimension((int)(2 + size2/20), (int)(size2)) ;
//		int mirror = UtilS.MirrorFromRelPos(relPos) ;
//		int RectT = 1 ;
//		Color BackgroundColor = colorPalette[7] ;
//		if (dir.equals("Vertical"))
//		{
//			pos = new Point(pos.x + mirror*offset.x, pos.y + offset.y) ;
//			DrawRect(pos, Align.bottomLeft, size, RectT, BackgroundColor, colorPalette[9]) ;
//			DrawRect(pos, Align.bottomLeft, new Dimension(size.width, size.height * counter / delay), RectT, color, null) ;	
//		}
//		if (dir.equals("Horizontal"))
//		{
//			pos = new Point(pos.x + offset.x, pos.y + mirror*offset.y) ;
//			DrawRect(pos, Align.bottomLeft, new Dimension(size.height, size.width), RectT, BackgroundColor, colorPalette[9]) ;
//			DrawRect(new Point(pos.x - size.height / 2, pos.y + size.width / 2), Align.bottomLeft, new Dimension(size.height * counter / delay, size.width), RectT, color, null) ;	
//		}			
//	}
//	public void AttackAnimation(Point attackerPos, Point targetPos, Dimension targetSize, int effect, String elem, int counter, int duration)
//	{
//		int rate = counter / duration ;
//		if (effect == 0)
//		{
//			targetPos = new Point(targetPos.x - targetSize.width / 2, targetPos.y + targetSize.height / 2) ;
//			Color lineColor = colorPalette[9] ;
//			DrawLine(new Point(targetPos.x, targetPos.y - 15), new Point(targetPos.x + 50 * rate, targetPos.y - 15 - 50 * rate), 1, lineColor) ;
//			DrawLine(new Point(targetPos.x, targetPos.y), new Point(targetPos.x + 50 * rate, targetPos.y - 50 * rate), 1, lineColor) ;
//			DrawLine(new Point(targetPos.x, targetPos.y + 15), new Point(targetPos.x + 50 * rate, targetPos.y + 15 - 50 * rate), 1, lineColor) ;
//		}
//		else if (effect == 1 & -1 < UtilS.ElementID(elem))
//		{
//			Point imagePos = new Point(attackerPos.x + (targetPos.x - attackerPos.x) * rate, attackerPos.y + (targetPos.y - attackerPos.y) * rate) ;
//			DrawImage(ElementImages[UtilS.ElementID(elem)], imagePos, 0, new Scale(1.5, 1.5), Align.center) ;
//		}
//		else if (effect == 2)
//		{
//			float angle = (float) Math.atan((targetPos.y - attackerPos.y)/(targetPos.x - attackerPos.x)) ;
//			if (targetPos.x < attackerPos.x)
//			{
//				angle = (float) (angle*180/Math.PI) ;
//			}
//			else
//			{
//				angle = (float) (angle*180/Math.PI - 90) ;
//			}
//			DrawImage(Equip.ArrowImage, new Point(attackerPos.x + (targetPos.x - attackerPos.x) * rate, attackerPos.y + (targetPos.y - attackerPos.y) * rate),
//					angle, new Scale(1, 1), Align.center) ;
//		}
//	}
	/*public void CrazyArrowAnimation(int map, int counter, int looptime, Image CrazyArrowImage)
	{
		Point InitialPos = new Point(0, 0) ;
		float angle = -1 ;
		float dx = 0, dh = 0 ;
		if (map == 0)
		{
			InitialPos = new Point((int) (0.5*screenSize.width), (int) (0.5*screenSize.height)) ;
			angle = 0 ;
			dx = (float) (0.005*screenSize.width) ;
		}
		else if (map == 1)
		{
			InitialPos = new Point((int) (0.5*screenSize.width), (int) (0.5*screenSize.height)) ;
			angle = 90 ;
			dh = (float) (0.005*screenSize.height) ;
		}
		else if (map == 2)
		{
			InitialPos = new Point((int) (0.5*screenSize.width), (int) (0.5*screenSize.height)) ;
			angle = 180 ;
			dx = (float) (0.005*screenSize.width) ;
		}
		else if (map == 3)
		{
			InitialPos = new Point((int) (0.5*screenSize.width), (int) (0.8*screenSize.height)) ;
			angle = 180 ;
			dx = (float) (0.005*screenSize.width) ;
		}
		else if (map == 4)
		{
			InitialPos = new Point((int) (0.5*screenSize.width), (int) (0.5*screenSize.height)) ;
			angle = 90 ;
			dh = (float) (0.005*screenSize.height) ;
		}
		dx = dx*UtilS.UpAndDownCounter(counter, looptime) ;
		dh = dh*UtilS.UpAndDownCounter(counter, looptime) ;
		DrawImage(CrazyArrowImage, new Point((int) (InitialPos.x + dx), (int) (InitialPos.y + dh)), angle, new Scale(1, 1), alignPoints.center) ;
	}*/
	/*public void TutorialAnimations()
	{
		int font.getSize() = 20 ;
		 *Items.BagIDs[5], Items.EquipsBonus, Player.ActionKeys, 
		Point Pos = new int[] {(int) (0.35*WinDim[0]), (int) (0.5*WinDim[1])} ;
		int L = (int) (0.1*WinDim[0]), H = (int) (0.12*WinDim[1]) ;
		int TextH = Utg.TextH(font.getSize()) ;
    	int duration = 50 ;*/
		/*if (animation == 1)	// Basic attributes (life, mp, exp, satiation)
		{
			Pos = player.getPos() ;
			font.getSize() = 12 ;
			int Sy = (int)(0.01*WinDim[1]) ;
			float dx = (float) 0.8 ;
			int TextCat = AllTextCat[6] ;
			if (0 < counter & counter < duration)
			{
				dx = dx*Ut.UpAndDownCounter(counter, duration/10) ;
				DrawText(new Point((int) (Pos.x + player.getSize()[0]/2 + dx), Pos.y - player.getSize()[1]/2 - (int)(0.78*H)}, "Left", OverallAngle, AllText[TextCat][2], font, ColorPalette[6]) ;
				DrawText(new Point((int) (Pos.x + player.getSize()[0]/2 + dx), Pos.y - player.getSize()[1]/2 - (int)(0.78*H) + Sy}, "Left", OverallAngle, AllText[TextCat][3], font, ColorPalette[5]) ;
				DrawText(new Point((int) (Pos.x - 1.2*player.getSize()[0] + dx), Pos.y - player.getSize()[1]/2 - (int)(0.78*H) + 2*Sy}, "Right", OverallAngle, AllText[TextCat][18], font, ColorPalette[1]) ;
				DrawText(new Point((int) (Pos.x - 1.2*player.getSize()[0] + dx), Pos.y - player.getSize()[1]/2 - (int)(0.78*H) + 3*Sy}, "Right", OverallAngle, AllText[TextCat][20], font, ColorPalette[2]) ;
			}
		}*/
		/*if (animation == 4)	// Bag
		{		
			Pos = new int[] {(int) (0.2*WinDim[0]), (int) (0.75*WinDim[1])} ;
			float dh = 1 ;
			if (0 < counter & counter < duration)
			{
				dh = dh*Ut.UpAndDownCounter(counter, duration) ;
				DrawText(new Point(Pos.x, Pos.y - (int)(0.6*H + dh)}, alignPoints.center, OverallAngle, ActionKeys[0], SansFont, font.getSize(), ColorPalette[5]) ;	
				DrawText(new Point(Pos.x, (int) (Pos.y + dh)}, alignPoints.center, OverallAngle, ActionKeys[2], SansFont, font.getSize(), ColorPalette[5]) ;	
				DrawArrowIcon(new int[] {Pos.x, Pos.y - (int)(0.85*H + dh)}, alignPoints.center, 90, new float[] {1, 1}, new boolean[] {false, false}) ;
				DrawArrowIcon(new int[] {Pos.x, Pos.y + (int)(0.2*H + dh)}, alignPoints.center, 270, new float[] {1, 1}, new boolean[] {false, false}) ;
			}
		}
		if (animation == 17)	// Forge
		{
			if (0 < counter & counter < duration)
			{
				DrawMenuWindow(Pos, L, H, null, 0, ColorPalette[20], ColorPalette[20]) ;
				DrawEquips(new int[] {(int) (Pos.x + 0.5*L), (int) (Pos.y - 0.5*H + 0.5*TextH)}, player.getJob(), 0, FirstEquipID, EquipsBonus, new float[] {1, 1}, OverallAngle) ;
				DrawText(new Point((int) (Pos.x + 0.5*L), (int) (Pos.y - H + 0.6*TextH)}, alignPoints.center, OverallAngle, "+ 0", SansFont, font.getSize(), player.getColors()[0]) ;
			}
			if (duration/3 <= counter & counter < duration)
			{
				DrawImage(PointingArrowImage, new int[] {(int) (Pos.x + 1.2*L + PointingArrowImage.getWidth(null)/2), (int) (Pos.y - 0.5*H)}, OverallAngle, new float[] {1, 1}, new boolean[] {false, false}, alignPoints.center) ;
			}
			if (2*duration/3 <= counter & counter < duration)
			{
				DrawMenuWindow(new int[] {(int) (Pos.x + 1.4*L + PointingArrowImage.getWidth(null)), (int) (Pos.y)}, L, H, null, 0, ColorPalette[20], ColorPalette[20]) ;
				DrawEquips(new int[] {(int) (Pos.x + 1.9*L + PointingArrowImage.getWidth(null)), (int) (Pos.y - 0.5*H + 0.5*TextH)}, player.getJob(), 0, FirstEquipID, EquipsBonus, new float[] {1, 1}, OverallAngle) ;
				DrawText(new Point((int) (Pos.x + 1.9*L + PointingArrowImage.getWidth(null)), (int) (Pos.y - H + 0.6*TextH)}, alignPoints.center, OverallAngle, "+ 1", SansFont, font.getSize(), player.getColors()[0]) ;
			}
		}
	}*/

}
