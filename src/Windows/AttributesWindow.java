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
import Utilities.Size;
import Utilities.UtilG;
import Utilities.UtilS;

public class AttributesWindow extends Window
{
	Icon AddAttIcon ;
	public AttributesWindow()
	{
		super(null, 0, 0, 0, 0) ;
		Image PlusSignImage = new ImageIcon(Game.ImagesPath + "PlusSign.png").getImage() ;
		Image SelectedPlusSignImage = new ImageIcon(Game.ImagesPath + "ShiningPlusSign.png").getImage() ;
		AddAttIcon = new Icon(0, "Plus sign", new Point(0, 0), null, PlusSignImage, SelectedPlusSignImage) ;
	}
	
	public void display(LiveBeing user, Map<String, String[]> allText, int[] Equips, float[][] EquipsBonus, int AttPoints, Point MousePos, PersonalAttributes PA, BattleAttributes BA, DrawPrimitives DP)
	{
		Size screenSize = Game.getScreen().getSize() ;
		Point WindowPos = new Point((int) (0.2 * screenSize.x), (int)(0.3 * screenSize.y)) ;
		float TextAngle = DrawPrimitives.OverallAngle ;
		int L = Player.AttWindowImages[0].getWidth(null), H = Player.AttWindowImages[0].getHeight(null) ;
		Point PlayerImagePos = new Point(WindowPos.x + (int) (0.55 * L), WindowPos.y - (int) (0.85 * H)) ;
		Font Namefont = new Font("GothicE", Font.BOLD, L / 28 + 1) ;
		Font font = new Font("GothicE", Font.BOLD, L / 28 + 2) ;
		Font Equipfont = new Font("GothicE", Font.BOLD, L / 28) ;
		Color[] ColorPalette = Game.ColorPalette ;
		Color[] TabColor = new Color[] {ColorPalette[7], ColorPalette[7], ColorPalette[7]}, TabTextColor = new Color[] {ColorPalette[5], ColorPalette[5], ColorPalette[5]} ;
		Color TextColor = ColorPalette[2] ;
		int TextH = UtilG.TextH(font.getSize()) ;
		String[] classesText = allText.get("* Classes *") ;
		String[] proClassesText = allText.get("* ProClasses *") ;
		String[] attText = allText.get("* Atributos *") ;
		String[] collectText = allText.get("* Coleta *") ;
		String[] equipsText = allText.get("* Equipamentos *") ;
		String[] tabsText = allText.get("* Janela do jogador *") ;
		TabColor[tab] = ColorPalette[19] ;
		TabTextColor[tab] = ColorPalette[3] ;
		
		// Main window
		DP.DrawImage(Player.AttWindowImages[tab], WindowPos, "TopLeft") ;
		DP.DrawText(new Point(WindowPos.x + 5, WindowPos.y + (int)(0.05*H)), "Center", 90, tabsText[1], Namefont, TabTextColor[0]) ;				// Tab 0 text	
		DP.DrawText(new Point(WindowPos.x + 5, WindowPos.y + (int)(0.15*H)), "Center", 90, tabsText[2], Namefont, TabTextColor[1]) ;				// Tab 1 text	
		DP.DrawText(new Point(WindowPos.x + 5, WindowPos.y + (int)(0.25*H)), "Center", 90, tabsText[3], Namefont, TabTextColor[2]) ;				// Tab 2 text	
		if (tab == 0)
		{
			//	Player
			//user.display(PlayerImagePos, new float[] {(float) 1.8, (float) 1.8}, PA.getDir(), false, DP) ;
			user.display() ;
			DP.DrawText(new Point(WindowPos.x + (int)(0.5*L), WindowPos.y + (int)(0.1*H)), "Center", TextAngle, PA.getName(), Namefont, TextColor) ;						// Name text			
			DP.DrawText(new Point(WindowPos.x + (int)(0.5*L), WindowPos.y + (int)(0.03*H)), "Center", TextAngle, attText[1] + ": " + PA.getLevel(), font, ColorPalette[6]) ;	// Level text		
			if(PA.getProJob() == 0)
			{
				DP.DrawText(new Point(WindowPos.x + (int)(0.5*L), WindowPos.y + (int)(0.06*H)), "Center", TextAngle, classesText[PA.getJob() + 1], font, ColorPalette[5]) ;	// Job text			
			}
			else
			{
				DP.DrawText(new Point(WindowPos.x + (int)(0.5*L), WindowPos.y + (int)(0.15*H + TextH/2)), "Center", TextAngle, proClassesText[PA.getProJob() + 2*PA.getJob()], font, ColorPalette[5]) ;	// Pro job text					
			}
			
			//	Equips
			int[] EqRectL = new int[] {51, 51, 51, 8} ;
			int[] EqRectH = new int[] {51, 51, 51, 24} ;
			Point[] EqRectPos = new Point[] {new Point(WindowPos.x + 62, WindowPos.y + 78),
					new Point(WindowPos.x + 200, WindowPos.y + 43),
					new Point(WindowPos.x + 200, WindowPos.y + 96), 
					new Point(WindowPos.x + 53, WindowPos.y + 90)} ;	// Weapon, armor, shield, arrow
			for (int eq = 0 ; eq <= 4 - 1 ; eq += 1)
			{
				Image ElemImage = DrawFunctions.ElementImages[UtilS.ElementID(PA.getElem()[eq + 1])] ;
				if (0 < Equips[eq])
				{
					if (eq <= 2)
					{
						if (0 < EquipsBonus[Equips[eq] - Items.BagIDs[6]][0])
						{
							DP.DrawText(new Point(EqRectPos[eq].x, EqRectPos[eq].y - EqRectH[eq] / 2 - TextH), "Center", TextAngle, equipsText[eq + 1] + " + " + (int)(EquipsBonus[Equips[eq] - Items.BagIDs[6]][1]), font, TextColor) ;					
						}
						//DF.DrawEquips(EqRectPos[eq], PA.getJob(), eq, Equips[eq] - Items.BagIDs[6], EquipsBonus, new float[] {1, 1}, TextAngle) ;
					}
					else if (eq == 3)
					{
						//DF.DrawEquips(EqRectPos[eq], PA.getJob(), eq, Equips[0] - Items.BagIDs[6], EquipsBonus, new float[] {1, 1}, TextAngle) ;
					}
					DP.DrawImage(ElemImage, new Point((int) (EqRectPos[eq].x + 0.3*EqRectL[eq]), (int) (EqRectPos[eq].y + 0.3*EqRectH[eq])), TextAngle, new float[] {(float) 0.12, (float) 0.12}, new boolean[] {false, false}, "Center", 1) ;					
					//DP.DrawTextUntil(new Point(EqRectPos[eq].x - EqRectL[eq] / 2, EqRectPos[eq].y + EqRectH[eq] / 2 + TextH), "BotLeft", TextAngle, items[Equips[eq]].getName(), Equipfont, TextColor, 14, MousePos) ;	// Equip text	
				}
				else
				{
					DP.DrawText(new Point(EqRectPos[eq].x + EqRectL[eq]/2, EqRectPos[eq].y - EqRectH[eq] / 2 - TextH), "Center", TextAngle, equipsText[eq + 1], font, TextColor) ;
				}
			}
			
			// Super element
			if (PA.getElem()[1].equals(PA.getElem()[2]) & PA.getElem()[2].equals(PA.getElem()[3]))
			{
				DP.DrawImage(DrawFunctions.ElementImages[UtilS.ElementID(PA.getElem()[4])], new Point(WindowPos.x + (int)(0.5*L), WindowPos.y + (int)(0.45*H)), TextAngle, new float[] {(float) 0.3, (float) 0.3}, new boolean[] {false, false}, "Center", 1) ;
			}
			
			//	Attributes
			float[] Attributes = new float[] {BA.TotalPhyAtk(), BA.TotalMagAtk(), BA.TotalPhyDef(), BA.TotalMagDef(), BA.TotalDex(), BA.TotalAgi()} ;
			int AttSy = 22 ;
			DP.DrawText(new Point(WindowPos.x + 15, WindowPos.y + 30), "BotLeft", TextAngle, attText[2] + ": " + UtilG.Round(PA.getLife()[0], 1), font, ColorPalette[6]) ;	// Life text	
			DP.DrawText(new Point(WindowPos.x + 15, WindowPos.y + 40), "BotLeft", TextAngle, attText[3] + ": " + UtilG.Round(PA.getMp()[0], 1), font, ColorPalette[5]) ;	// MP text

			DP.DrawImage(Equip.SwordImage, new Point(WindowPos.x + 30, WindowPos.y + 134 + 1 * AttSy), new float[] {(float) (11 / 38.0), (float) (11 / 38.0)}, "Center") ;	// Draw sword icon
			for (int i = 0; i <= Attributes.length - 1; i += 1)
			{
				DP.DrawText(new Point(WindowPos.x + 45, WindowPos.y + 136 + (i + 1) * AttSy), "BotLeft", TextAngle, attText[4] + ": " + UtilG.Round(Attributes[i], 1), font, TextColor) ;
			}	
			DP.DrawText(new Point(WindowPos.x + 45, WindowPos.y + 136 + 7 * AttSy), "BotLeft", TextAngle, attText[10] + ": " + UtilG.Round(100 * BA.TotalCritAtkChance(), 1) + "%", font, ColorPalette[6]) ;		
			
			//	Collection
			//if (Language.equals("P"))
			//{	
				//DP.DrawText(new Point(WindowPos.x + (int)(0.95*L), WindowPos.y + (int)(0.895*H)), "TopRight", TextAngle, AllText[AttCat][17] + " de " + AllText[CollectCat][3] + " = " + Utg.Round(Collect[0], 1), font, DrawFunctions.MapsTypeColor[13]) ;		
				//DP.DrawText(new Point(WindowPos.x + (int)(0.95*L), WindowPos.y + (int)(0.945*H)), "TopRight", TextAngle, AllText[AttCat][17] + " de " + AllText[CollectCat][4] + " = " + Utg.Round(Collect[1], 1), font, DrawFunctions.MapsTypeColor[14]) ;		
				//DP.DrawText(new Point(WindowPos.x + (int)(0.95*L), WindowPos.y + (int)(0.975*H)), "TopRight", TextAngle, AllText[AttCat][17] + " de " + AllText[CollectCat][5] + " = " + Utg.Round(Collect[2], 1), font, DrawFunctions.MapsTypeColor[15]) ;
			//}
			//else if (Language.equals("E"))
			//{
			//	DP.DrawText(new Point(WindowPos.x + (int)(0.95*L), WindowPos.y + (int)(0.895*H)), "TopRight", TextAngle, AllText[CollectCat][3] + " " + AllText[AttCat][17] + " = " + Utg.Round(Collect[0], 1), font, DrawFunctions.MapsTypeColor[13]) ;		
			//	DP.DrawText(new Point(WindowPos.x + (int)(0.95*L), WindowPos.y + (int)(0.945*H)), "TopRight", TextAngle, AllText[CollectCat][4] + " " + AllText[AttCat][17] + " = " + Utg.Round(Collect[1], 1), font, DrawFunctions.MapsTypeColor[14]) ;		
			//	DP.DrawText(new Point(WindowPos.x + (int)(0.95*L), WindowPos.y + (int)(0.975*H)), "TopRight", TextAngle, AllText[CollectCat][5] + " " + AllText[AttCat][17] + " = " + Utg.Round(Collect[2], 1), font, DrawFunctions.MapsTypeColor[15]) ;
			//}
			
			//	Gold
			//DP.DrawImage(GoldCoinImage, new Point(WindowPos.x + (int)(0.04*L), WindowPos.y + (int)(0.975*H)), TextAngle, new float[] {(float) 1.2, (float) 1.2}, new boolean[] {false, false}, "BotLeft", 1) ;
			//DP.DrawText(new Point(WindowPos.x + (int)(0.18*L), WindowPos.y + (int)(0.96*H) - TextH/2), "BotLeft", TextAngle, String.valueOf(Utg.Round(Gold[0], 1)), font, ColorPalette[18]) ;	// Gold text
		
			//	Plus sign
			if (0 < AttPoints)
			{
				for (int i = 9 ; i <= 17 - 1 ; i += 1)
				{
					AddAttIcon.DrawImage(TextAngle, 0, MousePos, DP) ;
				}
			}
		}
		else if (tab == 1)
		{
			//DF.DrawSpecialAttributesWindow(AllText, AllTextCat, WindowPos, L, H, DP) ;
		}
		else if (tab == 2)
		{
			//DF.DrawStats(AllText, AllTextCat, WindowPos, L, H, Stats, DP) ;
		}
	}
}
