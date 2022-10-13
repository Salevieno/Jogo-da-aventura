package Windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;

import Graphics.DrawFunctions;
import Graphics.DrawPrimitives;
import Items.Alchemy;
import Items.Arrow;
import Items.Equip;
import Items.Fab;
import Items.Food;
import Items.Forge;
import Items.GeneralItem;
import Items.Item;
import Items.PetItem;
import Items.Potion;
import Items.QuestItem;
import Main.Game;
import Main.Utg;

public class Bag extends Window
{	
	private Potion[] pot ;
	private Alchemy[] alch ;
	private Forge[] forge ;
	private PetItem[] petItem ;
	private Food[] food ;
	private Arrow[] arrow ;
	private Equip[] equip ;
	private GeneralItem[] genItem ;
	private Fab[] fab ;
	private QuestItem[] quest ;
	
	public static Image MenuImage = new ImageIcon(Game.ImagesPath + "BagMenu.png").getImage() ;
    public static Image SlotImage = new ImageIcon(Game.ImagesPath + "BagSlot.png").getImage() ;
	
	public Bag(Potion[] pot, Alchemy[] alch, Forge[] forge, PetItem[] petItem, Food[] food, Arrow[] arrow, Equip[] equip, GeneralItem[] genItem, Fab[] fab, QuestItem[] quest)
	{
		super(null, 10, 2, 0, 0) ;
		this.pot = pot ;
		this.alch = alch ;
		this.forge = forge ;
		this.petItem = petItem ;
		this.food = food ;
		this.arrow = arrow ;
		this.equip = equip ;
		this.genItem = genItem ;
		this.fab = fab ;
		this.quest = quest ;
	}
	
	public Potion[] getPotions() {return pot ;}
	public Alchemy[] getAlchemy() {return alch ;}
	public Forge[] getForge() {return forge ;}
	public PetItem[] getPetItem() {return petItem ;}
	public Food[] getFood() {return food ;}
	public Arrow[] getArrow() {return arrow ;}
	public Equip[] getEquip() {return equip ;}
	public GeneralItem[] getGenItem() {return genItem ;}
	public Fab[] getFab() {return fab ;}
	public QuestItem[] getQuest() {return quest ;}
	

	public void Add(Potion newPot)
	{
		pot = Utg.AddElem(pot, newPot) ;
	}
	public void remove(int itemID)
	{
		System.arraycopy(pot, itemID + 1, pot, itemID, pot.length - itemID - 1); ;
	}
	
	public Item getSelectedItem()
	{
		if (menu == 0)
		{
			return pot[item] ;
		}
		
		return null ;
	}
	
	public void display(Point MousePos, int[] AllTextCat, String[][] AllText, DrawFunctions DF)
	{
		DrawPrimitives DP = DF.getDrawPrimitives() ;
		float OverallAngle = DF.getOverallAngle() ;
		int[] WinDim = Game.getScreen().getDimensions() ;
		Point pos = new Point((int)(0.35 * WinDim[0]), (int)(0.48 * WinDim[1])) ;
		int[] size = new int[] {(int)(0.52 * WinDim[0]), (int)(0.4 * WinDim[1])} ;
		int windowLimit = 20 ;
		Color[] ColorPalette = Game.ColorPalette ;
		//DF.DrawBag(pos, size, this, MenuImage, SlotImage, 0, 0, 0, 10, 0, MousePos) ;
		
		int MenusCat = AllTextCat[30] ;
		Font MenuFont = new Font("SansSerif", Font.BOLD, 13) ;
		Font ItemFont = new Font("SansSerif", Font.BOLD, 10) ;
		Color BGColor = ColorPalette[11] ;
		int NSlotsmax = 20 ;
		if (tab == 1)
		{
			BGColor = ColorPalette[19] ;
		}
		
		
		// Draw menus
		int MenuL = MenuImage.getWidth(null) ;
		int MenuH = MenuImage.getHeight(null) ;
		for (int m = 0 ; m <= AllText[MenusCat].length - 3 ; m += 1)
		{
			Point MenuPos = new Point(pos.x + 8, pos.y + m * (MenuH - 1)) ;
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
		DP.DrawRoundRect(pos, "TopLeft", size[0], size[1], 1, BGColor, BGColor, true) ;
		
		
		// Draw items
		Item[] ActiveItems = null;
		if (menu == 0)
		{
			ActiveItems = getPotions() ;
		}
		if (ActiveItems != null)
		{
			numberItems = Math.min(NSlotsmax, ActiveItems.length - NSlotsmax * window) ;
		}
		else
		{
			numberItems = 0 ;
		}
		
		int slotW = SlotImage.getWidth(null) ;
		int slotH = SlotImage.getHeight(null) ;
		Point[] slotCenter = new Point[numberItems] ;
		Point[] textPos = new Point[numberItems] ;
		for (int i = 0 ; i <= numberItems - 1 ; i += 1)
		{
			int sx = size[0] / 2, sy = (size[1] - 6 - slotH) / 9 ;
			int row = i % (NSlotsmax / 2), col = i / (NSlotsmax / 2) ;
			slotCenter[i] = new Point((int) (pos.x + 5 + slotW / 2 + col * sx), (int) (pos.y + 3 + slotH / 2 + row * sy)) ;
			textPos[i] = new Point(slotCenter[i].x + slotW / 2 + 5, slotCenter[i].y) ;
		}
		
		for (int i = 0 ; i <= numberItems - 1 ; i += 1)
		{
			String text = ActiveItems[i].getName() ;//+ " (x" + bag[i] + ")" ;
			Color TextColor = ColorPalette[3] ;
			if (i == item)
			{
				TextColor = ColorPalette[6] ;
			}
			DP.DrawImage(SlotImage, slotCenter[i], "Center") ;				// Draw slots
			//DP.DrawImage(items[i].getImage(), slotCenter[i], "Center") ;	// Draw items
			DP.DrawTextUntil(textPos[i], "CenterLeft", OverallAngle, text, ItemFont, TextColor, 10, MousePos) ;	
		}
		if (0 < windowLimit)
		{
			DF.DrawWindowArrows(new Point(pos.x, pos.y + size[1]), size[0], 0, window, windowLimit) ;
		}
	}
	
}
