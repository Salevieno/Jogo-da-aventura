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
import UI.GameIconButton;
import attributes.Attributes;
import attributes.BasicBattleAttribute;
import graphics.Align;
import graphics.Scale;
import graphics2.Draw;
import items.Equip;
import liveBeings.Player;
import main.Elements;
import main.Game;
import main.GamePanel;
import main.ImageLoader;
import main.Palette;
import main.Path;
import main.TextCategories;
import screen.Screen;
import utilities.Util;


public class PlayerAttributesWindow extends AttributesWindow
{
	
	private Player player ;
	private Map<Attributes, GameButton> incAttButtons ;
	
	private static final Point WINDOW_POS = Screen.getMe().pos(0.01, 0.25) ;
	private static final Image TAB_0_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "PlayerAttWindow1.png") ;
	private static final Image TAB_1_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "PlayerAttWindow2.png") ;
	private static final Image TAB_2_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "PlayerAttWindow3.png") ;	
	private static final Image PLUS_SIGN_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "PlusSign.png") ;
	private static final Image PLUS_SELECTED_SIGN_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "PlusSignShining.png") ;

	public PlayerAttributesWindow()
	{
		
		super(TAB_0_IMAGE, 3) ;
		
		incAttButtons = new HashMap<>() ;	
		
	}
	
	public void setPlayer(Player player) { this.player = player ;}

	public void initializeAttIncButtons(Player player)
	{

		Point pos = Util.translate(WINDOW_POS, 27, 280) ;
		for (Attributes att : Arrays.asList(Attributes.getIncrementable()))
		{
			ButtonFunction method = () -> {player.getBA().mapAttributes(att).incBaseValue(1) ;} ;
			GameButton newAttButton = new GameIconButton(pos, Align.center, PLUS_SIGN_IMAGE, PLUS_SELECTED_SIGN_IMAGE, method) ;
			newAttButton.deactivate() ;
			incAttButtons.put(att, newAttButton) ;
			pos.y += 27 ;
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

		Font smallFont = new Font(Game.getMainFontName(), Font.BOLD, 9) ;
		String[] equipsText = Game.getAllText().get(TextCategories.equipments) ;
		int eqSlotSize = 51 ;
		Point[] eqSlotCenter = new Point[] {
				Util.translate(WINDOW_POS, leftSlotCenter.x, leftSlotCenter.y),
				Util.translate(leftSlotCenter, 259, 63),
				Util.translate(leftSlotCenter, 259, 135),
				Util.translate(leftSlotCenter, 165, 80)} ;
		for (int eq = 0 ; eq <= equips.length - 1 ; eq += 1)
		{
			if (equips[eq] == null) { continue ;}
			
			Equip equip = equips[eq] ;
			Point bottomTextPos = Util.translate(eqSlotCenter[eq], -eqSlotSize / 2, eqSlotSize / 2 + 13) ;

			GamePanel.getDP().drawImage(equip.fullSizeImage(), eqSlotCenter[eq], Align.center) ;
			Draw.textUntil(bottomTextPos, Align.bottomLeft, angle, equip.getName(), smallFont, textColor, 14, mousePos) ;

			Point upperTextPos = Util.translate(eqSlotCenter[eq], 0, -eqSlotSize / 2 - 3) ;
			if (0 < equip.getForgeLevel())
			{
				String equipText = equipsText[eq] + " + " + equip.getForgeLevel() ;
				GamePanel.getDP().drawText(upperTextPos, Align.bottomCenter, angle, equipText, smallFont, textColor) ;					
			}

			Elements eqElem = player.getEquips()[eq + 1] != null ? player.getEquips()[eq + 1].getElem() : null ;

			if (eqElem == null || eq == 3) { continue ;}
			
			Image elemImage = eqElem.image ;
			Point elemPos = Util.translate(eqSlotCenter[eq], eqSlotSize / 2 - 12, eqSlotSize / 2 - 12) ;

			GamePanel.getDP().drawImage(elemImage, elemPos, angle, new Scale(0.5, 0.5), Align.center) ;
		}

		// Arrow
		if (player.getEquippedArrow() != null)
		{
			GamePanel.getDP().drawImage(player.getEquippedArrow().fullSizeImage(), Util.translate(WINDOW_POS, 100, 133), Align.bottomCenter) ;
		}
	}
	
	private void displayAttributes(Point centerLeftPos, double angle, Color textColor)
	{
		Font font = SUBTITLE_FONT ;
		String[] attText = Game.getAllText().get(TextCategories.attributes) ;
		Point lifePos = centerLeftPos ;
		Point mpPos = Util.translate(centerLeftPos, 0, 27) ;
		int attTextImgOffset = 12 + 4 ;
		int attSpacingY = 27 ;
		Point battleAttCenterLeft = Util.translate(WINDOW_POS, 35 + 18, 289) ;
		Point collectImgCenter = Util.translate(WINDOW_POS, 324, 401) ;
		Point goldImgCenter = Util.translate(collectImgCenter, 0, 80) ;
		Point critImgCenter = Util.translate(WINDOW_POS, 35 + 18, 477) ;
		Point critPos = Util.translate(critImgCenter, attTextImgOffset, 0) ;

		String lifeText = attText[1] + ": " + Util.round(player.getPA().getLife().getTotalValue(), 1) ;
		String mpText = attText[2] + ": " + Util.round(player.getPA().getMp().getTotalValue(), 1) ;
		GamePanel.getDP().drawText(lifePos, Align.centerLeft, angle, lifeText, font, Palette.colors[7]) ;
		GamePanel.getDP().drawText(mpPos, Align.centerLeft, angle, mpText, font, Palette.colors[19]) ;
		
		BasicBattleAttribute[] attributes = player.getBA().basicAttributes() ;
		for (int i = 0; i <= attributes.length - 3; i += 1)
		{
			Point attValuePos = Util.translate(battleAttCenterLeft, attTextImgOffset, i * attSpacingY) ;
			Point attImagePos = Util.translate(WINDOW_POS, 35 + 18, 289 + i * attSpacingY) ;
			GamePanel.getDP().drawImage(ATT_ICONS[i], attImagePos, Scale.unit, Align.center) ;
			GamePanel.getDP().drawText(attValuePos, Align.centerLeft, angle, attributes[i].text(), font, textColor) ;
		}

		String critValue = Util.round(100 * player.getBA().TotalCritAtkChance(), 1) + "%" ;
		GamePanel.getDP().drawImage(CRIT_ICON, critImgCenter, Scale.unit, Align.center) ;
		GamePanel.getDP().drawText(critPos, Align.centerLeft, angle, critValue, font, Palette.colors[6]) ;
		
		//	Collecting
		GamePanel.getDP().drawImage(COLLECT_ICONS[0], Util.translate(collectImgCenter, 0, 0), Scale.unit, Align.center) ;
		GamePanel.getDP().drawImage(COLLECT_ICONS[1], Util.translate(collectImgCenter, 0, attSpacingY), Scale.unit, Align.center) ;
		GamePanel.getDP().drawImage(COLLECT_ICONS[2], Util.translate(collectImgCenter, 0, 2 * attSpacingY), Scale.unit, Align.center) ;
		
		Point herbPos = Util.translate(collectImgCenter, attTextImgOffset, 0) ;
		Point woodPos = Util.translate(collectImgCenter, attTextImgOffset, attSpacingY) ;
		Point metalPos = Util.translate(collectImgCenter, attTextImgOffset, 2 * attSpacingY) ;
		String herbValue = String.valueOf(Util.round(player.getCollect().get(0), 1)) ;
		String woodValue = String.valueOf(Util.round(player.getCollect().get(1), 1)) ;
		String metalValue = String.valueOf(Util.round(player.getCollect().get(2), 1)) ;
		GamePanel.getDP().drawText(herbPos, Align.centerLeft, angle, herbValue, font, Palette.colors[4]) ;
		GamePanel.getDP().drawText(woodPos, Align.centerLeft, angle, woodValue, font, Palette.colors[8]) ;
		GamePanel.getDP().drawText(metalPos, Align.centerLeft, angle, metalValue, font, Palette.colors[1]) ;

		//	Gold
		String goldValue = String.valueOf(Util.round(player.getBag().getGold(), 1)) ;
		Point goldTextPos = Util.translate(goldImgCenter, attTextImgOffset, 0) ;
		GamePanel.getDP().drawImage(Player.getCoinImg(), goldImgCenter, angle, Scale.unit, Align.center) ;
		GamePanel.getDP().drawText(goldTextPos, Align.centerLeft, angle, goldValue, font, Palette.colors[13]) ;	
	}
	
	public void displayTab0(Point mousePos)
	{
		Font font = SUBTITLE_FONT ;
		String[] classesText = Game.getAllText().get(TextCategories.classes) ;
		String[] proClassesText = Game.getAllText().get(TextCategories.proclasses) ;

		Point playerImgPos = Util.translate(WINDOW_POS, size.width / 2, 156) ;
		Point equipsLeftSlotCenter = new Point(110, 156) ;
		Point superElemPos = Util.translate(playerImgPos, 0, 35) ;
		Point attCenterLeftPos = Util.translate(WINDOW_POS, 37, 49) ;
		Point powerPos = Util.translate(WINDOW_POS, 430, 490) ;

		player.getMovingAni().spriteIdle.display(GamePanel.getDP(), playerImgPos, Align.center) ;

		Point levelPos = Util.translate(WINDOW_POS, size.width / 2, 38) ;	
		GamePanel.getDP().drawText(levelPos, Align.center, Draw.stdAngle, "Level: " + player.getLevel(), font, Palette.colors[7]) ;
		
		String jobText = player.getProJob() == 0 ? classesText[player.getJob()] : proClassesText[2 * player.getJob() + player.getProJob() - 1] ;
		Point jobTextPos = Util.translate(WINDOW_POS, size.width / 2, 56) ;
		GamePanel.getDP().drawText(jobTextPos, Align.center, Draw.stdAngle, jobText, font, Palette.colors[0]) ;
		
		displayEquips(equipsLeftSlotCenter, mousePos, Draw.stdAngle, Palette.colors[0]) ;		

		if (player.hasSuperElement())
		{
			Image superElemImage = player.getSuperElem().image ;
			GamePanel.getDP().drawImage(superElemImage, superElemPos, Draw.stdAngle, new Scale(0.3, 0.3), Align.center) ;
		}
		
		displayAttributes(attCenterLeftPos, Draw.stdAngle, Palette.colors[0]) ;
		player.displayPowerBar(powerPos) ;
		
		incAttButtons.values().forEach(button -> button.display(Draw.stdAngle, false, mousePos)) ;
	}
	
	public void displayTab1(Player player)
	{
		
		Font font = new Font(Game.getMainFontName(), Font.BOLD, 11) ;
		Color textColor = Palette.colors[0] ;
		double angle = Draw.stdAngle ;
		String[] attText = Game.getAllText().get(TextCategories.attributes) ;
		
		int leftColX = 44 + 4 ;
		int rightColX = 197 + 4 ;
		int topRowY = 35 ;
		int secondRowY = 35 + 110 ;
		int bottomRowY = 35 + 261 ;
		
		// Titles
		GamePanel.getDP().drawText(Util.translate(WINDOW_POS, leftColX, topRowY), Align.centerLeft, angle, attText[10], font, textColor) ;
		GamePanel.getDP().drawText(Util.translate(WINDOW_POS, rightColX, topRowY), Align.centerLeft, angle, attText[11], font, textColor) ;
		GamePanel.getDP().drawText(Util.translate(WINDOW_POS, leftColX, secondRowY), Align.centerLeft, angle, attText[12], font, textColor) ;
		GamePanel.getDP().drawText(Util.translate(WINDOW_POS, rightColX, secondRowY), Align.centerLeft, angle, attText[13], font, textColor) ;
		GamePanel.getDP().drawText(Util.translate(WINDOW_POS, leftColX, bottomRowY), Align.centerLeft, angle, attText[14], font, textColor) ;

		// att values
		Point stunValuesPos = Util.translate(WINDOW_POS, leftColX, 56) ;
		Point blockValuesPos = Util.translate(WINDOW_POS, rightColX, 56) ;
		Point bloodValuesPos = Util.translate(WINDOW_POS, leftColX, 56 + 110) ;
		Point poisonValuesPos = Util.translate(WINDOW_POS, rightColX, 56 + 110) ;
		Point silenceValuesPos = Util.translate(WINDOW_POS, leftColX, 56 + 261) ;
		for (int i = 0 ; i <= 3 - 1 ; ++i)
		{
			GamePanel.getDP().drawText(stunValuesPos, Align.centerLeft, angle, player.getBA().getStun().texts()[i], font, textColor) ;
			GamePanel.getDP().drawText(blockValuesPos, Align.centerLeft, angle, player.getBA().getBlock().texts()[i], font, textColor) ;
			GamePanel.getDP().drawText(silenceValuesPos, Align.centerLeft, angle, player.getBA().getSilence().texts()[i], font, textColor) ;
			
			stunValuesPos.y += 22 ;
			blockValuesPos.y += 22 ;
			silenceValuesPos.x += 77 ;
		}
		for (int i = 0 ; i <= 5 - 1 ; ++i)
		{
			GamePanel.getDP().drawText(bloodValuesPos, Align.centerLeft, angle, player.getBA().getBlood().texts()[i], font, textColor) ;
			GamePanel.getDP().drawText(poisonValuesPos, Align.centerLeft, angle, player.getBA().getPoison().texts()[i], font, textColor) ;
			
			bloodValuesPos.y += 22 ;
			poisonValuesPos.y += 22 ;
		}
	}
	
	public void displayTab2(Player player)
	{
		
		Font font = new Font(Game.getMainFontName(), Font.BOLD, 11) ;
		String title = "Totais" ;
		List<String> subTitles = List.of("Causados", "Recebidos", "Defendidos") ;
		Color textColor = Palette.colors[0] ;
		
		Map<String, Integer> numberStats = player.getStatistics().numberStats() ;
		Map<String, Double> damageStats = player.getStatistics().damageStats() ;
		Map<String, Double> maxStats = player.getStatistics().maxStats() ;
		Point titlesPos = Util.translate(WINDOW_POS, size.width / 2, 35 + 12 + 6 - 2) ;
		Point subTitlesPos = Util.translate(WINDOW_POS, 21 + 48, 195 + 25 + 6) ;
		Point topLeft1 = Util.translate(WINDOW_POS, 35 + 16, 35 + 31 + 10) ;
		Point topLeft2 = Util.translate(WINDOW_POS, 32 + 6, 193 + 12) ;
		Point topLeft3 = Util.translate(WINDOW_POS, 32 + 16, 195 + 25 + 27) ;
		
		// Titles
		GamePanel.getDP().drawText(titlesPos, Align.bottomCenter, Draw.stdAngle, title, font, textColor) ;

		// subtitles
		subTitles.forEach(sub -> {
			GamePanel.getDP().drawText(subTitlesPos, Align.center, Draw.stdAngle, sub, font, textColor) ;
			subTitlesPos.x += 96 ;
		}) ;
		
		// number stats
		int i = 0 ;
		for (String key : numberStats.keySet())
		{
			String text = String.valueOf(numberStats.get(key)) ;
			Point textPos = Util.translate(topLeft1, (i / 6) * 140, (i % 6) * 18) ;
			GamePanel.getDP().drawText(textPos, Align.centerLeft, Draw.stdAngle, text, font, textColor) ;
			i += 1 ;
		}
		
		// damage stats
		i = 0 ;
		for (String key : damageStats.keySet())
		{
			String text = String.valueOf(Util.round((double) damageStats.get(key), 1)) ;
			Point textPos = Util.translate(topLeft3, (i % 3) * 96, (i / 3) * 18) ;
			GamePanel.getDP().drawText(textPos, Align.centerLeft, Draw.stdAngle, text, font, textColor) ;
			i += 1 ;
		}
		
		// max stats
		i = 0 ;
		for (String key : maxStats.keySet())
		{
			String text = "dano " + key + " máx: " + String.valueOf(Util.round((double) maxStats.get(key), 1)) ;
			Point textPos = Util.translate(topLeft2, i * 126, 0) ;
			GamePanel.getDP().drawText(textPos, Align.centerLeft, Draw.stdAngle, text, font, textColor) ;
			i += 1 ;
		}
		
	}
		
	public void display(Point mousePos)
	{
		
		String[] tabsText = Game.getAllText().get(TextCategories.playerWindow) ;
		Image windowImage = tab == 0 ? image : (tab == 1 ? TAB_1_IMAGE : TAB_2_IMAGE) ;
		
		// Main window
		GamePanel.getDP().drawImage(windowImage, WINDOW_POS, Align.topLeft) ;

		Point tabsTextPos = Util.translate(WINDOW_POS, 18, 6 + 30) ;
		for (int i = 0 ; i <= 3 - 1 ; i += 1)
		{
			Color tabTextColor = i == tab ? Palette.colors[18] : Palette.colors[0] ;
			GamePanel.getDP().drawText(tabsTextPos, Align.center, 90, tabsText[i], TITLE_FONT, tabTextColor) ;
			tabsTextPos.y += 90 ;
		}
			
		switch (tab)
		{
			case 0: displayTab0(mousePos) ; break ;
			case 1: displayTab1(player) ; break ;
			case 2: displayTab2(player) ; break ;
		}		

		// Player name
		Point namePos = Util.translate(WINDOW_POS, size.width / 2, 11) ;
		GamePanel.getDP().drawText(namePos, Align.center, Draw.stdAngle, player.getName(), TITLE_FONT, Palette.colors[0]) ;	
		
	}
	
}
