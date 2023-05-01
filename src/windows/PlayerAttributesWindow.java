package windows;

import java.awt.Color;
import java.awt.Dimension;
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
import utilities.Align;
import utilities.Elements;
import utilities.Scale;
import utilities.UtilG;

public class PlayerAttributesWindow extends AttributesWindow
{
	
	Point windowPos = new Point((int) (0.1 * Game.getScreen().getSize().width), (int)(0.2 * Game.getScreen().getSize().height)) ;
	public Map<Attributes, GameButton> incAttButtons ;

	Image tab1Image = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "PlayerAttWindow2.png") ;
	Image tab2Image = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "PlayerAttWindow3.png") ;
	
	Image plusSign = UtilG.loadImage(Game.ImagesPath + "\\SideBar\\" + "PlusSign.png") ;
	Image selectedPlusSign = UtilG.loadImage(Game.ImagesPath + "\\SideBar\\" + "ShiningPlusSign.png") ;
	
	public PlayerAttributesWindow(Image image)
	{
		super(image, 3) ;
		
		incAttButtons = new HashMap<>() ;
		
		
	}
	
	public void initializeAttIncButtons(Player player)
	{

		Point pos = UtilG.Translate(windowPos, 0, 180) ;
		for (Attributes att : Arrays.asList(Attributes.getBattle()))
		{
			IconFunction method = () -> {player.getBA().mapAttributes(att).incBaseValue(1) ;} ;
			GameButton newAttButton = new GameButton(pos, plusSign, selectedPlusSign, method) ;
			newAttButton.deactivate() ;
			incAttButtons.put(att, newAttButton) ;
			pos = UtilG.Translate(pos, 0, 22) ;
		}
		
	}
	
	public void updateAttIncButtons(Player player)
	{

		if (((Player) player).getAttPoints() <= 0) { return ;}
		
		incAttButtons.values().forEach(button -> button.activate());
		
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
	
	public void displayTab0(Player player, Equip[] equips, Point mousePos, DrawingOnPanel DP)
	{
		
		Color[] colorPalette = Game.colorPalette ;		
		Font font = new Font(Game.MainFontName, Font.BOLD, 11) ;
		double angle = DrawingOnPanel.stdAngle ;

		String[] classesText = Game.allText.get("Classes") ;
		String[] proClassesText = Game.allText.get("ProClasses") ;
		String[] attText = Game.allText.get("Atributos") ;
		String[] equipsText = Game.allText.get("Equipamentos") ;
		Color textColor = colorPalette[9] ;
		
		Image userImage = player.getMovingAni().idleGif ;
		Point userPos = UtilG.Translate(windowPos, size.width / 2, 120) ;
		DP.DrawImage(userImage, userPos, Align.center) ;

		Point levelPos = UtilG.Translate(windowPos, size.width / 2, 38) ;	
		DP.DrawText(levelPos, Align.center, angle, attText[0] + ": " + player.getLevel(), font, colorPalette[6]) ;
		
		String jobText = player.getProJob() == 0 ? classesText[player.getJob()] : proClassesText[player.getProJob() + 2*player.getJob()];
		Point jobTextPos = UtilG.Translate(windowPos, size.width / 2, 56) ;
		DP.DrawText(jobTextPos, Align.center, angle, jobText, font, colorPalette[5]) ;
		
		
		//	Equips
		if (equips != null)
		{
			int[] eqSlotW = new int[] {51, 51, 51, 8} ;
			int[] eqSlotH = new int[] {51, 51, 51, 24} ;
			Point[] eqSlotCenter = new Point[] {UtilG.Translate(windowPos, 66, 110),
					UtilG.Translate(windowPos, 244, 62),
					UtilG.Translate(windowPos, 244, 134), 
					UtilG.Translate(windowPos, 95, 117)} ;
			for (int eq = 0 ; eq <= equips.length - 1 ; eq += 1)
			{
				if (equips[eq] == null) { continue ;}
				
				Equip equip = equips[eq] ;
				Image[] eqImages = new Image[] {Equip.SwordImage, Equip.ShieldImage, Equip.ArmorImage, Equip.ArrowImage} ;
				Point textPos = UtilG.Translate(eqSlotCenter[eq], -eqSlotW[eq] / 2 - 5, -eqSlotH[eq] / 2 - 2) ;

				DP.DrawImage(eqImages[eq], eqSlotCenter[eq], Align.center) ;
//				DP.DrawTextUntil(textPos, Align.bottomLeft, angle, equip.getName(), font, textColor, 14, mousePos) ;
				if (0 < equip.getForgeLevel())
				{
					DP.DrawText(textPos, Align.bottomCenter, angle, equipsText[eq + 1] + " + " + equip.getForgeLevel(), font, textColor) ;					
				}
				
				Elements eqElem = player.getElem()[eq + 1] ;
				if (eqElem != null) { continue ;}
				
				int elemID = Elements.getID(eqElem) ;
				Image elemImage = DrawingOnPanel.ElementImages[elemID] ;
				Point elemPos = UtilG.Translate(eqSlotCenter[eq], eqSlotW[eq] - 12, eqSlotH[eq] / 2) ;
				DP.DrawImage(elemImage, elemPos, angle, new Scale(0.12, 0.12), Align.center) ;
			}
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
		Point lifePos = UtilG.Translate(windowPos, 20, 38) ;
		Point mpPos = UtilG.Translate(windowPos, 20, 56) ;
		String lifeText = attText[1] + ": " + UtilG.Round(player.getPA().getLife().getCurrentValue(), 1) ;
		String mpText = attText[2] + ": " + UtilG.Round(player.getPA().getMp().getCurrentValue(), 1) ;
		DP.DrawText(lifePos, Align.centerLeft, angle, lifeText, font, colorPalette[6]) ;
		DP.DrawText(mpPos, Align.centerLeft, angle, mpText, font, colorPalette[5]) ;
		
		BasicBattleAttribute[] attributes = player.getBA().basicAttributes() ;
		Point initialAttPos = UtilG.Translate(windowPos, 46, 191) ;
		for (int i = 0; i <= attributes.length - 1; i += 1)
		{
			Point attPos = UtilG.Translate(initialAttPos, 0, i * 22) ;
			String attValue = UtilG.Round(attributes[i].getBaseValue(), 1) + " + " + UtilG.Round(attributes[i].getBonus(), 1) + " + " + UtilG.Round(attributes[i].getTrain(), 1) ;
			DP.DrawImage(attIcons[i], UtilG.Translate(windowPos, 31, 191 + i * 22), new Scale(1, 1), Align.center) ;
			DP.DrawText(attPos, Align.centerLeft, angle, attValue, font, textColor) ;
		}
		Point critPos = UtilG.Translate(initialAttPos, 0, 9 + attributes.length * 22) ;
		String critValue = attText[9] + ": " + UtilG.Round(100 * player.getBA().TotalCritAtkChance(), 1) + "%" ;
		DP.DrawImage(critIcon, UtilG.Translate(windowPos, 31, 333), new Scale(1, 1), Align.center) ;
		DP.DrawText(critPos, Align.centerLeft, angle, critValue, font, colorPalette[6]) ;		
		
		//	Collecting
		DP.DrawImage(collectIcons[0], UtilG.Translate(windowPos, 195, 257), new Scale(1, 1), Align.center) ;
		DP.DrawImage(collectIcons[1], UtilG.Translate(windowPos, 195, 279), new Scale(1, 1), Align.center) ;
		DP.DrawImage(collectIcons[2], UtilG.Translate(windowPos, 195, 301), new Scale(1, 1), Align.center) ;
		
		Point herbPos = UtilG.Translate(windowPos, 210, 257) ;
		Point woodPos = UtilG.Translate(windowPos, 210, 279) ;
		Point metalPos = UtilG.Translate(windowPos, 210, 301) ;
		String herbValue = String.valueOf(UtilG.Round(player.getCollect()[0], 1)) ;
		String woodValue = String.valueOf(UtilG.Round(player.getCollect()[1], 1)) ;
		String metalValue = String.valueOf(UtilG.Round(player.getCollect()[2], 1)) ;
		DP.DrawText(herbPos, Align.centerLeft, angle, herbValue, font, colorPalette[1]) ;
		DP.DrawText(woodPos, Align.centerLeft, angle, woodValue, font, colorPalette[19]) ;
		DP.DrawText(metalPos, Align.centerLeft, angle, metalValue, font, colorPalette[4]) ;


		//	Gold
		Point coinPos = UtilG.Translate(windowPos, 195, 332) ;
		Point goldPos = UtilG.Translate(windowPos, 210, 332) ;
		String goldValue = String.valueOf(UtilG.Round(player.getBag().getGold(), 1)) ;
		DP.DrawImage(Player.CoinIcon, coinPos, angle, new Scale(1, 1), Align.center) ;
		DP.DrawText(goldPos, Align.centerLeft, angle, goldValue, font, Game.colorPalette[2]) ;
		
		incAttButtons.values().forEach(button -> button.display(angle, Align.topLeft, false, mousePos, DP)) ;
	}
	
	public void displayTab1(DrawingOnPanel DP)
	{
		
		Color[] colorPalette = Game.colorPalette ;
		
		double angle = DrawingOnPanel.stdAngle ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 11) ;
		Color textColor = colorPalette[9] ;
		
		String[] attText = Game.allText.get("Atributos") ;
		
		String[] specialAttrPropText = Game.allText.get("Propriedades dos atributos especiais") ;
		int L = size.width ;
		double sx = 0.15 * L ;
		double sy = 1.8 * font.getSize() ;
		Color[] AttributeColor = new Color[] {colorPalette[5], colorPalette[5], colorPalette[6], colorPalette[3], colorPalette[9]} ;
		DP.DrawText(new Point(windowPos.x + (int)(0.65*L), (int)(windowPos.y + sy)), Align.center, angle, specialAttrPropText[1], font, textColor) ;	
		DP.DrawText(new Point(windowPos.x + (int)(0.375*L + sx), (int)(windowPos.y + 2*sy)), Align.center, angle, specialAttrPropText[2], font, textColor) ;	
		DP.DrawText(new Point(windowPos.x + (int)(0.375*L + 3*sx), (int)(windowPos.y + 2*sy)), Align.center, angle, specialAttrPropText[3], font, textColor) ;
		DP.DrawText(new Point(windowPos.x + (int)(0.45*L), (int)(windowPos.y + 3*sy)), Align.center, angle, specialAttrPropText[4], font, textColor) ;	
		DP.DrawText(new Point(windowPos.x + (int)(0.45*L + sx), (int)(windowPos.y + 3*sy)), Align.center, angle, specialAttrPropText[5], font, textColor) ;	
		DP.DrawText(new Point(windowPos.x + (int)(0.45*L + 2*sx), (int)(windowPos.y + 3*sy)), Align.center, angle, specialAttrPropText[4], font, textColor) ;	
		DP.DrawText(new Point(windowPos.x + (int)(0.45*L + 3*sx), (int)(windowPos.y + 3*sy)), Align.center, angle, specialAttrPropText[5], font, textColor) ;	
		for (int i = 0 ; i <= 4 ; ++i)
		{
			DP.DrawText(new Point(windowPos.x + (int)(0.2*L), (int)(windowPos.y + (i + 4)*sy)), Align.center, angle, attText[i + 10], font, AttributeColor[i]) ;	
		}
		for (int i = 0 ; i <= 3 ; ++i)
		{
//			DP.DrawText(new Point(windowPos.x + (int)(0.45*L + i*sx), (int)(windowPos.y + 4*sy)), Align.center, TextAngle, String.valueOf(UtilG.Round(user.getBA().getStun()[i], 2)), font, AttributeColor[0]) ;	
//			DP.DrawText(new Point(windowPos.x + (int)(0.45*L + i*sx), (int)(windowPos.y + 5*sy)), Align.center, TextAngle, String.valueOf(UtilG.Round(user.getBA().getBlock()[i], 2)), font, AttributeColor[1]) ;	
//			DP.DrawText(new Point(windowPos.x + (int)(0.45*L + i*sx), (int)(windowPos.y + 6*sy)), Align.center, TextAngle, String.valueOf(UtilG.Round(user.getBA().getBlood()[i], 2)), font, AttributeColor[2]) ;	
//			DP.DrawText(new Point(windowPos.x + (int)(0.45*L + i*sx), (int)(windowPos.y + 7*sy)), Align.center, TextAngle, String.valueOf(UtilG.Round(user.getBA().getPoison()[i], 2)), font, AttributeColor[3]) ;	
//			DP.DrawText(new Point(windowPos.x + (int)(0.45*L + i*sx), (int)(windowPos.y + 8*sy)), Align.center, TextAngle, String.valueOf(UtilG.Round(user.getBA().getSilence()[i], 2)), font, AttributeColor[4]) ;				
		}			
		DP.DrawText(new Point(windowPos.x + (int)(0.65*L), (int)(windowPos.y + 10*sy)), Align.center, angle, specialAttrPropText[6], font, textColor) ;	
		DP.DrawText(new Point(windowPos.x + (int)(0.375*L + sx), (int)(windowPos.y + 11*sy)), Align.center, angle, specialAttrPropText[2], font, textColor) ;	
		DP.DrawText(new Point(windowPos.x + (int)(0.375*L + 3*sx), (int)(windowPos.y + 11*sy)), Align.center, angle, specialAttrPropText[3], font, textColor) ;
		DP.DrawText(new Point(windowPos.x + (int)(0.45*L), (int)(windowPos.y + 12*sy)), Align.center, angle, specialAttrPropText[4], font, textColor) ;	
		DP.DrawText(new Point(windowPos.x + (int)(0.45*L + sx), (int)(windowPos.y + 12*sy)), Align.center, angle, specialAttrPropText[5], font, textColor) ;	
		DP.DrawText(new Point(windowPos.x + (int)(0.45*L + 2*sx), (int)(windowPos.y + 12*sy)), Align.center, angle, specialAttrPropText[4], font, textColor) ;	
		DP.DrawText(new Point(windowPos.x + (int)(0.45*L + 3*sx), (int)(windowPos.y + 12*sy)), Align.center, angle, specialAttrPropText[5], font, textColor) ;	
		for (int i = 0 ; i <= 1 ; ++i)
		{
			DP.DrawText(new Point(windowPos.x + (int)(0.2*L), (int)(windowPos.y + (i + 13)*sy)), Align.center, angle, attText[i + 13], font, AttributeColor[i + 2]) ;	
		}
		for (int i = 0 ; i <= 3 ; ++i)
		{
//			DP.DrawText(new Point(windowPos.x + (int)(0.45*L + i*sx), (int)(windowPos.y + 13*sy)), Align.center, TextAngle, String.valueOf(UtilG.Round(user.getBA().getBlood()[i + 4], 2)), font, AttributeColor[2]) ;	
//			DP.DrawText(new Point(windowPos.x + (int)(0.45*L + i*sx), (int)(windowPos.y + 14*sy)), Align.center, TextAngle, String.valueOf(UtilG.Round(user.getBA().getPoison()[i + 4], 2)), font, AttributeColor[3]) ;	
		}			
//		DP.DrawText(new Point(windowPos.x + (int)(0.025*L), (int)(windowPos.y + 16*sy)), Align.bottomLeft, TextAngle, attText[11] + " " + specialAttrPropText[7] + " = " + UtilG.Round(user.getBA().getStun()[4], 2), font, AttributeColor[0]) ;	
//		DP.DrawText(new Point(windowPos.x + (int)(0.025*L), (int)(windowPos.y + 17*sy)), Align.bottomLeft, TextAngle, attText[12] + " " + specialAttrPropText[7] + " = " + UtilG.Round(user.getBA().getBlock()[4], 2), font, AttributeColor[1]) ;	
//		DP.DrawText(new Point(windowPos.x + (int)(0.025*L), (int)(windowPos.y + 18*sy)), Align.bottomLeft, TextAngle, attText[13] + " " + specialAttrPropText[7] + " = " + UtilG.Round(user.getBA().getBlood()[8], 2), font, AttributeColor[2]) ;	
//		DP.DrawText(new Point(windowPos.x + (int)(0.025*L), (int)(windowPos.y + 19*sy)), Align.bottomLeft, TextAngle, attText[14] + " " + specialAttrPropText[7] + " = " + UtilG.Round(user.getBA().getPoison()[8], 2), font, AttributeColor[3]) ;	
//		DP.DrawText(new Point(windowPos.x + (int)(0.025*L), (int)(windowPos.y + 20*sy)), Align.bottomLeft, TextAngle, attText[15] + " " + specialAttrPropText[7] + " = " + UtilG.Round(user.getBA().getSilence()[4], 2), font, AttributeColor[4]) ;	
		
	}
	
	public void displayTab2(Player player, DrawingOnPanel DP)
	{
		
		Font font = new Font(Game.MainFontName, Font.BOLD, 11) ;
		
		Map<String, Double> allStats = player.getStatistics().allStatistics() ;
		Dimension offset = new Dimension(25, 30) ;
		Point textPos = new Point(windowPos.x + offset.width, windowPos.y + offset.height) ;
		int sy = 2 + font.getSize() ;
		for (String key : allStats.keySet())
		{
			String text = key + ": " + String.valueOf(UtilG.Round((double) allStats.get(key), 1)) ;
			DP.DrawText(textPos, Align.bottomLeft, DrawingOnPanel.stdAngle, text, font, Game.colorPalette[9]) ;
			textPos = UtilG.Translate(textPos, 0, sy) ;
		}
		
	}
		
	public void display(Player player, Equip[] equips, Point mousePos, DrawingOnPanel DP)
	{
		
		Font namefont = new Font(Game.MainFontName, Font.BOLD, 13) ;

		String[] tabsText = Game.allText.get("Janela do jogador") ;
		Color[] colorPalette = Game.colorPalette ;
		Color[] tabColor = new Color[] {colorPalette[7], colorPalette[7], colorPalette[7]} ;
		Color[] tabTextColor = new Color[] {colorPalette[5], colorPalette[5], colorPalette[5]} ;
		tabColor[tab] = colorPalette[19] ;
		tabTextColor[tab] = colorPalette[3] ;
		
		
		// Main window
		switch (tab)
		{
			case 0: DP.DrawImage(image, windowPos, Align.topLeft) ; break ;
			case 1: DP.DrawImage(tab1Image, windowPos, Align.topLeft) ; break ;
			case 2: DP.DrawImage(tab2Image, windowPos, Align.topLeft) ; break ;
		}

		DP.DrawText(UtilG.Translate(windowPos, 5, 20), Align.center, 90, tabsText[0], namefont, tabTextColor[0]) ;
		DP.DrawText(UtilG.Translate(windowPos, 5, 55), Align.center, 90, tabsText[1], namefont, tabTextColor[1]) ;
		DP.DrawText(UtilG.Translate(windowPos, 5, 90), Align.center, 90, tabsText[2], namefont, tabTextColor[2]) ;
			
		switch (tab)
		{
			case 0: displayTab0(player, equips, mousePos, DP) ; break ;
			case 1: displayTab1(DP) ; break ;
			case 2: displayTab2(player, DP) ; break ;
		}		

		Point namePos = UtilG.Translate(windowPos, size.width / 2, 11) ;
		DP.DrawText(namePos, Align.center, DrawingOnPanel.stdAngle, player.getName(), namefont, colorPalette[9]) ;	
		
	}
	
}
