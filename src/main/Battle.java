package main ;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Clip;

import graphics.Animation;
import graphics.AnimationTypes;
import graphics.DrawPrimitives;
import liveBeings.Creature;
import liveBeings.LiveBeing;
import liveBeings.LiveBeingStates;
import liveBeings.Pet;
import liveBeings.Player;
import liveBeings.Spell;
import simulations.PlayerEvolutionSimulation;
import utilities.AtkEffects;
import utilities.Elements;
import utilities.GameStates;
import utilities.Log;
import utilities.UtilG;

public abstract class Battle 
{
	private static double randomAmp ;
	private static List<Elements> ElemID ;
	private static double[][] ElemMult ;
	
	public static final Clip hitSound ;
	
	private static int damageStyle ;
	public static final Color phyAtkColor = Game.colorPalette[6] ;
	public static final Color magAtkColor = Game.colorPalette[5] ;
	

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
		damageStyle = 0 ;
//		damageAni = new ArrayList<>() ;
		randomAmp = (double)0.1 ;
		hitSound = Music.musicFileToClip(new File(Game.MusicPath + "16-Hit.wav").getAbsoluteFile()) ;
	}


	public static void updateDamageAnimation(int newDamageStyle)
	{
		damageStyle = newDamageStyle ;
	}
	
	private static void playDamageAnimation(LiveBeing receiver, AtkResults atkResults)
	{
		
		if (atkResults.getAtkType() == null) { return ;}
		
		Animation.start(AnimationTypes.damage, new Object[] {receiver.getPos(), damageStyle, atkResults});
		
	}
	
	public static double basicElemMult(Elements atk, Elements def)
	{
		return ElemMult[ElemID.indexOf(atk)][ElemID.indexOf(def)] ;
	}
	
	public static boolean hit(double dex, double agi)
	{
		double hitChance = 1 - 1 / (1 + Math.pow(1.1, dex - agi)) ;
		return UtilG.chance(hitChance) ;
	}
	
	public static boolean block(double blockDef)
	{
		return UtilG.chance(blockDef) ;
	}

	public static boolean criticalAtk(double critAtk, double critDef)
	{
		return UtilG.chance(critAtk - critDef) ;
	}
		
	public static double calcElemMult(Elements atk, Elements weapon, Elements armor, Elements shield, Elements superElem)
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

	public static AtkResults calcPhysicalAtk(LiveBeing attacker, LiveBeing receiver)
	{
		double arrowPower = 0 ;
		if (attacker.actionIsArrowAtk())
		{
			arrowPower += ((Player) attacker).getEquippedArrow().getAtkPower() ;
		}
		
		double atkDex = attacker.getBA().TotalDex() ;
		double defAgi = receiver.getBA().TotalAgi() ;
		double atkCrit = attacker.getBA().TotalCritAtkChance() ;
		double defCrit = receiver.getBA().TotalCritDefChance() ;
		double defBlock = receiver.getBA().getStatus().getBlock() ;
		double atkPhyAtk = attacker.getBA().TotalPhyAtk() ;
		double defPhyDef = receiver.getBA().TotalPhyDef() ;
		Elements[] atkElems = attacker.atkElems() ;
		Elements[] defElems = receiver.defElems() ;
		
		AtkEffects effect = calcEffect(atkDex, defAgi, atkCrit, defCrit, defBlock) ;
		int damage = calcDamage(effect, atkPhyAtk + arrowPower, defPhyDef, atkElems, defElems, 1) ;

		return new AtkResults(AtkTypes.physical, effect, damage) ;
	}
	// TODO learn spell
	public static AtkEffects calcEffect(double dex, double agi, double critAtk, double critDef, double blockDef)
	{
		if (block(blockDef))
		{
			return AtkEffects.block ;
		}
		
		if (hit(dex, agi))
		{
			if (criticalAtk(critAtk, critDef))
			{
				return AtkEffects.crit ;
			}
			return AtkEffects.hit ;
		}
		
		return AtkEffects.miss ;
	}
	
	public static int calcDamage(AtkEffects effect, double atk, double def, Elements[] atkElems, Elements[] defElems, double elemMod)
	{
		double baseDamage = switch(effect)
		{
			case hit -> Math.max(0, (atk - def)) ;
			case crit -> atk ;
			default -> 0 ;
		};
		
		double randomMult = UtilG.RandomMult(randomAmp) ;
		double elemMult = calcElemMult(atkElems[0], atkElems[1], defElems[0], defElems[0], atkElems[2]) ;		
		return (int) UtilG.Round(randomMult * elemMult * elemMod * baseDamage, 0) ;
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
	
	public static void throwItem(LiveBeing attacker, LiveBeing receiver, double itemPower, Elements itemElem)
	{
		double atkDex = attacker.getBA().TotalDex() ;
		double defAgi = receiver.getBA().TotalAgi() ;
		double atkCrit = attacker.getBA().TotalCritAtkChance() ;
		double defCrit = receiver.getBA().TotalCritDefChance() ;
		double defBlock = receiver.getBA().getStatus().getBlock() ;
		double defPhyDef = receiver.getBA().TotalPhyDef() ;
		
		Elements[] atkElems = new Elements[] {itemElem, Elements.neutral, Elements.neutral} ;
		AtkEffects effect = calcEffect(atkDex, defAgi, atkCrit, defCrit, defBlock) ;
		
		if (!effect.equals(AtkEffects.hit) & !effect.equals(AtkEffects.crit)) { return ;}
		
		int damage = Battle.calcDamage(AtkEffects.hit, itemPower, defPhyDef, atkElems, receiver.defElems(), 1.0) ;
		receiver.getPA().getLife().decTotalValue(damage) ;
		AtkResults atkResults = new AtkResults(AtkTypes.physical, effect, damage) ;
		playDamageAnimation(receiver, atkResults) ;
		startAtkAnimations(attacker, atkResults.getAtkType()) ;
	}
	
	private static AtkTypes atkTypeFromAction(LiveBeing attacker)
	{
		if (attacker.usedPhysicalAtk() | attacker.actionIsArrowAtk())
		{
			return AtkTypes.physical ;
		}
		if (attacker.actionIsSpell() & !attacker.isSilent())
		{
			return AtkTypes.magical ;
		}
		if (attacker.usedDef())
		{
			return AtkTypes.defense ;
		}
		
		return null ;
	}
	
	public static Point knockback(Point originPos, Point targetPos, int dist)
	{
		double angle = targetPos.x != originPos.x ? Math.atan((targetPos.y - originPos.y) / (double)(targetPos.x - originPos.x)) : Math.PI / 2.0 ;
		int travelX = (int) (Math.signum(targetPos.x - originPos.x) * Math.cos(angle) * dist) ;
		int travelY = (int) (Math.signum(targetPos.y - originPos.y) * Math.sin(angle) * dist) ;
		
		Point destiny = new Point(targetPos.x + travelX, targetPos.y + travelY) ;
		return destiny ;
	}
		
	private static void incrementCounters(Player player, Pet pet, Creature creature)
	{
		player.incrementBattleActionCounters() ;
		if (pet != null) {pet.incrementBattleActionCounters() ;}
		creature.incrementBattleActionCounters() ;
	}
 	
	private static void activateCounters(Player player, Pet pet, Creature creature)
	{
		if (player.getBattleActionCounter().finished() & player.getCurrentAtkType() != null)
		{
			if (player.getCurrentAtkType().equals(AtkTypes.defense))
			{
				player.deactivateDef() ;
			}
//			UtilS.PrintBattleActions(6, "Player", "creature", 0, 0, player.getBA().getSpecialStatus(), creature.getElem()) ;
		}
		if (pet != null)
		{
			if (pet.isAlive() & pet.getBattleActionCounter().finished() & pet.getCurrentAtkType() != null)
			{
				if (pet.getCurrentAtkType().equals(AtkTypes.defense))
				{
//					UtilS.PrintBattleActions(6, "Pet", "creature", 0, 0, player.getBA().getSpecialStatus(), creature.getElem()) ;
		 			pet.deactivateDef() ;
				}
			}
		}
		if (creature.getBattleActionCounter().finished() & creature.getCurrentAtkType() != null)
		{
			if (creature.getCurrentAtkType().equals(AtkTypes.defense))
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
//			case physical: user.phyHitGif.start() ; return ;
//			case magical: user.magHitGif.start() ; return ;
			default: return ;
		}
	}
		
	private static void playAtkAnimations(LiveBeing user, Point pos, DrawPrimitives DP)
	{
//		if (user.phyHitGif.isPlaying())
//		{
//			user.phyHitGif.play(pos, Align.center, DP) ;
//		}
//		else
//		{
//			user.phyHitGif.resetTimeCounter() ;
//		}
//		if (user.magHitGif.isPlaying())
//		{
//			user.magHitGif.play(pos, Align.center, DP) ;
//		}
//		else
//		{
//			user.magHitGif.resetTimeCounter() ;
//		}
	}
			
	private static void checkSpendArrow(LiveBeing attacker)
	{
		if (!(attacker instanceof Player)) { return ;}
		
		Player player = (Player) attacker ;
		
//		if (player.getJob() != 2) { return ;}
		if (!player.arrowIsEquipped()) { return ;}
		if (!player.usedPhysicalAtk() & !player.usedSpell()) { return ;}
		
		player.spendArrow() ;
	}

	private static AtkResults performAtk(AtkTypes atkType, LiveBeing attacker, LiveBeing receiver)
	{		
		
		if (atkType == null) { return new AtkResults() ;}
		
		

		AtkResults atkResult = switch (atkType)
		{
			case physical -> calcPhysicalAtk(attacker, receiver) ;
			case magical ->
			{
				int spellID = Player.SpellKeys.indexOf(attacker.getCurrentAction()) ;
				Spell spell = attacker.getSpells().get(spellID) ;
				if (spell.isReady() & attacker.hasEnoughMP(spell))
				{
					yield attacker.useSpell(spell, receiver);
				}
				
				yield new AtkResults();
			}
			case physicalMagical ->
			{
				yield new AtkResults();
			}
			case defense ->
			{
	 			attacker.activateDef() ;
				yield new AtkResults(AtkTypes.defense , AtkEffects.none, 0);
			}
			default -> new AtkResults() ;
		} ;
		
		checkSpendArrow(attacker) ;
		
		
		
		AtkEffects effect = atkResult.getEffect() ;
		// TODO using mystical inspirations effect = null
		if (!effect.equals(AtkEffects.hit) & !effect.equals(AtkEffects.crit)) { return atkResult ;}


		receiver.takeDamage(atkResult.getDamage()) ;
		int[] appliedStatus = calcStatus(attacker.getBA().baseAtkChances(), receiver.getBA().baseDefChances(),
				attacker.getBA().baseDurations()) ;				
		receiver.getBA().getStatus().receiveStatus(appliedStatus) ;

		return atkResult ;
	}	
		
	private static void runTurn(LiveBeing attacker, LiveBeing receiver, DrawPrimitives DP)
	{
		
		if (!attacker.isAlive()) { return ;}
		
		// TODO criatura tem que tomar blood and poison dano do player e do pet
		attacker.drawTimeBar("Left", Game.colorPalette[13], DP) ;

		playAtkAnimations(attacker, receiver.center(), DP) ;
		if (attacker.isDefending())
		{
			attacker.displayDefending(DP) ;
		}
		
		if (!attacker.canAtk() | !attacker.isInRange(receiver.getPos())) { return ;}
		
		if (attacker instanceof Creature)
		{
			((Creature) attacker).chooseFightMove(receiver.getCurrentAction()) ;
		}

		if (!attacker.hasActed()) { return ;}
		
		AtkTypes atkType = atkTypeFromAction(attacker) ;
		attacker.setCurrentAtkType(atkType) ;
		AtkResults atkResults = performAtk(atkType, attacker, receiver) ;
		if (!(attacker instanceof Creature)) { attacker.train(atkResults) ;}
		if (attacker instanceof Player) { ((Player) attacker).getStatistics().update(atkResults) ;}
		
		attacker.useAutoSpells(true) ;

		attacker.updateCombo() ;
		attacker.resetBattleActions() ;
		
		Log.atkResults(attacker, atkResults) ;
		
		playDamageAnimation(receiver, atkResults) ;
		startAtkAnimations(attacker, atkType) ;

		if (Game.getPlayer().getSettings().getSoundEffectsAreOn())
		{
			Music.PlayMusic(hitSound) ;
		}

		
	}
		
	
	
	public static void runBattle(Player player, Pet pet, Creature creature, DrawPrimitives DP)
	{
		incrementCounters(player, pet, creature) ;
		activateCounters(player, pet, creature) ;
		
		LiveBeing creatureTarget = player ;
		if (pet != null) { creatureTarget = creature.chooseTarget(player.isAlive(), pet.isAlive()).equals("player") ? player : pet ;}

		runTurn(player, creature, DP) ;
		if (pet != null) { runTurn(pet, creature, DP) ;}
		runTurn(creature, creatureTarget, DP) ;
		
		if (!isOver(player, pet, creature)) { return ;}
		
		finishBattle(player, pet, creature) ;
	}
		
	
	
	private static void finishBattle(Player player, Pet pet, Creature creature)
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
			spell.getDurationCounter().reset() ;
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


	public static double getRandomAmp() { return randomAmp ;}
}
