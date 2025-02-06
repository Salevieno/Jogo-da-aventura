package liveBeings;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import attributes.Attributes;
import graphics.Align;
import main.Game;
import utilities.Directions;
import utilities.GameTimer;
import utilities.UtilS;

public class LiveBeingStatus
{
	
	private GameTimer counter ;
	private double intensity ;
	private final Image image ;

	public static final Map<Attributes, Image> images ;
	
	static
	{
		images = new HashMap<>() ;
		for (Attributes att : Attributes.values())
		{
			images.put(att, UtilS.loadImage("\\Status\\" + att.toString() + ".png")) ;
		}
	}
	
	public LiveBeingStatus(Attributes att)
	{
		counter = new GameTimer(0) ;
		intensity = 0.0 ;
		this.image = images.get(att) ;
	}

	public GameTimer getCounter() { return counter ;}
	public double getIntensity() { return intensity ;}
	public boolean isActive() { return counter.isActive() ;}

	public void inflictStatus(double intensity, double duration)
	{
		this.intensity = intensity ;
		counter.setDuration(duration) ;
		counter.start() ;
	}
	
	public void reset()
	{
		counter = new GameTimer(counter.getDuration()) ;
		intensity = 0.0 ;
	}
	
	public void display(Point pos, Dimension size, Directions dir)
	{
		if (!isActive()) { return ;}
		
		Game.DP.drawImage(image, pos, Align.center) ;
	}

	@Override
	public String toString()
	{
		return "LiveBeingStatus [counter=" + counter + ", intensity=" + intensity + ", image=" + image + "]";
	}

	public static LiveBeingStatus fromJson(JSONObject jsonData)
	{

//		int stun = (int) (long) jsonData.get("stun") ;
//		int block = (int) (long) jsonData.get("block") ;
//		int blood = (int) (long) jsonData.get("blood") ;
//		int poison = (int) (long) jsonData.get("poison") ;
//		int silence = (int) (long) jsonData.get("silence") ;
		LiveBeingStatus newLiveBeingStatus = new LiveBeingStatus(Attributes.agi) ;
//		newLiveBeingStatus.setStun(stun);
//		newLiveBeingStatus.setBlock(block);
////		newLiveBeingStatus.setBlood(blood);
//		newLiveBeingStatus.setPoison(poison);
//		newLiveBeingStatus.setSilence(silence);
		
		return newLiveBeingStatus ;
	}
	
//	@SuppressWarnings("unchecked")
	public JSONObject toJsonObject()
	{

        JSONObject content = new JSONObject();
//        content.put("stun", stun);
//        content.put("block", block);
////        content.put("blood", counters.get(Attributes.blood));
//        content.put("poison", poison);
//        content.put("silence", silence);
        
        return content ;
	}
	
}
