package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.List;

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
	
	private static final int numberItemsPerWindow = 10 ;
	
	public ShoppingWindow(List<Item> ItemsForSale)
	{
		super("Shopping", UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "Shopping.png"), 1, 1, Math.min(ItemsForSale.size(), numberItemsPerWindow), ItemsForSale.size() / numberItemsPerWindow + 1) ;
		this.itemsForSale = ItemsForSale ;
		itemsOnWindow = itemsOnWindow() ;
		buyMode = true ;
	}

	public void setBuyMode(boolean buyMode) { this.buyMode = buyMode ;}
	
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
		itemsOnWindow = itemsOnWindow() ;
		numberItems = itemsOnWindow.size() ;
	}
	
	public void buyItem(BagWindow bag)
	{
		if (bag.getGold() < itemsForSale.get(item).getPrice())
		{
			Game.getAnimations()[11].start(200, new Object[] {});
			return ;
		}
		
		bag.Add(itemsForSale.get(item), 1) ;
		bag.addGold(-itemsForSale.get(item).getPrice()) ;
		Game.getAnimations()[3].start(300, new Object[] {new Item[] {itemsForSale.get(item)}});
	}
	
	public void sellItem(BagWindow bag)
	{
		Item bagSelectedItem = bag.getSelectedItem() ;
		bag.Remove(bagSelectedItem, 1) ;
		bag.addGold(bagSelectedItem.getPrice()) ;
		// TODO animation get gold
	}
	
	private List<Item> itemsOnWindow()
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
		Point windowPos = new Point((int)(0.4*Game.getScreen().getSize().width), (int)(0.2*Game.getScreen().getSize().height)) ;
		Point itemPos = UtilG.Translate(windowPos, border + padding + Item.slot.getWidth(null) / 2, border + 20 + padding + Item.slot.getHeight(null) / 2) ;
		Point titlePos = UtilG.Translate(windowPos, size.width / 2, 16) ;
		double angle = DrawingOnPanel.stdAngle ;
		
		DP.DrawImage(image, windowPos, angle, new Scale(1, 1), Align.topLeft) ;
		
		DP.DrawText(titlePos, Align.center, angle, name, titleFont, Game.colorPalette[2]) ;
				
		
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
			DP.DrawImage(Item.slot, itemPos, angle, new Scale(1, 1), Align.center) ;
			DP.DrawImage(item.getImage(), itemPos, angle, new Scale(1, 1), Align.center) ;
			DP.DrawText(namePos, Align.centerLeft, angle, item.getName(), stdFont, itemColor) ;
			DP.DrawText(pricePos, Align.centerRight, angle, String.valueOf(item.getPrice()), stdFont, Game.colorPalette[2]) ;
			DP.DrawImage(Player.CoinIcon, coinPos, Align.center) ;
			itemPos.y += 23 ;
		}
		
		DP.DrawWindowArrows(UtilG.Translate(windowPos, size.width / 2, size.height), size.width, window, numberWindows) ;
		
	}
}
