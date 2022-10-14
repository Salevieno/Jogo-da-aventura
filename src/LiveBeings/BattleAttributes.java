package LiveBeings ;

import java.util.Arrays;

import Main.Utg;

public class BattleAttributes
{
	private float[] PhyAtk ;		// 0: Basic atk, 1: bonus, 2: train
	private float[] MagAtk ;		// 0: Basic atk, 1: bonus, 2: train
	private float[] PhyDef ;		// 0: Basic def, 1: bonus, 2: train
	private float[] MagDef ;		// 0: Basic def, 1: bonus, 2: train
	private float[] Dex ;		// 0: Basic dex, 1: bonus, 2: train
	private float[] Agi ;		// 0: Basic agi, 1: bonus, 2: train
	private float[] Crit ;		// 0: Basic crit atk chance, 1: bonus, 2: basic crit def chance, 3: bonus
	private float[] Stun ;		// 0: Basic atk chance, 1: bonus, 2: basic def chance, 3: bonus, 4: duration
	private float[] Block ;		// 0: Basic atk chance, 1: bonus, 2: basic def chance, 3: bonus, 4: duration
	private float[] Blood ;		// 0: Basic atk chance, 1: bonus, 2: basic def chance, 3: bonus, 4: basic atk, 5: bonus, 6: basic def, 7: bonus, 8: duration
	private float[] Poison ;		// 0: Basic atk chance, 1: bonus, 2: basic def chance, 3: bonus, 4: basic atk, 5: bonus, 6: basic def, 7: bonus, 8: duration
	private float[] Silence ;	// 0: Basic atk chance, 1: bonus, 2: basic def chance, 3: bonus, 4: duration
	private int[] Status ; 		// 0: Life, 1: Mp, 2: Phy atk, 3: Phy def, 4: Mag atk, 5: Mag def, 6: Dex, 7: Agi
	private int[] SpecialStatus ; 	// 0: Stun, 1: Block, 2: Blood, 3: Poison, 4: Silence
	private int[][] BattleActions ;	// [Atk][Counter, delay, permission]
	private String CurrentAction ;	// Current battle action (atk, def, skill)
	
	public BattleAttributes(float[] PhyAtk, float[] MagAtk, float[] PhyDef, float[] MagDef, float[] Dex, float[] Agi, float[] Crit, float[] Stun, float[] Block, float[] Blood, float[] Poison, float[] Silence, int[] Status, int[] SpecialStatus, int[][] BattleActions)
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
		this.BattleActions = BattleActions ;
		CurrentAction = "" ;
	}

	public float[] getPhyAtk() {return PhyAtk ;}
	public float[] getMagAtk() {return MagAtk ;}
	public float[] getPhyDef() {return PhyDef ;}
	public float[] getMagDef() {return MagDef ;}
	public float[] getDex() {return Dex ;}
	public float[] getAgi() {return Agi ;}
	public float[] getCrit() {return Crit ;}
	public float[] getStun() {return Stun ;}
	public float[] getBlock() {return Block ;}
	public float[] getBlood() {return Blood ;}
	public float[] getPoison() {return Poison ;}
	public float[] getSilence() {return Silence ;}
	public int[] getStatus() {return Status ;}
	public int[] getSpecialStatus() {return SpecialStatus ;}
	public int[][] getBattleActions() {return BattleActions ;}
	public String getCurrentAction() {return CurrentAction ;}
	public void setPhyAtk(float[] PA) {PhyAtk = PA ;}
	public void setMagAtk(float[] MA) {MagAtk = MA ;}
	public void setPhyDef(float[] PD) {PhyDef = PD ;}
	public void setMagDef(float[] MD) {MagDef = MD ;}
	public void setDex(float[] D) {Dex = D ;}
	public void setAgi(float[] A) {Agi = A ;}
	public void setCrit(float[] C) {Crit = C ;}
	public void setStun(float[] S) {Stun = S ;}
	public void setBlock(float[] B) {Block = B ;}
	public void setBlood(float[] B) {Blood = B ;}
	public void setPoison(float[] P) {Poison = P ;}
	public void setSilence(float[] S) {Silence = S ;}
	public void setStatus(int[] S) {Status = S ;}
	public void setSpecialStatus(int[] S) {SpecialStatus = S ;}
	public void setBattleActions(int[][] A) {BattleActions = A ;}
	public void setCurrentAction(String A) {CurrentAction = A ;}
	
	public float TotalPhyAtk()
	{
		return getPhyAtk()[0] + getPhyAtk()[1] + getPhyAtk()[2] ;
	}
	public float TotalMagAtk()
	{
		return getMagAtk()[0] + getMagAtk()[1] + getMagAtk()[2] ;
	}
	public float TotalPhyDef()
	{
		return getPhyDef()[0] + getPhyDef()[1] + getPhyDef()[2] ;
	}
	public float TotalMagDef()
	{
		return getMagDef()[0] + getMagDef()[1] + getMagDef()[2] ;
	}
	public float TotalDex()
	{
		return getDex()[0] + getDex()[1] + getDex()[2] ;
	}
	public float TotalAgi()
	{
		return getAgi()[0] + getAgi()[1] + getAgi()[2] ;
	}
	public float TotalCritAtkChance()
	{
		return getCrit()[0] + getCrit()[1] ;
	}
	public float TotalCritDefChance()
	{
		return getCrit()[2] + getCrit()[3] ;
	}
	public float TotalStunAtkChance()
	{
		return getStun()[0] + getStun()[1] ;
	}
	public float TotalStunDefChance()
	{
		return getStun()[2] + getStun()[3] ;
	}
	public float StunDuration()
	{
		return getStun()[4] ;
	}
	public float TotalBlockAtkChance()
	{
		return getBlock()[0] + getBlock()[1] ;
	}
	public float TotalBlockDefChance()
	{
		return getBlock()[2] + getBlock()[3] ;
	}
	public float BlockDuration()
	{
		return getBlock()[4] ;
	}
	public float TotalBloodAtkChance()
	{
		return getBlood()[0] + getBlood()[1] ;
	}
	public float TotalBloodDefChance()
	{
		return getBlood()[2] + getBlood()[3] ;
	}
	public float TotalBloodAtk()
	{
		return getBlood()[4] + getBlood()[5] ;
	}
	public float TotalBloodDef()
	{
		return getBlood()[6] + getBlood()[7] ;
	}
	public float BloodDuration()
	{
		return getBlood()[8] ;
	}
	public float TotalPoisonAtkChance()
	{
		return getPoison()[0] + getPoison()[1] ;
	}
	public float TotalPoisonDefChance()
	{
		return getPoison()[2] + getPoison()[3] ;
	}
	public float TotalPoisonAtk()
	{
		return getPoison()[4] + getPoison()[5] ;
	}
	public float TotalPoisonDef()
	{
		return getPoison()[6] + getPoison()[7] ;
	}
	public float PoisonDuration()
	{
		return getPoison()[8] ;
	}
	public float TotalSilenceAtkChance()
	{
		return getSilence()[0] + getSilence()[1] ;
	}
	public float TotalSilenceDefChance()
	{
		return getSilence()[2] + getSilence()[3] ;
	}
	public float SilenceDuration()
	{
		return getSilence()[4] ;
	}

	
	public boolean isStun()
	{
		return 0 < SpecialStatus[0] ;
	}
	public boolean actionisskill()
	{
		return Utg.isNumeric(CurrentAction) ;
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
		for (int s = 0 ; s <= SpecialStatus.length - 1 ; s += 1)
		{
			if (-1 < SpecialStatus[s])
			{
				SpecialStatus[s] += -1 ;		
			}
		}
	}

	
	/* Print methods */
	public void printAtt()
	{
		System.out.println("Phy atk: " + Arrays.toString(PhyAtk));
		System.out.println("Mag atk: " + Arrays.toString(MagAtk));
		System.out.println("Phy def: " + Arrays.toString(PhyDef));
		System.out.println("Mag def: " + Arrays.toString(MagDef));
		System.out.println("Dex: " + Arrays.toString(Dex));
		System.out.println("Agi: " + Arrays.toString(Agi));
		System.out.println("Crit: " + Arrays.toString(Crit));
		System.out.println("Stun: " + Arrays.toString(Stun));
		System.out.println("Block: " + Arrays.toString(Block));
		System.out.println("Blood: " + Arrays.toString(Blood));
		System.out.println("Poison: " + Arrays.toString(Poison));
		System.out.println("Silence: " + Arrays.toString(Silence));
		System.out.println("Status: " + Arrays.toString(Status));
		System.out.println("SpecialStatus: " + Arrays.toString(SpecialStatus));
		System.out.println("BattleActions: " + Arrays.deepToString(BattleActions));
		System.out.println("CurrentAction: " + CurrentAction);
	}
}