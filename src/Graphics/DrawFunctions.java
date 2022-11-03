package Graphics ;
import java.awt.Color ;
import java.awt.Font ;
import java.awt.Graphics ;
import java.awt.Graphics2D ;
import java.awt.GraphicsEnvironment;
import java.awt.Image ;
import java.awt.Point;
import java.util.Arrays ;
import java.util.Map;

import javax.swing.ImageIcon ;

import GameComponents.Buildings ;
import GameComponents.Icon ;
import GameComponents.Items ;
import GameComponents.Maps ;
import GameComponents.NPCs ;
import GameComponents.Quests ;
import Items.Item;
import LiveBeings.CreatureTypes;
import LiveBeings.Creatures;
import LiveBeings.Pet;
import LiveBeings.Player;
import LiveBeings.Spells;
import Screen.Screen;
import Screen.Sky;
import Screen.SkyComponent;
import Utilities.Size;
import Utilities.UtilG;
import Utilities.UtilS;
import Windows.Bag;
import Windows.Settings;
import Main.Game;

public class DrawFunctions
{
	private Size screenSize ;
	private Color[] ColorPalette ;
	private DrawPrimitives DP ;
	private Graphics2D G ;

	private float OverallAngle ;
	private Color[] MenuColor ;		// Colors for menus
	private Image MenuWindow1 ;
	private Image ArrowIconImage ;
	
	public static Image[] ElementImages ;
	public static Color[] MapsTypeColor ;	// 0: free, 1: wall, 2: water, 3: tree, 4: grass, 5: rock, 6: crystal, 7: stalactite, 8: volcano, 9: lava, 10: ice, 11: chest, 12: berry, 13: herb, 14: wood, 15: metal, 16: invisible wall
	public static Color[] ContinentsColor ;	// 0: Forest, 1: Cave, 2: Island, 3: Volcano, 4: Snowland, 5: Special
	public static Color[] ElementColor ; 	// 0: neutral, 1: water, 2: fire, 3: plant, 4: earth, 5: air, 6: thunder, 7: light, 8: dark, 9: snow
	
	public DrawFunctions(Graphics g)
	{
		this.screenSize = Game.getScreen().getSize() ;
		this.ColorPalette = Game.ColorPalette ;
		G = (Graphics2D)(g) ;
		DP = new DrawPrimitives(ColorPalette, G) ;
	}
	public void InitializeVariables(String ImagesPath)
	{
		OverallAngle = DrawPrimitives.OverallAngle ;
		MenuColor = new Color[] {ColorPalette[20], ColorPalette[0], ColorPalette[20]} ;		
		MenuWindow1 = new ImageIcon(ImagesPath + "MenuWindow1.png").getImage() ;
		ArrowIconImage = new ImageIcon(ImagesPath + "ArrowIcon.png").getImage() ;
		
		ElementColor = new Color[10] ;
		MapsTypeColor = new Color[] {null, ColorPalette[4], ColorPalette[5], ColorPalette[3], ColorPalette[3], ColorPalette[4], ColorPalette[0], ColorPalette[19], ColorPalette[6], ColorPalette[10], ColorPalette[7], ColorPalette[3], ColorPalette[6], ColorPalette[1], ColorPalette[19], ColorPalette[4], null} ;
		ContinentsColor = new Color[] {MapsTypeColor[4], ColorPalette[19], ColorPalette[20], ColorPalette[10], ColorPalette[8], ColorPalette[5]} ;
		ElementColor = new Color[] {ColorPalette[7], ColorPalette[5], ColorPalette[2], ColorPalette[3], ColorPalette[19], ColorPalette[8], ColorPalette[2], ColorPalette[7], ColorPalette[9], ColorPalette[8]} ;
		Image ElemNeutral = new ImageIcon(ImagesPath + "ElementNeutral.png").getImage() ;
		Image ElemWater = new ImageIcon(ImagesPath + "ElementWater.png").getImage() ;
		Image ElemFire = new ImageIcon(ImagesPath + "ElementFire.png").getImage() ;
		Image ElemPlant = new ImageIcon(ImagesPath + "ElementPlant.png").getImage() ;
		Image ElemEarth = new ImageIcon(ImagesPath + "ElementEarth.png").getImage() ;
		Image ElemAir = new ImageIcon(ImagesPath + "ElementAir.png").getImage() ;
		Image ElemThunder = new ImageIcon(ImagesPath + "ElementThunder.png").getImage() ;
		Image ElemLight = new ImageIcon(ImagesPath + "ElementLight.png").getImage() ;
		Image ElemDark = new ImageIcon(ImagesPath + "ElementDark.png").getImage() ;
		Image ElemSnow = new ImageIcon(ImagesPath + "ElementSnow.png").getImage() ;
		ElementImages = new Image[] {ElemNeutral, ElemWater, ElemFire, ElemPlant, ElemEarth, ElemAir, ElemThunder, ElemLight, ElemDark, ElemSnow} ;
	}	
	public float getOverallAngle() {return OverallAngle ;}

	public void paint(Graphics g) 
	{ 
		
	}
	public void refresh()
	{
		
	}	
	public Graphics2D getGraphs()
	{
		return G ;
	}
	public DrawPrimitives getDrawPrimitives()
	{
		return DP ;
	}
	
	
	/* General drawing */
	
	

	public void DrawMenuWindow(Point Pos, Size size, String Title, int type, Color color1, Color color2)
	{
		if (type == 0)
		{
			DP.DrawRoundRect(Pos, "TopLeft", size, 3, color1, color2, true) ;
			if (Title != null)
			{
				Font font = new Font("SansSerif", Font.BOLD, size.x * size.y / 3500) ;
				Point WindowPos = new Point((int) (Pos.x + 0.5 * size.x), (int) (Pos.y - size.y - 0.5*3*UtilG.TextH(font.getSize()))) ;
				Color TextColor = ColorPalette[9] ;
				DP.DrawRoundRect(WindowPos, "Center", new Size((int)(0.6 * size.x), (int)(3*UtilG.TextH(font.getSize()))), 3, color1, color2, true) ;
				DP.DrawText(WindowPos, "Center", OverallAngle, Title, font, TextColor) ;
			}
		}
		if (type == 1)
		{
			int ImageW = MenuWindow1.getWidth(null), ImageH = MenuWindow1.getHeight(null) ;
			DP.DrawImage(MenuWindow1, Pos, new float[] {(float) size.x / ImageW, (float) size.y / ImageH}, "TopLeft") ;
		}
	}
	/*public void DrawWindowArray(int[] NumberOfWindows, Point InitialPos, String Alignment, Size size, float sx, float sy, int thickness, Color[] colors, int maxwindows)
	{
		for (int nx = 0 ; nx <= NumberOfWindows[0] - 1 ; nx += 1)
		{
			for (int ny = 0 ; ny <= NumberOfWindows[1] - 1 ; ny += 1)
			{
				if (nx * NumberOfWindows[0] + ny < maxwindows)
				{
					Point WindowPos = new Point((int) (InitialPos.x + nx * (size.x + sx)), (int) (InitialPos.y + ny * (size.y + sy))) ;
					DP.DrawRoundRect(WindowPos, Alignment, size, thickness, colors[0], colors[1], true) ;
				}
			}
		}
	}*/
	public void DrawChoicesWindow(Point NPCPos, Font font, int selChoice, String[] Choices, Image NPCimage, Color color)
	{
		Point Pos = new Point((int) (NPCPos.x - NPCimage.getWidth(null) - 10), NPCPos.y) ;
		float Lmax = 0 ;
		for (int i = 0 ; i <= Choices.length - 1 ; i += 1)
		{
			Lmax = Math.max(Lmax, Choices[i].length()) ;
		}
		int Sy = 2 * UtilG.TextH(font.getSize()) ;
		Size size = new Size((int)(Lmax * 0.012 * screenSize.x + 0.01 * screenSize.x), 10 + Choices.length * Sy) ;
		
		DrawMenuWindow(Pos, size, null, 0, ColorPalette[7], ColorPalette[7]) ;
		for (int i = 0 ; i <= Choices.length - 1 ; i += 1)
		{
			if (i == selChoice)
			{
				DP.DrawText(new Point(Pos.x + 5, Pos.y + 5 + i * Sy), "TopLeft", 0, Choices[i], font, ColorPalette[6]) ;
			}
			else
			{
				DP.DrawText(new Point(Pos.x + 5, Pos.y + 5 + i * Sy), "TopLeft", 0, Choices[i], font, color) ;	
			}
		}
	}
	public void DrawSpeech(Point Pos, String text, Font font, Image NPCimage, Image SpeakingBubble, Color color)
	{
		int ImageL = SpeakingBubble.getWidth(null), ImageH = SpeakingBubble.getHeight(null) ;
		Pos = new Point (Pos.x, Pos.y - NPCimage.getHeight(null)) ;
		int MaxTextL = 20 ;
		if (0.7 * screenSize.x < Pos.x)
		{
			DP.DrawImage(SpeakingBubble, new Point(Pos.x + ImageL, Pos.y), OverallAngle, new float[] {1, 1}, new boolean[] {true, false}, "BotCenter", 1) ;
		}
		else
		{
			DP.DrawImage(SpeakingBubble, Pos, OverallAngle, new float[] {1, 1}, new boolean[] {false, false}, "BotCenter", 1) ;
		}
		DP.DrawFitText(new Point((int) (Pos.x - ImageL / 2 + 14), (int) (Pos.y - ImageH + 5)), UtilG.TextH(font.getSize() + 1), "TopLeft", text, font, MaxTextL, color) ;		
	}
	public void DrawWindowArrows(Point Pos, int L, float angle, int SelectedWindow, int MaxWindow)
	{
		Font font = new Font("SansSerif", Font.BOLD, 11) ;
		int TextL = UtilG.TextL(Player.ActionKeys[1], font, G) ;
		int ImageH = ArrowIconImage.getHeight(null) ;
		Point RightArrowPos = new Point(Pos.x + (int)(0.9 * L), Pos.y + ImageH / 2 + 3) ;
		Point LeftArrowPos = new Point(Pos.x + (int)(0.1 * L), Pos.y + ImageH / 2 + 3) ;
		if (0 < SelectedWindow)
		{
			DP.DrawImage(ArrowIconImage, LeftArrowPos, OverallAngle + angle, new float[] {-1, -1}, new boolean[] {false, false}, "Center", 1) ;
			DP.DrawText(new Point(LeftArrowPos.x + 2 * TextL, LeftArrowPos.y), "TopRight", OverallAngle + angle, Player.ActionKeys[1], font, ColorPalette[5]) ;			
		}
		if (SelectedWindow < MaxWindow)
		{
			DP.DrawImage(ArrowIconImage, RightArrowPos, OverallAngle + angle, new float[] {1, -1}, new boolean[] {false, false}, "Center", 1) ;
			DP.DrawText(new Point(RightArrowPos.x - TextL, RightArrowPos.y), "TopRight", OverallAngle + angle, Player.ActionKeys[3], font, ColorPalette[5]) ;		
		}
	}
	public void DrawOrganogram(int[] Sequence, Point Pos, int sx, int sy, Size size, String[][] Text1, String[] Text2, Icon SlotIcon, Font font, Color[] TextColor, Point MousePos)
	{
		int[] x0 = new int[] {screenSize.x / 30 + size.x + sx, screenSize.x / 30 + size.x / 2 + sx / 2, screenSize.x / 30} ;
		int IconH = SlotIcon.getImage().getHeight(null) ;
		int RectH = (int) (0.67 * IconH) ;
		int id = 0 ;
		for (int row = 0 ; row <= Sequence.length - 1 ; row += 1)
		{
			for (int col = 0 ; col <= Sequence[row] - 1 ; col += 1)
			{
				Point slotCenter = new Point(Pos.x + x0[Sequence[row] - 1] + (size.x + sx) * col + size.x / 2, Pos.y + size.y / 2 + sy / 2 + (size.y + sy) * row) ;
				SlotIcon.setPos(slotCenter) ;
				DP.DrawImage(SlotIcon.getImage(), slotCenter, "Center") ;
				if (UtilG.isInside(MousePos, new Point(slotCenter.x - size.x / 2, slotCenter.y + RectH), size))
				{
					DP.DrawImage(SlotIcon.getSelectedImage(), slotCenter, "Center") ;
				}
				
				int TextH = UtilG.TextH(font.getSize()) ;
				int textsy = RectH - 10 - TextH * (Text1.length - 1) ;
				if (1 < Text1.length)
				{
					textsy = (int) UtilG.spacing(RectH, Sequence[row], TextH, 2) ;
				}
				for (int textrow = 0 ; textrow <= Text1.length - 1 ; textrow += 1)
				{
					DP.DrawTextUntil(new Point(slotCenter.x, slotCenter.y - IconH / 2 + 5 + textrow * textsy), "TopCenter", OverallAngle, Text1[textrow][id], font, TextColor[id], 20, MousePos) ;
				}
				DP.DrawText(new Point(slotCenter.x, slotCenter.y + size.y / 3), "Center", OverallAngle, Text2[id], font, TextColor[id]) ;							
				id += 1 ;
			}
		}
	}
	/*public void DrawShineOnImage(Image image, Point ImagePos, String Alignment, float size, Color color, int alpha)
	{
		int imageW = image.getWidth(null), imageH = image.getHeight(null) ;
		int[] offset = Utg.OffsetFromPos(Alignment, imageW, imageH) ;
		color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha) ;
		Image shine = Utg.ChangeImageColor(image, color) ;
		DP.DrawImage(shine, new Point(ImagePos.x + offset[0] + imageW / 2, ImagePos.y - offset[1] - imageH / 2), new float[] {1 + size, 1 + size}, "Center") ;
	}*/
	
	
	/* Player, pet, and creature windows */
	public void DrawPlayerRange(Player player)
	{
		DP.DrawCircle(player.getPos(), (int)(2*player.getRange()), 2, ColorPalette[player.getJob()], false, true) ;
	}
	public void DrawEquips(Point Pos, int Job, int equiptype, int EquipID, float[][] EquipsBonus, float[] scale, float angle)
	{
		int bonus = 0 ;
		if (EquipsBonus[EquipID][1] == 10)
		{
			bonus = 8 ;
		}
		if (equiptype == 0)	// 0: weapon
		{
			DP.DrawImage(Items.EquipImage[Job + bonus], Pos, angle, scale, new boolean[] {false, false}, "Center", 1) ;
		}
		if (1 <= equiptype)	// 1: shield, 2: armor, 3: arrow
		{
			DP.DrawImage(Items.EquipImage[equiptype + 1 + bonus], Pos, angle, scale, new boolean[] {false, false}, "Center", 1) ;
		}
	}
	public void DrawPlayerWeapon(Player player, Point Pos, float[] playerscale)
	{
		float[] scale = new float[] {(float) 0.6, (float) 0.6} ;
		float[] angle = new float[] {50, 30, 0, 0, 0} ;
		Point EqPos = new Point((int)(Pos.x + 0.16*player.getSize()[0]*playerscale[0]), (int)(Pos.y - 0.4*player.getSize()[1]*playerscale[1])) ;
		if (0 < player.getEquips()[0])
		{
			DrawEquips(EqPos, player.getJob(), 0, player.getEquips()[0] - Items.BagIDs[6], Items.EquipsBonus, scale, angle[player.getJob()]) ;
		}	
	}
	/*public void DrawSpecialAttributesWindow(Player player, Point Pos, Size size, float[] Stun, float[] Block, float[] Blood, float[] Poison, float[] Silence)
	{
		int SpecialAttrPropCat = AllTextCat[8], AttrCat = AllTextCat[6] ;	
		Font font = new Font("SansSerif", Font.BOLD, 13) ;
		int Linewidth = 2 ;
		int L = size.x, H = size.y ;
		float sx = (float)0.15*L, sy = (float)(1.8*Utg.TextH(font.getSize())) ;
		Color TextColor = ColorPalette[5], LineColor = ColorPalette[9] ;
		Color[] AttributeColor = new Color[] {ColorPalette[5], ColorPalette[5], ColorPalette[6], ColorPalette[3], ColorPalette[9]} ;
		DP.DrawRoundRect(Pos, "BotLeft", size, 1, ColorPalette[0], ColorPalette[7], true) ;
		DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.025*L)}, new int[] {(int)(Pos.y - H + 0.5*sy), (int)(Pos.y - H + 8.5*sy)}, Linewidth, LineColor) ;
		DP.DrawLine(new int[] {Pos.x + (int)(0.375*L), Pos.x + (int)(0.375*L)}, new int[] {(int)(Pos.y - H + 0.5*sy), (int)(Pos.y - H + 3.5*sy)}, Linewidth, LineColor) ;
		DP.DrawLine(new int[] {Pos.x + (int)(0.975*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + 0.5*sy), (int)(Pos.y - H + 8.5*sy)}, Linewidth, LineColor) ;
		DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + 0.5*sy), (int)(Pos.y - H + 0.5*sy)}, Linewidth, LineColor) ;
		DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + 8.5*sy), (int)(Pos.y - H + 8.5*sy)}, Linewidth, LineColor) ;
		DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + 1.5*sy), (int)(Pos.y - H + 1.5*sy)}, Linewidth, LineColor) ;
		DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + 2.5*sy), (int)(Pos.y - H + 2.5*sy)}, Linewidth, LineColor) ;
		DP.DrawLine(new int[] {Pos.x + (int)(0.375*L + 2*sx), Pos.x + (int)(0.375*L + 2*sx)}, new int[] {(int)(Pos.y - H + 1.5*sy), (int)(Pos.y - H + 2.5*sy)}, Linewidth, LineColor) ;
		DP.DrawText(new Point(Pos.x + (int)(0.65*L), (int)(Pos.y - H + sy)), "Center", OverallAngle, AllText[SpecialAttrPropCat][1], font, TextColor) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.375*L + sx), (int)(Pos.y - H + 2*sy)), "Center", OverallAngle, AllText[SpecialAttrPropCat][2], font, TextColor) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.375*L + 3*sx), (int)(Pos.y - H + 2*sy)), "Center", OverallAngle, AllText[SpecialAttrPropCat][3], font, TextColor) ;
		DP.DrawText(new Point(Pos.x + (int)(0.45*L), (int)(Pos.y - H + 3*sy)), "Center", OverallAngle, AllText[SpecialAttrPropCat][4], font, TextColor) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.45*L + sx), (int)(Pos.y - H + 3*sy)), "Center", OverallAngle, AllText[SpecialAttrPropCat][5], font, TextColor) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.45*L + 2*sx), (int)(Pos.y - H + 3*sy)), "Center", OverallAngle, AllText[SpecialAttrPropCat][4], font, TextColor) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.45*L + 3*sx), (int)(Pos.y - H + 3*sy)), "Center", OverallAngle, AllText[SpecialAttrPropCat][5], font, TextColor) ;	
		for (int i = 0 ; i <= 4 ; ++i)
		{
			DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + (i + 3.5)*sy), (int)(Pos.y - H + (i + 3.5)*sy)}, Linewidth, LineColor) ;
			DP.DrawText(new Point(Pos.x + (int)(0.2*L), (int)(Pos.y - H + (i + 4)*sy)), "Center", OverallAngle, AllText[AttrCat][i + 11], font, AttributeColor[i]) ;	
		}
		for (int i = 0 ; i <= 3 ; ++i)
		{
			DP.DrawLine(new int[] {Pos.x + (int)(0.375*L + i*sx), Pos.x + (int)(0.375*L + i*sx)}, new int[] {(int)(Pos.y - H + 2*sy + sy/2), (int)(Pos.y - H + 8*sy + sy/2)}, Linewidth, LineColor) ;
			DP.DrawText(new Point(Pos.x + (int)(0.45*L + i*sx), (int)(Pos.y - H + 4*sy)), "Center", OverallAngle, String.valueOf(Utg.Round(Stun[i], 2)), font, AttributeColor[0]) ;	
			DP.DrawText(new Point(Pos.x + (int)(0.45*L + i*sx), (int)(Pos.y - H + 5*sy)), "Center", OverallAngle, String.valueOf(Utg.Round(Block[i], 2)), font, AttributeColor[1]) ;	
			DP.DrawText(new Point(Pos.x + (int)(0.45*L + i*sx), (int)(Pos.y - H + 6*sy)), "Center", OverallAngle, String.valueOf(Utg.Round(Blood[i], 2)), font, AttributeColor[2]) ;	
			DP.DrawText(new Point(Pos.x + (int)(0.45*L + i*sx), (int)(Pos.y - H + 7*sy)), "Center", OverallAngle, String.valueOf(Utg.Round(Poison[i], 2)), font, AttributeColor[3]) ;	
			DP.DrawText(new Point(Pos.x + (int)(0.45*L + i*sx), (int)(Pos.y - H + 8*sy)), "Center", OverallAngle, String.valueOf(Utg.Round(Silence[i], 2)), font, AttributeColor[4]) ;				
		}
		
		DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.025*L)}, new int[] {(int)(Pos.y - H + 9.5*sy), (int)(Pos.y - H + 14.5*sy)}, Linewidth, LineColor) ;
		DP.DrawLine(new int[] {Pos.x + (int)(0.375*L), Pos.x + (int)(0.375*L)}, new int[] {(int)(Pos.y - H + 9.5*sy), (int)(Pos.y - H + 12.5*sy)}, Linewidth, LineColor) ;
		DP.DrawLine(new int[] {Pos.x + (int)(0.975*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + 9.5*sy), (int)(Pos.y - H + 14.5*sy)}, Linewidth, LineColor) ;
		DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + 9.5*sy), (int)(Pos.y - H + 9.5*sy)}, Linewidth, LineColor) ;
		DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + 14.5*sy), (int)(Pos.y - H + 14.5*sy)}, Linewidth, LineColor) ;
		DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + 10.5*sy), (int)(Pos.y - H + 10.5*sy)}, Linewidth, LineColor) ;
		DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + 11.5*sy), (int)(Pos.y - H + 11.5*sy)}, Linewidth, LineColor) ;
		DP.DrawLine(new int[] {Pos.x + (int)(0.375*L + 2*sx), Pos.x + (int)(0.375*L + 2*sx)}, new int[] {(int)(Pos.y - H + 10.5*sy), (int)(Pos.y - H + 12.5*sy)}, Linewidth, LineColor) ;
		DP.DrawText(new Point(Pos.x + (int)(0.65*L), (int)(Pos.y - H + 10*sy)), "Center", OverallAngle, AllText[SpecialAttrPropCat][6], font, TextColor) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.375*L + sx), (int)(Pos.y - H + 11*sy)), "Center", OverallAngle, AllText[SpecialAttrPropCat][2], font, TextColor) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.375*L + 3*sx), (int)(Pos.y - H + 11*sy)), "Center", OverallAngle, AllText[SpecialAttrPropCat][3], font, TextColor) ;
		DP.DrawText(new Point(Pos.x + (int)(0.45*L), (int)(Pos.y - H + 12*sy)), "Center", OverallAngle, AllText[SpecialAttrPropCat][4], font, TextColor) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.45*L + sx), (int)(Pos.y - H + 12*sy)), "Center", OverallAngle, AllText[SpecialAttrPropCat][5], font, TextColor) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.45*L + 2*sx), (int)(Pos.y - H + 12*sy)), "Center", OverallAngle, AllText[SpecialAttrPropCat][4], font, TextColor) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.45*L + 3*sx), (int)(Pos.y - H + 12*sy)), "Center", OverallAngle, AllText[SpecialAttrPropCat][5], font, TextColor) ;	
		for (int i = 0 ; i <= 1 ; ++i)
		{
			DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + (i + 12.5)*sy), (int)(Pos.y - H + (i + 12.5)*sy)}, Linewidth, LineColor) ;
			DP.DrawText(new Point(Pos.x + (int)(0.2*L), (int)(Pos.y - H + (i + 13)*sy)), "Center", OverallAngle, AllText[AttrCat][i + 13], font, AttributeColor[i + 2]) ;	
		}
		for (int i = 0 ; i <= 3 ; ++i)
		{
			DP.DrawLine(new int[] {Pos.x + (int)(0.375*L + i*sx), Pos.x + (int)(0.375*L + i*sx)}, new int[] {(int)(Pos.y - H + 11.5*sy), (int)(Pos.y - H + 14.5*sy)}, Linewidth, LineColor) ;
			DP.DrawText(new Point(Pos.x + (int)(0.45*L + i*sx), (int)(Pos.y - H + 13*sy)), "Center", OverallAngle, String.valueOf(Utg.Round(Blood[i + 4], 2)), font, AttributeColor[2]) ;	
			DP.DrawText(new Point(Pos.x + (int)(0.45*L + i*sx), (int)(Pos.y - H + 14*sy)), "Center", OverallAngle, String.valueOf(Utg.Round(Poison[i + 4], 2)), font, AttributeColor[3]) ;	
		}
		
		DP.DrawText(new Point(Pos.x + (int)(0.025*L), (int)(Pos.y - H + 16*sy)), "BotLeft", OverallAngle, AllText[AttrCat][11] + " " + AllText[SpecialAttrPropCat][7] + " = " + Utg.Round(Stun[4], 2), font, AttributeColor[0]) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.025*L), (int)(Pos.y - H + 17*sy)), "BotLeft", OverallAngle, AllText[AttrCat][12] + " " + AllText[SpecialAttrPropCat][7] + " = " + Utg.Round(Block[4], 2), font, AttributeColor[1]) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.025*L), (int)(Pos.y - H + 18*sy)), "BotLeft", OverallAngle, AllText[AttrCat][13] + " " + AllText[SpecialAttrPropCat][7] + " = " + Utg.Round(Blood[8], 2), font, AttributeColor[2]) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.025*L), (int)(Pos.y - H + 19*sy)), "BotLeft", OverallAngle, AllText[AttrCat][14] + " " + AllText[SpecialAttrPropCat][7] + " = " + Utg.Round(Poison[8], 2), font, AttributeColor[3]) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.025*L), (int)(Pos.y - H + 20*sy)), "BotLeft", OverallAngle, AllText[AttrCat][15] + " " + AllText[SpecialAttrPropCat][7] + " = " + Utg.Round(Silence[4], 2), font, AttributeColor[4]) ;				
	}*/
	/*public void DrawPlayerStats(Player player, Point Pos, Size size)
	{
		int TextCat = AllTextCat[7] ;
		Font font = new Font("SansSerif", Font.BOLD, 13) ;
		Point TextPos = new Point((int) (Pos.x + 0.05 * size.x), (int) (Pos.y - size.y + 0.05 * size.y)) ;
		DP.DrawRoundRect(Pos, "BotLeft", size, 1, ColorPalette[0], ColorPalette[7], true) ;
		for (int i = 0 ; i <= player.getStats().length - 1 ; i += 1)
		{
			DP.DrawText(TextPos, "BotLeft", OverallAngle, AllText[TextCat][i + 1] + " " + String.valueOf(Utg.Round(player.getStats()[i], 1)), font, ColorPalette[5]) ;
			TextPos.y += 0.95 * size.y / player.getStats().length ;
		}
	}*/
	public void DrawPlayerAttributes(Player player, int design)
	{
		float[] attRate = new float[] {player.getLife()[0] / player.getLife()[1], player.getMp()[0] / player.getMp()[1], player.getExp()[0] / player.getExp()[1], player.getSatiation()[0] / player.getSatiation()[1], player.getThirst()[0] / player.getThirst()[1]} ;
		Color attColor[] = new Color[] {ColorPalette[6], ColorPalette[5], ColorPalette[1], ColorPalette[2], ColorPalette[0]} ;
		
		if (design == 0)
		{
			Point Pos = new Point((int)(player.getPos().x - player.getSize()[0]/2), (int)(player.getPos().y - player.getSize()[1] - 10)) ;
			Size size = new Size((int)(0.05*screenSize.x), (int)(0.01*screenSize.y)) ;
			int Sy = (int)(0.01*screenSize.y) ;
			int barthick = 1 ;
			for (int att = 0; att <= attRate.length - 1; att += 1)
			{
				DP.DrawRect(new Point(Pos.x, Pos.y + (att + 1) * Sy), "TopLeft", new Size((int)(attRate[0] * size.x), size.y), barthick, attColor[att], ColorPalette[9], true) ;
			}
		}
		if (design == 1)
		{
			Point Pos = new Point((int)(0.01*screenSize.x), (int)(0.03*screenSize.y)) ;
			Size size = new Size((int)(0.13*screenSize.x), (int)(0.013*screenSize.y)) ;
			int Sy = size.y ;
			int barthick = 1 ;
			DP.DrawRoundRect(Pos, "TopLeft", new Size((int)(1.4 * size.x), (attRate.length + 1) * Sy), barthick, ColorPalette[8], ColorPalette[4], true) ;
			for (int att = 0; att <= attRate.length - 1; att += 1)
			{
				DP.DrawRect(new Point((int) (Pos.x + 0.3 * size.x), Pos.y + (att + 1) * Sy), "CenterLeft", size, barthick, null, ColorPalette[9], true) ;
				DP.DrawRect(new Point((int) (Pos.x + 0.3 * size.x), Pos.y + (att + 1) * Sy), "CenterLeft", new Size((int)(attRate[att] * size.x), size.y), barthick, attColor[att], ColorPalette[9], true) ;
			}
		}
	}	
	public void DrawPetAttributes(Pet pet)
	{
		Color color[] = new Color[] {ColorPalette[6], ColorPalette[5], ColorPalette[1], ColorPalette[2]} ;
		Point Pos = new Point((int)(pet.getPos().x - pet.getSize()[0]/2), (int)(pet.getPos().y - 0.6*pet.getSize()[1])) ;
		int L = (int)(0.025*screenSize.x), H = (int)(0.005*screenSize.y), Sy = H ;
		int RectThickness = 1 ;
		DP.DrawRect(new Point(Pos.x, Pos.y), "BotLeft", new Size(L, 3 * H), RectThickness, null, ColorPalette[9], true) ;
		DP.DrawRect(new Point(Pos.x, Pos.y - 3*Sy), "BotLeft", new Size((int)(L*pet.getLife()[0]/pet.getLife()[1]), H), RectThickness, color[0], ColorPalette[9], true) ;
		DP.DrawRect(new Point(Pos.x, Pos.y - 2*Sy), "BotLeft", new Size((int)(L*pet.getMp()[0]/pet.getMp()[1]), H), RectThickness, color[1], ColorPalette[9], true) ;
		DP.DrawRect(new Point(Pos.x, Pos.y - Sy), "BotLeft", new Size((int)(L*pet.getExp()[0]/pet.getExp()[1]), H), RectThickness, color[2], ColorPalette[9], true) ;
		DP.DrawRect(new Point(Pos.x, Pos.y), "BotLeft", new Size((int)(L*pet.getSatiation()[0]/pet.getSatiation()[1]), H), RectThickness, color[3], ColorPalette[9], true) ;
	}
	/*public void DrawCreatureAttributes(Creatures creature)
	{
		Color color[] = new Color[] {ColorPalette[6], ColorPalette[5]} ;
		Point Pos = new Point((int)(creature.getPos().x - creature.getSize()[0]/2), (int)(creature.getPos().y - creature.getSize()[1])) ;
		int L = (int)(0.05*screenSize.x), H = (int)(0.01*screenSize.y), Sy = (int)(0.01*screenSize.y) ;
		int RectThickness = 1 ;
		DP.DrawRect(new Point(Pos.x, Pos.y), "BotLeft", new Size(L, 2*H), RectThickness, null, ColorPalette[9], true) ;
		DP.DrawRect(new Point(Pos.x, Pos.y - Sy), "BotLeft", new Size((int)(L*creature.getLife()[0]/creature.getLife()[1]), H), RectThickness, color[0], ColorPalette[9], true) ;
		DP.DrawRect(new Point(Pos.x, Pos.y), "BotLeft", new Size((int)(L*creature.getMp()[0]/creature.getMp()[1]), H), RectThickness, color[1], ColorPalette[9], true) ;
	}*/	
	/*public void DrawCreatureTypeWindow(CreatureTypes creature)
	{
		Font Namefont = new Font("SansSerif", Font.BOLD, 14) ;
		Font font = new Font("SansSerif", Font.BOLD, 13) ;
		int TextCat = AllTextCat[6] ;
		Point Pos = new Point((int)(0.1*screenSize.x), (int)(0.9*screenSize.y)) ;
		int W = (int)(0.4*screenSize.x), H = (int)(0.6*screenSize.y) ;
		DrawMenuWindow(Pos, new Size(W, H), "Creature type window", 0, ColorPalette[7], ColorPalette[9]) ;
		DrawCreature(new Point(Pos.x + (int)(0.5*W), Pos.y - (int)(0.7*H)), new int[] {30, 30}, creature.getimage(), creature.getColor()) ;
		DP.DrawText(new Point(Pos.x + (int)(0.4*W), Pos.y - (int)(0.95*H)), "BotLeft", OverallAngle, creature.getName(), Namefont, creature.getColor()) ;				
		DP.DrawText(new Point(Pos.x + (int)(0.43*W), Pos.y - (int)(0.90*H)), "BotLeft", OverallAngle, AllText[TextCat][1] + ": " + creature.getLevel(), font, ColorPalette[6]) ;		
		DP.DrawText(new Point(Pos.x + (int)(0.025*W), Pos.y - (int)(0.9*H)), "BotLeft", OverallAngle, AllText[TextCat][2] + ": " + Utg.Round(creature.getLife()[0], 1), font, ColorPalette[6]) ;		
		DP.DrawText(new Point(Pos.x + (int)(0.025*W), Pos.y - (int)(0.85*H)), "BotLeft", OverallAngle, AllText[TextCat][3] + ": " + Utg.Round(creature.getMp()[0], 1), font, ColorPalette[5]) ;		
		DP.DrawText(new Point(Pos.x + (int)(0.025*W), Pos.y - (int)(0.4*H)), "BotLeft", OverallAngle, AllText[TextCat][4] + ": " + Utg.Round(creature.getPhyAtk()[0], 1) + " + " + Utg.Round(creature.getPhyAtk()[1], 1), font, creature.getColor()) ;		
		DP.DrawText(new Point(Pos.x + (int)(0.025*W), Pos.y - (int)(0.35*H)), "BotLeft", OverallAngle, AllText[TextCat][5] + ": " + Utg.Round(creature.getMagAtk()[0], 1) + " + " + Utg.Round(creature.getMagAtk()[1], 1), font, creature.getColor()) ;		
		DP.DrawText(new Point(Pos.x + (int)(0.025*W), Pos.y - (int)(0.3*H)), "BotLeft", OverallAngle, AllText[TextCat][6] + ": " + Utg.Round(creature.getPhyDef()[0], 1) + " + " + Utg.Round(creature.getPhyDef()[1], 1), font, creature.getColor()) ;		
		DP.DrawText(new Point(Pos.x + (int)(0.025*W), Pos.y - (int)(0.25*H)), "BotLeft", OverallAngle, AllText[TextCat][7] + ": " + Utg.Round(creature.getMagDef()[0], 1) + " + " + Utg.Round(creature.getMagDef()[1], 1), font, creature.getColor()) ;		
		DP.DrawText(new Point(Pos.x + (int)(0.025*W), Pos.y - (int)(0.2*H)), "BotLeft", OverallAngle, AllText[TextCat][8] + ": " + Utg.Round(creature.getDex()[0], 1) + " + " + Utg.Round(creature.getDex()[1], 1), font, creature.getColor()) ;		
		DP.DrawText(new Point(Pos.x + (int)(0.025*W), Pos.y - (int)(0.15*H)), "BotLeft", OverallAngle, AllText[TextCat][9] + ": " + Utg.Round(creature.getAgi()[0], 1) + " + " + Utg.Round(creature.getAgi()[1], 1), font, creature.getColor()) ;		
		DP.DrawText(new Point(Pos.x + (int)(0.025*W), Pos.y - (int)(0.1*H)), "BotLeft", OverallAngle, AllText[TextCat][10] + ": " + Utg.Round(creature.getCrit()[0], 1) + " + " + Utg.Round(creature.getCrit()[1], 1), font, ColorPalette[6]) ;		
		DP.DrawText(new Point(Pos.x + (int)(0.05*W), Pos.y - (int)(0.6*H)), "BotLeft", OverallAngle, AllText[TextCat][11] + ": " + creature.getElem()[0], font, creature.getColor()) ;	
	}*/
	/*public void DrawCreatureWindow(Creatures creature)
	{
		Font Namefont = new Font("SansSerif", Font.BOLD, 14) ;
		Font font = new Font("SansSerif", Font.BOLD, 13) ;
		int TextCat = AllTextCat[6] ;
		Point Pos = new Point((int)(0.2*screenSize.x), (int)(0.6*screenSize.y)) ;
		int W = (int)(0.3*screenSize.x), H = (int)(0.3*screenSize.y) ;
		float sy = (float) (0.07*H) ;
		DrawMenuWindow(Pos, new Size(W, H), "Creature window", 0, ColorPalette[7], ColorPalette[4]) ;
		DrawCreature(new Point(Pos.x + (int)(0.5*W), Pos.y - (int)(0.65*H)), new int[] {42, 30}, creature.getimage(), creature.getColor()) ;
		DP.DrawText(new Point(Pos.x + (int)(0.4*W), Pos.y - (int)(0.92*H)), "BotLeft", OverallAngle, creature.getName(), Namefont, creature.getColor()) ;				
		DP.DrawText(new Point(Pos.x + (int)(0.43*W), Pos.y - (int)(0.85*H)), "BotLeft", OverallAngle, AllText[TextCat][1] + ": " + creature.getLevel(), font, ColorPalette[6]) ;		
		DP.DrawText(new Point(Pos.x + (int)(0.025*W), Pos.y - (int)(0.9*H)), "BotLeft", OverallAngle, AllText[TextCat][2] + ": " + Utg.Round(creature.getLife()[0], 1), font, ColorPalette[6]) ;		
		DP.DrawText(new Point(Pos.x + (int)(0.025*W), Pos.y - (int)(0.9*H - sy)), "BotLeft", OverallAngle, AllText[TextCat][3] + ": " + Utg.Round(creature.getMp()[0], 1), font, ColorPalette[5]) ;		
		DP.DrawText(new Point(Pos.x + (int)(0.025*W), Pos.y - (int)(0.45*H)), "BotLeft", OverallAngle, AllText[TextCat][4] + ": " + Utg.Round(creature.getPhyAtk()[0], 1) + " + " + Utg.Round(creature.getPhyAtk()[1], 1), font, creature.getColor()) ;		
		DP.DrawText(new Point(Pos.x + (int)(0.025*W), Pos.y - (int)(0.45*H - sy)), "BotLeft", OverallAngle, AllText[TextCat][5] + ": " + Utg.Round(creature.getMagAtk()[0], 1) + " + " + Utg.Round(creature.getMagAtk()[1], 1), font, creature.getColor()) ;		
		DP.DrawText(new Point(Pos.x + (int)(0.025*W), Pos.y - (int)(0.45*H - 2*sy)), "BotLeft", OverallAngle, AllText[TextCat][6] + ": " + Utg.Round(creature.getPhyDef()[0], 1) + " + " + Utg.Round(creature.getPhyDef()[1], 1), font, creature.getColor()) ;		
		DP.DrawText(new Point(Pos.x + (int)(0.025*W), Pos.y - (int)(0.45*H - 3*sy)), "BotLeft", OverallAngle, AllText[TextCat][7] + ": " + Utg.Round(creature.getMagDef()[0], 1) + " + " + Utg.Round(creature.getMagDef()[1], 1), font, creature.getColor()) ;		
		DP.DrawText(new Point(Pos.x + (int)(0.025*W), Pos.y - (int)(0.45*H - 4*sy)), "BotLeft", OverallAngle, AllText[TextCat][8] + ": " + Utg.Round(creature.getDex()[0], 1) + " + " + Utg.Round(creature.getDex()[1], 1), font, creature.getColor()) ;		
		DP.DrawText(new Point(Pos.x + (int)(0.025*W), Pos.y - (int)(0.45*H - 5*sy)), "BotLeft", OverallAngle, AllText[TextCat][9] + ": " + Utg.Round(creature.getAgi()[0], 1) + " + " + Utg.Round(creature.getAgi()[1], 1), font, creature.getColor()) ;		
		DP.DrawText(new Point(Pos.x + (int)(0.025*W), Pos.y - (int)(0.45*H - 6*sy)), "BotLeft", OverallAngle, AllText[TextCat][10] + ": " + Utg.Round(creature.getCrit()[0], 1) + " + " + Utg.Round(creature.getCrit()[1], 1), font, ColorPalette[6]) ;		
		DP.DrawText(new Point(Pos.x + (int)(0.5*W), Pos.y - (int)(0.55*H)), "Center", OverallAngle, AllText[TextCat][16] + ": " + creature.getElem()[0], font, creature.getColor()) ;	
	}*/
	public void DrawPet(Point Pos, float[] Scale, Image PetImage)
	{
		DP.DrawImage(PetImage, Pos, OverallAngle, Scale, new boolean[] {false, false}, "Center", 1) ;
	}
	public void DrawCreature(Point Pos, int[] Scale, Image CreatureImage, Color color)
	{
		DP.DrawImage(CreatureImage, Pos, 0, new float[] {1, 1}, new boolean[] {false, false}, "Center", 1) ;
	}
	/*public void DrawCreatureInfoWindow(Point Pos, CreatureTypes creatureType, Items[] items)
	{
		int TextCat = AllTextCat[1] ;
		Font Namefont = new Font("SansSerif", Font.BOLD, 15) ;
		Font font = new Font("SansSerif", Font.BOLD, 13) ;
		Color TextColor = ColorPalette[9] ;
		int L = (int)(0.28*screenSize.x), H = (int)(0.5*screenSize.y) ;
		int l = (int) (1.5*creatureType.getSize()[0]), h = (int) (1.5*creatureType.getSize()[1]) ;
		int offsetx = 5, offseth = 5 ;
		float sy = (float) (1.0/17*(H - 1.5*h)) ;
		DrawMenuWindow(Pos, new Size(L, H), null, 0, ColorPalette[7], ColorPalette[8]) ;
		DP.DrawRoundRect(new Point((int) (Pos.x + 0.5*L), (int) (Pos.y - H + h)), "Center", new Size(l, h), 3, null, ColorPalette[9], true) ;
		DrawCreature(new Point((int) (Pos.x + 0.5*L), (int) (Pos.y - H + h + 0.1*creatureType.getSize()[1])), new int[] {creatureType.getSize()[0], creatureType.getSize()[1]}, creatureType.getimage(), creatureType.getColor()) ;
		DP.DrawText(new Point(Pos.x + offsetx, (int) (Pos.y - H + 1.5*h + offseth + 1*sy)), "BotLeft", OverallAngle, creatureType.getName(), Namefont, creatureType.getColor()) ;		// Name
		DP.DrawText(new Point(Pos.x + offsetx, (int) (Pos.y - H + 1.5*h + offseth + 2*sy)), "BotLeft", OverallAngle, AllText[TextCat][2] + ": " + creatureType.getLevel(), font, TextColor) ;	// Level
		DP.DrawText(new Point(Pos.x + offsetx, (int) (Pos.y - H + 1.5*h + offseth + 3*sy)), "BotLeft", OverallAngle, AllText[TextCat][3] + ": " + (int)creatureType.getLife()[0], font, TextColor) ;	// Life
		DP.DrawText(new Point(Pos.x + offsetx, (int) (Pos.y - H + 1.5*h + offseth + 4*sy)), "BotLeft", OverallAngle, AllText[TextCat][4] + ": " + creatureType.getExp(), font, TextColor) ;	// Exp
		DP.DrawText(new Point(Pos.x + offsetx, (int) (Pos.y - H + 1.5*h + offseth + 5*sy)), "BotLeft", OverallAngle, AllText[TextCat][5] + ": " + creatureType.getGold(), font, TextColor) ;	// Gold
		DP.DrawText(new Point(Pos.x + offsetx, (int) (Pos.y - H + 1.5*h + offseth + 6*sy)), "BotLeft", OverallAngle, AllText[TextCat][6] + ": ", font, TextColor) ;							// Items
		for (int item = 0 ; item <= creatureType.getBag().length - 1 ; item += 1)
		{
			int itemID = creatureType.getBag()[item] ;
			if (-1 < itemID)
			{
				DP.DrawText(new Point(Pos.x + offsetx, (int) (Pos.y - H + 1.5*h + offseth + (7 + item)*sy)), "BotLeft", OverallAngle, items[itemID].getName(), font, TextColor) ;	// Items
			}
		}
	}*/
	
	
	/* Opening and load */
	public void DrawOpeningScreen(Image OpeningGif)
	{
		DP.DrawGif(OpeningGif, new Point(0, 0), "TopLeft") ;
	}
	public void DrawLoadingText(Image LoadingGif, Point Pos)
	{
		DP.DrawGif(LoadingGif, Pos, "Center");
	}
	public void DrawLoadingGameScreen(Player player, Pet pet, Map<String, String[]> allText, Icon[] icons, int SlotID, int NumberOfUsedSlots, Image GoldCoinImage)
	{
		Point[] WindowPos = new Point[] {new Point((int)(0.15*screenSize.x), (int)(0.2*screenSize.y)),
				new Point((int)(0.65*screenSize.x), (int)(0.2*screenSize.y)),
				new Point((int)(0.5*screenSize.x), (int)(0.2*screenSize.y))} ;
		Font font = new Font("SansSerif", Font.BOLD, 28) ;
		
		DP.DrawText(new Point((int)(0.5*screenSize.x), (int)(0.05*screenSize.y)), "Center", OverallAngle, "Slot " + (SlotID + 1), font, ColorPalette[5]) ;
		//player.DrawAttWindow(MainWinDim, WindowPos[0], null, AllText, AllTextCat, 0, GoldCoinImage, icons, DP) ;
		player.getAttWindow().display(player, allText, player.getEquips(), player.getEquipsBonus(), player.getAttPoints(), new Point(0, 0), player.getPersonalAtt(), player.getBattleAtt(), DP) ;
		if (0 < pet.getLife()[0])
		{
 			DrawPetWindow(pet, WindowPos[0]) ;
		}
 		if (ArrowIconImage != null)
 		{
 			DrawWindowArrows(WindowPos[2], (int)(0.5*screenSize.x), 0, SlotID, NumberOfUsedSlots - 1) ;
 		}
	}
	public void DrawEmptyLoadingSlot(int SlotID, int NumSlots)
	{
		Point[] WindowPos = new Point[] {new Point((int)(0.35*screenSize.x), (int)(0.2*screenSize.y)),
				new Point((int)(0.65*screenSize.x), (int)(0.2*screenSize.y)),
				new Point((int)(0.5*screenSize.x), (int)(0.2*screenSize.y))} ;
		DP.DrawRoundRect(WindowPos[0], "TopLeft", new Size(screenSize.x / 3, screenSize.y / 2), 2, Color.white, Color.lightGray, true) ;
		DP.DrawText(new Point(WindowPos[0].x + screenSize.x / 6, WindowPos[0].y + screenSize.y / 4), "Center", OverallAngle, "Slot " + String.valueOf(SlotID + 1) + " is empty", new Font("SansSerif", Font.BOLD, 20), ColorPalette[5]) ;
		DrawWindowArrows(new Point(WindowPos[0].x, WindowPos[0].y + screenSize.y / 2), screenSize.x / 3, 0, SlotID, NumSlots) ;
	}
	public void DrawCustomizationMenu(Player player, int SelectedOption, int[] CurrentColorValue)
	{
		/*Font Titlefont = new Font("SansSerif", Font.BOLD, 16) ;
		Font font = new Font("SansSerif", Font.BOLD, 16) ;
		int CustomCat = AllTextCat[34] ;	// Main text category
		int ColorsCat = AllTextCat[39] ;	// Colors text category
		Point ColorBarsPos = new Point((int)(0.4 * screenSize.x), (int)(0.9 * screenSize.y)) ;
		int Sx = 100, Sy = font.getSize() ;
		int BarSize = 50 ;
		float[] ArrowScale = new float[] {1, 1} ;
		Color[] BarColor = new Color[] {ColorPalette[6], ColorPalette[3], ColorPalette[5]} ;
		Color[] BarTextColor = new Color[] {ColorPalette[6], ColorPalette[6], ColorPalette[6]} ;
		int[] MaxColorValue = new int[] {255, 255, 255} ;	// [Red, Green, Blue]
		
		player.display(new Point(screenSize.x / 2,  screenSize.y / 2 + player.getSize()[1]), new float[] {2, 2}, player.getDir(), false,  DP) ;
		
		// Main text
		DP.DrawText(new Point(screenSize.x / 2,  screenSize.y / 10), "Center", OverallAngle, AllText[CustomCat][1], Titlefont, ColorPalette[5]) ;
		DP.DrawText(new Point(screenSize.x / 2,  screenSize.y / 10 + 2 * Sy), "Center", OverallAngle, AllText[CustomCat][2], Titlefont, ColorPalette[5]) ;
		DP.DrawText(new Point(ColorBarsPos.x + Sx / 2, ColorBarsPos.y - 8 * Sy), "Center", OverallAngle, AllText[CustomCat][3], Titlefont, ColorPalette[5]) ;
		
		// Side bar text
		DP.DrawText(new Point(ColorBarsPos.x + Sx, ColorBarsPos.y - 5 * Sy), "Center", OverallAngle, Player.ActionKeys[0], font, ColorPalette[5]) ;	
		DP.DrawText(new Point(ColorBarsPos.x + Sx, ColorBarsPos.y - 3 * Sy), "Center", OverallAngle, Player.ActionKeys[2], font, ColorPalette[5]) ;	
		
		// Side bar arrows
		DP.DrawImage(ArrowIconImage, new Point(ColorBarsPos.x - Sx/2, ColorBarsPos.y - 6 * Sy), 90, ArrowScale, new boolean[] {false, false}, "Center", 1) ;
		DP.DrawImage(ArrowIconImage, new Point(ColorBarsPos.x - Sx/2, ColorBarsPos.y - 2 * Sy), 270, ArrowScale, new boolean[] {false, false}, "Center", 1) ;
		
		// Color bars
		BarTextColor[SelectedOption] = ColorPalette[3] ;
		for (int i = 0 ; i <= BarColor.length - 1 ; i += 1)
		{
			DP.DrawText(new Point(ColorBarsPos.x + i * Sx, ColorBarsPos.y - (int)(1.5 * BarSize)), "Center", OverallAngle, AllText[ColorsCat][i + 1], font, BarTextColor[i]) ;
			DrawTimeBar(new Point(ColorBarsPos.x + i * Sx, ColorBarsPos.y - (int)(0.5 * BarSize)), CurrentColorValue[i], MaxColorValue[i], 50, new int[] {0, 0}, "Right", "Vertical", BarColor[i]) ;
		}
		
		// Color bar arrows
		DP.DrawImage(ArrowIconImage, new Point(ColorBarsPos.x + (int)(0.5 * BarSize + BarSize/10) + SelectedOption * Sx, ColorBarsPos.y + 220 - (int)(0.5 * BarSize)), 0, ArrowScale, new boolean[] {false, false}, "Center", 1) ;	// right arrow
		DP.DrawImage(ArrowIconImage, new Point(ColorBarsPos.x - (int)(0.5 * BarSize + BarSize/10) + SelectedOption * Sx, ColorBarsPos.y + 220 - (int)(0.5 * BarSize)), 0, ArrowScale, new boolean[] {true, false}, "Center", 1) ;	// left arrow
		DP.DrawImage(ArrowIconImage, new Point(ColorBarsPos.x + SelectedOption * Sx + BarSize/20, ColorBarsPos.y + 220 + (int)(0.2 * BarSize)), 90, ArrowScale, new boolean[] {false, true}, "Center", 1) ;						// up arrow
		DP.DrawImage(ArrowIconImage, new Point(ColorBarsPos.x + SelectedOption * Sx + BarSize/20, ColorBarsPos.y + 220 + (int)(0.6 * BarSize)), 270, ArrowScale, new boolean[] {false, true}, "Center", 1) ;						// down arrow
	*/
	}
	
	
	/* Map and side bar */
	/*public void DrawSpellsBar(Player player, Spells[] spells, Image CooldownImage, Image SlotImage, Point MousePos)
	{
		Font Titlefont = new Font("SansSerif", Font.BOLD, 10) ;
		Font font = new Font("SansSerif", Font.BOLD, 9) ;
		String Title = AllText[AllTextCat[61]][1] ;
		Point Pos = new Point(screenSize.x + 1, (int) (0.99 * screenSize.y) - 70) ;
		int[] ActiveSpells = player.activeSpells(spells) ;
		Size size = new Size(36, 130) ;
		int slotW = SlotImage.getWidth(null), slotH = SlotImage.getHeight(null) ;	// Bar size
		int Ncols = Math.max(ActiveSpells.length / 11 + 1, 1) ;
		int Nrows = ActiveSpells.length / Ncols + 1 ;
		int Sx = (int) Utg.spacing(size.x, Ncols, slotW, 3), Sy = (int) Utg.spacing(size.y, Nrows, slotH, 5) ;		
		String[] Key = Player.SkillKeys ;
		Color BGcolor = Player.ClassColors[player.getJob()] ;
		Color TextColor = player.getColors()[0] ;
		int[] Counter = Utg.ArrayInPos(player.getSkillCounter(), 1) ;
		
		DP.DrawRoundRect(Pos, "BotLeft", size, 1, ColorPalette[7], BGcolor, true) ;
		DP.DrawText(new Point(Pos.x + size.x / 2, Pos.y - size.y + 3), "TopCenter", OverallAngle, Title, Titlefont, ColorPalette[5]) ;
		for (int i = 0 ; i <= ActiveSpells.length - 1 ; ++i)
		{
			if (0 < player.getSpell()[ActiveSpells[i]])
			{
				Point slotCenter = new Point(Pos.x + slotW / 2 + (i / Nrows) * Sx + 3, Pos.y - size.y + slotH / 2 + (i % Nrows) * Sy + Titlefont.getSize() + 5) ;
				if (player.getMp()[0] < spells[ActiveSpells[i]].getMpCost())
				{
					DP.DrawImage(SlotImage, slotCenter, "Center") ;
				}
				else
				{
					DP.DrawImage(SlotImage, slotCenter, "Center") ;
				}
				DP.DrawText(slotCenter, "Center", OverallAngle, Key[i], font, TextColor) ;
				int ImageW = CooldownImage.getWidth(null) ;
				int ImageH = CooldownImage.getHeight(null) ;
				if (Counter[ActiveSpells[i]] < spells[ActiveSpells[i]].getCooldown())
				{
					float[] Imscale = new float[] {1, 1 - (float) Counter[ActiveSpells[i]] / spells[ActiveSpells[i]].getCooldown()} ;
					DP.DrawImage(CooldownImage, new Point(slotCenter.x - ImageW / 2, slotCenter.y + ImageH / 2), OverallAngle, Imscale, new boolean[] {false, false}, "BotLeft", 1) ;
				}
				if (Utg.isInside(MousePos, new Point(slotCenter.x - ImageW / 2, slotCenter.y - ImageH / 2), ImageW, ImageH))
				{
					DP.DrawText(new Point(slotCenter.x - ImageW - 10, slotCenter.y), "CenterRight", OverallAngle, spells[ActiveSpells[i]].getName(), Titlefont, TextColor) ;
				}
			}
		}
	}*/
	
	
	
	
	
	/*public void DrawSideBar(Player player, Pet pet, Point MousePos, Spells[] spells, Icon[] icons, Image SkillCooldownImage, Image SlotImage)
	{
		// icons: 0: Options 1: Bag 2: Quest 3: Map 4: Book, 5: player, 6: pet
		Font font = new Font("SansSerif", Font.BOLD, 13) ;
		String[] IconKey = new String[] {Player.ActionKeys[9], Player.ActionKeys[4], Player.ActionKeys[9], Player.ActionKeys[7]} ;
		Color TextColor = ColorPalette[7] ;
		
		DP.DrawRect(new Point(screenSize.x, screenSize.y), "BotLeft", new Size(40, screenSize.y), 1, ColorPalette[9], ColorPalette[9], false) ;	// Background
		DrawSpellsBar(player, spells, SkillCooldownImage, SlotImage, MousePos) ;
		for (int i = 0 ; i <= 3 - 1 ; i += 1)	// Options, bag, and quest
		{
			icons[i].DrawImage(OverallAngle, 0, MousePos, DP) ;
			if (i != 0 | i != 6)	// Options and fab book doesn't have a text
			{
				DP.DrawText(icons[i].getPos(), "BotLeft", OverallAngle, IconKey[i], font, TextColor) ;
			}
		}
		if (player.getQuestSkills()[player.getContinent()])	// Map
		{
			icons[3].DrawImage(OverallAngle, 0, MousePos, DP) ;
			DP.DrawText(icons[3].getPos(), "BotLeft", OverallAngle, IconKey[3], font, TextColor) ;
		}
		if (player.getQuestSkills()[6])
		{
			icons[4].DrawImage(OverallAngle, 0, MousePos, DP) ;
		}
		player.display(icons[5].getPos(), new float[] {1, 1}, Player.MoveKeys[0], false, DP) ;						// Player
		DP.DrawText(icons[5].getPos(), "BotLeft", OverallAngle, Player.ActionKeys[5], font, TextColor) ;
		if (0 < player.getAttPoints())
		{
			DP.DrawImage(icons[5].getImage(), new Point(icons[5].getPos().x - icons[5].getImage().getWidth(null), icons[5].getPos().y), OverallAngle, new float[] {1, 1}, new boolean[] {false, false}, "BotLeft", 1) ;
		}
		if (0 < pet.getLife()[0])
		{
			DrawPet(icons[6].getPos(), new float[] {1, 1}, pet.getPersonalAtt().getimage()[0]) ;						// Pet
			DP.DrawText(icons[6].getPos(), "BotLeft", OverallAngle, Player.ActionKeys[8], font, TextColor) ;
		}
		
		// Hotkeys
		DP.DrawRoundRect(new Point(screenSize.x + 1, screenSize.y - 70), "TopLeft", new Size(36, 60), 1, ColorPalette[7], ColorPalette[19], true) ;
		for (int i = 0 ; i <= Player.HotKeys.length - 1 ; i += 1)
		{
			Point slotCenter = new Point(screenSize.x + 10, screenSize.y - 60 + 20 * i) ;
			int slotL = SlotImage.getWidth(null), slotH = SlotImage.getHeight(null) ;
			DP.DrawImage(SlotImage, slotCenter, "Center") ;
			DP.DrawText(new Point(slotCenter.x + slotL / 2 + 5, slotCenter.y + slotH / 2), "BotLeft", OverallAngle, Player.HotKeys[i], font, TextColor) ;
			if (player.hotkeyItem[i] != null)
			{
				DP.DrawImage(player.hotkeyItem[i].getImage(), slotCenter, "Center") ;
				if (Utg.isInside(MousePos, slotCenter, slotL, slotH))
				{
					DP.DrawText(new Point(slotCenter.x - slotL - 10, slotCenter.y), "CenterRight", OverallAngle, player.hotkeyItem[i].getName(), font, TextColor) ;
				}
			}
		}
	}*/
	/*public void DrawMapElems(Player player, Maps map)
	{
		if (map.getgroundType() != null)
		{
			Object[] groundType = map.getgroundType() ;
			for (int gt = 0 ; gt <= groundType.length - 1 ; gt += 1)
			{
				Object[] o = (Object[]) groundType[gt] ;
				String type = (String) o[0] ;
				Point p = (Point) o[1] ;
				if (type.equals("water"))
				{
					DP.DrawRect(new Point(p.x, p.y), "center", new Size(10, 10), 0, Color.blue, null, false);
				}
			}
		}
		if (map.getMapElem() != null)
		{
			for (int me = 0 ; me <= map.getMapElem().length - 1 ; me += 1)
			{
				map.getMapElem()[me].DrawImage(OverallAngle, DP) ;
			}
		}
	}*/
	/*public void DrawNPCsIntro(Player player, NPCs[] npc, float[] NPCScale, int[] NPCsInMap)
	{
		Font font = new Font("SansSerif", Font.BOLD, 12) ;
		for (int i = 0 ; i <= NPCsInMap.length - 1 ; ++i)
		{
			int NPCID = NPCsInMap[i] ;
			if (-1 < NPCID)
			{
				DrawNPC(player, npc, NPCID, NPCScale) ;
			}
		}
		Color TextColor = new Color(0, 0, 0) ;
		for (int i = 0 ; i <= NPCsInMap.length - 1 ; ++i)
		{
			int NPCID = NPCsInMap[i] ;
			if (-1 < NPCID)
			{
				int TextH = Utg.TextH(font.getSize()) ;
				int TextL = Math.max(Utg.TextL(npc[NPCID].getName(), font, G), Utg.TextL(npc[NPCID].getInfo(), font, G)) ;
				TextColor = npc[NPCID].getColor() ;
				Size WindowSize = new Size((int)(15 + TextL), (int)(10 + 2.2*TextH)) ;
				Point WindowPos = new Point((int) (npc[NPCID].getPos().x + 0.5*npc[0].getImage().getWidth(null) - 0.5*WindowSize.x), npc[NPCID].getPos().y - npc[0].getImage().getHeight(null)) ;
				if (i == 9)	// Elemental
				{
					TextColor = ColorPalette[8] ;
				}
				if (i == 1)	// Equips seller
				{
					WindowPos.y += -1.2*TextH ;
				}
				
				Point TextPos = new Point((int) (WindowPos.x + 5 + 0.5*WindowSize.x), (int) (WindowPos.y + TextH - 0.5*WindowSize.y)) ;
				DrawMenuWindow(WindowPos, WindowSize, null, 0, ColorPalette[player.getJob()], ColorPalette[7]) ;
				DP.DrawText(TextPos, "Center", OverallAngle, npc[NPCID].getInfo(), font, TextColor) ;
				TextPos.y += -1.2*TextH ;
				DP.DrawText(TextPos, "Center", OverallAngle, npc[NPCID].getName(), font, TextColor) ;
			}
		}
	}*/
	/*public void DrawNPC(Player player, NPCs[] npc, int NPCID, float[] NPCScale)
	{
		if (-1 < NPCID)
		{
			if (NPCID == 11 + 17*player.getMap())	// Master
			{
				player.display(npc[NPCID].getPos(), new float[] {1, 1}, Player.MoveKeys[3], false, DP) ;
			}
			else
			{
				DP.DrawImage(npc[NPCID].getImage(), npc[NPCID].getPos(), OverallAngle, NPCScale, new boolean[] {false, false}, "BotCenter", 1) ;
				DP.DrawText(npc[NPCID].getPos(), "BotCenter", OverallAngle, String.valueOf(NPCID), new Font("SansSerif", Font.BOLD, 10), Color.blue) ;				
			}
		}
	}*/
	/*public void DrawOutsideNPCs(Player player, NPCs[] npc, float[] NPCScale, int[] NPCsInMap)
	{
		if (NPCsInMap != null)	// Map has NPCs
		{
			for (int i = 0 ; i <= NPCsInMap.length - 1 ; i += 1)
			{
				int NPCID = NPCsInMap[i] ;
				if (npc[NPCID].getPosRelToBuilding().equals("Outside"))
				{
					DrawNPC(player, npc, NPCID, NPCScale) ;			
				}
			}
		}
	}*/
	/*public void DrawBuildings(Player player, NPCs[] npc, Buildings[] building, float[] NPCScale, float[] Scale)
	{
		int mapID = player.getMap() ;
		int[] BuildingsInCity = Uts.BuildingsInCity(building, mapID) ;
		int[][] NPCsInBuildings = Uts.NPCsInBuildings(npc, building, mapID, BuildingsInCity) ;
		int TextCat = AllTextCat[33] ;
		Font font = new Font("SansSerif", Font.BOLD, 13) ;
		for (int b = 0 ; b <= BuildingsInCity.length - 1 ; b += 1)
		{
			int id = BuildingsInCity[b] ;
			building[id].display(player.getPos(), OverallAngle, new float[] {1, 1}, DP) ;
			if (building[id].playerIsInside(player.getPos()))
			{				
				if (NPCsInBuildings[b] != null)		// If the building has NPCs
				{
					for (int n = 0 ; n <= NPCsInBuildings[b].length - 1 ; n += 1)
					{
						DrawNPC(player, npc, NPCsInBuildings[b][n], NPCScale) ;
					}
				}
			}
		}
		
		// sign building
		Point SignPos = Uts.BuildingPos(building, mapID, "Sign") ;
		if (building[5].playerIsInside(player.getPos()))
		{			
			int[][] SignTextPos = new int[][] {{SignPos.x - 200, SignPos.y - 150}, {SignPos.x + 50, SignPos.y - 50}, {SignPos.x + 50, SignPos.y - 50}, {SignPos.x + 100, SignPos.y - 50}, {SignPos.x - 540, SignPos.y - 50}} ;
			Point Pos = new Point(SignTextPos[mapID][0], SignTextPos[mapID][1]) ;
			DrawMenuWindow(Pos, new Size((int)(0.25*Utg.TextL(AllText[TextCat][mapID + 1], font, G)), (int)(7*Utg.TextH(font.getSize()))), null, 0, ColorPalette[4], ColorPalette[4]) ;
			DP.DrawFitText(new Point(Pos.x + 10, Pos.y - (int)(5.5*Utg.TextH(font.getSize()))), Utg.TextH(font.getSize()), "BotLeft", AllText[TextCat][mapID + 1], font, 35, ColorPalette[5]) ;		
		}
	}*/

	public void DrawGrid(int[] spacing)
	{
		for (int i = 0 ; i <= screenSize.x/spacing[0] - 1 ; ++i)
		{
			int LineThickness = 1 ;
			Color color = ColorPalette[9] ;
			if (i % 10 == 0)
			{
				LineThickness = 2 ;
			}
			if (i % 20 == 0)
			{
				LineThickness = 2 ;
				color = ColorPalette[5] ;
			}
			DP.DrawLine(new int[] {i*spacing[0], i*spacing[0]}, new int[] {0, screenSize.y}, LineThickness, color) ;
			for (int j = 0 ; j <= screenSize.y/spacing[1] - 1 ; ++j)
			{
				LineThickness = 1 ;
				color = ColorPalette[9] ;
				if (j % 10 == 0)
				{
					LineThickness = 2 ;
				}
				if (j % 20 == 0)
				{
					LineThickness = 2 ;
					color = ColorPalette[5] ;
				}
				DP.DrawLine(new int[] {0, screenSize.x}, new int[] {j*spacing[1], j*spacing[1]}, LineThickness, color) ;
			}							
		}
	}	
	public void DrawFullMap(Player player, Pet pet, NPCs[] npc, Buildings[] buildings, Sky sky, Icon[] SBicons, Point MousePos)
	{
		sky.display(DP) ;
		player.getMap().display(DP) ;
		player.getMap().displayElements(DP) ;
		player.getMap().displayBuildings(player.getPos(), player.allText.get("* Mensagem das placas *"), DP) ;
		player.getMap().displayNPCs(DP) ;	
		//DrawGrid(new int[] {20, 20}) ;
		player.DrawSideBar(player, pet,  MousePos, player.getSpell(), SBicons, DP) ;
		
		// draw time
		Font font = new Font("SansSerif", Font.BOLD, 14) ;
		float time = (float)(sky.dayTime) / Game.DayDuration ;
		DP.DrawText(new Point(0, (int) (0.99*screenSize.y)), "BotLeft", OverallAngle, (int)(24*time) + ":" + (int)(24*60*time % 60), font, ColorPalette[5]) ;
	}
	public void DrawTimeBar(Point Pos, int Counter, int Delay, int size2, int[] offset, String relPos, String dir, Color color)
	{
		Size size = new Size((int)(2 + size2/20), (int)(size2)) ;
		int mirror = UtilS.MirrorFromRelPos(relPos) ;
		int RectT = 1 ;
		Color BackgroundColor = ColorPalette[7] ;
		if (dir.equals("Vertical"))
		{
			Pos = new Point(Pos.x + mirror*offset[0], Pos.y + offset[1]) ;
			DP.DrawRect(Pos, "Center", size, RectT, BackgroundColor, ColorPalette[9], true) ;
			DP.DrawRect(new Point(Pos.x - size.x / 2, Pos.y + size.y / 2), "BotLeft", new Size(size.x, size.y * Counter / Delay), RectT, color, ColorPalette[9], false) ;	
		}
		if (dir.equals("Horizontal"))
		{
			Pos = new Point(Pos.x + offset[0], Pos.y + mirror*offset[1]) ;
			DP.DrawRect(Pos, "Center", new Size(size.y, size.x), RectT, BackgroundColor, ColorPalette[9], true) ;
			DP.DrawRect(new Point(Pos.x - size.y / 2, Pos.y + size.x / 2), "BotLeft", new Size(size.y * Counter / Delay, size.x), RectT, color, ColorPalette[9], false) ;	
		}			
	}
	
	
	/* Player actions */
	/*public void DrawBag(Point Pos, Size size, Bag bag, Image MenuImage, Image SlotImage, int menu, int Tab, int window, int windowLimit, int SelectedItem, Point MousePos)
	{
		int MenusCat = AllTextCat[30] ;
		Font MenuFont = new Font("SansSerif", Font.BOLD, 13) ;
		Font ItemFont = new Font("SansSerif", Font.BOLD, 10) ;
		Color BGColor = ColorPalette[11] ;
		int NSlotsmax = 20 ;
		if (Tab == 1)
		{
			BGColor = ColorPalette[19] ;
		}
		
		
		// Draw menus
		int MenuL = MenuImage.getWidth(null) ;
		int MenuH = MenuImage.getHeight(null) ;
		for (int m = 0 ; m <= AllText[MenusCat].length - 3 ; m += 1)
		{
			Point MenuPos = new Point(Pos.x + 8, Pos.y + m * (MenuH - 1)) ;
			Color TextColor = ColorPalette[12] ;
			if (m == menu)
			{
				TextColor = ColorPalette[3] ;
				MenuPos.x += 3 ;
			}
			DP.DrawImage(MenuImage, MenuPos, "TopRight") ;
			DP.DrawText(new Point(MenuPos.x - MenuL / 2, MenuPos.y + MenuH / 2), "Center", OverallAngle, AllText[MenusCat][m + 1],
					MenuFont, TextColor) ;
		}
		
		// Draw bag
		DP.DrawRoundRect(Pos, "TopLeft", size, 1, BGColor, BGColor, true) ;
		
		
		// Draw items
		Item[] ActiveItems = null;
		if (menu == 0)
		{
			ActiveItems = bag.getPotions() ;
		}
		int NumberOfSlots = Math.min(NSlotsmax, ActiveItems.length - NSlotsmax * window) ;
		
		int slotW = SlotImage.getWidth(null) ;
		int slotH = SlotImage.getHeight(null) ;
		Point[] slotCenter = new Point[NumberOfSlots] ;
		Point[] textPos = new Point[NumberOfSlots] ;
		for (int i = 0 ; i <= NumberOfSlots - 1 ; i += 1)
		{
			int sx = size.x / 2, sy = (size.y - 6 - slotH) / 9 ;
			int row = i % (NSlotsmax / 2), col = i / (NSlotsmax / 2) ;
			slotCenter[i] = new Point((int) (Pos.x + 5 + slotW / 2 + col * sx), (int) (Pos.y + 3 + slotH / 2 + row * sy)) ;
			textPos[i] = new Point(slotCenter[i].x + slotW / 2 + 5, slotCenter[i].y) ;
		}
		
		for (int i = 0 ; i <= NumberOfSlots - 1 ; i += 1)
		{
			String text = ActiveItems[i].getName() ;//+ " (x" + bag[i] + ")" ;
			Color TextColor = ColorPalette[3] ;
			if (i == SelectedItem)
			{
				TextColor = ColorPalette[6] ;
			}
			DP.DrawImage(SlotImage, slotCenter[i], "Center") ;				// Draw slots
			//DP.DrawImage(items[i].getImage(), slotCenter[i], "Center") ;	// Draw items
			DP.DrawTextUntil(textPos[i], "CenterLeft", OverallAngle, text, ItemFont, TextColor, 10, MousePos) ;	
		}
		if (0 < windowLimit)
		{
			DrawWindowArrows(new Point(Pos.x, Pos.y + size.y), size.x, 0, window, windowLimit) ;
		}
	}	*/
	/*public void DrawBag(int PlayerJob, Point Pos, int L, int H, int[] Bag, int NSlotsmax, Items[] items, Image MenuImage, Image SlotImage, int[] ActiveItems, int[][] slotCenter, int[][] textPos, int menu, int Tab, int window, int windowLimit, int SelectedItem, Point MousePos)
	{
		int MenusCat = AllTextCat[30] ;
		Font MenuFont = new Font("SansSerif", Font.BOLD, 13) ;
		Font ItemFont = new Font("SansSerif", Font.BOLD, 10) ;
		Color BGColor = ColorPalette[11] ;
		if (Tab == 1)
		{
			BGColor = ColorPalette[19] ;
		}
		
		
		// Draw menus 
		int MenuL = MenuImage.getWidth(null) ;
		int MenuH = MenuImage.getHeight(null) ;
		for (int m = 0 ; m <= AllText[MenusCat].length - 3 ; m += 1)
		{
			int[] MenuPos = new int[] {Pos.x + 8, Pos.y + m * (MenuH - 1)} ;
			Color TextColor = ColorPalette[12] ;
			if (m == menu)
			{
				TextColor = ColorPalette[3] ;
				MenuPos.x += 3 ;
			}
			DP.DrawImage(MenuImage, MenuPos, "TopRight") ;
			DP.DrawText(new Point(MenuPos.x - MenuL / 2, MenuPos.y + MenuH / 2}, "Center", OverallAngle, AllText[MenusCat][m + 1], MenuFont, TextColor) ;
		}
		
		
		DP.DrawRoundRect(Pos, "TopLeft", L, H, 1, BGColor, BGColor, true) ;
		int NumberOfSlots = Math.min(NSlotsmax, ActiveItems.length - NSlotsmax * window) ;
		for (int i = 0 ; i <= NumberOfSlots - 1 ; i += 1)
		{
			int ItemID = ActiveItems[i + NSlotsmax * window] + Items.BagIDs[menu] ;
			if (Items.BagIDs[5] <= ItemID & ItemID < Items.BagIDs[6])	// Equips
			{
				ItemID += Items.NumberOfItems[6] * PlayerJob ;
			}
			if (0 < Bag[ItemID])
			{
				String text = items[ItemID].getName() + " (x" + Bag[ItemID] + ")" ;
				Color TextColor = ColorPalette[3] ;
				if (ItemID == SelectedItem)
				{
					TextColor = ColorPalette[6] ;
				}
				DP.DrawImage(SlotImage, slotCenter[i], "Center") ;			// Draw slots
				DP.DrawImage(items[i].getImage(), slotCenter[i], "Center") ;	// Draw items
				DP.DrawTextUntil(textPos[i], "CenterLeft", OverallAngle, text, ItemFont, TextColor, 10, MousePos) ;
			}	
		}
		if (0 < windowLimit)
		{
			DrawWindowArrows(new int[] {Pos.x, Pos.y + H}, L, 0, window, windowLimit) ;
		}
	}	*/
	public void DrawFabBook(Image BookImage, Items[] items, int SelectedPage, int[][] Ingredients, int[][] Products, Point MousePos)
	{
		Point Pos = new Point((int)(0.5*screenSize.x), (int)(0.5*screenSize.y)) ;
		Font Titlefont = new Font("SansSerif", Font.BOLD, 16) ;
		Font font = new Font("SansSerif", Font.BOLD, 14) ;
		int L = BookImage.getWidth(null), H = BookImage.getHeight(null) ;
		int sy = H/15 ;
		int IngredientsCont = 0, ProductsCont = 0 ;
		int MaxTextL = 10 ;
		DP.DrawImage(BookImage, Pos, OverallAngle, new float[] {1, 1}, new boolean[] {false, false}, "Center", 1) ;
		DP.DrawText(new Point(Pos.x - 3*L/8, Pos.y - H/5 - sy/4), "BotLeft", OverallAngle, "Ingredients:", Titlefont, ColorPalette[5]) ;
		DP.DrawText(new Point(Pos.x + 3*L/8, Pos.y - H/5 - sy/4), "TopRight", OverallAngle, "Products:", Titlefont, ColorPalette[5]) ;		
		for (int j = 0 ; j <= Ingredients[SelectedPage].length - 1 ; ++j)
		{
			if (-1 < Ingredients[SelectedPage][j])
			{
				/*if (Utg.MouseIsInside(MousePos, new int[] {Pos.x - 3*L/8, Pos.y - H/5 + IngredientsCont*sy + sy/2}, MaxTextL*font.getSize()/2, Utg.TextH(font.getSize())))
				{
					DP.DrawText(new Point(Pos.x - 3*L/8, Pos.y - H/5 + IngredientsCont*sy + sy/2}, "BotLeft", OverallAngle, items[Ingredients[SelectedPage][j]].getName(), font, ColorPalette[5]) ;
				}
				else
				{*/
					DP.DrawTextUntil(new Point(Pos.x - 3*L/8, Pos.y - H/5 + IngredientsCont*sy + sy/2), "BotLeft", OverallAngle, items[Ingredients[SelectedPage][j]].getName(), font, ColorPalette[5], MaxTextL, MousePos) ;
				//}
				IngredientsCont += 1 ;
			}
		}
		for (int j = 0 ; j <= Products[SelectedPage].length - 1 ; ++j)
		{
			if (-1 < Products[SelectedPage][j])
			{
				/*if (Utg.MouseIsInside(MousePos, new int[] {Pos.x + 3*L/8 - MaxTextL*font.getSize()/2, Pos.y - H/5 + ProductsCont*sy + sy/2}, MaxTextL*font.getSize()/2, Utg.TextH(font.getSize())))
				{
					DP.DrawText(new Point(Pos.x + 3*L/8, Pos.y - H/5 + ProductsCont*sy + sy/2}, "TopRight", OverallAngle, items[Products[SelectedPage][j]].getName(), font, ColorPalette[5]) ;
				}
				else
				{*/
					DP.DrawTextUntil(new Point(Pos.x + 3*L/8, Pos.y - H/5 + ProductsCont*sy + sy/2), "TopRight", OverallAngle, items[Products[SelectedPage][j]].getName(), font, ColorPalette[5], MaxTextL, MousePos) ;
				//}
				ProductsCont += 1 ;
			}
		}
		DrawWindowArrows(new Point(Pos.x, Pos.y + 15*H/32), L, 0, SelectedPage, Ingredients.length - 1) ;
	}
	/*public void DrawMap(int playerMap, String playerDir, Maps[] maps)
	{
		int LineThickness = 2 ;
		Font font = new Font("SansSerif", Font.BOLD, 16) ;
		Point WindowPos = new Point((int)(0.25*screenSize.x), (int)(0.75*screenSize.y)) ;
		int L = (int)(0.05*screenSize.x), H = (int)(0.05*screenSize.y) ;
		int Sx = (int)(0.01*screenSize.x), Sy = (int)(0.01*screenSize.y) ;
		float[][] MapsPos = new float[][] {{0, 3}, {2, 0}, {4, 4}, {4, 6}, {0, 0}, {1, 0}, {0, 1}, {1, 1}, {2, 1}, {3, 1}, {1, 2}, {2, 2}, {3, 2}, {4, 2}, {1, 3}, {2, 3}, {3, 3}, {4, 3}, {1, 4}, {2, 4}, {3, 4}, {1, 5}, {2, 5}, {3, 5}, {4, 5}, {1, 6}, {2, 6}, {3, 6}, {2, 7}, {3, 7}, {0, 1}, {1, 1}, {2, 1}, {0, 0}, {1, 0}, {2, 0}, {3, (float)(0.5)}, {4, 1}, {4, 0}, {5, (float)(0.5)}, {0, (float)(0.5)}, {1, 1}, {1, 0}, {2, 1}, {2, 0}, {2, 0}, {0, 1}, {1, 1}, {2, 1}, {0, 2}, {1, 2}, {2, 2}, {0, 3}, {1, 3}, {2, 3}, {2, 1}, {2, 0}, {1, 1}, {1, 0}, {0, 0}, {1, 0}, {0, 3}, {1, 3}, {2, 3}, {3, 3}, {1, 2}, {1, 1}} ;
		int[][] MapsLeftBot = new int[MapsPos.length][2] ;
		int [] MapStart = new int[] {0, 30, 40, 45, 55, 60}, MapEnd = new int[] {29, 39, 44, 54, 59, 66} ;
		int Continent = maps[playerMap].getContinent() ;
		Color LineColor = ColorPalette[9], ContinentColor = ColorPalette[9] ;
		
		for (int map = 0 ; map <= MapsPos.length - 1 ; map += 1)
		{
			MapsLeftBot[map] = new int[] {(int)(Sx + MapsPos[map][0]*(L + Sx)), (int)(-Sy - MapsPos[map][1]*(H + Sy))} ; 
		}
		ContinentColor = ContinentsColor[Continent] ;
		DP.DrawRoundRect(WindowPos, "BotLeft", new Size(8 * L, 10 * H), 1, ColorPalette[18], ColorPalette[7], true) ;
		for (int map = MapStart[Continent] ; map <= MapEnd[Continent] ; map += 1)
		{
			L = (int)(0.05*screenSize.x) ;
			H = (int)(0.05*screenSize.y) ;
			if (map == 25)
			{
				H = 2*H + Sy ;
			} else if (map == 3)
			{
				L = 2*L + Sx ;
				H = 2*H + Sy ;
			}
			Point MapPos = new Point(WindowPos.x + MapsLeftBot[map][0], WindowPos.y + MapsLeftBot[map][1]) ;
			int[] MapConnections = Arrays.copyOf(maps[map].getConnections(), maps[map].getConnections().length) ;	// 0: Top left, 1: Left top, 2: Left bottom, 3: Bottom left, 4: Bottom right, 5: Right bottom, 6: Right top, 7: Top right
			int[][][] LineType = new int[12][2][] ;
			
			LineType[0] = new int[][] {{MapPos.x + L/2, MapPos.x + L/2}, {MapPos.y - H, MapPos.y - Sy - H}} ;		// Vertical middle line going up
			LineType[1] = new int[][] {{MapPos.x + L/4, MapPos.x + L/4}, {MapPos.y - H, MapPos.y - Sy - H}} ;		// Vertical left line going up
			LineType[2] = new int[][] {{MapPos.x + 3*L/4, MapPos.x + 3*L/4}, {MapPos.y - H, MapPos.y - Sy - H}} ;	// Vertical right line going up
			LineType[3] = new int[][] {{MapPos.x - Sx, MapPos.x}, {MapPos.y - H/2, MapPos.y - H/2}} ;				// Horizontal middle line going right
			LineType[4] = new int[][] {{MapPos.x - Sx, MapPos.x}, {MapPos.y - 3*H/4, MapPos.y - 3*H/4}} ;			// Horizontal top line going right
			LineType[5] = new int[][] {{MapPos.x - Sx, MapPos.x}, {MapPos.y - H/4, MapPos.y - H/4}} ;				// Horizontal bottom line going right
			LineType[6] = new int[][] {{MapPos.x + L/2, MapPos.x + L/2}, {MapPos.y, MapPos.y + Sy}} ;				// Vertical middle line going down
			LineType[7] = new int[][] {{MapPos.x + L/4, MapPos.x + L/4}, {MapPos.y, MapPos.y + Sy}} ;				// Vertical left line going down
			LineType[8] = new int[][] {{MapPos.x + 3*L/4, MapPos.x + 3*L/4}, {MapPos.y, MapPos.y + Sy}} ;			// Vertical right line going down
			LineType[9] = new int[][] {{MapPos.x + L, MapPos.x + Sx + L}, {MapPos.y - H/2, MapPos.y - H/2}} ;		// Horizontal middle line going left
			LineType[10] = new int[][] {{MapPos.x + L, MapPos.x + Sx + L}, {MapPos.y - 3*H/4, MapPos.y - 3*H/4}} ;	// Horizontal top line going left
			LineType[11] = new int[][] {{MapPos.x + L, MapPos.x + Sx + L}, {MapPos.y - H/4, MapPos.y - H/4}} ;		// Horizontal bottom line going left
					
			// Draw connection lines
			if (map != 3 & map != 25 & map != 36 & map != 39)
			{
				if (-1 < MapConnections[0] & -1 < MapConnections[7])
				{
					DP.DrawLine(LineType[0][0], LineType[0][1], LineThickness, LineColor) ;				
				} else if (-1 < MapConnections[0])
				{
					DP.DrawLine(LineType[1][0], LineType[1][1], LineThickness, LineColor) ;				
				} else if (-1 < MapConnections[7])
				{
					DP.DrawLine(LineType[2][0], LineType[2][1], LineThickness, LineColor) ;									
				}
				if (-1 < MapConnections[1] & -1 < MapConnections[2])
				{
					DP.DrawLine(LineType[3][0], LineType[3][1], LineThickness, LineColor) ;				
				} else if (-1 < MapConnections[1])
				{
					DP.DrawLine(LineType[4][0], LineType[4][1], LineThickness, LineColor) ;				
				} else if (-1 < MapConnections[2])
				{
					DP.DrawLine(LineType[5][0], LineType[5][1], LineThickness, LineColor) ;				
				}
				if (-1 < MapConnections[3] & -1 < MapConnections[4])
				{
					DP.DrawLine(LineType[6][0], LineType[6][1], LineThickness, LineColor) ;				
				} else if (-1 < MapConnections[3])
				{
					DP.DrawLine(LineType[7][0], LineType[7][1], LineThickness, LineColor) ;				
				} else if (-1 < MapConnections[4])
				{
					DP.DrawLine(LineType[8][0], LineType[8][1], LineThickness, LineColor) ;				
				}
				if (-1 < MapConnections[5] & -1 < MapConnections[6])
				{
					DP.DrawLine(LineType[9][0], LineType[9][1], LineThickness, LineColor) ;				
				} else if (-1 < MapConnections[5])
				{
					DP.DrawLine(LineType[11][0], LineType[11][1], LineThickness, LineColor) ;				
				} else if (-1 < MapConnections[6])
				{
					DP.DrawLine(LineType[10][0], LineType[10][1], LineThickness, LineColor) ;				
				}
			}
			if (map == 3)
			{
				DP.DrawLine(new int[] {MapPos.x + L/4 - Sx/4, MapPos.x + L/4 - Sx/4}, new int[] {MapPos.y- H, MapPos.y - Sy - H}, LineThickness, LineColor) ;				
				DP.DrawLine(new int[] {MapPos.x + 3*L/4 + Sx/4, MapPos.x + 3*L/4 + Sx/4}, new int[] {MapPos.y- H, MapPos.y - Sy - H}, LineThickness, LineColor) ;				
				DP.DrawLine(new int[] {MapPos.x + L/4 - Sx/4, MapPos.x + L/4 - Sx/4}, new int[] {MapPos.y, MapPos.y + Sy}, LineThickness, LineColor) ;								
				DP.DrawLine(new int[] {MapPos.x + L, MapPos.x + Sx + L}, new int[] {MapPos.y - 3*H/4 - Sy/4, MapPos.y - 3*H/4 - Sy/4}, LineThickness, LineColor) ;				
			}
			if (map == 25)
			{
				DP.DrawLine(new int[] {MapPos.x - Sx, MapPos.x}, new int[] {MapPos.y - H/4 + Sy/4, MapPos.y - H/4 + Sy/4}, LineThickness, LineColor) ;				
				DP.DrawLine(new int[] {MapPos.x - Sx, MapPos.x}, new int[] {MapPos.y - 3*H/4 - Sy/4, MapPos.y - 3*H/4 - Sy/4}, LineThickness, LineColor) ;							
			}
		}
		
		for (int map = MapStart[Continent] ; map <= MapEnd[Continent] ; map += 1)
		{
			L = (int)(0.05*screenSize.x) ;
			H = (int)(0.05*screenSize.y) ;
			if (map == 25)
			{
				H = 2*H + Sy ;
			} else if (map == 3)
			{
				L = 2*L + Sx ;
				H = 2*H + Sy ;
			}
			Point MapPos = new Point(WindowPos.x + MapsLeftBot[map][0], WindowPos.y + MapsLeftBot[map][1]) ;
			// Maps
			if (map == 0 | map == 4 | map == 2 | map == 13 | map == 17)
			{
				DP.DrawRect(MapPos, "BotLeft", L, H, 1, ColorPalette[19], ColorPalette[9], true) ;
			}
			else
			{
				DP.DrawRect(MapPos, "BotLeft", L, H, 1, ContinentColor, ColorPalette[9], true) ;
			}	
			
			// Text
			if (Continent == 5)
			{
				DP.DrawText(new Point(MapPos.x + L/3, MapPos.y - H/3), "BotLeft", OverallAngle, String.valueOf(map), font, ColorPalette[20]) ;					
			}
			else
			{
				DP.DrawText(new Point(MapPos.x + L/2, MapPos.y - H/2), "Center", OverallAngle, String.valueOf(map), font, ColorPalette[5]) ;				
			}
			
			// Player
			if (playerMap == map)
			{
				//player.DrawPlayer(new int[] {MapPos.x + (int)(player.getPos()[0]/(float)(WinDim[0])*L), MapPos.y - (int)((WinDim[1] - player.getPos()[1])/(float)(WinDim[1])*H)}, new float[] {(float)0.2, (float)0.2}, player.getDir(), false, DP) ;
			}
		}
	}*/
	public void DrawPetWindow(Pet pet, Point Pos)
	{
		//	 TODO decide what to do with the text here
		/*Font Namefont = new Font("SansSerif", Font.BOLD, 16) ;
		Font font = new Font("SansSerif", Font.BOLD, 14) ;
		Size size = new Size((int)(0.35*screenSize.x), (int)(0.5*screenSize.y)) ;
		int sy = (int)(0.07 * size.y) ;
		float angle = OverallAngle ;
		Color TextColor = ColorPalette[5] ;
		DP.DrawRoundRect(Pos, "BotLeft", size, 1, pet.getColor(), ColorPalette[7], true) ;
		DrawPet(new Point(Pos.x + (int)(0.5 * size.x), Pos.y - (int)(0.7 * size.y)), new float[] {(float)2, (float)2}, pet.getMovingAnimations().idleGif) ;
		DP.DrawText(new Point(Pos.x + (int)(0.5 * size.x), Pos.y - (int)(0.92 * size.y)), "Center", angle, pet.getName(), Namefont, TextColor) ;				// Name	
		DP.DrawText(new Point(Pos.x + (int)(0.43 * size.x), Pos.y - (int)(0.85 * size.y)), "BotLeft", angle, AllText[TextCat][1] + ": " + pet.getLevel(), font, ColorPalette[6]) ;	// Level
		DP.DrawText(new Point(Pos.x + (int)(0.025 * size.x), Pos.y - (int)(0.83 * size.y)), "BotLeft", angle, AllText[TextCat][2] + ": " + UtilG.Round(pet.getLife()[0], 1), font, ColorPalette[6]) ;	// Life		
		DP.DrawText(new Point(Pos.x + (int)(0.025 * size.x), Pos.y - (int)(0.78 * size.y)), "BotLeft", angle, AllText[TextCat][3] + ": " + UtilG.Round(pet.getMp()[0], 1), font, ColorPalette[5]) ;	// MP
		DP.DrawText(new Point(Pos.x + (int)(0.025 * size.x), Pos.y - 13*sy/2), "BotLeft", angle, AllText[TextCat][4] + ": " + UtilG.Round(pet.getPhyAtk()[0], 1) + " + " + UtilG.Round(pet.getPhyAtk()[1], 1) + " + " + UtilG.Round(pet.getPhyAtk()[2], 1), font, TextColor) ;		
		DP.DrawText(new Point(Pos.x + (int)(0.025 * size.x), Pos.y - 11*sy/2), "BotLeft", angle, AllText[TextCat][5] + ": " + UtilG.Round(pet.getMagAtk()[0], 1) + " + " + UtilG.Round(pet.getMagAtk()[1], 1) + " + " + UtilG.Round(pet.getMagAtk()[2], 1), font, TextColor) ;		
		DP.DrawText(new Point(Pos.x + (int)(0.025 * size.x), Pos.y - 9*sy/2), "BotLeft", angle, AllText[TextCat][6] + ": " + UtilG.Round(pet.getPhyDef()[0], 1) + " + " + UtilG.Round(pet.getPhyDef()[1], 1) + " + " + UtilG.Round(pet.getPhyDef()[2], 1), font, TextColor) ;		
		DP.DrawText(new Point(Pos.x + (int)(0.025 * size.x), Pos.y - 7*sy/2), "BotLeft", angle, AllText[TextCat][7] + ": " + UtilG.Round(pet.getMagDef()[0], 1) + " + " + UtilG.Round(pet.getMagDef()[1], 1) + " + " + UtilG.Round(pet.getMagDef()[2], 1), font, TextColor) ;		
		DP.DrawText(new Point(Pos.x + (int)(0.025 * size.x), Pos.y - 5*sy/2), "BotLeft", angle, AllText[TextCat][8] + ": " + UtilG.Round(pet.getDex()[0], 1) + " + " + UtilG.Round(pet.getDex()[1], 1) + " + " + UtilG.Round(pet.getDex()[2], 1), font, TextColor) ;		
		DP.DrawText(new Point(Pos.x + (int)(0.025 * size.x), Pos.y - 3*sy/2), "BotLeft", angle, AllText[TextCat][9] + ": " + UtilG.Round(pet.getAgi()[0], 1) + " + " + UtilG.Round(pet.getAgi()[1], 1) + " + " + UtilG.Round(pet.getAgi()[2], 1), font, TextColor) ;		
		DP.DrawText(new Point(Pos.x + (int)(0.025 * size.x), Pos.y - sy/2), "BotLeft", angle, AllText[TextCat][10] + ": " + (int)(100*pet.getCrit()[0]) + "% + " + (int)(100*pet.getCrit()[1]) + "%", font, ColorPalette[6]) ;		
		DP.DrawImage(ElementImages[UtilS.ElementID(pet.getElem()[1])], new Point(Pos.x + (int)(0.5 * size.x), Pos.y - (int)(0.52 * size.y)), angle, new float[] {(float) 0.5, (float) 0.5}, new boolean[] {false, false}, "Center", 1) ;
	*/
		}
	/*public void DrawOptionsWindow(int SelectedItem, int SelectedMenu, String[] ActionKeys, Settings settings)
	{
		int TextCat = AllTextCat[36] ;
		int L = (int)(0.5*screenSize.x), H = (int)Math.max(0.34*screenSize.y, ActionKeys.length*(Utg.TextH(L / 20 + 2) + 4) + 8) ;
		Font font = new Font("SansSerif", Font.BOLD, L / 20 + 2) ;
		Color[] TextColor = new Color[3 + ActionKeys.length] ;
		Point Pos = new Point((int)(0.4*screenSize.x), (int)(0.4*screenSize.y)) ;
		Point TextPos = new Point(Pos.x + 5, Pos.y + 5) ;
		int TextH = Utg.TextH(font.getSize()) ;
		int Sx = 7*L/8, Sy = TextH + 4 ;
		Arrays.fill(TextColor, ColorPalette[5]) ;
		TextColor[SelectedItem] = ColorPalette[3] ;
		DrawMenuWindow(Pos, L, H, null, 0, ColorPalette[8], ColorPalette[7]) ;
		if (SelectedMenu == 0)
		{
			for (int i = 0 ; i <= 5 ; i += 1)
			{
				DP.DrawText(new Point(TextPos.x, TextPos.y + (i + 1)*Sy), "BotLeft", OverallAngle, AllText[TextCat][i + 1], font, TextColor[i]) ;
			}
			settings.displayValue(settings.getMusicIsOn(), new Point(TextPos.x + Sx, TextPos.y + Sy), OverallAngle, font, DP) ;
			settings.displayValue(settings.getSoundEffectsAreOn(), new Point(TextPos.x + Sx, TextPos.y + 2 * Sy), OverallAngle, font, DP) ;
			settings.displayValue(settings.getShowPlayerRange(), new Point(TextPos.x + Sx, TextPos.y + 3 * Sy), OverallAngle, font, DP) ;
			DP.DrawText(new Point(TextPos.x + Sx, TextPos.y + (3 + 1)*Sy), "BotCenter", OverallAngle, String.valueOf(settings.getAttDisplay()), font, TextColor[3]) ;
			DP.DrawText(new Point(TextPos.x + Sx, TextPos.y + (4 + 1)*Sy), "BotCenter", OverallAngle, String.valueOf(settings.getDamageAnimation()), font, TextColor[4]) ;				
		}
		else if (SelectedMenu == 1)
		{
			for (int i = 0 ; i <= ActionKeys.length - 1 ; i += 1)
			{
				DP.DrawText(new Point(TextPos.x, TextPos.y + (i + 1)*Sy), "BotLeft", OverallAngle, AllText[TextCat][i + 7], font, TextColor[i]) ;
				DP.DrawText(new Point(TextPos.x + Sx, TextPos.y + (i + 1)*Sy - TextH/2), "Center", OverallAngle, ActionKeys[i], font, ColorPalette[5]) ;			
			}
		}
		else if (SelectedMenu == 2)
		{
			DrawMenuWindow(Pos, L, (int)Math.max(0.34*screenSize.y, ActionKeys.length*(Utg.TextH(L / 20 + 2) + 4) + 8), null, 0, ColorPalette[8], ColorPalette[7]) ;
			for (int i = 0 ; i <= ActionKeys.length - 1 ; i += 1)
			{
				DP.DrawText(new Point(TextPos.x, TextPos.y + (i + 1)*Sy), "BotLeft", OverallAngle, AllText[TextCat][i + 7], font, TextColor[i]) ;
				DP.DrawText(new Point(TextPos.x + Sx, TextPos.y + (i + 1)*Sy - TextH/2), "Center", OverallAngle, ActionKeys[i], font, TextColor[i]) ;			
			}
		}
	}*/
	/*public void DrawQuestRequirementsList(CreatureTypes[] creatureTypes, Creatures[] creatures, Items[] items, Quests quest, NPCs npc, int[] SpeechPos, boolean QuestHasCreatures, boolean QuestHasItems, Image SpeakingBubbleImage)
	{
		Font font = new Font("SansSerif", Font.BOLD, 14) ;
		int ExpTextCat = AllTextCat[26] ;
		int ItemsTextCat = AllTextCat[27] ;
		int[] ReqCreatures = quest.getReqCreatures() ;
		int[] ReqItems = quest.getReqItems() ;
		if (QuestHasCreatures)
		{
			Size size = new Size((int)(0.23*screenSize.x), (int) (0.2*screenSize.y)) ;
			int sy = size.y / 5 ;
			Point WindowPos = new Point((int) (0.4*screenSize.x), (int) (0.6*screenSize.y)) ;
			DrawMenuWindow(WindowPos, size, null, 0, MenuColor[0], ColorPalette[7]) ;
			DP.DrawText(new Point((int) (WindowPos.x + 0.5 * size.x), (int) (WindowPos.y - size.y + 1.1*Utg.TextH(font.getSize()))), "Center", OverallAngle, AllText[ExpTextCat][3], font, ColorPalette[0]) ;
			for (int i = 0 ; i <= ReqCreatures.length - 1 ; i += 1)
			{
				if (-1 < ReqCreatures[i])
				{
					int CreatureType = ReqCreatures[i] ;
					String CreatureName = creatureTypes[CreatureType].getName() ;
					Point Pos1 = new Point((int) (WindowPos.x + 0.05 * size.x), (int) (WindowPos.y - size.y + 1.1*Utg.TextH(font.getSize()) + (i + 1.5)*sy)) ;
					Point Pos2 = new Point((int) (WindowPos.x + Utg.TextL("Creature 299: ", font, G)) + creatureTypes[CreatureType].getSize()[0]/2, (int) (WindowPos.y - size.y + 1.1*Utg.TextH(font.getSize()) + (i + 1.5)*sy)) ;
					DP.DrawText(Pos1, "BotLeft", OverallAngle, CreatureName + ":", font, ColorPalette[0]) ;
					DrawCreature(Pos2, new int[] {creatureTypes[CreatureType].getSize()[0]/2, creatureTypes[CreatureType].getSize()[1]/2}, creatureTypes[CreatureType].getimage(), creatureTypes[CreatureType].getColor()) ;
				}
			}
		}
		if (QuestHasItems)
		{
			Size size = new Size((int) (0.16*screenSize.x), (int) (1.6*Utg.TextH(font.getSize())*(ReqItems.length + 1))) ;
			int sy = size.y / (ReqItems.length + 1) ;
			Point WindowPos = new Point((int) (0.2*screenSize.x), (int) (0.6*screenSize.y)) ;
			DrawMenuWindow(WindowPos, size, null, 0, MenuColor[0], ColorPalette[7]) ;
			DP.DrawText(new Point((int) (WindowPos.x + 0.05 * size.x), (int) (WindowPos.y - size.y + 1.1*Utg.TextH(font.getSize()))), "BotLeft", OverallAngle, AllText[ItemsTextCat][3], font, npc.getColor()) ;
			for (int i = 0 ; i <= ReqItems.length - 1 ; i += 1)
			{
				if (-1 < ReqItems[i])
				{
					String ItemName = items[ReqItems[i]].getName() ;
					Point Pos1 = new Point((int) (WindowPos.x + 0.05 * size.x), (int) (WindowPos.y - size.y + 1.1*Utg.TextH(font.getSize()) + 0.15 * size.y + i*sy)) ;
					DP.DrawText(Pos1, "BotLeft", OverallAngle, ItemName, font, npc.getColor()) ;
				}
			}
		}
	}*/
	/*public void DrawQuestWindow(CreatureTypes[] creatureTypes, Creatures[] creatures, Quests[] quest, Items[] items, String Language, int Continent, int[] Bag, Point MousePos, int[] ActiveQuests, int window, int[] windowLimit, Image QuestImage)
	{
		Font Titlefont = new Font("SansSerif", Font.BOLD, 13) ;
		Font font = new Font("SansSerif", Font.BOLD, 12) ;
		int ContinentCat = AllTextCat[32], QuestCat = AllTextCat[35] ;
		int[] FirstQuestIDPerContinent = new int[] {0, 45, 49, 53, 61, 64, 64} ;
		int MaxNumberOfQuestsPerWindow = 5 ;
		int NumberOfQuestsInWindow = Math.min(MaxNumberOfQuestsPerWindow, ActiveQuests.length - MaxNumberOfQuestsPerWindow*window) ;
		Point Pos = new Point((int)(0.5*screenSize.x), (int)(0.55*screenSize.y)) ;
		int L = QuestImage.getWidth(null), H = QuestImage.getHeight(null) ;
		int Sx = L/10, Sy = H/35 ;
		Color TextColor = ColorPalette[23] ;
		DP.DrawImage(QuestImage, Pos, OverallAngle, new float[] {1, 1}, new boolean[] {false, false}, "Center", 1) ;		// Scroll
		if (Language.equals("P"))
		{
			DP.DrawText(new Point(Pos.x, Pos.y - 2*H/5 + Utg.TextH(font.getSize())), "Center", OverallAngle, AllText[QuestCat][1] + " da " + AllText[ContinentCat][Continent + 1], Titlefont, TextColor) ;				
		}
		else if (Language.equals("E"))
		{
			DP.DrawText(new Point(Pos.x, Pos.y - 2*H/5 + Utg.TextH(font.getSize())), "Center", OverallAngle, AllText[ContinentCat][Continent + 1] + " " + AllText[QuestCat][1], Titlefont, TextColor) ;				
		}
		Point TextPos = new Point(Pos.x - 2*Sx, (int) (Pos.y - 0.34*H)) ;
		DP.DrawText(TextPos, "Center", OverallAngle, AllText[QuestCat][1], Titlefont, TextColor) ;								
		DP.DrawText(new Point(TextPos.x + 2*Sx, TextPos.y), "Center", OverallAngle, AllText[QuestCat][2], Titlefont, TextColor) ;								
		DP.DrawText(new Point(TextPos.x + 4*Sx, TextPos.y), "Center", OverallAngle, AllText[QuestCat][3], Titlefont, TextColor) ;
		for (int q = 0 ; q <= NumberOfQuestsInWindow - 1 ; q += 1)
		{
			int QuestID = ActiveQuests[q + MaxNumberOfQuestsPerWindow*window] ;
			if (-1 < QuestID & QuestID <= FirstQuestIDPerContinent[Continent + 1] - 1)
			{
				Point RectPos = new Point(TextPos.x + 2*Sx, (int) (TextPos.y + 3.5*Sy + q*5*Sy - Utg.TextH(font.getSize())/2)) ;
				DP.DrawRoundRect(RectPos, "Center", new Size(8*Sx, (int)(4.5*Sy)), 2, null, null, true) ;
				DP.DrawRoundRect(RectPos, "Center", new Size(3*Sx, (int)(4.5*Sy)), 2, null, null, true) ;
				DP.DrawText(new Point((int) (TextPos.x - 1*Sx), (int) (TextPos.y + 3.2*Sy + q*5*Sy)), "Center", OverallAngle, "Quest " + QuestID + ": ", font, TextColor) ;	
				for (int j = 0 ; j <= quest[QuestID].getReqCreatures().length - 1 ; j += 1)
				{
					int ReqCreatureID = quest[QuestID].getReqCreatures()[j] ;
					int ReqCreatureAmount = quest[QuestID].getReqCreaturesAmounts()[j] ;
					if (-1 < ReqCreatureID)
					{
						DP.DrawText(new Point((int) (TextPos.x + 2*Sx), (int) (TextPos.y + (j + 2)*Sy + q*5*Sy)), "Center", OverallAngle, creatureTypes[ReqCreatureID].getName() + ": " + quest[QuestID].getCounter()[j] + "/" + ReqCreatureAmount, font, TextColor) ;
					}
				}
				for (int j = 0 ; j <= quest[QuestID].getReqItems().length - 1 ; j += 1)
				{
					if (-1 < quest[QuestID].getReqItems()[j])
					{
						String ItemName = items[quest[QuestID].getReqItems()[j]].getName() ;
						if (0 < Bag[quest[QuestID].getReqItems()[j]])
						{
							TextColor = ColorPalette[3] ;
						}
						Point ItemNamePos = new Point((int) (TextPos.x + 3.5*Sx) + 5, (int) (TextPos.y + 2*Sy + q*5*Sy + j*(Utg.TextH(font.getSize()) + 4.5) - Utg.TextH(font.getSize())/2)) ;
						/*if (Utg.MouseIsInside(MousePos, ItemNamePos, Utg.TextL(ItemName, font, G), Utg.TextH(font.getSize())))
						{
							DP.DrawText(ItemNamePos, "BotLeft", OverallAngle, ItemName, font, TextColor) ;
						}
						else
						{
							DP.DrawTextUntil(ItemNamePos, "BotLeft", OverallAngle, ItemName, font, TextColor, 10, MousePos) ;
						//}
					}
				}
			}
			QuestID += 1 ;
		}
		DrawWindowArrows(new Point(Pos.x, Pos.y + (int)(0.42*H)), L, 0, window, windowLimit[Continent]) ;
	}*/
	/*public void DrawHintsMenu(Point MousePos, int SelectedWindow, int NumberOfHints, Color ClassColor)
	{
		int TextCat = AllTextCat[37] ;
		Font font = new Font("SansSerif", Font.BOLD, 12) ;
		Point Pos = new Point((int) (0.19 * screenSize.x), (int) (0.5 * screenSize.y)) ;
		int L = (int)(0.62 * screenSize.x), H = (int) (0.2 * screenSize.y) ;
		int sy = Utg.TextH(font.getSize()) + 2 ;
		int MaxTextL = L ;
		Point TextPos = new Point(Pos.x + 5, Pos.y - H + Utg.TextH(font.getSize())) ;
		Color TextColor = ColorPalette[5] ;
		DrawMenuWindow(Pos, L, H, null, 0, ClassColor, ColorPalette[7]) ;
		DP.DrawText(new Point(TextPos.x + L/2, TextPos.y), "Center", OverallAngle, AllText[TextCat][1], font, TextColor) ;
		DP.DrawText(new Point(TextPos.x, TextPos.y + H - 2 * Utg.TextH(font.getSize())), "BotLeft", OverallAngle, AllText[TextCat][2], font, TextColor) ;
		DP.DrawText(new Point(TextPos.x + (int)(0.98 * L), TextPos.y + H - 2 * Utg.TextH(font.getSize())), "TopRight", OverallAngle, AllText[TextCat][3], font, TextColor) ;
		DP.DrawText(new Point(Pos.x + L/2, Pos.y - 3 * Utg.TextH(font.getSize())), "Center", OverallAngle, AllText[TextCat][4], font, TextColor) ;
		DP.DrawFitText(new Point(TextPos.x, TextPos.y + 2 * Utg.TextH(font.getSize())), sy, "BotLeft", AllText[TextCat][SelectedWindow + 5], font, MaxTextL, TextColor) ;
		DrawWindowArrows(new Point(Pos.x + L/2, Pos.y - 3 * Utg.TextH(font.getSize())/2), L, 0, SelectedWindow, NumberOfHints - 1) ;
	}*/
	/*public void DrawBestiary(CreatureTypes[] creatureTypes, int[] CreaturesDiscovered, Items[] items, Point MousePos, int window)
	{
		Point Pos = new Point((int)(0.05*screenSize.x), (int)(0.95*screenSize.y)) ;
		int[] NumCreatureWindows = new int[] {6, 6} ;
		int NumberOfCreaturesPerWindow = NumCreatureWindows[0]*NumCreatureWindows[1] ;
		int NumberOfCreaturesInWindow = 0 ;
		if (CreaturesDiscovered != null)
		{
			NumberOfCreaturesInWindow = Math.min(NumberOfCreaturesPerWindow, CreaturesDiscovered.length - NumberOfCreaturesPerWindow*window) ;
		}
		int L = (int)(0.6*screenSize.x), H = (int)(0.6*screenSize.y) ;
		float offset = 5 ;	// Offset from the edges
		float Sx = offset, Sy = offset ;
		int l = (int) ((L - 2*offset - (NumCreatureWindows[0] - 1)*Sx)/NumCreatureWindows[0]), h = (int) (H - 2*offset - (NumCreatureWindows[1] - 1)*Sy)/NumCreatureWindows[1] ;
		int thickness = 2 ;
		DrawMenuWindow(Pos, L, H, AllText[AllTextCat[1]][1], 0, ColorPalette[7], ColorPalette[2]) ;
		DrawWindowArray(NumCreatureWindows, new Point((int) (Pos.x + offset), (int) (Pos.y - H + h + offset)), "BotLeft", l, h, Sx, Sy, thickness, new Color[] {ColorPalette[7], ColorPalette[0]}, NumberOfCreaturesInWindow) ;
		
		if (CreaturesDiscovered != null)
		{
			int SelectedCreature = -1 ;
			for (int cx = 0 ; cx <= NumCreatureWindows[0] - 1 ; cx += 1)
			{
				for (int cy = 0 ; cy <= NumCreatureWindows[1] - 1 ; cy += 1)
				{
					if (cx*NumCreatureWindows[0] + cy + NumberOfCreaturesPerWindow*window < CreaturesDiscovered.length)
					{
						int CreatureID = CreaturesDiscovered[cx*NumCreatureWindows[0] + cy + NumberOfCreaturesPerWindow*window] ;
						Point InitPos = new Point((int) (Pos.x + offset + cx*(l + Sx)), (int) (Pos.y - H + h + offset + cy*(h + Sy))) ;
						if (Utg.isInside(MousePos, InitPos, l, h))
						{
							SelectedCreature = CreatureID ;
						}
						DrawCreature(new Point((int) (InitPos.x + 0.5*l), (int) (InitPos.y - 0.5*h)), new int[] {creatureTypes[CreatureID].getSize()[0]/2, creatureTypes[CreatureID].getSize()[1]/2}, creatureTypes[CreatureID].getimage(), creatureTypes[CreatureID].getColor()) ;
					}
				}
			}
			if (-1 < SelectedCreature)
			{
				DrawCreatureInfoWindow(new Point(Pos.x + L, Pos.y), creatureTypes[SelectedCreature], items) ;
			}
		}
	}*/
	
	
	/* NPC windows */
	/*public void DrawCrafting(int SelectedMenu, int SelectedItem, int SelectedWindow, Items[] items, int[][] Ingredients, int[][] IngredientAmounts, int[][] Products, int[][] ProductAmounts, int[] AvailableProducts, Point MousePos)
	{
		Font font = new Font("SansSerif", Font.BOLD, 14) ;
		int NmaxItemsPerWindow = 10 ;
		Point Pos = new Point((int) (0.1 * screenSize.x), (int) (0.88 * screenSize.y)) ;
		Size size = new Size((int) (0.35 * screenSize.x), (int) (0.45 * screenSize.y)) ;
		int sy = (int) (0.1 * size.y) ;
		Color TextColor[] = new Color[Products.length] ;
		Arrays.fill(TextColor, ColorPalette[4]) ;
		DrawMenuWindow(Pos, size, null, 0, ColorPalette[7], MenuColor[0]) ;
		int NitensOnWindow = Math.min(Products.length - SelectedWindow * NmaxItemsPerWindow, NmaxItemsPerWindow) ;
		for (int i = 0 ; i <= NitensOnWindow - 1 ; i += 1)
		{
			int ItemID = i + SelectedWindow * NmaxItemsPerWindow ;
			if (0 < Products[ItemID].length)
			{
				Point TextPos = new Point((int) (Pos.x + 0.03 * size.x), (int) (Pos.y - 0.97 * size.y + Utg.TextH(font.getSize())) + i * sy) ;
				String ProductName = items[Products[ItemID][0]].getName() ;
				String ProductAmount = String.valueOf(ProductAmounts[ItemID][0]) ;
				if (Utg.ArrayContains(AvailableProducts, Products[ItemID][0]))
				{
					TextColor[ItemID] = ColorPalette[23] ;
				}
				TextColor[SelectedItem] = ColorPalette[3] ;
				DP.DrawText(TextPos, "BotLeft", OverallAngle, ProductName, font, TextColor[ItemID]) ;
				DP.DrawText(new Point((int) (TextPos.x + 0.9 * size.x), TextPos.y), "TopRight", OverallAngle, ProductAmount, font, TextColor[ItemID]) ;
			}
		}
		DrawWindowArrows(Pos, size.x, 0, SelectedWindow, Math.max(0, (Products.length - 1) / NmaxItemsPerWindow)) ;
	}*/
	/*public void DrawShopping(Point WindowPos, Size size, int SelectedItem, int SelectedWindow, Point[] textpos, String mode, Items[] items, int[] ItemsOnSale, Point MousePos, Image CoinIcon)
	{
		int ItemsPerWindow = 10 ;
		int offset = 6 ;
		Font font = new Font("SansSerif", Font.BOLD, CoinIcon.getHeight(null) - 2) ;
		int Sy = (int) Utg.spacing(size.y, ItemsPerWindow, font.getSize(), offset) ;
		Color TextColor[] = new Color[ItemsOnSale.length] ;
		Arrays.fill(TextColor, ColorPalette[1]) ;
		TextColor[SelectedItem] = ColorPalette[3] ;
		DrawMenuWindow(WindowPos, size, null, 0, ColorPalette[0], ColorPalette[0]) ;
		for (int i = 0 ; i <= Math.min(ItemsOnSale.length - ItemsPerWindow * SelectedWindow, ItemsPerWindow) - 1 ; i += 1)
		{
			int ItemID = i + ItemsPerWindow * SelectedWindow ;
			int TextL = Utg.TextL(items[ItemsOnSale[ItemID]].getName(), font, G) ;
			if (Utg.isInside(MousePos, textpos[i], new Size(TextL, (int) (0.99 * Sy))))
			{
				TextColor[i] = ColorPalette[3] ;
			}
			if (i + ItemsPerWindow * SelectedWindow == SelectedItem)
			{
				DP.DrawRect(new Point(textpos[i].x - 2, textpos[i].y), "CenterLeft", new Size(TextL, font.getSize()), DrawPrimitives.StdThickness, ColorPalette[18], ColorPalette[9], false) ;			
			}
			DP.DrawText(textpos[i], "CenterLeft", OverallAngle, items[ItemsOnSale[ItemID]].getName(), font, TextColor[i]) ;
			String ItemPrice = null ;
			if (mode.equals("Selling"))
			{
				ItemPrice = String.valueOf((int)(0.7 * items[ItemsOnSale[ItemID]].getPrice())) ;
			}
			else if (mode.equals("Buying"))
			{
				ItemPrice = String.valueOf(items[ItemsOnSale[ItemID]].getPrice()) ;
			}
			DP.DrawText(new Point((int) (textpos[i].x + size.x - 13 - CoinIcon.getWidth(null)), textpos[i].y), "CenterRight", OverallAngle, ItemPrice, font, ColorPalette[18]) ;		
			DP.DrawImage(CoinIcon, new Point((int) (textpos[i].x + size.x - 10), textpos[i].y), "CenterRight") ;
		}	
		//DP.DrawLine(new int[] {(int) (Pos.x + Sx), (int) (Pos.x + Sx)}, new int[] {(int) (Pos.y), (int) (Pos.y - H)}, 2, ColorPalette[9]) ;		
		DrawWindowArrows(new Point((int) (WindowPos.x + 0.85 * size.x), WindowPos.y), size.x, 0, SelectedWindow, Math.max(0, (ItemsOnSale.length - 1) / ItemsPerWindow)) ;
	}*/
	/*public void DrawElementalNPCScreen(int SelectedItem, Items[] items, int[] Ingredients)
	{
		Font font = new Font("SansSerif", Font.BOLD, 20) ;
		Point Pos = new Point((int)(0.1*screenSize.x), (int)(0.88*screenSize.y)) ;
		Size size = new Size((int)(0.5*screenSize.x), (int)(0.5*screenSize.y)) ;
		Point TextPos = new Point((int)(Pos.x + 0.02*screenSize.x), (int)(Pos.y - size.y + font.getSize())) ;
		int sy = (int)(0.05*screenSize.y) ;
		Color TextColor[] = new Color[Ingredients.length] ;
		for (int i = 0 ; i <= Ingredients.length - 1 ; ++i)
		{
			TextColor[i] = ColorPalette[6] ;
		}
		TextColor[SelectedItem] = ColorPalette[3] ;
		DrawMenuWindow(Pos, size, null, 0, ColorPalette[20], ColorPalette[20]) ;
		for (int i = 0 ; i <= Ingredients.length - 1 ; ++i)
		{
			DP.DrawText(new Point(TextPos.x, TextPos.y + i*sy), "BotLeft", OverallAngle, items[Ingredients[i]].getName(), font, TextColor[i]) ;	
		}
	}*/
	/*public void DrawSpellsTree(Player player, Spells[] spells, Point MousePos, int SelectedSpell, Icon SpellsTreeIcon)
	{
		int ClassesCat = AllTextCat[4], ProClassesCat = AllTextCat[5] ;
		Font font = new Font("SansSerif", Font.BOLD, 10) ;
		Font Largefont = new Font("SansSerif", Font.BOLD, 12) ;
		Point Pos = new Point((int)(0.1*screenSize.x), (int)(0.9*screenSize.y)) ;
		Size Size = new Size((int)(0.7*screenSize.x), (int)(0.66*screenSize.y)) ;
		int TabL = Size.x / 20, TabH = Size.y / 3 ;
		Size size = new Size((int)(0.2*screenSize.x), (int)(0.1*screenSize.y)) ;
		int Sx = size.x / 10, Sy = size.y / 10 ;
		Color[] SpellsColors = null, TabColor = new Color[] {ColorPalette[7], ColorPalette[7]}, TabTextColor = new Color[] {ColorPalette[5], ColorPalette[5]} ;
		int tab = 0 ;
		int[] Sequence = player.GetSpellSequence() ;
		int[] ProSequence = player.GetProSpellSequence() ;
		int NumberOfSpells = player.GetNumberOfSpells() ;
		int NumberOfProSpells = 0 ;
		if (NumberOfSpells - 1 < SelectedSpell)
		{
			tab = 1 ;
			NumberOfProSpells = player.GetNumberOfProSpells() ;
			Sequence = ProSequence ;
		}

		Color[] color = new Color[spells.length] ;
		for (int i = 0 ; i <= spells.length - 1 ; i += 1)
		{
			color[i] = ColorPalette[4] ;
			if(UtilS.SpellIsAvailable(player, spells, i))
			{
				color[i] = ColorPalette[5] ;
			}
		}
		
		// Main window
		DP.DrawRoundRect(Pos, "BotLeft", Size, 1, MenuColor[0], MenuColor[0], true) ;
		TabColor[tab] = MenuColor[0] ;
		TabTextColor[tab] = ColorPalette[3] ;
		DP.DrawRoundRect(new Point(Pos.x, Pos.y - 2*TabH), "BotRight", new Size(TabL, TabH), 1, TabColor[0], ColorPalette[8], true) ;
		DP.DrawText(new Point(Pos.x + TabL/2 + UtilG.TextH(font.getSize())/2, Pos.y - 2*TabH - TabH/2), "Center", 90, AllText[ClassesCat][player.getJob() + 1], Largefont, TabTextColor[0]) ;
		if (0 < player.getProJob())
		{
			DP.DrawRoundRect(new Point(Pos.x, Pos.y - TabH), "BotRight", new Size(TabL, TabH), 1, TabColor[1], ColorPalette[8], true) ;	
			DP.DrawText(new Point(Pos.x + TabL/2 + UtilG.TextH(font.getSize())/2, Pos.y - 3*TabH/2), "Center", 90, AllText[ProClassesCat][player.getProJob() + 2*player.getJob()], Largefont, TabTextColor[1]) ;
		}
		
		// Organogram
		//String[] SkillNames = null ;
		String[][] SpellNames = new String[2][] ;
		String[] SpellLevels = null ;
		if (tab == 0)
		{
			for (int spell = 0 ; spell <= NumberOfSpells - 1 ; spell += 1)
			{
				SpellNames[0] = UtilG.AddElem(SpellNames[0], spells[spell].getName()) ;
				SpellNames[1] = UtilG.AddElem(SpellNames[1], spells[spell].getType()) ;
				SpellLevels = UtilG.AddElem(SpellLevels, String.valueOf(player.getSpell()[spell])) ;
				SpellsColors = UtilG.AddElem(SpellsColors, color[spell]) ;
			}
		}
		if (tab == 1)
		{
			for (int spell = NumberOfSpells ; spell <= NumberOfSpells + NumberOfProSpells - 1 ; spell += 1)
			{
				SpellNames[0] = UtilG.AddElem(SpellNames[0], spells[spell].getName()) ;
				SpellNames[1] = UtilG.AddElem(SpellNames[1], spells[spell].getType()) ;
				SpellLevels = UtilG.AddElem(SpellLevels, String.valueOf(player.getSpell()[spell])) ;
				SpellsColors = UtilG.AddElem(SpellsColors, color[spell]) ;
			}
		}
		SpellsColors[SelectedSpell - tab*NumberOfSpells] = ColorPalette[3] ;
		DrawOrganogram(Sequence, new Point(Pos.x, Pos.y - Size.y), Sx, Sy, size, SpellNames, SpellLevels, SpellsTreeIcon, font, SpellsColors, MousePos) ;
		
		// Skill info
		int TextmaxL = Size.x / 5, sx = 10, sy = UtilG.TextH(font.getSize()) + 2 ;
		String Description = spells[SelectedSpell].getInfo()[0], Effect = spells[SelectedSpell].getInfo()[1] ;
		DP.DrawRoundRect(new Point(Pos.x, Pos.y - Size.y), "BotLeft", new Size(Size.x, Size.y / 4), 1, ColorPalette[7], ColorPalette[7], true) ;
		DP.DrawFitText(new Point(Pos.x + sx, Pos.y - Size.y - Size.y / 5), sy, "BotLeft", Effect, font, TextmaxL, player.getColors()[0]) ;
		DP.DrawFitText(new Point(Pos.x + sx, Pos.y - Size.y - Size.y / 10), sy, "BotLeft", Description, font, TextmaxL - 6, player.getColors()[0]) ;		
		DP.DrawText(new Point(Pos.x + Size.x, Pos.y), "TopRight", OverallAngle, "Pontos: " +  player.getSkillPoints(), font, player.getColors()[0]) ;		
	}*/
	

	/* Battle animations */
	public void DrawDamageAnimation(Point Pos, int damage, int effect, int counter, int duration, int damageAnimation, Color color)
	{
		Font font = new Font("SansSerif", Font.BOLD, 18) ;
		float anirate = counter / (float) duration ;
		int dx = 0, dh = 0 ;
		if (damageAnimation == 1)
		{
			dh = (int) (10 * anirate) ;
		}
		if (damageAnimation == 2)
		{
			dx = (int) (40 * Math.pow(anirate, 2)) ;
			dh = (int) (10 * anirate) ;
		}
		if (damageAnimation == 3)
		{
			dx = (int) (Math.pow(40 * anirate, 2)) ;
			dh = (int) (10 * anirate) ;
		}
		if (effect == 1)		// Crit
		{
			DP.DrawText(new Point(Pos.x + dx, Pos.y - dh), "Center", OverallAngle, String.valueOf(UtilG.Round(damage, 1)) + "!", font, color) ;
		}
		else if (effect == 2)	// Miss
		{
			DP.DrawText(new Point(Pos.x + dx, Pos.y - dh), "Center", OverallAngle, "Miss", font, color) ;		
		}
		else if (effect == 3)	// Block
		{
			DP.DrawText(new Point(Pos.x + dx, Pos.y - dh), "Center", OverallAngle, "Block", font, ColorPalette[5]) ;	
		}
		else
		{
			DP.DrawText(new Point(Pos.x + dx, Pos.y - dh), "Center", OverallAngle, String.valueOf(UtilG.Round(damage, 1)), font, color) ;	
		}
	}
	public void DrawSkillNameAnimation(Point Pos, String SkillName, Color color)
	{
		Font font = new Font("SansSerif", Font.BOLD, 18) ;
		DP.DrawText(Pos, "Center", OverallAngle, SkillName, font, color) ;
	}
	/*public void DrawStatusDamageAnimation(Point Pos, float[] damage, int[] status)
	{
		Font font = new Font("SansSerif", Font.BOLD, 18) ;
		if (0 < status[0])
		{
			DP.DrawText(Pos, "BotLeft", OverallAngle, String.valueOf(Utg.Round(damage[0], 1)), font, ColorPalette[6]) ;
		}
		if (0 < status[1])
		{
			DP.DrawText(new Point(Pos.x, Pos.y + 20), "BotLeft", OverallAngle, String.valueOf(Utg.Round(damage[1], 1)), font, ColorPalette[3]) ;
		}
	}*/
	/*public void ShowEffectsAndStatusAnimation(Point Pos, int mirror, int[] offset, Image[] IconImages, int[] effect, boolean[] status)
	{
		// effect 0: Stun, 1: Block, 2: Blood, 3: Poison, 4: Silence
		int Sy = (int)(1.1 * IconImages[0].getHeight(null)) ;
		if (status[0])	// Defending
		{
			int ImageW = IconImages[0].getWidth(null) ;
			Point ImagePos = new Point(Pos.x + mirror * (ImageW + offset[0]), Pos.y - offset[1]) ;
			DP.DrawImage(IconImages[0], ImagePos, "Center") ;
		}
		if (0 < effect[0])	// Stun
		{
			Point ImagePos = new Point(Pos.x, Pos.y + mirror * offset[1]) ;
			DP.DrawImage(IconImages[1], ImagePos, "Center") ;
		}
		for (int e = 1 ; e <= 4 - 1 ; e += 1)	// Block, blood, poison and silence
		{
			if (0 < effect[e])
			{
				int ImageW = IconImages[e + 1].getWidth(null) ;
				Point ImagePos = new Point(Pos.x + mirror * (ImageW + offset[0]), Pos.y - offset[1] + Sy) ;
				DP.DrawImage(IconImages[e + 1], ImagePos, "Center") ;
				Sy += IconImages[e + 1].getHeight(null) + 2 ;
			}
		}
	}	*/
	
	
	/* Animations */
	public void ChestRewardsAnimation(Items[] items, int counter, int duration, int[] ItemRewards, int[] GoldRewards, Color TextColor, Image CoinIcon)
	{
		/*Font font = new Font("SansSerif", Font.BOLD, 20) ;
		int TextCat = AllTextCat[29] ;
		Point Pos = new Point((int)(0.3*screenSize.x), (int)(0.8*screenSize.y)) ;
		Size size = new Size((int)(0.5*screenSize.x), (int)(0.6*screenSize.y)) ;
		int Sy = size.y / 12 ;
		Point TextPos = new Point(Pos.x + (int)(0.05 * size.x), Pos.y - (int)(0.95 * size.y)) ;
		DrawMenuWindow(Pos, size, null, 0, ColorPalette[18], ColorPalette[7]) ;
		DP.DrawText(TextPos, "BotLeft", OverallAngle, AllText[TextCat][1], font, TextColor) ;
		if (counter < duration/3)
		{
			DP.DrawText(new Point(TextPos.x, TextPos.y + Sy), "BotLeft", OverallAngle, AllText[TextCat][2], font, TextColor) ;			
		} else if (counter < duration)
		{
			DP.DrawText(new Point(TextPos.x, TextPos.y + Sy), "BotLeft", OverallAngle, AllText[TextCat][2], font, TextColor) ;			
			for (int i = 0 ; i <= ItemRewards.length - 1 ; i += 1)
			{
				if (duration/3 + 2*duration/30*i < counter % duration)
				{
					DP.DrawText(new Point(TextPos.x, TextPos.y + (i + 2)*Sy), "BotLeft", OverallAngle, items[ItemRewards[i]].getName(), font, TextColor) ;															
				}
			}
			for (int i = 0 ; i <= GoldRewards.length - 1 ; i += 1)
			{
				if (duration/3 + 2*duration/30*i < counter % duration)
				{
					DP.DrawText(new Point(TextPos.x, TextPos.y + (i + 2 + ItemRewards.length)*Sy), "BotLeft", OverallAngle, String.valueOf(GoldRewards[i]), font, ColorPalette[18]) ;															
					DP.DrawImage(CoinIcon, new Point((int) (TextPos.x + 1.05*UtilG.TextL(String.valueOf(GoldRewards[i]), font, G)), TextPos.y + (i + 2 + ItemRewards.length)*Sy + UtilG.TextH(font.getSize())/2), OverallAngle, new float[] {1, 1}, new boolean[] {false, false}, "BotLeft", 1) ;
				}
			}
		}
		*/
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
				DP.DrawText(new Point(Pos.x + (int)(0.05*screenSize.x) - L/2, Pos.y - (int)(0.05*screenSize.y)), "BotLeft", OverallAngle, AllText[TextCat][1] + " " + CollectibleName[CollectibleType] + ".", font, CollectibleColor[CollectibleType]) ;
			} else if (counter % (3*DotsDelay) < 2*DotsDelay)
			{
				DP.DrawText(new Point(Pos.x + (int)(0.05*screenSize.x) - L/2, Pos.y - (int)(0.05*screenSize.y)), "BotLeft", OverallAngle, AllText[TextCat][1] + " " + CollectibleName[CollectibleType] + "..", font, CollectibleColor[CollectibleType]) ;
			} else
			{
				DP.DrawText(new Point(Pos.x + (int)(0.05*screenSize.x) - L/2, Pos.y - (int)(0.05*screenSize.y)), "BotLeft", OverallAngle, AllText[TextCat][1] + " " + CollectibleName[CollectibleType] + "...", font, CollectibleColor[CollectibleType]) ;				
			}	
			DrawTimeBar(Pos, counter, delay, L, new int[] {0, 0}, "Right", "Horizontal", CollectibleColor[CollectibleType]) ;
		}
		else
		{
			DP.DrawText(new Point(Pos.x + (int)(0.05*screenSize.x) - L/2, Pos.y - (int)(0.05*screenSize.y)), "BotLeft", OverallAngle, Message, font, CollectibleColor[CollectibleType]) ;			
		}
		*/
	}
	public void TentAnimation(Point Pos, int counter, int delay, Image TentImage)
	{
		DP.DrawImage(TentImage, Pos, "Center") ;
	}
	public void AttackAnimation(Point PlayerPos, Point CreaturePos, int[] CreatureSize, int effect, String elem, int counter, int duration)
	{
		if (effect == 0)
		{
			Point Pos = new Point(CreaturePos.x - CreatureSize[0]/2, CreaturePos.y + CreatureSize[1]/2) ;
			DP.DrawLine(new int[] {Pos.x, Pos.x + counter*50/duration}, new int[] {Pos.y - 15, Pos.y - 15 - counter*50/duration}, 1, ColorPalette[9]) ;
			DP.DrawLine(new int[] {Pos.x, Pos.x + counter*50/duration}, new int[] {Pos.y, Pos.y - counter*50/duration}, 1, ColorPalette[9]) ;
			DP.DrawLine(new int[] {Pos.x, Pos.x + counter*50/duration}, new int[] {Pos.y + 15, Pos.y + 15 - counter*50/duration}, 1, ColorPalette[9]) ;
		}
		else if (effect == 1 & -1 < UtilS.ElementID(elem))
		{
			DP.DrawImage(ElementImages[UtilS.ElementID(elem)], new Point(PlayerPos.x + (CreaturePos.x - PlayerPos.x)*counter/duration, PlayerPos.y + (CreaturePos.y - PlayerPos.y)*counter/duration), 0, new float[] {(float) 1.5, (float) 1.5}, new boolean[] {false, false}, "Center", 1) ;
		}
		else if (effect == 2)
		{
			float angle = (float) Math.atan((CreaturePos.y - PlayerPos.y)/(CreaturePos.x - PlayerPos.x)) ;
			if (CreaturePos.x < PlayerPos.x)
			{
				angle = (float) (angle*180/Math.PI) ;
			}
			else
			{
				angle = (float) (angle*180/Math.PI - 90) ;
			}
			DP.DrawImage(Items.EquipImage[7], new Point(PlayerPos.x + (CreaturePos.x - PlayerPos.x)*counter/duration, PlayerPos.y + (CreaturePos.y - PlayerPos.y)*counter/duration), angle, new float[] {1, 1}, new boolean[] {false, false}, "Center", 1) ;
		}
	}
	/*public void GainItemAnimation(Items[] items, int[] ItemIDs, int counter, int delay, Color textColor)
	{
		int TextCat = AllTextCat[10] ;
		Font font = new Font("SansSerif", Font.BOLD, 14) ;
		Point Pos = new Point((int) (0.5*screenSize.x), (int) (0.25*screenSize.y)) ;
		int[] ValidItemIDs = Utg.ArrayWithValuesGreaterThan(ItemIDs, -1) ;
		Size size = new Size((int) (1.2*Utg.TextL(items[ItemIDs[0]].getName(), font, G)), (int) (3.5*Utg.TextH(font.getSize())*ValidItemIDs.length)) ;
		int sy = (int) (1.2*Utg.TextH(font.getSize())) ;
		if (counter < delay)
		{
			DrawMenuWindow(new Point(Pos.x - size.x / 2, Pos.y + size.y / 2), size, null, 0, ColorPalette[8], ColorPalette[7]) ;
			DP.DrawText(new Point(Pos.x, Pos.y - size.y / 2 + sy), "Center", OverallAngle, AllText[TextCat][3], font, textColor) ;
			for (int i = 0 ; i <= ValidItemIDs.length - 1 ; i += 1)
			{
				DP.DrawText(new Point(Pos.x, Pos.y - size.y / 2 + (i + 2)*sy), "Center", OverallAngle, items[ValidItemIDs[i]].getName(), font, textColor) ;
				
			}
		}
	}*/
	public void WinAnimation(int counter, int duration, String[] ItemsObtained, Color textColor)
	{
		/*int WinCat = AllTextCat[10] ;
		Font font = new Font("SansSerif", Font.BOLD, 16) ;
		Point Pos = new Point((int)(0.25 * screenSize.x), (int)(0.6 * screenSize.y)) ;
		Size size = new Size((int)(0.3 * screenSize.x),  (int)(0.4 * screenSize.y)) ;
		int Sy = size.y / 10 ;
		Point TextPos = new Point(Pos.x + (int)(0.05 * size.x), Pos.y - size.y + UtilG.TextH(font.getSize()) + 10) ;
		DrawMenuWindow(Pos, size, null, 0, MenuColor[1], ColorPalette[7]) ;
		DP.DrawText(TextPos, "BotLeft", OverallAngle, AllText[WinCat][2], font, textColor) ;
		if (counter < duration / 3)
		{
			DP.DrawText(new Point(TextPos.x, TextPos.y + Sy), "BotLeft", OverallAngle, AllText[WinCat][3], font, textColor) ;			
		}
		else if (counter < duration)
		{
			DP.DrawText(new Point(TextPos.x, TextPos.y + Sy), "BotLeft", OverallAngle, AllText[WinCat][3], font, textColor) ;			
			for (int i = 0 ; i <= ItemsObtained.length - 1 ; ++i)
			{
				if (duration / 3 + duration * i * 2 / 3 / Math.max(1, ItemsObtained.length - 1) < counter % duration)
				{
					DP.DrawText(new Point(TextPos.x, TextPos.y + (i + 2) * Sy), "BotLeft", OverallAngle, ItemsObtained[i], font, textColor) ;								
				}
			}
		}
		*/
	}
	public void PlayerLevelUpAnimation(int counter, int duration, float[] AttributeIncrease, int playerLevel, Color textColor)
	{
		/*int LevelUpCat = AllTextCat[10], AtributosCat = AllTextCat[6] ;
		Font font = new Font("SansSerif", Font.BOLD, 16) ;
		Size size = new Size((int)(0.4*screenSize.x), (int)(0.4*screenSize.y)) ;
		int Sy = size.y / 10 ;
		Point Pos = new Point((int)(0.25*screenSize.x), (int)(0.6*screenSize.y)) ;
		Point TextPos = new Point(Pos.x + (int)(0.05 * size.x), Pos.y - size.y + UtilG.TextH(font.getSize()) + 10) ;
		DrawMenuWindow(Pos, size, null, 0, MenuColor[1], ColorPalette[7]) ;
		DP.DrawText(TextPos, "BotLeft", OverallAngle, AllText[LevelUpCat][1], font, textColor) ;
		if (counter < duration)
		{
			DP.DrawText(new Point(TextPos.x, TextPos.y + Sy), "BotLeft", OverallAngle, AllText[AtributosCat][1] + " " + playerLevel, font, ColorPalette[6]) ;
			if (duration/3 < counter)
			{				
				for (int i = 0 ; i <= AttributeIncrease.length - 1 ; i += 1)
				{
					if (duration/3 + duration*i*2/3/Math.max(1, AttributeIncrease.length - 1) < counter)
					{
						DP.DrawText(new Point(TextPos.x, TextPos.y + (i + 2)*Sy), "BotLeft", OverallAngle, AllText[AtributosCat][i + 2] + " + " + AttributeIncrease[i], font, textColor) ;								
					}
				}
			}	
		}
		*/
	}
	public void PetLevelUpAnimation(Pet pet, int counter, int duration, float[] AttributeIncrease)
	{
		/*Font font = new Font("SansSerif", Font.BOLD, 20) ;
		int AttCat = AllTextCat[6], WinCat = AllTextCat[10] ;
		Point Pos = new Point((int)(0.25*screenSize.x), (int)(0.8*screenSize.y)) ;
		Size size = new Size((int)(0.5*screenSize.x), (int)(0.6*screenSize.y)) ;
		int Sy = size.y / 10 ;
		DrawMenuWindow(Pos, size, null, 0, ColorPalette[pet.getJob()], ColorPalette[7]) ;
		DP.DrawText(new Point(Pos.x + (int)(0.05 * size.x), Pos.y - (int)(0.95 * size.y)), "BotLeft", OverallAngle, AllText[WinCat][1], font, pet.getColor()) ;
		if (counter % duration < duration/3)
		{
			DP.DrawText(new Point(Pos.x + (int)(0.05 * size.x), Pos.y - (int)(0.95 * size.y) + Sy), "BotLeft", OverallAngle, AllText[AttCat][1] + " " + pet.getLevel(), font, pet.getColor()) ;		
		} else if (counter % duration < 2*duration/3)
		{
			DP.DrawText(new Point(Pos.x + (int)(0.05 * size.x), Pos.y - (int)(0.95 * size.y) + Sy), "BotLeft", OverallAngle, AllText[AttCat][1] + " " + pet.getLevel(), font, pet.getColor()) ;					
			for (int i = 0 ; i <= AttributeIncrease.length - 1 ; ++i)
			{
				if (duration/3 + duration/15*i < counter % duration)
				{
					DP.DrawText(new Point(Pos.x + (int)(0.05 * size.x), Pos.y - (int)(0.95 * size.y) + (i + 2)*Sy), "BotLeft", OverallAngle, AllText[AttCat][i + 2] + " + " + AttributeIncrease[i], font, pet.getColor()) ;								
				}
			}
		}
		*/
	}
	public void SailingAnimation(Player player, NPCs npc, Maps[] maps, String Destination, int counter, int Duration, Image BoatImage)
	{
		/*int Step = player.getStep()/2 ;
		int NPCLength = npc.getImage().getWidth(null), NPCHeight = npc.getImage().getHeight(null) ;
		if (Destination.equals("Island"))
		{
			Point InitialPos = new Point(Step, (int)(0.5*screenSize.y)) ;	
			Point Pos = new Point(Math.abs((InitialPos.x + Step*counter)) % screenSize.x, InitialPos.y) ;
			if (Pos.x + Step < screenSize.x)
			{
				DP.DrawImage(BoatImage, Pos, OverallAngle, new float[] {1, 1}, new boolean[] {false, false}, "BotLeft", 1) ;
				DP.DrawImage(npc.getImage(), new Point(Pos.x + NPCLength, (int) (Pos.y - 0.5*NPCHeight)), OverallAngle, new float[] {(float)0.5, (float)0.5}, new boolean[] {false, false}, "Center", 1) ;
				player.setPos(new Point(Pos.x + Step, Pos.y)) ;
			}
			else
			{
				player.setPos(InitialPos) ;
				player.setMap(maps[player.getMap().getid() + 1]) ;
				if (player.getMap().getid() == 65)
				{
					player.setPos(new Point(20, 500)) ;
					player.setMap(maps[40]) ;
					/if (MusicIsOn)
					///{
					//	UtilGeral.SwitchMusic(Music[11], Music[MusicInMap[player.getMap()]]) ;
					//}
				}
			}
		}
		else if (Destination.equals("Forest"))
		{
			Point InitialPos = new Point(screenSize.x - Step, (int)(0.5*screenSize.y)) ;
			Point Pos = new Point((InitialPos.x - Step*(counter % (screenSize.x/Step - 1))) % screenSize.x, InitialPos.y) ;
			if (0 < Pos.x - Step)
			{
				DP.DrawImage(BoatImage, Pos, OverallAngle, new float[] {(float)1, (float)1}, new boolean[] {false, false}, "BotLeft", 1) ;
				DP.DrawImage(npc.getImage(), new Point(Pos.x + NPCLength, (int) (Pos.y - 0.5*NPCHeight)), OverallAngle, new float[] {(float)0.5, (float)0.5}, new boolean[] {false, false}, "Center", 1) ;
				player.setPos(new Point(Pos.x - Step, Pos.y)) ;
			}
			else
			{
				player.setPos(InitialPos) ;
				player.setMap(maps[player.getMap().getid() - 1]) ;
				if (player.getMap().getid() == 60)
				{
					player.setPos(new Point(640, 500)) ;
					player.setMap(maps[13]) ;
					//if (MusicIsOn)
					//{
					//	UtilGeral.SwitchMusic(Music[11], Music[MusicInMap[player.getMap()]]) ;
					//}
				}
			}
		}
		*/
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
		UtilG.PlayGif(Pos, FishingGif, DP) ;
	}
	public void PterodactileAnimation(int counter, int duration, Image SpeakingBubbleImage, Image PterodactileImage)
	{
		/*Point InitialPos = new Point(screenSize.x + PterodactileImage.getWidth(null)/2, (int)(0.25*screenSize.y)) ;
		Font font = new Font("SansSerif", Font.BOLD, 15) ;
		int TextCat = AllTextCat[31] ;
		if (counter < 0.25*duration)
		{
			InitialPos.x += -0.5*(screenSize.x + PterodactileImage.getWidth(null))*counter/(0.25*duration) ;
			InitialPos.y += 0.25*screenSize.x*counter/(0.25*duration) ;
		} else if (counter < 0.5*duration)
		{
			InitialPos.x += -0.5*(screenSize.x + PterodactileImage.getWidth(null)) ;
			InitialPos.y += 0.25*screenSize.x ;
			DrawSpeech(new Point(InitialPos.x - (int)(0.07*screenSize.x), InitialPos.y - (int)(0.09*screenSize.y)), AllText[TextCat][1], font, PterodactileImage, SpeakingBubbleImage, ColorPalette[19]) ;
		} else if (counter < 0.75*duration)
		{
			InitialPos.x += -0.5*(screenSize.x + PterodactileImage.getWidth(null)) ;
			InitialPos.y += 0.25*screenSize.x ;
			DrawSpeech(new Point(InitialPos.x - (int)(0.07*screenSize.x), InitialPos.y - (int)(0.09*screenSize.y)), AllText[TextCat][2], font, PterodactileImage, SpeakingBubbleImage, ColorPalette[19]) ;
		} else if (counter < duration)
		{
			InitialPos.x += -0.5*(screenSize.x + PterodactileImage.getWidth(null))*(counter - 0.75*duration)/(0.25*duration) - 0.5*(screenSize.x + PterodactileImage.getWidth(null)) ;
			InitialPos.y += -0.25*screenSize.x*(counter - 0.75*duration)/(0.25*duration) + 0.25*screenSize.x ;
		}
		DP.DrawImage(PterodactileImage, InitialPos, 1, new float[] {1, 1}, new boolean[] {false, false}, "Center", 1) ;
		*/
	}
	/*public void RainAnimation(int counter)
	{
		float x0 = (float)0*screenSize.x, y0 = -20 ;
		int delay = 10, duration = 1000 ;
		int thickness = 2 ;
		float h = 20, sx = (float)0.02*screenSize.x, sy = 0 ;
		float y ;
		counter += -60000 ;
		if (counter <= duration)
		{
			for (int i = 0 ; i <= 35 ; ++i)
			{
				y = y0 + (i % 2 + i + 0*counter/delay)*(sy + h) + ((counter/delay) % 2)*h ;
				for (int j = 0 ; j <= 50 ; ++j)
				{
					DP.DrawLine(new int[] {(int)(x0 + j*sx), (int)(x0 + j*sx)}, new int[] {(int)(y), (int)(y + h)}, thickness, ColorPalette[5]) ;
				}
				y = y0 + ((i + 1) % 2 + i + 0*counter/delay)*(sy + h) + ((counter/delay) % 2)*h ;
				for (int j = 0 ; j <= 50 ; ++j)
				{
					DP.DrawLine(new int[] {(int)(x0 + j*sx + 0.5*sx), (int)(x0 + j*sx + 0.5*sx)}, new int[] {(int)(y), (int)(y + h)}, thickness, ColorPalette[5]) ;
				}
			}
		}
	}*/
	public void CrazyArrowAnimation(int map, int counter, int looptime, Image CrazyArrowImage)
	{
		Point InitialPos = new Point(0, 0) ;
		float angle = -1 ;
		float dx = 0, dh = 0 ;
		if (map == 0)
		{
			InitialPos = new Point((int) (0.5*screenSize.x), (int) (0.5*screenSize.y)) ;
			angle = 0 ;
			dx = (float) (0.005*screenSize.x) ;
		}
		else if (map == 1)
		{
			InitialPos = new Point((int) (0.5*screenSize.x), (int) (0.5*screenSize.y)) ;
			angle = 90 ;
			dh = (float) (0.005*screenSize.y) ;
		}
		else if (map == 2)
		{
			InitialPos = new Point((int) (0.5*screenSize.x), (int) (0.5*screenSize.y)) ;
			angle = 180 ;
			dx = (float) (0.005*screenSize.x) ;
		}
		else if (map == 3)
		{
			InitialPos = new Point((int) (0.5*screenSize.x), (int) (0.8*screenSize.y)) ;
			angle = 180 ;
			dx = (float) (0.005*screenSize.x) ;
		}
		else if (map == 4)
		{
			InitialPos = new Point((int) (0.5*screenSize.x), (int) (0.5*screenSize.y)) ;
			angle = 90 ;
			dh = (float) (0.005*screenSize.y) ;
		}
		dx = dx*UtilS.UpAndDownCounter(counter, looptime) ;
		dh = dh*UtilS.UpAndDownCounter(counter, looptime) ;
		DP.DrawImage(CrazyArrowImage, new Point((int) (InitialPos.x + dx), (int) (InitialPos.y + dh)), angle, new float[] {1, 1}, new boolean[] {false, false}, "Center", 1) ;
	}
	public void TutorialAnimations()
	{
		/*int font.getSize() = 20 ;
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
				DP.DrawText(new Point((int) (Pos.x + player.getSize()[0]/2 + dx), Pos.y - player.getSize()[1]/2 - (int)(0.78*H)}, "Left", OverallAngle, AllText[TextCat][2], font, ColorPalette[6]) ;
				DP.DrawText(new Point((int) (Pos.x + player.getSize()[0]/2 + dx), Pos.y - player.getSize()[1]/2 - (int)(0.78*H) + Sy}, "Left", OverallAngle, AllText[TextCat][3], font, ColorPalette[5]) ;
				DP.DrawText(new Point((int) (Pos.x - 1.2*player.getSize()[0] + dx), Pos.y - player.getSize()[1]/2 - (int)(0.78*H) + 2*Sy}, "Right", OverallAngle, AllText[TextCat][18], font, ColorPalette[1]) ;
				DP.DrawText(new Point((int) (Pos.x - 1.2*player.getSize()[0] + dx), Pos.y - player.getSize()[1]/2 - (int)(0.78*H) + 3*Sy}, "Right", OverallAngle, AllText[TextCat][20], font, ColorPalette[2]) ;
			}
		}*/
		/*if (animation == 4)	// Bag
		{		
			Pos = new int[] {(int) (0.2*WinDim[0]), (int) (0.75*WinDim[1])} ;
			float dh = 1 ;
			if (0 < counter & counter < duration)
			{
				dh = dh*Ut.UpAndDownCounter(counter, duration) ;
				DP.DrawText(new Point(Pos.x, Pos.y - (int)(0.6*H + dh)}, "Center", OverallAngle, ActionKeys[0], SansFont, font.getSize(), ColorPalette[5]) ;	
				DP.DrawText(new Point(Pos.x, (int) (Pos.y + dh)}, "Center", OverallAngle, ActionKeys[2], SansFont, font.getSize(), ColorPalette[5]) ;	
				DrawArrowIcon(new int[] {Pos.x, Pos.y - (int)(0.85*H + dh)}, "Center", 90, new float[] {1, 1}, new boolean[] {false, false}) ;
				DrawArrowIcon(new int[] {Pos.x, Pos.y + (int)(0.2*H + dh)}, "Center", 270, new float[] {1, 1}, new boolean[] {false, false}) ;
			}
		}
		if (animation == 17)	// Forge
		{
			if (0 < counter & counter < duration)
			{
				DrawMenuWindow(Pos, L, H, null, 0, ColorPalette[20], ColorPalette[20]) ;
				DrawEquips(new int[] {(int) (Pos.x + 0.5*L), (int) (Pos.y - 0.5*H + 0.5*TextH)}, player.getJob(), 0, FirstEquipID, EquipsBonus, new float[] {1, 1}, OverallAngle) ;
				DP.DrawText(new Point((int) (Pos.x + 0.5*L), (int) (Pos.y - H + 0.6*TextH)}, "Center", OverallAngle, "+ 0", SansFont, font.getSize(), player.getColors()[0]) ;
			}
			if (duration/3 <= counter & counter < duration)
			{
				DP.DrawImage(PointingArrowImage, new int[] {(int) (Pos.x + 1.2*L + PointingArrowImage.getWidth(null)/2), (int) (Pos.y - 0.5*H)}, OverallAngle, new float[] {1, 1}, new boolean[] {false, false}, "Center") ;
			}
			if (2*duration/3 <= counter & counter < duration)
			{
				DrawMenuWindow(new int[] {(int) (Pos.x + 1.4*L + PointingArrowImage.getWidth(null)), (int) (Pos.y)}, L, H, null, 0, ColorPalette[20], ColorPalette[20]) ;
				DrawEquips(new int[] {(int) (Pos.x + 1.9*L + PointingArrowImage.getWidth(null)), (int) (Pos.y - 0.5*H + 0.5*TextH)}, player.getJob(), 0, FirstEquipID, EquipsBonus, new float[] {1, 1}, OverallAngle) ;
				DP.DrawText(new Point((int) (Pos.x + 1.9*L + PointingArrowImage.getWidth(null)), (int) (Pos.y - H + 0.6*TextH)}, "Center", OverallAngle, "+ 1", SansFont, font.getSize(), player.getColors()[0]) ;
			}
		}*/
	}


}