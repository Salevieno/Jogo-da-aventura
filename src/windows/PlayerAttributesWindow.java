package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.Map;

import attributes.AttributeBonus;
import attributes.BasicBattleAttribute;
import attributes.BattleAttributes;
import attributes.PersonalAttributes;
import components.GameIcon;
import graphics.DrawingOnPanel;
import items.Equip;
import liveBeings.LiveBeing;
import liveBeings.Player;
import main.Game;
import utilities.Align;
import utilities.Elements;
import utilities.Scale;
import utilities.UtilG;
import utilities.UtilS;

public class PlayerAttributesWindow extends GameWindow
{
	private GameIcon[] addAttIcon ;
	
	public PlayerAttributesWindow(Image image)
	{
		super("Atributos", image, 0, 3, 0, 0) ;
		Image PlusSignImage = UtilG.loadImage(Game.ImagesPath + "\\Icons\\" + "PlusSign.png") ;
		Image SelectedPlusSignImage = UtilG.loadImage(Game.ImagesPath + "\\Icons\\" + "ShiningPlusSign.png") ;
		addAttIcon = new GameIcon[7] ;
		addAttIcon[0] = new GameIcon(0, "Plus sign", new Point(100, 100), null, PlusSignImage, SelectedPlusSignImage) ;
		addAttIcon[1] = new GameIcon(1, "Plus sign", new Point(100, 120), null, PlusSignImage, SelectedPlusSignImage) ;
		addAttIcon[2] = new GameIcon(2, "Plus sign", new Point(100, 140), null, PlusSignImage, SelectedPlusSignImage) ;
		addAttIcon[3] = new GameIcon(3, "Plus sign", new Point(100, 160), null, PlusSignImage, SelectedPlusSignImage) ;
		addAttIcon[4] = new GameIcon(4, "Plus sign", new Point(100, 180), null, PlusSignImage, SelectedPlusSignImage) ;
		addAttIcon[5] = new GameIcon(5, "Plus sign", new Point(100, 200), null, PlusSignImage, SelectedPlusSignImage) ;
		addAttIcon[6] = new GameIcon(6, "Plus sign", new Point(100, 220), null, PlusSignImage, SelectedPlusSignImage) ;
	}
	
	public void navigate(String action)
	{
//		if (getTab() == 0)
//		{
//			ApplyAttributesIncrease(player, MousePos) ;
//		}
		if (action.equals(Player.ActionKeys[2]))
		{
			tabUp() ;
		}
		if (action.equals(Player.ActionKeys[0]))
		{
			tabDown() ;
		}
	}
	
	public void ApplyAttributesIncrease(Player player, Point MousePos)
	{
		if (0 < player.getAttPoints())
		{
			PersonalAttributes PA = player.getPA() ;
			BattleAttributes BA = player.getBA() ;
			AttributeBonus PlayerAttributeIncrease = player.getAttIncrease() ;
			for (int attIcon = 0 ; attIcon <= addAttIcon.length - 1 ; attIcon += 1)
			{
				if (addAttIcon[attIcon].ishovered(MousePos) & (player.getCurrentAction().equals("Enter") | player.getCurrentAction().equals("MouseLeftClick")))
				{
//					if (attIcon == 0)
//					{
//						PA.getLife().incCurrentValue((int) PlayerAttributeIncrease[attIcon]); ;
//						PA.getLife().incCurrentValue((int) PlayerAttributeIncrease[attIcon]); ;
//					}
//					else if (attIcon == 1)
//					{
//						PA.getMp().incCurrentValue((int) PlayerAttributeIncrease[attIcon]); ;
//						PA.getMp().incCurrentValue((int) PlayerAttributeIncrease[attIcon]); ;
//					}
//					else if (attIcon == 2)
//					{
//						BA.getPhyAtk().incBaseValue(PlayerAttributeIncrease[attIcon]) ;
//					}
//					else if (attIcon == 3)
//					{
//						BA.getMagAtk().incBaseValue(PlayerAttributeIncrease[attIcon]) ;
//					}
//					else if (attIcon == 4)
//					{
//						BA.getPhyDef().incBaseValue(PlayerAttributeIncrease[attIcon]) ;
//					}
//					else if (attIcon == 5)
//					{
//						BA.getMagDef().incBaseValue(PlayerAttributeIncrease[attIcon]) ;
//					}
//					else if (attIcon == 6)
//					{
//						BA.getDex().incBaseValue(PlayerAttributeIncrease[attIcon]) ;
//					}
//					else if (attIcon == 7)
//					{
//						BA.getAgi().incBaseValue(PlayerAttributeIncrease[attIcon]) ;
//					}
					player.decAttPoints(1) ;
				}
			}
		}
	}
	
	public void display(LiveBeing user, Map<String, String[]> allText, Equip[] equips, Point mousePos, DrawingOnPanel DP)
	{
		Dimension screenSize = Game.getScreen().getSize() ;
		Point windowPos = new Point((int) (0.2 * screenSize.width), (int)(0.2 * screenSize.height)) ;
		double angle = DrawingOnPanel.stdAngle ;
		
		Font namefont = new Font(Game.MainFontName, Font.BOLD, 13) ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 11) ;
		
		Color[] colorPalette = Game.ColorPalette ;
		Color[] tabColor = new Color[] {colorPalette[7], colorPalette[7], colorPalette[7]} ;
		Color[] tabTextColor = new Color[] {colorPalette[5], colorPalette[5], colorPalette[5]} ;
		Color textColor = colorPalette[2] ;
		tabColor[tab] = colorPalette[19] ;
		tabTextColor[tab] = colorPalette[3] ;
		
		String[] classesText = allText.get("* Classes *") ;
		String[] proClassesText = allText.get("* ProClasses *") ;
		String[] attText = allText.get("* Atributos *") ;
		String[] equipsText = allText.get("* Equipamentos *") ;
		String[] tabsText = allText.get("* Janela do jogador *") ;
		
		// Main window
		DP.DrawImage(image, windowPos, Align.topLeft) ;
		
		DP.DrawText(UtilG.Translate(windowPos, 5, 20), Align.center, 90, tabsText[1], namefont, tabTextColor[0]) ;
		DP.DrawText(UtilG.Translate(windowPos, 5, 30), Align.center, 90, tabsText[2], namefont, tabTextColor[1]) ;
		DP.DrawText(UtilG.Translate(windowPos, 5, 40), Align.center, 90, tabsText[3], namefont, tabTextColor[2]) ;
		if (tab == 0)
		{
			Image userImage = user.getMovingAni().idleGif ;
			Point userPos = UtilG.Translate(windowPos, size.width / 2, 120) ;
			DP.DrawImage(userImage, userPos, Align.center) ;

			Point namePos = UtilG.Translate(windowPos, size.width / 2, 11) ;
			Point levelPos = UtilG.Translate(windowPos, size.width / 2, 38) ;
			DP.DrawText(namePos, Align.center, angle, user.getName(), namefont, textColor) ;		
			DP.DrawText(levelPos, Align.center, angle, attText[1] + ": " + user.getLevel(), font, colorPalette[6]) ;
			
			String jobText = user.getProJob() == 0 ? classesText[user.getJob() + 1] : proClassesText[user.getProJob() + 2*user.getJob()];
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
//					DP.DrawTextUntil(textPos, Align.bottomLeft, angle, equip.getName(), font, textColor, 14, mousePos) ;
					if (0 < equip.getForgeLevel())
					{
						DP.DrawText(textPos, Align.bottomCenter, angle, equipsText[eq + 1] + " + " + equip.getForgeLevel(), font, textColor) ;					
					}
					
					Elements eqElem = user.getElem()[eq + 1] ;
					if (eqElem != null) { continue ;}
					
					Image elemImage = DrawingOnPanel.ElementImages[UtilS.ElementID(eqElem)] ;
					Point elemPos = UtilG.Translate(eqSlotCenter[eq], eqSlotW[eq] - 12, eqSlotH[eq] / 2) ;
					DP.DrawImage(elemImage, elemPos, angle, new Scale(0.12, 0.12), Align.center) ;
				}
			}
			
			
			// super element
			if (user.hasSuperElement())
			{
				Point superElemPos = UtilG.Translate(userPos, 0, 35) ;
				Image superElemImage = DrawingOnPanel.ElementImages[UtilS.ElementID(user.getElem()[4])] ;
				DP.DrawImage(superElemImage, superElemPos, angle, new Scale(0.3, 0.3), Align.center) ;
			}
			
			
			// attributes
			Point lifePos = UtilG.Translate(windowPos, 20, 38) ;
			Point mpPos = UtilG.Translate(windowPos, 20, 56) ;
			String lifeText = attText[2] + ": " + UtilG.Round(user.getPA().getLife().getCurrentValue(), 1) ;
			String mpText = attText[3] + ": " + UtilG.Round(user.getPA().getMp().getCurrentValue(), 1) ;
			DP.DrawText(lifePos, Align.centerLeft, angle, lifeText, font, colorPalette[6]) ;
			DP.DrawText(mpPos, Align.centerLeft, angle, mpText, font, colorPalette[5]) ;
			
			Scale iconsScale = new Scale(13 / 38.0, 13 / 38.0) ;
			DP.DrawImage(Equip.SwordImage, UtilG.Translate(windowPos, 31, 191), iconsScale, Align.center) ;
			DP.DrawImage(Equip.SwordImage, UtilG.Translate(windowPos, 31, 213), iconsScale, Align.center) ;
			DP.DrawImage(Equip.SwordImage, UtilG.Translate(windowPos, 31, 235), iconsScale, Align.center) ;
			DP.DrawImage(Equip.SwordImage, UtilG.Translate(windowPos, 31, 257), iconsScale, Align.center) ;
			DP.DrawImage(Equip.SwordImage, UtilG.Translate(windowPos, 31, 279), iconsScale, Align.center) ;
			DP.DrawImage(Equip.SwordImage, UtilG.Translate(windowPos, 31, 301), iconsScale, Align.center) ;

			BasicBattleAttribute[] attributes = user.getBA().basicAttributes() ;
			Point initialAttPos = UtilG.Translate(windowPos, 46, 191) ;
			for (int i = 0; i <= attributes.length - 1; i += 1)
			{
				Point attPos = UtilG.Translate(initialAttPos, 0, i * 22) ;
				String attValue = UtilG.Round(attributes[i].getBaseValue(), 1) + " + " + UtilG.Round(attributes[i].getBonus(), 1) + " + " + UtilG.Round(attributes[i].getTrain(), 1) ;
				DP.DrawText(attPos, Align.centerLeft, angle, attValue, font, textColor) ;
			}
			Point critPos = UtilG.Translate(initialAttPos, 0, 9 + attributes.length * 22) ;
			String critValue = attText[10] + ": " + UtilG.Round(100 * user.getBA().TotalCritAtkChance(), 1) + "%" ;
			DP.DrawText(critPos, Align.centerLeft, angle, critValue, font, colorPalette[6]) ;		
			
			//	Collecting
			if (user instanceof Player)
			{
				Player player = (Player) user ;
				DP.DrawImage(Equip.SwordImage, UtilG.Translate(windowPos, 195, 257), iconsScale, Align.center) ;
				DP.DrawImage(Equip.SwordImage, UtilG.Translate(windowPos, 195, 279), iconsScale, Align.center) ;
				DP.DrawImage(Equip.SwordImage, UtilG.Translate(windowPos, 195, 301), iconsScale, Align.center) ;
				
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
				DP.DrawText(goldPos, Align.centerLeft, angle, goldValue, font, textColor) ;
			}
		}
		else if (tab == 1)
		{
			String[] specialAttrPropText = allText.get("* Propriedades dos atributos especiais *") ;
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
				DP.DrawText(new Point(windowPos.x + (int)(0.2*L), (int)(windowPos.y + (i + 4)*sy)), Align.center, angle, attText[i + 11], font, AttributeColor[i]) ;	
			}
			for (int i = 0 ; i <= 3 ; ++i)
			{
//				DP.DrawText(new Point(windowPos.x + (int)(0.45*L + i*sx), (int)(windowPos.y + 4*sy)), Align.center, TextAngle, String.valueOf(UtilG.Round(user.getBA().getStun()[i], 2)), font, AttributeColor[0]) ;	
//				DP.DrawText(new Point(windowPos.x + (int)(0.45*L + i*sx), (int)(windowPos.y + 5*sy)), Align.center, TextAngle, String.valueOf(UtilG.Round(user.getBA().getBlock()[i], 2)), font, AttributeColor[1]) ;	
//				DP.DrawText(new Point(windowPos.x + (int)(0.45*L + i*sx), (int)(windowPos.y + 6*sy)), Align.center, TextAngle, String.valueOf(UtilG.Round(user.getBA().getBlood()[i], 2)), font, AttributeColor[2]) ;	
//				DP.DrawText(new Point(windowPos.x + (int)(0.45*L + i*sx), (int)(windowPos.y + 7*sy)), Align.center, TextAngle, String.valueOf(UtilG.Round(user.getBA().getPoison()[i], 2)), font, AttributeColor[3]) ;	
//				DP.DrawText(new Point(windowPos.x + (int)(0.45*L + i*sx), (int)(windowPos.y + 8*sy)), Align.center, TextAngle, String.valueOf(UtilG.Round(user.getBA().getSilence()[i], 2)), font, AttributeColor[4]) ;				
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
//				DP.DrawText(new Point(windowPos.x + (int)(0.45*L + i*sx), (int)(windowPos.y + 13*sy)), Align.center, TextAngle, String.valueOf(UtilG.Round(user.getBA().getBlood()[i + 4], 2)), font, AttributeColor[2]) ;	
//				DP.DrawText(new Point(windowPos.x + (int)(0.45*L + i*sx), (int)(windowPos.y + 14*sy)), Align.center, TextAngle, String.valueOf(UtilG.Round(user.getBA().getPoison()[i + 4], 2)), font, AttributeColor[3]) ;	
			}			
//			DP.DrawText(new Point(windowPos.x + (int)(0.025*L), (int)(windowPos.y + 16*sy)), Align.bottomLeft, TextAngle, attText[11] + " " + specialAttrPropText[7] + " = " + UtilG.Round(user.getBA().getStun()[4], 2), font, AttributeColor[0]) ;	
//			DP.DrawText(new Point(windowPos.x + (int)(0.025*L), (int)(windowPos.y + 17*sy)), Align.bottomLeft, TextAngle, attText[12] + " " + specialAttrPropText[7] + " = " + UtilG.Round(user.getBA().getBlock()[4], 2), font, AttributeColor[1]) ;	
//			DP.DrawText(new Point(windowPos.x + (int)(0.025*L), (int)(windowPos.y + 18*sy)), Align.bottomLeft, TextAngle, attText[13] + " " + specialAttrPropText[7] + " = " + UtilG.Round(user.getBA().getBlood()[8], 2), font, AttributeColor[2]) ;	
//			DP.DrawText(new Point(windowPos.x + (int)(0.025*L), (int)(windowPos.y + 19*sy)), Align.bottomLeft, TextAngle, attText[14] + " " + specialAttrPropText[7] + " = " + UtilG.Round(user.getBA().getPoison()[8], 2), font, AttributeColor[3]) ;	
//			DP.DrawText(new Point(windowPos.x + (int)(0.025*L), (int)(windowPos.y + 20*sy)), Align.bottomLeft, TextAngle, attText[15] + " " + specialAttrPropText[7] + " = " + UtilG.Round(user.getBA().getSilence()[4], 2), font, AttributeColor[4]) ;	
		}
		else if (tab == 2)
		{
			if (user instanceof Player)
			{
				Player player = (Player) user ;
				Map<String, Double> allStats = player.getStatistics().allStatistics() ;
				Dimension offset = new Dimension(25, 20) ;
				Point textPos = new Point(windowPos.x + offset.width, windowPos.y + offset.height) ;
				int sy = 20 ;
				for (String key : allStats.keySet())
				{
					String text = key + ": " + String.valueOf(UtilG.Round((double) allStats.get(key), 1)) ;
					DP.DrawText(textPos, Align.bottomLeft, angle, text, namefont, colorPalette[5]) ;
					textPos = UtilG.Translate(textPos, 0, sy) ;
				}
			}
		}
	}
}
