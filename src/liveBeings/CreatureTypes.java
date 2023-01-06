package liveBeings ;

import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Image ;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import graphics.DrawingOnPanel;
import maps.GameMap;
import utilities.Align;
import utilities.Directions;
import utilities.Scale;
import utilities.TimeCounter;

public class CreatureTypes 
{
	private int Type ;
	
	protected String name ;
	protected int level;
	protected Dimension size ;
	protected double range ;
	protected int step ;
	protected String[] elem ;			// 0: Atk, 1: Weapon, 2: Armor, 3: Shield, 4: SuperElem
	protected int mpDuration ;			// counts the mp reduction
	protected int satiationDuration ;	// counts the satiation reduction
	protected int moveDuration ;		// counts the move
	protected int stepCounter ;			// counts the steps in the movement	TODO -> TimeCounter ? (não é tempo, é step)
	
	protected MovingAnimations movingAni ;
	protected PersonalAttributes PA ;
	protected BattleAttributes BA ;
	private ArrayList<Spell> spell ;
	private int[] Bag ;
	private int Gold ;
	private Color color ;
	private int[] StatusCounter ;// [Life, Mp, Phy atk, Phy def, Mag atk, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence]	
	
	private static int NumberOfCreatureTypes ;
	
	public CreatureTypes(
			int Type,
			String name,
			int level,
			Dimension size,
			double range,
			int step,
			String[] elem,
			int mpDuration,
			int satiationDuration,
			int moveDuration,
			int stepCounter,
			MovingAnimations movingAni,
			PersonalAttributes PA,
			BattleAttributes BA,
			ArrayList<Spell> spell,
			int[] Bag,
			int Gold,
			Color color,
			int[] StatusCounter)
	{
		this.Type = Type ;
		
		this.name = name;
		this.level = level;
		this.size = size;
		this.range = range;
		this.step = step;
		this.elem = elem;
		this.mpDuration = mpDuration;
		this.satiationDuration = satiationDuration;
		this.moveDuration = moveDuration;
		this.stepCounter = stepCounter;
		
		this.movingAni = movingAni ;
		this.PA = PA ;
		this.BA = BA ;
		this.spell = spell ;
		this.Bag = Bag ;
		this.Gold = Gold ;
		this.color = color ;
		this.StatusCounter = StatusCounter ;
	}

	public int getID() {return Type ;}
	public MovingAnimations getMovingAnimations() {return movingAni ;}
	public PersonalAttributes getPA() {return PA ;}
	public BattleAttributes getBA() {return BA ;}
	public ArrayList<Spell> getSpell() {return spell ;}
	public int[] getBag() {return Bag ;}
	public int getGold() {return Gold ;}
	public Color getColor() {return color ;}
	public int[] getStatusCounter() {return StatusCounter ;}
	public void setID(int I) {Type = I ;}
	public void setSpell(ArrayList<Spell> S) {spell = S ;}
	public void setBag(int[] B) {Bag = B ;}
	public void setGold(int G) {Gold = G ;}
	public void setColor(Color C) {color = C ;}
	public void setStatusCounter(int[] S) {StatusCounter = S ;}
	
	public static int getNumberOfCreatureTypes()
	{
		return NumberOfCreatureTypes ;
	}
	public static void setNumberOfCreatureTypes(int num)
	{
		NumberOfCreatureTypes = num ;
	}
	
	public void display(Point pos, Scale scale, DrawingOnPanel DP)
	{
		DP.DrawImage(movingAni.idleGif, pos, scale, Align.center) ;
	}

	@Override
	public String toString() {
		return "CreatureTypes [Type=" + Type + "]";
	}
}
