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
	private static int numberItemsPerWindow = 10 ;
	private List<Item> itemsForSale ;
	private List<Item> itemsOnWindow ;
	
	public ShoppingWindow(List<Item> ItemsForSale)
	{
		super("Shopping", UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "Shopping.png"), 1, 1, Math.min(ItemsForSale.size(), numberItemsPerWindow), ItemsForSale.size() / numberItemsPerWindow + 1) ;
		this.itemsForSale = ItemsForSale ;
		itemsOnWindow = itemsOnWindow() ;
	}

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
	
	public void action(String action, BagWindow bag)
	{
		if (action.equals("Enter") | action.equals("MouseLeftClick"))
		{
			buyItem(bag) ;
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
		if (itemsForSale.get(item).getPrice() <= bag.getGold())
		{
			bag.Add(itemsForSale.get(item), 1) ;
			bag.addGold(-itemsForSale.get(item).getPrice()) ;
		}
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
		
		DP.DrawText(titlePos, Align.center, angle, name, titleFont, Game.ColorPalette[2]) ;
				
		
		for (Item item : itemsOnWindow)
		{
			Point namePos = UtilG.Translate(itemPos, border + 10, 0) ;
			Point pricePos = UtilG.Translate(namePos, size.width - border - padding - 50, 0) ;
			Point coinPos = UtilG.Translate(pricePos, 10, 0) ;
			
			if (UtilG.isInside(mousePos, namePos, new Dimension(100, 20)))
			{
				this.item = itemsOnWindow.indexOf(item) ;
			}
			
			Color itemColor = this.item == itemsOnWindow.indexOf(item) ? Game.ColorPalette[6] : Game.ColorPalette[9] ;
			DP.DrawImage(Item.slot, itemPos, angle, new Scale(1, 1), Align.center) ;
			DP.DrawImage(item.getImage(), itemPos, angle, new Scale(1, 1), Align.center) ;
			DP.DrawText(namePos, Align.centerLeft, angle, item.getName(), stdFont, itemColor) ;
			DP.DrawText(pricePos, Align.centerRight, angle, String.valueOf(item.getPrice()), stdFont, Game.ColorPalette[2]) ;
			DP.DrawImage(Player.CoinIcon, coinPos, Align.center) ;
			itemPos.y += 23 ;
		}
		
		DP.DrawWindowArrows(UtilG.Translate(windowPos, size.width / 2, size.height), size.width, window, numberWindows) ;
		
	}
}
