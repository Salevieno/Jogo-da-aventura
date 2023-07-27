package main ;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import graphics.Animation;
import graphics.DrawingOnPanel;
import liveBeings.Creature;
import liveBeings.LiveBeing;
import liveBeings.LiveBeingStates;
import liveBeings.Pet;
import liveBeings.Player;
import liveBeings.Spell;
import simulations.PlayerEvolutionSimulation;
import utilities.AttackEffects;
import utilities.Elements;
import utilities.GameStates;
import utilities.UtilG;

public class Battle 
{
	private static double randomAmp ;
	protected static List<Elements> ElemID ;
	protected static double[][] ElemMult ;
	
	static
	{
		int NumElem = 10 ;
    	ElemID = new ArrayList<>() ;
		ElemMult = new double[NumElem][NumElem] ;
		List<String[]> ElemInput = UtilG.ReadcsvFile(Game.CSVPath + "Elem.csv") ;
		for (int i = 0 ; i <= NumElem - 1 ; ++i)
		{
			ElemID.add(Elements.valueOf(ElemInput.get(i)[0])) ;
			for (int j = 0 ; j <= NumElem - 1 ; ++j)
			{
				ElemMult[i][j] = Double.parseDouble(ElemInput.get(i)[j + 1]) ;
			}				
		}
		randomAmp = (double)0.1 ;
	}
	
	public Battle()
	{	
		//this.SoundEffects = SoundEffects ;
//		hitSound = Music.musicFileToClip(new File(Game.MusicPath + "16-Hit.wav").getAbsoluteFile()) ;

	}

	// métodos de cálculo de batalha válidos para todos os participantes
	private static double basicElemMult(Elements Atk, Elements Def)
	{
		return ElemMult[ElemID.indexOf(Atk)][ElemID.indexOf(Def)] ;
	}
	
	private static boolean hit(double Dex, double Agi)
	{
		boolean hit = false ;
		
		if (Math.random() <= 1 - 1/(1 + Math.pow(1.1, Dex - Agi)))
		{
			hit = true ;
		}
		
		return hit ;
	}
	
	private static boolean block(double BlockDef)
	{
		if (Math.random() < BlockDef)
		{
			return true ;
		}
		return false ;
	}

	private static boolean criticalAtk(double CritAtk, double CritDef)
	{
		return (Math.random() + CritDef <= CritAtk) ;
	}
		
	private static double calcElemMult(Elements Atk, Elements Weapon, Elements Armor, Elements Shield, Elements SuperElem)
	{
		double mult = 1 ;
		mult = basicElemMult(Atk, Armor) * mult ;
		mult = basicElemMult(Atk, Shield) * mult ;
		mult = basicElemMult(Weapon, Armor) * mult ;
		mult = basicElemMult(Weapon, Shield) * mult ;
		mult = basicElemMult(SuperElem, Armor) * mult ;
		mult = basicElemMult(SuperElem, Shield) * mult ;
		return mult ;
	}

	private static AtkResults calcPhysicalAtk(LiveBeing attacker, LiveBeing receiver)
	{
		AttackEffects effect = calcEffect(attacker.getBA().TotalDex(), receiver.getBA().TotalAgi(), attacker.getBA().TotalCritAtkChance(), receiver.getBA().TotalCritDefChance(), receiver.getBA().getStatus().getBlock()) ;
		int damage = calcDamage(effect, attacker.getBA().TotalPhyAtk(), receiver.getBA().TotalPhyDef(), attacker.atkElems(), receiver.defElems(), 1) ;

		return new AtkResults(AtkTypes.physical , effect, damage) ;
	}
	
	public static AttackEffects calcEffect(double Dex, double Agi, double CritAtk, double CritDef, double BlockDef)
	{
		AttackEffects effect ;
		if (block(BlockDef))
		{
			effect = AttackEffects.block ;
		} 
		else if (hit(Dex, Agi))
		{
			effect = AttackEffects.hit ;
			if (criticalAtk(CritAtk, CritDef))
			{
				effect = AttackEffects.crit ;
			}
		}
		else
		{
			effect = AttackEffects.miss ;
		}
		return effect;
	}
	
	public static int calcDamage(AttackEffects effect, double Atk, double Def, Elements[] atkElems, Elements[] defElem, double ElemModifier)
	{
		int damage = -1 ;
		if (effect.equals(AttackEffects.miss) | effect.equals(AttackEffects.block))
		{
			damage = 0 ;
		} 
		else if (effect.equals(AttackEffects.hit))
		{
			damage = Math.max(0, (int)(Atk - Def)) ;
		}
		else if (effect.equals(AttackEffects.crit))
		{
			damage = (int) Atk ;
		}
		double randomMult = UtilG.RandomMult(randomAmp) ;
		double elemMult = calcElemMult(atkElems[0], atkElems[1], defElem[0], defElem[0], atkElems[2]) ;
		damage = (int)(randomMult*elemMult*ElemModifier*Atk) ;
		return damage ;
	}
		
	private static int[] calcStatus(double[] atkChances, double[] defChances, int[] durations)
	{
		int[] status = new int[atkChances.length] ;
		
		for (int i = 0; i <= atkChances.length - 1; i += 1)
		{
			if (Math.random() <= atkChances[i] - defChances[i])
			{
				status[i] = durations[i] ;
			}
		}
		return status ;
	}
	
	public static Point knockback(Point originPos, Point targetPos, int dist)
	{
		double angle = targetPos.x != originPos.x ? Math.atan((targetPos.y - originPos.y) / (double)(targetPos.x - originPos.x)) : Math.PI / 2.0 ;
		int travelX = (int) (Math.signum(targetPos.x - originPos.x) * Math.cos(angle) * dist) ;
		int travelY = (int) (Math.signum(targetPos.y - originPos.y) * Math.sin(angle) * dist) ;
		
		Point destiny = new Point(targetPos.x + travelX, targetPos.y + travelY) ;
		return destiny ;
	}
		
	private void ActivateCounters(Player player, Pet pet, Creature creature)
	{
		if (player.getBattleActionCounter().finished() & player.getCurrentBattleAction() != null)
		{
			if (player.getCurrentBattleAction().equals(AtkTypes.defense))
			{
				player.DeactivateDef() ;
			}
//			UtilS.PrintBattleActions(6, "Player", "creature", 0, 0, player.getBA().getSpecialStatus(), creature.getElem()) ;
		}
		if (pet != null)
		{
			if (pet.isAlive() & pet.getBattleActionCounter().finished() & pet.getCurrentBattleAction() != null)
			{
				if (pet.getCurrentBattleAction().equals(AtkTypes.defense))
				{
//					UtilS.PrintBattleActions(6, "Pet", "creature", 0, 0, player.getBA().getSpecialStatus(), creature.getElem()) ;
		 			pet.DeactivateDef() ;
				}
			}
		}
		if (creature.getBattleActionCounter().finished() & creature.getCurrentBattleAction() != null)
		{
			if (creature.getCurrentBattleAction().equals(AtkTypes.defense))
			{
//				UtilS.PrintBattleActions(6, "Creature", "creature", 0, 0, player.getBA().getSpecialStatus(), creature.getElem()) ;
	 			creature.DeactivateDef() ;
			}
		}
		
//		player.ActivateBattleActionCounters() ;
//		pet.ActivateBattleActionCounters() ;
//		creature.ActivateBattleActionCounters() ;
		
		if (player.getBattleActionCounter().finished())
		{
			player.resetBattleAction() ;
		}
		if (pet != null)
		{
			if (pet.getBattleActionCounter().finished())
			{
				pet.resetBattleAction() ;
			}
		}
		if (creature.getBattleActionCounter().finished())
		{
			creature.resetBattleAction() ;
		}
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
	
	private AtkResults Atk(LiveBeing attacker, LiveBeing receiver)
	{
		AtkResults atkResult = new AtkResults() ;
		if (attacker.actionIsArrowAtk())
		{
			// TODO add arrow atk, arrows can be physical and magical at the same time
		}
		if (attacker.actionIsPhysicalAtk())
		{
			atkResult = calcPhysicalAtk(attacker, receiver) ;
			if (atkResult.getEffect().equals(AttackEffects.hit))
			{
				receiver.getPA().getLife().incCurrentValue(-atkResult.getDamage()) ;
				int[] inflictedStatus = calcStatus(attacker.getBA().baseAtkChances(), receiver.getBA().baseDefChances(), attacker.getBA().baseDurations()) ;				
				receiver.getBA().getStatus().receiveStatus(inflictedStatus) ;
			}
		}
		else if (attacker.actionIsSpell() & !attacker.isSilent())
		{
			Spell spell = attacker.getSpells().get(Player.SpellKeys.indexOf(attacker.getCurrentAction())) ;
			if (spell.isReady() & attacker.hasEnoughMP(spell))
			{
				atkResult = attacker.useSpell(spell, receiver) ;
				if (atkResult.getEffect().equals(AttackEffects.hit) | atkResult.getEffect().equals(AttackEffects.crit))
				{
					receiver.getPA().getLife().incCurrentValue(-atkResult.getDamage()) ;
					int[] inflictedStatus = calcStatus(attacker.getBA().baseAtkChances(), receiver.getBA().baseDefChances(), attacker.getBA().baseDurations()) ;				
					receiver.getBA().getStatus().receiveStatus(inflictedStatus) ;
				}
			}
		}
		else if (attacker.actionIsDef())
		{
			atkResult = new AtkResults(AtkTypes.defense , null, 0) ;
 			attacker.ActivateDef() ;
		}
		
		if (attacker.actionIsPhysicalAtk() | (attacker.actionIsSpell() & !attacker.isSilent()) | attacker.actionIsDef())
		{
			if (attacker instanceof Player & attacker.getJob() == 2)
			{
				if (((Player) attacker).arrowIsEquipped()) { ((Player) attacker).spendArrow() ;}
			}
			attacker.updateCombo() ;
			attacker.resetBattleActions() ;
		}
		
		attacker.setBattleAction(atkResult.getAtkType()) ;
		
		return atkResult ;
	}	
	
	private void runTurn(LiveBeing attacker, LiveBeing receiver, int damageAnimation, Animation damageAni, DrawingOnPanel DP)
	{
		
		if (attacker.isAlive())
		{
			// UtilS.RelPos(attacker.getPos(), receiver.getPos())
			attacker.DrawTimeBar("Left", Game.colorPalette[2], DP) ;
			// TODO criatura tem que tomar blood and poison dano do player e do pet
			attacker.TakeBloodAndPoisonDamage(receiver.getBA().getBlood().TotalAtk(), receiver.getBA().getPoison().TotalAtk()) ;
			if (attacker.canAtk() & attacker.isInRange(receiver.getPos()))
			{
				if (attacker instanceof Creature) { ((Creature) attacker).fight(receiver.getCurrentAction()) ;}
//				if (attacker instanceof Pet) { ((Pet) attacker).fight() ;}

				AtkResults atkResults = Atk(attacker, receiver) ;
				if (attacker.actionIsPhysicalAtk() | attacker.actionIsSpell()) { receiver.getDisplayDamage().reset() ;}
//				if (attacker.getSettings().getSoundEffectsAreOn()) { Music.PlayMusic(hitSound) ;}

				// add player surprise atk
//				attacker.autoSpells(receiver, attacker.getSpells());

				// TODO automatically activated spells
//				player.autoSpells(creature, player.getSpells());	

				
//				if (player.getJob() == 4)
//				{
//					//SkillBuffIsActive[11][0] = false ;	// surprise attack
//				}
//				if (effect != null)
//				{
//					if (player.getJob() == 4 & Math.random() < 0.2*player.getSpells().get(11).getLevel() & effect.equals(AttackEffects.crit))	// Surprise attack
//					{
//						creature.getLife().incCurrentValue((int) -Math.min(0.5 * damage, 2*player.getPhyAtk().getBaseValue())); ;
//						// needs to show the atk animation
//						//SkillBuffIsActive[11][0] = true ;
//					}
//					player.updatedefensiveStats(damage, effect, creature.actionIsSpell(), creature) ;
//				}
				
				
				if ( atkResults.getEffect() != null | atkResults.getAtkType() == AtkTypes.defense )
				{
					if (!(attacker instanceof Creature)) { attacker.train(atkResults) ;}
					if (attacker instanceof Player) { ((Player) attacker).getStatistics().update(atkResults) ;}
				}
				damageAni.start(100, new Object[] {receiver.getPos(), receiver.getSize(), atkResults, damageAnimation}) ;
			}
			attacker.getBA().getStatus().display(attacker.getPos(), attacker.getDir(), DP);
			if (attacker.isDefending()) { attacker.displayDefending(DP) ;}			
		}
		
	}
	
	public void RunBattle(Player player, Pet pet, Creature creature, Animation[] ani, DrawingOnPanel DP)
	{
		player.incrementBattleActionCounters() ;
		if (pet != null) {pet.incrementBattleActionCounters() ;}
		creature.incrementBattleActionCounters() ;
		
		ActivateCounters(player, pet, creature) ;
		int damageStyle = player.getSettings().getDamageAnimation() ;
		LiveBeing creatureTarget = player ;
		if (pet != null) { creatureTarget = creature.chooseTarget(player.isAlive(), pet.isAlive()).equals("player") ? player : pet ;}

		runTurn(player, creature, damageStyle, ani[0], DP) ;
		if (pet != null) { runTurn(pet, creature, damageStyle, ani[1], DP) ;}
		runTurn(creature, creatureTarget, damageStyle, ani[2], DP) ;
		
		if (!battleIsOver(player, pet, creature)) { return ;}
		
		FinishBattle(player, pet, creature, ani[3]) ;
	}
	
	public static boolean battleIsOver(Player player, Pet pet, Creature creature)
	{
//		if (pet != null)
//		{
//			if (creature.isAlive() & (player.isAlive() | pet.isAlive())) { return false ;}
//		}
//		else
//		{
//			if (creature.isAlive() & player.isAlive()) { return false ;}
//		}
		
		if (creature.isAlive() & player.isAlive()) { return false ;}
		
		return true ;
	}
	
	private void FinishBattle(Player player, Pet pet, Creature creature, Animation winAni)
	{
		if (Game.getState().equals(GameStates.simulation))
		{
			PlayerEvolutionSimulation.setBattleResults(player.getLife().getCurrentValue(), creature.getLife().getCurrentValue()) ;
		}
		
		player.setState(LiveBeingStates.idle) ;
		player.resetOpponent() ;
		player.resetClosestCreature() ;
		
		for (Spell spell : player.getSpells())
		{
			spell.getEffectCounter().reset() ;
		}
		
		// deactivate survivor's instinct (animal's spell)
		if (player.getJob() == 3)	// Survivor's instinct
		{
			Spell survivorInstinct = player.getSpells().get(12) ;
			if (0 < survivorInstinct.getLevel())
			{
				for (int i = 0 ; i <= survivorInstinct.getBuffs().size() - 1 ; ++i)
				{
					// TODO survivor instinct
					//BuffsAndNerfs(player, pet, creature, survivorInstinct.getBuffs(), survivorInstinct.getLevel(), i, false, "Player", "deactivate") ;
				}
				//SkillBuffIsActive[12][0] = false ;
			}
		}
		
		if (!creature.isAlive())
		{
			if (player.isAlive())
			{
				player.win(creature, winAni) ;
			}
			player.resetAction() ;
			player.resetBattleAction() ;
			if (pet != null)
			{
				if (pet.isAlive())
				{
					//pet.Win(creature) ;
				}
				pet.resetBattleActions() ;
			}
			creature.dies() ;
			
			return ;
		}
		
		creature.getPA().getLife().setToMaximum() ;
		creature.getPA().getMp().setToMaximum() ;
		player.dies() ;
		if (pet != null) {pet.dies() ; pet.setPos(player.getPos());}
		creature.setFollow(false) ;
	}


	
	
	
//	private AtkResults PlayerSpell(Player player, Pet pet, Creature creature, Spell spell)
//	{
//		//int level = spell.getLevel() ;
//		//player.ResetSpellCooldownCounter(spellID) ;
//		if (spell.getType().equals(SpellTypes.offensive))
//		{
////			atkResult = OffensiveSkills(player, creature, spell, spellID) ;
////			int damage = (int) atkResult[0] ;
////			System.out.println("attack result = " + damage);
////			atkResult[0] = Math.max(0, damage) ;
////			creature.getLife()[0] += -UtilG.RandomMult(randomAmp) * damage ;
////			if (player.getJob() == 2 & spellID == 3)	// Double shot
////			{
////				atkResult = OffensiveSkills(player, creature, spell, spellID) ;
////				atkResult[0] = Math.max(0, damage) ;
////				creature.getLife()[0] += -UtilG.RandomMult(randomAmp) * damage ;
////			}
////			if (player.getJob() == 2 & spellID == 12)	// Knocking shot
////			{
////				knockback(creature.getPos(), 2 * creature.getStep() * spellLevel, player.getPA()) ;
////			}
////			if (player.getJob() == 3 & spellID == 5)	// Head hit
////			{
////				if (Math.random() <= 0.004*spellLevel)
////				{
////					player.getBA().receiveStatus(new int[] {(int) player.getBA().StunDuration(), 0, 0, 0, 0}) ;
////				}
////			}
////			if (player.getJob() == 4 & spellID == 3)	// Steal
////			{
////				//player.StealItem(creature, items, skilllevel) ;
////			}
////			if (player.getJob() == 4 & spellID == 12)	// Murder
////			{
////				if (Math.random() <= 0.016*spellLevel + 0.02*(player.getBA().TotalDex() - creature.getBA().TotalAgi())/(player.getBA().TotalDex() + creature.getBA().TotalAgi()))
////				{
////					creature.getLife()[0] = 0 ;
////				}
////			}
//		}
//		return new AtkResults("Spell", AttackEffects.hit, 0) ;
//	}
//	
//	private AtkResults PetSpell(Pet pet, Creature creature, int selectedSpell)
//	{
//		Spell spell = pet.getSpells().get(selectedSpell) ;
//		int damage = -1 ;
//		AttackEffects effect = null;
//		int level = pet.getSpells().get(selectedSpell).getLevel() ;
//		double BasicPhyAtk = pet.getBA().TotalPhyAtk() ;
//		double BasicMagAtk = pet.getBA().TotalMagAtk() ;
//		double BasicPhyDef = creature.getBA().TotalPhyDef() ;
//		double BasicMagDef = creature.getBA().TotalMagDef() ;
//		double BasicAtkDex = pet.getBA().TotalDex() ;
//		double BasicDefAgi = creature.getBA().TotalAgi() ;
//		double BasicAtkCrit = pet.getBA().TotalCritAtkChance() ;
//		double BasicDefCrit = creature.getBA().TotalCritDefChance() ;
//		double CreatureElemModif = 1 ;
//		if (spell.getMpCost() <= pet.getMp().getCurrentValue())
//		{
//			if (pet.getJob() == 0)
//			{
//				if (selectedSpell == 0)
//				{	
//					effect = calcEffect(BasicAtkDex, BasicDefAgi, BasicAtkCrit, BasicDefCrit, creature.getBA().getStatus().getBlock()) ;
//					damage = calcDamage(effect, BasicPhyAtk*(double)(1 + 0.02*level) + level, BasicPhyDef, new String[] {pet.getElem()[0], pet.getElem()[1], pet.getElem()[4]}, new String[] {creature.getElem()[0], creature.getElem()[0]}, CreatureElemModif, randomAmp) ;															
//				}
//			}
//			if (pet.getJob() == 1)
//			{
//				if (selectedSpell == 0)
//				{				
//					effect = calcEffect(BasicAtkDex, BasicDefAgi, BasicAtkCrit, BasicDefCrit, creature.getBA().getStatus().getBlock()) ;
//					damage = calcDamage(effect, BasicMagAtk*(double)(1 + 0.02*level) + level, BasicMagDef, new String[] {pet.getElem()[0], pet.getElem()[1], pet.getElem()[4]}, new String[] {creature.getElem()[0], creature.getElem()[0]}, CreatureElemModif, randomAmp) ;															
//				}
//			}
//			if (pet.getJob() == 2)
//			{
//				
//			}
//			if (pet.getJob() == 3)
//			{
//				
//			}
//			damage = Math.max(0, damage) ;
//			creature.getLife().incCurrentValue((int) (-UtilG.RandomMult(randomAmp) * damage)); ;		
//			pet.getMp().incCurrentValue(-spell.getMpCost()); ;	
//		}		
//		return new AtkResults ("Spell", effect, damage) ;
//	}
	
	
	
	
	

//	private void IncrementCounters()
//	{	
		/*for (int i = 0 ; i <= ShowAtkCounters.length - 1 ; i += 1)
		{
			for (int j = 0 ; j <= ShowAtkCounters[i].length - 1 ; j += 1)
			{
				if (ShowAtkCounters[i][j] < ShowAtkDurations[i][j])
				{
					ShowAtkCounters[i][j] += 1 ;
				}
			}
		}*/
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
//	}	
	

//	private void printActions(Player player, Pet pet, Creature creature)
//	{
//		System.out.println("Printing battle actions \n");
//		System.out.println("creature life: " + creature.getLife().getCurrentValue());
//		System.out.println("creature mp: " + creature.getMp().getCurrentValue());
//		System.out.println("creature action: " + creature.getCurrentAction());
//		System.out.println("creature phy def: " + creature.getPhyDef().getTotal());
//		System.out.println("creature mag def: " + creature.getMagDef().getTotal());
//		System.out.println("player life: " + player.getLife().getCurrentValue());
//		System.out.println("pet life: " + pet.getLife().getCurrentValue());
//		System.out.println("pet phy def: " + pet.getPhyDef().getTotal());
////		System.out.println("player pos: " + player.getPos());
//		System.out.println("\n\n");
//	}
	
//	private void BuffsAndNerfs(Player player, Pet pet, Creature creature, double[][] Buffs, int BuffNerfLevel, int effect, boolean SkillIsActive, String Target, String action)
//	{
//		int ActionMult = 1 ;
//		double[][] Buff = new double[14][5] ;	// [Life, Mp, PhyAtk, MagAtk, Phy def, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence][effect]
//		double[] OriginalValue = new double[14] ;	// [Life, Mp, PhyAtk, MagAtk, Phy def, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence]
//		if (action.equals("deactivate"))
//		{
//			ActionMult = -1 ;
//		}
//		if (Target.equals("Player"))
//		{
//			OriginalValue = new double[] {player.getLife().getMaxValue(), player.getMp().getMaxValue(), 
//					player.getBA().getPhyAtk().getBaseValue(),
//					player.getBA().getMagAtk().getBaseValue(), player.getBA().getPhyDef().getBaseValue(), player.getBA().getMagDef().getBaseValue(),
//					player.getBA().getDex().getBaseValue(), player.getBA().getAgi().getBaseValue(), player.getBA().getCrit()[0], player.getBA().getStun().getBasicAtkChance(),
//					player.getBA().getBlock().getBasicAtkChance(),
//					player.getBA().getBlood().getBasicAtkChance(), player.getBA().getBlood().getBasicDefChance(), player.getBA().getBlood().getBasicAtk(),
//					player.getBA().getBlood().getBasicDef(),
//					player.getBA().getPoison().getBasicAtkChance(), player.getBA().getPoison().getBasicDefChance(), player.getBA().getPoison().getBasicAtk(),
//					player.getBA().getPoison().getBasicDef(),
//					player.getBA().getSilence().getBasicAtkChance()} ;
//			//double[] battleAttValues = player.getBA().getBaseValues() ;
//		}
////		if (Target.equals("Pet"))
////		{
////			OriginalValue = new double[] {pet.getLife().getMaxValue(), pet.getMp().getMaxValue(), pet.getPhyAtk().getBaseValue(), pet.getMagAtk().getBaseValue(),
////					pet.getPhyDef().getBaseValue(), pet.getMagDef().getBaseValue(), pet.getDex().getBaseValue(), pet.getAgi().getBaseValue(),
////					pet.getCrit()[0], pet.getStun().getBasicAtkChance(), pet.getBlock().getBasicAtkChance(), pet.getBlood()[0], pet.getBlood()[2], pet.getBlood()[4], pet.getBlood()[6],
////					pet.getPoison()[0], pet.getPoison()[2], pet.getPoison()[4], pet.getPoison()[6], pet.getSilence().getBasicAtkChance()} ;
////			//double[] battleAttValues = pet.getBA().getBaseValues() ;
////		}
////		if (Target.equals("Creature"))
////		{
////			OriginalValue = new double[] {creature.getLife().getMaxValue(), creature.getMp().getMaxValue(), creature.getPhyAtk().getBaseValue(),
////					creature.getMagAtk().getBaseValue(), creature.getPhyDef().getBaseValue(), creature.getMagDef().getBaseValue(),
////					creature.getDex().getBaseValue(), creature.getAgi().getBaseValue(), creature.getCrit()[0], creature.getStun().getBasicAtkChance(),
////					creature.getBlock().getBasicAtkChance(), creature.getBlood()[0], creature.getBlood()[2], creature.getBlood()[4], creature.getBlood()[6],
////					creature.getPoison()[0], creature.getPoison()[2], creature.getPoison()[4], creature.getPoison()[6], creature.getSilence().getBasicAtkChance()} ;
////			//double[] battleAttValues = creature.getBA().getBaseValues() ;
////		}
//		if (effect == 11 | effect == 12)
//		{
//			if (action.equals("deactivate"))
//			{
//				Buff[effect][0] += (OriginalValue[effect]*Buffs[effect][0] + Buffs[effect][1])*BuffNerfLevel*ActionMult ;
//				Buff[effect][1] += (OriginalValue[effect]*Buffs[effect][3] + Buffs[effect][4])*BuffNerfLevel*ActionMult ;
//				Buff[effect][2] += (OriginalValue[effect]*Buffs[effect][6] + Buffs[effect][7])*BuffNerfLevel*ActionMult ;
//				Buff[effect][3] += (OriginalValue[effect]*Buffs[effect][9] + Buffs[effect][10])*BuffNerfLevel*ActionMult ;
//				Buff[effect][4] += Buffs[effect][12]*ActionMult ;
//			}
//			else
//			{
//				if (Math.random() <= Buffs[effect][2])
//				{
//					Buff[effect][1] += (OriginalValue[effect]*Buffs[effect][0] + Buffs[effect][1])*BuffNerfLevel*ActionMult ;
//				}
//				if (Math.random() <= Buffs[effect][5])
//				{
//					Buff[effect][1] += (OriginalValue[effect]*Buffs[effect][3] + Buffs[effect][4])*BuffNerfLevel*ActionMult ;
//				}
//				if (Math.random() <= Buffs[effect][8])
//				{
//					Buff[effect][2] += (OriginalValue[effect]*Buffs[effect][6] + Buffs[effect][7])*BuffNerfLevel*ActionMult ;
//				}
//				if (Math.random() <= Buffs[effect][11])
//				{
//					Buff[effect][3] += (OriginalValue[effect]*Buffs[effect][9] + Buffs[effect][10])*BuffNerfLevel*ActionMult ;
//				}
//				Buff[effect][4] += Buffs[effect][12]*ActionMult ;
//			}
//		}
//		else if (action.equals("deactivate") | Math.random() <= Buffs[effect][2])
//		{
//			Buff[effect][0] += (OriginalValue[effect]*Buffs[effect][0] + Buffs[effect][1])*BuffNerfLevel*ActionMult ;
//		}
//		if (Target.equals("Player") & !SkillIsActive)
//		{
//			player.getLife().incCurrentValue((int) Buff[0][0]); ;
//			player.getMp().incCurrentValue((int) Buff[1][0]); ;
//			player.getPhyAtk().incBonus(Buff[2][0]) ;
//			player.getMagAtk().incBonus(Buff[3][0]) ;
//			player.getPhyDef().incBonus(Buff[4][0]) ;
//			player.getMagDef().incBonus(Buff[5][0]) ;
//			player.getDex().incBonus(Buff[6][0]) ;
//			player.getAgi().incBonus(Buff[7][0]) ;
//			player.getCrit()[1] += Buff[8][0] ;
//			player.getStun().incAtkChanceBonus(Buff[9][0]) ;
//			player.getBlock().incAtkChanceBonus(Buff[10][0]) ;
//			player.getBlood().incAtkChanceBonus(Buff[11][0]) ;
//			player.getBlood().incDefChanceBonus(Buff[11][1]) ;
//			player.getBlood().incAtkBonus(Buff[11][2]) ;
//			player.getBlood().incDefBonus(Buff[11][3]) ;
//			player.getBlood().incDuration(Buff[11][4]) ;
//			player.getPoison().incAtkChanceBonus(Buff[12][0]) ;
//			player.getPoison().incDefChanceBonus(Buff[12][1]) ;
//			player.getPoison().incAtkBonus(Buff[12][2]) ;
//			player.getPoison().incDefBonus(Buff[12][3]) ;
//			player.getPoison().incDuration(Buff[12][4]) ;
//			player.getSilence().incAtkChanceBonus(Buff[13][0]) ;
//		}
////		if (Target.equals("Pet") & !SkillIsActive)
////		{
////			pet.getLife().incCurrentValue((int) Buff[0][0]); ;
////			pet.getMp().incCurrentValue((int) Buff[1][0]); ;
////			pet.getPhyAtk().incBonus(Buff[2][0]) ;
////			pet.getMagAtk().incBonus(Buff[3][0]) ;
////			pet.getPhyDef().incBonus(Buff[4][0]) ;
////			pet.getMagDef().incBonus(Buff[5][0]) ;
////			pet.getDex().incBonus(Buff[6][0]) ;
////			pet.getAgi().incBonus(Buff[7][0]) ;
////			pet.getCrit()[1] += Buff[8][0] ;
////			pet.getStun().incAtkChanceBonus(Buff[9][0]) ;
////			pet.getBlock().incAtkChanceBonus(Buff[10][0]) ;
////			pet.getBlood()[1] += Buff[11][0] ;
////			pet.getBlood()[3] += Buff[11][1] ;
////			pet.getBlood()[5] += Buff[11][2] ;
////			pet.getBlood()[7] += Buff[11][3] ;
////			pet.getBlood()[8] += Buff[11][4] ;
////			pet.getPoison()[1] += Buff[12][0] ;
////			pet.getPoison()[3] += Buff[12][1] ;
////			pet.getPoison()[5] += Buff[12][2] ;
////			pet.getPoison()[7] += Buff[12][3] ;
////			pet.getPoison()[8] += Buff[12][4] ;
////			pet.getSilence().incAtkChanceBonus(Buff[13][0]) ;
////		}
////		if (Target.equals("Creature") & !SkillIsActive)
////		{
////			creature.getLife().incCurrentValue((int) -Buff[0][0]); ;
////			creature.getMp().incCurrentValue((int) -Buff[1][0]); ;
////			creature.getPhyAtk().incBonus(-Buff[2][0]) ;
////			creature.getMagAtk().incBonus(-Buff[3][0]) ;
////			creature.getPhyDef().incBonus(-Buff[4][0]) ;
////			creature.getMagDef().incBonus(-Buff[5][0]) ;
////			creature.getDex().incBonus(-Buff[6][0]) ;
////			creature.getAgi().incBonus(-Buff[7][0]) ;
////			creature.getCrit()[1] += -Buff[8][0] ;
////			creature.getStun().incAtkChanceBonus(-Buff[9][0]) ;
////			creature.getBlock().incAtkChanceBonus(-Buff[10][0]) ;
////			creature.getBlood()[1] += -Buff[11][0] ;
////			creature.getBlood()[3] += -Buff[11][1] ;
////			creature.getBlood()[5] += -Buff[11][2] ;
////			creature.getBlood()[7] += -Buff[11][3] ;
////			creature.getBlood()[8] += -Buff[11][4] ;
////			creature.getPoison()[1] += -Buff[12][0] ;
////			creature.getPoison()[3] += -Buff[12][1] ;
////			creature.getPoison()[5] += -Buff[12][2] ;
////			creature.getPoison()[7] += -Buff[12][3] ;
////			creature.getPoison()[8] += -Buff[12][4] ;
////			creature.getSilence().incAtkChanceBonus(-Buff[13][0]) ;
////		}
//	}

	// TODO item effect in battle check elem mult
//	private void ItemEffectInBattle(BattleAttributes PlayerBA, BattleAttributes PetBA, BattleAttributes creatureBA, Elements[] creatureElem, double[] creatureLife, int ItemID, String target, Elements Elem, double[][] Effects, double[][] Buffs, String action)
//	{
//		double elemMult = calcElemMult(Elem, Elements.neutral, creatureElem[0], creatureElem[0], Elements.neutral) ;
//		creatureLife[0] += -Math.max(0, Effects[0][1])*elemMult*UtilG.RandomMult(randomAmp) ;
//		if (target.equals("Player"))
//		{
////			//PlayerBA.getBattleActions()[0][1] += -Effects[1][1] ;	// Atk speed
////			PlayerBA.getSpecialStatus()[0] += Effects[2][2] ;	// Stun
////			PlayerBA.getSpecialStatus()[2] += Effects[3][2] ;	// Blood
////			PlayerBA.getSpecialStatus()[3] += Effects[4][2] ;	// Poison
////			PlayerBA.getSpecialStatus()[4] += Effects[5][2] ;	// Silence
////			PlayerBA.getSpecialStatus()[5] += Effects[6][2] ;	// Drunk
////		}
////		if (target.equals("Pet"))
////		{
////			//PetBA.getBattleActions()[0][1] += -Effects[1][1] ;	// Atk speed
////			PetBA.getSpecialStatus()[0] += Effects[2][2] ;	// Stun
////			PetBA.getSpecialStatus()[2] += Effects[3][2] ;	// Blood
////			PetBA.getSpecialStatus()[3] += Effects[4][2] ;	// Poison
////			PetBA.getSpecialStatus()[4] += Effects[5][2] ;	// Silence
////		}
////		if (target.equals("Creature"))
////		{
////			//creatureBA.getBattleActions()[0][1] += -Effects[1][1] ;	// Atk speed
////			creatureBA.getSpecialStatus()[0] += Effects[2][2] ;	// Stun
////			creatureBA.getSpecialStatus()[2] += Effects[3][2] ;	// Blood
////			//PlayerBloodItemBonus = Effects[3][1] ;		// Blood atk
////			creatureBA.getSpecialStatus()[3] += Effects[4][2] ;	// Poison
////			//PlayerPoisonItemBonus = Effects[4][1] ;		// Poison atk
////			creatureBA.getSpecialStatus()[4] += Effects[5][2] ;	// Silence
//		}	
////		for (int i = 0 ; i <= items[0].getBuffs() - 1 ; i += 1)
////		{
//			//BuffsAndNerfs(player, pet, creature, Buffs, 1, i, false, target, action) ;	
//			//ItemEffectIsActive[ItemID][i] = true ;
////		}
//	}
	
//	private void OffensiveSkillsStatus(Player player, Creature creature, Spell skills)
//	{
//		BattleAttributes playerBA = player.getBA();		// Battle attributes of the player
//		BattleAttributes creatureBA = creature.getBA();	// Battle attributes of the creature
//		
//		double[] StunMod = skills.getStunMod() ;
//		double[] BlockMod = skills.getBlockMod() ;
//		double[] BloodMod = skills.getBloodMod() ;
//		double[] PoisonMod = skills.getPoisonMod() ;
//		double[] SilenceMod = skills.getSilenceMod() ;
//		BattleSpecialAttribute Stun = new BattleSpecialAttribute(playerBA.getStun().TotalAtkChance() + StunMod[0], 0.0, creatureBA.getStun().TotalDefChance() + StunMod[1], 0.0, playerBA.getStun().getDuration() + (int)StunMod[2]) ;
//		BattleSpecialAttribute Block = new BattleSpecialAttribute(playerBA.getBlock().TotalAtkChance() + BlockMod[0], 0.0, creatureBA.getBlock().TotalDefChance() + BlockMod[1], 0.0, playerBA.getBlock().getDuration() + (int)BlockMod[2]) ;
//		double[] Blood = new double[] {playerBA.getBlood().TotalAtkChance() + BloodMod[0], creatureBA.getBlood().TotalDefChance() + BloodMod[1], playerBA.getBlood().getDuration() + BloodMod[2]} ;
//		double[] Poison = new double[] {playerBA.getPoison().TotalAtkChance() + PoisonMod[0], creatureBA.getPoison().TotalDefChance() + PoisonMod[1], playerBA.getPoison().getDuration() + PoisonMod[2]} ;
//		BattleSpecialAttribute Silence = new BattleSpecialAttribute(playerBA.getSilence().TotalAtkChance() + SilenceMod[0], 0.0, creatureBA.getSilence().TotalDefChance() + SilenceMod[1], 0.0, playerBA.getSilence().getDuration() + (int)SilenceMod[2]) ;
////		int[] SkillStatus = CalcStatus(Stun, Block, Blood, Poison, Silence) ;
////		creatureBA.receiveStatus(SkillStatus) ;
//	}
//	
//	private AtkResults OffensiveSkills(Player player, Creature creature, Spell skills, int spellID)
//	{
//		int skilllevel = player.getSpells().get(spellID).getLevel() ;
//		int damage = -1 ;
//		AttackEffects effect ;
//		double PhyAtk = player.getBA().TotalPhyAtk() ;
//		double MagAtk = player.getBA().TotalMagAtk() ;
//		double PhyDef = creature.getBA().TotalPhyDef() ;
//		double MagDef = creature.getBA().TotalMagDef() ;
//		double AtkDex = player.getBA().TotalDex() ;
//		double DefAgi = creature.getBA().TotalAgi() ;
//		double AtkCrit = player.getBA().TotalCritAtkChance() ;
//		double DefCrit = creature.getBA().TotalCritDefChance() ;
//		double CreatureElemMod = 1 ;
//		double ArrowAtk = 0 ;
//		double[] AtkMod = new double[] {skills.getAtkMod()[0] * skilllevel, 1 + skills.getAtkMod()[1] * skilllevel} ;	// Atk modifier
//		double[] DefMod = new double[] {skills.getDefMod()[0] * skilllevel, 1 + skills.getDefMod()[1] * skilllevel} ;	// Def modifier
//		double[] DexMod = new double[] {skills.getDexMod()[0] * skilllevel, 1 + skills.getDexMod()[1] * skilllevel} ;	// Dex modifier
//		double[] AgiMod = new double[] {skills.getAgiMod()[0] * skilllevel, 1 + skills.getAgiMod()[1] * skilllevel} ;	// Agi modifier
//		double AtkCritMod = skills.getAtkCritMod()[0] * skilllevel ;	// Critical atk modifier
//		double DefCritMod = skills.getDefCritMod()[0] * skilllevel ;	// Critical def modifier
//		double BlockDef = creature.getBA().getStatus().getBlock() ;
//		double BasicAtk = 0 ;
//		double BasicDef = 0 ;
//		String[] AtkElem = new String[] {skills.getElem(), player.getElem()[1], player.getElem()[4]} ;
//		String[] DefElem = new String[] {creature.getElem()[0], creature.getElem()[0]} ;
//
//		if (player.getJob() == 2 & player.arrowIsEquipped())	// If Arrow is equipped
//		{
//			ArrowAtk = Items.ArrowPower[player.getEquips()[3].getId() - Items.BagIDs[4]][0] ;
////			UtilS.PrintBattleActions(4, "Player", "creature", -1, 0, creature.getBA().getSpecialStatus(), player.getElem()) ;
//		}
//		if (player.getJob() == 0)
//		{
//			BasicAtk = PhyAtk ;
//			BasicDef = PhyDef ;
//		}
//		if (player.getJob() == 1)
//		{
//			BasicAtk = MagAtk ;
//			BasicDef = MagDef ;
//		}
//		if (player.getJob() == 2)
//		{
//			if (spellID == 0 | spellID == 3 | spellID == 6 | spellID == 9 | spellID == 12)
//			{
//				BasicAtk = PhyAtk + ArrowAtk ;
//				BasicDef = PhyDef ;
//			}
//			if (spellID == 2 | spellID == 5 | spellID == 11)
//			{
//				BasicAtk = (double) ((PhyAtk + MagAtk) / 2.0 + ArrowAtk) ;
//				BasicDef = (double) ((PhyDef + MagDef) / 2.0) ;
//			}
//			if (spellID == 14)
//			{
//				BasicAtk = MagAtk + ArrowAtk ;
//				BasicDef = MagDef ;
//			}
//		}
//		if (player.getJob() == 3)
//		{
//			BasicAtk = PhyAtk ;
//			BasicDef = PhyDef ;
//		}
//		if (player.getJob() == 4)
//		{
//			BasicAtk = PhyAtk ;
//			BasicDef = PhyDef ;
//		}
//		System.out.println() ;
//		System.out.println("Skill Atk = " + UtilG.Round((AtkMod[0] + BasicAtk*AtkMod[1]), 1) + " = " + AtkMod[0] + " + " + UtilG.Round(BasicAtk, 1) + " * " + AtkMod[1]) ;
//		System.out.println("Skill Def = " + UtilG.Round((DefMod[0] + BasicDef*DefMod[1]), 1) + " = " + DefMod[0] + " + " + UtilG.Round(BasicDef, 1) + " * " + DefMod[1]) ;
//		System.out.println("Skill Dex atk = " + UtilG.Round((DexMod[0] + AtkDex*DexMod[1]), 1) + " = " + DexMod[0] + " + " + UtilG.Round(AtkDex, 1) + " * " + DexMod[1]) ;
//		System.out.println("Skill Agi def = " + UtilG.Round((AgiMod[0] + DefAgi*AgiMod[1]), 1) + " = " + AgiMod[0] + " + " + UtilG.Round(DefAgi, 1) + " * " + AgiMod[1]) ;
//		System.out.println("Skill Crit atk = " + UtilG.Round((AtkCrit + AtkCritMod), 1) + " = " + UtilG.Round(AtkCrit, 1) + " + " + AtkCritMod) ;
//		System.out.println("Skill Crit def = " + UtilG.Round((DefCrit + DefCritMod), 1) + " = " + UtilG.Round(DefCrit, 1) + " + " + DefCritMod) ;
//		System.out.println("Skill Atk elem = " + Arrays.toString(AtkElem)) ;
//		System.out.println("Skill Def elem = " + Arrays.toString(DefElem)) ;
//		System.out.println("Skill Creature elem mod = " + CreatureElemMod) ;
//		System.out.println("Skill Block def = " + BlockDef) ;
//		effect = calcEffect(DexMod[0] + AtkDex*DexMod[1], AgiMod[0] + DefAgi*AgiMod[1], AtkCrit + AtkCritMod, DefCrit + DefCritMod, BlockDef) ;
//		damage = calcDamage(effect, AtkMod[0] + BasicAtk*AtkMod[1], DefMod[0] + BasicDef*DefMod[1], AtkElem, DefElem, CreatureElemMod, randomAmp) ;
//		return new AtkResults ("Spell", effect, damage) ;
//	}
//	

//	
//	
//	private AtkResults PlayerAtk(Player player, Pet pet, Creature creature)
//	{
//		int damage = -1 ;
//		AttackEffects effect = null;
//		String atkType = "none";
//		
//		if (player.actionIsAtk())
//		{
//			atkType = "Physical" 
//			double arrowPower = 0 ;
//			if (player.getJob() == 2 & player.arrowIsEquipped())
//			{
//				arrowPower = Items.ArrowPower[player.getEquips()[3].getId() - Items.BagIDs[4]][0] ;
//				player.SpendArrow() ;
//			}
//			effect = calcEffect(player.getBA().TotalDex(), creature.getBA().TotalAgi(), player.getBA().TotalCritAtkChance(), creature.getBA().TotalCritDefChance(), creature.getBA().getStatus().getBlock()) ;
////			damage = calcDamage(effect, player.getBA().TotalPhyAtk() + arrowPower, creature.getBA().TotalPhyDef(), player.getAtkElems(), creature.getDefElems(), 1, randomAmp) ;
//			if (effect.equals(AttackEffects.hit))
//			{
//				creature.getPA().getLife().incCurrentValue(-damage) ;
//				int[] inflictedStatus = CalcStatus(player.getBA().baseAtkChances(), creature.getBA().baseDefChances(), player.getBA().baseDurations()) ;				
//				creature.getBA().getStatus().receiveStatus(inflictedStatus) ;
//			}
//			player.ResetBattleActions() ;
//		}
//		else if (player.actionIsSpell())
//		{
//			atkType = "Spell" ;
//			if (!player.isSilent() & player.hasTheSpell(player.getCurrentAction()))
//			{
//				Spell spell = player.getSpells().get(Integer.parseInt(player.getCurrentAction())) ;				
//				if (spell.isReady() & player.hasEnoughMP(spell))
//				{
//					AtkResults atkResult = PlayerSpell(player, pet, creature, spell) ;
//					damage = (int) atkResult.getDamage() ;
//					effect = (AttackEffects) atkResult.getEffect() ;
//				}
//			}
//			player.ResetBattleActions() ;
//		}
//		else if (player.actionIsDef())
//		{
//			atkType = "Defense" ;
// 			player.ActivateDef() ;
//			player.ResetBattleActions() ;
//		}
//
//		return new AtkResults(atkType, effect, damage) ;
//	}
//		
//	private AtkResults PetAtk(Pet pet, Creature creature, DrawingOnPanel DP)
//	{
//		int damage = -1 ;
//		AttackEffects effect = null ;
//		String atkType = "none";
////		int[] inflictedStatus = new int[5] ;	// [Stun, block, blood, poison, silence]
////		BattleAttributes creatureBA = creature.getBA(), petBA = pet.getBA() ;
//		
//		if (!pet.getBA().isStun())	// If not stun
//		{
//			//petBA.setCurrentAction(pet.action) ;
//			if (pet.actionIsAtk())	// Physical atk
//			{
//				atkType = "Physical" ;
//				effect = calcEffect(pet.getBA().TotalDex(), creature.getBA().TotalAgi(), pet.getBA().TotalCritAtkChance(), creature.getBA().TotalCritDefChance(), creature.getBA().getStatus().getBlock()) ;
////				damage = calcDamage(effect, pet.getBA().TotalPhyAtk(), creature.getBA().TotalPhyDef(), new String[] {pet.getElem()[0], pet.getElem()[1], pet.getElem()[4]}, creature.getDefElems(), 1, randomAmp) ;
//				//int[] AtkResult = CalcAtk(petBA.TotalPhyAtk(), creatureBA.TotalPhyDef(), petBA.TotalDex(), creatureBA.TotalAgi(), petBA.TotalCritAtkChance(), creatureBA.TotalCritDefChance(), new String[] {pet.getElem()[0], pet.getElem()[1], pet.getElem()[4]}, new String[] {creature.getElem()[0], creature.getElem()[0]}, CreatureElemModif, creatureBA.getSpecialStatus()[1]) ;
//				//damage = AtkResult[0] ;
//				//effect = AtkResult[1] ;
//				if (effect.equals(AttackEffects.hit))
//				{
//					creature.getLife().incCurrentValue(-damage) ;
////					int[] inflictedStatus = CalcStatus(new double[] {pet.getBA().getStun().TotalAtkChance(), creature.getBA().getStun().TotalDefChance(), pet.getBA().getStun().getDuration()},
////							new double[] {pet.getBA().getBlock().TotalAtkChance(), creature.getBA().getBlock().TotalDefChance(), pet.getBA().getBlock().getDuration()},
////							new double[] {pet.getBA().TotalBloodAtkChance(), creature.getBA().TotalBloodDefChance(), pet.getBA().BloodDuration()},
////							new double[] {pet.getBA().TotalPoisonAtkChance(), creature.getBA().TotalPoisonDefChance(), pet.getBA().PoisonDuration()},
////							new double[] {pet.getBA().getSilence().TotalAtkChance(), creature.getBA().getSilence().TotalDefChance(), pet.getBA().getSilence().getDuration()}) ;
////					creature.getBA().receiveStatus(inflictedStatus) ;
//				}
//				//Show[1][0] = true ;
//				//ShowAtkCounters[1][0] = 0 ;
//				pet.ResetBattleActions() ;
//			}
//			else if (pet.actionIsSpell() & !pet.isSilent())	// magical atk
//			{
//				atkType = "Spell" ;
//				if (block(creature.getBA().getStatus().getBlock()))
//				{
//					effect = AttackEffects.block ;
//				}
//				else
//				{
//					/*int SkillID = Integer.parseInt(pet.action) ;
//					if (SkillIsReady[UtilG.IndexOf(Player.SpellKeys, pet.action)] & pet.getSpells()[SkillID].getMpCost() <= pet.getMp()[0])
//					{
//						Show[1][0] = true ;
//						ShowAtkCounters[1][0] = 0 ;
//						damage = (int) PetSpell(pet, creature, Integer.parseInt(pet.action))[0] ;
//						pet.ResetBattleActions() ;
//						SkillIsReady[UtilG.IndexOf(Player.SpellKeys, pet.action)] = false ;
//						pet.getMagAtk()[2] += Train(pet.getMagAtk()[2]) ;
//					}*/
//				}
//				pet.ResetBattleActions() ;
//			}
//			else if (pet.actionIsDef())
//			{
//				atkType = "Defense" ;
//	 			pet.ActivateDef() ;
//	 			pet.ResetBattleActions() ;
//			}
//		}
//		return new AtkResults(atkType, effect, damage) ;
//	}	
//	
//	private AtkResults CreatureAtk(Player player, Pet pet, Creature creature)
//	{
//		int damage = -1 ;
//		AttackEffects effect = null ;
//		String atkType = "none";
//		int[] Status = new int[5] ;	// [Stun, block, blood, poison, silence]
//		int MpCost = 10 ;
//		String creatureAction = creature.getCurrentAction() ;
//		BattleAttributes creatureBA = creature.getBA(), playerBA = player.getBA(), petBA = pet.getBA() ;
//		
//		creature.fight() ;
//		creature.UpdateCombo() ;
//		int target = 0 ;
//		if (pet.isAlive())
//		{
//			target = (int)(2*Math.random() - 0.01) ;
//		}
//		if (creature.actionIsAtk() | creature.actionIsSpell())
//		{
//			if (target == 0)	// Creature atks player
//			{
//				if (creature.actionIsAtk())
//				{
//					atkType = "Physical" ;
//					effect = calcEffect(creatureBA.TotalDex(), playerBA.TotalAgi(), creatureBA.TotalCritAtkChance(), playerBA.TotalCritDefChance(), player.getBlock().getBasicAtkChanceBonus()) ;
//					damage = calcDamage(effect, creatureBA.TotalPhyAtk(), playerBA.TotalPhyDef(), new String[] {creature.getElem()[0], "n", "n"}, new String[] {player.getElem()[2], player.getElem()[3]}, 1, randomAmp) ; //player.getElemMult()[UtilS.ElementID(creature.getElem()[0])]) ;
//				}
//				else if (creature.actionIsSpell() & !creature.isSilent())
//				{	
//					atkType = "Spell" ;
////					int spellID = UtilG.IndexOf(Player.SpellKeys, creatureAction) ;
////					if (creature.hasEnoughMP(spellID))
////					{
////						damage = creature.useSpell(spellID, player) ;
////						creature.ResetBattleActions() ;
////					}
//				}
//				if (effect != null)
//				{
//					if (effect.equals(AttackEffects.hit))
//					{
//						player.getLife().incCurrentValue(-damage); ;
////						Status = CalcStatus(new double[] {creatureBA.getStun().TotalAtkChance(), playerBA.getStun().TotalDefChance(), creatureBA.getStun().getDuration()},
////								new double[] {creatureBA.getBlock().TotalAtkChance(), playerBA.getBlock().TotalDefChance(), creatureBA.getBlock().getDuration()},
////								new double[] {creatureBA.TotalBloodAtkChance(), playerBA.TotalBloodDefChance(), creatureBA.BloodDuration()},
////								new double[] {creatureBA.TotalPoisonAtkChance(), playerBA.TotalPoisonDefChance(), creatureBA.PoisonDuration()},
////								new double[] {creatureBA.getSilence().TotalAtkChance(), playerBA.getSilence().TotalDefChance(), creatureBA.getSilence().getDuration()}) ;
////						playerBA.receiveStatus(Status) ;
//					}
//				}
//			}
//			else if (target == 1)	// Creature atks pet
//			{
//				if (creature.actionIsAtk())
//				{
//					atkType = "Physical" ;
//					effect = calcEffect(creatureBA.TotalDex(), petBA.TotalAgi(), creatureBA.TotalCritAtkChance(), petBA.TotalCritDefChance(), pet.getBlock().getBasicAtkChanceBonus()) ;
//					damage = calcDamage(effect, creatureBA.TotalPhyAtk(), petBA.TotalPhyDef(), new String[] {creature.getElem()[0], "n", "n"}, new String[] {pet.getElem()[2], pet.getElem()[3]}, pet.getElemMult()[UtilS.ElementID(creature.getElem()[0])], randomAmp) ;
//				}
//				else if (creature.actionIsSpell() & !creature.isSilent())
//				{	
////					atkType = "Spell" ;
////					int spellID = UtilG.IndexOf(Player.SpellKeys, creatureAction) ;
////					if (creature.hasEnoughMP(spellID))
////					{
////						creature.ResetBattleActions() ;
////					}
//				}
//				if (effect != null)
//				{
//					if (effect.equals(AttackEffects.hit))
//					{
//						pet.getLife().incCurrentValue(-damage); ;
////						Status = CalcStatus(new double[] {creatureBA.getStun().TotalAtkChance(), petBA.getStun().TotalDefChance(), creatureBA.getStun().getDuration()},
////								new double[] {creatureBA.getBlock().TotalAtkChance(), petBA.getBlock().TotalDefChance(), creatureBA.getBlock().getDuration()},
////								new double[] {creatureBA.TotalBloodAtkChance(), petBA.TotalBloodDefChance(), creatureBA.BloodDuration()},
////								new double[] {creatureBA.TotalPoisonAtkChance(), petBA.TotalPoisonDefChance(), creatureBA.PoisonDuration()},
////								new double[] {creatureBA.getSilence().TotalAtkChance(), petBA.getSilence().TotalDefChance(), creatureBA.getSilence().getDuration()}) ;
////						petBA.receiveStatus(Status) ;
//					}
//				}
//			}
//		}
//		else if (creature.getCurrentAction().equals(Player.ActionKeys[3]))	// Creature defends
//		{
//			atkType = "Defense" ;
// 			creature.ActivateDef() ;
//		}
//		if (creature.getCurrentAction().equals(Player.ActionKeys[1]) | creature.getCurrentAction().equals(Player.ActionKeys[3]))
//		{
//			creature.ResetBattleActions() ;
//		}
//		
//		return new AtkResults(atkType, effect, damage) ;
//	}
//	
	
//	private void AtkAnimations(Point AttackerPos, Point TargetPos, Dimension AttackerSize, Dimension TargetSize, boolean UsedSkill, Object[] PlayerAtkResult, int DamageAnimation,
//			boolean[] Show, int[] Durations, List<Spell> spells, List<Spell> activePlayerSpells, String PlayerMove)
//	{
//		if (0 <= (int)PlayerAtkResult[0])
//		{
//			Ani.SetAniVars(1, new Object[] {Durations[0], TargetPos, TargetSize, PlayerAtkResult, DamageAnimation}) ;	// Damage animation
//			Ani.StartAni(1) ;
//		}
//		if (Show[1])	// Phy atk animation
//		{
//			
//		}
//		Ani.SetAniVars(2, new Object[] {Durations[1], AttackerPos, TargetPos, TargetSize, DamageAnimation}) ;
//		Ani.StartAni(2) ;
//		if ( UsedSkill)	// Mag atk animation
//		{
//			Ani.SetAniVars(3, new Object[] {Durations[2], Show[3], AttackerPos, TargetPos, AttackerSize, TargetSize, activePlayerSpells.get(UtilG.IndexOf(Player.SpellKeys, PlayerMove))}) ;
//			Ani.StartAni(3) ;
//		}
//		if (Show[4])	// Arrow atk animation
//		{
//		}
//		Ani.SetAniVars(4, new Object[] {Durations[0], AttackerPos, TargetPos, TargetSize}) ;
//		Ani.StartAni(4) ;
//	}

	/*public void StatusAnimations(int AniID, int[] AttackerPos, int[] TargetPos, int[] AttackerSize, int Duration, int[] SpecialStatus, boolean isDefending)
	{
		String RelPos = Uts.RelPos(AttackerPos, TargetPos) ;
		if (isDefending)	// Status animation
		{
			Ani.SetAniVars(AniID, new Object[] {Duration, AttackerPos, AttackerSize, RelPos, BattleIconImages, SpecialStatus, isDefending}) ;
			Ani.StartAni(AniID) ;
		}
	}*/
	
}
