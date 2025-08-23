package liveBeings;

import java.awt.Color;
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
import graphics.Align;
import graphics.Scale;
import graphics2.SpriteAnimation;
import items.Item;
import main.Game;
import main.Languages;
import utilities.Elements;
import utilities.Util;
import windows.CreatureAttributesWindow;

public class CreatureType
{
	private final int id;

	protected final String name;
	protected final int level;
	protected final Dimension size;
	protected final int range;
	protected final int step;
	protected final Elements atkElem ;
	protected final Elements weaponElem ;
	protected final Elements armorElem ;
	protected final Elements shieldElem ;
	protected final Elements superElem ;
	protected final double mpDuration;
	protected final double satiationDuration;
	protected final double actionDuration;
	protected final double stepDuration;
	protected final MovePattern movePattern ;
	protected final double battleActionDuration;
	protected final MovingAnimations movingAni;
	protected final PersonalAttributes PA;
	protected final BattleAttributes BA;

	private Genetics genes;

	private final List<Spell> spell;
	private final Set<Item> items;
	private final int gold;
	private final Color color;
	// TODO remover
	private final int[] StatusCounter;// [Life, Mp, Phy atk, Phy def, Mag atk, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence]
	private final String hitboxType ;

	private static int NumberOfCreatureTypes;
	private static final List<MovingAnimations> moveAni;
	public static final int numberCreatureTypesImages ;
	public static final List<CreatureType> all;
	public static final CreatureAttributesWindow attWindow;

	static
	{
		numberCreatureTypesImages = 2;
		moveAni = new ArrayList<>();
		for (int i = 0; i <= numberCreatureTypesImages - 1; i += 1)
		{
			moveAni.add(new MovingAnimations(
					new SpriteAnimation("\\Creatures\\" + "creature" + i + "_idle" + ".png", new Point(0, 0), Align.bottomCenter, 9, 13),
					new SpriteAnimation("\\Creatures\\" + "creature" + i + "_movingup" + ".png", new Point(0, 0), Align.bottomCenter, 4, 5),
					new SpriteAnimation("\\Creatures\\" + "creature" + i + "_movingdown" + ".png", new Point(0, 0), Align.bottomCenter, 4, 5),
					new SpriteAnimation("\\Creatures\\" + "creature" + i + "_movingleft" + ".png", new Point(0, 0), Align.bottomCenter, 4, 5),
					new SpriteAnimation("\\Creatures\\" + "creature" + i + "_movingright" + ".png", new Point(0, 0), Align.bottomCenter, 4, 5)));
		}

		attWindow = new CreatureAttributesWindow();
		all = new ArrayList<>();
	}

	public CreatureType(int id, String name, int level, Dimension size, int range, int step, MovePattern movePattern, Elements[] elem,
			double mpDuration, double satiationDuration, double actionDuration, double stepDuration, double battleActionDuration,
			int stepCounter, MovingAnimations movingAni, PersonalAttributes PA, BattleAttributes BA, List<Spell> spell,
			Set<Item> items, int gold, Color color, int[] StatusCounter, String hitboxType)
	{
		this.id = id;

		this.name = name;
		this.level = level;
		this.size = size;
		this.range = range;
		this.step = step;
		this.atkElem = elem[0];
		this.weaponElem = elem[0];
		this.armorElem = elem[0];
		this.shieldElem = elem[0];
		this.superElem = elem[0];
		this.mpDuration = mpDuration;
		this.satiationDuration = satiationDuration;
		this.actionDuration = actionDuration;
		this.stepDuration = stepDuration;
		this.movePattern = movePattern ;
		this.battleActionDuration = battleActionDuration;

		this.movingAni = movingAni;
		this.PA = PA;
		this.BA = BA;
		this.spell = spell;
		this.items = items;
		this.gold = gold;
		this.color = color;
		this.StatusCounter = StatusCounter;
		this.hitboxType = hitboxType ;

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

	public String getHitboxType()
	{
		return hitboxType;
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

	private static boolean isOceanCreature(int id) { return 270 < id & id <= 299 ;}

	public static void load(Languages language, int difficultLevel)
	{
		List<String[]> input = Util.ReadcsvFile(Game.CSVPath + "CreatureTypes.csv");
		CreatureType.setNumberOfCreatureTypes(input.size());
		CreatureType[] creatureTypes = new CreatureType[CreatureType.getNumberOfCreatureTypes()];
		Color[] color = new Color[creatureTypes.length];
		double diffMult = difficultLevel == 0 ? 0.6 : (difficultLevel == 1 ? 0.8 : 1.0);

		for (int row = 0; row <= creatureTypes.length - 1; row += 1)
		{
			int colorid = (int) ((Creature.getskinColor().length - 1) * Math.random());
			String[] inp = input.get(row);
			color[row] = Creature.getskinColor()[colorid];
			if (isOceanCreature(row))
			{
				color[row] = Game.palette[5];
			}

			MovingAnimations moveAni = CreatureType.moveAni.get(row % numberCreatureTypesImages);

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
			BasicBattleAttribute AtkSpeed = new BasicBattleAttribute(Double.parseDouble(inp[52]) * diffMult, 0, 0);

			BattleAttributes BA = new BattleAttributes(PhyAtk, MagAtk, PhyDef, MagDef, Dex, Agi, CritAtk, CritDef, Stun,
					Block, Blood, Poison, Silence, AtkSpeed);

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
				// TODO eu só preciso de uma cópia do spell existente aqui
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
			Dimension size = new Dimension(moveAni.spriteIdle.getFrameSize().width, moveAni.spriteIdle.getFrameSize().height);
			int range = (int) (Integer.parseInt(inp[7]) * diffMult);
			int step = Integer.parseInt(inp[48]);
			Elements[] elem = new Elements[] { Elements.valueOf(inp[35]) };
			double mpDuration = Double.parseDouble(inp[49]);
			double satiationDuration = 1.25;
			double actionDuration = Double.parseDouble(inp[51]);
			double stepDuration = Double.parseDouble(inp[50]);
			double battleActionDuration = BA.TotalAtkSpeed();
			int stepCounter = 0;
			String hitboxType = inp[53] ;
			MovePattern movePattern = MovePattern.values()[Integer.parseInt(inp[54])] ;

			new CreatureType(row, name, level, size, range, step, movePattern, elem, mpDuration, satiationDuration, actionDuration, stepDuration,
					battleActionDuration, stepCounter, moveAni, PA, BA, spells, items, Gold, color[row], StatusCounter, hitboxType);
		}
	}

	public void display(Point pos, Scale scale)
	{
		movingAni.displayIdle(pos, 0.0, scale, Align.center);
	}

	public String toString()
	{
		return "CreatureTypes [Type=" + id + "]";
	}
}
