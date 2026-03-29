package main;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Buildings.Building;
import NPC.NPC;
import NPC.NPCType;
import UI.GameButton;
import animations.Animation;
import battle.AtkTypes;
import battle.Battle;
import components.Projectiles;
import components.Quest;
import components.QuestSkills;
import graphics.Align;
import graphics2.Draw;
import graphics2.Drawable;
import graphics2.SpriteAnimation;
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
import liveBeings.Creature;
import liveBeings.HotKeysBar;
import liveBeings.Pet;
import liveBeings.Player;
import liveBeings.Spell;
import liveBeings.SpellsBar;
import maps.CityMap;
import maps.Continents;
import maps.FieldMap;
import maps.GameMap;
import screen.Screen;
import screen.SideBar;
import simulations.EvolutionSimulation;
import utilities.Util;
import windows.BankWindow;
import windows.PauseWindow;

public class Game
{
	// TODO arquivos - nomes das criaturas
	// TODO optional - unificar throw item, calcPhysicalAtk e useSpell dos liveBeings
	// TODO ideia - magias e flechas são projéteis que precisam acertar o oponente para dar dano. O oponente pode se mover durante a luta E usar magias de longe enquanto se move (ou defender)
	// TODO ideia - todo personagem pode inspecionar para aprender 1 att ou 2 da criatura, mas tem que estar perto e isso pode provocar certas criaturas meio agressivas
	// TODO sign deixa de ser building
	// TODO shopping de cada cidade vender itens diferentes
	// TODO ideia: ao invés de ter mapa, os npcs indicarem a direção das cidades
	// FIXME bichos andando sobre a água ao perseguir
	private static final List<String> konamiCode = List.of("Up", "Up", "Down", "Down", "Left", "Right", "Left", "Right", "B", "A") ;


	public static final String MainFontName = "Comics";
	private static final GameStates mainState = GameStates.running;
	private static final boolean testMode = true;
	public static final boolean debugMode = false;

	private static GameStates state = GameStates.loading;
	private static Languages gameLanguage;
	private static boolean shouldRepaint; // tells if the panel should be repainted, responding to multiple requests only once
	private static boolean konamiCodeIsActive;
	private static double dt;
	public static GameTimer dayTimer;

	public static Map<TextCategories, String[]> allText;

	private static Player player;
	private static Pet pet;
	public static int difficultLevel = 0;
	private static int saveSlotInUse = -1;

	private static final Screen screen;
	private static CityMap[] cityMaps;
	private static FieldMap[] fieldMaps;
	private static GameMap[] allMaps;
	private static NPCType[] npcTypes;
	private static Item[] allItems;
	private static Spell[] allSpells;
	private static Quest[] allQuests;

	// TODO unificar pausewindow com as outras windows
	private static final PauseWindow pauseWindow;
	public static final List<String> arrowKeys = List.of(KeyEvent.getKeyText(KeyEvent.VK_UP),
															KeyEvent.getKeyText(KeyEvent.VK_LEFT),
															KeyEvent.getKeyText(KeyEvent.VK_DOWN),
															KeyEvent.getKeyText(KeyEvent.VK_RIGHT));
	private static final int DAY_DURATION = 600 ;
    private static Settings settings ;

	static
	{
		screen = new Screen(new Dimension(GameFrame.getWindowsize().width, GameFrame.getWindowsize().height), null);
		gameLanguage = Languages.portugues;
		allText = new HashMap<>();
		dayTimer = new GameTimer(DAY_DURATION);
		settings = new Settings(false, true, false, 1, 0) ;

		pauseWindow = new PauseWindow();
		dt = System.nanoTime();
	}

	public Game()
	{
		player = new Player("", "", 1);
	}

	public static GameStates getState() { return state ;}
	public static Languages getLanguage() { return gameLanguage ;}
	public static Settings getSettings() { return settings ;}
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
	public static PauseWindow getPauseWindow() { return pauseWindow ;}
	public static void setPlayer(Player newPlayer) { player = newPlayer ;}
	public static void setState(GameStates newState) { state = newState ;}
	public static GameTimer getDayTimer() { return dayTimer ;}
	public static void setCityMaps(CityMap[] cityMaps) { Game.cityMaps = cityMaps ;}
	public static void setFieldMaps(FieldMap[] fieldMaps) { Game.fieldMaps = fieldMaps ;}
	public static void setAllMaps(GameMap[] allMaps) { Game.allMaps = allMaps ;}
	public static void setNpcTypes(NPCType[] npcTypes) { Game.npcTypes = npcTypes ;}
	public static void setAllQuests(Quest[] allQuests) { Game.allQuests = allQuests ;}

	public static void switchToMainState() { state = mainState ;}
	public static double dayTimeRate() { return dayTimer.rate() <= 0.5 ? dayTimer.rate() + 0.5 : dayTimer.rate() - 0.5 ;}

	private static void activateKonamiEffect()
	{
		Palette.switchToKonami() ;
		dayTimer.setDuration(DAY_DURATION / 100.0);
	}

	private static void deactivateKonamiEffect()
	{
		Palette.switchToNormal() ;
		dayTimer.setDuration(DAY_DURATION);
		Draw.stdAngle = 0;
	}

	private static void checkKonamiCode(List<String> combo)
	{
		if (!konamiCode.equals(combo)) { return ;}

		konamiCodeIsActive = !konamiCodeIsActive;
		if (konamiCodeIsActive)
		{
			activateKonamiEffect() ;
		}
		else
		{
			deactivateKonamiEffect() ;
		}
		player.resetCombo();
	}

	private static void applyKonamiEffect(double dt)
	{
		if (dayTimer.rate() <= 0.25)
		{
			Draw.stdAngle += 0.04;
		}
		else if (dayTimer.rate() <= 0.75)
		{
			Draw.stdAngle -= 0.04;
		}
		else
		{
			Draw.stdAngle += 0.04;
		}
	}

	public static void removePet()
	{
		pet = null;
	}

	public static void letThereBePet()
	{
		int job = Util.randomInt(0, 3);
		pet = new Pet(job);
		pet.setPos(player.getPosAsDouble());
		if (player.getJob() == 3 & 0 < player.getSpells().get(13).getLevel()) // Best friend
		{
			int spellLevel = player.getSpells().get(13).getLevel();
			pet.getPA().getLife().incMaxValue(10 * spellLevel);
			pet.getPA().getLife().setToMaximum();
			pet.getPA().getMp().incMaxValue(10 * spellLevel);
			pet.getPA().getMp().setToMaximum();
			pet.getBA().getPhyAtk().incBaseValue(2 * spellLevel);
			pet.getBA().getMagAtk().incBaseValue(2 * spellLevel);
			pet.getBA().getDex().incBaseValue(1 * spellLevel);
			pet.getBA().getAgi().incBaseValue(1 * spellLevel);
		}
		SideBar.addPetButton(player, pet);
	}

	private static void initializeTestMode()
	{

		player.setName("Rosquinhawwwwwwwwwwwwwww");
		// player.setLevel(50) ;
		player.setMap(cityMaps[1]);
		// fieldMaps[1].getCreatures().get(0).setPos(player.getPos());
		// player.setPos(new Point(393, 140)) ;

		// letThereBePet() ;
		// pet.takeDamage(40);
		// pet.getMp().decTotalValue(50);

		for (int i = 0; i <= fieldMaps.length - 1; i += 1)
		{
			player.discoverCreature(fieldMaps[i].getCreatures().get(0).getType());
		}

		player.getPA().getLife().incMaxValue(1000);
		player.getPA().getMp().incMaxValue(1000);
		;
		// player.getBA().getPhyAtk().incBaseValue(1000) ;
		// player.getBA().getMagAtk().incBaseValue(1000) ;
		// player.getBA().getPhyDef().incBaseValue(1000) ;
		// player.getBA().getMagDef().incBaseValue(1000) ;
		// player.getBA().getAgi().incBaseValue(1000) ;
		player.getBA().getDex().incBaseValue(1000);
		player.getPA().getLife().setToMaximum();

		// player.getBA().getStun().incAtkChance(1) ;
		player.getBA().getStun().incDuration(1);

		// player.getBA().getBlock().incAtkChance(1) ;
		player.getBA().getBlock().incDuration(1);

		// player.getBA().getBlood().incAtkChance(1) ;
		player.getBA().getBlood().incAtkBonus(8);
		player.getBA().getBlood().incDefBonus(1);
		player.getBA().getBlood().incDuration(1);

		// player.getBA().getPoison().incAtkChance(1) ;
		player.getBA().getPoison().incAtkBonus(1);
		player.getBA().getPoison().incDefBonus(1);
		player.getBA().getPoison().incDuration(1);

		// player.getBA().getSilence().incAtkChance(1) ;
		player.getBA().getSilence().incDuration(1);

		player.takeDamage(500);
		player.getBag().addGold(30000);
		for (Spell spell : player.getSpells())
		{
			spell.incLevel(5);
		}
		SpellsBar.updateSpells(player.getActiveSpells());

		//
		for (Item item : Potion.getAll())
		{
			player.getBag().add(item, 10);
		}
		for (Item item : Alchemy.getAll())
		{
			player.getBag().add(item, 20);
		}
		for (Item item : Forge.getAll())
		{
			player.getBag().add(item, 3);
		}
		for (Item item : PetItem.getAll())
		{
			player.getBag().add(item, 2);
		}
		for (Item item : Food.getAll())
		{
			player.getBag().add(item, 10);
		}
		for (Item item : Arrow.getAll())
		{
			player.getBag().add(item, 20);
		}
		// for (Item item : Equip.getAll()) { player.getBag().add(item, 20) ;}
		for (Item item : GeneralItem.getAll())
		{
			player.getBag().add(item, 2);
		}
		for (Item item : Fab.getAll())
		{
			player.getBag().add(item, 10);
		}
		for (Item item : QuestItem.getAll())
		{
			player.getBag().add(item, 10);
		}
		// player.getElem()[4] = Elements.water ;
		//
		player.getBag().add(Equip.getAll()[0], 20);
		player.getBag().add(Equip.getAll()[1], 20);
		player.getBag().add(Equip.getAll()[2], 20);
		player.getBag().add(Equip.getAll()[100], 20);
		player.getBag().add(Equip.getAll()[102], 20);
		player.getBag().add(Equip.getAll()[110], 20);
		player.getBag().add(Equip.getAll()[111], 20);
		player.getBag().add(Equip.getAll()[112], 20);
		player.getBag().add(Equip.getAll()[115], 20);
		player.getBag().add(Equip.getAll()[116], 20);
		player.getBag().add(Equip.getAll()[117], 20);
		player.getBag().add(Equip.getAll()[121], 20);
		player.getBag().add(Equip.getAll()[122], 20);
		player.getBag().add(Equip.getAll()[123], 20);
		for (QuestSkills skill : QuestSkills.values())
		{
			player.getQuestSkills().replace(skill, true);
		}
		Quest.all.forEach(quest -> player.addQuest(quest));

		// for (int i = 0 ; i <= 50 - 1 ; i += 1)
		// {
		// player.getExp().incCurrentValue(player.getExp().getMaxValue()) ;
		// player.levelUp(null) ; // Game.getAnimations()[4]
		// }

		// for (int i = 0 ; i <= 30000 - 1 ; i += 1)
		// {
		// player.train(new AtkResults(AtkTypes.physical, AttackEffects.hit, 0)) ;
		// player.train(new AtkResults(AtkTypes.magical, AttackEffects.hit, 0)) ;
		// player.train(new AtkResults(AtkTypes.defense, AttackEffects.hit, 0)) ;
		// }
		////
		// for (int i = 0 ; i <= 3 - 1 ; i += 1)
		// {
		// ((Equip) player.getBag().getMenuListItems().get(i + 200 *
		// player.getJob())).use(player) ;
		// for (int j = 0 ; j <= 10 - 1 ; j += 1)
		// {
		// player.getEquips()[i].incForgeLevel() ;
		// }
		// }

		// player.getExp().incCurrentValue(5000) ;

	}


	private void activateCounters()
	{
		if (dayTimer.hasFinished())
		{
			dayTimer.restart();
			NPC.renewStocks();
		}
		player.activateSpellCounters();

		if (pet != null)
		{
			pet.activateSpellCounters();
		}

		if (player.getMap().isField())
		{
			FieldMap fm = (FieldMap) player.getMap();
			fm.getCreatures().forEach(Creature::activateSpellCounters);
		}

		for (CityMap city : cityMaps)
		{
			Building bank = city.getBank();

			if (!bank.hasNPCs())
			{
				continue;
			}

			BankWindow bankWindow = (BankWindow) bank.getNPCs().get(0).getWindow();
			if (bankWindow.hasInvestment() & bankWindow.investmentIsComplete())
			{
				bankWindow.completeInvestment();
			}
		}

		player.activateCounters();

		if (pet != null)
		{
			if (0 < pet.getLife().getCurrentValue())
			{
				pet.activateCounters();
			}
		}

		if (!player.getMap().isField())
		{
			return;
		}

		FieldMap fm = (FieldMap) player.getMap();
		fm.getCreatures().forEach(Creature::activateCounters);
		fm.activateCollectiblesCounter();

	}

	private void creaturesAct(double dt)
	{
		List<Creature> creaturesInMap = ((FieldMap) player.getMap()).getCreatures();
		for (Creature creature : creaturesInMap)
		{
			if (creature.getBattleActionCounter().hasFinished() && AtkTypes.defense.equals(creature.getCurrentAtkType()))
			{
				creature.deactivateDef() ;
				creature.setCurrentAtkType(null);
			}
			creature.takeBloodAndPoisonDamage();
			creature.act(player.getPosAsDouble(), player.getMap(), dt);
		}
	}

	private void petActs(double dt)
	{
		if (!pet.isAlive())
		{
			return;
		}

		pet.takeBloodAndPoisonDamage();
		pet.updateCombo();
		pet.think(player.isFighting(), player.getPosAsDouble());
		pet.act(player, dt);
	}

	private void playerActs(double dt)
	{
		player.takeBloodAndPoisonDamage();
		player.updateBloodAndPoisonStatistics();
		if (player.canAct() && player.hasActed())
		{
			player.act(pet, GamePanel.getMousePos());
		}

		player.doCurrentAction();
		player.applyAdjacentGroundEffect();
		player.finishStatus();

		if (player.isMoving() && !player.isCollecting())
		{
			player.move(pet, dt);
		}
		player.updateCombo() ;
	}

	private void run(double dt)
	{
		if (Math.pow(10, 1) <= dt) { return ;}

		screen.updateSky(dt);
		activateCounters();
		
		// TODO mover as verificações de batalha pra cá e unir às gerais
		if (player.getBattleActionCounter().hasFinished() & player.getCurrentAtkType() != null)
		{
			if (player.getCurrentAtkType().equals(AtkTypes.defense))
			{
				player.deactivateDef() ;
				player.setCurrentAtkType(null);
			}
		}

		checkKonamiCode(player.getCombo());
		if (konamiCodeIsActive)
		{
			applyKonamiEffect(dt);
		}

		if (player.getMap().isField())
		{
			creaturesAct(dt);
			shouldRepaint = true;
		}

		if (pet != null && pet.isAlive())
		{
			if (pet.getBattleActionCounter().hasFinished() && AtkTypes.defense.equals(pet.getCurrentAtkType()))
			{
				pet.deactivateDef() ;
			}
			petActs(dt);
		}

		playerActs(dt);

		player.checkMeet(GamePanel.getMousePos());

		if (player.isFighting() && player.getOpponent() != null)
		{
			if (Battle.isOver(player, pet, player.getOpponent()))
			{
				Battle.finishBattle(player, pet, player.getOpponent());
			}
		}

		if (player.getMap().isField())
		{
			player.setClosestCreature(player.closestCreatureInRange());
		}

		if (player.shouldLevelUP())
		{
			player.levelUp();
		}

		if (pet != null)
		{
			if (pet.shouldLevelUP())
			{
				pet.levelUp();
			}
		}

		if (player.isInContactWithNPC() && player.getNPCInContact().isInteracting())
		{
			player.getNPCInContact().act(player, pet, GamePanel.getMousePos()) ;
		}

		Projectiles.updateAll(player, pet);

		if (player.getJob() == 3)
		{
			player.useAutoSpell(true, player.getSpells().get(12));
		}

		
		if (pauseWindow.isOpen())
		{
			pauseWindow.navigate(player.getCurrentAction()) ;
		}

		player.resetAction();

		SpriteAnimation.updateAll();

	}

	private void draw()
	{
		if (!player.getMap().getContinent().equals(Continents.cave))
		{
			screen.displaySky();
		}
		player.getMap().display();
		player.getMap().displayGroundTypes();
		Draw.mapElements(player.getHitbox(), player.getPos(), player.getMap());

		List<Drawable> drawables = new ArrayList<>();

		if (player.getMap().isField())
		{
			List<Creature> creaturesInMap = ((FieldMap) player.getMap()).getCreatures();
			for (Creature creature : creaturesInMap)
			{
				drawables.add(creature);
			}
		}

		if (pet != null)
		{
			drawables.add(pet);
		}

		drawables.add(player);
		if (player.getMap().getBuildings() != null)
		{
			player.getMap().getBuildings().forEach(building -> drawables.add(building));
		}
		player.getMap().getMapElem().forEach(mapElem -> drawables.add(mapElem));
		drawables.sort(Comparator.comparingInt(d -> d.getPos().y));
		drawables.forEach(Drawable::display);
		if (player.getMap() instanceof CityMap)
		{
			((CityMap) player.getMap()).getForge().display() ;
			((CityMap) player.getMap()).getSign().display(player.getPos()) ;
		}

		if (player.isInContactWithNPC() && player.getNPCInContact().isInteracting())
		{
			player.getNPCInContact().displaySpeech();
			player.getNPCInContact().displayOptions();
		}

		player.showWindows(pet, GamePanel.getMousePos());

		if (player.getBag().getItemFetched() != null)
		{
			GamePanel.DP.drawImage(player.getBag().getItemFetched().getImage(), GamePanel.getMousePos(), Align.center);
		}

		Animation.playAll();
		SpriteAnimation.displayAll(GamePanel.DP);

		SideBar.display(player, pet, GamePanel.getMousePos());

		if (pauseWindow.isOpen())
		{
			pauseWindow.display(GamePanel.getMousePos());
		}

		if (debugMode)
		{
			screen.displayBorders();
		}

	}

	protected void update()
	{
		GameButton.updateMouseCursor(GamePanel.getMousePos());
		GameButton.updateSelected(GamePanel.getMousePos());
		GameButton.actWhenClicked(GamePanel.getMousePos(), player.getCurrentAction());
		switch (state)
		{
		case opening:
			Opening.run(player, GamePanel.getMousePos());
			shouldRepaint = true;
			return;

		case loading:
			LoadingGame.load(player, GamePanel.getMousePos(), gameLanguage);
			
			if (Game.testMode && LoadingGame.isOver())
			{
				Game.initializeTestMode();
			}
			shouldRepaint = true;
			return;

		case simulation:
			EvolutionSimulation.run(GamePanel.getMousePos());
			return;

		case running:
			run((System.nanoTime() - dt) / Math.pow(10, 9));
			dt = System.nanoTime();
			draw();
			return;

		case paused:
			return;
		default:
			return;
		}
	}

	private String keypadNumberValue(int keyCode)
	{
		return String.valueOf(keyCode - 96);
	}

	private void switchPauseRunning()
	{
		if (state.equals(GameStates.paused))
		{
			GameFrame.resumeGame();
			GameTimer.resumeAll();
		} else
		{
			GameFrame.pauseGame();
			GameTimer.stopAll();
		}
	}

	protected void keyPressedAction(KeyEvent event)
	{
		int keyCode = event.getKeyCode();
		if (KeyEvent.VK_NUMPAD0 <= keyCode & keyCode <= KeyEvent.VK_NUMPAD9)
		{
			player.setCurrentAction(keypadNumberValue(keyCode));
			return;
		}

		if (keyCode == KeyEvent.VK_PAUSE)
		{
			switchPauseRunning();
		}

		if (keyCode == KeyEvent.VK_F12)
		{
			GameFrame.getMe().resizeWindow();
			screen.updateScale();
		}

		if (keyCode == KeyEvent.VK_ESCAPE && !player.hasWindowOpen())
		{
			pauseWindow.updateButtons() ;
			pauseWindow.switchOpenClose() ;
		}

		if (arrowKeys.contains(KeyEvent.getKeyText(keyCode)))
		{
			player.addKeyPressed(KeyEvent.getKeyText(keyCode));
			if (player.isMoving())
			{
				player.updateDirection(KeyEvent.getKeyText(keyCode));
			}
		}

		player.setCurrentAction(KeyEvent.getKeyText(keyCode));
	}

	protected void keyReleasedAction(KeyEvent event)
	{
		int keyCode = event.getKeyCode();
		if (arrowKeys.contains(KeyEvent.getKeyText(keyCode)))
		{
			player.removeKeyPressed(KeyEvent.getKeyText(keyCode));
		}
	}

	protected void mouseClickedAction(MouseEvent evt)
	{
	}

	protected void mousePressedAction(MouseEvent evt)
	{
		if (!GameStates.running.equals(state))
		{
			return;
		}

		if (player.getBag().isOpen())
		{
			player.getBag().setItemFetched(player.getBag().itemHovered(GamePanel.getMousePos()));
		}
	}

	protected void mouseReleaseAction(MouseEvent evt)
	{
		if (evt.getButton() == 1)
		{
			player.setCurrentAction("LeftClick");
		}
		if (evt.getButton() == 3)
		{
			player.setCurrentAction("MouseRightClick");
			player.setPos(new Point2D.Double(GamePanel.getMousePos().x, GamePanel.getMousePos().y));
			// Log.attributes(player) ;
			if (pet != null)
			{
				pet.setPos(player.getPosAsDouble());
			}
		}

		if (!GameStates.running.equals(state))
		{
			return;
		}

		int hotKeySlotHovered = HotKeysBar.slotHovered(GamePanel.getMousePos());

		if (-1 < hotKeySlotHovered)
		{
			player.getHotItems().set(hotKeySlotHovered, player.getBag().getItemFetched());
		}

		player.getBag().setItemFetched(null);
	}

	protected void mouseWheelAction(MouseWheelEvent evt)
	{
		if (evt.getWheelRotation() < 0)
		{
			player.setCurrentAction("MouseWheelUp");
		} else
		{
			player.setCurrentAction("MouseWheelDown");
		}
	}

}