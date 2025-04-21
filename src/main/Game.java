package main ;

import java.awt.Color ;
import java.awt.Dimension ;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList ;
import java.util.Arrays ;
import java.util.Comparator;
import java.util.HashMap ;
import java.util.Iterator ;
import java.util.List ;
import java.util.Map ;

import org.json.simple.JSONArray ;
import org.json.simple.JSONObject ;

import Battle.Battle;
import components.Building ;
import components.BuildingType ;
import components.NPCType ;
import components.NPCs ;
import components.Projectiles ;
import components.Quest ;
import components.QuestSkills;
import graphics.Align;
import graphics.Scale;
import graphics2.Animation;
import graphics2.Draw;
import graphics2.Drawable;
import graphics2.Gif;
import graphics2.SpriteAnimation;
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
import liveBeings.Buff ;
import liveBeings.Creature ;
import liveBeings.CreatureType ;
import liveBeings.HotKeysBar;
import liveBeings.Pet ;
import liveBeings.Player ;
import liveBeings.Spell ;
import liveBeings.SpellsBar;
import maps.CityMap ;
import maps.Continents;
import maps.FieldMap ;
import maps.GameMap ;
import maps.SpecialMap ;
import screen.Screen ;
import screen.SideBar ;
import screen.Sky ;
import simulations.EvolutionSimulation;
import utilities.GameStates ;
import utilities.GameTimer;
import utilities.Log;
import utilities.Util;
import utilities.UtilS ;
import windows.BankWindow ;

public class Game
{
	// TODO arquivos - nomes das criaturas
	// TODO como reviver o pet quando ele/ela morre? :O
	// TODO optional - unificar throw item, calcPhysicalAtk e useSpell dos liveBeings
	// TODO make game run by time
	private static final String[] konamiCode = new String[] { "Up", "Up", "Down", "Down", "Left", "Right", "Left", "Right", "B", "A" } ;
	private static final Color[] normalPalette ;
	private static final Color[] konamiPalette ;
	
	public static final String JSONPath = ".\\json\\" ;
	public static final String CSVPath = ".\\csv\\" ;
	public static final String dadosPath = ".\\dados\\" ;
	public static final String ImagesPath = ".\\images\\" ;
	public static final String MusicPath = ".\\music\\" ;
	public static final String TextPathBR = "./Texto-PT-br.json" ;
	public static final String MainFontName = "Comics" ;

	private static final GameStates mainState = GameStates.running ;
	private static final boolean cheatMode = false ;
	public static final boolean displayHitboxes = false;

	private static GameStates state = GameStates.loading ;
	private static Languages gameLanguage ;
	private static boolean shouldRepaint ; // tells if the panel should be repainted, created to respond multiple requests only once
	private static boolean konamiCodeActive ;


	public static Color[] palette ;
	public static Map<TextCategories, String[]> allText ;

	private static Player player ;
	private static Pet pet ;
	public static int difficultLevel = 2 ;
	private static int slotLoaded = -1 ;

	private static final Screen screen ;
	private static Sky sky ;
	private static CityMap[] cityMaps ;
	private static FieldMap[] fieldMaps ;
	private static SpecialMap[] specialMaps ;
	private static GameMap[] allMaps ;
	private static BuildingType[] buildingTypes ;
	private static NPCType[] npcTypes ;
	private static Item[] allItems ;
	private static Spell[] allSpells ;
	private static Quest[] allQuests ;

	static
	{
		Dimension windowSize = GameFrame.getWindowsize() ;
		screen = new Screen(new Dimension(windowSize.width, windowSize.height), null) ;
		gameLanguage = Languages.portugues ;
		normalPalette = UtilS.ReadColorPalette(UtilS.loadImage("ColorPalette4.png"), "Normal") ;
		konamiPalette = UtilS.ReadColorPalette(UtilS.loadImage("ColorPalette.png"), "Konami") ;
		palette = normalPalette ;
		konamiCodeActive = false ;
		allText = new HashMap<>() ;
		shouldRepaint = false ;
	}

	public Game()
	{
		player = new Player("", "", 2) ;
	}
	
	public static GameStates getState() { return state ;}
	public static Languages getLanguage() { return gameLanguage ;}
	public static Screen getScreen() { return screen ;}
	public static NPCType[] getNPCTypes() { return npcTypes ;}
	public static Player getPlayer() { return player ;}
	public static Pet getPet() { return pet ;}
	public static GameMap[] getMaps() { return allMaps ;}
	public static Quest[] getAllQuests() { return allQuests ;}
	public static Item[] getAllItems() { return allItems ;}
	public static Spell[] getAllSpells() { return allSpells ;}
	public static boolean getShouldRepaint() { return shouldRepaint ;}
	public static int getSlotLoaded() { return slotLoaded ;}
	public static void setSlotLoaded(int newSlotLoaded) { slotLoaded = newSlotLoaded ;}
	
	
	public static void setPlayer(Player newPlayer) { player = newPlayer ;}
	public static void setState(GameStates newState) { state = newState ;}
	public static void switchToMainState() { state = mainState ;}

	private static void checkKonamiCode(List<String> combo)
	{
		if (!Arrays.equals(combo.toArray(new String[combo.size()]), konamiCode)) { return ;}
		System.out.println("Activating konami code!");
		konamiCodeActive = !konamiCodeActive ;
		player.resetCombo() ;
	}

	private static void konamiCode()
	{
		Sky.dayCounter.setDuration(6) ;
		palette = konamiPalette ;
		if (Sky.dayCounter.getCounter() % 1200 <= 300)
		{
			Draw.stdAngle += 0.04 ;
		}
		else if (Sky.dayCounter.getCounter() % 1200 <= 900)
		{
			Draw.stdAngle -= 0.04 ;
		}
		else
		{
			Draw.stdAngle += 0.04 ;
		}
	}

	public static void removePet() { pet = null ;}

	public static void letThereBePet()
	{
		int job = Util.randomInt(0, 3) ;
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
		SideBar.addPetButton(player, pet) ;
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
				TextCategories textCatOption = TextCategories.catFromBRName(catName + npcName + "Opcoes" + j) ;

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

	private static void initializeCheatMode()
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
    	player.getBag().add(Equip.getAll()[2], 20) ;
    	player.getBag().add(Equip.getAll()[100], 20) ;
    	player.getBag().add(Equip.getAll()[102], 20) ;
    	player.getBag().add(Equip.getAll()[110], 20) ;
    	player.getBag().add(Equip.getAll()[111], 20) ;
    	player.getBag().add(Equip.getAll()[112], 20) ;
    	player.getBag().add(Equip.getAll()[115], 20) ;
    	player.getBag().add(Equip.getAll()[116], 20) ;
    	player.getBag().add(Equip.getAll()[117], 20) ;
    	player.getBag().add(Equip.getAll()[121], 20) ;
    	player.getBag().add(Equip.getAll()[122], 20) ;
    	player.getBag().add(Equip.getAll()[123], 20) ;
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
	
	protected static void initialize(int step)
	{
		
		long initialTime = System.nanoTime();

		switch (step)
		{
			case 0:
				sky = new Sky() ;
				screen.setBorders(new int[] { 0, Sky.height, screen.getSize().width - 40, screen.getSize().height }) ;
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
				npcTypes = NPCType.load(gameLanguage) ;
				Log.loadTime("npc types", initialTime) ;
				return ;
				
			case 7:
				buildingTypes = BuildingType.load() ;
				Log.loadTime("building types", initialTime) ;
				return ;
				
			case 8:
				allQuests = Quest.load(gameLanguage, player.getJob(), CreatureType.all, Item.allItems) ;
				Log.loadTime("quests", initialTime) ;
				return ;
				
			case 9:
				cityMaps = CityMap.load(buildingTypes) ;
				fieldMaps = FieldMap.load(npcTypes) ;
				specialMaps = SpecialMap.load(Item.allItems) ;
				allMaps = GameMap.assemble(cityMaps, fieldMaps, specialMaps) ;
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
				

				if (Game.cheatMode)
				{
					Game.initializeCheatMode() ;
				}
				
				// TODO incluir criaturas 230 a 234 em algum mapa
//				Map<Integer, Integer> mapLevelPower = new HashMap<>() ;
//				for (CreatureType creatureType : CreatureType.all)
//				{
//					Creature creature = new Creature(creatureType) ;
//					System.out.println(creature.getType().getID() + " " + creature.getType().getLevel() + " " + creature.totalPower());
//					if (mapLevelPower.get(creature.getType().getLevel()) == null)
//					{
//						mapLevelPower.put(creature.getType().getLevel(), creature.totalPower()) ;
//					}
//					else
//					{
//						mapLevelPower.put(creature.getType().getLevel(), mapLevelPower.get(creature.getType().getLevel()) + creature.totalPower()) ;
//					}
//				}
//				
//				mapLevelPower.forEach((key, value) -> System.out.println(key + " " + (value / 5.0))) ;
//				for (Item item : Equip.getAll()) { player.getBag().add(item, 20) ;}
//				for (int i = 0 ; i <= 0 ; i += 1)
//				{
////					player.useItem(Equip.getAll()[200 * player.getJob() + i]) ;
//					System.out.println(player.totalPower()) ;
//					EvolutionSimulation.playerLevelUpAvr() ;
//					EvolutionSimulation.autoApplyAttPoints(player) ;
//					EvolutionSimulation.trainAvr(player) ;
//				}
				
//				int numCat = 12 ;
//				int maxPontos = 100 ;
//				
//				for (int crea = 0 ; crea <= 300 - 1 ; crea += 1)
//				{
//					List<Integer> ptsPorCat = new ArrayList<>() ;
//
//					for (int i = 0 ; i <= numCat - 1 ; i += 1)
//					{
//						ptsPorCat.add(0) ;
//					}
//					for (int i = 0 ; i <= (maxPontos * numCat - 1) / 2 ; i += 1)
//					{
//						List<Integer> validCats = new ArrayList<>() ;
//						for (int j = 0 ; j <= ptsPorCat.size() - 1 ; j += 1)
//						{
//							if (ptsPorCat.get(j) < maxPontos)
//							{
//								validCats.add(j) ;
//							}
//						}
//						int randomCat = validCats.get(Util.randomInt(0, validCats.size() - 1)) ;
//						ptsPorCat.set(randomCat, ptsPorCat.get(randomCat) + 1) ;
//					}
//					System.out.println(ptsPorCat) ;
////					System.out.println(ptsPorCat.stream().mapToInt(i -> i).sum()) ;
//				}
				
				return ;
			
			default: return ;
		}
		
	}


	private void activateCounters()
	{
		if (Sky.dayCounter.finished())
		{
			Sky.dayCounter.restart() ;
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
			Building bank = city.getBank() ;
			
			if (!bank.hasNPCs()) { continue ;}
			
			BankWindow bankWindow = (BankWindow) bank.getNPCs().get(0).getWindow() ;
			if (bankWindow.hasInvestment() & bankWindow.investmentIsComplete())
			{
				bankWindow.completeInvestment() ;
			}
		}

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

//			repaint() ;
		}
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
				creature.display(creature.getPos(), Scale.unit) ;
				continue ;
			}
			if (creature.canAct())
			{
				creature.think() ;
				creature.act() ;
			}
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
	}
	
	private void playerActs()
	{

		player.takeBloodAndPoisonDamage() ;
		player.updateBloodAndPoisonStatistics() ;
		
		if (player.canAct() & player.hasActed())
		{
			player.acts(pet, GamePanel.getMousePos()) ;

			if (player.getActionCounter().finished())
			{
				player.getActionCounter().reset() ;
			}
		}

		player.doCurrentAction() ;
		player.applyAdjacentGroundEffect() ;
		player.finishStatus() ;
	}
	
	
	private void run()
	{

		activateCounters() ;

		checkKonamiCode(player.getCombo()) ;
		if (konamiCodeActive)
		{
			konamiCode() ;
		}

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

		player.checkMeet(GamePanel.getMousePos()) ;

		if (player.isInBattle())
		{
			Battle.runBattle(player, pet, player.getOpponent()) ;
			if (Battle.isOver(player, pet, player.getOpponent()))
			{
				Battle.finishBattle(player, pet, player.getOpponent()) ;
			}
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
			player.talkToNPC(GamePanel.getMousePos()) ;

		}

		Projectiles.updateAll(player, pet);
		
		if (player.getJob() == 3)
		{
			player.useAutoSpell(true, player.getSpells().get(12)) ;
		}
		
		SideBar.act(player.getCurrentAction(), GamePanel.getMousePos()) ;
		
		player.resetAction() ;

		SpriteAnimation.updateAll();
		
		draw() ;
		
		
		
//		player.display(player.getPos(), Scale.unit, player.getDir(), player.getSettings().getShowAtkRange()) ;
//		player.getMap().displayBuildings(player.getHitbox(), player.getPos(), Arrays.asList(Game.getMaps()).indexOf(player.getMap())) ;


	}
	
	private void draw()
	{
//		Draw.map(player.getMap(), sky) ;

		if (!player.getMap().getContinent().equals(Continents.cave))
		{
			sky.display() ;
		}
		player.getMap().display() ;
		player.getMap().displayGroundTypes() ;
		Draw.mapElements(player.getHitbox(), player.getPos(), player.getMap(), sky) ;
		

		List<Drawable> drawables = new ArrayList<>() ;
		
		if (player.getMap().isAField())
		{
			List<Creature> creaturesInMap = ((FieldMap) player.getMap()).getCreatures() ;
			for (Creature creature : creaturesInMap)
			{
				drawables.add(creature) ;
			}	
		}
		

		if (pet != null)
		{
			drawables.add(pet) ;
		}
		
		drawables.add(player) ;
		if (player.getMap().getBuildings() != null)
		{			
			player.getMap().getBuildings().forEach(building -> drawables.add(building)) ;
		}
		player.getMap().getMapElem().forEach(mapElem -> drawables.add(mapElem));
		drawables.sort(Comparator.comparingInt(d -> d.getPos().y));
		drawables.forEach(drawable -> drawable.display()) ;
		
		if (player.isTalkingToNPC())
		{
			player.getNPCInContact().displaySpeech() ;
			player.getNPCInContact().displayOptions() ;
		}
		
		player.showWindows(pet, GamePanel.getMousePos()) ;
		
		if (player.getBag().getItemFetched() != null)
		{
			GamePanel.DP.drawImage(player.getBag().getItemFetched().getImage(), GamePanel.getMousePos(), Align.center) ;
		}

		Animation.playAll() ;
		Gif.playAll() ;
		SpriteAnimation.displayAll(GamePanel.DP) ;
		
		SideBar.display(player, pet, GamePanel.getMousePos()) ;
		
	}

	protected void update()
	{
		switch (state)
		{
			case opening:
				Opening.run(player, GamePanel.getMousePos()) ;
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
				LoadingGame.run(player, GamePanel.getMousePos()) ;
				shouldRepaint = true ;
//				Opening.displayLoadingScreen(player.getCurrentAction(), GamePanel.getMousePos()) ;
//				if (!Opening.loadingIsOver())
//				{
//					initialize(Opening.getLoadingStep()) ;
//					Opening.incLoadingStep() ;
//					if (Opening.loadingIsOver())
//					{
//						Opening.activateStartButton() ;
////						Opening.incLoadingStep() ;
////						JobBuild.printAll() ;
//					}
//				}
//	
//				if (Opening.gameStarted())
//				{
////			    	player.switchOpenClose(player.getHintsindow()) ;
//					if (cheatMode) { initializeCheatMode() ;}
//					player.startCounters() ;
//					Game.setState(mainState) ;
////					player.levelUp();
//				}
				break ;

			case simulation:
				EvolutionSimulation.run(GamePanel.getMousePos()) ;	
				break ;

			case running:
				run() ;
				// GamePanel.DP.DrawImage(Util.loadImage("./images/test.png"), mousePos, Align.center) ;	
				break ;

			case playingStopTimeGif:
				playStopTimeGifs() ;	
				break ;

			case paused: break ;
			default: break ;
		}
	}
	
	public void keyAction(KeyEvent event)
	{
		
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
				GameFrame.resumeGame();
				GameTimer.resumeAll() ;
			}
			else
			{
				GameFrame.pauseGame() ;
				GameTimer.stopAll() ;
			}
		}
		
		if (keyCode == KeyEvent.VK_6)
		{
			GameFrame.getMe().resizeWindow() ;
			screen.updateScale() ;
		}

		if (keyCode == KeyEvent.VK_ESCAPE)
		{
			MainGame3_4.closeGame() ;
		}
		
		player.setCurrentAction(KeyEvent.getKeyText(keyCode)) ;
	}
	
	public void mouseAction(MouseEvent evt)
	{
		if (evt.getButton() == 1)
		{
			player.setCurrentAction("LeftClick") ;
		}
		if (evt.getButton() == 3)
		{
			player.setCurrentAction("MouseRightClick") ;
    		player.setPos(GamePanel.getMousePos()) ;
    		if (pet != null)
    		{
    			pet.setPos(player.getPos()) ;
    		}
		}
	}
	
	public void mousePressedAction(MouseEvent evt)
	{
		if (player.getBag().isOpen())
		{
			player.getBag().setItemFetched(player.getBag().itemHovered(GamePanel.getMousePos())) ;
		}
	}
	
	public void mouseReleaseAction(MouseEvent evt)
	{
		// TODO must work only when game is runnning
//		int hotKeySlotHovered = HotKeysBar.slotHovered(GamePanel.getMousePos()) ;
//		
//		if (-1 < hotKeySlotHovered)
//		{
//			player.getHotItems()[hotKeySlotHovered] = player.getBag().getItemFetched() ;
//		}

		player.getBag().setItemFetched(null) ;
	}
	
	public void mouseWheelAction(MouseWheelEvent evt)
	{
		if (evt.getWheelRotation() < 0)
		{
			player.setCurrentAction("MouseWheelUp") ;
		}
		else
		{
			player.setCurrentAction("MouseWheelDown") ;
		}
	}


}