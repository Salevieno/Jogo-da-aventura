package Utilities;

public class TimeCounter
{
	private int counter ;
	private int duration ;
	
	public TimeCounter(int initialValue, int duration)
	{
		this.counter = initialValue ;
		this.duration = duration ;
	}
	
	public int getCounter() {return counter ;}
	public int getDuration() {return duration ;}
	
	public void inc() { if (counter <= duration - 1) {counter += 1 ;} }
	public void reset() {counter = 0 ;}
	
	public boolean finished() {return counter == duration ;}

	@Override
	public String toString() {
		return "TimeCounter [counter=" + counter + ", duration=" + duration + "]";
	}
}
