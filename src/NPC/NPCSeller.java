package NPC;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import items.Item;
import liveBeings.Pet;
import liveBeings.Player;
import utilities.Util;
import windows.ShoppingWindow;

public class NPCSeller extends NPC
{

	private boolean renewStocks = false ;
    private final ShoppingWindow shopping ;

    public NPCSeller(String name, Point pos, List<NPCMenu> menus, List<Item> itemsForSale)
    {// TODO corrigir npcType
        super(NPCJobs.equipsSeller, name, pos, menus) ;
        this.shopping = new ShoppingWindow(itemsForSale) ;
    }
	
	private int[] newSmuggledStock()
	{
		List<Integer> fullStock = new ArrayList<>() ;
		for (int i = 0 ; i <= 99 - 1 ; i += 1)
		{
			fullStock.add(200 + i) ;
		}
		int[] newStockIDs = new int[12] ; 
		for (int i = 0 ; i <= newStockIDs.length - 1 ; i += 1)
		{
			int newItem = Util.randomInt(0, fullStock.size() - 1) ;
			newStockIDs[i] = fullStock.get(newItem) ;
			fullStock.remove(newItem) ;
		}
		
		return newStockIDs ;
	}
	
	public void renewStocks() { renewStocks = true ;}


// 			case equipsSeller:
// 			{
// 				int[] itemIDs = new int[] {300, 305, 307, 309, 315, 322, 326, 328, 332, 336, 340, 344} ;
// 				int cityID = id / 17 ;
// 				for (int i = 0 ; i <= itemIDs.length - 1; i += 1) { itemIDs[i] += 200 * cityID ;}
// //		    	List<Item> itemsOnSale = new ArrayList<>() ;
// //		    	for (int itemID : itemIDs) { itemsOnSale.add(Game.getAllItems()[itemID]) ;}
		    	
// 		    	window = new ShoppingWindow(Item.getItems(itemIDs)) ;
// 				((NPCSeller) this).action(player, playerAction) ;
		    	
// 		    	// sellerAction(player, playerAction, (ShoppingWindow) window) ;
		    	
// 		    	break ;
// 			}
// 			case itemsSeller:
// 				int[] itemIDs = new int[] {1329, 0, 1, 4, 5, 121, 122, 125, 130, 1301, 1305, 1702, 1708, 1710, 1713} ;
// //		    	List<Item> itemsOnSale = new ArrayList<>() ;
// //		    	for (int itemID : itemIDs) { itemsOnSale.add(Game.getAllItems()[itemID]) ;}
		    	
// 		    	window = new ShoppingWindow(Item.getItems(itemIDs)) ;
		    	
// 				((NPCSeller) this).action(player, playerAction) ;
// 		    	// sellerAction(player, playerAction, (ShoppingWindow) window) ;
		    	
// 		    	break ;
				
// 			case smuggleSeller:
// 				int[] itemids = newSmuggledStock() ;
// 				if (renewStocks)
// 				{
// 					itemids = newSmuggledStock() ;
// 					renewStocks = false ;
// 				}
// 				int cityID = id / 17 ;
// 				for (int i = 0 ; i <= itemids.length - 1; i += 1)
// 				{
// 					itemids[i] += 200 * cityID ;
// 				}
		    	
// 		    	window = new ShoppingWindow(Item.getItems(itemids)) ;
		    	
// 				((NPCSeller) this).action(player, playerAction) ;
// 		    	// sellerAction(player, playerAction, (ShoppingWindow) window) ;
				
// 		    	break ;

	public void act(Player player, Pet pet, String action)
	{

		if (action == null) { return ;}
		
		if (currentMenuID == 0 & actionIsForward(action))
		{
			shopping.setBuyMode(selOption == 0) ;
			if (selOption == 1)
			{
				shopping.setIemsForSellingMode(player.getBag()) ;
			}
			player.switchOpenClose(shopping) ;
		}
		
	}
}
