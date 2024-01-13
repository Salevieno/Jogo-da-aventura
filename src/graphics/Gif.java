package graphics;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;

import main.MainGame3_4;
import utilities.Align;
import utilities.TimeCounter;
import utilities.UtilG;

public class Gif
{
	private Image image ;
//	private int timeCounter ;
//	private int duration ;
	private boolean loop ;
	private boolean timeStopper ;
	
	private TimeCounter timeCounter ;
	
	public Gif(Image image, double duration, boolean loop, boolean timeStopper)
	{
		this.image = image;
		timeCounter = new TimeCounter(duration) ;
//		this.duration = duration;
		this.loop = loop;
		this.timeStopper = timeStopper;
	}
	
	public Image getImage() { return image ;}	
	public double getDuration() { return timeCounter.getDuration() ;}
	public TimeCounter getTimeCounter() { return timeCounter ;}

//	public void start() { timeCounter = 1 ;}
//	public void resetTimeCounter() { timeCounter = 0 ;}
//	private void incTimeCounter() { timeCounter += 1 ;}
	
	public boolean isLoop() { return loop ;}
	public boolean isTimeStopper() { return timeStopper ;}	
//	public boolean isStarting() { return (timeCounter.getCounter() == 0) ;}
	public boolean isDonePlaying() { return timeCounter.finished() ;}	
//	public boolean isPlaying() { return (0 < timeCounter & !isDonePlaying()) ;}
	public Dimension size() { return UtilG.getSize(image) ;}
	
	public void play(Point pos, Align alignment, DrawPrimitives DP)
	{
		if (timeCounter.finished())
		{
			if (timeStopper) { MainGame3_4.resumeGame() ;}
			
			return ;
		}
		

//		if (isStarting() & timeStopper)
//		{
//			Game.playStopTimeGif() ;
//		}
		Draw.gif(image, pos, alignment) ;
//		incTimeCounter() ;
	}
}
