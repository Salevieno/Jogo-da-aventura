package components ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import attributes.PersonalAttributes;
import items.Item;
import liveBeings.CreatureType;
import main.Game;
import main.Path;
import utilities.Util;
import windows.BagWindow;

public class Quest
{
	private final int id ;
	private final boolean isRepeatable ;
	private final Map<Integer, Integer> reqCreaturesCounter ;
	private final Map<Integer, Integer> reqCreatureTypeIDs;
	private final Map<Integer, Integer> reqItemIDs;
	private final Map<Integer, Integer> rewardItemIDs ;
	private final int goldReward ;
	private final int expReward ;
	private String name ;
	private String description ;
	private boolean isComplete ;

	protected static final String dadosPath = Path.DADOS + "quests\\" ;
	private static final List<Quest> all ;
	
	static
	{
		all = new ArrayList<>() ;
	}
	
	// public Quest(int id, boolean isRepeatable, Map<CreatureType, Integer> reqCreatureTypes, Map<Item, Integer> reqItems, int goldReward, int expReward, Map<Item, Integer> rewardItems)
	// {		
	// 	this.id = id ;
	// 	isComplete = false ;
	// 	this.isRepeatable = isRepeatable ;
	// 	reqCreaturesCounter = new HashMap<>() ;
	// 	if (reqCreatureTypes != null)
	// 	{
	// 		reqCreatureTypes.keySet().forEach(creatureType -> reqCreaturesCounter.put(creatureType, 0)) ;
	// 	}
		
	// 	this.reqCreatureTypes = reqCreatureTypes ;
	// 	this.reqItems = reqItems ;
	// 	this.goldReward = goldReward ;
	// 	this.expReward = expReward ;
	// 	this.rewardItems = rewardItems ;
	// 	all.add(this);
	// }
	
	public Quest(int id, boolean isRepeatable, Map<Integer, Integer> reqCreatureTypeIDs, Map<Integer, Integer> reqItemIDs, int goldReward, int expReward, Map<Integer, Integer> rewardItemIDs)
	{		
		this.id = id ;
		isComplete = false ;
		this.isRepeatable = isRepeatable ;
		reqCreaturesCounter = new HashMap<>() ;
		if (reqCreatureTypeIDs != null)
		{
			reqCreatureTypeIDs.keySet().forEach(typeID -> reqCreaturesCounter.put(typeID, 0)) ;
		}
		
		this.reqCreatureTypeIDs = reqCreatureTypeIDs ;
		this.reqItemIDs = reqItemIDs ;
		this.goldReward = goldReward ;
		this.expReward = expReward ;
		this.rewardItemIDs = rewardItemIDs ;
		all.add(this);
	}

	public static void create(String language)
	{
		QuestData.create() ;
		updateText(language) ;
	}

	public static void updateText(String language)
	{
		List<String[]> inputs = Util.readcsvFile(Path.DADOS + language + "/QuestsInfo.csv") ;
		for (int i = 0 ; i <= inputs.size() - 1 ; i += 1)
		{
			Quest quest = all.get(i) ;
			quest.setName(inputs.get(i)[1]) ;
			quest.setDescription(inputs.get(i)[2]) ;
		}
	}
	
	
	public boolean isComplete() { return isComplete ;}
	public boolean isRepeatable() { return isRepeatable ;}
	
	private void resetCreaturesCounter() { reqCreatureTypeIDs.keySet().forEach(creatureType -> reqCreaturesCounter.put(creatureType, 0)) ;}
	
	public void IncReqCreaturesCounter(CreatureType creatureType)
	{
		reqCreatureTypeIDs.keySet().forEach(type -> {
			if (type.equals(creatureType.getID()))
			{
				reqCreaturesCounter.put(type, reqCreaturesCounter.get(type) + 1) ;
			}
		});
	}
	
	public void checkCompletion(BagWindow bag)
	{
		isComplete = true ;
		if (reqCreatureTypeIDs != null && !reqCreatureTypeIDs.isEmpty() && reqCreaturesCounter != null && !reqCreaturesCounter.isEmpty())
		{
			reqCreatureTypeIDs.keySet().forEach(typeID -> {
				if (reqCreaturesCounter.get(typeID) < reqCreatureTypeIDs.get(typeID))
				{
					isComplete = false ;
				}
			});
		}
		
		if (reqItemIDs == null) { return ;}

		reqItemIDs.keySet().forEach(itemID -> {
			if (!bag.contains(Item.getAllItems().get(itemID)))
			{
				isComplete = false ;
			}
		});
		
	}

	public void giveSkillRewards(Map<QuestSkills, Boolean> skills)
	{
		switch (id)
		{
			case 19: skills.replace(QuestSkills.dig, true) ; return ;
			case 27: skills.replace(QuestSkills.craftWindow, true) ; return ;
			case 39: Game.letThereBePet() ; return ;
			case 44: skills.replace(QuestSkills.bestiary, true) ; return ;
			case 42: skills.replace(QuestSkills.forestMap, true) ; return ;
			case 46: skills.replace(QuestSkills.caveMap, true) ; return ;
			case 50: skills.replace(QuestSkills.islandMap, true) ; return ;
			case 51: skills.replace(QuestSkills.ride, true) ; return ;
			case 56: skills.replace(QuestSkills.volcanoMap, true) ; return ;
			case 57: return ; // proTODO advanced crafting (add recipes)
			case 60: skills.replace(QuestSkills.dragonAura, true) ; return ;
			case 62: skills.replace(QuestSkills.snowlandMap, true) ; return ;
			default: return ;
		}
	}
	
	public void complete(BagWindow bag, PersonalAttributes PA, Map<QuestSkills, Boolean> skills)
	{
		
		resetCreaturesCounter() ;
		
		if (isRepeatable) { isComplete = false ;}
		
		PA.getExp().incCurrentValue(expReward) ;
		bag.addGold(goldReward) ;

		Map<Item, Integer> reqItems = reqItemIDs.entrySet().stream().collect(Collectors.toMap(entry -> Item.getAllItems().get(entry.getKey()), Map.Entry::getValue)) ;
		Map<Item, Integer> rewardItems = rewardItemIDs.entrySet().stream().collect(Collectors.toMap(entry -> Item.getAllItems().get(entry.getKey()), Map.Entry::getValue)) ;

		for (Item item : reqItems.keySet()) { bag.remove(item, 1) ;}
		for (Item item : rewardItems.keySet()) { bag.add(item, 1) ;}

		giveSkillRewards(skills) ;
//		Game.getAnimations().get(12).start(200, new Object[] {Game.getScreen().pos(0.2, 0.1), "Quest completa!", Game.colorPalette[4]}) ;		
		isComplete = true ;
		
	}

	public String getName() {return name ;}
	public Map<Integer, Integer> getCounter() {return reqCreaturesCounter ;}
	public Map<Integer, Integer> getReqCreatures() {return reqCreatureTypeIDs ;}
	public Map<Integer, Integer> getReqItemIDs() {return reqItemIDs ;}	
	public void setName(String name) { this.name = name ;}
	public void setDescription(String description) { this.description = description ;}
	public static List<Quest> getAll() { return all ;}

	@Override
	public String toString()
	{
		return "Quest [id=" + id + ", name=" + name + ", isRepeatable=" + isRepeatable				
				+ ", goldReward=" + goldReward + ", expReward=" + expReward + ", description=" + description + ", isComplete=" + isComplete + "]";
	}

}