package components ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import attributes.PersonalAttributes;
import items.Item;
import liveBeings.CreatureType;
import main.Game;
import main.Languages;
import main.Path;
import utilities.Util;
import windows.BagWindow;

public class Quest
{
	private final int id ;
	private final String name ;
	private final String type ;
//	private final boolean isActive ;
	private final boolean isRepeatable ;
	private final Map<CreatureType, Integer> reqCreaturesCounter ;
	private final Map<CreatureType, Integer> reqCreatureTypes;
	private final Map<Item, Integer> reqItems;
	private final int goldReward ;
	private final int expReward ;
	private final Map<Item, Integer> rewardItems ;
	private final String description ;
	private boolean isComplete ;

	protected static final String dadosPath = Path.DADOS + "quests\\" ;
	public static final List<Quest> all ;
	
	static
	{
		all = new ArrayList<>() ;
	}
	
	public Quest(int id, String type, boolean isRepeatable, Map<CreatureType, Integer> reqCreatureTypes, Map<Item, Integer> reqItems,
			int goldReward, int expReward, Map<Item, Integer> rewardItems, String name, String description)
	{
		
		this.id = id ;
		this.name = name ;
		this.type = type ;
		isComplete = false ;
		this.isRepeatable = isRepeatable ;
		reqCreaturesCounter = new HashMap<>() ;
		if (reqCreatureTypes != null)
		{
			reqCreatureTypes.keySet().forEach(creatureType -> reqCreaturesCounter.put(creatureType, 0)) ;
		}
		
		this.reqCreatureTypes = reqCreatureTypes ;
		this.reqItems = reqItems ;
		this.goldReward = goldReward ;
		this.expReward = expReward ;
		this.rewardItems = rewardItems ;
		this.description = description ;
		all.add(this);
	}


	public static Quest[] load(Languages language, int playerJob, List<CreatureType> creatureTypes, List<Item> allItems)
	{
		List<String[]> inputs = Util.readcsvFile(dadosPath + "Quests.csv") ;
		Quest[] quests = new Quest[inputs.size()] ;
		for (int i = 0 ; i <= quests.length - 1 ; i += 1)
		{
			String[] input = inputs.get(i) ;
			int id = Integer.parseInt(input[0]) ;
			String type = input[1] ;
			Map<CreatureType, Integer> reqCreatureTypes = new HashMap<>() ;
			Map<Item, Integer> reqItems = new HashMap<>() ;

			for (int j = 2 ; j <= 8 - 1 ; j += 2)
			{
				if (Integer.parseInt(input[j]) <= -1) { continue ;}
				
				reqCreatureTypes.put(creatureTypes.get(Integer.parseInt(input[j])), Integer.parseInt(input[j + 1])) ;
			}

			for (int j = 8 ; j <= 18 - 1 ; j += 2)
			{
				if (Integer.parseInt(input[j]) <= -1) { continue ;}
				
				reqItems.put(allItems.get(Integer.parseInt(input[j])), Integer.parseInt(input[j + 1])) ;
			}

			int goldReward = Integer.parseInt(input[18]) ;
			int expReward = Integer.parseInt(input[19]) ;
			boolean isRepeatable = 0 <= expReward ;
			Map<Item, Integer> rewardItems = new HashMap<>() ;

			for (int j = 20 ; j <= 28 - 1 ; j += 2)
			{
				if (Integer.parseInt(input[j + 1]) <= -1) { continue ;}
				
				rewardItems.put(allItems.get(Integer.parseInt(input[j])), Integer.parseInt(input[j + 1])) ;
			}

			String name = input[30 + language.ordinal()] ;
			String description = input[32 + language.ordinal()] ;

			quests[i] = new Quest(id, type, isRepeatable, reqCreatureTypes, reqItems, goldReward, expReward,
					rewardItems, name, description) ;
		}

		return quests ;
	}
	
	
	public boolean isComplete() { return isComplete ;}
	public boolean isRepeatable() { return isRepeatable ;}
	
	private void resetCreaturesCounter() { reqCreatureTypes.keySet().forEach(creatureType -> reqCreaturesCounter.put(creatureType, 0)) ;}
	
	public void IncReqCreaturesCounter(CreatureType creatureType)
	{
		reqCreatureTypes.keySet().forEach(type -> {
			if (type.equals(creatureType)) {reqCreaturesCounter.put(type, reqCreaturesCounter.get(type) + 1) ;}
		});
	}
	
	public void checkCompletion(BagWindow bag)
	{
		
		isComplete = true ;
		if (reqCreatureTypes != null)
		{
			reqCreatureTypes.keySet().forEach(type -> {
				if (reqCreaturesCounter.get(type) < reqCreatureTypes.get(type)) {isComplete = false ;}
			});
		}
		
		if (reqItems == null) { return ;}

		reqItems.keySet().forEach(item -> {
			if (!bag.contains(item)) {isComplete = false ;}
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

		for (Item item : reqItems.keySet()) { bag.remove(item, 1) ;}
		for (Item item : rewardItems.keySet()) { bag.add(item, 1) ;}
		giveSkillRewards(skills) ;
//		Game.getAnimations().get(12).start(200, new Object[] {Game.getScreen().pos(0.2, 0.1), "Quest completa!", Game.colorPalette[4]}) ;
		
		
		isComplete = true ;
		
	}

	public int getID() {return id ;}
	public String getName() {return name ;}
	public String getType() {return type ;}
	public Map<CreatureType, Integer> getCounter() {return reqCreaturesCounter ;}
	public Map<CreatureType, Integer> getReqCreatures() {return reqCreatureTypes ;}
	public Map<Item, Integer> getReqItems() {return reqItems ;}
	public int getGoldReward() {return goldReward ;}
	public int getExpReward() {return expReward ;}
	public String getDescription() {return description ;}
	public Map<Item, Integer> getRewardItems() {return rewardItems ;}

	@Override
	public String toString()
	{
		return id + ";" + type + ";" + isRepeatable + ";" + reqCreatureTypes + ";" + reqItems + ";" + goldReward + ";" + expReward + ";" + rewardItems ;
	}	
}