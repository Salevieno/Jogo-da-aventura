package liveBeings ;

import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import attributes.BasicAttribute;
import attributes.BasicBattleAttribute;
import attributes.BattleAttributes;
import attributes.BattleSpecialAttribute;
import attributes.BattleSpecialAttributeWithDamage;
import attributes.PersonalAttributes;
import graphics.DrawPrimitives;
import items.Item;
import main.Game;
import main.Languages;
import utilities.Align;
import utilities.Elements;
import utilities.Scale;
import utilities.UtilG;
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
	public static final List<CreatureType> all ;
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
		all = new ArrayList<>() ;
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
		all.add(this);
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
	public static void load(Languages language, int difficultLevel)
	{
		List<String[]> input = UtilG.ReadcsvFile(Game.CSVPath + "CreatureTypes.csv") ;
		CreatureType.setNumberOfCreatureTypes(input.size()) ;
		CreatureType[] creatureTypes = new CreatureType[CreatureType.getNumberOfCreatureTypes()] ;
		Color[] color = new Color[creatureTypes.length] ;
		int numberCreatureTypes = 7 ;
		double diffMult = difficultLevel == 0 ? 0.6 : (difficultLevel == 1 ? 0.8 : 1.0) ;
		
		for (int ct = 0 ; ct <= creatureTypes.length - 1 ; ct += 1)
		{
			int colorid = (int) ((Creature.getskinColor().length - 1) * Math.random()) ;
			color[ct] = Creature.getskinColor()[colorid] ;
			if (270 < ct & ct <= 299) // Ocean creatures
			{
				color[ct] = Game.colorPalette[5] ;
			}

			MovingAnimations moveAni = CreatureType.moveAni.get(ct % numberCreatureTypes) ;

			BasicAttribute Life = new BasicAttribute((int) (Integer.parseInt(input.get(ct)[5]) * diffMult),
					(int) (Integer.parseInt(input.get(ct)[5]) * diffMult), 1) ;
			BasicAttribute Mp = new BasicAttribute((int) (Integer.parseInt(input.get(ct)[6]) * diffMult),
					(int) (Integer.parseInt(input.get(ct)[6]) * diffMult), 1) ;
			BasicAttribute Exp = new BasicAttribute(Integer.parseInt(input.get(ct)[36]), 999999999, 1) ;
			BasicAttribute Satiation = new BasicAttribute(100, 100, 1) ;
			BasicAttribute Thirst = new BasicAttribute(100, 100, 1) ;
			PersonalAttributes PA = new PersonalAttributes(Life, Mp, Exp, Satiation, Thirst) ;

			BasicBattleAttribute PhyAtk = new BasicBattleAttribute(Double.parseDouble(input.get(ct)[8]) * diffMult, 0,
					0) ;
			BasicBattleAttribute MagAtk = new BasicBattleAttribute(Double.parseDouble(input.get(ct)[9]) * diffMult, 0,
					0) ;
			BasicBattleAttribute PhyDef = new BasicBattleAttribute(Double.parseDouble(input.get(ct)[10]) * diffMult, 0,
					0) ;
			BasicBattleAttribute MagDef = new BasicBattleAttribute(Double.parseDouble(input.get(ct)[11]) * diffMult, 0,
					0) ;
			BasicBattleAttribute Dex = new BasicBattleAttribute(Double.parseDouble(input.get(ct)[12]) * diffMult, 0, 0) ;
			BasicBattleAttribute Agi = new BasicBattleAttribute(Double.parseDouble(input.get(ct)[13]) * diffMult, 0, 0) ;
			BasicBattleAttribute CritAtk = new BasicBattleAttribute(Double.parseDouble(input.get(ct)[14]) * diffMult, 0,
					0) ;
			BasicBattleAttribute CritDef = new BasicBattleAttribute(Double.parseDouble(input.get(ct)[15]) * diffMult, 0,
					0) ;
			BattleSpecialAttribute Stun = new BattleSpecialAttribute(Double.parseDouble(input.get(ct)[16]) * diffMult,
					0, Double.parseDouble(input.get(ct)[17]) * diffMult, 0,
					(int) (Double.parseDouble(input.get(ct)[18]) * diffMult)) ;
			BattleSpecialAttribute Block = new BattleSpecialAttribute(Double.parseDouble(input.get(ct)[19]) * diffMult,
					0, Double.parseDouble(input.get(ct)[20]) * diffMult, 0,
					(int) (Double.parseDouble(input.get(ct)[21]) * diffMult)) ;
			BattleSpecialAttributeWithDamage Blood = new BattleSpecialAttributeWithDamage(
					Double.parseDouble(input.get(ct)[22]) * diffMult, 0,
					Double.parseDouble(input.get(ct)[23]) * diffMult, 0,
					(int) (Double.parseDouble(input.get(ct)[24]) * diffMult), 0,
					(int) (Double.parseDouble(input.get(ct)[25]) * diffMult), 0,
					(int) (Integer.parseInt(input.get(ct)[26]) * diffMult)) ;
			BattleSpecialAttributeWithDamage Poison = new BattleSpecialAttributeWithDamage(
					Double.parseDouble(input.get(ct)[27]) * diffMult, 0,
					Double.parseDouble(input.get(ct)[28]) * diffMult, 0,
					(int) (Double.parseDouble(input.get(ct)[29]) * diffMult), 0,
					(int) (Double.parseDouble(input.get(ct)[30]) * diffMult), 0,
					(int) (Integer.parseInt(input.get(ct)[31]) * diffMult)) ;
			BattleSpecialAttribute Silence = new BattleSpecialAttribute(
					Double.parseDouble(input.get(ct)[32]) * diffMult, 0,
					Double.parseDouble(input.get(ct)[33]) * diffMult, 0,
					(int) (Double.parseDouble(input.get(ct)[34]) * diffMult)) ;
			LiveBeingStatus status = new LiveBeingStatus() ;
			BattleAttributes BA = new BattleAttributes(PhyAtk, MagAtk, PhyDef, MagDef, Dex, Agi, CritAtk, CritDef, Stun,
					Block, Blood, Poison, Silence, status) ;

			// TODO spells para as criaturas
			List<Spell> spells = new ArrayList<>() ;
			spells.add(Spell.all.get(0)) ;
			spells.get(0).incLevel(1) ;
//			spells.add(allSpells[1]) ;
//			spells.add(allSpells[2]) ;
//			spells.add(allSpells[3]) ;
//			spells.add(allSpells[4]) ;

			Set<Item> items = new HashSet<>() ;
			for (int i = 0 ; i <= 10 - 1 ; i += 1)
			{
				int itemID = Integer.parseInt(input.get(ct)[37 + i]) ;
				if (-1 < itemID)
				{
					items.add(Item.allItems.get(itemID)) ;
				}
			}

			int Gold = Integer.parseInt(input.get(ct)[47]) ;
			int[] StatusCounter = new int[8] ;

			String name = input.get(ct)[1 + language.ordinal()] ;
			int level = Integer.parseInt(input.get(ct)[3]) ;
			Dimension size = new Dimension(moveAni.idleGif.getWidth(null), moveAni.idleGif.getHeight(null)) ;
			int range = (int) (Integer.parseInt(input.get(ct)[7]) * diffMult) ;
			int step = Integer.parseInt(input.get(ct)[48]) ;
			Elements[] elem = new Elements[] { Elements.valueOf(input.get(ct)[35]) } ;
			int mpDuration = Integer.parseInt(input.get(ct)[49]) ;
			int satiationDuration = 100 ;
			int numberSteps = Integer.parseInt(input.get(ct)[50]) ;
			int battleActionDuration = Integer.parseInt(input.get(ct)[51]) ;
			int stepCounter = 0 ;

			new CreatureType(ct, name, level, size, range, step, elem, mpDuration,
					satiationDuration, numberSteps, battleActionDuration, stepCounter, moveAni, PA, BA, spells, items,
					Gold, color[ct], StatusCounter) ;
		}
	}
	
	public void display(Point pos, Scale scale, DrawPrimitives DP)
	{
		DP.drawImage(movingAni.idleGif, pos, scale, Align.center) ;
	}

	public String toString()
	{
		return "CreatureTypes [Type=" + id + "]";
	}
}
