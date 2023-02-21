package liveBeings ;

import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import attributes.BasicAttribute;
import attributes.BasicBattleAttribute;
import attributes.BattleAttributes;
import attributes.BattleSpecialAttribute;
import attributes.BattleSpecialAttributeWithDamage;
import attributes.PersonalAttributes;
import graphics.Animations;
import graphics.DrawingOnPanel;
import main.AtkResults;
import main.AtkTypes;
import main.Battle;
import main.Game;
import maps.GameMap;
import utilities.Align;
import utilities.AttackEffects;
import utilities.Directions;
import utilities.Elements;
import utilities.Scale;
import utilities.TimeCounter;
import utilities.UtilG;
import windows.PlayerAttributesWindow;

public class Pet extends LiveBeing
{
	private Color color ;
	private int Job ;
	private int spellPoints ;
	private double[] ElemMult ;		// [Neutral, Water, Fire, Plant, Earth, Air, Thunder, Light, Dark, Snow]
	private int[] StatusCounter ;	// [Life, Mp, Phy atk, Phy def, Mag atk, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence]
	
	public static int NumberOfSpells = 5 ;
	public static double[] AttributeIncrease ;
	public static double[] ChanceIncrease ;
	
	private static ArrayList<String[]> PetProperties = UtilG.ReadcsvFile(Game.CSVPath + "PetInitialStats.csv") ;
	private static ArrayList<String[]> PetEvolutionProperties = UtilG.ReadcsvFile(Game.CSVPath + "PetEvolution.csv") ;
	
	public static String[] SpellKeys = new String[] {"0", "1", "2", "3"} ;
	
	public Pet(int Job)
	{
		super(
				InitializePersonalAttributes(Job),
				InitializeBattleAttributes(Job),
				new MovingAnimations(UtilG.loadImage(Game.ImagesPath + "\\Pet\\" + "PetType" + String.valueOf(Job) + ".png"),
				UtilG.loadImage(Game.ImagesPath + "\\Pet\\" + "PetType" + String.valueOf(Job) + ".png"),
				UtilG.loadImage(Game.ImagesPath + "\\Pet\\" + "PetType" + String.valueOf(Job) + ".png"),
				UtilG.loadImage(Game.ImagesPath + "\\Pet\\" + "PetType" + String.valueOf(Job) + ".png"),
				UtilG.loadImage(Game.ImagesPath + "\\Pet\\" + "PetType" + String.valueOf(Job) + ".png")),
				new PlayerAttributesWindow(UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "PetAttWindow1.png"))
			) ;
		
		name = PetProperties.get(Job)[0] ;
		level = 1 ;
		proJob = 0 ;
		map = null ;
		pos = new Point(0, 0) ;
		dir = Directions.up ;
		state = LiveBeingStates.idle ;
		size = new Dimension (movingAni.idleGif.getWidth(null), movingAni.idleGif.getHeight(null)) ;	
		range = Integer.parseInt(PetProperties.get(Job)[4]) ;
		step = Integer.parseInt(PetProperties.get(Job)[32]) ;
		elem = new String[] {"n", "n", "n", "n", "n"} ;
		mpCounter = new TimeCounter(0, Integer.parseInt(PetProperties.get(Job)[33])) ;
		satiationCounter = new TimeCounter(0, Integer.parseInt(PetProperties.get(Job)[34])) ;
		moveCounter = new TimeCounter(0, Integer.parseInt(PetProperties.get(Job)[35])) ;
		battleActionCounter = new TimeCounter(0, Integer.parseInt(PetProperties.get(Job)[36])) ;
		stepCounter = 0 ;
		currentAction = null ;
		combo = new ArrayList<>();
		
		this.Job = Job ;
		Color[] ColorPalette = Game.ColorPalette ;
		Color[] PetColor = new Color[] {ColorPalette[3], ColorPalette[1], ColorPalette[18], ColorPalette[18]} ;
		color = PetColor[Job] ;
		spells = InitializePetSpells();
		spellPoints = 0 ;

		
		ElemMult = new double[10] ;
		StatusCounter = new int[8] ;
			
    	AttributeIncrease = new double[8] ;
    	ChanceIncrease = new double[8] ;
		for (int i = 0 ; i <= 7 ; ++i)
		{
			AttributeIncrease[i] = Double.parseDouble(PetEvolutionProperties.get(Job)[i + 1]) ;
			ChanceIncrease[i] = Double.parseDouble(PetEvolutionProperties.get(Job)[i + 9]) ;
		}
	}
	
	private static PersonalAttributes InitializePersonalAttributes(int Job)
	{
		BasicAttribute Life = new BasicAttribute(Integer.parseInt(PetProperties.get(Job)[2]), Integer.parseInt(PetProperties.get(Job)[2]), 1) ;
		BasicAttribute Mp = new BasicAttribute(Integer.parseInt(PetProperties.get(Job)[3]), Integer.parseInt(PetProperties.get(Job)[3]), 1) ;
		BasicAttribute Exp = new BasicAttribute(0, 50, 1) ;
		BasicAttribute Satiation = new BasicAttribute(100, 100, 1) ;
		BasicAttribute Thirst = new BasicAttribute(100, 100, 1) ;
		return new PersonalAttributes(Life, Mp, Exp, Satiation, Thirst) ;
	}
	
	private static BattleAttributes InitializeBattleAttributes(int Job)
	{
		BasicBattleAttribute PhyAtk = new BasicBattleAttribute(Double.parseDouble(PetProperties.get(Job)[5]), 0, 0) ;
		BasicBattleAttribute MagAtk = new BasicBattleAttribute(Double.parseDouble(PetProperties.get(Job)[6]), 0, 0) ;
		BasicBattleAttribute PhyDef = new BasicBattleAttribute(Double.parseDouble(PetProperties.get(Job)[7]), 0, 0) ;
		BasicBattleAttribute MagDef = new BasicBattleAttribute(Double.parseDouble(PetProperties.get(Job)[8]), 0, 0) ;
		BasicBattleAttribute Dex = new BasicBattleAttribute(Double.parseDouble(PetProperties.get(Job)[9]), 0, 0) ;
		BasicBattleAttribute Agi = new BasicBattleAttribute(Double.parseDouble(PetProperties.get(Job)[10]), 0, 0) ;
		double[] Crit = new double[] {Double.parseDouble(PetProperties.get(Job)[11]), 0, Double.parseDouble(PetProperties.get(Job)[12]), 0} ;
		BattleSpecialAttribute Stun = new BattleSpecialAttribute(Double.parseDouble(PetProperties.get(Job)[13]), 0, Double.parseDouble(PetProperties.get(Job)[14]), 0, Integer.parseInt(PetProperties.get(Job)[15])) ;
		BattleSpecialAttribute Block = new BattleSpecialAttribute(Double.parseDouble(PetProperties.get(Job)[16]), 0, Double.parseDouble(PetProperties.get(Job)[17]), 0, Integer.parseInt(PetProperties.get(Job)[18])) ;
		BattleSpecialAttributeWithDamage Blood = new BattleSpecialAttributeWithDamage(Double.parseDouble(PetProperties.get(Job)[19]), 0, Double.parseDouble(PetProperties.get(Job)[20]), 0, Integer.parseInt(PetProperties.get(Job)[21]), 0, Integer.parseInt(PetProperties.get(Job)[22]), 0, Integer.parseInt(PetProperties.get(Job)[23])) ;
		BattleSpecialAttributeWithDamage Poison = new BattleSpecialAttributeWithDamage(Double.parseDouble(PetProperties.get(Job)[24]), 0, Double.parseDouble(PetProperties.get(Job)[25]), 0, Integer.parseInt(PetProperties.get(Job)[26]), 0, Integer.parseInt(PetProperties.get(Job)[27]), 0, Integer.parseInt(PetProperties.get(Job)[28])) ;
		BattleSpecialAttribute Silence = new BattleSpecialAttribute(Double.parseDouble(PetProperties.get(Job)[29]), 0, Double.parseDouble(PetProperties.get(Job)[30]), 0, Integer.parseInt(PetProperties.get(Job)[31])) ;
		LiveBeingStatus status = new LiveBeingStatus() ;
		return new BattleAttributes(PhyAtk, MagAtk, PhyDef, MagDef, Dex, Agi, Crit, Stun, Block, Blood, Poison, Silence, status) ;
	}

	public ArrayList<Spell> InitializePetSpells()
    {
		SpellType[] allSpellTypes = Game.getAllSpellTypes() ;
		
		ArrayList<Spell> petspells = new ArrayList<>() ;
		//ArrayList<String[]> PetSpellsInput = UtilG.ReadcsvFile(Game.CSVPath + "PetSpells.csv") ;	
		ArrayList<String[]> PetSpellsBuffsInput = UtilG.ReadcsvFile(Game.CSVPath + "PetSpellsBuffs.csv") ;
		ArrayList<String[]> PetSpellsNerfsInput = UtilG.ReadcsvFile(Game.CSVPath + "PetSpellsNerfs.csv") ;
		double[][][] PetSpellBuffs = new double[Pet.NumberOfSpells][14][13] ;	// [Life, MP, PhyAtk, MagAtk, PhyDef, MagDef, Dex, Agi, Crit, Stun, Block, Blood, Poison, Silence][atk chance %, atk chance, chance, def chance %, def chance, chance, atk %, atk, chance, def %, def, chance, duration]		
		double[][][] PetSpellNerfs = new double[Pet.NumberOfSpells][14][13] ;	// [Life, MP, PhyAtk, MagAtk, PhyDef, MagDef, Dex, Agi, Crit, Stun, Block, Blood, Poison, Silence][atk chance %, atk chance, chance, def chance %, def chance, chance, atk %, atk, chance, def %, def, chance, duration]	
		String[][] spellsInfo = new String[NumberOfSpells][2] ;
		for (int i = 0 ; i <= Pet.NumberOfSpells - 1 ; i += 1)
		{
			int ID = i + Job*Pet.NumberOfSpells ;
			int BuffCont = 0, NerfCont = 0 ;
			for (int j = 0 ; j <= 14 - 1 ; j += 1)
			{
				if (j == 11 | j == 12)
				{
					for (int k = 0 ; k <= 13 - 1 ; k += 1)
					{
						PetSpellBuffs[i][j][k] = Double.parseDouble(PetSpellsBuffsInput.get(ID)[BuffCont + 3]) ;
						BuffCont += 1 ;
					}
				}
				else
				{
					PetSpellBuffs[i][j][0] = Double.parseDouble(PetSpellsBuffsInput.get(ID)[BuffCont + 3]) ;
					PetSpellBuffs[i][j][1] = Double.parseDouble(PetSpellsBuffsInput.get(ID)[BuffCont + 4]) ;
					PetSpellBuffs[i][j][2] = Double.parseDouble(PetSpellsBuffsInput.get(ID)[BuffCont + 5]) ;
					PetSpellBuffs[i][j][12] = Double.parseDouble(PetSpellsBuffsInput.get(ID)[BuffCont + 6]) ;
					BuffCont += 4 ;
				}
			}
			for (int j = 0 ; j <= 14 - 1 ; j += 1)
			{
				if (j == 11 | j == 12)
				{
					for (int k = 0 ; k <= 13 - 1 ; k += 1)
					{
						PetSpellNerfs[i][j][k] = Double.parseDouble(PetSpellsNerfsInput.get(ID)[NerfCont + 3]) ;
						NerfCont += 1 ;
					}
				}
				else
				{
					PetSpellNerfs[i][j][0] = Double.parseDouble(PetSpellsNerfsInput.get(ID)[NerfCont + 3]) ;
					PetSpellNerfs[i][j][1] = Double.parseDouble(PetSpellsNerfsInput.get(ID)[NerfCont + 4]) ;
					PetSpellNerfs[i][j][2] = Double.parseDouble(PetSpellsNerfsInput.get(ID)[NerfCont + 5]) ;
					PetSpellNerfs[i][j][12] = Double.parseDouble(PetSpellsNerfsInput.get(ID)[NerfCont + 6]) ;
					NerfCont += 4 ;
				}
			}
			//if (Language.equals("P"))
			//{
				//spellsInfo[i] = new String[] {PetSpellsInput.get(ID)[42], PetSpellsInput.get(ID)[43]} ;
			//}
			//else if (Language.equals("E"))
			//{
			//	spellsInfo[i] = new String[] {PetSpellsInput.get(ID)[44], PetSpellsInput.get(ID)[45]} ;
			//}
			//String Name, int MaxLevel, double MpCost, String Type, int[][] PreRequisites, int Cooldown, int Duration, double[][] Buffs, double[][] Nerfs, double[] AtkMod, double[] DefMod, double[] DexMod, double[] AgiMod, double[] AtkCritMod, double[] DefCritMod, double[] StunMod, double[] BlockMod, double[] BloodMod, double[] PoisonMod, double[] SilenceMod, String Elem, String[] Info
			/*String Name = PetSpellsInput.get(ID)[4] ;
			int MaxLevel = Integer.parseInt(PetSpellsInput.get(ID)[5]) ;
			double MpCost = Double.parseDouble(PetSpellsInput.get(ID)[6]) ;
			SpellTypes Type ;
			if (PetSpellsInput.get(ID)[7].equals("Active"))
			{
				Type = SpellTypes.active ;
			}
			else if (PetSpellsInput.get(ID)[7].equals("Passive"))
			{
				Type = SpellTypes.passive ;
			}
			else if (PetSpellsInput.get(ID)[7].equals("Offensive"))
			{
				Type = SpellTypes.offensive ;
			}
			else
			{
				Type = SpellTypes.support ;
			}
			Map<Spell, Integer> preRequisites = new HashMap<>() ;
			for (int p = 0 ; p <= 6 - 1 ; p += 2)
			{
				if (-1 < Integer.parseInt(PetSpellsInput.get(ID)[p + 8]))
				{
					preRequisites.put(spell.get(Integer.parseInt(PetSpellsInput.get(ID)[p + 8])), Integer.parseInt(PetSpellsInput.get(ID)[p + 9])) ;
				}
			}
			int Cooldown = Integer.parseInt(PetSpellsInput.get(ID)[14]) ;
			int Duration = Integer.parseInt(PetSpellsInput.get(ID)[15]) ;
			double[] Atk = new double[] {Double.parseDouble(PetSpellsInput.get(ID)[16]), Double.parseDouble(PetSpellsInput.get(ID)[17])} ;
			double[] Def = new double[] {Double.parseDouble(PetSpellsInput.get(ID)[18]), Double.parseDouble(PetSpellsInput.get(ID)[19])} ;
			double[] Dex = new double[] {Double.parseDouble(PetSpellsInput.get(ID)[20]), Double.parseDouble(PetSpellsInput.get(ID)[21])} ;
			double[] Agi = new double[] {Double.parseDouble(PetSpellsInput.get(ID)[22]), Double.parseDouble(PetSpellsInput.get(ID)[23])} ;
			double[] AtkCrit = new double[] {Double.parseDouble(PetSpellsInput.get(ID)[24])} ;
			double[] DefCrit = new double[] {Double.parseDouble(PetSpellsInput.get(ID)[25])} ;
			double[] Stun = new double[] {Double.parseDouble(PetSpellsInput.get(ID)[26]), Double.parseDouble(PetSpellsInput.get(ID)[27]), Double.parseDouble(PetSpellsInput.get(ID)[28])} ;
			double[] Block = new double[] {Double.parseDouble(PetSpellsInput.get(ID)[29]), Double.parseDouble(PetSpellsInput.get(ID)[30]), Double.parseDouble(PetSpellsInput.get(ID)[31])} ;
			double[] Blood = new double[] {Double.parseDouble(PetSpellsInput.get(ID)[32]), Double.parseDouble(PetSpellsInput.get(ID)[33]), Double.parseDouble(PetSpellsInput.get(ID)[34])} ;
			double[] Poison = new double[] {Double.parseDouble(PetSpellsInput.get(ID)[35]), Double.parseDouble(PetSpellsInput.get(ID)[36]), Double.parseDouble(PetSpellsInput.get(ID)[37])} ;
			double[] Silence = new double[] {Double.parseDouble(PetSpellsInput.get(ID)[38]), Double.parseDouble(PetSpellsInput.get(ID)[39]), Double.parseDouble(PetSpellsInput.get(ID)[40])} ;
			String Elem = PetSpellsInput.get(ID)[41] ;*/
			
			petspells.add(new Spell(allSpellTypes[ID])) ;	
		}
		
		petspells.get(0).incLevel(1) ;
		return petspells ;
    }
	
	public Color getColor() {return color ;}
	public int getJob() {return Job ;}
	public MovingAnimations getMovingAnimations() {return movingAni ;}
	public List<Spell> getSpells() {return spells ;}
	public int getSpellPoints() {return spellPoints ;}
	public BasicAttribute getLife() {return PA.getLife() ;}
	public BasicAttribute getMp() {return PA.getMp() ;}
	public BasicBattleAttribute getPhyAtk() {return BA.getPhyAtk() ;}
	public BasicBattleAttribute getMagAtk() {return BA.getMagAtk() ;}
	public BasicBattleAttribute getPhyDef() {return BA.getPhyDef() ;}
	public BasicBattleAttribute getMagDef() {return BA.getMagDef() ;}
	public BasicBattleAttribute getDex() {return BA.getDex() ;}
	public BasicBattleAttribute getAgi() {return BA.getAgi() ;}
	public double[] getCrit() {return BA.getCrit() ;}
	public BattleSpecialAttribute getStun() {return BA.getStun() ;}
	public BattleSpecialAttribute getBlock() {return BA.getBlock() ;}
	public BattleSpecialAttributeWithDamage getBlood() {return BA.getBlood() ;}
	public BattleSpecialAttributeWithDamage getPoison() {return BA.getPoison() ;}
	public BattleSpecialAttribute getSilence() {return BA.getSilence() ;}
	public double[] getElemMult() {return ElemMult ;}
	public BasicAttribute getExp() {return PA.getExp() ;}
	public BasicAttribute getSatiation() {return PA.getSatiation() ;}
	//public int[][] getActions() {return PA.Actions ;}
	public int[] getStatusCounter() {return StatusCounter ;}

	public boolean isAlive()
	{
		return (0 < PA.getLife().getCurrentValue()) ;
	}
	
	public Point CenterPos()
	{
		return new Point((int) (pos.x + 0.5 * size.width), (int) (pos.y - 0.5 * size.height)) ;
	}

	public String fight(String[] ActionKeys)
	{
		int move = -1 ;
		if (10 <= PA.getMp().getCurrentValue())	// if there is enough mp
		{
			move = (int)(3*Math.random() - 0.01) ;	// consider using spell
		}
		else
		{
			move = (int)(2*Math.random() - 0.01) ;	// only physical atk of def
		}
		if (move == 0)
		{
			return ActionKeys[1] ;
		}
		if (move == 1)
		{
			return ActionKeys[3] ;
		}
		if (move == 2)
		{
			return String.valueOf((int)(5*Math.random() - 0.01)) ;
		}
		return "" ;
	}
	public Point Follow(Point Pos, Point Target, int step, double mindist)
	{
		Point pos = new Point(Pos.x, Pos.y) ; // Prevent the method from modifying the original variable Pos
		step = 1 ;
		double distY = Math.abs(pos.y - Target.y) ;
		double distX = Math.abs(pos.x - Target.x) ;
		if (mindist < pos.distance(Target))
		{
			if (distY < distX)
			{
				if (pos.x < Target.x)
				{
					pos.x += step ;
				}
				else
				{
					pos.x += -step ;
				}
			}
			else
			{
				if (pos.y < Target.y)
				{
					pos.y += step ;
				}
				else
				{
					pos.y += -step ;
				}
			}
		}
		return pos ;
	}
	public boolean closeToPlayer(Point playerPos)
	{
		return UtilG.dist(pos, playerPos) <= 40 ;
	}
	public void Move(Point playerPos, GameMap playerMap, Creature opponent, String playerElem)
	{
		Point nextPos ;
		if (opponent != null)
		{
			nextPos = Follow(pos, opponent.getPos(), step, step) ;
		}
		else if (closeToPlayer(playerPos))
		{
			if (Math.random() <= 0.2)
			{
				dir = PA.randomDir() ;
			}
			nextPos = PA.CalcNewPos(dir, pos, step) ;
		}
		else
		{
			nextPos = Follow(pos, playerPos, step, step) ;
		}
		if (playerMap.GroundIsWalkable(nextPos, playerElem))
		{
			setPos(nextPos) ;
		}
	}
	public void Dies()
	{
		PA.getLife().incCurrentValue(-PA.getLife().getCurrentValue()) ;
	}
	public void ActivateActionCounters(boolean SomeAnimationIsOn)
	{
		/*if (PA.Actions[1][0] % PA.Actions[1][1] == 0)
		{
			PA.getSatiation()[0] = Math.max(PA.getSatiation()[0] - 1, 0) ;
			if (PA.getSatiation()[0] == 0)	// pet is hungry
			{
				PA.getLife()[0] = Math.max(PA.getLife()[0] - 1, 0) ;
			}
			PA.Actions[1][0] = 0 ;
		}
		if (PA.Actions[2][0] % PA.Actions[2][1] == 0)	// Pet heals mp
		{
			PA.getMp()[0] = (double)(Math.min(PA.getMp()[0] + 0.02*PA.getMp()[1], PA.getMp()[1])) ;	
			PA.Actions[2][0] = 0 ;
		}
		if (PA.Actions[0][0] % PA.Actions[0][1] == 0 & !SomeAnimationIsOn)
		{
			PA.Actions[0][2] = 1 ;	// pet can move
		}*/
	}
	
	public AtkResults useSpell(Spell spell, LiveBeing receiver)
	{
		int spellLevel = spell.getLevel() ;
		int damage = -1 ;
		AttackEffects effect = null ;

		double MagAtk = BA.TotalMagAtk() ;
		double MagDef = receiver.getBA().TotalMagDef() ;
		double AtkDex = BA.TotalDex() ;
		double DefAgi = receiver.getBA().TotalAgi() ;
		double AtkCrit = BA.TotalCritAtkChance() ;
		double DefCrit = receiver.getBA().TotalCritDefChance() ;
		double receiverElemMod = 1 ;
		double[] AtkMod = new double[] {spell.getAtkMod()[0] * spellLevel, 1 + spell.getAtkMod()[1] * spellLevel} ;
		double[] DefMod = new double[] {spell.getDefMod()[0] * spellLevel, 1 + spell.getDefMod()[1] * spellLevel} ;
		double[] DexMod = new double[] {spell.getDexMod()[0] * spellLevel, 1 + spell.getDexMod()[1] * spellLevel} ;
		double[] AgiMod = new double[] {spell.getAgiMod()[0] * spellLevel, 1 + spell.getAgiMod()[1] * spellLevel} ;
		double AtkCritMod = spell.getAtkCritMod()[0] * spellLevel ;	// Critical atk modifier
		double DefCritMod = spell.getDefCritMod()[0] * spellLevel ;	// Critical def modifier
		double BlockDef = receiver.getBA().getStatus().getBlock() ;
		double BasicAtk = 0 ;
		double BasicDef = 0 ;
		String[] AtkElem = new String[] {spell.getElem(), elem[1], elem[4]} ;
		String[] DefElem = receiver.defElems() ;
		
		BasicAtk = MagAtk ;
		BasicDef = MagDef ;
		
		effect = Battle.calcEffect(DexMod[0] + AtkDex*DexMod[1], AgiMod[0] + DefAgi*AgiMod[1], AtkCrit + AtkCritMod, DefCrit + DefCritMod, BlockDef) ;
		damage = Battle.calcDamage(effect, AtkMod[0] + BasicAtk*AtkMod[1], DefMod[0] + BasicDef*DefMod[1], AtkElem, DefElem, receiverElemMod) ;
		
		return new AtkResults(AtkTypes.magical, effect, damage) ;
	}
	
	public void TakeBloodAndPoisonDamage(Creature creature)
	{
		int BloodDamage = 0 ;
		int PoisonDamage = 0 ;
		if (0 < BA.getStatus().getBlood())
		{
			BloodDamage = (int) Math.max(creature.getBA().getBlood().TotalAtk() - BA.getBlood().TotalDef(), 0) ;
		}
		if (0 < BA.getStatus().getPoison())
		{
			PoisonDamage = (int) Math.max(creature.getBA().getPoison().TotalAtk() - BA.getPoison().TotalDef(), 0) ;
		}
		PA.getLife().incCurrentValue(-BloodDamage - PoisonDamage) ;
	}
	public void Win(Creature creature)
	{
		PA.getExp().incCurrentValue((int) (creature.getExp().getCurrentValue() * PA.getExp().getMultiplier())); ;
	}
	public boolean shouldLevelUP() {return getExp().getMaxValue() <= getExp().getCurrentValue() ;}
	public void checkLevelUp(Animations ani)
	{
		if (shouldLevelUP())
		{
			double[] attributesIncrease = CalcAttIncrease() ;
			setLevel(level + 1) ;
			spellPoints += 1 ;
			PA.getLife().incMaxValue((int) attributesIncrease[0]) ;
			PA.getLife().setToMaximum() ;
			PA.getMp().incMaxValue((int) attributesIncrease[1]) ;	
			PA.getMp().setToMaximum() ;
			BA.getPhyAtk().incBaseValue(attributesIncrease[2]) ;
			BA.getMagAtk().incBaseValue(attributesIncrease[3]) ;
			BA.getPhyDef().incBaseValue(attributesIncrease[4]) ;
			BA.getMagDef().incBaseValue(attributesIncrease[5]) ;
			BA.getAgi().incBaseValue(attributesIncrease[6]) ;
			BA.getDex().incBaseValue(attributesIncrease[7]) ;
			PA.getExp().incMaxValue((int) attributesIncrease[8]) ;		

//			ani.SetAniVars(13, new Object[] {150, attributesIncrease, level, Game.ColorPalette[5]}) ;
//			ani.StartAni(13) ;
		}
	}
	public double[] CalcAttIncrease()
	{
		// Life, Mp, Phyatk, Magatk, Phydef, Magdef, Dex, Agi, Exp
		double[] Increase = new double[AttributeIncrease.length + 1] ;
		for (int i = 0 ; i <= AttributeIncrease.length - 1 ; ++i)
		{
			if (Math.random() <= ChanceIncrease[i])
			{
				Increase[i] = AttributeIncrease[i] ;
			}
		}
		Increase[AttributeIncrease.length] = (double) (10*(3*Math.pow(level - 1, 2) + 3*(level - 1) + 1) - 5) ;
		return Increase ;
	}

	/* Save and load methods */
	public void Save(BufferedWriter bW)
	{
		try
		{
			bW.write("\nPet name: \n" + getName()) ;
			bW.write("\nPet size: \n" + getSize()) ;
			bW.write("\nPet color: \n" + getColor()) ;
			bW.write("\nPet job: \n" + getJob()) ;
			bW.write("\nPet coords: \n" + getPos()) ;
			//bW.write("\nPet skill: \n" + Arrays.toString(getSpells())) ;
			bW.write("\nPet skillPoints: \n" + getSpellPoints()) ;
			//bW.write("\nPet life: \n" + Arrays.toString(getLife())) ;
			//bW.write("\nPet mp: \n" + Arrays.toString(getMp())) ;
			bW.write("\nPet range: \n" + getRange()) ;
			//bW.write("\nPet phyAtk: \n" + Arrays.toString(getPhyAtk())) ;
			//bW.write("\nPet magAtk: \n" + Arrays.toString(getMagAtk())) ;
			//bW.write("\nPet phyDef: \n" + Arrays.toString(getPhyDef())) ;
			//bW.write("\nPet magDef: \n" + Arrays.toString(getMagDef())) ;
			//bW.write("\nPet dex: \n" + Arrays.toString(getDex())) ;
			//bW.write("\nPet agi: \n" + Arrays.toString(getAgi())) ;
			bW.write("\nPet crit: \n" + Arrays.toString(getCrit())) ;
//			bW.write("\nPet stun: \n" + Arrays.toString(getStun())) ;
//			bW.write("\nPet block: \n" + Arrays.toString(getBlock())) ;
//			bW.write("\nPet blood: \n" + Arrays.toString(getBlood())) ;
//			bW.write("\nPet poison: \n" + Arrays.toString(getPoison())) ;
//			bW.write("\nPet silence: \n" + Arrays.toString(getSilence())) ;
			bW.write("\nPet elem: \n" + Arrays.toString(getElem())) ;
			bW.write("\nPet elem mult: \n" + Arrays.toString(getElemMult())) ;
			bW.write("\nPet level: \n" + getLevel()) ;
			bW.write("\nPet step: \n" + getStep()) ;
			//bW.write("\nPet satiation: \n" + Arrays.toString(getSatiation())) ;
			//bW.write("\nPet exp: \n" + Arrays.toString(getExp())) ;
//			bW.write("\nPet status: \n" + Arrays.toString(getBA().getSpecialStatus())) ; 
			//bW.write("\nPet actions: \n" + Arrays.deepToString(getActions())) ; 
			//bW.write("\nPet battle actions: \n" + Arrays.deepToString(getBA().getBattleActions())) ; 
			bW.write("\nPet status counter: \n" + Arrays.toString(getStatusCounter())) ;
		}
		catch (IOException e)
		{
			System.out.println("Error writing the pet attributes to file") ;
		}
	}
	public void Load(String[][] ReadFile)
	{
		/*int NumberOfPlayerAttributes = 49 ;
		PA.setName(ReadFile[2*(NumberOfPlayerAttributes + 1)][0]) ;
		PA.setSize((Size) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 2)]), "String", "int")) ;
		color = UtilG.toColor(ReadFile[2*(NumberOfPlayerAttributes + 3)])[0] ;
		Job = Integer.parseInt(ReadFile[2*(NumberOfPlayerAttributes + 4)][0]) ;
		setPos((Point) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 5)]), "String", "int")) ;
		spell = (Spells[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 6)]), "String", "int") ;
		spellPoints = Integer.parseInt(ReadFile[2*(NumberOfPlayerAttributes + 7)][0]) ;
		PA.setLife((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 8)]), "String", "double")) ;
		PA.setMp((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 9)]), "String", "double")) ;
		PA.setRange(Double.parseDouble(ReadFile[2*(NumberOfPlayerAttributes + 10)][0])) ;
		BA.setPhyAtk((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 11)]), "String", "double")) ;
		BA.setMagAtk((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 12)]), "String", "double")) ;
		BA.setPhyDef((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 13)]), "String", "double")) ;
		BA.setMagDef((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 14)]), "String", "double")) ;
		BA.setDex((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 15)]), "String", "double")) ;
		BA.setAgi((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 16)]), "String", "double")) ;
		BA.setCrit((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 17)]), "String", "double")) ;
		BA.setStun((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 18)]), "String", "double")) ;
		BA.setBlock((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 19)]), "String", "double")) ;
		BA.setBlood((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 20)]), "String", "double")) ;
		BA.setPoison((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 21)]), "String", "double")) ;
		BA.setSilence((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 22)]), "String", "double")) ;
		PA.Elem = (String[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 23)]), "String", "String") ;
		ElemMult = (double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 24)]), "String", "double") ;
		PA.setLevel(Integer.parseInt(ReadFile[2*(NumberOfPlayerAttributes + 25)][0])) ;
		PA.setStep(Integer.parseInt(ReadFile[2*(NumberOfPlayerAttributes + 26)][0])) ;
		PA.setSatiation((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 27)]), "String", "double")) ;
		PA.setExp((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 28)]), "String", "double")) ;
		BA.setSpecialStatus((int[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 29)]), "String", "int")) ;
		PA.Actions = (int[][]) UtilG.ConvertDoubleArray(UtilG.deepToString(ReadFile[2*(NumberOfPlayerAttributes + 30)], 3), "String", "int") ;
		BA.setBattleActions((int[][]) UtilG.ConvertDoubleArray(UtilG.deepToString(ReadFile[2*(NumberOfPlayerAttributes + 31)], 3), "String", "int")) ;
		StatusCounter = (int[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 32)]), "String", "int") ;*/
	}
	
	
	/* Drawing methods */
	public void display(Point Pos, Scale scale, DrawingOnPanel DP)
	{
		//	TODO add moving animations
		double OverallAngle = DrawingOnPanel.stdAngle ;
		DP.DrawImage(movingAni.idleGif, Pos, OverallAngle, scale, Align.center) ;
	}
	
}