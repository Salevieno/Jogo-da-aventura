package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
	private static int NumberItemsPerWindow = 10 ;
	private List<Item> ItemsForSale ;
	
	public ShoppingWindow(List<Item> ItemsForSale)
	{
		super("Shopping", UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "Shopping.png"), 1, 1, Math.min(ItemsForSale.size(), NumberItemsPerWindow), ItemsForSale.size() / NumberItemsPerWindow + 1) ;
		this.ItemsForSale = ItemsForSale ;
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
		}
		if (action.equals(Player.ActionKeys[1]))
		{
			windowDown() ;
		}
	}
	
	private void removeItem()
	{
		ItemsForSale.remove(item) ;
		if (item == ItemsForSale.size())
		{
			item += -1 ;
		}
		numberItems += -1 ;
	}
	
	public void buyItem(BagWindow bag)
	{
		if (ItemsForSale.get(item).getPrice() <= bag.getGold())
		{
//			removeItem() ;
			bag.Add(ItemsForSale.get(item), 1) ;
			bag.addGold(-ItemsForSale.get(item).getPrice()) ;
		}
	}
	
	public void display(Point mousePos, DrawingOnPanel DP)
	{
		Point windowPos = new Point((int)(0.4*Game.getScreen().getSize().width), (int)(0.2*Game.getScreen().getSize().height)) ;
		Point itemPos = UtilG.Translate(windowPos, 30, 70) ;
		Point titlePos = UtilG.Translate(windowPos, size.width / 2, 28) ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		Font titleFont = new Font(Game.MainFontName, Font.BOLD, 16) ;
		double angle = DrawingOnPanel.stdAngle ;
		List<Item> itemsOnWindow = NumberItemsPerWindow <= ItemsForSale.size() ? ItemsForSale.subList(0, NumberItemsPerWindow) : ItemsForSale ;
		
		DP.DrawImage(image, windowPos, angle, new Scale(1, 1), Align.topLeft) ;
		
		DP.DrawText(titlePos, Align.center, angle, name, titleFont, Game.ColorPalette[2]) ;
		
		
		
		for (Item item : itemsOnWindow)
		{
			Point namePos = UtilG.Translate(itemPos, 20, -6) ;
			Point pricePos = UtilG.Translate(namePos, 220, 0) ;
			Point coinPos = UtilG.Translate(pricePos, 10, 0) ;
			
			if (UtilG.isInside(mousePos, namePos, new Dimension(100, 20)))
			{
				this.item = itemsOnWindow.indexOf(item) ;
			}
			
			Color itemColor = this.item == itemsOnWindow.indexOf(item) ? Game.ColorPalette[6] : Game.ColorPalette[9] ;
			DP.DrawImage(Item.slot, itemPos, angle, new Scale(1, 1), Align.center) ;
			DP.DrawImage(item.getImage(), itemPos, angle, new Scale(1, 1), Align.center) ;
			DP.DrawText(namePos, Align.topLeft, angle, item.getName(), font, itemColor) ;
			DP.DrawText(pricePos, Align.centerRight, angle, String.valueOf(item.getPrice()), font, Game.ColorPalette[2]) ;
			DP.DrawImage(Player.CoinIcon, coinPos, Align.center) ;
			itemPos.y += 30 ;
		}
		
		DP.DrawWindowArrows(UtilG.Translate(windowPos, size.width / 2, size.height), size.width, window, numberWindows) ;
		
	}
}
