package windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import UI.ButtonFunction;
import UI.GameButton;
import attributes.Attributes;
import attributes.BasicBattleAttribute;
import graphics.Align;
import graphics.Scale;
import graphics2.Draw;
import graphics2.SpriteAnimation;
import items.Equip;
import liveBeings.Player;
import main.Elements;
import main.Game;
import main.GamePanel;
import main.Path;
import main.TextCategories;
import utilities.Util;


public class PlayerAttributesWindow extends AttributesWindow
{
	
	private Point windowPos = Game.getScreen().pos(0.01, 0.25) ;
	private Player player ;
	public Map<Attributes, GameButton> incAttButtons ;

	public static final Image tab0Image ;
	public static final Image tab1Image ;
	public static final Image tab2Image ;	
	private static final Image plusSign ;
	private static final Image selectedPlusSign ;
	
	static
	{
		tab0Image = Game.loadImage(Path.WINDOWS_IMG + "PlayerAttWindow1.png") ;
		tab1Image = Game.loadImage(Path.WINDOWS_IMG + "PlayerAttWindow2.png") ;
		tab2Image = Game.loadImage(Path.WINDOWS_IMG + "PlayerAttWindow3.png") ;
		plusSign = Game.loadImage(Path.SIDEBAR_IMG + "PlusSign.png") ;
		selectedPlusSign = Game.loadImage(Path.SIDEBAR_IMG + "ShiningPlusSign.png") ;
	}
	
	public PlayerAttributesWindow()
	{
		
		super(tab0Image, 3) ;
		
		incAttButtons = new HashMap<>() ;	
		
	}
	
	public void setPlayer(Player player) { this.player = player ;}

	public void initializeAttIncButtons(Player player)
	{

		Point pos = Util.translate(windowPos, 27, 180) ;
		for (Attributes att : Arrays.asList(Attributes.getIncrementable()))
		{
			ButtonFunction method = () -> {player.getBA().mapAttributes(att).incBaseValue(1) ;} ;
			GameButton newAttButton = new GameButton(pos, Align.center, plusSign, selectedPlusSign, method) ;
			newAttButton.deactivate() ;
			incAttButtons.put(att, newAttButton) ;
			pos.y += 22 ;
		}
		
	}
	
	public void updateAttIncButtons(Player player)
	{

		if (player.getAttPoints() <= 0)
		{
			incAttButtons.values().forEach(button -> button.deactivate()) ;
			return ;
		}
		
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
	
	private void displayEquips(Point leftSlotCenter, Point mousePos, double angle, Color textColor)
	{
		Equip[] equips = player.getEquips() ;

		if (equips == null) { return ;}

		Font smallFont = new Font(Game.MainFontName, Font.BOLD, 9) ;
		String[] equipsText = Game.allText.get(TextCategories.equipments) ;
		int eqSlotSize = 51 ;
		Point[] eqSlotCenter = new Point[] {
				Util.translate(windowPos, leftSlotCenter.x, leftSlotCenter.y),
				Util.translate(leftSlotCenter, 259, 63),
				Util.translate(leftSlotCenter, 259, 135),
				Util.translate(leftSlotCenter, 165, 80)} ;
		for (int eq = 0 ; eq <= equips.length - 1 ; eq += 1)
		{
			if (equips[eq] == null) { continue ;}
			
			Equip equip = equips[eq] ;
			Point bottomTextPos = Util.translate(eqSlotCenter[eq], -eqSlotSize / 2, eqSlotSize / 2 + 13) ;

			GamePanel.DP.drawImage(equip.fullSizeImage(), eqSlotCenter[eq], Align.center) ;
			Draw.textUntil(bottomTextPos, Align.bottomLeft, angle, equip.getName(), smallFont, textColor, 14, mousePos) ;

			Point upperTextPos = Util.translate(eqSlotCenter[eq], 0, -eqSlotSize / 2 - 3) ;
			if (0 < equip.getForgeLevel())
			{
				String equipText = equipsText[eq] + " + " + equip.getForgeLevel() ;
				GamePanel.DP.drawText(upperTextPos, Align.bottomCenter, angle, equipText, smallFont, textColor) ;					
			}

			Elements eqElem = player.getEquips()[eq + 1].getElem() ;

			if (eqElem == null || eq == 3) { continue ;}
			
			Image elemImage = eqElem.image ;
			Point elemPos = Util.translate(eqSlotCenter[eq], eqSlotSize / 2 - 12, eqSlotSize / 2 - 12) ;

			GamePanel.DP.drawImage(elemImage, elemPos, angle, new Scale(0.5, 0.5), Align.center) ;
		}

		// Arrow
		if (player.getEquippedArrow() != null)
		{
			GamePanel.DP.drawImage(player.getEquippedArrow().fullSizeImage(), Util.translate(windowPos, 100, 133), Align.bottomCenter) ;
		}
	}
	
	private void displayAttributes(Point topLeftPos, double angle, Color textColor)
	{
		Font font = titleFont ;
		String[] attText = Game.allText.get(TextCategories.attributes) ;
		Point lifePos = topLeftPos ;
		Point mpPos = Util.translate(topLeftPos, 0, 25) ;
		int attOffset = 4 ;
		Point battleAttTopLeft = Util.translate(topLeftPos, 22 + attOffset, 300) ;
		int battleAttSy = 22 ;
		Point collectTopLeft = Util.translate(battleAttTopLeft, 300 + attOffset, 3 * battleAttSy) ;
		int collecSy = 22 ;
		Point goldPos = Util.translate(collectTopLeft, 0, 75) ;

		String lifeText = attText[1] + ": " + Util.round(player.getPA().getLife().getTotalValue(), 1) ;
		String mpText = attText[2] + ": " + Util.round(player.getPA().getMp().getTotalValue(), 1) ;
		GamePanel.DP.drawText(lifePos, Align.centerLeft, angle, lifeText, font, Game.palette[7]) ;
		GamePanel.DP.drawText(mpPos, Align.centerLeft, angle, mpText, font, Game.palette[20]) ;
		
		BasicBattleAttribute[] attributes = player.getBA().basicAttributes() ;
		for (int i = 0; i <= attributes.length - 3; i += 1)
		{
			Point attValuePos = Util.translate(battleAttTopLeft, 0, i * battleAttSy) ;
			Point attImagePos = Util.translate(battleAttTopLeft, -13, i * battleAttSy) ;
			GamePanel.DP.drawImage(attIcons[i], attImagePos, Scale.unit, Align.center) ;
			GamePanel.DP.drawText(attValuePos, Align.centerLeft, angle, attributes[i].text(), font, textColor) ;
		}
		Point critPos = Util.translate(battleAttTopLeft, 0, 9 + (attributes.length - 2) * battleAttSy) ;
		Point critImagePos = Util.translate(battleAttTopLeft, -13, (attributes.length - 2) * battleAttSy) ;
		String critValue = Util.round(100 * player.getBA().TotalCritAtkChance(), 1) + "%" ;
		GamePanel.DP.drawImage(critIcon, critImagePos, Scale.unit, Align.center) ;
		GamePanel.DP.drawText(critPos, Align.centerLeft, angle, critValue, font, Game.palette[6]) ;

		
		//	Collecting
		GamePanel.DP.drawImage(collectIcons[0], Util.translate(collectTopLeft, -13, 0), Scale.unit, Align.center) ;
		GamePanel.DP.drawImage(collectIcons[1], Util.translate(collectTopLeft, -13, collecSy), Scale.unit, Align.center) ;
		GamePanel.DP.drawImage(collectIcons[2], Util.translate(collectTopLeft, -13, 2 * collecSy), Scale.unit, Align.center) ;
		
		Point herbPos = collectTopLeft ;
		Point woodPos = Util.translate(collectTopLeft, 0, collecSy) ;
		Point metalPos = Util.translate(collectTopLeft, 0, 2 * collecSy) ;
		String herbValue = String.valueOf(Util.round(player.getCollect().get(0), 1)) ;
		String woodValue = String.valueOf(Util.round(player.getCollect().get(1), 1)) ;
		String metalValue = String.valueOf(Util.round(player.getCollect().get(2), 1)) ;
		GamePanel.DP.drawText(herbPos, Align.centerLeft, angle, herbValue, font, Game.palette[4]) ;
		GamePanel.DP.drawText(woodPos, Align.centerLeft, angle, woodValue, font, Game.palette[8]) ;
		GamePanel.DP.drawText(metalPos, Align.centerLeft, angle, metalValue, font, Game.palette[1]) ;

		//	Gold
		Point coinPos = Util.translate(goldPos, -13, 0) ;
		String goldValue = String.valueOf(Util.round(player.getBag().getGold(), 1)) ;
		GamePanel.DP.drawImage(Player.CoinIcon, coinPos, angle, Scale.unit, Align.center) ;
		GamePanel.DP.drawText(goldPos, Align.centerLeft, angle, goldValue, font, Game.palette[13]) ;	
	}
	
	public void displayTab0(Point mousePos)
	{
		
		Color[] colorPalette = Game.palette ;
		Font font = subTitleFont ;
		double angle = Draw.stdAngle ;

		String[] classesText = Game.allText.get(TextCategories.classes) ;
		String[] proClassesText = Game.allText.get(TextCategories.proclasses) ;
		Color textColor = colorPalette[0] ;
		
		SpriteAnimation userImage = player.getMovingAni().spriteIdle ;
		Point userPos = Util.translate(windowPos, size.width / 2, 120) ;
		Point equipsLeftSlotCenter = new Point(70, 110) ;
		Point superElemPos = Util.translate(userPos, 0, 35) ;
		Point attTopLeftPos = Util.translate(windowPos, 37, 39) ;

		userImage.display(GamePanel.DP, userPos, Align.center) ;

		Point levelPos = Util.translate(windowPos, size.width / 2, 38) ;	
		GamePanel.DP.drawText(levelPos, Align.center, angle, "Level: " + player.getLevel(), font, colorPalette[7]) ;
		
		String jobText = player.getProJob() == 0 ? classesText[player.getJob()] : proClassesText[2 * player.getJob() + player.getProJob() - 1] ;
		Point jobTextPos = Util.translate(windowPos, size.width / 2, 56) ;
		GamePanel.DP.drawText(jobTextPos, Align.center, angle, jobText, font, colorPalette[0]) ;
		
		displayEquips(equipsLeftSlotCenter, mousePos, angle, textColor) ;		
		
		// super element
		if (player.hasSuperElement())
		{
			Image superElemImage = player.getSuperElem().image ;
			GamePanel.DP.drawImage(superElemImage, superElemPos, angle, new Scale(0.3, 0.3), Align.center) ;
		}
		
		displayAttributes(attTopLeftPos, angle, textColor) ;

		// Power bar
		Point powerPos = Util.translate(windowPos, 410, 460) ;
		player.displayPowerBar(powerPos) ;
		
		incAttButtons.values().forEach(button -> button.display(angle, false, mousePos)) ;
	}
	
	public void displayTab1(Player player)
	{
		
		Font font = new Font(Game.MainFontName, Font.BOLD, 11) ;
		Color textColor = Game.palette[0] ;
		double angle = Draw.stdAngle ;
		String[] attText = Game.allText.get(TextCategories.attributes) ;
		
		int leftColX = 44 + 4 ;
		int rightColX = 197 + 4 ;
		int topRowY = 35 ;
		int secondRowY = 35 + 110 ;
		int bottomRowY = 35 + 261 ;
		
		// Titles
		GamePanel.DP.drawText(Util.translate(windowPos, leftColX, topRowY), Align.centerLeft, angle, attText[10], font, textColor) ;
		GamePanel.DP.drawText(Util.translate(windowPos, rightColX, topRowY), Align.centerLeft, angle, attText[11], font, textColor) ;
		GamePanel.DP.drawText(Util.translate(windowPos, leftColX, secondRowY), Align.centerLeft, angle, attText[12], font, textColor) ;
		GamePanel.DP.drawText(Util.translate(windowPos, rightColX, secondRowY), Align.centerLeft, angle, attText[13], font, textColor) ;
		GamePanel.DP.drawText(Util.translate(windowPos, leftColX, bottomRowY), Align.centerLeft, angle, attText[14], font, textColor) ;

		// att values
		Point stunValuesPos = Util.translate(windowPos, leftColX, 56) ;
		Point blockValuesPos = Util.translate(windowPos, rightColX, 56) ;
		Point bloodValuesPos = Util.translate(windowPos, leftColX, 56 + 110) ;
		Point poisonValuesPos = Util.translate(windowPos, rightColX, 56 + 110) ;
		Point silenceValuesPos = Util.translate(windowPos, leftColX, 56 + 261) ;
		for (int i = 0 ; i <= 3 - 1 ; ++i)
		{
			GamePanel.DP.drawText(stunValuesPos, Align.centerLeft, angle, player.getBA().getStun().texts()[i], font, textColor) ;
			GamePanel.DP.drawText(blockValuesPos, Align.centerLeft, angle, player.getBA().getBlock().texts()[i], font, textColor) ;
			GamePanel.DP.drawText(silenceValuesPos, Align.centerLeft, angle, player.getBA().getSilence().texts()[i], font, textColor) ;
			
			stunValuesPos.y += 22 ;
			blockValuesPos.y += 22 ;
			silenceValuesPos.x += 77 ;
		}
		for (int i = 0 ; i <= 5 - 1 ; ++i)
		{
			GamePanel.DP.drawText(bloodValuesPos, Align.centerLeft, angle, player.getBA().getBlood().texts()[i], font, textColor) ;
			GamePanel.DP.drawText(poisonValuesPos, Align.centerLeft, angle, player.getBA().getPoison().texts()[i], font, textColor) ;
			
			bloodValuesPos.y += 22 ;
			poisonValuesPos.y += 22 ;
		}
	}
	
	public void displayTab2(Player player)
	{
		
		Font font = new Font(Game.MainFontName, Font.BOLD, 11) ;
		String title = "Totais" ;
		List<String> subTitles = List.of("Causados", "Recebidos", "Defendidos") ;
		Color textColor = Game.palette[0] ;
		
		Map<String, Integer> numberStats = player.getStatistics().numberStats() ;
		Map<String, Double> damageStats = player.getStatistics().damageStats() ;
		Map<String, Double> maxStats = player.getStatistics().maxStats() ;
		Point titlesPos = Util.translate(windowPos, size.width / 2, 35 + 12 + 6 - 2) ;
		Point subTitlesPos = Util.translate(windowPos, 21 + 48, 195 + 25 + 6) ;
		Point topLeft1 = Util.translate(windowPos, 35 + 16, 35 + 31 + 10) ;
		Point topLeft2 = Util.translate(windowPos, 32 + 6, 193 + 12) ;
		Point topLeft3 = Util.translate(windowPos, 32 + 16, 195 + 25 + 27) ;
		
		// Titles
		GamePanel.DP.drawText(titlesPos, Align.bottomCenter, Draw.stdAngle, title, font, textColor) ;

		// subtitles
		subTitles.forEach(sub -> {
			GamePanel.DP.drawText(subTitlesPos, Align.center, Draw.stdAngle, sub, font, textColor) ;
			subTitlesPos.x += 96 ;
		}) ;
		
		// number stats
		int i = 0 ;
		for (String key : numberStats.keySet())
		{
			String text = String.valueOf(numberStats.get(key)) ;
			Point textPos = Util.translate(topLeft1, (i / 6) * 140, (i % 6) * 18) ;
			GamePanel.DP.drawText(textPos, Align.centerLeft, Draw.stdAngle, text, font, textColor) ;
			i += 1 ;
		}
		
		// damage stats
		i = 0 ;
		for (String key : damageStats.keySet())
		{
			String text = String.valueOf(Util.round((double) damageStats.get(key), 1)) ;
			Point textPos = Util.translate(topLeft3, (i % 3) * 96, (i / 3) * 18) ;
			GamePanel.DP.drawText(textPos, Align.centerLeft, Draw.stdAngle, text, font, textColor) ;
			i += 1 ;
		}
		
		// max stats
		i = 0 ;
		for (String key : maxStats.keySet())
		{
			String text = "dano " + key + " máx: " + String.valueOf(Util.round((double) maxStats.get(key), 1)) ;
			Point textPos = Util.translate(topLeft2, i * 126, 0) ;
			GamePanel.DP.drawText(textPos, Align.centerLeft, Draw.stdAngle, text, font, textColor) ;
			i += 1 ;
		}
		
	}
		
	public void display(Point mousePos)
	{
		
		String[] tabsText = Game.allText.get(TextCategories.playerWindow) ;
		Image windowImage = tab == 0 ? image : (tab == 1 ? tab1Image : tab2Image) ;
		
		// Main window
		GamePanel.DP.drawImage(windowImage, windowPos, Align.topLeft) ;

		Point tabsTextPos = Util.translate(windowPos, 18, 6 + 30) ;
		for (int i = 0 ; i <= 3 - 1 ; i += 1)
		{
			Color tabTextColor = i == tab ? Game.palette[18] : Game.palette[0] ;
			GamePanel.DP.drawText(tabsTextPos, Align.center, 90, tabsText[i], titleFont, tabTextColor) ;
			tabsTextPos.y += 90 ;
		}
			
		switch (tab)
		{
			case 0: displayTab0(mousePos) ; break ;
			case 1: displayTab1(player) ; break ;
			case 2: displayTab2(player) ; break ;
		}		

		// Player name
		Point namePos = Util.translate(windowPos, size.width / 2, 11) ;
		GamePanel.DP.drawText(namePos, Align.center, Draw.stdAngle, player.getName(), titleFont, Game.palette[0]) ;	
		
	}
	
}
