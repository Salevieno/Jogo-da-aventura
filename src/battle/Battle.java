package battle ;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import attributes.Attributes;
import liveBeings.AttackModifiers;
import liveBeings.Creature;
import liveBeings.LiveBeing;
import liveBeings.Pet;
import liveBeings.Player;
import liveBeings.Spell;
import main.Elements;
import main.Game;
import main.GameStates;
import main.Path;
import simulations.EvolutionSimulation;
import utilities.Util;

public abstract class Battle 
{
	private static double randomAmp ;
	private static List<Elements> allElements ;
	private static double[][] elemMult ;

	static
	{
		randomAmp = 0.1 ;		
    	allElements = Arrays.asList(Elements.values()) ;
		List<String[]> ElemInput = Util.readcsvFile(Path.CSV + "Elem.csv") ;
		elemMult = new double[ElemInput.size()][ElemInput.size()] ;
		for (int i = 0 ; i <= ElemInput.size() - 1 ; ++i)
		{
			for (int j = 0 ; j <= ElemInput.size() - 1 ; ++j)
			{
				elemMult[i][j] = Double.parseDouble(ElemInput.get(i)[j + 1]) ;
			}				
		}	
	}

	public static void removeRandomness() { randomAmp = 0 ;}

	private static double basicElemMult(Elements atk, Elements def) { return elemMult[allElements.indexOf(atk)][allElements.indexOf(def)] ;}
	
	private static boolean hit(double dex, double agi)
	{
		double hitChance = 1 - 1 / (1 + Math.pow(1.1, dex - agi)) ;
		return Util.chance(hitChance) ;
	}
	
	private static boolean block(double blockDef) { return Util.chance(blockDef) ;}

	private static boolean criticalAtk(double critAtk, double critDef) {return Util.chance(critAtk - critDef) ;}
		
	private static double calcElemMult(Elements atk, Elements weapon, Elements armor, Elements shield, Elements superElem)
	{
		double mult = 1 ;
		mult *= atk != null && armor != null ? basicElemMult(atk, armor) : 1 ;
		mult *= atk != null && shield != null ? basicElemMult(atk, shield) : 1 ;
		mult *= weapon != null && armor != null ? basicElemMult(weapon, armor) : 1 ;
		mult *= weapon != null && shield != null ? basicElemMult(weapon, shield) : 1 ;
		mult *= superElem != null && armor != null ? basicElemMult(superElem, armor) : 1 ;
		mult *= superElem != null && shield != null ? basicElemMult(superElem, shield) : 1 ;
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
		double defBlock = receiver.getBA().getBlock().TotalDefChance() ;
		double atkPhyAtk = attacker.getBA().TotalPhyAtk() ;
		double defPhyDef = receiver.getBA().TotalPhyDef() ;
		Elements[] atkElems = attacker.atkElems() ;
		Elements[] defElems = receiver.defElems() ;
		double elemRes = receiver.defElems()[0] != null ? attacker.getBA().getElemResistanceMult().get(receiver.defElems()[0]) : 1.0 ;
		
		AtkEffects effect = calcEffect(atkDex, defAgi, atkCrit, defCrit, defBlock) ;
		int damage = calcDamage(effect, atkPhyAtk + arrowPower, defPhyDef, atkElems, defElems, elemRes) ;
		Map<Attributes, Double> inflictedStatus = calcStatus(attacker.getBA().baseAtkChances(), receiver.getBA().baseDefChances(), attacker.getBA().baseDurations()) ;				
		
		return new AtkResults(AtkTypes.physical, effect, damage, inflictedStatus) ;
	}

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
		
		double randomMult = Util.randomMult(randomAmp) ;
		double elemMult = calcElemMult(atkElems[0], atkElems[1], defElems[0], defElems[0], atkElems[2]) ;
		return (int) Util.round(randomMult * elemMult * elemMod * baseDamage, 0) ;
	}

	public static Map<Attributes, Double> calcStatus(double[] atkChances, double[] defChances, double[] durations)
	{
		double stun = 0 ;
		double block = 0 ;
		double blood = 0 ;
		double poison = 0 ;
		double silence = 0 ;
		if (Util.chance(atkChances[1] - defChances[1])) {block = durations[1] ;}
		if (0 < block) 
		{
			return Map.of(
				Attributes.stun, 0.0,
				Attributes.block, block,
				Attributes.blood, 0.0,
				Attributes.poison, 0.0,
				Attributes.silence, 0.0
			) ;
		}
		
		if (Util.chance(atkChances[0] - defChances[0])) {stun = durations[0] ;}
		if (Util.chance(atkChances[2] - defChances[2])) {blood = durations[2] ;}
		if (Util.chance(atkChances[3] - defChances[3])) {poison = durations[3] ;}
		if (Util.chance(atkChances[4] - defChances[4])) {silence = durations[4] ;}
		
		
		return Map.of(
			Attributes.stun, stun,
			Attributes.block, block,
			Attributes.blood, blood,
			Attributes.poison, poison,
			Attributes.silence, silence
		) ;
	}
	
	public static void throwItem(LiveBeing attacker, LiveBeing receiver, double itemPower, AttackModifiers itemAtkMod, Elements itemElem)
	{
		double atkDex = attacker.getBA().TotalDex() ;
		double defAgi = receiver.getBA().TotalAgi() ;
		double atkCrit = attacker.getBA().TotalCritAtkChance() ;
		double defCrit = receiver.getBA().TotalCritDefChance() ;
		double defBlock = receiver.getBA().getBlock().TotalDefChance() ;
		double defPhyDef = receiver.getBA().TotalPhyDef() ;
		double[] baseAtkChances = itemAtkMod == null ? new double[5] : itemAtkMod.getBaseAtkChances() ;
		
		Elements[] atkElems = new Elements[] {itemElem, Elements.neutral, Elements.neutral} ;
		AtkEffects effect = calcEffect(atkDex, defAgi, atkCrit, defCrit, defBlock) ;
		double elemRes = attacker.getBA().getElemResistanceMult().get(receiver.defElems()[0]) ;
		
		if (!effect.equals(AtkEffects.hit) & !effect.equals(AtkEffects.crit)) { return ;}
		int damage = Battle.calcDamage(AtkEffects.hit, itemPower, defPhyDef, atkElems, receiver.defElems(), elemRes) ;
		Map<Attributes, Double> inflictedStatus = calcStatus(baseAtkChances, receiver.getBA().baseDefChances(), attacker.getBA().baseDurations()) ;				
		
		AtkResults atkResults = new AtkResults(AtkTypes.physical, effect, damage, inflictedStatus) ;
		receiver.takeDamage(atkResults.getDamage()) ;
		if (attacker instanceof Player)
		{
			((Player) attacker).getStatistics().updateInflicedStatus(atkResults.getStatus());
		}
//		receiver.getBA().getStatus().receiveStatus(atkResults.getStatus()) ;
//		playDamageAnimation(receiver, atkResults) ;
//		receiver.playDamageAnimation(damageStyle, atkResults, Game.colorPalette[7]) ;
		startAtkAnimations(attacker, atkResults.getAtkType()) ;
	}
	
	public static Point2D.Double knockback(Point2D.Double originPos, Point2D.Double targetPos, double power)
	{
		double angle = Math.atan2((targetPos.y - originPos.y), (targetPos.x - originPos.x)) ;		
		return new Point2D.Double(Math.cos(angle) * power, Math.sin(angle) * power) ;
	}
	
	public static boolean isOver(Player player, Pet pet, Creature creature)
	{
		return pet == null ? !creature.isAlive() || !player.isAlive() : !creature.isAlive() || (!player.isAlive() & !pet.isAlive()) ;
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

	public static void finishBattle(Player player, Pet pet, Creature creature)
	{
		if (Game.getState().equals(GameStates.simulation))
		{
			EvolutionSimulation.setBattleResults(player.getLife().getCurrentValue(), creature.getLife().getCurrentValue()) ;
		}		
		
		for (Spell spell : player.getSpells())
		{
			spell.getDurationCounter().reset() ;
		}
	
		if (!creature.isAlive())
		{
			creature.dies() ;
			if (player.isAlive())
			{
				player.win(creature, true) ;
			}
			player.resetAction() ;
			player.resetBattleAction() ;
			player.leaveBattle() ;
			if (pet != null)
			{
				if (pet.isAlive())
				{
					pet.win(creature) ;
				}
			}
			
			return ;
		}
		
		creature.leaveBattle() ;
		player.leaveBattle() ;
		player.dies() ;
		if (pet != null)
		{
			pet.dies() ;
		}
	}
}