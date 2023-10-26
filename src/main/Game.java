package main ;

import java.awt.Color ;
import java.awt.Dimension ;
import java.awt.Graphics ;
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
import java.io.File ;
import java.util.ArrayList ;
import java.util.Arrays ;
import java.util.HashMap ;
import java.util.HashSet ;
import java.util.Iterator ;
import java.util.List ;
import java.util.Map ;
import java.util.Set ;

import javax.sound.sampled.Clip ;
import javax.swing.JPanel ;

import org.json.simple.JSONArray ;
import org.json.simple.JSONObject ;

import attributes.BasicAttribute ;
import attributes.BasicBattleAttribute ;
import attributes.BattleAttributes ;
import attributes.BattleSpecialAttribute ;
import attributes.BattleSpecialAttributeWithDamage ;
import attributes.PersonalAttributes ;
import components.Building ;
import components.BuildingNames ;
import components.BuildingType ;
import components.NPCJobs ;
import components.NPCType ;
import components.NPCs ;
import components.Projectiles ;
import components.Quest ;
import components.SpellTypes ;
import graphics.Animation ;
import graphics.DrawingOnPanel ;
import items.Alchemy ;
import items.Arrow ;
import items.Equip ;
import items.Fab ;
import items.Food ;
import items.Forge ;
import items.GeneralItem ;
import items.Item ;
import items.PetItem ;
import items.Potion ;
import items.QuestItem ;
import items.Recipe ;
import liveBeings.Buff ;
import liveBeings.Creature ;
import liveBeings.CreatureType ;
import liveBeings.LiveBeingStates ;
import liveBeings.LiveBeingStatus ;
import liveBeings.MovingAnimations ;
import liveBeings.Pet ;
import liveBeings.Player ;
import liveBeings.PlayerJobs ;
import liveBeings.Pterodactile ;
import liveBeings.Spell ;
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
import simulations.PlayerEvolutionSimulation ;
import utilities.Align ;
import utilities.Directions ;
import utilities.Elements ;
import utilities.GameStates ;
import utilities.Scale ;
import utilities.UtilG ;
import utilities.UtilS ;
import windows.BankWindow ;
import windows.CreatureAttributesWindow ;

public class Game extends JPanel
{
	// TODO soundtrack
	// TODO design das construções
	// TODO spells
	// TODO nomes das criaturas
	// TODO descrição dos itens
	// TODO no superelemento de fogo, todos os panos na mochila viram panos em chamas
	// TODO conferir a contagem das estatísticas do player
	// TODO figure out how to get gif duration
	private static final long serialVersionUID = 1L ;
	private static final String[] konamiCode = new String[] { "Acima", "Acima", "Abaixo", "Abaixo", "Esquerda",
			"Direita", "Esquerda", "Direita", "B", "A" } ;
	public static final String JSONPath = ".\\json\\" ;
	public static final String CSVPath = ".\\csv\\" ;
	public static final String ImagesPath = ".\\images\\" ;
	public static final String MusicPath = ".\\music\\" ;
	public static final String TextPathBR = "./Texto-PT-br.json" ;
	public static final String MainFontName = "Comics" ;

	private JPanel mainPanel = this ;
	private static Point mousePos ;
	private static GameStates state ;
	private static Languages gameLanguage ;
	private static boolean shouldRepaint ; // tells if the panel should be repainted, created to handle multiple repaint
											// requests at once
	private static boolean konamiCodeActive ;

	public static Color[] colorPalette ;
	public static int DayDuration ;
	public static Map<TextCategories, String[]> allText ;

	private DrawingOnPanel DP ;
	private static Player player ;
	private static Pet pet ;
	public static int difficultLevel ;

	private static Screen screen ;
	private static Sky sky ;
	private static SideBar sideBar ;
	private static CreatureType[] creatureTypes ;
	private static CityMap[] cityMaps ;
	private static FieldMap[] fieldMaps ;
	private static SpecialMap[] specialMaps ;
	private static GameMap[] allMaps ;
	private static BuildingType[] buildingTypes ;
	private static NPCType[] NPCTypes ;
	private static List<Recipe> allRecipes ;
	private static Item[] allItems ;
	private static Spell[] allSpells ;
	private static Quest[] allQuests ;
	private static Battle bat ;
	private static List<Projectiles> projs ;
	private static List<Animation> animations ;

	static
	{
		Dimension windowSize = MainGame3_4.getWindowsize() ;
		screen = new Screen(new Dimension(windowSize.width - 40, windowSize.height), null) ;
		screen.calcCenter() ;
		gameLanguage = Languages.portugues ;
		state = GameStates.opening ;
		colorPalette = UtilS.ReadColorPalette(UtilG.loadImage(ImagesPath + "ColorPalette4.png"), "Normal") ;
		konamiCodeActive = false ;
		initializeAnimations() ;

		allText = new HashMap<>() ;
		shouldRepaint = false ;
	}

	public Game()
	{
		DP = new DrawingOnPanel() ;
		player = new Player("", "", 0) ;

		addMouseListener(new MouseEventDemo()) ;
		addMouseWheelListener(new MouseWheelEventDemo()) ;
		addKeyListener(new TAdapter()) ;
		setFocusable(true) ;
	}

	public static GameStates getState() { return state ;}
	public static Languages getLanguage() { return gameLanguage ;}
	public static Screen getScreen() { return screen ;}
	public static Sky getSky() { return sky ;}
	public static CreatureType[] getCreatureTypes() { return creatureTypes ;}
	public static NPCType[] getNPCTypes() { return NPCTypes ;}
	public static Player getPlayer() { return player ;}
	public static Pet getPet() { return pet ;}
	public static BuildingType[] getBuildingTypes() { return buildingTypes ;}
	public static GameMap[] getMaps() { return allMaps ;}
	public static Quest[] getAllQuests() { return allQuests ;}
	public static List<Recipe> getAllRecipes() { return allRecipes ;}
	public static Item[] getAllItems() { return allItems ;}
	public static Spell[] getAllSpells() { return allSpells ;}
	public static List<Animation> getAnimations() { return animations ;}
	public static boolean getShouldRepaint() { return shouldRepaint ;}
	public static Point getMousePos() { return mousePos ;}
	
	public static List<Item> getItems(int[] itemIDs)
	{
		List<Item> items = new ArrayList<>() ;
    	for (int itemID : itemIDs)
    	{
    		items.add(Game.getAllItems()[itemID]) ;
    	}
    	
    	return items ;
	}
	
	public static void setState(GameStates newState) { state = newState ;}
	public static void playStopTimeGif() { state = GameStates.playingStopTimeGif ;}
	public static void shouldNotRepaint() { shouldRepaint = false ;}

	public static boolean someAnimationIsActive() { return (animations.get(3).isActive() | animations.get(4).isActive() | animations.get(5).isActive()) ;}

	private static void loadAllText()
	{

		JSONObject textData = UtilG.readJsonObject(TextPathBR) ;

		Iterator<?> iterator = textData.keySet().iterator() ;

		while (iterator.hasNext())
		{
			Object key = iterator.next() ;
			TextCategories catName = TextCategories.catFromBRName((String) key) ;

			if (catName != null)
			{
				if (catName.equals(TextCategories.npcs))
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
		}

//		allText.entrySet().forEach(text -> System.out.println(text.getKey() + " " + Arrays.toString(text.getValue()))) ;

	}

	private static void initializeAnimations()
	{
		animations = new ArrayList<>() ;
		for (int i = 0 ; i <= 13 - 1 ; i += 1)
		{
			Animation ani = new Animation() ;
			switch(i)
			{
				case 0 :
					ani.setDisplayFunction((vars, DP) -> {
						Point targetPos = (Point) vars[0] ;
						Dimension targetSize = (Dimension) vars[1] ;
						AtkResults atkResults = (AtkResults) vars[2] ;
						int style = (int) vars[3] ;
						Point pos = new Point(targetPos.x, targetPos.y - targetSize.height - 25) ;
						DP.DrawDamageAnimation(pos, atkResults, ani.getCounter(), style, Game.colorPalette[6]) ;
					}) ;
					break ;
					
				case 1 :
					ani.setDisplayFunction((vars, DP) -> {
						Point targetPos = (Point) vars[0] ;
						Dimension targetSize = (Dimension) vars[1] ;
						AtkResults atkResults = (AtkResults) vars[2] ;
						int style = (int) vars[3] ;
						Point pos = new Point(targetPos.x, targetPos.y - targetSize.height - 25) ;
						DP.DrawDamageAnimation(pos, atkResults, ani.getCounter(), style, Game.colorPalette[6]) ;
					}) ;
					break ;
					
				case 2 :
					ani.setDisplayFunction((vars, DP) -> {
						Point targetPos = (Point) vars[0] ;
						Dimension targetSize = (Dimension) vars[1] ;
						AtkResults atkResults = (AtkResults) vars[2] ;
						int style = (int) vars[3] ;
						Point pos = new Point(targetPos.x, targetPos.y - targetSize.height - 25) ;
						DP.DrawDamageAnimation(pos, atkResults, ani.getCounter(), style, Game.colorPalette[6]) ;
					}) ;
					break ;
					
				case 3 :
					ani.setDisplayFunction((vars, DP) -> {
						Item[] itemsObtained = (Item[]) vars[0] ;
						DP.winAnimation(ani.getCounter(), itemsObtained) ;
					}) ;
					break ;
					
				case 4 :
					ani.setDisplayFunction((vars, DP) -> {
						Point playerPos = (Point) vars[0] ;
						Player.levelUpGif.play(playerPos, Align.bottomCenter, DP) ;
						if (Player.levelUpGif.isDonePlaying())
						{
							Player.levelUpGif.resetTimeCounter() ;
							// TODO reset gif
						}
					}) ;
					break ;
					
				case 5 :
					ani.setDisplayFunction((vars, DP) -> {
						Point petPos = (Point) vars[0] ;
						Pet.levelUpGif.play(petPos, Align.bottomCenter, DP) ;
						if (Pet.levelUpGif.isDonePlaying())
						{
							Pet.levelUpGif.resetTimeCounter() ;
						}
					}) ;
					break ;
					
				case 6 :
					ani.setDisplayFunction((vars, DP) -> {
						double[] attributesInc = (double[]) vars[0] ;
						int playerLevel = (int) vars[1] ;
						Color textColor = Game.colorPalette[6] ;

						DP.levelUpAnimation(ani.getCounter(), attributesInc, playerLevel, textColor) ;
					}) ;
					break ;
					
				case 7 :
					ani.setDisplayFunction((vars, DP) -> {
						double[] attributesInc = (double[]) vars[0] ;
						int playerLevel = (int) vars[1] ;
						Color textColor = Game.colorPalette[6] ;

						DP.levelUpAnimation(ani.getCounter(), attributesInc, playerLevel, textColor) ;
					}) ;
					break ;
					
				case 8 :
					ani.setDisplayFunction((vars, DP) -> {
						Image PterodactileImage = (Image) vars[0] ;
						Image SpeakingBubbleImage = (Image) vars[1] ;
						String[] message = (String[]) vars[2] ;
						DP.PterodactileAnimation(ani.getCounter(), PterodactileImage, SpeakingBubbleImage, message) ;
					}) ;
					break ;
					
				case 9 :
					ani.setDisplayFunction((vars, DP) -> {
						Point playerPos = (Point) vars[0] ;
						Directions playerDir = (Directions) vars[1] ;
						Point fishingPos = UtilG.Translate(playerPos, 0, 0) ;

						switch (playerDir)
						{
							case left:
								fishingPos = UtilG.Translate(playerPos, -Player.FishingGif.size().width, 0) ;
								break ;
								
							case right:
								fishingPos = UtilG.Translate(playerPos, Player.FishingGif.size().width, 0) ;
								break ;
								
							case up:
								fishingPos = UtilG.Translate(playerPos, 0, -Player.FishingGif.size().height) ;
								break ;
								
							case down:
								fishingPos = UtilG.Translate(playerPos, 0, Player.FishingGif.size().height) ;
								break ;
						}

						Player.FishingGif.play(fishingPos, Align.bottomCenter, DP) ;
						if (Player.FishingGif.isDonePlaying())
						{
							Player.FishingGif.resetTimeCounter() ;
						}

					}) ;
					break ;
					
				case 10 :
					ani.setDisplayFunction((vars, DP) -> {
						int goldObtained = (int) vars[0] ;
						DP.gainGoldAnimation(ani.getCounter(), goldObtained) ;
					}) ;
					break ;
					
				case 11 :
					ani.setDisplayFunction((vars, DP) -> {
						DP.notEnoughGold(ani.getCounter()) ;
					}) ;
					break ;
					
				case 12 :
					ani.setDisplayFunction((vars, DP) -> {
						Point pos = (Point) vars[0] ;
						String message = (String) vars[1] ;
						Color color = (Color) vars[2] ;
						DP.quickTextAnimation(pos, ani.getCounter(), message, color) ;
					}) ;
					break ;
					
				default: break ;
			}
			
			animations.add(ani) ;
		}
		
	}

	private static void checkKonamiCode(List<String> Combo)
	{
		String[] combo = Combo.toArray(new String[Combo.size()]) ;
		if (Arrays.equals(combo, konamiCode))
		{
			konamiCodeActive = !konamiCodeActive ;
			player.resetCombo() ;
		}
	}

	private static void konamiCode()
	{
		DayDuration = 12 ;
		colorPalette = UtilS.ReadColorPalette(UtilG.loadImage(ImagesPath + "ColorPalette.png"), "Konami") ;
		if (sky.dayTime.getCounter() % 1200 <= 300)
		{
			DrawingOnPanel.stdAngle += 0.04 ;
		} else if (sky.dayTime.getCounter() % 1200 <= 900)
		{
			DrawingOnPanel.stdAngle -= 0.04 ;
		} else
		{
			DrawingOnPanel.stdAngle += 0.04 ;
		}
	}

	public static void removePet() { pet = null ;}

	public static void letThereBePet()
	{
		int job = UtilG.randomIntFromTo(0, 3) ;
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
	}

	private static NPCs readNPCfromJson(JSONObject npcJSONObject)
	{

		JSONObject npc = npcJSONObject ;
		String npcName = (String) npc.get("name") ;
		double posX = (Double) ((JSONObject) npc.get("pos")).get("x") ;
		double posY = (Double) ((JSONObject) npc.get("pos")).get("y") ;
		Point npcPos = screen.getPointWithinBorders(posX, posY) ;
		NPCJobs npcJob = NPCJobs.valueOf(npcName) ;
		NPCType npcType = null ;
		for (NPCType type : NPCTypes)
		{
			if (!npcJob.equals(type.getJob()))
			{
				continue ;
			}

			npcType = type ;
		}
		return new NPCs(npcType, npcPos) ;

	}

	private static List<Recipe> LoadCraftingRecipes()
	{
		List<Recipe> recipes = new ArrayList<>() ;

		long elapsedTimeReadingFile = System.nanoTime();
		JSONArray input = UtilG.readJsonArray(Game.JSONPath + "craftRecipes.json") ;

		System.out.println("Time reading recipes file = " + (System.nanoTime() - elapsedTimeReadingFile) / 1000000) ;
		for (int i = 0 ; i <= input.size() - 1 ; i += 1)
		{
			JSONObject recipe = (JSONObject) input.get(i) ;

			JSONArray listIngredients = (JSONArray) recipe.get("IngredientIDs") ;
			JSONArray listIngredientAmounts = (JSONArray) recipe.get("IngredientAmounts") ;
			Map<Item, Integer> ingredients = new HashMap<>() ;
			for (int ing = 0 ; ing <= listIngredients.size() - 1 ; ing += 1)
			{
				ingredients.put(allItems[(int) (long) listIngredients.get(ing)],
						(int) (long) listIngredientAmounts.get(ing)) ;
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

	private static NPCType[] initializeNPCTypes(Languages language)
	{
		List<String[]> input = UtilG.ReadcsvFile(CSVPath + "NPCTypes.csv") ;
		NPCType[] npcType = new NPCType[input.size()] ;
		for (int i = 0 ; i <= npcType.length - 1 ; i += 1)
		{
			String name = input.get(i)[language.ordinal()] ;
			NPCJobs job = NPCJobs.valueOf(input.get(i)[2]) ;
			String info = input.get(i)[3 + language.ordinal()] ;
			Color color = job.getColor() ;
			String imageExtension = !job.equals(NPCJobs.master) ? ".png" : ".gif" ;
			Image image = UtilG.loadImage(ImagesPath + "\\NPCs\\" + "NPC_" + job.toString() + imageExtension) ;
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

	private static BuildingType[] initializeBuildingTypes()
	{
		JSONArray input = UtilG.readJsonArray("./json/buildingTypes.json") ;
		BuildingType[] buildingTypes = new BuildingType[input.size()] ;
		String path = ImagesPath + "\\Buildings\\" ;
		for (int i = 0 ; i <= input.size() - 1 ; i += 1)
		{
			JSONObject type = (JSONObject) input.get(i) ;
			BuildingNames name = BuildingNames.valueOf((String) type.get("name")) ;
			Image outsideImage = UtilG.loadImage(path + "Building" + name + ".png") ;

			buildingTypes[i] = new BuildingType(name, outsideImage) ;

			boolean hasInterior = (boolean) type.get("hasInterior") ;
			if (hasInterior)
			{
				Image insideImage = UtilG.loadImage(path + "Building" + name + "Inside.png") ;
				Image[] OrnamentImages = new Image[] { UtilG.loadImage(path + "Building" + name + "Ornament.png") } ;
				buildingTypes[i].setInsideImage(insideImage) ;
				buildingTypes[i].setOrnamentImages(OrnamentImages) ;
			}
		}

		return buildingTypes ;
	}

	private static CreatureType[] initializeCreatureTypes(Languages language, int difficultLevel)
	{

		long elapsedTimeReadingFile = System.nanoTime();
		List<String[]> input = UtilG.ReadcsvFile(CSVPath + "CreatureTypes.csv") ;
		System.out.println("Time reading creature types file = " + (System.nanoTime() - elapsedTimeReadingFile) / 1000000) ;
		String path = ImagesPath + "\\Creatures\\" ;
		CreatureType.setNumberOfCreatureTypes(input.size()) ;
		CreatureType[] creatureTypes = new CreatureType[CreatureType.getNumberOfCreatureTypes()] ;
		Color[] color = new Color[creatureTypes.length] ;
		int numberCreatureTypes = 7 ;
		double diffMult = difficultLevel == 0 ? 0.6 : (difficultLevel == 1 ? 0.8 : 1.0) ;
		for (int ct = 0 ; ct <= creatureTypes.length - 1 ; ct += 1)
		{
			int colorid = (int) ((Creature.getskinColor().length - 1) * Math.random()) ;
			color[ct] = Creature.getskinColor()[colorid] ;
			if (270 < ct & ct <= 299) // Ocean creatures
			{
				color[ct] = colorPalette[5] ;
			}

			MovingAnimations moveAni = new MovingAnimations(
					UtilG.loadImage(path + "creature" + (ct % numberCreatureTypes) + "_idle.gif"),
					UtilG.loadImage(path + "creature" + (ct % numberCreatureTypes) + "_movingup.gif"),
					UtilG.loadImage(path + "creature" + (ct % numberCreatureTypes) + "_movingdown.gif"),
					UtilG.loadImage(path + "creature" + (ct % numberCreatureTypes) + "_movingleft.gif"),
					UtilG.loadImage(path + "creature" + (ct % numberCreatureTypes) + "_movingright.gif")) ;

			BasicAttribute Life = new BasicAttribute((int) (Integer.parseInt(input.get(ct)[5]) * diffMult),
					(int) (Integer.parseInt(input.get(ct)[5]) * diffMult), 1) ;
			BasicAttribute Mp = new BasicAttribute((int) (Integer.parseInt(input.get(ct)[6]) * diffMult),
					(int) (Integer.parseInt(input.get(ct)[6]) * diffMult), 1) ;
			BasicAttribute Exp = new BasicAttribute(Integer.parseInt(input.get(ct)[36]), 999999999, 1) ;
			BasicAttribute Satiation = new BasicAttribute(100, 100, 1) ;
			BasicAttribute Thirst = new BasicAttribute(100, 100, 1) ;
			PersonalAttributes PA = new PersonalAttributes(Life, Mp, Exp, Satiation, Thirst) ;

			BasicBattleAttribute PhyAtk = new BasicBattleAttribute(Double.parseDouble(input.get(ct)[8]) * diffMult, 0,
					0) ;
			BasicBattleAttribute MagAtk = new BasicBattleAttribute(Double.parseDouble(input.get(ct)[9]) * diffMult, 0,
					0) ;
			BasicBattleAttribute PhyDef = new BasicBattleAttribute(Double.parseDouble(input.get(ct)[10]) * diffMult, 0,
					0) ;
			BasicBattleAttribute MagDef = new BasicBattleAttribute(Double.parseDouble(input.get(ct)[11]) * diffMult, 0,
					0) ;
			BasicBattleAttribute Dex = new BasicBattleAttribute(Double.parseDouble(input.get(ct)[12]) * diffMult, 0, 0) ;
			BasicBattleAttribute Agi = new BasicBattleAttribute(Double.parseDouble(input.get(ct)[13]) * diffMult, 0, 0) ;
			BasicBattleAttribute CritAtk = new BasicBattleAttribute(Double.parseDouble(input.get(ct)[14]) * diffMult, 0,
					0) ;
			BasicBattleAttribute CritDef = new BasicBattleAttribute(Double.parseDouble(input.get(ct)[15]) * diffMult, 0,
					0) ;
			BattleSpecialAttribute Stun = new BattleSpecialAttribute(Double.parseDouble(input.get(ct)[16]) * diffMult,
					0, Double.parseDouble(input.get(ct)[17]) * diffMult, 0,
					(int) (Double.parseDouble(input.get(ct)[18]) * diffMult)) ;
			BattleSpecialAttribute Block = new BattleSpecialAttribute(Double.parseDouble(input.get(ct)[19]) * diffMult,
					0, Double.parseDouble(input.get(ct)[20]) * diffMult, 0,
					(int) (Double.parseDouble(input.get(ct)[21]) * diffMult)) ;
			BattleSpecialAttributeWithDamage Blood = new BattleSpecialAttributeWithDamage(
					Double.parseDouble(input.get(ct)[22]) * diffMult, 0,
					Double.parseDouble(input.get(ct)[23]) * diffMult, 0,
					(int) (Double.parseDouble(input.get(ct)[24]) * diffMult), 0,
					(int) (Double.parseDouble(input.get(ct)[25]) * diffMult), 0,
					(int) (Integer.parseInt(input.get(ct)[26]) * diffMult)) ;
			BattleSpecialAttributeWithDamage Poison = new BattleSpecialAttributeWithDamage(
					Double.parseDouble(input.get(ct)[27]) * diffMult, 0,
					Double.parseDouble(input.get(ct)[28]) * diffMult, 0,
					(int) (Double.parseDouble(input.get(ct)[29]) * diffMult), 0,
					(int) (Double.parseDouble(input.get(ct)[30]) * diffMult), 0,
					(int) (Integer.parseInt(input.get(ct)[31]) * diffMult)) ;
			BattleSpecialAttribute Silence = new BattleSpecialAttribute(
					Double.parseDouble(input.get(ct)[32]) * diffMult, 0,
					Double.parseDouble(input.get(ct)[33]) * diffMult, 0,
					(int) (Double.parseDouble(input.get(ct)[34]) * diffMult)) ;
			LiveBeingStatus status = new LiveBeingStatus() ;
			BattleAttributes BA = new BattleAttributes(PhyAtk, MagAtk, PhyDef, MagDef, Dex, Agi, CritAtk, CritDef, Stun,
					Block, Blood, Poison, Silence, status) ;

			// TODO spells para as criaturas
			List<Spell> spells = new ArrayList<>() ;
			spells.add(allSpells[0]) ;
//			spells.add(allSpells[1]) ;
//			spells.add(allSpells[2]) ;
//			spells.add(allSpells[3]) ;
//			spells.add(allSpells[4]) ;

			Set<Item> items = new HashSet<>() ;
			for (int i = 0 ; i <= 10 - 1 ; i += 1)
			{
				int itemID = Integer.parseInt(input.get(ct)[37 + i]) ;
				if (-1 < itemID)
				{
					items.add(allItems[itemID]) ;
				}
			}

			int Gold = Integer.parseInt(input.get(ct)[47]) ;
			int[] StatusCounter = new int[8] ;

			String name = input.get(ct)[1 + language.ordinal()] ;
			int level = Integer.parseInt(input.get(ct)[3]) ;
			Dimension size = new Dimension(moveAni.idleGif.getWidth(null), moveAni.idleGif.getHeight(null)) ;
			int range = (int) (Integer.parseInt(input.get(ct)[7]) * diffMult) ;
			int step = Integer.parseInt(input.get(ct)[48]) ;
			Elements[] elem = new Elements[] { Elements.valueOf(input.get(ct)[35]) } ;
			int mpDuration = Integer.parseInt(input.get(ct)[49]) ;
			int satiationDuration = 100 ;
			int numberSteps = Integer.parseInt(input.get(ct)[50]) ;
			int battleActionDuration = Integer.parseInt(input.get(ct)[51]) ;
			int stepCounter = 0 ;

			creatureTypes[ct] = new CreatureType(ct, name, level, size, range, step, elem, mpDuration,
					satiationDuration, numberSteps, battleActionDuration, stepCounter, moveAni, PA, BA, spells, items,
					Gold, color[ct], StatusCounter) ;
		}
		return creatureTypes ;
	}

	private static CityMap[] initializeCityMaps()
	{
		JSONArray input = UtilG.readJsonArray(Game.JSONPath + "mapsCity.json") ;
		CityMap[] cityMaps = new CityMap[input.size()] ;
		String path = ImagesPath + "\\Maps\\" ;

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

			Image image = UtilG.loadImage(path + "Map" + String.valueOf(id) + ".png") ;
			Clip music = Music.musicFileToClip(new File(MusicPath + "7-Forest.wav").getAbsoluteFile()) ;

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
							.addGroundType(new GroundType(GroundTypes.water, new Point(500, sky.height), new Dimension(140, 480 - sky.height))) ;
					break ;
				default: break ;
			}
		}

		return cityMaps ;
	}

	private static FieldMap[] initializeFieldMaps()
	{
		JSONArray input = UtilG.readJsonArray(Game.JSONPath + "mapsField.json") ;
		FieldMap[] fieldMaps = new FieldMap[input.size()] ;
		String path = ImagesPath + "\\Maps\\" ;
		File forestMusic = new File(MusicPath + "7-Forest.wav").getAbsoluteFile() ;

		int mod = 0 ;
		for (int id = 0 ; id <= input.size() - 1 ; id += 1)
		{
			int mapID = id + cityMaps.length ;

			if (id == 34 | id == 54)
			{
				mod += 1 ;
			}

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

			Image image = UtilG.loadImage(path + "Map" + String.valueOf(mapID + mod) + ".png") ;
			Clip music = Music.musicFileToClip(forestMusic) ;

			JSONObject collectibles = (JSONObject) map.get("Collectibles") ;
			int collectibleLevel = (int) (long) collectibles.get("level") ;
			int[] collectiblesDelay = new int[4] ;
			collectiblesDelay[0] = (int) (long) collectibles.get("berryDelay") ;
			collectiblesDelay[1] = (int) (long) collectibles.get("herbDelay") ;
			collectiblesDelay[2] = (int) (long) collectibles.get("woodDelay") ;
			collectiblesDelay[3] = (int) (long) collectibles.get("metalDelay") ;

			List<Long> creatures = (List<Long>) map.get("Creatures") ;
			int[] creatureIDs = new int[creatures.size()] ;
			for (int i = 0 ; i <= creatures.size() - 1 ; i += 1)
			{
				creatureIDs[i] = (int) (long) creatures.get(i) ;
			}

			JSONArray listNPCs = (JSONArray) map.get("NPCs") ;
			List<NPCs> npcs = new ArrayList<>() ;
			for (int i = 0 ; i <= listNPCs.size() - 1 ; i += 1)
			{
				npcs.add(readNPCfromJson((JSONObject) listNPCs.get(i))) ;
			}

			fieldMaps[id] = new FieldMap(name, continent, connections, image, music, collectibleLevel,
					collectiblesDelay, creatureIDs, npcs) ;

			switch (id)
			{
			case 1:
				fieldMaps[id]
						.addGroundType(new GroundType(GroundTypes.water, new Point(120, 200), new Dimension(50, 20))) ;
				break ;
			case 3:
				fieldMaps[id].addGroundType(new GroundType(GroundTypes.water, new Point(120, 100), new Dimension(45, 30))) ;
				break ;
			case 8:
				fieldMaps[id]
						.addGroundType(new GroundType(GroundTypes.water, new Point(500, sky.height), new Dimension(140, 480 - sky.height))) ;
				break ;
			case 9:
				fieldMaps[id]
						.addGroundType(new GroundType(GroundTypes.water, new Point(50, 250), new Dimension(120, 210))) ;
				break ;
			case 12:
				fieldMaps[id]
						.addGroundType(new GroundType(GroundTypes.water, new Point(500, sky.height), new Dimension(140, 480 - sky.height))) ;
				break ;
			case 13:
				fieldMaps[id]
						.addGroundType(new GroundType(GroundTypes.water, new Point(50, 250), new Dimension(120, 210))) ;
				break ;
			case 14:
				fieldMaps[id]
						.addGroundType(new GroundType(GroundTypes.water, new Point(50, 250), new Dimension(120, 210))) ;
				break ;
			case 15:
				fieldMaps[id]
						.addGroundType(new GroundType(GroundTypes.water, new Point(50, 250), new Dimension(120, 210))) ;
				break ;
			case 22:
				fieldMaps[id]
						.addGroundType(new GroundType(GroundTypes.water, new Point(50, 250), new Dimension(120, 210))) ;
				break ;
			default:
				break ;
			}
		}

		return fieldMaps ;
	}

	private static SpecialMap[] initializeSpecialMaps()
	{
		List<String[]> input = UtilG.ReadcsvFile(CSVPath + "MapsSpecial.csv") ;
		SpecialMap[] specialMaps = new SpecialMap[input.size()] ;
		Image treasureChestsImage = UtilG.loadImage(ImagesPath + "\\MapElements\\" + "MapElem15_Chest.png") ;
		Clip music = Music.musicFileToClip(new File(MusicPath + "12-Special.wav").getAbsoluteFile()) ;
		String path = ImagesPath + "\\Maps\\" ;

		for (int id = 0 ; id <= specialMaps.length - 1 ; id += 1)
		{
			String name = input.get(id)[0] ;
			Continents continent = Continents.values()[Integer.parseInt(input.get(id)[1])] ;
			int[] connections = new int[] { Integer.parseInt(input.get(id)[9]), Integer.parseInt(input.get(id)[2]),
					Integer.parseInt(input.get(id)[3]), Integer.parseInt(input.get(id)[4]),
					Integer.parseInt(input.get(id)[5]), Integer.parseInt(input.get(id)[6]),
					Integer.parseInt(input.get(id)[7]), Integer.parseInt(input.get(id)[8]) } ;
			Image image = UtilG.loadImage(path + "MapSpecial" + String.valueOf(id) + ".png") ;

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

	private static GameMap[] initializeAllMaps()
	{

		cityMaps = initializeCityMaps() ;
		fieldMaps = initializeFieldMaps() ;
		specialMaps = initializeSpecialMaps() ;
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

	public static void initalizeMapsTest()
	{
		allSpells = initializeAllSpells(gameLanguage) ;
		allItems = initializeAllItems() ;
		creatureTypes = initializeCreatureTypes(gameLanguage, 1) ;
		allRecipes = LoadCraftingRecipes() ;
		NPCTypes = initializeNPCTypes(gameLanguage) ;
		buildingTypes = initializeBuildingTypes() ;
		allMaps = initializeAllMaps() ;
	}

	private static Quest[] initializeQuests(Languages language, int playerJob)
	{
		List<String[]> inputs = UtilG.ReadcsvFile(CSVPath + "Quests.csv") ;
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
				
				reqCreatureTypes.put(creatureTypes[Integer.parseInt(input[j])], Integer.parseInt(input[j + 1])) ;
			}

			for (int j = 8 ; j <= 18 - 1 ; j += 2)
			{
				if (Integer.parseInt(input[j]) <= -1) { continue ;}
				
				reqItems.put(allItems[Integer.parseInt(input[j])], Integer.parseInt(input[j + 1])) ;
			}

			int goldReward = Integer.parseInt(input[18]) ;
			int expReward = Integer.parseInt(input[19]) ;
			boolean isRepeatable = 0 <= expReward ;
			Map<Item, Integer> rewardItems = new HashMap<>() ;

			for (int j = 20 ; j <= 28 - 1 ; j += 2)
			{
				if (Integer.parseInt(input[j + 1]) <= -1) { continue ;}
				
				rewardItems.put(allItems[Integer.parseInt(input[j])], Integer.parseInt(input[j + 1])) ;
			}

			String description = input[30 + language.ordinal()] ;

			quests[i] = new Quest(id, type, isRepeatable, reqCreatureTypes, reqItems, goldReward, expReward,
					rewardItems, description) ;
		}

		return quests ;
	}

	private static Spell[] initializeAllSpells(Languages language)
	{
		List<String[]> spellTypesInput = UtilG.ReadcsvFile(Game.CSVPath + "SpellTypes.csv") ;
		List<String[]> spellsBuffsInput = UtilG.ReadcsvFile(Game.CSVPath + "SpellsBuffs.csv") ;
		List<String[]> spellsNerfsInput = UtilG.ReadcsvFile(Game.CSVPath + "SpellsNerfs.csv") ;

		Spell[] allSpells = new Spell[spellTypesInput.size()] ;
		String[][] info = new String[allSpells.length][2] ;

		for (int i = 0 ; i <= allSpells.length - 1 ; i += 1)
		{
			int id = i ;

			List<Buff> buffs = new ArrayList<>() ;
			buffs.add(Buff.load(spellsBuffsInput.get(id))) ;

			List<Buff> nerfs = new ArrayList<>() ;
			nerfs.add(Buff.load(spellsNerfsInput.get(id))) ;

			info[i] = new String[] { spellTypesInput.get(id)[42],
					spellTypesInput.get(id)[43 + 2 * language.ordinal()] } ;
			String name = spellTypesInput.get(id)[4] ;
			String job = PlayerJobs.jobFromSpellID(i).toString() ;
			Image image = UtilG.loadImage(ImagesPath + "\\Spells\\" + "spell" + job + i + ".png") ;
			int maxLevel = Integer.parseInt(spellTypesInput.get(id)[5]) ;
			int mpCost = Integer.parseInt(spellTypesInput.get(id)[6]) ;
			SpellTypes type = SpellTypes.valueOf(spellTypesInput.get(id)[7]) ;
			Map<Spell, Integer> preRequisites = new HashMap<>() ;
			for (int p = 0 ; p <= 6 - 1 ; p += 2)
			{
				if (-1 < Integer.parseInt(spellTypesInput.get(id)[p + 8]))
				{
					preRequisites.put(allSpells[Integer.parseInt(spellTypesInput.get(id)[p + 8])],
							Integer.parseInt(spellTypesInput.get(id)[p + 9])) ;
				}
			}
			int cooldown = Integer.parseInt(spellTypesInput.get(id)[14]) ;
			int duration = Integer.parseInt(spellTypesInput.get(id)[15]) ;
			double[] atkMod = new double[] { Double.parseDouble(spellTypesInput.get(id)[16]),
					Double.parseDouble(spellTypesInput.get(id)[17]) } ;
			double[] defMod = new double[] { Double.parseDouble(spellTypesInput.get(id)[18]),
					Double.parseDouble(spellTypesInput.get(id)[19]) } ;
			double[] dexMod = new double[] { Double.parseDouble(spellTypesInput.get(id)[20]),
					Double.parseDouble(spellTypesInput.get(id)[21]) } ;
			double[] agiMod = new double[] { Double.parseDouble(spellTypesInput.get(id)[22]),
					Double.parseDouble(spellTypesInput.get(id)[23]) } ;
			double[] atkCritMod = new double[] { Double.parseDouble(spellTypesInput.get(id)[24]) } ;
			double[] defCritMod = new double[] { Double.parseDouble(spellTypesInput.get(id)[25]) } ;
			double[] stunMod = new double[] { Double.parseDouble(spellTypesInput.get(id)[26]),
					Double.parseDouble(spellTypesInput.get(id)[27]), Double.parseDouble(spellTypesInput.get(id)[28]) } ;
			double[] blockMod = new double[] { Double.parseDouble(spellTypesInput.get(id)[29]),
					Double.parseDouble(spellTypesInput.get(id)[30]), Double.parseDouble(spellTypesInput.get(id)[31]) } ;
			double[] bloodMod = new double[] { Double.parseDouble(spellTypesInput.get(id)[32]),
					Double.parseDouble(spellTypesInput.get(id)[33]), Double.parseDouble(spellTypesInput.get(id)[34]) } ;
			double[] poisonMod = new double[] { Double.parseDouble(spellTypesInput.get(id)[35]),
					Double.parseDouble(spellTypesInput.get(id)[36]), Double.parseDouble(spellTypesInput.get(id)[37]) } ;
			double[] silenceMod = new double[] { Double.parseDouble(spellTypesInput.get(id)[38]),
					Double.parseDouble(spellTypesInput.get(id)[39]), Double.parseDouble(spellTypesInput.get(id)[40]) } ;
			Elements elem = Elements.valueOf(spellTypesInput.get(id)[41]) ;

			allSpells[i] = new Spell(id, name, image, maxLevel, mpCost, type, preRequisites, buffs, nerfs, atkMod,
					defMod, dexMod, agiMod, atkCritMod, defCritMod, stunMod, blockMod, bloodMod, poisonMod, silenceMod,
					cooldown, duration, elem, info[i]) ;
		}
		return allSpells ;
	}

	private static Item[] initializeAllItems()
	{
		List<Item> allItems = new ArrayList<>() ;
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

	public static List<NPCs> getAllNPCs()
	{
		// TODO function for dev only
		List<NPCs> allNPCs = new ArrayList<>() ;
		for (GameMap map : allMaps)
		{
			if (map.getNPCs() == null)
			{
				continue ;
			}
			if (map.getNPCs().isEmpty())
			{
				continue ;
			}
			allNPCs.addAll(map.getNPCs()) ;
		}

		return allNPCs ;
	}

	
	private void incrementCounters()
	{
		sky.dayTime.inc() ;
		if (sky.dayTime.finished())
		{
			sky.dayTime.reset() ;
		}
		player.incrementCounters() ;

		if (pet != null)
		{
			pet.incrementCounters() ;
		}

		if (player.getMap().isAField())
		{
			FieldMap fm = (FieldMap) player.getMap() ;
			fm.getCreatures().forEach(Creature::incrementCounters) ;
			fm.IncCollectiblesCounter() ;
		}

		for (CityMap city : cityMaps)
		{
			BankWindow bank = (BankWindow) city.getBuildings().get(3).getNPCs().get(0).getWindow() ;
			if (bank.hasInvestment())
			{
//				System.out.println(bank.isInvested() + " " + bank.getInvestmentCounter()) ;
				bank.incInvestmentCounter() ;
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
				// pet.ActivateActionCounters(ani.SomeAnimationIsActive()) ;
			}
		}

		if (!player.getMap().isAField())
		{
			return ;
		}

		FieldMap fm = (FieldMap) player.getMap() ;
//		fm.getCreatures().forEach(creature -> creature.ActivateActionCounters()) ;			
		fm.ActivateCollectiblesCounter() ;

	}

	private void playStopTimeGifs()
	{
		if (Opening.getOpeningGif().isTimeStopper())
		{
			Opening.getOpeningGif().play(new Point(0, 0), Align.topLeft, DP) ;

			repaint() ;
		}
	}

	private void playGifs(DrawingOnPanel DP)
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
		sideBar.display(player, pet, mousePos, DP) ;
		sideBar.act(player.getCurrentAction(), mousePos) ;

		// creatures act
		if (player.getMap().isAField())
		{
			List<Creature> creaturesInMap = ((FieldMap) player.getMap()).getCreatures() ;
			for (Creature creature : creaturesInMap)
			{
//				creature.incActionCounters() ;
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

//				creature.DrawAttributes(0, DP) ;
			}
			// TODO eliminar shouldRepaint
			shouldRepaint = true ;
		}

		// pet acts
		if (pet != null)
		{
			if (pet.isAlive())
			{
				pet.updateCombo() ;
				pet.think(player.isInBattle(), player.getPos()) ;
				pet.act(player) ;
				pet.display(pet.getPos(), Scale.unit, DP) ;
				pet.DrawAttributes(0, DP) ;
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
				if (player.getOpponent() == null)
				{
					player.setState(LiveBeingStates.idle) ;
				}
				else
				{
					player.setState(LiveBeingStates.fighting) ;
				}
			}
		}

		if (player.isCollecting())
		{
			player.collect(DP) ;
		}

		if (player.isOpeningChest())
		{
			player.openChest() ;
		}

		player.applyAdjacentGroundEffect() ;
		player.DrawAttributes(0, DP) ;
		player.display(player.getPos(), Scale.unit, player.getDir(), player.getSettings().getShowAtkRange(), DP) ;
		if (player.weaponIsEquipped())
		{
			player.drawWeapon(player.getPos(), Scale.unit, DP) ;
		}
		player.displayState(DP) ;
//		player.getMap().displayInfoWindow(DP) ;

		player.doCurrentAction(DP) ;

		// find the closest creature to the player
		if (!player.getMap().IsACity())
		{
			player.setClosestCreature(player.closestCreatureInRange()) ;
		}

		// check if the player met something
		player.meet(mousePos, DP) ;

		// if the player is in battle, run battle
		if (player.isInBattle())
		{
			bat.RunBattle(player, pet, player.getOpponent(), animations, DP) ;
		}

		// level up the player and the pet if they should
		if (player.shouldLevelUP())
		{
			player.levelUp(animations.get(6)) ;
		}
		
		if (pet != null)
		{
			if (pet.shouldLevelUP())
			{
				pet.levelUp(animations.get(7)) ;
			}	
		}

		// show the active player windows
		player.showWindows(pet, mousePos, DP) ;

		// move the active projectiles and check if they collide with something
		if (projs != null)
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

		player.resetAction() ;

//		for (Gif gif : allGifs) { gif.play(mousePos, null, DP) ;}

		for (Animation ani : animations)
		{
			ani.run(DP) ;
		}

	}

	private static void initializeCheatMode()
	{

//    	player.setName("Rosquinhawwwwwwwwwwwwwww") ;
//    	player.setLevel(50) ;
//    	player.setMap(cityMaps[3]) ;
//    	player.setPos(new Point(393, 140)) ;

//    	letThereBePet() ;

//		for (int i = 0 ; i <= fieldMaps.length - 1 ; i += 1)
//		{
//			player.discoverCreature(fieldMaps[i].getCreatures().get(0).getType()) ;
//		}

//    	System.out.println("Game npcs: " + Game.getAllNPCs().size()) ;
//    	int i = 0 ;
//    	for (NPCs npc : Game.getAllNPCs())
//    	{
//    		if (npc.getType().getJob().equals(NPCJobs.questExp) | npc.getType().getJob().equals(NPCJobs.questItem))
//    		{
//    			i++ ;
//    			System.out.println(npc + " " + npc.getID()) ;
//    		}
//    	}
//    	System.out.println(i + " npc quests") ;
//    	Game.getAllNPCs().forEach(System.out::println) ;
    	player.getBag().addGold(30000) ;
//
//    	
//    	for (Item item : Potion.getAll()) { player.getBag().add(item, 10) ;}
//    	for (Item item : Alchemy.getAll()) { player.getBag().add(item, 20) ;}
    	for (Item item : Forge.getAll()) { player.getBag().add(item, 1) ;}
    	for (Item item : PetItem.getAll()) { player.getBag().add(item, 2) ;}
//    	for (Item item : Food.getAll()) { player.getBag().add(item, 10) ;}
//    	for (Item item : Arrow.getAll()) { player.getBag().add(item, 20) ;}
    	for (Item item : Equip.getAll()) { player.getBag().add(item, 10) ;}
    	for (Item item : GeneralItem.getAll()) { player.getBag().add(item, 10) ;}
//    	for (Item item : Fab.getAll()) { player.getBag().add(item, 10) ;}
//    	for (Item item : QuestItem.getAll()) { player.getBag().add(item, 10) ;}
    	player.getElem()[4] = Elements.water ;
//
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
//    	player.getBag().menuUp() ;
//    	player.getBag().menuUp() ;
//    	player.getBag().menuUp() ;
//    	player.getBag().menuUp() ;
//    	player.getBag().menuUp() ;
//    	player.getBag().menuUp() ;
//    	
//    	for (int i = 0 ; i <= 3 - 1 ; i += 1)
//    	{
//        	((Equip) player.getBag().getMenuListItems().get(i + 200 * player.getJob())).use(player) ;
//        	player.getEquips()[i].incForgeLevel() ;
//        	player.getEquips()[i].incForgeLevel() ;
//        	player.getEquips()[i].incForgeLevel() ;
//        	player.getEquips()[i].incForgeLevel() ;
//        	player.getEquips()[i].incForgeLevel() ;
//        	player.getEquips()[i].incForgeLevel() ;
//        	player.getEquips()[i].incForgeLevel() ;
//        	player.getEquips()[i].incForgeLevel() ;
//        	player.getEquips()[i].incForgeLevel() ;
//        	player.getEquips()[i].incForgeLevel() ;
//    	}

//    	player.getExp().incCurrentValue(5000) ;

//    	player.getMap().addGroundType(new GroundType(GroundTypes.water, new Point(50, 250), new Dimension(120, 210))) ;

    	//				for (int i = 0 ; i <= 10000 - 1 ; i += 1)
    	//				{
    	//					Genetics genes = new Genetics() ;
    	////					genes.randomizeGenes() ;
    	////					genes.setGenes(Genetics.normalize(genes.getGenes())) ;
    	//					System.out.println(genes.getGenes()) ;
    	//				}
    	
	}
	
	private static void initialize()
	{
		
		long elapsedTimeInit = System.nanoTime();
		long elapsedTimeText = System.nanoTime();
		System.out.println("Initializing") ;
		System.out.println("Loading text") ;
		loadAllText() ;
		System.out.println("Finished loading in " + (System.nanoTime() - elapsedTimeText) / 1000000) ;

		DayDuration = 1200 ;
		sky = new Sky() ;
		screen.setBorders(new int[] { 0, sky.height, screen.getSize().width, screen.getSize().height }) ;
		screen.setMapCenter() ;
		long elapsedTimeSpells = System.nanoTime();
		System.out.println("Loading spells") ;
		allSpells = initializeAllSpells(gameLanguage) ;
		System.out.println("Finished loading in " + (System.nanoTime() - elapsedTimeSpells) / 1000000) ;
		long elapsedTimeItems = System.nanoTime();
		System.out.println("Loading items") ;
		allItems = initializeAllItems() ;
		System.out.println("Finished loading in " + (System.nanoTime() - elapsedTimeItems) / 1000000) ;
		long elapsedTimeCreatures = System.nanoTime();
		System.out.println("Loading creature types") ;
		creatureTypes = initializeCreatureTypes(gameLanguage, difficultLevel) ;
		System.out.println("Finished loading in " + (System.nanoTime() - elapsedTimeCreatures) / 1000000) ;
		long elapsedTimeRecipes = System.nanoTime();
		System.out.println("Loading recipes") ;
		allRecipes = LoadCraftingRecipes() ;
		System.out.println("Finished loading in " + (System.nanoTime() - elapsedTimeRecipes) / 1000000) ;
		long elapsedTimeNPCs = System.nanoTime();
		System.out.println("Loading NPC types") ;
		NPCTypes = initializeNPCTypes(gameLanguage) ;
		System.out.println("Finished loading in " + (System.nanoTime() - elapsedTimeNPCs) / 1000000) ;
		long elapsedTimeBuildings = System.nanoTime();
		System.out.println("Loading building types") ;
		buildingTypes = initializeBuildingTypes() ;
		System.out.println("Finished loading in " + (System.nanoTime() - elapsedTimeBuildings) / 1000000) ;
		long elapsedTimeQuests = System.nanoTime();
		System.out.println("Loading quests") ;
		allQuests = initializeQuests(gameLanguage, player.getJob()) ;
		System.out.println("Finished loading in " + (System.nanoTime() - elapsedTimeQuests) / 1000000) ;
		long elapsedTimeMaps = System.nanoTime();
		System.out.println("Loading maps") ;
		allMaps = initializeAllMaps() ;
		System.out.println("Finished loading in " + (System.nanoTime() - elapsedTimeMaps) / 1000000) ;
		NPCs.setIDs() ;
		sideBar = new SideBar(player, player.getMovingAni().idleGif, pet != null ? pet.getMovingAni().idleGif : null) ;
		bat = new Battle() ;

		Pterodactile.setMessage(Game.allText.get(TextCategories.pterodactile)) ;

		player.InitializeSpells() ;
		player.setMap(Game.getMaps()[player.getJob()]) ;
		player.setPos(Game.getScreen().getCenter()) ;
		if (player.getSettings().getMusicIsOn())
		{
			Music.SwitchMusic(player.getMap().getMusic()) ;
		}
		System.out.println("Finished loading in " + (System.nanoTime() - elapsedTimeInit) / 1000000) ;
		
	}

	
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g) ;
		mousePos = UtilG.GetMousePos(mainPanel) ;
		DP.setGraphics((Graphics2D) g) ;

		// System.out.println("game is " + gameState) ;
		switch (state)
		{
			case opening:
				Opening.run(player, mousePos, DP) ;
				if (Opening.isOver())
				{
					Game.setState(GameStates.loading) ;
				}
				shouldRepaint = true ;	
				break ;

			case loading:
				Opening.displayLoadingScreen(DP) ;
				String name = Opening.getPlayerInfo()[0] ;
				String sex = Opening.getPlayerInfo()[1] ;
				difficultLevel = Integer.parseInt(Opening.getPlayerInfo()[2]) ;
				int job = Integer.parseInt(Opening.getPlayerInfo()[3]) ;
//				String name = "Salezin" ;
//				String sex = "M" ;
//				difficultLevel = 1 ;
//				int job = 1 ;
				player = new Player(name, sex, job) ;
				initialize() ;
//				initializeCheatMode() ;
				state = GameStates.running ;
	
				shouldRepaint = true ;	
				break ;

			case simulation:
				player.incrementCounters() ;
				player.activateCounters() ;
				player.getSatiation().setToMaximum() ;
				player.getThirst().setToMaximum() ;
				if (pet != null)
				{
					pet.incrementCounters() ;
				}
				if (pet != null)
				{
					pet.activateCounters() ;
				}
				if (pet != null)
				{
					pet.getSatiation().setToMaximum() ;
				}
	
				if (player.getCurrentAction() != null)
				{
					PlayerEvolutionSimulation.act(player.getCurrentAction(), mousePos) ;
				}
				if (player.isInBattle())
				{
	//	        		Creature creature = player.getOpponent() ;
	//	        		creature.fight() ;
					player.getOpponent().incrementCounters() ;
					PlayerEvolutionSimulation.playerFight() ;
	//	        		if (pet != null) { pet.fight() ;}
					bat.RunBattle(player, pet, player.getOpponent(), animations, DP) ;
					if (!player.isInBattle())
					{
						PlayerEvolutionSimulation.updateFitness() ;
						if (PlayerEvolutionSimulation.shouldUpdateGenes())
						{
							PlayerEvolutionSimulation.updateRecords() ;
							PlayerEvolutionSimulation.updateCreatureGenes() ;
						}
						PlayerEvolutionSimulation.checkPlayerWin() ;
					}
				} else
				{
					PlayerEvolutionSimulation.checkBattleRepeat() ;
				}
				PlayerEvolutionSimulation.displayInterface(mousePos, DP) ;
				if (player.shouldLevelUP())
				{
					player.levelUp(null) ;
				}
				if (pet != null)
				{
					if (pet.shouldLevelUP())
					{
						pet.levelUp(null) ;
					}
				}
				player.showWindows(pet, mousePos, DP) ;
	
				if (player.getOpponent() != null)
				{
					if (player.getOpponent().getAttWindow().isOpen())
					{
						((CreatureAttributesWindow) player.getOpponent().getAttWindow()).display(player.getOpponent(), DP) ;
					}
				}
	
				for (Animation ani : animations)
				{
					ani.run(DP) ;
				}
				player.resetAction() ;	
				break ;

			case running:
				runGame(DP) ;
				playGifs(DP) ;
				// DP.DrawImage(UtilG.loadImage("./images/test.png"), mousePos, Align.center) ;	
				break ;

			case playingStopTimeGif:
				playStopTimeGifs() ;	
				break ;

			case paused: break ;
			default: break ;
		}

		Toolkit.getDefaultToolkit().sync() ;
		g.dispose() ;
	}

	

	class TAdapter extends KeyAdapter
	{
		// TODO transformar actions em keyEvents
		@Override
		public void keyPressed(KeyEvent e)
		{
			int key = e.getKeyCode() ;

			shouldRepaint = true ;
			switch (key)
			{
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_UP:
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_RIGHT:
				player.setCurrentAction(KeyEvent.getKeyText(key)) ;
				return ;

			case KeyEvent.VK_ENTER:
			case KeyEvent.VK_ESCAPE:
			case KeyEvent.VK_BACK_SPACE:
			case KeyEvent.VK_SPACE:
			case KeyEvent.VK_F1:
			case KeyEvent.VK_F2:
			case KeyEvent.VK_F3:
			case KeyEvent.VK_F4:
			case KeyEvent.VK_F5:
			case KeyEvent.VK_F6:
			case KeyEvent.VK_F7:
			case KeyEvent.VK_F8:
			case KeyEvent.VK_F9:
			case KeyEvent.VK_F10:
			case KeyEvent.VK_F11:
			case KeyEvent.VK_F12:
			case KeyEvent.VK_A:
			case KeyEvent.VK_B:
			case KeyEvent.VK_C:
			case KeyEvent.VK_D:
			case KeyEvent.VK_E:
			case KeyEvent.VK_F:
			case KeyEvent.VK_G:
			case KeyEvent.VK_H:
			case KeyEvent.VK_I:
			case KeyEvent.VK_J:
			case KeyEvent.VK_K:
			case KeyEvent.VK_L:
			case KeyEvent.VK_M:
			case KeyEvent.VK_N:
			case KeyEvent.VK_O:
			case KeyEvent.VK_P:
			case KeyEvent.VK_Q:
			case KeyEvent.VK_R:
			case KeyEvent.VK_S:
			case KeyEvent.VK_T:
			case KeyEvent.VK_U:
			case KeyEvent.VK_V:
			case KeyEvent.VK_W:
			case KeyEvent.VK_X:
			case KeyEvent.VK_Y:
			case KeyEvent.VK_Z:
			case KeyEvent.VK_0:
			case KeyEvent.VK_1:
			case KeyEvent.VK_2:
			case KeyEvent.VK_3:
			case KeyEvent.VK_4:
			case KeyEvent.VK_5:
			case KeyEvent.VK_6:
			case KeyEvent.VK_7:
			case KeyEvent.VK_8:
			case KeyEvent.VK_9:
				player.setCurrentAction(KeyEvent.getKeyText(key)) ;
				return ;

			case KeyEvent.VK_NUMPAD0:
			case KeyEvent.VK_NUMPAD1:
			case KeyEvent.VK_NUMPAD2:
			case KeyEvent.VK_NUMPAD3:
			case KeyEvent.VK_NUMPAD4:
			case KeyEvent.VK_NUMPAD5:
			case KeyEvent.VK_NUMPAD6:
			case KeyEvent.VK_NUMPAD7:
			case KeyEvent.VK_NUMPAD8:
			case KeyEvent.VK_NUMPAD9:
				player.setCurrentAction(String.valueOf(key - 96)) ;
				return ;

			case KeyEvent.VK_PAUSE: return ;

			default: return ;
			}
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
			if (evt.getButton() == 1) // Left click
			{
				player.setCurrentAction("LeftClick") ;

//        		TestingAnimations.runTests(ani) ;
				// testGif.start() ;
			}
			if (evt.getButton() == 3) // Right click
			{
				player.setCurrentAction("MouseRightClick") ;
//        		player.setPos(mousePos) ;
				// testGif2.start() ;
			}
			// shouldRepaint = true ;
//			System.out.println(UtilG.Round(mousePos.x / 600.0, 2) + "," + UtilG.Round((mousePos.y - 96) / 384.0, 2) + " " + mousePos.x + " " + mousePos.y) ;
		}

		@Override
		public void mouseReleased(MouseEvent e)
		{
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