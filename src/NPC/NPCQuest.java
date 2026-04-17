package NPC;

import java.awt.Point;
import java.util.List;

import components.Quest;
import liveBeings.Pet;
import liveBeings.Player;
import main.Log;
import maps.GameMap;

public class NPCQuest extends NPC
{

    public NPCQuest(String name, Point pos, List<NPCMenu> menus)
    {
        super(NPCJobs.questExp, name, pos, menus);
    }
	
	private static int getQuestNPCid(NPC questNPC)
	{
		int questId = 0 ;

		for (GameMap map : GameMap.getAllMaps())
		{
			List<NPC> npcsInMap = map.getNPCs() ;
			if (npcsInMap == null) { continue ;}
			if (npcsInMap.isEmpty()) { continue ;}
			
			for (int i = 0 ; i <= npcsInMap.size() - 1 ; i += 1)
			{
				NPCJobs npcJob = npcsInMap.get(i).getJob() ;
				if (!npcJob.equals(NPCJobs.questExp) & !npcJob.equals(NPCJobs.questItem)) { continue ;}
				if (!npcsInMap.get(i).equals(questNPC)) { questId += 1; continue ;}
				
				return questId ;
			}
		}
		
		return -1 ;
	}

	public void act(Player player, Pet pet, String action)
	{

		if (action == null) { return ;}

		int questID = getQuestNPCid(this) ;
// TODO usar name
		if (questID == -1) { Log.warn("Quest id não encontrado para npc " + job.toString() + " " + id) ; return ;}
		Quest quest = Quest.getAll().get(questID) ;

		if (action.equals("Enter") & selOption == 0)
		{

			if (!player.getQuests().contains(quest))
			{
				quest.checkCompletion(player.getBag()) ;
				if (!quest.isRepeatable() & quest.isComplete())
				{
					quest.complete(player.getBag(), player.getPA(), player.getQuestSkills()) ;
					return ;
				}
				
				player.getQuests().add(quest) ;
			}
			
			quest.checkCompletion(player.getBag()) ;
			
			if (!quest.isComplete()) { return ; }
			
			quest.complete(player.getBag(), player.getPA(), player.getQuestSkills()) ;
			player.getQuests().remove(quest) ;
			
		}
		
	}
}
