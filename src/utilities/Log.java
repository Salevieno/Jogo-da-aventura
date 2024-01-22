package utilities;

import java.util.List;

import graphics.Animation;
import graphics.Gif;
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
	
	public static void animationStart(Animation ani)
	{
		System.out.println("Started playing animation " + ani);
	}
	
	public static void gifStart(Gif gif)
	{
		System.out.println("Started playing gif " + gif);
	}
	
	public static void __()
	{
		
	}
}
