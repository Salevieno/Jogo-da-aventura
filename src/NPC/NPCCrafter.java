package NPC;

import java.awt.Point;
import java.util.List;

import items.Recipe;
import liveBeings.Pet;
import liveBeings.Player;
import windows.CraftWindow;

public class NPCCrafter extends NPC
{

    public NPCCrafter(String name, Point pos, List<NPCMenu> menus, List<Recipe> recipes)
    {
        super(NPCJobs.crafter, name, pos, menus);		
        this.window = new CraftWindow(recipes) ;
    }
	
	public void act(Player player, Pet pet, String action)
	{
		if (action == null) { return ;}

		if (currentMenuID == 0 & selOption == 0 & actionIsForward(action))
		{
			((CraftWindow) window).setBag(player.getBag()) ;
			player.switchOpenClose(window) ;
		}
    }
    
}
