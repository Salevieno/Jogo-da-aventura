package main;

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
import utilities.Util;

public class GameTimer
{
	private boolean active ;
	private double initialTime ;
	private double prevCounter ;
	private double elapsedTime ;
	private double duration ; // in seconds
	private double elapsedTimeAtStop ;
	
	private static double timeAtStop ;
	private static final Set<GameTimer> all = new HashSet<>() ;

	public GameTimer(double duration)
	{
		this.active = false ;
		this.elapsedTime = 0 ;
		this.prevCounter = elapsedTime ;
		this.duration = duration ;
		
		all.add(this) ;
	}
	
	public double getElapsedTime() { return elapsedTime ;}
	public double getDuration() {return duration ;}	
	public void setDuration(double duration) { this.duration = duration ;}
	private static double timeNowInSec() { return System.nanoTime() * Math.pow(10, -9) ;}
	
	public void start() { initialTime = timeNowInSec() ; active = true ;}
	public void startAtRate(double rate) { elapsedTimeAtStop = -rate * duration ; initialTime = timeNowInSec() ; active = true ;} // TODO armengue
	public void stop() { active = false ;}
	public void resume() { active = hasStarted() ;}
	public void reset() { initialTime = timeNowInSec() ; elapsedTimeAtStop = 0 ; elapsedTime = 0 ; prevCounter = 0 ;}
	public void restart() { reset() ; start() ;}
	public void restartAtRate(double rate) { reset() ; startAtRate(rate) ;}
	public double rate() { return elapsedTime / duration ;}
	public boolean crossedTime(double time) { return active && (elapsedTime % time <= prevCounter % time) ;}
	public boolean isActive() { return active ;}
	public boolean hasStarted() { return 0 < elapsedTime ;}
	public boolean hasFinished() { return duration <= elapsedTime ;}
	
	public void update()
	{
		if (!active) { return ;}
		
		prevCounter = elapsedTime ;
		elapsedTime = (timeNowInSec() - initialTime - elapsedTimeAtStop) ;
		if (hasFinished())
		{
			finish() ;
		}
	}

	private void finish()
	{
		elapsedTime = duration ;
		active = false ;
	}

	public void display(Point botLeftPos, Align align, Color color)
	{
		int stroke = DrawPrimitives.stdStroke ;
		Dimension barSize = new Dimension(6, 24) ;
		Dimension offset = new Dimension (barSize.width / 2 + (LiveBeingStatus.IMAGES.get(Attributes.stun).getWidth(null) + 5), barSize.height / 2) ;
		Dimension fillSize = new Dimension(barSize.width, (int) (barSize.height * rate())) ;
		Point rectPos = Util.translate(botLeftPos, offset.width, offset.height) ;
		
		GamePanel.getDP().drawRect(rectPos, align, barSize, stroke, null, Palette.colors[0], 1.0) ;
		GamePanel.getDP().drawRect(rectPos, align, fillSize, stroke, color, null, 1.0) ;
	}
	
	public void display(Point botLeftPos, Color color)
	{
		display(botLeftPos, Align.bottomLeft, Palette.colors[18]) ;
	}
	
	public void display(Point botLeftPos)
	{
		display(botLeftPos, Palette.colors[18]) ;
	}
	
	public static void stopAll()
	{
		timeAtStop = timeNowInSec() ;
		all.forEach(GameTimer::stop) ;
	}
	
	public static void resumeAll()
	{
		all.forEach(timeCounter -> timeCounter.elapsedTimeAtStop += timeCounter.hasStarted() ? timeNowInSec() - timeAtStop : 0) ;
		all.forEach(GameTimer::resume) ;
	}
	
	public static void updateAll()
	{
		all.forEach(GameTimer::update) ;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJson()
	{

        JSONObject content = new JSONObject();
        content.put("active", active);
        content.put("initialTime", initialTime);
        content.put("prevCounter", prevCounter);
        content.put("counter", elapsedTime);
        content.put("duration", duration);
        content.put("timeElapsedAtStop", elapsedTimeAtStop);
        
        return content ;
	}
	

	public static GameTimer fromJson(JSONObject jsonData)
	{
		double duration = (double) (Double) jsonData.get("duration") ;
		GameTimer timer = new GameTimer(duration) ;
		
		timer.active = (boolean) jsonData.get("active") ;
		timer.initialTime = (double) (Double) jsonData.get("initialTime") ;
		timer.prevCounter = (double) (Double) jsonData.get("prevCounter") ;
		timer.elapsedTime = (double) (Double) jsonData.get("counter") ;
		timer.elapsedTimeAtStop = (double) (Double) jsonData.get("timeElapsedAtStop") ;
		
		return timer ;
	}
	
	@Override
	public String toString()
	{
		return "TimeCounter [active = " + active + " time = " + elapsedTime + ", duration = " + duration + "]";
	}
}
