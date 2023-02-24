package components ;

import java.util.Map;

import items.Item;
import liveBeings.CreatureTypes;
import windows.BagWindow;

public class Quests
{
	private int id ;
	private String name ;
	private String type ;
	private boolean isActive ;
	private boolean isComplete ;
	private Map<CreatureTypes, Integer> reqCreaturesCounter ;
	private Map<CreatureTypes, Integer> reqCreatureTypes;
	private Map<Item, Integer> reqItems;
	private int goldReward ;
	private int expReward ;
	private Map<Item, Integer> rewardItems ;
	private String description ;
	
	public Quests(int id, String type, Map<CreatureTypes, Integer> reqCreaturesCounter, 
			Map<CreatureTypes, Integer> reqCreatureTypes, Map<Item, Integer> reqItems,
			int goldReward, int expReward, Map<Item, Integer> rewardItems, String description)
	{
		this.id = id ;
		name = String.valueOf("Quest " + id) ;
		this.type = type ;
		isActive = false ;
		isComplete = false ;
		this.reqCreaturesCounter = reqCreaturesCounter ;
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
	public Map<CreatureTypes, Integer> getCounter() {return reqCreaturesCounter ;}
	public Map<CreatureTypes, Integer> getReqCreatures() {return reqCreatureTypes ;}
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
	
	public boolean isActive() { return isActive ;}
	public boolean isComplete() { return isComplete ;}
	
	public void activate() { isActive = true ;}
	public void deactivate() { isActive = false ;}
	
	public void IncReqCreaturesCounter(CreatureTypes creatureType)
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
				if (!reqCreatureTypes.get(type).equals(reqCreaturesCounter.get(type))) {isComplete = false ;}
			});
		}
		
		if (reqItems == null) { return ;}

		reqItems.keySet().forEach(item -> {
			if (!bag.contains(item)) {isComplete = false ;}
		});
	}
}