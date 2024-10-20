package graphics;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import libUtil.Align;
import libUtil.Util;
import main.MainGame3_4;
import utilities.TimeCounter;

public class Gif
{
	private Image image ;
	Point pos ;
	Align align ;
	boolean hasPlayed ;
	private TimeCounter counter ;
	private boolean loop ;
	private boolean timeStopper ;
	
	private static final List<Gif> activeGifs = new ArrayList<>() ;
	
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
	public Dimension size() { return Util.getSize(image) ;}

	public void start(Point pos, Align align)
	{
		if (counter.isActive()) { return ;}
		
		this.pos = pos ;
		this.align = align ;
		activeGifs.add(this) ;
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
		
		Draw.gif(image, pos, align) ;
	}
	
	private void end()
	{
		hasPlayed = true ;
		counter.reset() ;
		this.getImage().flush() ;
		activeGifs.remove(this) ;
	}
	
	public static void playAll()
	{
		activeGifs.forEach(gif -> gif.play()) ;
	}

	@Override
	public String toString() {
		return "Gif [image=" + image + ", pos=" + pos + ", align=" + align + ", counter=" + counter + "]";
	}
	
	
}
