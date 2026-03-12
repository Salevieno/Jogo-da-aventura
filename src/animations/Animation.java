package animations ;

import java.util.ArrayList;
import java.util.List;

import main.GameTimer;

public abstract class Animation 
{
	protected final GameTimer timer ;
	
	private static final List<Animation> all = new ArrayList<>() ;

	protected Animation(double duration)
	{
		this.timer = new GameTimer(duration) ;
	}

	protected abstract void play() ;
	
	public static List<Animation> getAll() { return all ;}
	
    public void start()
    {
        all.add(this) ;
		timer.start() ;
    }
	
	public static void playAll()
	{
		all.forEach(Animation::play) ;
		for (int i = 0; i < all.size(); i++)
		{
			all.get(i).finishWhenDone() ;
		}
	}

	private void finishWhenDone()
	{
		if (!timer.hasFinished()) { return ;}

		timer.reset() ;
		all.remove(this) ;
	}
}