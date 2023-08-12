package windows;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
	private Map<Item, Integer> itemsOnWindow ;
	private int gold ;
	
	private final Point windowPos = new Point((int)(0.3 * Game.getScreen().getSize().width), (int)(0.48 * Game.getScreen().getSize().height)) ;
	private final int numberSlotMax = 20 ;
	
	public static final Image BagImage = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "Bag.png") ;
	public static final Image SelectedBag = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "BagSelected.png") ;
	public static final Image MenuImage = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "BagMenu.png") ;
	public static final Image SelectedMenuTab0 = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "BagSelectedMenuTab0.png") ;
	public static final Image SelectedMenuTab1 = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "BagSelectedMenuTab1.png") ;
    public static final Image SlotImage = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "BagSlot.png") ;
	
	public BagWindow(Map<Potion, Integer> pot, Map<Alchemy, Integer> alch, Map<Forge, Integer> forge, Map<PetItem, Integer> petItem,
			Map<Food, Integer> food, Map<Arrow, Integer> arrow, Map<Equip, Integer> equip, Map<GeneralItem, Integer> genItem,
			Map<Fab, Integer> fab, Map<QuestItem, Integer> quest)
	{
		
		super("Mochila", BagImage, 10, 2, 0, 0) ;
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
		itemsOnWindow = new LinkedHashMap<>() ;
		gold = 0 ;
		
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
	
	public void navigate(String action)
	{
		if (tab == 0)
		{
			if (action.equals(Player.ActionKeys[2]))
			{
				menuUp() ;
				window = 0 ;
				updateWindow() ;
			}
			if (action.equals(Player.ActionKeys[0]))
			{
				menuDown() ;
				window = 0 ;
				updateWindow() ;
			}			
			if (action.equals("Enter") | action.equals("MouseLeftClick"))
			{
				tabUp() ;
			}
			if (action.equals("Escape"))
			{
				close() ;
			}
		}
		if (tab == 1)
		{
			if (action.equals(Player.ActionKeys[2]))
			{
				itemUp() ;
			}
			if (action.equals(Player.ActionKeys[0]))
			{
				if (numberSlotMax * window + 1 <= item)
				{
					itemDown() ;
				}
			}
			if (action.equals(Player.ActionKeys[3]))
			{
				windowUp() ;
				updateWindow() ;
			}
			if (action.equals(Player.ActionKeys[1]))
			{
				windowDown() ;
				updateWindow() ;
			}
			if (action.equals("Escape") | action.equals("MouseRightClick"))
			{
				tabDown() ;
				setItem(0) ;
			}
		}
	}
	
	public void Add(Item item, int amount)
	{
		if (item instanceof Potion)
		{
			if (pot.containsKey(item)) { pot.put((Potion) item, pot.get((Potion) item) + amount) ;}
			else { pot.put((Potion) item, amount) ;}
			numberItems = pot.size() ;
			numberWindows = pot.size() / numberSlotMax ;
		}
		if (item instanceof Alchemy)
		{
			if (alch.containsKey(item)) { alch.put((Alchemy) item, alch.get((Alchemy) item) + amount) ;}
			else { alch.put((Alchemy) item, amount) ;}
			numberItems = alch.size() ;
			numberWindows = alch.size() / numberSlotMax ;
		}
		if (item instanceof Forge)
		{
			if (forges.containsKey(item)) { forges.put((Forge) item, forges.get((Forge) item) + amount) ;}
			else { forges.put((Forge) item, amount) ;}
			numberItems = forges.size() ;
			numberWindows = forges.size() / numberSlotMax ;
		}
		if (item instanceof PetItem)
		{
			if (petItems.containsKey(item)) { petItems.put((PetItem) item, petItems.get((PetItem) item) + amount) ;}
			else { petItems.put((PetItem) item, amount) ;}
			numberItems = petItems.size() ;
			numberWindows = petItems.size() / numberSlotMax ;
		}
		if (item instanceof Food)
		{
			if (foods.containsKey(item)) { foods.put((Food) item, foods.get((Food) item) + amount) ;}
			else { foods.put((Food) item, amount) ;}
			numberItems = foods.size() ;
			numberWindows = foods.size() / numberSlotMax ;
		}
		if (item instanceof Arrow)
		{
			if (arrows.containsKey(item)) { arrows.put((Arrow) item, arrows.get((Arrow) item) + amount) ;}
			else { arrows.put((Arrow) item, amount) ;}
			numberItems = arrows.size() ;
			numberWindows = arrows.size() / numberSlotMax ;
		}
		if (item instanceof GeneralItem)
		{
			if (genItems.containsKey(item)) { genItems.put((GeneralItem) item, genItems.get((GeneralItem) item) + amount) ;}
			else { genItems.put((GeneralItem) item, amount) ;}
			numberItems = genItems.size() ;
			numberWindows = genItems.size() / numberSlotMax ;
		}
		if (item instanceof Equip)
		{
			if (equips.containsKey(item)) { equips.put((Equip) item, equips.get((Equip) item) + amount) ;}
			else { equips.put((Equip) item, amount) ;}
			numberItems = equips.size() ;
			numberWindows = equips.size() / numberSlotMax ;
		}
		if (item instanceof Fab)
		{
			if (fabItems.containsKey(item)) { fabItems.put((Fab) item, fabItems.get((Fab) item) + amount) ;}
			else { fabItems.put((Fab) item, amount) ;}
			numberItems = fabItems.size() ;
			numberWindows = fabItems.size() / numberSlotMax ;
		}
		if (item instanceof QuestItem)
		{
			if (questItems.containsKey(item)) { questItems.put((QuestItem) item, questItems.get((QuestItem) item) + amount) ;}
			else { questItems.put((QuestItem) item, amount) ;}
			numberItems = questItems.size() ;
			numberWindows = questItems.size() / numberSlotMax ;
		}
	}

	public void Remove(Item item, int amount)
	{

		Map<Item, Integer> menuItems = getMenuItems() ;

		if (!menuItems.containsKey(item)) { System.out.println("Tentando remover um item que não existe na mochila") ; return ;}
		if (menuItems.get(item) < amount) { System.out.println("Tentando remover mais itens do que a quantidade existente na mochila") ; return ;}
		
		if (item instanceof Potion)
		{
			Potion potion = (Potion) item ;
			if (pot.get(potion) == amount) { pot.remove(potion) ; item = null ; tab = 0 ; return ;}
			
			pot.put(potion, pot.get(potion) - amount) ;
		}
		if (item instanceof Alchemy)
		{
			Alchemy alchemy = (Alchemy) item ;
			if (alch.get(alchemy) == amount) { alch.remove(alchemy) ; item = null ; tab = 0 ; return ;}
			
			alch.put(alchemy, alch.get(alchemy) - amount) ;
		}
		if (item instanceof Forge)
		{
			Forge forge = (Forge) item ;
			if (forges.get(forge) == amount) { forges.remove(forge) ; item = null ; tab = 0 ; return ;}
			
			forges.put(forge, forges.get(forge) - amount) ;
		}
		if (item instanceof PetItem)
		{
			PetItem petItem = (PetItem) item ;
			if (petItems.get(petItem) == amount) { petItems.remove(petItem) ; item = null ; tab = 0 ; return ;}
			
			petItems.put(petItem, petItems.get(petItem) - amount) ;
		}
		if (item instanceof Food)
		{
			Food food = (Food) item ;
			if (foods.get(food) == amount) { foods.remove(food) ; item = null ; tab = 0 ; return ;}
			
			foods.put(food, foods.get(food) - amount) ;
		}
		if (item instanceof Arrow)
		{
			Arrow arrow = (Arrow) item ;
			if (arrows.get(arrow) == amount) { arrows.remove(arrow) ; item = null ; tab = 0 ; return ;}
			
			arrows.put(arrow, arrows.get(arrow) - amount) ;
		}
		if (item instanceof Equip)
		{
			Equip equip = (Equip) item ;
			if (equips.get(equip) == amount) { equips.remove(equip) ; item = null ; tab = 0 ; return ;}
			
			equips.put(equip, equips.get(equip) - amount) ;
		}
		if (item instanceof GeneralItem)
		{
			GeneralItem genItem = (GeneralItem) item ;
			if (genItems.get(genItem) == amount) { genItems.remove(genItem) ; item = null ; tab = 0 ; return ;}
			
			genItems.put(genItem, genItems.get(genItem) - amount) ;
		}
		if (item instanceof Fab)
		{
			Fab fab = (Fab) item ;
			if (fabItems.get(fab) == amount) { fabItems.remove(fab) ; item = null ; tab = 0 ; return ;}
			
			fabItems.put(fab, fabItems.get(fab) - amount) ;
		}
		if (item instanceof QuestItem)
		{
			QuestItem questItem = (QuestItem) item ;
			if (questItems.get(questItem) == amount) { questItems.remove(questItem) ; item = null ; tab = 0 ; return ;}
			
			questItems.put(questItem, questItems.get(questItem) - amount) ;
		}
		
	}
	
	public void empty()
	{
		pot.clear() ;
		alch.clear() ;
		forges.clear() ;
		petItems.clear() ;
		foods.clear() ;
		arrows.clear() ;
		equips.clear() ;
		genItems.clear() ;
		fabItems.clear() ;
		questItems.clear() ;
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

	private Map<Item, Integer> getMenuItems()
	{
		Map<Item, Integer> items = new LinkedHashMap<>() ;
		
		switch (menu)
		{
			case 0: pot.entrySet().forEach(item -> items.put(item.getKey(), item.getValue())) ; break ;
			case 1: alch.entrySet().forEach(item -> items.put(item.getKey(), item.getValue())) ; break ;
			case 2: forges.entrySet().forEach(item -> items.put(item.getKey(), item.getValue())) ; break ;
			case 3: petItems.entrySet().forEach(item -> items.put(item.getKey(), item.getValue())) ; break ;
			case 4: foods.entrySet().forEach(item -> items.put(item.getKey(), item.getValue())) ; break ;
			case 5: arrows.entrySet().forEach(item -> items.put(item.getKey(), item.getValue())) ; break ;
			case 6: equips.entrySet().forEach(item -> items.put(item.getKey(), item.getValue())) ; break ;
			case 7: genItems.entrySet().forEach(item -> items.put(item.getKey(), item.getValue())) ; break ;
			case 8: fabItems.entrySet().forEach(item -> items.put(item.getKey(), item.getValue())) ; break ;
			case 9: questItems.entrySet().forEach(item -> items.put(item.getKey(), item.getValue())) ; break ;
			default: return null ;
		}

		return items ;
	}
	
	private Item[] getMenuArrayItems()
	{
		switch (menu)
		{
			case 0: return Potion.getAll() ;
			case 1: return Alchemy.getAll() ;
			case 2: return Forge.getAll() ;
			case 3: return PetItem.getAll() ;
			case 4: return Food.getAll() ;
			case 5: return Arrow.getAll() ;
			case 6: return Equip.getAll() ;
			case 7: return GeneralItem.getAll() ;
			case 8: return Fab.getAll() ;
			case 9: return QuestItem.getAll() ;
			default: return null ;
		}
	}
	
	public List<Item> getMenuListItems()
	{
		Map<Item, Integer> menuItems = getMenuItems() ;
		return new ArrayList<>(menuItems.keySet()) ;
		 
//		switch (menu)
//		{
//			case 0: return new ArrayList<>(pot.keySet()) ;
//			case 1: return new ArrayList<>(alch.keySet()) ;
//			case 2: return new ArrayList<>(forges.keySet()) ;
//			case 3: return new ArrayList<>(petItems.keySet()) ;
//			case 4: return new ArrayList<>(foods.keySet()) ;
//			case 5: return new ArrayList<>(arrows.keySet()) ;
//			case 6: return new ArrayList<>(equips.keySet()) ;
//			case 7: return new ArrayList<>(genItems.keySet()) ;
//			case 8: return new ArrayList<>(fabItems.keySet()) ;
//			case 9: return new ArrayList<>(questItems.keySet()) ;
//			default: return null ;
//		}
	}
	
	private Map<Item, Integer> removeItemsOutsideWindow(Map<Item, Integer> orderedItems)
	{// TODO só funciona se o jogador tiver os itens iniciais
		List<Item> keySet = getMenuListItems() ;
		for (Item key : keySet)
		{
			if ((window + 1) * numberSlotMax <= key.getId() | key.getId() <= window * numberSlotMax - 1)
			{
				orderedItems.remove(key) ;
			}
		}
		
		return orderedItems ;
	}
	
	private Map<Item, Integer> orderItems(Map<Item, Integer> originalItems)
	{
		
		Map<Item, Integer> orderedItems = new LinkedHashMap<>() ;
		
		for (Item item : getMenuArrayItems())
		{
			if (originalItems.get(item) == null) { continue ;}
			
			orderedItems.put(item, originalItems.get(item)) ;
		}
		
		return orderedItems ;
		
	}
	
	private Map<Item, Integer> getItemsWithAmount()
	{
		return getMenuItems().entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
	}
	
	public Map<Item, Integer> getItemsOnWindow()
	{
		Map<Item, Integer> items = new LinkedHashMap<>() ;
		Map<Item, Integer> orderedItems = new LinkedHashMap<>() ;
		items = getItemsWithAmount() ;
		orderedItems = orderItems(items)  ;
		orderedItems = removeItemsOutsideWindow(orderedItems) ;
		
		return orderedItems ;
	}
	
	public Item getSelectedItem()
	{
		itemsOnWindow = getItemsOnWindow() ;
		numberItems = 0 ;
		
		if (itemsOnWindow.isEmpty()) { return null ;}
		
		numberItems = numberSlotMax * window + Math.min(numberSlotMax, itemsOnWindow.size()) ;
		
		int i = numberSlotMax * window ;
		for (Map.Entry<Item, Integer> activeItem : itemsOnWindow.entrySet())
		{
			if (i != item)
			{
				i += 1 ;
				continue ;
			}	
			
			return activeItem.getKey() ;	
		}
		
		return null ;
	}
	
	public boolean contains(Item item)
	{
		Map<Item, Integer> menuItems = getMenuItems() ;
		
		if (!menuItems.containsKey(item)) { return false ;}
		
		return 1 <= menuItems.get(item) ;
		
//		if (item instanceof Potion)
//		{
//			if (!pot.containsKey(item)) { return false ;}
//			
//			return  1 <= pot.get(item) ;
//		}
//		if (item instanceof Alchemy)
//		{
//			if (!alch.containsKey(item)) { return false ;}
//			
//			return  1 <= alch.get(item) ;
//		}
//		if (item instanceof Forge)
//		{
//			if (!forges.containsKey(item)) { return false ;}
//			
//			return  1 <= forges.get(item) ;
//		}
//		if (item instanceof PetItem)
//		{
//			if (!petItems.containsKey(item)) { return false ;}
//			
//			return  1 <= petItems.get(item) ;
//		}
//		if (item instanceof Food)
//		{
//			if (!foods.containsKey(item)) { return false ;}
//			
//			return  1 <= foods.get(item) ;
//		}
//		if (item instanceof Arrow)
//		{
//			if (!arrows.containsKey(item)) { return false ;}
//			
//			return  1 <= arrows.get(item) ;
//		}
//		if (item instanceof Equip)
//		{
//			if (!equips.containsKey(item)) { return false ;}
//			
//			return  1 <= equips.get(item) ;
//		}
//		if (item instanceof GeneralItem)
//		{
//			if (!genItems.containsKey(item)) { return false ;}
//			
//			return  1 <= genItems.get(item) ;
//		}
//		if (item instanceof Fab)
//		{
//			if (!fabItems.containsKey(item)) { return false ;}
//			
//			return  1 <= fabItems.get(item) ;
//		}
//		if (item instanceof QuestItem)
//		{
//			if (!questItems.containsKey(item)) { return false ;}
//			
//			return  1 <= questItems.get(item) ;
//		}
//		
//		System.out.println("Item procurado na mochila não pertence a uma categoria válida");
//		return false ;
	}
	
	public boolean contains(List<Item> items)
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

		Map<Item, Integer> menuItems = getMenuItems() ;
		return qtd <= menuItems.get(item) ;
		
//		if (item instanceof Potion) { return qtd <= pot.get(item) ;}
//		if (item instanceof Alchemy) { return qtd <= alch.get(item) ;}
//		if (item instanceof Forge) { return qtd <= forges.get(item) ;}
//		if (item instanceof PetItem) { return qtd <= petItems.get(item) ;}
//		if (item instanceof Food) { return qtd <= foods.get(item) ;}
//		if (item instanceof Arrow) { return qtd <= arrows.get(item) ;}
//		if (item instanceof Equip) { return qtd <= equips.get(item) ;}
//		if (item instanceof GeneralItem) { return qtd <= genItems.get(item) ;}
//		if (item instanceof Fab) { return qtd <= fabItems.get(item) ;}
//		if (item instanceof QuestItem) { return qtd <= questItems.get(item) ;}
//
//		System.out.println("Item procurado na mochila não pertence a uma categoria válida");
//		return false ;
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
	
	public void updateWindow()
	{
		item = window * numberSlotMax ;
		itemsOnWindow = getItemsOnWindow() ;
		numberItems = (window + 1) * numberSlotMax ;
		numberWindows = getMenuItems().size() / numberSlotMax ;
	}
	
	public int calcGenItemsValue()
	{
		int value = 0 ;

		for (Item item : genItems.keySet())
		{
			value += item.getPrice() * genItems.get(item) ;
		}
		
		return value ;
	}
	
	public int calcValue()
	{
		int value = 0 ;
		
		for (Item item : pot.keySet())
		{
			value += item.getPrice() * pot.get(item) ;
		}
		for (Item item : alch.keySet())
		{
			value += item.getPrice() * alch.get(item) ;
		}
		for (Item item : forges.keySet())
		{
			value += item.getPrice() * forges.get(item) ;
		}
		for (Item item : petItems.keySet())
		{
			value += item.getPrice() * petItems.get(item) ;
		}
		for (Item item : foods.keySet())
		{
			value += item.getPrice() * foods.get(item) ;
		}
		for (Item item : arrows.keySet())
		{
			value += item.getPrice() * arrows.get(item) ;
		}
		for (Item item : equips.keySet())
		{
			value += item.getPrice() * equips.get(item) ;
		}
		for (Item item : genItems.keySet())
		{
			value += item.getPrice() * genItems.get(item) ;
		}
		for (Item item : fabItems.keySet())
		{
			value += item.getPrice() * fabItems.get(item) ;
		}
		for (Item item : questItems.keySet())
		{
			value += item.getPrice() * questItems.get(item) ;
		}
		
		return value ;
	}
	
	public void display(Point MousePos, String[] allText, DrawingOnPanel DP)
	{
		
		// draw menus
		for (int m = 0 ; m <= allText.length - 1 ; m += 1)
		{
			Point menuPos = UtilG.Translate(windowPos, 0, border + m * MenuImage.getHeight(null)) ;
			menuPos.x += m == menu ? 3 : 0 ; 
			Point textPos = UtilG.Translate(menuPos, 3, MenuImage.getHeight(null) / 2) ;
			Color textColor = getTextColor(m == menu) ;
			Image menuImage = m == menu ? (tab == 0 ? SelectedMenuTab0 : SelectedMenuTab1) : MenuImage ;
			
			DP.DrawImage(menuImage, menuPos, Align.topLeft) ;
			DP.DrawText(textPos, Align.centerLeft, DrawingOnPanel.stdAngle, allText[m], titleFont, textColor) ;
		}
		
		// draw bag
		DP.DrawImage(tab == 0 ? image : SelectedBag, windowPos, Align.topLeft) ;
		
		// draw items
		int slotW = SlotImage.getWidth(null) ;
		int slotH = SlotImage.getHeight(null) ;
		int itemID = window * numberSlotMax ;
		
//		if (!itemsAreOrdered)
//		{
//			itemsOnWindow = getItemsOnWindow() ;
//			itemsAreOrdered = true ;
//		}
		itemsOnWindow = getItemsOnWindow() ;
			
		
		List<Item> itemsDisplayed = new ArrayList<>(itemsOnWindow.keySet()) ;
		List<Integer> amountsDisplayed = new ArrayList<>(itemsOnWindow.values()) ;
		
		for (int i = 0 ; i <= itemsDisplayed.size() - 1; i += 1)
		{
			if ((window + 1) * numberSlotMax <= itemID) { break ;}
			
			int row = (itemID - window * numberSlotMax) % ( numberSlotMax / 2) ;
			int col = (itemID - window * numberSlotMax) / ( numberSlotMax / 2) ;
			Point slotCenter = UtilG.Translate(windowPos, 70 + 6 + slotW / 2 + col * (140 + slotW), border + padding + 2 + slotH / 2 + row * 21) ;
			String itemText = itemsDisplayed.get(i).getName() + " (x " + amountsDisplayed.get(i) + ")" ;
			Point textPos = new Point(slotCenter.x + slotW / 2 + 5, slotCenter.y) ;
			Color textColor = getTextColor(itemID == item) ;
			
			DP.DrawImage(SlotImage, slotCenter, Align.center) ;
			DP.DrawImage(itemsDisplayed.get(i).getImage(), slotCenter, Align.center) ;
			DP.DrawTextUntil(textPos, Align.centerLeft, DrawingOnPanel.stdAngle, itemText, stdFont, textColor, 10, MousePos) ;
			itemID += 1 ;
		}
		
		DP.DrawWindowArrows(UtilG.Translate(windowPos, 0, size.height + 5), size.width, window, numberWindows) ;
		
	}
	
	@Override
	public String toString() {
		return "Bag [pot=" + pot + ", alch=" + alch + ", forge=" + forges + ", petItem=" + petItems + ", food=" + foods
				+ ", arrow=" + arrows + ", equip=" + equips + ", genItem=" + genItems + ", fab=" + fabItems + ", quest=" + questItems
				+ "]";
	}
	
}
