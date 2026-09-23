package NPC;

import java.awt.Point;
import java.util.List;

import liveBeings.Pet;
import liveBeings.Player;

public class NPCMaster extends NPC
{

    public NPCMaster(String name, Point pos, List<NPCMenu> menus)
    {
        super(NPCJobs.master, name, pos, menus);
    }

	public void act(Player player, Pet pet, String action)
	{
		if (50 <= player.getLevel() && player.getProJob() == 0 && currentMenuID == 3)
		{
			if (action == null) { return ;}

			if (actionIsForward(action))
			{
				player.setProJob(1 + selOption) ;
				player.addProSpells() ;
				player.getSpellsTreeWindow().enableTab2() ;
			}			
		}
		
		if (action == null) { return ;}
	
		if ((currentMenuID == 0 || currentMenuID == 5) && actionIsForward(action))
		{
			player.switchOpenClose(player.getSpellsTreeWindow()) ;
		}
    }
    
}
