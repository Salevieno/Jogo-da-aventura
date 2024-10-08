package graphics ;

import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Font ;
import java.awt.Image ;
import java.awt.Point;
import java.util.Arrays;
import java.util.List;

import components.GameButton;
import items.Item;
import libUtil.Align;
import libUtil.Util;
import liveBeings.Pet;
import liveBeings.Player;
import liveBeings.PlayerActions;
import main.AtkResults;
import main.AtkTypes;
import main.Battle;
import main.Game;
import main.TextCategories;
import maps.Continents;
import maps.FieldMap;
import maps.GameMap;
import screen.Sky;
import utilities.AtkEffects;
import utilities.Scale;
import utilities.TimeCounter;
import utilities.UtilS;
import windows.AttributesWindow;
import windows.BagWindow;
import windows.PlayerAttributesWindow;

public abstract class Draw 
{
	
	private static DrawPrimitives DP ;
	private static Dimension screenSize ;
	private static final Image ArrowIconImage ;
	public static final Image KeyboardButtonImage ;
	public static double stdAngle ;
	
	private static final Font stdFont = DrawPrimitives.stdFont ;
	private static final Font smallFont = new Font(DrawPrimitives.stdFont.getName(), Font.BOLD, 10) ;
	
	static
	{
		screenSize = Game.getScreen().getSize() ;
		stdAngle = DrawPrimitives.stdAngle;
		ArrowIconImage = UtilS.loadImage("\\Windows\\" + "ArrowIcon.png") ;
		KeyboardButtonImage = UtilS.loadImage("KeyboardButton.png") ;
	}

	public static void setDP(DrawPrimitives newDP)
	{
		DP = newDP ;
	}

	public static void gif(Image gif, Point pos, Align align)
	{
		
		Dimension size = new Dimension(gif.getWidth(null), gif.getHeight(null)) ;
		Point offset = Util.offsetForAlignment(align, size) ;
		DP.drawImage(gif, Util.Translate(pos, offset.x, offset.y), align);
//		graphs.drawImage(gif, pos.x + offset.x, pos.y + offset.y, null) ;
	}
	
	public static void fitText(Point pos, int sy, Align align, String text, Font font, int maxLength, Color color)
	{
		List<String> FitText = Util.FitText(text, maxLength) ;
		for (int i = 0 ; i <= FitText.size() - 1 ; i += 1)
		{
			DP.drawText(new Point(pos.x, pos.y + i*sy), align, stdAngle, FitText.get(i), font, color) ;						
		}
	}
	
	public static void textUntil(Point pos, Align align, double angle, String text, Font font, Color color, int maxLength, Point mousePos)
	{
		Point offset = Util.offsetForAlignment(align, new Dimension(maxLength, Util.TextH(font.getSize()))) ;
		int minlength = 3 ;	// 3 is the length of "..."
		String shortText = text ;
		maxLength = Math.max(maxLength, minlength) ;
		if (maxLength < text.length())
		{
			char[] chararray = new char[maxLength - minlength] ;
			text.getChars(0, maxLength - 4, chararray, 0) ;
			shortText = String.valueOf(chararray) ;
		}
		
		Point topLeftPos = Util.Translate(pos, offset.x, offset.y) ;
		Dimension size = new Dimension(DP.TextL(shortText, font), Util.TextH(font.getSize())) ;
		String textDrawn =  text.length() <= maxLength | Util.isInside(mousePos, topLeftPos, size) ? text : shortText + "..." ;
		DP.drawText(pos, align, stdAngle, textDrawn, font, color) ;
	}
	
	public static void speech(Point pos, String text, Font font, Image speechBubble, Color color)
	{
		// obs: text must end with . , ? or ! for this function to work
		int bubbleL = speechBubble.getWidth(null) ;
		int bubbleH = speechBubble.getHeight(null) ;
		boolean flipH = Game.getScreen().getSize().width / 2 <= pos.x ;
		Color textColor = color != null ? color : Game.colorPalette[21] ;
		
		DP.drawImage(speechBubble, pos, DrawPrimitives.stdAngle, Scale.unit, flipH, false, Align.bottomCenter, 1) ;
		
		Point textOffset = new Point(6, 5) ;
		Point textPos = Util.Translate(pos, textOffset.x - bubbleL / 2, textOffset.y - bubbleH) ;
		int maxTextL = 35 ;
		int sy = font.getSize() + 1 ;
		fitText(textPos, sy, Align.topLeft, text, font, maxTextL, textColor) ;
	}

	public static void keyboardButton(Point pos, String key)
	{
		keyboardButton(pos, key, Game.colorPalette[0]) ;
	}
	
	public static void keyboardButton(Point pos, String key, Color color)
	{
		DP.drawImage(Draw.KeyboardButtonImage, pos, Draw.stdAngle, Scale.unit, Align.center) ;
		DP.drawText(pos, Align.center, Draw.stdAngle, key, Draw.stdFont, Game.colorPalette[0]) ;	
	}
	
	public static void windowArrows(Point pos, int width, int selectedWindow, int numberWindows, double opacity)
	{
		if (0 < selectedWindow)
		{
			Point leftArrowPos = Util.Translate(pos, 25, 0) ;
			Point textPos = Util.Translate(leftArrowPos, 18, 0) ;
			DP.drawImage(ArrowIconImage, leftArrowPos, stdAngle, new Scale(-1, -1), Align.center, opacity) ;
			Draw.keyboardButton(textPos, PlayerActions.moveLeft.getKey()) ;			
		}
		if (selectedWindow < numberWindows - 1)
		{
			Point rightArrowPos = Util.Translate(pos, width - 25, 0) ;
			Point textPos = Util.Translate(rightArrowPos, -18, 0) ;
			DP.drawImage(ArrowIconImage, rightArrowPos, stdAngle, new Scale(1, -1), Align.center, opacity) ;
			Draw.keyboardButton(textPos, PlayerActions.moveRight.getKey()) ;	
		}
	}
	
	public static void loadingText(Image LoadingGif, Point Pos) { gif(LoadingGif, Pos, Align.center) ;}
	
	public static void loadingGameScreen(Player player, Pet pet, GameButton[] icons, int SlotID, int NumberOfUsedSlots, Image GoldCoinImage)
	{
		Point[] WindowPos = new Point[] {Game.getScreen().pos(0.15, 0.2), Game.getScreen().pos(0.65, 0.2), Game.getScreen().pos(0.5, 0.2)} ;
		Font font = new Font("SansSerif", Font.BOLD, 28) ;
		
		DP.drawText(Game.getScreen().pos(0.5, 0.05), Align.center, stdAngle, "Slot " + (SlotID + 1), font, Game.colorPalette[5]) ;
		((PlayerAttributesWindow) player.getAttWindow()).display(new Point(0, 0), DP) ;
		if (0 < pet.getLife().getCurrentValue())
		{
 			//pet.getAttWindow().display(pet, allText, null, null, NumberOfUsedSlots, null, null, null, null);
		}
 		if (ArrowIconImage == null) { return ;}
		
 		windowArrows(WindowPos[2], (int)(0.5*screenSize.width), SlotID, NumberOfUsedSlots - 1, 1.0) ;
	}
	
	public static void emptyLoadingSlot(int SlotID, int NumSlots)
	{
		Point[] WindowPos = new Point[] {new Point((int)(0.35*screenSize.width), (int)(0.2*screenSize.height)),
				new Point((int)(0.65*screenSize.width), (int)(0.2*screenSize.height)),
				new Point((int)(0.5*screenSize.width), (int)(0.2*screenSize.height))} ;
		DP.drawRoundRect(WindowPos[0], Align.topLeft, new Dimension(screenSize.width / 3, screenSize.height / 2), 2, Game.colorPalette[3], true) ;
		DP.drawText(new Point(WindowPos[0].x + screenSize.width / 6, WindowPos[0].y + screenSize.height / 4), Align.center, stdAngle, "Slot " + String.valueOf(SlotID + 1) + " is empty", new Font("SansSerif", Font.BOLD, 20), Game.colorPalette[5]) ;
		windowArrows(new Point(WindowPos[0].x, WindowPos[0].y + screenSize.height / 2), screenSize.width / 3, SlotID, NumSlots, 1.0) ;
	}

	public static void gameGrid(int[] spacing)
	{
		for (int i = 0 ; i <= screenSize.width/spacing[0] - 1 ; ++i)
		{
			int LineThickness = 1 ;
			Color color = Game.colorPalette[21] ;
			if (i % 10 == 0)
			{
				LineThickness = 2 ;
			}
			if (i % 20 == 0)
			{
				LineThickness = 2 ;
				color = Game.colorPalette[5] ;
			}
			DP.drawLine(new Point(i*spacing[0], 0), new Point(i*spacing[0], screenSize.height), LineThickness, color) ;
			for (int j = 0 ; j <= screenSize.height/spacing[1] - 1 ; ++j)
			{
				LineThickness = 1 ;
				color = Game.colorPalette[21] ;
				if (j % 10 == 0)
				{
					LineThickness = 2 ;
				}
				if (j % 20 == 0)
				{
					LineThickness = 2 ;
					color = Game.colorPalette[5] ;
				}
				DP.drawLine(new Point(0, j*spacing[1]), new Point(screenSize.width, j*spacing[1]), LineThickness, color) ;
			}							
		}
	}
	
	public static void menu(Point pos, Align align, Dimension size)
	{
		DP.drawRoundRect(pos, align, size, 1, Game.colorPalette[3], true);
	}
	
	public static void time(Sky sky)
	{
		float time = (float) Sky.dayTimeRate() ;
		String message = (int) (24 * time) + ":" + (int) (24 * 60 * time % 60) ;
		DP.drawText(Game.getScreen().pos(0, 0.99), Align.bottomLeft, stdAngle, message, stdFont, Game.colorPalette[20]) ;
	}
	
	public static void map(GameMap map, Sky sky)
	{
		if (!map.getContinent().equals(Continents.cave))
		{
			sky.display(DP) ;
		}
		map.display(DP) ;
		map.displayGroundTypes(DP) ;
	}
	
	public static void mapElements(Point playerPos, GameMap map, Sky sky)
	{		
		map.displayElements(playerPos, DP) ;
		map.displayBuildings(playerPos, Arrays.asList(Game.getMaps()).indexOf(map), DP) ;
		map.displayNPCs(playerPos, DP) ;
		
		if (map instanceof FieldMap)
		{
			FieldMap fm = (FieldMap) map ;
			fm.displayCollectibles(DP) ;
		}
		map.displayTudoEstaBem(DP);
		time(sky) ;
	}

	public static void damageAnimation(Point initialPos, AtkResults atkResults, TimeCounter counter, int style, Color color)
	{
		AtkEffects effect = atkResults.getEffect() ;
		
		if (effect == AtkEffects.none) { System.out.println("Damage animation with effect = none") ; return ;}

		String damage = String.valueOf(Util.Round(atkResults.getDamage(), 1)) ;
		String message = switch (effect)
		{
			case miss -> "Miss" ;
			case hit -> damage ;
			case crit -> damage + "!" ;
			case block -> "Block" ;
			default -> "" ;
		} ;

		double rate = Math.pow(counter.rate(), 0.6) ;
		
		Point trajectory = switch (style)
		{
			case 0 -> new Point(0, (int) (-20 * rate)) ;
			case 1 -> new Point((int) (Math.pow(8 * rate, 2)), (int) (-20 * rate)) ;
			default -> new Point(0, 0) ;
		} ;

		Font font = new Font(Game.MainFontName, Font.BOLD, 12) ;
		Point currentPos = Util.Translate(initialPos, trajectory) ;
		Color textColor = color != null ? color : (atkResults.getAtkType().equals(AtkTypes.magical) ? Battle.magAtkColor : Battle.phyAtkColor) ;
		DP.drawText(currentPos, Align.center, stdAngle, message, font, textColor) ;
		
	}
	
	public static void skillNameAnimation(Point Pos, String SkillName, Color color)
	{
		Font largeFont = new Font("SansSerif", Font.BOLD, 18) ;
		DP.drawText(Pos, Align.center, stdAngle, SkillName, largeFont, color) ;
	}
	
	public static void tentAnimation(Point Pos, int counter, int delay, Image TentImage)
	{
		DP.drawImage(TentImage, Pos, Align.center) ;
	}

	public static void gainGoldAnimation(TimeCounter counter, int goldObtained)
	{
		
		Point pos = Game.getScreen().pos(0.45, 0.1) ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		Color textColor = Game.colorPalette[14] ;

		Point textPos = Util.Translate(pos, 5, 0) ;
		DP.drawText(textPos, Align.centerLeft, stdAngle, "+", font, textColor) ;
		
		Point coinPos = Util.Translate(pos, 15, 0) ;
		DP.drawImage(Player.CoinIcon, coinPos, Scale.unit, Align.centerLeft) ;
		
		Point amountPos = Util.Translate(pos, 35, 0) ;
		DP.drawText(amountPos, Align.centerLeft, stdAngle, String.valueOf(goldObtained), font, textColor) ;
		
	}

	public static void quickTextAnimation(Point pos, TimeCounter counter, String text, Color color)
	{
		pos = Util.Translate(pos, 0, (int) (-30 * counter.rate())) ;
		DP.drawImage(Animation.messageBox, pos, stdAngle, Scale.unit, Align.topCenter, 0.9) ;
		DP.drawText(Util.Translate(pos, 5 - Animation.messageBox.getWidth(null) / 2, 20), Align.centerLeft, stdAngle, text, smallFont, color) ;
		
	}

	public static void obtainedItemAnimation(Point pos, TimeCounter counter, String text, Color color)
	{
		pos = Util.Translate(pos, 0, (int) (-30 * counter.rate())) ;
		DP.drawImage(Animation.obtainedItem, pos, Align.topCenter) ;
		DP.drawText(Util.Translate(pos, 0, 3), Align.topCenter, stdAngle, "Você obteve", stdFont, color) ;
		DP.drawText(Util.Translate(pos, 5 - Animation.obtainedItem.getWidth(null) / 2, 20), Align.topLeft, stdAngle, text, smallFont, color) ;
		
	}

	public static void winAnimation(TimeCounter counter, List<Item> items)
	{
		Point pos = Game.getScreen().pos(0.35, 0.2) ;

		DP.drawImage(Animation.win, pos, Scale.unit, Align.topLeft) ;
		
		if ( counter.rate() <= 0.1 ) { return ;}
		
		Point textPos = Util.Translate(pos, 65, 15) ;
		DP.drawText(textPos, Align.bottomCenter, stdAngle, "Você obteve!", stdFont, Game.colorPalette[0]) ;
		
		if ( counter.rate() <= 0.3 ) { return ;}
		
		for (int i = 0 ; i <= items.size() - 1 ; i += 1)
		{
			if ( 0.3 + 0.5 * i / items.size() <= counter.rate() )
			{
				Point itemTextPos = Util.Translate(pos, 10, 20 + (i + 1) * 15) ;
				DP.drawText(itemTextPos, Align.bottomLeft, stdAngle, items.get(i).getName(), smallFont, Game.colorPalette[3]) ;
			}
		}
	}
	
	public static void levelUpAnimation(TimeCounter counter, double[] attributeInc, int newLevel)
	{

		Point pos = Game.getScreen().pos(0.55, 0.2) ;
		Point offset = new Point(15, 15) ;
		String[] attText = Game.allText.get(TextCategories.attributes) ;
		
		DP.drawImage(Animation.levelUp, pos, Scale.unit, Align.topLeft) ;
		Point textPos = Util.Translate(pos, Animation.win.getWidth(null) / 2, offset.y) ;
		
		DP.drawText(textPos, Align.bottomCenter, stdAngle, attText[0] + " " + newLevel + "!", smallFont, Game.colorPalette[0]) ;

		int nRows = 4 ;
		int nCols = 2 ;
		int sy = smallFont.getSize() + 15 ;
		Point topLeftSlotCenter = Util.Translate(pos, 18, 35) ;
		int[] attOrder = new int[] {0, 2, 4, 6, 1, 3, 5, 7} ;
		for (int i = 0 ; i <= attOrder.length - 1 ; i += 1)
		{
			Point imagePos = Util.calcGridPos(topLeftSlotCenter, i, nRows, new Point(80, sy)) ;
			DP.drawImage(BagWindow.slotImage, imagePos, Align.center) ;
			DP.drawImage(AttributesWindow.getIcons()[attOrder[i]], imagePos, Align.center) ;
		}
		
		if (counter.rate() <= 0.2) { return ;}
		
		String[] attNames = Arrays.copyOfRange(attText, 1, 9) ;
		
		for (int i = 0 ; i <= attNames.length - 1 ; i += 1)
		{
			if (counter.rate() <= 0.2 + 0.5 * i / (attNames.length - 1)) { continue ;}

			int row = i % nCols ;
			int col = i / nCols ;
			Point attTextPos = Util.Translate(pos, 28 + row * 80, 40 + col * sy) ;
			DP.drawText(attTextPos, Align.bottomLeft, stdAngle, " + " + attributeInc[i], smallFont, Game.colorPalette[0]) ;
		}
		
	}
	
//	public static void sailingAnimation(Image playerImage, Image sailorImage, Image boatImage, TimeCounter counter, String destination)
//	{
////		int Step = player.getStep()/2 ;
////		Dimension sailorSize = new Dimension(sailorImage.getWidth(null), sailorImage.getHeight(null)) ;
//		if (destination.equals("Island"))
//		{
//			
//		}
//		int step = 1 ;
//		Point startPos = new Point(step, (int)(0.5*screenSize.height)) ;	
//		Point currentPos = Util.Translate(startPos, (int) (step * counter.rate()), 0) ;
//
//		DP.drawImage(boatImage, currentPos, Align.center) ;
//		DP.drawImage(sailorImage, currentPos, Align.center) ;
//		DP.drawImage(playerImage, currentPos, Align.center) ;
//		
//		if (Game.getScreen().posIsWithinBorders(currentPos)) { return ;}
//		
//		
////		else
////		{
////			player.setPos(startPos) ;
////			player.setMap(maps[player.getMap().getid() + 1]) ;
////			if (player.getMap().getid() == 65)
////			{
////				player.setPos(new Point(20, 500)) ;
////				player.setMap(maps[40]) ;
////			}
////		}
////		else if (Destination.equals("Forest"))
////		{
////			Point InitialPos = new Point(screenSize.width - Step, (int)(0.5*screenSize.height)) ;
////			Point Pos = new Point((InitialPos.x - Step*(counter % (screenSize.width/Step - 1))) % screenSize.width, InitialPos.y) ;
////			if (0 < Pos.x - Step)
////			{
////				DrawImage(BoatImage, Pos, OverallAngle, new float[] {(float)1, (float)1}, new boolean[] {false, false}, alignPoints.bottomLeft, 1) ;
////				DrawImage(npc, new Point(Pos.x + NPCLength, (int) (Pos.y - 0.5*NPCHeight)), OverallAngle, new float[] {(float)0.5, (float)0.5}, new boolean[] {false, false}, alignPoints.center, 1) ;
////				player.setPos(new Point(Pos.x - Step, Pos.y)) ;
////			}
////			else
////			{
////				player.setPos(InitialPos) ;
////				player.setMap(maps[player.getMap().getid() - 1]) ;
////				if (player.getMap().getid() == 60)
////				{
////					player.setPos(new Point(640, 500)) ;
////					player.setMap(maps[13]) ;
////					//if (MusicIsOn)
////					//{
////					//	UtilGeral.SwitchMusic(Music[11], Music[MusicInMap[player.getMap()]]) ;
////					//}
////				}
////			}
////		}
//	}
		
	public static void pterodactileAnimation(TimeCounter counter, Image pterodactile, Image speakingBubble, String[] message)
	{
		int imageWidth = pterodactile.getWidth(null) ;
		Point pos = new Point(-imageWidth / 2, (int)(0.25*screenSize.height)) ;
		
		if (counter.rate() <= 0.25)
		{
			pos.x += 4 * counter.rate() * (screenSize.width / 2 + imageWidth / 2) ;
			pos.y += screenSize.height * counter.rate() ;
		}
		else if (counter.rate() <= 0.5)
		{
			pos.x += screenSize.width / 2 + imageWidth / 2 ;
			pos.y += 0.25 * screenSize.height ;
			speech(Util.Translate(pos, 0, -10), message[0], stdFont, speakingBubble, Game.colorPalette[19]) ;
		}
		else if (counter.rate() <= 0.75)
		{
			pos.x +=screenSize.width / 2 + imageWidth / 2 ;
			pos.y += 0.25 * screenSize.height ;
			speech(Util.Translate(pos, 0, -10), message[1], stdFont, speakingBubble, Game.colorPalette[19]) ;
		}
		else
		{
			pos.x += (screenSize.width / 2 + imageWidth / 2) + 2 * (screenSize.width + imageWidth) * (counter.rate() - 0.75) ;
			pos.y += 0.25 * screenSize.height - 1 * screenSize.height * (counter.rate() - 0.75) ;
		}
		DP.drawImage(pterodactile, pos, Align.center) ;
		
	}
	
}