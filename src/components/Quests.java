package components ;

import java.util.HashMap;
import java.util.Map;

import items.Item;
import liveBeings.CreatureTypes;
import main.Game;
import main.Languages;

public class Quests
{
	private int ID ;
	private String Name ;
	private String Type ;
	private boolean isActive ;
	private int[] Counter ;				// Counter for the creatures killed
	private Map<CreatureTypes, Integer> reqCreatureTypes;
	private Map<Item, Integer> reqItems;
	//private int[] ReqCreatures ;
	//private int[] ReqCreaturesAmounts ;
	//private int[] ReqItems ;
	//private int[] ReqItemsAmounts ;
	private int GoldReward ;
	private int ExpReward ;
	private Map<Item, Integer> rewardItems;
	//private int[] ItemsReward ;
	//private int[] ItemsRewardAmounts ;
	private String Description ;
	
	public Quests(int ID)
	{
		this.ID = ID ;
		reqCreatureTypes = new HashMap<>() ;
		reqItems = new HashMap<>() ;
		rewardItems = new HashMap<>() ;
	}

	public int getID() {return ID ;}
	public String getName() {return Name ;}
	public String getType() {return Type ;}
	public int[] getCounter() {return Counter ;}
	public Map<CreatureTypes, Integer> getReqCreatures() {return reqCreatureTypes ;}
	public Map<Item, Integer> getReqItems() {return reqItems ;}
	//public int[] getReqCreaturesAmounts() {return ReqCreaturesAmounts ;}
	//public int[] getReqItems() {return ReqItems ;}
	//public int[] getReqItemsAmounts() {return ReqItemsAmounts ;}
	public int getGoldReward() {return GoldReward ;}
	public int getExpReward() {return ExpReward ;}
	//public int[] getItemsReward() {return ItemsReward ;}
	//public int[] getItemsRewardAmounts() {return ItemsRewardAmounts ;}
	public String getDescription() {return Description ;}
	public void setID(int id) {ID = id ;}
	public void setName(String N) {Name = N ;}
	public void setType(String T) {Type = T ;}
	public void setCounter(int[] C) {Counter = C ;}
	//public void setReqCreatures(int[] RC) {ReqCreatures = RC ;}
	//public void setReqCreaturesAmounts(int[] RCA) {ReqCreaturesAmounts = RCA ;}
	//public void setReqItems(int[] RI) {ReqItems = RI ;}
	//public void setReqItemsAmounts(int[] RIA) {ReqItemsAmounts = RIA ;}
	public void setGoldReward(int GR) {GoldReward = GR ;}
	public void setExpReward(int ER) {ExpReward = ER ;}
	public Map<Item, Integer> getRewardItems() {return rewardItems ;}
	//public void setItemsReward(int[] IR) {ItemsReward = IR ;}
	//public void setItemsRewardAmounts(int[] IRA) {ItemsRewardAmounts = IRA ;}
	public void setDescription(String D) {Description = D ;}
	
	public boolean isActive()
	{
		return isActive ;
	}
	
	public void activate()
	{
		isActive = true ;
	}
	public void deactivate()
	{
		isActive = false ;
	}
	
	public void IncReqCreaturesCounter(CreatureTypes creatureType)
	{
		/*for (int c = 0 ; c <= reqCreatureTypes.size() - 1 ; c += 1)
		{
			if (creatureType == reqCreatureTypes.get(creatureType))
			{
				Counter[c] += 1 ;				
			}
		}*/
	}

	public void Initialize(String[] Input, Languages Language, int id, int PlayerJob)
	{
		Name = String.valueOf("Quest " + id) ;
		Type = Input[1] ;
		isActive = false ;
		for (int i = 2 ; i <= 8 - 1 ; i += 2)
		{
			if (0 <= Integer.parseInt(Input[i + 1]))	// TODO quando o input for para o JSon, essa condição pode sair
			{
				reqCreatureTypes.put(Game.getCreatureTypes()[Integer.parseInt(Input[i])], Integer.parseInt(Input[i + 1])) ;
			}
		}
		for (int i = 8 ; i <= 18 - 1 ; i += 2)
		{
			if (0 <= Integer.parseInt(Input[i + 1]))	// TODO quando o input for para o JSon, essa condição pode sair
			{
				reqItems.put(Game.getAllItems()[Integer.parseInt(Input[i])], Integer.parseInt(Input[i + 1])) ;
			}
		}
		GoldReward = Integer.parseInt(Input[18]) ;
		ExpReward = Integer.parseInt(Input[19]) ;
		for (int i = 20 ; i <= 28 - 1 ; i += 2)
		{
			if (0 <= Integer.parseInt(Input[i + 1]))	// TODO quando o input for para o JSon, essa condição pode sair
			{
				rewardItems.put(Game.getAllItems()[Integer.parseInt(Input[i])], Integer.parseInt(Input[i + 1])) ;
			}
		}
		Counter = new int[reqCreatureTypes.size()] ;
		if (Language.equals(Languages.portugues))
		{
			Description = Input[30] ;
		}
		else if (Language.equals(Languages.english))
		{
			Description = Input[31] ;
		}
	}
}