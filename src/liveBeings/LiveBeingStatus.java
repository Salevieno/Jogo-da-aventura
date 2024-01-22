package liveBeings;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;

import org.json.simple.JSONObject;

import graphics.DrawPrimitives;
import utilities.Align;
import utilities.Directions;
import utilities.UtilG;
import utilities.UtilS;

public class LiveBeingStatus
{
	private int life ;
	private int mp ;
	private int phyAtk ;
	private int magAtk ;
	private int phyDef ;
	private int magDef ;
	private int dex ;
	private int agi ;
	
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
	
	
	public int getLife()
	{
		return life;
	}
	public void setLife(int life)
	{
		this.life = life;
	}
	public int getMP()
	{
		return mp;
	}
	public void setMP(int mp)
	{
		this.mp = mp;
	}
	public int getPhyAtk()
	{
		return phyAtk;
	}
	public void setPhyAtk(int phyAtk)
	{
		this.phyAtk = phyAtk;
	}
	public int getMagAtk()
	{
		return magAtk;
	}
	public void setMagAtk(int magAtk)
	{
		this.magAtk = magAtk;
	}
	public int getPhyDef()
	{
		return phyDef;
	}
	public void setPhyDef(int phyDef)
	{
		this.phyDef = phyDef;
	}
	public int getMagDef()
	{
		return magDef;
	}
	public void setMagDef(int magDef)
	{
		this.magDef = magDef;
	}
	public int getDex()
	{
		return dex;
	}
	public void setDex(int dex)
	{
		this.dex = dex;
	}
	public int getAgi()
	{
		return agi;
	}
	public void setAgi(int agi)
	{
		this.agi = agi;
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
	
	
	public void receiveStatus(int[] AppliedStatus)
	{// TODO include all status
		stun = Math.max(stun, AppliedStatus[0]) ;
		block = Math.max(block, AppliedStatus[1]) ;
		blood = Math.max(blood, AppliedStatus[2]) ;
		poison = Math.max(poison, AppliedStatus[3]) ;
		silence = Math.max(silence, AppliedStatus[4]) ;
	}
	public void decreaseStatus()
	{
		if (0 < life) {life += -1 ; }
		if (0 < mp) {mp += -1 ; }
		if (0 < phyAtk) {phyAtk += -1 ; }
		if (0 < magAtk) {magAtk += -1 ; }
		if (0 < phyDef) {phyDef += -1 ; }
		if (0 < magDef) {magDef += -1 ; }
		if (0 < dex) {dex += -1 ; }
		if (0 < agi) {agi += -1 ; }
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
			Point imgPos = UtilG.Translate(bodyCenter, 0, -offset.y) ;
			DP.drawImage(stunImage, imgPos, Align.center) ;
		}
		
		int[] statusList = new int[] {block, blood, poison, silence} ;
		Image[] imgList = new Image[] {blockImage, bloodImage, poisonImage, silenceImage} ;
		Point imgPos = UtilG.Translate(bodyCenter, mirror * offset.x, -offset.y) ;
		for (int i = 0 ; i <= statusList.length - 1 ; i += 1)
		{
			if (statusList[i] <= 0) { continue ;}
			
			DP.drawImage(imgList[i], imgPos, Align.center) ;
			imgPos.y += imgList[i].getHeight(null) + 2 ;
		}
	}
	

	public static LiveBeingStatus fromJson(JSONObject jsonData)
	{

		int life = (int) (long) jsonData.get("life") ;
		int mp = (int) (long) jsonData.get("mp") ;
		int phyAtk = (int) (long) jsonData.get("phyAtk") ;
		int magAtk = (int) (long) jsonData.get("magAtk") ;
		int phyDef = (int) (long) jsonData.get("phyDef") ;
		int magDef = (int) (long) jsonData.get("magDef") ;
		int dex = (int) (long) jsonData.get("dex") ;
		int agi = (int) (long) jsonData.get("agi") ;
		int stun = (int) (long) jsonData.get("stun") ;
		int block = (int) (long) jsonData.get("block") ;
		int blood = (int) (long) jsonData.get("blood") ;
		int poison = (int) (long) jsonData.get("poison") ;
		int silence = (int) (long) jsonData.get("silence") ;
		LiveBeingStatus newLiveBeingStatus = new LiveBeingStatus() ;
		newLiveBeingStatus.setLife(life) ;
		newLiveBeingStatus.setMP(mp) ;
		newLiveBeingStatus.setPhyAtk(phyAtk);
		newLiveBeingStatus.setMagAtk(magAtk);
		newLiveBeingStatus.setPhyDef(phyDef);
		newLiveBeingStatus.setMagDef(magDef);
		newLiveBeingStatus.setDex(dex);
		newLiveBeingStatus.setAgi(agi);
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
        content.put("life", life);
        content.put("mp", mp);
        content.put("phyAtk", phyAtk);
        content.put("magAtk", magAtk);
        content.put("phyDef", phyDef);
        content.put("magDef", magDef);
        content.put("dex", dex);
        content.put("agi", agi);
        content.put("stun", stun);
        content.put("block", block);
        content.put("blood", blood);
        content.put("poison", poison);
        content.put("silence", silence);
        
        return content ;
	}
	
	@Override
	public String toString()
	{
		return "LiveBeingStatus [life=" + life + ", MP=" + mp + ", phyAtk=" + phyAtk + ", magAtk=" + magAtk
				+ ", phyDef=" + phyDef + ", magDef=" + magDef + ", dex=" + dex + ", agi=" + agi + ", stun=" + stun
				+ ", block=" + block + ", blood=" + blood + ", poison=" + poison + ", silence=" + silence + "]";
	}
	
	
}
