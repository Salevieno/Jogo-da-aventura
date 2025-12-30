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
import graphics2.Animation;
import graphics2.AnimationTypes;
import graphics2.Draw;
import items.Item;
import liveBeings.Player;
import main.Game;
import main.GamePanel;
import main.Path;
import utilities.Util;


public class ShoppingWindow extends GameWindow
{
	private List<Item> itemsForSale ;
	private List<Item> itemsOnWindow ;
	private boolean buyMode ;

	private static final Point windowPos = Game.getScreen().pos(0.4, 0.2) ;
	private static final int numberItemsPerWindow = 10 ;
	private static final Image image = Game.loadImage(Path.WINDOWS_IMG + "Shopping.png") ;
	
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
			Animation.start(AnimationTypes.message, new Object[] {Game.getScreen().pos(0.1, 0.2), "Você não possui ouro suficiente", Game.palette[0]}) ;
			return ;
		}

		Animation.start(AnimationTypes.obtainedItem, new Object[] {Game.getScreen().pos(0.5, 0.2), selectedItem().getName(), Game.palette[0]}) ;
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

		if (!bag.contains(selectedItem)) { System.out.println("Tentando vender item que não possui") ; return ;}
		
		bag.remove(selectedItem, 1) ;
		bag.addGold(selectedItem.getPrice()) ;
		setIemsForSellingMode(bag) ;
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
	
	public void display(Point mousePos)
	{
		Point itemPos = Util.translate(windowPos, border + padding + Item.slot.getWidth(null) / 2, border + 20 + padding + Item.slot.getHeight(null) / 2) ;
		Point titlePos = Util.translate(windowPos, size.width / 2, 16) ;
		double angle = Draw.stdAngle ;
		
		GamePanel.DP.drawImage(image, windowPos, angle, Scale.unit, Align.topLeft, stdOpacity) ;
		
		GamePanel.DP.drawText(titlePos, Align.center, angle, name, titleFont, Game.palette[0]) ;
				
		
		for (int i = 0 ; i <= itemsOnWindow.size() - 1 ; i += 1)
		{
			Item bagItem = itemsOnWindow.get(i) ;
			String qtdItem = buyMode ? "" : "" ; // TODO pegar bag e mostrar qtos itens tem
			Point namePos = Util.translate(itemPos, border + 10, 0) ;
			Point pricePos = Util.translate(namePos, size.width - border - padding - 50, 0) ;
			Point coinPos = Util.translate(pricePos, 10, 0) ;
			
			checkMouseSelection(mousePos, namePos, Align.centerLeft, new Dimension(100, 10), i) ;
			Color itemColor = this.item == itemsOnWindow.indexOf(bagItem) ? Game.selColor : stdColor ;
			GamePanel.DP.drawImage(Item.slot, itemPos, angle, Scale.unit, Align.center) ;
			GamePanel.DP.drawImage(bagItem.getImage(), itemPos, angle, Scale.unit, Align.center) ;
			GamePanel.DP.drawText(namePos, Align.centerLeft, angle, bagItem.getName() + qtdItem, stdFont, itemColor) ;
			GamePanel.DP.drawText(pricePos, Align.centerRight, angle, String.valueOf(bagItem.getPrice()), stdFont, Game.palette[14]) ;
			GamePanel.DP.drawImage(Player.getCoinImg(), coinPos, Align.center) ;
			
			if (this.item == itemsOnWindow.indexOf(bagItem))
			{
				bagItem.displayInfo(Util.translate(windowPos, -10, 0), Align.topRight) ;
			}
			itemPos.y += 23 ;
		}
		
		Draw.windowArrows(Util.translate(windowPos, 0, size.height + 10), size.width, window, numberWindows, stdOpacity) ;
		
	}
}
