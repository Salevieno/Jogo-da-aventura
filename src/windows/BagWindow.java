package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
import main.Palette;
import main.Path;
import main.TextCategories;
import screen.Screen;
import utilities.Util;


public class BagWindow extends GameWindow
{
	private Map<Item, Integer> itemsOnMenu ;
	private Map<Item, Integer> itemsOnPage ;
	private Map<Item, Integer> itemsInBag ;
	private Map<Integer, List<Item>> recentlyUsedItems ;
	private int gold ;
	private Item itemFetched ;
	private final List<Point> itemPos ;
	private final List<Point> recentItemPos ;
    private final List<Point> tabTextPos ;
	private final Point insideTopLeft = Util.translate(topLeftPos, 240, 96) ;

	private static final int QTD_COL = 3 ;
	private static final int MAX_RECENTLY_USED_ITEMS = 3 ;
	private static final int QTD_SLOTS_PER_PAGE = 12 ;
	private static final Point SPACING = new Point(74, 48) ;
	private static final Image IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "Bag.png") ;
	private static final Image ITEM_DESCRIPTION_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "BagItemDescription.png") ;
	private static final Image SELECTED_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "BagSelected.png") ;
	private static final Image SLOT_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "BagSlot.png") ;
	private static final Image SLOT_SELECTED_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "BagSlotSelected.png") ;
	private static final Image TAB_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "BagTab.png") ;
	private static final Image TAB_SELECTED_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "BagTabSelected.png") ;    
    private static final Dimension TAB_SIZE = Util.getSize(TAB_IMAGE) ;
    private static final Dimension SLOT_SIZE = Util.getSize(SLOT_IMAGE) ;

    public BagWindow()
	{
    	super("Mochila", Screen.getMe().pos(0.28, 0.4), IMAGE, 2, 10, 0, 0) ;
		Dimension size = Util.getSize(image) ;
		this.buttons.add(pageUpButton(new Point(topLeftPos.x + size.width - 10, topLeftPos.y + size.height + 10), Align.topLeft)) ;
		this.buttons.add(pageDownButton(new Point(topLeftPos.x + 10, topLeftPos.y + size.height + 10), Align.topLeft)) ;
		this.itemsOnPage = new LinkedHashMap<>() ;
        this.itemsOnMenu = new LinkedHashMap<>() ;
        this.itemsInBag = new LinkedHashMap<>() ;
		this.recentlyUsedItems = new HashMap<>() ;
        for (int i = 0 ; i <= numberTabs - 1 ; i += 1)
        {
            this.recentlyUsedItems.put(i, new ArrayList<>()) ;
        }
		this.gold = 0 ;

		Point offset = new Point(16 + SLOT_SIZE.width, 80) ;
		itemPos = new ArrayList<>() ;
		for (int i = 0 ; i <= QTD_SLOTS_PER_PAGE - 1; i += 1)
		{
			int row = i % (QTD_SLOTS_PER_PAGE / QTD_COL) ;
			int col = i / (QTD_SLOTS_PER_PAGE / QTD_COL) ;
			itemPos.add(Util.translate(insideTopLeft, offset.x + col * SPACING.x, offset.y + row * SPACING.y)) ;
		}

        recentItemPos = new ArrayList<>() ;
		for (int i = 0 ; i <= MAX_RECENTLY_USED_ITEMS - 1; i += 1)
		{
			recentItemPos.add(Util.translate(insideTopLeft, offset.x + i * SPACING.x, offset.y - 56)) ;
		}

        tabTextPos = new ArrayList<>() ;
        for (int i = 0 ; i <= numberTabs - 1 ; i += 1)
        {
            tabTextPos.add(Util.translate(topLeftPos, 55 + 113 * (i % 2), 133 + (i % 2) * (-8 + 24 * i) + (((i + 1) % 2) * 48 * i / 2))) ;
        }
		
	}

    protected void onOpen()
    {
        updatePage() ;
    }

	public void updateRecentlyUsedItem(Item item)
	{
        int itemMenu = -1 ;
        for (int i = 0 ; i <= numberTabs - 1 ; i += 1)
        {
            if (itemIsInMenu(item, i))
            {
                itemMenu = i ; 
                break ;
            }
        }

        if (recentlyUsedItems.get(itemMenu).contains(item)) { return ;}

		recentlyUsedItems.get(itemMenu).add(0, item) ;
		if (MAX_RECENTLY_USED_ITEMS == recentlyUsedItems.get(itemMenu).size() - 1)
		{
			recentlyUsedItems.get(itemMenu).remove(recentlyUsedItems.get(itemMenu).size() - 1) ;
		}
	}

	
	public Map<Item, Integer> getAllItems() { return Collections.unmodifiableMap(itemsInBag) ;}
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
				page = 0 ;
				updatePage() ;
			}
			if (action.equals(stdMenuUp))
			{
				tabDown() ;
				page = 0 ;
				updatePage() ;
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
                if (QTD_SLOTS_PER_PAGE * (page + 1) - 1 == item)
				{
                    pageUp() ;
                    updatePage() ;
                }
                else
                {
                    itemUp() ;
                }
			}
			if (action.equals(stdMenuUp))
			{
				if (QTD_SLOTS_PER_PAGE * page == item)
				{
                    pageDown() ;
                    updatePage() ;
                    item = QTD_SLOTS_PER_PAGE * (page + 1) - 1 ;
                }
                else
                {
                    itemDown() ;
                }
			}
			if (action.equals(stdPageUp))
			{
				pageUp() ;
				updatePage() ;
			}
			if (action.equals(stdPageDown))
			{
				pageDown() ;
				updatePage() ;
			}
			if (action.equals(stdExit) || action.equals(stdReturn))
			{
				menuDown() ;
			}
		}
	}
	
	public void act(Player player, Point mousePos)
	{
		buttons.forEach(button -> { if (button.isClicked(mousePos, player.getCurrentAction())) { button.act() ;}}) ;
		
		if (menu == 1 && actionIsForward(player.getCurrentAction()))
		{
			player.useItem(getSelectedItem()) ;
            updatePage() ;
		}		
	}
	
	public void add(Item item, int amount)
	{
        itemsInBag.put(item, itemsInBag.getOrDefault(item, 0) + amount) ;
        updatePage() ;
	}

	public void remove(Item item, int amount)
	{
		if (item == null) { Log.warn("Tentando remover item nulo") ; return ;}
		if (!contains(item)) { Log.warn("Tentando remover um item que não existe na mochila") ; return ;}
		if (!hasEnough(item, amount)) { Log.warn("Tentando remover mais itens do que a quantidade existente na mochila") ; return ;}
        
        if (itemsInBag.get(item) == amount)
        {
            itemsInBag.remove(item) ;
            this.item = 0 ;
        }
        else
        {
			itemsInBag.put(item, itemsInBag.get(item) - amount) ;
        }
        updatePage() ;
	}
	
	public void empty()
	{
        itemsInBag.clear() ;
        updatePage() ;
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

	private Map<Item, Integer> getItemsInTab(int tab)
	{
        return itemsInBag.entrySet().stream().filter(entry -> itemIsInMenu(entry.getKey(), tab)).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
	}

    private boolean itemIsInMenu(Item item, int tab)
    {
        return switch (tab)
		{
			case 0 -> item instanceof Potion ;
			case 1 -> item instanceof Alchemy ;
			case 2 -> item instanceof Forge ;
			case 3 -> item instanceof PetItem ;
			case 4 -> item instanceof Food ;
			case 5 -> item instanceof Arrow ;
			case 6 -> item instanceof Equip ;
			case 7 -> item instanceof GeneralItem ;
			case 8 -> item instanceof Fab ;
			case 9 -> item instanceof QuestItem ;
			default -> false ;
		} ;
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
		return new ArrayList<>(getItemsInTab(tab).keySet()) ;
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
	
	private Map<Item, Integer> getItemsInSelectedMenuWithAmounts()
	{
		return getItemsInTab(tab).entrySet().stream().filter(entry -> 0 < entry.getValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)) ;
	}
	
	private Item getSelectedItem()
	{
		if (itemsOnMenu.isEmpty()) { return null ;}
		
		int i = 0 ;
		for (Map.Entry<Item, Integer> activeItem : itemsOnMenu.entrySet())
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
		return itemsInBag.getOrDefault(item, 0) ;
	}
	
	public boolean contains(Item item)
	{
		return itemsInBag.containsKey(item) && 1 <= itemsInBag.get(item) ;
	}
	
	public boolean hasEnough(Item item, int qtd)
	{
		return contains(item) && qtd <= itemsInBag.get(item) ;
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
	
	private void updatePage()
	{
		item = page * QTD_SLOTS_PER_PAGE ;
		
        itemsOnMenu = orderItems(getItemsInSelectedMenuWithAmounts()) ;

        // removes the first items that are not in the current window and limits the number of items to the max number of slots per window, while keeping item order
        itemsOnPage = itemsOnMenu.entrySet().stream().skip(page * QTD_SLOTS_PER_PAGE).limit(QTD_SLOTS_PER_PAGE)
                                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (existing, replacement) -> existing, LinkedHashMap::new)) ;

		numberItems = Math.min((page + 1) * QTD_SLOTS_PER_PAGE, itemsOnPage.size() + page * QTD_SLOTS_PER_PAGE) ;
		numberPages = getItemsInTab(tab).size() / QTD_SLOTS_PER_PAGE + 1 ;
	}
	
	public int totalValue() // TODO considerar valor de venda dos itens
	{
		return itemsInBag.entrySet().stream().mapToInt(entry -> entry.getKey().getPrice() * entry.getValue()).sum() ;
	}
		
	public Item itemHovered(Point mousePos)
	{	
		List<Item> itemsDisplayed = new ArrayList<>(itemsOnPage.keySet()) ;
		int numberItemsDisplayed = Math.min(QTD_SLOTS_PER_PAGE, itemsDisplayed.size()) ;

		for (int i = 0 ; i <= numberItemsDisplayed - 1 ; i += 1)
		{
			Point slotCenter = itemPos.get(i) ;
			Point slotCenterLeft = UtilAlignment.getTopLeft(slotCenter, Align.center, Util.getSize(SLOT_IMAGE)) ;
			if (Util.isInside(mousePos, slotCenterLeft, Util.getSize(SLOT_IMAGE)))
			{
				return itemsDisplayed.get(i) ;
			}
		}
		
		return null ;
	}
	
	private void switchTabOnHover(Point mousePos, Point tabPos, int tabID)
	{
        if (1 <= menu) { return ;}

        Point tabTopLeftPos = UtilAlignment.getTopLeft(tabPos, Align.center, TAB_SIZE) ;
		if (!Util.isInside(mousePos, tabTopLeftPos, Util.getSize(TAB_IMAGE))) { return ;}
        if (tab == tabID) { return ;}
		
		switchTab(tabID) ;
        updatePage() ;
	}

    private void switchTab(int tabID)
    {
		item = 0 ;
		page = 0 ;
		tab = tabID ;
    }

	public void setClothOnFire()
	{
        Map<Item, Integer> genItems = itemsInBag.entrySet().stream().filter(item -> item instanceof GeneralItem).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)) ;
		Set<Item> generalItems = Set.copyOf(genItems.keySet()) ;
		for (Item item : generalItems)
		{
			if (item.getId() == 114 || item.getId() == 119)
			{
				int amount = genItems.get(item) ;
				remove(item, amount) ;
				add(GeneralItem.getAll()[115], amount) ;
			}
		}
	}
	
	private void displayTabs(Point mousePos)
	{
		String[] tabNames = Game.getAllText().get(TextCategories.bagMenus) ;
		for (int i = 0 ; i <= tabNames.length - 1 ; i += 1)
		{
			Point tabPos = Util.translate(tabTextPos.get(i), 0, -5) ;
			boolean selected = i == tab ;
            Color textColor = getTextColor(selected) ;
			switchTabOnHover(mousePos, tabPos, i) ;

            GamePanel.getDP().drawImage(selected ? TAB_SELECTED_IMAGE : TAB_IMAGE, tabPos, Align.center);
			GamePanel.getDP().drawText(tabTextPos.get(i), Align.center, tabNames[i], TITLE_FONT, textColor) ;
		}
	}

    private void displayRecentlyUsedItems(Point mousePos)
    {
        for (int i = 0 ; i <= recentlyUsedItems.get(tab).size() - 1 ; i += 1)
        {
            Item item = recentlyUsedItems.get(tab).get(i) ;

            if (!contains(item)) { continue ;}

			Point slotCenter = recentItemPos.get(i) ;
            int amount = getAmount(item);
            int itemID = new ArrayList<>(itemsOnMenu.keySet()).indexOf(item) ;
			updateSelectedItemOnHover(mousePos, slotCenter, Align.center, SLOT_SIZE, itemID) ;
            displayItem(item, amount, slotCenter, false) ;
        }

    }

    private void displayItem(Item itemDisplayed, int amount, Point slotCenter, boolean selected)
    {
        Point amountTextPos = Util.translate(slotCenter, SLOT_SIZE.width / 2, SLOT_SIZE.height / 2) ;

        GamePanel.getDP().drawImage(selected ? SLOT_SELECTED_IMAGE : SLOT_IMAGE, slotCenter, Align.center) ;
        GamePanel.getDP().drawImage(itemDisplayed.getImage(), slotCenter, Align.center) ;
        GamePanel.getDP().drawText(amountTextPos, Align.centerLeft, String.valueOf(amount), selected ? SELECTED_COLOR : Palette.colors[12]) ;

        if (selected)
        {
            displayItemDescription(itemDisplayed) ;
        }
    }

	private void displayItemInfo(Item selectedItem)
	{
        if (selectedItem == null) { return ;}

		if (selectedItem instanceof Equip || selectedItem instanceof GeneralItem)
		{
			selectedItem.displayInfo(topLeftPos, Align.topRight) ;
		}
	}

    private void displayItemDescription(Item selectedItem)
    {
        Point imagePos = Util.translate(insideTopLeft, 0, -64) ;
        Point textPos = Util.translate(imagePos, 0, 0) ;
        GamePanel.getDP().drawImage(ITEM_DESCRIPTION_IMAGE, imagePos, Align.topLeft);
        Draw.fitText(textPos, Align.topLeft, selectedItem.getName() + ": " + selectedItem.getDescription(), STD_FONT, ITEM_DESCRIPTION_IMAGE.getWidth(null), Palette.colors[0]) ;
    }

    private void displayItems(Point mousePos, List<Item> itemsDisplayed)
    {
		int qtdItemsDisplayed = Math.min(QTD_SLOTS_PER_PAGE, itemsDisplayed.size()) ;
		List<Integer> amountsDisplayed = new ArrayList<>(itemsOnPage.values()) ;

        for (int i = 0 ; i <= qtdItemsDisplayed - 1; i += 1)
		{
			int itemID = i + page * QTD_SLOTS_PER_PAGE ;
            boolean selected = 1 <= menu && item == itemID ;
			Point slotCenter = itemPos.get(i) ;

			updateSelectedItemOnHover(mousePos, slotCenter, Align.center, SLOT_SIZE, itemID) ;			
            displayItem(itemsDisplayed.get(i), amountsDisplayed.get(i), slotCenter, selected) ;
		}
    }

	public void display(Point mousePos)
	{	
		GamePanel.getDP().drawImage(menu == 0 ? image : SELECTED_IMAGE, topLeftPos, Align.topLeft) ;
		displayTabs(mousePos) ;

		List<Item> listItemsDisplayed = new ArrayList<>(itemsOnPage.keySet()) ;
		List<Item> listItemsOnMenu = new ArrayList<>(itemsOnMenu.keySet()) ;
        Item selectedItem = 1 <= listItemsOnMenu.size() ? listItemsOnMenu.get(item) : null ;

		displayRecentlyUsedItems(mousePos) ;
		displayItems(mousePos, listItemsDisplayed) ;
		displayItemInfo(selectedItem) ;

        GamePanel.getDP().drawText(Util.translate(topLeftPos, size), Align.center, String.valueOf(totalValue()), Palette.colors[12]) ;
		
		buttons.forEach(button -> button.display(false, mousePos)) ;
	}
	
	@Override
	public String toString()
	{
        return itemsInBag.toString() ;
	}
}
