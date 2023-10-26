package components ;

import java.util.HashMap;
import java.util.Map;

import attributes.PersonalAttributes;
import items.Item;
import liveBeings.CreatureType;
import main.Game;
import windows.BagWindow;

public class Quest
{
	private int id ;
	private String name ;
	private String type ;
//	private boolean isActive ;
	private boolean isComplete ;
	private boolean isRepeatable ;
	private Map<CreatureType, Integer> reqCreaturesCounter ;
	private Map<CreatureType, Integer> reqCreatureTypes;
	private Map<Item, Integer> reqItems;
	private int goldReward ;
	private int expReward ;
	private Map<Item, Integer> rewardItems ;
	private String description ;
	
	public Quest(int id, String type, boolean isRepeatable, Map<CreatureType, Integer> reqCreatureTypes, Map<Item, Integer> reqItems,
			int goldReward, int expReward, Map<Item, Integer> rewardItems, String description)
	{
		
		this.id = id ;
		name = String.valueOf("Quest " + id) ;
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
	public void setID(int id) {this.id = id ;}
	public void setName(String N) {name = N ;}
	public void setType(String T) {type = T ;}
	public void setGoldReward(int GR) {goldReward = GR ;}
	public void setExpReward(int ER) {expReward = ER ;}
	public Map<Item, Integer> getRewardItems() {return rewardItems ;}
	public void setDescription(String D) {description = D ;}
	
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
			case 19: skills.replace(QuestSkills.shovel, true) ; return ;
			case 27: skills.replace(QuestSkills.craftWindow, true) ; return ;
			case 39: Game.letThereBePet() ; return ;
			case 44: skills.replace(QuestSkills.bestiary, true) ; return ;
			case 42: skills.replace(QuestSkills.forestMap, true) ; return ;
			case 46: skills.replace(QuestSkills.caveMap, true) ; return ;
			case 50: skills.replace(QuestSkills.islandMap, true) ; return ;
			case 51: skills.replace(QuestSkills.ride, true) ; return ;
			case 56: skills.replace(QuestSkills.volcanoMap, true) ; return ;
			case 57: return ; // TODO advanced crafting (add recipes)
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
		for (Item item : rewardItems.keySet()) { bag.add(item, 1) ; System.out.println("added " + item);}
		giveSkillRewards(skills) ;
		Game.getAnimations().get(12).start(200, new Object[] {Game.getScreen().pos(0.2, 0.1), "Quest completa!", Game.colorPalette[4]}) ;
		
		
		isComplete = true ;
		
	}

	@Override
	public String toString()
	{
		return id + ";" + type + ";" + isRepeatable + ";" + reqCreatureTypes + ";" + reqItems + ";" + goldReward + ";" + expReward + ";" + rewardItems ;
//		return "Quest [id=" + id + ", name=" + name + ", type=" + type + ", isComplete="
//				+ isComplete + ", isRepeatable=" + isRepeatable + ", reqCreaturesCounter=" + reqCreaturesCounter
//				+ ", reqCreatureTypes=" + reqCreatureTypes + ", reqItems=" + reqItems + ", goldReward=" + goldReward
//				+ ", expReward=" + expReward + ", rewardItems=" + rewardItems + ", description=" + description + "]";
	}
		
	
}