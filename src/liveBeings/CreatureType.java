package liveBeings ;

import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import attributes.BattleAttributes;
import attributes.PersonalAttributes;
import graphics.DrawPrimitives;
import items.Item;
import utilities.Align;
import utilities.Elements;
import utilities.Scale;
import utilities.UtilS;
import windows.CreatureAttributesWindow;

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
	protected int numberSteps ;			// counts the steps
	protected int battleActionDuration ;// counts the battle actions
	
	private Genetics genes ;
	
	protected MovingAnimations movingAni ;
	protected PersonalAttributes PA ;
	protected BattleAttributes BA ;
	private List<Spell> spell ;
	private Set<Item> items ;
	private int gold ;
	private Color color ;
	private int[] StatusCounter ;// [Life, Mp, Phy atk, Phy def, Mag atk, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence]	
	
	private static int NumberOfCreatureTypes ;
	public static final List<MovingAnimations> moveAni ;
	public static final CreatureAttributesWindow attWindow ;
	
	static
	{
		moveAni = new ArrayList<>() ;
		for (int i = 0 ; i <= 7 - 1 ; i += 1)
		{
			moveAni.add(new MovingAnimations(
			UtilS.loadImage("\\Creatures\\" + "creature" + i + "_idle.gif"),
			UtilS.loadImage("\\Creatures\\" + "creature" + i + "_movingup.gif"),
			UtilS.loadImage("\\Creatures\\" + "creature" + i + "_movingdown.gif"),
			UtilS.loadImage("\\Creatures\\" + "creature" + i + "_movingleft.gif"),
			UtilS.loadImage("\\Creatures\\" + "creature" + i + "_movingright.gif"))) ;
		}
		
		attWindow = new CreatureAttributesWindow() ;
	}
	
	public CreatureType(
			int id,
			String name,
			int level,
			Dimension size,
			int range,
			int step,
			Elements[] elem,
			int mpDuration,
			int satiationDuration,
			int numberSteps,
			int battleActionDuration,
			int stepCounter,
			MovingAnimations movingAni,
			PersonalAttributes PA,
			BattleAttributes BA,
			List<Spell> spell,
			Set<Item> items,
			int gold,
			Color color,
			int[] StatusCounter)
	{
		this.id = id ;
		
		this.name = name;
		this.level = level;
		this.size = size;
		this.range = range;
		this.step = step;
		this.elem = elem;
		this.mpDuration = mpDuration;
		this.satiationDuration = satiationDuration;
		this.numberSteps = numberSteps;
		this.battleActionDuration = battleActionDuration ;
		
		this.movingAni = movingAni ;
		this.PA = PA ;
		this.BA = BA ;
		this.spell = spell ;
		this.items = items ;
		this.gold = gold ;
		this.color = color ;
		this.StatusCounter = StatusCounter ;
		
		genes = new Genetics() ;
	}

	public int getID() {return id ;}
	public String getName() {return name ;}
	public Dimension getSize() {return size ;}
	public int getLevel() { return level ;}
	public MovingAnimations getMovingAnimations() {return movingAni ;}
	public PersonalAttributes getPA() {return PA ;}
	public BattleAttributes getBA() {return BA ;}
	public List<Spell> getSpell() {return spell ;}
	public Set<Item> getItems() {return items ;}
	public int getGold() {return gold ;}
	public Color getColor() {return color ;}
	public int[] getStatusCounter() {return StatusCounter ;}
	public Genetics getGenes() {return genes ;}
	public void setID(int I) {id = I ;}
	public void setSpell(List<Spell> S) {spell = S ;}
	public void setItems(Set<Item> B) {items = B ;}
	public void setGold(int G) {gold = G ;}
	public void setColor(Color C) {color = C ;}
	public void setStatusCounter(int[] S) {StatusCounter = S ;}
	public void setGenes(Genetics newGenes) {genes = newGenes ;}
	
	public static int getNumberOfCreatureTypes() { return NumberOfCreatureTypes ;}
	public static void setNumberOfCreatureTypes(int num) { NumberOfCreatureTypes = num ;}
	
	public void display(Point pos, Scale scale, DrawPrimitives DP)
	{
		DP.drawImage(movingAni.idleGif, pos, scale, Align.center) ;
	}

	public String toString()
	{
		return "CreatureTypes [Type=" + id + "]";
	}
}
