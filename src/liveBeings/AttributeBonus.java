package liveBeings;

public class AttributeBonus
{
	private int life ;
	private int MP ;
	private int phyAtk ;
	private int magAtk ;
	private int phyDef ;
	private int magDef ;
	private int dex ;
	private int agi ;
	private float critAtkChance ;
	private float critDefChance ;
	private float stunAtkChance ;
	private float stunDefChance ;
	private int stunDuration ;
	private float blockAtkChance ;
	private float blockDefChance ;
	private int blockDuration ;
	private float bloodAtkChance ;
	private float bloodDefChance ;
	private int bloodAtk ;
	private int bloodDef ;
	private int bloodDuration ;
	private float poisonAtkChance ;
	private float poisonDefChance ;
	private int poisonAtk ;
	private int poisonDef ;
	private int poisonDuration ;
	private float silenceAtkChance ;
	private float silenceDefChance ;
	private int silenceDuration ;
	
	public AttributeBonus(int life, int MP,
			int phyAtk, int magAtk, int phyDef,	int magDef,
			int dex, int agi,
			float critAtkChance, float critDefChance,
			float stunAtkChance, float stunDefChance, int stunDuration,
			float blockAtkChance, float blockDefChance,	int blockDuration,
			float bloodAtkChance, float bloodDefChance, int bloodAtk, int bloodDef, int bloodDuration,
			float poisonAtkChance, float poisonDefChance, int poisonAtk, int poisonDef,	int poisonDuration,
			float silenceAtkChance,	float silenceDefChance,	int silenceDuration)
	{
		this.life = life ;
		this.MP = MP ;
		this.phyAtk = phyAtk ;
		this.magAtk = magAtk ;
		this.phyDef = phyDef ;
		this.magDef = magDef ;
		this.dex = dex ;
		this.agi = agi ;
		this.critAtkChance = critAtkChance ;
		this.critDefChance = critDefChance ;
		this.stunAtkChance = stunAtkChance ;
		this.stunDefChance = stunDefChance ;
		this.stunDuration = stunDuration ;
		this.blockAtkChance = blockAtkChance ;
		this.blockDefChance = blockDefChance ;
		this.blockDuration = blockDuration ;
		this.bloodAtkChance = bloodAtkChance ;
		this.bloodDefChance = bloodDefChance ;
		this.bloodAtk = bloodAtk ;
		this.bloodDef = bloodDef ;
		this.bloodDuration = bloodDuration ;
		this.poisonAtkChance = poisonAtkChance ;
		this.poisonDefChance = poisonDefChance ;
		this.poisonAtk = poisonAtk ;
		this.poisonDef = poisonDef ;
		this.poisonDuration = poisonDuration ;
		this.silenceAtkChance = silenceAtkChance ;
		this.silenceDefChance = silenceDefChance ;
		this.silenceDuration = silenceDuration ;
	}	

	public int getLife() {return life ;}
	public int getMP() {return MP ;}
	public int getPhyAtk() {return phyAtk ;}
	public int getMagAtk() {return magAtk ;}
	public int getPhyDef() {return phyDef ;}
	public int getMagDef() {return magDef ;}
	public int getDex() {return dex ;}
	public int getAgi() {return agi ;}
	public float getCritAtkChance() {return critAtkChance ;}
	public float getCritDefChance() {return critDefChance ;}
	public float getStunAtkChance() {return stunAtkChance ;}
	public float getStunDefChance() {return stunDefChance ;}
	public int getStunDuration() {return stunDuration ;}
	public float getBlockAtkChance() {return blockAtkChance ;}
	public float getBlockDefChance() {return blockDefChance ;}
	public int getBlockDuration() {return blockDuration ;}
	public float getBloodAtkChance() {return bloodAtkChance ;}
	public float getBloodDefChance() {return bloodDefChance ;}
	public int getBloodAtk() {return bloodAtk ;}
	public int getBloodDef() {return bloodDef ;}
	public int getBloodDuration() {return bloodDuration ;}
	public float getPoisonAtkChance() {return poisonAtkChance ;}
	public float getPoisonDefChance() {return poisonDefChance ;}
	public int getPoisonAtk() {return poisonAtk ;}
	public int getPoisonDef() {return poisonDef ;}
	public int getPoisonDuration() {return poisonDuration ;}
	public float getSilenceAtkChance() {return silenceAtkChance ;}
	public float getSilenceDefChance() {return silenceDefChance ;}
	public int getSilenceDuration() {return silenceDuration ;}

	public void printAtt()
	{
		System.out.println("   life bonus: " + life +
				"   mp bonus: " + MP +
				"   phy atk bonus: " + phyAtk +
				"   mag atk bonus: " + magAtk +
				"   phy def bonus: " + phyDef +
				"   mag def bonus: " + magDef +
				"   dex bonus: " + dex +
				"   agi bonus: " + agi +
				"   crit atk chance bonus: " + critAtkChance +
				"   crit def chance bonus: " + critDefChance +
				"   stun atk chance bonus: " + stunAtkChance +
				"   stun def chance bonus: " + stunDefChance +
				"   stun duration bonus: " + stunDuration +
				"   block atk chance bonus: " + blockAtkChance +
				"   block def chance bonus: " + blockDefChance +
				"   block duration bonus: " + blockDuration +
				"   blood atk chance bonus: " + bloodAtkChance +
				"   blood def chance bonus: " + bloodDefChance +
				"   blood atk bonus: " + bloodAtk +
				"   blood def bonus: " + bloodDef +
				"   blood duration bonus: " + bloodDuration +
				"   poison atk chance bonus: " + poisonAtkChance +
				"   poison def chance bonus: " + poisonDefChance +
				"   poison atk bonus: " + poisonAtk +
				"   poison def bonus: " + poisonDef +
				"   poison duration bonus: " + poisonDuration +
				"   silence atk chance bonus: " + silenceAtkChance +
				"   silence def chance bonus: " + silenceDefChance +
				"   silence duration bonus: " + silenceDuration);
	}
	
}
