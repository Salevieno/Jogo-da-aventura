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
	
	public double[] all()
	{
		return new double[] {life, MP, phyAtk, magAtk, phyDef, magDef, dex, agi,
		critAtkChance, critDefChance ,
		stunAtkChance, stunDefChance, stunDuration, 
		blockAtkChance, blockDefChance, blockDuration, 
		bloodAtkChance, bloodDefChance, bloodAtk, bloodDef, bloodDuration, 
		poisonAtkChance, poisonDefChance, poisonAtk, poisonDef, poisonDuration, 
		silenceAtkChance, silenceDefChance, silenceDuration} ;
	}
	
	public void resetAll()
	{
		 life = 0 ;
		 MP = 0 ;
		 phyAtk = 0 ;
		 magAtk = 0 ;
		 phyDef = 0 ;
		 magDef = 0 ;
		 dex = 0 ;
		 agi = 0 ;
		 critAtkChance = 0 ;
		 critDefChance = 0 ;
		 stunAtkChance = 0 ;
		 stunDefChance = 0 ;
		 stunDuration = 0 ;
		 blockAtkChance = 0 ;
		 blockDefChance = 0 ;
		 blockDuration = 0 ;
		 bloodAtkChance = 0 ;
		 bloodDefChance = 0 ;
		 bloodAtk = 0 ;
		 bloodDef = 0 ;
		 bloodDuration = 0 ;
		 poisonAtkChance = 0 ;
		 poisonDefChance = 0 ;
		 poisonAtk = 0 ;
		 poisonDef = 0 ;
		 poisonDuration = 0 ;
		 silenceAtkChance = 0 ;
		 silenceDefChance = 0 ;
		 silenceDuration = 0 ;
	}
	
	public void inc(double[] amount)
	{
		  life += amount[0] ;
		  MP += amount[1] ;
		  phyAtk += amount[2] ;
		  magAtk += amount[3] ;
		  phyDef += amount[4] ;
		  magDef += amount[5] ;
		  dex += amount[6] ;
		  agi += amount[7] ;
		  critAtkChance += amount[8] ;
		  critDefChance += amount[9] ;
		  stunAtkChance += amount[10] ;
		  stunDefChance += amount[11] ;
		  stunDuration += amount[12] ;
		  blockAtkChance += amount[13] ;
		  blockDefChance += amount[14] ;
		  blockDuration += amount[15] ;
		  bloodAtkChance += amount[16] ;
		  bloodDefChance += amount[17] ;
		  bloodAtk += amount[18] ;
		  bloodDef += amount[19] ;
		  bloodDuration += amount[20] ;
		  poisonAtkChance += amount[21] ;
		  poisonDefChance += amount[22] ;
		  poisonAtk += amount[23] ;
		  poisonDef += amount[24] ;
		  poisonDuration += amount[25] ;
		  silenceAtkChance += amount[26] ;
		  silenceDefChance += amount[27] ;
		  silenceDuration += amount[28] ;
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
