package utilities;

public class FrameCounter
{
	private int counter ;
	private int duration ;
	
	public FrameCounter(int initialValue, int duration)
	{
		this.counter = initialValue ;
		this.duration = duration ;
	}
	
	public int getCounter() {return counter ;}
	public int getDuration() {return duration ;}	
	public void setDuration(int duration) { this.duration = duration ;}

	public double rate() { return (double) counter / duration ;}
	public boolean finished() {return counter == duration ;}
	
	public void inc() { if (counter <= duration - 1) {counter += 1 ;} }
	public void reset() {counter = 0 ;}
	

	@Override
	public String toString()
	{
		return "FrameCounter [counter=" + counter + ", duration=" + duration + "]";
	}
}
