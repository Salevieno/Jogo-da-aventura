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
	private int job ;
	private int spellPoints ;
	private double[] ElemMult ;		// [Neutral, Water, Fire, Plant, Earth, Air, Thunder, Light, Dark, Snow]
//	private int[] StatusCounter ;	// [Life, Mp, Phy atk, Phy def, Mag atk, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence]
	
	public static int NumberOfSpells = 5 ;
	public static double[] AttributeIncrease ;
	public static double[] ChanceIncrease ;
	
	public static String[] SpellKeys = new String[] {"0", "1", "2", "3"} ;
	
	private static ArrayList<String[]> PetProperties = UtilG.ReadcsvFile(Game.CSVPath + "PetInitialStats.csv") ;
	private static ArrayList<String[]> PetEvolutionProperties = UtilG.ReadcsvFile(Game.CSVPath + "PetEvolution.csv") ;
	private static PlayerAttributesWindow attWindow = new PlayerAttributesWindow(UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "PetAttWindow1.png")) ;
	
	
	public Pet(int Job)
	{
		super(InitializePersonalAttributes(Job), InitializeBattleAttributes(Job), initializeMovingAnimations(Job), attWindow) ;
		
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
		elem = new Elements[] {Elements.neutral, Elements.neutral, Elements.neutral, Elements.neutral, Elements.neutral} ;
		actionCounter = new TimeCounter(0, Integer.parseInt(PetProperties.get(Job)[33])) ;
		satiationCounter = new TimeCounter(0, Integer.parseInt(PetProperties.get(Job)[34])) ;
		mpCounter = new TimeCounter(0, Integer.parseInt(PetProperties.get(Job)[35])) ;
		battleActionCounter = new TimeCounter(0, Integer.parseInt(PetProperties.get(Job)[36])) ;
		stepCounter = new TimeCounter(0, 20) ;
		currentAction = null ;
		combo = new ArrayList<>();
		
		this.job = Job ;
		Color[] ColorPalette = Game.ColorPalette ;
		Color[] PetColor = new Color[] {ColorPalette[3], ColorPalette[1], ColorPalette[18], ColorPalette[18]} ;
		color = PetColor[Job] ;
		spells = InitializePetSpells();
		spellPoints = 0 ;

		
		ElemMult = new double[10] ;
//		StatusCounter = new int[8] ;
			
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
		BasicAttribute life = new BasicAttribute(Integer.parseInt(PetProperties.get(Job)[2]), Integer.parseInt(PetProperties.get(Job)[2]), 1) ;
		BasicAttribute mp = new BasicAttribute(Integer.parseInt(PetProperties.get(Job)[3]), Integer.parseInt(PetProperties.get(Job)[3]), 1) ;
		BasicAttribute exp = new BasicAttribute(0, 50, 1) ;
		BasicAttribute satiation = new BasicAttribute(100, 100, 1) ;
		BasicAttribute thirst = new BasicAttribute(100, 100, 1) ;
		return new PersonalAttributes(life, mp, exp, satiation, thirst) ;
	}
	
	private static BattleAttributes InitializeBattleAttributes(int Job)
	{
		BasicBattleAttribute phyAtk = new BasicBattleAttribute(Double.parseDouble(PetProperties.get(Job)[5]), 0, 0) ;
		BasicBattleAttribute magAtk = new BasicBattleAttribute(Double.parseDouble(PetProperties.get(Job)[6]), 0, 0) ;
		BasicBattleAttribute phyDef = new BasicBattleAttribute(Double.parseDouble(PetProperties.get(Job)[7]), 0, 0) ;
		BasicBattleAttribute magDef = new BasicBattleAttribute(Double.parseDouble(PetProperties.get(Job)[8]), 0, 0) ;
		BasicBattleAttribute dex = new BasicBattleAttribute(Double.parseDouble(PetProperties.get(Job)[9]), 0, 0) ;
		BasicBattleAttribute agi = new BasicBattleAttribute(Double.parseDouble(PetProperties.get(Job)[10]), 0, 0) ;
		double[] crit = new double[] {Double.parseDouble(PetProperties.get(Job)[11]), 0, Double.parseDouble(PetProperties.get(Job)[12]), 0} ;
		BattleSpecialAttribute stun = new BattleSpecialAttribute(Double.parseDouble(PetProperties.get(Job)[13]), 0, Double.parseDouble(PetProperties.get(Job)[14]), 0, Integer.parseInt(PetProperties.get(Job)[15])) ;
		BattleSpecialAttribute block = new BattleSpecialAttribute(Double.parseDouble(PetProperties.get(Job)[16]), 0, Double.parseDouble(PetProperties.get(Job)[17]), 0, Integer.parseInt(PetProperties.get(Job)[18])) ;
		BattleSpecialAttributeWithDamage blood = new BattleSpecialAttributeWithDamage(Double.parseDouble(PetProperties.get(Job)[19]), 0, Double.parseDouble(PetProperties.get(Job)[20]), 0, Integer.parseInt(PetProperties.get(Job)[21]), 0, Integer.parseInt(PetProperties.get(Job)[22]), 0, Integer.parseInt(PetProperties.get(Job)[23])) ;
		BattleSpecialAttributeWithDamage poison = new BattleSpecialAttributeWithDamage(Double.parseDouble(PetProperties.get(Job)[24]), 0, Double.parseDouble(PetProperties.get(Job)[25]), 0, Integer.parseInt(PetProperties.get(Job)[26]), 0, Integer.parseInt(PetProperties.get(Job)[27]), 0, Integer.parseInt(PetProperties.get(Job)[28])) ;
		BattleSpecialAttribute silence = new BattleSpecialAttribute(Double.parseDouble(PetProperties.get(Job)[29]), 0, Double.parseDouble(PetProperties.get(Job)[30]), 0, Integer.parseInt(PetProperties.get(Job)[31])) ;
		LiveBeingStatus status = new LiveBeingStatus() ;
		return new BattleAttributes(phyAtk, magAtk, phyDef, magDef, dex, agi, crit, stun, block, blood, poison, silence, status) ;
	}
	
	public static MovingAnimations initializeMovingAnimations(int Job)
	{
		String filePath = Game.ImagesPath + "\\Pet\\" + "PetType" ;
		return new MovingAnimations(UtilG.loadImage(filePath + String.valueOf(Job) + ".png"),
				UtilG.loadImage(filePath + String.valueOf(Job) + ".png"),
				UtilG.loadImage(filePath + String.valueOf(Job) + ".png"),
				UtilG.loadImage(filePath + String.valueOf(Job) + ".png"),
				UtilG.loadImage(filePath + String.valueOf(Job) + ".png")) ;
	}

	public ArrayList<Spell> InitializePetSpells()
    {
		SpellType[] allSpellTypes = Game.getAllSpellTypes() ;		
		ArrayList<Spell> petspells = new ArrayList<>() ;
		
		for (int i = 0 ; i <= Pet.NumberOfSpells - 1 ; i += 1)
		{
			int ID = i + job * Pet.NumberOfSpells ;
			
			petspells.add(new Spell(allSpellTypes[ID])) ;	
		}
		
		petspells.get(0).incLevel(1) ;
		return petspells ;
    }
	
	public Color getColor() {return color ;}
	public int getJob() {return job ;}
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

	public boolean isAlive() { return 0 < PA.getLife().getCurrentValue() ;}
	public boolean shouldLevelUP() {return getExp().getMaxValue() <= getExp().getCurrentValue() ;}
	public boolean closeToPlayer(Point playerPos) { return UtilG.dist(pos, playerPos) <= 40 ; }
	
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
	
	public void Move(Point playerPos, GameMap playerMap, Creature opponent, Elements playerElem)
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
		if (playerMap.groundIsWalkable(nextPos, playerElem))
		{
			setPos(nextPos) ;
		}
	}
	public void Dies()
	{
		PA.getLife().incCurrentValue(-PA.getLife().getCurrentValue()) ;
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
		Elements[] AtkElem = new Elements[] {spell.getElem(), elem[1], elem[4]} ;
		Elements[] DefElem = receiver.defElems() ;
		
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
	
	public void levelUp(Animations ani)
	{
		double[] attIncrease = calcAttributesIncrease() ;
		setLevel(level + 1) ;
		spellPoints += 1 ;
		PA.getLife().incMaxValue((int) attIncrease[0]) ;
		PA.getLife().setToMaximum() ;
		PA.getMp().incMaxValue((int) attIncrease[1]) ;	
		PA.getMp().setToMaximum() ;
		BA.getPhyAtk().incBaseValue(attIncrease[2]) ;
		BA.getMagAtk().incBaseValue(attIncrease[3]) ;
		BA.getPhyDef().incBaseValue(attIncrease[4]) ;
		BA.getMagDef().incBaseValue(attIncrease[5]) ;
		BA.getAgi().incBaseValue(attIncrease[6]) ;
		BA.getDex().incBaseValue(attIncrease[7]) ;
		PA.getExp().incMaxValue((int) attIncrease[8]) ;
		
		ani.start(new Object[] {150, Arrays.copyOf(attIncrease, attIncrease.length - 1), level, color}) ;
	}
	public double[] calcAttributesIncrease()
	{
		double[] Increase = new double[AttributeIncrease.length + 1] ;
		for (int i = 0 ; i <= AttributeIncrease.length - 1 ; ++i)
		{
			if (Math.random() <= ChanceIncrease[i])
			{
				Increase[i] = AttributeIncrease[i] ;
			}
		}
		Increase[AttributeIncrease.length] = (double) (10 * (3 * Math.pow(level - 1, 2) + 3 * (level - 1) + 1) - 5) ;
		return Increase ;
	}

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
//			bW.write("\nPet status counter: \n" + Arrays.toString(getStatusCounter())) ;
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
	
	
	public void display(Point pos, Scale scale, DrawingOnPanel DP)
	{
		movingAni.display(dir, pos, DrawingOnPanel.stdAngle, scale, DP) ;
	}
	
}