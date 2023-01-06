package liveBeings ;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;

import maps.GameMap;
import utilities.Align;
import utilities.Directions;
import utilities.TimeCounter;
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

	
	public Directions randomDir()
	{
		int dir = (int)(4*Math.random() - 0.01) ;

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
	
	/*public void incLife(int amount)
	{
		Life[0] = Math.max(0, Math.min(Life[0] + amount, Life[1])) ;	
	}
	public void incMP(int amount)
	{
		Mp[0] = Math.max(0, Math.min(Mp[0] + amount, Mp[1])) ;	
	}
	public void incSatiation(int amount)
	{
		Satiation[0] = Math.max(0, Math.min(Satiation[0] + amount, Satiation[1])) ;	
	}
	public void incThirst(int amount)
	{
		Thirst[0] = Math.max(0, Math.min(Thirst[0] + amount, Thirst[1])) ;	
	}*/

}