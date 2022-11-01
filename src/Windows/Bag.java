package Windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;

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
import LiveBeings.LiveBeing;
import LiveBeings.Player;
import Main.Game;
import Utilities.Size;
import Utilities.UtilG;

public class Bag extends Window
{	
	private ArrayList<Potion> pot ;
	private ArrayList<Alchemy> alch ;
	private ArrayList<Forge> forge ;
	private ArrayList<PetItem> petItem ;
	private ArrayList<Food> food ;
	private ArrayList<Arrow> arrow ;
	private ArrayList<Equip> equip ;
	private ArrayList<GeneralItem> genItem ;
	private ArrayList<Fab> fab ;
	private ArrayList<QuestItem> quest ;
	
	public static Image MenuImage = new ImageIcon(Game.ImagesPath + "BagMenu.png").getImage() ;
    public static Image SlotImage = new ImageIcon(Game.ImagesPath + "BagSlot.png").getImage() ;
	
	public Bag(ArrayList<Potion> pot, ArrayList<Alchemy> alch, ArrayList<Forge> forge, ArrayList<PetItem> petItem,
			ArrayList<Food> food, ArrayList<Arrow> arrow, ArrayList<Equip> equip, ArrayList<GeneralItem> genItem, ArrayList<Fab> fab, ArrayList<QuestItem> quest)
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
	
	public ArrayList<Potion> getPotions() {return pot ;}
	public ArrayList<Alchemy> getAlchemy() {return alch ;}
	public ArrayList<Forge> getForge() {return forge ;}
	public ArrayList<PetItem> getPetItem() {return petItem ;}
	public ArrayList<Food> getFood() {return food ;}
	public ArrayList<Arrow> getArrow() {return arrow ;}
	public ArrayList<Equip> getEquip() {return equip ;}
	public ArrayList<GeneralItem> getGenItem() {return genItem ;}
	public ArrayList<Fab> getFab() {return fab ;}
	public ArrayList<QuestItem> getQuest() {return quest ;}
	

	public void Add(Potion newPot)
	{
		pot.add(newPot) ;
	}
	public void addItem(Arrow newArrow)
	{
		arrow.add(newArrow) ;
	}
	public void remove(int itemID)
	{
		//System.arraycopy(pot, itemID + 1, pot, itemID, pot.length - itemID - 1); ;
	}
	
	public Item getSelectedItem()
	{
		if (menu == 0)
		{
			return pot.get(item) ;
		}
		
		return null ;
	}
	
	public void navigate(String action)
	{
		if (getTab() == 0)
		{
			if (action.equals(Player.ActionKeys[2]))
			{
				menuUp() ;
			}
			if (action.equals(Player.ActionKeys[0]))
			{
				menuDown() ;
			}
		}
		if (getTab() == 1)
		{
			if (action.equals(Player.ActionKeys[2]))
			{
				itemUp() ;
			}
			if (action.equals(Player.ActionKeys[0]))
			{
				itemDown() ;
			}
			if (action.equals(Player.ActionKeys[3]))
			{
				windowUp() ;
			}
			if (action.equals(Player.ActionKeys[1]))
			{
				windowDown() ;
			}
			if (action.equals("Enter") | action.equals("MouseLeftClick"))
			{
				//UseItem(this, getSelectedItem()) ;
			}
		}
		
		if (action.equals("Enter") | action.equals("MouseLeftClick"))
		{
			tabUp() ;
		}
		if (action.equals("Escape") | action.equals("MouseRightClick"))
		{
			tabDown() ;
			setItem(0) ;
		}
	}
	public void useItem(Player user, Item item)
	{
		if (item != null)	// if the item is valid
		{
			if (item instanceof Potion)	// potions
			{
				Potion pot = (Potion) item ;
				float PotMult = 1 ;
				if (user.getPA().getJob() == 3)
				{
					PotMult += 0.06 * user.getSpell()[7].getLevel() ;
				}
				
				user.getPA().incLife(pot.getLifeHeal() * user.getPA().getLife()[1] * PotMult) ;
				user.getPA().incMP(pot.getMPHeal() * user.getPA().getMp()[1] * PotMult) ;					
			}
			user.getBag().remove(user.getBag().getItem());	
		}
	}
	public void display(Point MousePos, String[] allText, DrawFunctions DF)
	{
		DrawPrimitives DP = DF.getDrawPrimitives() ;
		float OverallAngle = DF.getOverallAngle() ;
		Size screenSize = Game.getScreen().getSize() ;
		Point pos = new Point((int)(0.35 * screenSize.x), (int)(0.48 * screenSize.y)) ;
		Size size = new Size((int)(0.52 * screenSize.x), (int)(0.4 * screenSize.y)) ;
		int windowLimit = 20 ;
		Color[] ColorPalette = Game.ColorPalette ;
		//DF.DrawBag(pos, size, this, MenuImage, SlotImage, 0, 0, 0, 10, 0, MousePos) ;
		
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
		for (int m = 0 ; m <= allText.length - 3 ; m += 1)
		{
			Point MenuPos = new Point(pos.x + 8, pos.y + m * (MenuH - 1)) ;
			Color TextColor = ColorPalette[12] ;
			if (m == menu)
			{
				TextColor = ColorPalette[3] ;
				MenuPos.x += 3 ;
			}
			DP.DrawImage(MenuImage, MenuPos, "TopRight") ;
			DP.DrawText(new Point(MenuPos.x - MenuL / 2, MenuPos.y + MenuH / 2), "Center", OverallAngle, allText[m + 1],
					MenuFont, TextColor) ;
		}
		
		// Draw bag
		DP.DrawRoundRect(pos, "TopLeft", size, 1, BGColor, BGColor, true) ;
		
		
		// Draw items
		/*ArrayList<Item> ActiveItems = null;
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
		}*/
		
		int slotW = SlotImage.getWidth(null) ;
		int slotH = SlotImage.getHeight(null) ;
		Point[] slotCenter = new Point[numberItems] ;
		Point[] textPos = new Point[numberItems] ;
		for (int i = 0 ; i <= numberItems - 1 ; i += 1)
		{
			int sx = size.x / 2, sy = (size.y - 6 - slotH) / 9 ;
			int row = i % (NSlotsmax / 2), col = i / (NSlotsmax / 2) ;
			slotCenter[i] = new Point((int) (pos.x + 5 + slotW / 2 + col * sx), (int) (pos.y + 3 + slotH / 2 + row * sy)) ;
			textPos[i] = new Point(slotCenter[i].x + slotW / 2 + 5, slotCenter[i].y) ;
		}
		
		for (int i = 0 ; i <= numberItems - 1 ; i += 1)
		{
			//String text = ActiveItems[i].getName() ;//+ " (x" + bag[i] + ")" ;
			Color TextColor = ColorPalette[3] ;
			if (i == item)
			{
				TextColor = ColorPalette[6] ;
			}
			DP.DrawImage(SlotImage, slotCenter[i], "Center") ;				// Draw slots
			//DP.DrawImage(items[i].getImage(), slotCenter[i], "Center") ;	// Draw items
			//DP.DrawTextUntil(textPos[i], "CenterLeft", OverallAngle, text, ItemFont, TextColor, 10, MousePos) ;	
		}
		if (0 < windowLimit)
		{
			DF.DrawWindowArrows(new Point(pos.x, pos.y + size.y), size.x, 0, window, windowLimit) ;
		}
	}
	
}
