package main ;

import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Graphics ;
import java.awt.Graphics2D;
import java.awt.Image ;
import java.awt.Point;
import java.awt.Toolkit ;
import java.awt.event.KeyAdapter ;
import java.awt.event.KeyEvent ;
import java.awt.event.MouseEvent ;
import java.awt.event.MouseListener ;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sound.sampled.Clip;
import javax.swing.JPanel ;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import attributes.Attributes;
import attributes.BasicAttribute;
import attributes.BasicBattleAttribute;
import attributes.BattleAttributes;
import attributes.BattleSpecialAttribute;
import attributes.BattleSpecialAttributeWithDamage;
import attributes.PersonalAttributes;
import components.BuildingType;
import components.Buildings;
import components.GameIcon;
import components.NPCJobs;
import components.NPCType;
import components.NPCs;
import components.Projectiles;
import components.Quests;
import components.SpellTypes;
import graphics.Animations;
import graphics.DrawingOnPanel;
import items.Alchemy;
import items.Arrow;
import items.Equip;
import items.Fab;
import items.Food;
import items.Forge;
import items.GeneralItem;
import items.Item;
import items.PetItem;
import items.Potion;
import items.QuestItem;
import items.Recipe;
import liveBeings.Buff;
import liveBeings.Creature;
import liveBeings.CreatureTypes;
import liveBeings.LiveBeingStates;
import liveBeings.LiveBeingStatus;
import liveBeings.MovingAnimations;
import liveBeings.Pet;
import liveBeings.Player;
import liveBeings.Pterodactile;
import liveBeings.Spell;
import liveBeings.SpellType;
import maps.CityMap;
import maps.FieldMap;
import maps.GameMap;
import maps.SpecialMap;
import maps.TreasureChest;
import screen.Screen;
import screen.SideBar;
import screen.Sky;
import testing.TestingAnimations;
import utilities.Align;
import utilities.AttackEffects;
import utilities.Elements;
import utilities.GameStates;
import utilities.Scale;
import utilities.UtilG;
import utilities.UtilS;
import windows.CraftWindow;
import windows.ShoppingWindow;

public class Game extends JPanel
{
	private static final long serialVersionUID = 1L ;

	private JPanel mainPanel = this ;
    private static Point mousePos ;

	public static String JSONPath ;
	public static String CSVPath ;
	public static String ImagesPath ;
	public static String MusicPath ;
	public static String MainFontName ;
	public static Color[] ColorPalette ;	
	public static int DayDuration ;
	
	protected static GameStates state ;
	private static boolean shouldRepaint ;	// tells if the panel should be repainted, created to handle multiple repaint requests at once
	private Languages GameLanguage ;
	private static boolean konamiCodeActive ;

	private DrawingOnPanel DP ;
	private GameIcon[] sideBarIcons;
	//private Icon[] plusSignIcon ;
	private Player player ;
	private Pet pet ;
	private Creature[] creature ;
	//private ArrayList<Recipe> allRecipes ;
	private List<Projectiles> proj ;
	public static int difficultLevel ;

	private static Screen screen ;
	private static Sky sky ;
	private static SideBar sideBar ;
	private static Opening opening ;
	private static CreatureTypes[] creatureTypes ;
	private static CityMap[] cityMaps;
	private static FieldMap[] fieldMaps;
	private static SpecialMap[] specialMaps;
	private static GameMap[] allMaps ;
	private static BuildingType[] buildingTypes ;
	private static NPCType[] NPCTypes ;
	private static Item[] allItems ;
	private static SpellType[] allSpellTypes ;
	//private static NPCs[] allNPCs ;
	private static Quests[] allQuests ;
	private static Battle bat ;
	private static Animations[] ani ;
	
	private static final String[] konamiCode = new String[] {"Acima", "Acima", "Abaixo", "Abaixo", "Esquerda", "Direita", "Esquerda", "Direita", "B", "A"} ;
	public static final Image slotImage = UtilG.loadImage(".\\images\\" + "slot.png") ;
	
	public Game(Dimension windowDimension) 
	{
    	screen = new Screen(new Dimension(windowDimension.width - 40, windowDimension.height), null) ;
    	screen.calcCenter() ;
    	JSONPath = ".\\json\\" ;
		CSVPath = ".\\csv files\\" ;
		ImagesPath = ".\\images\\" ;
		MusicPath = ".\\music\\" ;
		MainFontName = "Comics" ;
		ColorPalette = UtilS.ReadColorPalette(UtilG.loadImage(ImagesPath + "ColorPalette.png"), "Normal") ;    	
    	ani = new Animations[] {
    			new Animations(),
    			new Animations(),
    			new Animations(),
    			new Animations(),
    			new Animations(),
    			new Animations()
    	};
		opening = new Opening() ;
		DP = new DrawingOnPanel() ;
		GameLanguage = Languages.portugues ;
		state = GameStates.loading;
		konamiCodeActive = false ;
		shouldRepaint = false ;
    	//OpeningIsOn = true ; 

    	player = new Player("", "", "", 1) ;
    	//music = new Music() ;
    	
		addMouseListener(new MouseEventDemo()) ;
		addKeyListener(new TAdapter()) ;
		setFocusable(true) ;
	}

	public static Screen getScreen() {return screen ;}
	public static Sky getSky() {return sky ;}
	public static CreatureTypes[] getCreatureTypes() {return creatureTypes ;}
	public static NPCType[] getNPCTypes() {return NPCTypes ;}

	public static BuildingType[] getBuildingTypes() {return buildingTypes ;}
	public static GameMap[] getMaps() {return allMaps ;}
	public static Quests[] getAllQuests() {return allQuests ;}
	public static Item[] getAllItems() {return allItems ;}
	public static SpellType[] getAllSpellTypes() {return allSpellTypes ;}
	public static boolean getShouldRepaint() {return shouldRepaint ;}
	public static Point getMousePos()
	{
		return mousePos ;
	}
	
	public static void playStopTimeGif() {state = GameStates.playingStopTimeGif ;}
	public boolean SomeAnimationIsActive() { return (ani[3].isActive() | ani[4].isActive() | ani[5].isActive()) ;}
	public static void shouldRepaint() {shouldRepaint = true ;}
	
	public ArrayList<Recipe> LoadCraftingRecipes()
	{
		ArrayList<Recipe> recipes = new ArrayList<>() ;
		
		JSONArray input = UtilG.readJsonArray(Game.JSONPath + "CraftRecipes.json") ;		
		for (int i = 0 ; i <= input.size() - 1 ; i += 1)
		{
			JSONObject recipe = (JSONObject) input.get(i) ;
			
			JSONArray listIngredients = (JSONArray) recipe.get("IngredientIDs") ;
			JSONArray listIngredientAmounts = (JSONArray) recipe.get("IngredientAmounts") ;
			Map<Item, Integer> ingredients = new HashMap<>() ;
			for (int ing = 0 ; ing <= listIngredients.size() - 1 ; ing += 1)
			{
				ingredients.put(allItems[(int) (long) listIngredients.get(ing)], (int) (long) listIngredientAmounts.get(ing)) ;
			}

			JSONArray listProducts = (JSONArray) recipe.get("ProductIDs") ;
			JSONArray listProductAmounts = (JSONArray) recipe.get("ProductAmounts") ;
			Map<Item, Integer> products = new HashMap<>() ;
			for (int prod = 0 ; prod <= listProducts.size() - 1 ; prod += 1)
			{
				products.put(allItems[(int) (long) listProducts.get(prod)], (int) (long) listProductAmounts.get(prod)) ;
			}
			
			recipes.add(new Recipe(ingredients, products)) ;
		}

		return recipes ;
	}
	
    private NPCType[] initializeNPCTypes(Languages language)
    {
    	ArrayList<String[]> input = UtilG.ReadcsvFile(CSVPath + "NPCTypes.csv") ;
		String path = ImagesPath + "\\NPCs\\";
		NPCType[] npcType = new NPCType[input.size()] ;
		for (int i = 0 ; i <= npcType.length - 1 ; i += 1)
		{
			String  name = input.get(i)[language.ordinal()] ;
			NPCJobs job = null ;
			switch (input.get(i)[2])
			{
				case "doctor": job = NPCJobs.doctor ; break ;
				case "equipsSeller": job = NPCJobs.equipsSeller ; break ;
				case "itemsSeller": job = NPCJobs.itemsSeller ; break ;
				case "smuggleSeller": job = NPCJobs.smuggleSeller ; break ;
				case "banker": job = NPCJobs.banker ; break ;
				case "alchemist": job = NPCJobs.alchemist ; break ;
				case "woodcrafter": job = NPCJobs.woodcrafter ; break ;
				case "crafter": job = NPCJobs.crafter ; break ;
				case "forger": job = NPCJobs.forger ; break ;
				case "elemental": job = NPCJobs.elemental ; break ;
				case "saver": job = NPCJobs.saver ; break ;
				case "master": job = NPCJobs.master ; break ;
				case "quest": job = NPCJobs.quest ; break ;
				case "citizen": job = NPCJobs.citizen ; break ;
				case "caveEntry": job = NPCJobs.caveEntry ; break ;
				case "caveExit": job = NPCJobs.caveExit ; break ;
				case "sailor": job = NPCJobs.sailor ; break ;
			}

			String info = input.get(i)[3 + language.ordinal()] ;
			Color color = ColorPalette[0] ;
			Image image = UtilG.loadImage(path + "NPC_" + job.toString() + ".png") ;
			String[] speech = player.allText.get("* " + name + " *") ;

			// TODO NPC options vai ser uma lista de listas, cada uma correspondendo a uma speech
			String[] options = new String[] {"Sim", "Não"} ;
			
			npcType[i] = new NPCType(name, job, info, color, image, speech, options) ;
		}
    	
		return npcType ;
    }
    
    private BuildingType[] initializeBuildingTypes()
    {
    	JSONArray input = UtilG.readJsonArray("./json/buildingTypes.json") ;
    	BuildingType[] buildingTypes = new BuildingType[input.size()] ;
		String path = ImagesPath + "\\Buildings\\";
    	for (int i = 0 ; i <= input.size() - 1; i += 1)
    	{
    		JSONObject type = (JSONObject) input.get(i) ;
    		String name = (String) type.get("name") ;
			Image outsideImage = UtilG.loadImage(path + "Building" + name + ".png") ;
			
			// adding npcs to the building
			List<NPCs> npcs = new ArrayList<>() ;
			JSONArray arrayNPCs = (JSONArray) type.get("npcs") ;
			for (int j = 0; j <= arrayNPCs.size() - 1; j += 1)
			{
				JSONObject newNPC = (JSONObject) arrayNPCs.get(j) ;
				NPCJobs npcJob = NPCJobs.valueOf((String) newNPC.get("job")) ;
				NPCType npcType = NPCs.typeFromJob(npcJob) ;
				JSONArray arrayPos = (JSONArray) newNPC.get("pos") ;
				Point npcPos = new Point((int) (long) arrayPos.get(0), (int) (long) arrayPos.get(1)) ;
				if (npcType != null)
				{
					npcs.add(new NPCs(0, npcType, npcPos)) ;
				}
			}
			
    		buildingTypes[i] = new BuildingType(name, outsideImage, npcs) ;
    		
    		boolean hasInterior = (boolean) type.get("hasInterior") ;
			if (hasInterior)
			{
				Image insideImage = UtilG.loadImage(path + "Building" + name + "Inside.png") ;
				Image[] OrnamentImages = new Image[] {UtilG.loadImage(path + "Building" + name + "Ornament.png")} ;
				buildingTypes[i].setInsideImage(insideImage) ;
				buildingTypes[i].setOrnamentImages(OrnamentImages) ;
			}
    	}
		
		return buildingTypes ;
    }
    
    private CreatureTypes[] initializeCreatureTypes(Languages language, double diffMult)
    {
		ArrayList<String[]> input = UtilG.ReadcsvFile(CSVPath + "CreatureTypes.csv") ;
		String path = ImagesPath + "\\Creatures\\";
    	CreatureTypes.setNumberOfCreatureTypes(input.size());
		CreatureTypes[] creatureTypes = new CreatureTypes[CreatureTypes.getNumberOfCreatureTypes()] ;
		Color[] color = new Color[creatureTypes.length] ;
		for (int ct = 0 ; ct <= creatureTypes.length - 1 ; ct += 1)
		{
			int colorid = (int)((Creature.getskinColor().length - 1)*Math.random()) ;
			color[ct] = Creature.getskinColor()[colorid] ;
			if (270 < ct & ct <= 299)	// Ocean creatures
			{
				color[ct] = ColorPalette[5] ;
			}
			
			MovingAnimations moveAni = new MovingAnimations(UtilG.loadImage(path + "creature" + (ct % 5) + "_idle.gif"),
					UtilG.loadImage(path + "creature" + (ct % 5) + "_movingup.gif"),
					UtilG.loadImage(path + "creature" + (ct % 5) + "_movingdown.gif"),
					UtilG.loadImage(path + "creature" + (ct % 5) + "_movingleft.gif"),
					UtilG.loadImage(path + "creature" + (ct % 5) + "_movingright.gif")) ;
			
			BasicAttribute Life = new BasicAttribute((int) (Integer.parseInt(input.get(ct)[5]) * diffMult), (int) (Integer.parseInt(input.get(ct)[5]) * diffMult), 1) ;
			BasicAttribute Mp = new BasicAttribute((int) (Integer.parseInt(input.get(ct)[6]) * diffMult), (int) (Integer.parseInt(input.get(ct)[6]) * diffMult), 1) ;
			BasicAttribute Exp = new BasicAttribute(Integer.parseInt(input.get(ct)[36]), 999999999, 1) ;
			BasicAttribute Satiation = new BasicAttribute(100, 100, 1) ;
			BasicAttribute Thirst = new BasicAttribute(100, 100, 1) ;	
			PersonalAttributes PA = new PersonalAttributes(Life, Mp, Exp, Satiation, Thirst) ;

			BasicBattleAttribute PhyAtk = new BasicBattleAttribute(Double.parseDouble(input.get(ct)[8]) * diffMult, 0, 0) ;
			BasicBattleAttribute MagAtk = new BasicBattleAttribute(Double.parseDouble(input.get(ct)[9]) * diffMult, 0, 0) ;
			BasicBattleAttribute PhyDef = new BasicBattleAttribute(Double.parseDouble(input.get(ct)[10]) * diffMult, 0, 0) ;
			BasicBattleAttribute MagDef = new BasicBattleAttribute(Double.parseDouble(input.get(ct)[11]) * diffMult, 0, 0) ;
			BasicBattleAttribute Dex = new BasicBattleAttribute(Double.parseDouble(input.get(ct)[12]) * diffMult, 0, 0) ;
			BasicBattleAttribute Agi = new BasicBattleAttribute(Double.parseDouble(input.get(ct)[13]) * diffMult, 0, 0) ;
			double[] Crit = new double[] {Double.parseDouble(input.get(ct)[14]) * diffMult, 0, Double.parseDouble(input.get(ct)[15]) * diffMult, 0} ;
			BattleSpecialAttribute Stun = new BattleSpecialAttribute(Double.parseDouble(input.get(ct)[16]) * diffMult, 0, Double.parseDouble(input.get(ct)[17]) * diffMult, 0, (int) (Double.parseDouble(input.get(ct)[18]) * diffMult)) ;
			BattleSpecialAttribute Block = new BattleSpecialAttribute(Double.parseDouble(input.get(ct)[19]) * diffMult, 0, Double.parseDouble(input.get(ct)[20]) * diffMult, 0, (int) (Double.parseDouble(input.get(ct)[21]) * diffMult)) ;
			BattleSpecialAttributeWithDamage Blood = new BattleSpecialAttributeWithDamage(Double.parseDouble(input.get(ct)[22]) * diffMult, 0, Double.parseDouble(input.get(ct)[23]) * diffMult, 0, (int) (Double.parseDouble(input.get(ct)[24]) * diffMult), 0, (int) (Double.parseDouble(input.get(ct)[25]) * diffMult), 0, (int) (Integer.parseInt(input.get(ct)[26]) * diffMult)) ;
			BattleSpecialAttributeWithDamage Poison = new BattleSpecialAttributeWithDamage(Double.parseDouble(input.get(ct)[27]) * diffMult, 0, Double.parseDouble(input.get(ct)[28]) * diffMult, 0, (int) (Double.parseDouble(input.get(ct)[29]) * diffMult), 0, (int) (Double.parseDouble(input.get(ct)[30]) * diffMult), 0, (int) (Integer.parseInt(input.get(ct)[31]) * diffMult)) ;
			BattleSpecialAttribute Silence = new BattleSpecialAttribute(Double.parseDouble(input.get(ct)[32]) * diffMult, 0, Double.parseDouble(input.get(ct)[33]) * diffMult, 0, (int) (Double.parseDouble(input.get(ct)[34]) * diffMult)) ;
			LiveBeingStatus status = new LiveBeingStatus() ;
			BattleAttributes BA = new BattleAttributes(PhyAtk, MagAtk, PhyDef, MagDef, Dex, Agi, Crit, Stun, Block, Blood, Poison, Silence, status) ;
						
			List<Spell> spell = new ArrayList<>() ;
			spell.add(new Spell(allSpellTypes[0])) ;
			spell.add(new Spell(allSpellTypes[1])) ;
			spell.add(new Spell(allSpellTypes[2])) ;
			spell.add(new Spell(allSpellTypes[3])) ;
			spell.add(new Spell(allSpellTypes[4])) ;
			
			Set<Item> Bag = new HashSet<>() ;
			for (int i = 0 ; i <= 10 - 1; i += 1)
			{
				int itemID = Integer.parseInt(input.get(ct)[37 + i]) ;
				if (-1 < itemID) { Bag.add(allItems[itemID]) ;}
			}

			int Gold = Integer.parseInt(input.get(ct)[47]) ;
			int[] StatusCounter = new int[8] ;

			String name = input.get(ct)[1 + language.ordinal()] ;
			int level = Integer.parseInt(input.get(ct)[3]) ;
			Dimension size = new Dimension(moveAni.idleGif.getWidth(null), moveAni.idleGif.getHeight(null)) ;	
			double range = Double.parseDouble(input.get(ct)[7]) * diffMult ;
			int step = Integer.parseInt(input.get(ct)[48]) ;
			Elements[] elem = new Elements[] {Elements.valueOf(input.get(ct)[35])} ;
			int mpDuration = Integer.parseInt(input.get(ct)[49]) ;
			int satiationDuration = 100 ;
			int moveDuration = Integer.parseInt(input.get(ct)[50]) ;
			int battleActionDuration = Integer.parseInt(input.get(ct)[51]) ;
			int stepCounter = 0 ;
			
			creatureTypes[ct] = new CreatureTypes(ct, name, level, size, range, step, elem,
					mpDuration, satiationDuration, moveDuration, battleActionDuration, stepCounter,
					moveAni, PA, BA, spell, Bag, Gold, color[ct], StatusCounter) ;	
		}
		return creatureTypes ;
    }
    
    private CityMap[] initializeCityMaps()
    {
		ArrayList<String[]> input = UtilG.ReadcsvFile(CSVPath + "MapsCity.csv") ;
		String path = ImagesPath + "\\Maps\\";
		CityMap[] cityMap = new CityMap[input.size()] ;
		
		for (int id = 0 ; id <= cityMap.length - 1 ; id += 1)
		{
			String name = input.get(id)[0] ;
			int continent = Integer.parseInt(input.get(id)[1]) ;
			int[] connections = new int[] {
											Integer.parseInt(input.get(id)[2]),
											Integer.parseInt(input.get(id)[3]),
											Integer.parseInt(input.get(id)[4]),
											Integer.parseInt(input.get(id)[5]),
											Integer.parseInt(input.get(id)[6]),
											Integer.parseInt(input.get(id)[7]),
											Integer.parseInt(input.get(id)[8]),
											Integer.parseInt(input.get(id)[9])
											} ;
			Image image = UtilG.loadImage(path + "Map" + String.valueOf(id) + ".png") ;
			Clip music = Music.musicFileToClip(new File(MusicPath + (id + 2) + "-" + name + ".wav").getAbsoluteFile()) ;
			
			
			// adding buildings top map
			List<Buildings> buildings = new ArrayList<>() ;
			for (int i = 0; i <= 6 - 1; i += 1)
			{
				String buildingName = input.get(id)[10 + 3 * i] ;
				BuildingType buildingType = null ;
				for (int j = 0 ; j <= buildingTypes.length - 1 ; j += 1)
				{
					if (buildingName.equals(buildingTypes[j].getName()))
					{
						buildingType = buildingTypes[j] ;
					}
				}
				
				int buildingPosX = (int) (screen.getSize().width * Double.parseDouble(input.get(id)[11 + 3 * i])) ;
				int buildingPosY = (int) (sky.height + screen.getSize().height * Double.parseDouble(input.get(id)[12 + 3 * i])) ;
				Point buildingPos = new Point(buildingPosX, buildingPosY) ;
				
				buildings.add(new Buildings(buildingType, buildingPos)) ;
			}

			
			// adding npcs to map
			List<NPCs> npcs = new ArrayList<NPCs>() ;
			for (int i = 0; i <= 17 - 1; i += 1)
			{
				String npcJob = input.get(id)[28 + 3 * i] ;
				NPCType NPCType = null ;
				for (int j = 0 ; j <= getNPCTypes().length - 1 ; j += 1)
				{
					if (npcJob.equals(getNPCTypes()[j].getJob().toString()))
					{
						NPCType = getNPCTypes()[j] ;
					}
				}
				
				int NPCPosX = (int) (screen.getSize().width * Double.parseDouble(input.get(id)[29 + 3 * i])) ;
				int NPCPosY = (int) (sky.height + screen.getSize().height * Double.parseDouble(input.get(id)[30 + 3 * i])) ;
				Point NPCPos = new Point(NPCPosX, NPCPosY) ;
				npcs.add(new NPCs(i, NPCType, NPCPos)) ;
			}
			
			cityMap[id] = new CityMap(name, continent, connections, image, music, buildings, npcs);
		}
		
		return cityMap ;
    }
    
    private FieldMap[] initializeFieldMaps()
    {
    	ArrayList<String[]> input = UtilG.ReadcsvFile(CSVPath + "MapsField.csv") ;
    	String path = ImagesPath + "\\Maps\\";
    	FieldMap[] fieldMap = new FieldMap[input.size()] ;
		
    	// TODO mapas 39 e 64 est�o entrando como fieldmaps e specialmaps
		for (int id = 0 ; id <= fieldMap.length - 1 ; id += 1)
		{
			String name = input.get(id)[0] ;
			int continent = Integer.parseInt(input.get(id)[1]) ;
			int collectibleLevel = Integer.parseInt(input.get(id)[2]) ;
			int berryDelay = Integer.parseInt(input.get(id)[3]) ;
			int herbDelay = Integer.parseInt(input.get(id)[4]) ;
			int woodDelay = Integer.parseInt(input.get(id)[5]) ;
			int metalDelay = Integer.parseInt(input.get(id)[6]) ;
			int[] Connections = new int[] {
											Integer.parseInt(input.get(id)[7]),
											Integer.parseInt(input.get(id)[8]),
											Integer.parseInt(input.get(id)[9]),
											Integer.parseInt(input.get(id)[10]),
											Integer.parseInt(input.get(id)[11]),
											Integer.parseInt(input.get(id)[12]),
											Integer.parseInt(input.get(id)[13]),
											Integer.parseInt(input.get(id)[14])
											} ;
			int[] creatureIDs = new int[] {
											Integer.parseInt(input.get(id)[15]),
											Integer.parseInt(input.get(id)[16]),
											Integer.parseInt(input.get(id)[17]),
											Integer.parseInt(input.get(id)[18]),
											Integer.parseInt(input.get(id)[19]),
											Integer.parseInt(input.get(id)[20]),
											Integer.parseInt(input.get(id)[21]),
											Integer.parseInt(input.get(id)[22]),
											Integer.parseInt(input.get(id)[23]),
											Integer.parseInt(input.get(id)[24])
											} ;
			Image image = UtilG.loadImage(path + "Map" + String.valueOf(id + cityMaps.length) + ".png") ;
			Clip music = Music.musicFileToClip(new File(MusicPath + "7-Forest.wav").getAbsoluteFile()) ;
			fieldMap[id] = new FieldMap(name, continent, Connections, image, music, collectibleLevel, new int[] {berryDelay, herbDelay, woodDelay, metalDelay}, creatureIDs) ;
		}
		
		return fieldMap ;
    }
    
    private SpecialMap[] initializeSpecialMaps()
    {
    	ArrayList<String[]> input = UtilG.ReadcsvFile(CSVPath + "MapsSpecial.csv") ;
		String path = ImagesPath + "\\Maps\\";
		SpecialMap[] specialMaps = new SpecialMap[input.size()] ;
		
		for (int id = 0 ; id <= specialMaps.length - 1 ; id += 1)
		{
			String name = input.get(id)[0] ;
			int continent = Integer.parseInt(input.get(id)[1]) ;
			int[] connections = new int[] {
											Integer.parseInt(input.get(id)[2]),
											Integer.parseInt(input.get(id)[3]),
											Integer.parseInt(input.get(id)[4]),
											Integer.parseInt(input.get(id)[5]),
											Integer.parseInt(input.get(id)[6]),
											Integer.parseInt(input.get(id)[7]),
											Integer.parseInt(input.get(id)[8]),
											Integer.parseInt(input.get(id)[9])
											} ;
			Image image = UtilG.loadImage(path + "MapSpecial" + String.valueOf(id) + ".png") ;
			Clip music = Music.musicFileToClip(new File(MusicPath + "12-Special.wav").getAbsoluteFile()) ;
			
			// adding treasure chests
			List<TreasureChest> treasureChests = new ArrayList<>() ;
			Image treasureChestsImage = UtilG.loadImage(ImagesPath + "\\MapElements\\" + "MapElem15_Chest.png") ;
			for (int chest = 0 ; chest <= 5 - 1; chest += 1)
			{
				Point pos = new Point((int) (Double.parseDouble(input.get(id)[10 + 13 * chest]) * screen.getSize().width), (int) (Double.parseDouble(input.get(id)[11 + 13 * chest]) * screen.getSize().height)) ;
				List<Item> itemRewards = new ArrayList<>() ;
				for (int item = 0 ; item <= 10 - 1; item += 1)
				{
					int itemID = Integer.parseInt(input.get(id)[12 + 13 * chest + item]) ;
					if (-1 < itemID)
					{
						itemRewards.add(allItems[itemID]) ;
					}
				}
				int goldReward = Integer.parseInt(input.get(id)[22 + 13 * chest]) ;
				treasureChests.add(new TreasureChest(chest, pos, treasureChestsImage, itemRewards, goldReward)) ;
			}
			specialMaps[id] = new SpecialMap(name, continent, connections, image, music, treasureChests) ;
		}
		
		return specialMaps ;
    }
    
    private Quests[] initializeQuests(Languages language, int playerJob)
    {
		ArrayList<String[]> input = UtilG.ReadcsvFile(CSVPath + "Quests.csv") ;
		Quests[] quests = new Quests[input.size()] ;
		for (int i = 0 ; i <= quests.length - 1 ; i += 1)
		{
			int id = Integer.parseInt(input.get(i)[0]) ;
			quests[i] = new Quests(Integer.parseInt(input.get(i)[0])) ;
			quests[i].Initialize(input.get(id), language, id, playerJob) ;
		}
		
		return quests ;
    }
   
    private GameIcon[] initializeIcons(Dimension screenSize)
    {
    	String path = Game.ImagesPath + "\\Icons\\";
    	
		// Plus sign icons
     	//plusSignIcon = new Icon[8] ;
     	Point[] PlusSignPos = new Point[] {new Point(175, 206), new Point(175, 225), new Point(175, 450),
     			new Point(175, 475), new Point(175, 500), new Point(175, 525), new Point(175, 550), new Point(175, 575)} ;
		Image PlusSignImage = UtilG.loadImage(path + "PlusSign.png") ;
		Image SelectedPlusSignImage = UtilG.loadImage(path + "ShiningPlusSign.png") ;
		for (int i = 0 ; i <= PlusSignPos.length - 1 ; i += 1)
    	{
     		GameIcon newIcon = new GameIcon(i + 8, "Plus sign", PlusSignPos[i], null, PlusSignImage, SelectedPlusSignImage) ;
    		//plusSignIcon[i] = newIcon;
     		GameIcon.addToAllIconsList(newIcon) ;
    	}
		
    	return sideBarIcons ;
    }
 	
    private SpellType[] initializeSpellTypes(Languages language)
    {
    	// TODO check spelltypes csv elements names
    	List<String[]> spellTypesInput = UtilG.ReadcsvFile(Game.CSVPath + "SpellTypes.csv") ;	
    	SpellType[] allSpellTypes = new SpellType[spellTypesInput.size()] ;
    	List<String[]> spellsBuffsInput = UtilG.ReadcsvFile(Game.CSVPath + "SpellsBuffs.csv") ;
//    	List<String[]> spellsNerfsInput = UtilG.ReadcsvFile(Game.CSVPath + "SpellsNerfs.csv") ;

		String[][] spellsInfo = new String[allSpellTypes.length][2] ;
		for (int i = 0 ; i <= allSpellTypes.length - 1 ; i += 1)
		{
			int ID = i ;
			
			Map<Attributes, Double> percentIncrease = new HashMap<>() ;
			Map<Attributes, Double> valueIncrease = new HashMap<>() ;
			Map<Attributes, Double> chance = new HashMap<>() ;
			int BuffCont = 0 ;
			String[] spellsBuffsInp = spellsBuffsInput.get(ID) ;
			for (Attributes att : Attributes.values())
			{
				if (att.equals(Attributes.exp) | att.equals(Attributes.satiation) | att.equals(Attributes.thirst))
				{
					continue ;
				}
				if (att.equals(Attributes.blood) | att.equals(Attributes.poison))
				{
					// TODO ajustar o csv e fazer a leitura correta
					percentIncrease.put(att, Double.parseDouble(spellsBuffsInp[BuffCont + 3])) ;
					valueIncrease.put(att, Double.parseDouble(spellsBuffsInp[BuffCont + 4])) ;
					chance.put(att, Double.parseDouble(spellsBuffsInp[BuffCont + 5])) ;
					BuffCont += 12 ;
				}
				else
				{
					percentIncrease.put(att, Double.parseDouble(spellsBuffsInp[BuffCont + 3])) ;
					valueIncrease.put(att, Double.parseDouble(spellsBuffsInp[BuffCont + 4])) ;
					chance.put(att, Double.parseDouble(spellsBuffsInp[BuffCont + 5])) ;
					BuffCont += 3 ;
				}
			}
			List<Buff> buffs = new ArrayList<>() ;
			buffs.add(new Buff(percentIncrease, valueIncrease, chance)) ;

			spellsInfo[i] = new String[] {spellTypesInput.get(ID)[42], spellTypesInput.get(ID)[43 + 2 * language.ordinal()]} ;
			String Name = spellTypesInput.get(ID)[4] ;
			int MaxLevel = Integer.parseInt(spellTypesInput.get(ID)[5]) ;
			int MpCost = Integer.parseInt(spellTypesInput.get(ID)[6]) ;
			SpellTypes Type = SpellTypes.getByName(spellTypesInput.get(ID)[7]) ;
			Map<SpellType, Integer> preRequisites = new HashMap<>() ;
			for (int p = 0 ; p <= 6 - 1 ; p += 2)
			{
				if (-1 < Integer.parseInt(spellTypesInput.get(ID)[p + 8]))
				{
					preRequisites.put(allSpellTypes[Integer.parseInt(spellTypesInput.get(ID)[p + 8])], Integer.parseInt(spellTypesInput.get(ID)[p + 9])) ;
				}
			}
			int Cooldown = Integer.parseInt(spellTypesInput.get(ID)[14]) ;
			int Duration = Integer.parseInt(spellTypesInput.get(ID)[15]) ;
			double[] Atk = new double[] {Double.parseDouble(spellTypesInput.get(ID)[16]), Double.parseDouble(spellTypesInput.get(ID)[17])} ;
			double[] Def = new double[] {Double.parseDouble(spellTypesInput.get(ID)[18]), Double.parseDouble(spellTypesInput.get(ID)[19])} ;
			double[] Dex = new double[] {Double.parseDouble(spellTypesInput.get(ID)[20]), Double.parseDouble(spellTypesInput.get(ID)[21])} ;
			double[] Agi = new double[] {Double.parseDouble(spellTypesInput.get(ID)[22]), Double.parseDouble(spellTypesInput.get(ID)[23])} ;
			double[] AtkCrit = new double[] {Double.parseDouble(spellTypesInput.get(ID)[24])} ;
			double[] DefCrit = new double[] {Double.parseDouble(spellTypesInput.get(ID)[25])} ;
			double[] Stun = new double[] {Double.parseDouble(spellTypesInput.get(ID)[26]), Double.parseDouble(spellTypesInput.get(ID)[27]), Double.parseDouble(spellTypesInput.get(ID)[28])} ;
			double[] Block = new double[] {Double.parseDouble(spellTypesInput.get(ID)[29]), Double.parseDouble(spellTypesInput.get(ID)[30]), Double.parseDouble(spellTypesInput.get(ID)[31])} ;
			double[] Blood = new double[] {Double.parseDouble(spellTypesInput.get(ID)[32]), Double.parseDouble(spellTypesInput.get(ID)[33]), Double.parseDouble(spellTypesInput.get(ID)[34])} ;
			double[] Poison = new double[] {Double.parseDouble(spellTypesInput.get(ID)[35]), Double.parseDouble(spellTypesInput.get(ID)[36]), Double.parseDouble(spellTypesInput.get(ID)[37])} ;
			double[] Silence = new double[] {Double.parseDouble(spellTypesInput.get(ID)[38]), Double.parseDouble(spellTypesInput.get(ID)[39]), Double.parseDouble(spellTypesInput.get(ID)[40])} ;
			Elements Elem = Elements.valueOf(spellTypesInput.get(ID)[41]) ;
			
			allSpellTypes[i] = new SpellType(Name, MaxLevel, MpCost, Type, preRequisites, Cooldown, Duration, buffs,
					Atk, Def, Dex, Agi, AtkCrit, DefCrit, Stun, Block, Blood, Poison, Silence, Elem, spellsInfo[i]) ;
		}
		return allSpellTypes ;
    }

	private Item[] initializeAllItems()
	{
		ArrayList<Item> allItems = new ArrayList<>() ;
		for (int i = 0 ; i <= Potion.getAll().length - 1 ; i += 1)
		{
			allItems.add(Potion.getAll()[i]) ;
		}
		for (int i = 0 ; i <= Alchemy.getAll().length - 1 ; i += 1)
		{
			allItems.add(Alchemy.getAll()[i]) ;
		}
		for (int i = 0 ; i <= Forge.getAll().length - 1 ; i += 1)
		{
			allItems.add(Forge.getAll()[i]) ;
		}
		for (int i = 0 ; i <= PetItem.getAll().length - 1 ; i += 1)
		{
			allItems.add(PetItem.getAll()[i]) ;
		}
		for (int i = 0 ; i <= Food.getAll().length - 1 ; i += 1)
		{
			allItems.add(Food.getAll()[i]) ;
		}
		for (int i = 0 ; i <= Arrow.getAll().length - 1 ; i += 1)
		{
			allItems.add(Arrow.getAll()[i]) ;
		}
		for (int i = 0 ; i <= Equip.getAll().length - 1 ; i += 1)
		{
			allItems.add(Equip.getAll()[i]) ;
		}
		for (int i = 0 ; i <= GeneralItem.getAll().length - 1 ; i += 1)
		{
			allItems.add(GeneralItem.getAll()[i]) ;
		}
		for (int i = 0 ; i <= Fab.getAll().length - 1 ; i += 1)
		{
			allItems.add(Fab.getAll()[i]) ;
		}
		for (int i = 0 ; i <= QuestItem.getAll().length - 1 ; i += 1)
		{
			allItems.add(QuestItem.getAll()[i]) ;
		}
		
		return (Item[]) allItems.toArray(new Item[allItems.size()]) ;
	}
	

    
	private void incrementCounters()
	{
		sky.dayTime.inc() ;
		player.IncActionCounters() ;
		player.SupSpellCounters(creature, player.getOpponent()) ;
		player.getBA().getStatus().decreaseStatus() ;
		
		if (pet != null)
		{
			pet.IncActionCounters() ;
			pet.getBA().getStatus().decreaseStatus() ;
		}
		
		if (player.getMap() instanceof FieldMap)
		{
			FieldMap fm = (FieldMap) player.getMap() ;
			fm.getCreatures().forEach(creature ->
			{
				creature.IncActionCounters() ;
				creature.getBA().getStatus().decreaseStatus() ;
			});
		}
		
		if (player.isInBattle())
		{
			player.IncBattleActionCounters() ;
			pet.IncBattleActionCounters() ;
			player.getOpponent().IncBattleActionCounters() ;
		}

		for (FieldMap fieldMap : fieldMaps)
		{
			if (player.getMap().equals(fieldMap)) { fieldMap.IncCollectiblesCounter() ;}
		}
	}
	
	private void activateCounters()
	{	
		player.activateCounters();
		//player.ActivateActionCounters(ani.SomeAnimationIsActive()) ;
		if (pet != null)
		{
			if (0 < pet.getLife().getCurrentValue())
			{
				//pet.ActivateActionCounters(ani.SomeAnimationIsActive()) ;
			}
		}
//		if (!player.getMap().IsACity())
//		{
//			if (player.getMap() instanceof FieldMap)
//			{
//				FieldMap fm = (FieldMap) player.getMap() ;
//				for (Creature creature : fm.getCreatures())
//				{
//					if (!ani.isActive(12) & !ani.isActive(13) & !ani.isActive(14) & !ani.isActive(18))	// TODO define which animations stop the run
//					{
//						creature.ActivateActionCounters() ;
//					}
//				}
//			}
//		}
		for (FieldMap fieldMap : fieldMaps)
		{
			fieldMap.ActivateCollectiblesCounter() ;
		}
	}
	
	
	
	private void konamiCode()
	{
		DayDuration = 12 ;
		ColorPalette = UtilS.ReadColorPalette(UtilG.loadImage(ImagesPath + "ColorPalette.png"), "Konami") ;
		if (sky.dayTime.getCounter() % 1200 <= 300)
		{
			DrawingOnPanel.stdAngle += 0.04 ;
		}
		else if (sky.dayTime.getCounter() % 1200 <= 900)
		{
			DrawingOnPanel.stdAngle -= 0.04 ;
		}
		else
		{
			DrawingOnPanel.stdAngle += 0.04 ;
		}
	}
	
	private void checkKonamiCode(List<String> Combo)
	{
		String[] combo = Combo.toArray(new String[Combo.size()]) ;
		if (Arrays.equals(combo, konamiCode))
		{
			konamiCodeActive = !konamiCodeActive ;
			player.resetCombo() ;
		}
	}
	
	private void playStopTimeGifs()
	{
		if (opening.getOpeningGif().isTimeStopper())
		{
			opening.getOpeningGif().play(new Point(0, 0), Align.topLeft, DP);
			
			repaint();
		}
	}
	
	private void playGifs(DrawingOnPanel DP)
	{
		/*if (!testGif.isTimeStopper())
		{
			testGif.play(new Point(100, 100), AlignmentPoints.topLeft, DP);
			
			repaint();
		}*/
		/*if (testGif.isPlaying())
		{
			testGif.play(mousePos, Align.center, DP);
			shouldRepaint = true ;
		}
		if (testGif2.isPlaying())
		{
			testGif2.play(new Point(300, 100), Align.center, DP);
			shouldRepaint = true ;
		}*/
	}
	
		
	private void runGame(DrawingOnPanel DP)
	{
		// increment and activate counters
		incrementCounters() ;
		activateCounters() ;
		
		// check for the Konami code
		checkKonamiCode(player.getCombo()) ;
		if (konamiCodeActive)
		{
			konamiCode() ;
		}
		
		
		// draw the map (cities, forest, etc.)
		DP.DrawFullMap(player.getPos(), player.getMap(), sky) ;
		sideBar.display(player, pet, mousePos, DP);
		
		// creatures act
		if (!player.getMap().IsACity())
		{
			if (player.getMap() instanceof FieldMap)
			{
				FieldMap fm = (FieldMap) player.getMap() ;
				for (Creature creature : fm.getCreatures())
				{				
					creature.act(player.getPos(), player.getMap()) ;
					creature.display(creature.getPos(), Scale.unit, DP) ;
					creature.DrawAttributes(0, DP) ;
				}
				shouldRepaint = true ;
			}
		}
		
		
		// player acts
		if (player.canAct())
		{
			if (player.hasActed())
			{
				player.acts(pet, mousePos, sideBar) ;			

		        if (player.getMoveCounter().finished())
		        {
		        	player.getMoveCounter().reset() ;
		        }
			}
		}		
		if (player.isMoving())
		{
			player.move(pet) ;
			if (player.isDoneMoving())
			{
				if (player.getOpponent() == null) { player.setState(LiveBeingStates.idle) ;}
				else { player.setState(LiveBeingStates.fighting) ;};
			}
		}
		player.DrawAttributes(0, DP) ;
		player.display(player.getPos(), new Scale(1, 1), player.getDir(), player.getSettings().getShowPlayerRange(), DP) ;
		if (player.weaponIsEquipped())	// if the player is equipped with a weapon
		{
			player.DrawWeapon(player.getPos(), new double[] {1, 1}, DP) ;
		}
		player.displayState(DP) ;
		
		
		// pet acts
		if (pet != null)
		{
			if (pet.isAlive())
			{
				pet.updateCombo() ;
				pet.Move(player.getPos(), player.getMap(), player.getOpponent(), player.getElem()[4]) ;
				pet.display(pet.getPos(), new Scale(1, 1), DP) ;
				pet.DrawAttributes(0, DP) ;
			}
		}
		
		
		// find the closest creature to the player
		if (!player.getMap().IsACity())
		{
			player.closestCreature = UtilS.ClosestCreatureInRange(player, creature, allMaps) ;
		}
		
		// check if the player met something
		if (!player.isInBattle())
		{
			player.meet(creature, DP) ;
		}
		
		
		// if the player is in battle, run battle
		if (player.isInBattle())	// & !ani.isActive(12) & !ani.isActive(13) & !ani.isActive(14) & !ani.isActive(16) only enter battle if the animations for win (12), level up (13), pet level up (14), and pterodactile (16) are off
		{
			bat.RunBattle(player, pet, player.getOpponent(), ani, DP) ;
		}
		
		
		// level up the player and the pet if they should
		player.checkLevelUp(ani[4]) ;
		pet.checkLevelUp(ani[4]) ;
//		if (!ani.isActive(12))
//		{
//			player.checkLevelUp(ani) ;
//		}
//		if (pet != null & !ani.isActive(13))
//		{
//			pet.checkLevelUp(ani) ;
//		}
		
		
		// show the active player windows
		player.ShowWindows(pet, creature, creatureTypes, allMaps, bat, mousePos, DP) ;
		
		
		// move the active projectiles and check if they collide with something
		if (proj != null)
		{
			List<Creature> creaturesInMap = new ArrayList<>() ;
			if (!player.getMap().IsACity())
			{
				FieldMap fm = (FieldMap) player.getMap() ;
				creaturesInMap = fm.getCreatures() ;
			}
			for (int p = 0 ; p <= proj.size() - 1 ; p += 1)
			{
				proj.get(p).go(player, creaturesInMap, pet, DP) ;
				if (proj.get(0).collidedwith(player, creaturesInMap, pet) != - 3)	// if the projectile has hit something
				{
					proj.remove(p) ;
				}
			}
		}
		
		player.resetAction() ;

    	for (int i = 0 ; i <= ani.length - 1 ; i += 1) { ani[i].run(i, DP) ;}
	}
			
	private void testingInitialization()
	{
		// Quick start
		
		try {Potion.Initialize() ;} catch (IOException e) {e.printStackTrace() ;} // Initialize the list with all the potions
		try {Alchemy.Initialize() ;} catch (IOException e) {e.printStackTrace() ;} // Initialize the list with all the alchemy items
		try {Forge.Initialize() ;} catch (IOException e) {e.printStackTrace() ;} // Initialize the list with all the forge items
		try {PetItem.Initialize() ;} catch (IOException e) {e.printStackTrace() ;} // Initialize the list with all the pet items
		try {Food.Initialize() ;} catch (IOException e) {e.printStackTrace() ;} // Initialize the list with all the food items
		try {Arrow.Initialize() ;} catch (IOException e) {e.printStackTrace() ;} // Initialize the list with all the arrow items
		try {Equip.Initialize() ;} catch (IOException e) {e.printStackTrace() ;} // Initialize the list with all the equip items
		try {GeneralItem.Initialize() ;} catch (IOException e) {e.printStackTrace() ;} // Initialize the list with all the general items
		try {Fab.Initialize() ;} catch (IOException e) {e.printStackTrace() ;} // Initialize the list with all the fabrication items
		try {QuestItem.Initialize() ;} catch (IOException e) {e.printStackTrace() ;} // Initialize the list with all the quest items	
    	
    	// Minimum initialization
    	DayDuration = 120000 ;
    	sky = new Sky() ;
    	screen.setBorders(new int[] {0, sky.height, screen.getSize().width, screen.getSize().height});
    	screen.setMapCenter() ;
    	allSpellTypes = initializeSpellTypes(GameLanguage) ;
		allItems = initializeAllItems() ;
		creatureTypes = initializeCreatureTypes(GameLanguage, 1) ;
		initializeIcons(screen.getSize()) ;
		//allRecipes = LoadCraftingRecipes() ;
		NPCTypes = initializeNPCTypes(GameLanguage) ;
		buildingTypes = initializeBuildingTypes() ;
		
		cityMaps = initializeCityMaps() ;
		fieldMaps = initializeFieldMaps() ;
		specialMaps = initializeSpecialMaps() ;
		allQuests = initializeQuests(GameLanguage, player.getJob()) ;
		allMaps = new GameMap[cityMaps.length + fieldMaps.length + specialMaps.length] ;
		System.arraycopy(cityMaps, 0, allMaps, 0, cityMaps.length) ;
		System.arraycopy(fieldMaps, 0, allMaps, cityMaps.length, fieldMaps.length) ;
		System.arraycopy(specialMaps, 0, allMaps, cityMaps.length + fieldMaps.length, specialMaps.length) ;
		pet = new Pet((int) (4 * Math.random())) ;
    	pet.getPA().setLife(new BasicAttribute(100, 100, 1));
    	pet.setPos(player.getPos());
    	sideBar = new SideBar(player.getMovingAni().idleGif, pet.getMovingAni().idleGif) ;
    	bat = new Battle() ;
    	
    	player.InitializeSpells() ;
    	player.getSpellsTreeWindow().setSpells(player.getSpells().toArray(new Spell[0])) ;
    	player.setMap(cityMaps[1]) ;
    	player.setPos(new Point(60, screen.getSize().height / 2)) ;
//    	player.getBag().Add(Potion.getAll()[0], 3) ;
//    	player.getBag().Add(Potion.getAll()[0], 2) ;
//    	player.getBag().Add(Potion.getAll()[1], 2) ;
//    	player.getBag().Add(Potion.getAll()[2], 2) ;
//    	player.getBag().Add(Alchemy.getAll()[0], 2) ;
//    	player.getBag().Add(Alchemy.getAll()[2], 2) ;
//    	player.getBag().Add(Forge.getAll()[2], 3) ;
//    	System.out.println(player.getBag().numberItems);
//    	for (int i = 0; i <= 20 - 1; i += 1)
//    	{
//    		player.getBag().Add(Potion.getAll()[i], 3) ;
//    	}
    	//player.getPA().setExp(new BasicAttribute(50, 50, 1)) ;	// level up
    	//System.out.println("player life = " + player.getLife().getCurrentValue());
    	player.getLife().incCurrentValue(-10);
    	//System.out.println("player life = " + player.getLife().getCurrentValue());
		//System.out.println("player PA = " + player.getPA());
    	for (int i = 0 ; i <= Player.NumberOfSpellsPerJob[player.getJob()] - 1 ; i += 1)
    	{
        	player.getSpells().get(i).incLevel(1) ;
    	}

    	//System.out.println("player spells = " + player.getSpell());
    	//player.getSpell().add(new Spell(allSpellTypes[0])) ;
    	//player.getLife().incCurrentValue(-20);
    	
    	/*System.out.println("\nplayer life");
    	System.out.println(player.getLife());
    	player.applyBuff(true, player.getSpell().get(0).getBuffs().get(0));
    	//player.ApplyBuffsAndNerfs("activate", "", 0, player.getSpell().get(0).getBuffs().get(0), 0, false);
    	System.out.println("\nbuff activated");
    	System.out.println(player.getLife());
    	player.applyBuff(false, player.getSpell().get(0).getBuffs().get(0));
    	//player.ApplyBuffsAndNerfs("deactivate", "", 0, player.getSpell().get(0).getBuffs().get(0), 0, false);
    	System.out.println("\nbuff deactivated");
    	System.out.println(player.getLife());*/
    	
    	for (int i = 0; i <= fieldMaps.length - 1 ; i += 1)
    	{
        	player.bestiary.addDiscoveredCreature(fieldMaps[i].getCreatures().get(0).getType()) ;
    	}
    	Pterodactile.setMessage(player.allText.get("* Pterodactile *")) ;
    	/*System.out.println(player.getBag().getPotions()) ;
    	player.getBag().Add(Potion.getAll()[0], 4) ;
    	player.getBag().Add(Alchemy.getAll()[0], 1) ;
    	System.out.println(player.getBag().getPotions()) ;
    	player.getBag().Remove(Potion.getAll()[0], 3) ;
    	System.out.println(player.getBag().getPotions()) ;
    	player.getBag().useItem(player, Potion.getAll()[0]) ;
    	System.out.println(player.getBag().getPotions()) ;*/
    	if (player.getSettings().getMusicIsOn())
		{
			Music.SwitchMusic(player.getMap().getMusic()) ;
		}    	
	}
	
	private void testing()
	{
//		List<Item> ItemsOnSale = new ArrayList<>() ;
//		ItemsOnSale.add(allItems[0]) ;
//		ItemsOnSale.add(allItems[1]) ;
//		ItemsOnSale.add(allItems[2]) ;
//		ItemsOnSale.add(allItems[10]) ;
//		ItemsOnSale.add(allItems[3]) ;
//		ItemsOnSale.add(allItems[50]) ;
//		ItemsOnSale.add(allItems[60]) ;
//		ItemsOnSale.add(allItems[73]) ;
//		ItemsOnSale.add(allItems[24]) ;
//		ItemsOnSale.add(allItems[35]) ;
//		ItemsOnSale.add(allItems[14]) ;
//		ShoppingWindow SW = new ShoppingWindow(ItemsOnSale) ;
//		SW.display(mousePos, DP) ;
//		
//
//		List<Item> ItemsForCrafting = new ArrayList<>() ;
//		ItemsForCrafting.add(allItems[0]) ;
//		ItemsForCrafting.add(allItems[1]) ;
//		ItemsForCrafting.add(allItems[2]) ;
//		ItemsForCrafting.add(allItems[10]) ;
//		ItemsForCrafting.add(allItems[3]) ;
//		ItemsForCrafting.add(allItems[50]) ;
//		ItemsForCrafting.add(allItems[60]) ;
//		ItemsForCrafting.add(allItems[73]) ;
//		ItemsForCrafting.add(allItems[24]) ;
//		ItemsForCrafting.add(allItems[35]) ;
//		ItemsForCrafting.add(allItems[14]) ;
//		CraftWindow CW = new CraftWindow(ItemsForCrafting) ;
//		CW.display(mousePos, DP) ;
	}
	

	
	/*private void battleSimulation()
	{
    	GameLanguage = "P" ;
		AllText = Utg.ReadTextFile(GameLanguage) ;
		AllTextCat = Uts.FindAllTextCat(AllText, GameLanguage) ;
    	PlayerName = "" ;
    	PlayerJob = 1 ;
    	PlayerSex = "N" ;
    	MainInitialization() ;
    	OpeningIsOn = false ;
    	RunGame = true ;
		//player.PrintAllAttributes();
		player.CreatureInBattle = 85;
		//creatureTypes[creature[player.CreatureInBattle].getType()].printAtt() ;
		player.setPos(creature[player.CreatureInBattle].getPos()) ;
		player.setPos(new int[] {player.getPos()[0] + 20, player.getPos()[1] + 15});
		//player.getBA().setStun(new double[] {1, 0, 0, 0, 500});
		player.getEquips()[0] = 301 ;
		player.getEquips()[1] = 302 ;
		player.getEquips()[2] = 303 ;
		player.setCurrentAction("Fighting");
	}*/
	
	@Override
	protected void paintComponent(Graphics g)
	{
        super.paintComponent(g) ;
		mousePos = UtilG.GetMousePos(mainPanel) ;
        DP.setGraphics((Graphics2D) g);

    	//System.out.println("game is " + gameState);
        switch (state)
        {
	        case opening:
	        {
	        	if (!opening.getOpeningGif().isDonePlaying())
	        	{
		    		opening.getOpeningGif().play(new Point(0, 0), Align.topLeft, DP);
	        	}
	        	else
	        	{
		    		opening.Run(player.getCurrentAction(), mousePos, DP) ;
		    		if (opening.isOver())
		    		{
		    			state = GameStates.loading ;
		    		}
					player.resetAction(); ;
	        	}
				shouldRepaint = true ;
	    		
	    		break ;
	        }
	        case loading:
	        {
	        	//loading.displayText(DP) ;
				//MainInitialization() ;
	    		testingInitialization() ;
				state = GameStates.running;
				
				shouldRepaint = true ;
				
	    		break ;
	        }
	        case running:
	        {
	        	runGame(DP) ;
				testing() ;
				playGifs(DP) ;

	    		break ;
	        }
	        case playingStopTimeGif:
	        {
	        	playStopTimeGifs();
				 
				break ;
	        }
	        case paused:
	        {

	    		break ;
	        }
        }        
        
        Toolkit.getDefaultToolkit().sync() ;
        g.dispose() ;  
    }
		
	class TAdapter extends KeyAdapter 
	{	
	    @Override
	    public void keyPressed(KeyEvent e) 
	    {
	        int key = e.getKeyCode() ;
	        
            if (key == KeyEvent.VK_LEFT | key == KeyEvent.VK_UP | key == KeyEvent.VK_DOWN | key == KeyEvent.VK_RIGHT) 
            {
            	player.setCurrentAction(KeyEvent.getKeyText(key)) ;
            }
            else if (key == KeyEvent.VK_ENTER | key == KeyEvent.VK_ESCAPE | key == KeyEvent.VK_BACK_SPACE | key == KeyEvent.VK_F1 | key == KeyEvent.VK_F2 | key == KeyEvent.VK_F3 | key == KeyEvent.VK_F4 | key == KeyEvent.VK_F5 | key == KeyEvent.VK_F6 | key == KeyEvent.VK_F7 | key == KeyEvent.VK_F8 | key == KeyEvent.VK_F9 | key == KeyEvent.VK_F10 | key == KeyEvent.VK_F11 | key == KeyEvent.VK_F12 | key == KeyEvent.VK_A | key == KeyEvent.VK_B | key == KeyEvent.VK_C | key == KeyEvent.VK_D | key == KeyEvent.VK_E | key == KeyEvent.VK_F | key == KeyEvent.VK_G | key == KeyEvent.VK_H | key == KeyEvent.VK_I | key == KeyEvent.VK_J | key == KeyEvent.VK_K | key == KeyEvent.VK_L | key == KeyEvent.VK_M | key == KeyEvent.VK_N | key == KeyEvent.VK_O | key == KeyEvent.VK_P | key == KeyEvent.VK_Q | key == KeyEvent.VK_R | key == KeyEvent.VK_S | key == KeyEvent.VK_T | key == KeyEvent.VK_U | key == KeyEvent.VK_V | key == KeyEvent.VK_W | key == KeyEvent.VK_X | key == KeyEvent.VK_Y | key == KeyEvent.VK_Z | key == KeyEvent.VK_0 | key == KeyEvent.VK_1 | key == KeyEvent.VK_2 | key == KeyEvent.VK_3 | key == KeyEvent.VK_4 | key == KeyEvent.VK_5 | key == KeyEvent.VK_6 | key == KeyEvent.VK_7 | key == KeyEvent.VK_8 | key == KeyEvent.VK_9) 
            {
        		player.setCurrentAction(KeyEvent.getKeyText(key)) ;
            }
            else if (key == KeyEvent.VK_SPACE)
            {
        		player.setCurrentAction(KeyEvent.getKeyText(key)) ;
            }
            else if (key == KeyEvent.VK_NUMPAD0 | key == KeyEvent.VK_NUMPAD1 | key == KeyEvent.VK_NUMPAD2 | key == KeyEvent.VK_NUMPAD3 | key == KeyEvent.VK_NUMPAD4 | key == KeyEvent.VK_NUMPAD5 | key == KeyEvent.VK_NUMPAD6 | key == KeyEvent.VK_NUMPAD7 | key == KeyEvent.VK_NUMPAD8 | key == KeyEvent.VK_NUMPAD9)
            {
        		player.setCurrentAction(String.valueOf(key - 96)) ;
            }
            else if (key == KeyEvent.VK_PAUSE) 
            {
            	
            }
            shouldRepaint = true ;
	    }
	
	    @Override
	    public void keyReleased(KeyEvent e) 
	    {

	    }
	}
	
	class MouseEventDemo implements MouseListener 
	{
		@Override
		public void mouseClicked(MouseEvent evt)
		{
		}

		@Override
		public void mouseEntered(MouseEvent arg0)
		{
			
		}

		@Override
		public void mouseExited(MouseEvent arg0)
		{
			
		}

		@Override
		public void mousePressed(MouseEvent evt)
		{
			if (evt.getButton() == 1)	// Left click
			{
        		player.setCurrentAction("MouseLeftClick") ;

//        		TestingAnimations.runTests(ani) ;
        		//testGif.start();
			}
			if (evt.getButton() == 3)	// Right click
			{
        		player.setCurrentAction("MouseRightClick") ;
        		//testGif2.start();
			}
            //shouldRepaint = true ;
		}

		@Override
		public void mouseReleased(MouseEvent e) 
		{

		}		
	}

//  private void mainInitialization()
//	{
//		DayDuration = 120000 ;
//  	sky = new Sky() ;
//  	screen.setBorders(new int[] {0, sky.height, screen.getSize().width, screen.getSize().height});
//  	screen.setMapCenter() ;    			
//  	GameMap.InitializeStaticVars(ImagesPath) ;
//		//allNPCs = InitializeNPCTypes(GameLanguage, screen.getSize()) ;
//		buildingTypes = initializeBuildingTypes() ;
//		creatureTypes = initializeCreatureTypes(GameLanguage, 1) ;
//		cityMaps = initializeCityMaps() ;
//		fieldMaps = initializeFieldMaps() ;
//		allMaps = new GameMap[cityMaps.length + fieldMaps.length] ;
//		System.arraycopy(cityMaps, 0, allMaps, 0, cityMaps.length) ;
//		System.arraycopy(fieldMaps, 0, allMaps, cityMaps.length, fieldMaps.length) ;
//		//DifficultMult = new double[] {(double) 0.5, (double) 0.7, (double) 1.0} ;
//		//player = InitializePlayer(PlayerInitialName, PlayerInitialJob, GameLanguage, PlayerInitialSex) ;
//		//pet = InitializePet() ;
//		//creature = InitializeCreatures(creatureTypes, screen.getSize(), fieldMaps) ;
//		for (int map = 0 ; map <= allMaps.length - 1 ; map += 1)
//		{
//			//allMaps[map].InitializeNPCsInMap(allNPCs) ;
//		}
//		allQuests = initializeQuests(GameLanguage, player.getJob()) ;
//		initializeIcons(screen.getSize()) ;
//
//		// Initialize classes
//  	bat = new Battle(new int[] {player.getBA().getBattleActions()[0][1]/2, pet.getBA().getBattleActions()[0][1]/2}, ani) ;
//	}
	
}