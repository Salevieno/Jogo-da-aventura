package Maps;

public class Collectible
{
	private int level ;
	private int counter ;
	private int delay ;
	
	public Collectible(int level, int counter, int delay)
	{
		this.level = level ;
		this.counter = counter ;
		this.delay = delay ;
	}

	public int getLevel() { return level ; }
	public int getCounter() { return counter ; }
	public int getDelay() { return delay ; }
	
	public void incCounter() { counter = (counter + 1) % delay ;}
}
