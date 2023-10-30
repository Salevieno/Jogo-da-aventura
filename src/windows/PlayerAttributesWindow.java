package windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import attributes.Attributes;
import attributes.BasicBattleAttribute;
import components.GameButton;
import components.IconFunction;
import graphics.DrawingOnPanel;
import items.Equip;
import liveBeings.Player;
import main.Game;
import main.TextCategories;
import utilities.Align;
import utilities.Elements;
import utilities.Scale;
import utilities.UtilG;

public class PlayerAttributesWindow extends AttributesWindow
{
	
	private Point windowPos = new Point((int) (0.1 * Game.getScreen().getSize().width), (int)(0.2 * Game.getScreen().getSize().height)) ;
	private Player player ;
//	private Equip[] equips ;
//	private Arrow equippedArrow ;
	public Map<Attributes, GameButton> incAttButtons ;

	private Image tab1Image = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "PlayerAttWindow2.png") ;
	private Image tab2Image = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "PlayerAttWindow3.png") ;
	
	private Image plusSign = UtilG.loadImage(Game.ImagesPath + "\\SideBar\\" + "PlusSign.png") ;
	private Image selectedPlusSign = UtilG.loadImage(Game.ImagesPath + "\\SideBar\\" + "ShiningPlusSign.png") ;
	
	public PlayerAttributesWindow(Image image)
	{
		
		super(image, 3) ;
		
		incAttButtons = new HashMap<>() ;	
		
	}
	
	public void setPlayer(Player player) { this.player = player ;}
//	public void setEquips(Equip[] equips) { this.equips = equips ;}
//	public void setEquippedArrow(Arrow equippedArrow) { this.equippedArrow = equippedArrow ;}

	public void initializeAttIncButtons(Player player)
	{

		Point pos = UtilG.Translate(windowPos, 0, 180) ;
		for (Attributes att : Arrays.asList(Attributes.getIncrementable()))
		{
			IconFunction method = () -> {player.getBA().mapAttributes(att).incBaseValue(1) ;} ;
			GameButton newAttButton = new GameButton(pos, Align.topLeft, plusSign, selectedPlusSign, method) ;
			newAttButton.deactivate() ;
			incAttButtons.put(att, newAttButton) ;
			pos = UtilG.Translate(pos, 0, 22) ;
		}
		
	}
	
	public void updateAttIncButtons(Player player)
	{

		if (player.getAttPoints() <= 0) { incAttButtons.values().forEach(button -> button.deactivate()) ; return ;}
		
		incAttButtons.values().forEach(button -> button.activate()) ;
		
	}
	
	public void navigate(String action)
	{
		if (action.equals(Player.ActionKeys[2]))
		{
			tabUp() ;
		}
		if (action.equals(Player.ActionKeys[0]))
		{
			tabDown() ;
		}
	}
	
	public void activateIncAttButtons(int attPoints)
	{
		if (attPoints <= 0) { return ;}		

		incAttButtons.values().forEach(button -> button.activate()) ;		
	}
	
	public void act(Player player, Point mousePos, String action)
	{
		if (player.getAttPoints() <= 0) { return ;}
		
		incAttButtons.values().forEach(button -> {
			if (button.isClicked(mousePos, action))
			{
				button.act() ;
				player.decAttPoints(1) ;
			}
		});
		
		if (player.getAttPoints() <= 0)
		{
			incAttButtons.values().forEach(button -> button.deactivate()) ;	
		}
	}
	
	public void displayTab0(Point mousePos, DrawingOnPanel DP)
	{
		
		Color[] colorPalette = Game.colorPalette ;		
		Font font = subTitleFont ;
		double angle = DrawingOnPanel.stdAngle ;

		String[] classesText = Game.allText.get(TextCategories.classes) ;
		String[] proClassesText = Game.allText.get(TextCategories.proclasses) ;
		String[] attText = Game.allText.get(TextCategories.attributes) ;
		String[] equipsText = Game.allText.get(TextCategories.equipments) ;
		Color textColor = colorPalette[0] ;
		
		Image userImage = player.getMovingAni().idleGif ;
		Point userPos = UtilG.Translate(windowPos, size.width / 2, 120) ;
		DP.DrawImage(userImage, userPos, Align.center) ;

		Point levelPos = UtilG.Translate(windowPos, size.width / 2, 38) ;	
		DP.DrawText(levelPos, Align.center, angle, attText[0] + ": " + player.getLevel(), font, colorPalette[7]) ;
		
		String jobText = player.getProJob() == 0 ? classesText[player.getJob()] : proClassesText[2 * player.getJob() + player.getProJob() - 1] ;
		Point jobTextPos = UtilG.Translate(windowPos, size.width / 2, 56) ;
		DP.DrawText(jobTextPos, Align.center, angle, jobText, font, colorPalette[0]) ;
		
		
		//	Equips
		Equip[] equips = player.getEquips() ;
		if (equips != null)
		{
			int eqSlotSize = 51 ;
			Point[] eqSlotCenter = new Point[] {UtilG.Translate(windowPos, 70, 110), UtilG.Translate(windowPos, 259, 63), UtilG.Translate(windowPos, 259, 135)} ;
			for (int eq = 0 ; eq <= equips.length - 1 ; eq += 1)
			{
				if (equips[eq] == null) { continue ;}
				
				Equip equip = equips[eq] ;		

				DP.DrawImage(equip.fullSizeImage(), eqSlotCenter[eq], Align.center) ;
//				DP.DrawTextUntil(textPos, Align.bottomLeft, angle, equip.getName(), font, textColor, 14, mousePos) ;
				
				Point textPos = UtilG.Translate(eqSlotCenter[eq], 0, -eqSlotSize / 2 - 3) ;
				if (0 < equip.getForgeLevel())
				{
					DP.DrawText(textPos, Align.bottomCenter, angle, equipsText[eq] + " + " + equip.getForgeLevel(), font, textColor) ;					
				}
				
				Elements eqElem = player.getElem()[eq + 1] ;

				if (eqElem == null) { continue ;}
				
				int elemID = Elements.getID(eqElem) ;
				Image elemImage = DrawingOnPanel.ElementImages[elemID] ;
				Point elemPos = UtilG.Translate(eqSlotCenter[eq], eqSlotSize / 2 - 12, eqSlotSize / 2 - 12) ;

				DP.DrawImage(elemImage, elemPos, angle, new Scale(0.25, 0.25), Align.center) ;
			}
		}
		
		// Arrow
		if (player.getEquippedArrow() != null)
		{
			DP.DrawImage(player.getEquippedArrow().fullSizeImage(), UtilG.Translate(windowPos, 100, 133), Align.bottomCenter) ;
		}
		
		
		// super element
		if (player.hasSuperElement())
		{
			int elemID = Elements.getID(player.getElem()[4]) ;
			Point superElemPos = UtilG.Translate(userPos, 0, 35) ;
			Image superElemImage = DrawingOnPanel.ElementImages[elemID] ;
			DP.DrawImage(superElemImage, superElemPos, angle, new Scale(0.3, 0.3), Align.center) ;
		}
		
		
		// attributes
		Point lifePos = UtilG.Translate(windowPos, 22, 39) ;
		Point mpPos = UtilG.Translate(windowPos, 22, 58) ;
		String lifeText = attText[1] + ": " + UtilG.Round(player.getPA().getLife().getCurrentValue(), 1) ;
		String mpText = attText[2] + ": " + UtilG.Round(player.getPA().getMp().getCurrentValue(), 1) ;
		DP.DrawText(lifePos, Align.centerLeft, angle, lifeText, font, colorPalette[7]) ;
		DP.DrawText(mpPos, Align.centerLeft, angle, mpText, font, colorPalette[21]) ;
		
		BasicBattleAttribute[] attributes = player.getBA().basicAttributes() ;
		int attOffset = 4 ;
		Point initialAttPos = UtilG.Translate(windowPos, 44 + attOffset, 191) ;
		for (int i = 0; i <= attributes.length - 3; i += 1)
		{
			Point attValuePos = UtilG.Translate(initialAttPos, 0, i * 22) ;
			Point attImagePos = UtilG.Translate(windowPos, 35, 191 + i * 22) ;
			DP.DrawImage(attIcons[i], attImagePos, Scale.unit, Align.center) ;
			DP.DrawText(attValuePos, Align.centerLeft, angle, attributes[i].text(), font, textColor) ;
		}
		Point critPos = UtilG.Translate(initialAttPos, 0, 9 + (attributes.length - 2) * 22) ;
		String critValue = UtilG.Round(100 * player.getBA().TotalCritAtkChance(), 1) + "%" ;
		DP.DrawImage(critIcon, UtilG.Translate(windowPos, 35, 333), Scale.unit, Align.center) ;
		DP.DrawText(critPos, Align.centerLeft, angle, critValue, font, colorPalette[6]) ;		

		Point powerPos = UtilG.Translate(windowPos, 260, 300) ;
		player.displayPowerBar(powerPos, DP) ;
		
		//	Collecting
		DP.DrawImage(collectIcons[0], UtilG.Translate(windowPos, 188, 257), Scale.unit, Align.center) ;
		DP.DrawImage(collectIcons[1], UtilG.Translate(windowPos, 188, 279), Scale.unit, Align.center) ;
		DP.DrawImage(collectIcons[2], UtilG.Translate(windowPos, 188, 301), Scale.unit, Align.center) ;
		
		Point herbPos = UtilG.Translate(windowPos, 197 + attOffset, 257) ;
		Point woodPos = UtilG.Translate(windowPos, 197 + attOffset, 279) ;
		Point metalPos = UtilG.Translate(windowPos, 197 + attOffset, 301) ;
		String herbValue = String.valueOf(UtilG.Round(player.getCollect()[0], 1)) ;
		String woodValue = String.valueOf(UtilG.Round(player.getCollect()[1], 1)) ;
		String metalValue = String.valueOf(UtilG.Round(player.getCollect()[2], 1)) ;
		DP.DrawText(herbPos, Align.centerLeft, angle, herbValue, font, colorPalette[4]) ;
		DP.DrawText(woodPos, Align.centerLeft, angle, woodValue, font, colorPalette[8]) ;
		DP.DrawText(metalPos, Align.centerLeft, angle, metalValue, font, colorPalette[1]) ;


		//	Gold
		Point coinPos = UtilG.Translate(windowPos, 185, 332) ;
		Point goldPos = UtilG.Translate(windowPos, 197 + attOffset, 332) ;
		String goldValue = String.valueOf(UtilG.Round(player.getBag().getGold(), 1)) ;
		DP.DrawImage(Player.CoinIcon, coinPos, angle, Scale.unit, Align.center) ;
		DP.DrawText(goldPos, Align.centerLeft, angle, goldValue, font, Game.colorPalette[15]) ;
		
		incAttButtons.values().forEach(button -> button.display(angle, false, mousePos, DP)) ;
	}
	
	public void displayTab1(Player player, DrawingOnPanel DP)
	{
		
		Font font = new Font(Game.MainFontName, Font.BOLD, 11) ;
		Color textColor = Game.colorPalette[0] ;
		double angle = DrawingOnPanel.stdAngle ;
		String[] attText = Game.allText.get(TextCategories.attributes) ;
		
		int leftColX = 44 + 4 ;
		int rightColX = 197 + 4 ;
		int topRowY = 35 ;
		int secondRowY = 35 + 110 ;
		int bottomRowY = 35 + 261 ;
		
		// Titles
		DP.DrawText(UtilG.Translate(windowPos, leftColX, topRowY), Align.centerLeft, angle, attText[10], font, textColor) ;
		DP.DrawText(UtilG.Translate(windowPos, rightColX, topRowY), Align.centerLeft, angle, attText[11], font, textColor) ;
		DP.DrawText(UtilG.Translate(windowPos, leftColX, secondRowY), Align.centerLeft, angle, attText[12], font, textColor) ;
		DP.DrawText(UtilG.Translate(windowPos, rightColX, secondRowY), Align.centerLeft, angle, attText[13], font, textColor) ;
		DP.DrawText(UtilG.Translate(windowPos, leftColX, bottomRowY), Align.centerLeft, angle, attText[14], font, textColor) ;

		// att values
		Point stunValuesPos = UtilG.Translate(windowPos, leftColX, 56) ;
		Point blockValuesPos = UtilG.Translate(windowPos, rightColX, 56) ;
		Point bloodValuesPos = UtilG.Translate(windowPos, leftColX, 56 + 110) ;
		Point poisonValuesPos = UtilG.Translate(windowPos, rightColX, 56 + 110) ;
		Point silenceValuesPos = UtilG.Translate(windowPos, leftColX, 56 + 261) ;
		for (int i = 0 ; i <= 3 - 1 ; ++i)
		{
			DP.DrawText(stunValuesPos, Align.centerLeft, angle, player.getBA().getStun().texts()[i], font, textColor) ;
			DP.DrawText(blockValuesPos, Align.centerLeft, angle, player.getBA().getBlock().texts()[i], font, textColor) ;
			DP.DrawText(silenceValuesPos, Align.centerLeft, angle, player.getBA().getSilence().texts()[i], font, textColor) ;
			
			stunValuesPos.y += 22 ;
			blockValuesPos.y += 22 ;
			silenceValuesPos.x += 77 ;
		}
		for (int i = 0 ; i <= 5 - 1 ; ++i)
		{
			DP.DrawText(bloodValuesPos, Align.centerLeft, angle, player.getBA().getBlood().texts()[i], font, textColor) ;
			DP.DrawText(poisonValuesPos, Align.centerLeft, angle, player.getBA().getPoison().texts()[i], font, textColor) ;
			
			bloodValuesPos.y += 22 ;
			poisonValuesPos.y += 22 ;
		}
	}
	
	public void displayTab2(Player player, DrawingOnPanel DP)
	{
		
		Font font = new Font(Game.MainFontName, Font.BOLD, 11) ;
		Color textColor = Game.colorPalette[0] ;
		
		Map<String, Integer> numberStats = player.getStatistics().numberStats() ;
		Map<String, Double> damageStats = player.getStatistics().damageStats() ;
		Map<String, Double> maxStats = player.getStatistics().maxStats() ;
		Point[] titlesPos = new Point[] {UtilG.Translate(windowPos, size.width / 2, 35 + 12 + 6 - 2), UtilG.Translate(windowPos, size.width / 2, 195 - 2)} ;
		Point topLeft1 = UtilG.Translate(windowPos, 32 + 16, 35 + 25 + 10) ;
		Point topLeft2 = UtilG.Translate(windowPos, 32, 190 + 12) ;
		Point topLeft3 = UtilG.Translate(windowPos, 32 + 16, 195 + 25 + 27) ;
		
		// Titles
		DP.DrawText(titlesPos[0], Align.bottomCenter, DrawingOnPanel.stdAngle, "Totais", font, textColor) ;
		DP.DrawText(titlesPos[1], Align.bottomCenter, DrawingOnPanel.stdAngle, "Danos", font, textColor) ;

		// subtitles
		DP.DrawText(UtilG.Translate(windowPos, 21 + 48, 195 + 25 + 6), Align.topCenter, DrawingOnPanel.stdAngle, "Causados", font, textColor) ;
		DP.DrawText(UtilG.Translate(windowPos, 21 + 3 * 48, 195 + 25 + 6), Align.topCenter, DrawingOnPanel.stdAngle, "Recebidos", font, textColor) ;
		DP.DrawText(UtilG.Translate(windowPos, 21 + 5 * 48, 195 + 25 + 6), Align.topCenter, DrawingOnPanel.stdAngle, "Defendidos", font, textColor) ;
		
		// number stats
		int i = 0 ;
		for (String key : numberStats.keySet())
		{
			String text = String.valueOf(numberStats.get(key)) ;
			Point textPos = UtilG.Translate(topLeft1, (i / 6) * 140, (i % 6) * 18) ;
			DP.DrawText(textPos, Align.centerLeft, DrawingOnPanel.stdAngle, text, font, textColor) ;
			i += 1 ;
		}
		
		// damage stats
		i = 0 ;
		for (String key : damageStats.keySet())
		{
			String text = String.valueOf(UtilG.Round((double) damageStats.get(key), 1)) ;
			Point textPos = UtilG.Translate(topLeft3, (i % 3) * 96, (i / 3) * 18) ;
			DP.DrawText(textPos, Align.centerLeft, DrawingOnPanel.stdAngle, text, font, textColor) ;
			i += 1 ;
		}
		
		// max stats
		i = 0 ;
		for (String key : maxStats.keySet())
		{
			String text = key + ": " + String.valueOf(UtilG.Round((double) maxStats.get(key), 1)) ;
			Point textPos = UtilG.Translate(topLeft2, (i + 1) * 70, 0) ;
			DP.DrawText(textPos, Align.centerLeft, DrawingOnPanel.stdAngle, text, font, textColor) ;
			i += 1 ;
		}
		
	}
		
	public void display(Point mousePos, DrawingOnPanel DP)
	{
		
		String[] tabsText = Game.allText.get(TextCategories.playerWindow) ;
		Image windowImage = tab == 0 ? image : (tab == 1 ? tab1Image : tab2Image) ;
		
		// Main window
		DP.DrawImage(windowImage, windowPos, Align.topLeft) ;

		Point tabsTextPos = UtilG.Translate(windowPos, 7, 6 + 22) ;
		for (int i = 0 ; i <= 3 - 1 ; i += 1)
		{
			Color tabTextColor = i == tab ? Game.colorPalette[18] : Game.colorPalette[0] ;
			DP.DrawText(tabsTextPos, Align.center, 90, tabsText[i], stdFont, tabTextColor) ;
			tabsTextPos.y += 45 ;
		}
			
		switch (tab)
		{
			case 0: displayTab0(mousePos, DP) ; break ;
			case 1: displayTab1(player, DP) ; break ;
			case 2: displayTab2(player, DP) ; break ;
		}		

		// Player name
		Point namePos = UtilG.Translate(windowPos, size.width / 2, 11) ;
		DP.DrawText(namePos, Align.center, DrawingOnPanel.stdAngle, player.getName(), titleFont, Game.colorPalette[0]) ;	
		
	}
	
}
