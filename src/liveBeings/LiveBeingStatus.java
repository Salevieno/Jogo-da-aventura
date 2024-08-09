package liveBeings;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;

import org.json.simple.JSONObject;

import graphics.DrawPrimitives;
import libUtil.Align;
import libUtil.Util;
import utilities.Directions;
import utilities.UtilS;

public class LiveBeingStatus
{
	private int stun ;
	private int block ;
	private int blood ;
	private int poison ;
	private int silence ;
	
	public static final Image stunImage = UtilS.loadImage("\\Status\\" + "Stun.png") ;
	public static final Image blockImage = UtilS.loadImage("\\Status\\" + "Block.png") ;
	public static final Image bloodImage = UtilS.loadImage("\\Status\\" + "Blood.png") ;
	public static final Image poisonImage = UtilS.loadImage("\\Status\\" + "Poison.png") ;
	public static final Image silenceImage = UtilS.loadImage("\\Status\\" + "Silence.png") ;
	
	public LiveBeingStatus()
	{
		
	}
	
	public LiveBeingStatus(LiveBeingStatus liveBeingStatus)
	{
		this.stun = liveBeingStatus.getStun() ;
		this.block = liveBeingStatus.getBlock() ;
		this.blood = liveBeingStatus.getBlood() ;
		this.poison = liveBeingStatus.getPoison() ;
		this.silence = liveBeingStatus.getSilence() ;
	}
	
	public int getStun()
	{
		return stun;
	}
	public void setStun(int stun)
	{
		this.stun = stun;
	}
	public int getBlock()
	{
		return block;
	}
	public void setBlock(int block)
	{
		this.block = block;
	}
	public int getBlood()
	{
		return blood;
	}
	public void setBlood(int blood)
	{
		this.blood = blood;
	}
	public int getPoison()
	{
		return poison;
	}
	public void setPoison(int poison)
	{
		this.poison = poison;
	}
	public int getSilence()
	{
		return silence;
	}
	public void setSilence(int silence)
	{
		this.silence = silence;
	}
	
	public void resetStun()
	{
		this.stun = 0;
	}
	public void resetBlock()
	{
		this.block = 0;
	}
	public void resetBlood()
	{
		this.blood = 0;
	}
	public void resetPoison()
	{
		this.poison = 0;
	}
	public void resetSilence()
	{
		this.silence = 0;
	}
	
	public void receiveStatus(int[] AppliedStatus)
	{
		stun = Math.max(stun, AppliedStatus[0]) ;
		block = Math.max(block, AppliedStatus[1]) ;
		blood = Math.max(blood, AppliedStatus[2]) ;
		poison = Math.max(poison, AppliedStatus[3]) ;
		silence = Math.max(silence, AppliedStatus[4]) ;
	}
	public void decreaseStatus()
	{
		if (0 < stun) {stun += -1 ; }
		if (0 < block) {block += -1 ; }
		if (0 < blood) {blood += -1 ; }
		if (0 < poison) {poison += -1 ; }
		if (0 < silence) {silence += -1 ; }
	}
	
	public void display(Point bodyCenter, Dimension size, Directions dir, DrawPrimitives DP)
	{

//		int mirror = UtilS.MirrorFromRelPos(UtilS.RelPos(getPos(), creature.getPos())) ;
		int mirror = -1 ;
		Point offset = new Point(size.width / 2 - 4, size.height / 2 + 4) ;

		if (0 < stun)
		{
			Point imgPos = Util.Translate(bodyCenter, 0, -offset.y) ;
			DP.drawImage(stunImage, imgPos, Align.center) ;
		}
		
		int[] statusList = new int[] {block, blood, poison, silence} ;
		Image[] imgList = new Image[] {blockImage, bloodImage, poisonImage, silenceImage} ;
		Point imgPos = Util.Translate(bodyCenter, mirror * offset.x, -offset.y) ;
		for (int i = 0 ; i <= statusList.length - 1 ; i += 1)
		{
			if (statusList[i] <= 0) { continue ;}
			
			DP.drawImage(imgList[i], imgPos, Align.center) ;
			imgPos.y += imgList[i].getHeight(null) + 2 ;
		}
	}
	

	public static LiveBeingStatus fromJson(JSONObject jsonData)
	{

		int stun = (int) (long) jsonData.get("stun") ;
		int block = (int) (long) jsonData.get("block") ;
		int blood = (int) (long) jsonData.get("blood") ;
		int poison = (int) (long) jsonData.get("poison") ;
		int silence = (int) (long) jsonData.get("silence") ;
		LiveBeingStatus newLiveBeingStatus = new LiveBeingStatus() ;
		newLiveBeingStatus.setStun(stun);
		newLiveBeingStatus.setBlock(block);
		newLiveBeingStatus.setBlood(blood);
		newLiveBeingStatus.setPoison(poison);
		newLiveBeingStatus.setSilence(silence);
		
		return newLiveBeingStatus ;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJsonObject()
	{

        JSONObject content = new JSONObject();
        content.put("stun", stun);
        content.put("block", block);
        content.put("blood", blood);
        content.put("poison", poison);
        content.put("silence", silence);
        
        return content ;
	}
	
}
