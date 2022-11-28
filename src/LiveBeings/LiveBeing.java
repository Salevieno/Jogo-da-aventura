package LiveBeings;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import Graphics.DrawPrimitives;
import Main.Game;
import Utilities.Size;
import Utilities.UtilS;
import Windows.AttributesWindow;

public class LiveBeing
{
	protected int level;
	protected Spells[] spell ;
	protected PersonalAttributes PA ;		// Personal attributes
	protected BattleAttributes BA ;			// Battle attributes
	protected MovingAnimations movingAni ;	// Moving animations
	protected AttributesWindow attWindow ;	// Attributes window
	
	public static Image[] StatusImages ;	// 0: Shield, 1: Stun, 2: Block, 3: Blood, 4: Poison, 5: Silence
	
	public LiveBeing(int level, PersonalAttributes PA, BattleAttributes BA, MovingAnimations movingAni, AttributesWindow attWindow)
	{
		this.level = level;
		this.PA = PA;
		this.BA = BA;
		this.movingAni = movingAni ;
		this.attWindow = attWindow ;

		Image ShieldIcon = new ImageIcon(Game.ImagesPath + "ShieldIcon.png").getImage() ;
		Image StunIcon = new ImageIcon(Game.ImagesPath + "StunIcon.png").getImage() ;
		Image BlockIcon = new ImageIcon(Game.ImagesPath + "BlockIcon.png").getImage() ;
		Image BloodIcon = new ImageIcon(Game.ImagesPath + "BloodIcon.png").getImage() ;
		Image PoisonIcon = new ImageIcon(Game.ImagesPath + "PoisonIcon.png").getImage() ;
		Image SilenceIcon = new ImageIcon(Game.ImagesPath + "SilenceIcon.png").getImage() ;
		StatusImages = new Image[] {ShieldIcon, StunIcon, BlockIcon, BloodIcon, PoisonIcon, SilenceIcon} ;
	}

	public PersonalAttributes getPA() {return PA ;}
	public BattleAttributes getBA() {return BA ;}
	public AttributesWindow getAttWindow() {return attWindow ;}
	public MovingAnimations getMovingAni() {return movingAni ;}
	
	public void IncActionCounters()
	{
		for (int a = 0 ; a <= PA.Actions.length - 1 ; a += 1)
		{
			if (PA.Actions[a][0] < PA.Actions[a][1])
			{
				PA.Actions[a][0] += 1 ;
			}	
		}
	}
	public void IncBattleActionCounters()
	{
		for (int a = 0 ; a <= BA.getBattleActions().length - 1 ; a += 1)
		{
			if (BA.getBattleActions()[a][0] < BA.getBattleActions()[a][1])
			{
				BA.getBattleActions()[a][0] += 1 ;
			}	
		}
	}
	public void ResetBattleActions()
	{
		BA.getBattleActions()[0][0] = 0 ;
		BA.getBattleActions()[0][2] = 0 ;
	}
	
	public boolean isAlive()
	{
		return 0 < PA.getLife()[0] ;
	}
	public boolean canAtk()
	{
		if (BA.getBattleActions()[0][2] == 1 & !BA.isStun())
		{
			return true ;
		}
		else
		{
			return false ;
		}
	}
	public boolean isSilent()
	{
		if (BA.getSpecialStatus()[4] <= 0)
		{
			return false ;
		}
		return true ;
	}
	public boolean isDefending()
	{
		if (BA.getCurrentAction().equals("D") & !canAtk())
		{
			return true ;
		}
		else
		{
			return false ;
		}
	}
	public void ActivateDef()
	{
		BA.getPhyDef()[1] += BA.getPhyDef()[0] ;
		BA.getMagDef()[1] += BA.getMagDef()[0] ;
	}
	public void DeactivateDef()
	{
		BA.getPhyDef()[1] += -BA.getPhyDef()[0] ;
		BA.getMagDef()[1] += -BA.getMagDef()[0] ;
	}
	public void train(Object[] playerAtkResult)
	{
		String effect = (String) playerAtkResult[1] ;
		String atkType = (String) playerAtkResult[2] ;
		if (atkType.equals("Physical"))	// Physical atk
		{
			BA.getPhyAtk()[2] += 0.025 / (BA.getPhyAtk()[2] + 1) ;					
		}
		if (effect.equals("Crit"))
		{
			if (PA.getJob() == 2)
			{
				BA.getCrit()[1] += 0.025 * 0.000212 / (BA.getCrit()[1] + 1) ;	// 100% after 10,000 hits starting from 0.12
			}
		}
		if (effect.equals("Hit"))
		{
			BA.getDex()[2] += 0.025 / (BA.getDex()[2] + 1) ;
		}
		if (atkType.equals("Spell"))
		{
			BA.getMagAtk()[2] += 0.025 / (BA.getMagAtk()[2] + 1) ;
		}
		if (atkType.equals("Defense"))
		{
			BA.getPhyDef()[2] += 0.025 / (BA.getPhyDef()[2] + 1) ;
			BA.getMagDef()[2] += 0.025 / (BA.getMagDef()[2] + 1) ;
		}
	}
	
	public void DrawAttributes(int style, DrawPrimitives DP)
	{
		Color[] colorPalette = Game.ColorPalette ;
		Size screenSize = Game.getScreen().getSize() ;
		
		ArrayList<Float> attRate = new ArrayList<>() ;
		ArrayList<Color> attColor = new ArrayList<>() ;
		if (2 <= PA.getLife().length)
		{
			attRate.add(PA.getLife()[0] / PA.getLife()[1]) ;
			attColor.add(colorPalette[6]) ;
		}
		if (2 <= PA.getMp().length)
		{
			attRate.add(PA.getMp()[0] / PA.getMp()[1]) ;
			attColor.add(colorPalette[5]) ;
		}
		if (2 <= PA.getExp().length)
		{
			attRate.add(PA.getExp()[0] / PA.getExp()[1]) ;
			attColor.add(colorPalette[1]) ;
		}
		if (2 <= PA.getSatiation().length)
		{
			attRate.add(PA.getSatiation()[0] / PA.getSatiation()[1]) ;
			attColor.add(colorPalette[2]) ;
		}
		if (2 <= PA.getThirst().length)
		{
			attRate.add(PA.getThirst()[0] / PA.getThirst()[1]) ;
			attColor.add(colorPalette[0]) ;
		}
		
		if (style == 0)
		{
			Point Pos = new Point((int)(PA.getPos().x - PA.getSize().x / 2), (int)(PA.getPos().y - PA.getSize().y - 5 * (1 + attRate.size()))) ;
			Size size = new Size((int)(0.05*screenSize.x), (int)(0.01*screenSize.y)) ;
			int Sy = (int)(0.01*screenSize.y) ;
			int barthick = 1 ;
			for (int att = 0; att <= attRate.size() - 1; att += 1)
			{
				DP.DrawRect(new Point(Pos.x, Pos.y + (att + 1) * Sy), "TopLeft", new Size((int)(attRate.get(att) * size.x), size.y), barthick, attColor.get(att), colorPalette[9], true) ;
			}
		}
		if (style == 1)
		{
			Point Pos = new Point((int)(0.01*screenSize.x), (int)(0.03*screenSize.y)) ;
			Size size = new Size((int)(0.13*screenSize.x), (int)(0.013*screenSize.y)) ;
			int Sy = size.y ;
			int barthick = 1 ;
			DP.DrawRoundRect(Pos, "TopLeft", new Size((int)(1.4 * size.x), (attRate.size() + 1) * Sy), barthick, colorPalette[8], colorPalette[4], true) ;
			for (int att = 0; att <= attRate.size() - 1; att += 1)
			{
				DP.DrawRect(new Point((int) (Pos.x + 0.3 * size.x), Pos.y + (att + 1) * Sy), "CenterLeft", size, barthick, null, colorPalette[9], true) ;
				DP.DrawRect(new Point((int) (Pos.x + 0.3 * size.x), Pos.y + (att + 1) * Sy), "CenterLeft", new Size((int)(attRate.get(att) * size.x), size.y), barthick, attColor.get(att), colorPalette[9], true) ;
			}
		}
	}
	public void DrawTimeBar(String relPos, Color color, DrawPrimitives DP)
	{
		Size size = new Size((int)(2 + PA.getSize().y/20), (int)(PA.getSize().y)) ;
		int RectT = 1 ;
		Color BackgroundColor = Game.ColorPalette[7] ;
		int counter = BA.getBattleActions()[0][0] ;
		int delay = BA.getBattleActions()[0][1] ;
		int mirror = UtilS.MirrorFromRelPos(relPos) ;
		Size offset = new Size (PA.getSize().x / 2 + (StatusImages[0].getWidth(null) + 5), -PA.getSize().y / 2) ;
		Point rectPos = new Point(PA.getPos().x + mirror * offset.x, PA.getPos().y + offset.y) ;
		DP.DrawRect(rectPos, "Center", size, RectT, BackgroundColor, Game.ColorPalette[9], true) ;
		DP.DrawRect(new Point(PA.getPos().x - size.x / 2, PA.getPos().y + size.y / 2), "BotLeft", new Size(size.x, size.y * counter / delay), RectT, color, Game.ColorPalette[9], false) ;
	}
	public void ShowEffectsAndStatusAnimation(Point Pos, int mirror, Size offset, Image[] IconImages, int[] effect, boolean isDefending, DrawPrimitives DP)
	{
		// effect 0: Stun, 1: Block, 2: Blood, 3: Poison, 4: Silence
		int Sy = (int)(1.1 * IconImages[0].getHeight(null)) ;
		if (isDefending)	// Defending
		{
			int ImageW = IconImages[0].getWidth(null) ;
			Point ImagePos = new Point(Pos.x + mirror * (ImageW + offset.x), Pos.y - offset.y) ;
			DP.DrawImage(IconImages[0], ImagePos, "Center") ;
		}
		if (0 < effect[0])	// Stun
		{
			Point ImagePos = new Point(Pos.x, Pos.y + mirror * offset.y) ;
			DP.DrawImage(IconImages[1], ImagePos, "Center") ;
		}
		for (int e = 1 ; e <= 4 - 1 ; e += 1)	// Block, blood, poison and silence
		{
			if (0 < effect[e])
			{
				int ImageW = IconImages[e + 1].getWidth(null) ;
				Point ImagePos = new Point(Pos.x + mirror * (ImageW + offset.x), Pos.y - offset.y + Sy) ;
				DP.DrawImage(IconImages[e + 1], ImagePos, "Center") ;
				Sy += IconImages[e + 1].getHeight(null) + 2 ;
			}
		}
	}
}
