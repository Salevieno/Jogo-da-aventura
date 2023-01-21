package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import graphics.DrawFunctions;
import graphics.DrawingOnPanel;
import items.Alchemy;
import items.Arrow;
import items.Equip;
import items.Fab;
import items.Food;
import items.Forge;
import items.GeneralItem;
import items.Item;
import items.PetItem;
import items.Potion;
import items.QuestItem;
import liveBeings.LiveBeing;
import liveBeings.Player;
import main.Game;
import utilities.Align;
import utilities.UtilG;

public class BagWindow extends GameWindow
{	
	//private ArrayList<Map<Item, Integer>> menus ;
	private Map<Potion, Integer> pot ;
	private Map<Alchemy, Integer> alch ;
	private ArrayList<Forge> forge ;
	private ArrayList<PetItem> petItem ;
	private ArrayList<Food> food ;
	private ArrayList<Arrow> arrow ;
	private ArrayList<Equip> equip ;
	private ArrayList<GeneralItem> genItem ;
	private ArrayList<Fab> fab ;
	private ArrayList<QuestItem> quest ;
	private Item selectedItem ;
	private int numberSlotMax ;
	private int windowLimit ;
	private Map<Item, Integer> activeItems ;
	
	public static Image MenuImage = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "BagMenu.png") ;
    public static Image SlotImage = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "BagSlot.png") ;
	
	public BagWindow(Map<Potion, Integer> pot, Map<Alchemy, Integer> alch, ArrayList<Forge> forge, ArrayList<PetItem> petItem,
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
		selectedItem = null ;
		numberSlotMax = 20 ;
		windowLimit = 20 ;
		activeItems = new HashMap<>() ;
		
		//menus.add(pot) ;
	}

	public Map<Potion, Integer> getPotions() {return pot ;}
	//public ArrayList<Potion> getPotions() {return pot ;}
	public Map<Alchemy, Integer> getAlchemy() {return alch ;}
	public ArrayList<Forge> getForge() {return forge ;}
	public ArrayList<PetItem> getPetItem() {return petItem ;}
	public ArrayList<Food> getFood() {return food ;}
	public ArrayList<Arrow> getArrow() {return arrow ;}
	public ArrayList<Equip> getEquip() {return equip ;}
	public ArrayList<GeneralItem> getGenItem() {return genItem ;}
	public ArrayList<Fab> getFab() {return fab ;}
	public ArrayList<QuestItem> getQuest() {return quest ;}
	

	public void Add(Item item, int amount)
	{
		if (item instanceof Potion)
		{
			Potion potion = (Potion) item ;
			if (pot.get(potion) == null)
			{
				pot.put(potion, amount) ;
			}
			else
			{
				pot.put(potion, pot.get(potion) + amount) ;
			}
			numberItems = pot.size() ;
		}
		if (item instanceof Alchemy)
		{
			Alchemy alchemy = (Alchemy) item ;
			if (alch.get(alchemy) == null)
			{
				alch.put(alchemy, amount) ;
			}
			else
			{
				alch.put(alchemy, alch.get(alchemy) + amount) ;
			}
			numberItems = alch.size() ;
		}
	}
	public void Remove(Item item, int amount)
	{
		if (item instanceof Potion)	// potions
		{
			Potion potion = (Potion) item ;
			if (pot.get(potion) != null)
			{
				if (amount <= pot.get(potion))
				{
					pot.put(potion, pot.get(potion) - amount) ;
				}
				else
				{
					// TODO what to do if an item has amount < 0 ?
				}
			}
			numberItems = pot.size() ;
		}
	}
	
	public void getActiveItems()
	{
		activeItems = new HashMap<>() ;
		if (menu == 0)
		{
			Map<Potion, Integer> potions = getPotions() ;
			for (Map.Entry<Potion, Integer> potion : potions.entrySet())
			{
				if (0 < potion.getValue())
				{
					activeItems.put(potion.getKey(), potion.getValue()) ;
				}
			}
		}
		if (menu == 1)
		{
			Map<Alchemy, Integer> alchemys = getAlchemy() ;
			for (Map.Entry<Alchemy, Integer> alchemy : alchemys.entrySet())
			{
				if (0 < alchemy.getValue())
				{
					activeItems.put(alchemy.getKey(), alchemy.getValue()) ;
				}
			}
		}
	}
	
	public Item getSelectedItem()
	{
		getActiveItems() ;
		if (!activeItems.isEmpty())
		{
			numberItems = Math.min(numberSlotMax, activeItems.size() - numberSlotMax * window) ;
		}
		else
		{
			numberItems = 0 ;
		}
		
		int i = 0 ;
		for (Map.Entry<Item, Integer> activeItem : activeItems.entrySet())
		{
			if (i == item)
			{
				selectedItem = activeItem.getKey() ;
				
				break ;
			}			
			i += 1 ;
		}
		
		return selectedItem ;
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
	public void display(Point MousePos, String[] allText, DrawingOnPanel DP)
	{
		Dimension screenSize = Game.getScreen().getSize() ;
		Point pos = new Point((int)(0.35 * screenSize.width), (int)(0.48 * screenSize.height)) ;
		Dimension size = new Dimension((int)(0.52 * screenSize.width), (int)(0.4 * screenSize.height)) ;
		Color[] colorPalette = Game.ColorPalette ;
		//DF.DrawBag(pos, size, this, MenuImage, SlotImage, 0, 0, 0, 10, 0, MousePos) ;
		
		Font MenuFont = new Font(Game.MainFontName, Font.BOLD, 13) ;
		Font ItemFont = new Font(Game.MainFontName, Font.BOLD, 10) ;
		Color BGColor = colorPalette[11] ;
		if (tab == 1)
		{
			BGColor = colorPalette[19] ;
		}
		
		
		// Draw menus
		int MenuH = MenuImage.getHeight(null) ;
		for (int m = 0 ; m <= allText.length - 3 ; m += 1)
		{
			Point MenuPos = new Point(pos.x + 8, pos.y + m * (MenuH - 1)) ;
			Color TextColor = colorPalette[12] ;
			if (m == menu)
			{
				TextColor = colorPalette[3] ;
				MenuPos.x += 3 ;
			}
			DP.DrawImage(MenuImage, MenuPos, Align.topRight) ;
			DP.DrawText(MenuPos, Align.topLeft, DrawingOnPanel.stdAngle, allText[m + 1], MenuFont, TextColor) ;
		}
		
		// Draw bag
		DP.DrawRoundRect(pos, Align.topLeft, size, 1, BGColor, BGColor, true) ;
		
		
		// determine items in the selected menu
		/*Map<Item, Integer> ActiveItems = new HashMap<>() ;
		if (menu == 0)
		{
			Map<Potion, Integer> potions = getPotions() ;
			for (Map.Entry<Potion, Integer> potion : potions.entrySet())
			{
				if (0 < potion.getValue())
				{
					ActiveItems.put(potion.getKey(), potion.getValue()) ;
				}
			}
		}
		if (menu == 1)
		{
			Map<Alchemy, Integer> alchemys = getAlchemy() ;
			for (Map.Entry<Alchemy, Integer> alchemy : alchemys.entrySet())
			{
				if (0 < alchemy.getValue())
				{
					ActiveItems.put(alchemy.getKey(), alchemy.getValue()) ;
				}
			}
		}
		if (!ActiveItems.isEmpty())
		{
			numberItems = Math.min(NSlotsmax, ActiveItems.size() - NSlotsmax * window) ;
		}
		else
		{
			numberItems = 0 ;
		}*/
		
		
		// draw items
		int slotW = SlotImage.getWidth(null) ;
		int slotH = SlotImage.getHeight(null) ;
		int i = 0 ;
		if (activeItems.isEmpty())
		{
			getActiveItems() ;
		}
		for (Map.Entry<Item, Integer> activeItem : activeItems.entrySet())
		{
			int sx = size.width / 2 ;
			int sy = (size.height - 6 - slotH) / 9 ;
			int row = i % (numberSlotMax / 2) ;
			int col = i / (numberSlotMax / 2) ;
			Point slotCenter = new Point((int) (pos.x + 5 + slotW / 2 + col * sx), (int) (pos.y + 3 + slotH / 2 + row * sy)) ;
			Point textPos = new Point(slotCenter.x + slotW / 2 + 5, slotCenter.y) ;
			Color TextColor = colorPalette[3] ;
			if (i == item)
			{
				TextColor = colorPalette[6] ;
			}
			
			DP.DrawImage(SlotImage, slotCenter, Align.center) ;							// Draw slots
			DP.DrawImage(activeItem.getKey().getImage(), slotCenter, Align.center) ;	// Draw items
			DP.DrawTextUntil(textPos, Align.centerLeft, DrawingOnPanel.stdAngle, activeItem.getKey().getName(), ItemFont, TextColor, 10, MousePos) ;
			i += 1 ;
		}
		
		// draw window arrows
		if (0 < windowLimit)
		{
			DP.DrawWindowArrows(new Point(pos.x, pos.y + size.height), size.width, window, windowLimit) ;
		}
	}

	
	@Override
	public String toString() {
		return "Bag [pot=" + pot + ", alch=" + alch + ", forge=" + forge + ", petItem=" + petItem + ", food=" + food
				+ ", arrow=" + arrow + ", equip=" + equip + ", genItem=" + genItem + ", fab=" + fab + ", quest=" + quest
				+ "]";
	}
	
}
