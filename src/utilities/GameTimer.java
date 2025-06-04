package utilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONObject;

import attributes.Attributes;
import graphics.Align;
import graphics.DrawPrimitives;
import liveBeings.LiveBeingStatus;
import main.Game;
import main.GamePanel;

public class GameTimer
{
	private boolean active ;
	private double initialTime ;
	private double prevCounter ;
	private double counter ;
	private double duration ;	// duration of the counter in seconds
	private double timeElapsedAtStop ;
	
	private static final Set<GameTimer> all ;
	private static double timeAtStop ;
	static 
	{
		all = new HashSet<>() ;
	}
	
	public GameTimer(double duration)
	{
		this.active = false ;
		this.counter = 0 ;
		this.prevCounter = counter ;
		this.duration = duration/1.0 ;
		
		all.add(this) ;
	}
	
	public double getCounter() { return counter ;}
	public double getDuration() {return duration ;}	
	public void setDuration(double duration) { this.duration = duration ;}
	private static double timeNowInSec() { return System.nanoTime() * Math.pow(10, -9) ;}
	
	public void start() { initialTime = timeNowInSec() ; active = true ;}
	public void stop() { active = false ;}
	public void resume() { active = hasStarted() ;}
	public void reset() { initialTime = timeNowInSec() ; timeElapsedAtStop = 0 ; counter = 0 ; prevCounter = 0 ;}
	public void restart() { reset() ; start() ;}
	public double rate() { return counter / duration ;}
	public boolean crossedTime(double time) { return active && prevCounter == 0 | (counter % time <= prevCounter % time) ;}
	public boolean isActive() { return active ;}
	public boolean hasStarted() { return 0 < counter ;}
	public boolean finished() { return duration <= counter ;}
	
	public void update()
	{
		if (!active) { return ;}
		
		prevCounter = counter ;
		counter = (timeNowInSec() - initialTime - timeElapsedAtStop) ;
		if (duration <= counter)
		{
			counter = duration ;
			active = false ;
		}
	}

	public void display(Point botLeftPos, Align align, Color color)
	{
		int stroke = DrawPrimitives.stdStroke ;
		Dimension barSize = new Dimension(6, 24) ;
		Dimension offset = new Dimension (barSize.width / 2 + (LiveBeingStatus.images.get(Attributes.stun).getWidth(null) + 5), barSize.height / 2) ;
		Dimension fillSize = new Dimension(barSize.width, (int) (barSize.height * rate())) ;
		Point rectPos = Util.Translate(botLeftPos, offset.width, offset.height) ;
		
		GamePanel.DP.drawRect(rectPos, align, barSize, stroke, null, Game.palette[0], 1.0) ;
		GamePanel.DP.drawRect(rectPos, align, fillSize, stroke, color, null, 1.0) ;
	}
	
	public void display(Point botLeftPos, Color color)
	{
		display(botLeftPos, Align.bottomLeft, Game.palette[18]) ;
	}
	
	public void display(Point botLeftPos)
	{
		display(botLeftPos, Game.palette[18]) ;
	}
	
	public static void stopAll()
	{
		timeAtStop = timeNowInSec() ;
		all.forEach(GameTimer::stop) ;
	}
	
	public static void resumeAll()
	{
		all.forEach(timeCounter -> timeCounter.timeElapsedAtStop += timeCounter.hasStarted() ? timeNowInSec() - timeAtStop : 0) ;
		all.forEach(GameTimer::resume) ;
	}
	
	public static void updateAll()
	{
		all.forEach(GameTimer::update);
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJson()
	{

        JSONObject content = new JSONObject();
        content.put("active", active);
        content.put("initialTime", initialTime);
        content.put("prevCounter", prevCounter);
        content.put("counter", counter);
        content.put("duration", duration);
        content.put("timeElapsedAtStop", timeElapsedAtStop);
        
        return content ;
	}
	

	public static GameTimer fromJson(JSONObject jsonData)
	{
		double duration = (double) (Double) jsonData.get("duration") ;
		GameTimer timer = new GameTimer(duration) ;
		
		timer.active = (boolean) jsonData.get("active") ;
		timer.initialTime = (double) (Double) jsonData.get("initialTime") ;
		timer.prevCounter = (double) (Double) jsonData.get("prevCounter") ;
		timer.counter = (double) (Double) jsonData.get("counter") ;
		timer.timeElapsedAtStop = (double) (Double) jsonData.get("timeElapsedAtStop") ;
		
		return timer ;
	}
	
	@Override
	public String toString()
	{
		return "TimeCounter [active = " + active + " time = " + counter + ", duration = " + duration + "]";
	}
}
