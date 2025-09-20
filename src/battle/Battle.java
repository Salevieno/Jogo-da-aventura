package battle ;

import java.awt.Point;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.sound.sampled.Clip;

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
import main.Music;
import main.Path;
import simulations.EvolutionSimulation;
import utilities.Util;

public abstract class Battle 
{
	private static double randomAmp ;
	private static List<Elements> allElements ;
	private static double[][] elemMult ;
	
	private static final Clip hitSound ;	

	static
	{
		randomAmp = (double)0.1 ;		
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
		
		hitSound = Music.musicFileToClip(new File(Path.MUSIC + "16-Hit.wav").getAbsoluteFile()) ;		
	}

	public static double getRandomAmp() { return randomAmp ;}
	public static void removeRandomness() { randomAmp = 0 ;}
	
//	
//	public static void playDamageAnimation(LiveBeing receiver, AtkResults atkResults)
//	{
//		
//		if (atkResults.getAtkType() == null) { return ;}
//		
//		Animation.start(AnimationTypes.damage, new Object[] {receiver.headPos(), damageStyle, atkResults});
//		
//	}
	
	public static double basicElemMult(Elements atk, Elements def) { return elemMult[allElements.indexOf(atk)][allElements.indexOf(def)] ;}
	
	public static boolean hit(double dex, double agi)
	{
		double hitChance = 1 - 1 / (1 + Math.pow(1.1, dex - agi)) ;
		return Util.chance(hitChance) ;
	}
	
	public static boolean block(double blockDef) { return Util.chance(blockDef) ;}

	public static boolean criticalAtk(double critAtk, double critDef) {return Util.chance(critAtk - critDef) ;}
		
	public static double calcElemMult(Elements atk, Elements weapon, Elements armor, Elements shield, Elements superElem)
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
		double[] inflictedStatus = calcStatus(attacker.getBA().baseAtkChances(), receiver.getBA().baseDefChances(), attacker.getBA().baseDurations()) ;				
		
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
		
	public static double[] calcStatus(double[] atkChances, double[] defChances, double[] durations)
	{
		double stun = 0 ;
		double block = 0 ;
		double blood = 0 ;
		double poison = 0 ;
		double silence = 0 ;
		if (Util.chance(atkChances[1] - defChances[1])) {block = durations[1] ;}
		if (0 < block) { return new double[] {0, block, 0, 0, 0} ;}
		
		if (Util.chance(atkChances[0] - defChances[0])) {stun = durations[0] ;}
		if (Util.chance(atkChances[2] - defChances[2])) {blood = durations[2] ;}
		if (Util.chance(atkChances[3] - defChances[3])) {poison = durations[3] ;}
		if (Util.chance(atkChances[4] - defChances[4])) {silence = durations[4] ;}
		
		
		return new double[] {stun, block, blood, poison, silence} ;
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
		double[] inflictedStatus = calcStatus(baseAtkChances, receiver.getBA().baseDefChances(), attacker.getBA().baseDurations()) ;				
		
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
	
	private static AtkTypes atkTypeFromAction(LiveBeing attacker)
	{
		if (attacker.usedPhysicalAtk() | attacker.actionIsArrowAtk()) { return AtkTypes.physical ;}
		if (attacker.actionIsSpell()) { return AtkTypes.magical ;}
		if (attacker.usedDef()) { return AtkTypes.defense ;}
		
		return null ;
	}
	
	public static Point knockback(Point originPos, Point targetPos, double dist)
	{
		double angle = targetPos.x != originPos.x ? Math.atan((targetPos.y - originPos.y) / (double)(targetPos.x - originPos.x)) : Math.PI / 2.0 ;
		int travelX = (int) (Math.signum(targetPos.x - originPos.x) * Math.cos(angle) * dist) ;
		int travelY = (int) (Math.signum(targetPos.y - originPos.y) * Math.sin(angle) * dist) ;
		
		Point destiny = new Point(targetPos.x + travelX, targetPos.y + travelY) ;
		return destiny ;
	}

	private static void activateCounters(Player player, Pet pet, Creature creature)
	{
		if (player.getBattleActionCounter().hasFinished() & player.getCurrentAtkType() != null)
		{
			if (player.getCurrentAtkType().equals(AtkTypes.defense))
			{
				player.deactivateDef() ;
			}
		}
		if (pet != null)
		{
			if (pet.isAlive() & pet.getBattleActionCounter().hasFinished() & pet.getCurrentAtkType() != null)
			{
				if (pet.getCurrentAtkType().equals(AtkTypes.defense))
				{
		 			pet.deactivateDef() ;
				}
			}
		}
		if (creature.getBattleActionCounter().hasFinished() & creature.getCurrentAtkType() != null)
		{
			if (creature.getCurrentAtkType().equals(AtkTypes.defense))
			{
	 			creature.deactivateDef() ;
			}
		}
		
		if (player.getBattleActionCounter().hasFinished())
		{
			player.resetBattleAction() ;
		}
		if (pet != null)
		{
			if (pet.getBattleActionCounter().hasFinished())
			{
				pet.resetBattleAction() ;
			}
		}
		if (creature.getBattleActionCounter().hasFinished())
		{
			creature.resetBattleAction() ;
		}
	}
		
	public static boolean isOver(Player player, Pet pet, Creature creature)
	{
		return pet == null ? !creature.isAlive() | !player.isAlive() : !creature.isAlive() | (!player.isAlive() & !pet.isAlive()) ;
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
		
//	private static void playAtkAnimations(LiveBeing user, Point pos)
//	{
//		if (user.phyHitGif.isPlaying())
//		{
//			user.phyHitGif.play(pos, Align.center) ;
//		}
//		else
//		{
//			user.phyHitGif.resetTimeCounter() ;
//		}
//		if (user.magHitGif.isPlaying())
//		{
//			user.magHitGif.play(pos, Align.center) ;
//		}
//		else
//		{
//			user.magHitGif.resetTimeCounter() ;
//		}
//	}
			
	private static void checkSpendArrow(LiveBeing attacker)
	{
		if (!(attacker instanceof Player)) { return ;}
		
		Player player = (Player) attacker ;
		
		if (!player.arrowIsEquipped()) { return ;}
		if (!player.usedPhysicalAtk() & !player.usedSpell()) { return ;}
		
		player.spendArrow() ;
		if (player.getJob() == 2)
		{
			attacker.useAutoSpell(true, player.getSpells().get(13)) ;
		}
	}

	private static AtkResults performAtk(AtkTypes atkType, LiveBeing attacker, LiveBeing receiver)
	{

		if (atkType == null) { System.out.println("Warn: atkType null at Battle.performAtk") ; return new AtkResults() ;}
		
		// TODO usar o switch com retorno aqui estava gerando um bug louco, que passou a acontecer qdo GamePanel foi criada
		AtkResults atkResults = null ;
		switch (atkType)
		{
			case physical:
				atkResults = calcPhysicalAtk(attacker, receiver) ;
				break ;
				
			case magical:
			{
				int spellID = Player.spellKeys.indexOf(attacker.getCurrentAction()) ;
				Spell spell = attacker.getActiveSpells().get(spellID) ;
				if (!attacker.canUseSpell(spell)) { System.out.println(attacker.getName() + ": trying to use spell. But no can use, baby!") ;}
				if (attacker.canUseSpell(spell))
				{
					atkResults = attacker.useSpell(spell, receiver);
				}
				else
				{					
					atkResults = new AtkResults();
				}
				
				break ;
			}
			case physicalMagical:
			{
				atkResults = new AtkResults();
				break ;
			}
			case defense:
			{
	 			attacker.activateDef() ;
	 			atkResults = new AtkResults(AtkTypes.defense , AtkEffects.none, 0, null);
	 			break ;
			}
			default: atkResults = new AtkResults() ; break ;
		} ;
		
//		AtkResults atkResults = switch (atkType)
//		{
//			case physical -> calcPhysicalAtk(attacker, receiver) ;
//			case magical ->
//			{
//				int spellID = Player.SpellKeys.indexOf(attacker.getCurrentAction()) ;
//				Spell spell = attacker.getActiveSpells().get(spellID) ;
//				if (!attacker.canUseSpell(spell)) { System.out.println(attacker.getName() + ": trying to use spell. But no can use, baby!") ;}
//				if (attacker.canUseSpell(spell))
//				{
//					yield attacker.useSpell(spell, receiver);
//				}
//				
//				yield new AtkResults();
//			}
//			case physicalMagical ->
//			{
//				yield new AtkResults();
//			}
//			case defense ->
//			{
//	 			attacker.activateDef() ;
//				yield new AtkResults(AtkTypes.defense , AtkEffects.none, 0, null);
//			}
//			default -> new AtkResults() ;
//		} ;
		
		checkSpendArrow(attacker) ;	
		
		AtkEffects effect = atkResults.getEffect() ;
		if (!effect.equals(AtkEffects.hit) & !effect.equals(AtkEffects.crit)) { return atkResults ;}

		receiver.takeDamage(atkResults.getDamage()) ;
		receiver.setPos(knockback(attacker.getPos(), receiver.getPos(), attacker.getBA().getKnockbackPower().getTotal())) ;
		if (attacker instanceof Player)
		{
			((Player) attacker).getStatistics().updateInflicedStatus(atkResults.getStatus());
		}
		
		if (0 < atkResults.getStatus()[0])
		{
			receiver.getStatus().get(Attributes.stun).inflictStatus(0, attacker.getBA().getStun().getDuration()) ;
		}
		if (0 < atkResults.getStatus()[1])
		{
			receiver.getStatus().get(Attributes.block).inflictStatus(0, attacker.getBA().getBlock().getDuration()) ;
		}
		if (0 < atkResults.getStatus()[2])
		{
			receiver.getStatus().get(Attributes.blood).inflictStatus(Math.max(attacker.getBA().getBlood().TotalAtk() - receiver.getBA().getBlood().TotalDef(), 0), attacker.getBA().getBlood().getDuration()) ;
		}
		if (0 < atkResults.getStatus()[3])
		{
			receiver.getStatus().get(Attributes.poison).inflictStatus(Math.max(attacker.getBA().getPoison().TotalAtk() - receiver.getBA().getPoison().TotalDef(), 0), attacker.getBA().getPoison().getDuration()) ;
		}
		if (0 < atkResults.getStatus()[4])
		{
			receiver.getStatus().get(Attributes.silence).inflictStatus(0, attacker.getBA().getSilence().getDuration()) ;
		}

		return atkResults ;
	}	
		
	private static void runTurn(LiveBeing attacker, LiveBeing receiver)
	{
		
		if (!attacker.isAlive()) { return ;}
		
		if (attacker.isDefending())
		{
			attacker.displayDefending() ;
		}
		
		if (!attacker.canAtk() | !attacker.isInRange(receiver.getPosAsDouble())) { return ;}
		
		if (attacker instanceof Creature)
		{
			((Creature) attacker).chooseFightMove(receiver.getCurrentAction()) ;
		}

		if (!attacker.hasActed()) { return ;}
		AtkTypes atkType = atkTypeFromAction(attacker) ;
		attacker.setCurrentAtkType(atkType) ;
		AtkResults atkResults = performAtk(atkType, attacker, receiver) ;
		if (!(attacker instanceof Creature)) { attacker.trainOffensive(atkResults) ;}
		if (attacker instanceof Creature) { receiver.trainDefensive(atkResults) ;}
		if (attacker instanceof Player) { ((Player) attacker).getStatistics().updateOffensive(atkResults) ;}
		if (receiver instanceof Player) { ((Player) receiver).getStatistics().updateDefensive(atkResults, receiver.getBA().getPhyDef().getTotal(), receiver.getBA().getMagDef().getTotal()) ;}

		attacker.updateCombo() ;
		attacker.resetBattleActions() ;

		EvolutionSimulation.updateBattleStats(attacker, receiver, atkResults) ;
		
		receiver.playDamageAnimation(atkResults, Game.palette[7]) ;
		startAtkAnimations(attacker, atkType) ;

		if (Game.getPlayer().getSettings().getSoundEffectsAreOn())
		{
			Music.PlayMusic(hitSound) ;
		}
		
	}
	
	public static void runBattle(Player player, Pet pet, Creature creature)
	{
		activateCounters(player, pet, creature) ;
		
		LiveBeing creatureTarget = player ;
		if (pet != null)
		{
			creatureTarget = creature.chooseTarget(player.isAlive(), pet.isAlive()).equals("player") ? player : pet ;
		}
		
		runTurn(player, creature) ;
		if (pet != null)
		{
			runTurn(pet, creature) ;
		}
		runTurn(creature, creatureTarget) ;
		
	}
	
	public static void finishBattle(Player player, Pet pet, Creature creature)
	{
		if (Game.getState().equals(GameStates.simulation))
		{
			EvolutionSimulation.setBattleResults(player.getLife().getCurrentValue(), creature.getLife().getCurrentValue()) ;
		}
		
		player.leaveBattle() ;
		
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
			creature.dies() ;
			
			return ;
		}
		
		creature.leaveBattle() ;
		player.dies() ;
		if (pet != null)
		{
			pet.dies() ;
		}
	}

}
