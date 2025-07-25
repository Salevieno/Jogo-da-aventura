package utilities;

import java.util.List;
import java.util.Map;

import battle.AtkResults;
import graphics2.Animation;
import graphics2.Gif;
import items.Item;
import liveBeings.Buff;
import liveBeings.CreatureType;
import liveBeings.LiveBeing;
import liveBeings.Spell;

public abstract class Log
{
	public static void loadingStatus(boolean loadedSuccessfully, int slot)
	{
		System.out.println(loadedSuccessfully ? "Info: save " + slot + " loaded successfully!" : "Info: save " + slot + " not loaded correctly or not found!");
	}
	public static void valorNegativo(String metodo)
	{
		System.out.println("tentando usar " + metodo + " com valor negativo") ;
	}
	
	public static void buffs(List<Buff> buffs)
	{
		buffs.forEach(System.out::println) ;
	}
	
	public static void spells(List<Spell> spells)
	{
		spells.forEach(System.out::println) ;
	}
	
	public static void spellUsed(LiveBeing user, String spellName, int spellLevel)
	{
		System.out.println(user.getName() + " used " + spellName + " level " + spellLevel);
	}
	
	public static void itemUsed(LiveBeing user, Item item)
	{
		System.out.println(user.getName() + " used " + item.getName());
	}
	
	public static void counter(GameTimer counter)
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
	
	public static void attributes(LiveBeing being)
	{
		System.out.println(" ====== player " + being.getName() + " attributes ======") ;
		System.out.println("action delay: " + being.getActionCounter().getDuration());
		System.out.println("spells");
		being.getSpells().forEach(System.out::println);
		System.out.println("==================\n");
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

	public static void allEntityListsLength()
	{
		System.out.println("\n All entity lists length");
		System.out.println("Animations: " + Animation.getAll().size()) ;
		System.out.println("Creature types: " + CreatureType.all.size()) ;
	}
	
	public static void diggingItems(Map<Item, Double> allDiggingItems)
	{
		allDiggingItems.entrySet().forEach(System.out::println);
	}
	
	public static void __()
	{
		
	}
}
