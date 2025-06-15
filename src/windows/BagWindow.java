package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import UI.GameButton;
import graphics.Align;
import graphics2.Draw;
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
import main.GamePanel;
import main.TextCategories;
import utilities.Util;
import utilities.UtilS;

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
	private Item itemFetched ;
	
	private final List<GameButton> buttons ;
	

	private static final Point windowPos = Game.getScreen().pos(0.28, 0.4) ;
	private static final int numberSlotMax ;
	private static final List<Point> itemsPos ;
	private static final Point spacing = new Point(161, 21) ;
	private static final Dimension itemNameSize = new Dimension(140, 10) ;
	private static final Image bagImage = UtilS.loadImage("\\Windows\\" + "Bag.png") ;
	private static final Image selectedBag = UtilS.loadImage("\\Windows\\" + "BagSelected.png") ;
	private static final Image menuImage = UtilS.loadImage("\\Windows\\" + "BagMenu.png") ;
	private static final Image selectedMenuTab0 = UtilS.loadImage("\\Windows\\" + "BagSelectedMenuTab0.png") ;
	private static final Image selectedMenuTab1 = UtilS.loadImage("\\Windows\\" + "BagSelectedMenuTab1.png") ;
	public static final Image slotImage = UtilS.loadImage("\\Windows\\" + "BagSlot.png") ;
	public static final Image selectedSlotImage = UtilS.loadImage("\\Windows\\" + "BagSelectedSlot.png") ;
	
	static
	{
		numberSlotMax = 20 ;
		itemsPos = new ArrayList<>() ;
		for (int i = 0 ; i <= numberSlotMax - 1; i += 1)
		{
			itemsPos.add(calcSlotCenter(i)) ;
		}
//		itemsPos = List.of(new Point()) ;
	}
	
    public BagWindow()
	{
    	super("Mochila", windowPos, bagImage, 2, 10, 0, 0) ;
		buttons = List.of(windowUpButton(new Point(windowPos.x + bagImage.getWidth(null) - 10, windowPos.y + bagImage.getHeight(null) + 10), Align.topLeft),
				windowDownButton(new Point(windowPos.x + 10, windowPos.y + bagImage.getHeight(null) + 10), Align.topLeft)) ;


		this.pot = new LinkedHashMap<Potion, Integer>() ;
		this.alch = new LinkedHashMap<Alchemy, Integer>() ;
		this.forges = new LinkedHashMap<Forge, Integer>() ;
		this.petItems = new LinkedHashMap<PetItem, Integer>() ;
		this.foods = new LinkedHashMap<Food, Integer>() ;
		this.arrows = new LinkedHashMap<Arrow, Integer>() ;
		this.equips = new LinkedHashMap<Equip, Integer>() ;
		this.genItems = new LinkedHashMap<GeneralItem, Integer>() ;
		this.fabItems = new LinkedHashMap<Fab, Integer>() ;
		this.questItems = new LinkedHashMap<QuestItem, Integer>() ;
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
	public Item getItemFetched() { return itemFetched ;}
	
	public void setItemFetched(Item itemFetched) { this.itemFetched = itemFetched ;}

	private static Point calcSlotCenter(int itemID)
	{
		int row = itemID % ( numberSlotMax / 2) ;
		int col = itemID / ( numberSlotMax / 2) ;
		int slotW = slotImage.getWidth(null) ;
		int slotH = slotImage.getHeight(null) ;
		Point offset = new Point(70 + border + slotW / 2, border + padding + 2 + slotH / 2) ;
		return Util.Translate(windowPos, offset.x + col * spacing.x, offset.y + row * spacing.y) ;
	}
	
	public void navigate(String action)
	{
		if (menu == 0)
		{
			if (action.equals(stdMenuDown))
			{
				tabUp() ;
				window = 0 ;
				updateWindow() ;
			}
			if (action.equals(stdMenuUp))
			{
				tabDown() ;
				window = 0 ;
				updateWindow() ;
			}			
			if (actionIsForward(action))
			{
				menuUp() ;
			}
		}
		if (menu == 1)
		{
			if (action.equals(stdMenuDown))
			{
				itemUp() ;
			}
			if (action.equals(stdMenuUp))
			{
				if (numberSlotMax * window + 1 <= item)
				{
					itemDown() ;
				}
			}
			if (action.equals(stdWindowUp))
			{
				windowUp() ;
				updateWindow() ;
			}
			if (action.equals(stdWindowDown))
			{
				windowDown() ;
				updateWindow() ;
			}
			if (action.equals(stdExit) | action.equals(stdReturn))
			{
				menuDown() ;
			}
		}
	}
	
	public void act(String action, Point mousePos, Player player)
	{
		buttons.forEach(button -> { if (button.isClicked(mousePos, action)) {button.act() ;}}) ;
		
		if (menu == 0) { return ;}
		
		if (menu == 1 & actionIsForward(action))
		{
			player.useItem(getSelectedItem()) ;
		}
		
	}
	
	public void add(Item item, int amount)
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

	public void remove(Item item, int amount)
	{

		if (item == null) { System.out.println("Tentando remover item nulo") ; return ;}
		if (!contains(item)) { System.out.println("Tentando remover um item que não existe na mochila") ; return ;}
		if (!hasEnough(item, amount)) { System.out.println("Tentando remover mais itens do que a quantidade existente na mochila") ; return ;}
		
		if (item instanceof Potion)
		{
			Potion potion = (Potion) item ;
			if (pot.get(potion) == amount) { pot.remove(potion) ; item = null ; menu = 0 ; return ;}
			
			pot.put(potion, pot.get(potion) - amount) ;
		}
		if (item instanceof Alchemy)
		{
			Alchemy alchemy = (Alchemy) item ;
			if (alch.get(alchemy) == amount) { alch.remove(alchemy) ; item = null ; menu = 0 ; return ;}
			
			alch.put(alchemy, alch.get(alchemy) - amount) ;
		}
		if (item instanceof Forge)
		{
			Forge forge = (Forge) item ;
			if (forges.get(forge) == amount) { forges.remove(forge) ; item = null ; menu = 0 ; return ;}
			
			forges.put(forge, forges.get(forge) - amount) ;
		}
		if (item instanceof PetItem)
		{
			PetItem petItem = (PetItem) item ;
			if (petItems.get(petItem) == amount) { petItems.remove(petItem) ; item = null ; menu = 0 ; return ;}
			
			petItems.put(petItem, petItems.get(petItem) - amount) ;
		}
		if (item instanceof Food)
		{
			Food food = (Food) item ;
			if (foods.get(food) == amount) { foods.remove(food) ; item = null ; menu = 0 ; return ;}
			
			foods.put(food, foods.get(food) - amount) ;
		}
		if (item instanceof Arrow)
		{
			Arrow arrow = (Arrow) item ;
			if (arrows.get(arrow) == amount) { arrows.remove(arrow) ; item = null ; menu = 0 ; return ;}
			
			arrows.put(arrow, arrows.get(arrow) - amount) ;
		}
		if (item instanceof Equip)
		{
			Equip equip = (Equip) item ;
			if (equips.get(equip) == amount) { equips.remove(equip) ; item = null ; menu = 0 ; return ;}
			
			equips.put(equip, equips.get(equip) - amount) ;
		}
		if (item instanceof GeneralItem)
		{
			GeneralItem genItem = (GeneralItem) item ;
			if (genItems.get(genItem) == amount) { genItems.remove(genItem) ; item = null ; menu = 0 ; return ;}
			
			genItems.put(genItem, genItems.get(genItem) - amount) ;
		}
		if (item instanceof Fab)
		{
			Fab fab = (Fab) item ;
			if (fabItems.get(fab) == amount) { fabItems.remove(fab) ; item = null ; menu = 0 ; return ;}
			
			fabItems.put(fab, fabItems.get(fab) - amount) ;
		}
		if (item instanceof QuestItem)
		{
			QuestItem questItem = (QuestItem) item ;
			if (questItems.get(questItem) == amount) { questItems.remove(questItem) ; item = null ; menu = 0 ; return ;}
			
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
		
		switch (tab)
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
	
	public Map<Item, Integer> getAllItems()
	{
		Map<Item, Integer> items = new LinkedHashMap<>() ;
		
		pot.entrySet().forEach(item -> items.put(item.getKey(), item.getValue())) ;
		alch.entrySet().forEach(item -> items.put(item.getKey(), item.getValue())) ;
		forges.entrySet().forEach(item -> items.put(item.getKey(), item.getValue())) ;
		petItems.entrySet().forEach(item -> items.put(item.getKey(), item.getValue())) ;
		foods.entrySet().forEach(item -> items.put(item.getKey(), item.getValue())) ;
		arrows.entrySet().forEach(item -> items.put(item.getKey(), item.getValue())) ;
		equips.entrySet().forEach(item -> items.put(item.getKey(), item.getValue())) ;
		genItems.entrySet().forEach(item -> items.put(item.getKey(), item.getValue())) ;
		fabItems.entrySet().forEach(item -> items.put(item.getKey(), item.getValue())) ;
		questItems.entrySet().forEach(item -> items.put(item.getKey(), item.getValue())) ;

		return items ;
	}
	
 	private Item[] getMenuArrayItems()
	{
		switch (tab)
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
		Map<Item, Integer> tabItems = getMenuItems() ;
		return new ArrayList<>(tabItems.keySet()) ;
	}
	
	private Map<Item, Integer> removeItemsOutsideWindow(Map<Item, Integer> orderedItems)
	{
		List<Item> keySet = getMenuListItems() ;
		for (int i = 0 ; i <= keySet.size() - 1; i += 1)
		{
			if ((window + 1) * numberSlotMax <= i | i <= window * numberSlotMax - 1)
			{
				orderedItems.remove(keySet.get(i)) ;
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

	public int getAmount(Item item)
	{
		Map<Item, Integer> tabItems = getAllItems() ;

		if (!tabItems.containsKey(item)) { return 0 ;}
		
		return tabItems.get(item) ;

	}
	
	public boolean contains(Item item)
	{
		Map<Item, Integer> tabItems = getAllItems() ;

		if (!tabItems.containsKey(item)) { return false ;}
		
		return 1 <= tabItems.get(item) ;

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

		Map<Item, Integer> allItems = getAllItems() ;

		return qtd <= allItems.get(item) ;
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
		
	public Item itemHovered(Point mousePos)
	{
		itemsOnWindow = getItemsOnWindow() ;		
		List<Item> itemsDisplayed = new ArrayList<>(itemsOnWindow.keySet()) ;
		int numberItemsDisplayed = Math.min(numberSlotMax, itemsDisplayed.size()) ;

		for (int i = 0 ; i <= numberItemsDisplayed - 1; i += 1)
		{
			Point slotCenter = itemsPos.get(i) ;
			Point slotCenterLeft = Util.getPosAt(slotCenter, Align.center, Align.centerLeft, Util.getSize(slotImage)) ;
			if (Util.isInside(mousePos, slotCenterLeft, itemNameSize)) { return itemsDisplayed.get(i) ;}
		}
		
		return null ;
	}
	
	private void checkMenuMouseSelection(Point mousePos, Point tabPos, int tabID)
	{
		if (!Util.isInside(mousePos, tabPos, Util.getSize(menuImage))) { return ;}
		
		item = 0 ;
		window = 0 ;
		tab = tabID ;
	}
	
	public void display(Point mousePos)
	{
		String[] tabNames = Game.allText.get(TextCategories.bagMenus) ;
		
		// draw tabs
		for (int m = 0 ; m <= tabNames.length - 1 ; m += 1)
		{
			Point tabPos = Util.Translate(windowPos, 0, border + m * menuImage.getHeight(null)) ;
			tabPos.x += m == tab ? 3 : 0 ; 
			Point textPos = Util.Translate(tabPos, 3, menuImage.getHeight(null) / 2) ;
			Color textColor = getTextColor(m == tab) ;
			Image tabImage = m == tab ? (menu == 0 ? selectedMenuTab0 : selectedMenuTab1) : menuImage ;
			checkMenuMouseSelection(mousePos, tabPos, m) ;
			
			GamePanel.DP.drawImage(tabImage, tabPos, Align.topLeft) ;
			GamePanel.DP.drawText(textPos, Align.centerLeft, Draw.stdAngle, tabNames[m], titleFont, textColor) ;
		}
		
		// draw bag
		GamePanel.DP.drawImage(menu == 0 ? image : selectedBag, windowPos, Align.topLeft) ;
		
		// draw items		
		itemsOnWindow = getItemsOnWindow() ;		
		List<Item> itemsDisplayed = new ArrayList<>(itemsOnWindow.keySet()) ;
		List<Integer> amountsDisplayed = new ArrayList<>(itemsOnWindow.values()) ;
		int numberItemsDisplayed = Math.min(numberSlotMax, itemsDisplayed.size()) ;
		
		for (int i = 0 ; i <= numberItemsDisplayed - 1; i += 1)
		{
			int itemID = i + window * numberSlotMax ;
			Point slotCenter = itemsPos.get(i) ;
			Point slotCenterLeft = Util.getPosAt(slotCenter, Align.center, Align.centerLeft, Util.getSize(slotImage)) ;
			String itemText = itemsDisplayed.get(i).getName() + " (x " + amountsDisplayed.get(i) + ")" ;
			Point textPos = Util.Translate(slotCenterLeft, slotImage.getWidth(null) + 5, 0) ;
			checkMouseSelection(mousePos, slotCenterLeft, Align.centerLeft, itemNameSize, itemID) ;
			Color textColor = getTextColor(itemID == item) ;
			
			GamePanel.DP.drawImage(slotImage, slotCenter, Align.center) ;
			GamePanel.DP.drawImage(itemsDisplayed.get(i).getImage(), slotCenter, Align.center) ;
			Draw.textUntil(textPos, Align.centerLeft, Draw.stdAngle, itemText, stdFont, textColor, 10, mousePos) ;
		}
		
		if (0 < numberItemsDisplayed)
		{
			Item selectedItem = itemsDisplayed.get(item - window * numberSlotMax) ;
			if (selectedItem instanceof Equip || selectedItem instanceof GeneralItem)
			{
				selectedItem.displayInfo(windowPos, Align.topRight) ;
			}
		}
		
		buttons.forEach(button -> button.display(Draw.stdAngle, false, mousePos)) ;
		
	}
	
	@Override
	public String toString()
	{
		return "pot = " + pot + "\n" +
				"alchemy = " + alch + "\n" +
				"forge = " + forges + "\n" +
				"petItems = " + petItems + "\n" +
				"food = " + foods + "\n" +
				"arrows = " + arrows + "\n" +
				"equips = " + equips + "\n" +
				"genItems = " + genItems + "\n" +
				"fab = " + fabItems + "\n" +
				"quest = " + questItems + "\n" ;
	}

	public void setClothOnFire()
	{
		Set<Item> generalItems = Set.copyOf(genItems.keySet()) ;
		for (Item item : generalItems)
		{
			if (item.getId() == 114 | item.getId() == 119)
			{
				int amount = genItems.get(item) ;
				remove(item, amount) ;
				add(GeneralItem.getAll()[115], amount) ;
			}
		}
	}
}
