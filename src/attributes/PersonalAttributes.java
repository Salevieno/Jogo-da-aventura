package attributes ;

import java.awt.Point;

import utilities.Directions;
import utilities.UtilG;

public class PersonalAttributes
{
	
	private BasicAttribute Life ;
	private BasicAttribute Mp ;
	private BasicAttribute Exp ;
	private BasicAttribute Satiation ;
	private BasicAttribute Thirst ;
	
	public PersonalAttributes(BasicAttribute Life, BasicAttribute Mp, BasicAttribute Exp, BasicAttribute Satiation, BasicAttribute Thirst)
	{
		this.Life = Life ;
		this.Mp = Mp ;
		this.Exp = Exp ;
		this.Satiation = Satiation ;
		this.Thirst = Thirst ;
	}
	public BasicAttribute getLife() {return Life ;}
	public BasicAttribute getMp() {return Mp ;}
	public BasicAttribute getExp() {return Exp ;}
	public BasicAttribute getSatiation() {return Satiation ;}
	public BasicAttribute getThirst() {return Thirst ;}
	
	public void setLife(BasicAttribute newValue) {Life = newValue ;}
	public void setMp(BasicAttribute newValue) {Mp = newValue ;}
	public void setExp(BasicAttribute newValue) {Exp = newValue ;}
	public void setSatiation(BasicAttribute newValue) {Satiation = newValue ;}
	public void setThirst(BasicAttribute newValue) {Thirst = newValue ;}

	public BasicAttribute mapAttributes(Attributes att)
	{
		switch (att)
		{
			case life: return Life ;
			case mp: return Mp ;
			case exp: return Exp ;
			case satiation: return Satiation ;
			case thirst: return Thirst ;
			
			default: return null ;
		}
	}
	public static int numberFightsToLevelUp(int currentExp, int totalExp, int opponentExp, double expMult)
	{
		return 1 + (int) ((totalExp - currentExp) / (opponentExp * expMult)) ;
	}
	
	public static Directions randomDir()
	{
		int dir = UtilG.randomIntFromTo(0, 3) ;

		return Directions.getDir(dir) ;
	}
	public Point CalcNewPos(Directions dir, Point currentPos, int step)
	{
		Point newPos = new Point(0, 0) ;
		step = 1 ;
		switch (dir)
		{
			case up: newPos = new Point(currentPos.x, currentPos.y - step) ; break ;
			case down: newPos = new Point(currentPos.x, currentPos.y + step) ; break ;
			case left: newPos = new Point(currentPos.x - step, currentPos.y) ; break ;
			case right: newPos = new Point(currentPos.x + step, currentPos.y) ; break ;
		}
		
		return newPos ;
	}
	
	@Override
	public String toString()
	{
		return "PersonalAttributes [Life=" + Life + ", Mp=" + Mp + ", Exp=" + Exp + ", Satiation=" + Satiation
				+ ", Thirst=" + Thirst + "]";
	}

}