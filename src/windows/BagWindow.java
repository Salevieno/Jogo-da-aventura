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

import graphics.Align;
import graphics.UtilAlignment;
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
import main.ImageLoader;
import main.Log;
import main.Path;
import main.TextCategories;
import screen.Screen;
import utilities.Util;


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
	private final List<Point> itemPos ;

	private static final int QTD_SLOTS_PER_WINDOW = 20 ;
	private static final Point SPACING = new Point(300, 35) ;
	private static final Image IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "Bag.png") ;
	private static final Dimension ITEM_NAME_SIZE = new Dimension(140, 10) ;
	private static final Image SELECTED_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "BagSelected.png") ;
	private static final Image MENU_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "BagMenu.png") ;
	private static final Image SELECTED_MENU_TAB_0_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "BagSelectedMenuTab0.png") ;
	private static final Image SELECTED_MENU_TAB_1_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "BagSelectedMenuTab1.png") ;

    public BagWindow()
	{
    	super("Mochila", Screen.getMe().pos(0.28, 0.4), IMAGE, 2, 10, 0, 0) ;
		this.buttons = List.of(windowUpButton(new Point(topLeftPos.x + image.getWidth(null) - 10, topLeftPos.y + image.getHeight(null) + 10), Align.topLeft),
				windowDownButton(new Point(topLeftPos.x + 10, topLeftPos.y + image.getHeight(null) + 10), Align.topLeft)) ;
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
		this.itemsOnWindow = new LinkedHashMap<>() ;
		this.gold = 0 ;

		
		int slotW = SLOT_IMAGE.getWidth(null) ;
		int slotH = SLOT_IMAGE.getHeight(null) ;
		Point offset = new Point(70 + BORDER + slotW / 2, BORDER + PADDING + 2 + slotH / 2) ;
		itemPos = new ArrayList<>() ;
		for (int i = 0 ; i <= QTD_SLOTS_PER_WINDOW - 1; i += 1)
		{
			int row = i % (QTD_SLOTS_PER_WINDOW / 2) ;
			int col = i / (QTD_SLOTS_PER_WINDOW / 2) ;
			itemPos.add(Util.translate(topLeftPos, offset.x + col * SPACING.x, offset.y + row * SPACING.y)) ;
		}
		
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
				if (QTD_SLOTS_PER_WINDOW * window + 1 <= item)
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
			numberWindows = pot.size() / QTD_SLOTS_PER_WINDOW ;
		}
		if (item instanceof Alchemy)
		{
			if (alch.containsKey(item)) { alch.put((Alchemy) item, alch.get((Alchemy) item) + amount) ;}
			else { alch.put((Alchemy) item, amount) ;}
			numberItems = alch.size() ;
			numberWindows = alch.size() / QTD_SLOTS_PER_WINDOW ;
		}
		if (item instanceof Forge)
		{
			if (forges.containsKey(item)) { forges.put((Forge) item, forges.get((Forge) item) + amount) ;}
			else { forges.put((Forge) item, amount) ;}
			numberItems = forges.size() ;
			numberWindows = forges.size() / QTD_SLOTS_PER_WINDOW ;
		}
		if (item instanceof PetItem)
		{
			if (petItems.containsKey(item)) { petItems.put((PetItem) item, petItems.get((PetItem) item) + amount) ;}
			else { petItems.put((PetItem) item, amount) ;}
			numberItems = petItems.size() ;
			numberWindows = petItems.size() / QTD_SLOTS_PER_WINDOW ;
		}
		if (item instanceof Food)
		{
			if (foods.containsKey(item)) { foods.put((Food) item, foods.get((Food) item) + amount) ;}
			else { foods.put((Food) item, amount) ;}
			numberItems = foods.size() ;
			numberWindows = foods.size() / QTD_SLOTS_PER_WINDOW ;
		}
		if (item instanceof Arrow)
		{
			if (arrows.containsKey(item)) { arrows.put((Arrow) item, arrows.get((Arrow) item) + amount) ;}
			else { arrows.put((Arrow) item, amount) ;}
			numberItems = arrows.size() ;
			numberWindows = arrows.size() / QTD_SLOTS_PER_WINDOW ;
		}
		if (item instanceof GeneralItem)
		{
			if (genItems.containsKey(item)) { genItems.put((GeneralItem) item, genItems.get((GeneralItem) item) + amount) ;}
			else { genItems.put((GeneralItem) item, amount) ;}
			numberItems = genItems.size() ;
			numberWindows = genItems.size() / QTD_SLOTS_PER_WINDOW ;
		}
		if (item instanceof Equip)
		{
			if (equips.containsKey(item)) { equips.put((Equip) item, equips.get((Equip) item) + amount) ;}
			else { equips.put((Equip) item, amount) ;}
			numberItems = equips.size() ;
			numberWindows = equips.size() / QTD_SLOTS_PER_WINDOW ;
		}
		if (item instanceof Fab)
		{
			if (fabItems.containsKey(item)) { fabItems.put((Fab) item, fabItems.get((Fab) item) + amount) ;}
			else { fabItems.put((Fab) item, amount) ;}
			numberItems = fabItems.size() ;
			numberWindows = fabItems.size() / QTD_SLOTS_PER_WINDOW ;
		}
		if (item instanceof QuestItem)
		{
			if (questItems.containsKey(item)) { questItems.put((QuestItem) item, questItems.get((QuestItem) item) + amount) ;}
			else { questItems.put((QuestItem) item, amount) ;}
			numberItems = questItems.size() ;
			numberWindows = questItems.size() / QTD_SLOTS_PER_WINDOW ;
		}
	}

	public void remove(Item item, int amount)
	{

		if (item == null) { Log.warn("Tentando remover item nulo") ; return ;}
		if (!contains(item)) { Log.warn("Tentando remover um item que não existe na mochila") ; return ;}
		if (!hasEnough(item, amount)) { Log.warn("Tentando remover mais itens do que a quantidade existente na mochila") ; return ;}
		
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
		
		Log.warn("Tentando remover mais dinheiro do que o existente na mochila") ;
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
			if ((window + 1) * QTD_SLOTS_PER_WINDOW <= i | i <= window * QTD_SLOTS_PER_WINDOW - 1)
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
		
		numberItems = QTD_SLOTS_PER_WINDOW * window + Math.min(QTD_SLOTS_PER_WINDOW, itemsOnWindow.size()) ;
		
		int i = QTD_SLOTS_PER_WINDOW * window ;
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
		item = window * QTD_SLOTS_PER_WINDOW ;
		itemsOnWindow = getItemsOnWindow() ;
		numberItems = (window + 1) * QTD_SLOTS_PER_WINDOW ;
		numberWindows = getMenuItems().size() / QTD_SLOTS_PER_WINDOW ;
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
		int numberItemsDisplayed = Math.min(QTD_SLOTS_PER_WINDOW, itemsDisplayed.size()) ;

		for (int i = 0 ; i <= numberItemsDisplayed - 1; i += 1)
		{
			Point slotCenter = itemPos.get(i) ;
			Point slotCenterLeft = UtilAlignment.getPosAt(slotCenter, Align.center, Align.centerLeft, Util.getSize(SLOT_IMAGE)) ;
			if (Util.isInside(mousePos, slotCenterLeft, ITEM_NAME_SIZE))
			{
				return itemsDisplayed.get(i) ;
			}
		}
		
		return null ;
	}
	
	private void checkMenuMouseSelection(Point mousePos, Point tabPos, int tabID)
	{
		if (!Util.isInside(mousePos, tabPos, Util.getSize(MENU_IMAGE))) { return ;}
		
		item = 0 ;
		window = 0 ;
		tab = tabID ;
	}
	
	public void display(Point mousePos)
	{
		String[] tabNames = Game.getAllText().get(TextCategories.bagMenus) ;
		
		// draw tabs
		for (int m = 0 ; m <= tabNames.length - 1 ; m += 1)
		{
			Point tabPos = Util.translate(topLeftPos, 0, BORDER + m * MENU_IMAGE.getHeight(null)) ;
			tabPos.x += m == tab ? 3 : 0 ; 
			Point textPos = Util.translate(tabPos, 3, MENU_IMAGE.getHeight(null) / 2) ;
			Color textColor = getTextColor(m == tab) ;
			Image tabImage = m == tab ? (menu == 0 ? SELECTED_MENU_TAB_0_IMAGE : SELECTED_MENU_TAB_1_IMAGE) : MENU_IMAGE ;
			checkMenuMouseSelection(mousePos, tabPos, m) ;
			
			GamePanel.getDP().drawImage(tabImage, tabPos, Align.topLeft) ;
			GamePanel.getDP().drawText(textPos, Align.centerLeft, Draw.stdAngle, tabNames[m], TITLE_FONT, textColor) ;
		}
		
		// draw bag
		GamePanel.getDP().drawImage(menu == 0 ? image : SELECTED_IMAGE, topLeftPos, Align.topLeft) ;
		
		// draw items		
		itemsOnWindow = getItemsOnWindow() ;		
		List<Item> itemsDisplayed = new ArrayList<>(itemsOnWindow.keySet()) ;
		List<Integer> amountsDisplayed = new ArrayList<>(itemsOnWindow.values()) ;
		int numberItemsDisplayed = Math.min(QTD_SLOTS_PER_WINDOW, itemsDisplayed.size()) ;
		
		for (int i = 0 ; i <= numberItemsDisplayed - 1; i += 1)
		{
			int itemID = i + window * QTD_SLOTS_PER_WINDOW ;
			Point slotCenter = itemPos.get(i) ;
			Point slotCenterLeft = UtilAlignment.getPosAt(slotCenter, Align.center, Align.centerLeft, Util.getSize(SLOT_IMAGE)) ;
			String itemText = itemsDisplayed.get(i).getName() + " (x " + amountsDisplayed.get(i) + ")" ;
			Point textPos = Util.translate(slotCenterLeft, SLOT_IMAGE.getWidth(null) + 5, 0) ;
			checkMouseSelection(mousePos, slotCenterLeft, Align.centerLeft, ITEM_NAME_SIZE, itemID) ;
			Color textColor = getTextColor(itemID == item) ;
			
			GamePanel.getDP().drawImage(SLOT_IMAGE, slotCenter, Align.center) ;
			GamePanel.getDP().drawImage(itemsDisplayed.get(i).getImage(), slotCenter, Align.center) ;
			Draw.textUntil(textPos, Align.centerLeft, Draw.stdAngle, itemText, TITLE_FONT, textColor, 40, mousePos) ;
		}
		
		if (0 < numberItemsDisplayed)
		{
			Item selectedItem = itemsDisplayed.get(item - window * QTD_SLOTS_PER_WINDOW) ;
			if (selectedItem instanceof Equip || selectedItem instanceof GeneralItem)
			{
				selectedItem.displayInfo(topLeftPos, Align.topRight) ;
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
