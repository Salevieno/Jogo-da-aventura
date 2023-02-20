package attributes ;

import liveBeings.LiveBeingStatus;

public class BattleAttributes
{
	private BasicBattleAttribute phyAtk ;
	private BasicBattleAttribute magAtk ;
	private BasicBattleAttribute phyDef ;
	private BasicBattleAttribute magDef ;
	private BasicBattleAttribute dex ;
	private BasicBattleAttribute agi ;
	
	private double[] crit ;		// 0: Base crit atk chance, 1: bonus, 2: basic crit def chance, 3: bonus
	
	private BattleSpecialAttribute stun ;
	private BattleSpecialAttribute block ;
	private BattleSpecialAttributeWithDamage blood ;
	private BattleSpecialAttributeWithDamage poison ;
	private BattleSpecialAttribute silence ;
	
	private LiveBeingStatus status ;
	
	public BattleAttributes(BasicBattleAttribute PhyAtk, BasicBattleAttribute MagAtk, BasicBattleAttribute PhyDef, BasicBattleAttribute MagDef, BasicBattleAttribute Dex, BasicBattleAttribute Agi,
			double[] Crit,
			BattleSpecialAttribute Stun, BattleSpecialAttribute Block, BattleSpecialAttributeWithDamage Blood, BattleSpecialAttributeWithDamage Poison, BattleSpecialAttribute Silence,
			LiveBeingStatus status)
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
		this.status = status ;
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
	public LiveBeingStatus getStatus() {return status ;}
	
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
		return 0 < status.getStun() ;
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
	public double[] baseAtkChances()
	{
		return new double[] {stun.TotalAtkChance(), block.TotalAtkChance(), blood.TotalAtkChance(), poison.TotalAtkChance(), silence.TotalAtkChance()} ;
	}
	public double[] baseDefChances()
	{
		return new double[] {stun.TotalDefChance(), block.TotalDefChance(), blood.TotalDefChance(), poison.TotalDefChance(), silence.TotalDefChance()} ;
	}
	public int[] baseDurations()
	{
		return new int[] {stun.getDuration(), block.getDuration(), blood.getDuration(), poison.getDuration(), silence.getDuration()} ;
	}

	
}