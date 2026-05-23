package animations ;

import java.util.ArrayList;
import java.util.List;

import main.GameTimer;

public abstract class Animation 
{
	protected final GameTimer timer ;
	
	private static final List<Animation> ALL = new ArrayList<>() ;

	protected Animation(double duration)
	{
		this.timer = new GameTimer(duration) ;
	}

	protected abstract void play() ;
	
	public static List<Animation> getAll() { return ALL ;}
	
    public void start()
    {
        ALL.add(this) ;
		timer.start() ;
    }
	
	public static void playAll()
	{
		ALL.forEach(Animation::play) ;
		for (int i = 0; i < ALL.size(); i++)
		{
			ALL.get(i).finishWhenDone() ;
		}
	}

	private void finishWhenDone()
	{
		if (!timer.hasFinished()) { return ;}

		timer.reset() ;
		ALL.remove(this) ;
	}
}