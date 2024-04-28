package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import graphics.Animation;
import graphics.AnimationTypes;
import graphics.Draw;
import graphics.DrawPrimitives;
import items.Item;
import libUtil.Align;
import libUtil.Util;
import liveBeings.Player;
import main.Game;
import utilities.Scale;
import utilities.UtilS;

public class ShoppingWindow extends GameWindow
{
	private List<Item> itemsForSale ;
	private List<Item> itemsOnWindow ;
	private boolean buyMode ;

	private static final Point windowPos = Game.getScreen().pos(0.4, 0.2) ;
	private static final int numberItemsPerWindow = 10 ;
	private static final Image image = UtilS.loadImage("\\Windows\\" + "Shopping.png") ;
	
	public ShoppingWindow(List<Item> itemsForSale)
	{
		super("Shopping", windowPos, image, 1, 1, Math.min(itemsForSale.size(), numberItemsPerWindow), calcNumberWindows(itemsForSale.size())) ;
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
			Animation.start(AnimationTypes.message, new Object[] {Game.getScreen().pos(0.1, 0.2), "Você não possui ouro suficiente", Game.colorPalette[0]}) ;
			return ;
		}

		Animation.start(AnimationTypes.obtainedItem, new Object[] {Game.getScreen().pos(0.1, 0.2), selectedItem().getName(), Game.colorPalette[0]}) ;
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
		
		Item selectedItem = selectedItem() ;
		if (!bag.contains(selectedItem)) { System.out.println("Tentando vender item que não possui") ; return ;}
		
		System.out.println("Vendendo " + selectedItem.getName());
		bag.remove(selectedItem, 1) ;
		bag.addGold(selectedItem.getPrice()) ;
		setIemsForSellingMode(bag) ;
//		Game.getAnimations().get(10).start(200, new Object[] {selectedItem.getPrice()}) ;

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
	
	public void display(Point mousePos, DrawPrimitives DP)
	{
		Point itemPos = Util.Translate(windowPos, border + padding + Item.slot.getWidth(null) / 2, border + 20 + padding + Item.slot.getHeight(null) / 2) ;
		Point titlePos = Util.Translate(windowPos, size.width / 2, 16) ;
		double angle = Draw.stdAngle ;
		
		DP.drawImage(image, windowPos, angle, Scale.unit, Align.topLeft) ;
		
		DP.drawText(titlePos, Align.center, angle, name, titleFont, Game.colorPalette[1]) ;
				
		
		for (int i = 0 ; i <= itemsOnWindow.size() - 1 ; i += 1)
		{
			Item bagItem = itemsOnWindow.get(i) ;
			Point namePos = Util.Translate(itemPos, border + 10, 0) ;
			Point pricePos = Util.Translate(namePos, size.width - border - padding - 50, 0) ;
			Point coinPos = Util.Translate(pricePos, 10, 0) ;
			
			checkMouseSelection(mousePos, namePos, Align.centerLeft, new Dimension(100, 10), i) ;
			Color itemColor = this.item == itemsOnWindow.indexOf(bagItem) ? selColor : stdColor ;
			DP.drawImage(Item.slot, itemPos, angle, Scale.unit, Align.center) ;
			DP.drawImage(bagItem.getImage(), itemPos, angle, Scale.unit, Align.center) ;
			DP.drawText(namePos, Align.centerLeft, angle, bagItem.getName(), stdFont, itemColor) ;
			DP.drawText(pricePos, Align.centerRight, angle, String.valueOf(bagItem.getPrice()), stdFont, Game.colorPalette[14]) ;
			DP.drawImage(Player.CoinIcon, coinPos, Align.center) ;
			
			if (this.item == itemsOnWindow.indexOf(bagItem))
			{
				bagItem.displayInfo(Util.Translate(windowPos, -10, 0), Align.topRight, DP) ;
			}
			itemPos.y += 23 ;
		}
		
		Draw.windowArrows(Util.Translate(windowPos, 0, size.height + 10), size.width, window, numberWindows) ;
		
	}
}
