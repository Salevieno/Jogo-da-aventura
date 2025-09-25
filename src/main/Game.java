package main ;

import java.awt.Color ;
import java.awt.Dimension ;
import java.awt.Image ;
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

import UI.GameButton;
import battle.Battle;
import components.Building ;
import components.BuildingType ;
import components.NPC ;
import components.NPCType ;
import components.Projectiles ;
import components.Quest ;
import components.QuestSkills;
import graphics.Align;
import graphics2.Animation;
import graphics2.Draw;
import graphics2.Drawable;
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
import liveBeings.LiveBeing;
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
import utilities.Util;
import windows.BankWindow ;
import windows.PauseWindow;

public class Game
{
	// TODO arquivos - nomes das criaturas
	// TODO como reviver o pet quando ele/ela morre? :O
	// TODO optional - unificar throw item, calcPhysicalAtk e useSpell dos liveBeings
	// TODO make game run by time
	// TODO redraw art for new screen size
	// TODO resize fonts
	// TODO pause screen
	// TODO add fullscreen to the settings
	// TODO settings outside the player
	// TODO animations gif -> spritesheet
	private static final String[] konamiCode = new String[] { "Up", "Up", "Down", "Down", "Left", "Right", "Left", "Right", "B", "A" } ;
	
	public static Color[] palette ;
	public static final Color selColor ;
	private static final Color[] normalPalette ;
	private static final Color[] konamiPalette ;

	public static final String MainFontName = "Comics" ;
	private static final List<String> loadedImagePaths = new ArrayList<>() ;

	private static final GameStates mainState = GameStates.running ;
	private static final boolean testMode = true ;
	public static final boolean debugMode = false;

	private static GameStates state = GameStates.loading ;
	private static Languages gameLanguage ;
	private static boolean shouldRepaint ; // tells if the panel should be repainted, created to respond multiple requests only once
	private static boolean konamiCodeActive ;
	private static double dt ;
	public static GameTimer dayTimer ;


	public static Map<TextCategories, String[]> allText ;

	private static Player player ;
	private static Pet pet ;
	public static int difficultLevel = 2 ;
	private static int saveSlotInUse = -1 ;

	private static final Screen screen ;
	private static CityMap[] cityMaps ;
	private static FieldMap[] fieldMaps ;
	private static SpecialMap[] specialMaps ;
	private static GameMap[] allMaps ;
	private static BuildingType[] buildingTypes ;
	private static NPCType[] npcTypes ;
	private static Item[] allItems ;
	private static Spell[] allSpells ;
	private static Quest[] allQuests ;

	private static final PauseWindow settingsWindow ;
	public static final List<String> arrowKeys = List.of(KeyEvent.getKeyText(KeyEvent.VK_UP),
															KeyEvent.getKeyText(KeyEvent.VK_LEFT),
															KeyEvent.getKeyText(KeyEvent.VK_DOWN),
															KeyEvent.getKeyText(KeyEvent.VK_RIGHT)) ;

	static
	{
		screen = new Screen(new Dimension(GameFrame.getWindowsize().width, GameFrame.getWindowsize().height), null) ;
		normalPalette = readColorPalette(loadImage("ColorPalette.png"), "Normal") ;
		konamiPalette = readColorPalette(loadImage("KonamiPalette.png"), "Konami") ;
		selColor = normalPalette[18] ;
		palette = normalPalette ;
		gameLanguage = Languages.portugues ;
		allText = new HashMap<>() ;
		dayTimer = new GameTimer(600) ;

		settingsWindow = new PauseWindow() ;
		dt = System.nanoTime() ;
	}

	public Game()
	{
		player = new Player("", "", 1) ;
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
	public static int getSaveSlotInUse() { return saveSlotInUse ;}
	public static void setSaveSlotInUse(int newSaveSlotInUse) { saveSlotInUse = newSaveSlotInUse ;}

	public static double dayTimeRate() { return dayTimer.rate() <= 0.5 ? dayTimer.rate() + 0.5 : dayTimer.rate() - 0.5 ;}
	
	public static void setPlayer(Player newPlayer) { player = newPlayer ;}
	public static void setState(GameStates newState) { state = newState ;}
	public static void switchToMainState() { state = mainState ;}

	private static Color[] readColorPalette(Image paletteImage, String mode)
	{
		Color[] palette = new Color[28] ;

		switch (mode)
		{
			case "Normal":			
				for (int y = 0 ; y <= 7 - 1 ; y += 1)
				{
					for (int x = 0 ; x <= 4 - 1 ; x += 1)
					{
						palette[x + 4 * y] = Util.getPixelColor(Util.toBufferedImage(paletteImage), new Point(x, y)) ;
					}
				}				
				return palette ;
			
			case "Konami":			
				for (int x = 0 ; x <= 4 - 1 ; x += 1)
				{
					for (int y = 0 ; y <= 7 - 1 ; y += 1)
					{
						palette[7 * x + y] = Util.getPixelColor(Util.toBufferedImage(paletteImage), new Point(x, y)) ;
					}
				}				
				return palette ;

			default: return null ;
		}
	}
	
	public static Image loadImage(String path)
	{
		if (loadedImagePaths.contains(path)) { System.out.println("Warn: Loading image " + path + " multiple times") ;}

		loadedImagePaths.add(path);
		return Util.loadImage(Path.IMAGES + path) ;
	}

	private static void checkKonamiCode(List<String> combo)
	{
		if (!Arrays.equals(combo.toArray(new String[combo.size()]), konamiCode)) { return ;}
		System.out.println("Activating konami code!");
		konamiCodeActive = !konamiCodeActive ;
		player.resetCombo() ;
	}

	private static void konamiCode()
	{
		dayTimer.setDuration(6) ;
		palette = konamiPalette ;
		if (dayTimer.getCounter() % 1200 <= 300)
		{
			Draw.stdAngle += 0.04 ;
		}
		else if (dayTimer.getCounter() % 1200 <= 900)
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
	
	
	private static void loadAllText()
	{

		JSONObject textData = Util.readJsonObject(Path.TEXT_BR) ;

		Iterator<?> iterator = textData.keySet().iterator() ;

		while (iterator.hasNext())
		{
			Object key = iterator.next() ;
			TextCategories catName = TextCategories.catFromBRName((String) key) ;

			if (catName == null) { continue ;}
			
			if (catName.equals(TextCategories.npcs))
			{
				NPC.loadText(textData, key, allText, catName) ;
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

	private static void initializeTestMode()
	{

		
    	player.setName("Rosquinhawwwwwwwwwwwwwww") ;
//    	player.setLevel(50) ;
    	player.setMap(cityMaps[1]) ;
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
				screen.setBorders(new int[] {0 + 20, Sky.height + 20, screen.getSize().width - 60 - 20, screen.getSize().height - 20}) ;
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
				NPC.setIDs() ;

//				player.InitializeSpells() ;
				if (player.getSpells().isEmpty())
				{
					player.setSpells(Player.jobSpells(player.getJob())) ;
				}
				player.setMap(Game.getMaps()[player.getJob()]) ;
				player.setPos(Game.getScreen().getCenter()) ;
				LiveBeing.updateDamageAnimation(player.getSettings().getDamageAnimation()) ;
				SideBar.initialize();
				
				if (player.getSettings().getMusicIsOn())
				{
					Music.SwitchMusic(player.getMap().getMusic()) ;
				}
				dayTimer.start() ;
				Log.loadTime("last stuff", initialTime) ;
				

				if (Game.testMode)
				{
					Game.initializeTestMode() ;
				}

				return ;
			
			default: return ;
		}
		
	}


	private void activateCounters()
	{
		if (dayTimer.hasFinished())
		{
			dayTimer.restart() ;
			NPC.renewStocks() ;
		}
		player.activateSpellCounters() ;

		if (pet != null)
		{
			pet.activateSpellCounters() ;
		}

		if (player.getMap().isField())
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

		if (!player.getMap().isField()) { return ;}

		FieldMap fm = (FieldMap) player.getMap() ;
		fm.getCreatures().forEach(Creature::activateCounters) ;			
		fm.activateCollectiblesCounter() ;

	}
	
	private void creaturesAct(double dt)
	{
		List<Creature> creaturesInMap = ((FieldMap) player.getMap()).getCreatures() ;
		for (Creature creature : creaturesInMap)
		{
			creature.takeBloodAndPoisonDamage() ;
			creature.act(player.getPosAsDouble(), player.getMap(), dt);
		}
	}
	
	private void petActs(double dt)
	{
		if (!pet.isAlive()) { return ;}

		pet.takeBloodAndPoisonDamage() ;
		pet.updateCombo() ;
		pet.think(player.isFighting(), player.getPosAsDouble()) ;
		pet.act(player, dt) ;
	}
	
	private void playerActs()
	{

		player.takeBloodAndPoisonDamage() ;
		player.updateBloodAndPoisonStatistics() ;
		if (player.canAct() & player.hasActed())
		{
			player.acts(pet, GamePanel.getMousePos()) ;
		}

		player.doCurrentAction() ;
		player.applyAdjacentGroundEffect() ;
		player.finishStatus() ;

		if (player.isMoving())
		{
			for (int i = 0 ; i <= 0 ; i += 1)
			{
				player.move(pet);
			}
		}
	}
	
	
	private void run(double dt)
	{

		if (Math.pow(10, 10) <= dt) { return ;}

		activateCounters() ;

		checkKonamiCode(player.getCombo()) ;
		if (konamiCodeActive)
		{
			konamiCode() ;
		}

		if (player.getMap().isField())
		{
			creaturesAct(dt / Math.pow(10, 9)) ;
			shouldRepaint = true ;
		}

		if (pet != null)
		{
			petActs(dt) ;
		}

		playerActs() ;

		if (player.getMap().isField())
		{
			player.setClosestCreature(player.closestCreatureInRange()) ;
		}

		player.checkMeet(GamePanel.getMousePos()) ;

		if (player.isFighting() && player.getOpponent() != null)
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
		
		player.resetAction() ;

		SpriteAnimation.updateAll();

	}
	
	private void draw()
	{
		if (!player.getMap().getContinent().equals(Continents.cave))
		{
			screen.displaySky() ;
		}
		player.getMap().display() ;
		player.getMap().displayGroundTypes() ;
		Draw.mapElements(player.getHitbox(), player.getPos(), player.getMap()) ;
		
		List<Drawable> drawables = new ArrayList<>() ;
		
		if (player.getMap().isField())
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
		drawables.forEach(Drawable::display) ;
		
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
		SpriteAnimation.displayAll(GamePanel.DP) ;
		
		SideBar.display(player, pet, GamePanel.getMousePos()) ;
		
		if (settingsWindow.isOpen())
		{
			settingsWindow.display(GamePanel.getMousePos()) ;
		}

		if (debugMode)
		{
			screen.displayBorders() ;
		}
	}

	protected void update()
	{
		GameButton.updateMouseCursor(GamePanel.getMousePos()) ;
		GameButton.updateSelected(GamePanel.getMousePos()) ;
		GameButton.actWhenClicked(GamePanel.getMousePos(), player.getCurrentAction());
		switch (state)
		{
			case opening:
				Opening.run(player, GamePanel.getMousePos()) ;
				shouldRepaint = true ;
				return ;

			case loading:
				LoadingGame.run(player, GamePanel.getMousePos()) ;
				shouldRepaint = true ;
				return ;

			case simulation:
				EvolutionSimulation.run(GamePanel.getMousePos()) ;	
				return ;

			case running:
				run(System.nanoTime() - dt) ;
				dt = System.nanoTime() ;
				draw() ;
				return ;

			case paused: return ;
			default: return ;
		}
	}
	
	private String keypadNumberValue(int keyCode) { return String.valueOf(keyCode - 96) ;}
	
	private void switchPauseRunning()
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

	protected void keyPressedAction(KeyEvent event)
	{		
		int keyCode = event.getKeyCode() ;
		if (KeyEvent.VK_NUMPAD0 <= keyCode & keyCode <= KeyEvent.VK_NUMPAD9)
		{
			player.setCurrentAction(keypadNumberValue(keyCode)) ;
			return ;
		}
		
		if (keyCode == KeyEvent.VK_PAUSE)
		{
			switchPauseRunning() ;
		}
		
		if (keyCode == KeyEvent.VK_F12)
		{
			GameFrame.getMe().resizeWindow() ;
			screen.updateScale() ;
		}

		if (keyCode == KeyEvent.VK_ESCAPE)
		{
			settingsWindow.switchOpenClose() ;
		}

		if (arrowKeys.contains(KeyEvent.getKeyText(keyCode)))
		{
			player.addKeyPressed(KeyEvent.getKeyText(keyCode)) ;
			if (player.isMoving())
			{
				player.chooseDirection(KeyEvent.getKeyText(keyCode)) ;
			}
		}
		
		player.setCurrentAction(KeyEvent.getKeyText(keyCode)) ;
	}

	protected void keyReleasedAction(KeyEvent event)
	{
		int keyCode = event.getKeyCode() ;
		if (arrowKeys.contains(KeyEvent.getKeyText(keyCode)))
		{
			player.removeKeyPressed(KeyEvent.getKeyText(keyCode)) ;
		}
	}
	
	protected void mouseClickedAction(MouseEvent evt)
	{
	}
	
	protected void mousePressedAction(MouseEvent evt)
	{
		if (!GameStates.running.equals(state)) { return ;}

		if (player.getBag().isOpen())
		{
			player.getBag().setItemFetched(player.getBag().itemHovered(GamePanel.getMousePos())) ;
		}
	}
	
	protected void mouseReleaseAction(MouseEvent evt)
	{
		if (evt.getButton() == 1)
		{
			player.setCurrentAction("LeftClick") ;
		}
		if (evt.getButton() == 3)
		{
			player.setCurrentAction("MouseRightClick") ;
    		player.setPos(GamePanel.getMousePos()) ;
    		// Log.attributes(player) ;
    		// System.out.println("map: " + player.getMap().getName());
    		if (pet != null)
    		{
    			pet.setPos(player.getPos()) ;
    		}
		}

		if (!GameStates.running.equals(state)) { return ;}

		int hotKeySlotHovered = HotKeysBar.slotHovered(GamePanel.getMousePos()) ;
		
		if (-1 < hotKeySlotHovered)
		{
			player.getHotItems().set(hotKeySlotHovered, player.getBag().getItemFetched()) ;
		}

		player.getBag().setItemFetched(null) ;
	}
	
	protected void mouseWheelAction(MouseWheelEvent evt)
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