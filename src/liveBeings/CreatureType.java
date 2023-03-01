package liveBeings ;

import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Point;
import java.util.List;
import java.util.Set;

import attributes.BattleAttributes;
import attributes.PersonalAttributes;
import graphics.DrawingOnPanel;
import items.Item;
import utilities.Align;
import utilities.Elements;
import utilities.Scale;

public class CreatureType 
{
	private int id ;
	
	protected String name ;
	protected int level;
	protected Dimension size ;
	protected int range ;
	protected int step ;
	protected Elements[] elem ;			// 0: Atk, 1: Weapon, 2: Armor, 3: Shield, 4: SuperElem
	protected int mpDuration ;			// counts the mp reduction
	protected int satiationDuration ;	// counts the satiation reduction
	protected int moveDuration ;		// counts the move
	protected int battleActionDuration ;// counts the battle actions
	
	protected MovingAnimations movingAni ;
	protected PersonalAttributes PA ;
	protected BattleAttributes BA ;
	private List<Spell> spell ;
	private Set<Item> Bag ;
	private int Gold ;
	private Color color ;
	private int[] StatusCounter ;// [Life, Mp, Phy atk, Phy def, Mag atk, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence]	
	
	private static int NumberOfCreatureTypes ;
	
	public CreatureType(
			int Type,
			String name,
			int level,
			Dimension size,
			int range,
			int step,
			Elements[] elem,
			int mpDuration,
			int satiationDuration,
			int moveDuration,
			int battleActionDuration,
			int stepCounter,
			MovingAnimations movingAni,
			PersonalAttributes PA,
			BattleAttributes BA,
			List<Spell> spell,
			Set<Item> Bag,
			int Gold,
			Color color,
			int[] StatusCounter)
	{
		this.id = Type ;
		
		this.name = name;
		this.level = level;
		this.size = size;
		this.range = range;
		this.step = step;
		this.elem = elem;
		this.mpDuration = mpDuration;
		this.satiationDuration = satiationDuration;
		this.moveDuration = moveDuration;
		this.battleActionDuration = battleActionDuration ;
//		this.stepCounter = stepCounter;
		
		this.movingAni = movingAni ;
		this.PA = PA ;
		this.BA = BA ;
		this.spell = spell ;
		this.Bag = Bag ;
		this.Gold = Gold ;
		this.color = color ;
		this.StatusCounter = StatusCounter ;
	}

	public int getID() {return id ;}
	public String getName() {return name ;}
	public Dimension getSize() {return size ;}
	public int getLevel() { return level ;}
	public MovingAnimations getMovingAnimations() {return movingAni ;}
	public PersonalAttributes getPA() {return PA ;}
	public BattleAttributes getBA() {return BA ;}
	public List<Spell> getSpell() {return spell ;}
	public Set<Item> getBag() {return Bag ;}
	public int getGold() {return Gold ;}
	public Color getColor() {return color ;}
	public int[] getStatusCounter() {return StatusCounter ;}
	public void setID(int I) {id = I ;}
	public void setSpell(List<Spell> S) {spell = S ;}
	public void setBag(Set<Item> B) {Bag = B ;}
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
		return "CreatureTypes [Type=" + id + "]";
	}
}
