package graphics;

import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import main.Game;
import main.MainGame3_4;
import utilities.Align;

public class Gif
{
	private Image image ;
	private int timeCounter ;
	private int duration ;
	private boolean loop ;
	private boolean timeStopper ;
	
	public Gif(Image image, int duration, boolean loop, boolean timeStopper)
	{
		this.image = image;
		timeCounter = 0 ;
		this.duration = duration;
		this.loop = loop;
		this.timeStopper = timeStopper;
	}
	
	public Image getImage()
	{
		return image;
	}
	public void resetTimeCounter()
	{
		timeCounter = 0 ;
	}
	public void incTimeCounter()
	{
		timeCounter += 1 ;
	}
	public boolean isLoop()
	{
		return loop;
	}
	public boolean isTimeStopper()
	{
		return timeStopper;
	}
	public boolean isStarting()
	{
		return (timeCounter == 0);
	}
	public boolean isDonePlaying()
	{
		return (timeCounter == duration);
	}
	
	public void play(Point pos, Align alignment, DrawingOnPanel DP)
	{
		if (!isDonePlaying())
		{
			if (isStarting() & timeStopper)
			{
				Game.playStopTimeGif();
			}
			DP.DrawGif(image, pos, alignment) ;
			incTimeCounter();
		}
		else
		{
			if (timeStopper)
			{
				MainGame3_4.resumeGame();
			}
		}
	}
}