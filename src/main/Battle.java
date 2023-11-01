package main ;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Clip;

import graphics.Animation;
import graphics.DrawingOnPanel;
import liveBeings.Creature;
import liveBeings.LiveBeing;
import liveBeings.LiveBeingStates;
import liveBeings.Pet;
import liveBeings.Player;
import liveBeings.Spell;
import simulations.PlayerEvolutionSimulation;
import utilities.Align;
import utilities.AttackEffects;
import utilities.Elements;
import utilities.GameStates;
import utilities.UtilG;

public class Battle 
{
	private static double randomAmp ;
	protected static List<Elements> ElemID ;
	protected static double[][] ElemMult ;
	
	public static final Clip hitSound ;
	

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
		hitSound = Music.musicFileToClip(new File(Game.MusicPath + "16-Hit.wav").getAbsoluteFile()) ;
	}
	
	public Battle()
	{	
		//this.SoundEffects = SoundEffects ;
//		hitSound = Music.musicFileToClip(new File(Game.MusicPath + "16-Hit.wav").getAbsoluteFile()) ;

	}

	public static void PrintStart()
	{
//		System.out.println();
//		System.out.println();
//		System.out.println("  Battle started!  ");
	}
	
	private static void PrintTurn(LiveBeing attacker, LiveBeing receiver)
	{
//		System.out.println();
//		System.out.println(attacker.getName() + " acts!");
	}
	
	private static void PrintAtkResults(AtkResults atkResults)
	{
//		System.out.println(atkResults);
	}
	
	private static void PrintReceiverCondition(LiveBeing receiver)
	{
//		System.out.println(receiver.getName() + " action = " + receiver.getCurrentAction()) ;
//		System.out.println(receiver.getName() + " life = " + receiver.getPA().getLife().getTotalValue()) ;
//		System.out.println(receiver.getName() + " " + receiver.getPA()) ;
//		System.out.println(receiver.getName() + " " + receiver.getBA().getStatus()) ;
	}
	
	private static void PrintElement(Elements atkElem, Elements weaponElem, Elements armorElem, Elements shieldElem, Elements superElem, double elemMult)
	{
//		System.out.println("Elements: atk = " + atkElem + " | weapon = " + weaponElem + " | armor = " + armorElem + " | def = " + shieldElem + " | super = " + superElem + " | mult = " + elemMult) ;
	}
	
	private static double basicElemMult(Elements atk, Elements def)
	{
		return ElemMult[ElemID.indexOf(atk)][ElemID.indexOf(def)] ;
	}
	
	private static boolean hit(double dex, double agi)
	{
		double hitChance = 1 - 1 / (1 + Math.pow(1.1, dex - agi)) ;
		return UtilG.chance(hitChance) ;
	}
	
	private static boolean block(double blockDef)
	{
		return UtilG.chance(blockDef) ;
	}

	private static boolean criticalAtk(double critAtk, double critDef)
	{
		return UtilG.chance(critAtk - critDef) ;
	}
		
	private static double calcElemMult(Elements atk, Elements weapon, Elements armor, Elements shield, Elements superElem)
	{
		double mult = 1 ;
		mult = basicElemMult(atk, armor) * mult ;
		mult = basicElemMult(atk, shield) * mult ;
		mult = basicElemMult(weapon, armor) * mult ;
		mult = basicElemMult(weapon, shield) * mult ;
		mult = basicElemMult(superElem, armor) * mult ;
		mult = basicElemMult(superElem, shield) * mult ;
		return mult ;
	}

	private static AtkResults calcPhysicalAtk(LiveBeing attacker, LiveBeing receiver)
	{
		double arrowPower = 0 ;
		if (attacker.actionIsArrowAtk())
		{
			arrowPower += ((Player) attacker).getEquippedArrow().getAtkPower() ;
		}
		
		AttackEffects effect = calcEffect(attacker.getBA().TotalDex(), receiver.getBA().TotalAgi(), attacker.getBA().TotalCritAtkChance(), receiver.getBA().TotalCritDefChance(), receiver.getBA().getStatus().getBlock()) ;
		int damage = calcDamage(effect, attacker.getBA().TotalPhyAtk() + arrowPower, receiver.getBA().TotalPhyDef(), attacker.atkElems(), receiver.defElems(), 1) ;

		return new AtkResults(AtkTypes.physical, effect, damage) ;
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
	
	public static int calcDamage(AttackEffects effect, double atk, double def, Elements[] atkElems, Elements[] defElem, double elemModifier)
	{
		int damage = -1 ;
		
		switch(effect)
		{
			case miss: case block:
				damage = 0 ;
				break ;
				
			case hit:
				damage = Math.max(0, (int)(atk - def)) ;
				break ;
			
			case crit:
				damage = (int) atk ;
				break ;
			
			default: return 0 ;
		}
		
		double randomMult = UtilG.RandomMult(randomAmp) ;
		double elemMult = calcElemMult(atkElems[0], atkElems[1], defElem[0], defElem[0], atkElems[2]) ;
		PrintElement(atkElems[0], atkElems[1], defElem[0], defElem[0], atkElems[2], elemMult) ;
		damage = (int) (randomMult * elemMult * elemModifier * damage) ;
		return damage ;
	}
		
	private static int[] calcStatus(double[] atkChances, double[] defChances, int[] durations)
	{
		int[] status = new int[atkChances.length] ;
		
		for (int i = 0; i <= atkChances.length - 1; i += 1)
		{
			if (UtilG.chance(atkChances[i] - defChances[i]))
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

	private static void activateCounters(Player player, Pet pet, Creature creature)
	{
		if (player.getBattleActionCounter().finished() & player.getCurrentBattleAction() != null)
		{
			if (player.getCurrentBattleAction().equals(AtkTypes.defense))
			{
				player.deactivateDef() ;
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
		 			pet.deactivateDef() ;
				}
			}
		}
		if (creature.getBattleActionCounter().finished() & creature.getCurrentBattleAction() != null)
		{
			if (creature.getCurrentBattleAction().equals(AtkTypes.defense))
			{
//				UtilS.PrintBattleActions(6, "Creature", "creature", 0, 0, player.getBA().getSpecialStatus(), creature.getElem()) ;
	 			creature.deactivateDef() ;
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
	
	public static boolean isOver(Player player, Pet pet, Creature creature)
	{
		// TODO add pet life condition to finish battle
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
		
	private static void startAtkAnimations(LiveBeing user, AtkTypes atkType)
	{
		if (atkType == null) { return ;}

		switch (atkType)
		{
			case physical: user.phyHitGif.start() ; return ;
			case magical: user.magHitGif.start() ; return ;
			default: return ;
		}
	}
	
	private static void playAtkAnimation(LiveBeing user, Point pos, DrawingOnPanel DP)
	{
		if (user.phyHitGif.isPlaying())
		{
			user.phyHitGif.play(pos, Align.center, DP) ;
		}
		else
		{
			user.phyHitGif.resetTimeCounter() ;
		}
		if (user.magHitGif.isPlaying())
		{
			user.magHitGif.play(pos, Align.center, DP) ;
		}
		else
		{
			user.magHitGif.resetTimeCounter() ;
		}
	}
	
	
	private AtkResults atk(AtkTypes atkType, LiveBeing attacker, LiveBeing receiver)
	{
		switch (atkType)
		{
			case physical: return calcPhysicalAtk(attacker, receiver) ;
			case magical:
			{
				Spell spell = attacker.getSpells().get(Player.SpellKeys.indexOf(attacker.getCurrentAction())) ;
				if (spell.isReady() & attacker.hasEnoughMP(spell))
				{
					return attacker.useSpell(spell, receiver) ;
				}
				
				return new AtkResults() ;
			}
			case physicalMagical: 
			{
				// TODO physicalMagical atks
				return new AtkResults() ;
			}
			case defense:
			{
	 			attacker.activateDef() ;
				return new AtkResults(AtkTypes.defense , AttackEffects.none, 0) ;
			}
			default: return new AtkResults() ;
		}
	}
	
	private AtkResults runTurn(LiveBeing attacker, LiveBeing receiver)
	{
		AtkResults atkResult = new AtkResults() ;
		if (attacker.actionIsPhysicalAtk() | attacker.actionIsArrowAtk())
		{
			atkResult = atk(AtkTypes.physical, attacker, receiver) ;
		}
		else if (attacker.actionIsSpell() & !attacker.isSilent())
		{
			atkResult = atk(AtkTypes.magical, attacker, receiver) ;
		}
		else if (attacker.actionIsDef())
		{
			atkResult = atk(AtkTypes.defense, attacker, receiver) ;
		}
		
		if (atkResult.getEffect().equals(AttackEffects.hit) | atkResult.getEffect().equals(AttackEffects.crit))
		{
			receiver.getPA().getLife().decTotalValue(atkResult.getDamage()) ;
			int[] inflictedStatus = calcStatus(attacker.getBA().baseAtkChances(), receiver.getBA().baseDefChances(), attacker.getBA().baseDurations()) ;				
			receiver.getBA().getStatus().receiveStatus(inflictedStatus) ;
		}
		
		if (attacker.actionIsPhysicalAtk() | (attacker.actionIsSpell() & !attacker.isSilent()))
		{
			if (attacker instanceof Player & attacker.getJob() == 2)
			{
				if (((Player) attacker).arrowIsEquipped()) { ((Player) attacker).spendArrow() ;}
			}
		}
		
		if (attacker.actionIsPhysicalAtk() | (attacker.actionIsSpell() & !attacker.isSilent()) | attacker.actionIsDef())
		{
			attacker.updateCombo() ;
			attacker.resetBattleActions() ;
		}
		
		attacker.setCurrentAtkType(atkResult.getAtkType()) ;
		PrintAtkResults(atkResult) ;
		PrintReceiverCondition(receiver) ;
		return atkResult ;
	}	
	
	private void checkTurn(LiveBeing attacker, LiveBeing receiver, int damageAnimation, Animation damageAni, DrawingOnPanel DP)
	{
		
		if (!attacker.isAlive()) { return ;}
		
		// TODO criatura tem que tomar blood and poison dano do player e do pet
		attacker.DrawTimeBar("Left", Game.colorPalette[2], DP) ;
//		attacker.TakeBloodAndPoisonDamage(receiver.getBA().getBlood().TotalAtk(), receiver.getBA().getPoison().TotalAtk()) ;
		if (attacker.canAtk() & attacker.isInRange(receiver.getPos()))
		{
			if (attacker instanceof Creature)
			{
				((Creature) attacker).fight(receiver.getCurrentAction()) ;
			
			}
			AtkResults atkResults = new AtkResults() ;
			if (attacker.hasActed())
			{
				PrintTurn(attacker, receiver) ;
				atkResults = runTurn(attacker, receiver) ;
			}
			if (attacker.actionIsPhysicalAtk() | attacker.actionIsSpell())
			{
				receiver.getDisplayDamage().reset() ;
			}

			if (Game.getPlayer().getSettings().getSoundEffectsAreOn())
			{
				Music.PlayMusic(hitSound) ;
			}


			attacker.useAutoSpells(true) ;

			
			if (atkResults.getAtkType() != null)
			{
				if ( !atkResults.getEffect().equals(AttackEffects.none) | atkResults.getAtkType().equals(AtkTypes.defense) )
				{
					if (!(attacker instanceof Creature)) { attacker.train(atkResults) ;}
					if (attacker instanceof Player) { ((Player) attacker).getStatistics().update(atkResults) ;}
				}
			}			

			damageAni.start(100, new Object[] {receiver.getPos(), receiver.getSize(), atkResults, damageAnimation}) ;
			if (attacker instanceof Player) { startAtkAnimations(attacker, atkResults.getAtkType()) ;}
		}
		
		attacker.getBA().getStatus().display(attacker.getPos(), attacker.getDir(), DP);
		if (attacker.isDefending())
		{
			attacker.displayDefending(DP) ;
		}
		playAtkAnimation(attacker, receiver.getPos(), DP) ;
		
	}
	
	public void RunBattle(Player player, Pet pet, Creature creature, List<Animation> ani, DrawingOnPanel DP)
	{
		player.incrementBattleActionCounters() ;
		if (pet != null) {pet.incrementBattleActionCounters() ;}
		creature.incrementBattleActionCounters() ;
		
		activateCounters(player, pet, creature) ;
		int damageStyle = player.getSettings().getDamageAnimation() ;
		LiveBeing creatureTarget = player ;
		if (pet != null) { creatureTarget = creature.chooseTarget(player.isAlive(), pet.isAlive()).equals("player") ? player : pet ;}

		checkTurn(player, creature, damageStyle, ani.get(0), DP) ;
		if (pet != null) { checkTurn(pet, creature, damageStyle, ani.get(1), DP) ;}
		checkTurn(creature, creatureTarget, damageStyle, ani.get(2), DP) ;
		
		if (!isOver(player, pet, creature)) { return ;}
		
		FinishBattle(player, pet, creature) ;
	}
	
	private void FinishBattle(Player player, Pet pet, Creature creature)
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
	
		
		if (!creature.isAlive())
		{
			if (player.isAlive())
			{
				player.win(creature, true) ;
			}
			player.resetAction() ;
			player.resetBattleAction() ;
			if (pet != null)
			{
				if (pet.isAlive())
				{
					pet.win(creature) ;
				}
			}
//			creature.dies() ;
			
			return ;
		}
		
		creature.getPA().getLife().setToMaximum() ;
		creature.getPA().getMp().setToMaximum() ;
//		player.dies() ;
//		if (pet != null) {pet.dies() ; pet.setPos(player.getPos());}
		creature.setFollow(false) ;
	}


}
