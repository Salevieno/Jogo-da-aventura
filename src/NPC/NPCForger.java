package NPC;

import java.awt.Image;
import java.awt.Point;
import java.util.List;

import liveBeings.Pet;
import liveBeings.Player;
import main.ImageLoader;
import main.Path;
import windows.ForgeWindow;

public class NPCForger extends NPC
{

	private static final Image forgeDeskImage = ImageLoader.loadImage(Path.NPC_IMG + "Forge.png") ;

    public NPCForger(String name, Point pos, List<NPCMenu> menus)
    {
        super(NPCJobs.forger, name, pos, menus, forgeDeskImage);
        this.window = new ForgeWindow() ;
    }
	// TODO update on interaction with player
	// 				((ForgeWindow) window).setItemsForForge(equipsForForge);
// 				((ForgeWindow) window).setBag(playerBag);


				 
// 			case forger:
// 			{
// 				List<Equip> equipsForForge = new ArrayList<>() ;
// 				playerBag.getEquip().keySet().forEach(equipsForForge::add) ;
// 				equipsForForge = equipsForForge.stream().filter(eq -> !Arrays.asList(player.getEquips()).contains(eq)).collect(Collectors.toList());
// 				((ForgeWindow) window).setItemsForForge(equipsForForge);
// 				((ForgeWindow) window).setBag(playerBag);
				
// 				forgerAction(player, playerAction, (ForgeWindow) window) ;
				
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
