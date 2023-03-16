package attributes;

public class AttributeBonus
{
	private int life ;
	private int MP ;
	private double phyAtk ;
	private double magAtk ;
	private double phyDef ;
	private double magDef ;
	private double dex ;
	private double agi ;
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
	public double getPhyAtk() {return phyAtk ;}
	public double getMagAtk() {return magAtk ;}
	public double getPhyDef() {return phyDef ;}
	public double getMagDef() {return magDef ;}
	public double getDex() {return dex ;}
	public double getAgi() {return agi ;}
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
		phyAtk = (double) basicAttInc[2] ;
		magAtk = (double) basicAttInc[3] ;
		phyDef = (double) basicAttInc[4] ;
		magDef = (double) basicAttInc[5] ;
		dex = (double) basicAttInc[6] ;
		agi = (double) basicAttInc[7] ;
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

	@Override
	public String toString()
	{
		return "AttributeBonus [life=" + life + ", MP=" + MP + ", phyAtk=" + phyAtk + ", magAtk=" + magAtk + ", phyDef="
				+ phyDef + ", magDef=" + magDef + ", dex=" + dex + ", agi=" + agi + ", critAtkChance=" + critAtkChance
				+ ", critDefChance=" + critDefChance + ", stunAtkChance=" + stunAtkChance + ", stunDefChance="
				+ stunDefChance + ", stunDuration=" + stunDuration + ", blockAtkChance=" + blockAtkChance
				+ ", blockDefChance=" + blockDefChance + ", blockDuration=" + blockDuration + ", bloodAtkChance="
				+ bloodAtkChance + ", bloodDefChance=" + bloodDefChance + ", bloodAtk=" + bloodAtk + ", bloodDef="
				+ bloodDef + ", bloodDuration=" + bloodDuration + ", poisonAtkChance=" + poisonAtkChance
				+ ", poisonDefChance=" + poisonDefChance + ", poisonAtk=" + poisonAtk + ", poisonDef=" + poisonDef
				+ ", poisonDuration=" + poisonDuration + ", silenceAtkChance=" + silenceAtkChance
				+ ", silenceDefChance=" + silenceDefChance + ", silenceDuration=" + silenceDuration + "]";
	}


}
