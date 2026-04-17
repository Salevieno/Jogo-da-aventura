package NPC;

import java.awt.Point;
import java.util.List;

import liveBeings.Pet;
import liveBeings.Player;
import windows.ElementalWindow;

public class NPCElemental extends NPC
{

    public NPCElemental(String name, Point pos, List<NPCMenu> menus)
    {
        super(NPCJobs.elemental, name, pos, menus);
        this.window = new ElementalWindow() ;
    }
	// 			case elemental:
// 			{
// 				List<Equip> listEquips = new ArrayList<Equip> (playerBag.getEquip().keySet()) ;

// 				((ElementalWindow) window).setItems(listEquips, ElementalWindow.spheresInBag(playerBag)) ;
				
// 				elementalAction(player, (ElementalWindow) window, player.getCurrentAction()) ;

// 				break ;
// 			}
	public void act(Player player, Pet pet, String action)
	{
		
		if (action == null) { return ;}

		if (currentMenuID == 0 & selOption == 0 & actionIsForward(action))
		{
			player.switchOpenClose(window) ;
		}	

    }
    
}
