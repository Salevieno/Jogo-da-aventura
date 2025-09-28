package liveBeings;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import attributes.Attributes;
import graphics.Align;
import main.Directions;
import main.Game;
import main.GamePanel;
import main.GameTimer;
import main.Path;


public class LiveBeingStatus
{
	
	private GameTimer timer ;
	private double intensity ;
	private final Image image ;

	public static final Map<Attributes, Image> images ;
	
	static
	{
		images = new HashMap<>() ;
		for (Attributes att : Attributes.values())
		{
			images.put(att, Game.loadImage(Path.STATUS_IMG + att.toString() + ".png")) ;
		}
	}
	
	public LiveBeingStatus(Attributes att)
	{
		timer = new GameTimer(0) ;
		intensity = 0.0 ;
		this.image = images.get(att) ;
	}

	public GameTimer getTimer() { return timer ;}
	public double getIntensity() { return intensity ;}
	public boolean isActive() { return timer.isActive() ;}

	public void inflictStatus(double intensity, double duration)
	{
		this.intensity = intensity ;
		timer.setDuration(duration) ;
		timer.start() ;
	}
	
	public void reset()
	{
		timer = new GameTimer(timer.getDuration()) ;
		intensity = 0.0 ;
	}
	
	public void display(Point pos, Dimension size, Directions dir)
	{
		if (!isActive()) { return ;}
		
		GamePanel.DP.drawImage(image, pos, Align.center) ;
	}

	@Override
	public String toString()
	{
		return "LiveBeingStatus [timer=" + timer + ", intensity=" + intensity + ", image=" + image + "]";
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
