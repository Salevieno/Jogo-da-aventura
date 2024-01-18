package utilities;

import java.util.List;

import liveBeings.Buff;
import liveBeings.Spell;

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
}
