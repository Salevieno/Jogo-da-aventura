package LiveBeings;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import Graphics.DrawPrimitives;
import Main.Game;
import Utilities.Size;
import Windows.AttributesWindow;

public class LiveBeing
{
	protected int level;
	protected PersonalAttributes PA ;		// Personal attributes
	protected BattleAttributes BA ;			// Battle attributes
	protected MovingAnimations movingAni ;	// Moving animations
	protected AttributesWindow attWindow ;	// Attributes window
	
	public LiveBeing(int level, PersonalAttributes PA, BattleAttributes BA, MovingAnimations movingAni, AttributesWindow attWindow)
	{
		this.level = level;
		this.PA = PA;
		this.BA = BA;
		this.movingAni = movingAni ;
		this.attWindow = attWindow ;
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

	public void drawAttributes(int style, DrawPrimitives DP)
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
}
