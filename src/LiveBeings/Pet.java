package LiveBeings ;

import java.awt.Color ;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ImageIcon ;

import Graphics.Animations;
import Graphics.DrawPrimitives;
import Utilities.Size;
import Utilities.UtilG;
import Windows.AttributesWindow;
import Main.Game;
import Maps.Maps;

public class Pet extends LiveBeing
{
	private Color color ;
	private int Job ;
	private Spells[] spell ;
	private int spellPoints ;
	private float[] ElemMult ;		// [Neutral, Water, Fire, Plant, Earth, Air, Thunder, Light, Dark, Snow]
	private int[] StatusCounter ;	// [Life, Mp, Phy atk, Phy def, Mag atk, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence]
	private String[] Combo ;		// Record of the last movement
	
	public static int NumberOfSpells = 5 ;
	public String action = "" ;
	public static float[] AttributeIncrease ;
	public static float[] ChanceIncrease ;
	
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
				new AttributesWindow()) ;
		this.Job = Job ;
		Color[] ColorPalette = Game.ColorPalette ;
		Color[] PetColor = new Color[] {ColorPalette[3], ColorPalette[1], ColorPalette[18], ColorPalette[18]} ;
		color = PetColor[Job] ;
		spell = InitializePetSpells();
		spellPoints = 0 ;

		
		ElemMult = new float[10] ;
		StatusCounter = new int[8] ;
		Combo = new String[1] ;
			
    	AttributeIncrease = new float[8] ;
    	ChanceIncrease = new float[8] ;
		for (int i = 0 ; i <= 7 ; ++i)
		{
			AttributeIncrease[i] = Float.parseFloat(PetEvolutionProperties.get(Job)[i + 1]) ;
			ChanceIncrease[i] = Float.parseFloat(PetEvolutionProperties.get(Job)[i + 9]) ;
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
		String Thought = "Exist" ;
		Size size = new Size (new ImageIcon(Game.ImagesPath + "PetType" + String.valueOf(Job) + "png").getImage().getWidth(null), new ImageIcon(Game.ImagesPath + "PetType" + String.valueOf(Job) + "png").getImage().getHeight(null)) ;	
		float[] Life = new float[] {Integer.parseInt(PetProperties.get(Job)[2]), Integer.parseInt(PetProperties.get(Job)[2])} ;
		float[] Mp = new float[] {Integer.parseInt(PetProperties.get(Job)[3]), Integer.parseInt(PetProperties.get(Job)[3])} ;
		int Range = Integer.parseInt(PetProperties.get(Job)[4]) ;
		int Step = Integer.parseInt(PetProperties.get(Job)[32]) ;
		float[] Exp = new float[] {0, 50, 1} ;
		float[] Satiation = new float[] {100, 100, 1} ;
		float[] Thirst = new float[] {100, 100, 0} ;
		String[] Elem = new String[] {"n", "n", "n", "n", "n"} ;
		int[][] Actions = new int[][] {{0, Integer.parseInt(PetProperties.get(Job)[33]), 0}, {0, Integer.parseInt(PetProperties.get(Job)[34]), 0}, {0, Integer.parseInt(PetProperties.get(Job)[35]), 0}} ;
		String currentAction = "" ;
		int countmove = 0 ;
		return new PersonalAttributes(Name, Level, Job, ProJob, Map, Pos, dir, Thought, size, Life, Mp, Range, Step, Exp, Satiation, Thirst, Elem, Actions, currentAction, countmove) ;
	}
	
	private static BattleAttributes InitializeBattleAttributes(int Job)
	{
		float[] PhyAtk = new float[] {Float.parseFloat(PetProperties.get(Job)[5]), 0, 0} ;
		float[] MagAtk = new float[] {Float.parseFloat(PetProperties.get(Job)[6]), 0, 0} ;
		float[] PhyDef = new float[] {Float.parseFloat(PetProperties.get(Job)[7]), 0, 0} ;
		float[] MagDef = new float[] {Float.parseFloat(PetProperties.get(Job)[8]), 0, 0} ;
		float[] Dex = new float[] {Float.parseFloat(PetProperties.get(Job)[9]), 0, 0} ;
		float[] Agi = new float[] {Float.parseFloat(PetProperties.get(Job)[10]), 0, 0} ;
		float[] Crit = new float[] {Float.parseFloat(PetProperties.get(Job)[11]), 0, Float.parseFloat(PetProperties.get(Job)[12]), 0} ;
		float[] Stun = new float[] {Float.parseFloat(PetProperties.get(Job)[13]), 0, Float.parseFloat(PetProperties.get(Job)[14]), 0, Float.parseFloat(PetProperties.get(Job)[15])} ;
		float[] Block = new float[] {Float.parseFloat(PetProperties.get(Job)[16]), 0, Float.parseFloat(PetProperties.get(Job)[17]), 0, Float.parseFloat(PetProperties.get(Job)[18])} ;
		float[] Blood = new float[] {Float.parseFloat(PetProperties.get(Job)[19]), 0, Float.parseFloat(PetProperties.get(Job)[20]), 0, Float.parseFloat(PetProperties.get(Job)[21]), 0, Float.parseFloat(PetProperties.get(Job)[22]), 0, Float.parseFloat(PetProperties.get(Job)[23])} ;
		float[] Poison = new float[] {Float.parseFloat(PetProperties.get(Job)[24]), 0, Float.parseFloat(PetProperties.get(Job)[25]), 0, Float.parseFloat(PetProperties.get(Job)[26]), 0, Float.parseFloat(PetProperties.get(Job)[27]), 0, Float.parseFloat(PetProperties.get(Job)[28])} ;
		float[] Silence = new float[] {Float.parseFloat(PetProperties.get(Job)[29]), 0, Float.parseFloat(PetProperties.get(Job)[30]), 0, Float.parseFloat(PetProperties.get(Job)[31])} ;
		int[] Status = new int[8] ;
		int[] SpecialStatus = new int[5] ;
		int[][] BattleActions = new int[][] {{0, Integer.parseInt(PetProperties.get(Job)[36]), 0}} ;
		return new BattleAttributes(PhyAtk, MagAtk, PhyDef, MagDef, Dex, Agi, Crit, Stun, Block, Blood, Poison, Silence, Status, SpecialStatus, BattleActions) ;
	}

	public Spells[] InitializePetSpells()
    {
		Spells[] petspells = new Spells[Pet.NumberOfSpells] ;
		ArrayList<String[]> PetSpellsInput = UtilG.ReadcsvFile(Game.CSVPath + "PetSpells.csv") ;	
		ArrayList<String[]> PetSpellsBuffsInput = UtilG.ReadcsvFile(Game.CSVPath + "PetSpellsBuffs.csv") ;
		ArrayList<String[]> PetSpellsNerfsInput = UtilG.ReadcsvFile(Game.CSVPath + "PetSpellsNerfs.csv") ;
		float[][][] PetSpellBuffs = new float[Pet.NumberOfSpells][14][13] ;	// [Life, MP, PhyAtk, MagAtk, PhyDef, MagDef, Dex, Agi, Crit, Stun, Block, Blood, Poison, Silence][atk chance %, atk chance, chance, def chance %, def chance, chance, atk %, atk, chance, def %, def, chance, duration]		
		float[][][] PetSpellNerfs = new float[Pet.NumberOfSpells][14][13] ;	// [Life, MP, PhyAtk, MagAtk, PhyDef, MagDef, Dex, Agi, Crit, Stun, Block, Blood, Poison, Silence][atk chance %, atk chance, chance, def chance %, def chance, chance, atk %, atk, chance, def %, def, chance, duration]	
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
						PetSpellBuffs[i][j][k] = Float.parseFloat(PetSpellsBuffsInput.get(ID)[BuffCont + 3]) ;
						BuffCont += 1 ;
					}
				}
				else
				{
					PetSpellBuffs[i][j][0] = Float.parseFloat(PetSpellsBuffsInput.get(ID)[BuffCont + 3]) ;
					PetSpellBuffs[i][j][1] = Float.parseFloat(PetSpellsBuffsInput.get(ID)[BuffCont + 4]) ;
					PetSpellBuffs[i][j][2] = Float.parseFloat(PetSpellsBuffsInput.get(ID)[BuffCont + 5]) ;
					PetSpellBuffs[i][j][12] = Float.parseFloat(PetSpellsBuffsInput.get(ID)[BuffCont + 6]) ;
					BuffCont += 4 ;
				}
			}
			for (int j = 0 ; j <= 14 - 1 ; j += 1)
			{
				if (j == 11 | j == 12)
				{
					for (int k = 0 ; k <= 13 - 1 ; k += 1)
					{
						PetSpellNerfs[i][j][k] = Float.parseFloat(PetSpellsNerfsInput.get(ID)[NerfCont + 3]) ;
						NerfCont += 1 ;
					}
				}
				else
				{
					PetSpellNerfs[i][j][0] = Float.parseFloat(PetSpellsNerfsInput.get(ID)[NerfCont + 3]) ;
					PetSpellNerfs[i][j][1] = Float.parseFloat(PetSpellsNerfsInput.get(ID)[NerfCont + 4]) ;
					PetSpellNerfs[i][j][2] = Float.parseFloat(PetSpellsNerfsInput.get(ID)[NerfCont + 5]) ;
					PetSpellNerfs[i][j][12] = Float.parseFloat(PetSpellsNerfsInput.get(ID)[NerfCont + 6]) ;
					NerfCont += 4 ;
				}
			}
			//if (Language.equals("P"))
			//{
				spellsInfo[i] = new String[] {PetSpellsInput.get(ID)[42], PetSpellsInput.get(ID)[43]} ;
			//}
			//else if (Language.equals("E"))
			//{
			//	spellsInfo[i] = new String[] {PetSpellsInput.get(ID)[44], PetSpellsInput.get(ID)[45]} ;
			//}
			//String Name, int MaxLevel, float MpCost, String Type, int[][] PreRequisites, int Cooldown, int Duration, float[][] Buffs, float[][] Nerfs, float[] AtkMod, float[] DefMod, float[] DexMod, float[] AgiMod, float[] AtkCritMod, float[] DefCritMod, float[] StunMod, float[] BlockMod, float[] BloodMod, float[] PoisonMod, float[] SilenceMod, String Elem, String[] Info
			String Name = PetSpellsInput.get(ID)[4] ;
			int MaxLevel = Integer.parseInt(PetSpellsInput.get(ID)[5]) ;
			float MpCost = Float.parseFloat(PetSpellsInput.get(ID)[6]) ;
			String Type = PetSpellsInput.get(ID)[7] ;
			int[][] PreRequisites = new int[][] {{Integer.parseInt(PetSpellsInput.get(ID)[8]), Integer.parseInt(PetSpellsInput.get(ID)[9])}, {Integer.parseInt(PetSpellsInput.get(ID)[10]), Integer.parseInt(PetSpellsInput.get(ID)[11])}, {Integer.parseInt(PetSpellsInput.get(ID)[12]), Integer.parseInt(PetSpellsInput.get(ID)[13])}} ;
			int Cooldown = Integer.parseInt(PetSpellsInput.get(ID)[14]) ;
			int Duration = Integer.parseInt(PetSpellsInput.get(ID)[15]) ;
			float[] Atk = new float[] {Float.parseFloat(PetSpellsInput.get(ID)[16]), Float.parseFloat(PetSpellsInput.get(ID)[17])} ;
			float[] Def = new float[] {Float.parseFloat(PetSpellsInput.get(ID)[18]), Float.parseFloat(PetSpellsInput.get(ID)[19])} ;
			float[] Dex = new float[] {Float.parseFloat(PetSpellsInput.get(ID)[20]), Float.parseFloat(PetSpellsInput.get(ID)[21])} ;
			float[] Agi = new float[] {Float.parseFloat(PetSpellsInput.get(ID)[22]), Float.parseFloat(PetSpellsInput.get(ID)[23])} ;
			float[] AtkCrit = new float[] {Float.parseFloat(PetSpellsInput.get(ID)[24])} ;
			float[] DefCrit = new float[] {Float.parseFloat(PetSpellsInput.get(ID)[25])} ;
			float[] Stun = new float[] {Float.parseFloat(PetSpellsInput.get(ID)[26]), Float.parseFloat(PetSpellsInput.get(ID)[27]), Float.parseFloat(PetSpellsInput.get(ID)[28])} ;
			float[] Block = new float[] {Float.parseFloat(PetSpellsInput.get(ID)[29]), Float.parseFloat(PetSpellsInput.get(ID)[30]), Float.parseFloat(PetSpellsInput.get(ID)[31])} ;
			float[] Blood = new float[] {Float.parseFloat(PetSpellsInput.get(ID)[32]), Float.parseFloat(PetSpellsInput.get(ID)[33]), Float.parseFloat(PetSpellsInput.get(ID)[34])} ;
			float[] Poison = new float[] {Float.parseFloat(PetSpellsInput.get(ID)[35]), Float.parseFloat(PetSpellsInput.get(ID)[36]), Float.parseFloat(PetSpellsInput.get(ID)[37])} ;
			float[] Silence = new float[] {Float.parseFloat(PetSpellsInput.get(ID)[38]), Float.parseFloat(PetSpellsInput.get(ID)[39]), Float.parseFloat(PetSpellsInput.get(ID)[40])} ;
			String Elem = PetSpellsInput.get(ID)[41] ;
			
			petspells[i] = new Spells(Name, MaxLevel, MpCost, Type, PreRequisites, Cooldown, Duration, PetSpellBuffs[i], PetSpellNerfs[i], Atk, Def, Dex, Agi, AtkCrit, DefCrit, Stun, Block, Blood, Poison, Silence, Elem, spellsInfo[i]) ;	
		}
		
		petspells[0].incLevel(1) ;
		return petspells ;
    }
	
	public String getName() {return PA.getName() ;}
	public Size getSize() {return PA.getSize() ;}
	public Color getColor() {return color ;}
	public int getJob() {return Job ;}
	public MovingAnimations getMovingAnimations() {return movingAni ;}
	public Point getPos() {return PA.getPos() ;}
	public Spells[] getSpells() {return spell ;}
	public int getSpellPoints() {return spellPoints ;}
	public float[] getLife() {return PA.getLife() ;}
	public float[] getMp() {return PA.getMp() ;}
	public float getRange() {return PA.getRange() ;}
	public PersonalAttributes getPersonalAtt() {return PA ;}
	public BattleAttributes getBattleAtt() {return BA ;}
	public float[] getPhyAtk() {return BA.getPhyAtk() ;}
	public float[] getMagAtk() {return BA.getMagAtk() ;}
	public float[] getPhyDef() {return BA.getPhyDef() ;}
	public float[] getMagDef() {return BA.getMagDef() ;}
	public float[] getDex() {return BA.getDex() ;}
	public float[] getAgi() {return BA.getAgi() ;}
	public float[] getCrit() {return BA.getCrit() ;}
	public float[] getStun() {return BA.getStun() ;}
	public float[] getBlock() {return BA.getBlock() ;}
	public float[] getBlood() {return BA.getBlood() ;}
	public float[] getPoison() {return BA.getPoison() ;}
	public float[] getSilence() {return BA.getSilence() ;}
	public String[] getElem() {return PA.Elem ;}
	public float[] getElemMult() {return ElemMult ;}
	public int getLevel() {return PA.getLevel() ;}
	public int getStep() {return PA.getStep() ;}
	public float[] getExp() {return PA.getExp() ;}
	public float[] getSatiation() {return PA.getSatiation() ;}
	public int[][] getActions() {return PA.Actions ;}
	public int[] getStatusCounter() {return StatusCounter ;}
	public String[] getCombo() {return Combo ;}
	public String getAction() {return action ;}
	public void setCombo(String[] C) {Combo = C ;}

	
	
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
	public void Follow(Point Pos, Point Target, int step, float mindist)
	{
		Point pos = new Point(Pos.x, Pos.y) ; // Prevent the method from modifying the original variable Pos
		float verdist = Math.abs(pos.y - Target.y), hordist = Math.abs(pos.x - Target.x) ;
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
			PA.getMp()[0] = (float)(Math.min(PA.getMp()[0] + 0.02*PA.getMp()[1], PA.getMp()[1])) ;	
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
	public void TakeBloodAndPoisonDamage(Creatures creature)
	{
		float BloodDamage = 0 ;
		float PoisonDamage = 0 ;
		if (0 < BA.getSpecialStatus()[2])	// Blood
		{
			BloodDamage = Math.max(creature.getBattleAtt().TotalBloodAtk() - BA.TotalBloodDef(), 0) ;
		}
		if (0 < BA.getSpecialStatus()[3])	// Poison
		{
			PoisonDamage = Math.max(creature.getBattleAtt().TotalPoisonAtk() - BA.TotalPoisonDef(), 0) ;
		}
		PA.getLife()[0] += -BloodDamage - PoisonDamage ;
	}
	public void train(Object[] PetAtkResult)
	{
		int effect = (int) PetAtkResult[1] ;
		String atkType = (String) PetAtkResult[3] ;
		if (atkType.equals("Physical"))	// Physical atk
		{
			getPhyAtk()[2] += 0.025 / (getPhyAtk()[2] + 1) ;					
		}
		if (effect == 1)	// crit
		{
			if (getJob() == 2)
			{
				getCrit()[1] += 0.000212*0.025 / (getCrit()[1] + 1) ;	// 100% after 10,000 hits starting from 0.12
			}
		}
		if (effect <= 1)	// hit
		{
			getDex()[2] += 0.025 / (getDex()[2] + 1) ;
		}
		if (atkType.equals("Spell"))
		{
			getMagAtk()[2] += 0.025 / (getMagAtk()[2] + 1) ;
		}
		if (atkType.equals("Defense"))
		{
			getPhyDef()[2] += 0.025 / (getPhyDef()[2] + 1) ;
			getMagDef()[2] += 0.025 / (getMagDef()[2] + 1) ;
		}
	}
	public void Win(Creatures creature)
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
		float[] attributesIncrease = CalcAttIncrease() ;
		PA.setLevel(PA.getLevel() + 1) ;
		spellPoints += 1 ;
		PA.getLife()[1] += attributesIncrease[0] ;
		PA.getLife()[0] = PA.getLife()[1] ;
		PA.getMp()[1] += attributesIncrease[1] ;	
		PA.getMp()[0] = PA.getMp()[1] ;
		BA.getPhyAtk()[0] += attributesIncrease[2] ;
		BA.getMagAtk()[0] += attributesIncrease[3] ;
		BA.getPhyDef()[0] += attributesIncrease[4] ;
		BA.getMagDef()[0] += attributesIncrease[5] ;
		BA.getAgi()[0] += attributesIncrease[6] ;
		BA.getDex()[0] += attributesIncrease[7] ;
		PA.getExp()[1] += attributesIncrease[8] ;		

		ani.SetAniVars(14, new Object[] {150, this, attributesIncrease}) ;
		ani.StartAni(14) ;
	}
	public float[] CalcAttIncrease()
	{
		// Life, Mp, Phyatk, Magatk, Phydef, Magdef, Dex, Agi, Exp
		float[] Increase = new float[AttributeIncrease.length + 1] ;
		for (int i = 0 ; i <= AttributeIncrease.length - 1 ; ++i)
		{
			if (Math.random() <= ChanceIncrease[i])
			{
				Increase[i] = AttributeIncrease[i] ;
			}
		}
		Increase[AttributeIncrease.length] = (float) (10*(3*Math.pow(PA.getLevel() - 1, 2) + 3*(PA.getLevel() - 1) + 1) - 5) ;
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
			bW.write("\nPet skill: \n" + Arrays.toString(getSpells())) ;
			bW.write("\nPet skillPoints: \n" + getSpellPoints()) ;
			bW.write("\nPet life: \n" + Arrays.toString(getLife())) ;
			bW.write("\nPet mp: \n" + Arrays.toString(getMp())) ;
			bW.write("\nPet range: \n" + getRange()) ;
			bW.write("\nPet phyAtk: \n" + Arrays.toString(getPhyAtk())) ;
			bW.write("\nPet magAtk: \n" + Arrays.toString(getMagAtk())) ;
			bW.write("\nPet phyDef: \n" + Arrays.toString(getPhyDef())) ;
			bW.write("\nPet magDef: \n" + Arrays.toString(getMagDef())) ;
			bW.write("\nPet dex: \n" + Arrays.toString(getDex())) ;
			bW.write("\nPet agi: \n" + Arrays.toString(getAgi())) ;
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
			bW.write("\nPet status: \n" + Arrays.toString(getBattleAtt().getSpecialStatus())) ; 
			bW.write("\nPet actions: \n" + Arrays.deepToString(getActions())) ; 
			bW.write("\nPet battle actions: \n" + Arrays.deepToString(getBattleAtt().getBattleActions())) ; 
			bW.write("\nPet status counter: \n" + Arrays.toString(getStatusCounter())) ;
		}
		catch (IOException e)
		{
			System.out.println("Error writing the pet attributes to file") ;
		}
	}
	public void Load(String[][] ReadFile)
	{
		int NumberOfPlayerAttributes = 49 ;
		PA.setName(ReadFile[2*(NumberOfPlayerAttributes + 1)][0]) ;
		PA.setSize((Size) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 2)]), "String", "int")) ;
		color = UtilG.toColor(ReadFile[2*(NumberOfPlayerAttributes + 3)])[0] ;
		Job = Integer.parseInt(ReadFile[2*(NumberOfPlayerAttributes + 4)][0]) ;
		PA.setPos((Point) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 5)]), "String", "int")) ;
		spell = (Spells[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 6)]), "String", "int") ;
		spellPoints = Integer.parseInt(ReadFile[2*(NumberOfPlayerAttributes + 7)][0]) ;
		PA.setLife((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 8)]), "String", "float")) ;
		PA.setMp((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 9)]), "String", "float")) ;
		PA.setRange(Float.parseFloat(ReadFile[2*(NumberOfPlayerAttributes + 10)][0])) ;
		BA.setPhyAtk((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 11)]), "String", "float")) ;
		BA.setMagAtk((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 12)]), "String", "float")) ;
		BA.setPhyDef((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 13)]), "String", "float")) ;
		BA.setMagDef((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 14)]), "String", "float")) ;
		BA.setDex((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 15)]), "String", "float")) ;
		BA.setAgi((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 16)]), "String", "float")) ;
		BA.setCrit((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 17)]), "String", "float")) ;
		BA.setStun((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 18)]), "String", "float")) ;
		BA.setBlock((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 19)]), "String", "float")) ;
		BA.setBlood((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 20)]), "String", "float")) ;
		BA.setPoison((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 21)]), "String", "float")) ;
		BA.setSilence((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 22)]), "String", "float")) ;
		PA.Elem = (String[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 23)]), "String", "String") ;
		ElemMult = (float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 24)]), "String", "float") ;
		PA.setLevel(Integer.parseInt(ReadFile[2*(NumberOfPlayerAttributes + 25)][0])) ;
		PA.setStep(Integer.parseInt(ReadFile[2*(NumberOfPlayerAttributes + 26)][0])) ;
		PA.setSatiation((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 27)]), "String", "float")) ;
		PA.setExp((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 28)]), "String", "float")) ;
		BA.setSpecialStatus((int[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 29)]), "String", "int")) ;
		PA.Actions = (int[][]) UtilG.ConvertDoubleArray(UtilG.deepToString(ReadFile[2*(NumberOfPlayerAttributes + 30)], 3), "String", "int") ;
		BA.setBattleActions((int[][]) UtilG.ConvertDoubleArray(UtilG.deepToString(ReadFile[2*(NumberOfPlayerAttributes + 31)], 3), "String", "int")) ;
		StatusCounter = (int[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*(NumberOfPlayerAttributes + 32)]), "String", "int") ;
	}
	
	
	/* Drawing methods */
	public void display(Point Pos, float[] Scale, DrawPrimitives DP)
	{
		//	TODO add moving animations
		float OverallAngle = DrawPrimitives.OverallAngle ;
		DP.DrawImage(movingAni.idleGif, Pos, OverallAngle, Scale, new boolean[] {false, false}, "Center", 1) ;
	}

	
	@Override
	public String toString() {
		return "Pet [color=" + color + ", Job=" + Job + ", Skill=" + Arrays.toString(spell) + ", SkillPoints="
				+ spellPoints + ", ElemMult=" + Arrays.toString(ElemMult) + ", StatusCounter="
				+ Arrays.toString(StatusCounter) + ", Combo=" + Arrays.toString(Combo) + ", action=" + action + "]";
	}
	
}