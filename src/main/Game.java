package main ;

import java.awt.Color ;
import java.awt.Dimension ;
import java.awt.Graphics;
import java.awt.Graphics2D ;
import java.awt.Image ;
import java.awt.Point ;
import java.awt.Toolkit ;
import java.awt.event.KeyAdapter ;
import java.awt.event.KeyEvent ;
import java.awt.event.MouseEvent ;
import java.awt.event.MouseListener ;
import java.awt.event.MouseWheelEvent ;
import java.awt.event.MouseWheelListener ;
import java.util.ArrayList ;
import java.util.Arrays ;
import java.util.HashMap ;
import java.util.Iterator ;
import java.util.List ;
import java.util.Map ;

import javax.sound.sampled.Clip ;
import javax.swing.JPanel ;

import org.json.simple.JSONArray ;
import org.json.simple.JSONObject ;

import attributes.Attributes;
import components.Building ;
import components.BuildingNames ;
import components.BuildingType ;
import components.NPCJobs ;
import components.NPCType ;
import components.NPCs ;
import components.Projectiles ;
import components.Quest ;
import components.QuestSkills;
import graphics.Animation ;
import graphics.Draw ;
import graphics.DrawPrimitives;
import graphics.Gif;
import items.Alchemy ;
import items.Arrow ;
import items.Equip ;
import items.Fab ;
import items.Food ;
import items.Forge ;
import items.GeneralItem;
import items.Item ;
import items.PetItem ;
import items.Potion ;
import items.QuestItem ;
import items.Recipe ;
import libUtil.Align;
import libUtil.Util;
import liveBeings.Buff ;
import liveBeings.Creature ;
import liveBeings.CreatureType ;
import liveBeings.HotKeysBar;
import liveBeings.LiveBeing;
import liveBeings.LiveBeingStatus;
import liveBeings.Pet ;
import liveBeings.Player ;
import liveBeings.Spell ;
import liveBeings.SpellsBar;
import maps.CityMap ;
import maps.Continents ;
import maps.FieldMap ;
import maps.GameMap ;
import maps.GroundType ;
import maps.GroundTypes ;
import maps.SpecialMap ;
import maps.TreasureChest ;
import screen.Screen ;
import screen.SideBar ;
import screen.Sky ;
import simulations.EvolutionSimulation;
import utilities.GameStates ;
import utilities.Log;
import utilities.Scale ;
import utilities.TimeCounter;
import utilities.UtilS ;
import windows.BankWindow ;

public class Game extends JPanel
{
	// TODO arte - soundtrack
	// TODO arquivos - nomes das criaturas
	// TODO optional - unificar throw item, calcPhysicalAtk e useSpell dos liveBeings
	private static final long serialVersionUID = 1L ;
	private static final String[] konamiCode = new String[] { "Acima", "Acima", "Abaixo", "Abaixo", "Esquerda", "Direita", "Esquerda", "Direita", "B", "A" } ;
	public static final String JSONPath = ".\\json\\" ;
	public static final String CSVPath = ".\\csv\\" ;
	public static final String ImagesPath = ".\\images\\" ;
	public static final String MusicPath = ".\\music\\" ;
	public static final String TextPathBR = "./Texto-PT-br.json" ;
	public static final String MainFontName = "Comics" ;

	private JPanel mainPanel = this ;
	private static Point mousePos ;
	private static GameStates state = GameStates.loading ;
	private static boolean cheatMode = true ;
	private static Languages gameLanguage ;
	private static boolean shouldRepaint ; // tells if the panel should be repainted, created to respond multiple requests only once
	private static boolean konamiCodeActive ;

	public static Color[] colorPalette ;
	public static Map<TextCategories, String[]> allText ;

	private DrawPrimitives DP ;
	private static Player player ;
	private static Pet pet ;
	public static int difficultLevel = 2 ;
	private static int slotLoaded = -1 ;

	private static Screen screen ;
	private static Sky sky ;
	private static CityMap[] cityMaps ;
	private static FieldMap[] fieldMaps ;
	private static SpecialMap[] specialMaps ;
	private static GameMap[] allMaps ;
	private static BuildingType[] buildingTypes ;
	private static NPCType[] NPCTypes ;
	private static Item[] allItems ;
	private static Spell[] allSpells ;
	private static Quest[] allQuests ;
	private static List<Projectiles> projs ;

	static
	{
		Dimension windowSize = MainGame3_4.getWindowsize() ;
		screen = new Screen(new Dimension(windowSize.width - 40, windowSize.height), null) ;
		screen.calcCenter() ;
		gameLanguage = Languages.portugues ;
//		state = GameStates.loading ;
		colorPalette = UtilS.ReadColorPalette(UtilS.loadImage("ColorPalette4.png"), "Normal") ;
		konamiCodeActive = false ;
//		initializeAnimations() ;

		allText = new HashMap<>() ;
		shouldRepaint = false ;
	}

	public Game()
	{
		DP = new DrawPrimitives() ;
		player = new Player("", "", 1) ;

		addMouseListener(new MouseEventDemo()) ;
		addMouseWheelListener(new MouseWheelEventDemo()) ;
		addKeyListener(new TAdapter()) ;
		setFocusable(true) ;
	}

	public static GameStates getState() { return state ;}
	public static Languages getLanguage() { return gameLanguage ;}
	public static Screen getScreen() { return screen ;}
	public static Sky getSky() { return sky ;}
	public static NPCType[] getNPCTypes() { return NPCTypes ;}
	public static Player getPlayer() { return player ;}
	public static Pet getPet() { return pet ;}
	public static BuildingType[] getBuildingTypes() { return buildingTypes ;}
	public static GameMap[] getMaps() { return allMaps ;}
	public static Quest[] getAllQuests() { return allQuests ;}
	public static Item[] getAllItems() { return allItems ;}
	public static Spell[] getAllSpells() { return allSpells ;}
	public static boolean getShouldRepaint() { return shouldRepaint ;}
	public static Point getmousePos() { return mousePos ;}
	public static int getSlotLoaded() { return slotLoaded ;}
	public static void setSlotLoaded(int newSlotLoaded) { slotLoaded = newSlotLoaded ;}
	
	public static List<Item> getItems(int[] itemIDs)
	{
		List<Item> items = new ArrayList<>() ;
    	for (int itemID : itemIDs)
    	{
    		items.add(Item.allItems.get(itemID)) ;
    	}
    	
    	return items ;
	}
	
	public static void setPlayer(Player newPlayer) { player = newPlayer ;}
	public static void setState(GameStates newState) { state = newState ;}
	public static void playStopTimeGif() { state = GameStates.playingStopTimeGif ;}
	public static void shouldNotRepaint() { shouldRepaint = false ;}

	private static void checkKonamiCode(List<String> combo)
	{
		if (!Arrays.equals(combo.toArray(new String[combo.size()]), konamiCode)) { return ;}
		System.out.println("Activating konami code!");
		konamiCodeActive = !konamiCodeActive ;
		player.resetCombo() ;
	}

	private static void konamiCode()
	{
		Sky.dayCounter.setDuration(5) ;
		colorPalette = UtilS.ReadColorPalette(UtilS.loadImage("ColorPalette.png"), "Konami") ;
		if (Sky.dayCounter.getCounter() % 1200 <= 300)
		{
			Draw.stdAngle += 0.04 ;
		} else if (Sky.dayCounter.getCounter() % 1200 <= 900)
		{
			Draw.stdAngle -= 0.04 ;
		} else
		{
			Draw.stdAngle += 0.04 ;
		}
	}

	public static void removePet() { pet = null ;}

	public static void letThereBePet()
	{
		int job = Util.randomIntFromTo(0, 3) ;
		pet = new Pet(job) ;
		pet.setPos(player.getPos()) ;
		if (player.getJob() == 3 & 0 < player.getSpells().get(13).getLevel()) // Best friend
		{
			int spellLevel = player.getSpells().get(13).getLevel() ;
			pet.getPA().getLife().incMaxValue(10 * spellLevel) ;
			pet.getPA().getLife().setToMaximum() ;
			pet.getPA().getMp().incMaxValue(10 * spellLevel) ;
			pet.getPA().getMp().setToMaximum() ;
			pet.getBA().getPhyAtk().incBaseValue(2 * spellLevel) ;
			pet.getBA().getMagAtk().incBaseValue(2 * spellLevel) ;
			pet.getBA().getDex().incBaseValue(1 * spellLevel) ;
			pet.getBA().getAgi().incBaseValue(1 * spellLevel) ;
		}
		SideBar.setPet(player, pet) ;
	}
	
	
	private static void loadNPCText(JSONObject textData, Object key, TextCategories catName)
	{
		JSONArray npcData = (JSONArray) textData.get(key) ;

		for (int i = 0 ; i <= npcData.size() - 1 ; i += 1)
		{
			JSONObject npc = (JSONObject) npcData.get(i) ;
			String npcName = (String) npc.get("Nome") ;
			List<String> falas = (List<String>) npc.get("Falas") ;
			List<JSONArray> opcoes = (List<JSONArray>) npc.get("Opcoes") ;
			TextCategories textCatName = TextCategories.catFromBRName(catName + "Nome") ;
			TextCategories textCatFala = TextCategories.catFromBRName(catName + npcName + "Falas") ;

			if (textCatName != null)
			{
				allText.put(textCatName, falas.toArray(new String[0])) ;
			}

			if (textCatFala != null)
			{
				allText.put(textCatFala, falas.toArray(new String[0])) ;
			}

			for (int j = 0 ; j <= opcoes.size() - 1 ; j += 1)
			{
				List<String> opcoesMenu = (List<String>) opcoes.get(j) ;
				TextCategories textCatOption = TextCategories
						.catFromBRName(catName + npcName + "Opcoes" + j) ;

				if (textCatOption == null)
				{
					continue ;
				}

				allText.put(textCatOption, opcoesMenu.toArray(new String[0])) ;
			}
		}
	}
	
	private static void loadAllText()
	{

		JSONObject textData = Util.readJsonObject(TextPathBR) ;

		Iterator<?> iterator = textData.keySet().iterator() ;

		while (iterator.hasNext())
		{
			Object key = iterator.next() ;
			TextCategories catName = TextCategories.catFromBRName((String) key) ;

			if (catName == null) { continue ;}
			
			if (catName.equals(TextCategories.npcs))
			{
				loadNPCText(textData, key, catName) ;
				continue ;
			}

			JSONArray listText = (JSONArray) textData.get(key) ;

			List<String> listValues = new ArrayList<>() ;
			for (int j = 0 ; j <= listText.size() - 1 ; j += 1)
			{
				listValues.add((String) listText.get(j)) ;
			}

			allText.put(catName, listValues.toArray(new String[] {})) ;
		}

//		allText.entrySet().forEach(text -> System.out.println(text.getKey() + " " + Arrays.toString(text.getValue()))) ;

	}

	private static NPCs readNPCfromJson(JSONObject npcJSONObject)
	{

		JSONObject npc = npcJSONObject ;
		String npcName = (String) npc.get("name") ;
		double posX = (Double) ((JSONObject) npc.get("pos")).get("x") ;
		double posY = (Double) ((JSONObject) npc.get("pos")).get("y") ;
		Point npcPos = screen.getPointWithinBorders(posX, posY) ;
		NPCType npcType = NPCs.typeFromName(npcName) ;
		return new NPCs(npcType, npcPos) ;

	}

	private static NPCType[] loadNPCTypes(Languages language)
	{
		List<String[]> input = Util.ReadcsvFile(CSVPath + "NPCTypes.csv") ;
		NPCType[] npcType = new NPCType[input.size()] ;
		for (int i = 0 ; i <= npcType.length - 1 ; i += 1)
		{
			String name = input.get(i)[language.ordinal()] ;
			NPCJobs job = NPCJobs.valueOf(input.get(i)[2]) ;
			String info = input.get(i)[3 + language.ordinal()] ;
			Color color = job.getColor() ;
			String imageExtension = !job.equals(NPCJobs.master) ? ".png" : ".gif" ;
			Image image = UtilS.loadImage("\\NPCs\\" + "NPC_" + job.toString() + imageExtension) ;
			String[] speech = null ;
			List<List<String>> options = new ArrayList<>() ;
			TextCategories speechName = TextCategories.catFromBRName("npcs" + name + "Falas") ;

			if (Game.allText.get(speechName) != null)
			{
				speech = Game.allText.get(speechName) ;

				for (int o = 0 ; o <= speech.length - 1 ; o += 1)
				{
					TextCategories optionName = TextCategories.catFromBRName("npcs" + name + "Opcoes" + o) ;

					if (Game.allText.get(optionName) != null)
					{
						List<String> option = Arrays.asList(Game.allText.get(optionName)) ;
						options.add(option) ;
					}
				}
			}
//			System.out.println(job + " " + speech + " " + options) ;

			npcType[i] = new NPCType(name, job, info, color, image, speech, options) ;
		}

		return npcType ;
	}

	private static BuildingType[] loadBuildingTypes()
	{
		JSONArray input = Util.readJsonArray("./json/buildingTypes.json") ;
		BuildingType[] buildingTypes = new BuildingType[input.size()] ;
		String path = ImagesPath + "\\Buildings\\" ;
		for (int i = 0 ; i <= input.size() - 1 ; i += 1)
		{
			JSONObject type = (JSONObject) input.get(i) ;
			BuildingNames name = BuildingNames.valueOf((String) type.get("name")) ;
			Image outsideImage = Util.loadImage(path + "Building" + name + ".png") ;

			buildingTypes[i] = new BuildingType(name, outsideImage) ;

			boolean hasInterior = (boolean) type.get("hasInterior") ;
			if (hasInterior)
			{
				Image insideImage = Util.loadImage(path + "Building" + name + "Inside.png") ;
				Image[] OrnamentImages = new Image[] { Util.loadImage(path + "Building" + name + "Ornament.png") } ;
				buildingTypes[i].setInsideImage(insideImage) ;
				buildingTypes[i].setOrnamentImages(OrnamentImages) ;
			}
		}

		return buildingTypes ;
	}

	private static CityMap[] loadCityMaps()
	{
		JSONArray input = Util.readJsonArray(Game.JSONPath + "mapsCity.json") ;
		CityMap[] cityMaps = new CityMap[input.size()] ;

		for (int id = 0 ; id <= input.size() - 1 ; id += 1)
		{
			JSONObject map = (JSONObject) input.get(id) ;

			String name = (String) map.get("Name") ;
			int continentID = (int) (long) map.get("Continent") ;
			Continents continent = Continents.values()[continentID] ;
			JSONObject connectionIDs = (JSONObject) map.get("Connections") ;
			int[] connections = new int[8] ;
			connections[0] = (int) (long) connectionIDs.get("topRight") ;
			connections[1] = (int) (long) connectionIDs.get("topLeft") ;
			connections[2] = (int) (long) connectionIDs.get("leftTop") ;
			connections[3] = (int) (long) connectionIDs.get("leftBottom") ;
			connections[4] = (int) (long) connectionIDs.get("bottomLeft") ;
			connections[5] = (int) (long) connectionIDs.get("bottomRight") ;
			connections[6] = (int) (long) connectionIDs.get("rightBottom") ;
			connections[7] = (int) (long) connectionIDs.get("rightTop") ;

			Image image = CityMap.images.get(id) ;
			Clip music = GameMap.musicForest ;

			JSONArray listBuildings = (JSONArray) map.get("Buildings") ;
			List<Building> buildings = new ArrayList<>() ;
			for (int i = 0 ; i <= listBuildings.size() - 1 ; i += 1)
			{
				JSONObject building = (JSONObject) listBuildings.get(i) ;
				BuildingNames buildingName = BuildingNames.valueOf((String) building.get("name")) ;
				double posX = (Double) ((JSONObject) building.get("pos")).get("x") ;
				double posY = (Double) ((JSONObject) building.get("pos")).get("y") ;
				Point buildingPos = screen.getPointWithinBorders(posX, posY) ;

				BuildingType buildingType = null ;
				for (BuildingType type : buildingTypes)
				{
					if (!buildingName.equals(type.getName()))
					{
						continue ;
					}

					buildingType = type ;
				}

				buildings.add(new Building(buildingType, buildingPos)) ;

			}

			JSONArray listNPCs = (JSONArray) map.get("NPCs") ;
			List<NPCs> npcs = new ArrayList<>() ;
			for (int i = 0 ; i <= listNPCs.size() - 1 ; i += 1)
			{
				NPCs npc = readNPCfromJson((JSONObject) listNPCs.get(i)) ;
				npcs.add(npc) ;
			}

			cityMaps[id] = new CityMap(name, continent, connections, image, music, buildings, npcs) ;
			
			switch (id)
			{
				case 2:
					cityMaps[id]
							.addGroundType(new GroundType(GroundTypes.water, new Point(500, Sky.height), new Dimension(140, 480 - Sky.height))) ;
					break ;
				default: break ;
			}
		}

		return cityMaps ;
	}

	private static FieldMap[] loadFieldMaps()
	{
		JSONArray input = Util.readJsonArray(Game.JSONPath + "mapsField.json") ;
		FieldMap[] fieldMaps = new FieldMap[input.size()] ;

		for (int id = 0 ; id <= input.size() - 1 ; id += 1)
		{

			JSONObject mapData = (JSONObject) input.get(id) ;

			String name = (String) mapData.get("Name") ;
			int continentID = (int) (long) mapData.get("Continent") ;
			Continents continent = Continents.values()[continentID] ;
			JSONObject connectionIDs = (JSONObject) mapData.get("Connections") ;
			int[] connections = new int[8] ;
			connections[0] = (int) (long) connectionIDs.get("topRight") ;
			connections[1] = (int) (long) connectionIDs.get("topLeft") ;
			connections[2] = (int) (long) connectionIDs.get("leftTop") ;
			connections[3] = (int) (long) connectionIDs.get("leftBottom") ;
			connections[4] = (int) (long) connectionIDs.get("bottomLeft") ;
			connections[5] = (int) (long) connectionIDs.get("bottomRight") ;
			connections[6] = (int) (long) connectionIDs.get("rightBottom") ;
			connections[7] = (int) (long) connectionIDs.get("rightTop") ;

			Image image = FieldMap.images.get(id) ;
			Clip music = GameMap.musicForest ;

			JSONObject collectibles = (JSONObject) mapData.get("Collectibles") ;
			int collectibleLevel = (int) (long) collectibles.get("level") ;

			List<Long> creatures = (List<Long>) mapData.get("Creatures") ;
			int[] creatureIDs = new int[creatures.size()] ;
			for (int i = 0 ; i <= creatures.size() - 1 ; i += 1)
			{
				creatureIDs[i] = (int) (long) creatures.get(i) ;
			}

			JSONArray listNPCs = (JSONArray) mapData.get("NPCs") ;
			List<NPCs> npcs = FieldMap.createQuestNPCs(id) ;
//			List<NPCs> npcs = new ArrayList<>() ;
//			for (int i = 0 ; i <= listNPCs.size() - 1 ; i += 1)
//			{
//				npcs.add(readNPCfromJson((JSONObject) listNPCs.get(i))) ;
//			}

			FieldMap map = new FieldMap(name, continent, connections, image, music, collectibleLevel,
					creatureIDs, npcs) ;

			switch (id)
			{
//			case 1:
//				map.addGroundType(new GroundType(GroundTypes.water, new Point(120, 200), new Dimension(50, 20))) ;
//				break ;
//			case 3:
//				map.addGroundType(new GroundType(GroundTypes.water, new Point(120, 100), new Dimension(45, 30))) ;
//				break ;
//			case 8:
//				map
//						.addGroundType(new GroundType(GroundTypes.water, new Point(500, Sky.height), new Dimension(140, 480 - Sky.height))) ;
//				break ;
//			case 9:
//				map
//						.addGroundType(new GroundType(GroundTypes.water, new Point(50, 250), new Dimension(120, 210))) ;
//				break ;
//			case 12:
//				map
//						.addGroundType(new GroundType(GroundTypes.water, new Point(500, Sky.height), new Dimension(140, 480 - Sky.height))) ;
//				break ;
			case 13:
				map.addGroundType(new GroundType(GroundTypes.water, new Point(50, 250), new Dimension(120, 210))) ;
				break ;
//			case 14:
//				map
//						.addGroundType(new GroundType(GroundTypes.water, new Point(50, 250), new Dimension(120, 210))) ;
//				break ;
//			case 15:
//				map
//						.addGroundType(new GroundType(GroundTypes.water, new Point(50, 250), new Dimension(120, 210))) ;
//				break ;
			case 22:
				map.addGroundType(new GroundType(GroundTypes.water, new Point(50, 250), new Dimension(120, 210))) ;
				break ;
			default:
				break ;
			}

			fieldMaps[id] = map ;
		}

		return fieldMaps ;
	}

	private static SpecialMap[] loadSpecialMaps(List<Item> allItems)
	{
		List<String[]> input = Util.ReadcsvFile(CSVPath + "MapsSpecial.csv") ;
		SpecialMap[] specialMaps = new SpecialMap[input.size()] ;
		


		for (int id = 0 ; id <= specialMaps.length - 1 ; id += 1)
		{
			String name = input.get(id)[0] ;
			Continents continent = Continents.values()[Integer.parseInt(input.get(id)[1])] ;
			int[] connections = new int[] { Integer.parseInt(input.get(id)[9]), Integer.parseInt(input.get(id)[2]),
					Integer.parseInt(input.get(id)[3]), Integer.parseInt(input.get(id)[4]),
					Integer.parseInt(input.get(id)[5]), Integer.parseInt(input.get(id)[6]),
					Integer.parseInt(input.get(id)[7]), Integer.parseInt(input.get(id)[8]) } ;

			Image image = SpecialMap.images.get(id) ;
			Clip music = GameMap.musicForest ;
			
			// adding treasure chests
			List<TreasureChest> treasureChests = new ArrayList<>() ;
			for (int chest = 0 ; chest <= 5 - 1 ; chest += 1)
			{
				Point pos = new Point(
						(int) (Double.parseDouble(input.get(id)[10 + 13 * chest]) * screen.getSize().width),
						(int) (Double.parseDouble(input.get(id)[11 + 13 * chest]) * screen.getSize().height)) ;
				List<Item> itemRewards = new ArrayList<>() ;
				for (int item = 0 ; item <= 10 - 1 ; item += 1)
				{
					int itemID = Integer.parseInt(input.get(id)[12 + 13 * chest + item]) ;
					if (-1 < itemID)
					{
						itemRewards.add(allItems.get(itemID)) ;
					}
				}
				int goldReward = Integer.parseInt(input.get(id)[22 + 13 * chest]) ;
				treasureChests.add(new TreasureChest(chest, pos, itemRewards, goldReward)) ;
			}
			specialMaps[id] = new SpecialMap(name, continent, connections, image, music, treasureChests) ;
		}

		return specialMaps ;
	}

	private static GameMap[] loadAllMaps(List<Item> allItems)
	{

		cityMaps = loadCityMaps() ;
		fieldMaps = loadFieldMaps() ;
		specialMaps = loadSpecialMaps(allItems) ;
		GameMap[] allMaps = new GameMap[cityMaps.length + fieldMaps.length + specialMaps.length] ;
		for (int i = 0 ; i <= cityMaps.length - 1 ; i += 1)
		{
			allMaps[i] = cityMaps[i] ;
		}
		for (int i = cityMaps.length ; i <= cityMaps.length + 34 - 1 ; i += 1)
		{
			allMaps[i] = fieldMaps[i - cityMaps.length] ;
		}
		allMaps[39] = specialMaps[0] ;
		for (int i = 40 ; i <= 60 - 1 ; i += 1)
		{
			allMaps[i] = fieldMaps[i - cityMaps.length - 1] ;
		}
		allMaps[60] = specialMaps[1] ;
		for (int i = 61 ; i <= cityMaps.length + fieldMaps.length + specialMaps.length - 1 ; i += 1)
		{
			allMaps[i] = fieldMaps[i - cityMaps.length - 2] ;
		}
//		System.arraycopy(cityMaps, 0, allMaps, 0, cityMaps.length) ;
//		System.arraycopy(fieldMaps, 0, allMaps, cityMaps.length, fieldMaps.length) ;
//		System.arraycopy(specialMaps, 0, allMaps, cityMaps.length + fieldMaps.length, specialMaps.length) ;
		return allMaps ;

	}

	private static Quest[] loadQuests(Languages language, int playerJob, List<CreatureType> creatureTypes, List<Item> allItems)
	{
		List<String[]> inputs = Util.ReadcsvFile(CSVPath + "Quests.csv") ;
		Quest[] quests = new Quest[inputs.size()] ;
		for (int i = 0 ; i <= quests.length - 1 ; i += 1)
		{
			String[] input = inputs.get(i) ;
			int id = Integer.parseInt(input[0]) ;
			String type = input[1] ;
			Map<CreatureType, Integer> reqCreatureTypes = new HashMap<>() ;
			Map<Item, Integer> reqItems = new HashMap<>() ;

			for (int j = 2 ; j <= 8 - 1 ; j += 2)
			{
				if (Integer.parseInt(input[j]) <= -1) { continue ;}
				
				reqCreatureTypes.put(creatureTypes.get(Integer.parseInt(input[j])), Integer.parseInt(input[j + 1])) ;
			}

			for (int j = 8 ; j <= 18 - 1 ; j += 2)
			{
				if (Integer.parseInt(input[j]) <= -1) { continue ;}
				
				reqItems.put(allItems.get(Integer.parseInt(input[j])), Integer.parseInt(input[j + 1])) ;
			}

			int goldReward = Integer.parseInt(input[18]) ;
			int expReward = Integer.parseInt(input[19]) ;
			boolean isRepeatable = 0 <= expReward ;
			Map<Item, Integer> rewardItems = new HashMap<>() ;

			for (int j = 20 ; j <= 28 - 1 ; j += 2)
			{
				if (Integer.parseInt(input[j + 1]) <= -1) { continue ;}
				
				rewardItems.put(allItems.get(Integer.parseInt(input[j])), Integer.parseInt(input[j + 1])) ;
			}

			String description = input[30 + language.ordinal()] ;

			quests[i] = new Quest(id, type, isRepeatable, reqCreatureTypes, reqItems, goldReward, expReward,
					rewardItems, description) ;
		}

		return quests ;
	}
	
	
	private void incrementCounters()
	{	
		if (Sky.dayCounter.finished())
		{
			Sky.dayCounter.reset() ;
			Sky.dayCounter.start() ;
			NPCs.renewStocks() ;
		}
		player.activateSpellCounters() ;

		if (pet != null)
		{
			pet.activateSpellCounters() ;
		}

		if (player.getMap().isAField())
		{
			FieldMap fm = (FieldMap) player.getMap() ;
			fm.getCreatures().forEach(Creature::activateSpellCounters) ;
		}

		for (CityMap city : cityMaps)
		{
			BankWindow bank = (BankWindow) city.getBuildings().get(3).getNPCs().get(0).getWindow() ;
			if (bank.hasInvestment())
			{
//				System.out.println(bank.isInvested() + " " + bank.getInvestmentCounter()) ;
//				bank.incInvestmentCounter() ;
				if (bank.investmentIsComplete())
				{
					bank.completeInvestment() ;
//					System.out.println("balance = " + bank.getBalance()) ;
				}
			}
		}

	}

	private void activateCounters()
	{

		player.activateCounters() ;
		if (pet != null)
		{
			if (0 < pet.getLife().getCurrentValue())
			{
				 pet.activateCounters() ;
			}
		}

		if (!player.getMap().isAField()) { return ;}

		FieldMap fm = (FieldMap) player.getMap() ;
		fm.getCreatures().forEach(Creature::activateCounters) ;			
		fm.activateCollectiblesCounter() ;

	}

	private void playStopTimeGifs()
	{
		if (Opening.getOpeningGif().isTimeStopper())
		{
//			Opening.getOpeningGif().start(new Point(0, 0), Align.topLeft) ;

			repaint() ;
		}
	}

	private void playGifs(DrawPrimitives DP)
	{
		/*
		 * if (!testGif.isTimeStopper()) { testGif.play(new Point(100, 100),
		 * AlignmentPoints.topLeft, DP) ;
		 * 
		 * repaint() ; }
		 */
		/*
		 * if (testGif.isPlaying()) { testGif.play(mousePos, Align.center, DP) ;
		 * shouldRepaint = true ; } if (testGif2.isPlaying()) { testGif2.play(new
		 * Point(300, 100), Align.center, DP) ; shouldRepaint = true ; }
		 */
	}

	
	private void creaturesAct()
	{
		List<Creature> creaturesInMap = ((FieldMap) player.getMap()).getCreatures() ;
		for (Creature creature : creaturesInMap)
		{
			creature.takeBloodAndPoisonDamage() ;
			if (creature.isMoving())
			{
				creature.move(player.getPos(), player.getMap()) ;
				creature.display(creature.getPos(), Scale.unit, DP) ;
				continue ;
			}
			if (creature.canAct())
			{
				creature.think() ;
				creature.act() ;
			}
			creature.display(creature.getPos(), Scale.unit, DP) ;
//			creature.getBA().finishStatus() ;
//			creature.displayAdditionalInfo(DP) ;
		}
		shouldRepaint = true ;
	}
	
	private void petActs()
	{
		if (!pet.isAlive()) { return ;}

		pet.takeBloodAndPoisonDamage() ;
		pet.updateCombo() ;
		pet.think(player.isInBattle(), player.getPos()) ;
		pet.act(player) ;
		pet.display(pet.getPos(), Scale.unit, DP) ;
		pet.displayAttributes(0, DP) ;
//		pet.getBA().finishStatus() ;
	}
	
	private void playerActs()
	{

		player.takeBloodAndPoisonDamage() ;
		player.updateBloodAndPoisonStatistics() ;
		
		if (player.canAct() & player.hasActed())
		{
			player.acts(pet, mousePos) ;

			if (player.getActionCounter().finished())
			{
				player.getActionCounter().reset() ;
			}
		}

		player.doCurrentAction() ;
		
		player.applyAdjacentGroundEffect() ;
		player.displayAttributes(player.getSettings().getAttDisplay(), DP) ;
		player.display(player.getPos(), Scale.unit, player.getDir(), player.getSettings().getShowAtkRange(), DP) ;
		if (player.weaponIsEquipped())
		{
			player.drawWeapon(player.getPos(), Scale.unit, DP) ;
		}		

		player.finishStatus() ;
	}
	
	private void updateProjectiles()
	{
		List<Creature> creaturesInMap = new ArrayList<>() ;
		if (!player.getMap().IsACity())
		{
			FieldMap fm = (FieldMap) player.getMap() ;
			creaturesInMap = fm.getCreatures() ;
		}
		for (Projectiles proj : projs)
		{
			proj.go(player, creaturesInMap, pet, DP) ;
			if (proj.collidedwith(player, creaturesInMap, pet) != -3) // if the projectile has hit some live being
			{
				projs.remove(proj) ;
			}
		}
	}
	
	private void run(DrawPrimitives DP)
	{

		incrementCounters() ;
		activateCounters() ;

		checkKonamiCode(player.getCombo()) ;
		if (konamiCodeActive)
		{
			konamiCode() ;
		}

		Draw.map(player.getMap(), sky) ;
		Draw.mapElements(player.getPos(), player.getMap(), sky) ;

		if (player.getMap().isAField())
		{
			creaturesAct() ;
		}

		if (pet != null)
		{
			petActs() ;
		}

		playerActs() ;

		if (player.getMap().isAField())
		{
			player.setClosestCreature(player.closestCreatureInRange()) ;
		}

		player.checkMeet(mousePos, DP) ;

		if (player.isInBattle())
		{
			Battle.runBattle(player, pet, player.getOpponent(), DP) ;
		}

		if (player.shouldLevelUP())
		{
			player.levelUp() ;
		}
		
		if (pet != null)
		{
			if (pet.shouldLevelUP())
			{
				pet.levelUp() ;
			}	
		}
		
		if (player.isTalkingToNPC())
		{
			player.talkToNPC(mousePos, DP) ;
		}

		player.showWindows(pet, mousePos, DP) ;

		if (projs != null)
		{
			updateProjectiles() ;
		}
		
		if (player.getBag().getItemFetched() != null)
		{
			DP.drawImage(player.getBag().getItemFetched().getImage(), mousePos, Align.center) ;
		}
		
		if (player.getJob() == 3)
		{
			player.useAutoSpell(true, player.getSpells().get(12)) ;
		}

		Animation.playAll(DP) ;
		Gif.playAll() ;
		
		SideBar.display(player, pet, mousePos, DP) ;
		SideBar.act(player.getCurrentAction(), mousePos) ;

		player.resetAction() ;

	}

	private static void setCheatMode()
	{

		
    	player.setName("Rosquinhawwwwwwwwwwwwwww") ;
//    	player.setLevel(50) ;
    	player.setMap(cityMaps[2]) ;
//    	fieldMaps[1].getCreatures().get(0).setPos(player.getPos());
//    	player.setPos(new Point(393, 140)) ;

    	letThereBePet() ;
//    	pet.takeDamage(40);
//    	pet.getMp().decTotalValue(50);

		for (int i = 0 ; i <= fieldMaps.length - 1 ; i += 1)
		{
			player.discoverCreature(fieldMaps[i].getCreatures().get(0).getType()) ;
		}


		player.getPA().getLife().incMaxValue(1000) ;
		player.getPA().getMp().incMaxValue(1000); ;	
//		player.getBA().getPhyAtk().incBaseValue(1000) ;
//		player.getBA().getMagAtk().incBaseValue(1000) ;
//		player.getBA().getPhyDef().incBaseValue(1000) ;
//		player.getBA().getMagDef().incBaseValue(1000) ;
//		player.getBA().getAgi().incBaseValue(1000) ;
		player.getBA().getDex().incBaseValue(1000) ;
		player.getPA().getLife().setToMaximum() ;
		
//		player.getBA().getStun().incAtkChance(1) ;
		player.getBA().getStun().incDuration(100) ;
		
//		player.getBA().getBlock().incAtkChance(1) ;
		player.getBA().getBlock().incDuration(100) ;
		
//		player.getBA().getBlood().incAtkChance(1) ;
		player.getBA().getBlood().incAtkBonus(8);
		player.getBA().getBlood().incDefBonus(1);
		player.getBA().getBlood().incDuration(100);
		
//		player.getBA().getPoison().incAtkChance(1) ;
		player.getBA().getPoison().incAtkBonus(1);
		player.getBA().getPoison().incDefBonus(1);
		player.getBA().getPoison().incDuration(100);

//		player.getBA().getSilence().incAtkChance(1) ;
		player.getBA().getSilence().incDuration(100) ;
		
		player.takeDamage(500);
		player.getBag().addGold(30000) ;
    	for (Spell spell : player.getSpells())
    	{
    		spell.incLevel(5) ;
    	}
    	SpellsBar.updateSpells(player.getActiveSpells()) ;
		
//    	
    	for (Item item : Potion.getAll()) { player.getBag().add(item, 10) ;}
    	for (Item item : Alchemy.getAll()) { player.getBag().add(item, 20) ;}
    	for (Item item : Forge.getAll()) { player.getBag().add(item, 3) ;}
    	for (Item item : PetItem.getAll()) { player.getBag().add(item, 2) ;}
    	for (Item item : Food.getAll()) { player.getBag().add(item, 10) ;}
    	for (Item item : Arrow.getAll()) { player.getBag().add(item, 20) ;}
//    	for (Item item : Equip.getAll()) { player.getBag().add(item, 20) ;}
    	for (Item item : GeneralItem.getAll()) { player.getBag().add(item, 2) ;}
    	for (Item item : Fab.getAll()) { player.getBag().add(item, 10) ;}
    	for (Item item : QuestItem.getAll()) { player.getBag().add(item, 10) ;}
//    	player.getElem()[4] = Elements.water ;
//
    	player.getBag().add(Equip.getAll()[0], 20) ;
    	player.getBag().add(Equip.getAll()[1], 20) ;
    	player.getBag().add(Equip.getAll()[100], 20) ;
    	player.getBag().add(Equip.getAll()[102], 20) ;
    	for (QuestSkills skill : QuestSkills.values())
    	{
    		player.getQuestSkills().replace(skill, true) ;
    	}
    	Quest.all.forEach(quest -> player.addQuest(quest)) ;
    	
//    	for (int i = 0 ; i <= 50 - 1 ; i += 1)
//    	{
//    		player.getExp().incCurrentValue(player.getExp().getMaxValue()) ;
//			player.levelUp(null) ; // Game.getAnimations()[4]
//    	}
    	
//    	for (int i = 0 ; i <= 30000 - 1 ; i += 1)
//    	{
//    		player.train(new AtkResults(AtkTypes.physical, AttackEffects.hit, 0)) ;
//    		player.train(new AtkResults(AtkTypes.magical, AttackEffects.hit, 0)) ;
//    		player.train(new AtkResults(AtkTypes.defense, AttackEffects.hit, 0)) ;
//    	}
////    	
//    	for (int i = 0 ; i <= 3 - 1 ; i += 1)
//    	{
//        	((Equip) player.getBag().getMenuListItems().get(i + 200 * player.getJob())).use(player) ;
//        	for (int j = 0 ; j <= 10 - 1 ; j += 1)
//        	{
//            	player.getEquips()[i].incForgeLevel() ;
//        	}
//    	}

//    	player.getExp().incCurrentValue(5000) ;
    	
	}
	
	public static void initialize(int step)
	{
		
		long initialTime = System.nanoTime();

		switch (step)
		{
			case 0:
				sky = new Sky() ;
				screen.setBorders(new int[] { 0, Sky.height, screen.getSize().width, screen.getSize().height }) ;
				screen.setMapCenter() ;
				Log.loadTime("initial stuff", initialTime) ;
				return ;
				
			case 1:
				loadAllText() ;
				Log.loadTime("text", initialTime) ;
				return ;
				
			case 2:
				Buff.loadBuffs() ;
				Buff.loadDebuffs() ;
				Spell.load(gameLanguage, Buff.allBuffs, Buff.allDebuffs) ;
				Log.loadTime("spells", initialTime) ;
				return ;
				
			case 3:
				Item.load();
				Log.loadTime("items", initialTime) ;
				return ;
				
			case 4:
				CreatureType.load(gameLanguage, difficultLevel) ;
				Log.loadTime("creature types", initialTime) ;
				return ;
				
			case 5:
				Recipe.load(Item.allItems) ;
				Log.loadTime("recipes", initialTime) ;
				return ;
				
			case 6:
				NPCTypes = loadNPCTypes(gameLanguage) ;
				Log.loadTime("npc types", initialTime) ;
				return ;
				
			case 7:
				buildingTypes = loadBuildingTypes() ;
				Log.loadTime("building types", initialTime) ;
				return ;
				
			case 8:
				allQuests = loadQuests(gameLanguage, player.getJob(), CreatureType.all, Item.allItems) ;
				Log.loadTime("quests", initialTime) ;
				return ;
				
			case 9:
				allMaps = loadAllMaps(Item.allItems) ;
				Log.loadTime("maps", initialTime) ;
				return ;
				
			case 10:
				NPCs.setIDs() ;

				player.InitializeSpells() ;
				player.setMap(Game.getMaps()[player.getJob()]) ;
				player.setPos(Game.getScreen().getCenter()) ;
				Battle.updateDamageAnimation(player.getSettings().getDamageAnimation()) ;
				SideBar.initialize();
				
				if (player.getSettings().getMusicIsOn())
				{
					Music.SwitchMusic(player.getMap().getMusic()) ;
				}
				Log.loadTime("last stuff", initialTime) ;
				return ;
			
			default: return ;
		}
		
	}

	
	
	@Override
	protected void paintComponent(Graphics graphs)
	{
		super.paintComponent(graphs) ;
		mousePos = Util.GetMousePos(mainPanel) ;
		DP.setGraphics((Graphics2D) graphs) ;
		Draw.setDP(DP) ;
		TimeCounter.updateAll() ;
		switch (state)
		{
			case opening:
				Opening.run(player, mousePos, DP) ;
				if (Opening.isOver())
				{
					if (Opening.newGame())
					{
						difficultLevel = Opening.getChosenDifficultLevel() ;
						player = Opening.getChosenPlayer() ;
					}
					Game.setState(GameStates.loading) ;
				}
				shouldRepaint = true ;
				break ;

			case loading:
				Opening.displayLoadingScreen(player.getCurrentAction(), mousePos, DP) ;
				if (!Opening.loadingIsOver())
				{
					initialize(Opening.getLoadingStep()) ;
					Opening.incLoadingStep() ;
					if (Opening.loadingIsOver())
					{
						Opening.activateStartButton() ;
//						JobBuild.printAll() ;
					}
				}
	
				if (Opening.gameStarted())
				{
//			    	player.switchOpenClose(player.getHintsindow()) ;
					if (cheatMode) { setCheatMode() ;}
					player.startCounters() ;
					Game.setState(GameStates.running) ;
//					player.levelUp();
				}
				shouldRepaint = true ;
				break ;

			case simulation:
				EvolutionSimulation.run(mousePos, DP) ;	
				break ;

			case running:
				run(DP) ;
				playGifs(DP) ;
				// DP.DrawImage(Util.loadImage("./images/test.png"), mousePos, Align.center) ;	
				break ;

			case playingStopTimeGif:
				playStopTimeGifs() ;	
				break ;

			case paused: break ;
			default: break ;
		}

		Toolkit.getDefaultToolkit().sync() ;
		graphs.dispose() ;
	}

	

	class TAdapter extends KeyAdapter
	{
		@Override
		public void keyPressed(KeyEvent event)
		{
			
			shouldRepaint = true ;
			int keyCode = event.getKeyCode() ;
			
			if (KeyEvent.VK_NUMPAD0 <= keyCode & keyCode <= KeyEvent.VK_NUMPAD9)
			{
				player.setCurrentAction(String.valueOf(keyCode - 96)) ;
				return ;
			}
			
			if (keyCode == KeyEvent.VK_PAUSE)
			{
				if (state.equals(GameStates.paused))
				{
					MainGame3_4.resumeGame();
					TimeCounter.resumeAll() ;
				}
				else
				{
					MainGame3_4.pauseGame() ;
					TimeCounter.stopAll() ;
				}
			}

			player.setCurrentAction(KeyEvent.getKeyText(keyCode)) ;
			
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
			
			if (evt.getButton() == 1) // Left click
			{
				player.setCurrentAction("LeftClick") ;
//					System.out.println(Util.Round(mousePos.x / 600.0, 2) + "," + Util.Round((mousePos.y - 96) / 384.0, 2) + " " + mousePos.x + " " + mousePos.y) ;	
			}
			if (evt.getButton() == 3) // Right click
			{
				player.setCurrentAction("MouseRightClick") ;
        		player.setPos(mousePos) ;
//        		player.inflictStatus(Attributes.blood, 3, 10);
//        		player.inflictStatus(Attributes.poison, 2, 6);
        		if (pet != null)
        		{
        			pet.setPos(player.getPos()) ;
        		}
        		//	        		TestingAnimations.run() ;
//					testGif.start() ;
			}
			
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
			if (player.getBag().isOpen())
			{
				player.getBag().setItemFetched(player.getBag().itemHovered(mousePos)) ;
			}
		}

		@Override
		public void mouseReleased(MouseEvent e)
		{
			int hotKeySlotHovered = HotKeysBar.slotHovered(mousePos) ;
			
			if (-1 < hotKeySlotHovered)
			{
				player.getHotItems()[hotKeySlotHovered] = player.getBag().getItemFetched() ;
			}

			player.getBag().setItemFetched(null) ;
		}
	}

	class MouseWheelEventDemo implements MouseWheelListener
	{
		@Override
		public void mouseWheelMoved(MouseWheelEvent evt)
		{
			if (evt.getWheelRotation() < 0) // wheel up
			{
				player.setCurrentAction("MouseWheelUp") ;
			}
			else
			{
				player.setCurrentAction("MouseWheelDown") ;
			}
		}
	}

}