package LiveBeings;

import GameComponents.MovingAnimations;
import Windows.AttributesWindow;

public class LiveBeing
{
	protected int level;
	protected PersonalAttributes PA ;		// Personal attributes
	protected BattleAttributes BA ;			// Battle attributes
	protected static MovingAnimations movingAni ;	// Moving animations
	protected AttributesWindow attWindow ;	// Attributes window
	
	public LiveBeing(int level, PersonalAttributes PA, BattleAttributes BA, MovingAnimations movingAni, AttributesWindow attWindow)
	{
		this.level = level;
		this.PA = PA;
		this.BA = BA;
		LiveBeing.movingAni = movingAni ;
		this.attWindow = attWindow ;
	}

	public PersonalAttributes getPA() {return PA ;}
	public BattleAttributes getBA() {return BA ;}
	public AttributesWindow getAttWindow() {return attWindow ;}
	public static MovingAnimations getMovingAni() {return movingAni ;}
	
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

	public void display()
	{
		
	}
}
