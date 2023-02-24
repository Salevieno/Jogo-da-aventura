package attributes;

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
	private double critAtkChance ;
	private double critDefChance ;
	private double stunAtkChance ;
	private double stunDefChance ;
	private int stunDuration ;
	private double blockAtkChance ;
	private double blockDefChance ;
	private int blockDuration ;
	private double bloodAtkChance ;
	private double bloodDefChance ;
	private int bloodAtk ;
	private int bloodDef ;
	private int bloodDuration ;
	private double poisonAtkChance ;
	private double poisonDefChance ;
	private int poisonAtk ;
	private int poisonDef ;
	private int poisonDuration ;
	private double silenceAtkChance ;
	private double silenceDefChance ;
	private int silenceDuration ;
	
	public AttributeBonus()
	{
		
	}
	
	public AttributeBonus(int life, int MP,
			int phyAtk, int magAtk, int phyDef,	int magDef,
			int dex, int agi,
			double critAtkChance, double critDefChance,
			double stunAtkChance, double stunDefChance, int stunDuration,
			double blockAtkChance, double blockDefChance,	int blockDuration,
			double bloodAtkChance, double bloodDefChance, int bloodAtk, int bloodDef, int bloodDuration,
			double poisonAtkChance, double poisonDefChance, int poisonAtk, int poisonDef,	int poisonDuration,
			double silenceAtkChance,	double silenceDefChance,	int silenceDuration)
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
	public double getCritAtkChance() {return critAtkChance ;}
	public double getCritDefChance() {return critDefChance ;}
	public double getStunAtkChance() {return stunAtkChance ;}
	public double getStunDefChance() {return stunDefChance ;}
	public int getStunDuration() {return stunDuration ;}
	public double getBlockAtkChance() {return blockAtkChance ;}
	public double getBlockDefChance() {return blockDefChance ;}
	public int getBlockDuration() {return blockDuration ;}
	public double getBloodAtkChance() {return bloodAtkChance ;}
	public double getBloodDefChance() {return bloodDefChance ;}
	public int getBloodAtk() {return bloodAtk ;}
	public int getBloodDef() {return bloodDef ;}
	public int getBloodDuration() {return bloodDuration ;}
	public double getPoisonAtkChance() {return poisonAtkChance ;}
	public double getPoisonDefChance() {return poisonDefChance ;}
	public int getPoisonAtk() {return poisonAtk ;}
	public int getPoisonDef() {return poisonDef ;}
	public int getPoisonDuration() {return poisonDuration ;}
	public double getSilenceAtkChance() {return silenceAtkChance ;}
	public double getSilenceDefChance() {return silenceDefChance ;}
	public int getSilenceDuration() {return silenceDuration ;}

	public void setBasic(double[] basicAttInc)
	{
		life = (int) basicAttInc[0] ;
		MP = (int) basicAttInc[1] ;
		phyAtk = (int) basicAttInc[2] ;
		magAtk = (int) basicAttInc[3] ;
		phyDef = (int) basicAttInc[4] ;
		magDef = (int) basicAttInc[5] ;
		dex = (int) basicAttInc[6] ;
		agi = (int) basicAttInc[7] ;
	}
	
	public double[] basic()
	{
		return new double[] {life, MP, phyAtk, magAtk, phyDef, magDef, dex, agi} ;
	}
	
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
