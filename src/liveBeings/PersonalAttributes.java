package liveBeings ;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;

import maps.GameMap;
import utilities.Directions;
import utilities.TimeCounter;

public class PersonalAttributes
{
	private String Name ;
	private int Level ;
	protected int Job ;
	protected int ProJob ;
	private int continent ;
	private GameMap map ;
	private Point Pos ;
	private Directions dir ;			// direction of the movement
	private LiveBeingStates state ;		// current state
	private Dimension size ;
	private BasicAttribute Life ;
	private BasicAttribute Mp ;
	private double Range ;
	private int Step ;
	private BasicAttribute Exp ;
	private BasicAttribute Satiation ;
	private BasicAttribute Thirst ;
	protected String[] Elem ;					// 0: Atk, 1: Weapon, 2: Armor, 3: Shield, 4: SuperElem
	protected TimeCounter mpCounter ;			// counts the mp reduction
	protected TimeCounter satiationCounter ;	// counts the satiation reduction
	protected TimeCounter moveCounter ;			// counts the move
	protected int stepCounter ;					// counts the steps in the movement	TODO -> TimeCounter ? (não é tempo, é step)
	protected String currentAction; 
	private ArrayList<String> combo ;			// record of the last 10 movements
	
	public PersonalAttributes(String Name, int Level, int Job, int ProJob, GameMap map, Point Pos, Directions dir, LiveBeingStates state, Dimension size,
			BasicAttribute Life, BasicAttribute Mp, double Range, int Step, BasicAttribute Exp, BasicAttribute Satiation, BasicAttribute Thirst, String[] Elem, int mpDuration, int satiationDuration,
			int moveDuration, int stepCounter, String currentAction)
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
		this.state = state ;
		this.size = size ;
		this.Life = Life ;
		this.Mp = Mp ;
		this.Range = Range ;
		this.Step = Step ;
		this.Exp = Exp ;
		this.Satiation = Satiation ;
		this.Thirst = Thirst ;
		this.Elem = Elem ;
		//this.Actions = Actions ;
		this.currentAction = currentAction ;
		mpCounter = new TimeCounter(0, mpDuration) ;
		satiationCounter = new TimeCounter(0, satiationDuration) ;
		moveCounter = new TimeCounter(0, moveDuration) ;
		this.stepCounter = stepCounter ;
		combo = new ArrayList<>() ;
	}

	public String getName() {return Name ;}
	public int getLevel() {return Level ;}
	public int getJob() {return Job ;}
	public int getProJob() {return ProJob ;}
	public int getContinent() {return continent ;}
	public GameMap getMap() {return map ;}
	public Directions getDir() {return dir ;}
	public LiveBeingStates getState() {return state ;}
	public Point getPos() {return Pos ;}
	public Dimension getSize() {return size ;}
	public BasicAttribute getLife() {return Life ;}
	public BasicAttribute getMp() {return Mp ;}
	public double getRange() {return Range ;}
	public int getStep() {return Step ;}
	public BasicAttribute getExp() {return Exp ;}
	public BasicAttribute getSatiation() {return Satiation ;}
	public BasicAttribute getThirst() {return Thirst ;}
	public String[] getElem() {return Elem ;}
	//public int[][] getActions() {return Actions ;}
	public String getCurrentAction() {return currentAction ;}
	public TimeCounter getMpCounter() {return mpCounter ;}
	public TimeCounter getSatiationCounter() {return satiationCounter ;}
	public TimeCounter getMoveCounter() {return moveCounter ;}
	public int getStepCounter() {return stepCounter ;}
	public ArrayList<String> getCombo() {return combo ;}
	public void setCurrentAction(String newValue) {currentAction = newValue ;}
	
	public void setName(String newValue) {Name = newValue ;}
	public void setLevel(int newValue) {Level = newValue ;}
	public void setJob(int newValue) {Job = newValue ;}
	public void setProJob(int newValue) {ProJob = newValue ;}
	public void setContinent(int newValue) {continent = newValue ;}
	public void setMap(GameMap newValue) {map = newValue ;}
	public void setdir(Directions newValue) {dir = newValue ;}
	public void setState(LiveBeingStates newValue) {state = newValue ;}
	public void setPos(Point newValue) {Pos = newValue ;}
	public void setSize(Dimension newValue) {size = newValue ;}
	public void setLife(BasicAttribute newValue) {Life = newValue ;}
	public void setMp(BasicAttribute newValue) {Mp = newValue ;}
	public void setRange(double newValue) {Range = newValue ;}
	public void setStep(int newValue) {Step = newValue ;}
	public void setExp(BasicAttribute newValue) {Exp = newValue ;}
	public void setSatiation(BasicAttribute newValue) {Satiation = newValue ;}
	public void setThirst(BasicAttribute newValue) {Thirst = newValue ;}
	public void setCombo(ArrayList<String> newValue) {combo = newValue ;}

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
	public Point CalcNewPos()
	{
		Point newPos = new Point(0, 0) ;
		Step = 1 ;
		switch (dir)
		{
			case up: newPos = new Point(Pos.x, Pos.y - Step) ; break ;
			case down: newPos = new Point(Pos.x, Pos.y + Step) ; break ;
			case left: newPos = new Point(Pos.x - Step, Pos.y) ; break ;
			case right: newPos = new Point(Pos.x + Step, Pos.y) ; break ;		
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