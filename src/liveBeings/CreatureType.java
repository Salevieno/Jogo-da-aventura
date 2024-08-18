package liveBeings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import attributes.Attributes;
import attributes.BasicAttribute;
import attributes.BasicBattleAttribute;
import attributes.BattleAttributes;
import attributes.BattleSpecialAttribute;
import attributes.BattleSpecialAttributeWithDamage;
import attributes.PersonalAttributes;
import graphics.DrawPrimitives;
import items.Item;
import libUtil.Align;
import libUtil.Util;
import main.Game;
import main.Languages;
import utilities.Elements;
import utilities.Scale;
import utilities.UtilS;
import windows.CreatureAttributesWindow;

public class CreatureType
{
	private int id;

	protected String name;
	protected int level;
	protected Dimension size;
	protected int range;
	protected int step;
	protected Elements[] elem; // 0: Atk, 1: Weapon, 2: Armor, 3: Shield, 4: SuperElem
	protected double mpDuration; // counts the mp reduction
	protected double satiationDuration; // counts the satiation reduction
	protected double numberSteps; // counts the steps
	protected double battleActionDuration;// counts the battle actions

	private Genetics genes;

	protected MovingAnimations movingAni;
	protected PersonalAttributes PA;
	protected BattleAttributes BA;
	private List<Spell> spell;
	private Set<Item> items;
	private int gold;
	private Color color;
	private int[] StatusCounter;// [Life, Mp, Phy atk, Phy def, Mag atk, Mag def, Dex, Agi, Stun, Block, Blood,
								// Poison, Silence]

	private static int NumberOfCreatureTypes;
	public static final List<CreatureType> all;
	public static final List<MovingAnimations> moveAni;
	public static final CreatureAttributesWindow attWindow;

	static
	{
		moveAni = new ArrayList<>();
		for (int i = 0; i <= 7 - 1; i += 1)
		{
			moveAni.add(new MovingAnimations(UtilS.loadImage("\\Creatures\\" + "creature" + i + "_idle.gif"),
					UtilS.loadImage("\\Creatures\\" + "creature" + i + "_movingup.gif"),
					UtilS.loadImage("\\Creatures\\" + "creature" + i + "_movingdown.gif"),
					UtilS.loadImage("\\Creatures\\" + "creature" + i + "_movingleft.gif"),
					UtilS.loadImage("\\Creatures\\" + "creature" + i + "_movingright.gif")));
		}

		attWindow = new CreatureAttributesWindow();
		all = new ArrayList<>();
	}

	public CreatureType(int id, String name, int level, Dimension size, int range, int step, Elements[] elem,
			double mpDuration, double satiationDuration, double numberSteps, double battleActionDuration,
			int stepCounter, MovingAnimations movingAni, PersonalAttributes PA, BattleAttributes BA, List<Spell> spell,
			Set<Item> items, int gold, Color color, int[] StatusCounter)
	{
		this.id = id;

		this.name = name;
		this.level = level;
		this.size = size;
		this.range = range;
		this.step = step;
		this.elem = elem;
		this.mpDuration = mpDuration;
		this.satiationDuration = satiationDuration;
		this.numberSteps = numberSteps;
		this.battleActionDuration = battleActionDuration;

		this.movingAni = movingAni;
		this.PA = PA;
		this.BA = BA;
		this.spell = spell;
		this.items = items;
		this.gold = gold;
		this.color = color;
		this.StatusCounter = StatusCounter;

		genes = new Genetics();
		all.add(this);
	}

	public int getID()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public Dimension getSize()
	{
		return size;
	}

	public int getLevel()
	{
		return level;
	}

	public MovingAnimations getMovingAnimations()
	{
		return movingAni;
	}

	public PersonalAttributes getPA()
	{
		return PA;
	}

	public BattleAttributes getBA()
	{
		return BA;
	}

	public List<Spell> getSpell()
	{
		return spell;
	}

	public Set<Item> getItems()
	{
		return items;
	}

	public int getGold()
	{
		return gold;
	}

	public Color getColor()
	{
		return color;
	}

	public int[] getStatusCounter()
	{
		return StatusCounter;
	}

	public Genetics getGenes()
	{
		return genes;
	}

	public void setID(int I)
	{
		id = I;
	}

	public void setSpell(List<Spell> S)
	{
		spell = S;
	}

	public void setItems(Set<Item> B)
	{
		items = B;
	}

	public void setGold(int G)
	{
		gold = G;
	}

	public void setColor(Color C)
	{
		color = C;
	}

	public void setStatusCounter(int[] S)
	{
		StatusCounter = S;
	}

	public void setGenes(Genetics newGenes)
	{
		genes = newGenes;
	}

	public static int getNumberOfCreatureTypes()
	{
		return NumberOfCreatureTypes;
	}

	public static void setNumberOfCreatureTypes(int num)
	{
		NumberOfCreatureTypes = num;
	}

	public static void load(Languages language, int difficultLevel)
	{
		List<String[]> input = Util.ReadcsvFile(Game.CSVPath + "CreatureTypes.csv");
		CreatureType.setNumberOfCreatureTypes(input.size());
		CreatureType[] creatureTypes = new CreatureType[CreatureType.getNumberOfCreatureTypes()];
		Color[] color = new Color[creatureTypes.length];
		int numberCreatureTypes = 7;
		double diffMult = difficultLevel == 0 ? 0.6 : (difficultLevel == 1 ? 0.8 : 1.0);

		for (int row = 0; row <= creatureTypes.length - 1; row += 1)
		{
			int colorid = (int) ((Creature.getskinColor().length - 1) * Math.random());
			String[] inp = input.get(row);
			color[row] = Creature.getskinColor()[colorid];
			if (270 < row & row <= 299) // Ocean creatures
			{
				color[row] = Game.colorPalette[5];
			}

			MovingAnimations moveAni = CreatureType.moveAni.get(row % numberCreatureTypes);

			BasicAttribute Life = new BasicAttribute((int) (Integer.parseInt(inp[5]) * diffMult),
					(int) (Integer.parseInt(inp[5]) * diffMult), 1);
			BasicAttribute Mp = new BasicAttribute((int) (Integer.parseInt(inp[6]) * diffMult),
					(int) (Integer.parseInt(inp[6]) * diffMult), 1);
			BasicAttribute Exp = new BasicAttribute(Integer.parseInt(inp[36]), 999999999, 1);
			BasicAttribute Satiation = new BasicAttribute(100, 100, 1);
			BasicAttribute Thirst = new BasicAttribute(100, 100, 1);
			PersonalAttributes PA = new PersonalAttributes(Life, Mp, Exp, Satiation, Thirst);

			BasicBattleAttribute PhyAtk = new BasicBattleAttribute(Double.parseDouble(inp[8]) * diffMult, 0, 0);
			BasicBattleAttribute MagAtk = new BasicBattleAttribute(Double.parseDouble(inp[9]) * diffMult, 0, 0);
			BasicBattleAttribute PhyDef = new BasicBattleAttribute(Double.parseDouble(inp[10]) * diffMult, 0, 0);
			BasicBattleAttribute MagDef = new BasicBattleAttribute(Double.parseDouble(inp[11]) * diffMult, 0, 0);
			BasicBattleAttribute Dex = new BasicBattleAttribute(Double.parseDouble(inp[12]) * diffMult, 0, 0);
			BasicBattleAttribute Agi = new BasicBattleAttribute(Double.parseDouble(inp[13]) * diffMult, 0, 0);
			BasicBattleAttribute CritAtk = new BasicBattleAttribute(Double.parseDouble(inp[14]) * diffMult, 0, 0);
			BasicBattleAttribute CritDef = new BasicBattleAttribute(Double.parseDouble(inp[15]) * diffMult, 0, 0);
			BattleSpecialAttribute Stun = new BattleSpecialAttribute(Double.parseDouble(inp[16]) * diffMult, 0,
					Double.parseDouble(inp[17]) * diffMult, 0, (int) (Double.parseDouble(inp[18]) * diffMult));
			BattleSpecialAttribute Block = new BattleSpecialAttribute(Double.parseDouble(inp[19]) * diffMult, 0,
					Double.parseDouble(inp[20]) * diffMult, 0, (int) (Double.parseDouble(inp[21]) * diffMult));
			BattleSpecialAttributeWithDamage Blood = new BattleSpecialAttributeWithDamage(
					Double.parseDouble(inp[22]) * diffMult, 0, Double.parseDouble(inp[23]) * diffMult, 0,
					(int) (Double.parseDouble(inp[24]) * diffMult), 0, (int) (Double.parseDouble(inp[25]) * diffMult),
					0, (int) (Integer.parseInt(inp[26]) * diffMult));
			BattleSpecialAttributeWithDamage Poison = new BattleSpecialAttributeWithDamage(
					Double.parseDouble(inp[27]) * diffMult, 0, Double.parseDouble(inp[28]) * diffMult, 0,
					(int) (Double.parseDouble(inp[29]) * diffMult), 0, (int) (Double.parseDouble(inp[30]) * diffMult),
					0, (int) (Integer.parseInt(inp[31]) * diffMult));
			BattleSpecialAttribute Silence = new BattleSpecialAttribute(Double.parseDouble(inp[32]) * diffMult, 0,
					Double.parseDouble(inp[33]) * diffMult, 0, (int) (Double.parseDouble(inp[34]) * diffMult));
			BasicBattleAttribute AtkSpeed = new BasicBattleAttribute(Double.parseDouble(inp[51]) * diffMult, 0, 0);

			BattleAttributes BA = new BattleAttributes(PhyAtk, MagAtk, PhyDef, MagDef, Dex, Agi, CritAtk, CritDef, Stun,
					Block, Blood, Poison, Silence, AtkSpeed);

//			String[] myAtts = new String[inp.length - 3] ;
//			for (int i = 0 ; i <= myAtts.length - 1 ; i += 1)
//			{
//				myAtts[i] = inp[i + 3] ;
//			}
//			BA = new BattleAttributes(myAtts, diffMult) ;


			List<Spell> spells = new ArrayList<>();
			int[] spellIDs = switch (row % 3)
			{
			case 0 -> new int[] { 104 };
			case 1 -> new int[] { 44 };
			case 2 -> new int[] { 106 };
			case 3 -> new int[] { 110 };
			case 4 -> new int[] { 143 };
			case 5 -> new int[] { 16 };
			case 6 -> new int[] { 6 };
			case 7 -> new int[] { 34 };
			case 8 -> new int[] { 41 };
			default -> new int[] {};
			};
			for (int id : spellIDs)
			{
				spells.add(new Spell(Spell.all.get(id)));
			}
			spells.forEach(spell -> spell.incLevel(1));

			Set<Item> items = new HashSet<>();
			for (int i = 0; i <= 10 - 1; i += 1)
			{
				int itemID = Integer.parseInt(inp[37 + i]);
				if (-1 < itemID)
				{
					items.add(Item.allItems.get(itemID));
				}
			}

			int Gold = Integer.parseInt(inp[47]);
			int[] StatusCounter = new int[8];

			String name = inp[1 + language.ordinal()];
			int level = Integer.parseInt(inp[3]);
			Dimension size = new Dimension(moveAni.idleGif.getWidth(null), moveAni.idleGif.getHeight(null));
			int range = (int) (Integer.parseInt(inp[7]) * diffMult);
			int step = Integer.parseInt(inp[48]);
			Elements[] elem = new Elements[] { Elements.valueOf(inp[35]) };
			double mpDuration = Double.parseDouble(inp[49]);
			double satiationDuration = 1.25;
			double numberSteps = Double.parseDouble(inp[50]);
			double battleActionDuration = BA.TotalAtkSpeed();
			int stepCounter = 0;

			new CreatureType(row, name, level, size, range, step, elem, mpDuration, satiationDuration, numberSteps,
					battleActionDuration, stepCounter, moveAni, PA, BA, spells, items, Gold, color[row], StatusCounter);
		}
	}

	public void display(Point pos, Scale scale, DrawPrimitives DP)
	{
		DP.drawImage(movingAni.idleGif, pos, scale, Align.center);
	}

	public String toString()
	{
		return "CreatureTypes [Type=" + id + "]";
	}
}
