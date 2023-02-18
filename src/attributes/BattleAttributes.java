package attributes ;


public class BattleAttributes
{
	private BasicBattleAttribute phyAtk ;
	private BasicBattleAttribute magAtk ;
	private BasicBattleAttribute phyDef ;
	private BasicBattleAttribute magDef ;
	private BasicBattleAttribute dex ;
	private BasicBattleAttribute agi ;
	
	private double[] crit ;		// 0: Base crit atk chance, 1: bonus, 2: basic crit def chance, 3: bonus
	
	private BattleSpecialAttribute stun ;		// 0: Basic atk chance, 1: bonus, 2: basic def chance, 3: bonus, 4: duration
	private BattleSpecialAttribute block ;	// 0: Basic atk chance, 1: bonus, 2: basic def chance, 3: bonus, 4: duration
	private BattleSpecialAttributeWithDamage blood ;	// 0: Basic atk chance, 1: bonus, 2: basic def chance, 3: bonus, 4: basic atk, 5: bonus, 6: basic def, 7: bonus, 8: duration
	private BattleSpecialAttributeWithDamage poison ;	// 0: Basic atk chance, 1: bonus, 2: basic def chance, 3: bonus, 4: basic atk, 5: bonus, 6: basic def, 7: bonus, 8: duration
	private BattleSpecialAttribute silence ;	// 0: Basic atk chance, 1: bonus, 2: basic def chance, 3: bonus, 4: duration
	
	// TODO status viram mapa
	private int[] status ; 		// 0: Life, 1: Mp, 2: Phy atk, 3: Phy def, 4: Mag atk, 5: Mag def, 6: Dex, 7: Agi
	private int[] specialStatus ; 	// 0: Stun, 1: Block, 2: Blood, 3: Poison, 4: Silence
	
	public BattleAttributes(BasicBattleAttribute PhyAtk, BasicBattleAttribute MagAtk, BasicBattleAttribute PhyDef, BasicBattleAttribute MagDef, BasicBattleAttribute Dex, BasicBattleAttribute Agi,
			double[] Crit,
			BattleSpecialAttribute Stun, BattleSpecialAttribute Block, BattleSpecialAttributeWithDamage Blood, BattleSpecialAttributeWithDamage Poison, BattleSpecialAttribute Silence,
			int[] Status, int[] SpecialStatus)
	{
		this.phyAtk = PhyAtk ;
		this.magAtk = MagAtk ;
		this.phyDef = PhyDef ;
		this.magDef = MagDef ;
		this.dex = Dex ;
		this.agi = Agi ;
		this.crit = Crit ;
		this.stun = Stun ;
		this.block = Block ;
		this.blood = Blood ;
		this.poison = Poison ;
		this.silence = Silence ;
		this.status = Status ;
		this.specialStatus = SpecialStatus ;
	}

	public BasicBattleAttribute getPhyAtk() {return phyAtk ;}
	public BasicBattleAttribute getMagAtk() {return magAtk ;}
	public BasicBattleAttribute getPhyDef() {return phyDef ;}
	public BasicBattleAttribute getMagDef() {return magDef ;}
	public BasicBattleAttribute getDex() {return dex ;}
	public BasicBattleAttribute getAgi() {return agi ;}
	public double[] getCrit() {return crit ;}
	public BattleSpecialAttribute getStun() {return stun ;}
	public BattleSpecialAttribute getBlock() {return block ;}
	public BattleSpecialAttributeWithDamage getBlood() {return blood ;}
	public BattleSpecialAttributeWithDamage getPoison() {return poison ;}
	public BattleSpecialAttribute getSilence() {return silence ;}
	public int[] getStatus() {return status ;}
	public int[] getSpecialStatus() {return specialStatus ;}
	
	public double TotalPhyAtk()
	{
		return phyAtk.getTotal() ;
	}
	public double TotalMagAtk()
	{
		return magAtk.getTotal() ;
	}
	public double TotalPhyDef()
	{
		return phyDef.getTotal() ;
	}
	public double TotalMagDef()
	{
		return magDef.getTotal() ;
	}
	public double TotalDex()
	{
		return dex.getTotal() ;
	}
	public double TotalAgi()
	{
		return agi.getTotal() ;
	}
	public double TotalCritAtkChance()
	{
		return getCrit()[0] + getCrit()[1] ;
	}
	public double TotalCritDefChance()
	{
		return getCrit()[2] + getCrit()[3] ;
	}

	
	public boolean isStun()
	{
		return 0 < specialStatus[0] ;
	}
	
	public double[] getBaseValues()
	{
		return new double[] {
				phyAtk.getBaseValue(), magAtk.getBaseValue(), phyDef.getBaseValue(), magDef.getBaseValue(),
				dex.getBaseValue(), agi.getBaseValue(),
				crit[0],
				stun.getBasicAtkChance(),
				block.getBasicAtkChance(),
				blood.getBasicAtkChance(), blood.getBasicDefChance(), blood.getBasicAtk(), blood.getBasicDef(),
				poison.getBasicAtkChance(), poison.getBasicDefChance(), poison.getBasicAtk(),poison.getBasicDef(),
				silence.getBasicAtkChance()
				} ;
	}
	
	public void receiveStatus(int[] AppliedStatus)
	{
		specialStatus[0] = Math.max(specialStatus[0], AppliedStatus[0]) ;
		specialStatus[1] = Math.max(specialStatus[1], AppliedStatus[1]) ;
		specialStatus[2] = Math.max(specialStatus[2], AppliedStatus[2]) ;
		specialStatus[3] = Math.max(specialStatus[3], AppliedStatus[3]) ;
		specialStatus[4] = Math.max(specialStatus[4], AppliedStatus[4]) ;
	}
	public void decreaseStatus()
	{
		for (int status : specialStatus)
		{
			if (-1 < status)
			{
				status += -1 ;		
			}
		}
	}

	
}