package liveBeings;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;

import graphics.DrawingOnPanel;
import main.Game;
import utilities.Align;
import utilities.Directions;
import utilities.UtilG;

public class LiveBeingStatus
{
	private int life ;
	private int MP ;
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
	
	private static Image stunImage = UtilG.loadImage(Game.ImagesPath + "\\Status\\" + "Stun.png") ;
	private static Image blockImage = UtilG.loadImage(Game.ImagesPath + "\\Status\\" + "Block.png") ;
	private static Image bloodImage = UtilG.loadImage(Game.ImagesPath + "\\Status\\" + "Blood.png") ;
	private static Image poisonImage = UtilG.loadImage(Game.ImagesPath + "\\Status\\" + "Poison.png") ;
	private static Image silenceImage = UtilG.loadImage(Game.ImagesPath + "\\Status\\" + "Silence.png") ;
	
	
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
		return MP;
	}
	public void setMP(int MP)
	{
		this.MP = MP;
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
		if (0 < MP) {MP += -1 ; }
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
	
	public void display(Point liveBeingPos, Directions dir, DrawingOnPanel DP)
	{

//		int mirror = UtilS.MirrorFromRelPos(UtilS.RelPos(getPos(), creature.getPos())) ;
		int mirror = 1 ;
		Dimension offset = new Dimension(8, 4) ;
		if (0 < stun)	// Stun
		{
			Point ImagePos = new Point(liveBeingPos.x, liveBeingPos.y - offset.height) ;
			DP.DrawImage(stunImage, ImagePos, Align.center) ;
		}
		
		int[] statusList = new int[] {block, blood, poison, silence} ;
		Image[] statusImageList = new Image[] {blockImage, bloodImage, poisonImage, silenceImage} ;
		Point ImagePos = new Point(liveBeingPos.x + mirror * (statusImageList[0].getWidth(null) + offset.width), liveBeingPos.y) ;
		for (int statusID = 0 ; statusID <= statusList.length - 1 ; statusID += 1)
		{
			if (0 < statusList[statusID])
			{
				DP.DrawImage(statusImageList[statusID], ImagePos, Align.center) ;
				ImagePos.y += statusImageList[statusID].getHeight(null) + 2 ;
			}
		}
	}
	
	@Override
	public String toString()
	{
		return "LiveBeingStatus [life=" + life + ", MP=" + MP + ", phyAtk=" + phyAtk + ", magAtk=" + magAtk
				+ ", phyDef=" + phyDef + ", magDef=" + magDef + ", dex=" + dex + ", agi=" + agi + ", stun=" + stun
				+ ", block=" + block + ", blood=" + blood + ", poison=" + poison + ", silence=" + silence + "]";
	}
	
	
}
