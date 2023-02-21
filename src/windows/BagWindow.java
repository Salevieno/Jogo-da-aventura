package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

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
	private Map<Forge, Integer> forge ;
	private Map<PetItem, Integer> petItem ;
	private Map<Food, Integer> food ;
	private Map<Arrow, Integer> arrow ;
	private Map<Equip, Integer> equip ;
	private Map<GeneralItem, Integer> genItem ;
	private Map<Fab, Integer> fab ;
	private Map<QuestItem, Integer> quest ;
	private Item selectedItem ;
	private int numberSlotMax ;
	private int windowLimit ;
	private Map<Item, Integer> activeItems ;
	
	public static Image MenuImage = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "BagMenu.png") ;
    public static Image SlotImage = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "BagSlot.png") ;
	
	public BagWindow(Map<Potion, Integer> pot, Map<Alchemy, Integer> alch, Map<Forge, Integer> forge, Map<PetItem, Integer> petItem,
			Map<Food, Integer> food, Map<Arrow, Integer> arrow, Map<Equip, Integer> equip, Map<GeneralItem, Integer> genItem,
			Map<Fab, Integer> fab, Map<QuestItem, Integer> quest)
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
	//public Map<Potion> getPotions() {return pot ;}
	public Map<Alchemy, Integer> getAlchemy() {return alch ;}
	public Map<Forge, Integer> getForge() {return forge ;}
	public Map<PetItem, Integer> getPetItem() {return petItem ;}
	public Map<Food, Integer> getFood() {return food ;}
	public Map<Arrow, Integer> getArrow() {return arrow ;}
	public Map<Equip, Integer> getEquip() {return equip ;}
	public Map<GeneralItem, Integer> getGenItem() {return genItem ;}
	public Map<Fab, Integer> getFab() {return fab ;}
	public Map<QuestItem, Integer> getQuest() {return quest ;}
	

	public void Add(Item item, int amount)
	{
		if (item instanceof Potion)
		{
			if (pot.containsKey(item)) { pot.put((Potion) item, pot.get((Potion) item) + amount) ;}
			else { pot.put((Potion) item, amount) ;}
//			numberItems = pot.size() ;
		}
		if (item instanceof Alchemy)
		{
			if (alch.containsKey(item)) { alch.put((Alchemy) item, alch.get((Alchemy) item) + amount) ;}
			else { alch.put((Alchemy) item, amount) ;}
		}
		if (item instanceof Forge)
		{
			if (forge.containsKey(item)) { forge.put((Forge) item, forge.get((Forge) item) + amount) ;}
			else { forge.put((Forge) item, amount) ;}
		}
		if (item instanceof PetItem)
		{
			if (petItem.containsKey(item)) { petItem.put((PetItem) item, petItem.get((PetItem) item) + amount) ;}
			else { petItem.put((PetItem) item, amount) ;}
		}
		if (item instanceof Food)
		{
			if (food.containsKey(item)) { food.put((Food) item, food.get((Food) item) + amount) ;}
			else { food.put((Food) item, amount) ;}
		}
		if (item instanceof Arrow)
		{
			if (arrow.containsKey(item)) { arrow.put((Arrow) item, arrow.get((Arrow) item) + amount) ;}
			else { arrow.put((Arrow) item, amount) ;}
		}
		if (item instanceof GeneralItem)
		{
			if (genItem.containsKey(item)) { genItem.put((GeneralItem) item, genItem.get((GeneralItem) item) + amount) ;}
			else { genItem.put((GeneralItem) item, amount) ;}
		}
		if (item instanceof Equip)
		{
			if (equip.containsKey(item)) { equip.put((Equip) item, equip.get((Equip) item) + amount) ;}
			else { equip.put((Equip) item, amount) ;}
		}
		if (item instanceof Fab)
		{
			if (fab.containsKey(item)) { fab.put((Fab) item, fab.get((Fab) item) + amount) ;}
			else { fab.put((Fab) item, amount) ;}
		}
		if (item instanceof QuestItem)
		{
			if (quest.containsKey(item)) { quest.put((QuestItem) item, quest.get((QuestItem) item) + amount) ;}
			else { quest.put((QuestItem) item, amount) ;}
		}
	}
	public void Remove(Item item, int amount)
	{
		if (item instanceof Potion)	// potions
		{
			Potion potion = (Potion) item ;
			if (pot.containsKey(potion))
			{
				if (amount <= pot.get(potion))
				{
					pot.put(potion, pot.get(potion) - amount) ;
				}
				else
				{
					pot.put(potion, 0) ;
					System.out.println("Tentando remover mais itens do que tem na mochila");
				}
			}
//			numberItems = pot.size() ;
		}
	}
	
	public Map<Item, Integer> getActiveItems()
	{
		switch (menu)
		{
			case 0: return pot.entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
			case 1: return alch.entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
			case 2: return forge.entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
			case 3: return petItem.entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
			case 4: return food.entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
			case 5: return arrow.entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
			case 6: return equip.entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
			case 7: return genItem.entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
			case 8: return fab.entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
			case 9: return quest.entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
			default: return null ;
		}
//		if (menu == 0)
//		{
//			Map<Potion, Integer> potions = getPotions() ;
//			for (Map.Entry<Potion, Integer> potion : potions.entrySet())
//			{
//				if (0 < potion.getValue())
//				{
//					activeItems.put(potion.getKey(), potion.getValue()) ;
//				}
//			}
//		}
//		if (menu == 1)
//		{
//			Map<Alchemy, Integer> alchemys = getAlchemy() ;
//			for (Map.Entry<Alchemy, Integer> alchemy : alchemys.entrySet())
//			{
//				if (0 < alchemy.getValue())
//				{
//					activeItems.put(alchemy.getKey(), alchemy.getValue()) ;
//				}
//			}
//		}
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
		
		Font MenuFont = new Font(Game.MainFontName, Font.BOLD, 13) ;
		Font ItemFont = new Font(Game.MainFontName, Font.BOLD, 10) ;
		Color BGColor = tab == 1 ? colorPalette[19] : colorPalette[11] ;
		
		
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
		
		
		// draw items
		int slotW = SlotImage.getWidth(null) ;
		int slotH = SlotImage.getHeight(null) ;
		int i = 0 ;
		activeItems = getActiveItems() ;
//		if (activeItems.isEmpty())
//		{
//			activeItems = getActiveItems() ;
//			System.out.println(activeItems);
//		}
		for (Map.Entry<Item, Integer> activeItem : activeItems.entrySet())
		{
			int sx = size.width / 2 ;
			int sy = (size.height - 6 - slotH) / 9 ;
			int row = i % (numberSlotMax / 2) ;
			int col = i / (numberSlotMax / 2) ;
			Point slotCenter = new Point((int) (pos.x + 5 + slotW / 2 + col * sx), (int) (pos.y + 3 + slotH / 2 + row * sy)) ;
			Point textPos = new Point(slotCenter.x + slotW / 2 + 5, slotCenter.y) ;
			Color TextColor = i == item ? colorPalette[6] : colorPalette[3] ;
			
			DP.DrawImage(SlotImage, slotCenter, Align.center) ;							// Draw slots
			DP.DrawImage(activeItem.getKey().getImage(), slotCenter, Align.center) ;	// Draw items
			DP.DrawTextUntil(textPos, Align.centerLeft, DrawingOnPanel.stdAngle, activeItem.getKey().getName() + " (x " + activeItem.getValue() + ")", ItemFont, TextColor, 10, MousePos) ;
			i += 1 ;
		}
		
		DP.DrawWindowArrows(new Point(pos.x, pos.y + size.height), size.width, window, windowLimit) ;
		
		
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
	}

	
	@Override
	public String toString() {
		return "Bag [pot=" + pot + ", alch=" + alch + ", forge=" + forge + ", petItem=" + petItem + ", food=" + food
				+ ", arrow=" + arrow + ", equip=" + equip + ", genItem=" + genItem + ", fab=" + fab + ", quest=" + quest
				+ "]";
	}
	
}
