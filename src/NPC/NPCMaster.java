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
	
// 			case master:
// 			{
// 				player.getSpellsTreeWindow().setSpells(player.getSpells()) ;
				
// 				if (50 <= player.getLevel() & player.getProJob() == 0 & menu == 0)
// 				{
// 					menu = 2 ;
// 					String[] proClassesText = Game.allText.get(TextCategories.proclasses) ;
// 					String proJob1 = proClassesText[2 * player.getJob() + 0] ;
// 					String proJob2 = proClassesText[2 * player.getJob() + 1] ;
// 					type.getOptions().set(3, new ArrayList<String>(Arrays.asList(proJob1, proJob2))) ;
// 				}
// 				window = player.getSpellsTreeWindow() ;
				
// 				masterAction(player, player.getCurrentAction(), mousePos, (SpellsTreeWindow) window) ;

// 				break ;
// 			}
	public void act(Player player, Pet pet, String action)
	{
		if (50 <= player.getLevel() & player.getProJob() == 0 & currentMenuID == 3)
		{
			if (action == null) { return ;}

			if (actionIsForward(action))
			{
				player.setProJob(1 + selOption) ;
				player.addProSpells() ;
				player.getSpellsTreeWindow().switchTo2Tabs() ;
			}			
		}
		
		if (action == null) { return ;}
	
		if ((currentMenuID == 0 | currentMenuID == 5) & actionIsForward(action))
		{
			player.getSpellsTreeWindow().setplayerCurrentSpells(player.getSpells()) ;
			player.getSpellsTreeWindow().setPoints(player.getSpellPoints()) ;
			player.getSpellsTreeWindow().setSpells(player.getSpells()) ;
			player.getSpellsTreeWindow().updateSpellsDistribution() ;
			player.switchOpenClose(player.getSpellsTreeWindow()) ;
		}
    }
    
}
