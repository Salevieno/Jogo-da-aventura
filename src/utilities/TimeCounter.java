package utilities;

import java.util.HashSet;
import java.util.Set;

public class TimeCounter
{
	private boolean active ;
	private double initialTime ;
	private double counter ;
	private double duration ;	// duration of the counter in seconds
	
	private static final Set<TimeCounter> all ;
	private static double timeAtStop ;
	private double timeElapsedAtStop ;
	static 
	{
		all = new HashSet<>() ;
	}
	
	public TimeCounter(double duration)
	{
		this.active = false ;
		this.counter = 0 ;
		this.duration = duration ;
		
		all.add(this) ;
	}
	
	public double getCounter() { return counter ;}
	public double getDuration() {return duration ;}	
	public void setDuration(double duration) { this.duration = duration ;}
	private static double timeNowInSec() { return System.nanoTime() * Math.pow(10, -9) ;}
	
	public void start() { initialTime = timeNowInSec() ; active = true ;}
	public void stop() { active = false ;}
	public void resume() { active = hasStarted() ;}
	public void reset() { initialTime = timeNowInSec() ; timeElapsedAtStop = 0 ; counter = 0 ;}
	public double rate() { return counter / duration ;}
	public boolean isActive() { return active ;}
	public boolean hasStarted() { return 0 < counter ;}
	public boolean finished() { return duration <= counter ;}
	
	public void update()
	{
		if (!active) { return ;}
		
		counter = (timeNowInSec() - initialTime - timeElapsedAtStop) ;
		if (duration <= counter)
		{
			counter = duration ;
			active = false ;
		}
	}
	
	public static void stopAll()
	{
		timeAtStop = timeNowInSec() ;
		all.forEach(TimeCounter::stop) ;
	}
	
	public static void resumeAll()
	{
		all.forEach(timeCounter -> timeCounter.timeElapsedAtStop += timeCounter.hasStarted() ? timeNowInSec() - timeAtStop : 0) ;
		all.forEach(TimeCounter::resume) ;
	}
	
	public static void updateAll()
	{
		all.forEach(TimeCounter::update);
	}

	@Override
	public String toString()
	{
		return "TimeCounter [active = " + active + " time = " + counter + ", duration = " + duration + "]";
	}
}
