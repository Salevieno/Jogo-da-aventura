package windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import attributes.Attributes;
import attributes.BasicBattleAttribute;
import components.GameButton;
import components.IconFunction;
import graphics.Draw;
import graphics.DrawPrimitives;
import items.Equip;
import libUtil.Align;
import libUtil.Util;
import liveBeings.Player;
import main.Game;
import main.TextCategories;
import utilities.Elements;
import utilities.Scale;
import utilities.UtilS;

public class PlayerAttributesWindow extends AttributesWindow
{
	
	private Point windowPos = Game.getScreen().pos(0.01, 0.2) ;
	private Player player ;
	public Map<Attributes, GameButton> incAttButtons ;

	public static final Image tab0Image ;
	public static final Image tab1Image ;
	public static final Image tab2Image ;	
	private static final Image plusSign ;
	private static final Image selectedPlusSign ;
	
	static
	{
		tab0Image = UtilS.loadImage("\\Windows\\" + "PlayerAttWindow1.png") ;
		tab1Image = UtilS.loadImage("\\Windows\\" + "PlayerAttWindow2.png") ;
		tab2Image = UtilS.loadImage("\\Windows\\" + "PlayerAttWindow3.png") ;
		plusSign = UtilS.loadImage("\\SideBar\\" + "PlusSign.png") ;
		selectedPlusSign = UtilS.loadImage("\\SideBar\\" + "ShiningPlusSign.png") ;
	}
	
	public PlayerAttributesWindow()
	{
		
		super(tab0Image, 3) ;
		
		incAttButtons = new HashMap<>() ;	
		
	}
	
	public void setPlayer(Player player) { this.player = player ;}

	public void initializeAttIncButtons(Player player)
	{

		Point pos = Util.Translate(windowPos, 27, 180) ;
		for (Attributes att : Arrays.asList(Attributes.getIncrementable()))
		{
			IconFunction method = () -> {player.getBA().mapAttributes(att).incBaseValue(1) ;} ;
			GameButton newAttButton = new GameButton(pos, Align.center, plusSign, selectedPlusSign, method) ;
			newAttButton.deactivate() ;
			incAttButtons.put(att, newAttButton) ;
			pos = Util.Translate(pos, 0, 22) ;
		}
		
	}
	
	public void updateAttIncButtons(Player player)
	{

		if (player.getAttPoints() <= 0) { incAttButtons.values().forEach(button -> button.deactivate()) ; return ;}
		
		incAttButtons.values().forEach(button -> button.activate()) ;
		
	}
	
	public void navigate(String action)
	{
		if (action.equals(stdMenuDown))
		{
			tabUp() ;
		}
		if (action.equals(stdMenuUp))
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
	
	public void displayTab0(Point mousePos, DrawPrimitives DP)
	{
		
		Color[] colorPalette = Game.colorPalette ;		
		Font font = subTitleFont ;
		Font smallFont = new Font(Game.MainFontName, Font.BOLD, 9) ;
		double angle = Draw.stdAngle ;

		String[] classesText = Game.allText.get(TextCategories.classes) ;
		String[] proClassesText = Game.allText.get(TextCategories.proclasses) ;
		String[] attText = Game.allText.get(TextCategories.attributes) ;
		String[] equipsText = Game.allText.get(TextCategories.equipments) ;
		Color textColor = colorPalette[0] ;
		
		Image userImage = player.getMovingAni().idleGif ;
		Point userPos = Util.Translate(windowPos, size.width / 2, 120) ;
		DP.drawImage(userImage, userPos, Align.center) ;

		Point levelPos = Util.Translate(windowPos, size.width / 2, 38) ;	
		DP.drawText(levelPos, Align.center, angle, attText[0] + ": " + player.getLevel(), font, colorPalette[7]) ;
		
		String jobText = player.getProJob() == 0 ? classesText[player.getJob()] : proClassesText[2 * player.getJob() + player.getProJob() - 1] ;
		Point jobTextPos = Util.Translate(windowPos, size.width / 2, 56) ;
		DP.drawText(jobTextPos, Align.center, angle, jobText, font, colorPalette[0]) ;
		
		
		//	Equips
		Equip[] equips = player.getEquips() ;
		if (equips != null)
		{
			int eqSlotSize = 51 ;
			Point[] eqSlotCenter = new Point[] {
					Util.Translate(windowPos, 70, 110),
					Util.Translate(windowPos, 259, 63),
					Util.Translate(windowPos, 259, 135),
					Util.Translate(windowPos, 165, 80)} ;
			for (int eq = 0 ; eq <= equips.length - 1 ; eq += 1)
			{
				if (equips[eq] == null) { continue ;}
				
				Equip equip = equips[eq] ;
				Point bottomTextPos = Util.Translate(eqSlotCenter[eq], -eqSlotSize / 2, eqSlotSize / 2 + 13) ;

				DP.drawImage(equip.fullSizeImage(), eqSlotCenter[eq], Align.center) ;
				Draw.textUntil(bottomTextPos, Align.bottomLeft, angle, equip.getName(), smallFont, textColor, 14, mousePos) ;

				Point upperTextPos = Util.Translate(eqSlotCenter[eq], 0, -eqSlotSize / 2 - 3) ;
				if (0 < equip.getForgeLevel())
				{
					DP.drawText(upperTextPos, Align.bottomCenter, angle, equipsText[eq] + " + " + equip.getForgeLevel(), smallFont, textColor) ;					
				}
				
				Elements eqElem = player.getElem()[eq + 1] ;

				if (eqElem == null || eq == 3) { continue ;}
				
				Image elemImage = eqElem.image ;
				Point elemPos = Util.Translate(eqSlotCenter[eq], eqSlotSize / 2 - 12, eqSlotSize / 2 - 12) ;

				DP.drawImage(elemImage, elemPos, angle, new Scale(0.5, 0.5), Align.center) ;
			}
		}
		
		// Arrow
		if (player.getEquippedArrow() != null)
		{
			DP.drawImage(player.getEquippedArrow().fullSizeImage(), Util.Translate(windowPos, 100, 133), Align.bottomCenter) ;
		}
		
		// super element
		if (player.hasSuperElement())
		{
			Point superElemPos = Util.Translate(userPos, 0, 35) ;
			Image superElemImage = player.getElem()[4].image ;
			DP.drawImage(superElemImage, superElemPos, angle, new Scale(0.3, 0.3), Align.center) ;
		}
		
		
		// attributes
		Point lifePos = Util.Translate(windowPos, 22, 39) ;
		Point mpPos = Util.Translate(windowPos, 22, 58) ;
		String lifeText = attText[1] + ": " + Util.Round(player.getPA().getLife().getTotalValue(), 1) ;
		String mpText = attText[2] + ": " + Util.Round(player.getPA().getMp().getTotalValue(), 1) ;
		DP.drawText(lifePos, Align.centerLeft, angle, lifeText, font, colorPalette[7]) ;
		DP.drawText(mpPos, Align.centerLeft, angle, mpText, font, colorPalette[20]) ;
		
		BasicBattleAttribute[] attributes = player.getBA().basicAttributes() ;
		int attOffset = 4 ;
		Point initialAttPos = Util.Translate(windowPos, 44 + attOffset, 191) ;
		for (int i = 0; i <= attributes.length - 3; i += 1)
		{
			Point attValuePos = Util.Translate(initialAttPos, 0, i * 22) ;
			Point attImagePos = Util.Translate(windowPos, 35, 191 + i * 22) ;
			DP.drawImage(attIcons[i], attImagePos, Scale.unit, Align.center) ;
			DP.drawText(attValuePos, Align.centerLeft, angle, attributes[i].text(), font, textColor) ;
		}
		Point critPos = Util.Translate(initialAttPos, 0, 9 + (attributes.length - 2) * 22) ;
		String critValue = Util.Round(100 * player.getBA().TotalCritAtkChance(), 1) + "%" ;
		DP.drawImage(critIcon, Util.Translate(windowPos, 35, 333), Scale.unit, Align.center) ;
		DP.drawText(critPos, Align.centerLeft, angle, critValue, font, colorPalette[6]) ;		

		// Power bar
		Point powerPos = Util.Translate(windowPos, 260, 310) ;
		player.displayPowerBar(powerPos, DP) ;
		
		//	Collecting
		DP.drawImage(collectIcons[0], Util.Translate(windowPos, 188, 257), Scale.unit, Align.center) ;
		DP.drawImage(collectIcons[1], Util.Translate(windowPos, 188, 279), Scale.unit, Align.center) ;
		DP.drawImage(collectIcons[2], Util.Translate(windowPos, 188, 301), Scale.unit, Align.center) ;
		
		Point herbPos = Util.Translate(windowPos, 197 + attOffset, 257) ;
		Point woodPos = Util.Translate(windowPos, 197 + attOffset, 279) ;
		Point metalPos = Util.Translate(windowPos, 197 + attOffset, 301) ;
		String herbValue = String.valueOf(Util.Round(player.getCollect()[0], 1)) ;
		String woodValue = String.valueOf(Util.Round(player.getCollect()[1], 1)) ;
		String metalValue = String.valueOf(Util.Round(player.getCollect()[2], 1)) ;
		DP.drawText(herbPos, Align.centerLeft, angle, herbValue, font, colorPalette[4]) ;
		DP.drawText(woodPos, Align.centerLeft, angle, woodValue, font, colorPalette[8]) ;
		DP.drawText(metalPos, Align.centerLeft, angle, metalValue, font, colorPalette[1]) ;


		//	Gold
		Point coinPos = Util.Translate(windowPos, 185, 332) ;
		Point goldPos = Util.Translate(windowPos, 197 + attOffset, 332) ;
		String goldValue = String.valueOf(Util.Round(player.getBag().getGold(), 1)) ;
		DP.drawImage(Player.CoinIcon, coinPos, angle, Scale.unit, Align.center) ;
		DP.drawText(goldPos, Align.centerLeft, angle, goldValue, font, Game.colorPalette[13]) ;
		
		incAttButtons.values().forEach(button -> button.display(angle, false, mousePos, DP)) ;
	}
	
	public void displayTab1(Player player, DrawPrimitives DP)
	{
		
		Font font = new Font(Game.MainFontName, Font.BOLD, 11) ;
		Color textColor = Game.colorPalette[0] ;
		double angle = Draw.stdAngle ;
		String[] attText = Game.allText.get(TextCategories.attributes) ;
		
		int leftColX = 44 + 4 ;
		int rightColX = 197 + 4 ;
		int topRowY = 35 ;
		int secondRowY = 35 + 110 ;
		int bottomRowY = 35 + 261 ;
		
		// Titles
		DP.drawText(Util.Translate(windowPos, leftColX, topRowY), Align.centerLeft, angle, attText[10], font, textColor) ;
		DP.drawText(Util.Translate(windowPos, rightColX, topRowY), Align.centerLeft, angle, attText[11], font, textColor) ;
		DP.drawText(Util.Translate(windowPos, leftColX, secondRowY), Align.centerLeft, angle, attText[12], font, textColor) ;
		DP.drawText(Util.Translate(windowPos, rightColX, secondRowY), Align.centerLeft, angle, attText[13], font, textColor) ;
		DP.drawText(Util.Translate(windowPos, leftColX, bottomRowY), Align.centerLeft, angle, attText[14], font, textColor) ;

		// att values
		Point stunValuesPos = Util.Translate(windowPos, leftColX, 56) ;
		Point blockValuesPos = Util.Translate(windowPos, rightColX, 56) ;
		Point bloodValuesPos = Util.Translate(windowPos, leftColX, 56 + 110) ;
		Point poisonValuesPos = Util.Translate(windowPos, rightColX, 56 + 110) ;
		Point silenceValuesPos = Util.Translate(windowPos, leftColX, 56 + 261) ;
		for (int i = 0 ; i <= 3 - 1 ; ++i)
		{
			DP.drawText(stunValuesPos, Align.centerLeft, angle, player.getBA().getStun().texts()[i], font, textColor) ;
			DP.drawText(blockValuesPos, Align.centerLeft, angle, player.getBA().getBlock().texts()[i], font, textColor) ;
			DP.drawText(silenceValuesPos, Align.centerLeft, angle, player.getBA().getSilence().texts()[i], font, textColor) ;
			
			stunValuesPos.y += 22 ;
			blockValuesPos.y += 22 ;
			silenceValuesPos.x += 77 ;
		}
		for (int i = 0 ; i <= 5 - 1 ; ++i)
		{
			DP.drawText(bloodValuesPos, Align.centerLeft, angle, player.getBA().getBlood().texts()[i], font, textColor) ;
			DP.drawText(poisonValuesPos, Align.centerLeft, angle, player.getBA().getPoison().texts()[i], font, textColor) ;
			
			bloodValuesPos.y += 22 ;
			poisonValuesPos.y += 22 ;
		}
	}
	
	public void displayTab2(Player player, DrawPrimitives DP)
	{
		
		Font font = new Font(Game.MainFontName, Font.BOLD, 11) ;
		String title = "Totais" ;
		List<String> subTitles = List.of("Causados", "Recebidos", "Defendidos") ;
		Color textColor = Game.colorPalette[0] ;
		
		Map<String, Integer> numberStats = player.getStatistics().numberStats() ;
		Map<String, Double> damageStats = player.getStatistics().damageStats() ;
		Map<String, Double> maxStats = player.getStatistics().maxStats() ;
		Point titlesPos = Util.Translate(windowPos, size.width / 2, 35 + 12 + 6 - 2) ;
		Point subTitlesPos = Util.Translate(windowPos, 21 + 48, 195 + 25 + 6) ;
		Point topLeft1 = Util.Translate(windowPos, 35 + 16, 35 + 31 + 10) ;
		Point topLeft2 = Util.Translate(windowPos, 32 + 6, 193 + 12) ;
		Point topLeft3 = Util.Translate(windowPos, 32 + 16, 195 + 25 + 27) ;
		
		// Titles
		DP.drawText(titlesPos, Align.bottomCenter, Draw.stdAngle, title, font, textColor) ;

		// subtitles
		subTitles.forEach(sub -> {
			DP.drawText(subTitlesPos, Align.center, Draw.stdAngle, sub, font, textColor) ;
			subTitlesPos.x += 96 ;
		}) ;
		
		// number stats
		int i = 0 ;
		for (String key : numberStats.keySet())
		{
			String text = String.valueOf(numberStats.get(key)) ;
			Point textPos = Util.Translate(topLeft1, (i / 6) * 140, (i % 6) * 18) ;
			DP.drawText(textPos, Align.centerLeft, Draw.stdAngle, text, font, textColor) ;
			i += 1 ;
		}
		
		// damage stats
		i = 0 ;
		for (String key : damageStats.keySet())
		{
			String text = String.valueOf(Util.Round((double) damageStats.get(key), 1)) ;
			Point textPos = Util.Translate(topLeft3, (i % 3) * 96, (i / 3) * 18) ;
			DP.drawText(textPos, Align.centerLeft, Draw.stdAngle, text, font, textColor) ;
			i += 1 ;
		}
		
		// max stats
		i = 0 ;
		for (String key : maxStats.keySet())
		{
			String text = "dano " + key + " máx: " + String.valueOf(Util.Round((double) maxStats.get(key), 1)) ;
			Point textPos = Util.Translate(topLeft2, i * 126, 0) ;
			DP.drawText(textPos, Align.centerLeft, Draw.stdAngle, text, font, textColor) ;
			i += 1 ;
		}
		
	}
		
	public void display(Point mousePos, DrawPrimitives DP)
	{
		
		String[] tabsText = Game.allText.get(TextCategories.playerWindow) ;
		Image windowImage = tab == 0 ? image : (tab == 1 ? tab1Image : tab2Image) ;
		
		// Main window
		DP.drawImage(windowImage, windowPos, Align.topLeft) ;

		Point tabsTextPos = Util.Translate(windowPos, 7, 6 + 22) ;
		for (int i = 0 ; i <= 3 - 1 ; i += 1)
		{
			Color tabTextColor = i == tab ? Game.colorPalette[18] : Game.colorPalette[0] ;
			DP.drawText(tabsTextPos, Align.center, 90, tabsText[i], stdFont, tabTextColor) ;
			tabsTextPos.y += 45 ;
		}
			
		switch (tab)
		{
			case 0: displayTab0(mousePos, DP) ; break ;
			case 1: displayTab1(player, DP) ; break ;
			case 2: displayTab2(player, DP) ; break ;
		}		

		// Player name
		Point namePos = Util.Translate(windowPos, size.width / 2, 11) ;
		DP.drawText(namePos, Align.center, Draw.stdAngle, player.getName(), titleFont, Game.colorPalette[0]) ;	
		
	}
	
}
