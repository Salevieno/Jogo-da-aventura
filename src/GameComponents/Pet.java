package GameComponents ;

import java.awt.Color ;
import java.awt.Image ;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.ImageIcon ;

import Main.Uts ;
import Main.Game;
import Main.Utg ;

public class Pet
{
	private Color color ;
	private int Job ;
	private int[] Skill ;
	private int SkillPoints ;
	private PersonalAttributes PA ;
	private BattleAttributes BA ;
	private String[] Elem ;		// [Atk, Weapon, Armor, Shield, SuperElem]
	private float[] ElemMult ;	// [Neutral, Water, Fire, Plant, Earth, Air, Thunder, Light, Dark, Snow]
	private int[][] Actions ;	// [Move, Satiation, Mp][Counter, delay, permission]
	private int[] StatusCounter ;// [Life, Mp, Phy atk, Phy def, Mag atk, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence]
	private String[] Combo ;		// Record of the last movement
	
	public static int NumberOfPetSkills = 5 ;
	public String action = "" ;
	public static float[] AttributeIncrease ;
	public static float[] ChanceIncrease ;
	
	String[][] PetProperties = Utg.ReadTextFile(Game.CSVPath + "PetInitialStats.csv", 4, 37) ;
	String[][] PetEvolutionProperties = Utg.ReadTextFile(Game.CSVPath + "PetEvolution.csv", 5, 17) ;
	
	public Pet(int Job)
	{
		this.Job = Job ;
		Color[] ColorPalette = Uts.ReadColorPalette(new ImageIcon(Game.ImagesPath + "ColorPalette.png").getImage(), "Normal") ;
		Color[] PetColor = new Color[] {ColorPalette[3], ColorPalette[1], ColorPalette[18], ColorPalette[18]} ;
		color = PetColor[Job] ;
		Skill = new int[Pet.NumberOfPetSkills] ;
		SkillPoints = 0 ;

		String Name = PetProperties[Job][0] ;
		Image image = new ImageIcon(Game.ImagesPath + "PetType" + String.valueOf(Job) + ".png").getImage() ;
		int Level = 1 ;
		int Continent = 0 ;
		int Map = 0 ;
		int[] Pos = new int[2] ;
		String dir = Player.MoveKeys[0] ;
		String Thought = "Exist" ;
		int[] Size = new int[] {image.getWidth(null), image.getHeight(null)} ;	
		float[] Life = new float[] {Integer.parseInt(PetProperties[Job][2]), Integer.parseInt(PetProperties[Job][2])} ;
		float[] Mp = new float[] {Integer.parseInt(PetProperties[Job][3]), Integer.parseInt(PetProperties[Job][3])} ;
		int Range = Integer.parseInt(PetProperties[Job][4]) ;
		int Step = Integer.parseInt(PetProperties[Job][32]) ;
		float[] Exp = new float[] {0, 50, 1} ;
		float[] Satiation = new float[] {100, 100, 1} ;
		float[] Thirst = new float[] {100, 100, 0} ;		
		PA = new PersonalAttributes(Name, new Image[] {image}, Level, Continent, Map, Pos, dir, Thought, Size, Life, Mp, Range, Step, Exp, Satiation, Thirst) ;
		
		float[] PhyAtk = new float[] {Float.parseFloat(PetProperties[Job][5]), 0, 0} ;
		float[] MagAtk = new float[] {Float.parseFloat(PetProperties[Job][6]), 0, 0} ;
		float[] PhyDef = new float[] {Float.parseFloat(PetProperties[Job][7]), 0, 0} ;
		float[] MagDef = new float[] {Float.parseFloat(PetProperties[Job][8]), 0, 0} ;
		float[] Dex = new float[] {Float.parseFloat(PetProperties[Job][9]), 0, 0} ;
		float[] Agi = new float[] {Float.parseFloat(PetProperties[Job][10]), 0, 0} ;
		float[] Crit = new float[] {Float.parseFloat(PetProperties[Job][11]), 0, Float.parseFloat(PetProperties[Job][12]), 0} ;
		float[] Stun = new float[] {Float.parseFloat(PetProperties[Job][13]), 0, Float.parseFloat(PetProperties[Job][14]), 0, Float.parseFloat(PetProperties[Job][15])} ;
		float[] Block = new float[] {Float.parseFloat(PetProperties[Job][16]), 0, Float.parseFloat(PetProperties[Job][17]), 0, Float.parseFloat(PetProperties[Job][18])} ;
		float[] Blood = new float[] {Float.parseFloat(PetProperties[Job][19]), 0, Float.parseFloat(PetProperties[Job][20]), 0, Float.parseFloat(PetProperties[Job][21]), 0, Float.parseFloat(PetProperties[Job][22]), 0, Float.parseFloat(PetProperties[Job][23])} ;
		float[] Poison = new float[] {Float.parseFloat(PetProperties[Job][24]), 0, Float.parseFloat(PetProperties[Job][25]), 0, Float.parseFloat(PetProperties[Job][26]), 0, Float.parseFloat(PetProperties[Job][27]), 0, Float.parseFloat(PetProperties[Job][28])} ;
		float[] Silence = new float[] {Float.parseFloat(PetProperties[Job][29]), 0, Float.parseFloat(PetProperties[Job][30]), 0, Float.parseFloat(PetProperties[Job][31])} ;
		int[] Status = new int[8] ;
		int[] SpecialStatus = new int[5] ;
		int[][] BattleActions = new int[][] {{0, Integer.parseInt(PetProperties[Job][36]), 0}} ;
		BA = new BattleAttributes(PhyAtk, MagAtk, PhyDef, MagDef, Dex, Agi, Crit, Stun, Block, Blood, Poison, Silence, Status, SpecialStatus, BattleActions) ;

		Elem = new String[] {"n", "n", "n", "n", "n"} ;
		ElemMult = new float[10] ;
		Actions = new int[][] {{0, Integer.parseInt(PetProperties[Job][33]), 0}, {0, Integer.parseInt(PetProperties[Job][34]), 0}, {0, Integer.parseInt(PetProperties[Job][35]), 0}} ;
		StatusCounter = new int[8] ;
		Combo = new String[1] ;
			
    	AttributeIncrease = new float[8] ;
    	ChanceIncrease = new float[8] ;
		for (int i = 0 ; i <= 7 ; ++i)
		{
			AttributeIncrease[i] = Float.parseFloat(PetEvolutionProperties[Job][i + 1]) ;
			ChanceIncrease[i] = Float.parseFloat(PetEvolutionProperties[Job][i + 9]) ;
		}
	}

	public String getName() {return PA.getName() ;}
	public int[] getSize() {return PA.getSize() ;}
	public Color getColor() {return color ;}
	public int getJob() {return Job ;}
	public int[] getPos() {return PA.getPos() ;}
	public int[] getSkill() {return Skill ;}
	public int getSkillPoints() {return SkillPoints ;}
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
	public String[] getElem() {return Elem ;}
	public float[] getElemMult() {return ElemMult ;}
	public int getLevel() {return PA.getLevel() ;}
	public int getStep() {return PA.getStep() ;}
	public float[] getExp() {return PA.getExp() ;}
	public float[] getSatiation() {return PA.getSatiation() ;}
	public int[][] getActions() {return Actions ;}
	public int[] getStatusCounter() {return StatusCounter ;}
	public String[] getCombo() {return Combo ;}
	public String getAction() {return action ;}
	public void setCombo(String[] C) {Combo = C ;}

	public int[] CenterPos()
	{
		return new int[] {(int) (PA.getPos()[0] + 0.5 * PA.getSize()[0]), (int) (PA.getPos()[1] - 0.5 * PA.getSize()[1])} ;
	}
	public boolean isAlive()
	{
		return 0 < PA.getLife()[0] ;
	}
	public String Action(String[] ActionKeys)
	{
		int move = -1 ;
		if (10 <= PA.getMp()[0])
		{
			move = (int)(3*Math.random() - 0.01) ;
		}
		else
		{
			move = (int)(2*Math.random() - 0.01) ;
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
	public void Follow(int[] Pos, int[] Target, int step, float mindist)
	{
		int[] pos = new int[] {Pos[0], Pos[1]} ; // Prevent the method from modifying the original variable Pos
		float verdist = Math.abs(pos[1] - Target[1]), hordist = Math.abs(pos[0] - Target[0]) ;
		if (mindist < Utg.dist2D(pos, Target))
		{
			if (verdist < hordist)
			{
				if (pos[0] < Target[0])
				{
					pos[0] += step ;
				}
				else
				{
					pos[0] += -step ;
				}
			}
			else
			{
				if (pos[1] < Target[1])
				{
					pos[1] += step ;
				}
				else
				{
					pos[1] += -step ;
				}
			}
		}
		PA.setPos(pos) ;
	}
	public void Move(Player player,  Maps[] maps)
	{
		int[] NextPos = new int[2] ;
		Follow(PA.getPos(), player.getPos(), PA.getStep(), PA.getStep()) ;
		if (maps[player.getMap()].GroundIsWalkable(NextPos, player.getElem()[4]))
		{
			PA.setPos(NextPos) ;
		}
	}
	public void ActivateDef()
	{
		BA.getPhyDef()[1] += BA.getPhyDef()[0] ;
		BA.getMagDef()[1] += BA.getMagDef()[0] ;
	}
	public void DeactivateDef()
	{
		BA.getPhyDef()[1] += -BA.getPhyDef()[0] ;
		BA.getMagDef()[1] += -BA.getMagDef()[0] ;
	}
	public boolean isSilent()
	{
		if (BA.getSpecialStatus()[4] <= 0)
		{
			return false ;
		}
		return true ;
	}
	public boolean canAtk()
	{
		if (BA.getBattleActions()[0][2] == 1 & !BA.isStun())
		{
			return true ;
		}
		else
		{
			return false ;
		}
	}
	public boolean isDefending()
	{
		if (BA.getCurrentAction().equals("D") & !canAtk())
		{
			return true ;
		}
		else
		{
			return false ;
		}
	}
	public void IncActionCounters()
	{
		for (int a = 0 ; a <= Actions.length - 1 ; a += 1)
		{
			if (Actions[a][0] < Actions[a][1])
			{
				Actions[a][0] += 1 ;
			}	
		}
	}
	public void ActivateActionCounters(boolean SomeAnimationIsOn)
	{
		if (Actions[1][0] % Actions[1][1] == 0)
		{
			PA.getSatiation()[0] = Math.max(PA.getSatiation()[0] - 1, 0) ;
			if (PA.getSatiation()[0] == 0)	// pet is hungry
			{
				PA.getLife()[0] = Math.max(PA.getLife()[0] - 1, 0) ;
			}
			Actions[1][0] = 0 ;
		}
		if (Actions[2][0] % Actions[2][1] == 0)	// Pet heals mp
		{
			PA.getMp()[0] = (float)(Math.min(PA.getMp()[0] + 0.02*PA.getMp()[1], PA.getMp()[1])) ;	
			Actions[2][0] = 0 ;
		}
		if (Actions[0][0] % Actions[0][1] == 0 & !SomeAnimationIsOn)
		{
			Actions[0][2] = 1 ;	// pet can move
		}
	}
	public void IncBattleActionCounters()
	{
		for (int a = 0 ; a <= BA.getBattleActions().length - 1 ; a += 1)
		{
			if (BA.getBattleActions()[a][0] < BA.getBattleActions()[a][1])
			{
				BA.getBattleActions()[a][0] += 1 ;
			}	
		}
	}
	public void ActivateBattleActionCounters()
	{
		if (BA.getBattleActions()[0][0] == BA.getBattleActions()[0][1])
		{
			BA.getBattleActions()[0][2] = 1 ;	// Pet can atk
		}
	}
	public void ResetBattleActions()
	{
		BA.getBattleActions()[0][0] = 0 ;
		BA.getBattleActions()[0][2] = 0 ;
	}
	public boolean usedSkill()
	{
		return Utg.isNumeric(BA.getCurrentAction()) ;
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
	public void LevelUp(float[] AttributesIncrease)
	{
		PA.setLevel(PA.getLevel() + 1) ;
		SkillPoints += 1 ;
		PA.getLife()[1] += AttributesIncrease[0] ;
		PA.getLife()[0] = PA.getLife()[1] ;
		PA.getMp()[1] += AttributesIncrease[1] ;	
		PA.getMp()[0] = PA.getMp()[1] ;
		BA.getPhyAtk()[0] += AttributesIncrease[2] ;
		BA.getMagAtk()[0] += AttributesIncrease[3] ;
		BA.getPhyDef()[0] += AttributesIncrease[4] ;
		BA.getMagDef()[0] += AttributesIncrease[5] ;
		BA.getAgi()[0] += AttributesIncrease[6] ;
		BA.getDex()[0] += AttributesIncrease[7] ;
		PA.getExp()[1] += AttributesIncrease[8] ;
	}

	/* Save and load methods */
	public void Save(BufferedWriter bufferedWriter)
	{
		try
		{
			bufferedWriter.write("\nPet name: \n" + getName()) ;
			bufferedWriter.write("\nPet size: \n" + Arrays.toString(getSize())) ;
			bufferedWriter.write("\nPet color: \n" + getColor()) ;
			bufferedWriter.write("\nPet job: \n" + getJob()) ;
			bufferedWriter.write("\nPet coords: \n" + Arrays.toString(getPos())) ;
			bufferedWriter.write("\nPet skill: \n" + Arrays.toString(getSkill())) ;
			bufferedWriter.write("\nPet skillPoints: \n" + getSkillPoints()) ;
			bufferedWriter.write("\nPet life: \n" + Arrays.toString(getLife())) ;
			bufferedWriter.write("\nPet mp: \n" + Arrays.toString(getMp())) ;
			bufferedWriter.write("\nPet range: \n" + getRange()) ;
			bufferedWriter.write("\nPet phyAtk: \n" + Arrays.toString(getPhyAtk())) ;
			bufferedWriter.write("\nPet magAtk: \n" + Arrays.toString(getMagAtk())) ;
			bufferedWriter.write("\nPet phyDef: \n" + Arrays.toString(getPhyDef())) ;
			bufferedWriter.write("\nPet magDef: \n" + Arrays.toString(getMagDef())) ;
			bufferedWriter.write("\nPet dex: \n" + Arrays.toString(getDex())) ;
			bufferedWriter.write("\nPet agi: \n" + Arrays.toString(getAgi())) ;
			bufferedWriter.write("\nPet crit: \n" + Arrays.toString(getCrit())) ;
			bufferedWriter.write("\nPet stun: \n" + Arrays.toString(getStun())) ;
			bufferedWriter.write("\nPet block: \n" + Arrays.toString(getBlock())) ;
			bufferedWriter.write("\nPet blood: \n" + Arrays.toString(getBlood())) ;
			bufferedWriter.write("\nPet poison: \n" + Arrays.toString(getPoison())) ;
			bufferedWriter.write("\nPet silence: \n" + Arrays.toString(getSilence())) ;
			bufferedWriter.write("\nPet elem: \n" + Arrays.toString(getElem())) ;
			bufferedWriter.write("\nPet elem mult: \n" + Arrays.toString(getElemMult())) ;
			bufferedWriter.write("\nPet level: \n" + getLevel()) ;
			bufferedWriter.write("\nPet step: \n" + getStep()) ;
			bufferedWriter.write("\nPet satiation: \n" + Arrays.toString(getSatiation())) ;
			bufferedWriter.write("\nPet exp: \n" + Arrays.toString(getExp())) ;
			bufferedWriter.write("\nPet status: \n" + Arrays.toString(getBattleAtt().getSpecialStatus())) ; 
			bufferedWriter.write("\nPet actions: \n" + Arrays.deepToString(getActions())) ; 
			bufferedWriter.write("\nPet battle actions: \n" + Arrays.deepToString(getBattleAtt().getBattleActions())) ; 
			bufferedWriter.write("\nPet status counter: \n" + Arrays.toString(getStatusCounter())) ;
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
		PA.setSize((int[]) Utg.ConvertArray(Utg.toString(ReadFile[2*(NumberOfPlayerAttributes + 2)]), "String", "int")) ;
		color = Utg.toColor(ReadFile[2*(NumberOfPlayerAttributes + 3)])[0] ;
		Job = Integer.parseInt(ReadFile[2*(NumberOfPlayerAttributes + 4)][0]) ;
		PA.setPos((int[]) Utg.ConvertArray(Utg.toString(ReadFile[2*(NumberOfPlayerAttributes + 5)]), "String", "int")) ;
		Skill = (int[]) Utg.ConvertArray(Utg.toString(ReadFile[2*(NumberOfPlayerAttributes + 6)]), "String", "int") ;
		SkillPoints = Integer.parseInt(ReadFile[2*(NumberOfPlayerAttributes + 7)][0]) ;
		PA.setLife((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*(NumberOfPlayerAttributes + 8)]), "String", "float")) ;
		PA.setMp((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*(NumberOfPlayerAttributes + 9)]), "String", "float")) ;
		PA.setRange(Float.parseFloat(ReadFile[2*(NumberOfPlayerAttributes + 10)][0])) ;
		BA.setPhyAtk((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*(NumberOfPlayerAttributes + 11)]), "String", "float")) ;
		BA.setMagAtk((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*(NumberOfPlayerAttributes + 12)]), "String", "float")) ;
		BA.setPhyDef((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*(NumberOfPlayerAttributes + 13)]), "String", "float")) ;
		BA.setMagDef((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*(NumberOfPlayerAttributes + 14)]), "String", "float")) ;
		BA.setDex((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*(NumberOfPlayerAttributes + 15)]), "String", "float")) ;
		BA.setAgi((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*(NumberOfPlayerAttributes + 16)]), "String", "float")) ;
		BA.setCrit((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*(NumberOfPlayerAttributes + 17)]), "String", "float")) ;
		BA.setStun((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*(NumberOfPlayerAttributes + 18)]), "String", "float")) ;
		BA.setBlock((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*(NumberOfPlayerAttributes + 19)]), "String", "float")) ;
		BA.setBlood((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*(NumberOfPlayerAttributes + 20)]), "String", "float")) ;
		BA.setPoison((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*(NumberOfPlayerAttributes + 21)]), "String", "float")) ;
		BA.setSilence((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*(NumberOfPlayerAttributes + 22)]), "String", "float")) ;
		Elem = (String[]) Utg.ConvertArray(Utg.toString(ReadFile[2*(NumberOfPlayerAttributes + 23)]), "String", "String") ;
		ElemMult = (float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*(NumberOfPlayerAttributes + 24)]), "String", "float") ;
		PA.setLevel(Integer.parseInt(ReadFile[2*(NumberOfPlayerAttributes + 25)][0])) ;
		PA.setStep(Integer.parseInt(ReadFile[2*(NumberOfPlayerAttributes + 26)][0])) ;
		PA.setSatiation((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*(NumberOfPlayerAttributes + 27)]), "String", "float")) ;
		PA.setExp((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*(NumberOfPlayerAttributes + 28)]), "String", "float")) ;
		BA.setSpecialStatus((int[]) Utg.ConvertArray(Utg.toString(ReadFile[2*(NumberOfPlayerAttributes + 29)]), "String", "int")) ;
		Actions = (int[][]) Utg.ConvertDoubleArray(Utg.deepToString(ReadFile[2*(NumberOfPlayerAttributes + 30)], 3), "String", "int") ;
		BA.setBattleActions((int[][]) Utg.ConvertDoubleArray(Utg.deepToString(ReadFile[2*(NumberOfPlayerAttributes + 31)], 3), "String", "int")) ;
		StatusCounter = (int[]) Utg.ConvertArray(Utg.toString(ReadFile[2*(NumberOfPlayerAttributes + 32)]), "String", "int") ;
	}
	
	/* Printing methods */
	public void PrintAllAttributes()
	{
		System.out.println();
		System.out.println("** Player attributes **");
		PA.printAtt();
		BA.printAtt();
		System.out.println("Elem: " + Arrays.toString(Elem));
	}
}