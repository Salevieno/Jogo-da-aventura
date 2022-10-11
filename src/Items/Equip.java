package Items;

import java.io.IOException;

import Main.Game;
import Main.Utg;

public class Equip extends Item
{
	private int id ;
	private int forgeLevel ;
	private int lifeBonus ;
	private int MPBonus ;
	private int phyAtkBonus ;
	private int magAtkBonus ;
	private int phyDefBonus ;
	private int magDefBonus ;
	private int dexBonus ;
	private int agiBonus ;
	private float critAtkChanceBonus ;
	private float critDefChanceBonus ;
	private float stunAtkChanceBonus ;
	private float stunDefChanceBonus ;
	private int stunDurationBonus ;
	private float blockAtkChanceBonus ;
	private float blockDefChanceBonus ;
	private int blockDurationBonus ;
	private float bloodAtkChanceBonus ;
	private float bloodDefChanceBonus ;
	private int bloodAtkBonus ;
	private int bloodDefBonus ;
	private int bloodDurationBonus ;
	private float poisonAtkChanceBonus ;
	private float poisonDefChanceBonus ;
	private int poisonAtkBonus ;
	private int poisonDefBonus ;
	private int poisonDurationBonus ;
	private float silenceAtkChanceBonus ;
	private float silenceDefChanceBonus ;
	private int silenceDurationBonus ;
	private String elem ;
	
	private static Equip[] AllEquip ;
	public Equip(int id, String Name, String Description, int price, float dropChance, 
			int forgeLevel,
			int lifeBonus,
			int MPBonus,
			int phyAtkBonus,
			int magAtkBonus,
			int phyDefBonus,
			int magDefBonus,
			int dexBonus,
			int agiBonus,
			float critAtkChanceBonus,
			float critDefChanceBonus,
			float stunAtkChanceBonus,
			float stunDefChanceBonus,
			int stunDurationBonus,
			float blockAtkChanceBonus,
			float blockDefChanceBonus,
			int blockDurationBonus,
			float bloodAtkChanceBonus,
			float bloodDefChanceBonus,
			int bloodAtkBonus,
			int bloodDefBonus,
			int bloodDurationBonus,
			float poisonAtkChanceBonus,
			float poisonDefChanceBonus,
			int poisonAtkBonus,
			int poisonDefBonus,
			int poisonDurationBonus,
			float silenceAtkChanceBonus,
			float silenceDefChanceBonus,
			int silenceDurationBonus, String elem)
	{
		super(Name, Description, price, dropChance) ;
		this.id = id ;
		this.forgeLevel = forgeLevel ;
		this.lifeBonus = lifeBonus ;
		this.MPBonus = MPBonus ;
		this.phyAtkBonus = phyAtkBonus ;
		this.magAtkBonus = magAtkBonus ;
		this.phyDefBonus = phyDefBonus ;
		this.magDefBonus = magDefBonus ;
		this.dexBonus = dexBonus ;
		this.agiBonus = agiBonus ;
		this.critAtkChanceBonus = critAtkChanceBonus ;
		this.critDefChanceBonus = critDefChanceBonus ;
		this.stunAtkChanceBonus = stunAtkChanceBonus ;
		this.stunDefChanceBonus = stunDefChanceBonus ;
		this.stunDurationBonus = stunDurationBonus ;
		this.blockAtkChanceBonus = blockAtkChanceBonus ;
		this.blockDefChanceBonus = blockDefChanceBonus ;
		this.blockDurationBonus = blockDurationBonus ;
		this.bloodAtkChanceBonus = bloodAtkChanceBonus ;
		this.bloodDefChanceBonus = bloodDefChanceBonus ;
		this.bloodAtkBonus = bloodAtkBonus ;
		this.bloodDefBonus = bloodDefBonus ;
		this.bloodDurationBonus = bloodDurationBonus ;
		this.poisonAtkChanceBonus = poisonAtkChanceBonus ;
		this.poisonDefChanceBonus = poisonDefChanceBonus ;
		this.poisonAtkBonus = poisonAtkBonus ;
		this.poisonDefBonus = poisonDefBonus ;
		this.poisonDurationBonus = poisonDurationBonus ;
		this.silenceAtkChanceBonus = silenceAtkChanceBonus ;
		this.silenceDefChanceBonus = silenceDefChanceBonus ;
		this.silenceDurationBonus = silenceDurationBonus ;
		this.elem = elem ;
	}

	public int getId() {return id ;}
	public int getForgeLevel() {return forgeLevel ;}
	public int getLifeBonus() {return lifeBonus ;}
	public int getMPBonus() {return MPBonus ;}
	public int getPhyAtkBonus() {return phyAtkBonus ;}
	public int getMagAtkBonus() {return magAtkBonus ;}
	public int getPhyDefBonus() {return phyDefBonus ;}
	public int getMagDefBonus() {return magDefBonus ;}
	public int getDexBonus() {return dexBonus ;}
	public int getAgiBonus() {return agiBonus ;}
	public float getCritAtkChanceBonus() {return critAtkChanceBonus ;}
	public float getCritDefChanceBonus() {return critDefChanceBonus ;}
	public float getStunAtkChanceBonus() {return stunAtkChanceBonus ;}
	public float getStunDefChanceBonus() {return stunDefChanceBonus ;}
	public int getStunDurationBonus() {return stunDurationBonus ;}
	public float getBlockAtkChanceBonus() {return blockAtkChanceBonus ;}
	public float getBlockDefChanceBonus() {return blockDefChanceBonus ;}
	public int getBlockDurationBonus() {return blockDurationBonus ;}
	public float getBloodAtkChanceBonus() {return bloodAtkChanceBonus ;}
	public float getBloodDefChanceBonus() {return bloodDefChanceBonus ;}
	public int getBloodAtkBonus() {return bloodAtkBonus ;}
	public int getBloodDefBonus() {return bloodDefBonus ;}
	public int getBloodDurationBonus() {return bloodDurationBonus ;}
	public float getPoisonAtkChanceBonus() {return poisonAtkChanceBonus ;}
	public float getPoisonDefChanceBonus() {return poisonDefChanceBonus ;}
	public int getPoisonAtkBonus() {return poisonAtkBonus ;}
	public int getPoisonDefBonus() {return poisonDefBonus ;}
	public int getPoisonDurationBonus() {return poisonDurationBonus ;}
	public float getSilenceAtkChanceBonus() {return silenceAtkChanceBonus ;}
	public float getSilenceDefChanceBonus() {return silenceDefChanceBonus ;}
	public int getSilenceDurationBonus() {return silenceDurationBonus ;}
	public String getElem() {return elem ;}
	public static Equip[] getAll() {return AllEquip ;}
	
	
	public static void Initialize() throws IOException
	{
		int NumEquip = Utg.count(Game.CSVPath + "Item_Equip.csv") ;
		String[][] Input = Utg.ReadTextFile(Game.CSVPath + "Item_Equip.csv", NumEquip) ;
		AllEquip = new Equip[NumEquip] ;
		for (int p = 0; p <= NumEquip - 1; p += 1)
		{
			AllEquip[p] = new Equip(Integer.parseInt(Input[p][0]), Input[p][1], Input[p][3], Integer.parseInt(Input[p][5]), Float.parseFloat(Input[p][6]),
					Integer.parseInt(Input[p][7]),																																	// forge level
					Integer.parseInt(Input[p][8]), Integer.parseInt(Input[p][9]),																									// life and mp
					Integer.parseInt(Input[p][10]),	Integer.parseInt(Input[p][11]), Integer.parseInt(Input[p][12]), Integer.parseInt(Input[p][13]),									// phyatk magatk phydef magdef
					Integer.parseInt(Input[p][14]), Integer.parseInt(Input[p][15]),																									// dex and agi
					Float.parseFloat(Input[p][16]), Float.parseFloat(Input[p][17]),																									// crit atk and def chance
					Float.parseFloat(Input[p][18]), Float.parseFloat(Input[p][19]), Integer.parseInt(Input[p][20]),																	// stun
					Float.parseFloat(Input[p][21]), Float.parseFloat(Input[p][22]), Integer.parseInt(Input[p][23]),																	// block
					Float.parseFloat(Input[p][24]), Float.parseFloat(Input[p][25]), Integer.parseInt(Input[p][26]), Integer.parseInt(Input[p][27]), Integer.parseInt(Input[p][28]),	// blood
					Float.parseFloat(Input[p][29]), Float.parseFloat(Input[p][30]), Integer.parseInt(Input[p][31]), Integer.parseInt(Input[p][32]), Integer.parseInt(Input[p][33]),	// poison
					Float.parseFloat(Input[p][34]), Float.parseFloat(Input[p][35]), Integer.parseInt(Input[p][36]),																	// silence
					Input[p][37]);																																					// elem
		}		
	}

	public void printAtt()
	{
		System.out.println("Equip id: " + AllEquip[id].getId() +
				"   name: " + AllEquip[id].getName() +
				"   description: " + AllEquip[id].getDescription() +
				"   price: " + AllEquip[id].getPrice() +
				"   drop chance: " + AllEquip[id].getDropChance() + "%" + 
				"   forge level: " + AllEquip[id].getForgeLevel() +
				"   life bonus: " + AllEquip[id].getLifeBonus() +
				"   mp bonus: " + AllEquip[id].getMPBonus() +
				"   phy atk bonus: " + AllEquip[id].getPhyAtkBonus() +
				"   mag atk bonus: " + AllEquip[id].getMagAtkBonus() +
				"   phy def bonus: " + AllEquip[id].getPhyDefBonus() +
				"   mag def bonus: " + AllEquip[id].getMagDefBonus() +
				"   dex bonus: " + AllEquip[id].getDexBonus() +
				"   agi bonus: " + AllEquip[id].getAgiBonus() +
				"   crit atk chance bonus: " + AllEquip[id].getCritAtkChanceBonus() +
				"   crit def chance bonus: " + AllEquip[id].getCritDefChanceBonus() +
				"   stun atk chance bonus: " + AllEquip[id].getStunAtkChanceBonus() +
				"   stun def chance bonus: " + AllEquip[id].getStunDefChanceBonus() +
				"   stun duration bonus: " + AllEquip[id].getStunDurationBonus() +
				"   block atk chance bonus: " + AllEquip[id].getBlockAtkChanceBonus() +
				"   block def chance bonus: " + AllEquip[id].getBlockDefChanceBonus() +
				"   block duration bonus: " + AllEquip[id].getBlockDurationBonus() +
				"   blood atk chance bonus: " + AllEquip[id].getBloodAtkChanceBonus() +
				"   blood def chance bonus: " + AllEquip[id].getBloodDefChanceBonus() +
				"   blood atk bonus: " + AllEquip[id].getBloodAtkBonus() +
				"   blood def bonus: " + AllEquip[id].getBloodDefBonus() +
				"   blood duration bonus: " + AllEquip[id].getBloodDurationBonus() +
				"   poison atk chance bonus: " + AllEquip[id].getPoisonAtkChanceBonus() +
				"   poison def chance bonus: " + AllEquip[id].getPoisonDefChanceBonus() +
				"   poison atk bonus: " + AllEquip[id].getPoisonAtkBonus() +
				"   poison def bonus: " + AllEquip[id].getPoisonDefBonus() +
				"   poison duration bonus: " + AllEquip[id].getPoisonDurationBonus() +
				"   silence atk chance bonus: " + AllEquip[id].getSilenceAtkChanceBonus() +
				"   silence def chance bonus: " + AllEquip[id].getSilenceDefChanceBonus() +
				"   silence duration bonus: " + AllEquip[id].getSilenceDurationBonus() +
				"   elem: " + AllEquip[id].getElem());
	}
	
}
