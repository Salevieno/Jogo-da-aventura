package windows;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
	private Map<Item, Integer> itemsOnWindow ;
	private int gold ;
	
	private Point windowPos = new Point((int)(0.3 * Game.getScreen().getSize().width), (int)(0.48 * Game.getScreen().getSize().height)) ;

	public static Image SelectedBag = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "BagSelected.png") ;
	public static Image MenuImage = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "BagMenu.png") ;
	public static Image SelectedMenuTab0 = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "BagSelectedMenuTab0.png") ;
	public static Image SelectedMenuTab1 = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "BagSelectedMenuTab1.png") ;
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
		itemsOnWindow = new HashMap<>() ;
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
		// TODO if bag is open bag.orderItems() ;
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
			if (pot.get(potion) == amount) { pot.remove(potion) ; item = null ; tab = 0 ; return ;}
			
			pot.put(potion, pot.get(potion) - amount) ;
		}
		if (item instanceof Alchemy)
		{
			Alchemy alchemy = (Alchemy) item ;
			if (!alch.containsKey(alchemy)) { return ;}
			if (alch.get(alchemy) < amount) { return ;}
			if (alch.get(alchemy) == amount) { alch.remove(alchemy) ; item = null ; tab = 0 ; return ;}
			
			alch.put(alchemy, alch.get(alchemy) - amount) ;
		}
		if (item instanceof Forge)
		{
			Forge forge = (Forge) item ;
			if (!forges.containsKey(forge)) { return ;}
			if (forges.get(forge) < amount) { return ;}
			if (forges.get(forge) == amount) { forges.remove(forge) ; item = null ; tab = 0 ; return ;}
			
			forges.put(forge, forges.get(forge) - amount) ;
		}
		if (item instanceof PetItem)
		{
			PetItem petItem = (PetItem) item ;
			if (!petItems.containsKey(petItem)) { return ;}
			if (petItems.get(petItem) < amount) { return ;}
			if (petItems.get(petItem) == amount) { petItems.remove(petItem) ; item = null ; tab = 0 ; return ;}
			
			petItems.put(petItem, petItems.get(petItem) - amount) ;
		}
		if (item instanceof Food)
		{
			Food food = (Food) item ;
			if (!foods.containsKey(food)) { return ;}
			if (foods.get(food) < amount) { return ;}
			if (foods.get(food) == amount) { foods.remove(food) ; item = null ; tab = 0 ; return ;}
			
			foods.put(food, foods.get(food) - amount) ;
		}
		if (item instanceof Arrow)
		{
			Arrow arrow = (Arrow) item ;
			if (!arrows.containsKey(arrow)) { return ;}
			if (arrows.get(arrow) < amount) { return ;}
			if (arrows.get(arrow) == amount) { arrows.remove(arrow) ; item = null ; tab = 0 ; return ;}
			
			arrows.put(arrow, arrows.get(arrow) - amount) ;
		}
		if (item instanceof Equip)
		{
			Equip equip = (Equip) item ;
			if (!equips.containsKey(equip)) { return ;}
			if (equips.get(equip) < amount) { return ;}
			if (equips.get(equip) == amount) { equips.remove(equip) ; item = null ; tab = 0 ; return ;}
			
			equips.put(equip, equips.get(equip) - amount) ;
		}
		if (item instanceof GeneralItem)
		{
			GeneralItem genItem = (GeneralItem) item ;
			if (!genItems.containsKey(genItem)) { return ;}
			if (genItems.get(genItem) < amount) { return ;}
			if (genItems.get(genItem) == amount) { genItems.remove(genItem) ; item = null ; tab = 0 ; return ;}
			
			genItems.put(genItem, genItems.get(genItem) - amount) ;
		}
		if (item instanceof Fab)
		{
			Fab fab = (Fab) item ;
			if (!fabItems.containsKey(fab)) { return ;}
			if (fabItems.get(fab) < amount) { return ;}
			if (fabItems.get(fab) == amount) { fabItems.remove(fab) ; item = null ; tab = 0 ; return ;}
			
			fabItems.put(fab, fabItems.get(fab) - amount) ;
		}
		if (item instanceof QuestItem)
		{
			QuestItem questItem = (QuestItem) item ;
			if (!questItems.containsKey(questItem)) { return ;}
			if (questItems.get(questItem) < amount) { return ;}
			if (questItems.get(questItem) == amount) { questItems.remove(questItem) ; item = null ; tab = 0 ; return ;}
			
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
	private Map<Potion, Integer> orderItems(Map<Potion, Integer> pot)
	{
		Map<Potion, Integer> orderedItems = new LinkedHashMap<>() ;
		Set<Potion> pots = pot.keySet() ;
		List<Integer> potIds = new ArrayList<>() ;
		pots.forEach(p -> potIds.add(p.getId())) ;
		potIds.sort(Comparator.naturalOrder()) ;
		
		for (int i = 0; i <= pot.entrySet().size() - 1 ; i += 1)
		{
			orderedItems.put(Potion.getAll()[potIds.get(i)], pot.get(Potion.getAll()[potIds.get(i)])) ;
		}
		
		return orderedItems ;
	}
	private Map<Equip, Integer> orderEquips(Map<Equip, Integer> pot)
	{
		Map<Equip, Integer> orderedItems = new LinkedHashMap<>() ;
		Set<Equip> pots = pot.keySet() ;
		List<Integer> potIds = new ArrayList<>() ;
		pots.forEach(p -> potIds.add(p.getId())) ;
		potIds.sort(Comparator.naturalOrder());
		
		for (int i = 0; i <= pot.entrySet().size() - 1 ; i += 1)
		{
			orderedItems.put(Equip.getAll()[potIds.get(i)], pot.get(Equip.getAll()[potIds.get(i)])) ;
		}
		
		return orderedItems ;
	}
	
	public Map<Item, Integer> getItemsOnWindow()
	{
		// pega os itens em ordem maior que 0 e mapeia em ordem aleatória e mostra
		// 
		
		// [0, 21, 45, 32, 44, 10]
		// [0, 10, 21, 32, 44]
		// [45]
		Map<Item, Integer> items = new LinkedHashMap<>() ;
		
		switch (menu)
		{
			case 0: 
			{
				pot = orderItems(pot) ;
				return pot.entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
			}
			case 1: return alch.entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
			case 2: return forges.entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
			case 3: return petItems.entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
			case 4: return foods.entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
			case 5: return arrows.entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
			case 6: 
			{
				equips = orderEquips(equips) ;
				items = equips.entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
				
				break ;
			}
			case 7: return genItems.entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
			case 8: return fabItems.entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
			case 9: return questItems.entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
			default: return null ;
		}
		
		List<Item> listItems = new ArrayList<>(items.keySet()) ;		
		for (int i = 0 ; i <= items.size() - 1 ; i += 1)
		{
			if (i < window * numberSlotMax)
			{
				items.remove(listItems.get(i)) ;
			}
		}
		
		return items ;
	}
	
	public Item getSelectedItem()
	{
		selectedItem = null ;
		getItemsOnWindow() ;
		numberItems = 0 ;
		if (!itemsOnWindow.isEmpty())
		{
			numberItems = Math.min(numberSlotMax, itemsOnWindow.size() - numberSlotMax * window) ;
		}
		
		int i = 0 ;
		for (Map.Entry<Item, Integer> activeItem : itemsOnWindow.entrySet())
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
		if (item instanceof Potion)
		{
			if (!pot.containsKey(item)) { return false ;}
			
			return  1 <= pot.get(item) ;
		}
		if (item instanceof Alchemy)
		{
			if (!alch.containsKey(item)) { return false ;}
			
			return  1 <= alch.get(item) ;
		}
		if (item instanceof Forge)
		{
			if (!forges.containsKey(item)) { return false ;}
			
			return  1 <= forges.get(item) ;
		}
		if (item instanceof PetItem)
		{
			if (!petItems.containsKey(item)) { return false ;}
			
			return  1 <= petItems.get(item) ;
		}
		if (item instanceof Food)
		{
			if (!foods.containsKey(item)) { return false ;}
			
			return  1 <= foods.get(item) ;
		}
		if (item instanceof Arrow)
		{
			if (!arrows.containsKey(item)) { return false ;}
			
			return  1 <= arrows.get(item) ;
		}
		if (item instanceof Equip)
		{
			if (!equips.containsKey(item)) { return false ;}
			
			return  1 <= equips.get(item) ;
		}
		if (item instanceof GeneralItem)
		{
			if (!genItems.containsKey(item)) { return false ;}
			
			return  1 <= genItems.get(item) ;
		}
		if (item instanceof Fab)
		{
			if (!fabItems.containsKey(item)) { return false ;}
			
			return  1 <= fabItems.get(item) ;
		}
		if (item instanceof QuestItem)
		{
			if (!questItems.containsKey(item)) { return false ;}
			
			return  1 <= questItems.get(item) ;
		}
		
		System.out.println("Item procurado na mochila não pertence a uma categoria válida");
		return false ;
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
		if (tab == 0)
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
				itemDown() ;
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

	public void updateWindow()
	{
		item = window * numberSlotMax ;
		itemsOnWindow = getItemsOnWindow() ;
		numberItems = (window + 1) * numberSlotMax ;
		switch (menu)
		{
			case 0: numberWindows = pot.size() / numberSlotMax ; break ;
			case 1: numberWindows = alch.size() / numberSlotMax ; break ;
			case 2: numberWindows = forges.size() / numberSlotMax ; break ;
			case 3: numberWindows = petItems.size() / numberSlotMax ; break ;
			case 4: numberWindows = foods.size() / numberSlotMax ; break ;
			case 5: numberWindows = arrows.size() / numberSlotMax ; break ;
			case 6: numberWindows = equips.size() / numberSlotMax ; break ;
			case 7: numberWindows = genItems.size() / numberSlotMax ; break ;
			case 8: numberWindows = fabItems.size() / numberSlotMax ; break ;
			case 9: numberWindows = questItems.size() / numberSlotMax ; break ;
			default: return ;
		}
		
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
		itemsOnWindow = getItemsOnWindow() ;
		for (Map.Entry<Item, Integer> activeItem : itemsOnWindow.entrySet())
		{
			if ((window + 1) * numberSlotMax <= itemID) { break ;}
			
			int row = (itemID - window * numberSlotMax) % ( numberSlotMax / 2) ;
			int col = (itemID - window * numberSlotMax) / ( numberSlotMax / 2) ;
			Point slotCenter = UtilG.Translate(windowPos, 70 + 6 + slotW / 2 + col * (140 + slotW), border + padding + 2 + slotH / 2 + row * 21) ;
			String itemText = activeItem.getKey().getName() + " (x " + activeItem.getValue() + ")" ;
			Point textPos = new Point(slotCenter.x + slotW / 2 + 5, slotCenter.y) ;
			Color textColor = getTextColor(itemID == item) ;
			
			DP.DrawImage(SlotImage, slotCenter, Align.center) ;
			DP.DrawImage(activeItem.getKey().getImage(), slotCenter, Align.center) ;
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
