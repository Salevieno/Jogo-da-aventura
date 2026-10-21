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
            new NPCSeller(NPCJobs.equipsSeller.toString(), new Point(0, 0), NPC.getJobMenus().get(NPCJobs.equipsSeller), getEquipsStock(i)) ;
            new NPCSeller(NPCJobs.itemsSeller.toString(), new Point(0, 0), NPC.getJobMenus().get(NPCJobs.itemsSeller), getItemsStock(i)) ;
            new NPCSeller(NPCJobs.smuggleSeller.toString(), new Point(0, 0), NPC.getJobMenus().get(NPCJobs.smuggleSeller), getSmuggleStock(i)) ;
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
    private static Map<Item, Integer> getEquipsStock(int cityID)
    {
        Map<Item, Integer> equipsStock = new HashMap<>() ;
        equipsStock.put(Item.getAllItems().get(300 + 200 * cityID), 1) ;
        equipsStock.put(Item.getAllItems().get(305 + 200 * cityID), 1) ;
        equipsStock.put(Item.getAllItems().get(307 + 200 * cityID), 1) ;
        equipsStock.put(Item.getAllItems().get(309 + 200 * cityID), 1) ;
        equipsStock.put(Item.getAllItems().get(315 + 200 * cityID), 1) ;
        equipsStock.put(Item.getAllItems().get(322 + 200 * cityID), 1) ;
        equipsStock.put(Item.getAllItems().get(326 + 200 * cityID), 1) ;
        equipsStock.put(Item.getAllItems().get(328 + 200 * cityID), 1) ;
        equipsStock.put(Item.getAllItems().get(332 + 200 * cityID), 1) ;
        equipsStock.put(Item.getAllItems().get(336 + 200 * cityID), 1) ;
        equipsStock.put(Item.getAllItems().get(340 + 200 * cityID), 1) ;
        equipsStock.put(Item.getAllItems().get(344 + 200 * cityID), 1) ;

        return equipsStock ;
    }

    private static Map<Item, Integer> getItemsStock(int cityID)
    {
        Map<Item, Integer> itemsStock = new HashMap<>() ;
        itemsStock.put(Item.getAllItems().get(0), 12) ;
        itemsStock.put(Item.getAllItems().get(1), 12) ;
        itemsStock.put(Item.getAllItems().get(4), 4) ;
        itemsStock.put(Item.getAllItems().get(5), 4) ;
        itemsStock.put(Item.getAllItems().get(121), 2) ;
        itemsStock.put(Item.getAllItems().get(122), 6) ;
        itemsStock.put(Item.getAllItems().get(125), 4) ;
        itemsStock.put(Item.getAllItems().get(130), 2) ;
        itemsStock.put(Item.getAllItems().get(1301), 8) ;
        itemsStock.put(Item.getAllItems().get(1305), 1) ;
        itemsStock.put(Item.getAllItems().get(1702), 1) ;
        itemsStock.put(Item.getAllItems().get(1708), 2) ;
        itemsStock.put(Item.getAllItems().get(1710), 10) ;
        itemsStock.put(Item.getAllItems().get(1713), 13) ;

        return itemsStock ;
    }

    private static Map<Item, Integer> getSmuggleStock(int cityID)
    {
        Map<Item, Integer> smuggleStock = new HashMap<>() ;
        smuggleStock.put(Item.getAllItems().get(400), 1) ;
        smuggleStock.put(Item.getAllItems().get(402), 1) ;
        smuggleStock.put(Item.getAllItems().get(403), 1) ;
        smuggleStock.put(Item.getAllItems().get(406), 1) ;
        smuggleStock.put(Item.getAllItems().get(407), 1) ;
        smuggleStock.put(Item.getAllItems().get(410), 1) ;
        smuggleStock.put(Item.getAllItems().get(413), 1) ;
        smuggleStock.put(Item.getAllItems().get(415), 1) ;
        smuggleStock.put(Item.getAllItems().get(420), 1) ;
        smuggleStock.put(Item.getAllItems().get(421), 1) ;
        smuggleStock.put(Item.getAllItems().get(424), 1) ;
        smuggleStock.put(Item.getAllItems().get(426), 1) ;
        smuggleStock.put(Item.getAllItems().get(429), 1) ;
        smuggleStock.put(Item.getAllItems().get(430), 1) ;
        smuggleStock.put(Item.getAllItems().get(432), 1) ;
        smuggleStock.put(Item.getAllItems().get(436), 1) ;
        smuggleStock.put(Item.getAllItems().get(440), 1) ;

        return smuggleStock ;
    }
}
