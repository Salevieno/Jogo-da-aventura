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

	private static final Image FORGE_DESK_IMAGE = ImageLoader.loadImage(Path.NPC_IMG + "Forge.png") ;

    public NPCForger(String name, Point pos, List<NPCMenu> menus)
    {
        super(NPCJobs.forger, name, pos, menus, FORGE_DESK_IMAGE, new ForgeWindow());
    }

	public void act(Player player, Pet pet, String action)
	{
		if (action == null) { return ;}

		if (currentMenuID == 0 & selOption == 0 & actionIsForward(action))
		{
			player.switchOpenClose(window) ;
		}
    }    
}