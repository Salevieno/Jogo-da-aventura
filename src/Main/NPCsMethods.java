package Main;

import java.awt.Font;

import GameComponents.NPCs;
import GameComponents.Pet;
import GameComponents.Player;
import Speech.Speech;

public class NPCsMethods
{
	Main M = new Main();
	Speech S = new Speech();
	int TextSize = 20;
	public void Doctor(Player player, Pet pet, NPCs npc, Font TextFont)
	{
		/*String Choice = "";
		if (player.getLife() < player.getLifeMax() | pet.getLife() < pet.getLifeMax())
		{
			DF.DrawText(npc.getPos(), TextFont, S.OpeningText("* Doctor *", 1), TextSize, npc.getColor());
			//Choice = M.PlayerChoice(player);
			if (Choice == "s")
			{
				player.setLife(player.getLifeMax());
				pet.setLife(pet.getLifeMax());
			}
			DF.DrawText(npc.getPos(), TextFont, S.OpeningText("* Doctor *", 2), TextSize, npc.getColor());
		}
		else
		{
			DF.DrawText(npc.getPos(), TextFont, S.OpeningText("* Doctor *", 3), TextSize, npc.getColor());			
		}*/
	}
}
