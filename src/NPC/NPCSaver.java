package NPC;

import java.awt.Point;
import java.util.List;

import liveBeings.Pet;
import liveBeings.Player;
import main.Game;

public class NPCSaver extends NPC
{
    public NPCSaver(String name, Point pos, List<NPCMenu> menus)
	{
		super(NPCJobs.saver, name, pos, menus);
    }
	
	public void act(Player player, Pet pet, String action)
	{
		
		if (action == null) { return ;}
		
		if (actionIsForward(action) & currentMenuID == 1)
		{
			int slot = selOption + 1 ;
	        player.save(slot) ;
	        Game.setSaveSlotInUse(slot) ;
		}

    }
    
}
