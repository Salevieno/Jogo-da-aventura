package graphics;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import main.MainGame3_4;
import utilities.Align;
import utilities.TimeCounter;
import utilities.UtilG;

public class Gif
{
	private Image image ;
	Point pos ;
	Align align ;
	boolean hasPlayed ;
	private TimeCounter counter ;
	private boolean loop ;
	private boolean timeStopper ;
	
	private static final List<Gif> all = new ArrayList<>() ;
	
	public Gif(String name, Image image, double duration, boolean loop, boolean timeStopper)
	{
		this.image = image;
		hasPlayed = false ;
		counter = new TimeCounter(duration) ;
		this.loop = loop;
		this.timeStopper = timeStopper;
	}
	
	public Image getImage() { return image ;}	
	public double getDuration() { return counter.getDuration() ;}
	public TimeCounter getCounter() { return counter ;}
	public boolean hasPlayed() { return hasPlayed ;}
	
	public boolean isLoop() { return loop ;}
	public boolean isTimeStopper() { return timeStopper ;}
	public boolean isActive() { return counter.isActive() ;}
	public boolean isDonePlaying() { return counter.finished() ;}
	public Dimension size() { return UtilG.getSize(image) ;}

	public void start(Point pos, Align align)
	{
		if (counter.isActive()) { return ;}
		
		this.pos = pos ;
		this.align = align ;
		all.add(this) ;
		counter.start() ;
	}
	
	private void play()
	{
		if (counter.finished())
		{
			if (timeStopper) { MainGame3_4.resumeGame() ;}
			end() ;
			return ;
		}
		

//		if (isStarting() & timeStopper)
//		{
//			Game.playStopTimeGif() ;
//		}
		Draw.gif(image, pos, align) ;
//		incTimeCounter() ;
	}
	
	private void end()
	{
		hasPlayed = true ;
		counter.reset() ;
		this.getImage().flush() ;
		all.remove(this) ;
	}
	
	public static void playAll()
	{
		for (int i = 0 ; i <= all.size() - 1; i += 1)
		{
			all.get(i).play() ;
		}
	}

	@Override
	public String toString() {
		return "Gif [image=" + image + ", pos=" + pos + ", align=" + align + ", counter=" + counter + "]";
	}
	
	
}
