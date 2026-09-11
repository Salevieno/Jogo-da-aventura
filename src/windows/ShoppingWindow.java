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
import main.GamePanel;
import main.ImageLoader;
import main.Log;
import main.Palette;
import main.Path;
import screen.Screen;
import shared.SharedImages;
import utilities.Util;


public class ShoppingWindow extends GameWindow
{
	private List<Item> itemsForSale ;
	private List<Item> itemsOnWindow ;
	private boolean buyMode ; // TODO fazer venda funcionar no shopping
    private final Point titlePos ;
	private final List<Point> itemPos ;    
	private final List<Point> namePos ;
	private final List<Point> pricePos ;
    private final List<Point> coinPos ;
	private final ShopBag shopBag ;

	private static final int MAX_ITEMS_PER_PAGE = 10 ;
	private static final Image IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "Shopping.png") ;
	
	public ShoppingWindow(List<Item> itemsForSale)
	{
		super("Shopping", Screen.getMe().pos(0.4, 0.2), IMAGE, 1, 1, Math.min(itemsForSale.size(), MAX_ITEMS_PER_PAGE), calcNumberWindows(itemsForSale.size())) ;
		this.itemsForSale = itemsForSale ;
		this.itemsOnWindow = calcItemsOnWindow() ;
		this.buyMode = true ;

        this.titlePos = Util.translate(topLeftPos, size.width / 2, 16) ;
        this.itemPos = new ArrayList<>() ;
        this.namePos = new ArrayList<>() ;
        this.pricePos = new ArrayList<>() ;
        this.coinPos = new ArrayList<>() ;
        for (int i = 0 ; i <= MAX_ITEMS_PER_PAGE - 1 ; i += 1)
        {
            this.itemPos.add(Util.translate(topLeftPos, BORDER + PADDING - 20, BORDER + PADDING + 23 * i)) ;
            this.namePos.add(Util.translate(itemPos.get(i), BORDER + 10, 0)) ;
            this.pricePos.add(Util.translate(namePos.get(i), size.width - BORDER - 60, 0)) ;
            this.coinPos.add(Util.translate(pricePos.get(i), 10, 0)) ;
        }

        this.shopBag = new ShopBag(Util.translate(topLeftPos, 300, 200)) ;
	}

	public void setBuyMode(boolean buyMode) { this.buyMode = buyMode ;}
	
	private Item selectedItem()
    {
        if (item + window * MAX_ITEMS_PER_PAGE <= -1) { return null ;}
        return itemsForSale.get(item + window * MAX_ITEMS_PER_PAGE) ;
    }
	
    public void openShopBag()
    {
        shopBag.open() ;
    }

    protected void closeShopBag()
    {
        shopBag.deactivateButtons() ;
    }

	public void setIemsForSellingMode(BagWindow bag)
	{
		Set<Item> newItems = bag.getAllItems().keySet();
		itemsForSale = new ArrayList<>(newItems) ;
		updateNumberWindows() ;
		updateWindow() ;
	}
	
	private static int calcNumberWindows(int numberItems) { return (int) Math.ceil(numberItems / (double)MAX_ITEMS_PER_PAGE) ;}
	
	private void updateNumberWindows() { numberWindows = calcNumberWindows(itemsForSale.size()) ;}
	
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
			
			sellItemFromBag(bag) ;
		}
	}
	
	private void updateWindow()
	{
		item = 0 ;
		itemsOnWindow = calcItemsOnWindow() ;
		numberItems = itemsOnWindow.size() ;
	}
	
    // TODO mover para shopBag
	private void sellItemFromBag(BagWindow bag)
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
		if (itemsForSale.size() <= MAX_ITEMS_PER_PAGE)
		{
			return itemsForSale ;
		}
		
		int firstItemID = window * MAX_ITEMS_PER_PAGE ;
		int lastItemID = Math.min(firstItemID + MAX_ITEMS_PER_PAGE, itemsForSale.size()) ;
		
		return itemsForSale.subList(firstItemID, lastItemID) ;		
	}
	
	public void display(Point mousePos)
	{
		GamePanel.getDP().drawImage(image, topLeftPos, Scale.unit, Align.topLeft, stdOpacity) ;		
		GamePanel.getDP().drawText(titlePos, Align.center, name, TITLE_FONT, Palette.colors[0]) ;				

		for (int i = 0 ; i <= itemsOnWindow.size() - 1 ; i += 1)
        {
            updateSelectedItemOnHover(mousePos, namePos.get(i), Align.centerLeft, new Dimension(100, 10), i) ;
			Item bagItem = itemsOnWindow.get(i) ;
            bagItem.displayInSlot(itemPos.get(i), false);
            
			String qtdItem = buyMode ? "" : "" ; // TODO pegar bag e mostrar qtos itens tem
			Color itemColor = this.item == i ? SELECTED_COLOR : STD_COLOR ;
			GamePanel.getDP().drawText(namePos.get(i), Align.centerLeft, bagItem.getName() + qtdItem, STD_FONT, itemColor) ;            
			GamePanel.getDP().drawText(pricePos.get(i), Align.centerRight, String.valueOf(bagItem.getPrice()), STD_FONT, Palette.colors[14]) ;
			GamePanel.getDP().drawImage(SharedImages.getCoinImg(), coinPos.get(i), Align.center) ;
			
			if (this.item == i)
			{
				bagItem.displayInfo(Util.translate(topLeftPos, -10, 0), Align.topRight) ;
			}
		}

		shopBag.display() ;
		
		drawNavigationButtons(Util.translate(topLeftPos, 0, size.height + 10), size.width, SUBTITLE_FONT, window, numberWindows, stdOpacity) ;
	}
}
