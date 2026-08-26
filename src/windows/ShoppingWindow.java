package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import graphics.Align;
import graphics.Scale;
import items.Item;
import liveBeings.Player;
import main.GamePanel;
import main.ImageLoader;
import main.Log;
import main.Palette;
import main.Path;
import screen.Screen;
import utilities.Util;


public class ShoppingWindow extends GameWindow
{
	private List<Item> itemsForSale ;
	private List<Item> itemsOnWindow ;
	private boolean buyMode ;
	private final ShopBag shopBag ;

	private static final int QTD_ITEMS_ON_WINDOW = 10 ;
	private static final Image IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "Shopping.png") ;
	
	public ShoppingWindow(List<Item> itemsForSale)
	{
		super("Shopping", Screen.getMe().pos(0.4, 0.2), IMAGE, 1, 1, Math.min(itemsForSale.size(), QTD_ITEMS_ON_WINDOW), calcNumberWindows(itemsForSale.size())) ;
		this.itemsForSale = itemsForSale ;
		itemsOnWindow = calcItemsOnWindow() ;
		buyMode = true ;
        shopBag = new ShopBag(Util.translate(topLeftPos, 300, 200)) ;
	}

	public void setBuyMode(boolean buyMode) { this.buyMode = buyMode ;}
	
	private Item selectedItem()
    {
        if (item + window * QTD_ITEMS_ON_WINDOW <= -1) { return null ;}
        return itemsForSale.get(item + window * QTD_ITEMS_ON_WINDOW) ;
    }
	
    public void openShopBag()
    {
        shopBag.open() ;
    }

	public void setIemsForSellingMode(BagWindow bag)
	{
		Set<Item> newItems = bag.getAllItems().keySet();
		itemsForSale = new ArrayList<>(newItems) ;
		updateNumberWindows() ;
		updateWindow() ;
	}
	
	private static int calcNumberWindows(int numberItems) { return (int) Math.ceil(numberItems / (double)QTD_ITEMS_ON_WINDOW) ;}
	
	public void updateNumberWindows() { numberWindows = calcNumberWindows(itemsForSale.size()) ;}
	
	public void navigate(String action)
	{
		if (action.equals(stdWindowDown))
		{
			windowDown() ;
			updateWindow() ;
		}
		if (action.equals(stdWindowUp))
		{
			windowUp() ;
			updateWindow() ;
		}
		if (action.equals(stdMenuUp))
		{
			itemDown() ;
		}
		if (action.equals(stdMenuDown))
		{
			itemUp() ;
		}
	}
	
	public void act(String action, BagWindow bag)
	{
		if (actionIsForward(action))
		{
			if (buyMode)
			{
		        Item selectedItem = selectedItem() ;
                if (selectedItem == null) { return ;}

                shopBag.addItem(selectedItem) ;
				return ;
			}
			
			sellItem(bag) ;
		}
	}
	
	private void updateWindow()
	{
		item = 0 ;
		itemsOnWindow = calcItemsOnWindow() ;
		numberItems = itemsOnWindow.size() ;
	}
	
	public void sellItem(BagWindow bag)
	{
		if (itemsForSale == null || itemsForSale.isEmpty()) { return ;}

		Item selectedItem = selectedItem() ;

		if (!bag.contains(selectedItem)) { Log.warn("Tentando vender item que não possui") ; return ;}
		
		bag.remove(selectedItem, 1) ;
		bag.addGold(selectedItem.getPrice()) ;
		setIemsForSellingMode(bag) ;
	}
	
	private List<Item> calcItemsOnWindow()
	{
		if (itemsForSale.size() <= QTD_ITEMS_ON_WINDOW)
		{
			return itemsForSale ;
		}
		
		int firstItemID = window * QTD_ITEMS_ON_WINDOW ;
		int lastItemID = Math.min(firstItemID + QTD_ITEMS_ON_WINDOW, itemsForSale.size()) ;
		
		return itemsForSale.subList(firstItemID, lastItemID) ;		
	}
	
	public void display(Point mousePos)
	{
		Point itemPos = Util.translate(topLeftPos, BORDER + PADDING + Item.getSlotImage().getWidth(null) / 2, BORDER + 20 + PADDING + Item.getSlotImage().getHeight(null) / 2) ;
		Point titlePos = Util.translate(topLeftPos, size.width / 2, 16) ;
		
		GamePanel.getDP().drawImage(image, topLeftPos, Scale.unit, Align.topLeft, stdOpacity) ;		
		GamePanel.getDP().drawText(titlePos, Align.center, name, TITLE_FONT, Palette.colors[0]) ;				
		
        item = -1 ;
		for (int i = 0 ; i <= itemsOnWindow.size() - 1 ; i += 1)
		{
			Point namePos = Util.translate(itemPos, BORDER + 10, 23 * i) ;
            int newItemID = getIDItemHovered(mousePos, namePos, Align.centerLeft, new Dimension(100, 10), i) ;
            if (newItemID != -1)
            {
                item = newItemID ;
                break ;
            }
        }

		for (int i = 0 ; i <= itemsOnWindow.size() - 1 ; i += 1)
		{
			Item bagItem = itemsOnWindow.get(i) ;
			String qtdItem = buyMode ? "" : "" ; // TODO pegar bag e mostrar qtos itens tem
			Point namePos = Util.translate(itemPos, BORDER + 10, 0) ;
			Point pricePos = Util.translate(namePos, size.width - BORDER - PADDING - 50, 0) ;
			Point coinPos = Util.translate(pricePos, 10, 0) ;

			Color itemColor = this.item == itemsOnWindow.indexOf(bagItem) ? SELECTED_COLOR : STD_COLOR ;
			GamePanel.getDP().drawImage(Item.getSlotImage(), itemPos, Scale.unit, Align.center) ;
			GamePanel.getDP().drawImage(bagItem.getImage(), itemPos, Scale.unit, Align.center) ;
			GamePanel.getDP().drawText(namePos, Align.centerLeft, bagItem.getName() + qtdItem, STD_FONT, itemColor) ;
			GamePanel.getDP().drawText(pricePos, Align.centerRight, String.valueOf(bagItem.getPrice()), STD_FONT, Palette.colors[14]) ;
			GamePanel.getDP().drawImage(Player.getCoinImg(), coinPos, Align.center) ;
			
			if (this.item == itemsOnWindow.indexOf(bagItem))
			{
				bagItem.displayInfo(Util.translate(topLeftPos, -10, 0), Align.topRight) ;
			}
			itemPos.y += 23 ;
		}

		shopBag.display() ;
		
		drawNavigationButtons(Util.translate(topLeftPos, 0, size.height + 10), size.width, SUBTITLE_FONT, window, numberWindows, stdOpacity) ;
	}
}
