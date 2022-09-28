package GameComponents ;

import java.awt.Color ;
import java.awt.Font ;
import java.awt.Image ;
import java.awt.event.KeyEvent ;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList ;
import java.util.Arrays ;

import javax.sound.sampled.Clip ;
import javax.swing.ImageIcon ;

import Actions.Battle ;
import Graphics.Animations ;
import Graphics.DrawFunctions ;
import Graphics.DrawPrimitives ;
import Main.Uts ;
import Main.Game ;
import Main.Utg ;

public class Player 
{
	private String Language ;
	private String Sex ;
	private Color[] color ;	// 0: Hair, 1: Eyes, 2: Neck, 3: Sleeves, 4: Shirt, 5: Legs, 6: Shoes, 7: Skin]
	private int Job ;
	private int ProJob ;
	private int[] Spell ;
	private int[] Quest ;
	private int[] Bag ;
	private int[] Equips ;		// 0: Weapon, 1: Shield, 2: Armor, 3: Arrow]
	private int SkillPoints ;
	private PersonalAttributes PA ;	// Personal attributes
	private BattleAttributes BA ;	// Battle attributes
	private String[] Elem ;		// 0: Atk, 1: Weapon, 2: Armor, 3: Shield, 4: SuperElem
	private float[] ElemMult ;	// 0: Neutral, 1: Water, 2: Fire, 3: Plant, 4: Earth, 5: Air, 6: Thunder, 7: Light, 8: Dark, 9: Snow
	private float[] Collect ;	// 0: Herb, 1: wood, 2: metal
	private float[] Gold ;		// 0: Current, 1: stored, 2: multiplier
	private boolean[] QuestSkills ;	// 0: Forest map, 1: Cave map, 2: Island map, 3: Volcano map, 4: Snowland map, 5: Shovel, 6: Book, 7: Ride, 8: Dragon's aura, 9: Bestiary
	private int[][] Actions ;	// [Move, Satiation, Mp][Counter, delay, permission]
	private String CurrentAction ;
	private boolean IsRiding ;
	private boolean[] SpellIsActive ;	// Tells if the skill is currently active
	private int[][] SpellCounter ;	// [0: skill 0, 1: skill 1...] [0: duration, 1: cooldown]
	private int[] StatusCounter ;// [Life, Mp, Phy atk, Phy def, Mag atk, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence, Drunk]
	private float[] Stats ;		// 0: Number of phy attacks, 1: number of skills used, 2: number of defenses, 3: total phy damage inflicted, 4: total phy damage received, 5: total mag damage inflicted, 6: total mag damage received, 7: total phy damage defended, 8: total mag damage defended, 9: total hits inflicted, 10: total hits received, 11: total dodges, 12: number of crit, 13: total crit damage, 14: total stun, 15: total block, 16: total blood, 17: total blood damage, 18: total blood def, 19: total poison, 20: total poison damage, 21: total poison def, 22: total silence
	private String[] Combo ;		// Record of the last 10 movements
	private int AttPoints ;		// Attribute points available
	private float[][] AttIncrease ;	// Amount of increase in each attribute when the player levels up
	private float[][] ChanceIncrease ;	// Chance of increase of these attributes
	private int[] CreaturesDiscovered ;	// Creatures that the player has encountered. Will appear in the bestiary
	
	private int[] SelectedWindow ;
	private int[] SelectedMenu = new int[2] ;
	private int[] SelectedItem = new int[2] ;
	private int ItemsMenu ;
	private String CustomKey ;
    public Object[] OptionStatus ;
    public int ClosestCreature ;
    public int CreatureInBattle ;
    public int DifficultLevel ;
	public Image RidingImage ;
	public String action = "" ;
	public int countmove = 0 ;
	public int SelectedOption = 0 ;
	public float[][] EquipsBonus ;
	public boolean[] WindowIsOpen = new boolean[10] ;	// 0: Bag, 1: Bestiary, 2: PlayerWindow, 3: PetWindow, 4: FabBook, 5: Map, 6: Quest, 7: Options, 8: Hints ;
	public Items[] hotkeyItem = new Items[3] ;
	
	public static String[] MoveKeys = new String[] {KeyEvent.getKeyText(KeyEvent.VK_UP), KeyEvent.getKeyText(KeyEvent.VK_LEFT), KeyEvent.getKeyText(KeyEvent.VK_DOWN), KeyEvent.getKeyText(KeyEvent.VK_RIGHT)} ;
	public static String[] ActionKeys = new String[] {"W", "A", "S", "D", "B", "C", "F", "M", "P", "Q", "H", "R", "T", "Z"} ;	// [Up, Left, Down, Right, Bag, Char window, Pet window, Map, Quest, Hint, Tent, Bestiary]
	public static String[] HotKeys = new String[] {"E", "X", "V"} ;	// [Hotkey 1, Hotkey 2, Hotkey 3]
	public static String[] SkillKeys = new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "F10", "F11", "F12"} ;
	public static String[][] Properties = Utg.ReadTextFile(Game.CSVPath + "PlayerInitialStats.csv", 5, 40) ;
	public static String[][] EvolutionProperties = Utg.ReadTextFile(Game.CSVPath + "PlayerEvolution.csv", 16, 18) ;	
	public static int[] NumberOfSkillsPerJob = new int[] {14 + 20, 15 + 20, 15 + 20, 14 + 20, 14 + 20} ;
	public static int[] CumNumberOfSkillsPerJob = new int[] {0, 34, 69, 104, 138} ;
	public static int NumberOfEquipTypes = 4 ;
    public static float[] DifficultMult = new float[] {(float) 0.5, (float) 0.7, (float) 1.0} ;
    public static Image[] AttWindow = new Image[] {new ImageIcon(Game.ImagesPath + "PlayerAttWindow1.png").getImage(), new ImageIcon(Game.ImagesPath + "PlayerAttWindow2.png").getImage(), new ImageIcon(Game.ImagesPath + "PlayerAttWindow3.png").getImage()} ;
    public static Color[] ClassColors = new Color[] {Game.ColorPalette[0], Game.ColorPalette[1], Game.ColorPalette[2], Game.ColorPalette[3], Game.ColorPalette[4]} ;

    public static Image BookImage = new ImageIcon(Game.ImagesPath + "Book.png").getImage() ;
    public static Image TentImage = new ImageIcon(Game.ImagesPath + "Tent.png").getImage() ;
    public static Image QuestImage = new ImageIcon(Game.ImagesPath + "Quest.png").getImage() ;
    public static Image DragonAuraImage = new ImageIcon(Game.ImagesPath + "DragonAura.png").getImage() ;
    public static Image BagMenuImage = new ImageIcon(Game.ImagesPath + "BagMenu.png").getImage() ;
    public static Image BagSlotImage = new ImageIcon(Game.ImagesPath + "BagSlot.png").getImage() ;
	private static Image PterodactileImage = new ImageIcon(Game.ImagesPath + "Pterodactile.png").getImage() ;
	private static Image SpeakingBubbleImage = new ImageIcon(Game.ImagesPath + "SpeakingBubble.png").getImage() ;
    
	public static Image MagicBlissGif = new ImageIcon(Game.ImagesPath + "MagicBliss.gif").getImage() ;
    public static Image FishingGif = new ImageIcon(Game.ImagesPath + "Fishing.gif").getImage() ;
	
	public Player(String Name, String Language, String Sex, int Job)
	{
		this.Language = Language ;
		this.Sex = Sex ;
		this.Job = Job ;
		ProJob = 0 ;
		Spell = new int[NumberOfSkillsPerJob[Job]] ;
		Quest = new int[Quests.NumberOfQuests] ;
		Bag = new int[Items.NumberOfAllItems] ;
		Equips = new int[NumberOfEquipTypes] ;
		SkillPoints = 0 ;
		Color[] ColorPalette = Uts.ReadColorPalette(new ImageIcon(Game.ImagesPath + "ColorPalette.png").getImage(), "Normal") ;
		color = new Color[] {ColorPalette[12], ColorPalette[13], ColorPalette[14], ColorPalette[14], ColorPalette[15], ColorPalette[7], ColorPalette[16]} ;

		Image PlayerBack = new ImageIcon(Game.ImagesPath + "PlayerBack.png").getImage() ;
		Image PlayerFront = new ImageIcon(Game.ImagesPath + "PlayerFront.png").getImage() ;
		Image PlayerLeft = new ImageIcon(Game.ImagesPath + "PlayerLeft.png").getImage() ;
		Image PlayerRight = new ImageIcon(Game.ImagesPath + "PlayerRight.png").getImage() ;
		Image PlayerBack2 = new ImageIcon(Game.ImagesPath + "PlayerBack2.png").getImage() ;
		Image PlayerFront2 = new ImageIcon(Game.ImagesPath + "PlayerFront2.png").getImage() ;
		Image PlayerLeft2 = new ImageIcon(Game.ImagesPath + "PlayerLeft2.png").getImage() ;
		Image PlayerRight2 = new ImageIcon(Game.ImagesPath + "PlayerRight2.png").getImage() ;
		Image[] Images = new Image[] {PlayerBack, PlayerFront, PlayerLeft, PlayerRight, PlayerBack2, PlayerFront2, PlayerLeft2, PlayerRight2} ;
    	int Level = 1 ;
		int Continent = 0 ;
		int Map = Job ;
		int[] Pos = new int[] {0, 0} ;
		String dir = Player.MoveKeys[0] ;
		String Thought = "Exist" ;
		int[] Size = new int[] {Images[0].getWidth(null), Images[0].getHeight(null)} ;
		float[] Life = new float[] {Float.parseFloat(Properties[Job][2]), Float.parseFloat(Properties[Job][2])} ;
		float[] Mp = new float[] {Float.parseFloat(Properties[Job][3]), Float.parseFloat(Properties[Job][3])} ;
		float Range = Float.parseFloat(Properties[Job][4]) ;
		int Step = Integer.parseInt(Properties[Job][33]) ;
		float[] Exp = new float[] {0, 5, Float.parseFloat(Properties[Job][34])} ;
		float[] Satiation = new float[] {100, 100, Float.parseFloat(Properties[Job][35])} ;
		float[] Thirst = new float[] {100, 100, Float.parseFloat(Properties[Job][36])} ;
		PA = new PersonalAttributes(Name, Images, Level, Continent, Map, Pos, dir, Thought, Size, Life, Mp, Range, Step, Exp, Satiation, Thirst) ;
    	
		float[] PhyAtk = new float[] {Float.parseFloat(Properties[Job][5]), 0, 0} ;
		float[] MagAtk = new float[] {Float.parseFloat(Properties[Job][6]), 0, 0} ;
		float[] PhyDef = new float[] {Float.parseFloat(Properties[Job][7]), 0, 0} ;
		float[] MagDef = new float[] {Float.parseFloat(Properties[Job][8]), 0, 0} ;
		float[] Dex = new float[] {Float.parseFloat(Properties[Job][9]), 0, 0} ;	
		float[] Agi = new float[] {Float.parseFloat(Properties[Job][10]), 0, 0} ;
		float[] Crit = new float[] {Float.parseFloat(Properties[Job][11]), 0, Float.parseFloat(Properties[Job][12]), 0} ;
		float[] Stun = new float[] {Float.parseFloat(Properties[Job][13]), 0, Float.parseFloat(Properties[Job][14]), 0, Float.parseFloat(Properties[Job][15])} ;
		float[] Block = new float[] {Float.parseFloat(Properties[Job][16]), 0, Float.parseFloat(Properties[Job][17]), 0, Float.parseFloat(Properties[Job][18])} ;
		float[] Blood = new float[] {Float.parseFloat(Properties[Job][19]), 0, Float.parseFloat(Properties[Job][20]), 0, Float.parseFloat(Properties[Job][21]), 0, Float.parseFloat(Properties[Job][22]), 0, Float.parseFloat(Properties[Job][23])} ;
		float[] Poison = new float[] {Float.parseFloat(Properties[Job][24]), 0, Float.parseFloat(Properties[Job][25]), 0, Float.parseFloat(Properties[Job][26]), 0, Float.parseFloat(Properties[Job][27]), 0, Float.parseFloat(Properties[Job][28])} ;
		float[] Silence = new float[] {Float.parseFloat(Properties[Job][29]), 0, Float.parseFloat(Properties[Job][30]), 0, Float.parseFloat(Properties[Job][31])} ;
		int[] Status = new int[9] ;
		int[] SpecialStatus = new int[5] ;
		int[][] BattleActions = new int[][] {{0, Integer.parseInt(Properties[Job][41]), 0}} ;
		BA = new BattleAttributes(PhyAtk, MagAtk, PhyDef, MagDef, Dex, Agi, Crit, Stun, Block, Blood, Poison, Silence, Status, SpecialStatus, BattleActions) ;

		Elem = new String[] {"n", "n", "n", "n", "n"} ;
		ElemMult = new float[10] ;
		Collect = new float[3] ;
		Gold = new float[] {0, 0, Float.parseFloat(Properties[Job][32])} ;
		QuestSkills = new boolean[9] ;
		Actions = new int[][] {{0, Integer.parseInt(Properties[Job][37]), 0}, {0, Integer.parseInt(Properties[Job][38]), 0}, {0, Integer.parseInt(Properties[Job][39]), 0}, {0, Integer.parseInt(Properties[Job][40]), 0}} ;
		CurrentAction = "existing" ;
		IsRiding = false ;
		if (Spell != null)
		{
			SpellIsActive = new boolean[Spell.length] ;
			SpellCounter = new int[Spell.length][2] ;	// 0: duration, 1: cooldown
		}
		StatusCounter = new int[8] ;
		Stats = new float[23] ;
		Combo = null ;
		AttPoints = 0 ;
		AttIncrease = new float[3][8] ;
		ChanceIncrease = new float[3][8] ;
		for (int i = 0 ; i <= 3 - 1 ; ++i)	// Job, ProJob 1, ProJob 2
		{
			for (int j = 0 ; j <= 7 ; ++j)
			{
				AttIncrease[i][j] = Float.parseFloat(EvolutionProperties[3*Job + i][j + 2]) ;
				ChanceIncrease[i][j] = Float.parseFloat(EvolutionProperties[3*Job + i][j + 10]) ;
			}
		}
		CreaturesDiscovered = null ;		
		SelectedWindow = new int[9] ;// [Player, bag, fab, quest, options, hints, attributes, bestiary]
		RidingImage = new ImageIcon(Game.ImagesPath + "Tiger.png").getImage() ;
		ClosestCreature = -1 ;
	    CreatureInBattle = -1 ;
	    DifficultLevel = 1 ;
		EquipsBonus = Items.EquipsBonus ;
		OptionStatus = new Object[] {true, true, false, 1, 1} ;	// Music, sound effects, player range
		WindowIsOpen[9] = true ;
	}

	public String getLanguage() {return Language ;}
	public String getName() {return PA.getName() ;}
	public String getSex() {return Sex ;}
	public String getDir() {return PA.getDir() ;}
	public int[] getSize() {return PA.getSize() ;}
	public Color[] getColors() {return color ;}
	public int getJob() {return Job ;}
	public int getProJob() {return ProJob ;}
	public int getContinent() {return PA.getContinent() ;}
	public int getMap() {return PA.getMap() ;}
	public int[] getPos() {return PA.getPos() ;}
	public int[] getSpell() {return Spell ;}
	public int[] getQuest() {return Quest ;}
	public int[] getBag() {return Bag ;}
	public int[] getEquips() {return Equips ;}
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
	public float[] getCollect() {return Collect ;}
	public int getLevel() {return PA.getLevel() ;}
	public float[] getGold() {return Gold ;}
	public int getStep() {return PA.getStep() ;}
	public float[] getExp() {return PA.getExp() ;}
	public float[] getSatiation() {return PA.getSatiation() ;}
	public float[] getThirst() {return PA.getThirst() ;}
	public boolean[] getQuestSkills() {return QuestSkills ;}
	public int[][] getActions() {return Actions ;}
	public String getCurrentAction() {return CurrentAction ;}
	public boolean[] getSkillIsActive() {return SpellIsActive ;}
	public int[][] getSkillCounter() {return SpellCounter ;}
	public int[] getStatusCounter() {return StatusCounter ;}
	public float[] getStats() {return Stats ;}
	public String[] getCombo() {return Combo ;}
	public int getAttPoints() {return AttPoints ;}
	public float[][] getAttIncrease() {return AttIncrease ;}
	public float[][] getChanceIncrease() {return ChanceIncrease ;}
	public int[] getCreaturesDiscovered() {return CreaturesDiscovered ;}
	public String getAction() {return action ;}
	public int getSelectedOption() {return SelectedOption ;}
	public void setSize(int[] S) {PA.setSize(S) ;}
	public void setProJob(int PJ) {ProJob = PJ ;}
	public void setMap(int M, Maps[] maps) {PA.setMap(M) ; PA.setContinent(maps[PA.getMap()].getContinent()) ;}
	public void setPos(int[] P) {PA.setPos(P) ;}
	public void setBag(int[] B) {Bag = B ;}
	public void setStep(int S) {PA.setStep(S) ;}
	public void setCurrentAction(String CA) {CurrentAction = CA ;}
	public void setCombo(String[] C) {Combo = C ;}
	
	public void addSkillPoint(int amount)
	{
		SkillPoints += amount;
	}
	public void incRange(float incR) {PA.setRange(PA.getRange() + incR) ;}
	
	public boolean ActionIsAMove(String move)
	{
		if (Utg.ArrayContains(Player.MoveKeys, move))
		{
			return true ;
		}
		return false ;
	}
	public boolean hasEnoughMP(Skills[] spell, int spellID)
	{
		return (spell[spellID].getMpCost() <= PA.getMp()[0]);
	}
	public void updateDir(String move)
	{
		if (move.equals(Player.MoveKeys[0]))	// North
		{
			PA.setdir(Player.MoveKeys[0]) ;
		}
		if (move.equals(Player.MoveKeys[1]))	// West
		{
			PA.setdir(Player.MoveKeys[1]) ;
		}
		if (move.equals(Player.MoveKeys[2]))	// South
		{
			PA.setdir(Player.MoveKeys[2]) ;
		}
		if (move.equals(Player.MoveKeys[3]))	// East
		{
			PA.setdir(Player.MoveKeys[3]) ;
		}
	}
	public void Move(Pet pet, Maps[] maps, Screen screen, Clip[] Music, boolean MusicIsOn, Animations Ani)
	{
		int[] NewPos = PA.CalcNewPos(getDir(), PA.getPos(), PA.getStep()) ;
		if (screen.posIsInMap(NewPos))	// if the player's new position is inside the map
		{	
			if (!Ani.isActive(10) & !Ani.isActive(12) & !Ani.isActive(13) & !Ani.isActive(15) & !Ani.isActive(18))
			{
				boolean NewPosIsWalkable = maps[PA.getMap()].GroundIsWalkable(NewPos, Elem[4]) ;
				if (NewPosIsWalkable)
				{
					setPos(NewPos) ;
				}	
			}			
		}
		else
		{
			//boolean NewPosIsWalkable = Uts.GroundIsWalkable(maps[PA.getMap()].getType()[NewPos[0]][NewPos[1]], Elem[4]) ; -> this has to be on the new map
			if (true)
			{
				MoveToNewMap(pet, screen, maps, action, Music, MusicIsOn, Ani) ;
			}
		}
		
		countmove += 1 ;
		if (getStep() < countmove)
		{
			countmove = 0 ;
		}
	}
	public void act(Pet pet, Maps[] maps, Skills[] spell, Screen screen, int[] MousePos, Icon Selectedbutton, Icon[] sideBarIcons, Animations Ani)
	{
		if (ActionIsAMove(action))
		{
			updateDir(action) ;
		}
		if (action.equals("MouseLeftClick"))
		{
			for (int i = 0 ; i <= sideBarIcons.length - 1 ; i += 1)	// + 2 to account for player and pet
			{
				if (sideBarIcons[i].ishovered(MousePos))
				{
					if (i == 0)
					{
						WindowIsOpen[7] = !WindowIsOpen[7] ;	// Options window
					}	
					if (i == 1)
					{
						WindowIsOpen[0] = !WindowIsOpen[0] ;	// Bag
					}
					if (i == 2)
					{
						WindowIsOpen[6] = !WindowIsOpen[6] ;	// Quest window
					}
					if (i == 3 & QuestSkills[getContinent()])
					{
						WindowIsOpen[5] = !WindowIsOpen[5] ;	// Map
					}
					if (i == 4 & QuestSkills[6])
					{
						WindowIsOpen[4] = !WindowIsOpen[4] ;	// Fab window
					}
					if (i == 5)
					{
						WindowIsOpen[2] = !WindowIsOpen[2] ;	// Player window
					}
					if (i == 6 & 0 < pet.getLife()[0])
					{
						WindowIsOpen[3] = !WindowIsOpen[3] ;	// Pet window
					}
				}
			}
		}
		if (action.equals(ActionKeys[4]))	// Bag
		{
			WindowIsOpen[0] = !WindowIsOpen[0] ;
		}
		if (action.equals(ActionKeys[5]))	// Player window
		{
			WindowIsOpen[2] = !WindowIsOpen[2] ;
		}
		/*if (action.equals(ActionKeys[6]))	// Fishing
		{
			String WaterPos = Uts.CheckAdjacentGround(getPos(), screen, maps[getMap()], "Water");
			if (0 < getBag()[1340] & (WaterPos.contains("Touching") | WaterPos.equals("Inside")))
			{
				Ani.SetAniVars(15, new Object[] {200, getPos(), FishingGif, WaterPos}) ;
				Ani.StartAni(15) ;
			}
		}*/
		if (action.equals(ActionKeys[7]) & QuestSkills[getContinent()])	// Map
		{
			WindowIsOpen[5] = !WindowIsOpen[5] ;
		}
		if (action.equals(ActionKeys[8]) & 0 < pet.getLife()[0])	// Pet window
		{
			WindowIsOpen[3] = !WindowIsOpen[3] ;
		}
		if (action.equals(ActionKeys[9]))							// Quest window
		{
			WindowIsOpen[6] = !WindowIsOpen[6] ;
		}
		if (action.equals(ActionKeys[10]))							// Hints window
		{
			WindowIsOpen[8] = !WindowIsOpen[8] ;
		}
		if (action.equals(ActionKeys[11]) & QuestSkills[7])			// Ride
		{
			ActivateRide() ;
		}
		if (action.equals(ActionKeys[12]) & !isInBattle())			// Tent
		{
			Ani.SetAniVars(11, new Object[] {100, getPos(), TentImage}) ;
			Ani.StartAni(11) ;
		}
		if (action.equals(ActionKeys[13]) & QuestSkills[8])	// Bestiary
		{
			WindowIsOpen[1] = !WindowIsOpen[1] ;
		}
		
		if (Uts.ActionIsSkill(SkillKeys, action) & (!isInBattle() | canAtk()) & Utg.IndexOf(SkillKeys, action) < activeSpells(spell).length)
		{
			SupSpell(pet, activeSpells(spell)[Utg.IndexOf(SkillKeys, action)], spell) ;
		}
		
		// if meets creature, enters battle
		if (-1 < ClosestCreature & (action.equals(ActionKeys[1]) | Uts.ActionIsSkill(SkillKeys, action)) & !isInBattle())
		{
			if (getJob() != 2 | (getJob() == 2 & 0 < getEquips()[3]))
			{
				CreatureInBattle = ClosestCreature ;
				setCurrentAction("Fighting") ;
			}
		}
		
		// Check if there is an animation on, if not, check if there is some ground effect to apply
		if (!Ani.SomeAnimationIsActive())
		{
			AdjacentGroundEffect(screen, maps[PA.getMap()]) ;
		}
		
		setCombo(Uts.RecordCombo(Combo, action, 10)) ;
	}
	public void AdjacentGroundEffect(Screen screen, Maps map)
	{
		if (Uts.CheckAdjacentGround(PA.getPos(), screen, map, "Lava").equals("Inside") & !Elem[4].equals("f"))
		{
			PA.incLife(-5) ;
		}
		if (Uts.isAdjacentTo(PA.getPos(), screen, map, "Water"))
		{
			PA.incThirst(1) ;
		}
	}
	
	public int[] meet(Creatures[] creature, NPCs[] npc, Maps map, int CreatureInBattle, Animations Ani)
	{
		float distx, disty ;
		String groundType = map.groundTypeAtPoint(PA.getPos()) ;
		if (!map.IsACity())	// Map is not a city
		{
			if (groundType != null)
			{
				/* Meeting with collectibles */
				if (groundType.equals("Berry"))
				{
					return new int[] {2, 0} ;
				}
				if (groundType.equals("Herb"))
				{
					return new int[] {2, 1} ;
				}
				if (groundType.equals("Wood"))
				{
					return new int[] {2, 2} ;
				}
				if (groundType.equals("Metal"))
				{
					return new int[] {2, 3} ;
				}
				
				/* Meeting with chests */
				if (groundType.contains("Chest"))
				{
					return new int[] {3, Integer.parseInt(groundType.substring(groundType.length() - 1))} ;
				}
			}
			
			/* Meeting with creatures */
			if (isInBattle())
			{
				distx = Math.abs(PA.getPos()[0] - creature[CreatureInBattle].getPos()[0]) ;
				disty = Math.abs(PA.getPos()[1] - PA.getSize()[1] / 2 - creature[CreatureInBattle].getPos()[1]) ;
				if (distx <= (PA.getSize()[0] + creature[CreatureInBattle].getSize()[0]) / 2 & disty <= (PA.getSize()[1] + creature[CreatureInBattle].getSize()[1]) / 2 & !Ani.isActive(10) & !Ani.isActive(19))
				{
					return new int[] {0, CreatureInBattle} ;
				}
			}
			for (int i = 0 ; i <= map.getCreatureIDs().length - 1 ; ++i)
			{
				int ID = map.getCreatureIDs()[i] ;
				distx = Utg.dist1D(PA.getPos()[0], creature[ID].getPos()[0]) ;
				disty = Utg.dist1D(PA.getPos()[1] - PA.getSize()[1] / 2, creature[ID].getPos()[1]) ;
				if (distx <= (PA.getSize()[0] + creature[ID].getSize()[0]) / 2 & disty <= (PA.getSize()[1] + creature[ID].getSize()[1]) / 2 & !Ani.isActive(10) & !Ani.isActive(19))
				{
					return new int[] {0, ID} ;
				}
			}
		}	
		
		/* Meeting with NPCs */
		if (map.NPCsInMap != null)	// Map has NPCs
		{
			for (int i = 0 ; i <= map.NPCsInMap.length - 1 ; i += 1)
			{
				int NPCID = map.NPCsInMap[i] ;
				if (-1 < NPCID)
				{
					distx = (float) Math.abs(PA.getPos()[0] - npc[NPCID].getPos()[0]) ;	
					disty = (float) Math.abs(PA.getPos()[1] - npc[NPCID].getPos()[1]) ;
					if (distx <= 0.5*PA.getSize()[0] & disty <= 0.5*PA.getSize()[1])
					{
						return new int[] {1, NPCID} ;
					}
				}
			}	
		}
		return new int[] {-1, -1} ;
	}
	public void Collect(int Coltype, Screen screen, Maps[] maps, Items[] items, String[][] AllText, int[] AllTextCat, DrawFunctions DF, Animations Ani)
	{
		int TextCat = AllTextCat[9] ;
		String CollectMessage ;
		if (Coltype == 0)	// Collectible type
		{
			CollectMessage = AllText[TextCat][6] + " " + items[Items.BagIDs[3]].getName() + "!" ;
		} else
		{
			CollectMessage = AllText[TextCat][6] + " " + items[Coltype + Items.BagIDs[0] - 1].getName() + "!" ;
		}
		Ani.SetAniVars(10, new Object[] {100, PA.getPos(), 10, Coltype, CollectMessage}) ;
		Ani.StartAni(10) ;
		
		maps[PA.getMap()].getType()[PA.getPos()[0]][PA.getPos()[1]] = "free" ;	// Make the ground where the player is standing free
		
		// The collecting act itself
		int CollectibleID = -1 ;
		float CollectChance = (float) ((maps[getMap()].getCollectibleLevel() + 2)*Math.random()) ;
		int MapCollectLevel = maps[getMap()].getCollectibleLevel() ;
		float PlayerCollectLevel = -1 ;
		if (Coltype == 0)	// Berry
		{
			Bag[Items.BagIDs[3] - 1 + MapCollectLevel + Coltype] += 1 ;
			if (Job == 3 & Math.random() <= 0.14*Spell[4])	// Double collect
			{
				Bag[Items.BagIDs[3] - 1 + MapCollectLevel + Coltype] += 1 ;	
			}
			CollectibleID = 0 ;
		}
		else if (MapCollectLevel <= PlayerCollectLevel + 1 & PlayerCollectLevel + 1 < CollectChance)
		{					
			Bag[Items.BagIDs[0] + 3*(MapCollectLevel - 1) + Coltype - 1] += 1 ;
			Collect[Coltype - 1] += 0.25/(PlayerCollectLevel + 1) ;
			if (Job == 3 & Math.random() <= 0.14*Spell[4])	// Double collect
			{
				Bag[Items.BagIDs[0] + 3*(MapCollectLevel - 1) + Coltype - 1] += 1 ;
				Collect[Coltype - 1] += 0.25/(PlayerCollectLevel + 1) ;	
			}
			CollectibleID = 0 ;
		}
		else if (MapCollectLevel <= PlayerCollectLevel + 1)
		{
			CollectibleID = 1 ;
		}
		else
		{
			CollectibleID = 2 ;
		}	
		if (!Ani.isActive(10))	// Player is done collecting
		{
			if (CollectibleID == 0)
			{
				CollectibleID = -1 ;
			}
			else
			{
				CollectibleID = Coltype ;
			}
		}
		else
		{
			CollectibleID = -1 ;
		}
		
		maps[PA.getMap()].CreateCollectible(screen, PA.getMap(), CollectibleID) ;	
	}
	public void MoveToNewMap(Pet pet, Screen screen, Maps[] maps, String action, Clip[] Music, boolean MusicIsOn, Animations Ani)
	{
		int[] screenDim = screen.getDimensions() ;
		int[] borders = screen.getBorders() ;
		int NextMap = -1 ;
		int Step = PA.getStep() ;
		int[] CurrentPos = Arrays.copyOf(PA.getPos(), PA.getPos().length), NextPos = new int[2] ;
		int[] MapCon = maps[PA.getMap()].getConnections() ;
		if (action.equals(MoveKeys[0]))	 // Up
		{
			NextPos = new int[] {CurrentPos[0], borders[3] - Step} ;
			if (-1 < MapCon[0] & CurrentPos[0] <= screenDim[0] / 2)
			{
				NextMap = MapCon[0] ;
			}
			else if (-1 < MapCon[7] & screenDim[0] / 2 < CurrentPos[0])
			{
				NextMap = MapCon[7] ;
			}			
		}
		if (action.equals(MoveKeys[1]))	 // Left
		{
			NextPos = new int[] {borders[2] - Step, CurrentPos[1]} ;
			if (-1 < MapCon[1] & CurrentPos[1] <= screenDim[1] / 2)
			{
				NextMap = MapCon[1] ;
			}
			else if (-1 < MapCon[2] & screenDim[1] / 2 < CurrentPos[1])
			{
				NextMap = MapCon[2] ;
			}		
		}
		if (action.equals(MoveKeys[2]))	 // Down
		{
			NextPos = new int[] {CurrentPos[0], borders[1] + Step} ;
			if(-1 < MapCon[3] & CurrentPos[0] <= screenDim[0] / 2)
			{
				NextMap = MapCon[3] ;
			}
			else if(-1 < MapCon[4] & screenDim[0] / 2 < CurrentPos[0])
			{
				NextMap = MapCon[4] ;	
			}
		}
		if (action.equals(MoveKeys[3]))	 // Right
		{			
			NextPos = new int[] {borders[0] + Step, CurrentPos[1]} ;
			if(-1 < MapCon[5] & screenDim[1] / 2 < CurrentPos[1])
			{
				NextMap = MapCon[5] ;
			}
			else if(-1 < MapCon[6] & CurrentPos[1] <= screenDim[1] / 2)
			{
				NextMap = MapCon[6] ;
			}
		}
		if (-1 < NextMap)
		{
			if (maps[NextMap].GroundIsWalkable(NextPos, Elem[4]))
			{
				if (MusicIsOn & Maps.Music[PA.getMap()] != Maps.Music[NextMap])
				{
					Utg.SwitchMusic(Music[Maps.Music[PA.getMap()]], Music[Maps.Music[NextMap]]) ;
				}
				PA.setMap(NextMap) ;
				PA.setContinent(maps[PA.getMap()].getContinent()) ;
				PA.setPos(NextPos) ;	
				if (0 < pet.getLife()[0])
				{
					pet.getPersonalAtt().setPos(NextPos) ;
				}
				if (maps[PA.getMap()].IsACity())
				{
					CurrentAction = "existing" ;
				}
			}
			

			if (!isInBattle())
			{
				if (getContinent() == 3)
				{
					Ani.SetAniVars(16, new Object[] {500, PterodactileImage, SpeakingBubbleImage}) ;
					Ani.StartAni(16) ;
				}
			}
		}
	}
	public void SupSkillCounters(Skills[] spell, Creatures[] creature, int CreatureID)
	{
		for (int s = 0 ; s <= Spell.length - 1 ; s += 1)
		{
			if (SpellCounter[s][0] < spell[s].getDuration() & SpellIsActive[s])
			{
				SpellCounter[s][0] += 1 ;
			}
			if (SpellCounter[s][0] == spell[s].getDuration())
			{
				SpellCounter[s][0] = 0 ;
				for (int i = 0 ; i <= spell[s].getBuffs().length - 1 ; ++i)
				{
					ApplyBuffsAndNerfs("deactivate", "buffs", i, s, spell, SpellIsActive(s, spell)) ;
				}
				for (int i = 0 ; i <= spell[s].getNerfs().length - 1 ; ++i)
				{
					if (-1 < CreatureID)
					{
						creature[CreatureID].ApplyBuffsAndNerfs("deactivate", "nerfs", i, Spell[s], spell[s], SpellIsActive(s, spell)) ;
					}
				}
				SpellIsActive[s] = false ;
			}
			if (SpellCounter[s][1] < spell[s].getCooldown())
			{
				SpellCounter[s][1] += 1 ;
			}
		}
	}
	

	public int GetNumberOfSpells()
	{
		int NumberOfSkills = 0 ;
		int[] Sequence = GetSpellSequence() ;
		for (int i = 0 ; i <= Sequence.length - 1 ; i += 1)
		{
			NumberOfSkills += Sequence[i] ;
		}
		return NumberOfSkills ;
	}
	public int GetNumberOfProSpells()
	{
		int NumberOfProSkills = 0 ;
		int[] ProSequence = GetProSpellSequence() ;
		for (int i = 0 ; i <= ProSequence.length - 1 ; i += 1)
		{
			NumberOfProSkills += ProSequence[i] ;
		}
		return NumberOfProSkills ;
	}
	public int[] activeSpells(Skills[] spell)
	{
		int[] ActiveSkills = null ;
		for (int i = 0 ; i <= spell.length - 1 ; i += 1)
		{
			if ((spell[i].getType().equals("Active") | spell[i].getType().equals("Support") | spell[i].getType().equals("Offensive")) & 0 < Spell[i])
			{
				ActiveSkills = Utg.AddElem(ActiveSkills, i) ;
			}
		}
		return ActiveSkills ;
	}
	public int[] GetSpellSequence()
	{
		// Sequence: [Player job][Number of skills per line]
		int[][] Sequence = new int[][] {{1, 3, 3, 3, 3, 1}, {3, 3, 3, 3, 3}, {3, 3, 3, 3, 3}, {2, 3, 3, 3, 3}, {2, 3, 3, 3, 3}} ;
		return Sequence[Job] ;
	}
	public int[] GetProSpellSequence()
	{
		// Sequence: [Player job][Number of skills per line]
		int[][] Sequence = new int[][] {{1, 2, 2, 2, 2, 1}, {1, 2, 2, 2, 2, 1}, {1, 2, 2, 2, 2, 1}, {1, 2, 2, 2, 2, 1}, {1, 2, 2, 2, 2, 1}, {1, 2, 2, 2, 2, 1}, {1, 2, 2, 2, 2, 1}, {1, 2, 2, 2, 2, 1}, {1, 2, 2, 2, 2, 1}, {1, 2, 2, 2, 2, 1}} ;
		return Sequence[ProJob] ;
	}
	public int[] GetActiveQuests()
	{
		int[] ActiveQuests = null ;
		for (int i = 0 ; i <= Quest.length - 1 ; i += 1)
		{
			if (-1 < Quest[i])
			{
				ActiveQuests = Utg.AddElem(ActiveQuests, i) ;
			}
		}
		return ActiveQuests ;
	}	
	

	/*public int Collect(Maps[] maps, Items[] item, int[] Pos, int CollectibleType, int[] BagIDs, int CollectResult, boolean PlayerIsCollecting, DrawFunctions DF)
	{	
		float CollectChance = (float) ((maps[getMap()].getCollectibleLevel() + 2)*Math.random()) ;
		int MapCollectLevel = maps[getMap()].getCollectibleLevel() ;
		float PlayerCollectLevel = -1 ;
		if (CollectibleType == 0)	// Berry
		{
			Bag[BagIDs[3] - 1 + MapCollectLevel + CollectibleType] += 1 ;
			if (Job == 3 & Math.random() <= 0.14*Skill[4])	// Double collect
			{
				Bag[BagIDs[3] - 1 + MapCollectLevel + CollectibleType] += 1 ;	
			}
			CollectResult = 0 ;
		}
		else if (MapCollectLevel <= PlayerCollectLevel + 1 & PlayerCollectLevel + 1 < CollectChance)
		{					
			Bag[BagIDs[0] + 3*(MapCollectLevel - 1) + CollectibleType - 1] += 1 ;
			Collect[CollectibleType - 1] += 0.25/(PlayerCollectLevel + 1) ;
			if (Job == 3 & Math.random() <= 0.14*Skill[4])	// Double collect
			{
				Bag[BagIDs[0] + 3*(MapCollectLevel - 1) + CollectibleType - 1] += 1 ;
				Collect[CollectibleType - 1] += 0.25/(PlayerCollectLevel + 1) ;	
			}
			CollectResult = 0 ;
		}
		else if (MapCollectLevel <= PlayerCollectLevel + 1)
		{
			CollectResult = 1 ;
		}
		else
		{
			CollectResult = 2 ;
		}	
		if (!PlayerIsCollecting)
		{
			if (CollectResult == 0)
			{
				return -1 ;
			}
			else
			{
				return CollectibleType ;
			}
		}
		return -1 ;
	}*/
	public void Tent()
	{
		PA.setLife(new float[] {PA.getLife()[0], PA.getLife()[1]}) ;
	}
	public void ActivateDef()
	{
		BA.getPhyDef()[1] += BA.getPhyDef()[0] ;
		BA.getMagDef()[1] += BA.getMagDef()[0] ;
	}
	public void QuestWindow(CreatureTypes[] creatureTypes, Creatures[] creatures, Quests[] quest, Items[] items, int[] MousePos, DrawFunctions DF)
	{
		int[] windowLimit = new int[] {8, 0, 0, 1, 0, 0} ;
		int[] ActiveQuests = Utg.ArrayWithValuesGreaterThan(Quest, -1) ;
		//windowLimit[player.getContinent()] = Math.max(ActiveQuests.length - 1, 0)/5 ;
		SelectedWindow[3] = Uts.MenuSelection(ActionKeys[1], ActionKeys[3], action, SelectedWindow[3], windowLimit[PA.getContinent()]) ;
		DF.DrawQuestWindow(creatureTypes, creatures, quest, items, Language, PA.getContinent(), Bag, MousePos, ActiveQuests, SelectedWindow[3], windowLimit, QuestImage) ;
	}
	public void DeactivateDef()
	{
		BA.getPhyDef()[1] += -BA.getPhyDef()[0] ;
		BA.getMagDef()[1] += -BA.getMagDef()[0] ;
	}
	public void ActivateRide()
	{
		if (IsRiding)
		{
			setStep(PA.getStep()/2) ;
		}
		else
		{
			setStep(2*PA.getStep()) ;
		}
		IsRiding = !IsRiding ;
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
		if (Actions[0][0] % Actions[0][1] == 0 & !SomeAnimationIsOn)
		{
			Actions[0][2] = 1 ;							// Player can move
		}
		if (Actions[1][0] % Actions[1][1] == 0)
		{
			PA.incMP((float) 0.02 * PA.getMp()[1]) ;	// Player heals Mp
			Actions[1][0] = 0 ;
		}
		if (Actions[2][0] % Actions[2][1] == 0)
		{
			PA.incSatiation(-1) ;					// decrease satiation
			if (PA.getSatiation()[0] == 0)				// Player is hungry
			{
				PA.incLife(-1) ;
			}
			Actions[2][0] = 0 ;
		}
		if (Actions[3][0] % Actions[3][1] == 0)
		{
			PA.incThirst(-1) ;						// decrease thrist
			if (PA.getThirst()[0] == 0)					// Player is thirsty
			{
				PA.incLife(-1) ;
			}
			Actions[3][0] = 0 ;
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
			BA.getBattleActions()[0][2] = 1 ;	// Player can atk
		}
	}
	public void ResetBattleActions()
	{
		BA.getBattleActions()[0][0] = 0 ;
		BA.getBattleActions()[0][2] = 0 ;
	}
	/*public boolean usedSkill()
	{
		return Utg.isNumeric(BA.getCurrentAction()) ;
	}*/
	public void ShowWindows(Pet pet, Creatures[] creature, CreatureTypes[] creatureTypes, Quests[] quest, Screen screen, Items[] items, Maps[] maps, Icon[] icon, Battle B, String[][] AllText, int[] AllTextCat, Clip[] Music, boolean MusicIsOn, boolean SoundEffectsAreOn, int[] MousePos, Image CoinIcon, DrawFunctions DF)
	{
		if (WindowIsOpen[0])
		{
			Bag(pet, creature, screen.getDimensions(), AllTextCat, AllText, items, B, MousePos, DF) ;
		}
		if (WindowIsOpen[2])
		{
			AttWindow(AllText, AllTextCat, items, icon, screen.getDimensions(), getAttIncrease()[getProJob()], MousePos, CoinIcon, DF.getDrawPrimitives()) ;
		}
		if (WindowIsOpen[4])
		{		
			FabBook(items, MousePos, DF) ;
		}
		Tent() ;
		if (WindowIsOpen[3])
		{
			DF.DrawPetWindow(pet, new int[] {(int)(0.1*screen.getDimensions()[0]), (int)(0.9*screen.getDimensions()[1])}) ;
		}
		if (WindowIsOpen[5])
		{
			DF.DrawMap(PA.getMap(), PA.getDir(), maps) ;
		}		
		if (WindowIsOpen[6])
		{
			QuestWindow(creatureTypes, creature, quest, items, MousePos, DF) ;
		}
		if (WindowIsOpen[1])
		{
			Bestiary(creatureTypes, items, getCreaturesDiscovered(), MousePos, DF) ;
		}
		if (WindowIsOpen[7])
		{
			Object[] OptionStatus = OptionsWindow(Music, DF) ;
			MusicIsOn = (boolean) OptionStatus[0] ;
			SoundEffectsAreOn = (boolean) OptionStatus[1] ;
			
			if (!MusicIsOn)
			{
				Utg.StopMusic(Music[Maps.Music[getMap()]]) ;
			}
		}
		if (WindowIsOpen[8])
		{
			HintsMenu(screen.getDimensions(), MousePos, ClassColors[getJob()], DF) ;
		}
	}
	public void TreasureChestReward(int ChestID, Image CoinIcon, Maps[] maps, Items[] items, Animations Ani)
	{
		int[][] ItemRewards = new int[][] {{}, {455 + (Items.BagIDs[6] - Items.BagIDs[5]) / 5 * Job}, {456 + (Items.BagIDs[6] - Items.BagIDs[5]) / 5 * Job}, {457 + (Items.BagIDs[6] - Items.BagIDs[5]) / 5 * Job}, {}, {134, 134, 134, 134, 134, 135, 135, 135, 135, 135}, {1346, 1348, 1349, 1350, 1351}} ;
		int[][] GoldRewards = new int[][] {{2000, 2000, 2000, 2000, 2000}, {}, {}, {}, {4000, 4000, 4000, 4000, 4000}, {}, {}} ;
		if (Job == 2)
		{
			ItemRewards = new int[][] {{}, {456 + (Items.BagIDs[6] - Items.BagIDs[5]) / 5 * Job}, {457 + (Items.BagIDs[6] - Items.BagIDs[5]) / 5 * Job}, {32, 33, 34, 35, 36, 37, 38, 39}, {}, {134, 134, 134, 134, 134, 135, 135, 135, 135, 135}, {1346, 1348, 1349, 1350, 1351}} ;
		}
		
		for (int j = 0 ; j <= ItemRewards.length - 1 ; j += 1)
		{
			Bag[ItemRewards[ChestID][j]] += 1 ;
		}
		for (int j = 0 ; j <= GoldRewards.length - 1 ; j += 1)
		{
			Gold[0] += GoldRewards[ChestID][j] ;
		}
		maps[PA.getMap()].getType()[PA.getPos()[0]][PA.getPos()[1]] = "free" ;
		Ani.SetAniVars(18, new Object[] {100, items, ItemRewards, GoldRewards, color[0], CoinIcon}) ;
		Ani.StartAni(18) ;
	}
	public void SpendArrow()
	{
		if (0 < Equips[3] & 0 < Bag[Equips[3]])
		{
			Bag[Equips[3]] += -1 ;
			if (Job == 2 & Math.random() <= 0.1*Spell[13])
			{
				Bag[Equips[3]] += 1 ;
			}
		}
		if (Bag[Equips[3]] == 0)
		{
			Equips[3] = 0 ;
		}
	}
	public int StealItem(Creatures creature, Items[] items, int spellLevel)
	{	
		int ID = (int)(Utg.ArrayWithValuesGreaterThan(creature.getBag(), -1).length*Math.random() - 0.01) ;
		int StolenItemID = -1 ;
		if(-1 < creature.getBag()[ID])
		{
			if(Math.random() <= 0.01*items[creature.getBag()[ID]].getDropChance() + 0.01*spellLevel)
			{
				Bag[creature.getBag()[ID]] += 1 ;
				StolenItemID = items[creature.getBag()[ID]].getID() ;
			}
		}
		return StolenItemID ;
	}
	public void OffensiveSpell(Creatures creature, int SpellID, Skills[] spell)
	{
		PA.getMp()[0] += -spell[SpellID].getMpCost() ;
		SpellCounter[SpellID][1] = 0 ;
		SpellIsActive[SpellID] = true ;
		if (Job == 0)
		{
			
		}
		if (Job == 1)
		{
			
		}
		if (Job == 2)
		{
			if (SpellID == 3)
			{
				
			}
		}
		for (int i = 0 ; i <= spell[SpellID].getBuffs().length - 1 ; ++i)
		{
			ApplyBuffsAndNerfs("activate", "buffs", i, SpellID, spell, SpellIsActive(SpellID, spell)) ;
			creature.ApplyBuffsAndNerfs("activate", "nerfs", i, Spell[SpellID], spell[SpellID], SpellIsActive(SpellID, spell)) ;
		}
	}
	public void SupSpell(Pet pet, int SpellID, Skills[] spell)
	{
		int skillLevel = Spell[SpellID] ;
		if (spell[SpellID].getMpCost() <= PA.getMp()[0] & 0 < skillLevel)
		{
			getStats()[1] += 1 ;	// Number of mag atks
			ResetBattleActions() ;			
			PA.getMp()[0] += -spell[SpellID].getMpCost() ;
			if (Job == 0)
			{
				
			}
			if (Job == 1)
			{
				if (SpellID == 10)
				{
					PA.getLife()[0] += (float)Math.min((0.04*skillLevel + 0.002*BA.TotalMagAtk()), 0.3)*PA.getLife()[1] ;
					PA.getLife()[0] = (float)Math.min(PA.getLife()[0], PA.getLife()[1]) ;
					if (0 < pet.getLife()[0] & spell[SpellID].getMpCost() <= pet.getMp()[0])
					{
						pet.getMp()[0] += -spell[SpellID].getMpCost() ;
						pet.getLife()[0] += (float)Math.min(Math.min((0.02*skillLevel + 0.002*(pet.getBattleAtt().TotalMagAtk())), 0.15)*pet.getLife()[1], pet.getLife()[1]) ;
						pet.getLife()[0] = (float)Math.min(pet.getLife()[0], pet.getLife()[1]) ;
					}
				}
			}
			for (int i = 0 ; i <= spell[SpellID].getBuffs().length - 1 ; ++i)
			{
				ApplyBuffsAndNerfs("activate", "buffs", i, SpellID, spell, SpellIsActive(SpellID, spell)) ;
			}
			SpellIsActive[SpellID] = true ;
		}
	}
	public void autoSpells(Creatures creature, Skills[] spell)
	{		
		/*if (Job == 3 & PA.getLife()[0] < 0.2 * PA.getLife()[1] & 0 < Skill[12] & !SkillBuffIsActive[12][0])	// Survivor's instinct
		{
			for (int i = 0 ; i <= skill[12].getBuffs().length - 1 ; ++i)
			{
				BuffsAndNerfs(player, pet, creature, skill[12].getBuffs(), Skill[12], i, false, "Player", "activate") ;
			}
			SkillBuffIsActive[12][0] = true ;
		}
		if (Job == 3 & 0.2*Life[1] <= Life[0] & 0 < Skill[12] & SkillBuffIsActive[12][0])	// Survivor's instinct
		{
			for (int i = 0 ; i <= skill[12].getBuffs().length - 1 ; ++i)
			{
				BuffsAndNerfs(player, pet, creature, skill[12].getBuffs(), Skill[12], i, false, "Player", "deactivate") ;
			}
			SkillBuffIsActive[12][0] = false ;
		}*/
	}
	public void ApplyBuffsAndNerfs(String action, String type, int att, int SkillID, Skills[] skills, boolean SkillIsActive)
	{
		int ActionMult = 1 ;
		float[][] Buff = new float[14][5] ;	// [PA.getLife(), PA.getMp(), PhyAtk, MagAtk, Phy def, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence][effect]
		float[] OriginalValue = new float[14] ;	// [PA.getLife(), PA.getMp(), PhyAtk, MagAtk, Phy def, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence]
		float[][] Buffs = null ;
		int BuffNerfLevel = Spell[SkillID] ;
		if (type.equals("buffs"))
		{
			Buffs = skills[SkillID].getBuffs() ;
		}
		else if (type.equals("nerfs"))
		{
			Buffs = skills[SkillID].getNerfs() ;
		}
		OriginalValue = new float[] {PA.getLife()[1], PA.getMp()[1], BA.getPhyAtk()[0], BA.getMagAtk()[0], BA.getPhyDef()[0], BA.getMagDef()[0], BA.getDex()[0], BA.getAgi()[0], BA.getCrit()[0], BA.getStun()[0], BA.getBlock()[0], BA.getBlood()[0], BA.getBlood()[2], BA.getBlood()[4], BA.getBlood()[6], BA.getPoison()[0], BA.getPoison()[2], BA.getPoison()[4], BA.getPoison()[6], BA.getSilence()[0]} ;
		if (action.equals("deactivate"))
		{
			ActionMult = -1 ;
		}
		if (att == 11 | att == 12)
		{
			if (action.equals("deactivate"))
			{
				Buff[att][0] += (OriginalValue[att]*Buffs[att][0] + Buffs[att][1])*BuffNerfLevel*ActionMult ;
				Buff[att][1] += (OriginalValue[att]*Buffs[att][3] + Buffs[att][4])*BuffNerfLevel*ActionMult ;
				Buff[att][2] += (OriginalValue[att]*Buffs[att][6] + Buffs[att][7])*BuffNerfLevel*ActionMult ;
				Buff[att][3] += (OriginalValue[att]*Buffs[att][9] + Buffs[att][10])*BuffNerfLevel*ActionMult ;
			}
			else
			{
				if (Math.random() <= Buffs[att][2])
				{
					Buff[att][1] += (OriginalValue[att]*Buffs[att][0] + Buffs[att][1])*BuffNerfLevel*ActionMult ;
				}
				if (Math.random() <= Buffs[att][5])
				{
					Buff[att][1] += (OriginalValue[att]*Buffs[att][3] + Buffs[att][4])*BuffNerfLevel*ActionMult ;
				}
				if (Math.random() <= Buffs[att][8])
				{
					Buff[att][2] += (OriginalValue[att]*Buffs[att][6] + Buffs[att][7])*BuffNerfLevel*ActionMult ;
				}
				if (Math.random() <= Buffs[att][11])
				{
					Buff[att][3] += (OriginalValue[att]*Buffs[att][9] + Buffs[att][10])*BuffNerfLevel*ActionMult ;
				}
			}
		}
		else if (action.equals("deactivate") | Math.random() <= Buffs[att][2])
		{
			Buff[att][0] += (OriginalValue[att]*Buffs[att][0] + Buffs[att][1])*BuffNerfLevel*ActionMult ;
		}
		if (!SkillIsActive)
		{
			PA.getLife()[0] += Buff[0][0] ;
			PA.getMp()[0] += Buff[1][0] ;
			BA.getPhyAtk()[1] += Buff[2][0] ;
			BA.getMagAtk()[1] += Buff[3][0] ;
			BA.getPhyDef()[1] += Buff[4][0] ;
			BA.getMagDef()[1] += Buff[5][0] ;
			BA.getDex()[1] += Buff[6][0] ;
			BA.getAgi()[1] += Buff[7][0] ;
			BA.getCrit()[1] += Buff[8][0] ;
			BA.getStun()[1] += Buff[9][0] ;
			BA.getBlock()[1] += Buff[10][0] ;
			BA.getBlood()[1] += Buff[11][0] ;
			BA.getBlood()[3] += Buff[11][1] ;
			BA.getBlood()[5] += Buff[11][2] ;
			BA.getBlood()[7] += Buff[11][3] ;
			BA.getBlood()[8] += Buff[11][4] ;
			BA.getPoison()[1] += Buff[12][0] ;
			BA.getPoison()[3] += Buff[12][1] ;
			BA.getPoison()[5] += Buff[12][2] ;
			BA.getPoison()[7] += Buff[12][3] ;
			BA.getPoison()[8] += Buff[12][4] ;
			BA.getSilence()[1] += Buff[13][0] ;
		}	
	}
	public void TakeBloodAndPoisonDamage(Creatures creature)
	{
		float BloodDamage = 0 ;
		float PoisonDamage = 0 ;
		float BloodMult = 1, PoisonMult = 1 ;
		if (Job == 4)
		{
			PoisonMult += -0.1*Spell[13] ;
		}
		if (0 < BA.getSpecialStatus()[2])	// Blood
		{
			BloodDamage = Math.max(creature.getBattleAtt().TotalBloodAtk()*BloodMult - BA.TotalBloodDef(), 0) ;
		}
		if (0 < BA.getSpecialStatus()[3])	// Poison
		{
			PoisonDamage = Math.max(creature.getBattleAtt().TotalPoisonAtk()*PoisonMult - BA.TotalPoisonDef(), 0) ;
		}
		PA.getLife()[0] += -BloodDamage - PoisonDamage ;
		if (0 < BloodDamage)
		{
			Stats[18] += BA.TotalBloodDef() ;
		}
		if (0 < PoisonDamage)
		{
			Stats[21] += BA.TotalPoisonDef() ;
		}
	}
	public void train(Object[] playerAtkResult)
	{
		int effect = (int) playerAtkResult[1] ;
		String atkType = (String) playerAtkResult[3] ;
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
	public void updateoffensiveStats(Object[] playerAtkResult, Creatures creature)
	{
		/* 0: Number of phy attacks, 
		 * 1: number of spells used, 
		 * 2: number of defenses, 
		 * 3: total phy damage inflicted, 
		 * 4: total phy damage received, 
		 * 5: total mag damage inflicted, 
		 * 6: total mag damage received, 
		 * 7: total phy damage defended, 
		 * 8: total mag damage defended, 
		 * 9: total hits inflicted, 
		 * 10: total hits received, 
		 * 11: total dodges, 
		 * 12: number of crit, 
		 * 13: total crit damage, 
		 * 14: total stun, 
		 * 15: total block, 
		 * 16: total blood, 
		 * 17: total blood damage, 
		 * 18: total blood def, 
		 * 19: total poison, 
		 * 20: total poison damage, 
		 * 21: total poison def, 
		 * 22: total silence
		*/
		int damage = (int) playerAtkResult[0] ;
		int effect = (int) playerAtkResult[1] ;
		if (!action.equals(""))						// player has performed an action
		{
			if (0 <= damage)							// player inflicted damage
			{	
				if (BA.actionisskill())					// player performed a magical atk
				{
					Stats[1] += 1 ;							// Total number of magical atks (spells) performed by the player
					Stats[5] += damage ;					// Total magical damage inflicted by the player
				}
				else									// player performed a physical atk
				{
					Stats[0] += 1 ;							// Total number of physical atks performed by the player
					Stats[3] += damage ;					// Total physical damage inflicted by the player
				}
			}		
			if (effect <= 1)							// player performed a successful hit
			{
				Stats[9] += 1 ;								// total number of successful hits performed by the player
				// for the status, dividing the duration of the status by the duration applied to get the number of times the status was applied
				if (0 < BA.getStun()[4])
				{
					Stats[14] += creature.getBattleAtt().getSpecialStatus()[0] / BA.getStun()[4] ;	// total number of stun inflicted by the player
				}
				if (0 < BA.getBlock()[4])
				{
					Stats[15] += creature.getBattleAtt().getSpecialStatus()[1] / BA.getBlock()[4] ;	// total number of block performed by the player
				}
				if (0 < BA.getBlood()[8])
				{
					Stats[16] += creature.getBattleAtt().getSpecialStatus()[2] / BA.getBlood()[8] ;	// total number of blood inflicted by the player
					if (0 < creature.getBattleAtt().getSpecialStatus()[2])
					{
						Stats[17] += 1 ;	// total number of blood inflicted by the player
					}
				}
				if (0 < BA.getPoison()[8])
				{
					Stats[19] += creature.getBattleAtt().getSpecialStatus()[3] / BA.getPoison()[8] ;	// total number of poison inflicted by the player
					if (0 < creature.getBattleAtt().getSpecialStatus()[3])
					{
						Stats[20] += 1 ;	// total number of blood inflicted by the player
					}
				}
				if (0 < BA.getSilence()[4])
				{
					Stats[22] += creature.getBattleAtt().getSpecialStatus()[4] / BA.getSilence()[4] ;	// total number of silence inflicted by the player
				}
			}
			if (effect == 1)				// player performed a critical atk (physical or magical)
			{
				Stats[12] += 1 ;							// total number of critical hits performed by the player
				Stats[13] += damage ;						// total critical damage (physical + magical) performed by the player
			}
			if (action.equals(Player.ActionKeys[3]))	// player defended
			{
				Stats[2] += 1 ;								// Number of defenses performed by the player
			}
		}
	}
	public void updatedefensiveStats(int[] creatureAtkResult, boolean creaturePhyAtk, Creatures creature)
	{
		/* 0: Number of phy attacks, 
		 * 1: number of spells used, 
		 * 2: number of defenses, 
		 * 3: total phy damage inflicted, 
		 * 4: total phy damage received, 
		 * 5: total mag damage inflicted, 
		 * 6: total mag damage received, 
		 * 7: total phy damage defended, 
		 * 8: total mag damage defended, 
		 * 9: total hits inflicted, 
		 * 10: total hits received, 
		 * 11: total dodges, 
		 * 12: number of crit, 
		 * 13: total crit damage, 
		 * 14: total stun, 
		 * 15: total block, 
		 * 16: total blood, 
		 * 17: total blood damage, 
		 * 18: total blood def, 
		 * 19: total poison, 
		 * 20: total poison damage, 
		 * 21: total poison def, 
		 * 22: total silence
		*/
		if (creatureAtkResult[1] <= 1)	// Hit
		{			
			Stats[10] += 1 ;						// number of hits the player has taken
			if (creaturePhyAtk)	// Creature physical atk
			{				
				Stats[4] += creatureAtkResult[0] ;	// total phy damage received by the player
				Stats[7] += BA.TotalPhyDef() ;		// total phy damage defended by the player
			}
			else				// Creature magical atk
			{
				Stats[6] += creatureAtkResult[0] ;	// total mag damage received by the player
				Stats[8] += BA.TotalMagDef() ;		// total mag damage defended by the player
			}
			if (0 < BA.getSpecialStatus()[2])
			{
				Stats[18] += BA.getBlood()[3] + BA.getBlood()[4] ;		// total number of blood defended by the player
			}
			if (0 < BA.getSpecialStatus()[3])
			{
				Stats[21] += BA.getPoison()[3] + BA.getPoison()[4] ;	// total number of blood defended by the player
			}
		}
		else
		{
			Stats[11] += 1 ;						// total number of hits the player has dogded
		}
	}
	public void Win(Creatures creature, Items[] items, Quests[] quest, Animations Ani)
	{		
		ArrayList<String> GetItemsObtained = new ArrayList<String>(Arrays.asList(new String[0])) ;		
		for (int i = 0 ; i <= 9 ; ++i)
		{
			if(-1 < creature.getBag()[i])
			{
				if(Math.random() <= 0.01*items[creature.getBag()[i]].getDropChance())
				{
					Bag[creature.getBag()[i]] += 1 ;	
					GetItemsObtained.add(items[creature.getBag()[i]].getName()) ;
				}
			}
		}
		String[] ItemsObtained = new String[GetItemsObtained.size()] ;
		ItemsObtained = GetItemsObtained.toArray(ItemsObtained) ;
		Gold[0] += creature.getGold()*Utg.RandomMult((float)0.1)*Gold[2] ;
		PA.getExp()[0] += creature.getExp()[0]*PA.getExp()[2] ;
		if (GetActiveQuests() != null)
		{
			for (int q = 0 ; q <= GetActiveQuests().length - 1 ; q += 1)
			{
				quest[GetActiveQuests()[q]].IncReqCreaturesCounter(creature.getType()) ;
			}
		}
		Ani.SetAniVars(12, new Object[] {100, ItemsObtained, color[0]}) ;
		Ani.StartAni(12) ;
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
		AttPoints += 2 ;
	}
	public void ResetPosition()
	{
		PA.setMap(Job) ;
		PA.setContinent(0) ;
		if (Job == 0)
		{
			PA.setPos(new int[] {340, 340}) ;
		}
		if (Job == 1)
		{
			PA.setPos(new int[] {340, 180}) ;
		}
		if (Job == 2)
		{
			PA.setPos(new int[] {40, 220}) ;
		}
		if (Job == 3)
		{
			PA.setPos(new int[] {340, 340}) ;
		}
		if (Job == 4)
		{
			PA.setPos(new int[] {340, 640}) ;
		}
		/*if (MusicIsOn)
		{
			UtilGeral.SwitchMusic(Music[MusicInMap[PA.getMap()]], Music[MusicInMap[Job]]) ;
		}*/
	}
	public void Dies()
	{
		Gold[0] = (float)(0.8*Gold[0]) ;
		PA.getLife()[0] = PA.getLife()[1] ;
		PA.getMp()[0] = PA.getMp()[1] ;
		PA.getSatiation()[0] = PA.getSatiation()[1] ;
		ResetPosition() ;
	}
	public void AddCreatureToBestiary(int CreatureID)
	{
		if (CreaturesDiscovered != null)
		{
			if (!Utg.ArrayContains(CreaturesDiscovered, CreatureID))
			{
				CreaturesDiscovered = Utg.AddElem(CreaturesDiscovered, CreatureID) ;
			}
		}
		else
		{
			CreaturesDiscovered = new int[] {CreatureID} ;
		}
	}

	
	/* Get current state methods */
	public boolean SpellIsActive(int SpellID, Skills[] spell)
	{
		if (SpellCounter[SpellID][0] != 0)
		{
			return true ;
		}
		return false ;
	}
	public boolean SkillIsReady(int SpellID, Skills[] spell)
	{
		if (SpellCounter[SpellID][1] == spell[SpellID].getCooldown())
		{
			return true ;
		}
		return false ;
	}
	public boolean isEquippable(int ItemID)
	{
		if ((Items.BagIDs[6] + Items.NumberOfItems[6] / 5 * Job) <= ItemID & ItemID <= Items.BagIDs[6] + Items.NumberOfItems[6] / 5 * (Job + 1))
		{
			return true ;
		}
		return false ;
	}
	public boolean IsRiding()
	{
		return IsRiding ;
	}
	public boolean isInBattle()
	{
		if (CurrentAction.equals("Fighting"))
		{
			return true ;
		}
		return false ;
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
	public boolean isStun()
	{
		return BA.isStun() ;
	}
	public boolean isSilent()
	{
		if (BA.getSpecialStatus()[4] <= 0)
		{
			return false ;
		}
		return true ;
	}
		
	
	/* Action windows */
	public void AttWindow(String[][] AllText, int[] AllTextCat, Items[] items, Icon[] icons, int[] MainWinDim, float[] PlayerAttributeIncrease, int[] MousePos, Image GoldCoinImage, DrawPrimitives DP)
	{
		int WindowLimit = 2 ;
		SelectedWindow[0] = Uts.MenuSelection(Player.ActionKeys[0], Player.ActionKeys[2], action, SelectedWindow[0], WindowLimit) ;
		for (int i = 0 ; i <= 7 - 1 ; i += 1)
		{
			if (icons[i].ishovered(MousePos) & (action.equals("Enter") | action.equals("MouseLeftClick")) & 0 < AttPoints)
			{
				int SelectedItem = i ;
				if (SelectedItem == 0)
				{
					PA.getLife()[0] += PlayerAttributeIncrease[SelectedItem] ;
					PA.getLife()[1] += PlayerAttributeIncrease[SelectedItem] ;
				}
				else if (SelectedItem == 1)
				{
					PA.getMp()[0] += PlayerAttributeIncrease[SelectedItem] ;
					PA.getMp()[1] += PlayerAttributeIncrease[SelectedItem] ;
				}
				else if (SelectedItem == 2)
				{
					BA.getPhyAtk()[0] += PlayerAttributeIncrease[SelectedItem] ;
				}
				else if (SelectedItem == 3)
				{
					BA.getMagAtk()[0] += PlayerAttributeIncrease[SelectedItem] ;
				}
				else if (SelectedItem == 4)
				{
					BA.getPhyDef()[0] += PlayerAttributeIncrease[SelectedItem] ;
				}
				else if (SelectedItem == 5)
				{
					BA.getMagDef()[0] += PlayerAttributeIncrease[SelectedItem] ;
				}
				else if (SelectedItem == 6)
				{
					BA.getDex()[0] += PlayerAttributeIncrease[SelectedItem] ;
				}
				else if (SelectedItem == 7)
				{
					BA.getAgi()[0] += PlayerAttributeIncrease[SelectedItem] ;
				}
				AttPoints += -1 ;
			}
		}
		int[] WinPos = new int[] {(int) (0.3 * MainWinDim[0]), (int) (0.2 * MainWinDim[1])} ;
		DrawAttWindow(MainWinDim, WinPos, MousePos, AllText, AllTextCat, SelectedWindow[0], GoldCoinImage, items, icons, DP) ;
	}
	public Object[] OptionsWindow(Clip[] Music, DrawFunctions DF)
	{
		boolean CustomKeyIsActionKey = false ;
		if (SelectedMenu[0] == 0)
		{
			SelectedItem[0] = Uts.MenuSelection(Player.ActionKeys[0], Player.ActionKeys[2], action, SelectedItem[0], 5) ;
			if (action.equals("Enter") | action.equals("MouseLeftClick"))
			{
				if (SelectedItem[0] <= 2)
				{
					OptionStatus[SelectedItem[0]] = !(boolean)OptionStatus[SelectedItem[0]] ;
					if (SelectedItem[0] == 0)
					{
						Utg.PlayMusic(Music[Maps.Music[PA.getMap()]]) ;
					}
				}
				else if (SelectedItem[0] == 3)
				{
					OptionStatus[3] = ((int)OptionStatus[3] + 1) % 2 ;
				}
				else if (SelectedItem[0] == 4)
				{
					OptionStatus[4] = ((int)OptionStatus[4] + 1) % 4 ;
				}
				else if (3 < SelectedItem[0] & SelectedItem[0] <= 3 + Player.ActionKeys.length)
				{
					SelectedMenu[0] = 1 ;
					SelectedItem[0] = 0 ;
				}
			}
		}
		else if (1 <= SelectedMenu[0])
		{
			if (SelectedMenu[0] == 1)
			{
				SelectedItem[0] = Uts.MenuSelection(Player.ActionKeys[0], Player.ActionKeys[2], action, SelectedItem[0], Player.ActionKeys.length - 1) ;
			}
			if (!action.equals(""))
			{
				if (SelectedMenu[0] == 2)
				{
					for (int i = 0 ; i <= Player.ActionKeys.length - 1 ; i += 1)
					{
						if (action.equals(Player.ActionKeys[i]) | action.equals("Enter") | action.equals("MouseLeftClick") | action.equals("Escape"))
						{
							CustomKeyIsActionKey = true ;
						}
					}
					if (!CustomKeyIsActionKey)
					{
						CustomKey = action ;
						Player.ActionKeys[SelectedItem[0]] = CustomKey ;
						SelectedMenu[0] = 1 ;
					}
				}
			}
			if (action.equals("Enter"))
			{
				SelectedMenu[0] = 2 ;
			}
			if (action.equals("Escape"))
			{
				if (SelectedMenu[0] == 1)
				{
					SelectedItem[0] = 0 ;
				}
				SelectedMenu[0] += -1 ;
			}
		}
		DF.DrawOptionsWindow(SelectedItem[0], SelectedMenu[0], Player.ActionKeys, OptionStatus) ;
		return OptionStatus ;
	}
	public void HintsMenu(int[] WinDim, int[] MousePos, Color ClassColor, DrawFunctions DF)
	{
		int NumberOfHints = 15 ;
		int[] Pos = new int[] {(int)(0.19 * WinDim[0]), (int)(0.5 * WinDim[1])} ;
		int L = (int)(0.62 * WinDim[0]), H = (int)(0.2 * WinDim[1]) ;
		int[] OkButtonPos = new int[] {(int) (Pos[0] + 0.7*L), (int) (Pos[1] - 0.125*H)} ;
		float OkButtonL = (float) (0.05*L), OkButtonH = (float) (0.125*H) ;
		if (action.equals(Player.ActionKeys[1]) | action.equals(Player.ActionKeys[3]))
		{
			SelectedWindow[6] = Uts.MenuSelection(Player.ActionKeys[1], Player.ActionKeys[3], action, SelectedWindow[6], NumberOfHints - 1) ;
		}
		if (action.equals("Enter") | (Utg.isInside(MousePos, new int[] {(int) (OkButtonPos[0] - OkButtonL/2), (int) (OkButtonPos[1] + OkButtonH/2)}, (int) (OkButtonL), (int) (OkButtonH)) & action.equals("MouseLeftClick")))
		{
			//return false ;
		}
		DF.DrawHintsMenu(MousePos, SelectedWindow[6], NumberOfHints, ClassColor) ;
	}
	public void ApplyEquipsBonus(int ID, float ActionMult)
	{
		PA.getLife()[1] += EquipsBonus[ID][2]*ActionMult ;
		PA.getMp()[1] += EquipsBonus[ID][3]*ActionMult ;
		BA.getPhyAtk()[1] += EquipsBonus[ID][4]*ActionMult ;
		BA.getMagAtk()[1] += EquipsBonus[ID][5]*ActionMult ;
		BA.getPhyDef()[1] += EquipsBonus[ID][6]*ActionMult ;
		BA.getMagDef()[1] += EquipsBonus[ID][7]*ActionMult ;
		BA.getDex()[1] += EquipsBonus[ID][8]*ActionMult ;
		BA.getAgi()[1] += EquipsBonus[ID][9]*ActionMult ;
		BA.getCrit()[0] += EquipsBonus[ID][10]*ActionMult ;
		BA.getCrit()[2] += EquipsBonus[ID][11]*ActionMult ;
		BA.getStun()[0] += EquipsBonus[ID][12]*ActionMult ;
		BA.getStun()[2] += EquipsBonus[ID][13]*ActionMult ;
		BA.getStun()[4] += EquipsBonus[ID][14]*ActionMult ;
		BA.getBlock()[0] += EquipsBonus[ID][15]*ActionMult ;
		BA.getBlock()[2] += EquipsBonus[ID][16]*ActionMult ;
		BA.getBlock()[4] += EquipsBonus[ID][17]*ActionMult ;
		BA.getBlood()[0] += EquipsBonus[ID][18]*ActionMult ;
		BA.getBlood()[2] += EquipsBonus[ID][19]*ActionMult ;
		BA.getBlood()[4] += EquipsBonus[ID][20]*ActionMult ;
		BA.getBlood()[6] += EquipsBonus[ID][21]*ActionMult ;
		BA.getBlood()[8] += EquipsBonus[ID][22]*ActionMult ;
		BA.getPoison()[0] += EquipsBonus[ID][23]*ActionMult ;
		BA.getPoison()[2] += EquipsBonus[ID][24]*ActionMult ;
		BA.getPoison()[4] += EquipsBonus[ID][25]*ActionMult ;
		BA.getPoison()[6] += EquipsBonus[ID][26]*ActionMult ;
		BA.getPoison()[8] += EquipsBonus[ID][27]*ActionMult ;
		BA.getSilence()[0] += EquipsBonus[ID][28]*ActionMult ;
		BA.getSilence()[2] += EquipsBonus[ID][29]*ActionMult ;
		BA.getSilence()[4] += EquipsBonus[ID][30]*ActionMult ;
	}
	public void Equip(Items[] items, int EquipID)
	{
		int NumberOfEquipTypes = 3 ;	// Sword/Staff/Bow/Claws/Dagger, shield, armor/robe (Archers have bow, bandana, and armor)
		int FirstEquipID = Items.BagIDs[6] ;
		int EquipType = (EquipID + Job) % NumberOfEquipTypes ;
		int CurrentEquip = Equips[EquipType] ;
		if (0 < Bag[EquipID])
		{
			if (0 < CurrentEquip)	// Unnequip the current equip
			{
				if (Uts.SetIsFormed(Equips))	// if the set was formed, remove the 20% bonus
				{
					ApplyEquipsBonus(Equips[0] - FirstEquipID, (float)-0.2) ;
					ApplyEquipsBonus(Equips[1] - FirstEquipID, (float)-0.2) ;
					ApplyEquipsBonus(Equips[2] - FirstEquipID, (float)-0.2) ;
				}
				Equips[EquipType] = 0 ;
				Elem[EquipType + 1] = "n" ;
				ApplyEquipsBonus(CurrentEquip - FirstEquipID, -1) ;
			}
			if (CurrentEquip != EquipID & Equips[(EquipID + Job) % NumberOfEquipTypes] == 0)	// Equip
			{
				Equips[EquipType] = EquipID ;
				Elem[EquipType + 1] = Items.EquipsElem[EquipID - FirstEquipID] ;
				ApplyEquipsBonus(EquipID - FirstEquipID, 1) ;
				if (Uts.SetIsFormed(Equips))	// if the set is formed, add the 20% bonus
				{
					ApplyEquipsBonus(Equips[0] - FirstEquipID, (float)0.2) ;
					ApplyEquipsBonus(Equips[1] - FirstEquipID, (float)0.2) ;
					ApplyEquipsBonus(Equips[2] - FirstEquipID, (float)0.2) ;
				}
			}
		}
		if (Elem[1].equals(Elem[2]) & Elem[2].equals(Elem[3]))	// if the elements of all equips are the same, activate the superelement
		{
			Elem[4] = Elem[1] ;
		}
		else
		{
			Elem[4] = "n" ;
		}
	}
	public void ItemEffect(int ItemID)
	{
		if (ItemID == 1381 | ItemID == 1382 | ItemID == 1384)
		{
			BA.getSpecialStatus()[2] = 0 ;
		}
		if (ItemID == 1411)
		{
			BA.getSpecialStatus()[4] = 0 ;
		}
	}
	public void UseItem(Pet pet, Creatures[] creature, boolean BattleIsOn, int creatureID, int itemID, String[] ArrowElem, Items[] items, Battle B)
	{
		float PotMult = 1 ;
		if (Job == 3)
		{
			PotMult += 0.06 * Spell[7] ;
		}
		if (-1 < itemID)	// if the item is valid
		{
			if (0 < Bag[itemID])	// if the player has the item in the bag
			{
				if (items[itemID].getType().equals("Potion"))	// potions
				{
					float HealPower = Items.PotionsHealing[itemID][1] * PotMult ;
					
					PA.incLife(HealPower * PA.getLife()[1]) ;
					PA.incMP(HealPower * PA.getMp()[1]) ;
					Bag[itemID] += -1 ;			
				}
				else if (items[itemID].getType().equals("Alchemy"))	// alchemy (using herbs heal half of their equivalent pot)
				{
					float HealPower = Items.PotionsHealing[itemID / 3 - Items.BagIDs[0] / 3 + 1][1] / 2 * PotMult ;
					
					PA.incLife(HealPower * PA.getLife()[1]) ;
					if (0 < pet.getLife()[0])
					{
						pet.getPersonalAtt().incLife(HealPower * pet.getLife()[1]) ;
						pet.getPersonalAtt().incMP(HealPower * pet.getMp()[1]) ;
						
						// Animal's skill "natural help": gives 20% bonus on the pet attributes. The enhanced attributes depend on the collectible used
						if (-1 < creatureID & BA.getBattleActions()[0][2] == 1 & BattleIsOn & Job == 3 & (itemID - Items.BagIDs[1] + 1) % 3 < Spell[10])
						{
							int ItemsWithEffectsID = -1 ;
							for (int i = 0 ; i <= Items.ItemsWithEffects.length - 1 ; ++i)
							{
								if (itemID == Items.ItemsWithEffects[i])
								{
									ItemsWithEffectsID = i ;
								}
							}
							if (-1 < ItemsWithEffectsID)
							{
								B.ItemEffectInBattle(BA, pet.getBattleAtt(), creature[creatureID].getBattleAtt(), creature[creatureID].getElem(), creature[creatureID].getLife(), items, ItemsWithEffectsID, Items.ItemsTargets[ItemsWithEffectsID], Items.ItemsElement[ItemsWithEffectsID], Items.ItemsEffects[ItemsWithEffectsID], Items.ItemsBuffs[ItemsWithEffectsID], "activate") ;
							}
						}
					}
					Bag[itemID] += -1 ;
				}
				else if (items[itemID].getType().equals("Pet"))		// pet items
				{
					pet.getPersonalAtt().incLife(Items.PetItems[itemID - Items.BagIDs[3] + 1][1] * pet.getLife()[1]) ;
					pet.getPersonalAtt().incMP(Items.PetItems[itemID - Items.BagIDs[3] + 1][2] * pet.getMp()[1]) ;
					pet.getPersonalAtt().incSatiation(Items.PetItems[itemID - Items.BagIDs[3] + 1][3]) ;
					Bag[itemID] += -1 ;
				}
				else if (items[itemID].getType().equals("Food"))	// food
				{
					PA.incSatiation(Items.FoodSatiation[itemID - Items.BagIDs[4] + 1][3] * PA.getSatiation()[2]) ;
					Bag[itemID] += -1 ;
				}
				else if (items[itemID].getType().equals("Arrow"))	// arrows
				{
					if (0 < Bag[itemID] & Job == 2)
					{
						if (itemID == Items.BagIDs[5] | itemID <= Items.BagIDs[5] + 3 & 1 <= Spell[4] | itemID == Items.BagIDs[5] + 4 & 2 <= Spell[4]  | itemID == Items.BagIDs[5] + 5 & 3 <= Spell[4]  | Items.BagIDs[5] + 6 <= itemID & itemID <= Items.BagIDs[5] + 14 & 4 <= Spell[4]  | Items.BagIDs[5] + 14 <= itemID & 5 <= Spell[4])
						{
							if (0 == Equips[3])
							{
								Equips[3] = itemID ;	// Equipped arrow
								Elem[0] = ArrowElem[itemID - Items.BagIDs[5]] ;
							}
							else
							{
								Equips[3] = 0 ;	// Unequipped arrow
								Elem[0] = "n" ;
							}
						}
					}
				}
				else if (items[itemID].getType().equals("Equip"))	// equipment
				{
					if (isEquippable(itemID))
					{
						Equip(items, itemID) ;
					}
				}
				else if (items[itemID].getType().equals("Item"))	// items
				{
					if (-1 < creatureID & BA.getBattleActions()[0][2] == 1 & BattleIsOn & Job == 4 & 0 < Spell[8])
					{		
						int ItemsWithEffectsID = -1 ;
						for (int i = 0 ; i <= Items.ItemsWithEffects.length - 1 ; ++i)
						{
							if (itemID == Items.ItemsWithEffects[i])
							{
								ItemsWithEffectsID = i ;
							}
						}
						if (-1 < ItemsWithEffectsID)
						{
							B.ItemEffectInBattle(BA, pet.getBattleAtt(), creature[creatureID].getBattleAtt(), creature[creatureID].getElem(), creature[creatureID].getLife(), items, ItemsWithEffectsID, Items.ItemsTargets[ItemsWithEffectsID], Items.ItemsElement[ItemsWithEffectsID], Items.ItemsEffects[ItemsWithEffectsID], Items.ItemsBuffs[ItemsWithEffectsID], "activate") ;
						}
						Bag[itemID] += -1 ;
						BA.getBattleActions()[0][0] = 0 ;
						BA.getBattleActions()[0][2] = 0 ;
					}
					else
					{
						ItemEffect(itemID) ;
						Bag[itemID] += -1 ;
					}
				}
			}			
		}
	}
	public void Bag(Pet pet, Creatures[] creature, int[] WinDim, int[] AllTextCat, String[][] AllText, Items[] items, Battle B, int[] MousePos, DrawFunctions DF)
	{
		int NMenus = 10, NItemsMax = 20 ;
		int[] WindowLimit = new int[NMenus] ;
		int[] ActiveItems = Utg.ArrayWithIndexesGreaterThan(Arrays.copyOfRange(Bag, Items.BagIDs[SelectedMenu[1]], Items.BagIDs[SelectedMenu[1] + 1]), 0) ;
		int[] BagPos = new int[] {(int)(0.35 * WinDim[0]), (int)(0.48 * WinDim[1])} ;
		if (SelectedMenu[1] == 6)	// Equips
		{
			//ActiveItems = Utg.ArrayWithIndexesGreaterThan(Arrays.copyOfRange(Bag, Items.BagIDs[5] + Items.NumberOfItems[6] / 5 * Job, Items.BagIDs[5] + Items.NumberOfItems[6] / 5 * (Job + 1)), 0) ;
		}
		WindowLimit[SelectedMenu[1]] = Math.max(ActiveItems.length - 1, 0) / NItemsMax  ;
		if (WindowLimit[SelectedMenu[1]] < SelectedWindow[1])
		{
			SelectedWindow[1] = 0 ;
		}
		if (ItemsMenu == 0)
		{
			SelectedMenu[1] = Uts.MenuSelection(Player.ActionKeys[0], Player.ActionKeys[2], action, SelectedMenu[1], NMenus - 1) ;
			if (action.equals(Player.ActionKeys[0]) | action.equals(Player.ActionKeys[2]))
			{
				SelectedWindow[1] = 0 ;
			}
			String[] BagMenus = AllText[AllTextCat[30]] ;
			int MenuL = BagMenuImage.getWidth(null) ;
			int MenuH = BagMenuImage.getHeight(null) ;
			for (int m = 0 ; m <= BagMenus.length - 3 ; m += 1)
			{
				if (Utg.isInside(MousePos, new int[] {BagPos[0] - MenuL, BagPos[1] + m * MenuH}, MenuL, MenuH))
				{
					SelectedMenu[1] = m ;
					SelectedWindow[1] = 0 ;
				}
			}
		}
		SelectedWindow[1] = Uts.MenuSelection(Player.ActionKeys[1], Player.ActionKeys[3], action, SelectedWindow[1], WindowLimit[SelectedMenu[1]]) ;
		if (action.equals("Enter") | action.equals("MouseLeftClick"))
		{
			if (ItemsMenu == 0)
			{
				ItemsMenu = 1 ;
				SelectedItem[1] = Items.BagIDs[SelectedMenu[1]] ;
			} 
			else if (ItemsMenu == 1)
			{
				UseItem(pet, creature, isInBattle(), ClosestCreature, SelectedItem[1], Items.ArrowElem, items, B) ;
			}
		}
		else if (action.equals("Escape") | action.equals("MouseRightClick"))
		{
			ItemsMenu = 0 ;
			SelectedItem[1] = 0 ;
		}
		
		/* Select item */
		int L = (int)(0.52 * WinDim[0]), H = (int)(0.4 * WinDim[1]) ;
		int NSlotsmax = 20 ;
		int NumberOfSlots = Math.min(NSlotsmax, ActiveItems.length - NSlotsmax * SelectedWindow[1]) ;
		int slotW = BagSlotImage.getWidth(null) ;
		int slotH = BagSlotImage.getHeight(null) ;
		int[][] SlotCenter = new int[NumberOfSlots][2] ;
		int[][] TextPos = new int[NumberOfSlots][2] ;		
		int cont = 0 ;
		for (int i = 0 ; i <= NumberOfSlots - 1 ; i += 1)
		{
			int ItemID = ActiveItems[i + NSlotsmax * SelectedWindow[1]] ;
			int sx = L / 2, sy = (H - 6 - slotH) / 9 ;
			int[] offset = Utg.OffsetFromPos("CenterLeft", sx, Utg.TextH(13)) ;
			int row = cont % (NSlotsmax / 2), col = cont / (NSlotsmax / 2) ;
			SlotCenter[i] = new int[] {(int) (BagPos[0] + 5 + slotW / 2 + col * sx), (int) (BagPos[1] + 3 + slotH / 2 + row * sy)} ;
			TextPos[i] = new int[] {SlotCenter[i][0] + slotW / 2 + 5, SlotCenter[i][1]} ;
			if (Utg.isInside(MousePos, new int[] {TextPos[i][0] + offset[0], TextPos[i][1] + offset[1]}, sx, Utg.TextH(13)))
			{
				SelectedItem[1] = Items.BagIDs[SelectedMenu[1]] + ItemID ;
				if (SelectedMenu[1] == 6)	// Equips
				{
					//SelectedItem[1] = Items.BagIDs[SelectedMenu[1]] + ItemID + Items.NumberOfItems[6] / 5 * Job ;
				}
			}
			if (0 < Bag[ItemID])
			{
				++cont ;
			}
		}
		DF.DrawBag(Job, BagPos, L, H, Bag, NSlotsmax, items, BagMenuImage, BagSlotImage, ActiveItems, SlotCenter, TextPos, SelectedMenu[1], ItemsMenu, SelectedWindow[1], WindowLimit[SelectedMenu[1]], SelectedItem[1], MousePos) ;
	}
	public void FabBook(Items[] items, int[] MousePos, DrawFunctions DF)
	{
		int[][] Ingredients = Items.CraftingIngredients, Products = Items.CraftingProducts ;
		int NumberOfPages = Ingredients.length - 1 ;
		SelectedWindow[2] = Uts.MenuSelection(Player.ActionKeys[1], Player.ActionKeys[3], action, SelectedWindow[2], NumberOfPages) ;
		DF.DrawFabBook(BookImage, items, SelectedWindow[2], Ingredients, Products, MousePos) ;
	}
	public void Bestiary(CreatureTypes[] creatureTypes, Items[] items, int[] CreaturesDiscovered, int[] MousePos, DrawFunctions DF)
	{
		int windowLimit = 0 ;
		if (CreaturesDiscovered != null)
		{
			windowLimit = Math.max(CreaturesDiscovered.length - 1, 0)/5 ;
		}
		SelectedWindow[8] = Uts.MenuSelection(Player.ActionKeys[1], Player.ActionKeys[3], action, SelectedWindow[8], windowLimit) ;
		DF.DrawBestiary(creatureTypes, CreaturesDiscovered, items, MousePos, SelectedWindow[8]) ;
	}

	
	/* Drawing methods */
	public void DrawStats(String[][] AllText, int[] AllTextCat, int[] Pos, int L, int H, float[] PlayerStats, DrawPrimitives DP)
	{
		Font font = new Font("SansSerif", Font.BOLD, L / 28) ;
		Color[] ColorPalette = Game.ColorPalette ;
		float OverallAngle = DrawPrimitives.OverallAngle ;
		int TextCat = AllTextCat[7] ;
		int[] TextPos = new int[] {(int) (Pos[0] + 5 + 0.05*L), (int) (Pos[1] + 0.05*H)} ;
		for (int i = 0 ; i <= PlayerStats.length - 1 ; i += 1)
		{
			DP.DrawText(TextPos, "BotLeft", OverallAngle, AllText[TextCat][i + 1] + " " + String.valueOf(Utg.Round(PlayerStats[i], 1)), font, ColorPalette[5]) ;
			TextPos[1] += 0.95*H/PlayerStats.length ;
		}
	}
	public void DrawSpecialAttributesWindow(String[][] AllText, int[] AllTextCat, int[] Pos, int L, int H, float[] Stun, float[] Block, float[] Blood, float[] Poison, float[] Silence, DrawPrimitives DP)
	{
		Font font = new Font("SansSerif", Font.BOLD, L / 20) ;
		Color[] ColorPalette = Game.ColorPalette ;
		float OverallAngle = DrawPrimitives.OverallAngle ;
		int SpecialAttrPropCat = AllTextCat[8], AttrCat = AllTextCat[6] ;
		int Linewidth = 2 ;
		float sx = (float)0.15*L, sy = (float)(1.8*Utg.TextH(font.getSize())) ;
		Color TextColor = ColorPalette[5], LineColor = ColorPalette[9] ;
		Color[] AttributeColor = new Color[] {ColorPalette[5], ColorPalette[5], ColorPalette[6], ColorPalette[3], ColorPalette[9]} ;
		Pos[1] += H ;
		DP.DrawLine(new int[] {Pos[0] + (int)(0.025*L), Pos[0] + (int)(0.025*L)}, new int[] {(int)(Pos[1] - H + 0.5*sy), (int)(Pos[1] - H + 8.5*sy)}, Linewidth, LineColor) ;
		DP.DrawLine(new int[] {Pos[0] + (int)(0.375*L), Pos[0] + (int)(0.375*L)}, new int[] {(int)(Pos[1] - H + 0.5*sy), (int)(Pos[1] - H + 3.5*sy)}, Linewidth, LineColor) ;
		DP.DrawLine(new int[] {Pos[0] + (int)(0.975*L), Pos[0] + (int)(0.975*L)}, new int[] {(int)(Pos[1] - H + 0.5*sy), (int)(Pos[1] - H + 8.5*sy)}, Linewidth, LineColor) ;
		DP.DrawLine(new int[] {Pos[0] + (int)(0.025*L), Pos[0] + (int)(0.975*L)}, new int[] {(int)(Pos[1] - H + 0.5*sy), (int)(Pos[1] - H + 0.5*sy)}, Linewidth, LineColor) ;
		DP.DrawLine(new int[] {Pos[0] + (int)(0.025*L), Pos[0] + (int)(0.975*L)}, new int[] {(int)(Pos[1] - H + 8.5*sy), (int)(Pos[1] - H + 8.5*sy)}, Linewidth, LineColor) ;
		DP.DrawLine(new int[] {Pos[0] + (int)(0.025*L), Pos[0] + (int)(0.975*L)}, new int[] {(int)(Pos[1] - H + 1.5*sy), (int)(Pos[1] - H + 1.5*sy)}, Linewidth, LineColor) ;
		DP.DrawLine(new int[] {Pos[0] + (int)(0.025*L), Pos[0] + (int)(0.975*L)}, new int[] {(int)(Pos[1] - H + 2.5*sy), (int)(Pos[1] - H + 2.5*sy)}, Linewidth, LineColor) ;
		DP.DrawLine(new int[] {Pos[0] + (int)(0.375*L + 2*sx), Pos[0] + (int)(0.375*L + 2*sx)}, new int[] {(int)(Pos[1] - H + 1.5*sy), (int)(Pos[1] - H + 2.5*sy)}, Linewidth, LineColor) ;
		DP.DrawText(new int[] {Pos[0] + (int)(0.65*L), (int)(Pos[1] - H + sy)}, "Center", OverallAngle, AllText[SpecialAttrPropCat][1], font, TextColor) ;	
		DP.DrawText(new int[] {Pos[0] + (int)(0.375*L + sx), (int)(Pos[1] - H + 2*sy)}, "Center", OverallAngle, AllText[SpecialAttrPropCat][2], font, TextColor) ;	
		DP.DrawText(new int[] {Pos[0] + (int)(0.375*L + 3*sx), (int)(Pos[1] - H + 2*sy)}, "Center", OverallAngle, AllText[SpecialAttrPropCat][3], font, TextColor) ;
		DP.DrawText(new int[] {Pos[0] + (int)(0.45*L), (int)(Pos[1] - H + 3*sy)}, "Center", OverallAngle, AllText[SpecialAttrPropCat][4], font, TextColor) ;	
		DP.DrawText(new int[] {Pos[0] + (int)(0.45*L + sx), (int)(Pos[1] - H + 3*sy)}, "Center", OverallAngle, AllText[SpecialAttrPropCat][5], font, TextColor) ;	
		DP.DrawText(new int[] {Pos[0] + (int)(0.45*L + 2*sx), (int)(Pos[1] - H + 3*sy)}, "Center", OverallAngle, AllText[SpecialAttrPropCat][4], font, TextColor) ;	
		DP.DrawText(new int[] {Pos[0] + (int)(0.45*L + 3*sx), (int)(Pos[1] - H + 3*sy)}, "Center", OverallAngle, AllText[SpecialAttrPropCat][5], font, TextColor) ;	
		for (int i = 0 ; i <= 4 ; ++i)
		{
			DP.DrawLine(new int[] {Pos[0] + (int)(0.025*L), Pos[0] + (int)(0.975*L)}, new int[] {(int)(Pos[1] - H + (i + 3.5)*sy), (int)(Pos[1] - H + (i + 3.5)*sy)}, Linewidth, LineColor) ;
			DP.DrawText(new int[] {Pos[0] + (int)(0.2*L), (int)(Pos[1] - H + (i + 4)*sy)}, "Center", OverallAngle, AllText[AttrCat][i + 11], font, AttributeColor[i]) ;	
		}
		for (int i = 0 ; i <= 3 ; ++i)
		{
			DP.DrawLine(new int[] {Pos[0] + (int)(0.375*L + i*sx), Pos[0] + (int)(0.375*L + i*sx)}, new int[] {(int)(Pos[1] - H + 2*sy + sy/2), (int)(Pos[1] - H + 8*sy + sy/2)}, Linewidth, LineColor) ;
			DP.DrawText(new int[] {Pos[0] + (int)(0.45*L + i*sx), (int)(Pos[1] - H + 4*sy)}, "Center", OverallAngle, String.valueOf(Utg.Round(Stun[i], 2)), font, AttributeColor[0]) ;	
			DP.DrawText(new int[] {Pos[0] + (int)(0.45*L + i*sx), (int)(Pos[1] - H + 5*sy)}, "Center", OverallAngle, String.valueOf(Utg.Round(Block[i], 2)), font, AttributeColor[1]) ;	
			DP.DrawText(new int[] {Pos[0] + (int)(0.45*L + i*sx), (int)(Pos[1] - H + 6*sy)}, "Center", OverallAngle, String.valueOf(Utg.Round(Blood[i], 2)), font, AttributeColor[2]) ;	
			DP.DrawText(new int[] {Pos[0] + (int)(0.45*L + i*sx), (int)(Pos[1] - H + 7*sy)}, "Center", OverallAngle, String.valueOf(Utg.Round(Poison[i], 2)), font, AttributeColor[3]) ;	
			DP.DrawText(new int[] {Pos[0] + (int)(0.45*L + i*sx), (int)(Pos[1] - H + 8*sy)}, "Center", OverallAngle, String.valueOf(Utg.Round(Silence[i], 2)), font, AttributeColor[4]) ;				
		}
		
		DP.DrawLine(new int[] {Pos[0] + (int)(0.025*L), Pos[0] + (int)(0.025*L)}, new int[] {(int)(Pos[1] - H + 9.5*sy), (int)(Pos[1] - H + 14.5*sy)}, Linewidth, LineColor) ;
		DP.DrawLine(new int[] {Pos[0] + (int)(0.375*L), Pos[0] + (int)(0.375*L)}, new int[] {(int)(Pos[1] - H + 9.5*sy), (int)(Pos[1] - H + 12.5*sy)}, Linewidth, LineColor) ;
		DP.DrawLine(new int[] {Pos[0] + (int)(0.975*L), Pos[0] + (int)(0.975*L)}, new int[] {(int)(Pos[1] - H + 9.5*sy), (int)(Pos[1] - H + 14.5*sy)}, Linewidth, LineColor) ;
		DP.DrawLine(new int[] {Pos[0] + (int)(0.025*L), Pos[0] + (int)(0.975*L)}, new int[] {(int)(Pos[1] - H + 9.5*sy), (int)(Pos[1] - H + 9.5*sy)}, Linewidth, LineColor) ;
		DP.DrawLine(new int[] {Pos[0] + (int)(0.025*L), Pos[0] + (int)(0.975*L)}, new int[] {(int)(Pos[1] - H + 14.5*sy), (int)(Pos[1] - H + 14.5*sy)}, Linewidth, LineColor) ;
		DP.DrawLine(new int[] {Pos[0] + (int)(0.025*L), Pos[0] + (int)(0.975*L)}, new int[] {(int)(Pos[1] - H + 10.5*sy), (int)(Pos[1] - H + 10.5*sy)}, Linewidth, LineColor) ;
		DP.DrawLine(new int[] {Pos[0] + (int)(0.025*L), Pos[0] + (int)(0.975*L)}, new int[] {(int)(Pos[1] - H + 11.5*sy), (int)(Pos[1] - H + 11.5*sy)}, Linewidth, LineColor) ;
		DP.DrawLine(new int[] {Pos[0] + (int)(0.375*L + 2*sx), Pos[0] + (int)(0.375*L + 2*sx)}, new int[] {(int)(Pos[1] - H + 10.5*sy), (int)(Pos[1] - H + 12.5*sy)}, Linewidth, LineColor) ;
		DP.DrawText(new int[] {Pos[0] + (int)(0.65*L), (int)(Pos[1] - H + 10*sy)}, "Center", OverallAngle, AllText[SpecialAttrPropCat][6], font, TextColor) ;	
		DP.DrawText(new int[] {Pos[0] + (int)(0.375*L + sx), (int)(Pos[1] - H + 11*sy)}, "Center", OverallAngle, AllText[SpecialAttrPropCat][2], font, TextColor) ;	
		DP.DrawText(new int[] {Pos[0] + (int)(0.375*L + 3*sx), (int)(Pos[1] - H + 11*sy)}, "Center", OverallAngle, AllText[SpecialAttrPropCat][3], font, TextColor) ;
		DP.DrawText(new int[] {Pos[0] + (int)(0.45*L), (int)(Pos[1] - H + 12*sy)}, "Center", OverallAngle, AllText[SpecialAttrPropCat][4], font, TextColor) ;	
		DP.DrawText(new int[] {Pos[0] + (int)(0.45*L + sx), (int)(Pos[1] - H + 12*sy)}, "Center", OverallAngle, AllText[SpecialAttrPropCat][5], font, TextColor) ;	
		DP.DrawText(new int[] {Pos[0] + (int)(0.45*L + 2*sx), (int)(Pos[1] - H + 12*sy)}, "Center", OverallAngle, AllText[SpecialAttrPropCat][4], font, TextColor) ;	
		DP.DrawText(new int[] {Pos[0] + (int)(0.45*L + 3*sx), (int)(Pos[1] - H + 12*sy)}, "Center", OverallAngle, AllText[SpecialAttrPropCat][5], font, TextColor) ;	
		for (int i = 0 ; i <= 1 ; ++i)
		{
			DP.DrawLine(new int[] {Pos[0] + (int)(0.025*L), Pos[0] + (int)(0.975*L)}, new int[] {(int)(Pos[1] - H + (i + 12.5)*sy), (int)(Pos[1] - H + (i + 12.5)*sy)}, Linewidth, LineColor) ;
			DP.DrawText(new int[] {Pos[0] + (int)(0.2*L), (int)(Pos[1] - H + (i + 13)*sy)}, "Center", OverallAngle, AllText[AttrCat][i + 13], font, AttributeColor[i + 2]) ;	
		}
		for (int i = 0 ; i <= 3 ; ++i)
		{
			DP.DrawLine(new int[] {Pos[0] + (int)(0.375*L + i*sx), Pos[0] + (int)(0.375*L + i*sx)}, new int[] {(int)(Pos[1] - H + 11.5*sy), (int)(Pos[1] - H + 14.5*sy)}, Linewidth, LineColor) ;
			DP.DrawText(new int[] {Pos[0] + (int)(0.45*L + i*sx), (int)(Pos[1] - H + 13*sy)}, "Center", OverallAngle, String.valueOf(Utg.Round(Blood[i + 4], 2)), font, AttributeColor[2]) ;	
			DP.DrawText(new int[] {Pos[0] + (int)(0.45*L + i*sx), (int)(Pos[1] - H + 14*sy)}, "Center", OverallAngle, String.valueOf(Utg.Round(Poison[i + 4], 2)), font, AttributeColor[3]) ;	
		}
		
		DP.DrawText(new int[] {Pos[0] + (int)(0.025*L), (int)(Pos[1] - H + 16*sy)}, "BotLeft", OverallAngle, AllText[AttrCat][11] + " " + AllText[SpecialAttrPropCat][7] + " = " + Utg.Round(Stun[4], 2), font, AttributeColor[0]) ;	
		DP.DrawText(new int[] {Pos[0] + (int)(0.025*L), (int)(Pos[1] - H + 17*sy)}, "BotLeft", OverallAngle, AllText[AttrCat][12] + " " + AllText[SpecialAttrPropCat][7] + " = " + Utg.Round(Block[4], 2), font, AttributeColor[1]) ;	
		DP.DrawText(new int[] {Pos[0] + (int)(0.025*L), (int)(Pos[1] - H + 18*sy)}, "BotLeft", OverallAngle, AllText[AttrCat][13] + " " + AllText[SpecialAttrPropCat][7] + " = " + Utg.Round(Blood[8], 2), font, AttributeColor[2]) ;	
		DP.DrawText(new int[] {Pos[0] + (int)(0.025*L), (int)(Pos[1] - H + 19*sy)}, "BotLeft", OverallAngle, AllText[AttrCat][14] + " " + AllText[SpecialAttrPropCat][7] + " = " + Utg.Round(Poison[8], 2), font, AttributeColor[3]) ;	
		DP.DrawText(new int[] {Pos[0] + (int)(0.025*L), (int)(Pos[1] - H + 20*sy)}, "BotLeft", OverallAngle, AllText[AttrCat][15] + " " + AllText[SpecialAttrPropCat][7] + " = " + Utg.Round(Silence[4], 2), font, AttributeColor[4]) ;				
	}
	public void DrawRange(DrawPrimitives DP)
	{
		DP.DrawCircle(PA.getPos(), (int)(2 * PA.getRange()), 2, Game.ColorPalette[Job], false, true) ;
	}
	public void DrawEquips(int[] Pos, int Job, int equiptype, int EquipID, float[][] EquipsBonus, float[] scale, float angle, DrawPrimitives DP)
	{
		int bonus = 0 ;
		if (EquipsBonus[EquipID][1] == 10)
		{
			bonus = 8 ;
		}
		if (equiptype == 0)
		{
			DP.DrawImage(Items.EquipImage[Job + bonus], Pos, angle, scale, new boolean[] {false, false}, "Center", 1) ;
		}
		if (equiptype == 1)
		{
			DP.DrawImage(Items.EquipImage[5 + bonus], Pos, angle, scale, new boolean[] {false, false}, "Center", 1) ;
		}
		if (equiptype == 2)
		{
			DP.DrawImage(Items.EquipImage[6 + bonus], Pos, angle, scale, new boolean[] {false, false}, "Center", 1) ;
		}
		if (equiptype == 3)
		{
			DP.DrawImage(Items.EquipImage[7], Pos, angle, scale, new boolean[] {false, false}, "Center", 1) ;
		}
	}
	public void DrawPlayerEquips(int[] Pos, float[] playerscale, DrawPrimitives DP)
	{
		float[] scale = new float[] {(float) 0.6, (float) 0.6} ;
		float[] angle = new float[] {50, 30, 0, 0, 0} ;
		int[] EqPos = new int[] {(int)(Pos[0] + 0.16 * PA.getSize()[0] * playerscale[0]), (int)(Pos[1] - 0.4 * PA.getSize()[1] * playerscale[1])} ;
		if (0 < Equips[0])
		{
			DrawEquips(EqPos, Job, 0, Equips[0] - Items.BagIDs[5], Items.EquipsBonus, scale, angle[Job], DP) ;
		}	
	}
	public void DrawPlayer(int[] PlayerPos, float[] scale, String dir, boolean ShowPlayerRange, DrawPrimitives DP)
	{
		// BP = Body part
		// 0,0 on shoes tip
		// 0: Legs, 1: Shoes, 2: Shirt, 3: Arms, 4: Head, 5: Eyes, 6: Hair

		float OverallAngle = DrawPrimitives.OverallAngle ;
		if (IsRiding())	// If the player is mounted
		{
			DP.DrawImage(RidingImage, new int[] {PA.getPos()[0] - RidingImage.getWidth(null)/2, PA.getPos()[1] + RidingImage.getHeight(null)/2}, OverallAngle, new float[] {1, 1}, new boolean[] {false, false}, "BotLeft", 1) ;
		}
		//Image[] PlayerImages = new Image[] {PA.getimage()} ;
		//float[][] BPScale = new float[PA.getimage().length][2] ;
		boolean[] mirror = null ;
		if (dir.equals("Acima"))
		{
			mirror = new boolean[] {false, false} ;
			DP.DrawImage(PA.getimage()[0], new int[] {PlayerPos[0], (int) (PlayerPos[1] - 0.5*scale[1] * PA.getSize()[1])}, OverallAngle, new float[] {1, 1}, mirror, "Center", 1) ;
		}
		if (dir.equals("Abaixo"))
		{
			mirror = new boolean[] {false, false} ;
			DP.DrawImage(PA.getimage()[1], new int[] {PlayerPos[0], (int) (PlayerPos[1] - 0.5*scale[1] * PA.getSize()[1])}, OverallAngle, new float[] {1, 1}, mirror, "Center", 1) ;
		}
		if (dir.equals("Esquerda"))
		{
			mirror = new boolean[] {false, false} ;
			DP.DrawImage(PA.getimage()[2], new int[] {PlayerPos[0], (int) (PlayerPos[1] - 0.5*scale[1] * PA.getSize()[1])}, OverallAngle, new float[] {1, 1}, mirror, "Center", 1) ;
		}
		if (dir.equals("Direita"))
		{
			mirror = new boolean[] {false, false} ;
			if (countmove % 2 == 0)
			{
				DP.DrawImage(PA.getimage()[3], new int[] {PlayerPos[0], (int) (PlayerPos[1] - 0.5*scale[1] * PA.getSize()[1])}, OverallAngle, new float[] {1, 1}, mirror, "Center", 1) ;
			}
			else
			{
				DP.DrawImage(PA.getimage()[7], new int[] {PlayerPos[0], (int) (PlayerPos[1] - 0.5*scale[1] * PA.getSize()[1])}, OverallAngle, new float[] {1, 1}, mirror, "Center", 1) ;
			}
		}
		if (ShowPlayerRange)
		{
			DrawRange(DP) ;
		}
		/*for (int i = 0 ; i <= PlayerImages.length - 1 ; i += 1)
		{
			BPScale[i][0] = (float)0.01*scale[0]*BodyPartsSizes[i][0] ;
			BPScale[i][1] = (float)0.01*scale[1]*BodyPartsSizes[i][1] ;
		}*/
		/*for (int i = 0 ; i <= PlayerImages.length - 1 ; i += 1)						
		{
			DP.DrawImage(PlayerImages[i], new int[] {PlayerPos[0], (int) (PlayerPos[1] - 0.5*scale[1] * PA.getSize()[1])}, OverallAngle, BPScale[i], mirror, "Center") ;
		}*/
		if (QuestSkills[8])
		{
			DP.DrawImage(DragonAuraImage, new int[] {PlayerPos[0], (int) (PlayerPos[1] - 0.5*scale[1] * PA.getSize()[1])}, OverallAngle, new float[] {1, 1}, mirror, "Center", 0.5) ;					
		}
		//DP.DrawRect(player.getPos(), "Center", (int)(player.getSize()[0]*scale[0]), (int)(player.getSize()[1]*scale[1]), 1, null, ColorPalette[9], true) ;	// Player contour
		//DrawCircle(player.getPos(), 2, 2, ColorPalette[6], false, true) ;	// Player center
	}
	public void DrawAttWindow(int[] MainWinDim, int[] WinPos, int[] MousePos, String[][] AllText, int[] AllTextCat, int Tab, Image GoldCoinImage, Items[] items, Icon[] icons, DrawPrimitives DP)
	{
		// Player player, Items[] items, Icon[] icons, int[] Pos, int Tab, float[] PlayerStats, int[] MousePos, Image GoldCoinImage, Image DragonAuraImage
		float TextAngle = DrawPrimitives.OverallAngle ;
		int L = Player.AttWindow[0].getWidth(null), H = Player.AttWindow[0].getHeight(null) ;
		int[] PlayerImagePos = new int[] {WinPos[0] + (int) (0.55 * L), WinPos[1] - (int) (0.85 * H)} ;
		int ClassesCat = -1, ProClassesCat = -1, AttCat = -1, CollectCat = -1, EquipsCat = -1, TabsCat = -1 ;
		Font Namefont = new Font("GothicE", Font.BOLD, L / 28 + 1) ;
		Font font = new Font("GothicE", Font.BOLD, L / 28 + 2) ;
		Font Equipfont = new Font("GothicE", Font.BOLD, L / 28) ;
		Color[] ColorPalette = Game.ColorPalette ;
		Color[] TabColor = new Color[] {ColorPalette[7], ColorPalette[7], ColorPalette[7]}, TabTextColor = new Color[] {ColorPalette[5], ColorPalette[5], ColorPalette[5]} ;
		Color TextColor = ColorPalette[2] ;
		int TextH = Utg.TextH(font.getSize()) ;
		if (AllTextCat != null)
		{
			ClassesCat = AllTextCat[4] ;
			ProClassesCat = AllTextCat[5] ;
			AttCat = AllTextCat[6] ;
			CollectCat = AllTextCat[9] ;
			EquipsCat = AllTextCat[11] ;
			TabsCat = AllTextCat[40] ;
		}
		TabColor[Tab] = ColorPalette[19] ;
		TabTextColor[Tab] = ColorPalette[3] ;
		
		// Main window
		DP.DrawImage(Player.AttWindow[Tab], WinPos, "TopLeft") ;
		DP.DrawText(new int[] {WinPos[0] + 5, WinPos[1] + (int)(0.05*H)}, "Center", 90, AllText[TabsCat][1], Namefont, TabTextColor[0]) ;				// Tab 0 text	
		DP.DrawText(new int[] {WinPos[0] + 5, WinPos[1] + (int)(0.15*H)}, "Center", 90, AllText[TabsCat][2], Namefont, TabTextColor[1]) ;				// Tab 1 text	
		DP.DrawText(new int[] {WinPos[0] + 5, WinPos[1] + (int)(0.25*H)}, "Center", 90, AllText[TabsCat][3], Namefont, TabTextColor[2]) ;				// Tab 2 text	
		if (Tab == 0)
		{
			//	Player
			DrawPlayer(PlayerImagePos, new float[] {(float) 1.8, (float) 1.8}, PA.getDir(), false, DP) ;
			DP.DrawText(new int[] {WinPos[0] + (int)(0.5*L), WinPos[1] + (int)(0.1*H)}, "Center", TextAngle, PA.getName(), Namefont, TextColor) ;						// Name text			
			DP.DrawText(new int[] {WinPos[0] + (int)(0.5*L), WinPos[1] + (int)(0.03*H)}, "Center", TextAngle, AllText[AttCat][1] + ": " + PA.getLevel(), font, ColorPalette[6]) ;	// Level text		
			if(ProJob == 0)
			{
				DP.DrawText(new int[] {WinPos[0] + (int)(0.5*L), WinPos[1] + (int)(0.06*H)}, "Center", TextAngle, AllText[ClassesCat][Job + 1], font, ColorPalette[5]) ;	// Job text			
			}
			else
			{
				DP.DrawText(new int[] {WinPos[0] + (int)(0.5*L), WinPos[1] + (int)(0.15*H + TextH/2)}, "Center", TextAngle, AllText[ProClassesCat][ProJob + 2*Job], font, ColorPalette[5]) ;	// Pro job text					
			}
			
			//	Equips
			int[] EqRectL = new int[] {51, 51, 51, 8} ;
			int[] EqRectH = new int[] {51, 51, 51, 24} ;
			int[][] EqRectPos = new int[][] {{WinPos[0] + 62, WinPos[1] + 78}, {WinPos[0] + 200, WinPos[1] + 43}, {WinPos[0] + 200, WinPos[1] + 96}, {WinPos[0] + 53, WinPos[1] + 90}} ;	// Weapon, armor, shield, arrow
			for (int eq = 0 ; eq <= 4 - 1 ; eq += 1)
			{
				Image ElemImage = DrawFunctions.ElementImages[Uts.ElementID(Elem[eq + 1])] ;
				if (0 < Equips[eq])
				{
					if (eq <= 2)
					{
						if (0 < EquipsBonus[Equips[eq] - Items.BagIDs[6]][0])
						{
							if (-1 < EquipsCat)
							{
								DP.DrawText(new int[] {EqRectPos[eq][0], EqRectPos[eq][1] - EqRectH[eq] / 2 - TextH}, "Center", TextAngle, AllText[EquipsCat][eq + 1] + " + " + (int)(EquipsBonus[Equips[eq] - Items.BagIDs[6]][1]), font, TextColor) ;					
							}
						}
						DrawEquips(EqRectPos[eq], Job, eq, Equips[eq] - Items.BagIDs[6], EquipsBonus, new float[] {1, 1}, TextAngle, DP) ;
					}
					else if (eq == 3)
					{
						DrawEquips(EqRectPos[eq], Job, eq, Equips[0] - Items.BagIDs[6], EquipsBonus, new float[] {1, 1}, TextAngle, DP) ;
					}
					DP.DrawImage(ElemImage, new int[] {(int) (EqRectPos[eq][0] + 0.3*EqRectL[eq]), (int) (EqRectPos[eq][1] + 0.3*EqRectH[eq])}, TextAngle, new float[] {(float) 0.12, (float) 0.12}, new boolean[] {false, false}, "Center", 1) ;					
					DP.DrawTextUntil(new int[] {EqRectPos[eq][0] - EqRectL[eq] / 2, EqRectPos[eq][1] + EqRectH[eq] / 2 + TextH}, "BotLeft", TextAngle, items[Equips[eq]].getName(), Equipfont, TextColor, 14, MousePos) ;	// Equip text	
				}
				else
				{
					DP.DrawText(new int[] {EqRectPos[eq][0] + EqRectL[eq]/2, EqRectPos[eq][1] - EqRectH[eq] / 2 - TextH}, "Center", TextAngle, AllText[EquipsCat][eq + 1], font, TextColor) ;
				}
			}
			
			// Super element
			if (Elem[1].equals(Elem[2]) & Elem[2].equals(Elem[3]))
			{
				DP.DrawImage(DrawFunctions.ElementImages[Uts.ElementID(Elem[4])], new int[] {WinPos[0] + (int)(0.5*L), WinPos[1] + (int)(0.45*H)}, TextAngle, new float[] {(float) 0.3, (float) 0.3}, new boolean[] {false, false}, "Center", 1) ;
			}
			
			//	Attributes
			float[] Attributes = new float[] {BA.TotalPhyAtk(), BA.TotalMagAtk(), BA.TotalPhyDef(), BA.TotalMagDef(), BA.TotalDex(), BA.TotalAgi()} ;
			int AttSy = 22 ;
			DP.DrawText(new int[] {WinPos[0] + 15, WinPos[1] + 30}, "BotLeft", TextAngle, AllText[AttCat][2] + ": " + Utg.Round(PA.getLife()[0], 1), font, ColorPalette[6]) ;	// Life text	
			DP.DrawText(new int[] {WinPos[0] + 15, WinPos[1] + 40}, "BotLeft", TextAngle, AllText[AttCat][3] + ": " + Utg.Round(PA.getMp()[0], 1), font, ColorPalette[5]) ;		// MP text

			DP.DrawImage(Items.EquipImage[0], new int[] {WinPos[0] + 30, WinPos[1] + 134 + 1 * AttSy}, new float[] {(float) (11 / 38.0), (float) (11 / 38.0)}, "Center") ;	// Draw sword icon
			for (int i = 0; i <= Attributes.length - 1; i += 1)
			{
				DP.DrawText(new int[] {WinPos[0] + 45, WinPos[1] + 136 + (i + 1) * AttSy}, "BotLeft", TextAngle, AllText[AttCat][4] + ": " + Utg.Round(Attributes[i], 1), font, TextColor) ;
			}	
			DP.DrawText(new int[] {WinPos[0] + 45, WinPos[1] + 136 + 7 * AttSy}, "BotLeft", TextAngle, AllText[AttCat][10] + ": " + Utg.Round(100 * BA.TotalCritAtkChance(), 1) + "%", font, ColorPalette[6]) ;		
			
			//	Collection
			if (Language.equals("P"))
			{	
				DP.DrawText(new int[] {WinPos[0] + (int)(0.95*L), WinPos[1] + (int)(0.895*H)}, "TopRight", TextAngle, AllText[AttCat][17] + " de " + AllText[CollectCat][3] + " = " + Utg.Round(Collect[0], 1), font, DrawFunctions.MapsTypeColor[13]) ;		
				DP.DrawText(new int[] {WinPos[0] + (int)(0.95*L), WinPos[1] + (int)(0.945*H)}, "TopRight", TextAngle, AllText[AttCat][17] + " de " + AllText[CollectCat][4] + " = " + Utg.Round(Collect[1], 1), font, DrawFunctions.MapsTypeColor[14]) ;		
				DP.DrawText(new int[] {WinPos[0] + (int)(0.95*L), WinPos[1] + (int)(0.975*H)}, "TopRight", TextAngle, AllText[AttCat][17] + " de " + AllText[CollectCat][5] + " = " + Utg.Round(Collect[2], 1), font, DrawFunctions.MapsTypeColor[15]) ;
			}
			else if (Language.equals("E"))
			{
				DP.DrawText(new int[] {WinPos[0] + (int)(0.95*L), WinPos[1] + (int)(0.895*H)}, "TopRight", TextAngle, AllText[CollectCat][3] + " " + AllText[AttCat][17] + " = " + Utg.Round(Collect[0], 1), font, DrawFunctions.MapsTypeColor[13]) ;		
				DP.DrawText(new int[] {WinPos[0] + (int)(0.95*L), WinPos[1] + (int)(0.945*H)}, "TopRight", TextAngle, AllText[CollectCat][4] + " " + AllText[AttCat][17] + " = " + Utg.Round(Collect[1], 1), font, DrawFunctions.MapsTypeColor[14]) ;		
				DP.DrawText(new int[] {WinPos[0] + (int)(0.95*L), WinPos[1] + (int)(0.975*H)}, "TopRight", TextAngle, AllText[CollectCat][5] + " " + AllText[AttCat][17] + " = " + Utg.Round(Collect[2], 1), font, DrawFunctions.MapsTypeColor[15]) ;
			}
			
			//	Gold
			DP.DrawImage(GoldCoinImage, new int[] {WinPos[0] + (int)(0.04*L), WinPos[1] + (int)(0.975*H)}, TextAngle, new float[] {(float) 1.2, (float) 1.2}, new boolean[] {false, false}, "BotLeft", 1) ;
			DP.DrawText(new int[] {WinPos[0] + (int)(0.18*L), WinPos[1] + (int)(0.96*H) - TextH/2}, "BotLeft", TextAngle, String.valueOf(Utg.Round(Gold[0], 1)), font, ColorPalette[18]) ;	// Gold text
		
			//	Plus sign
			if (0 < AttPoints)
			{
				for (int i = 9 ; i <= 17 - 1 ; i += 1)
				{
					icons[i].DrawImage(TextAngle, 0, MousePos, DP) ;
				}
			}
		}
		else if (Tab == 1)
		{
			DrawSpecialAttributesWindow(AllText, AllTextCat, WinPos, L, H, BA.getStun(), BA.getBlock(), BA.getBlood(), BA.getPoison(), BA.getSilence(), DP) ;
		}
		else if (Tab == 2)
		{
			DrawStats(AllText, AllTextCat, WinPos, L, H, Stats, DP) ;
		}
	}

	
	/* Save and load methods */
	public void Save(String fileName, Pet pet)
	{
		try
		{	
			FileWriter fileWriter = new FileWriter(fileName) ;
			BufferedWriter bw = new BufferedWriter(fileWriter) ; 
			bw.write("Save version: 3.41 \n" + getName()) ;
			bw.write("\nPlayer name: \n" + getName()) ;
			bw.write("\nPlayer language: \n" + getLanguage()) ;
			bw.write("\nPlayer sex: \n" + getSex()) ;
			bw.write("\nPlayer size: \n" + Arrays.toString(getSize())) ;
			bw.write("\nPlayer colors: \n" + Arrays.toString(getColors())) ;
			bw.write("\nPlayer job: \n" + getJob()) ;
			bw.write("\nPlayer proJob: \n" + getProJob()) ;
			bw.write("\nPlayer continent: \n" + getContinent()) ;
			bw.write("\nPlayer map: \n" + getMap()) ;
			bw.write("\nPlayer pos: \n" + Arrays.toString(getPos())) ;
			bw.write("\nPlayer skill: \n" + Arrays.toString(getSpell())) ;
			bw.write("\nPlayer quest: \n" + Arrays.toString(getQuest())) ;
			bw.write("\nPlayer bag: \n" + Arrays.toString(getBag())) ;
			bw.write("\nPlayer equips: \n" + Arrays.toString(getEquips())) ;
			bw.write("\nPlayer skillPoints: \n" + getSkillPoints()) ;
			bw.write("\nPlayer life: \n" + Arrays.toString(getLife())) ;
			bw.write("\nPlayer mp: \n" + Arrays.toString(getMp())) ;
			bw.write("\nPlayer range: \n" + getRange()) ;
			bw.write("\nPlayer phyAtk: \n" + Arrays.toString(getPhyAtk())) ;
			bw.write("\nPlayer magAtk: \n" + Arrays.toString(getMagAtk())) ;
			bw.write("\nPlayer phyDef: \n" + Arrays.toString(getPhyDef())) ;
			bw.write("\nPlayer magDef: \n" + Arrays.toString(getMagDef())) ;
			bw.write("\nPlayer dex: \n" + Arrays.toString(getDex())) ;
			bw.write("\nPlayer agi: \n" + Arrays.toString(getAgi())) ;
			bw.write("\nPlayer crit: \n" + Arrays.toString(getCrit())) ;
			bw.write("\nPlayer stun: \n" + Arrays.toString(getStun())) ;
			bw.write("\nPlayer block: \n" + Arrays.toString(getBlock())) ;
			bw.write("\nPlayer blood: \n" + Arrays.toString(getBlood())) ;
			bw.write("\nPlayer poison: \n" + Arrays.toString(getPoison())) ;
			bw.write("\nPlayer silence: \n" + Arrays.toString(getSilence())) ;
			bw.write("\nPlayer elem: \n" + Arrays.toString(getElem())) ;
			bw.write("\nPlayer elem mult: \n" + Arrays.toString(getElemMult())) ;
			bw.write("\nPlayer collect: \n" + Arrays.toString(getCollect())) ;
			bw.write("\nPlayer level: \n" + getLevel()) ;
			bw.write("\nPlayer gold: \n" + Arrays.toString(getGold())) ;
			bw.write("\nPlayer step: \n" + getStep()) ;
			bw.write("\nPlayer exp: \n" + Arrays.toString(getExp())) ;
			bw.write("\nPlayer satiation: \n" + Arrays.toString(getSatiation())) ;
			bw.write("\nPlayer quest skills: \n" + Arrays.toString(QuestSkills)) ;
			bw.write("\nPlayer status: \n" + Arrays.toString(getBattleAtt().getSpecialStatus())) ; 
			bw.write("\nPlayer actions: \n" + Arrays.deepToString(getActions())) ; 
			bw.write("\nPlayer battle actions: \n" + Arrays.deepToString(getBattleAtt().getBattleActions())) ; 
			bw.write("\nPlayer status counter: \n" + Arrays.toString(getStatusCounter())) ; 		
			bw.write("\nPlayer stats: \n" + Arrays.toString(getStats())) ;
			bw.write("\nPlayer available attribute points: \n" + getAttPoints()) ;
			bw.write("\nPlayer attribute increase: \n" + Arrays.deepToString(getAttIncrease())) ;
			bw.write("\nPlayer chance increase: \n" + Arrays.deepToString(getChanceIncrease())) ;
			bw.write("\nPlayer creatures discovered: \n" + Arrays.toString(getCreaturesDiscovered())) ;
			pet.Save(bw) ;	
			
			bw.write("\nEquips bonus: \n" + Arrays.deepToString(Items.EquipsBonus)) ;
			//bufferedWriter.write("\nNPCs contact: \n" + Arrays.toString(FirstNPCContact)) ;
			bw.write("\nDifficult level: \n" + DifficultLevel) ;
			bw.close() ;
		}		
		catch(IOException ex) 
		{
            System.out.println("Error writing to file '" + fileName + "'") ;
        }
	}
	public void Load(String FileName, Pet pet, Maps[] maps)
	{
		int Nrows = 1600, Ncolumns = 50 ;
		String[][] ReadFile = Utg.ReadTextFile(FileName, Nrows, Ncolumns) ;
		if (ReadFile[2*0][0].equals("3.41"))	// Save version
		{
			PA.setName(ReadFile[2*1][0]) ;
			Language = ReadFile[2*2][0] ;
			Sex = ReadFile[2*3][0] ;
			setSize((int[]) Utg.ConvertArray(Utg.toString(ReadFile[2*4]), "String", "int")) ;
			color = Utg.toColor(ReadFile[2*6]) ;
			Job = Integer.parseInt(ReadFile[2*7][0]) ;
			setProJob(Integer.parseInt(ReadFile[2*8][0])) ;
			setMap(Integer.parseInt(ReadFile[2*10][0]), maps) ;
			setPos((int[]) Utg.ConvertArray(Utg.toString(ReadFile[2*11]), "String", "int")) ;
			Spell = (int[]) Utg.ConvertArray(Utg.toString(ReadFile[2*12]), "String", "int") ;
			Quest = (int[]) Utg.ConvertArray(Utg.toString(ReadFile[2*13]), "String", "int") ;
			setBag((int[]) Utg.ConvertArray(Utg.toString(ReadFile[2*14]), "String", "int")) ;
			Equips = (int[]) Utg.ConvertArray(Utg.toString(ReadFile[2*15]), "String", "int") ;
			SkillPoints = Integer.parseInt(ReadFile[2*16][0]) ;
			PA.setLife((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*17]), "String", "float")) ;
			PA.setMp((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*18]), "String", "float")) ;
			PA.setRange(Float.parseFloat(ReadFile[2*19][0])) ;
			BA.setPhyAtk((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*20]), "String", "float")) ;
			BA.setMagAtk((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*21]), "String", "float")) ;
			BA.setPhyDef((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*22]), "String", "float")) ;
			BA.setMagDef((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*23]), "String", "float")) ;
			BA.setDex((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*24]), "String", "float")) ;
			BA.setAgi((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*25]), "String", "float")) ;
			BA.setCrit((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*26]), "String", "float")) ;
			BA.setStun((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*27]), "String", "float")) ;
			BA.setBlock((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*28]), "String", "float")) ;
			BA.setBlood((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*29]), "String", "float")) ;
			BA.setPoison((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*30]), "String", "float")) ;
			BA.setSilence((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*31]), "String", "float")) ;
			Elem = Utg.toString(ReadFile[2*32]) ;
			ElemMult = (float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*33]), "String", "float") ;
			Collect = (float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*34]), "String", "float") ;
			PA.setLevel(Integer.parseInt(ReadFile[2*35][0])) ;
			Gold = (float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*36]), "String", "float") ;
			setStep(Integer.parseInt(ReadFile[2*37][0])) ;
			PA.setExp((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*38]), "String", "float")) ;
			PA.setSatiation((float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*39]), "String", "float")) ;
			QuestSkills = (boolean[]) Utg.ConvertArray(Utg.toString(ReadFile[2*40]), "String", "boolean") ;
			getBattleAtt().setSpecialStatus((int[]) Utg.ConvertArray(Utg.toString(ReadFile[2*41]), "String", "int")) ;
			Actions = (int[][]) Utg.ConvertDoubleArray(Utg.deepToString(ReadFile[2*42], 3), "String", "int") ;
			getBattleAtt().setBattleActions((int[][]) Utg.ConvertDoubleArray(Utg.deepToString(ReadFile[2*43], 3), "String", "int")) ;
			SpellIsActive = new boolean[getSpell().length] ;
			SpellCounter = new int[getSpell().length][2] ;
			StatusCounter = (int[]) Utg.ConvertArray(Utg.toString(ReadFile[2*44]), "String", "int") ;
			Stats = (float[]) Utg.ConvertArray(Utg.toString(ReadFile[2*45]), "String", "float") ;
			AttPoints = Integer.parseInt(ReadFile[2*46][0]) ;
			AttIncrease = (float[][]) Utg.ConvertDoubleArray(Utg.deepToString(ReadFile[2*47], 8), "String", "float") ;
			ChanceIncrease = (float[][]) Utg.ConvertDoubleArray(Utg.deepToString(ReadFile[2*48], 8), "String", "float") ;
			if (!Utg.toString(ReadFile[2*49])[0].equals("null"))
			{
				CreaturesDiscovered = (int[]) Utg.ConvertArray(Utg.toString(ReadFile[2*49]), "String", "int") ;
			}
			else
			{
				CreaturesDiscovered = null ;
			}
			pet.Load(ReadFile) ;
		}
	}
	
	
	/* Printing methods */
	public void PrintAllAttributes()
	{
		System.out.println();
		System.out.println("** Player attributes **");
		System.out.println("Language: " + Language);
		System.out.println("Sex: " + Sex);
		System.out.println("color: " + Arrays.toString(color));
		System.out.println("Job: " + Job);
		System.out.println("ProJob: " + ProJob);
		System.out.println("Skill: " + Arrays.toString(Spell));
		System.out.println("Quest: " + Arrays.toString(Quest));
		System.out.println("Bag: " + Arrays.toString(Bag));
		System.out.println("Equips: " + Arrays.toString(Equips));
		System.out.println("SkillPoints: " + SkillPoints);
		System.out.println("** Personal attributes **");
		PA.printAtt();
		System.out.println("** Battle attributes **");
		BA.printAtt();
		System.out.println("Elem: " + Arrays.toString(Elem));
		System.out.println("ElemMult: " + Arrays.toString(ElemMult));
		System.out.println("Collect: " + Arrays.toString(Collect));
		System.out.println("Gold: " + Arrays.toString(Gold));
		System.out.println("QuestSkills: " + Arrays.toString(QuestSkills));
		System.out.println("Actions: " + Arrays.deepToString(Actions));
		System.out.println("CurrentAction: " + CurrentAction);
		System.out.println("IsRiding: " + IsRiding);
		System.out.println("SkillIsActive: " + Arrays.toString(SpellIsActive));
		System.out.println("SkillCounter: " + Arrays.deepToString(SpellCounter));
		System.out.println("StatusCounter: " + Arrays.toString(StatusCounter));
		System.out.println("Stats: " + Arrays.toString(Stats));
		System.out.println("Combo: " + Arrays.toString(Combo));
		System.out.println("AttPoints: " + AttPoints);
		System.out.println("AttIncrease: " + Arrays.deepToString(AttIncrease));
		System.out.println("ChanceIncrease: " + Arrays.deepToString(ChanceIncrease));
		System.out.println("CreaturesDiscovered: " + Arrays.toString(CreaturesDiscovered));

		System.out.println("SelectedWindow: " + Arrays.toString(SelectedWindow));
		System.out.println("SelectedMenu: " + Arrays.toString(SelectedMenu));
		System.out.println("SelectedItem: " + Arrays.toString(SelectedItem));
		System.out.println("ItemsMenu: " + ItemsMenu);
		System.out.println("CustomKey: " + CustomKey);
		System.out.println("OptionStatus: " + Arrays.toString(OptionStatus));
		System.out.println("ClosestCreature: " + ClosestCreature);
		System.out.println("CreatureInBattle: " + CreatureInBattle);
		System.out.println("DifficultLevel: " + DifficultLevel);
		System.out.println("RidingImage: " + RidingImage);
		System.out.println("action: " + action);
		System.out.println("countmove: " + countmove);
		System.out.println("SelectedOption: " + SelectedOption);
		System.out.println("EquipsBonus: " + Arrays.deepToString(EquipsBonus));
		System.out.println("WindowIsOpen: " + Arrays.toString(WindowIsOpen));
		System.out.println("hotkeyItem: " + Arrays.toString(hotkeyItem));
	}
}