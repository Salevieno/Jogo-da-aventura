package Actions ;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays ;
import javax.sound.sampled.Clip ;

import GameComponents.Items ;
import GameComponents.Quests;
import Graphics.Animations ;
import Graphics.DrawFunctions ;
import LiveBeings.BattleAttributes;
import LiveBeings.Creature;
import LiveBeings.Pet;
import LiveBeings.Player;
import LiveBeings.Spells;
import LiveBeings.States;
import Main.Game;
import Utilities.Size;
import Utilities.UtilG;
import Utilities.UtilS;

public class Battle 
{
	//private int[] PlayerAtkResult, PetAtkResult, CreatureAtkResult ;
	private Clip[] SoundEffects ;
	private Animations Ani ;
	
	private int[][] ShowAtkCounters, ShowAtkDurations ;
	private boolean[][] Show ;
	private int CreatureTarget ;
	private float randomAmp ;
	private int[][] PlayerSkillBuffCounter, PlayerSkillNerfCounter, PetSkillBuffCounter, PetSkillNerfCounter, PlayerItemEffectCounter ;
	private boolean[][] SkillBuffIsActive, ItemEffectIsActive ;

	protected static String[] ElemID ;
	protected static float[][] ElemMult ;
	
	public Battle(Spells[] skills, Spells[] petskills, Clip[] SoundEffects, int[] DamageDelay, Animations Ani)
	{	
		this.SoundEffects = SoundEffects ;
		this.Ani = Ani ;

		int NumElem = 10 ;
    	ElemID = new String[NumElem] ;
		ElemMult = new float[NumElem][NumElem] ;
		ArrayList<String[]> ElemInput = UtilG.ReadcsvFile(Game.CSVPath + "Elem.csv") ;
		for (int i = 0 ; i <= NumElem - 1 ; ++i)
		{
			ElemID[i] = ElemInput.get(i)[0] ;
			for (int j = 0 ; j <= NumElem - 1 ; ++j)
			{
				ElemMult[i][j] = Float.parseFloat(ElemInput.get(i)[j + 1]) ;
			}				
		}
		randomAmp = (float)0.1 ;
		
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
		PlayerSkillBuffCounter = new int[skills.length][skills[0].getBuffs().length] ;
		PlayerSkillNerfCounter = new int[skills.length][skills[0].getNerfs().length] ;
		PetSkillBuffCounter = new int[petskills.length][skills[0].getBuffs().length] ;
		PetSkillNerfCounter = new int[petskills.length][skills[0].getNerfs().length] ;
		//PlayerItemEffectCounter = new int[items.length][items[0].getBuffs().length] ;
		CreatureTarget = 0 ;
		for (int i = 0 ; i <= skills.length - 1 ; ++i)
		{
			for (int j = 0 ; j <= skills[i].getBuffs().length - 1 ; ++j)
			{
				PlayerSkillBuffCounter[i][j] = 0 ;
			}
			for (int j = 0 ; j <= skills[i].getNerfs().length - 1 ; ++j)
			{
				PlayerSkillNerfCounter[i][j] = 0 ;
			}
		}
		for (int i = 0 ; i <= petskills.length - 1 ; ++i)
		{
			for (int j = 0 ; j <= petskills[i].getBuffs().length - 1 ; ++j)
			{
				PetSkillBuffCounter[i][j] = 0 ;
			}
			for (int j = 0 ; j <= petskills[i].getNerfs().length - 1 ; ++j)
			{
				PetSkillNerfCounter[i][j] = 0 ;
			}
		}
		ShowAtkDurations[1][3] = DamageDelay[1] ;
		//SkillIsReady = new boolean[skills.length] ;
		SkillBuffIsActive = new boolean[skills.length][skills[0].getBuffs().length] ;
		//ItemEffectIsActive = new boolean[Items.ItemsWithEffects.length][items[0].getBuffs().length] ;
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

	public void BuffsAndNerfs(Player player, Pet pet, Creature creature, float[][] Buffs, int BuffNerfLevel, int effect, boolean SkillIsActive, String Target, String action)
	{
		int ActionMult = 1 ;
		float[][] Buff = new float[14][5] ;	// [Life, Mp, PhyAtk, MagAtk, Phy def, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence][effect]
		float[] OriginalValue = new float[14] ;	// [Life, Mp, PhyAtk, MagAtk, Phy def, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence]
		if (action.equals("deactivate"))
		{
			ActionMult = -1 ;
		}
		if (Target.equals("Player"))
		{
			OriginalValue = new float[] {player.getLife()[1], player.getMp()[1], player.getBA().getPhyAtk()[0], player.getBA().getMagAtk()[0], player.getBA().getPhyDef()[0], player.getBA().getMagDef()[0], player.getBA().getDex()[0], player.getBA().getAgi()[0], player.getBA().getCrit()[0], player.getBA().getStun()[0], player.getBA().getBlock()[0], player.getBA().getBlood()[0], player.getBA().getBlood()[2], player.getBA().getBlood()[4], player.getBA().getBlood()[6], player.getBA().getPoison()[0], player.getBA().getPoison()[2], player.getBA().getPoison()[4], player.getBA().getPoison()[6], player.getBA().getSilence()[0]} ;
		}
		if (Target.equals("Pet"))
		{
			OriginalValue = new float[] {pet.getLife()[1], pet.getMp()[1], pet.getPhyAtk()[0], pet.getMagAtk()[0], pet.getPhyDef()[0], pet.getMagDef()[0], pet.getDex()[0], pet.getAgi()[0], pet.getCrit()[0], pet.getStun()[0], pet.getBlock()[0], pet.getBlood()[0], pet.getBlood()[2], pet.getBlood()[4], pet.getBlood()[6], pet.getPoison()[0], pet.getPoison()[2], pet.getPoison()[4], pet.getPoison()[6], pet.getSilence()[0]} ;
		}
		if (Target.equals("Creature"))
		{
			OriginalValue = new float[] {creature.getLife()[1], creature.getMp()[1], creature.getPhyAtk()[0], creature.getMagAtk()[0], creature.getPhyDef()[0], creature.getMagDef()[0], creature.getDex()[0], creature.getAgi()[0], creature.getCrit()[0], creature.getStun()[0], creature.getBlock()[0], creature.getBlood()[0], creature.getBlood()[2], creature.getBlood()[4], creature.getBlood()[6], creature.getPoison()[0], creature.getPoison()[2], creature.getPoison()[4], creature.getPoison()[6], creature.getSilence()[0]} ;
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
			player.getLife()[0] += Buff[0][0] ;
			player.getMp()[0] += Buff[1][0] ;
			player.getPhyAtk()[1] += Buff[2][0] ;
			player.getMagAtk()[1] += Buff[3][0] ;
			player.getPhyDef()[1] += Buff[4][0] ;
			player.getMagDef()[1] += Buff[5][0] ;
			player.getDex()[1] += Buff[6][0] ;
			player.getAgi()[1] += Buff[7][0] ;
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
			pet.getLife()[0] += Buff[0][0] ;
			pet.getMp()[0] += Buff[1][0] ;
			pet.getPhyAtk()[1] += Buff[2][0] ;
			pet.getMagAtk()[1] += Buff[3][0] ;
			pet.getPhyDef()[1] += Buff[4][0] ;
			pet.getMagDef()[1] += Buff[5][0] ;
			pet.getDex()[1] += Buff[6][0] ;
			pet.getAgi()[1] += Buff[7][0] ;
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
			creature.getLife()[0] += -Buff[0][0] ;
			creature.getMp()[0] += -Buff[1][0] ;
			creature.getPhyAtk()[1] += -Buff[2][0] ;
			creature.getMagAtk()[1] += -Buff[3][0] ;
			creature.getPhyDef()[1] += -Buff[4][0] ;
			creature.getMagDef()[1] += -Buff[5][0] ;
			creature.getDex()[1] += -Buff[6][0] ;
			creature.getAgi()[1] += -Buff[7][0] ;
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
	
	public void ItemEffectInBattle(BattleAttributes PlayerBA, BattleAttributes PetBA, BattleAttributes creatureBA, String[] creatureElem, float[] creatureLife, Items[] items, int ItemID, String target, String Elem, float[][] Effects, float[][] Buffs, String action)
	{
		float elemMult = BattleActions.CalcElemMult(Elem, "n", creatureElem[0], creatureElem[0], "n") ;
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
	
	public void OffensiveSkillsStatus(Player player, Creature creature, Spells skills)
	{
		BattleAttributes playerBA = player.getBA();		// Battle attributes of the player
		BattleAttributes creatureBA = creature.getBA();	// Battle attributes of the creature
		
		float[] StunMod = skills.getStunMod() ;
		float[] BlockMod = skills.getBlockMod() ;
		float[] BloodMod = skills.getBloodMod() ;
		float[] PoisonMod = skills.getPoisonMod() ;
		float[] SilenceMod = skills.getSilenceMod() ;
		float[] Stun = new float[] {playerBA.TotalStunAtkChance() + StunMod[0], creatureBA.TotalStunDefChance() + StunMod[1], playerBA.StunDuration() + StunMod[2]} ;
		float[] Block = new float[] {playerBA.TotalBlockAtkChance() + BlockMod[0], creatureBA.TotalBlockDefChance() + BlockMod[1], playerBA.BlockDuration() + BlockMod[2]} ;
		float[] Blood = new float[] {playerBA.TotalBloodAtkChance() + BloodMod[0], creatureBA.TotalBloodDefChance() + BloodMod[1], playerBA.BloodDuration() + BloodMod[2]} ;
		float[] Poison = new float[] {playerBA.TotalPoisonAtkChance() + PoisonMod[0], creatureBA.TotalPoisonDefChance() + PoisonMod[1], playerBA.PoisonDuration() + PoisonMod[2]} ;
		float[] Silence = new float[] {playerBA.TotalSilenceAtkChance() + SilenceMod[0], creatureBA.TotalSilenceDefChance() + SilenceMod[1], playerBA.SilenceDuration() + SilenceMod[2]} ;
		int[] SkillStatus = BattleActions.CalcStatus(Stun, Block, Blood, Poison, Silence) ;
		creatureBA.receiveStatus(SkillStatus) ;
	}
	
	public Object[] OffensiveSkills(Player player, Creature creature, Spells skills, int SkillID)
	{
		int skilllevel = player.getSpell()[SkillID].getLevel() ;
		int damage = -1 ;
		String effect = "" ;
		float PhyAtk = player.getBA().TotalPhyAtk() ;
		float MagAtk = player.getBA().TotalMagAtk() ;
		float PhyDef = creature.getBA().TotalPhyDef() ;
		float MagDef = creature.getBA().TotalMagDef() ;
		float AtkDex = player.getBA().TotalDex() ;
		float DefAgi = creature.getBA().TotalAgi() ;
		float AtkCrit = player.getBA().TotalCritAtkChance() ;
		float DefCrit = creature.getBA().TotalCritDefChance() ;
		float CreatureElemMod = 1 ;
		float ArrowAtk = 0 ;
		float[] AtkMod = new float[] {skills.getAtkMod()[0] * skilllevel, 1 + skills.getAtkMod()[1] * skilllevel} ;	// Atk modifier
		float[] DefMod = new float[] {skills.getDefMod()[0] * skilllevel, 1 + skills.getDefMod()[1] * skilllevel} ;	// Def modifier
		float[] DexMod = new float[] {skills.getDexMod()[0] * skilllevel, 1 + skills.getDexMod()[1] * skilllevel} ;	// Dex modifier
		float[] AgiMod = new float[] {skills.getAgiMod()[0] * skilllevel, 1 + skills.getAgiMod()[1] * skilllevel} ;	// Agi modifier
		float AtkCritMod = skills.getAtkCritMod()[0] * skilllevel ;	// Critical atk modifier
		float DefCritMod = skills.getDefCritMod()[0] * skilllevel ;	// Critical def modifier
		float BlockDef = creature.getBA().getSpecialStatus()[1] ;
		float BasicAtk = 0 ;
		float BasicDef = 0 ;
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
			if (SkillID == 0 | SkillID == 3 | SkillID == 6 | SkillID == 9 | SkillID == 12)
			{
				BasicAtk = PhyAtk + ArrowAtk ;
				BasicDef = PhyDef ;
			}
			if (SkillID == 2 | SkillID == 5 | SkillID == 11)
			{
				BasicAtk = (float) ((PhyAtk + MagAtk) / 2.0 + ArrowAtk) ;
				BasicDef = (float) ((PhyDef + MagDef) / 2.0) ;
			}
			if (SkillID == 14)
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
		effect = BattleActions.CalcEffect(DexMod[0] + AtkDex*DexMod[1], AgiMod[0] + DefAgi*AgiMod[1], AtkCrit + AtkCritMod, DefCrit + DefCritMod, BlockDef) ;
		damage = BattleActions.CalcAtk(effect, AtkMod[0] + BasicAtk*AtkMod[1], DefMod[0] + BasicDef*DefMod[1], AtkElem, DefElem, CreatureElemMod, randomAmp) ;
		return new Object[] {damage, effect} ;
	}
	
	public Object[] PlayerSpell(Player player, Pet pet, Creature creature, int spellID)
	{
		Object[] atkResult = new Object[] {-1, ""} ;	// [damage, effect]
		int spellLevel = player.getSpell()[spellID].getLevel() ;
		Spells[] spells = player.getSpell() ;
		player.ResetSpellCooldownCounter(spellID) ;
		player.ResetBattleActions() ;
		// TODO spell de suporte não é usado na batalha?
		if (spells[spellID].getType().equals("Offensive"))
		{
			atkResult = OffensiveSkills(player, creature, spells[spellID], spellID) ;
			int damage = (int) atkResult[0] ;
			//player.OffensiveSkill(creature, SelectedSkill, skills) ;
			System.out.println("attack result = " + damage);
			atkResult[0] = Math.max(0, damage) ;
			creature.getLife()[0] += -UtilG.RandomMult(randomAmp) * damage ;
			if (player.getJob() == 2 & spellID == 3)	// Double shot
			{
				atkResult = OffensiveSkills(player, creature, spells[spellID], spellID) ;
				//player.OffensiveSkill(creature, SelectedSkill, skills) ;
				atkResult[0] = Math.max(0, damage) ;
				creature.getLife()[0] += -UtilG.RandomMult(randomAmp) * damage ;
			}
			if (player.getJob() == 2 & spellID == 12)	// Knocking shot
			{
				BattleActions.knockback(creature.getPos(), 2 * creature.getStep() * spellLevel, player.getPA()) ;
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
			}
		}		
		return atkResult ;
	}
	
	public Object[] PetSpell(Pet pet, Creature creature, int selectedSpell)
	{
		Spells spell = pet.getSpells()[selectedSpell] ;
		int damage = -1 ;
		String effect = "" ;
		int level = pet.getSpells()[selectedSpell].getLevel() ;
		float BasicPhyAtk = pet.getBA().TotalPhyAtk() ;
		float BasicMagAtk = pet.getBA().TotalMagAtk() ;
		float BasicPhyDef = creature.getBA().TotalPhyDef() ;
		float BasicMagDef = creature.getBA().TotalMagDef() ;
		float BasicAtkDex = pet.getBA().TotalDex() ;
		float BasicDefAgi = creature.getBA().TotalAgi() ;
		float BasicAtkCrit = pet.getBA().TotalCritAtkChance() ;
		float BasicDefCrit = creature.getBA().TotalCritDefChance() ;
		float CreatureElemModif = 1 ;
		if (spell.getMpCost() <= pet.getMp()[0])
		{
			if (pet.getJob() == 0)
			{
				if (selectedSpell == 0)
				{	
					effect = BattleActions.CalcEffect(BasicAtkDex, BasicDefAgi, BasicAtkCrit, BasicDefCrit, creature.getBA().getSpecialStatus()[1]) ;
					damage = BattleActions.CalcAtk(effect, BasicPhyAtk*(float)(1 + 0.02*level) + level, BasicPhyDef, new String[] {pet.getElem()[0], pet.getElem()[1], pet.getElem()[4]}, new String[] {creature.getElem()[0], creature.getElem()[0]}, CreatureElemModif, randomAmp) ;															
				}
			}
			if (pet.getJob() == 1)
			{
				if (selectedSpell == 0)
				{				
					effect = BattleActions.CalcEffect(BasicAtkDex, BasicDefAgi, BasicAtkCrit, BasicDefCrit, creature.getBA().getSpecialStatus()[1]) ;
					damage = BattleActions.CalcAtk(effect, BasicMagAtk*(float)(1 + 0.02*level) + level, BasicMagDef, new String[] {pet.getElem()[0], pet.getElem()[1], pet.getElem()[4]}, new String[] {creature.getElem()[0], creature.getElem()[0]}, CreatureElemModif, randomAmp) ;															
				}
			}
			if (pet.getJob() == 2)
			{
				
			}
			if (pet.getJob() == 3)
			{
				
			}
			damage = Math.max(0, damage) ;
			creature.getLife()[0] += -UtilG.RandomMult(randomAmp) * damage ;		
			pet.getMp()[0] += -spell.getMpCost() ;	
		}		
		return new Object[] {damage, effect} ;
	}
	
	
	public Object[] PlayerAtk(Player player, Pet pet, Creature creature)
	{
		int damage = -1 ;
		String effect = "" ;
		String atkType = "";
		String action = player.getPA().getCurrentAction() ;
		
		player.getBA().setCurrentAction(action) ;
		if (player.actionIsAnAtk())	// Physical atk
		{
			atkType = "Physical" ;	// TODO arrow atks can be physical and magical at the same time
			float ArrowAtk = 0 ;
			if (player.getJob() == 2 & player.arrowIsEquipped())
			{
				ArrowAtk = Items.ArrowPower[player.getEquips()[3].getId() - Items.BagIDs[4]][0] ;
				player.SpendArrow() ;
			}
			effect = BattleActions.CalcEffect(player.getBA().TotalDex(), creature.getBA().TotalAgi(), player.getBA().TotalCritAtkChance(), creature.getBA().TotalCritDefChance(), creature.getBA().getSpecialStatus()[1]) ;
			damage = BattleActions.CalcAtk(effect, player.getBA().TotalPhyAtk() + ArrowAtk, creature.getBA().TotalPhyDef(), player.getAtkElems(), creature.getDefElems(), 1, randomAmp) ;
			if (effect.equals("Hit"))
			{
				creature.getPA().incLife(-damage) ;
				int[] inflictedStatus = BattleActions.CalcStatus(new float[] {player.getBA().TotalStunAtkChance(), creature.getBA().TotalStunDefChance(), player.getBA().StunDuration()},
						new float[] {player.getBA().TotalBlockAtkChance(), creature.getBA().TotalBlockDefChance(), player.getBA().BlockDuration()},
						new float[] {player.getBA().TotalBloodAtkChance(), creature.getBA().TotalBloodDefChance(), player.getBA().BloodDuration()},
						new float[] {player.getBA().TotalPoisonAtkChance(), creature.getBA().TotalPoisonDefChance(), player.getBA().PoisonDuration()},
						new float[] {player.getBA().TotalSilenceAtkChance(), creature.getBA().TotalSilenceDefChance(), player.getBA().SilenceDuration()}) ;
				creature.getBA().receiveStatus(inflictedStatus) ;
			}
			player.ResetBattleActions() ;
		}
		else if (player.actionIsASpell())	// Magical atk
		{
			atkType = "Spell" ;
			if (!player.isSilent() & player.hasTheSpell(action))
			{
				int spellID = player.getSpellID(action) ;				
				if (player.SpellIsReady(spellID) & player.hasEnoughMP(spellID))	// 0 < player.getSpell()[SkillID].getLevel()
				{
					Object[] AtkResult = PlayerSpell(player, pet, creature, spellID) ;
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
	

	public Object[] PetAtk(Pet pet, Creature creature, DrawFunctions DF)
	{
		int damage = -1 ;
		int effect = 0 ;
		String atkType = "";
		int[] inflictedStatus = new int[5] ;	// [Stun, block, blood, poison, silence]
		BattleAttributes creatureBA = creature.getBA(), petBA = pet.getBA() ;
		
		if (!petBA.isStun())	// If not stun
		{
			petBA.setCurrentAction(pet.action) ;
			if (pet.actionIsAnAtk())	// Physical atk
			{
				atkType = "Physical" ;
				//int[] AtkResult = CalcAtk(petBA.TotalPhyAtk(), creatureBA.TotalPhyDef(), petBA.TotalDex(), creatureBA.TotalAgi(), petBA.TotalCritAtkChance(), creatureBA.TotalCritDefChance(), new String[] {pet.getElem()[0], pet.getElem()[1], pet.getElem()[4]}, new String[] {creature.getElem()[0], creature.getElem()[0]}, CreatureElemModif, creatureBA.getSpecialStatus()[1]) ;
				//damage = AtkResult[0] ;
				//effect = AtkResult[1] ;
				if (effect <= 1)
				{
					creature.getLife()[0] += -damage ;
					inflictedStatus = BattleActions.CalcStatus(new float[] {petBA.TotalStunAtkChance(), creatureBA.TotalStunDefChance(), petBA.StunDuration()}, new float[] {petBA.TotalBlockAtkChance(), creatureBA.TotalBlockDefChance(), petBA.BlockDuration()}, new float[] {petBA.TotalBloodAtkChance(), creatureBA.TotalBloodDefChance(), petBA.BloodDuration()}, new float[] {petBA.TotalPoisonAtkChance(), creatureBA.TotalPoisonDefChance(), petBA.PoisonDuration()}, new float[] {petBA.TotalSilenceAtkChance(), creatureBA.TotalSilenceDefChance(), petBA.SilenceDuration()}) ;
					creatureBA.receiveStatus(inflictedStatus) ;
				}
				Show[1][0] = true ;
				ShowAtkCounters[1][0] = 0 ;
			}
			else if (pet.actionIsASpell() & !pet.isSilent())	// magical atk, if not silent
			{
				atkType = "Spell" ;
				if (BattleActions.Block(creatureBA.getSpecialStatus()[1]))
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
			else if (pet.action.equals(Player.ActionKeys[3]))
			{
				atkType = "Defense" ;
	 			pet.ActivateDef() ;
			}
			if (pet.action.equals(Player.ActionKeys[1]) | pet.action.equals(Player.ActionKeys[3]))
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
		BattleAttributes creatureBA = creature.getBA(), playerBA = player.getBA(), petBA = pet.getBA() ;
		
		creature.fight(Player.ActionKeys) ;
		creature.UpdateCombo() ;
		CreatureTarget = 0 ;
		if (0 < pet.getLife()[0])
		{
			CreatureTarget = (int)(2*Math.random() - 0.01) ;
		}
		if (!creatureBA.isStun())	// If not stun
		{
			creatureBA.setCurrentAction(creature.getAction()) ;
			if (creature.getAction().equals(Player.ActionKeys[1]) | -1 < UtilG.IndexOf(Player.SpellKeys, creature.getAction()))	// Creature atks
			{
				if (CreatureTarget == 0)	// Creature atks player
				{
					if (creature.getAction().equals(Player.ActionKeys[1]))	// Physical atk
					{
						effect = BattleActions.CalcEffect(creatureBA.TotalDex(), playerBA.TotalAgi(), creatureBA.TotalCritAtkChance(), playerBA.TotalCritDefChance(), player.getBlock()[1]) ;
						damage = BattleActions.CalcAtk(effect, creatureBA.TotalPhyAtk(), playerBA.TotalPhyDef(), new String[] {creature.getElem()[0], "n", "n"}, new String[] {player.getElem()[2], player.getElem()[3]}, 1, randomAmp) ; //player.getElemMult()[UtilS.ElementID(creature.getElem()[0])]) ;
					}
					else if (creatureBA.actionisskill() & !creature.isSilent())	// Magical atk
					{	
						int skillID = UtilG.IndexOf(Player.SpellKeys, creatureBA.getCurrentAction()) ;
						if (creature.hasEnoughMP(skillID))	// creature has enough MP to use the skill
						{
							damage = creature.useSkill(skillID, player) ;
							creature.ResetBattleActions() ;
						}
					}
					if (effect.equals("Hit"))	// Hit
					{
						player.getLife()[0] += -damage ;
						Status = BattleActions.CalcStatus(new float[] {creatureBA.TotalStunAtkChance(), playerBA.TotalStunDefChance(), creatureBA.StunDuration()}, new float[] {creatureBA.TotalBlockAtkChance(), playerBA.TotalBlockDefChance(), creatureBA.BlockDuration()}, new float[] {creatureBA.TotalBloodAtkChance(), playerBA.TotalBloodDefChance(), creatureBA.BloodDuration()}, new float[] {creatureBA.TotalPoisonAtkChance(), playerBA.TotalPoisonDefChance(), creatureBA.PoisonDuration()}, new float[] {creatureBA.TotalSilenceAtkChance(), playerBA.TotalSilenceDefChance(), creatureBA.SilenceDuration()}) ;
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
						effect = BattleActions.CalcEffect(creatureBA.TotalDex(), petBA.TotalAgi(), creatureBA.TotalCritAtkChance(), petBA.TotalCritDefChance(), pet.getBlock()[1]) ;
						damage = BattleActions.CalcAtk(effect, creatureBA.TotalPhyAtk(), petBA.TotalPhyDef(), new String[] {creature.getElem()[0], "n", "n"}, new String[] {pet.getElem()[2], pet.getElem()[3]}, pet.getElemMult()[UtilS.ElementID(creature.getElem()[0])], randomAmp) ;
					}
					else if (-1 < UtilG.IndexOf(Player.SpellKeys, creature.getAction()) & !creature.isSilent() & MpCost <= creature.getMp()[0])	// Magical atk
					{	
						effect = BattleActions.CalcEffect(creatureBA.TotalDex(), petBA.TotalAgi(), creatureBA.TotalCritAtkChance(), petBA.TotalCritDefChance(), pet.getBlock()[1]) ;
						damage = BattleActions.CalcAtk(effect, creatureBA.TotalMagAtk(), petBA.TotalMagDef(), new String[] {creature.getElem()[0], "n", "n"}, new String[] {pet.getElem()[2], pet.getElem()[3]}, pet.getElemMult()[UtilS.ElementID(creature.getElem()[0])], randomAmp) ;
						creature.getMp()[0] += -MpCost ;
						creature.ResetBattleActions() ;
					}
					if (effect.equals("Hit"))	// Hit
					{
						pet.getLife()[0] += -damage ;
						Status = BattleActions.CalcStatus(new float[] {creatureBA.TotalStunAtkChance(), petBA.TotalStunDefChance(), creatureBA.StunDuration()}, new float[] {creatureBA.TotalBlockAtkChance(), petBA.TotalBlockDefChance(), creatureBA.BlockDuration()}, new float[] {creatureBA.TotalBloodAtkChance(), petBA.TotalBloodDefChance(), creatureBA.BloodDuration()}, new float[] {creatureBA.TotalPoisonAtkChance(), petBA.TotalPoisonDefChance(), creatureBA.PoisonDuration()}, new float[] {creatureBA.TotalSilenceAtkChance(), petBA.TotalSilenceDefChance(), creatureBA.SilenceDuration()}) ;
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

	
	public void AtkAnimations(Point AttackerPos, Point TargetPos, Size AttackerSize, Size TargetSize, boolean UsedSkill, Object[] PlayerAtkResult, int DamageAnimation, boolean[] Show, int[] Durations, Spells[] skills, ArrayList<Integer> ActivePlayerSkills, String PlayerMove)
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
			Ani.SetAniVars(3, new Object[] {Durations[2], Show[3], AttackerPos, TargetPos, AttackerSize, TargetSize, skills[ActivePlayerSkills.get(UtilG.IndexOf(Player.SpellKeys, PlayerMove))]}) ;
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
	
	
	public void RunBattle(Player player, Pet pet, Creature creature, Quests[] quest, Point MousePos, DrawFunctions DF)
	{	
		ShowAtkDurations[2][0] = creature.getBA().getBattleActions()[0][1]/2 ;
		IncrementCounters() ;
		ActivateCounters(player, pet, creature) ;
		if (0 < player.getLife()[0])
		{
			player.DrawTimeBar(creature, DF.getDrawPrimitives()) ;
			player.TakeBloodAndPoisonDamage(creature.getBA().TotalBloodAtk(), creature.getBA().TotalPoisonAtk()) ;
			if (player.canAtk() & UtilS.IsInRange(player.getPos(), creature.getPos(), player.getRange()))
			{
				Object[] PlayerAtkResult = PlayerAtk(player, pet, creature) ;
				int damage = (int) PlayerAtkResult[0] ;
				String effect = (String) PlayerAtkResult[1] ;
				if (player.getPA().getCurrentAction().equals(Player.BattleKeys[0]) | player.actionIsASpell())
				{
					AtkAnimations(player.getPos(), creature.getPos(), player.getSize(), creature.getSize(), player.getBA().actionisskill(), PlayerAtkResult, player.getSettings().getDamageAnimation(),
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
			player.ShowEffectsAndStatusAnimation(creature, DF.getDrawPrimitives()) ;
			player.getBA().decreaseStatus() ;
		}
		if (0 < pet.getLife()[0])
		{
			//DF.DrawTimeBar(pet.getPos(), pet.getBA().getBattleActions()[0][0], pet.getBA().getBattleActions()[0][1], pet.getSize()[1], new int[] {(BattleIconImages[0].getWidth(null) + 5), 0}, Uts.RelPos(pet.getPos(), creature.getPos()), "Vertical", Color.green) ;
			pet.TakeBloodAndPoisonDamage(creature) ;
			if (pet.canAtk() & UtilS.IsInRange(pet.getPos(), creature.getPos(), pet.getRange()))
			{
				Object[] PetAtkResult = PetAtk(pet, creature, DF) ;
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
		if (0 < creature.getLife()[0])
		{
			//DF.DrawTimeBar(creature.getPos(), creature.getBA().getBattleActions()[0][0], creature.getBA().getBattleActions()[0][1], creature.getSize()[1], new int[] {creature.getSize()[0] / 2 + (BattleIconImages[0].getWidth(null) + 5), -creature.getSize()[1] / 6}, Uts.RelPos(creature.getPos(), player.getPos()), "Vertical", Color.blue) ;
			creature.TakeBloodAndPoisonDamage(player, SkillBuffIsActive) ;
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
				if (player.getJob() == 4 & Math.random() < 0.2*player.getSpell()[11].getLevel() & effect.equals("Crit"))	// Surprise attack
				{
					creature.getLife()[0] += -Math.min(0.5 * damage, 2*player.getPhyAtk()[0]) ;
					// needs to show the atk animation
					SkillBuffIsActive[11][0] = true ;
				}
				//AtkAnimations(new int[] {8, 9}, creature.getPos(), player.getPos(), creature.getSize(), player.getSize(), false, CreatureAtkResult, Show[2], ShowAtkDurations[2], player.getSpell(), null, "") ;
				//HighestCreatureInflictedDamageOnPlayer = Math.max(HighestCreatureInflictedDamageOnPlayer, CreatureAtkResult[0]) ;
				//HighestCreatureInflictedDamageOnPet = Math.max(HighestCreatureInflictedDamageOnPet, CreatureAtkResult[0]) ;
				UtilS.PrintBattleActions2(player.getAction(), creature.getAction(), "creature", "player", new Object[] {CreatureAtkResult[0], CreatureAtkResult[1], creature.getBA().getSpecialStatus()}, creature.getElem()) ;
				player.updatedefensiveStats(damage, effect, creature.getBA().actionisskill(), creature) ;
				
				System.out.println();
				System.out.println("creature life: " + creature.getLife()[0]);
				System.out.println("creature mp: " + creature.getMp()[0]);
				System.out.println("creature action: " + creature.getAction());
				System.out.println("creature def: " + creature.getMagDef()[1]);
				System.out.println("player life: " + player.getLife()[0]);
				System.out.println("player pos: " + player.getPos());
			}
			//DF.ShowEffectsAndStatusAnimation(creature.getPos(), Uts.MirrorFromRelPos(Uts.RelPos(creature.getPos(), player.getPos())), new int[] {13, (int)(0.8*creature.getSize()[1])}, BattleIconImages, creature.getBA().getSpecialStatus(), new boolean[] {creature.isDefending()}) ;
			creature.getBA().decreaseStatus() ;
			//DF.DrawCreatureAttributes(creature) ;
		}
		if (creature.getLife()[0] <= 0 | player.getLife()[0] <= 0)
		{	
			FinishBattle(player, pet, creature, quest) ;
		}
	}
	
	
	public void FinishBattle(Player player, Pet pet, Creature creature, Quests[] quest)
	{
		System.out.println("Battle is over!");
		System.out.println();
		Spells[] playerSpell = player.getSpell() ;
		player.getPA().setState(States.idle) ;
		player.opponent = null ;
		creature.getLife()[0] = creature.getLife()[1] ;
		creature.getMp()[0] = creature.getMp()[1] ;
		creature.setFollow(false) ;
		
		// reset buffs
		for (int i = 0 ; i <= playerSpell[0].getBuffs().length - 1 ; ++i)
		{
			Arrays.fill(PlayerSkillBuffCounter[i], 0) ;
		}
		
		// deactivate survivor's instinct (skill of the animals)
		if (player.getJob() == 3 & 0 < player.getSpell()[12].getLevel() & SkillBuffIsActive[12][0])	// Survivor's instinct
		{
			for (int i = 0 ; i <= playerSpell[12].getBuffs().length - 1 ; ++i)
			{
				BuffsAndNerfs(player, pet, creature, playerSpell[12].getBuffs(), player.getSpell()[12].getLevel(), i, false, "Player", "deactivate") ;
			}
			SkillBuffIsActive[12][0] = false ;
		}
		
		// deactivate the battle view
		Show[0][0] = false ;
		Show[0][4] = false ;
		Show[0][2] = false ;
		Show[1][0] = false ;
		Show[2][0] = false ;
		
		if (0 < player.getLife()[0])	// If the player is alive
		{
			player.Win(creature, quest, Ani) ;
			if (0 < pet.getLife()[0])
			{
				pet.Win(creature) ;
			}
			creature.setRandomPos() ;
			player.setCurrentAction("") ;
		}
		else	// if the player is dead
		{
			player.Dies() ;
			if (0 < pet.getLife()[0])
			{
				pet.getPA().setPos(player.getPos()) ;
			}			
		}
	}
}
