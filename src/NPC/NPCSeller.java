package NPC;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Map;

import items.Item;
import liveBeings.Pet;
import liveBeings.Player;
import main.Game;
import windows.ShoppingWindow;

public class NPCSeller extends NPC
{
    public NPCSeller(String name, Point pos, List<NPCMenu> menus, Map<Item, Integer> maxStock)
    {// TODO criar super tipo para NPC, definindo acoes de interacao, e criar subtipos para cada tipo de NPC, para definir falas
        super(NPCJobs.equipsSeller, name, pos, menus, new ShoppingWindow(maxStock)) ;
    }

	protected void onEndInteraction()
	{
		if (selOption == 1)
		{
			Game.getPlayer().switchOpenClose(Game.getPlayer().getBag()) ;
		}
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
                player.getBag().setSellingMode(true) ;
                player.switchOpenClose(player.getBag()) ;

                return ;
			}
            shopping.openShopBag() ;
			player.switchOpenClose(shopping) ;
		}

        if (currentMenuID == 1 && action.equals(KeyEvent.getKeyText(KeyEvent.VK_ESCAPE)))
        {
            player.switchOpenClose(player.getBag()) ;
        }
	}
}