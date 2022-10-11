package GameComponents ;

import java.io.IOException ;

import Main.Utg ;

public class Quests
{
	private int ID ;
	private String Name ;
	private String Type ;
	private int[] Counter ;				// Counter for the creatures killed
	private int[] ReqCreatures ;
	private int[] ReqCreaturesAmounts ;
	private int[] ReqItems ;
	private int[] ReqItemsAmounts ;
	private int GoldReward ;
	private int ExpReward ;
	private int[] ItemsReward ;
	private int[] ItemsRewardAmounts ;
	private String Description ;
	
	public static int NumberOfQuests ;
	
	public Quests(int ID)
	{
		this.ID = ID ;
	}

	public int getID() {return ID ;}
	public String getName() {return Name ;}
	public String getType() {return Type ;}
	public int[] getCounter() {return Counter ;}
	public int[] getReqCreatures() {return ReqCreatures ;}
	public int[] getReqCreaturesAmounts() {return ReqCreaturesAmounts ;}
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
	public void setReqCreatures(int[] RC) {ReqCreatures = RC ;}
	public void setReqCreaturesAmounts(int[] RCA) {ReqCreaturesAmounts = RCA ;}
	public void setReqItems(int[] RI) {ReqItems = RI ;}
	public void setReqItemsAmounts(int[] RIA) {ReqItemsAmounts = RIA ;}
	public void setGoldReward(int GR) {GoldReward = GR ;}
	public void setExpReward(int ER) {ExpReward = ER ;}
	public void setItemsReward(int[] IR) {ItemsReward = IR ;}
	public void setItemsRewardAmounts(int[] IRA) {ItemsRewardAmounts = IRA ;}
	public void setDescription(String D) {Description = D ;}
	
	public static void CalcNumberOfQuests(String CSVPath)
	{
		try
		{
			NumberOfQuests = Utg.count(CSVPath + "Quests.csv") ;
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace() ;
		}
	}
	public void IncReqCreaturesCounter(int CreatureType)
	{
		for (int c = 0 ; c <= ReqCreaturesAmounts.length - 1 ; c += 1)
		{
			if (CreatureType == ReqCreatures[c])
			{
				Counter[c] += 1 ;				
			}
		}
	}

	public void Initialize(String CSVPath, String Language, int id, int PlayerJob)
	{
		String[][] Input = Utg.ReadTextFile(CSVPath + "Quests.csv", Quests.NumberOfQuests) ;
		Name = String.valueOf("Quest " + id) ;
		Type = Input[id][1] ;
		ReqCreatures = new int[] {Integer.parseInt(Input[id][2]), Integer.parseInt(Input[id][4]), Integer.parseInt(Input[id][6])} ;
		ReqCreaturesAmounts = new int[] {Integer.parseInt(Input[id][3]), Integer.parseInt(Input[id][5]), Integer.parseInt(Input[id][7])} ;
		ReqItems = new int[] {Integer.parseInt(Input[id][8]), Integer.parseInt(Input[id][10]), Integer.parseInt(Input[id][12]), Integer.parseInt(Input[id][14]), Integer.parseInt(Input[id][16])} ;
		ReqItemsAmounts = new int[] {Integer.parseInt(Input[id][9]), Integer.parseInt(Input[id][11]), Integer.parseInt(Input[id][13]), Integer.parseInt(Input[id][15]), Integer.parseInt(Input[id][17])} ;
		Counter = new int[ReqCreatures.length] ;
		GoldReward = Integer.parseInt(Input[id][18]) ;
		ExpReward = Integer.parseInt(Input[id][19]) ;
		ItemsReward = new int[] {Integer.parseInt(Input[id][20]), Integer.parseInt(Input[id][22]), Integer.parseInt(Input[id][24]), Integer.parseInt(Input[id][26]), Integer.parseInt(Input[id][28])} ;
		ItemsRewardAmounts = new int[] {Integer.parseInt(Input[id][21]), Integer.parseInt(Input[id][23]), Integer.parseInt(Input[id][25]), Integer.parseInt(Input[id][27]), Integer.parseInt(Input[id][29])} ;
		for (int item = 0 ; item <= ItemsReward.length - 1 ; item += 1)
		{
			if (Items.BagIDs[5] <= ItemsReward[item] & ItemsReward[item] < Items.BagIDs[6])
			{
				ItemsReward[item] += (Items.BagIDs[6] - Items.BagIDs[5]) / 5 * PlayerJob ;
			}
		}
		if (Language.equals("P"))
		{
			Description = Input[id][30] ;
		}
		else if (Language.equals("E"))
		{
			Description = Input[id][31] ;
		}
	}
}