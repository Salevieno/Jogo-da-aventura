package utilities;

import java.util.HashSet;
import java.util.Set;

public class TimeCounter
{
	private boolean active ;
	private double initialTime ;
	private double counter ;
	private double duration ;
	// TODO testar essa classe com o jogo pausado 
	private static Set<TimeCounter> allCounters ;
	
	static 
	{
		allCounters = new HashSet<>() ;
	}
	
	public TimeCounter(double duration)
	{
		this.active = false ;
		this.counter = 0 ;
		this.duration = duration ;
		
		allCounters.add(this) ;
	}
	
	public double getCounter() { return counter ;}
	public double getDuration() {return duration ;}	
	public void setDuration(int duration) { this.duration = duration ;}

	public void start() { initialTime = System.nanoTime() * Math.pow(10, -9) ; active = true ;}
	public void stop() { active = false ;}
	public void reset() { initialTime = System.nanoTime() * Math.pow(10, -9) ; counter = 0 ;}
//	public double current()
//	{
//		if (!active) { return counter ;}
//		
//		return Math.min((System.nanoTime() - counter) * Math.pow(10, -9), duration)  ;
//	}
	public double rate() { return getCounter() / duration ;}
	public boolean isActive() { return active ;}
	public boolean finished() { return duration <= getCounter() ;}
	
	public void update()
	{
		if (!active) { return ;}
		
		counter = (System.nanoTime() * Math.pow(10, -9) - initialTime) ;
		if (duration <= counter)
		{
			counter = duration ;
			active = false ;
		}
	}
	
	public static void updateAll()
	{
		allCounters.forEach(TimeCounter::update);
	}

//	public void inc() { if (counter <= duration) {counter = System.nanoTime() - counter ;}}
	

	@Override
	public String toString()
	{
		return "TimeCounter [active = " + active + "time = " + getCounter() + ", duration = " + duration + "]";
	}
}
