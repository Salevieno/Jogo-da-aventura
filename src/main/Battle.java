package main ;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays ;
import javax.sound.sampled.Clip ;

import components.Items;
import components.Quests;
import components.SpellTypes;
import graphics.Animations;
import graphics.DrawFunctions;
import graphics.DrawingOnPanel;
import liveBeings.BattleAttributes;
import liveBeings.Creature;
import liveBeings.LiveBeingStates;
import liveBeings.PersonalAttributes;
import liveBeings.Pet;
import liveBeings.Player;
import liveBeings.Spell;
import utilities.UtilG;
import utilities.UtilS;

public class Battle 
{
	//private int[] PlayerAtkResult, PetAtkResult, CreatureAtkResult ;
	private Clip[] SoundEffects ;
	private Animations Ani ;
	
	private int[][] ShowAtkCounters, ShowAtkDurations ;
	private boolean[][] Show ;
	private int CreatureTarget ;
	private double randomAmp ;
	private int[][] PlayerSkillBuffCounter, PlayerSkillNerfCounter, PetSkillBuffCounter, PetSkillNerfCounter, PlayerItemEffectCounter ;
	private boolean[][] SkillBuffIsActive, ItemEffectIsActive ;

	protected static String[] ElemID ;
	protected static double[][] ElemMult ;
	
	public Battle(int[] DamageDelay, Animations Ani)
	{	
		//this.SoundEffects = SoundEffects ;
		this.Ani = Ani ;

		int NumElem = 10 ;
    	ElemID = new String[NumElem] ;
		ElemMult = new double[NumElem][NumElem] ;
		ArrayList<String[]> ElemInput = UtilG.ReadcsvFile(Game.CSVPath + "Elem.csv") ;
		for (int i = 0 ; i <= NumElem - 1 ; ++i)
		{
			ElemID[i] = ElemInput.get(i)[0] ;
			for (int j = 0 ; j <= NumElem - 1 ; ++j)
			{
				ElemMult[i][j] = Double.parseDouble(ElemInput.get(i)[j + 1]) ;
			}				
		}
		randomAmp = (double)0.1 ;
		
		ShowAtkCounters = new int[3][4] ;
		ShowAtkDurations = new int[3][4] ;
		Show = new boolean[3][5] ;
		ShowAtkDurations[0][0] = DamageDelay[0] ;
		ShowAtkDurations[0][1] = DamageDelay[0] ;
		ShowAtkDurations[0][3] = DamageDelay[0] ;
		ShowAtkDurations[0][2] = DamageDelay[0] ;
		ShowAtkDurations[1][0] = DamageDelay[1] ;
		ShowAtkCounters[1][3] = 0 ;
		Show[1][3] = false ;
//		PlayerSkillBuffCounter = new int[skills.length][skills[0].getBuffs().length] ;
//		PlayerSkillNerfCounter = new int[skills.length][skills[0].getNerfs().length] ;
//		PetSkillBuffCounter = new int[petskills.length][skills[0].getBuffs().length] ;
//		PetSkillNerfCounter = new int[petskills.length][skills[0].getNerfs().length] ;
		//PlayerItemEffectCounter = new int[items.length][items[0].getBuffs().length] ;
		CreatureTarget = 0 ;
//		for (int i = 0 ; i <= skills.length - 1 ; ++i)
//		{
//			for (int j = 0 ; j <= skills[i].getBuffs().length - 1 ; ++j)
//			{
//				PlayerSkillBuffCounter[i][j] = 0 ;
//			}
//			for (int j = 0 ; j <= skills[i].getNerfs().length - 1 ; ++j)
//			{
//				PlayerSkillNerfCounter[i][j] = 0 ;
//			}
//		}
//		for (int i = 0 ; i <= petskills.length - 1 ; ++i)
//		{
//			for (int j = 0 ; j <= petskills[i].getBuffs().length - 1 ; ++j)
//			{
//				PetSkillBuffCounter[i][j] = 0 ;
//			}
//			for (int j = 0 ; j <= petskills[i].getNerfs().length - 1 ; ++j)
//			{
//				PetSkillNerfCounter[i][j] = 0 ;
//			}
//		}
		ShowAtkDurations[1][3] = DamageDelay[1] ;
		//SkillIsReady = new boolean[skills.length] ;
//		SkillBuffIsActive = new boolean[skills.length][skills[0].getBuffs().length] ;
		//ItemEffectIsActive = new boolean[Items.ItemsWithEffects.length][items[0].getBuffs().length] ;
	}

	// métodos de cálculo de batalha válidos para todos os participantes
	public static double BasicElemMult(String Atk, String Def)
	{
		return Battle.ElemMult[UtilG.IndexOf(Battle.ElemID, Atk)][UtilG.IndexOf(Battle.ElemID, Def)] ;
	}
	
	public static boolean Hit(double Dex, double Agi)
	{
		boolean hit = false ;
		
		if (Math.random() <= 1 - 1/(1 + Math.pow(1.1, Dex - Agi)))
		{
			hit = true ;
		}
		
		return hit ;
	}
	
	public static boolean Block(double BlockDef)
	{
		if (Math.random() < BlockDef)
		{
			return true ;
		}
		return false ;
	}

	public static boolean CriticalAtk(double CritAtk, double CritDef)
	{
		boolean Crit = false ;
		if (Math.random() + CritDef <= CritAtk)
		{
			Crit = true ;
		}
		return Crit ;
	}
		
	public static double CalcElemMult(String Atk, String Weapon, String Armor, String Shield, String SuperElem)
	{
		double mult = 1 ;
		mult = BasicElemMult(Atk, Armor)*mult ;
		mult = BasicElemMult(Atk, Shield)*mult ;
		mult = BasicElemMult(Weapon, Armor)*mult ;
		mult = BasicElemMult(Weapon, Shield)*mult ;
		mult = BasicElemMult(SuperElem, Armor)*mult ;
		mult = BasicElemMult(SuperElem, Shield)*mult ;
		return mult ;
	}
	
	public static String CalcEffect(double Dex, double Agi, double CritAtk, double CritDef, double BlockDef)
	{
		String effect = "" ;
		if (Block(BlockDef))
		{
			effect = "Block" ;
		} 
		else if (Hit(Dex, Agi))
		{
			effect = "Hit" ;
			if (CriticalAtk(CritAtk, CritDef))
			{
				effect = "Crit" ;
			}
		}
		else
		{
			effect = "Miss" ;
		}
		return effect;
	}
	
	public static int CalcAtk(String effect, double Atk, double Def, String[] AtkElem, String[] DefElem, double ElemModifier, double randomAmp)
	{
		int damage = -1 ;
		if (effect.equals("Miss") | effect.equals("Block"))
		{
			damage = 0 ;
		} 
		else if (effect.equals("Hit"))
		{
			damage = Math.max(0, (int)(Atk - Def)) ;
		}
		else if (effect.equals("Crit"))
		{
			double randomMult = UtilG.RandomMult(randomAmp) ;
			double elemMult = CalcElemMult(AtkElem[0], AtkElem[1], DefElem[0], DefElem[0], AtkElem[2]) ;
			damage = (int)(randomMult*elemMult*ElemModifier*Atk) ;
		}
		return damage ;
	}
	
	public static int[] CalcStatus(double[] Stun, double[] Block, double[] Blood, double[] Poison, double[] Silence)
	{
		// for each status 0: atk, 1: def, 2: duration
		int[] Status = new int[5] ;	// Stun, Block, Blood, Poison, Silence
		if (Math.random() <= Stun[0] - Stun[1])
		{
			Status[0] = (int)Stun[2] ;
		}
 		if (Math.random() <= Block[0] - Block[1])
 		{
 			Status[1] = (int)Block[2] ;
 		}
		if (Math.random() <= Blood[0] - Blood[1])
		{
			Status[2] = (int)Blood[2] ;
		}
		if (Math.random() <= Poison[0] - Poison[1])
		{
			Status[3] = (int)Poison[2] ;
		}
		if (Math.random() <= Silence[0] - Silence[1])
		{
			Status[4] = (int)Silence[2] ;
		}
		return Status ;
	}
	
	
	/* Attack and spell effects */
	public static Point knockback(Point SourcePos, int step, PersonalAttributes PA)
	{
		int angletan = (int)((PA.getPos().y - SourcePos.y) / (double)(PA.getPos().x - SourcePos.x)) ;
		int dirx = (int)Math.signum(PA.getPos().x - SourcePos.x) ;
		int diry = (int)Math.signum(PA.getPos().y - SourcePos.y) ;
		//PA.setPos(new int[] {PA.getPos()[0] + step * dirx, PA.getPos()[1] + step * diry * angletan}) ;
		return new Point(PA.getPos().x + step * dirx, PA.getPos().y + step * diry * angletan) ;
	}

	
	
	
	
	
	public void IncrementCounters()
	{	
		for (int i = 0 ; i <= ShowAtkCounters.length - 1 ; i += 1)
		{
			for (int j = 0 ; j <= ShowAtkCounters[i].length - 1 ; j += 1)
			{
				if (ShowAtkCounters[i][j] < ShowAtkDurations[i][j])
				{
					ShowAtkCounters[i][j] += 1 ;
				}
			}
		}
		/*for (int i = 0 ; i <= Items.ItemsWithEffects.length - 1 ; ++i)
		{
			int ItemID = Items.ItemsWithEffects[i] ;
			for (int j = 0 ; j <= items[ItemID].getBuffs().length - 1 ; ++j)
			{
				if (ItemEffectIsActive[i][j] & PlayerItemEffectCounter[i][j] < items[ItemID].getBuffs()[j][12])
				{
					++PlayerItemEffectCounter[i][j] ;
				}
			}
		}*/
	}	
	
	public void ActivateCounters(Player player, Pet pet, Creature creature)
	{
		for (int i = 0 ; i <= ShowAtkCounters.length - 1 ; i += 1)
		{
			for (int j = 0 ; j <= ShowAtkCounters[i].length - 1 ; j += 1)
			{
				if (ShowAtkCounters[0][j] == ShowAtkDurations[0][j])
				{
					Show[0][j] = false ;
				}
			}
		}
		if (player.getBA().getBattleActions()[0][0] == player.getBA().getBattleActions()[0][1] & player.isDefending())
		{
			player.DeactivateDef() ;
			UtilS.PrintBattleActions(6, "Player", "creature", 0, 0, player.getBA().getSpecialStatus(), creature.getElem()) ;
		}
		if (pet.getBA().getBattleActions()[0][0] == pet.getBA().getBattleActions()[0][1] & pet.isDefending())
		{
			UtilS.PrintBattleActions(6, "Pet", "creature", 0, 0, player.getBA().getSpecialStatus(), creature.getElem()) ;
 			pet.DeactivateDef() ;
		}
		if (creature.getBA().getBattleActions()[0][0] == creature.getBA().getBattleActions()[0][1] & creature.isDefending())
		{
			UtilS.PrintBattleActions(6, "Creature", "creature", 0, 0, player.getBA().getSpecialStatus(), creature.getElem()) ;
 			creature.DeactivateDef() ;
		}
		player.ActivateBattleActionCounters() ;
		pet.ActivateBattleActionCounters() ;
		creature.ActivateBattleActionCounters() ;
		/*for (int i = 0 ; i <= player.getSpell().length - 1 ; ++i)
		{
			SkillIsReady[i] = player.SkillIsReady(i, player.getSpell()) ;
		}*/
		/*for (int i = 0 ; i <= Items.ItemsWithEffects.length - 1 ; ++i)
		{
			int ItemID = Items.ItemsWithEffects[i] ;
			for (int j = 0 ; j <= items[ItemID].getBuffs().length - 1 ; ++j)
			{
				if (0 < items[ItemID].getBuffs()[j][12] & PlayerItemEffectCounter[i][j] == items[ItemID].getBuffs()[j][12])
				{
					BuffsAndNerfs(player, pet, creature, items[ItemID].getBuffs(), 1, j, false, Items.ItemsTargets[i], "deactivate") ;
					PlayerItemEffectCounter[i][j] = 0 ;
					ItemEffectIsActive[i][j] = false ;
				}
			}
		}*/
	}

	public void BuffsAndNerfs(Player player, Pet pet, Creature creature, double[][] Buffs, int BuffNerfLevel, int effect, boolean SkillIsActive, String Target, String action)
	{
		// TODO reprogramar buffs and nerfs
		int ActionMult = 1 ;
		double[][] Buff = new double[14][5] ;	// [Life, Mp, PhyAtk, MagAtk, Phy def, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence][effect]
		double[] OriginalValue = new double[14] ;	// [Life, Mp, PhyAtk, MagAtk, Phy def, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence]
		if (action.equals("deactivate"))
		{
			ActionMult = -1 ;
		}
		if (Target.equals("Player"))
		{
			OriginalValue = new double[] {player.getLife().getMaxValue(), player.getMp().getMaxValue(), 
					player.getBA().getPhyAtk().getBaseValue(),
					player.getBA().getMagAtk().getBaseValue(), player.getBA().getPhyDef().getBaseValue(), player.getBA().getMagDef().getBaseValue(),
					player.getBA().getDex().getBaseValue(), player.getBA().getAgi().getBaseValue(), player.getBA().getCrit()[0], player.getBA().getStun()[0],
					player.getBA().getBlock()[0], player.getBA().getBlood()[0], player.getBA().getBlood()[2], player.getBA().getBlood()[4],
					player.getBA().getBlood()[6], player.getBA().getPoison()[0], player.getBA().getPoison()[2], player.getBA().getPoison()[4],
					player.getBA().getPoison()[6], player.getBA().getSilence()[0]} ;
			double[] battleAttValues = player.getBA().getBaseValues() ;
		}
		if (Target.equals("Pet"))
		{
			OriginalValue = new double[] {pet.getLife().getMaxValue(), pet.getMp().getMaxValue(), pet.getPhyAtk().getBaseValue(), pet.getMagAtk().getBaseValue(),
					pet.getPhyDef().getBaseValue(), pet.getMagDef().getBaseValue(), pet.getDex().getBaseValue(), pet.getAgi().getBaseValue(),
					pet.getCrit()[0], pet.getStun()[0], pet.getBlock()[0], pet.getBlood()[0], pet.getBlood()[2], pet.getBlood()[4], pet.getBlood()[6],
					pet.getPoison()[0], pet.getPoison()[2], pet.getPoison()[4], pet.getPoison()[6], pet.getSilence()[0]} ;
			double[] battleAttValues = pet.getBA().getBaseValues() ;
		}
		if (Target.equals("Creature"))
		{
			OriginalValue = new double[] {creature.getLife().getMaxValue(), creature.getMp().getMaxValue(), creature.getPhyAtk().getBaseValue(),
					creature.getMagAtk().getBaseValue(), creature.getPhyDef().getBaseValue(), creature.getMagDef().getBaseValue(),
					creature.getDex().getBaseValue(), creature.getAgi().getBaseValue(), creature.getCrit()[0], creature.getStun()[0],
					creature.getBlock()[0], creature.getBlood()[0], creature.getBlood()[2], creature.getBlood()[4], creature.getBlood()[6],
					creature.getPoison()[0], creature.getPoison()[2], creature.getPoison()[4], creature.getPoison()[6], creature.getSilence()[0]} ;
			double[] battleAttValues = creature.getBA().getBaseValues() ;
		}
		if (effect == 11 | effect == 12)
		{
			if (action.equals("deactivate"))
			{
				Buff[effect][0] += (OriginalValue[effect]*Buffs[effect][0] + Buffs[effect][1])*BuffNerfLevel*ActionMult ;
				Buff[effect][1] += (OriginalValue[effect]*Buffs[effect][3] + Buffs[effect][4])*BuffNerfLevel*ActionMult ;
				Buff[effect][2] += (OriginalValue[effect]*Buffs[effect][6] + Buffs[effect][7])*BuffNerfLevel*ActionMult ;
				Buff[effect][3] += (OriginalValue[effect]*Buffs[effect][9] + Buffs[effect][10])*BuffNerfLevel*ActionMult ;
				Buff[effect][4] += Buffs[effect][12]*ActionMult ;
			}
			else
			{
				if (Math.random() <= Buffs[effect][2])
				{
					Buff[effect][1] += (OriginalValue[effect]*Buffs[effect][0] + Buffs[effect][1])*BuffNerfLevel*ActionMult ;
				}
				if (Math.random() <= Buffs[effect][5])
				{
					Buff[effect][1] += (OriginalValue[effect]*Buffs[effect][3] + Buffs[effect][4])*BuffNerfLevel*ActionMult ;
				}
				if (Math.random() <= Buffs[effect][8])
				{
					Buff[effect][2] += (OriginalValue[effect]*Buffs[effect][6] + Buffs[effect][7])*BuffNerfLevel*ActionMult ;
				}
				if (Math.random() <= Buffs[effect][11])
				{
					Buff[effect][3] += (OriginalValue[effect]*Buffs[effect][9] + Buffs[effect][10])*BuffNerfLevel*ActionMult ;
				}
				Buff[effect][4] += Buffs[effect][12]*ActionMult ;
			}
		}
		else if (action.equals("deactivate") | Math.random() <= Buffs[effect][2])
		{
			Buff[effect][0] += (OriginalValue[effect]*Buffs[effect][0] + Buffs[effect][1])*BuffNerfLevel*ActionMult ;
		}
		if (Target.equals("Player") & !SkillIsActive)
		{
			player.getLife().incCurrentValue((int) Buff[0][0]); ;
			player.getMp().incCurrentValue((int) Buff[1][0]); ;
			player.getPhyAtk().incBonus(Buff[2][0]) ;
			player.getMagAtk().incBonus(Buff[3][0]) ;
			player.getPhyDef().incBonus(Buff[4][0]) ;
			player.getMagDef().incBonus(Buff[5][0]) ;
			player.getDex().incBonus(Buff[6][0]) ;
			player.getAgi().incBonus(Buff[7][0]) ;
			player.getCrit()[1] += Buff[8][0] ;
			player.getStun()[1] += Buff[9][0] ;
			player.getBlock()[1] += Buff[10][0] ;
			player.getBlood()[1] += Buff[11][0] ;
			player.getBlood()[3] += Buff[11][1] ;
			player.getBlood()[5] += Buff[11][2] ;
			player.getBlood()[7] += Buff[11][3] ;
			player.getBlood()[8] += Buff[11][4] ;
			player.getPoison()[1] += Buff[12][0] ;
			player.getPoison()[3] += Buff[12][1] ;
			player.getPoison()[5] += Buff[12][2] ;
			player.getPoison()[7] += Buff[12][3] ;
			player.getPoison()[8] += Buff[12][4] ;
			player.getSilence()[1] += Buff[13][0] ;
		}
		if (Target.equals("Pet") & !SkillIsActive)
		{
			pet.getLife().incCurrentValue((int) Buff[0][0]); ;
			pet.getMp().incCurrentValue((int) Buff[1][0]); ;
			pet.getPhyAtk().incBonus(Buff[2][0]) ;
			pet.getMagAtk().incBonus(Buff[3][0]) ;
			pet.getPhyDef().incBonus(Buff[4][0]) ;
			pet.getMagDef().incBonus(Buff[5][0]) ;
			pet.getDex().incBonus(Buff[6][0]) ;
			pet.getAgi().incBonus(Buff[7][0]) ;
			pet.getCrit()[1] += Buff[8][0] ;
			pet.getStun()[1] += Buff[9][0] ;
			pet.getBlock()[1] += Buff[10][0] ;
			pet.getBlood()[1] += Buff[11][0] ;
			pet.getBlood()[3] += Buff[11][1] ;
			pet.getBlood()[5] += Buff[11][2] ;
			pet.getBlood()[7] += Buff[11][3] ;
			pet.getBlood()[8] += Buff[11][4] ;
			pet.getPoison()[1] += Buff[12][0] ;
			pet.getPoison()[3] += Buff[12][1] ;
			pet.getPoison()[5] += Buff[12][2] ;
			pet.getPoison()[7] += Buff[12][3] ;
			pet.getPoison()[8] += Buff[12][4] ;
			pet.getSilence()[1] += Buff[13][0] ;
		}
		if (Target.equals("Creature") & !SkillIsActive)
		{
			creature.getLife().incCurrentValue((int) -Buff[0][0]); ;
			creature.getMp().incCurrentValue((int) -Buff[1][0]); ;
			creature.getPhyAtk().incBonus(-Buff[2][0]) ;
			creature.getMagAtk().incBonus(-Buff[3][0]) ;
			creature.getPhyDef().incBonus(-Buff[4][0]) ;
			creature.getMagDef().incBonus(-Buff[5][0]) ;
			creature.getDex().incBonus(-Buff[6][0]) ;
			creature.getAgi().incBonus(-Buff[7][0]) ;
			creature.getCrit()[1] += -Buff[8][0] ;
			creature.getStun()[1] += -Buff[9][0] ;
			creature.getBlock()[1] += -Buff[10][0] ;
			creature.getBlood()[1] += -Buff[11][0] ;
			creature.getBlood()[3] += -Buff[11][1] ;
			creature.getBlood()[5] += -Buff[11][2] ;
			creature.getBlood()[7] += -Buff[11][3] ;
			creature.getBlood()[8] += -Buff[11][4] ;
			creature.getPoison()[1] += -Buff[12][0] ;
			creature.getPoison()[3] += -Buff[12][1] ;
			creature.getPoison()[5] += -Buff[12][2] ;
			creature.getPoison()[7] += -Buff[12][3] ;
			creature.getPoison()[8] += -Buff[12][4] ;
			creature.getSilence()[1] += -Buff[13][0] ;
		}
	}
	
	public void ItemEffectInBattle(BattleAttributes PlayerBA, BattleAttributes PetBA, BattleAttributes creatureBA, String[] creatureElem, double[] creatureLife, Items[] items, int ItemID, String target, String Elem, double[][] Effects, double[][] Buffs, String action)
	{
		double elemMult = CalcElemMult(Elem, "n", creatureElem[0], creatureElem[0], "n") ;
		creatureLife[0] += -Math.max(0, Effects[0][1])*elemMult*UtilG.RandomMult(randomAmp) ;
		if (target.equals("Player"))
		{
			PlayerBA.getBattleActions()[0][1] += -Effects[1][1] ;	// Atk speed
			PlayerBA.getSpecialStatus()[0] += Effects[2][2] ;	// Stun
			PlayerBA.getSpecialStatus()[2] += Effects[3][2] ;	// Blood
			PlayerBA.getSpecialStatus()[3] += Effects[4][2] ;	// Poison
			PlayerBA.getSpecialStatus()[4] += Effects[5][2] ;	// Silence
			PlayerBA.getSpecialStatus()[5] += Effects[6][2] ;	// Drunk
		}
		if (target.equals("Pet"))
		{
			PetBA.getBattleActions()[0][1] += -Effects[1][1] ;	// Atk speed
			PetBA.getSpecialStatus()[0] += Effects[2][2] ;	// Stun
			PetBA.getSpecialStatus()[2] += Effects[3][2] ;	// Blood
			PetBA.getSpecialStatus()[3] += Effects[4][2] ;	// Poison
			PetBA.getSpecialStatus()[4] += Effects[5][2] ;	// Silence
		}
		if (target.equals("Creature"))
		{
			creatureBA.getBattleActions()[0][1] += -Effects[1][1] ;	// Atk speed
			creatureBA.getSpecialStatus()[0] += Effects[2][2] ;	// Stun
			creatureBA.getSpecialStatus()[2] += Effects[3][2] ;	// Blood
			//PlayerBloodItemBonus = Effects[3][1] ;		// Blood atk
			creatureBA.getSpecialStatus()[3] += Effects[4][2] ;	// Poison
			//PlayerPoisonItemBonus = Effects[4][1] ;		// Poison atk
			creatureBA.getSpecialStatus()[4] += Effects[5][2] ;	// Silence
		}	
		for (int i = 0 ; i <= items[0].getBuffs().length - 1 ; i += 1)
		{
			//BuffsAndNerfs(player, pet, creature, Buffs, 1, i, false, target, action) ;	
			ItemEffectIsActive[ItemID][i] = true ;
		}
	}
	
	public void OffensiveSkillsStatus(Player player, Creature creature, Spell skills)
	{
		BattleAttributes playerBA = player.getBA();		// Battle attributes of the player
		BattleAttributes creatureBA = creature.getBA();	// Battle attributes of the creature
		
		double[] StunMod = skills.getStunMod() ;
		double[] BlockMod = skills.getBlockMod() ;
		double[] BloodMod = skills.getBloodMod() ;
		double[] PoisonMod = skills.getPoisonMod() ;
		double[] SilenceMod = skills.getSilenceMod() ;
		double[] Stun = new double[] {playerBA.TotalStunAtkChance() + StunMod[0], creatureBA.TotalStunDefChance() + StunMod[1], playerBA.StunDuration() + StunMod[2]} ;
		double[] Block = new double[] {playerBA.TotalBlockAtkChance() + BlockMod[0], creatureBA.TotalBlockDefChance() + BlockMod[1], playerBA.BlockDuration() + BlockMod[2]} ;
		double[] Blood = new double[] {playerBA.TotalBloodAtkChance() + BloodMod[0], creatureBA.TotalBloodDefChance() + BloodMod[1], playerBA.BloodDuration() + BloodMod[2]} ;
		double[] Poison = new double[] {playerBA.TotalPoisonAtkChance() + PoisonMod[0], creatureBA.TotalPoisonDefChance() + PoisonMod[1], playerBA.PoisonDuration() + PoisonMod[2]} ;
		double[] Silence = new double[] {playerBA.TotalSilenceAtkChance() + SilenceMod[0], creatureBA.TotalSilenceDefChance() + SilenceMod[1], playerBA.SilenceDuration() + SilenceMod[2]} ;
		int[] SkillStatus = CalcStatus(Stun, Block, Blood, Poison, Silence) ;
		creatureBA.receiveStatus(SkillStatus) ;
	}
	
	public Object[] OffensiveSkills(Player player, Creature creature, Spell skills, int spellID)
	{
		int skilllevel = player.getSpell().get(spellID).getLevel() ;
		int damage = -1 ;
		String effect = "" ;
		double PhyAtk = player.getBA().TotalPhyAtk() ;
		double MagAtk = player.getBA().TotalMagAtk() ;
		double PhyDef = creature.getBA().TotalPhyDef() ;
		double MagDef = creature.getBA().TotalMagDef() ;
		double AtkDex = player.getBA().TotalDex() ;
		double DefAgi = creature.getBA().TotalAgi() ;
		double AtkCrit = player.getBA().TotalCritAtkChance() ;
		double DefCrit = creature.getBA().TotalCritDefChance() ;
		double CreatureElemMod = 1 ;
		double ArrowAtk = 0 ;
		double[] AtkMod = new double[] {skills.getAtkMod()[0] * skilllevel, 1 + skills.getAtkMod()[1] * skilllevel} ;	// Atk modifier
		double[] DefMod = new double[] {skills.getDefMod()[0] * skilllevel, 1 + skills.getDefMod()[1] * skilllevel} ;	// Def modifier
		double[] DexMod = new double[] {skills.getDexMod()[0] * skilllevel, 1 + skills.getDexMod()[1] * skilllevel} ;	// Dex modifier
		double[] AgiMod = new double[] {skills.getAgiMod()[0] * skilllevel, 1 + skills.getAgiMod()[1] * skilllevel} ;	// Agi modifier
		double AtkCritMod = skills.getAtkCritMod()[0] * skilllevel ;	// Critical atk modifier
		double DefCritMod = skills.getDefCritMod()[0] * skilllevel ;	// Critical def modifier
		double BlockDef = creature.getBA().getSpecialStatus()[1] ;
		double BasicAtk = 0 ;
		double BasicDef = 0 ;
		String[] AtkElem = new String[] {skills.getElem(), player.getElem()[1], player.getElem()[4]} ;
		String[] DefElem = new String[] {creature.getElem()[0], creature.getElem()[0]} ;

		if (player.getJob() == 2 & player.arrowIsEquipped())	// If Arrow is equipped
		{
			ArrowAtk = Items.ArrowPower[player.getEquips()[3].getId() - Items.BagIDs[4]][0] ;
			UtilS.PrintBattleActions(4, "Player", "creature", -1, 0, creature.getBA().getSpecialStatus(), player.getElem()) ;
		}
		if (player.getJob() == 0)
		{
			BasicAtk = PhyAtk ;
			BasicDef = PhyDef ;
		}
		if (player.getJob() == 1)
		{
			BasicAtk = MagAtk ;
			BasicDef = MagDef ;
		}
		if (player.getJob() == 2)
		{
			if (spellID == 0 | spellID == 3 | spellID == 6 | spellID == 9 | spellID == 12)
			{
				BasicAtk = PhyAtk + ArrowAtk ;
				BasicDef = PhyDef ;
			}
			if (spellID == 2 | spellID == 5 | spellID == 11)
			{
				BasicAtk = (double) ((PhyAtk + MagAtk) / 2.0 + ArrowAtk) ;
				BasicDef = (double) ((PhyDef + MagDef) / 2.0) ;
			}
			if (spellID == 14)
			{
				BasicAtk = MagAtk + ArrowAtk ;
				BasicDef = MagDef ;
			}
		}
		if (player.getJob() == 3)
		{
			BasicAtk = PhyAtk ;
			BasicDef = PhyDef ;
		}
		if (player.getJob() == 4)
		{
			BasicAtk = PhyAtk ;
			BasicDef = PhyDef ;
		}
		System.out.println() ;
		System.out.println("Skill Atk = " + UtilG.Round((AtkMod[0] + BasicAtk*AtkMod[1]), 1) + " = " + AtkMod[0] + " + " + UtilG.Round(BasicAtk, 1) + " * " + AtkMod[1]) ;
		System.out.println("Skill Def = " + UtilG.Round((DefMod[0] + BasicDef*DefMod[1]), 1) + " = " + DefMod[0] + " + " + UtilG.Round(BasicDef, 1) + " * " + DefMod[1]) ;
		System.out.println("Skill Dex atk = " + UtilG.Round((DexMod[0] + AtkDex*DexMod[1]), 1) + " = " + DexMod[0] + " + " + UtilG.Round(AtkDex, 1) + " * " + DexMod[1]) ;
		System.out.println("Skill Agi def = " + UtilG.Round((AgiMod[0] + DefAgi*AgiMod[1]), 1) + " = " + AgiMod[0] + " + " + UtilG.Round(DefAgi, 1) + " * " + AgiMod[1]) ;
		System.out.println("Skill Crit atk = " + UtilG.Round((AtkCrit + AtkCritMod), 1) + " = " + UtilG.Round(AtkCrit, 1) + " + " + AtkCritMod) ;
		System.out.println("Skill Crit def = " + UtilG.Round((DefCrit + DefCritMod), 1) + " = " + UtilG.Round(DefCrit, 1) + " + " + DefCritMod) ;
		System.out.println("Skill Atk elem = " + Arrays.toString(AtkElem)) ;
		System.out.println("Skill Def elem = " + Arrays.toString(DefElem)) ;
		System.out.println("Skill Creature elem mod = " + CreatureElemMod) ;
		System.out.println("Skill Block def = " + BlockDef) ;
		effect = CalcEffect(DexMod[0] + AtkDex*DexMod[1], AgiMod[0] + DefAgi*AgiMod[1], AtkCrit + AtkCritMod, DefCrit + DefCritMod, BlockDef) ;
		damage = CalcAtk(effect, AtkMod[0] + BasicAtk*AtkMod[1], DefMod[0] + BasicDef*DefMod[1], AtkElem, DefElem, CreatureElemMod, randomAmp) ;
		return new Object[] {damage, effect} ;
	}
	
	public Object[] PlayerSpell(Player player, Pet pet, Creature creature, Spell spell)
	{
		Object[] atkResult = new Object[] {-1, ""} ;	// [damage, effect]
		int spellLevel = spell.getLevel() ;
		ArrayList<Spell> spells = player.getSpell() ;
		//player.ResetSpellCooldownCounter(spellID) ;
		player.ResetBattleActions() ;
		// TODO spell de suporte nï¿½o ï¿½ usado na batalha?
		if (spell.getType().equals(SpellTypes.offensive))
		{
			/*atkResult = OffensiveSkills(player, creature, spell, spellID) ;
			int damage = (int) atkResult[0] ;
			System.out.println("attack result = " + damage);
			atkResult[0] = Math.max(0, damage) ;
			creature.getLife()[0] += -UtilG.RandomMult(randomAmp) * damage ;
			if (player.getJob() == 2 & spellID == 3)	// Double shot
			{
				atkResult = OffensiveSkills(player, creature, spell, spellID) ;
				atkResult[0] = Math.max(0, damage) ;
				creature.getLife()[0] += -UtilG.RandomMult(randomAmp) * damage ;
			}
			if (player.getJob() == 2 & spellID == 12)	// Knocking shot
			{
				knockback(creature.getPos(), 2 * creature.getStep() * spellLevel, player.getPA()) ;
			}
			if (player.getJob() == 3 & spellID == 5)	// Head hit
			{
				if (Math.random() <= 0.004*spellLevel)
				{
					player.getBA().receiveStatus(new int[] {(int) player.getBA().StunDuration(), 0, 0, 0, 0}) ;
				}
			}
			if (player.getJob() == 4 & spellID == 3)	// Steal
			{
				//player.StealItem(creature, items, skilllevel) ;
			}
			if (player.getJob() == 4 & spellID == 12)	// Murder
			{
				if (Math.random() <= 0.016*spellLevel + 0.02*(player.getBA().TotalDex() - creature.getBA().TotalAgi())/(player.getBA().TotalDex() + creature.getBA().TotalAgi()))
				{
					creature.getLife()[0] = 0 ;
				}
			}*/
		}		
		return atkResult ;
	}
	
	public Object[] PetSpell(Pet pet, Creature creature, int selectedSpell)
	{
		Spell spell = pet.getSpells().get(selectedSpell) ;
		int damage = -1 ;
		String effect = "" ;
		int level = pet.getSpells().get(selectedSpell).getLevel() ;
		double BasicPhyAtk = pet.getBA().TotalPhyAtk() ;
		double BasicMagAtk = pet.getBA().TotalMagAtk() ;
		double BasicPhyDef = creature.getBA().TotalPhyDef() ;
		double BasicMagDef = creature.getBA().TotalMagDef() ;
		double BasicAtkDex = pet.getBA().TotalDex() ;
		double BasicDefAgi = creature.getBA().TotalAgi() ;
		double BasicAtkCrit = pet.getBA().TotalCritAtkChance() ;
		double BasicDefCrit = creature.getBA().TotalCritDefChance() ;
		double CreatureElemModif = 1 ;
		if (spell.getMpCost() <= pet.getMp().getCurrentValue())
		{
			if (pet.getJob() == 0)
			{
				if (selectedSpell == 0)
				{	
					effect = CalcEffect(BasicAtkDex, BasicDefAgi, BasicAtkCrit, BasicDefCrit, creature.getBA().getSpecialStatus()[1]) ;
					damage = CalcAtk(effect, BasicPhyAtk*(double)(1 + 0.02*level) + level, BasicPhyDef, new String[] {pet.getElem()[0], pet.getElem()[1], pet.getElem()[4]}, new String[] {creature.getElem()[0], creature.getElem()[0]}, CreatureElemModif, randomAmp) ;															
				}
			}
			if (pet.getJob() == 1)
			{
				if (selectedSpell == 0)
				{				
					effect = CalcEffect(BasicAtkDex, BasicDefAgi, BasicAtkCrit, BasicDefCrit, creature.getBA().getSpecialStatus()[1]) ;
					damage = CalcAtk(effect, BasicMagAtk*(double)(1 + 0.02*level) + level, BasicMagDef, new String[] {pet.getElem()[0], pet.getElem()[1], pet.getElem()[4]}, new String[] {creature.getElem()[0], creature.getElem()[0]}, CreatureElemModif, randomAmp) ;															
				}
			}
			if (pet.getJob() == 2)
			{
				
			}
			if (pet.getJob() == 3)
			{
				
			}
			damage = Math.max(0, damage) ;
			creature.getLife().incCurrentValue((int) (-UtilG.RandomMult(randomAmp) * damage)); ;		
			pet.getMp().incCurrentValue(-spell.getMpCost()); ;	
		}		
		return new Object[] {damage, effect} ;
	}
	
	
	public Object[] PlayerAtk(Player player, Pet pet, Creature creature)
	{
		int damage = -1 ;
		String effect = "" ;
		String atkType = "";
		String action = player.getPA().getCurrentAction() ;
		
		if (player.actionIsAnAtk())	// Physical atk
		{
			atkType = "Physical" ;	// TODO arrow atks can be physical and magical at the same time
			double ArrowAtk = 0 ;
			if (player.getJob() == 2 & player.arrowIsEquipped())
			{
				ArrowAtk = Items.ArrowPower[player.getEquips()[3].getId() - Items.BagIDs[4]][0] ;
				player.SpendArrow() ;
			}
			effect = CalcEffect(player.getBA().TotalDex(), creature.getBA().TotalAgi(), player.getBA().TotalCritAtkChance(), creature.getBA().TotalCritDefChance(), creature.getBA().getSpecialStatus()[1]) ;
			damage = CalcAtk(effect, player.getBA().TotalPhyAtk() + ArrowAtk, creature.getBA().TotalPhyDef(), player.getAtkElems(), creature.getDefElems(), 1, randomAmp) ;
			if (effect.equals("Hit"))
			{
				creature.getPA().getLife().incCurrentValue(-damage);;
				int[] inflictedStatus = CalcStatus(new double[] {player.getBA().TotalStunAtkChance(), creature.getBA().TotalStunDefChance(), player.getBA().StunDuration()},
						new double[] {player.getBA().TotalBlockAtkChance(), creature.getBA().TotalBlockDefChance(), player.getBA().BlockDuration()},
						new double[] {player.getBA().TotalBloodAtkChance(), creature.getBA().TotalBloodDefChance(), player.getBA().BloodDuration()},
						new double[] {player.getBA().TotalPoisonAtkChance(), creature.getBA().TotalPoisonDefChance(), player.getBA().PoisonDuration()},
						new double[] {player.getBA().TotalSilenceAtkChance(), creature.getBA().TotalSilenceDefChance(), player.getBA().SilenceDuration()}) ;
				creature.getBA().receiveStatus(inflictedStatus) ;
			}
			player.ResetBattleActions() ;
		}
		else if (player.actionIsASpell())	// Magical atk
		{
			atkType = "Spell" ;
			if (!player.isSilent() & player.hasTheSpell(action))
			{
				Spell spell = player.getSpell().get(Integer.parseInt(action)) ;				
				if (spell.isReady() & player.hasEnoughMP(spell))	// 0 < player.getSpell()[SkillID].getLevel()
				{
					Object[] AtkResult = PlayerSpell(player, pet, creature, spell) ;
					damage = (int) AtkResult[0] ;
					effect = (String) AtkResult[1] ;
				}
			}
			player.ResetBattleActions() ;
		}
		else if (action.equals(Player.BattleKeys[1]))							// Defense
		{
			atkType = "Defense" ;
 			player.ActivateDef() ;
			player.ResetBattleActions() ;
		}

		return new Object[] {damage, effect, atkType} ;
	}
	

	public Object[] PetAtk(Pet pet, Creature creature, DrawingOnPanel DP)
	{
		int damage = -1 ;
		int effect = 0 ;
		String atkType = "";
		int[] inflictedStatus = new int[5] ;	// [Stun, block, blood, poison, silence]
		String action = pet.getPA().getCurrentAction() ;
		BattleAttributes creatureBA = creature.getBA(), petBA = pet.getBA() ;
		
		if (!petBA.isStun())	// If not stun
		{
			//petBA.setCurrentAction(pet.action) ;
			if (pet.actionIsAnAtk())	// Physical atk
			{
				atkType = "Physical" ;
				//int[] AtkResult = CalcAtk(petBA.TotalPhyAtk(), creatureBA.TotalPhyDef(), petBA.TotalDex(), creatureBA.TotalAgi(), petBA.TotalCritAtkChance(), creatureBA.TotalCritDefChance(), new String[] {pet.getElem()[0], pet.getElem()[1], pet.getElem()[4]}, new String[] {creature.getElem()[0], creature.getElem()[0]}, CreatureElemModif, creatureBA.getSpecialStatus()[1]) ;
				//damage = AtkResult[0] ;
				//effect = AtkResult[1] ;
				if (effect <= 1)
				{
					creature.getLife().incCurrentValue(-damage); ;
					inflictedStatus = CalcStatus(new double[] {petBA.TotalStunAtkChance(), creatureBA.TotalStunDefChance(), petBA.StunDuration()}, new double[] {petBA.TotalBlockAtkChance(), creatureBA.TotalBlockDefChance(), petBA.BlockDuration()}, new double[] {petBA.TotalBloodAtkChance(), creatureBA.TotalBloodDefChance(), petBA.BloodDuration()}, new double[] {petBA.TotalPoisonAtkChance(), creatureBA.TotalPoisonDefChance(), petBA.PoisonDuration()}, new double[] {petBA.TotalSilenceAtkChance(), creatureBA.TotalSilenceDefChance(), petBA.SilenceDuration()}) ;
					creatureBA.receiveStatus(inflictedStatus) ;
				}
				Show[1][0] = true ;
				ShowAtkCounters[1][0] = 0 ;
			}
			else if (pet.actionIsASpell() & !pet.isSilent())	// magical atk, if not silent
			{
				atkType = "Spell" ;
				if (Block(creatureBA.getSpecialStatus()[1]))
				{
					effect = 3 ;	// Block
				}
				else
				{
					/*int SkillID = Integer.parseInt(pet.action) ;
					if (SkillIsReady[UtilG.IndexOf(Player.SpellKeys, pet.action)] & pet.getSpells()[SkillID].getMpCost() <= pet.getMp()[0])
					{
						Show[1][0] = true ;
						ShowAtkCounters[1][0] = 0 ;
						damage = (int) PetSpell(pet, creature, Integer.parseInt(pet.action))[0] ;
						pet.ResetBattleActions() ;
						SkillIsReady[UtilG.IndexOf(Player.SpellKeys, pet.action)] = false ;
						pet.getMagAtk()[2] += Train(pet.getMagAtk()[2]) ;
					}*/
				}
			}
			else if (action.equals(Player.ActionKeys[3]))
			{
				atkType = "Defense" ;
	 			pet.ActivateDef() ;
			}
			if (action.equals(Player.ActionKeys[1]) | action.equals(Player.ActionKeys[3]))
			{
				pet.ResetBattleActions() ;
			}
		}
		return new Object[] {damage, effect, inflictedStatus, atkType} ;
	}
	
	
	public Object[] CreatureAtk(Player player, Pet pet, Creature creature)
	{
		int damage = -1 ;
		String effect = "" ;
		int[] Status = new int[5] ;	// [Stun, block, blood, poison, silence]
		int MpCost = 10 ;
		String creatureAction = creature.getPA().getCurrentAction() ;
		BattleAttributes creatureBA = creature.getBA(), playerBA = player.getBA(), petBA = pet.getBA() ;
		
		creature.fight(Player.ActionKeys) ;
		creature.UpdateCombo() ;
		CreatureTarget = 0 ;
		if (pet.isAlive())
		{
			CreatureTarget = (int)(2*Math.random() - 0.01) ;
		}
		if (!creatureBA.isStun())	// If not stun
		{
			//creatureBA.setCurrentAction(creature.getAction()) ;
			if (creature.getAction().equals(Player.ActionKeys[1]) | -1 < UtilG.IndexOf(Player.SpellKeys, creature.getAction()))	// Creature atks
			{
				if (CreatureTarget == 0)	// Creature atks player
				{
					if (creature.getAction().equals(Player.ActionKeys[1]))	// Physical atk
					{
						effect = CalcEffect(creatureBA.TotalDex(), playerBA.TotalAgi(), creatureBA.TotalCritAtkChance(), playerBA.TotalCritDefChance(), player.getBlock()[1]) ;
						damage = CalcAtk(effect, creatureBA.TotalPhyAtk(), playerBA.TotalPhyDef(), new String[] {creature.getElem()[0], "n", "n"}, new String[] {player.getElem()[2], player.getElem()[3]}, 1, randomAmp) ; //player.getElemMult()[UtilS.ElementID(creature.getElem()[0])]) ;
					}
					else if (creature.actionIsASpell() & !creature.isSilent())	// Magical atk
					{	
						int spellID = UtilG.IndexOf(Player.SpellKeys, creatureAction) ;
						if (creature.hasEnoughMP(spellID))	// creature has enough MP to use the skill
						{
							damage = creature.useSpell(spellID, player) ;
							creature.ResetBattleActions() ;
						}
					}
					if (effect.equals("Hit"))	// Hit
					{
						player.getLife().incCurrentValue(-damage); ;
						Status = CalcStatus(new double[] {creatureBA.TotalStunAtkChance(), playerBA.TotalStunDefChance(), creatureBA.StunDuration()}, new double[] {creatureBA.TotalBlockAtkChance(), playerBA.TotalBlockDefChance(), creatureBA.BlockDuration()}, new double[] {creatureBA.TotalBloodAtkChance(), playerBA.TotalBloodDefChance(), creatureBA.BloodDuration()}, new double[] {creatureBA.TotalPoisonAtkChance(), playerBA.TotalPoisonDefChance(), creatureBA.PoisonDuration()}, new double[] {creatureBA.TotalSilenceAtkChance(), playerBA.TotalSilenceDefChance(), creatureBA.SilenceDuration()}) ;
						playerBA.receiveStatus(Status) ;
					}
					/*else if (effect.equals("Miss"))	// Miss
					{
						player.getAgi()[2] += Train(player.getAgi()[2]) ;
					}*/
				}
				else if (CreatureTarget == 1)	// Creature atks pet
				{
					if (creature.getAction().equals(Player.ActionKeys[1]))	// Physical atk
					{
						effect = CalcEffect(creatureBA.TotalDex(), petBA.TotalAgi(), creatureBA.TotalCritAtkChance(), petBA.TotalCritDefChance(), pet.getBlock()[1]) ;
						damage = CalcAtk(effect, creatureBA.TotalPhyAtk(), petBA.TotalPhyDef(), new String[] {creature.getElem()[0], "n", "n"}, new String[] {pet.getElem()[2], pet.getElem()[3]}, pet.getElemMult()[UtilS.ElementID(creature.getElem()[0])], randomAmp) ;
					}
					else if (-1 < UtilG.IndexOf(Player.SpellKeys, creature.getAction()) & !creature.isSilent() & MpCost <= creature.getMp().getCurrentValue())	// Magical atk
					{	
						effect = CalcEffect(creatureBA.TotalDex(), petBA.TotalAgi(), creatureBA.TotalCritAtkChance(), petBA.TotalCritDefChance(), pet.getBlock()[1]) ;
						damage = CalcAtk(effect, creatureBA.TotalMagAtk(), petBA.TotalMagDef(), new String[] {creature.getElem()[0], "n", "n"}, new String[] {pet.getElem()[2], pet.getElem()[3]}, pet.getElemMult()[UtilS.ElementID(creature.getElem()[0])], randomAmp) ;
						creature.getMp().incCurrentValue(-MpCost); ;
						creature.ResetBattleActions() ;
					}
					if (effect.equals("Hit"))	// Hit
					{
						pet.getLife().incCurrentValue(-damage); ;
						Status = CalcStatus(new double[] {creatureBA.TotalStunAtkChance(), petBA.TotalStunDefChance(), creatureBA.StunDuration()}, new double[] {creatureBA.TotalBlockAtkChance(), petBA.TotalBlockDefChance(), creatureBA.BlockDuration()}, new double[] {creatureBA.TotalBloodAtkChance(), petBA.TotalBloodDefChance(), creatureBA.BloodDuration()}, new double[] {creatureBA.TotalPoisonAtkChance(), petBA.TotalPoisonDefChance(), creatureBA.PoisonDuration()}, new double[] {creatureBA.TotalSilenceAtkChance(), petBA.TotalSilenceDefChance(), creatureBA.SilenceDuration()}) ;
						petBA.receiveStatus(Status) ;
					}
					/*else if (effect.equals("Miss"))
					{
						pet.getAgi()[2] += Train(pet.getAgi()[2]) ;
					}*/
				}
				//Show[2][0] = true ;
				//ShowAtkCounters[2][0] = 0 ;
			} else if (creature.getAction().equals(Player.ActionKeys[3]))	// Creature defends
			{
	 			creature.ActivateDef() ;
			}
			if (creature.getAction().equals(Player.ActionKeys[1]) | creature.getAction().equals(Player.ActionKeys[3]))
			{
				creature.ResetBattleActions() ;
			}
		}
		else
		{
			//UtilS.PrintBattleActions(1, "Creature", "pet", damage, effect, petBA.getSpecialStatus(), creature.getElem()) ;
		}
		return new Object[] {damage, effect} ;
	}

	
	public void AtkAnimations(Point AttackerPos, Point TargetPos, Dimension AttackerSize, Dimension TargetSize, boolean UsedSkill, Object[] PlayerAtkResult, int DamageAnimation,
			boolean[] Show, int[] Durations, ArrayList<Spell> spells, ArrayList<Spell> activePlayerSpells, String PlayerMove)
	{
		if (0 <= (int)PlayerAtkResult[0])
		{
			Ani.SetAniVars(1, new Object[] {Durations[0], TargetPos, TargetSize, PlayerAtkResult, DamageAnimation}) ;	// Damage animation
			Ani.StartAni(1) ;
		}
		if (Show[1])	// Phy atk animation
		{
			
		}
		Ani.SetAniVars(2, new Object[] {Durations[1], AttackerPos, TargetPos, TargetSize, DamageAnimation}) ;
		Ani.StartAni(2) ;
		if ( UsedSkill)	// Mag atk animation
		{
			Ani.SetAniVars(3, new Object[] {Durations[2], Show[3], AttackerPos, TargetPos, AttackerSize, TargetSize, activePlayerSpells.get(UtilG.IndexOf(Player.SpellKeys, PlayerMove))}) ;
			Ani.StartAni(3) ;
		}
		if (Show[4])	// Arrow atk animation
		{
		}
		Ani.SetAniVars(4, new Object[] {Durations[0], AttackerPos, TargetPos, TargetSize}) ;
		Ani.StartAni(4) ;
	}
	
	
	/*public void StatusAnimations(int AniID, int[] AttackerPos, int[] TargetPos, int[] AttackerSize, int Duration, int[] SpecialStatus, boolean isDefending)
	{
		String RelPos = Uts.RelPos(AttackerPos, TargetPos) ;
		if (isDefending)	// Status animation
		{
			Ani.SetAniVars(AniID, new Object[] {Duration, AttackerPos, AttackerSize, RelPos, BattleIconImages, SpecialStatus, isDefending}) ;
			Ani.StartAni(AniID) ;
		}
	}*/
	
	
	public void RunBattle(Player player, Pet pet, Creature creature, Quests[] quest, Point MousePos, DrawingOnPanel DP)
	{	
		ShowAtkDurations[2][0] = creature.getBA().getBattleActions()[0][1]/2 ;
		IncrementCounters() ;
		ActivateCounters(player, pet, creature) ;
		if (player.isAlive())
		{
			player.DrawTimeBar(creature, DP) ;
			player.TakeBloodAndPoisonDamage(creature.getBA().TotalBloodAtk(), creature.getBA().TotalPoisonAtk()) ;
			if (player.canAtk() & UtilS.IsInRange(player.getPos(), creature.getPos(), player.getRange()))
			{
				Object[] PlayerAtkResult = PlayerAtk(player, pet, creature) ;
				int damage = (int) PlayerAtkResult[0] ;
				String effect = (String) PlayerAtkResult[1] ;
				if (player.getPA().getCurrentAction().equals(Player.BattleKeys[0]) | player.actionIsASpell())
				{
					AtkAnimations(player.getPos(), creature.getPos(), player.getSize(), creature.getSize(), player.actionIsASpell(), PlayerAtkResult, player.getSettings().getDamageAnimation(),
							Show[0], ShowAtkDurations[0], player.getSpell(), player.GetActiveSpells(), player.getPA().getCurrentAction()) ;
				}
				//int effect = (int) PlayerAtkResult[1] ;
				//int[] statusinflicted = (int[]) PlayerAtkResult[2] ;
				//String atkType = (String) PlayerAtkResult[3] ;
				if (0 <= damage)
				{					
					//System.out.println();
					//System.out.println("player life: " + player.getLife()[0]);
					//System.out.println("player mp: " + player.getMp()[0]);
					//System.out.println("player action: " + player.getAction());
					//System.out.println("player life: " + player.getLife()[0]);
					if (player.getSettings().getSoundEffectsAreOn())
					{
						SoundEffects[0].start() ;	// Hit sound effect
					}
					if (player.getSettings().getSoundEffectsAreOn() & !SoundEffects[0].isActive())
					{
						SoundEffects[0].setMicrosecondPosition(0) ;
					}
				}
				//HighestPlayerInflictedDamage = Math.max(HighestPlayerInflictedDamage, PlayerAtkResult[0]) ;
				//Uts.PrintBattleActions2(player.getAction(), creature.getAction(), "player", "creature", new Object[] {PlayerAtkResult[0], PlayerAtkResult[1], creature.getBA().getSpecialStatus()}, creature.getElem()) ;
				player.autoSpells(creature, player.getSpell());
				player.train(PlayerAtkResult) ;
				player.updateoffensiveStats(PlayerAtkResult, creature) ;
			}
			player.ShowEffectsAndStatusAnimation(creature, DP) ;
			player.getBA().decreaseStatus() ;
		}
		if (pet.isAlive())
		{
			//DF.DrawTimeBar(pet.getPos(), pet.getBA().getBattleActions()[0][0], pet.getBA().getBattleActions()[0][1], pet.getSize()[1], new int[] {(BattleIconImages[0].getWidth(null) + 5), 0}, Uts.RelPos(pet.getPos(), creature.getPos()), "Vertical", Color.green) ;
			pet.TakeBloodAndPoisonDamage(creature) ;
			if (pet.canAtk() & UtilS.IsInRange(pet.getPos(), creature.getPos(), pet.getRange()))
			{
				Object[] PetAtkResult = PetAtk(pet, creature, DP) ;
				//AtkAnimations(new int[] {5, 6, 7}, pet.CenterPos(), creature.getPos(), pet.getSize(), creature.getSize(), pet.usedSkill(), PetAtkResult, Show[0], ShowAtkDurations[0], skills, new int[] {0, 1, 2, 3, 4}, pet.action) ;
				pet.train(PetAtkResult) ;
				//HighestPetInflictedDamage = Math.max(HighestPetInflictedDamage, PetAtkResult[0]) ;
				
				/*System.out.println();
				System.out.println("creature life: " + creature.getLife()[0]);
				System.out.println("creature mp: " + creature.getMp()[0]);
				System.out.println("creature action: " + creature.getAction());
				System.out.println("player life: " + player.getLife()[0]);*/
			}
			//DF.ShowEffectsAndStatusAnimation(pet.getPos(), Uts.MirrorFromRelPos(Uts.RelPos(pet.getPos(), creature.getPos())), new int[] {10, (int)(0.8*pet.getSize()[1])}, BattleIconImages, pet.getBA().getSpecialStatus(), new boolean[] {pet.isDefending()}) ;
			pet.getBA().decreaseStatus() ;
		}
		if (creature.isAlive())
		{
			//DF.DrawTimeBar(creature.getPos(), creature.getBA().getBattleActions()[0][0], creature.getBA().getBattleActions()[0][1], creature.getSize()[1], new int[] {creature.getSize()[0] / 2 + (BattleIconImages[0].getWidth(null) + 5), -creature.getSize()[1] / 6}, Uts.RelPos(creature.getPos(), player.getPos()), "Vertical", Color.blue) ;
			//creature.TakeBloodAndPoisonDamage(player, SkillBuffIsActive) ;
			creature.TakeBloodAndPoisonDamage(pet) ;
			if (creature.canAtk() & UtilS.IsInRange(creature.getPos(), player.getPos(), creature.getRange()))
			{
				Object[] CreatureAtkResult = CreatureAtk(player, pet, creature) ;
				int damage = (int) CreatureAtkResult[0] ;
				String effect = (String) CreatureAtkResult[1] ;
				if (player.getJob() == 4 & SkillBuffIsActive[11][0])	// Surprise attack
				{
					SkillBuffIsActive[11][0] = false ;
				}
				if (player.getJob() == 4 & Math.random() < 0.2*player.getSpell().get(11).getLevel() & effect.equals("Crit"))	// Surprise attack
				{
					creature.getLife().incCurrentValue((int) -Math.min(0.5 * damage, 2*player.getPhyAtk().getBaseValue())); ;
					// needs to show the atk animation
					SkillBuffIsActive[11][0] = true ;
				}
				//AtkAnimations(new int[] {8, 9}, creature.getPos(), player.getPos(), creature.getSize(), player.getSize(), false, CreatureAtkResult, Show[2], ShowAtkDurations[2], player.getSpell(), null, "") ;
				//HighestCreatureInflictedDamageOnPlayer = Math.max(HighestCreatureInflictedDamageOnPlayer, CreatureAtkResult[0]) ;
				//HighestCreatureInflictedDamageOnPet = Math.max(HighestCreatureInflictedDamageOnPet, CreatureAtkResult[0]) ;
				/*UtilS.PrintBattleActions2(player.getAction(), creature.getAction(), "creature", "player",
						new Object[] {CreatureAtkResult[0], CreatureAtkResult[1], creature.getBA().getSpecialStatus()}, creature.getElem()) ;*/
				player.updatedefensiveStats(damage, effect, creature.actionIsASpell(), creature) ;
				
				System.out.println();
				System.out.println("creature life: " + creature.getLife().getCurrentValue());
				System.out.println("creature mp: " + creature.getMp().getCurrentValue());
				System.out.println("creature action: " + creature.getAction());
				System.out.println("creature def: " + creature.getMagDef().getBonus());
				System.out.println("player life: " + player.getLife().getCurrentValue());
				System.out.println("player pos: " + player.getPos());
			}
			//DF.ShowEffectsAndStatusAnimation(creature.getPos(), Uts.MirrorFromRelPos(Uts.RelPos(creature.getPos(), player.getPos())), new int[] {13, (int)(0.8*creature.getSize()[1])}, BattleIconImages, creature.getBA().getSpecialStatus(), new boolean[] {creature.isDefending()}) ;
			creature.getBA().decreaseStatus() ;
			//DF.DrawCreatureAttributes(creature) ;
		}
		if (!creature.isAlive() | !player.isAlive())
		{	
			FinishBattle(player, pet, creature, quest) ;
		}
	}
	
	
	public void FinishBattle(Player player, Pet pet, Creature creature, Quests[] quest)
	{
		System.out.println("Battle is over!");
		System.out.println();
		ArrayList<Spell> playerSpell = player.getSpell() ;
		player.getPA().setState(LiveBeingStates.idle) ;
		player.resetOpponent() ;
		creature.Dies();
		
		// reset buffs
		for (int i = 0 ; i <= playerSpell.get(0).getBuffs().length - 1 ; ++i)
		{
			Arrays.fill(PlayerSkillBuffCounter[i], 0) ;
		}
		
		// deactivate survivor's instinct (skill of the animals)
		if (player.getJob() == 3)	// Survivor's instinct
		{
			Spell survivorInstinct = player.getSpell().get(12) ;
			if (0 <survivorInstinct.getLevel() & SkillBuffIsActive[12][0])
			{
				for (int i = 0 ; i <= survivorInstinct.getBuffs().length - 1 ; ++i)
				{
					BuffsAndNerfs(player, pet, creature, survivorInstinct.getBuffs(), survivorInstinct.getLevel(), i, false, "Player", "deactivate") ;
				}
				SkillBuffIsActive[12][0] = false ;
			}
		}
		
		// deactivate the battle view
		Show[0][0] = false ;
		Show[0][4] = false ;
		Show[0][2] = false ;
		Show[1][0] = false ;
		Show[2][0] = false ;
		
		if (player.isAlive())
		{
			player.Win(creature, quest, Ani) ;
			if (pet.isAlive())
			{
				pet.Win(creature) ;
			}
			creature.setRandomPos() ;
			player.getPA().setCurrentAction("") ;
		}
		else
		{
			player.Dies() ;
			if (pet.isAlive())
			{
				pet.getPA().setPos(player.getPos()) ;
			}			
		}
	}
}
