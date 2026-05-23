package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import animations.MessageAnimation;
import animations.ObtainedItemAnimation;
import graphics.Align;
import graphics.Scale;
import graphics2.Draw;
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

	private static final Point WINDOW_POS = Screen.getMe().pos(0.4, 0.2) ;
	private static final int QTD_ITEMS_ON_WINDOW = 10 ;
	private static final Image IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "Shopping.png") ;
	
	public ShoppingWindow(List<Item> itemsForSale)
	{
		super("Shopping", WINDOW_POS, IMAGE, 1, 1, Math.min(itemsForSale.size(), QTD_ITEMS_ON_WINDOW), calcNumberWindows(itemsForSale.size())) ;
		this.itemsForSale = itemsForSale ;
		itemsOnWindow = calcItemsOnWindow() ;
		buyMode = true ;
	}

	public void setBuyMode(boolean buyMode) { this.buyMode = buyMode ;}
	
	private Item selectedItem() { return itemsForSale.get(item + window * QTD_ITEMS_ON_WINDOW) ;}
	
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
				buyItem(bag) ;
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
	
	public void displayMessage(int i)
	{
		if (i == 0)
		{
			MessageAnimation.start(Screen.getMe().pos(0.1, 0.2), "Você não possui ouro suficiente", Palette.colors[0]) ;
			return ;
		}

		ObtainedItemAnimation.start(Screen.getMe().pos(0.5, 0.2), selectedItem().getName(), Palette.colors[0]) ;
	}
	
	public void buyItem(BagWindow bag)
	{
		Item selectedItem = selectedItem() ;
		if (bag.getGold() < selectedItem.getPrice()) { displayMessage(0) ; return ;}
		
		displayMessage(1) ;
		bag.add(selectedItem, 1) ;
		bag.addGold(-selectedItem.getPrice()) ;
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
		Point itemPos = Util.translate(WINDOW_POS, BORDER + PADDING + Item.getSlotImage().getWidth(null) / 2, BORDER + 20 + PADDING + Item.getSlotImage().getHeight(null) / 2) ;
		Point titlePos = Util.translate(WINDOW_POS, size.width / 2, 16) ;
		double angle = Draw.stdAngle ;
		
		GamePanel.getDP().drawImage(IMAGE, WINDOW_POS, angle, Scale.unit, Align.topLeft, stdOpacity) ;		
		GamePanel.getDP().drawText(titlePos, Align.center, angle, name, TITLE_FONT, Palette.colors[0]) ;				
		
		for (int i = 0 ; i <= itemsOnWindow.size() - 1 ; i += 1)
		{
			Item bagItem = itemsOnWindow.get(i) ;
			String qtdItem = buyMode ? "" : "" ; // TODO pegar bag e mostrar qtos itens tem
			Point namePos = Util.translate(itemPos, BORDER + 10, 0) ;
			Point pricePos = Util.translate(namePos, size.width - BORDER - PADDING - 50, 0) ;
			Point coinPos = Util.translate(pricePos, 10, 0) ;
			
			checkMouseSelection(mousePos, namePos, Align.centerLeft, new Dimension(100, 10), i) ;
			Color itemColor = this.item == itemsOnWindow.indexOf(bagItem) ? SELECTED_COLOR : STD_COLOR ;
			GamePanel.getDP().drawImage(Item.getSlotImage(), itemPos, angle, Scale.unit, Align.center) ;
			GamePanel.getDP().drawImage(bagItem.getImage(), itemPos, angle, Scale.unit, Align.center) ;
			GamePanel.getDP().drawText(namePos, Align.centerLeft, angle, bagItem.getName() + qtdItem, STD_FONT, itemColor) ;
			GamePanel.getDP().drawText(pricePos, Align.centerRight, angle, String.valueOf(bagItem.getPrice()), STD_FONT, Palette.colors[14]) ;
			GamePanel.getDP().drawImage(Player.getCoinImg(), coinPos, Align.center) ;
			
			if (this.item == itemsOnWindow.indexOf(bagItem))
			{
				bagItem.displayInfo(Util.translate(WINDOW_POS, -10, 0), Align.topRight) ;
			}
			itemPos.y += 23 ;
		}
		
		Draw.windowArrows(Util.translate(WINDOW_POS, 0, size.height + 10), size.width, window, numberWindows, stdOpacity) ;
		
	}
}
