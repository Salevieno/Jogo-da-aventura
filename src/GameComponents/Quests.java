package GameComponents ;

import java.io.IOException ;
import java.util.Map;

import LiveBeings.CreatureTypes;
import Main.Game;
import Utilities.UtilG;

public class Quests
{
	private int ID ;
	private String Name ;
	private String Type ;
	private boolean isActive ;
	private int[] Counter ;				// Counter for the creatures killed
	private Map<CreatureTypes, Integer> reqCreatureTypes;
	//private int[] ReqCreatures ;
	//private int[] ReqCreaturesAmounts ;
	private int[] ReqItems ;
	private int[] ReqItemsAmounts ;
	private int GoldReward ;
	private int ExpReward ;
	private int[] ItemsReward ;
	private int[] ItemsRewardAmounts ;
	private String Description ;
	
	public Quests(int ID)
	{
		this.ID = ID ;
	}

	public int getID() {return ID ;}
	public String getName() {return Name ;}
	public String getType() {return Type ;}
	public int[] getCounter() {return Counter ;}
	public Map<CreatureTypes, Integer> getReqCreatures() {return reqCreatureTypes ;}
	//public int[] getReqCreaturesAmounts() {return ReqCreaturesAmounts ;}
	public int[] getReqItems() {return ReqItems ;}
	public int[] getReqItemsAmounts() {return ReqItemsAmounts ;}
	public int getGoldReward() {return GoldReward ;}
	public int getExpReward() {return ExpReward ;}
	public int[] getItemsReward() {return ItemsReward ;}
	public int[] getItemsRewardAmounts() {return ItemsRewardAmounts ;}
	public String getDescription() {return Description ;}
	public void setID(int id) {ID = id ;}
	public void setName(String N) {Name = N ;}
	public void setType(String T) {Type = T ;}
	public void setCounter(int[] C) {Counter = C ;}
	//public void setReqCreatures(int[] RC) {ReqCreatures = RC ;}
	//public void setReqCreaturesAmounts(int[] RCA) {ReqCreaturesAmounts = RCA ;}
	public void setReqItems(int[] RI) {ReqItems = RI ;}
	public void setReqItemsAmounts(int[] RIA) {ReqItemsAmounts = RIA ;}
	public void setGoldReward(int GR) {GoldReward = GR ;}
	public void setExpReward(int ER) {ExpReward = ER ;}
	public void setItemsReward(int[] IR) {ItemsReward = IR ;}
	public void setItemsRewardAmounts(int[] IRA) {ItemsRewardAmounts = IRA ;}
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

	public void Initialize(String[] Input, String Language, int id, int PlayerJob)
	{
		Name = String.valueOf("Quest " + id) ;
		Type = Input[1] ;
		isActive = false ;
		// TODO change reqCreatures e items to Map<Creature, Amount>
		for (int i = 0 ; i <= 6 - 1 ; i += 2)
		{
			if (0 <= Integer.parseInt(Input[i + 3]))	// TODO quando o input for para o JSon, essa condição pode sair
			{
				reqCreatureTypes.put(Game.getCreatureTypes()[Integer.parseInt(Input[i + 2])], Integer.parseInt(Input[i + 3])) ;
			}
		}
		//ReqCreatures = new int[] {Integer.parseInt(Input[2]), Integer.parseInt(Input[4]), Integer.parseInt(Input[6])} ;
		//ReqCreaturesAmounts = new int[] {Integer.parseInt(Input[3]), Integer.parseInt(Input[5]), Integer.parseInt(Input[7])} ;
		ReqItems = new int[] {Integer.parseInt(Input[8]), Integer.parseInt(Input[10]), Integer.parseInt(Input[12]), Integer.parseInt(Input[14]), Integer.parseInt(Input[16])} ;
		ReqItemsAmounts = new int[] {Integer.parseInt(Input[9]), Integer.parseInt(Input[11]), Integer.parseInt(Input[13]), Integer.parseInt(Input[15]), Integer.parseInt(Input[17])} ;
		Counter = new int[reqCreatureTypes.size()] ;
		GoldReward = Integer.parseInt(Input[18]) ;
		ExpReward = Integer.parseInt(Input[19]) ;
		ItemsReward = new int[] {Integer.parseInt(Input[20]), Integer.parseInt(Input[22]), Integer.parseInt(Input[24]), Integer.parseInt(Input[26]), Integer.parseInt(Input[28])} ;
		ItemsRewardAmounts = new int[] {Integer.parseInt(Input[21]), Integer.parseInt(Input[23]), Integer.parseInt(Input[25]), Integer.parseInt(Input[27]), Integer.parseInt(Input[29])} ;
		for (int item = 0 ; item <= ItemsReward.length - 1 ; item += 1)
		{
			if (Items.BagIDs[5] <= ItemsReward[item] & ItemsReward[item] < Items.BagIDs[6])
			{
				ItemsReward[item] += (Items.BagIDs[6] - Items.BagIDs[5]) / 5 * PlayerJob ;
			}
		}
		if (Language.equals("P"))
		{
			Description = Input[30] ;
		}
		else if (Language.equals("E"))
		{
			Description = Input[31] ;
		}
	}
}