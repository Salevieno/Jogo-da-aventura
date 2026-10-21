package NPC;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import items.Item;
import items.Recipe;
import main.Log;

public abstract class NPCData
{
    
    public static void createNPCs()
    {
        if (!NPC.getAll().isEmpty()) { Log.warn("Trying to create NPCs twice") ; return ;}

        int[] itemIDs = new int[] {300, 305, 307, 309, 315, 322, 326, 328, 332, 336, 340, 344} ;
        Map<Item, Integer> equipsStock = new HashMap<>() ;
        for (int itemID : itemIDs)
        {
            equipsStock.put(Item.getAllItems().get(itemID), 1) ; // TODO  + 200 * cityID
        }

        itemIDs = new int[] {1329, 0, 1, 4, 5, 121, 122, 125, 130, 1301, 1305, 1702, 1708, 1710, 1713} ;
        int[] qtdInStock = new int[] {20, 12, 12, 4, 4, 2, 6, 4, 2, 8, 1, 1, 2, 10, 13} ;
        Map<Item, Integer> itemsStock = new HashMap<>() ;
        int j = 0 ;
        for (int itemID : itemIDs)
        {
            itemsStock.put(Item.getAllItems().get(itemID), qtdInStock[j]) ;
            j += 1 ;
        }

        itemIDs = new int[] {400, 402, 403, 406, 407, 410, 413, 415, 420, 421, 424, 426, 429, 430, 432, 436, 440} ;
        Map<Item, Integer> smuggleStock = new HashMap<>() ;
        for (int itemID : itemIDs)
        {
            smuggleStock.put(Item.getAllItems().get(itemID), 1) ;
        }

        // Creating city NPCs
        for (int i = 0 ; i <= 5 - 1 ; i += 1) // number cities
        {
            new NPCCrafter(NPCJobs.alchemist.toString(), new Point(0, 0), NPC.getJobMenus().get(NPCJobs.alchemist), Recipe.getAll().subList(0, 40)) ;
            new NPCBanker(NPCJobs.banker.toString(), new Point(0, 0), NPC.getJobMenus().get(NPCJobs.banker)) ;
            new NPCCitizen(NPCJobs.citizen.toString(), new Point(0, 0), NPC.getJobMenus().get(NPCJobs.citizen)) ;
            new NPCCitizen(NPCJobs.citizen.toString(), new Point(0, 0), NPC.getJobMenus().get(NPCJobs.citizen)) ;
            new NPCCitizen(NPCJobs.citizen.toString(), new Point(0, 0), NPC.getJobMenus().get(NPCJobs.citizen)) ;
            new NPCCitizen(NPCJobs.citizen.toString(), new Point(0, 0), NPC.getJobMenus().get(NPCJobs.citizen)) ;
            new NPCCrafter(NPCJobs.crafter.toString(), new Point(0, 0), NPC.getJobMenus().get(NPCJobs.crafter), Recipe.getAll().subList(58, Recipe.getAll().size())) ;
            new NPCDoctor(NPCJobs.doctor.toString(), new Point(0, 0), NPC.getJobMenus().get(NPCJobs.doctor)) ;
            new NPCElemental(NPCJobs.elemental.toString(), new Point(0, 0), NPC.getJobMenus().get(NPCJobs.elemental)) ;
            new NPCSeller(NPCJobs.equipsSeller.toString(), new Point(0, 0), NPC.getJobMenus().get(NPCJobs.equipsSeller), equipsStock) ;
            new NPCSeller(NPCJobs.itemsSeller.toString(), new Point(0, 0), NPC.getJobMenus().get(NPCJobs.itemsSeller), itemsStock) ;
            new NPCSeller(NPCJobs.smuggleSeller.toString(), new Point(0, 0), NPC.getJobMenus().get(NPCJobs.smuggleSeller), smuggleStock) ;
            new NPCMaster(NPCJobs.master.toString(), new Point(0, 0), NPC.getJobMenus().get(NPCJobs.master)) ;
            new NPCCrafter(NPCJobs.woodcrafter.toString(), new Point(0, 0), NPC.getJobMenus().get(NPCJobs.woodcrafter), Recipe.getAll().subList(40, 58)) ;
            new NPCForger(NPCJobs.forger.toString(), new Point(0, 0), NPC.getJobMenus().get(NPCJobs.forger)) ;
            new NPCSaver(NPCJobs.saver.toString(), new Point(0, 0), NPC.getJobMenus().get(NPCJobs.saver)) ;
            new NPCQuest(NPCJobs.questExp.toString(), new Point(0, 0), NPC.getJobMenus().get(NPCJobs.questExp), i) ;
        }

        for (int i = 0 ; i <= 64 - 1 ; i += 1) // number of quest NPCs
        {
            new NPCQuest(NPCJobs.questItem.toString(), new Point(0, 0), NPC.getJobMenus().get(NPCJobs.questItem), i + 5) ;
        }
    }
}
