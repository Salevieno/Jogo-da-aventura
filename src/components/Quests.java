package components ;

import java.util.HashMap;
import java.util.Map;

import items.Item;
import liveBeings.CreatureTypes;
import main.Game;
import main.Languages;
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
	private Map<Item, Integer> rewardItems;
	private String description ;
	
	public Quests(int ID)
	{
		this.id = ID ;
		name = String.valueOf("Quest " + id) ;
		isActive = false ;
		isComplete = false ;
		reqCreaturesCounter = new HashMap<>() ;
		reqCreatureTypes = new HashMap<>() ;
		reqItems = new HashMap<>() ;
		rewardItems = new HashMap<>() ;
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

	public void Initialize(String[] Input, Languages language, int id, int PlayerJob)
	{
		type = Input[1] ;
		for (int i = 2 ; i <= 8 - 1 ; i += 2)
		{
			if (0 <= Integer.parseInt(Input[i + 1]))	// TODO quando o input for para o JSon, essa condição pode sair
			{
				reqCreatureTypes.put(Game.getCreatureTypes()[Integer.parseInt(Input[i])], Integer.parseInt(Input[i + 1])) ;
				reqCreaturesCounter.put(Game.getCreatureTypes()[Integer.parseInt(Input[i])], 0) ;
			}
		}
		for (int i = 8 ; i <= 18 - 1 ; i += 2)
		{
			if (0 <= Integer.parseInt(Input[i + 1]))	// TODO quando o input for para o JSon, essa condição pode sair
			{
				reqItems.put(Game.getAllItems()[Integer.parseInt(Input[i])], Integer.parseInt(Input[i + 1])) ;
			}
		}
		goldReward = Integer.parseInt(Input[18]) ;
		expReward = Integer.parseInt(Input[19]) ;
		for (int i = 20 ; i <= 28 - 1 ; i += 2)
		{
			if (0 <= Integer.parseInt(Input[i + 1]))	// TODO quando o input for para o JSon, essa condição pode sair
			{
				rewardItems.put(Game.getAllItems()[Integer.parseInt(Input[i])], Integer.parseInt(Input[i + 1])) ;
			}
		}
		description = Input[30 + language.ordinal()] ;
	}
}