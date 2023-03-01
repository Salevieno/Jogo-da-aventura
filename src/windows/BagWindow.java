package windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

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
import liveBeings.Player;
import main.Game;
import utilities.Align;
import utilities.UtilG;

public class BagWindow extends GameWindow
{
	private Map<Potion, Integer> pot ;
	private Map<Alchemy, Integer> alch ;
	private Map<Forge, Integer> forges ;
	private Map<PetItem, Integer> petItems ;
	private Map<Food, Integer> foods ;
	private Map<Arrow, Integer> arrows ;
	private Map<Equip, Integer> equips ;
	private Map<GeneralItem, Integer> genItems ;
	private Map<Fab, Integer> fabItems ;
	private Map<QuestItem, Integer> questItems ;
	private Item selectedItem ;
	private int numberSlotMax ;
	private int windowLimit ;
	private Map<Item, Integer> activeItems ;
	private int gold ;

	public static Image SelectedBag = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "BagSelected.png") ;
	public static Image MenuImage = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "BagMenu.png") ;
	public static Image SelectedMenuImage = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "BagSelectedMenu.png") ;
    public static Image SlotImage = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "BagSlot.png") ;
	
	public BagWindow(Map<Potion, Integer> pot, Map<Alchemy, Integer> alch, Map<Forge, Integer> forge, Map<PetItem, Integer> petItem,
			Map<Food, Integer> food, Map<Arrow, Integer> arrow, Map<Equip, Integer> equip, Map<GeneralItem, Integer> genItem,
			Map<Fab, Integer> fab, Map<QuestItem, Integer> quest)
	{
		super("Mochila", UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "Bag.png"), 10, 2, 0, 0) ;
		this.pot = pot ;
		this.alch = alch ;
		this.forges = forge ;
		this.petItems = petItem ;
		this.foods = food ;
		this.arrows = arrow ;
		this.equips = equip ;
		this.genItems = genItem ;
		this.fabItems = fab ;
		this.questItems = quest ;
		selectedItem = null ;
		numberSlotMax = 20 ;
		windowLimit = 20 ;
		activeItems = new HashMap<>() ;
		gold = 0 ;
		
		//menus.add(pot) ;
	}

	public Map<Potion, Integer> getPotions() {return pot ;}
	public Map<Alchemy, Integer> getAlchemy() {return alch ;}
	public Map<Forge, Integer> getForge() {return forges ;}
	public Map<PetItem, Integer> getPetItem() {return petItems ;}
	public Map<Food, Integer> getFood() {return foods ;}
	public Map<Arrow, Integer> getArrow() {return arrows ;}
	public Map<Equip, Integer> getEquip() {return equips ;}
	public Map<GeneralItem, Integer> getGenItem() {return genItems ;}
	public Map<Fab, Integer> getFab() {return fabItems ;}
	public Map<QuestItem, Integer> getQuest() {return questItems ;}
	public int getGold() {return gold ;}
	

	public void Add(Item item, int amount)
	{
		if (item instanceof Potion)
		{
			if (pot.containsKey(item)) { pot.put((Potion) item, pot.get((Potion) item) + amount) ;}
			else { pot.put((Potion) item, amount) ;}
			numberItems = pot.size() ;
		}
		if (item instanceof Alchemy)
		{
			if (alch.containsKey(item)) { alch.put((Alchemy) item, alch.get((Alchemy) item) + amount) ;}
			else { alch.put((Alchemy) item, amount) ;}
			numberItems = alch.size() ;
		}
		if (item instanceof Forge)
		{
			if (forges.containsKey(item)) { forges.put((Forge) item, forges.get((Forge) item) + amount) ;}
			else { forges.put((Forge) item, amount) ;}
			numberItems = forges.size() ;
		}
		if (item instanceof PetItem)
		{
			if (petItems.containsKey(item)) { petItems.put((PetItem) item, petItems.get((PetItem) item) + amount) ;}
			else { petItems.put((PetItem) item, amount) ;}
			numberItems = petItems.size() ;
		}
		if (item instanceof Food)
		{
			if (foods.containsKey(item)) { foods.put((Food) item, foods.get((Food) item) + amount) ;}
			else { foods.put((Food) item, amount) ;}
			numberItems = foods.size() ;
		}
		if (item instanceof Arrow)
		{
			if (arrows.containsKey(item)) { arrows.put((Arrow) item, arrows.get((Arrow) item) + amount) ;}
			else { arrows.put((Arrow) item, amount) ;}
			numberItems = arrows.size() ;
		}
		if (item instanceof GeneralItem)
		{
			if (genItems.containsKey(item)) { genItems.put((GeneralItem) item, genItems.get((GeneralItem) item) + amount) ;}
			else { genItems.put((GeneralItem) item, amount) ;}
			numberItems = genItems.size() ;
		}
		if (item instanceof Equip)
		{
			if (equips.containsKey(item)) { equips.put((Equip) item, equips.get((Equip) item) + amount) ;}
			else { equips.put((Equip) item, amount) ;}
			numberItems = equips.size() ;
		}
		if (item instanceof Fab)
		{
			if (fabItems.containsKey(item)) { fabItems.put((Fab) item, fabItems.get((Fab) item) + amount) ;}
			else { fabItems.put((Fab) item, amount) ;}
			numberItems = fabItems.size() ;
		}
		if (item instanceof QuestItem)
		{
			if (questItems.containsKey(item)) { questItems.put((QuestItem) item, questItems.get((QuestItem) item) + amount) ;}
			else { questItems.put((QuestItem) item, amount) ;}
			numberItems = questItems.size() ;
		}
	}
//	public void removeItem(Map<Item, Integer> items, Item item, int amount)
//	{
//		if (!items.containsKey(item)) { return ;}
//		if (items.get(item) < amount) { return ;}
//		
//		items.put(item, items.get(item) - amount) ;
//	}
	public void Remove(Item item, int amount)
	{
		if (item instanceof Potion)
		{
			Potion potion = (Potion) item ;
			if (!pot.containsKey(potion)) { return ;}
			if (pot.get(potion) < amount) { return ;}
			
			pot.put(potion, pot.get(potion) - amount) ;
		}
		if (item instanceof Alchemy)
		{
			Alchemy alchemy = (Alchemy) item ;
			if (!alch.containsKey(alchemy)) { return ;}
			if (alch.get(alchemy) < amount) { return ;}
			
			alch.put(alchemy, alch.get(alchemy) - amount) ;
		}
		if (item instanceof Forge)
		{
			Forge forge = (Forge) item ;
			if (!forges.containsKey(forge)) { return ;}
			if (forges.get(forge) < amount) { return ;}
			
			forges.put(forge, forges.get(forge) - amount) ;
		}
		if (item instanceof PetItem)
		{
			PetItem petItem = (PetItem) item ;
			if (!petItems.containsKey(petItem)) { return ;}
			if (petItems.get(petItem) < amount) { return ;}
			
			petItems.put(petItem, petItems.get(petItem) - amount) ;
		}
		if (item instanceof Food)
		{
			Food food = (Food) item ;
			if (!foods.containsKey(food)) { return ;}
			if (foods.get(food) < amount) { return ;}
			
			foods.put(food, foods.get(food) - amount) ;
		}
		if (item instanceof Arrow)
		{
			Arrow arrow = (Arrow) item ;
			if (!arrows.containsKey(arrow)) { return ;}
			if (arrows.get(arrow) < amount) { return ;}
			
			arrows.put(arrow, arrows.get(arrow) - amount) ;
		}
		if (item instanceof Equip)
		{
			Equip equip = (Equip) item ;
			if (!equips.containsKey(equip)) { return ;}
			if (equips.get(equip) < amount) { return ;}
			
			equips.put(equip, equips.get(equip) - amount) ;
		}
		if (item instanceof GeneralItem)
		{
			GeneralItem genItem = (GeneralItem) item ;
			if (!genItems.containsKey(genItem)) { return ;}
			if (genItems.get(genItem) < amount) { return ;}
			
			genItems.put(genItem, genItems.get(genItem) - amount) ;
		}
		if (item instanceof Fab)
		{
			Fab fab = (Fab) item ;
			if (!fabItems.containsKey(fab)) { return ;}
			if (fabItems.get(fab) < amount) { return ;}
			
			fabItems.put(fab, fabItems.get(fab) - amount) ;
		}
		if (item instanceof QuestItem)
		{
			QuestItem questItem = (QuestItem) item ;
			if (!questItems.containsKey(questItem)) { return ;}
			if (questItems.get(questItem) < amount) { return ;}
			
			questItems.put(questItem, questItems.get(questItem) - amount) ;
		}
	}
	
	public void addGold (int amount) { gold += amount ;}
	public void removeGold (int amount)
	{
		if (hasEnoughGold(amount))
		{
			gold += -amount ;
			return ;
		}
		
		System.out.println("Tentando remover mais dinheiro do que o existente na mochila") ;
	}
	
	public Map<Item, Integer> getActiveItems()
	{
		switch (menu)
		{
			case 0: return pot.entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
			case 1: return alch.entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
			case 2: return forges.entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
			case 3: return petItems.entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
			case 4: return foods.entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
			case 5: return arrows.entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
			case 6: return equips.entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
			case 7: return genItems.entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
			case 8: return fabItems.entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
			case 9: return questItems.entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
			default: return null ;
		}
	}
	
	public Item getSelectedItem()
	{
		getActiveItems() ;
		numberItems = 0 ;
		if (!activeItems.isEmpty())
		{
			numberItems = Math.min(numberSlotMax, activeItems.size() - numberSlotMax * window) ;
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
	
	public boolean contains(Item item)
	{
		if (item instanceof Potion) { return pot.containsKey(item) ;}
		if (item instanceof Alchemy) { return alch.containsKey((Alchemy) item) ;}
		if (item instanceof Forge) { return forges.containsKey((Forge) item) ;}
		if (item instanceof PetItem) { return petItems.containsKey((PetItem) item) ;}
		if (item instanceof Food) { return foods.containsKey((Food) item) ;}
		if (item instanceof Arrow) { return arrows.containsKey(item) ;}
		if (item instanceof Equip) { return equips.containsKey(item) ;}
		if (item instanceof GeneralItem) { return genItems.containsKey(item) ;}
		if (item instanceof Fab) { return fabItems.containsKey(item) ;}
		if (item instanceof QuestItem) { return questItems.containsKey(item) ;}
		
		System.out.println("Item procurado na mochila não pertence a uma categoria válida");
		return false ;
	}
	
	public boolean contains (List<Item> items)
	{
		for (Item item : items)
		{
			if (!contains(item)) { return false ;}
		}
		
		return true ;
	}
	
	public boolean hasEnough(Item item, int qtd)
	{
		if (!contains(item)) { return false ;}

		if (item instanceof Potion) { return qtd <= pot.get(item) ;}
		if (item instanceof Alchemy) { return qtd <= alch.get(item) ;}
		if (item instanceof Forge) { return qtd <= forges.get(item) ;}
		if (item instanceof PetItem) { return qtd <= petItems.get(item) ;}
		if (item instanceof Food) { return qtd <= foods.get(item) ;}
		if (item instanceof Arrow) { return qtd <= arrows.get(item) ;}
		if (item instanceof Equip) { return qtd <= equips.get(item) ;}
		if (item instanceof GeneralItem) { return qtd <= genItems.get(item) ;}
		if (item instanceof Fab) { return qtd <= fabItems.get(item) ;}
		if (item instanceof QuestItem) { return qtd <= questItems.get(item) ;}

		System.out.println("Item procurado na mochila não pertence a uma categoria válida");
		return false ;
	}
	
	public boolean hasEnough(Map<Item, Integer> items)
	{
		for (Item item : items.keySet())
		{
			if (!hasEnough(item, items.get(item))) {return false ;}
		}
		
		return true ;
	}
	
	public boolean hasEnoughGold (int amount) { return amount <= gold ;}
	
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
			
			if (action.equals("Enter") | action.equals("MouseLeftClick"))
			{
				tabUp() ;
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
			if (action.equals("Escape") | action.equals("MouseRightClick"))
			{
				tabDown() ;
				setItem(0) ;
			}
		}
	}

	
	public void display(Point MousePos, String[] allText, DrawingOnPanel DP)
	{
		Point windowPos = new Point((int)(0.35 * Game.getScreen().getSize().width), (int)(0.48 * Game.getScreen().getSize().height)) ;
		Color[] colorPalette = Game.ColorPalette ;
		
		Font MenuFont = new Font(Game.MainFontName, Font.BOLD, 13) ;
		Font ItemFont = new Font(Game.MainFontName, Font.BOLD, 10) ;
		
		
		// draw menus
		int MenuH = MenuImage.getHeight(null) ;
		for (int m = 0 ; m <= allText.length - 3 ; m += 1)
		{
			Point menuPos = m == menu ? UtilG.Translate(windowPos, 3, m * (MenuH - 1) + 3) : UtilG.Translate(windowPos, 0, m * (MenuH - 1) + 3) ;
			Point textPos = UtilG.Translate(menuPos, 3, MenuH / 2) ;
			Color TextColor = m == menu ? colorPalette[3] : colorPalette[11] ;
			
			DP.DrawImage(m == menu ? SelectedMenuImage : MenuImage, menuPos, Align.topLeft) ;
			DP.DrawText(textPos, Align.centerLeft, DrawingOnPanel.stdAngle, allText[m + 1], MenuFont, TextColor) ;
		}
		
		// draw bag
		DP.DrawImage(tab == 0 ? image : SelectedBag, windowPos, Align.topLeft) ;
		
		// draw items
		int slotW = SlotImage.getWidth(null) ;
		int slotH = SlotImage.getHeight(null) ;
		int i = 0 ;
		activeItems = getActiveItems() ;
		for (Map.Entry<Item, Integer> activeItem : activeItems.entrySet())
		{
			if (numberSlotMax <= i) { break ;}
			
			int row = i % (numberSlotMax / 2) ;
			int col = i / (numberSlotMax / 2) ;
			Point slotCenter = UtilG.Translate(windowPos, 78 + slotW / 2 + col * 121, 12 + slotH / 2 + row * 21) ;
			String itemText = activeItem.getKey().getName() + " (x " + activeItem.getValue() + ")" ;
			Point textPos = new Point(slotCenter.x + slotW / 2 + 5, slotCenter.y) ;
			Color textColor = i == item ? colorPalette[3] : colorPalette[9] ;
			
			DP.DrawImage(SlotImage, slotCenter, Align.center) ;
			DP.DrawImage(activeItem.getKey().getImage(), slotCenter, Align.center) ;
			DP.DrawTextUntil(textPos, Align.centerLeft, DrawingOnPanel.stdAngle, itemText, ItemFont, textColor, 10, MousePos) ;
			i += 1 ;
		}
		
		DP.DrawWindowArrows(UtilG.Translate(windowPos, 0, size.height), size.width, window, windowLimit) ;
		
	}
	
	@Override
	public String toString() {
		return "Bag [pot=" + pot + ", alch=" + alch + ", forge=" + forges + ", petItem=" + petItems + ", food=" + foods
				+ ", arrow=" + arrows + ", equip=" + equips + ", genItem=" + genItems + ", fab=" + fabItems + ", quest=" + questItems
				+ "]";
	}
	
}
