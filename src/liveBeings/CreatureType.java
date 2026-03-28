package liveBeings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import attributes.BasicAttribute;
import attributes.BattleAttributes;
import attributes.PersonalAttributes;
import graphics.Align;
import graphics.Scale;
import graphics2.SpriteAnimation;
import items.Item;
import main.Elements;
import main.Path;
import windows.CreatureAttributesWindow;

public class CreatureType
{
	private final int id;
	private final String name;
	private final int level;
	private final Dimension size;
	private final int range;
	private final int step;
	private final Elements atkElem ;
	private final double mpDuration;
	private final double satiationDuration;
	private final MovePattern movePattern ;
	private final double battleActionDuration;
	private final MovingAnimations movingAni;
	private final PersonalAttributes PA;
	private final BattleAttributes BA;
	private final List<Spell> spell;
	private final Set<Item> items;
	private final int gold;
	private final Color color;
	private final String hitboxType ;
	private Genetics genes;

	private static final List<MovingAnimations> moveAni;
	public static final int numberCreatureTypesImages ;
	public static final List<CreatureType> all;
	public static final CreatureAttributesWindow attWindow;

	static
	{// TODO imagens das criaturas tem que refletir o poder (1, 6, 11, 16 e 21) são as nível 0, pode ser a mesma imagem mudando cores
		numberCreatureTypesImages = 3;
		moveAni = new ArrayList<>();
		for (int i = 0; i <= numberCreatureTypesImages - 1; i += 1)
		{
			String rootPath = Path.CREATURES_IMG + "creature" + i ;
			moveAni.add(new MovingAnimations(
				new SpriteAnimation(rootPath + "_idle.png", new Point(0, 0), Align.bottomCenter, 9, 0.333),
				new SpriteAnimation(rootPath + "_movingup.png", new Point(0, 0), Align.bottomCenter, 4, 0.16),
				new SpriteAnimation(rootPath + "_movingdown.png", new Point(0, 0), Align.bottomCenter, 4, 0.16),
				new SpriteAnimation(rootPath + "_movingleft.png", new Point(0, 0), Align.bottomCenter, 4, 0.16),
				new SpriteAnimation(rootPath + "_movingright.png", new Point(0, 0), Align.bottomCenter, 4, 0.16))
			);
		}

		attWindow = new CreatureAttributesWindow();
		all = new ArrayList<>();
	}

	public CreatureType(int id, String name, int level, int range, int step, int movePatternID, Elements[] elem,
			double mpDuration, double satiationDuration, double actionDuration, double stepDuration,
			double life, double mp, double exp, double satiation, double thirst, 
			double phyAtk, double magAtk, double phyDef, double magDef, double dex, double agi, double critAtk, double critDef,
			double stunAtkChance, double stunDefChance, double stunDuration,
			double bloodAtkChance, double bloodAtk, double bloodDefChance, double bloodDef, double bloodDuration,
			double poisonAtkChance, double poisonAtk, double poisonDefChance, double poisonDef, double poisonDuration,
			double blockAtkChance, double blockDefChance, double blockDuration,
			double silenceAtkChance, double silenceDefChance, double silenceDuration,
			double atkSpeed, double knockBackPower,
			List<Integer> spellIDs,
			Set<Integer> itemIDs, int gold, Color color, int[] StatusCounter, String hitboxType)
	{
		this.id = id;
		this.name = name;
		this.level = level;
		this.movingAni = CreatureType.moveAni.get(id % numberCreatureTypesImages);
		this.size = new Dimension(movingAni.spriteIdle.getFrameSize().width, movingAni.spriteIdle.getFrameSize().height);
		this.range = range;
		this.step = step;
		this.atkElem = elem[0];
		this.mpDuration = mpDuration;
		this.satiationDuration = satiationDuration;
		this.movePattern = MovePattern.values()[movePatternID] ;
		this.battleActionDuration = atkSpeed ;

		this.PA = new PersonalAttributes(
                new BasicAttribute((int) (life), (int) (life), 1),
                new BasicAttribute((int) (mp), (int) (mp), 1),
                new BasicAttribute((int) (exp), (int) (999999999), 1),
                new BasicAttribute((int) (satiation), (int) (satiation), 1),
                new BasicAttribute((int) (thirst), (int) (thirst), 1)
            );
		this.BA = new BattleAttributes(8, 7, 8, 7, 26, 9, 0.13 ,0.02,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			1.09, 10);
		this.spell = new ArrayList<>();
		for (int i : spellIDs)
		{
			this.spell.add(new Spell(Spell.all.get(i)));
		}
		this.spell.forEach(spell -> spell.incLevel(1));

		this.items = new HashSet<>();
		for (int i = 0; i <= this.items.size() - 1; i += 1)
		{
			if (-1 < i)
			{
				items.add(Item.allItems.get(i));
			}
		}
		this.gold = gold;
		this.color = color;
		this.hitboxType = hitboxType ;

		genes = new Genetics();
		all.add(this);
	}

	public CreatureType(int id, String name, int level, int range, int step, int movePatternID, Elements[] elem,
			double mpDuration, double satiationDuration, double actionDuration, double stepDuration,
			double life, double mp, double exp, double satiation, double thirst, 
			double phyAtk, double magAtk, double phyDef, double magDef, double dex, double agi, double critAtk, double critDef,
			double stunAtkChance, double stunDefChance, double stunDuration,
			double bloodAtkChance, double bloodAtk, double bloodDefChance, double bloodDef, double bloodDuration,
			double poisonAtkChance, double poisonAtk, double poisonDefChance, double poisonDef, double poisonDuration,
			double blockAtkChance, double blockDefChance, double blockDuration,
			double silenceAtkChance, double silenceDefChance, double silenceDuration,
			double atkSpeed, double knockBackPower,
			List<Integer> spellIDs,
			Set<Integer> itemIDs, int gold, Color color, int[] StatusCounter, String hitboxType, double diffMult)
	{
		this(id, name, level, range, step, movePatternID, elem,
			mpDuration, satiationDuration, actionDuration, stepDuration,
			life * diffMult, mp * diffMult, exp * diffMult, satiation * diffMult, thirst * diffMult, 
			phyAtk * diffMult, magAtk * diffMult, phyDef * diffMult, magDef * diffMult, dex * diffMult, agi * diffMult, critAtk * diffMult, critDef * diffMult,
			stunAtkChance * diffMult, stunDefChance * diffMult, stunDuration * diffMult,
			bloodAtkChance * diffMult, bloodAtk * diffMult, bloodDefChance * diffMult, bloodDef * diffMult, bloodDuration * diffMult,
			poisonAtkChance * diffMult, poisonAtk * diffMult, poisonDefChance * diffMult, poisonDef * diffMult, poisonDuration * diffMult,
			blockAtkChance * diffMult, blockDefChance * diffMult, blockDuration * diffMult,
			silenceAtkChance * diffMult, silenceDefChance * diffMult, silenceDuration * diffMult,
			atkSpeed, knockBackPower,
			spellIDs, itemIDs, gold, color, StatusCounter, hitboxType) ;
	}

	public int getID() { return id ;}
	public String getName() { return name ;}
	public Dimension getSize() { return size ;}
	public int getLevel() { return level ;}
	public int getRange() { return range ;}
	public int getStep() { return step ;}
	public Elements getAtkElem() { return atkElem ;}
	public double getMpTimerDuration() { return mpDuration ;}
	public double getSatiationTimerDuration() { return satiationDuration ;}
	public double getBattleActionTimerDuration() { return battleActionDuration ;}
	public double getMovingTimerDuration() { return movePattern.getDuration() ;}
	public MovePattern getMovePattern() { return movePattern ;}
	public MovingAnimations getMovingAnimations() { return movingAni ;}
	public PersonalAttributes getPA() { return PA ;}
	public BattleAttributes getBA() { return BA ;}
	public List<Spell> getSpell() { return spell ;}
	public Set<Item> getItems() { return items ;}
	public int getGold() { return gold ;}
	public Color getColor() { return color ;}
	public String getHitboxType() { return hitboxType ;}
	public Genetics getGenes() { return genes ;}
	public void setGenes(Genetics newGenes) { genes = newGenes ;}

	public void display(Point pos, Scale scale)
	{
		movingAni.displayIdle(pos, 0.0, scale, Align.center);
	}

	public String toString()
	{
		return String.format("CreatureType:\n  ID: %d\n  PA: %s\n  BA: %s\n  Spells: %s\n",
				id, PA.toString(), BA.toString(), spell.toString()) ;
	}
}