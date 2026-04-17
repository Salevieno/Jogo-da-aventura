package NPC;

import java.awt.Point;
import java.util.List;

import liveBeings.Pet;
import liveBeings.Player;

public class NPCDoctor extends NPC
{

    public NPCDoctor(String name, Point pos, List<NPCMenu> menus)
    {
        super(NPCJobs.doctor, name, pos, menus);
    }
	
	public void act(Player player, Pet pet, String action)
	{

		if (pet == null && player.getPA().getLife().isMaxed())
		{
			currentMenuID = 1 ;
			return ;
		}
		if (pet != null && pet.getPA().getLife().isMaxed() && player.getPA().getLife().isMaxed())
		{
			currentMenuID = 1 ;
			return ;
		}

		if (action == null) { return ;}
		
		if (selOption == 0 && actionIsForward(action))
		{

			if (pet != null)
			{
				pet.getPA().getLife().setToMaximum() ;
				pet.getPA().getMp().setToMaximum() ;
			}		
			
			player.getPA().getLife().setToMaximum() ;
			player.getPA().getMp().setToMaximum() ;
		}

    }
    
}
