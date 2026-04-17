package NPC;

import java.awt.Point;
import java.util.List;

import graphics2.Draw;
import liveBeings.Pet;
import liveBeings.Player;
import utilities.Util;

public class NPCCitizen extends NPC
{

    public NPCCitizen(String name, Point pos, List<NPCMenu> menus)
    {
        super(NPCJobs.citizen, name, pos, menus) ;
    }

    public void displaySpeech()
    {
		Point speechPos = Util.translate(pos, 0, 10 - job.getImage().getHeight(null)) ;

		Draw.speech(speechPos, menus.get(currentMenuID).getSpeech(), stdfont, speakingBubble, stdColor) ;
    }
	
	public void act(Player player, Pet pet, String action) { }
    
}
