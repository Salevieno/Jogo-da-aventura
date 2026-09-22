package NPC;

import java.awt.Point;
import java.util.List;
import java.util.Map;

import items.Item;
import liveBeings.Pet;
import liveBeings.Player;
import windows.ShoppingWindow;

public class NPCSeller extends NPC
{
    public NPCSeller(String name, Point pos, List<NPCMenu> menus, Map<Item, Integer> maxStock)
    {// TODO corrigir npcType
        super(NPCJobs.equipsSeller, name, pos, menus, new ShoppingWindow(maxStock)) ;
    }

	public void act(Player player, Pet pet, String action)
	{
		if (action == null) { return ;}
		
		if (currentMenuID == 0 && actionIsForward(action))
		{
			ShoppingWindow shopping = (ShoppingWindow) window ;
			shopping.setBuyMode(selOption == 0) ;
			if (selOption == 1)
			{
				shopping.setIemsForSellingMode(player.getBag()) ;
			}
            shopping.openShopBag() ;
			player.switchOpenClose(shopping) ;
		}
	}
}