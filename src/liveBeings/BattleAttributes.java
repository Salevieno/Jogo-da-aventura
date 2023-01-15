package liveBeings ;

import java.util.Arrays;

public class BattleAttributes
{
	private BasicBattleAttribute PhyAtk ;	// 0: Base atk, 1: bonus, 2: train
	private BasicBattleAttribute MagAtk ;	// 0: Base atk, 1: bonus, 2: train
	private BasicBattleAttribute PhyDef ;	// 0: Base def, 1: bonus, 2: train
	private BasicBattleAttribute MagDef ;	// 0: Base def, 1: bonus, 2: train
	private BasicBattleAttribute Dex ;		// 0: Base dex, 1: bonus, 2: train
	private BasicBattleAttribute Agi ;		// 0: Base agi, 1: bonus, 2: train
	
	private double[] Crit ;		// 0: Base crit atk chance, 1: bonus, 2: basic crit def chance, 3: bonus
	
	private double[] Stun ;		// 0: Basic atk chance, 1: bonus, 2: basic def chance, 3: bonus, 4: duration
	private double[] Block ;	// 0: Basic atk chance, 1: bonus, 2: basic def chance, 3: bonus, 4: duration
	private double[] Blood ;	// 0: Basic atk chance, 1: bonus, 2: basic def chance, 3: bonus, 4: basic atk, 5: bonus, 6: basic def, 7: bonus, 8: duration
	private double[] Poison ;	// 0: Basic atk chance, 1: bonus, 2: basic def chance, 3: bonus, 4: basic atk, 5: bonus, 6: basic def, 7: bonus, 8: duration
	private double[] Silence ;	// 0: Basic atk chance, 1: bonus, 2: basic def chance, 3: bonus, 4: duration
	
	// TODO status viram mapa
	private int[] Status ; 		// 0: Life, 1: Mp, 2: Phy atk, 3: Phy def, 4: Mag atk, 5: Mag def, 6: Dex, 7: Agi
	private int[] SpecialStatus ; 	// 0: Stun, 1: Block, 2: Blood, 3: Poison, 4: Silence
	
	public BattleAttributes(BasicBattleAttribute PhyAtk, BasicBattleAttribute MagAtk, BasicBattleAttribute PhyDef, BasicBattleAttribute MagDef, BasicBattleAttribute Dex, BasicBattleAttribute Agi,
			double[] Crit,
			double[] Stun, double[] Block, double[] Blood, double[] Poison, double[] Silence,
			int[] Status, int[] SpecialStatus)
	{
		this.PhyAtk = PhyAtk ;
		this.MagAtk = MagAtk ;
		this.PhyDef = PhyDef ;
		this.MagDef = MagDef ;
		this.Dex = Dex ;
		this.Agi = Agi ;
		this.Crit = Crit ;
		this.Stun = Stun ;
		this.Block = Block ;
		this.Blood = Blood ;
		this.Poison = Poison ;
		this.Silence = Silence ;
		this.Status = Status ;
		this.SpecialStatus = SpecialStatus ;
	}

	public BasicBattleAttribute getPhyAtk() {return PhyAtk ;}
	public BasicBattleAttribute getMagAtk() {return MagAtk ;}
	public BasicBattleAttribute getPhyDef() {return PhyDef ;}
	public BasicBattleAttribute getMagDef() {return MagDef ;}
	public BasicBattleAttribute getDex() {return Dex ;}
	public BasicBattleAttribute getAgi() {return Agi ;}
	public double[] getCrit() {return Crit ;}
	public double[] getStun() {return Stun ;}
	public double[] getBlock() {return Block ;}
	public double[] getBlood() {return Blood ;}
	public double[] getPoison() {return Poison ;}
	public double[] getSilence() {return Silence ;}
	public int[] getStatus() {return Status ;}
	public int[] getSpecialStatus() {return SpecialStatus ;}
	
	public double TotalPhyAtk()
	{
		return PhyAtk.getTotal() ;
	}
	public double TotalMagAtk()
	{
		return MagAtk.getTotal() ;
	}
	public double TotalPhyDef()
	{
		return PhyDef.getTotal() ;
	}
	public double TotalMagDef()
	{
		return MagDef.getTotal() ;
	}
	public double TotalDex()
	{
		return Dex.getTotal() ;
	}
	public double TotalAgi()
	{
		return Agi.getTotal() ;
	}
	public double TotalCritAtkChance()
	{
		return getCrit()[0] + getCrit()[1] ;
	}
	public double TotalCritDefChance()
	{
		return getCrit()[2] + getCrit()[3] ;
	}
	public double TotalStunAtkChance()
	{
		return getStun()[0] + getStun()[1] ;
	}
	public double TotalStunDefChance()
	{
		return getStun()[2] + getStun()[3] ;
	}
	public double StunDuration()
	{
		return getStun()[4] ;
	}
	public double TotalBlockAtkChance()
	{
		return getBlock()[0] + getBlock()[1] ;
	}
	public double TotalBlockDefChance()
	{
		return getBlock()[2] + getBlock()[3] ;
	}
	public double BlockDuration()
	{
		return getBlock()[4] ;
	}
	public double TotalBloodAtkChance()
	{
		return getBlood()[0] + getBlood()[1] ;
	}
	public double TotalBloodDefChance()
	{
		return getBlood()[2] + getBlood()[3] ;
	}
	public double TotalBloodAtk()
	{
		return getBlood()[4] + getBlood()[5] ;
	}
	public double TotalBloodDef()
	{
		return getBlood()[6] + getBlood()[7] ;
	}
	public double BloodDuration()
	{
		return getBlood()[8] ;
	}
	public double TotalPoisonAtkChance()
	{
		return getPoison()[0] + getPoison()[1] ;
	}
	public double TotalPoisonDefChance()
	{
		return getPoison()[2] + getPoison()[3] ;
	}
	public double TotalPoisonAtk()
	{
		return getPoison()[4] + getPoison()[5] ;
	}
	public double TotalPoisonDef()
	{
		return getPoison()[6] + getPoison()[7] ;
	}
	public double PoisonDuration()
	{
		return getPoison()[8] ;
	}
	public double TotalSilenceAtkChance()
	{
		return getSilence()[0] + getSilence()[1] ;
	}
	public double TotalSilenceDefChance()
	{
		return getSilence()[2] + getSilence()[3] ;
	}
	public double SilenceDuration()
	{
		return getSilence()[4] ;
	}

	
	public boolean isStun()
	{
		return 0 < SpecialStatus[0] ;
	}
	
	public double[] getBaseValues()
	{
		return new double[] {PhyAtk.getBaseValue(), MagAtk.getBaseValue(), PhyDef.getBaseValue(), MagDef.getBaseValue(), Dex.getBaseValue(), Agi.getBaseValue(),
				Crit[0], Stun[0], Block[0], Blood[0], Blood[2], Blood[4], Blood[6], Poison[0], Poison[2], Poison[4],Poison[6], Silence[0]} ;
	}
	
	public void receiveStatus(int[] AppliedStatus)
	{
		SpecialStatus[0] = Math.max(SpecialStatus[0], AppliedStatus[0]) ;
		SpecialStatus[1] = Math.max(SpecialStatus[1], AppliedStatus[1]) ;
		SpecialStatus[2] = Math.max(SpecialStatus[2], AppliedStatus[2]) ;
		SpecialStatus[3] = Math.max(SpecialStatus[3], AppliedStatus[3]) ;
		SpecialStatus[4] = Math.max(SpecialStatus[4], AppliedStatus[4]) ;
	}
	public void decreaseStatus()
	{
		for (int status : SpecialStatus)
		{
			if (-1 < status)
			{
				status += -1 ;		
			}
		}
	}

	@Override
	public String toString()
	{
		return "BattleAttributes [PhyAtk=" + PhyAtk + ", MagAtk=" + MagAtk + ", PhyDef=" + PhyDef + ", MagDef=" + MagDef
				+ ", Dex=" + Dex + ", Agi=" + Agi + ", Crit=" + Arrays.toString(Crit) + ", Stun="
				+ Arrays.toString(Stun) + ", Block=" + Arrays.toString(Block) + ", Blood=" + Arrays.toString(Blood)
				+ ", Poison=" + Arrays.toString(Poison) + ", Silence=" + Arrays.toString(Silence) + ", Status="
				+ Arrays.toString(Status) + ", SpecialStatus=" + Arrays.toString(SpecialStatus) + "]";
	}
	
	
}