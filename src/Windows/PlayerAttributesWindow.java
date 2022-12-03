package Windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.Map;

import javax.swing.ImageIcon;

import GameComponents.Icon;
import GameComponents.Items;
import Graphics.DrawFunctions;
import Graphics.DrawPrimitives;
import Items.Equip;
import LiveBeings.BattleAttributes;
import LiveBeings.LiveBeing;
import LiveBeings.PersonalAttributes;
import LiveBeings.Player;
import Main.Game;
import Utilities.Scale;
import Utilities.Size;
import Utilities.UtilG;
import Utilities.UtilS;

public class PlayerAttributesWindow extends Window
{
	Icon[] addAttIcon ;
	public PlayerAttributesWindow()
	{
		super(null, 0, 3, 0, 0) ;
		Image PlusSignImage = new ImageIcon(Game.ImagesPath + "PlusSign.png").getImage() ;
		Image SelectedPlusSignImage = new ImageIcon(Game.ImagesPath + "ShiningPlusSign.png").getImage() ;
		addAttIcon = new Icon[7] ;
		addAttIcon[0] = new Icon(0, "Plus sign", new Point(100, 100), null, PlusSignImage, SelectedPlusSignImage) ;
		addAttIcon[1] = new Icon(1, "Plus sign", new Point(100, 120), null, PlusSignImage, SelectedPlusSignImage) ;
		addAttIcon[2] = new Icon(2, "Plus sign", new Point(100, 140), null, PlusSignImage, SelectedPlusSignImage) ;
		addAttIcon[3] = new Icon(3, "Plus sign", new Point(100, 160), null, PlusSignImage, SelectedPlusSignImage) ;
		addAttIcon[4] = new Icon(4, "Plus sign", new Point(100, 180), null, PlusSignImage, SelectedPlusSignImage) ;
		addAttIcon[5] = new Icon(5, "Plus sign", new Point(100, 200), null, PlusSignImage, SelectedPlusSignImage) ;
		addAttIcon[6] = new Icon(6, "Plus sign", new Point(100, 220), null, PlusSignImage, SelectedPlusSignImage) ;
	}
	
	public void navigate(Player player, String action, Point MousePos)
	{
		if (getTab() == 0)
		{
			ApplyAttributesIncrease(player, MousePos) ;
		}
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
			float[] PlayerAttributeIncrease = player.getAttIncrease()[player.getProJob()] ;
			for (int attIcon = 0 ; attIcon <= addAttIcon.length - 1 ; attIcon += 1)
			{
				if (addAttIcon[attIcon].ishovered(MousePos) & (PA.getCurrentAction().equals("Enter") | PA.getCurrentAction().equals("MouseLeftClick")))
				{
					if (attIcon == 0)
					{
						PA.getLife()[0] += PlayerAttributeIncrease[attIcon] ;
						PA.getLife()[1] += PlayerAttributeIncrease[attIcon] ;
					}
					else if (attIcon == 1)
					{
						PA.getMp()[0] += PlayerAttributeIncrease[attIcon] ;
						PA.getMp()[1] += PlayerAttributeIncrease[attIcon] ;
					}
					else if (attIcon == 2)
					{
						BA.getPhyAtk()[0] += PlayerAttributeIncrease[attIcon] ;
					}
					else if (attIcon == 3)
					{
						BA.getMagAtk()[0] += PlayerAttributeIncrease[attIcon] ;
					}
					else if (attIcon == 4)
					{
						BA.getPhyDef()[0] += PlayerAttributeIncrease[attIcon] ;
					}
					else if (attIcon == 5)
					{
						BA.getMagDef()[0] += PlayerAttributeIncrease[attIcon] ;
					}
					else if (attIcon == 6)
					{
						BA.getDex()[0] += PlayerAttributeIncrease[attIcon] ;
					}
					else if (attIcon == 7)
					{
						BA.getAgi()[0] += PlayerAttributeIncrease[attIcon] ;
					}
					player.decAttPoints(1) ;
				}
			}
		}
	}
	
	public void display(LiveBeing user, Map<String, String[]> allText, Equip[] Equips, float[][] EquipsBonus, int AttPoints, Point MousePos, PersonalAttributes PA, BattleAttributes BA, DrawPrimitives DP)
	{
		// Font "GothicE"
		Size screenSize = Game.getScreen().getSize() ;
		Point WindowPos = new Point((int) (0.2 * screenSize.x), (int)(0.3 * screenSize.y)) ;
		Size attWindowSize = new Size(Player.AttWindowImages[0].getWidth(null), Player.AttWindowImages[0].getHeight(null)) ;
		float TextAngle = DrawPrimitives.OverallAngle ;
		
		Font namefont = new Font(Game.MainFontName, Font.BOLD, attWindowSize.x / 24) ;
		Font font = new Font(Game.MainFontName, Font.BOLD, attWindowSize.x / 24 + 1) ;
		
		Color[] ColorPalette = Game.ColorPalette ;
		Color[] TabColor = new Color[] {ColorPalette[7], ColorPalette[7], ColorPalette[7]}, TabTextColor = new Color[] {ColorPalette[5], ColorPalette[5], ColorPalette[5]} ;
		Color TextColor = ColorPalette[2] ;
		TabColor[tab] = ColorPalette[19] ;
		TabTextColor[tab] = ColorPalette[3] ;
		
		String[] classesText = allText.get("* Classes *") ;
		String[] proClassesText = allText.get("* ProClasses *") ;
		String[] attText = allText.get("* Atributos *") ;
		String[] equipsText = allText.get("* Equipamentos *") ;
		String[] tabsText = allText.get("* Janela do jogador *") ;
		
		// Main window
		DP.DrawImage(Player.AttWindowImages[tab], WindowPos, "TopLeft") ;
		DP.DrawText(new Point(WindowPos.x + 5, WindowPos.y + (int)(0.05*attWindowSize.y)), "Center", 90, tabsText[1], namefont, TabTextColor[0]) ;				// Tab 0 text	
		DP.DrawText(new Point(WindowPos.x + 5, WindowPos.y + (int)(0.15*attWindowSize.y)), "Center", 90, tabsText[2], namefont, TabTextColor[1]) ;				// Tab 1 text	
		DP.DrawText(new Point(WindowPos.x + 5, WindowPos.y + (int)(0.25*attWindowSize.y)), "Center", 90, tabsText[3], namefont, TabTextColor[2]) ;				// Tab 2 text	
		if (tab == 0)
		{
			//	Player
			if (user instanceof Player)
			{
				Player player = (Player) user ;
				Point PlayerImagePos = new Point(WindowPos.x + (int) (0.55 * attWindowSize.x), WindowPos.y + (int) (0.35 * attWindowSize.y)) ;
				player.display(PlayerImagePos, new Scale(1, 1), PA.getDir(), false, DP) ;
			}

			DP.DrawText(new Point(WindowPos.x + (int)(0.5*attWindowSize.x), WindowPos.y + (int)(0.03*attWindowSize.y)), "Center", TextAngle, PA.getName(), namefont, TextColor) ;							// Name text			
			DP.DrawText(new Point(WindowPos.x + (int)(0.5*attWindowSize.x), WindowPos.y + (int)(0.06*attWindowSize.y)), "Center", TextAngle, attText[1] + ": " + PA.getLevel(), font, ColorPalette[6]) ;	// Level text		
			if (PA.getProJob() == 0)
			{
				Point jobTextPos = new Point(WindowPos.x + (int)(0.5*attWindowSize.x), WindowPos.y + (int)(0.10*attWindowSize.y)) ;
				DP.DrawText(jobTextPos, "Center", TextAngle, classesText[PA.getJob() + 1], font, ColorPalette[5]) ;	// Job text			
			}
			else
			{
				Point proJobTextPos = new Point(WindowPos.x + (int)(0.5*attWindowSize.x), WindowPos.y + (int)(0.12*attWindowSize.y)) ;
				DP.DrawText(proJobTextPos, "Center", TextAngle, proClassesText[PA.getProJob() + 2*PA.getJob()], font, ColorPalette[5]) ;	// Pro job text					
			}
			
			
			//	Equips
			int[] EqRectL = new int[] {51, 51, 51, 8} ;
			int[] EqRectH = new int[] {51, 51, 51, 24} ;
			Point[] EqRectPos = new Point[] {new Point(WindowPos.x + 30, WindowPos.y + 90),
					new Point(WindowPos.x + 180, WindowPos.y + 50),
					new Point(WindowPos.x + 180, WindowPos.y + 115), 
					new Point(WindowPos.x + 53, WindowPos.y + 140)} ;	// Weapon, armor, shield, arrow
			for (int eq = 0 ; eq <= 4 - 1 ; eq += 1)
			{
				Image ElemImage = DrawFunctions.ElementImages[UtilS.ElementID(PA.getElem()[eq + 1])] ;
				if (Equips[eq] != null)
				{
					Point textPos = new Point(EqRectPos[eq].x, EqRectPos[eq].y - EqRectH[eq] / 2) ;
					int eqBonus = (int)(EquipsBonus[Equips[eq].getId() - Items.BagIDs[6]][1]) ;
					if (eq <= 2)
					{
						if (0 < EquipsBonus[Equips[eq].getId() - Items.BagIDs[6]][0])
						{
							DP.DrawText(textPos, "Center", TextAngle, equipsText[eq + 1] + " + " + eqBonus, font, TextColor) ;					
						}
						//DF.DrawEquips(EqRectPos[eq], PA.getJob(), eq, Equips[eq] - Items.BagIDs[6], EquipsBonus, new float[] {1, 1}, TextAngle) ;
					}
					else if (eq == 3)
					{
						//DF.DrawEquips(EqRectPos[eq], PA.getJob(), eq, Equips[0] - Items.BagIDs[6], EquipsBonus, new float[] {1, 1}, TextAngle) ;
					}
					DP.DrawImage(ElemImage, new Point((int) (EqRectPos[eq].x + 0.44*EqRectL[eq]), (int) (EqRectPos[eq].y + 0.15*EqRectH[eq])), TextAngle, new Scale(0.12, 0.12), new boolean[] {false, false}, "Center", 1) ;					
					//DP.DrawTextUntil(new Point(EqRectPos[eq].x - EqRectL[eq] / 2, EqRectPos[eq].y + EqRectH[eq] / 2 + TextH), "BotLeft", TextAngle, items[Equips[eq]].getName(), Equipfont, TextColor, 14, MousePos) ;	// Equip text	
				}
				else
				{
					DP.DrawText(new Point(EqRectPos[eq].x + EqRectL[eq]/2, EqRectPos[eq].y - EqRectH[eq] / 2), "Center", TextAngle, equipsText[eq + 1], font, TextColor) ;
				}
			}
			
			
			// Super element
			if (PA.getElem()[1].equals(PA.getElem()[2]) & PA.getElem()[2].equals(PA.getElem()[3]))
			{
				DP.DrawImage(DrawFunctions.ElementImages[UtilS.ElementID(PA.getElem()[4])],
						new Point(WindowPos.x + (int)(0.55*attWindowSize.x), WindowPos.y + (int)(0.4*attWindowSize.y)), TextAngle, new Scale(0.3, 0.3),
						new boolean[] {false, false}, "Center", 1) ;
			}
			
			
			//	Attributes
			float[] attributes = new float[] {BA.TotalPhyAtk(), BA.TotalMagAtk(), BA.TotalPhyDef(), BA.TotalMagDef(), BA.TotalDex(), BA.TotalAgi()} ;
			float[][] attDetails = new float[][] {BA.getPhyAtk(), BA.getMagAtk(), BA.getPhyDef(), BA.getMagDef(), BA.getDex(), BA.getAgi()} ;
			int AttSy = 22 ;
			DP.DrawText(new Point(WindowPos.x + 20, WindowPos.y + (int)(0.10*attWindowSize.y)), "BotLeft", TextAngle, attText[2] + ": " + UtilG.Round(PA.getLife()[0], 1), font, ColorPalette[6]) ;	// Life text	
			DP.DrawText(new Point(WindowPos.x + 20, WindowPos.y + (int)(0.15*attWindowSize.y)), "BotLeft", TextAngle, attText[3] + ": " + UtilG.Round(PA.getMp()[0], 1), font, ColorPalette[5]) ;	// MP text
			
			DP.DrawImage(Equip.SwordImage, new Point(WindowPos.x + 30, WindowPos.y + 134 + 1 * AttSy), new Scale(13 / 38.0, 13 / 38.0), "Center") ;	// Draw sword icon
			
			for (int i = 0; i <= attributes.length - 1; i += 1)
			{
				DP.DrawText(new Point(WindowPos.x + 45, WindowPos.y + 136 + (i + 1) * AttSy), "BotLeft", TextAngle, UtilG.Round(attDetails[i][0], 1) + " + "+ UtilG.Round(attDetails[i][1], 1) + " + " + UtilG.Round(attDetails[i][2], 1), font, TextColor) ;
			}	
			DP.DrawText(new Point(WindowPos.x + 45, (int) (WindowPos.y + 136 + 7.6 * AttSy)), "BotLeft", TextAngle, attText[10] + ": " + UtilG.Round(100 * BA.TotalCritAtkChance(), 1) + "%", font, ColorPalette[6]) ;		
			
			//	Collecting
			if (user instanceof Player)
			{
				Player player = (Player) user ;
				DP.DrawText(new Point(WindowPos.x + (int)(0.80 * attWindowSize.x), WindowPos.y + (int)(0.46 * attWindowSize.y)), "TopRight", TextAngle, String.valueOf(UtilG.Round(player.getCollect()[0], 1)), font, DrawFunctions.MapsTypeColor[13]) ;		
				DP.DrawText(new Point(WindowPos.x + (int)(0.80 * attWindowSize.x), WindowPos.y + (int)(0.53 * attWindowSize.y)), "TopRight", TextAngle, String.valueOf(UtilG.Round(player.getCollect()[1], 1)), font, DrawFunctions.MapsTypeColor[14]) ;		
				DP.DrawText(new Point(WindowPos.x + (int)(0.80 * attWindowSize.x), WindowPos.y + (int)(0.60 * attWindowSize.y)), "TopRight", TextAngle, String.valueOf(UtilG.Round(player.getCollect()[2], 1)), font, DrawFunctions.MapsTypeColor[15]) ;

				//	Gold
				DP.DrawImage(Player.CoinIcon, new Point(WindowPos.x + (int)(0.64 * attWindowSize.x), WindowPos.y + (int)(0.92 * attWindowSize.y)), TextAngle, new Scale(1.2, 1.2), new boolean[] {false, false}, "CenterLeft", 1) ;
				DP.DrawText(new Point(WindowPos.x + (int)(0.52 * attWindowSize.x), WindowPos.y + (int)(0.92 * attWindowSize.y)), "CenterLeft", TextAngle, String.valueOf(UtilG.Round(player.getGold()[0], 1)), font, TextColor) ;	// Gold text
			}
			//else if (Language.equals("E"))
			//{
			//	DP.DrawText(new Point(WindowPos.x + (int)(0.95*L), WindowPos.y + (int)(0.895*H)), "TopRight", TextAngle, AllText[CollectCat][3] + " " + AllText[AttCat][17] + " = " + UtilG.Round(Collect[0], 1), font, DrawFunctions.MapsTypeColor[13]) ;		
			//	DP.DrawText(new Point(WindowPos.x + (int)(0.95*L), WindowPos.y + (int)(0.945*H)), "TopRight", TextAngle, AllText[CollectCat][4] + " " + AllText[AttCat][17] + " = " + UtilG.Round(Collect[1], 1), font, DrawFunctions.MapsTypeColor[14]) ;		
			//	DP.DrawText(new Point(WindowPos.x + (int)(0.95*L), WindowPos.y + (int)(0.975*H)), "TopRight", TextAngle, AllText[CollectCat][5] + " " + AllText[AttCat][17] + " = " + UtilG.Round(Collect[2], 1), font, DrawFunctions.MapsTypeColor[15]) ;
			//}
			
		
			//	Plus sign
			if (0 < AttPoints)
			{
				for (int i = 0 ; i <= addAttIcon.length - 1 ; i += 1)
				{
					addAttIcon[i].DrawImage(TextAngle, 0, MousePos, DP) ;
				}
			}
		}
		else if (tab == 1)
		{
			String[] specialAttrPropText = allText.get("* Propriedades dos atributos especiais *") ;
			int L = attWindowSize.x ;
			float sx = (float)0.15*L, sy = (float)(1.8*UtilG.TextH(font.getSize())) ;
			Color[] AttributeColor = new Color[] {ColorPalette[5], ColorPalette[5], ColorPalette[6], ColorPalette[3], ColorPalette[9]} ;
			DP.DrawText(new Point(WindowPos.x + (int)(0.65*L), (int)(WindowPos.y + sy)), "Center", TextAngle, specialAttrPropText[1], font, TextColor) ;	
			DP.DrawText(new Point(WindowPos.x + (int)(0.375*L + sx), (int)(WindowPos.y + 2*sy)), "Center", TextAngle, specialAttrPropText[2], font, TextColor) ;	
			DP.DrawText(new Point(WindowPos.x + (int)(0.375*L + 3*sx), (int)(WindowPos.y + 2*sy)), "Center", TextAngle, specialAttrPropText[3], font, TextColor) ;
			DP.DrawText(new Point(WindowPos.x + (int)(0.45*L), (int)(WindowPos.y + 3*sy)), "Center", TextAngle, specialAttrPropText[4], font, TextColor) ;	
			DP.DrawText(new Point(WindowPos.x + (int)(0.45*L + sx), (int)(WindowPos.y + 3*sy)), "Center", TextAngle, specialAttrPropText[5], font, TextColor) ;	
			DP.DrawText(new Point(WindowPos.x + (int)(0.45*L + 2*sx), (int)(WindowPos.y + 3*sy)), "Center", TextAngle, specialAttrPropText[4], font, TextColor) ;	
			DP.DrawText(new Point(WindowPos.x + (int)(0.45*L + 3*sx), (int)(WindowPos.y + 3*sy)), "Center", TextAngle, specialAttrPropText[5], font, TextColor) ;	
			for (int i = 0 ; i <= 4 ; ++i)
			{
				DP.DrawText(new Point(WindowPos.x + (int)(0.2*L), (int)(WindowPos.y + (i + 4)*sy)), "Center", TextAngle, attText[i + 11], font, AttributeColor[i]) ;	
			}
			for (int i = 0 ; i <= 3 ; ++i)
			{
				DP.DrawText(new Point(WindowPos.x + (int)(0.45*L + i*sx), (int)(WindowPos.y + 4*sy)), "Center", TextAngle, String.valueOf(UtilG.Round(user.getBA().getStun()[i], 2)), font, AttributeColor[0]) ;	
				DP.DrawText(new Point(WindowPos.x + (int)(0.45*L + i*sx), (int)(WindowPos.y + 5*sy)), "Center", TextAngle, String.valueOf(UtilG.Round(user.getBA().getBlock()[i], 2)), font, AttributeColor[1]) ;	
				DP.DrawText(new Point(WindowPos.x + (int)(0.45*L + i*sx), (int)(WindowPos.y + 6*sy)), "Center", TextAngle, String.valueOf(UtilG.Round(user.getBA().getBlood()[i], 2)), font, AttributeColor[2]) ;	
				DP.DrawText(new Point(WindowPos.x + (int)(0.45*L + i*sx), (int)(WindowPos.y + 7*sy)), "Center", TextAngle, String.valueOf(UtilG.Round(user.getBA().getPoison()[i], 2)), font, AttributeColor[3]) ;	
				DP.DrawText(new Point(WindowPos.x + (int)(0.45*L + i*sx), (int)(WindowPos.y + 8*sy)), "Center", TextAngle, String.valueOf(UtilG.Round(user.getBA().getSilence()[i], 2)), font, AttributeColor[4]) ;				
			}			
			DP.DrawText(new Point(WindowPos.x + (int)(0.65*L), (int)(WindowPos.y + 10*sy)), "Center", TextAngle, specialAttrPropText[6], font, TextColor) ;	
			DP.DrawText(new Point(WindowPos.x + (int)(0.375*L + sx), (int)(WindowPos.y + 11*sy)), "Center", TextAngle, specialAttrPropText[2], font, TextColor) ;	
			DP.DrawText(new Point(WindowPos.x + (int)(0.375*L + 3*sx), (int)(WindowPos.y + 11*sy)), "Center", TextAngle, specialAttrPropText[3], font, TextColor) ;
			DP.DrawText(new Point(WindowPos.x + (int)(0.45*L), (int)(WindowPos.y + 12*sy)), "Center", TextAngle, specialAttrPropText[4], font, TextColor) ;	
			DP.DrawText(new Point(WindowPos.x + (int)(0.45*L + sx), (int)(WindowPos.y + 12*sy)), "Center", TextAngle, specialAttrPropText[5], font, TextColor) ;	
			DP.DrawText(new Point(WindowPos.x + (int)(0.45*L + 2*sx), (int)(WindowPos.y + 12*sy)), "Center", TextAngle, specialAttrPropText[4], font, TextColor) ;	
			DP.DrawText(new Point(WindowPos.x + (int)(0.45*L + 3*sx), (int)(WindowPos.y + 12*sy)), "Center", TextAngle, specialAttrPropText[5], font, TextColor) ;	
			for (int i = 0 ; i <= 1 ; ++i)
			{
				DP.DrawText(new Point(WindowPos.x + (int)(0.2*L), (int)(WindowPos.y + (i + 13)*sy)), "Center", TextAngle, attText[i + 13], font, AttributeColor[i + 2]) ;	
			}
			for (int i = 0 ; i <= 3 ; ++i)
			{
				DP.DrawText(new Point(WindowPos.x + (int)(0.45*L + i*sx), (int)(WindowPos.y + 13*sy)), "Center", TextAngle, String.valueOf(UtilG.Round(user.getBA().getBlood()[i + 4], 2)), font, AttributeColor[2]) ;	
				DP.DrawText(new Point(WindowPos.x + (int)(0.45*L + i*sx), (int)(WindowPos.y + 14*sy)), "Center", TextAngle, String.valueOf(UtilG.Round(user.getBA().getPoison()[i + 4], 2)), font, AttributeColor[3]) ;	
			}			
			DP.DrawText(new Point(WindowPos.x + (int)(0.025*L), (int)(WindowPos.y + 16*sy)), "BotLeft", TextAngle, attText[11] + " " + specialAttrPropText[7] + " = " + UtilG.Round(user.getBA().getStun()[4], 2), font, AttributeColor[0]) ;	
			DP.DrawText(new Point(WindowPos.x + (int)(0.025*L), (int)(WindowPos.y + 17*sy)), "BotLeft", TextAngle, attText[12] + " " + specialAttrPropText[7] + " = " + UtilG.Round(user.getBA().getBlock()[4], 2), font, AttributeColor[1]) ;	
			DP.DrawText(new Point(WindowPos.x + (int)(0.025*L), (int)(WindowPos.y + 18*sy)), "BotLeft", TextAngle, attText[13] + " " + specialAttrPropText[7] + " = " + UtilG.Round(user.getBA().getBlood()[8], 2), font, AttributeColor[2]) ;	
			DP.DrawText(new Point(WindowPos.x + (int)(0.025*L), (int)(WindowPos.y + 19*sy)), "BotLeft", TextAngle, attText[14] + " " + specialAttrPropText[7] + " = " + UtilG.Round(user.getBA().getPoison()[8], 2), font, AttributeColor[3]) ;	
			DP.DrawText(new Point(WindowPos.x + (int)(0.025*L), (int)(WindowPos.y + 20*sy)), "BotLeft", TextAngle, attText[15] + " " + specialAttrPropText[7] + " = " + UtilG.Round(user.getBA().getSilence()[4], 2), font, AttributeColor[4]) ;	
		}
		else if (tab == 2)
		{
			String[] statsText = allText.get("* Estatísticas do jogador *") ;
			Size offset = new Size(25, 20) ;
			Point textPos = new Point(WindowPos.x + offset.x, WindowPos.y + offset.y) ;
			if (user instanceof Player)
			{
				Player player = (Player) user ;
				for (int i = 0 ; i <= player.getStats().length - 1 ; i += 1)
				{
					String text = statsText[i + 1] + " " + String.valueOf(UtilG.Round(player.getStats()[i], 1)) ;
					DP.DrawText(textPos, "BotLeft", TextAngle, text, namefont, ColorPalette[5]) ;
					textPos.y += (attWindowSize.y - offset.y) / player.getStats().length ;
				}
			}
		}
	}
}