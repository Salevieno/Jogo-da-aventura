package LiveBeings ;

import java.awt.Color ;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon ;

import GameComponents.SpellTypes;
import Graphics.Animations;
import Graphics.DrawPrimitives;
import Utilities.Scale;
import Utilities.Size;
import Utilities.UtilG;
import Windows.PlayerAttributesWindow;
import Main.Game;
import Maps.Maps;

public class Pet extends LiveBeing
{
	private Color color ;
	private int Job ;
	private int spellPoints ;
	private double[] ElemMult ;		// [Neutral, Water, Fire, Plant, Earth, Air, Thunder, Light, Dark, Snow]
	private int[] StatusCounter ;	// [Life, Mp, Phy atk, Phy def, Mag atk, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence]
	
	public static int NumberOfSpells = 5 ;
	public String action = "" ;
	public static double[] AttributeIncrease ;
	public static double[] ChanceIncrease ;
	
	private static ArrayList<String[]> PetProperties = UtilG.ReadcsvFile(Game.CSVPath + "PetInitialStats.csv") ;
	private static ArrayList<String[]> PetEvolutionProperties = UtilG.ReadcsvFile(Game.CSVPath + "PetEvolution.csv") ;
	
	public Pet(int Job)
	{
		super(Job, InitializePersonalAttributes(Job), InitializeBattleAttributes(Job),
				new MovingAnimations(new ImageIcon(Game.ImagesPath + "PetType" + String.valueOf(Job) + ".png").getImage(),
				new ImageIcon(Game.ImagesPath + "PetType" + String.valueOf(Job) + ".png").getImage(),
				new ImageIcon(Game.ImagesPath + "PetType" + String.valueOf(Job) + ".png").getImage(),
				new ImageIcon(Game.ImagesPath + "PetType" + String.valueOf(Job) + ".png").getImage(),
				new ImageIcon(Game.ImagesPath + "PetType" + String.valueOf(Job) + ".png").getImage()),
				new PlayerAttributesWindow()) ;
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
		String Name = PetProperties.get(Job)[0] ;
		int Level = 1 ;
		int ProJob = 0 ;
		Maps Map = null ;
		Point Pos = new Point(0, 0) ;
		String dir = Player.MoveKeys[0] ;
		States state = States.idle ;
		Size size = new Size (new ImageIcon(Game.ImagesPath + "PetType" + String.valueOf(Job) + "png").getImage().getWidth(null), new ImageIcon(Game.ImagesPath + "PetType" + String.valueOf(Job) + "png").getImage().getHeight(null)) ;	
		double[] Life = new double[] {Integer.parseInt(PetProperties.get(Job)[2]), Integer.parseInt(PetProperties.get(Job)[2])} ;
		double[] Mp = new double[] {Integer.parseInt(PetProperties.get(Job)[3]), Integer.parseInt(PetProperties.get(Job)[3])} ;
		int Range = Integer.parseInt(PetProperties.get(Job)[4]) ;
		int Step = Integer.parseInt(PetProperties.get(Job)[32]) ;
		int[] Exp = new int[] {0, 50, 1} ;
		int[] Satiation = new int[] {100, 100, 1} ;
		int[] Thirst = new int[] {100, 100, 0} ;
		String[] Elem = new String[] {"n", "n", "n", "n", "n"} ;
		int[][] Actions = new int[][] {{0, Integer.parseInt(PetProperties.get(Job)[33]), 0}, {0, Integer.parseInt(PetProperties.get(Job)[34]), 0}, {0, Integer.parseInt(PetProperties.get(Job)[35]), 0}} ;
		String currentAction = "" ;
		int countmove = 0 ;
		return new PersonalAttributes(Name, Level, Job, ProJob, Map, Pos, dir, state, size, Life, Mp, Range, Step, Exp, Satiation, Thirst, Elem, Actions, currentAction, countmove) ;
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
		double[] Stun = new double[] {Double.parseDouble(PetProperties.get(Job)[13]), 0, Double.parseDouble(PetProperties.get(Job)[14]), 0, Double.parseDouble(PetProperties.get(Job)[15])} ;
		double[] Block = new double[] {Double.parseDouble(PetProperties.get(Job)[16]), 0, Double.parseDouble(PetProperties.get(Job)[17]), 0, Double.parseDouble(PetProperties.get(Job)[18])} ;
		double[] Blood = new double[] {Double.parseDouble(PetProperties.get(Job)[19]), 0, Double.parseDouble(PetProperties.get(Job)[20]), 0, Double.parseDouble(PetProperties.get(Job)[21]), 0, Double.parseDouble(PetProperties.get(Job)[22]), 0, Double.parseDouble(PetProperties.get(Job)[23])} ;
		double[] Poison = new double[] {Double.parseDouble(PetProperties.get(Job)[24]), 0, Double.parseDouble(PetProperties.get(Job)[25]), 0, Double.parseDouble(PetProperties.get(Job)[26]), 0, Double.parseDouble(PetProperties.get(Job)[27]), 0, Double.parseDouble(PetProperties.get(Job)[28])} ;
		double[] Silence = new double[] {Double.parseDouble(PetProperties.get(Job)[29]), 0, Double.parseDouble(PetProperties.get(Job)[30]), 0, Double.parseDouble(PetProperties.get(Job)[31])} ;
		int[] Status = new int[8] ;
		int[] SpecialStatus = new int[5] ;
		int[][] BattleActions = new int[][] {{0, Integer.parseInt(PetProperties.get(Job)[36]), 0}} ;
		return new BattleAttributes(PhyAtk, MagAtk, PhyDef, MagDef, Dex, Agi, Crit, Stun, Block, Blood, Poison, Silence, Status, SpecialStatus, BattleActions) ;
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
	
	public String getName() {return PA.getName() ;}
	public Size getSize() {return PA.getSize() ;}
	public Color getColor() {return color ;}
	public int getJob() {return Job ;}
	public MovingAnimations getMovingAnimations() {return movingAni ;}
	public Point getPos() {return PA.getPos() ;}
	public ArrayList<Spell> getSpells() {return spells ;}
	public int getSpellPoints() {return spellPoints ;}
	public double[] getLife() {return PA.getLife() ;}
	public double[] getMp() {return PA.getMp() ;}
	public double getRange() {return PA.getRange() ;}
	public BasicBattleAttribute getPhyAtk() {return BA.getPhyAtk() ;}
	public BasicBattleAttribute getMagAtk() {return BA.getMagAtk() ;}
	public BasicBattleAttribute getPhyDef() {return BA.getPhyDef() ;}
	public BasicBattleAttribute getMagDef() {return BA.getMagDef() ;}
	public BasicBattleAttribute getDex() {return BA.getDex() ;}
	public BasicBattleAttribute getAgi() {return BA.getAgi() ;}
	public double[] getCrit() {return BA.getCrit() ;}
	public double[] getStun() {return BA.getStun() ;}
	public double[] getBlock() {return BA.getBlock() ;}
	public double[] getBlood() {return BA.getBlood() ;}
	public double[] getPoison() {return BA.getPoison() ;}
	public double[] getSilence() {return BA.getSilence() ;}
	public String[] getElem() {return PA.Elem ;}
	public double[] getElemMult() {return ElemMult ;}
	public int getLevel() {return PA.getLevel() ;}
	public int getStep() {return PA.getStep() ;}
	public int[] getExp() {return PA.getExp() ;}
	public int[] getSatiation() {return PA.getSatiation() ;}
	public int[][] getActions() {return PA.Actions ;}
	public int[] getStatusCounter() {return StatusCounter ;}
	public ArrayList<String> getCombo() {return PA.getCombo() ;}
	public String getAction() {return action ;}
	public void setCombo(ArrayList<String> newValue) {PA.setCombo(newValue); ;}

	
	
	public Point CenterPos()
	{
		return new Point((int) (PA.getPos().x + 0.5 * PA.getSize().x), (int) (PA.getPos().y - 0.5 * PA.getSize().y)) ;
	}

	public String Action(String[] ActionKeys)
	{
		int move = -1 ;
		if (10 <= PA.getMp()[0])	// if there is enough mp
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
	public void Follow(Point Pos, Point Target, int step, double mindist)
	{
		Point pos = new Point(Pos.x, Pos.y) ; // Prevent the method from modifying the original variable Pos
		double verdist = Math.abs(pos.y - Target.y), hordist = Math.abs(pos.x - Target.x) ;
		if (mindist < pos.distance(Target))
		{
			if (verdist < hordist)
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
		PA.setPos(pos) ;
	}
	public void Move(Player player, Maps[] maps)
	{
		Point NextPos = new Point(0, 0) ;
		Follow(PA.getPos(), player.getPos(), PA.getStep(), PA.getStep()) ;
		if (player.getMap().GroundIsWalkable(NextPos, player.getElem()[4]))
		{
			PA.setPos(NextPos) ;
		}
	}
	public boolean actionIsAnAtk()
	{
		if (PA.getCurrentAction().equals(Player.BattleKeys[0]))
		{
			return true ;
		}
		return false ;
	}
	public boolean actionIsASpell()
	{
		if (UtilG.ArrayContains(Player.SpellKeys, PA.getCurrentAction()))
		{
			return true ;
		}
		return false ;
	}
	public void ActivateActionCounters(boolean SomeAnimationIsOn)
	{
		if (PA.Actions[1][0] % PA.Actions[1][1] == 0)
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
		}
	}

	public void ActivateBattleActionCounters()
	{
		if (BA.getBattleActions()[0][0] == BA.getBattleActions()[0][1])
		{
			BA.getBattleActions()[0][2] = 1 ;	// Pet can atk
		}
	}

	public boolean usedSkill()
	{
		return UtilG.isNumeric(BA.getCurrentAction()) ;
	}
	public void TakeBloodAndPoisonDamage(Creature creature)
	{
		double BloodDamage = 0 ;
		double PoisonDamage = 0 ;
		if (0 < BA.getSpecialStatus()[2])	// Blood
		{
			BloodDamage = Math.max(creature.getBA().TotalBloodAtk() - BA.TotalBloodDef(), 0) ;
		}
		if (0 < BA.getSpecialStatus()[3])	// Poison
		{
			PoisonDamage = Math.max(creature.getBA().TotalPoisonAtk() - BA.TotalPoisonDef(), 0) ;
		}
		PA.getLife()[0] += -BloodDamage - PoisonDamage ;
	}
	public void Win(Creature creature)
	{
		PA.getExp()[0] += creature.getExp()[0]*PA.getExp()[2] ;
	}
	public boolean ShouldLevelUP()
	{
		if (getExp()[1] <= getExp()[0])
		{
			return true ;
		}
		return false ;
	}
	public void LevelUp(Animations ani)
	{
		double[] attributesIncrease = CalcAttIncrease() ;
		PA.setLevel(PA.getLevel() + 1) ;
		spellPoints += 1 ;
		PA.getLife()[1] += attributesIncrease[0] ;
		PA.getLife()[0] = PA.getLife()[1] ;
		PA.getMp()[1] += attributesIncrease[1] ;	
		PA.getMp()[0] = PA.getMp()[1] ;
		BA.getPhyAtk().incBaseValue(attributesIncrease[2]) ;
		BA.getMagAtk().incBaseValue(attributesIncrease[3]) ;
		BA.getPhyDef().incBaseValue(attributesIncrease[4]) ;
		BA.getMagDef().incBaseValue(attributesIncrease[5]) ;
		BA.getAgi().incBaseValue(attributesIncrease[6]) ;
		BA.getDex().incBaseValue(attributesIncrease[7]) ;
		PA.getExp()[1] += attributesIncrease[8] ;		

		ani.SetAniVars(14, new Object[] {150, this, attributesIncrease}) ;
		ani.StartAni(14) ;
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
		Increase[AttributeIncrease.length] = (double) (10*(3*Math.pow(PA.getLevel() - 1, 2) + 3*(PA.getLevel() - 1) + 1) - 5) ;
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
			bW.write("\nPet life: \n" + Arrays.toString(getLife())) ;
			bW.write("\nPet mp: \n" + Arrays.toString(getMp())) ;
			bW.write("\nPet range: \n" + getRange()) ;
			//bW.write("\nPet phyAtk: \n" + Arrays.toString(getPhyAtk())) ;
			//bW.write("\nPet magAtk: \n" + Arrays.toString(getMagAtk())) ;
			//bW.write("\nPet phyDef: \n" + Arrays.toString(getPhyDef())) ;
			//bW.write("\nPet magDef: \n" + Arrays.toString(getMagDef())) ;
			//bW.write("\nPet dex: \n" + Arrays.toString(getDex())) ;
			//bW.write("\nPet agi: \n" + Arrays.toString(getAgi())) ;
			bW.write("\nPet crit: \n" + Arrays.toString(getCrit())) ;
			bW.write("\nPet stun: \n" + Arrays.toString(getStun())) ;
			bW.write("\nPet block: \n" + Arrays.toString(getBlock())) ;
			bW.write("\nPet blood: \n" + Arrays.toString(getBlood())) ;
			bW.write("\nPet poison: \n" + Arrays.toString(getPoison())) ;
			bW.write("\nPet silence: \n" + Arrays.toString(getSilence())) ;
			bW.write("\nPet elem: \n" + Arrays.toString(getElem())) ;
			bW.write("\nPet elem mult: \n" + Arrays.toString(getElemMult())) ;
			bW.write("\nPet level: \n" + getLevel()) ;
			bW.write("\nPet step: \n" + getStep()) ;
			bW.write("\nPet satiation: \n" + Arrays.toString(getSatiation())) ;
			bW.write("\nPet exp: \n" + Arrays.toString(getExp())) ;
			bW.write("\nPet status: \n" + Arrays.toString(getBA().getSpecialStatus())) ; 
			bW.write("\nPet actions: \n" + Arrays.deepToString(getActions())) ; 
			bW.write("\nPet battle actions: \n" + Arrays.deepToString(getBA().getBattleActions())) ; 
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
		PA.setPos((Point) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 5)]), "String", "int")) ;
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
	public void display(Point Pos, Scale scale, DrawPrimitives DP)
	{
		//	TODO add moving animations
		double OverallAngle = DrawPrimitives.OverallAngle ;
		DP.DrawImage(movingAni.idleGif, Pos, OverallAngle, scale, new boolean[] {false, false}, "Center", 1) ;
	}
	
}