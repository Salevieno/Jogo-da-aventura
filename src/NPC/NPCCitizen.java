package NPC;

import java.awt.Point;
import java.util.List;

import liveBeings.Pet;
import liveBeings.Player;

public class NPCCitizen extends NPC
{
    public NPCCitizen(String name, Point pos, List<NPCMenu> menus)
    {
        super(NPCJobs.citizen, name, pos, menus) ;
    }

	public void act(Player player, Pet pet, String action) { }    
}
