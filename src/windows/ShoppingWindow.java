package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import graphics.Align;
import graphics.Scale;
import items.Item;
import liveBeings.Player;
import main.GamePanel;
import main.GameTimer;
import main.ImageLoader;
import main.Palette;
import main.Path;
import screen.Screen;
import shared.SharedImages;
import utilities.Util;


public class ShoppingWindow extends GameWindow
{
	private final Map<Item, Integer> maxStock ;
    private GameTimer renewStockTimer ;
	private Map<Item, Integer> stock ;
	private Map<Item, Integer> itemsOnPage ;
	private boolean buyMode ;
    private final Point titlePos ;
	private final List<Point> itemPos ;
	private final List<Point> amountPos ;
	private final List<Point> namePos ;
	private final List<Point> pricePos ;
    private final List<Point> coinPos ;
	private final ShopBag shopBag ;
    private final int renewStockDuration ;

    private static final int QTD_ITEMS_ON_STOCK = 12 ;
	private static final int MAX_ITEMS_PER_PAGE = 10 ;
    private static final Dimension ITEM_HOVER_AREA = new Dimension(100, 10) ;
	private static final Image IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "Shopping.png") ;
	private static final double SELLING_PRICE_RATE = 0.75 ;
	
	public ShoppingWindow(Map<Item, Integer> maxStock)
	{
		super("Shopping", Screen.getMe().pos(0.4, 0.2), IMAGE, 1, 1, Math.min(maxStock.size(), MAX_ITEMS_PER_PAGE), calcNumberPages(maxStock.size())) ;
        this.titlePos = Util.translate(topLeftPos, size.width / 2, 16) ;
        this.itemPos = new ArrayList<>() ;
        this.amountPos = new ArrayList<>() ;
        this.namePos = new ArrayList<>() ;
        this.pricePos = new ArrayList<>() ;
        this.coinPos = new ArrayList<>() ;
        for (int i = 0 ; i <= MAX_ITEMS_PER_PAGE - 1 ; i += 1)
        {
            this.itemPos.add(Util.translate(topLeftPos, BORDER + PADDING - 20, BORDER + PADDING + 23 * i)) ;
            this.amountPos.add(Util.translate(itemPos.get(i), BORDER + 4, 0)) ;
            this.namePos.add(Util.translate(itemPos.get(i), BORDER + 10, 0)) ;
            this.pricePos.add(Util.translate(namePos.get(i), size.width - BORDER - 60, 0)) ;
            this.coinPos.add(Util.translate(pricePos.get(i), 10, 0)) ;
        }

        this.shopBag = new ShopBag(Util.translate(topLeftPos, 300, 200), false) ;
		this.maxStock = maxStock ;
        this.renewStockDuration = 1200 ;
        this.stock = calcNewStock();
		this.itemsOnPage = calcItemsOnPage() ;
		this.buyMode = true ;
        this.renewStockTimer = new GameTimer(renewStockDuration) ;
        this.renewStockTimer.start();
	}

    public static double getSellingPriceRate() { return SELLING_PRICE_RATE ;}

    protected void onOpen()
    {
        
    }

    public void addToStock(Item item)
    {
        if (!stock.containsKey(item))
        {
            stock.put(item, 1) ;
            return ;
        }

        stock.put(item, stock.get(item) + 1) ;
    }

    private Map<Item, Integer> calcNewStock()
    {
        if (stock == null)
        {
            stock = new LinkedHashMap<>() ;
        }

        Map<Item, Integer> newStock = new LinkedHashMap<>();

        for (Item item : stock.keySet())
        {
            if (maxStock.containsKey(item))
            {
                newStock.put(item, maxStock.get(item));
            }
        }

        List<Item> availableItems = new ArrayList<>(maxStock.keySet());
        availableItems.removeAll(newStock.keySet());

        int itemsToAdd = Math.min(
            QTD_ITEMS_ON_STOCK - newStock.size(),
            availableItems.size()
        );

        for (int i = 0; i < itemsToAdd; i++)
        {
            int itemIndex = Util.randomInt(0, availableItems.size() - 1);
            Item item = availableItems.remove(itemIndex);
            newStock.put(item, maxStock.get(item));
        }

        return newStock;
    }

    protected void onUpdate()
    {
        if (!renewStockTimer.hasFinished() || !buyMode || isOpen) { return ;}

        stock = calcNewStock();
        updateNumberPages();
        updatePage();
        renewStockTimer.restart();
    }

	public void setBuyMode(boolean buyMode) { this.buyMode = buyMode ;}
	
	private Item selectedItem()
    {
        int index = item + page * MAX_ITEMS_PER_PAGE ;

        if (index <= -1) { return null ;}
        if (stock.size() <= 0) { return null ;}

        List<Item> items = new ArrayList<>(stock.keySet());
        return items.get(index) ;
    }
	
    public void openShopBag()
    {
        shopBag.open() ;
    }

	public void setIemsForSellingMode(BagWindow bag)
	{
		stock = bag.getAllItems() ;
		updateNumberPages() ;
		updatePage() ;
	}
	
	private static int calcNumberPages(int numberItems) { return (int) Math.ceil(numberItems / (double)MAX_ITEMS_PER_PAGE) ;}
	
	private void updateNumberPages()
    {
        numberPages = calcNumberPages(stock.size()) ;
        if (numberPages <= page)
        {
            pageDown() ;
        }
    }
	
	public void navigate(String action)
	{
		if (action.equals(stdPageDown))
		{
			pageDown() ;
			updatePage() ;
		}
		if (action.equals(stdPageUp))
		{
			pageUp() ;
			updatePage() ;
		}
		if (action.equals(stdMenuUp))
		{
			itemDown() ;
		}
		if (action.equals(stdMenuDown))
		{
			itemUp() ;
		}

        returnAndCloseOnExit(action) ;
	}

	public void act(Player player, Point mousePos)
	{
		if (actionIsForward(player.getCurrentAction()))
		{
			if (buyMode)
			{
                if (shopBag.isFull()) { return ;}

		        Item selectedItem = selectedItem() ;
                if (selectedItem == null || stock.get(selectedItem) <= 0) { return ;}

                shopBag.addItem(selectedItem) ;
                if (stock.get(selectedItem) == 1)
                {
                    stock.remove(selectedItem) ;
                }
                else
                {
                    stock.put(selectedItem, stock.get(selectedItem) - 1) ;
                }
		        updatePage() ;
                updateNumberPages() ;
				return ;
			}
			
            Item selectedItem = selectedItem() ;
            player.getBag().sellItem(selectedItem) ;
            setIemsForSellingMode(player.getBag()) ;
		}
	}
    protected int itemHoveredID(Point mousePos)
    {
		for (int i = 0 ; i <= itemsOnPage.size() - 1 ; i += 1)
        {
            if (itemIsHovered(mousePos, namePos.get(i), Align.centerLeft, ITEM_HOVER_AREA)) { return i ;}
        }
        return -1 ;
    }

	public void updatePage()
	{
		item = 0 ;
        updateNumberPages() ;
		itemsOnPage = calcItemsOnPage() ;
		numberItems = itemsOnPage.size() ;
	}

	private Map<Item, Integer> calcItemsOnPage()
	{
		if (stock.size() <= MAX_ITEMS_PER_PAGE)
		{
			return stock ;
		}
		
		int firstItemID = page * MAX_ITEMS_PER_PAGE ;
		
		return stock.entrySet().stream()
                                .skip(firstItemID)
                                .limit(MAX_ITEMS_PER_PAGE)
                                .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    Map.Entry::getValue,
                                    (a, b) -> a,
                                    LinkedHashMap::new
                                )) ;		
	}
	
	public void display(Point mousePos)
	{
		GamePanel.getDP().drawImage(image, topLeftPos, Scale.unit, Align.topLeft, stdOpacity) ;		
		GamePanel.getDP().drawText(titlePos, Align.center, name, TITLE_FONT, Palette.colors[0]) ;

        int i = 0 ;
        for (Map.Entry<Item, Integer> entry : itemsOnPage.entrySet())
        {
            Item bagItem = entry.getKey();
            int qtdInStock = entry.getValue() ;

            bagItem.displayInSlot(itemPos.get(i));
			Color itemColor = this.item == i ? SELECTED_COLOR : STD_COLOR ;
            GamePanel.getDP().drawText(amountPos.get(i), Align.topLeft, String.valueOf(qtdInStock), STD_FONT, itemColor);
			GamePanel.getDP().drawText(namePos.get(i), Align.centerLeft, bagItem.getName(), STD_FONT, itemColor) ;            
			GamePanel.getDP().drawText(pricePos.get(i), Align.centerRight, String.valueOf(bagItem.getPrice()), STD_FONT, Palette.colors[14]) ;
			GamePanel.getDP().drawImage(SharedImages.getCoinImg(), coinPos.get(i), Align.center) ;
            i += 1 ;
        }

        Item selectedItem = selectedItem() ;
        if (selectedItem != null)
        {
            selectedItem.displayInfo(Util.translate(topLeftPos, -10, 0), Align.topRight) ;
        }

		shopBag.display() ;
		
		drawNavigationButtons(Util.translate(topLeftPos, 0, size.height + 10), size.width, SUBTITLE_FONT, page, numberPages, stdOpacity) ;
	}

	protected void onClose()
    {
        isOpen = false ;
        shopBag.close() ;
    }
}
