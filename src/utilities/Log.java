package utilities;

import java.util.List;

import graphics.Animation;
import graphics.Gif;
import liveBeings.Buff;
import liveBeings.LiveBeing;
import liveBeings.Spell;
import main.AtkResults;

public abstract class Log
{
	public static void buffs(List<Buff> buffs)
	{
		buffs.forEach(System.out::println) ;
	}
	
	public static void spells(List<Spell> spells)
	{
		spells.forEach(System.out::println) ;
	}
	
	public static void counter(TimeCounter counter)
	{
		System.out.println("Counting " + counter.getCounter() + " until " + counter.getDuration()) ;
	}
	
	public static void loadTime(String item, long initialTime)
	{
		long elapsedTime = (long) ((System.nanoTime() - initialTime) * Math.pow(10,  -6)) ;
		System.out.println("Loaded " + item + " in " + elapsedTime + " ms") ;
	}
	
	public static void animationStart(Animation ani)
	{
		System.out.println("Started playing animation " + ani);
	}
	
	public static void gifStart(Gif gif)
	{
		System.out.println("Started playing gif " + gif);
	}
	

	public static void battleStart()
	{
		System.out.println("\n  Battle started!  ");
	}
	
	public static void attackerAction(LiveBeing attacker)
	{
		System.out.println(attacker.getName() + " used action " + attacker.getCurrentAction()) ;
	}
	
	public static void atkResults(LiveBeing attacker, AtkResults atkResults)
	{
		
		if (atkResults.getAtkType() == null) { return ;}
		
		String atkType = atkResults.getAtkType().toString() ;
		System.out.println("\n" + attacker.getName() + " performed " + atkType + " action!");
		System.out.println("resulting in " + atkResults.getEffect().toString() + " and " + atkResults.getDamage() + " damage");
	}
	
	public static void elementAtk(Elements atkElem, Elements weaponElem, Elements armorElem, Elements shieldElem, Elements superElem, double elemMult)
	{
		System.out.println("Elements: atk = " + atkElem + " | weapon = " + weaponElem + " | armor = " + armorElem + " | def = " + shieldElem + " | super = " + superElem + " | mult = " + elemMult) ;
	}
	
	public static void atkType(LiveBeing attacker)
	{
		System.out.println("attacker action: " + attacker.getCurrentAction());
		System.out.println("attacker.usedPhysicalAtk(): " + attacker.usedPhysicalAtk());
		System.out.println("attacker.actionIsArrowAtk(): " + attacker.actionIsArrowAtk());
		System.out.println("attacker.actionIsSpell(): " + attacker.actionIsSpell());
		System.out.println("attacker.isSilent(): " + attacker.isSilent());
		System.out.println("attacker.usedDef(): " + attacker.usedDef() + "\n");
	}
	
	public static void __()
	{
		
	}
}
