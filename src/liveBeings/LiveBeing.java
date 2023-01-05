package liveBeings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import graphics.DrawingOnPanel;
import main.Game;
import utilities.Align;
import utilities.UtilS;
import windows.PlayerAttributesWindow;

public class LiveBeing
{
	protected int level;
	protected ArrayList<Spell> spells ;
	protected PersonalAttributes PA ;		// Personal attributes
	protected BattleAttributes BA ;			// Battle attributes
	protected MovingAnimations movingAni ;	// Moving animations
	protected PlayerAttributesWindow attWindow ;	// Attributes window
	
	public static Image[] StatusImages ;	// 0: Shield, 1: Stun, 2: Block, 3: Blood, 4: Poison, 5: Silence
	public static final String[] BattleKeys = new String[] {"A", "D"} ;
	
	public LiveBeing(int level, PersonalAttributes PA, BattleAttributes BA, MovingAnimations movingAni, PlayerAttributesWindow attWindow)
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
	public PlayerAttributesWindow getAttWindow() {return attWindow ;}
	public MovingAnimations getMovingAni() {return movingAni ;}
	
	public void IncActionCounters()
	{
		/*for (int a = 0 ; a <= PA.Actions.length - 1 ; a += 1)
		{
			if (PA.Actions[a][0] < PA.Actions[a][1])
			{
				PA.Actions[a][0] += 1 ;
			}	
		}*/
		PA.mpCounter.inc() ;
		PA.satiationCounter.inc() ;
		PA.moveCounter.inc() ;
	}
	public void IncBattleActionCounters()
	{
		// TODO get rid of battle action counters
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
	
	public void resetCombo()
	{
		PA.setCombo(new ArrayList<String>()) ;
	}
	
	public void UpdateCombo()
	{
		if (!PA.currentAction.equals(""))
		{
			if (PA.getCombo().size() <= 9)
			{
				PA.getCombo().add(PA.currentAction) ;
			}
			else
			{
				PA.getCombo().add(0, PA.currentAction) ;
				PA.getCombo().remove(PA.getCombo().size() - 1) ;
			}
		}
	}
	
	public boolean isAlive() {return 0 < PA.getLife().getCurrentValue() ;}
	public boolean canAtk() {return BA.getBattleActions()[0][2] == 1 & !BA.isStun() ;}
	public boolean isSilent() {return BA.getSpecialStatus()[4] <= 0 ;}
	public boolean isDefending() {return (PA.getCurrentAction().equals(BattleKeys[1]) & !canAtk()) ;}
	
	public void ActivateDef()
	{
		BA.getPhyDef().incBonus(BA.getPhyDef().getBaseValue()) ;
		BA.getMagDef().incBonus(BA.getMagDef().getBaseValue()) ;
	}
	public void DeactivateDef()
	{
		BA.getPhyDef().incBonus(-BA.getPhyDef().getBaseValue()) ;
		BA.getMagDef().incBonus(-BA.getMagDef().getBaseValue()) ;
	}
	public void train(Object[] playerAtkResult)
	{
		String effect = (String) playerAtkResult[1] ;
		String atkType = (String) playerAtkResult[2] ;
		if (atkType.equals("Physical"))	// Physical atk
		{
			BA.getPhyAtk().incTrain(0.025 / (BA.getPhyAtk().getTrain() + 1)) ;					
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
			BA.getDex().incTrain(0.025 / (BA.getDex().getTrain() + 1)) ;
		}
		if (atkType.equals("Spell"))
		{
			BA.getMagAtk().incTrain(0.025 / (BA.getMagAtk().getTrain() + 1)) ;
		}
		if (atkType.equals("Defense"))
		{
			BA.getPhyDef().incTrain(0.025 / (BA.getPhyDef().getTrain() + 1)) ;
			BA.getMagDef().incTrain(0.025 / (BA.getMagDef().getTrain() + 1)) ;
		}
	}
	
	public void DrawAttributes(int style, DrawingOnPanel DP)
	{
		Color[] colorPalette = Game.ColorPalette ;
		Dimension screenSize = Game.getScreen().getSize() ;
		
		ArrayList<Double> attRate = new ArrayList<>() ;
		ArrayList<Color> attColor = new ArrayList<>() ;
		
		attRate.add(PA.getLife().getRate()) ;
		attColor.add(colorPalette[6]) ;
		attRate.add(PA.getMp().getRate()) ;
		attColor.add(colorPalette[5]) ;
		attRate.add(PA.getExp().getRate()) ;
		attColor.add(colorPalette[1]) ;
		attRate.add(PA.getSatiation().getRate()) ;
		attColor.add(colorPalette[2]) ;
		attRate.add(PA.getThirst().getRate()) ;
		attColor.add(colorPalette[0]) ;
		
		if (style == 0)
		{
			Point Pos = new Point((int)(PA.getPos().x - PA.getSize().width / 2), (int)(PA.getPos().y - PA.getSize().height - 5 * (1 + attRate.size()))) ;
			Dimension size = new Dimension((int)(0.05*screenSize.width), (int)(0.01*screenSize.height)) ;
			int Sy = (int)(0.01*screenSize.height) ;
			int barthick = 1 ;
			for (int att = 0; att <= attRate.size() - 1; att += 1)
			{
				DP.DrawRect(new Point(Pos.x, Pos.y + (att + 1) * Sy), Align.topLeft, new Dimension((int)(attRate.get(att) * size.width), size.height), barthick, attColor.get(att), colorPalette[9]) ;
			}
		}
		if (style == 1)
		{
			Point Pos = new Point((int)(0.01*screenSize.width), (int)(0.03*screenSize.height)) ;
			Dimension size = new Dimension((int)(0.13*screenSize.width), (int)(0.013*screenSize.height)) ;
			int Sy = size.height ;
			int barthick = 1 ;
			DP.DrawRoundRect(Pos, Align.topLeft, new Dimension((int)(1.4 * size.width), (attRate.size() + 1) * Sy), barthick, colorPalette[8], colorPalette[4], true) ;
			for (int att = 0; att <= attRate.size() - 1; att += 1)
			{
				DP.DrawRect(new Point((int) (Pos.x + 0.3 * size.width), Pos.y + (att + 1) * Sy), Align.centerLeft, size, barthick, null, colorPalette[9]) ;
				DP.DrawRect(new Point((int) (Pos.x + 0.3 * size.width), Pos.y + (att + 1) * Sy), Align.centerLeft, new Dimension((int)(attRate.get(att) * size.width), size.height), barthick, attColor.get(att), colorPalette[9]) ;
			}
		}
	}
	public void DrawTimeBar(String relPos, Color color, DrawingOnPanel DP)
	{
		Dimension size = new Dimension((int)(2 + PA.getSize().height/20), (int)(PA.getSize().height)) ;
		int RectT = 1 ;
		Color BackgroundColor = Game.ColorPalette[7] ;
		int counter = BA.getBattleActions()[0][0] ;
		int delay = BA.getBattleActions()[0][1] ;
		int mirror = UtilS.MirrorFromRelPos(relPos) ;
		Dimension offset = new Dimension (PA.getSize().width / 2 + (StatusImages[0].getWidth(null) + 5), -PA.getSize().height / 2) ;
		Point rectPos = new Point(PA.getPos().x + mirror * offset.width, PA.getPos().y + offset.height) ;
		DP.DrawRect(rectPos, Align.center, size, RectT, BackgroundColor, Game.ColorPalette[9]) ;
		DP.DrawRect(new Point(PA.getPos().x - size.width / 2, PA.getPos().y + size.height / 2), Align.bottomLeft, new Dimension(size.width, size.height * counter / delay), RectT, color, null) ;
	}
	public void ShowEffectsAndStatusAnimation(Point Pos, int mirror, Dimension offset, Image[] IconImages, int[] effect, boolean isDefending, DrawingOnPanel DP)
	{
		// effect 0: Stun, 1: Block, 2: Blood, 3: Poison, 4: Silence
		int Sy = (int)(1.1 * IconImages[0].getHeight(null)) ;
		if (isDefending)	// Defending
		{
			int ImageW = IconImages[0].getWidth(null) ;
			Point ImagePos = new Point(Pos.x + mirror * (ImageW + offset.width), Pos.y - offset.height) ;
			DP.DrawImage(IconImages[0], ImagePos, Align.center) ;
		}
		if (0 < effect[0])	// Stun
		{
			Point ImagePos = new Point(Pos.x, Pos.y + mirror * offset.height) ;
			DP.DrawImage(IconImages[1], ImagePos, Align.center) ;
		}
		for (int e = 1 ; e <= 4 - 1 ; e += 1)	// Block, blood, poison and silence
		{
			if (0 < effect[e])
			{
				int ImageW = IconImages[e + 1].getWidth(null) ;
				Point ImagePos = new Point(Pos.x + mirror * (ImageW + offset.width), Pos.y - offset.height + Sy) ;
				DP.DrawImage(IconImages[e + 1], ImagePos, Align.center) ;
				Sy += IconImages[e + 1].getHeight(null) + 2 ;
			}
		}
	}
}
