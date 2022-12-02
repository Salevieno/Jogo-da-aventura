package LiveBeings ;

import java.awt.Image ;
import java.awt.Point;
import java.util.Arrays;

import Main.Game;
import Maps.Maps;
import Utilities.Size;

public class PersonalAttributes
{
	private String Name ;
	private int Level ;
	protected int Job ;
	protected int ProJob ;
	private int continent ;
	private Maps map ;
	private Point Pos ;
	private String dir ;			// direction of the movement
	private String Thought ;		// current thought
	private Size size ;
	private float[] Life ;		// 0: Current life, 1: max life]
	private float[] Mp ;			// 0: Current mp, 1: max mp]
	private float Range ;
	private int Step ;
	private int[] Exp ;		// 0: Current exp, 1: max exp, 2: multiplier
	private int[] Satiation ;	// 0: Current satiation, 1: max satiation, 2: multiplier
	private int[] Thirst ;	// 0: Current satiation, 1: max satiation, 2: multiplier
	protected String[] Elem ;	// 0: Atk, 1: Weapon, 2: Armor, 3: Shield, 4: SuperElem
	protected int[][] Actions ;	// [Move, Satiation, Mp][Counter, delay, permission]
	protected String currentAction; 
	protected int countmove ;
	
	public PersonalAttributes(String Name, int Level, int Job, int ProJob, Maps map, Point Pos, String dir, String Thought, Size size, float[] Life, float[] Mp, float Range, int Step, int[] Exp, int[] Satiation, int[] Thirst, String[] Elem, int[][] Actions, String currentAction, int countmove)
	{
		this.Name = Name ;
		this.Level = Level ;
		this.Job = Job ;
		this.ProJob = ProJob ;
		if (map != null)
		{
			this.continent = map.getContinent() ;
		}
		this.map = map ;
		this.Pos = Pos ;
		this.dir = dir ;
		this.Thought = Thought ;
		this.size = size ;
		this.Life = Life ;
		this.Mp = Mp ;
		this.Range = Range ;
		this.Step = Step ;
		this.Exp = Exp ;
		this.Satiation = Satiation ;
		this.Thirst = Thirst ;
		this.Elem = Elem ;
		this.Actions = Actions ;
		this.currentAction = currentAction ;
		this.countmove = countmove ;
	}

	public String getName() {return Name ;}
	public int getLevel() {return Level ;}
	public int getJob() {return Job ;}
	public int getProJob() {return ProJob ;}
	public int getContinent() {return continent ;}
	public Maps getMap() {return map ;}
	public String getDir() {return dir ;}
	public String getThought() {return Thought ;}
	public Point getPos() {return Pos ;}
	public Size getSize() {return size ;}
	public float[] getLife() {return Life ;}
	public float[] getMp() {return Mp ;}
	public float getRange() {return Range ;}
	public int getStep() {return Step ;}
	public int[] getExp() {return Exp ;}
	public int[] getSatiation() {return Satiation ;}
	public int[] getThirst() {return Thirst ;}
	public String[] getElem() {return Elem ;}
	public int[][] getActions() {return Actions ;}
	public String getCurrentAction() {return currentAction ;}
	public int getCountmove() {return countmove ;}
	
	public void setName(String newValue) {Name = newValue ;}
	public void setLevel(int newValue) {Level = newValue ;}
	public void setJob(int newValue) {Job = newValue ;}
	public void setProJob(int newValue) {ProJob = newValue ;}
	public void setContinent(int newValue) {continent = newValue ;}
	public void setMap(Maps newValue) {map = newValue ;}
	public void setdir(String newValue) {dir = newValue ;}
	public void setThought(String newValue) {Thought = newValue ;}
	public void setPos(Point newValue) {Pos = newValue ;}
	public void setSize(Size newValue) {size = newValue ;}
	public void setLife(float[] newValue) {Life = newValue ;}
	public void setMp(float[] newValue) {Mp = newValue ;}
	public void setRange(float newValue) {Range = newValue ;}
	public void setStep(int newValue) {Step = newValue ;}
	public void setExp(int[] newValue) {Exp = newValue ;}
	public void setSatiation(int[] newValue) {Satiation = newValue ;}
	public void setThirst(int[] newValue) {Thirst = newValue ;}

	public String randomDir()
	{
		int dir = (int)(4*Math.random() - 0.01) ;

		return Player.MoveKeys[dir] ;
	}
	public int[] CalcNewPos(int move, int[] CurrentPos, int step)
	{
		int[] newPos = new int[2] ;
		step = 2 ;
		if (move == 0)	// Down
		{
			newPos = new int[] {CurrentPos[0], CurrentPos[1] - step} ;	
		}
		if (move == 1)	// Left
		{
			newPos = new int[] {CurrentPos[0] - step, CurrentPos[1]} ;
		}
		if (move == 2)	// Up
		{
			newPos = new int[] {CurrentPos[0], CurrentPos[1] + step} ;
		}
		if (move == 3)	// Right
		{
			newPos = new int[] {CurrentPos[0] + step, CurrentPos[1]} ;
		}
		
		return newPos ;
	}
	public Point CalcNewPos(String dir, Point CurrentPos, int step)
	{
		Point newPos = new Point(0, 0) ;
		step = 1 ;
		if (dir.equals(Player.MoveKeys[0]))	// North
		{
			newPos = new Point(CurrentPos.x, CurrentPos.y - step) ;
		}
		if (dir.equals(Player.MoveKeys[1]))	// West
		{
			newPos = new Point(CurrentPos.x - step, CurrentPos.y) ;
		}
		if (dir.equals(Player.MoveKeys[2]))	// South
		{
			newPos = new Point(CurrentPos.x, CurrentPos.y + step) ;	
		}
		if (dir.equals(Player.MoveKeys[3]))	// East
		{
			newPos = new Point(CurrentPos.x + step, CurrentPos.y) ;
		}
		
		return newPos ;
	}
	public Point CalcNewPos()
	{
		Point newPos = new Point(0, 0) ;
		Step = 1 ;
		if (dir.equals(Player.MoveKeys[0]))	// North
		{
			newPos = new Point(Pos.x, Pos.y - Step) ;
		}
		if (dir.equals(Player.MoveKeys[1]))	// West
		{
			newPos = new Point(Pos.x - Step, Pos.y) ;
		}
		if (dir.equals(Player.MoveKeys[2]))	// South
		{
			newPos = new Point(Pos.x, Pos.y + Step) ;	
		}
		if (dir.equals(Player.MoveKeys[3]))	// East
		{
			newPos = new Point(Pos.x + Step, Pos.y) ;
		}
		
		return newPos ;
	}
	/*public int[] CenterPos()
	{
		return new int[] {(int) (Pos[0]), (int) (Pos[1] - 0.5 * Size[1])} ;
	}*/
	
	public void incLife(float amount)
	{
		Life[0] = Math.max(0, Math.min(Life[0] + amount, Life[1])) ;	
	}
	public void incMP(float amount)
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
	}

}