package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import graphics.DrawingOnPanel;
import items.Item;
import liveBeings.Player;
import main.Game;
import utilities.Align;
import utilities.Scale;
import utilities.UtilG;

public class ShoppingWindow extends GameWindow
{
	private List<Item> itemsForSale ;
	private List<Item> itemsOnWindow ;
	private boolean buyMode ;

	private static final Point windowPos = Game.getScreen().pos(0.4, 0.2) ;
	private static final int numberItemsPerWindow = 10 ;
	
	public ShoppingWindow(List<Item> itemsForSale)
	{
		super("Shopping", UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "Shopping.png"), 1, 1, Math.min(itemsForSale.size(), numberItemsPerWindow), calcNumberWindows(itemsForSale.size())) ;
		this.itemsForSale = itemsForSale ;
		itemsOnWindow = calcItemsOnWindow() ;
		buyMode = true ;
	}

	public void setBuyMode(boolean buyMode) { this.buyMode = buyMode ;}
	
	private Item selectedItem() { return itemsForSale.get(item + window * numberItemsPerWindow) ;}
	
	public void setIemsForSellingMode(BagWindow bag)
	{
		Set<Item> newItems = bag.getAllItems().keySet();
		itemsForSale = new ArrayList<>(newItems) ;
		updateNumberWindows() ;
		updateWindow() ;
	}
	
	private static int calcNumberWindows(int numberItems) { return (int) Math.ceil(numberItems / (double)numberItemsPerWindow) ;}
	
	public void updateNumberWindows() { numberWindows = calcNumberWindows(itemsForSale.size()) ;}
	
	public void navigate(String action)
	{
		if (action == null) { return ;}
		
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
	}
	
	public void act(String action, BagWindow bag)
	{
		// TODO reaction with "bought" or "not enough gold"
		if (Player.actionIsForward(action))
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
	
	public void buyItem(BagWindow bag)
	{
		Item selectedItem = selectedItem() ;
		if (bag.getGold() < selectedItem.getPrice())
		{
			Game.getAnimations().get(11).start(200, new Object[] {});
			return ;
		}
		
		bag.add(selectedItem, 1) ;
		bag.addGold(-selectedItem.getPrice()) ;
		Game.getAnimations().get(3).start(300, new Object[] {new Item[] {selectedItem}});
	}
	
	public void sellItem(BagWindow bag)
	{
		
		Item selectedItem = selectedItem() ;
		if (!bag.contains(selectedItem)) { System.out.println("Tentando vender item que não possui") ; return ;}
		
		System.out.println("Vendendo " + selectedItem.getName());
		bag.remove(selectedItem, 1) ;
		bag.addGold(selectedItem.getPrice()) ;
		setIemsForSellingMode(bag) ;
		Game.getAnimations().get(10).start(200, new Object[] {selectedItem.getPrice()}) ;

	}
	
	private List<Item> calcItemsOnWindow()
	{
		if (itemsForSale.size() <= numberItemsPerWindow)
		{
			return itemsForSale ;
		}
		
		int firstItemID = window * numberItemsPerWindow ;
		int lastItemID = Math.min(firstItemID + numberItemsPerWindow, itemsForSale.size()) ;
		
		return itemsForSale.subList(firstItemID, lastItemID) ;		
	}
	
	public void display(Point mousePos, DrawingOnPanel DP)
	{
		Point itemPos = UtilG.Translate(windowPos, border + padding + Item.slot.getWidth(null) / 2, border + 20 + padding + Item.slot.getHeight(null) / 2) ;
		Point titlePos = UtilG.Translate(windowPos, size.width / 2, 16) ;
		double angle = DrawingOnPanel.stdAngle ;
		
		DP.DrawImage(image, windowPos, angle, Scale.unit, Align.topLeft) ;
		
		DP.DrawText(titlePos, Align.center, angle, name, titleFont, Game.colorPalette[1]) ;
				
		
		for (Item item : itemsOnWindow)
		{
			Point namePos = UtilG.Translate(itemPos, border + 10, 0) ;
			Point pricePos = UtilG.Translate(namePos, size.width - border - padding - 50, 0) ;
			Point coinPos = UtilG.Translate(pricePos, 10, 0) ;
			
			if (UtilG.isInside(mousePos, namePos, new Dimension(100, 20)))
			{
				this.item = itemsOnWindow.indexOf(item) ;
			}
			
			Color itemColor = this.item == itemsOnWindow.indexOf(item) ? selColor : stdColor ;
			DP.DrawImage(Item.slot, itemPos, angle, Scale.unit, Align.center) ;
			DP.DrawImage(item.getImage(), itemPos, angle, Scale.unit, Align.center) ;
			DP.DrawText(namePos, Align.centerLeft, angle, item.getName(), stdFont, itemColor) ;
			DP.DrawText(pricePos, Align.centerRight, angle, String.valueOf(item.getPrice()), stdFont, Game.colorPalette[14]) ;
			DP.DrawImage(Player.CoinIcon, coinPos, Align.center) ;
			itemPos.y += 23 ;
		}
		
		DP.DrawWindowArrows(UtilG.Translate(windowPos, 0, size.height + 10), size.width, window, numberWindows) ;
		
	}
}
